/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.process;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.process.vector.TransformProcess;

public class FeatureProcessExamples {

public void exampleTransformProcess(SimpleFeatureCollection featureCollection){
    // transform start
    String transform =
        "the_geom=the_geom\n"+
        "name=name\n"+
        "area=area( the_geom )";
    
    TransformProcess process = new TransformProcess();
    
    SimpleFeatureCollection features = process.execute( featureCollection, transform );
    // transform end
}
}
