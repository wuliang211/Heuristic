import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.codehaus.jackson.map.ObjectMapper;

import constants.ConfidenceIntervalApp;
import constants.Utility;


public class heuristics {
	public static void main(String[]args)throws Exception
	{
//		frequency();//drawSingle2
		original();//drawSingle
	}
	
	public static HashMap<String,Double>getIdf(double documentN)throws Exception
	{
		HashMap<String,Double> ret = new HashMap<String,Double>();
		//800000
		double fenzi = documentN;
		
		BufferedReader brd = Utility.getReader("tweetdata3.txt");
		String line = "";
		HashMap<Integer,Integer>answermap =getAnswer();
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
	public static HashMap<Integer,Integer> getAnswer()throws Exception
	{
		HashMap<Integer,Integer> ret = new HashMap<Integer,Integer>();
		BufferedReader brd = Utility.getReader(".//libya_label.txt");
		String line = "";
		while((line=brd.readLine())!=null)
		{
			String[]strs = line.split("	");
			Integer id = Integer.parseInt(strs[0]);
			String label  = strs[1];
			Integer l = 0;
			if(label.contains("d"))
				l=1;
			else if(label.contains("s"))
				l=2;
			ret.put(id, l);
		}
		return ret;
	}//a 0; d 1; s 2;
	
	
	/*
	 *input:tweetdata3.txt, output: wekadata2.csv
	 * 
	 */
	public static void frequency()throws Exception
	{
		BufferedReader brd = Utility.getReader("tweetdata3.txt");
		String line = "";
		HashMap<Integer,HashMap<Long,Integer>>userdata = new HashMap<Integer,HashMap<Long,Integer>>();
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
						HashMap<Long,Integer> temp = new HashMap<Long,Integer>();
						userdata.put(userid, temp);
					}
				}
				else if(ctr==2)
				{
					timestamp = Long.parseLong(st);
					Long day=timestamp/1000/60/60/24;
					HashMap<Long,Integer>temp = userdata.get(userid);
					Utility.addMapLong(temp, day, 1);
				}
			}
		}
		

		HashMap<Integer,Integer>answermap =getAnswer();
		PrintWriter outf = Utility.getWriter("wekadata2.csv");
		for(Integer uid : userdata.keySet())
		{
			HashMap<Long,Integer> temp = userdata.get(uid);
			int max = 0;
			for(Long k : temp.keySet())
			{
				int v = temp.get(k);
				if(v>max)
					max=v;
			}
			int label = answermap.get(uid);
			String l = "";
			if(label==0)
				l="a";
			if(label==1)
				l="b";
			if(label==2)
				l="s";
			System.out.println(max);
			outf.println(max+","+l);
		}
	}
	public static void original()throws Exception
	{
		BufferedReader brd = Utility.getReader("tweetdata3.txt");
		String line = "";
		HashMap<Integer,Integer>answermap =getAnswer();
		HashMap<String,Double>idfmap = getIdf(800000.0);
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
		PrintWriter outf = Utility.getWriter("wekadata.csv");
		for(Integer uid : intervalmap.keySet())
		{
			Double ci = intervalmap.get(uid);
			Integer label = answermap.get(uid);
			String l = "";
			if(label==0)
				l="a";
			if(label==1)
				l="b";
			if(label==2)
				l="s";
			outf.println(ci.intValue()+","+l);
		}
	}
}
