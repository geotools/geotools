/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.GeoTools;
import org.geotools.swing.ProgressWindow;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;

import com.vividsolutions.jts.geom.Geometry;

/**
 * How to Read a Shapefile.
 * <p>
 * Please visit the GeoTools User Guide: <a
 * href="http://docs.codehaus.org/display/GEOTDOC/04+How+to+Read+a+Shapefile">
 * 
 * @author Jody Garnett (Refractions Research)
 * 
 *
 * @source $URL$
 */
public class ShapefileRead {

	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to GeoTools:" + GeoTools.getVersion());
		
		File file;
		if (args.length == 0){
		    file = JFileDataStoreChooser.showOpenFile("shp", null);
		}
		else {
			file = new File( args[0] );
		}
		if (file == null || !file.exists()){
			System.exit(1);
		}
		
		Map<String,Object> connect = new HashMap<String,Object>();
		connect.put("url", file.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(connect);
		String[] typeNames = dataStore.getTypeNames();
		String typeName = typeNames[0];

		System.out.println("Reading content " + typeName);

		SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
		SimpleFeatureCollection collection = featureSource.getFeatures();
		
		class DistanceVisitor implements FeatureVisitor {
			int length =0;
			public void visit(Feature feature) {
				Geometry geometry = (Geometry) feature.getDefaultGeometryProperty().getValue();
				length += geometry.getLength();
			}
		}

		DistanceVisitor distance = new DistanceVisitor();
		
		collection.accepts( distance, new ProgressWindow(null));
		System.out.println("Total length " + distance.length );
	}
}
