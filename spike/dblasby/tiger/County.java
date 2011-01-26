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
package tigerOtherPoly;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterType;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LikeFilter;
import org.geotools.filter.LogicFilter;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.strtree.SIRtree;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.operation.linemerge.LineMerger;
import com.vividsolutions.jts.operation.polygonize.Polygonizer;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

/**
 * 
 CREATE TABLE county_boundary AS
  SELECT * FROM completechain
  WHERE
     statel isnull or stater isnull or countyl isnull or countyr isnull
     or  (   (statel||'-'||countyl) != (stater||'-'||countyr) );

 
  alter table county_boundary add primary key (module,tlid);
   
            insert into geometry_columns values ('','','county_boundary','wkb_geometry',2,1,'GEOMETRY');

     
     
     create index county_boundary_indx1 on county_boundary (statel,countyl);
     create index county_boundary_indx2 on county_boundary (stater,countyr);
     vacuum analyse county_boundary;
     
 * @author david blasby
 */
public class County
{
    String MODULE = "";
    
    String state = "";
    String county ="";
    
    
    double tolerance1 = 0.00005; // 50% reduction
    double tolerance2 = 0.00025; // 75% reduction
    double tolerance3 = 0.00080; //87% reduction
    
    ArrayList lines = new ArrayList();
    DataStore ds = null;
	Collection resultPolygon =null;
	
	/**
	 * reads the completechain dataset
	 * @throws Exception
	 */
	public void getrows() throws Exception
	{
		
   //SELECT ... FROM ... WHERE
   //   (statel=state and countyl=county) OR (stater=state and countyr=county ) 
			FilterFactory ff = FilterFactory.createFilterFactory();

		
			CompareFilter cf1 = ff.createCompareFilter(FilterType.COMPARE_EQUALS);			   
			cf1.addLeftValue( ff.createAttributeExpression( null, "statel") );
			cf1.addRightValue( ff.createLiteralExpression(state));
			
			CompareFilter cf2 = ff.createCompareFilter(FilterType.COMPARE_EQUALS);			   
			cf2.addLeftValue( ff.createAttributeExpression( null, "countyl") );
			cf2.addRightValue( ff.createLiteralExpression(county));
			
			LogicFilter and1 = ff.createLogicFilter(cf1,cf2,Filter.LOGIC_AND);
			
			
			CompareFilter cf3 = ff.createCompareFilter(FilterType.COMPARE_EQUALS);			   
			cf3.addLeftValue( ff.createAttributeExpression( null, "stater") );
			cf3.addRightValue( ff.createLiteralExpression(state));
			
			CompareFilter cf4 = ff.createCompareFilter(FilterType.COMPARE_EQUALS);			   
			cf4.addLeftValue( ff.createAttributeExpression( null, "countyr") );
			cf4.addRightValue( ff.createLiteralExpression(county));
			
			LogicFilter and2 = ff.createLogicFilter(cf3,cf4,Filter.LOGIC_AND);
			
			LogicFilter or = ff.createLogicFilter(and1,and2,Filter.LOGIC_OR);
			
		

			
			String[] ps = new String []{"wkb_geometry","statel","countyl","stater","countyr"};
			Query q = new DefaultQuery("county_boundary",or,ps);	     	
			 
			 	
			FeatureReader fr = ds.getFeatureReader(q, new DefaultTransaction() );
 	
		    while (fr.hasNext())
		    {
		    	Feature f= fr.next();
		    	if (!alreadyThere(f.getDefaultGeometry()))
		    			lines.add(f.getDefaultGeometry());
		    }
		    fr.close();
}
	
 private void parseModule() throws Exception
 {
    Pattern p = Pattern.compile("TGR([0123456789][0123456789])([0123456789][0123456789][0123456789])");  //  \\z --> $ (end of line)
    Matcher m = p.matcher(MODULE);
    if ( m.matches())
    {
    	state = m.group(1);
    	county = m.group(2);
    }	    
    else 
    	throw new Exception("cannot parse module:"+MODULE);
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
			System.out.println("usage: \"host=myHost port=myPort  dbname=myDB user=myUser password=myPassword\" <module name> ");
			System.out.println("dont forget to:");
			System.out.println("1. put quotes around the postgresql connection string");
			System.out.println("2. pre-create the output database table:");
			return;
		}
			
