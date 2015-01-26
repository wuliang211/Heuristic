package org.dmml.bot.heuristic;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Random;

import constants.Utility;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.ThresholdVisualizePanel;

public class ROCDrawer {
	 public static void main(String[] args)throws Exception
	 {
		 BotClassifier1 c = new BotClassifier1();
		 String datafile = c.processData("tweetdata3.txt", "libya_label.txt");
		 drawROC(c,datafile);
	 } 
	
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	   public static void drawROC(AbstractBotClassifier classifier, String filename) throws Exception {
		     
			 DataSource source = new DataSource(filename);
			 Instances data = source.getDataSet();
			 data.setClassIndex(data.numAttributes()-1);
			 int size = data.numInstances();
//			 for(int i = 0 ; i < size ; i++)
//			 {
//				 Instance ins = data.instance(i);
//				 System.out.println(ins.classValue());
//				 ins.setClassValue("a");
//				 ins.setClassValue(1.0);
//				 System.out.println(ins.classIndex());
//				 System.out.println(ins.attribute(1).value(0));
				 
//				 int size2 = ins.numAttributes();
//				 for(int j = 0 ; j < size2 ; j++)
//				 {
//					 System.out.println(ins.attribute(j));
//				 }
//			 }
		   // load data
		     // Instances data = new Instances(); error rate: 0.08411141828803922
		     data.setClassIndex(data.numAttributes() - 1);
		     
		     // train classifier
		     Classifier cl = classifier;
		     
		     // Classifier cl = new NaiveBayes();
		     Evaluation eval = new Evaluation(data);
		     eval.crossValidateModel(cl, data, 10, new Random(1));
		     
//		     for(int i = 0 ; i < eval.predictions().size() ; i++)
//		     {
//				 System.out.println(eval.predictions().elementAt(i));
//					
//		     }
		     System.out.println("error rate: "+eval.errorRate());
		     
		     // generate curve
		     ThresholdCurve tc = new ThresholdCurve();
		     int classIndex = 2;
		     Instances result = tc.getCurve(eval.predictions(), classIndex);
		 
		     // plot curve
		     ThresholdVisualizePanel vmc = new ThresholdVisualizePanel();
		     vmc.setROCString("(Area under ROC = " + Utils.doubleToString(tc.getROCArea(result), 4) + ")");
		     vmc.setName(result.relationName());
		     PlotData2D tempd = new PlotData2D(result);
		     tempd.setPlotName(result.relationName());
		     tempd.addInstanceNumberAttribute();
		     // specify which points are connected
		     boolean[] cp = new boolean[result.numInstances()];
		     for (int n = 1; n < cp.length; n++)
		       cp[n] = true;
		     tempd.setConnectPoints(cp);
		     // add plot
		     vmc.addPlot(tempd);
		 
		     // display curve
		     String plotName = vmc.getName();
		     final javax.swing.JFrame jf =
		       new javax.swing.JFrame("Weka Classifier Visualize: "+plotName);
		     jf.setSize(500,400);
		     jf.getContentPane().setLayout(new BorderLayout());
		     jf.getContentPane().add(vmc, BorderLayout.CENTER);
		     jf.addWindowListener(new java.awt.event.WindowAdapter() {
		       public void windowClosing(java.awt.event.WindowEvent e) {
		       jf.dispose();
		       }
		     });
		     jf.setVisible(true);
		   }

	   
	   
	   
}
