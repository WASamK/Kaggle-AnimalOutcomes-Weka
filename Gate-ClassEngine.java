import java.awt.List;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;


import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.learning.RunMode;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

public class ClassEngine {
	private static List annotTypesToWrite = null;
	private static String encoding = null;
	private static CorpusController controller=null;
	private static Corpus corpus =null;
	File trainFile=null;
	File testFile=null;
    Document resDoc=null;
	
	public void createApp(String trainDataPath) throws GateException, IOException{
		Gate.init();
		File configFile = new File ("config.xml"); 

		RunMode tMode = RunMode.TRAINING; 
		FeatureMap pfm = Factory . newFeatureMap ();

		Corpus corp = Factory.newCorpus("Corp");
		
		File trainFile = new File(trainDataPath);
		
	    Document doc=null;
	    if(trainFile!=null){
		  		System.out.print("Processing document " + trainFile + "...\n");
		  		doc=Factory.newDocument(trainFile.toURL(), encoding);
		  		corp.add(doc); 
	    }

		pfm .put("corpus", corp );
		gate.creole.SerialAnalyserController pipeline = (gate.creole.SerialAnalyserController)
				gate.Factory.createResource ("gate.creole.SerialAnalyserController", pfm);

		FeatureMap fm = Factory . newFeatureMap ();
		fm. put("configFileURL", configFile.toURI ().toURL());

		fm. put("learningMode", tMode);
		gate.learning.LearningAPIMain learner = null;

		learner = (gate.learning.LearningAPIMain)gate.Factory.createResource ("gate.learning.LearningAPIMain", fm);
		pipeline.add(learner);

		pipeline.execute();

		File gappFile=new File("Classifier.gapp");
		gate.util.persistence.PersistenceManager.saveObjectToFile(pipeline, gappFile);

		Factory.deleteResource(pipeline);
		pipeline=null;
	}
	
	public void classify(String testDataPath) throws GateException, IOException{
		Gate.init();

		controller=(CorpusController) PersistenceManager.loadObjectFromFile(new File("Classifier.gapp"));
		corpus =Factory.newCorpus("c");

		controller.setCorpus(corpus);		
		
		testFile = new File(testDataPath);
		
	    resDoc=null;
	    if(testFile!=null){
		  		System.out.print("Processing document " + testFile + "...\n");
		  		resDoc=Factory.newDocument(testFile.toURL(), encoding);
		  		corpus.add(resDoc); 
	    }
	    

		controller.execute();
		corpus.clear();
	
	}
	
	public void dumpResults() throws IOException{
		String docXMLString = null;
		
		if(annotTypesToWrite == null) 
			docXMLString = resDoc.toXml();
		    		  

		String outputDir="";
		gate.AnnotationSet annotationSet =resDoc.getAnnotations("ASet");
		
		if (annotationSet.size() == 0) 
			System.out.println("pipeline has failed to annotate.");
		
		else{
			
			Iterator itr=annotationSet.iterator();
			String res="";
			
			while(itr.hasNext()) {
				gate.Annotation ann = (gate.Annotation) itr.next();
				FeatureMap lookupFeatures = ann.getFeatures();
				gate.FeatureMap features = Factory.newFeatureMap();
				features.putAll(lookupFeatures);
				
				int j=0;
				int sentCnt=1;
				
				if(features.toString().contains("OutcomeType"))
					res+=("\n"+sentCnt +" - "+features.toString());
			}
		    	
			String outputFileName =testFile.getName() + ".out.xml";
		  	File outputFile = new File(testFile.getParentFile()+"/"+outputDir, outputFileName);
		  	FileOutputStream fos = new FileOutputStream(outputFile);
		  	BufferedOutputStream bos = new BufferedOutputStream(fos);
		  	OutputStreamWriter out;
		  			
		  	if(encoding == null) 
		  		out = new OutputStreamWriter(bos);
		  	else
		  		out = new OutputStreamWriter(bos, encoding);
		  	
		  	out.write(docXMLString);
		  	out.close();
		  	Factory.deleteResource(resDoc);
			System.out.println(res);
		  	
		}
	}
	
}
