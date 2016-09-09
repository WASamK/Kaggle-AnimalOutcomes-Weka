import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.trees.J48;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.neural.*;
import weka.core.FastVector;
import weka.core.Instances;
 
public class Predictor {
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
 
	
 
	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];
 
		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}
 
		return split;
	}
 
	public static void main(String[] args) throws Exception {
		BufferedReader trainData = readDataFile("train.arff");
		BufferedReader testData = readDataFile("test.arff");
 
		Instances insTrain = new Instances(trainData);
		Instances insTest = new Instances(testData);
		insTrain.setClassIndex(insTrain.numAttributes() - 4);
		insTest.setClassIndex(insTest.numAttributes() - 4);
 
		// Do 10-split cross validation
		Instances[][] split = crossValidationSplit(insTrain, 10);
 
		// Separate split into training and testing arrays
		Instances[] trainingSplits = split[0];
		Instances[] testingSplits = split[1];
 
		//Clasifier c=new ClassifierJ48();
		Clasifier c=new  MultLyerPerceptron();
 
		// Collect every group of predictions for current model in a FastVector
		FastVector predictions = new FastVector();
 
		// For each training-testing split pair, train and test the classifier
		for (int i = 0; i < trainingSplits.length; i++) {
			Evaluation validation = c.buildModel( trainingSplits[i], testingSplits[i]);
			predictions.appendElements(validation.predictions());
		}
		
		// Calculate overall accuracy of current classifier on all splits
		double accuracy = c.calculateAccuracy(predictions);	
		System.out.println("Accuracy of " + c.getModel().getClass().getSimpleName() + ": "+ String.format("%.2f%%", accuracy));
		
		c.classify(insTest);
		c.dumpRes();
		
	}
	
}
