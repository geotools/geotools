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
package tigerpoly;

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


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.SIRtree;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;

/**
 *  a. load the completechains (lines)  store in hash table that takes TLID -> geometry
 *  b. load the polylink (links lines and polygons).  end result is a hashtable that takes a polyid to a list of lines (chains)
 *  c. load the PIP file and put in STRTree (spatial index)
 * 
 *  d. for each polygon (set of lines), polygonize.  
 *        for each polygon produced, they should enclose at least one PIP (>1 only for holes of the main polygon)
 *        the one one with the correct polygon is the result (exactly one pip inside it)
 *        if ANY polygon contains != 1 PIP then an error has occured
 * 
 *  e. send back polygon (with module and polyid)
 * 
 * 
 * @author david blasby
 *
 * insert into geometry_columns values ('','public','poly2','the_geom',2,1,'POLYGON');
 * create table poly2 (the_geom geometry,polyid numeric(10,0), module char(8)) with oids;
 */
public class TigerPoly
{
    String MODULE = "";
	Hashtable completeChains = new Hashtable();  // links TLID (Long) to LineString
	Hashtable polyLineSet = new Hashtable(); // links polyid (Long) to ArrayList of Linestring
	STRtree PIP = new STRtree() ;// links in the PIP Points (with internal JTS Long corresponding to polyid)
	
	Hashtable finishedPolys = new Hashtable(); //links POLYID (Long) to Polygon
	
	DataStore ds = null;
	
	boolean allowHolesWithNoPIP = false; // I noticed that some counties (TGR13231,TGR08031,TGR34017,TGR51683) have missing pips.  This
	                                    //  normally causes a qa/qc exception, this prevents it.  I recommend against this unless you know
	                                    // what you are doing.
	
	/** 
	 *  adds a line to the polygon line set
	 * @param tlidBD
	 * @param polyidBD
	 */
	public void addToPolylineset(BigDecimal tlidBD, BigDecimal polyidBD)
	{
		Long tlid = new Long(tlidBD.longValue());
		Long polyid = new Long(polyidBD.longValue());
		ArrayList al = (ArrayList)polyLineSet.get(polyid);
		if (al == null)
		{
			al = new ArrayList();
			polyLineSet.put(polyid,al);
		}
		al.add( completeChains.get(tlid) );
	}
	
	/**
	 * reads the completechain dataset
	 * @throws Exception
	 */
	public void getCC() throws Exception
	{

			FilterFactory ff = FilterFactory.createFilterFactory();
			CompareFilter cf = ff.createCompareFilter(FilterType.COMPARE_EQUALS);
			   
			  
			cf.addLeftValue( ff.createAttributeExpression( null, "module") );
			cf.addRightValue( ff.createLiteralExpression(MODULE));
			 	
			
			String[] ps = new String []{"wkb_geometry","tlid"};
			Query q = new DefaultQuery("completechain",cf,ps);	     	
			 
			 	
			FeatureReader fr = ds.getFeatureReader(q, new DefaultTransaction() );
 	
		    while (fr.hasNext())
		    {
		    	Feature f= fr.next();
		    	
		    	BigDecimal tlidBD = (BigDecimal) f.getAttribute(1);
		    	Long tlid = new Long(tlidBD.longValue());
		    	
		    	if (tlid == null)
		    		throw new Exception("cchain has null tlid");
		    	
		    	completeChains.put(tlid, f.getAttribute(0));//linestring
		    }
		    fr.close();
}
	
	/**
	 * 
	 *  reads the polychainlink dataset
	 */
	public void getPolygonLines() throws Exception
	{

			FilterFactory ff = FilterFactory.createFilterFactory();
			CompareFilter cf = ff.createCompareFilter(FilterType.COMPARE_EQUALS);
			   
			  
			cf.addLeftValue( ff.createAttributeExpression( null, "module") );
			cf.addRightValue( ff.createLiteralExpression(MODULE));
			 	
			
			String[] ps = new String []{"polyidl","polyidr","tlid"};
			Query q = new DefaultQuery("polychainlink",cf,ps);	     	
			 
			 	
			FeatureReader fr = ds.getFeatureReader(q, new DefaultTransaction() );
 	
		    while (fr.hasNext())
		    {
		    	Feature f= fr.next();
		    	f.getFeatureType();
		    	
		    	BigDecimal polyidlBD = (BigDecimal) f.getAttribute(0);
		    	BigDecimal polyidrBD = (BigDecimal) f.getAttribute(1);
		    	BigDecimal tlidBD = (BigDecimal) f.getAttribute(2);
		    	
		    	if (tlidBD == null)
		    		throw new Exception("polylink has null tlid");
		    	
		    	if ( (polyidlBD == null) && (polyidrBD ==null) )
		    		throw new Exception("polylink has 2 null polyids");
		    	
                boolean one_null = ((polyidlBD == null) || (polyidrBD ==null));
                if (one_null ||  (polyidlBD.longValue() != polyidrBD.longValue())) // == means that they are dangles
		    	{
			    	if (polyidlBD != null)
			    		addToPolylineset( tlidBD,  polyidlBD);
			    	if (polyidrBD != null)
			    		addToPolylineset( tlidBD,  polyidrBD);
		    	}
		    }
		    fr.close();

	}
	
