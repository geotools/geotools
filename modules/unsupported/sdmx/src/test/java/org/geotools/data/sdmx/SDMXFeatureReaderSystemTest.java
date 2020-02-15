/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sdmx;

import static org.junit.Assert.*;

import java.util.logging.Logger;
import org.geotools.data.Query;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class SDMXFeatureReaderSystemTest {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.arcgisrest");

    private SDMXDataStore dataStore;

    SDMXFeatureReader reader;
    SDMXDataflowFeatureSource dfSource;
    SDMXDimensionFeatureSource dimSource;
    SimpleFeatureType fType;

    @Test
    public void readFeaturesMeasureOldEndpoint() throws Exception {

        this.dataStore = (SDMXDataStore) Helper.createSDMXTestDataStore();
        Query query = new Query();

        this.fType = this.dataStore.getFeatureSource(Helper.ERP_LGA_DIMENSIONS).getSchema();
        this.dimSource =
                (SDMXDimensionFeatureSource)
                        this.dataStore.getFeatureSource(Helper.ERP_LGA_DIMENSIONS);
        this.dimSource.buildFeatureType();
        query.setFilter(ECQL.toFilter("CODE='AGE'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);
        assertTrue(this.reader.hasNext());

        query.setFilter(ECQL.toFilter("CODE='LGA_2016'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);
        assertTrue(this.reader.hasNext());

        this.fType = this.dataStore.getFeatureSource(Helper.T04_LGA).getSchema();
        this.dfSource = (SDMXDataflowFeatureSource) this.dataStore.getFeatureSource(Helper.T04_LGA);
        this.dfSource.buildFeatureType();
        query.setFilter(
                ECQL.toFilter(
                        "MEASURE = 1 and MSTP='TOT' and "
                                + "AGE='TOT' and "
                                + "STATE='1' and "
                                + "REGIONTYPE='STE' and "
                                + "REGION in ('1','2','3','4') and "
                                + "FREQUENCY='A'"));
        this.reader = (SDMXFeatureReader) this.dfSource.getReader(query);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat;
        int nObs = 0;
        while (this.reader.hasNext()) {
            feat = this.reader.next();
            assertNotNull(feat);
            if (nObs == 0) {
                assertNotNull(feat.getID());
                assertNull(feat.getDefaultGeometry());
                assertEquals("2001", feat.getAttribute(1));
                assertEquals(2468518.0, feat.getAttribute(2));
                assertEquals("TOT", feat.getAttribute(3));
                assertEquals("TOT", feat.getAttribute(4));
                assertEquals("1", feat.getAttribute(5));
                assertEquals("STE", feat.getAttribute(6));
                assertEquals("1", feat.getAttribute(7));
                assertEquals("A", feat.getAttribute(8));
            }
            String s =
                    feat.getID()
                            + "|"
                            + feat.getType().getGeometryDescriptor().getLocalName()
                            + ":"
                            + feat.getDefaultGeometry();
            for (int i = 1; i < feat.getAttributeCount(); i++) {
                s +=
                        "|"
                                + feat.getType().getDescriptor(i).getLocalName()
                                + ":"
                                + feat.getAttribute(i);
            }
            System.out.println(s);
            nObs++;
        }

        assertEquals(3, nObs);
    }

    @Test
    public void readFeaturesMeasureNewEndpoint() throws Exception {

        this.dataStore = (SDMXDataStore) Helper.createSDMXTestDataStore2();
        Query query = new Query();

        this.fType = this.dataStore.getFeatureSource(Helper.T04SA_DIMENSIONS).getSchema();
        this.dimSource =
                (SDMXDimensionFeatureSource)
                        this.dataStore.getFeatureSource(Helper.T04SA_DIMENSIONS);
        this.dimSource.buildFeatureType();
        query.setFilter(ECQL.toFilter("CODE='AGE'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);
        assertTrue(this.reader.hasNext());

        query.setFilter(ECQL.toFilter("CODE='SEX_ABS'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);
        assertTrue(this.reader.hasNext());

        this.fType = this.dataStore.getFeatureSource(Helper.KN_NIS).getSchema();
        this.dfSource = (SDMXDataflowFeatureSource) this.dataStore.getFeatureSource(Helper.T04SA);
        this.dfSource.buildFeatureType();
        query.setFilter(ECQL.toFilter("AGE='TT' and SEX_ABS='1'"));
        this.reader = (SDMXFeatureReader) this.dfSource.getReader(query);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat;
        int nObs = 0;
        while (this.reader.hasNext()) {
            feat = this.reader.next();
            assertNotNull(feat);
            if (nObs == 0) {
                assertNotNull(feat.getID());
                assertNull(feat.getDefaultGeometry());
                assertEquals("2016", feat.getAttribute(1));
            }
            String s =
                    feat.getID()
                            + "|"
                            + feat.getType().getGeometryDescriptor().getLocalName()
                            + ":"
                            + feat.getDefaultGeometry();
            for (int i = 1; i < feat.getAttributeCount(); i++) {
                s +=
                        "|"
                                + feat.getType().getDescriptor(i).getLocalName()
                                + ":"
                                + feat.getAttribute(i);
            }
            System.out.println(s);
            nObs++;
        }

        assertEquals(15151, nObs);
    }
}
