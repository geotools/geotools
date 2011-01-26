/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.gen;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import org.geotools.data.DataUtilities;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.gen.info.GeneralizationInfos;
import org.geotools.data.gen.info.GeneralizationInfosProvider;
import org.geotools.data.gen.info.GeneralizationInfosProviderImpl;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.junit.Assert;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

public class PreGeneralizedDataStoreTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestSetup.initialize();
    }

    public void testBaseFunctionallity() {

        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos ginfos = null;
        try {
            ginfos = provider.getGeneralizationInfos("src/test/resources/geninfo_only_base.xml");
            PreGeneralizedDataStore ds = new PreGeneralizedDataStore(ginfos, TestSetup.REPOSITORY);

            String typeName = ds.getTypeNames()[0];
            assertTrue("GenStreams".equals(typeName));
            Query query = new DefaultQuery(typeName);

            FeatureReader<SimpleFeatureType, SimpleFeature> reader = ds.getFeatureReader(query,
                    Transaction.AUTO_COMMIT);
            assertTrue(reader != null);
            reader.close();

            SimpleFeatureSource fsource = ds.getFeatureSource(typeName);
            assertTrue(fsource != null);

            fsource = ds.getFeatureSource(new NameImpl(typeName));
            assertTrue(fsource != null);

            ServiceInfo si = ds.getInfo();
            assertTrue(si != null);
            System.out.println(si);

            List<Name> names = ds.getNames();
            assertTrue(names.contains(new NameImpl(typeName)));
            assertTrue("GenStreams".equals(ds.getNames().get(0).getLocalPart()));

            fsource = DataUtilities.createView(ds, query);
            assertTrue(fsource != null);

            assertNotNull(ds.getSchema(typeName));
            assertNotNull(ds.getSchema(new NameImpl(typeName)));

            ds.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    public void testNotSupportedFeatures() {

        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos ginfos = null;
        PreGeneralizedDataStore ds = null;
        String typeName = null;
        try {
            ginfos = provider.getGeneralizationInfos("src/test/resources/geninfo_only_base.xml");
            ds = new PreGeneralizedDataStore(ginfos, TestSetup.REPOSITORY);
            typeName = ds.getTypeNames()[0];
        } catch (IOException ex) {
            ex.printStackTrace();
            Assert.fail();
        }

        boolean error = true;
        try {
            ds.getFeatureWriter(typeName, Transaction.AUTO_COMMIT);
        } catch (Throwable ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            ds.getFeatureWriter(typeName, null, Transaction.AUTO_COMMIT);
        } catch (Throwable ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            ds.getFeatureWriterAppend(typeName, Transaction.AUTO_COMMIT);
        } catch (Throwable ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            ds.getLockingManager();
        } catch (UnsupportedOperationException ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            ds.updateSchema("GenFeatures", null);
        } catch (Throwable ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            ds.updateSchema(new NameImpl("GenFeatures"), null);
        } catch (Throwable ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            ds.createSchema(null);
        } catch (Throwable ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

    }
}
