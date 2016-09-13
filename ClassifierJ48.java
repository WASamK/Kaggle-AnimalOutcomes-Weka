import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instances;

public class ClassifierJ48 extends Clasifier{ 
	String[] options = null;
	
	public ClassifierJ48() throws Exception{
		model=new J48();
		options = new String[1];
		options[0] = "-U"; 
		model.setOptions(options);
	}
	
}
