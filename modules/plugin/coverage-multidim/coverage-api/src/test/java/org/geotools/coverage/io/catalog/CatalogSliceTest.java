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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.h2.H2DataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/** @author Simone Giannecchini, GeoSolutions SAS */
public class CatalogSliceTest extends Assert {

    private H2DataStoreFactory INTERNAL_STORE_SPI = new H2DataStoreFactory();

    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory2();

    static final PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING);

    static final GeometryFactory GEOM_FACTORY = new GeometryFactory(PRECISION_MODEL);

    /** Default Logger * */
    private static final Logger LOGGER = Logging.getLogger(CatalogSliceTest.class);

    private static final double DELTA = 0.01d;

    @Test
    public void createTest() throws Exception {
        // connect to test catalog
        final File parentLocation = new File(TestData.file(this, "."), "db");
        if (parentLocation.exists()) {
            FileUtils.deleteDirectory(parentLocation);
        }
        assertTrue(parentLocation.mkdir());
        final String databaseName = "test";
        CoverageSlicesCatalog sliceCat = null;
        final Transaction t = new DefaultTransaction(Long.toString(System.nanoTime()));
        try {
            sliceCat = new CoverageSlicesCatalog(databaseName, parentLocation);
            String[] typeNames = sliceCat.getTypeNames();
            assertNull(typeNames);

            // create new schema 1
            final String schemaDef1 =
                    "the_geom:Polygon,coverage:String,imageindex:Integer,cloud_formations:Integer";
            sliceCat.createType("1", schemaDef1);
            typeNames = sliceCat.getTypeNames();
            assertNotNull(typeNames);
            assertEquals(1, typeNames.length);

            // add features to it
            SimpleFeatureType schema = DataUtilities.createType("1", schemaDef1);
            SimpleFeature feat = DataUtilities.template(schema);
            feat.setAttribute("coverage", "a");
            feat.setAttribute("imageindex", Integer.valueOf(0));
            feat.setAttribute("cloud_formations", Integer.valueOf(3));
            ReferencedEnvelope referencedEnvelope =
                    new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);
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
            CountVisitor cv = new CountVisitor();
            Query q = new Query("1");
            q.setFilter(Filter.INCLUDE);
            sliceCat.computeAggregateFunction(q, cv);
            assertEquals(2, cv.getCount());

            // create new schema 2
            final String schemaDef2 =
                    "the_geom:Polygon,coverage:String,imageindex:Integer,new:Double";
            sliceCat.createType("2", schemaDef2);
            typeNames = sliceCat.getTypeNames();
            assertNotNull(typeNames);
            assertEquals(2, typeNames.length);

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
            cv = new CountVisitor();
            q = new Query("2");
            q.setFilter(Filter.INCLUDE);
            sliceCat.computeAggregateFunction(q, cv);
            assertEquals(3, cv.getCount());

            // Get the CoverageSlices
            List<CoverageSlice> slices = sliceCat.getGranules(q);
            double[] news = new double[] {3.22, 1.12, 1.32};
            for (int i = 0; i < news.length; i++) {
                CoverageSlice slice = slices.get(i);
                assertTrue(slice.getGranuleBBOX().contains(referencedEnvelope));
                double newAttr = (double) slice.getOriginator().getAttribute("new");
                assertEquals(newAttr, news[i], DELTA);
            }

            // Creating a CoverageSliceCatalogSource and check if it behaves correctly
            CoverageSlicesCatalogSource src = new CoverageSlicesCatalogSource(sliceCat, "2");
            assertEquals(3, src.getCount(q));
            SimpleFeatureCollection coll = src.getGranules(q);
            cv.reset();
            coll.accepts(cv, null);
            assertEquals(3, cv.getCount());
            assertTrue(
                    src.getBounds(q)
                            .contains(
                                    referencedEnvelope.toBounds(
                                            referencedEnvelope.getCoordinateReferenceSystem())));
            assertEquals(src.getSchema(), schema);

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

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            t.rollback();
        } finally {
            if (sliceCat != null) {
                sliceCat.dispose();
            }

            t.close();

            FileUtils.deleteDirectory(parentLocation);
        }
    }

    @Test
    public void propertyNamesQuery() throws Exception {
        // connect to test catalog
        final File parentLocation = new File(TestData.file(this, "."), "db2");
        if (parentLocation.exists()) {
            FileUtils.deleteDirectory(parentLocation);
        }
        assertTrue(parentLocation.mkdir());
        final String databaseName = "test";
        CoverageSlicesCatalog sliceCat = null;
        final Transaction t = new DefaultTransaction(Long.toString(System.nanoTime()));
        try {
            sliceCat = new CoverageSlicesCatalog(databaseName, parentLocation);
            String[] typeNames = sliceCat.getTypeNames();
            assertNull(typeNames);

            // create new schema 1
            final String schemaDef1 =
                    "the_geom:Polygon,coverage:String,imageindex:Integer,cloud_formations:Integer";
            sliceCat.createType("test", schemaDef1);
            typeNames = sliceCat.getTypeNames();
            assertNotNull(typeNames);
            assertEquals(1, typeNames.length);

            ReferencedEnvelope referencedEnvelope =
                    new ReferencedEnvelope(-180, 180, -90, 90, DefaultGeographicCRS.WGS84);

            // add features to it
            SimpleFeatureType schema = DataUtilities.createType("test", schemaDef1);

            final int numGranules = 2;
            for (int i = 0; i < numGranules; i++) {
                SimpleFeature feat = DataUtilities.template(schema);
                feat.setAttribute("coverage", "a");
                feat.setAttribute("imageindex", Integer.valueOf(i));
                feat.setAttribute("cloud_formations", Integer.valueOf(i + 1));
                feat.setAttribute("the_geom", GEOM_FACTORY.toGeometry(referencedEnvelope));
                sliceCat.addGranule("test", feat, t);
            }
            t.commit();

            // read back with property names filtering
            Query q = new Query();
            q.setTypeName(typeNames[0]);
            String[] propertyNames = new String[] {"cloud_formations"};
            q.setPropertyNames(propertyNames);
            List<CoverageSlice> granules = sliceCat.getGranules(q);

            assertNotNull(granules);
            assertEquals(numGranules, granules.size());

            for (int i = 0; i < numGranules; i++) {
                CoverageSlice slice = granules.get(i);
                SimpleFeature feature = slice.getOriginator();
                List<AttributeDescriptor> attributes =
                        feature.getFeatureType().getAttributeDescriptors();

                // Check we are only getting the cloud_formations attribute due to propertyNames
                assertEquals(1, attributes.size());
                assertEquals("cloud_formations", attributes.get(0).getLocalName());

                int cloud = (int) feature.getAttribute("cloud_formations");
                assertEquals(cloud, i + 1);
                // Make sure the imageindex is not returned since we have excluded it
                // via propertyNames
                assertNull(feature.getAttribute("imageindex"));
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            t.rollback();
        } finally {
            if (sliceCat != null) {
                sliceCat.dispose();
            }

            t.close();

            FileUtils.deleteDirectory(parentLocation);
        }
    }

    @Test
    public void basicConnectionTest() throws Exception {
        // connect to test catalog
        final Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("ScanTypeNames", Boolean.valueOf(true));
        // H2 database URLs must not be percent-encoded: see GEOT-4504
        final URL url =
                new URL(
                        "file:"
                                + URLs.urlToFile(
                                        TestData.url(
                                                this,
                                                ".IASI_C_EUMP_20121120062959_31590_eps_o_l2")));
        params.put("ParentLocation", url);
        params.put("database", url + "/IASI_C_EUMP_20121120062959_31590_eps_o_l2");
        params.put("dbtype", "h2");
        params.put("user", "geotools");
        params.put("passwd", "geotools");

        assertTrue(INTERNAL_STORE_SPI.canProcess(params));
        DataStore ds = null;
        SimpleFeatureIterator it = null;
        try {
            // create the store
            ds = INTERNAL_STORE_SPI.createDataStore(params);
            assertNotNull(ds);

            // load typenames
            final String[] typeNames = ds.getTypeNames();
            assertNotNull(typeNames);
            assertEquals(20, typeNames.length);
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
            assertTrue(fs.getCount(Query.ALL) > 0);

            // complex
            final Query q = new Query();
            q.setTypeName("surface_emissivity");
            q.setFilter(ff.greaterOrEqual(ff.property("new"), ff.literal(0)));
            assertTrue(fs.getCount(q) > 0);

            final SimpleFeatureCollection fc = fs.getFeatures(q);
            assertFalse(fc.isEmpty());
            it = fc.features();
            while (it.hasNext()) {
                final SimpleFeature feat = it.next();

                assertTrue((Integer) feat.getAttribute("new") >= 0);
            }

        } finally {
            if (ds != null) {
                ds.dispose();
            }

            if (it != null) {
                it.close();
            }
        }
    }
}
