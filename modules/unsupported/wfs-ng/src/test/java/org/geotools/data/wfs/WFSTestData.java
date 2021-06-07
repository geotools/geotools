/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.data.wfs.internal.WFSConfig;

/** */
public class WFSTestData {

    /** A class holding the type name and test data location for a feature type */
    public static class TestDataType {
        /** Location of a test data capabilities file */
        public final URL CAPABILITIES;

        /** Location of a test DescribeFeatureType response for the given feature type */
        public final URL SCHEMA;

        /** The type name, including namespace */
        public final QName TYPENAME;

        /** The type name as referred in the capabilities (ej, "topp:states") */
        public final String FEATURETYPENAME;

        /** The FeatureType CRS as declared in the capabilities */
        public final String CRS;

        /** Location of a sample GetFeature response for this feature type */
        public final URL DATA;

        /**
         * @param folder the folder name under {@code test-data} where the test files for this
         *     feature type are stored
         * @param qName the qualified type name (ns + local name)
         * @param featureTypeName the name as stated in the capabilities
         * @param crs the default feature type CRS as stated in the capabilities
         */
        public TestDataType(
                final String folder,
                final QName qName,
                final String featureTypeName,
                final String crs) {

            TYPENAME = qName;
            FEATURETYPENAME = featureTypeName;
            CRS = crs;
            CAPABILITIES = url(folder + "/GetCapabilities.xml");
            SCHEMA = url(folder + "/DescribeFeatureType_" + qName.getLocalPart() + ".xsd");
            DATA = url(folder + "/GetFeature_" + qName.getLocalPart() + ".xml");
        }
    }

    public static final TestDataType GEOS_ARCHSITES_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.openplans.org/spearfish", "archsites"),
                    "sf_archsites",
                    "EPSG:26713");

    public static final TestDataType GEOS_POI_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.census.gov", "poi"),
                    "tiger_poi",
                    "EPSG:4326");

    public static final TestDataType GEOS_POI_10 =
            new TestDataType(
                    "GeoServer_1.7.x/1.0.0",
                    new QName("http://www.census.gov", "poi"),
                    "tiger_poi",
                    "EPSG:4326");

    public static final TestDataType GEOS_ROADS_20 =
            new TestDataType(
                    "GeoServer_2.2.x/2.0.0",
                    new QName("http://www.openplans.org/spearfish", "roads"),
                    "sf_roads",
                    "EPSG:26713");

    public static final TestDataType GEOS_ROADS_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.openplans.org/spearfish", "roads"),
                    "sf_roads",
                    "EPSG:26713");

    public static final TestDataType GEOS_ROADS_10 =
            new TestDataType(
                    "GeoServer_1.7.x/1.0.0",
                    new QName("http://www.openplans.org/spearfish", "roads"),
                    "sf_roads",
                    "EPSG:26713");

    public static final TestDataType GEOS_STATES_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.openplans.org/topp", "states"),
                    "topp_states",
                    "EPSG:4326");

    public static final TestDataType GEOS_STATES_10 =
            new TestDataType(
                    "GeoServer_1.7.x/1.0.0",
                    new QName("http://www.openplans.org/topp", "states"),
                    "topp_states",
                    "EPSG:4326");

    public static final TestDataType GEOS_TASMANIA_CITIES_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.openplans.org/topp", "tasmania_cities"),
                    "topp:tasmania_cities",
                    "EPSG:4326");

    public static final TestDataType GEOS_TIGER_ROADS_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.census.gov", "tiger_roads"),
                    "tiger:tiger_roads",
                    "EPSG:4326");

    public static final TestDataType CUBEWERX_GOVUNITCE =
            new TestDataType(
                    "CubeWerx_nsdi/1.1.0",
                    new QName("http://www.fgdc.gov/framework/073004/gubs", "GovernmentalUnitCE"),
                    "gubs_GovernmentalUnitCE",
                    "EPSG:4269");

    public static final TestDataType CUBEWERX_ROADSEG =
            new TestDataType(
                    "CubeWerx_nsdi/1.1.0",
                    new QName("http://www.fgdc.gov/framework/073004/transportation", "RoadSeg"),
                    "trans_RoadSeg",
                    "EPSG:4269");

    public static final TestDataType IONIC_STATISTICAL_UNIT =
            new TestDataType(
                    "Ionic_unknown/1.1.0",
                    new QName("http://www.fgdc.gov/fgdc/gubs", "StatisticalUnit"),
                    "gubs_StatisticalUnit",
                    "EPSG:4269");

    public static final TestDataType ARCGIS_CURVE =
            new TestDataType(
                    "ArcGIS/1.1.0",
                    new QName(
                            "https://www.arcgis.com/services/lineTestWFS/FeatureServer/WFSServer",
                            "lineTest"),
                    "lineTestWFS",
                    "EPSG:27700");

    public static URL url(String resource) {
        URL url = WFSTestData.class.getResource("test-data/" + resource);
        assertNotNull("resource not found: " + resource, url);
        return url;
    }

    public static InputStream stream(String resource) {
        InputStream stream = WFSTestData.class.getResourceAsStream("test-data/" + resource);
        assertNotNull("resource not found: " + resource, stream);
        return stream;
    }

    protected static class MutableWFSConfig extends WFSConfig {

        public void setAxisOrder(String axisOrder) {
            this.axisOrder = axisOrder;
        }

        public void setAxisOrderFilter(String axisOrderFilter) {
            this.axisOrderFilter = axisOrderFilter;
        }

        public void setUseDefaultSrs(boolean useDefaultSrs) {
            this.useDefaultSrs = useDefaultSrs;
        }

        public void setOutputformatOverride(String outputFormatOverride) {
            this.outputformatOverride = outputFormatOverride;
        }

        public void setProtocol(Boolean protocol) {
            if (protocol == null) {
                this.preferredMethod = PreferredHttpMethod.AUTO;
            } else {
                this.preferredMethod =
                        protocol.booleanValue()
                                ? PreferredHttpMethod.HTTP_POST
                                : PreferredHttpMethod.HTTP_GET;
            }
        }

        public void setGmlCompatibleTypeNames(boolean gmlCompatibleTypeNames) {
            this.gmlCompatibleTypenames = gmlCompatibleTypeNames;
        }
    }

    public static WFSConfig getGmlCompatibleConfig() {
        MutableWFSConfig config = new MutableWFSConfig();
        config.setGmlCompatibleTypeNames(true);
        return config;
    }
}
