package constants;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	
	
    /**
     * 获取删除代价
     *
     * @return
     */
    public static int getDeletionCost() {
            return 1;
    }

    /**
     * 获取插入代价
     *
     * @return
     */
    public static int getInsertionCost() {
            return 1;
    }

    /**
     * 获取替换代价
     *
     * @return
     */
    public static int getSubstitutionCost(char a, char b) {
            return (a == b) ? 0 : 1;
    }

    public static int editdistance(String S, String T) {
            int[][] D = null;
            if (S == null)
                    S = "";
            if (T == null)
                    T = "";

            char[] a = S.toCharArray();
            char[] b = T.toCharArray();

            int n = a.length; // 字符串S的长度
            int m = b.length; // 字符串T的长度

            if (a.length == 0) {
                    return b.length;
            } else if (b.length == 0) {
                    return a.length;
            }

            D = new int[a.length + 1][b.length + 1];
           
            /** 初始化D[i][0] */
            for (int i = 1; i <= n; i++) {
                    D[i][0] = D[i - 1][0] + getDeletionCost();
            }

            /** 初始化D[0][j] */
            for (int j = 1; j <= m; j++) {
                    D[0][j] = D[0][j - 1] + getInsertionCost();
            }

            for (int i = 1; i <= n; i++) {
                    for (int j = 1; j <= m; j++) {
                            D[i][j] = Math.min(Math.min(D[i - 1][j] + getDeletionCost(),
                                    D[i][j - 1] + getInsertionCost() ), D[i - 1][j - 1]
                                                            + getSubstitutionCost(a[i - 1], b[j - 1]));
                    }
            }

            return D[n][m];
    }

    /**
     * 应与getEditDistance(S, T)等同
     * @param s
     * @param t
     * @return
     */
    public static int getLevenshteinDistance(String s, String t) {
            if (s == null || t == null) {
                    throw new IllegalArgumentException("Strings must not be null");
            }
            int d[][]; // matrix
            int n; // length of s
            int m; // length of t
            int i; // iterates through s
            int j; // iterates through t
            char s_i; // ith character of s
            char t_j; // jth character of t
            int cost; // cost

            // Step 1
            n = s.length();
            m = t.length();
            if (n == 0) {
                    return m;
            }
            if (m == 0) {
                    return n;
            }
            d = new int[n + 1][m + 1];

            // Step 2
            for (i = 0; i <= n; i++) {
                    d[i][0] = i;
            }
            for (j = 0; j <= m; j++) {
                    d[0][j] = j;
            }

            // Step 3
            for (i = 1; i <= n; i++) {
                    s_i = s.charAt(i - 1);

                    // Step 4
                    for (j = 1; j <= m; j++) {
                            t_j = t.charAt(j - 1);

                            // Step 5
                            if (s_i == t_j) {
                                    cost = 0;
                            } else {
                                    cost = 1;
                            }

                            // Step 6
                            d[i][j] = Math.min(Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1),
                                            d[i - 1][j - 1] + cost);
                    }
            }

            // Step 7
            return d[n][m];
    }

	
	public static HashMap<Integer,HashMap<Integer,Double>> MatrixTranspose(HashMap<Integer,HashMap<Integer,Double>> m)
	{
		HashMap<Integer,HashMap<Integer,Double>> ret = new HashMap<Integer,HashMap<Integer,Double>>();
		for(Integer i : m.keySet())
		{
			HashMap<Integer,Double> row = m.get(i);
			for(Integer j:row.keySet())
			{
				Double val = row.get(j);
				if(!ret.containsKey(j))
				{
					HashMap<Integer,Double> temp = new HashMap<Integer,Double>();
					ret.put(j, temp);
				}
				ret.get(j).put(i, val);
			}
		}
		
		return ret;
	}
	
	public static HashMap<Integer,HashMap<Integer,Double>> MatrixTimes(HashMap<Integer,HashMap<Integer,Double>> m1,HashMap<Integer,HashMap<Integer,Double>> m2)
	{
		 HashMap<Integer,HashMap<Integer,Double>> ret = new  HashMap<Integer,HashMap<Integer,Double>>();
		 HashMap<Integer,HashMap<Integer,Double>> m2t = MatrixTranspose(m2);
		 for(Integer i : m1.keySet())
		 {
			 HashMap<Integer,Double> row = m1.get(i);
			 for(Integer j : m2t.keySet())
			 {
				 HashMap<Integer,Double> col = m2t.get(j);
				 Double val = 0.0;
				 for(Integer k: col.keySet())
				 {
					 if(row.containsKey(k))
					 {
						 Double tem = row.get(k)*col.get(k);
						 val+=tem;
					 }
				 }
				 if(!ret.containsKey(i))
				 {
					 HashMap<Integer,Double> temp = new HashMap<Integer,Double>();
					 ret.put(i, temp);
				 }
				 ret.get(i).put(j, val);
			 }
		 }
		 
		 return ret;
	}
	public static HashMap<Integer,HashMap<Integer,Double>> MatrixTimes(HashMap<Integer,HashMap<Integer,Double>> m1,Double m2)
	{
		 HashMap<Integer,HashMap<Integer,Double>> ret = new  HashMap<Integer,HashMap<Integer,Double>>();

		 for(Integer i : m1.keySet())
		 {
			 HashMap<Integer,Double> row = m1.get(i);
			 HashMap<Integer,Double> temprow = new HashMap<Integer,Double>();
			 for(Integer j: row.keySet())
			 {
				 Double val = row.get(j);
				 val*=m2;
				 temprow.put(j, val);
			 }
			 m1.put(i, temprow);
		 }
		 
		 return ret;
	}

	public static HashMap<Integer,HashMap<Integer,Double>> MatrixPlus(HashMap<Integer,HashMap<Integer,Double>> m1,HashMap<Integer,HashMap<Integer,Double>> m2)
	{
		 HashMap<Integer,HashMap<Integer,Double>> ret = new  HashMap<Integer,HashMap<Integer,Double>>();
		 for(Integer i : m1.keySet())
		 {
			 HashMap<Integer,Double> row = m1.get(i);
			 if(!ret.containsKey(i))
			 {
				 HashMap<Integer,Double> nrow = new HashMap<Integer,Double>();
				 ret.put(i, nrow);
			 }
			 for(Integer k : row.keySet())
			 {
				 Double val = row.get(k);
				 ret.get(i).put(k, val);
			 }
		 }
		 for(Integer i : m2.keySet())
		 {
			 HashMap<Integer,Double> row = m2.get(i);
			 if(!ret.containsKey(i))
			 {
				 HashMap<Integer,Double> nrow = new HashMap<Integer,Double>();
				 ret.put(i, nrow);
			 }
			 HashMap<Integer,Double> temp = ret.get(i);
			 for(Integer k : row.keySet())
			 {
				 Double val = row.get(k);
				 if(temp.containsKey(k))
				 {
					 val+=temp.get(k);
				 }
				 ret.get(i).put(k, val);
			 }
		 }
		 
		 return ret;
	}
	
	public static HashMap<Integer,HashMap<Integer,Double>> MatrixMinus(HashMap<Integer,HashMap<Integer,Double>> m1,HashMap<Integer,HashMap<Integer,Double>> m2)
	{
		 HashMap<Integer,HashMap<Integer,Double>> ret = new  HashMap<Integer,HashMap<Integer,Double>>();
		 for(Integer i : m1.keySet())
		 {
			 HashMap<Integer,Double> row = m1.get(i);
			 if(!ret.containsKey(i))
			 {
				 HashMap<Integer,Double> nrow = new HashMap<Integer,Double>();
				 ret.put(i, nrow);
			 }
			 for(Integer k : row.keySet())
			 {
				 Double val = row.get(k);
				 ret.get(i).put(k, val);
			 }
		 }
		 for(Integer i : m2.keySet())
		 {
			 HashMap<Integer,Double> row = m2.get(i);
			 if(!ret.containsKey(i))
			 {
				 HashMap<Integer,Double> nrow = new HashMap<Integer,Double>();
				 ret.put(i, nrow);
			 }
			 HashMap<Integer,Double> temp = ret.get(i);
			 for(Integer k : row.keySet())
			 {
				 Double val = row.get(k);
				 val = -1*val;
				 if(temp.containsKey(k))
				 {
					 val+=temp.get(k);
				 }
				 ret.get(i).put(k, val);
			 }
		 }
		 
		 return ret;
	}
	

	public static Double calcCosine(HashMap<Integer,Integer> vec1,HashMap<Integer,Integer> vec2)
	{
		Double score =0.0;
		Double fenzi = 0.0;
		Double fenmu = 0.0;
		for(Integer key : vec1.keySet())
		{
			if(vec2.containsKey(key))
			{
				Integer val1 = vec1.get(key);
				Integer val2 = vec2.get(key);
				fenzi+=val1*val2;
			}
		}
		if(fenzi<0.2)
			return fenzi;
		Double fenmu1 = 0.0;
		for(Integer key : vec1.keySet())
		{
			Integer val = vec1.get(key);
			val= val*val;
			fenmu1+=val;
		}
		fenmu1 = Math.sqrt(fenmu1);
		
		Double fenmu2 = 0.0;
		for(Integer key : vec2.keySet())
		{
			Integer val = vec2.get(key);
			val= val*val;
			fenmu2+=val;
		}
		fenmu2 = Math.sqrt(fenmu2);
		fenmu = fenmu1*fenmu2;
		
		if(fenmu<0.001)
			return 0.0;
		
		score = fenzi/fenmu;
		
		return score;
	}
	
	public static Double calcCosineDouble(HashMap<Integer,Double> vec1,HashMap<Integer,Double> vec2)
	{
		Double score =0.0;
		Double fenzi = 0.0;
		Double fenmu = 0.0;
		for(Integer key : vec1.keySet())
		{
			if(vec2.containsKey(key))
			{
				Double val1 = vec1.get(key);
				Double val2 = vec2.get(key);
				fenzi+=val1*val2;
			}
		}
		if(fenzi<0.2)
			return fenzi;
		Double fenmu1 = 0.0;
		for(Integer key : vec1.keySet())
		{
			Double val = vec1.get(key);
			val= val*val;
			fenmu1+=val;
		}
		fenmu1 = Math.sqrt(fenmu1);
		
		Double fenmu2 = 0.0;
		for(Integer key : vec2.keySet())
		{
			Double val = vec2.get(key);
			val= val*val;
			fenmu2+=val;
		}
		fenmu2 = Math.sqrt(fenmu2);
		fenmu = fenmu1*fenmu2;
		
		if(fenmu<0.001)
			return 0.0;
		
		score = fenzi/fenmu;
		
		return score;
	}
	
	public static PrintWriter getWriter(String filename) throws Exception
	{
		PrintWriter outf = new PrintWriter(new FileOutputStream(filename),true);  
		return outf;
	}
	
	public static void addMap(HashMap<Integer,Integer> map, Integer key, Integer count)
	{
		if(map.containsKey(key))
		{
			Integer temp = map.get(key);
			temp+=count;
			map.put(key, temp);
		}
		else
		{
			map.put(key, new Integer(count));
		}
	}
	
	public static void addMapLong(HashMap<Long,Integer> map, Long key, Integer count)
	{
		if(map.containsKey(key))
		{
			Integer temp = map.get(key);
			temp+=count;
			map.put(key, temp);
		}
		else
		{
			map.put(key, new Integer(count));
		}
	}
	
	public static void addMap(HashMap<Integer,Integer> map1, HashMap<Integer,Integer> map2)
	{
		for(Integer k : map2.keySet())
		{
			Integer v = map2.get(k);
			if(map1.containsKey(k))
			{
				Integer val = map1.get(k);
				val+=v;
				map1.put(k, val);
			}
			else
			{
				map1.put(k, v);
			}
		}

	}
	public static void addMapString(HashMap<String,Integer> map1, HashMap<String,Integer> map2)
	{
		for(String k : map2.keySet())
		{
			Integer v = map2.get(k);
			if(map1.containsKey(k))
			{
				Integer val = map1.get(k);
				val+=v;
				map1.put(k, val);
			}
			else
			{
				map1.put(k, v);
			}
		}

	}
	

	
	public static void addMap(HashMap<Integer,Double> map, Integer key, Double count)
	{
		if(map.containsKey(key))
		{
			Double temp = map.get(key);
			temp+=count;
			map.put(key, temp);
		}
		else
		{
			map.put(key, count.doubleValue());
		}
	}
	public static void addMap(HashMap<String,Integer> map, String key, Integer count)
	{
		if(map.containsKey(key))
		{
			Integer temp = map.get(key);
			temp+=count;
			map.put(key, temp);
		}
		else
		{
			map.put(key, new Integer(count));
		}
	}
	
	public static TreeMap<String,Integer> rankHashMap(HashMap<String,Integer>map)
	{
		ValueComparator bvc =  new ValueComparator(map);
		TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
		sorted_map.putAll(map);
		return sorted_map;
	}
	

	
	public static HashMap<String,Integer> loadDic(String filename)throws Exception
	{
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		BufferedReader brd = getReader(filename);
		String line = null;
		Integer id = 0;
		while((line=brd.readLine())!=null)
		{
			if(line.equals(""))
			{
				continue;
			}
			if(line.contains("@"))
			{
				if(line.contains("attribute"))
				{
//					System.out.println(line);
					String[] items = line.split(" ");
					map.put(items[1], id);
					id++;
				}
			}
			else
			{
				break;
			}
		}
		
		return map;
	}
	
	public static Object objLoad2(String filename) throws Exception
	{
		@SuppressWarnings("resource")
		ObjectInputStream oin3 = new ObjectInputStream(new FileInputStream(filename));
		Object qbase = (Object) oin3.readObject();	
		return qbase;
	}

	public static BufferedReader getReader(String filename) throws Exception
	{
		File path = new File(filename);
		FileInputStream  fr = new FileInputStream(path);
	    BufferedReader brd = new BufferedReader( new InputStreamReader(fr, "UTF-8") );
	    return brd;
	}
	
	  public static int distance(String s1, String s2) {
		    s1 = s1.toLowerCase();
		    s2 = s2.toLowerCase();
		    
		    int[] costs = new int[s2.length() + 1];
		    for (int i = 0; i <= s1.length(); i++) {
		      int lastValue = i;
		      for (int j = 0; j <= s2.length(); j++) {
		        if (i == 0)
		          costs[j] = j;
		        else {
		          if (j > 0) {
		            int newValue = costs[j - 1];
		            if (s1.charAt(i - 1) != s2.charAt(j - 1))
		              newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
		            costs[j - 1] = lastValue;
		            lastValue = newValue;
		          }
		        }
		      }
		      if (i > 0)
		        costs[s2.length()] = lastValue;
		    }
		    return costs[s2.length()];
	}
	public static void outputobj(Object obj,String filename) throws Exception
	{
		FileOutputStream fos1 = new FileOutputStream(filename);	
		ObjectOutputStream oos1 = new ObjectOutputStream(fos1);		
		oos1.writeObject(obj);
		oos1.flush();
		oos1.close();
	}
	public static String parseUA(String userAgent)
	{
	    String browserName = "Unknown";
	    String clientOsName = "Unknown";

        // determine browser name
        if (userAgent.indexOf("MSIE 5") != -1)
            browserName = "Internet Explorer 5";
        else if (userAgent.indexOf("MSIE 6") != -1)
            browserName = "Internet Explorer 6";
        else if (userAgent.indexOf("AOL") != -1)
            browserName = "AOL";
        else if (userAgent.indexOf("Opera") != -1)
            browserName = "Opera";
        else if (userAgent.indexOf("Gecko") != -1)
        {
            if (userAgent.indexOf("Firefox") != -1)
                browserName ="Firefox";
            else
                browserName = "Mozilla";
        }
        
        // determine operating system name
        if (userAgent.indexOf("Windows NT 5.0") != -1)
            clientOsName = "Windows 2000";      
        else if (userAgent.indexOf("Windows NT 5.1") != -1)
            clientOsName = "Windows XP";       
        else if (userAgent.indexOf("Windows NT 6.1") != -1)
            clientOsName = "Windows 7";        
        else if (userAgent.indexOf("AppleWebKit") != -1)
            clientOsName = "Apple";    
        else if (userAgent.indexOf("Linux") != -1)
            clientOsName = "Linux";
        String ret = clientOsName+"\t"+browserName;
        
        return ret;
	    
	}
	public static void outputHashMap(HashMap<String,Integer>count,String filename) throws Exception
	{
		TreeMap<String,Integer>tree = Utility.rankHashMap(count);
		PrintWriter outf = Utility.getWriter(filename);
		for(String word:tree.keySet())
		{
			Integer ct = count.get(word);
			outf.println(word+","+ct);
		}
	}
	public static String cleanString(String word)
	{
		String[] list = Constants.stopwords;
		for(String st: list)
		{
			if(word.contains(st))
			{
				word = word.replace(st, " ");
			}
		}

		word = word.toLowerCase();
		word = word.replace("             ", " ");
		word = word.replace("            ", " ");
		word = word.replace("           ", " ");
		word = word.replace("          ", " ");
		word = word.replace("         ", " ");
		word = word.replace("        ", " ");
		word = word.replace("       ", " ");
		word = word.replace("      ", " ");
		word = word.replace("     ", " ");
		word = word.replace("    ", " ");
		word = word.replace("   ", " ");
		word = word.replace("  ", " ");
		word = word.trim();
		return word;
	}

}
class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
