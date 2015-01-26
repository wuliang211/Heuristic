package org.dmml.bot.heuristic;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import constants.ConfidenceIntervalApp;
import constants.Utility;
import weka.core.Instance;
import weka.core.Instances;

public class BotClassifier2 extends AbstractBotClassifier {

	public double threshold = 0.0;
	public double min = -107;
	public double max = 20;
	
	public void setT(double nval)
	{
		threshold = nval;
	}
	
	@Override
	public void buildClassifier(Instances arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	public double classifyInstance(Instance instance)
	{
		
		
		double result = 0.0;
		
		double val = instance.value(0);
		if(val<threshold)
			result = 1.0;
		return result;
	}
	public double[] distributionForInstance(Instance instance)
	{
		double[] ret = new double[3];
		double val = instance.value(0);
		if (val == Double.NaN) 
		{
		    val=max;
		}

		ret[2] = (max-val)/(max-min);
		double res = (1-ret[2])/2.0;
		ret[0] = ret[1] = res;
		
		return ret;
	}

	@Override
	public String processData(String inputfilename, String labeldata)
			throws Exception {
		BufferedReader brd = Utility.getReader(inputfilename);
		String line = "";
		HashMap<Integer,Integer>answermap =getAnswer(labeldata);
		HashMap<String,Double>idfmap = getIdf(800000.0, inputfilename, labeldata);
		HashMap<Integer,Double>intervalmap = new HashMap<Integer,Double>();
		HashMap<Integer,LinkedList<Double>>userdata = new HashMap<Integer,LinkedList<Double>>();
		while((line=brd.readLine())!=null)
		{
			String[] sts = line.split("\t");
			int ctr=0;
			Integer userid = 0;
			Long timestamp = (long) 0;
			for(String st:sts)
			{
				ctr++;
				if(ctr==1)
				{
					userid = Integer.parseInt(st);
//					Integer label = answermap.get(t);
					if(!userdata.containsKey(userid))
					{
						LinkedList<Double>temp = new LinkedList<Double>();
						userdata.put(userid, temp);
					}
				}
				else if(ctr==2)
				{
					timestamp = Long.parseLong(st);
				}
				else
				{
					double idf = idfmap.get(st);
					userdata.get(userid).addFirst(idf);
				}
			}
		}
		for(Integer uid : userdata.keySet())
		{
			LinkedList<Double> temp = userdata.get(uid);
			double ci = ConfidenceIntervalApp.calc(temp);
			intervalmap.put(uid, ci);
		}
		PrintWriter outf = Utility.getWriter("wekadata2.csv");
		for(Integer uid : intervalmap.keySet())
		{
			Double ci = intervalmap.get(uid);
			Integer label = answermap.get(uid);
			String l = "";
			if(label==0)
				l="aa";//a
			if(label==1)
				l="bb";//b
			if(label==2)
				l="ss";//s
			outf.println(ci.intValue()+","+l);
		}
		return "wekadata2.csv";
	}
	

	public static HashMap<String,Double>getIdf(double documentN, String datafile, String labelfile)throws Exception
	{
		HashMap<String,Double> ret = new HashMap<String,Double>();
		//800000
		double fenzi = documentN;
		
		BufferedReader brd = Utility.getReader(datafile);
		String line = "";
		HashMap<Integer,Integer>answermap =getAnswer(labelfile);
		HashMap<String,Integer>count = new HashMap<String,Integer>();
		while((line=brd.readLine())!=null)
		{
			String[] sts = line.split("\t");
			int ctr=0;
			Integer userid = 0;
			Long timestamp = (long) 0;
			for(String st:sts)
			{
				ctr++;
				if(ctr==1)
				{
					userid = Integer.parseInt(st);
//					Integer label = answermap.get(t);
				}
				else if(ctr==2)
				{
					timestamp = Long.parseLong(st);
				}
				else
				{
					Utility.addMap(count, st, 1);
				}
			}
		}
		for(String key :count.keySet())
		{
			Integer val = count.get(key);
			double idf = Math.log(fenzi/val.doubleValue())/Math.log(2);
			ret.put(key, idf);
		}
		return ret;
		
	}
	public static HashMap<Integer,Integer> getAnswer(String labelfile)throws Exception
	{
		HashMap<Integer,Integer> ret = new HashMap<Integer,Integer>();
		BufferedReader brd = Utility.getReader(labelfile);
		String line = "";
		while((line=brd.readLine())!=null)
		{
			String[]strs = line.split("	");
			Integer id = Integer.parseInt(strs[0]);
			String label  = strs[1];
			Integer l = 0;
			if(label.contains("d"))
			{
				l=1;
			}
			else if(label.contains("s"))
			{
				l=2;
			}
			ret.put(id, l);
		}
		return ret;
	}//a 0; d 1; s 2;
}
