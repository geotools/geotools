/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.example;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.swing.JMapFrame;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * This example also works against a local geoserver.
 * 
 * @author Jody Garnett
 *
 * @source $URL$
 */
public class SLDExample {

	public static void main(String args[]) {
		try {
			//supressInfo();
			localSLD();
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
	}
	public static void supressInfo(){
		org.geotools.util.logging.Logging.getLogger("org.geotools.gml").setLevel( Level.SEVERE );
		org.geotools.util.logging.Logging.getLogger("net.refractions.xml").setLevel( Level.SEVERE);
	}
	public static void localSLD() throws Exception {
		SimpleFeatureSource source = demoFeatureSource();
		Style style = demoStyle( source.getSchema().getTypeName() );

		show( source, style );
	}
	static SimpleFeatureSource demoFeatureSource() throws Exception {
		String getCapabilities =
			"http://localhost:8080/geoserver/wfs?service=WFS&request=GetCapabilities";
		
		Map connectionParameters = new HashMap();
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );
		
		DataStore data = DataStoreFinder.getDataStore( connectionParameters );
		String typeName = data.getTypeNames()[0];
		return data.getFeatureSource( typeName );		
	}
	
	static Style demoStyle(String typeName) throws Exception {
		StyleFactory sf = CommonFactoryFinder.getStyleFactory( GeoTools.getDefaultHints() );
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( GeoTools.getDefaultHints() );
		
		Stroke stroke = sf.createStroke(
			ff.literal("#FF0000"),
			ff.literal(2));
		
		LineSymbolizer lineSymbolizer = sf.createLineSymbolizer();		
		lineSymbolizer.setStroke( stroke );
		
		Rule rule = sf.createRule();
		rule.setFilter( Filter.INCLUDE );
		rule.setSymbolizers( new Symbolizer[]{ lineSymbolizer });
		
		FeatureTypeStyle type = sf.createFeatureTypeStyle();
		type.setFeatureTypeName(typeName);
		type.addRule( rule );
		
		Style style = sf.createStyle();
		style.addFeatureTypeStyle(type);
		
		return style;
	}

    public static void show(SimpleFeatureSource source, Style style) throws Exception {
		    MapContext context = new DefaultMapContext( DefaultGeographicCRS.WGS84 );
		    context.setAreaOfInterest(source.getBounds(), DefaultGeographicCRS.WGS84 );
		    context.addLayer( source, style );
            context.setTitle("FOSS4G");

		    GTRenderer renderer = new StreamingRenderer();
		    HashMap hints = new HashMap();
		    hints.put("memoryPreloadingEnabled", Boolean.TRUE);
		    renderer.setRendererHints( hints );

            JMapFrame frame = new JMapFrame(context, renderer);
            frame.enableToolBar(true);
		    frame.setVisible(true);
	}
}
