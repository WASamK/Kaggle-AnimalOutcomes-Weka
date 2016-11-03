import java.awt.List;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.learning.RunMode;
import gate.FeatureMap;
import gate.Gate;
import gate.SimpleAnnotationSet;
import gate.util.GateException;
import gate.creole.ResourceInstantiationException;
import gate.util.persistence.PersistenceManager;

public class Classifier {
	
	public void saveApp(){}

	public static void main(String[] args) throws GateException, IOException {
		
		PreProcessor preProcessor=new PreProcessor();
		
		int trainSize=12;
		int testSize=10;

		String classAttrPath="TrainAnn.txt";
		String trainData="Train.txt";
		String testData="Test.txt";
		String preProcessedTrain="Train.xml";
		String preProcessedTest="Test.xml";
		
		preProcessor.extractAnn(classAttrPath,trainSize);
		preProcessor.preprocessTrain(trainData,trainSize,preProcessedTrain);
		preProcessor.preprocessTest(testData, testSize,preProcessedTest);*/
		
		ClassEngine engine=new ClassEngine();
		engine.createApp("Trained.xml");
		engine.classify(preProcessedTest);
	}

}
