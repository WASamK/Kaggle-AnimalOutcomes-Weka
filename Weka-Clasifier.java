import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instances;

public class Clasifier {
	static Classifier model;
	static Evaluation evaluation=null;
	 Instances labeled = null;
	
	
	public static Evaluation buildModel(Instances trainingSet, Instances testingSet) throws Exception {
		evaluation = new Evaluation(trainingSet);
 
		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);
 
		return evaluation;
	}

	public Classifier getModel(){
		return model;
	}
	
	public void classify(Instances testingSet) throws Exception{ 
		 labeled = new Instances(testingSet);
		 String res="";
		
		for (int i = 0; i < testingSet.numInstances(); i++) {
		   double clsLabel = model.classifyInstance(testingSet.instance(i));
		   labeled.instance(i).setClassValue(clsLabel);
		 }	
	}
	
	public static double calculateAccuracy(FastVector predictions) {
		double correct = 0;
 
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
 
		return 100 * correct / predictions.size();
	}


	public void dumpRes() throws IOException{
		String FILE_HEADER = "ID,Adoption,Died,Euthanasia,Return_to_owner,Transfer";
		String NEW_LINE_SEPARATOR = "\n";
		String COMMA_DELIMITER = ",";
		
		//Create a new list of student objects
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("output.csv");
		            //Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			//Write a new student object list to the CSV file
			for (int i = 0; i < labeled.numInstances(); i++) {
				fileWriter.append(i+1+"");
				fileWriter.append(COMMA_DELIMITER);
				
				if(labeled.instance(i).stringValue(0).equals("Adoption")){
					fileWriter.append(1+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
				}

				if(labeled.instance(i).stringValue(0).equals("Died")){
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(1+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
				}

				if(labeled.instance(i).stringValue(0).equals("Euthanasia")){
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(1+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
				}

				if(labeled.instance(i).stringValue(0).equals("Return_to_owner")){
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(1+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
				}

				if(labeled.instance(i).stringValue(0).equals("Transfer")){
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(0+"");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(1+"");
				}
				fileWriter.append(NEW_LINE_SEPARATOR);
				}
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
			
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
				}
			
		}

		
	}

}