	/**
	 * reads the pip dataset
	 * @throws Exception
	 */
	public void getPIP() throws Exception
	{

			FilterFactory ff = FilterFactory.createFilterFactory();
			CompareFilter cf = ff.createCompareFilter(FilterType.COMPARE_EQUALS);
			   
			  
			cf.addLeftValue( ff.createAttributeExpression( null, "module") );
			cf.addRightValue( ff.createLiteralExpression(MODULE));
			 	
			
			String[] ps = new String []{"wkb_geometry","polyid"};
			Query q = new DefaultQuery("pip",cf,ps);	     	
			 
			 	
			FeatureReader fr = ds.getFeatureReader(q, new DefaultTransaction() );
 	
		    while (fr.hasNext())
		    {
		    	Feature f= fr.next();
		    	
		    	BigDecimal polyidBD = (BigDecimal) f.getAttribute(1);
		    	Long polyid = new Long(polyidBD.longValue());
		    	
		    	if (polyid == null)
		    		throw new Exception("PIP  has null polyid");
		    	
		    	Geometry g = (Geometry) f.getAttribute(0);
		    	g.setUserData(polyid);
		    	
		    	PIP.insert(g.getEnvelopeInternal(),g);
		    }
		    fr.close();
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
			System.out.println("usage: \"host=myHost port=myPort  dbname=myDB user=myUser password=myPassword\" <module name> [allowMissingPIP]");
			System.out.println("dont forget to:");
			System.out.println("1. put quotes around the postgresql connection string");
			System.out.println("2. pre-create the output database table:");
			System.out.println("	     	 create table poly2 (the_geom geometry, polyid numeric(10,0), module char(8)) with oids;");
			System.out.println("             insert into geometry_columns values ('','public','poly2','the_geom',2,1,'POLYGON');");
			System.out.println("");
			System.out.println("allowMissingPIP -- dont set this unless you know what you're doing. TIGER is supposed to be a coverage, but a few groups have 'holes' in the coverage.  There's nothing you can do about it -- setting this option will cause this program NOT to throw qa-qc exceptions in this case. ");
			return;
		}
			
		TigerPoly THIS = new TigerPoly();
		
		if (args.length ==3)
		{
			if (args[2].equalsIgnoreCase("allowMissingPIP" ))
			{
			  THIS.allowHolesWithNoPIP = true;
			}
			else
			{
				System.out.println("to turn on 'allowMissingPIP', just put those words after your group.");
			}
		}
		
	    PostgisDataStoreFactory  pgdsf = new PostgisDataStoreFactory();
	
