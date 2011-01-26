/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package tigermajorroads;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
 
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.AttributeExpressionImpl2;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterType;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LikeFilter;


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.SIRtree;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.operation.linemerge.LineMerger;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

/**
 *  Load the delme_major table, based either on a state or module
 *    For each road loads,
 *      if its an interstate, stick it in the interstate hash
 *      if its a ushighway, stick it in the ushighway hash
 *      if its a statehighway, stick it in the statehighway hash
 *      otherwise, stick it in the other hash
 * 
 * 
 *   NOTE: "Highway 45" is interpreted as "State Highway 45"
 *         (same with routes)  
 *          
 * 
 http://en.wikipedia.org/wiki/Interstates
 http://en.wikipedia.org/wiki/U.S._Highway_System
 http://en.wikipedia.org/wiki/State_highway#United_States
 http://en.wikipedia.org/wiki/National_Highway_System
 http://en.wikipedia.org/wiki/United_States_Numbered_Highways
 
 * @author david blasby
 */
public class MajorRoads
{
    String MODULE = "";
    
    double tolerance1 = 0.00005; // 50% reduction
    double tolerance2 = 0.00025; // 75% reduction
    double tolerance3 = 0.00080; //87% reduction
    
     Hashtable interstate = new Hashtable();    //int to ArrayList of geometry
     Hashtable ushighway = new Hashtable();     //int to ArrayList of geometry
     Hashtable statehighway = new Hashtable();  //int to ArrayList of geometry
     Hashtable other =new Hashtable();  //String to ArrayList of geometry (we put cfcc in user data)
    
     DataStore ds = null;
	
	
	/**
	 * reads the completechain dataset
	 * @throws Exception
	 */
	public void getrows() throws Exception
	{

			FilterFactory ff = FilterFactory.createFilterFactory();
			LikeFilter like = ff.createLikeFilter();
			like.setPattern(MODULE+"*","*","?","!");
			like.setValue(ff.createAttributeExpression( null, "module"));

			
			String[] ps = new String []{"the_geom","cfcc",
					  "name",
					  "altname1","altname2","altname3","altname4","altname5",
					  "altname6","altname7","altname8","altname9"
					  
					};
			Query q = new DefaultQuery("delme_major",like,ps);	     	
			 
			 	
			FeatureReader fr = ds.getFeatureReader(q, new DefaultTransaction() );
 	
		    while (fr.hasNext())
		    {
		    	Feature f= fr.next();
		    	
		    	f.getDefaultGeometry().setUserData(  f.getAttribute(1) ); // sets CFCC in the geometry
		    	
		    	Integer interstate_num = null;
		    	Integer ushwy_num = null;
		    	Integer statehwy_num = null;
		    	String  other_nam = null;
		    	
		    	interstate_num = getInterstate(f);
		    	if (interstate_num == null)
		    	{
		    	   ushwy_num  = getUShwy(f);
		    	    if (ushwy_num == null)
			    	{ 
		    	        statehwy_num = getStateHwy(f);
		    	        if (ushwy_num == null)
				    	{ 
		    	             other_nam    = getOtherName(f); 
				    	}
			    	}
		    	}
		    	
		    	if (interstate_num != null)
		    	{
		    		ArrayList al = (ArrayList) interstate.get(interstate_num);
		    		if (al ==null)
		    		{
		    			al = new ArrayList();
		    			interstate.put(interstate_num,al);
		    		}
		    		al.add( f.getDefaultGeometry() );
		    	}
		    	else if (ushwy_num != null)
		    	{
		    		ArrayList al = (ArrayList) ushighway.get(ushwy_num);
		    		if (al ==null)
		    		{
		    			al = new ArrayList();
		    			ushighway.put(ushwy_num,al);
		    		}
		    		al.add( f.getDefaultGeometry() );
		    	}
		    	else if (statehwy_num != null)
		    	{
		    		ArrayList al = (ArrayList) statehighway.get(statehwy_num);
		    		if (al ==null)
		    		{
		    			al = new ArrayList();
		    			statehighway.put(statehwy_num,al);
		    		}
		    		al.add( f.getDefaultGeometry() );
		    	}
		    	else //other
		    	{
		    		String name= (String) f.getAttribute("name");
		    		ArrayList al = (ArrayList) other.get(name);
		    		if (al ==null)
		    		{
		    			al = new ArrayList();
		    			other.put(name,al);
		    		}
		    		al.add( f.getDefaultGeometry() );		    		
		    	}
		    }
		    fr.close();
}
	

	
	/**
	 * @param f
	 * @return
	 */
	private String getOtherName(Feature f) 
	{
		return (String) f.getAttribute("name"); // everything passes this!
	}

