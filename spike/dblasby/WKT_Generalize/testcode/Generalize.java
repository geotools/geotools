/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package WKT_Generalize.testcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;


public class Generalize
{
	public static Geometry simplify(Geometry g,double tolerance) throws Exception
	{
		g = TopologyPreservingSimplifier.simplify(g,tolerance);
		if (!(g instanceof GeometryCollection))
		{

			if (g instanceof LineString)
			{			
				LineString gg[] = new LineString[1];
				gg[0] = (LineString)g;
				g = g.getFactory().createMultiLineString( (LineString[]) gg );
			}
			if (g instanceof Polygon)
			{
				Polygon gg[] = new Polygon[1];
				gg[0] = (Polygon)g;
				g = g.getFactory().createMultiPolygon( (Polygon[]) gg );
			}
			if (g instanceof Point)
			{
				Point gg[] = new Point[1];
				gg[0] = (Point)g;
				g = g.getFactory().createMultiPoint( (Point[]) gg );
			}
		}
		GeometryCollection gc = (GeometryCollection) g;
		ArrayList subs = new ArrayList();
		for (int t=0;t<gc.getNumGeometries();t++)
		{
			//replace each sub component with a simpler version or null 
			Geometry gsub = gc.getGeometryN(t);
			if (gsub instanceof Point)
			{
				subs.add( gsub ); //always keep
			} 
			else if (gsub instanceof LineString)
			{
				if (gsub.getLength() > tolerance)
					subs.add( gsub ); 
			}
			else if (gsub instanceof Polygon)
			{
				Geometry p = handlePolygon( (Polygon) gsub, tolerance);
				if (p != null)
					subs.add( p ); 
			}
			else
				throw new Exception("cannot handle type -"+gsub.getGeometryType());
		}
		
	    if (subs.size() ==0)
	    	return null;
	    if (subs.size() ==1)
	    {
	    	return (Geometry) subs.get(0);
	    }
		
	    return g.getFactory().buildGeometry(subs);
	}
	
	/**
	 * @param gsub
	 * @return
	 */
	private static Polygon handlePolygon(Polygon p,double tol) 
	{
		if  (p.getArea() < tol*tol)
			return null;
		
		//okay, kill any ring with area < tol*tol
		
		LinearRing lr =  (LinearRing) p.getExteriorRing();
		if (area(lr) <tol*tol)
			return null;
		
		//now, start with inner rings
		ArrayList holes = new ArrayList();
		for (int t=0;t<p.getNumInteriorRing();t++)
		{
			LinearRing lr_hole =  (LinearRing) p.getInteriorRingN(t);
			if (area(lr_hole) >tol*tol)
			{
				holes.add(lr_hole);
			}
		}
		
		return p.getFactory().createPolygon(lr,(LinearRing[])holes.toArray(new LinearRing[0])  );		
	}
	
	public static double area(LinearRing lr)
	{
		Polygon p = lr.getFactory().createPolygon(lr,null);
		return p.getArea();
	}

	public static void main(String[] args) 
	{
		try{
			double TOLERANCE = 180.0/50000.0; // (smallest size)/(max image width)
		System.out.println("TOLERANCe = "+TOLERANCE);
			BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
			String COLUMN = args[3];
			String TABLE = args[2];
	        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
	        WKTReader wktreader = new WKTReader();
	        reader.readLine();
	        reader.readLine();
	        boolean keep_going = true;
	        while (keep_going)
	        {
	        	String line = reader.readLine();
	        	if (line != null)
	        	{
	        		int indx = line.indexOf('|');
	        		
	        		if (indx !=-1)
	        		{
		        		String oid = line.substring(0,indx-1);
		        		String wkt = line.substring(indx+1);
		        		
		        		System.out.println("doing: "+oid + " (size="+line.length()+")");
		        		
		        		Geometry g = wktreader.read(wkt);
		        		
		        		//g = TopologyPreservingSimplifier.simplify(g,TOLERANCE);
		        		g= simplify(g,TOLERANCE);
		        		
		        		String wkt2;
		        		
		        		if (g==null)
		        			wkt2 = "NULL";
		        		else
		        		{
		        			wkt2 = g.toString();
		        			wkt2 = "'"+wkt2+"'::geometry";
		        		}
		        		
		        		
		        		String sql = "update "+TABLE+" set "+COLUMN+"= "+wkt2+"  WHERE oid="+oid+";\n";
		        		//String sql = oid +" | "+ wkt2 +"\n";
		        		//sql  = sql.replaceAll(", ",",");
		        		//sql  = sql.replaceAll(" \\(\\(","((");
		        		writer.write(sql);
		        	//System.out.println("done: "+oid);
	//System.out.println("diff in length ("+oid+") = "+Math.abs(sql.length() - line.length()));	   
	        		}
	        	}
	        	else
	        	{
	        		keep_going = false;
	        	}
	        }
	        writer.flush();
	        writer.close();
	        System.out.println("DONE!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	      
	}
}
