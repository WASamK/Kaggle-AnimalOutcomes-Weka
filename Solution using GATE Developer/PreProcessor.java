import java.awt.List;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.SimpleAnnotationSet;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

public class PreProcessor {
	private static String encoding = null;
	private static String[] annType=null;
	private static List annotTypesToWrite = null;
	
	/*A method to get the annotations of he sentences to an array*/
	void extractAnn(String fileName, int trainSize) throws IOException{
		int sentcnt=0;
		annType=new String[trainSize];
		
		/*Read the file. The first word of a line is the class label/annotation*/
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
	    	    String[] words = line.split(",");
	    	    annType[sentcnt++]=words[0];

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		        
		        if(line!=null){
		    	   words = line.split(",");
		    	   annType[sentcnt++]=words[0];
		        }
		    }
		    String everything = sb.toString();
		} finally {
		    br.close();
		}
	}
	
	/*Pre process the test data*/
	void preprocessTest(String path,int testSetSize,String preProcessedTest) throws GateException, IOException{
		
		Gate.init();

		CorpusController annie = (CorpusController)
				PersistenceManager.loadObjectFromFile (new File ("ANNIE.gapp"));
		Corpus corpus = Factory.newCorpus("BatchProcessAppCorpus");
		annie.setCorpus(corpus);

		File file = new File(path);

		      if (file.isFile()) {
		    	  File docFile = new File(path);
		    	  System.out.print("\nProcessing document " + docFile + "...");
		    	 
		    	  Document doc = Factory.newDocument(docFile.toURL(), encoding);
		    	  corpus.add(doc);
		    	  /*Run annie for initial annotations*/
		    	  annie.execute();
				
		    	  String docXMLString = null;
		    	  long[] startOffset=new long[testSetSize+1];
		    	  long[] endOffset=new long[testSetSize+1];; 

		    	  gate.AnnotationSet annotationSt = doc.getAnnotations("ASet");
		    	  
		    	  int sentenceCnt=0;
		    	  if (annotationSt.size() == 0) 
		    		  System.out.println("pipeline has failed to annotate.");
		    	  else{
		    		  Iterator itr=annotationSt.iterator();
		    		  
		    		  while(itr.hasNext()) {
		    			  gate.Annotation ann = (gate.Annotation) itr.next();
		    			  String s=(String) ann.getType();
		    			  if(s.equals("Sentence")){
		    				  startOffset[sentenceCnt] =ann.getStartNode().getOffset();
		    			      endOffset[sentenceCnt++]= ann.getEndNode().getOffset(); 
		    			  }
		    			      
		    		}

                          for(int i=0; i<sentenceCnt;i++)
	    			      annotationSt.add(startOffset[i],endOffset[i],"OutcomeType", gate.Utils.featureMap());
			  }
		    	 
		    	  if(annotTypesToWrite != null) {
		    		  Set annotationsToWrite = new HashSet();
		    		  AnnotationSet defaultAnnots = doc.getAnnotations();
		    		  Iterator annotTypesIt = ((SimpleAnnotationSet) annotTypesToWrite).iterator();
		    		  
		    		  while(annotTypesIt.hasNext()){
		    			  AnnotationSet annotsOfThisType=defaultAnnots.get((String)annotTypesIt.next());
		    			  if(annotsOfThisType != null)
		    				  annotationsToWrite.addAll(annotsOfThisType);
		    			  }
		    		  }
		    	  else 
		    		  docXMLString = doc.toXml();
		    	  
		    	  gate.AnnotationSet annotationSet = doc.getAnnotations("ASet");
		    	  if (annotationSet.size() == 0) 
		    		  System.out.println("pipeline has failed to annotate.");
		    	  
		    	  Factory.deleteResource(doc);
		    	  String outputFileName = preProcessedTest+ ".xml";
		    	  File outputFile = new File(outputFileName);
		    	  FileOutputStream fos = new FileOutputStream(outputFile);
		    	  BufferedOutputStream bos = new BufferedOutputStream(fos);
		    	  OutputStreamWriter out;
		    	  if(encoding == null)
		    		  out = new OutputStreamWriter(bos);
		    	  else
		    		  out = new OutputStreamWriter(bos, encoding);
		    	  
		    	  out.write(docXMLString);
		    	  out.close();
		    	  }
		
	}
	/*A method to preprocess the train dataset*/
	void preprocessTrain(String path,int trainSetSize,String preProcessedTrain) throws GateException, IOException{
		
		Gate.init();

		CorpusController annie = (CorpusController)
				PersistenceManager.loadObjectFromFile (new File ("ANNIE.gapp"));
		Corpus corpus = Factory.newCorpus("BatchProcessAppCorpus");
		annie.setCorpus(corpus);

		File file = new File(path);

		      if (file.isFile()) {
		    	  File docFile = new File(path);
		    	  System.out.print("\nProcessing document " + docFile + "...");
		    	 
		    	  Document doc = Factory.newDocument(docFile.toURL(), encoding);
		    	  corpus.add(doc);
		    	  
		    	  annie.execute();
				
		    	  String docXMLString = null;
				
		    	  
		    	  long[] startOffset=new long[trainSetSize+1];
		    	  long[] endOffset=new long[trainSetSize+1];; 

		    	  gate.AnnotationSet annotationSt = doc.getAnnotations("ASet");
		    	  
		    	  int sentenceCnt=0;
		    	  

		    	  if (annotationSt.size() == 0) 
		    		  System.out.println("pipeline has failed to annotate.");
		    	  
		    	  else{
		    		  Iterator itr=annotationSt.iterator();
		    		  
		    		  while(itr.hasNext()) {
		    			  gate.Annotation ann = (gate.Annotation) itr.next();
		    			  String s=(String) ann.getType();
		    			  if(s.equals("Sentence")){
		    				  startOffset[sentenceCnt] =ann.getStartNode().getOffset();
		    			      endOffset[sentenceCnt++]= ann.getEndNode().getOffset(); 
		    			  }
		    			      
		    		  }


                      for(int i=0; i<sentenceCnt;i++)
	    			      annotationSt.add(startOffset[i],endOffset[i],"OutcomeType", gate.Utils.featureMap());
                      
                      sentenceCnt=0;
		    		  Iterator itr1=annotationSt.iterator();
		    		  
		    		  while(itr1.hasNext()) {
		    			  gate.Annotation ann = (gate.Annotation) itr1.next();
		    			  String s=(String) ann.getType();
		    			  if(s.equals("OutcomeType"))
		    			      ann.getFeatures().put("OutcomeType", annType[sentenceCnt++]);
		    			      
		    		  }
		    		  
		    	  }
		    	 
		    	  if(annotTypesToWrite != null) {
		    		  Set annotationsToWrite = new HashSet();
		    		  AnnotationSet defaultAnnots = doc.getAnnotations();
		    		  Iterator annotTypesIt = ((SimpleAnnotationSet) annotTypesToWrite).iterator();
		    		  
		    		  while(annotTypesIt.hasNext()){
		    			  AnnotationSet annotsOfThisType=defaultAnnots.get((String)annotTypesIt.next());
		    			  if(annotsOfThisType != null)
		    				  annotationsToWrite.addAll(annotsOfThisType);
		    			  }
		    		  }
		    	  else 
		    		  docXMLString = doc.toXml();
		    	  
		    	  gate.AnnotationSet annotationSet = doc.getAnnotations("ASet");
		    	  if (annotationSet.size() == 0) 
		    		  System.out.println("pipeline has failed to annotate.");
		    	  
		    	  Factory.deleteResource(doc);
		    	  String outputFileName = preProcessedTrain+ ".xml";
		    	  File outputFile = new File(outputFileName);
		    	  FileOutputStream fos = new FileOutputStream(outputFile);
		    	  BufferedOutputStream bos = new BufferedOutputStream(fos);
		    	  OutputStreamWriter out;
		    	  if(encoding == null)
		    		  out = new OutputStreamWriter(bos);
		    	  else
		    		  out = new OutputStreamWriter(bos, encoding);
		    	  
		    	  out.write(docXMLString);
		    	  out.close();
		    	  }
	}
	

}
