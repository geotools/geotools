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
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.GeoTools;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * How to Read a Shapefile using iterator.
 * <p>
 * Please visit the GeoTools User Guide: <a
 * href="http://docs.codehaus.org/display/GEOTDOC/04+How+to+Read+a+Shapefile">
 * 
 * @author Jody Garnett (Refractions Research)
 * 
 *
 * @source $URL$
 */
public class ShapefileRead2 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to GeoTools:" + GeoTools.getVersion());
		
		File file;
		if (args.length == 0){
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter( new FileFilter(){
                public boolean accept( File f ) {
                    return f.isDirectory() || f.getPath().endsWith("shp") || f.getPath().endsWith("SHP");
                }
                public String getDescription() {
                    return "Shapefiles";
                }
			    
			});
			int returnVal = chooser.showOpenDialog( null );
			
			if(returnVal != JFileChooser.APPROVE_OPTION) {
				System.exit(0);
			}
			System.out.println("You chose to open this file: " +
			chooser.getSelectedFile().getName());			
			file = chooser.getSelectedFile();
		}
		else {
			file = new File( args[0] );
		}
		if (!file.exists())
			System.exit(1);

		Map connect = new HashMap();
		connect.put("url", file.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(connect);
		String[] typeNames = dataStore.getTypeNames();
		String typeName = typeNames[0];

		System.out.println("Reading content " + typeName);

		SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
		SimpleFeatureCollection collection = featureSource.getFeatures();
		
		Iterator iterator = collection.iterator();
		int length = 0;		
		try {
			while (iterator.hasNext()) {
				SimpleFeature feature = (SimpleFeature) iterator.next();
				Geometry geometry = (Geometry) feature.getDefaultGeometry();
				
				length += geometry.getLength();
			}
		} finally {
			collection.close(iterator);
		}
		System.out.println("Total length " + length);
	}
}
