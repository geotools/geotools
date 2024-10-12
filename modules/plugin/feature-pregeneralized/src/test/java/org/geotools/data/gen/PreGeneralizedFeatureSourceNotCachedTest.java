/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;

import org.geotools.api.data.Query;
import org.geotools.api.data.Repository;
import org.geotools.api.data.Transaction;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class PreGeneralizedFeatureSourceNotCachedTest extends AbstractPreGeneralizedFeatureSourceTest {

    static final String ConfigName = "src/test/resources/geninfo_shapefile.xml";
    private Repository repository;

    @Override
    protected Repository getRepository() {
        repository = spy(new DSFinderRepository());
        return repository;
    }

    @Test
    public void getBoundsHitsRepositoryDataStore() {
        try {
            PreGeneralizedDataStore ds = getDataStore(ConfigName);
            PreGeneralizedFeatureSource fs = (PreGeneralizedFeatureSource) ds.getFeatureSource("GenStreams");

            fs.getBounds();
            Mockito.verify(repository, atLeastOnce()).dataStore(any());
            Mockito.clearInvocations(repository);

            fs.getBounds();
            Mockito.verify(repository, atLeastOnce()).dataStore(any());
            Mockito.clearInvocations(repository);

            Query query1 = new Query("GenStreams");
            fs.getCount(query1);
            Mockito.verify(repository, atLeastOnce()).dataStore(any());
            Mockito.clearInvocations(repository);

            Query query2 = new Query("GenStreams");
            query2.getHints().put(Hints.GEOMETRY_DISTANCE, 0.0);
            fs.getFeatures(query2);
            Mockito.verify(repository, atLeastOnce()).dataStore(any());
            Mockito.clearInvocations(repository);

            Query query3 = new Query("GenStreams");
            query3.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
            fs.getFeatures(query2);
            Mockito.verify(repository, atLeastOnce()).dataStore(any());
            Mockito.clearInvocations(repository);

            Query query4 = new Query("GenStreams");
            query4.getHints().put(Hints.GEOMETRY_DISTANCE, 5.0);
            fs.getFeatures(query3);
            Mockito.verify(repository, atLeastOnce()).dataStore(any());
            Mockito.clearInvocations(repository);

            Query query5 = new Query("GenStreams");
            query4.getHints().put(Hints.GEOMETRY_DISTANCE, 1.0);
            ds.getFeatureReader(query5, Transaction.AUTO_COMMIT);
            Mockito.verify(repository, atLeastOnce()).dataStore(any());
            Mockito.clearInvocations(repository);

            fs.getInfo();
            Mockito.verify(repository, atLeastOnce()).dataStore(any());
            Mockito.clearInvocations(repository);

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