	/**
	 *     Indiana State Route 933
    
 State Highway 1 South
 State Highway 1&78
  State Highway 1
     State 371  NW
 State Highway 11/38
 State Highway 258  W

 State Highway E- 470
 State Highway EE
 State Highway F

 State Hwy19
 State Hy 26
 State Route 317
  
 State Road 104

E State Road 241


Illinois Route 89
Indiana 64
 Georgia Highway 138
 Il Route 113
 Indiana 145
 Kansas State Highway 132
 Kentucky Highway 451
Ky-1043
Ky-1043-30
 Ky Hwy 15
 La 1064 Hwy
 La 107
 La Highway 1032
Lr-18010
Montana Highway 135  S
 Montana Highway 16

Montana Hwy 1
 Montana Hwy 1  E
N Il Highway 148
 N Il Route 251
 N Illinois Route 130
 N State Highway 173
 N State Route 115
 Nc Highway 11
 New Jersey Route 1
 New Mexico 114
 South Carolina Highway 219
 South Dakota Highway 21
 South Kentucky Highway 15

 Vt Rte 15
 Vt Rte 15 Hwy
 Al Highway 202
 Kansas State Highway 2
 Montana Highway 200
 Nm Hwy 244
 Al Hwy 35
  Vermont Route 73  E
 Ar 9 Hwy
 Arizona Hwy 77
 Kansas State Highway 9
 Route 10
Highway 1
State State

	 * @param s
	 * @return
	 */
	private Integer getStateHwy(String s) 
	{
		 // main morph is "State Highway xxx"
		
		if (s==null)
			return null;
	    if (s.length() ==0)
	    	return null;
	   
	    
	    s = s.replaceFirst("  "," ");
	    s = s.replaceFirst("  "," ");
	    
	    s = s.replaceFirst("^Highway ","State Highway ");

	    
	    s = s.replaceFirst("^New ","");
	    s = s.replaceFirst("^[ESNW]+ ","");
	    s = s.replaceFirst("^South ","");
	    s = s.replaceFirst("^North ","");
	    s = s.replaceFirst("^East ","");
	    s = s.replaceFirst("^West ","");
	    
	    s = s.replaceFirst(" [NSEW]+\\z","");  //  \\z --> $ (end of line)
	    
	    s = s.replaceFirst(" South\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst(" North\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst(" East\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst(" West\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst("[NSEW]+\\z","");  //  \\z --> $ (end of line)      \\iffy

	    s = s.replaceFirst("/ & [1234567890]+\\z","");  //State Highway  6 & 19
	    s = s.replaceFirst("/ &[1234567890]+\\z","");  // United States Highway 6 & 19
	    s = s.replaceFirst("/&[1234567890]+\\z","");  // United States Highway 6 & 19
	    s = s.replaceFirst("/& [1234567890]+\\z","");  // United States Highway 6 & 19

	    s = s.replaceFirst("/[1234567890]+\\z","");  // United States Highway 25/70

	    s = s.replaceFirst("  "," ");

	    s = s.replaceFirst("^Illinois ","State ");
	    s = s.replaceFirst("^Indiana ","State ");
	    s = s.replaceFirst("^Il ","State ");
	    s = s.replaceFirst("^Georgia ","State ");
	    s = s.replaceFirst("^Kansas ","State ");
	    s = s.replaceFirst("^Kentucky ","State ");
	    s = s.replaceFirst("^La ","State ");
	    s = s.replaceFirst("^Ky ","State ");
	    s = s.replaceFirst("^Montana ","State ");
	    s = s.replaceFirst("^New Jersey ","State ");
	    s = s.replaceFirst("^Jersey ","State ");
	    s = s.replaceFirst("^New Mexico ","State ");
	    s = s.replaceFirst("^Mexico ","State ");
	    s = s.replaceFirst("^South Carolina ","State ");
	    s = s.replaceFirst("^Carolina ","State ");
	    s = s.replaceFirst("^South Dakota ","State ");
	    s = s.replaceFirst("^Dakota ","State ");
	    s = s.replaceFirst("^South Kentucky ","State ");
	    s = s.replaceFirst("^Vt ","State ");
	    s = s.replaceFirst("^Nm ","State ");
	    s = s.replaceFirst("^Ar ","State ");
	    s = s.replaceFirst("^Al ","State ");
	    s = s.replaceFirst("^Arizona ","State ");
	    s = s.replaceFirst("^Vermont ","State ");
	    s = s.replaceFirst("  "," ");	    s = s.replaceFirst("  "," ");



	    s = s.replaceFirst("State State " ,"State ");
	    s = s.replaceFirst("State State " ,"State ");
	    
	    s = s.replaceFirst("State " ,"State Highway ");
	    s = s.replaceFirst("Highway Highway " ,"Highway ");
	    s = s.replaceFirst("Highway Highway " ,"Highway ");
	    
	    s = s.replaceFirst("State State " ,"State ");
	    s = s.replaceFirst("State State " ,"State ");
	    
	    
	    s = s.replaceFirst("^State Hwy","State Highway ");
	    s = s.replaceFirst("  "," ");

	    s = s.replaceFirst(" Rte "," Route ");
	    s = s.replaceFirst("^State Route ","State Highway ");
	    s = s.replaceFirst(" Route "," Highway ");
	    
	    s = s.replaceFirst("Route Highway","Highway ");
	    s = s.replaceFirst("Highway Route ","Highway ");
	   
	    s = s.replaceFirst("Highway Highway " ,"Highway ");	    
	    s = s.replaceFirst("State State " ,"State ");
	    s = s.replaceFirst("Highway Highway " ,"Highway ");	    
	    s = s.replaceFirst("State State " ,"State ");
	    
	    s = s.replaceFirst("  "," ");
	   
	    s = s.replaceFirst(" [NSEW]+\\z","");  //  \\z --> $ (end of line)
	    
	    s = s.replaceFirst(" South\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst(" North\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst(" East\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst(" West\\z","");  //  \\z --> $ (end of line)
	    
	    s = s.replaceFirst("[NSEW]+\\z","");  //  \\z --> $ (end of line)      \\iffy

	    
	    Pattern p = Pattern.compile("^State Highway ([0123456789]+)\\z");  //  \\z --> $ (end of line)
	    Matcher m = p.matcher(s);
	    if ( m.matches())
	    {
	    	return new Integer(m.group(1));
	    }	    
		return null;
	}
	
	/**
	 * returns the SMALLEST value in the list
	 * 
	 * @param possibles arraylist of Integers
	 * @return
	 */
	private Integer determineWhichOne(ArrayList possibles)
	{
		Integer result = null;
		boolean gotone = false;
		
		Iterator it = possibles.iterator();
		while (it.hasNext())
		{
			Integer i = (Integer) it.next();
			if (i !=null)
			{
				if (!gotone)
				{
					gotone = true;
					result = i;
				}
				else
				{
					if (i.intValue() < result.intValue())
					{
						result = i;
					}	
				}
			}
		}
		return result;
	}

	/**
	 * @param f
	 * @return
	 */
	private Integer getStateHwy(Feature f) 
	{
		ArrayList possibles = new ArrayList();
		
		possibles.add (  getStateHwy( (String) f.getAttribute("name")) );
		possibles.add (  getStateHwy( (String) f.getAttribute("altname1")) );
		possibles.add (  getStateHwy( (String) f.getAttribute("altname2")) );
		possibles.add (  getStateHwy( (String) f.getAttribute("altname3")) );
		possibles.add (  getStateHwy( (String) f.getAttribute("altname4")) );
		possibles.add (  getStateHwy( (String) f.getAttribute("altname5")) );
		possibles.add (  getStateHwy( (String) f.getAttribute("altname6")) );
		possibles.add (  getStateHwy( (String) f.getAttribute("altname7")) );
		possibles.add (  getStateHwy( (String) f.getAttribute("altname8")) );
		possibles.add (  getStateHwy( (String) f.getAttribute("altname9")) );
		
        return determineWhichOne(possibles);
	}

	/**
	 *  morphs:  (A=alternate B=business route)
   United States Highway 395
   US Highway 158
   US Hwy 45 and 47
   US Hwy 41 N 
   US-25
   USHwy 12
   US62                             //not handled
   United States Highway 25/70
   United States Highway 250  S
   United States Highway 45W
   United States Highway 51 South
   United States Highway 51 South  NE
    United States Highway 6 & 19
 United States Highway 64A
 United States Highway 64E

 United States Highway Route 1
 United States Highway Route 11
 United States Route 1

 New US Hwy 50
 
E United States Highway 10
 NW United States Highway 169


	 * @param s
	 * @return
	 */
	private Integer getUShwy(String s) 
	{
		if (s==null)
			return null;
	    if (s.length() ==0)
	    	return null;
	   
	    s = s.replaceFirst("^New ","");
	    
	    s = s.replaceFirst("^[ESNW]+ ","");
	    
	    s = s.replaceFirst("^US Highway ","United States Highway ");
	    s = s.replaceFirst("^US Hwy ","United States Highway ");
	    s = s.replaceFirst("^US-","United States Highway ");
	    s = s.replaceFirst("^USHwy ","United States Highway ");
	    
	    s = s.replaceFirst("^United States Highway Route ","United States Highway ");
	    s = s.replaceFirst("^United States Route ","United States Highway ");

	    
	   // s = s.replaceFirst("^Route ","United States Highway ");       //iffy
	  //  s = s.replaceFirst("^Highway ","United States Highway ");     //iffy
	 //   s = s.replaceFirst("^Hwy ","United States Highway ");     //iffy
	    
	   
	    s = s.replaceFirst(" and [0123456789]+\\z","");     // " and 47"
	    s = s.replaceFirst("/[1234567890]+\\z","");  // United States Highway 25/70
	    s = s.replaceFirst("/ & [1234567890]+\\z","");  // United States Highway 6 & 19
	    
	    s = s.replaceFirst("[AB]\\z","");  // A = alternative B=business
	    
	    
	    s = s.replaceFirst("  "," ");
	   
	    s = s.replaceFirst(" [NSEW]+\\z","");  //  \\z --> $ (end of line)
	    
	    s = s.replaceFirst(" South\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst(" North\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst(" East\\z","");  //  \\z --> $ (end of line)
	    s = s.replaceFirst(" West\\z","");  //  \\z --> $ (end of line)
	    
	    s = s.replaceFirst("[NSEW]+\\z","");  //  \\z --> $ (end of line)      \\iffy
	    
	    
	   
	    
	    if (!(s.startsWith("United States Highway ")))
	    	return null;
		
	    Pattern p = Pattern.compile("^United States Highway ([0123456789]+)\\z");  //  \\z --> $ (end of line)
	    Matcher m = p.matcher(s);
	    if ( m.matches())
	    {
	    	return new Integer(m.group(1));
	    }	    
		return null;
	}

	/**
	 * @param f
	 * @return
	 */
	private Integer getUShwy(Feature f) 
	{
		ArrayList possibles = new ArrayList();
		
		possibles.add (  getUShwy( (String) f.getAttribute("name")) );
		possibles.add (  getUShwy( (String) f.getAttribute("altname1")) );
		possibles.add (  getUShwy( (String) f.getAttribute("altname2")) );
		possibles.add (  getUShwy( (String) f.getAttribute("altname3")) );
		possibles.add (  getUShwy( (String) f.getAttribute("altname4")) );
		possibles.add (  getUShwy( (String) f.getAttribute("altname5")) );
		possibles.add (  getUShwy( (String) f.getAttribute("altname6")) );
		possibles.add (  getUShwy( (String) f.getAttribute("altname7")) );
		possibles.add (  getUShwy( (String) f.getAttribute("altname8")) );
		possibles.add (  getUShwy( (String) f.getAttribute("altname9")) );

		  return determineWhichOne(possibles);
	}

/**
 *     Morphs "I-95" 
       "Interstate40"  //not handled
       "Interstate 40" 
       "Interstate 40 Hwy"    
       "Interstate-40" 
       "I - 10" 
       "I -10"  
       "I-10  W"
       "I-35E" //not handled
       
       "I-475 S I- 75"
       "I-78 & US Route 22"
      
      
 * @param s
 * @return
 */
	Integer getInterstate(String s)
	{
		if (s==null)
			return null;
	    if (s.length() ==0)
	    	return null;
	    if (!(s.startsWith("I")))
	    	return null;
	    
	    s = s.replaceFirst("^[ESNW]+ ","");
	    
	    s = s.replaceFirst(" - ","-");
	    s = s.replaceFirst("  "," ");
	    s = s.replaceFirst("^Interstate ","I-");
	    s = s.replaceFirst(" Hwy\\z","");
	    s = s.replaceFirst("I- ","I-");
	    s = s.replaceFirst(" [NSEW]\\z","");  //  \\z --> $ (end of line)
	    
	    //should look like:
	    //"I-## (junk)"
	    
	    if (!(s.startsWith("I-")))
	    	return null;
		
	    Pattern p = Pattern.compile("^I-([0123456789]+)\\z");  //  \\z --> $ (end of line)
	    Matcher m = p.matcher(s);
	    if ( m.matches())
	    {
	    	return new Integer(m.group(1));
	    }	    
		return null;
	}

	/**
	 * @param f
	 * @return
	 */
	private Integer getInterstate(Feature f)
	{
		ArrayList possibles = new ArrayList();
		
		possibles.add (  getInterstate( (String) f.getAttribute("name")) );
		possibles.add (  getInterstate( (String) f.getAttribute("altname1")) );
		possibles.add (  getInterstate( (String) f.getAttribute("altname2")) );
		possibles.add (  getInterstate( (String) f.getAttribute("altname3")) );
		possibles.add (  getInterstate( (String) f.getAttribute("altname4")) );
		possibles.add (  getInterstate( (String) f.getAttribute("altname5")) );
		possibles.add (  getInterstate( (String) f.getAttribute("altname6")) );
		possibles.add (  getInterstate( (String) f.getAttribute("altname7")) );
		possibles.add (  getInterstate( (String) f.getAttribute("altname8")) );
		possibles.add (  getInterstate( (String) f.getAttribute("altname9")) );

		  return determineWhichOne(possibles);
	}



	/**
	 *  dbname=tiger2005fe user=postgres host=localhost
	 * 
	 *  parses the PG connect string into geotools postgis datastore params
	 * 
	 * pg_Connect("host=myHost port=myPort  dbname=myDB user=myUser password=myPassword ");
	 * @param connectstring
	 * @return
	 * @throws Exception
	 */
	public static Map parsePG(String connectstring) throws Exception
	{
		Map param = new HashMap();
		
		Pattern p =   Pattern.compile(".*host=([^ ]+).*");
		Matcher m = p.matcher(connectstring);
		
		if (m.matches())
		{
			param.put("host",m.group(1));
		}
		else
		{
			param.put("host","localhost");
		}
		
		p =   Pattern.compile(".*user=([^ ]+).*");
		m = p.matcher(connectstring);
		
		if (m.matches())
		{
			param.put("user",m.group(1));
		}
		else
		{
			throw new Exception("PG CONNECT string does not contain a user name.  connect string should look like: 'host=myHost port=myPort  dbname=myDB user=myUser password=myPassword'");
		}
		
		p =   Pattern.compile(".*dbname=([^ ]+).*");
		m = p.matcher(connectstring);
		
		if (m.matches())
		{
			param.put("database",m.group(1));
		}
		else
		{
			throw new Exception("PG CONNECT string does not contain a dbname.  connect string should look like: 'host=myHost port=myPort  dbname=myDB user=myUser password=myPassword'");
		}
		
		p =   Pattern.compile(".*port=([^ ]+).*");
		m = p.matcher(connectstring);
		
		if (m.matches())
		{
			param.put("port",m.group(1));
		}
		else
		{
			param.put("port","5432");
		}
		
		p =   Pattern.compile(".*password=([^ ]+).*");
		m = p.matcher(connectstring);
		
		if (m.matches())
		{
			param.put("passwd",m.group(1));
		}
		else
		{
			//do nothing
		}
	
		return param;
	}
	
	
	public static void main(String[] args) 
	{
		
		if ( (args.length !=2) && (args.length !=3) )
		{
			System.out.println("usage: \"host=myHost port=myPort  dbname=myDB user=myUser password=myPassword\" <module name or partial module name> ");
			System.out.println("dont forget to:");
			System.out.println("1. put quotes around the postgresql connection string");
			System.out.println("2. pre-create the output database table:");
			return;
		}
			
		MajorRoads THIS = new MajorRoads();
		
		
	    PostgisDataStoreFactory  pgdsf = new PostgisDataStoreFactory();
	
	     try{
	     	
	     	
	     	Map param =  parsePG(args[0]);
	    	     
	        param.put("wkb enabled","true");
	        param.put("loose bbox","true");
	        param.put("dbtype","postgis");
	        
          
            
	     	THIS.ds = pgdsf.createDataStore(param);
	     	
	        THIS.MODULE = args[1];
	     	
	     	System.out.println("start module = "+THIS.MODULE);
	     	  
	     	System.out.println("loading rows...");
	     	THIS.getrows();
	     	System.out.println("loaded rows");
	
	     	
	     	System.out.println("Processing...");
	     	THIS.process();
	     	
	     	System.out.println("Saving...");
	     	THIS.save();
	     	System.out.println("Done!");
	     }
	     catch (SchemaNotFoundException ee)
		 {
	     	System.out.println("you need to have major_roads in your database:");
	     	System.out.println("		create table major_roads (");
	     	System.out.println("               state text, ");
	   		System.out.println("               gen_full geometry,");
	   		System.out.println("               gen_1 geometry,");
	   		System.out.println("               gen_2 geometry,");
	   		System.out.println("               gen_3 geometry,");
			System.out.println("	           interstate int,");
			System.out.println("	           ushighway  int,");
			System.out.println("	           statehighway int,");
			System.out.println("	           otherName text     ) with oids;");
			System.out.println("insert into geometry_columns values ('','','major_roads','gen_full',2,1,'GEOMETRY');");
			System.out.println("insert into geometry_columns values ('','','major_roads','gen_1',2,1,'GEOMETRY');");
			System.out.println("insert into geometry_columns values ('','','major_roads','gen_2',2,1,'GEOMETRY');");
			System.out.println("insert into geometry_columns values ('','','major_roads','gen_3',2,1,'GEOMETRY');");
		 }
	     catch (Exception e)
		 {
	     	e.printStackTrace();
		 }
	}

	/**
	 * 
	 */
	private void save() throws Exception
	{
	    //write out the dataset
//		create table major_roads (
//		           state text, 
//		           the_geom geometry,
//		           interstate int,
//		           ushighway  int,
//		           statehighway int,
//		           otherName text     ) with oids;
		
		FeatureStore fs = (FeatureStore) ds.getFeatureSource("major_roads");
		FeatureType ft = fs.getSchema();
		
		MemoryDataStore memorystore =new MemoryDataStore();
		
		System.out.println("saving interstate");
		
		addStuff(memorystore,interstate,"interstate",ft);
		
		System.out.println("saving ushighway");
		
		addStuff(memorystore,ushighway,"ushighway",ft);
		
		System.out.println("saving statehighway");
		
		addStuff(memorystore,statehighway,"statehighway",ft);
		
		System.out.println("saving othername");
		
		addStuff(memorystore,other,"othername",ft);
		
		System.out.println("writing to DB");
		
		fs.addFeatures(memorystore.getFeatureReader("major_roads"));
	}
		



	/**
	 * @param memorystore
	 * @param interstate2
	 * @param string
	 */
	private void addStuff(MemoryDataStore memorystore, Hashtable ht, String column,FeatureType ft) throws Exception
	{
		 Enumeration en  =ht.keys();
	      while (en.hasMoreElements())
	      {
	      	  Object key  = en.nextElement();
	      	  ArrayList lines = (ArrayList) ht.get(key);  // list of geometry
	          Iterator it = lines.iterator();
	          while (it.hasNext())
	          {
	          	   Geometry g  = (Geometry) it.next();
	          	   Object[] values = new Object[9];
	 			   values[ft.find("state")] = MODULE;
	 			   values[ft.find("gen_full")] = g;
	 			      values[ft.find("gen_1")] = generalize(g,tolerance1);
	 			      values[ft.find("gen_2")] = generalize(g,tolerance2);
	 			      values[ft.find("gen_3")] = generalize(g,tolerance3);
	 			   values[ft.find(column)] = key;
	 			   
	 			    Feature f = ft.create(values);
	 		    	memorystore.addFeature(f);
	          }
	      }
		
	}



	/**
	 *  merges the lines for each entry in the hashtable
	 *    so input "45" -> ArrayList of Geometry   
	 *       ouput "45" -> ArrayList of Geometry   (hopefully smaller than the original)  
	 * @param ht
	 */
	 private void buildNetworkTable(Hashtable ht)
	 {
	 	  int count_orig = 0;
	 	  int count_new  = 0;
	 	  
	      Enumeration en  =ht.keys();
	      while (en.hasMoreElements())
	      {
	      	  Object key  = en.nextElement();
	      	  ArrayList lines = (ArrayList) ht.get(key);  // list of geometry
	      	  count_orig += lines.size();
	          LineMerger lm = new LineMerger();
	      	  lm.add(lines);
	    	  Collection merged = lm.getMergedLineStrings(); //merged lines
	    	  count_new += merged.size();
	    	  lines.clear();
	    	  lines.addAll(merged);
	      }
	      System.out.println("merged "+count_orig+" lines into "+count_new+" lines.");
	 }


	/**
	 *  1. build network out of the individual pieces
	 *  2. make simplified/generalized versions of the lines
	 */
	private void process() 
	{
		System.out.println("merging interstates - " + interstate.size() );
		buildNetworkTable(interstate);
		System.out.println("merging us highways - " + ushighway.size());
		buildNetworkTable(ushighway);
		System.out.println("merging state highways - " + statehighway.size());
		buildNetworkTable(statehighway);
		System.out.println("merging other - " + other.size());
		buildNetworkTable(other);
	}
	
	private Geometry generalize(Geometry g, double tolerance)
	{
		return TopologyPreservingSimplifier.simplify(g,tolerance);
	}
	
}
