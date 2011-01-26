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
import java.util.Collection;

import junit.framework.TestCase;

import org.geotools.data.gen.info.GeneralizationInfos;
import org.geotools.data.gen.info.GeneralizationInfosProvider;
import org.geotools.data.gen.info.GeneralizationInfosProviderImpl;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.junit.Assert;
import org.opengis.feature.simple.SimpleFeature;

public class PreGeneralizedFeatureCollectionTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestSetup.initialize();
    }

    public void testNotSupportedFeatures() {

        GeneralizationInfosProvider provider = new GeneralizationInfosProviderImpl();
        GeneralizationInfos ginfos = null;
        PreGeneralizedDataStore ds = null;
        SimpleFeatureCollection fCollection = null;
        String typeName = null;
        try {
            ginfos = provider.getGeneralizationInfos("src/test/resources/geninfo_only_base.xml");
            ds = new PreGeneralizedDataStore(ginfos, TestSetup.REPOSITORY);
            typeName = ds.getTypeNames()[0];
            fCollection = ds.getFeatureSource(typeName).getFeatures();
        } catch (IOException ex) {
            ex.printStackTrace();
            Assert.fail();
        }

        boolean error = true;
        try {
            fCollection.add(null);
        } catch (UnsupportedOperationException ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            fCollection.addAll((Collection<SimpleFeature>) null);
        } catch (UnsupportedOperationException ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            fCollection.addAll((SimpleFeatureCollection) null);
        } catch (UnsupportedOperationException ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            fCollection.clear();
        } catch (UnsupportedOperationException ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        // purge is depricated
        // error = true;
        // try {
        // fCollection.purge();
        // } catch (UnsupportedOperationException ex) {
        // error = false;
        // }
        // if (error) {
        // Assert.fail();
        // }

        error = true;
        try {
            fCollection.remove(null);
        } catch (UnsupportedOperationException ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            fCollection.removeAll(null);
        } catch (UnsupportedOperationException ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

        error = true;
        try {
            fCollection.retainAll(null);
        } catch (UnsupportedOperationException ex) {
            error = false;
        }
        if (error) {
            Assert.fail();
        }

    }
}
