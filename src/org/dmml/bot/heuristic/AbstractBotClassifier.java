package org.dmml.bot.heuristic;

import java.io.BufferedReader;
import java.util.HashMap;

import constants.Utility;
import weka.classifiers.Classifier;
import weka.core.Instance;

public abstract class AbstractBotClassifier extends  Classifier {


	/**
	 * @param instance: weka instance
	 * @return double[], with each entry represents the probability of assigning this instance to corresponding label
	 * an array containing the estimated membership probabilities 
	 * of the test instance in each class or the numeric prediction( normalized to sum to one )
	 */
	public abstract double[] distributionForInstance(Instance instance);
//	
//	num of features: instance.numAttributes()-2; last one is class index
//	instance.value(attIndex)
	//[0] [1] [2]
	// a s d
	
	/**
	 * 
	 * @param inputfilename: the data file
	 * @param labeldata: the label data file
	 * @return the filename of the output data file
	 * @throws Exception
	 */
	public abstract String processData(String inputfilename, String labeldata) throws Exception;//return the outputfilename
	
	public abstract double classifyInstance(Instance instance);
	
}