		County THIS = new County();
		
		
	    PostgisDataStoreFactory  pgdsf = new PostgisDataStoreFactory();
	
	     try{
	     	
	     	
	     	Map param =  parsePG(args[0]);
	    	     
	        param.put("wkb enabled","true");
	        param.put("loose bbox","true");
	        param.put("dbtype","postgis");
	        
          
            
	     	THIS.ds = pgdsf.createDataStore(param);
	     	
	        THIS.MODULE = args[1];
	        THIS.parseModule();
	     	
	     	System.out.println("start state = "+THIS.state+" county="+THIS.county);
	     	  
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
	     	System.out.println("		create table poly_county (");
	     	System.out.println("               module text, ");
	   		System.out.println("               gen_full geometry,");
	   		System.out.println("               gen_1 geometry,");
	   		System.out.println("               gen_2 geometry,");
	   		System.out.println("               gen_3 geometry) with oids;");
			System.out.println("insert into geometry_columns values ('','','poly_county','gen_full',2,1,'GEOMETRY');");
			System.out.println("insert into geometry_columns values ('','','poly_county','gen_1',2,1,'GEOMETRY');");
			System.out.println("insert into geometry_columns values ('','','poly_county','gen_2',2,1,'GEOMETRY');");
			System.out.println("insert into geometry_columns values ('','','poly_county','gen_3',2,1,'GEOMETRY');");
		 }
	     catch (Exception e)
		 {
	     	e.printStackTrace();
		 }
	}

	private Geometry generalize(Geometry g, double tolerance)
	{
		return TopologyPreservingSimplifier.simplify(g,tolerance);
	}

	/**
	 * 
	 */ 
	private void save() throws Exception
	{
		FeatureStore fs = (FeatureStore) ds.getFeatureSource("poly_county");
		FeatureType ft = fs.getSchema();
		
		MemoryDataStore memorystore =new MemoryDataStore();
		
		ArrayList polys = new ArrayList(resultPolygon);
		
		Geometry gfinal = null;
		if (polys.size() == 1)
		{
			gfinal = (Polygon) polys.get(0);   //POLYGON
		}
		else
		{
			GeometryFactory gf = ((Polygon) polys.get(0)).getFactory();
			gfinal = new MultiPolygon((Polygon[]) polys.toArray( new Polygon[polys.size()]),   gf    );
		}
		
		gfinal = gfinal.buffer(0); // for topologic problems.
		
			
			Object[] values = new Object[5];
			values[ft.find("module")] = MODULE;
			values[ft.find("gen_full")] = gfinal;
			
			values[ft.find("gen_1")] = generalize(gfinal,tolerance1);;
			values[ft.find("gen_2")] = generalize(gfinal,tolerance1);;
			values[ft.find("gen_3")] = generalize(gfinal,tolerance1);;
			
			Feature f = ft.create(values);
			memorystore.addFeature(f);
		
		fs.addFeatures(memorystore.getFeatureReader("poly_county"));
		
	}

	/**
	 * 
	 */
	private void process() throws Exception
	{
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
	  	 	
	  	 	resultPolygon = builtpolys;
		
	}

	
	private boolean alreadyThere(Geometry g)
	{
		Iterator it = lines.iterator();
		while (it.hasNext())
		{
			Geometry lineg = (Geometry) it.next();
			if (lineg.equalsExact(g))
				return true;
			else if (equalsExactBackwards(lineg,g))
				return true;
				
		}
		return false;
	}
	
	/**
	 * 	reverse direction of points in a line
	 */
	LineString reverse(LineString l)
	{
		List clist = Arrays.asList(l.getCoordinates() );
		Collections.reverse( clist );
		return l.getFactory().createLineString( (Coordinate[]) clist.toArray(new Coordinate[1] ) );		
	}
	

	/**
	 * @param lineg
	 * @param g
	 * @return
	 */
	private boolean equalsExactBackwards(Geometry lineg, Geometry g) 
	{
		if (lineg.getNumPoints() != g.getNumPoints())
			return false;
		return reverse( (LineString) g).equalsExact(lineg);
		
	}

	
}
