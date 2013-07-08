/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.catalog;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class CatalogSliceTest extends Assert{
    
    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory2();    
    
    final static PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING);
    
    final static GeometryFactory GEOM_FACTORY = new GeometryFactory(PRECISION_MODEL);
    
    @Test
    public void createTest() throws Exception{
        // connect to test catalog
        final File parentLocation = new File(TestData.file(this, "."),"db");
        if(parentLocation.exists()){
            FileUtils.deleteDirectory(parentLocation);
        }
        assertTrue(parentLocation.mkdir());
        final String databaseName="test";
        CoverageSlicesCatalog sliceCat=null;
        final Transaction t= new DefaultTransaction(Long.toString(System.nanoTime()));
        try{
            sliceCat= new CoverageSlicesCatalog(databaseName, parentLocation);
            String[] typeNames = sliceCat.getTypeNames();
            assertNull(typeNames);
            
            // create new schema 1
            final String schemaDef1="the_geom:Polygon,coverage:String,imageindex:Integer,cloud_formations:Integer";
            sliceCat.createType("1", schemaDef1);
            typeNames = sliceCat.getTypeNames();
            assertNotNull(typeNames);
            assertEquals(1,typeNames.length);
            
            // add features to it
            SimpleFeatureType schema = DataUtilities.createType("1", schemaDef1);
            SimpleFeature feat = DataUtilities.template(schema);
            feat.setAttribute("coverage", "a");
            feat.setAttribute("imageindex", Integer.valueOf(0));
            feat.setAttribute("cloud_formations", Integer.valueOf(3));
            ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);
            feat.setAttribute("the_geom", GEOM_FACTORY.toGeometry(referencedEnvelope));
            sliceCat.addGranule("1", feat, t);
            
            feat = DataUtilities.template(schema);
            feat.setAttribute("coverage", "a");
            feat.setAttribute("imageindex", Integer.valueOf(1));
            feat.setAttribute("cloud_formations", Integer.valueOf(2));
            feat.setAttribute("the_geom", GEOM_FACTORY.toGeometry(referencedEnvelope));
            sliceCat.addGranule("1", feat, t);
            t.commit();
            
            // read back
            CountVisitor cv= new CountVisitor();
            Query q = new Query("1");
            q.setFilter(Filter.INCLUDE);
            sliceCat.computeAggregateFunction(q, cv);
            assertEquals(2, cv.getCount());
            
            
            // create new schema 2
            final String schemaDef2="the_geom:Polygon,coverage:String,imageindex:Integer,new:Double";
            sliceCat.createType("2", schemaDef1);
            typeNames = sliceCat.getTypeNames();
            assertNotNull(typeNames);
            assertEquals(2,typeNames.length);
            
            // add features to it
            schema = DataUtilities.createType("2", schemaDef2);
            feat = DataUtilities.template(schema);
            feat.setAttribute("coverage", "b");
            feat.setAttribute("imageindex", Integer.valueOf(0));
            feat.setAttribute("new", Double.valueOf(3.22));
            feat.setAttribute("the_geom", GEOM_FACTORY.toGeometry(referencedEnvelope));
            sliceCat.addGranule("2", feat, t);
            
            feat = DataUtilities.template(schema);
            feat.setAttribute("coverage", "b");
            feat.setAttribute("imageindex", Integer.valueOf(1));
            feat.setAttribute("new", Double.valueOf(1.12));
            feat.setAttribute("the_geom", GEOM_FACTORY.toGeometry(referencedEnvelope));
            sliceCat.addGranule("2", feat, t);
            
            feat = DataUtilities.template(schema);
            feat.setAttribute("coverage", "b");
            feat.setAttribute("imageindex", Integer.valueOf(2));
            feat.setAttribute("new", Double.valueOf(1.32));
            feat.setAttribute("the_geom", GEOM_FACTORY.toGeometry(referencedEnvelope));
            sliceCat.addGranule("2", feat, t);
            
            t.commit();
            
            // read back
            cv= new CountVisitor();
            q = new Query("2");
            q.setFilter(Filter.INCLUDE);
            sliceCat.computeAggregateFunction(q, cv);
            assertEquals(3, cv.getCount());
            
            
            // remove
            sliceCat.removeGranules("1", Filter.INCLUDE, t);
            sliceCat.removeGranules("2", Filter.INCLUDE, t);
            t.commit();
            // check
            q = new Query("1");
            q.setFilter(Filter.INCLUDE);
            sliceCat.computeAggregateFunction(q, cv);
            assertEquals(0, cv.getCount());
            q = new Query("2");
            sliceCat.computeAggregateFunction(q, cv);
            assertEquals(0, cv.getCount());
            
        }catch (Exception e) {
            e.printStackTrace();
            t.rollback();
        }finally{
            if(sliceCat!=null){
                sliceCat.dispose();
            }
            
            t.close();
            
            FileUtils.deleteDirectory(parentLocation);
        }

    }
    
    @Test
    public void basicConnectionTest() throws Exception{
        // connect to test catalog
        final Map<String, Serializable> params= new HashMap<String ,Serializable>();
        params.put("ScanTypeNames", Boolean.valueOf(true));
        // H2 database URLs must not be percent-encoded: see GEOT-4504
        final URL url = new URL("file:"
                + DataUtilities.urlToFile(TestData.url(this,
                        ".IASI_C_EUMP_20121120062959_31590_eps_o_l2")));
        params.put("ParentLocation", url);
        params.put("database", url+"/IASI_C_EUMP_20121120062959_31590_eps_o_l2");
        params.put("dbtype", "h2");
        params.put("user", "geotools");
        params.put("passwd", "geotools");
        
        assertTrue(CoverageSlicesCatalog.INTERNAL_STORE_SPI.canProcess(params));
        DataStore ds = null;
        SimpleFeatureIterator it =null;        
        try{
            // create the store
            ds=CoverageSlicesCatalog.INTERNAL_STORE_SPI.createDataStore(params);
            assertNotNull(ds);
            
            // load typenames
            final String[] typeNames = ds.getTypeNames();
            assertNotNull(typeNames);
            assertEquals(20,typeNames.length);
            assertEquals("atmospheric_water_vapor", typeNames[2]);
            assertEquals("cloud_top_temperature", typeNames[5]);
            assertEquals("integrated_ozone", typeNames[11]);
            
            
            // work with surface emissivity
            final SimpleFeatureSource fs = ds.getFeatureSource("surface_emissivity");
            assertNotNull(fs);
            final SimpleFeatureType schema = fs.getSchema();
            assertNotNull(schema);
            
            // queries
            // simple
            assertTrue(fs.getCount(Query.ALL)>0);
            
            // complex
            final Query q= new Query();
            q.setTypeName("surface_emissivity");
            q.setFilter(ff.greaterOrEqual(ff.property("new"), ff.literal(0)));
            assertTrue(fs.getCount(q)>0);
            
            final SimpleFeatureCollection fc = fs.getFeatures(q);
            assertFalse(fc.isEmpty());
            it = fc.features();
            while(it.hasNext()){
                final SimpleFeature feat = it.next();
                System.out.println(feat.getAttribute("new"));
                assertTrue((Integer)feat.getAttribute("new")>=0);
                
            }
            
        }finally{
            if(ds!=null){
                ds.dispose();
            }
            
            if(it!=null){
                it.close();
            }
        }

    }

}
