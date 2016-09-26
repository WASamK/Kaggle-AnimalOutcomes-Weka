import weka.classifiers.bayes.NaiveBayesUpdateable;

public class NaiveBayesClassifier extends Clasifier{

	public NaiveBayesClassifier(){
		model= new NaiveBayesUpdateable();
	}
}
