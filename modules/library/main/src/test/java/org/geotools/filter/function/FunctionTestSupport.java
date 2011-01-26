/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Created on May 11, 2005, 9:02 PM
 */
package org.geotools.filter.function;

import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import junit.framework.TestCase;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 *
 * @author James
 * @source $URL$
 */
public class FunctionTestSupport extends TestCase {
    
    protected FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;
    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
    protected SimpleFeatureType dataType;
    protected SimpleFeature[] testFeatures;
    
    /** Creates a new instance of FunctionTestSupport */
    public FunctionTestSupport(String testName) {
        super(testName);
    }
    
    
    protected void setUp() throws java.lang.Exception {
        dataType = DataUtilities.createType("classification.test1",
                "id:0,foo:int,bar:double,geom:Point,group:String");
        
        int iVal[] = new int[]{4,90,20,43,29,61,8,12};
        double dVal[] = new double[]{2.5,80.433,24.5,9.75,18,53,43.2,16};
        
        testFeatures = new SimpleFeature[iVal.length];
        GeometryFactory fac=new GeometryFactory();
        
        for(int i=0; i< iVal.length; i++){
            testFeatures[i] = SimpleFeatureBuilder.build(dataType, new Object[] {
                        new Integer(i+1),
                        new Integer(iVal[i]),
                        new Double(dVal[i]),
                        fac.createPoint(new Coordinate( iVal[i], iVal[i])),
                        "Group"+(i%4)
            },"classification.t"+(i+1));
        }
        
        MemoryDataStore store = new MemoryDataStore();
        store.createSchema(dataType);
        store.addFeatures(testFeatures);
        
        featureCollection = store.getFeatureSource("test1").getFeatures();
    }
    
    public void testEmpty() {
        //to make tests pass
    }
}