	     try{
	     	
	     	
	     	Map param =  parsePG(args[0]);
	    	     
	     param.put("wkb enabled","true");
	     param.put("loose bbox","true");
	     param.put("dbtype","postgis");
	     
          
            
	     	THIS.ds = pgdsf.createDataStore(param);
	     	
	     	  THIS.MODULE = args[1];
	     	
	     	System.out.println("start module = "+THIS.MODULE);
	     	  
	     	System.out.println("loading completechains...");
	     	THIS.getCC();
	     	System.out.println("loaded " + THIS.completeChains.size() +" completechains...");
	     	
	     	System.out.println("loading polychainlink...");	     	
	     	THIS.getPolygonLines();
	     	System.out.println("loaded " + THIS.polyLineSet.size() +" polygons...");
	     	
	     	System.out.println("loading PIP...");	     	
	     	THIS.getPIP();
	     	System.out.println("loaded "  + THIS.PIP.size() +" PIPs...");
	     	
	     	if (THIS.polyLineSet.size() !=  THIS.PIP.size())
	     	{
	     		throw new Exception("polylineset and PIP should be the same size!");
	     	}
	     	
	     	System.out.println("Processing...");
	     	THIS.process();
	     	
	     	if (THIS.polyLineSet.size() !=  THIS.finishedPolys.size())
	     	{
	     		throw new Exception("didnt build enough polygons!");
	     	}
	     	System.out.println("writing");
	     	THIS.write();
	     	
	     	System.out.println("done! " +THIS.MODULE);
	     	
	     	
	     }
	     catch (SchemaNotFoundException ee)
		 {
	     	System.out.println("You must create the poly2 table in the postgis database:");
			System.out.println("	     	 create table poly2 (the_geom geometry, polyid numeric(10,0), module char(8)) with oids;");
			System.out.println("             insert into geometry_columns values ('','public','poly2','the_geom',2,1,'POLYGON');");
		 }
	     catch (Exception e)
		 {
	     	e.printStackTrace();
		 }
	}
	
	
	/** write out the results to poly2
	 * 
	 * @throws Exception
	 */
	public void write() throws Exception
	{
		FeatureStore fs = (FeatureStore) ds.getFeatureSource("poly2");
		FeatureType ft = fs.getSchema();
		
		MemoryDataStore memorystore =new MemoryDataStore();
		
		Enumeration enum = finishedPolys.elements();
		while (enum.hasMoreElements())
		{
			Polygon p = (Polygon) enum.nextElement();
			Long polyid = ( (Long) p.getUserData());
			Object[] values = new Object[3];
			values[ft.find("module")] = MODULE;
			values[ft.find("the_geom")] = p;
			values[ft.find("polyid")] = polyid;
			Feature f = ft.create(values);
			memorystore.addFeature(f);
		}
		fs.addFeatures(memorystore.getFeatureReader("poly2"));
	}

	/**
	 *  build polygon and qa/qc it
	 * @throws Exception
	 * 
	 */
	private void process() throws Exception 
	{
	  Enumeration polys =	polyLineSet.keys();
	  int t=0;

	  while(polys.hasMoreElements())
	  {
	  	
	  	 Long polyid = (Long) polys.nextElement();
	  	 long polyid_long = polyid.longValue();
	  	 ArrayList lines = (ArrayList) polyLineSet.get(polyid);
	  	 if (lines.size() ==0 )
	  	 {
	  	 	throw new Exception("polygon has no edges");
	  	 }
	  	 	Polygonizer polyizer = new Polygonizer();
	  	 	polyizer.add(lines);
	  	 	Collection builtpolys = polyizer.getPolygons();
	  	 	
	  	 	if (polyizer.getCutEdges().size() != 0)
	  	    {
		  	 	throw new Exception("polygon has cut edges");
		  	}
	  	 	if (polyizer.getDangles().size() != 0)
	  	    {
		  	 	throw new Exception("polygon has dandgle edges");
		  	}
	  	 	if (polyizer.getInvalidRingLines().size() != 0)
	  	    {
		  	 	throw new Exception("polygon has invalid edges");
	  	 	//	System.out.println("poly has invalid edges "+polyid_long);
		  	}
	  	 	
	  	 	//validate the polygons
	  	 	Geometry finalPolygon = null;
	  	 	Iterator it = builtpolys.iterator();
	  	 	ArrayList seenPolyIDs = new ArrayList();
	  	 	while (it.hasNext())
	  	 	{
	  	 		Polygon p = (Polygon) it.next();
	  	 		//each polygon must contain exactly one PIP
	  	 		//each PIP must be contained by at most one polygon!
	  	 		
	  	 		long polyidPIP;
	  	 		
	  	 		try{
	  	 		
	  	 			polyidPIP = getPIP(p,polyid_long, 0);
	  	 		}
	  	 		catch(Exception e)
				{
	  	 			if (!allowHolesWithNoPIP)
	  	 			{
	  	 				throw e; // rethrow it -- stop processing
	  	 			}
	  	 			//otherwise, this might be okay. 
	  	 			polyidPIP = -666; // fake -- we pretend its a 'real' hole (ie. one with a pip in it)
	  	 			System.out.println("found a polygon without a PIP!  Try adding a PIP at "+p.getInteriorPoint() + " module = "+MODULE);
				}
//	  	 		if (seenPolyIDs.contains( new Long(polyidPIP) ))
//	  	 		{
//	  	 		 //throw new Exception( "poly has 2 pips - dont think we're looking for it!");
//	  	 			// this is actually okay - you can have holes-inside holes! or holes touching each other
//	  	 		}
//	  	 		seenPolyIDs.add ( new Long(polyidPIP) );
	  	 		if (polyidPIP == polyid_long)
	  	 		{
	  	 			if (finalPolygon != null )
	  	 			{
	  		 	   	  	 throw new Exception( "poly has 2 pips - that we're looking for!");
	  		 	    }
	  	 			//this is ours
	  	 			finalPolygon = p;
	  	 			finalPolygon.setUserData( polyid);
	  	 		}
	  	 	}
	  	 	if (finalPolygon == null)
	  	 	{
	  	 		throw new Exception("couldnt find a pip for a main polygon - "+polyid.longValue());
	  	 	}
			finishedPolys.put(polyid,finalPolygon);
			finalPolygon = null;
	  	 	t++;
	  	 	
	  }
		
	}

	/**
	 *   given a polyon, find the pip for it.
	 * @param p
	 * @param pointsTouchingOuterRightCountAsInside  NEVER CALL WITH THIS TRUE!
	 * @return
	 * @throws Exception
	 */
	private long getPIP(Polygon p,long mainPolyID, int pointsTouchingOuterRightCountAsInside) throws Exception
	{
		//pointsTouchingOuterRightCountAsInside -- I noticed that there are points in the dataset that are touching
		// the outside edge of a polygon.  This only happens for very small polygons - when then numerical precision of the
		// outside edge's points arent really big enough to discern a point inside.
		//  this happens when the area is <10E-11
		
		// cases -- one pip & its the mainPolyID --> okay (found poly)
		//          1+ pip and none of them are mainpolyID (its a whole)
		//          0 --> exception (no point in this poly)
	    List l =  PIP.query(p.getEnvelopeInternal());
	    Iterator it = l.iterator();
	    
	    boolean determinedThisIsMainPolygon = false;
	   
	    boolean found=false;
	    long foundID = 0;
  	 	while (it.hasNext())
  	 	{
  	 	   Point point = (Point) it.next();
  	 	   boolean inside = p.contains(point);
  	 	   if (pointsTouchingOuterRightCountAsInside ==1) 
  	 	   {
  	 	         inside |= p.touches(point);
  	 	   }
  	 	   if (pointsTouchingOuterRightCountAsInside ==2) 
  	 	   {
	 	         inside |= p.distance(point) < 1E-8;
	 	         System.out.println("distance = "+p.distance(point));
	 	   }
  	 	   if (inside)
  	 	   {
	 	   	  foundID = ( (Long) point.getUserData()).longValue();  //Polyid for this PIP.
	 	   	  if (determinedThisIsMainPolygon)
	 	   	  {
	 	   	  	//bad -- this is the 2nd pip in a "main polygon"
	 	     	throw new Exception( "found a 2nd PIP in a main polygon "+mainPolyID+" and "+foundID);
	 	   	  }
  	 	      determinedThisIsMainPolygon |= (foundID == mainPolyID);
  	 	      found = true;
  	 	   }
  	 	}
  	 	if (!found)
  	 	{
  	 		     if (p.getArea() < 5E-9)
  	 		     {
  	 		     	// its really small - we re-try allowing touching to mean inside
  	 		     	if (pointsTouchingOuterRightCountAsInside ==0) // dont recurse indefinatly!
  	 		     	{
  	 		     		System.out.println("small polygon with no PIP - retrying with pointsTouchingOuterRightCountAsInside=1 - "+mainPolyID +" area="+p.getArea());
  	 		     		return getPIP( p, mainPolyID, 1);
  	 		     	}
  	 		     	if (pointsTouchingOuterRightCountAsInside ==1) // dont recurse indefinatly!
  	 		     	{
  	 		         	System.out.println("small polygon with no PIP - retrying with pointsTouchingOuterRightCountAsInside=2 - "+mainPolyID +" area="+p.getArea());
	 		     		return getPIP( p, mainPolyID, 2);
  	 		     	}
  	 		     }
  	 		     
	 	   	  	 throw new Exception( "poly has 0 pips! area="+p.getArea());
	 	}
  	    return foundID;
	}
}
