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

package org.geotools.data.sfs;


import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.geotools.data.sfs.SFSDataStoreUtil;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;


import com.vividsolutions.jts.geom.Geometry;


public class SFSDataStoreFeatureReaderTest extends TestCase {

    public SFSDataStoreFeatureReaderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /* return geojson with point geometry*/
    public String getFeatureWithPointGeometry() {
        String json =
                "{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "     'type': 'Point',"
                + "     'coordinates': [1.5, 2.9]"
                + "   },"
                + "   'properties': {"
                + "     'boundedBy': [-1.1, -1.2, 1.3, 1.4],"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one'"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

    /* return geojson with linsString geometry*/
    public String getFeatureWithLineStringGeometry() {
        String json =
                "{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "        'type': 'LineString',"
                + "        'coordinates': [[1.1, 1.2], [1.3, 1.4]]"
                + "   },"
                + "   'properties': {"
                + "     'boundedBy': [-1.1, -1.2, 1.3, 1.4],"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one'"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

    public String getFeatureWithpolygonTyp1Geometry() {
        String json =
                "{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "        'type': 'Polygon',"
                + "        'coordinates': [[100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1] ]"
                + "   },"
                + "   'properties': {"
                + "     'boundedBy': [-1.1, -1.2, 1.3, 1.4],"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one'"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

    public String getFeatureWithpolygonTyp2Geometry() {

        String json =
                "{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "        'type': 'Polygon',"
                + "        'coordinates': ["
                + "      [ [100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1] ],"
                + "      [ [100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2] ]"
                + "      ]"
                + "   },"
                + "   'properties': {"
                + "     'boundedBy': [-1.1, -1.2, 1.3, 1.4],"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one'"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

    public String getFeatureWithMultiPointGeometry() {
        String json =
                "{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "        'type': 'MultiPoint',"
                + "        'coordinates': [ [100.1, 0.1], [101.1, 1.1] ]"
                + "   },"
                + "   'properties': {"
                + "     'boundedBy': [-1.1, -1.2, 1.3, 1.4],"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one'"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

    public String getFeatureWithMultiLineGeometry() {
        String json =
                "{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "        'type': 'MultiLineString',"
                + "        'coordinates':["
                + "        [ [100.1, 0.1], [101.1, 1.1] ],"
                + "        [ [102.1, 2.1], [103.1, 3.1] ]"
                + "      ]"
                + "   },"
                + "   'properties': {"
                + "     'boundedBy': [-1.1, -1.2, 1.3, 1.4],"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one'"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

     public String getFeatureWithMultiPolygonGeometry() {
        String json =
                "{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "        'type': 'MultiPolygon',"
                + "        'coordinates':["
                + "      [[[102.1, 2.1], [103.1, 2.1], [103.1, 3.1], [102.1, 3.1], [102.1, 2.1]]],"
                + "      [[[100.1, 0.1], [101.1, 0.1], [101.1, 1.1], [100.1, 1.1], [100.1, 0.1]],"
                + "       [[100.2, 0.2], [100.8, 0.2], [100.8, 0.8], [100.2, 0.8], [100.2, 0.2]]]"
                + "      ]"
                + "   },"
                + "   'properties': {"
                + "     'boundedBy': [-1.1, -1.2, 1.3, 1.4],"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one'"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

     public String getFeatureWithGeometryCollectionGeometry() {
        String json =
                "{"
                + "   'type': 'Feature',"
                + "   'geometry' : { "
                + "     'type' : 'GeometryCollection',"
                + "     'geometries': ["
                + "          { 'type': 'Point',"
                + "            'coordinates': [100.1, 0.1]"
                + "          },"
                + "          { 'type': 'LineString',"
                + "            'coordinates': [ [101.1, 0.1], [102.1, 1.1]]"
                + "          }]"
                + "    },"
                + "   'properties': {"
                + "     'boundedBy': [-1.1, -1.2, 1.3, 1.4],"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one'"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }


    /* Test feature flipping with point geometry*/
    public void testFeatureFlippingWithPointGeom() throws IOException {
        FeatureJSON fjson = new FeatureJSON();

        SimpleFeature sf = fjson.readFeature(reader(strip(getFeatureWithPointGeometry())));

        Geometry fnG = (Geometry) sf.getDefaultGeometry();

        SFSDataStoreUtil.flipFeatureYX(fnG);

        assertEquals(fnG.getCoordinate().x, 2.9);

        assertEquals(fnG.getCoordinate().y, 1.5);

    }

    /* Test feature flipping with LineString geometry*/
    public void testFeatureFlippingWithLineStringGeom() throws IOException {
        FeatureJSON fjson = new FeatureJSON();

        SimpleFeature sf = fjson.readFeature(reader(strip(getFeatureWithLineStringGeometry())));

        Geometry fnG = (Geometry) sf.getDefaultGeometry();

        SFSDataStoreUtil.flipFeatureYX(fnG);

        /* Check for first point*/
        assertEquals(fnG.getCoordinates()[0].x, 1.2);
        assertEquals(fnG.getCoordinates()[0].y, 1.1);

        /* Check for second point*/
        assertEquals(fnG.getCoordinates()[1].x, 1.4);
        assertEquals(fnG.getCoordinates()[1].y, 1.3);
    }

    /* Test feature flipping with Polygon w/o holes geometry*/
    public void testFeatureFlippingWithPolygonNoHolesGeom() throws IOException {
        FeatureJSON fjson = new FeatureJSON();

        SimpleFeature sf = fjson.readFeature(reader(strip(getFeatureWithpolygonTyp1Geometry())));

        Geometry fnG = (Geometry) sf.getDefaultGeometry();

        SFSDataStoreUtil.flipFeatureYX(fnG);

        /* Check for first point*/
        assertEquals(fnG.getCoordinates()[0].x, 0.1);
        assertEquals(fnG.getCoordinates()[0].y, 100.1);

        /* Check for second point*/
        assertEquals(fnG.getCoordinates()[1].x, 0.1);
        assertEquals(fnG.getCoordinates()[1].y, 101.1);

        /* Check for last point*/
        assertEquals(fnG.getCoordinates()[4].x, 0.1);
        assertEquals(fnG.getCoordinates()[4].y, 100.1);
    }

    /* Test feature flipping with Polygon w/ holes geometry*/
    public void testFeatureFlippingWithPolygonHolesGeom() throws IOException {
        FeatureJSON fjson = new FeatureJSON();

        SimpleFeature sf = fjson.readFeature(reader(strip(getFeatureWithpolygonTyp2Geometry())));

        Geometry fnG = (Geometry) sf.getDefaultGeometry();
        
        SFSDataStoreUtil.flipFeatureYX(fnG);

        /* Check for first point*/
        assertEquals(fnG.getCoordinates()[0].x, 0.1);
        assertEquals(fnG.getCoordinates()[0].y, 100.1);

        /* Check for fourth point*/
        assertEquals(fnG.getCoordinates()[3].x, 1.1);
        assertEquals(fnG.getCoordinates()[3].y, 100.1);

        /* Check for sixth point*/
        assertEquals(fnG.getCoordinates()[5].x, 0.2);
        assertEquals(fnG.getCoordinates()[5].y, 100.2);

        /* Check for ninth point*/
        assertEquals(fnG.getCoordinates()[8].x, 0.8);
        assertEquals(fnG.getCoordinates()[8].y, 100.2);
    }

    /* Test feature flipping with Multi-Point geometry*/
    public void testFeatureFlippingWithMultiPointGeom() throws IOException {
        FeatureJSON fjson = new FeatureJSON();

        SimpleFeature sf = fjson.readFeature(reader(strip(getFeatureWithMultiPointGeometry())));

        Geometry fnG = (Geometry) sf.getDefaultGeometry();

        SFSDataStoreUtil.flipFeatureYX(fnG);

        /* Check for first point*/
        assertEquals(fnG.getCoordinates()[0].x, 0.1);
        assertEquals(fnG.getCoordinates()[0].y, 100.1);

        /* Check for second point*/
        assertEquals(fnG.getCoordinates()[1].x, 1.1);
        assertEquals(fnG.getCoordinates()[1].y, 101.1);
    }

    /* Test feature flipping with Multi-Line String geometry*/
    public void testFeatureFlippingWithMultiLineStringGeom() throws IOException {
        FeatureJSON fjson = new FeatureJSON();

        SimpleFeature sf = fjson.readFeature(reader(strip(getFeatureWithMultiLineGeometry())));

        Geometry fnG = (Geometry) sf.getDefaultGeometry();

        SFSDataStoreUtil.flipFeatureYX(fnG);

        /* Check for first point*/
        assertEquals(fnG.getCoordinates()[0].x, 0.1);
        assertEquals(fnG.getCoordinates()[0].y, 100.1);

        /* Check for second point*/
        assertEquals(fnG.getCoordinates()[1].x, 1.1);
        assertEquals(fnG.getCoordinates()[1].y, 101.1);

        /* Check for third point*/
        assertEquals(fnG.getCoordinates()[2].x, 2.1);
        assertEquals(fnG.getCoordinates()[2].y, 102.1);

        /* Check for last point*/
        assertEquals(fnG.getCoordinates()[3].x, 3.1);
        assertEquals(fnG.getCoordinates()[3].y, 103.1);
    }

    /* Test feature flipping with Multi-Polygongeometry*/
    public void testFeatureFlippingWithMultiPolygonGeom() throws IOException {
        FeatureJSON fjson = new FeatureJSON();

        SimpleFeature sf = fjson.readFeature(reader(strip(getFeatureWithMultiPolygonGeometry())));

        Geometry fnG = (Geometry) sf.getDefaultGeometry();

        SFSDataStoreUtil.flipFeatureYX(fnG);

        /* Check for first point*/
        assertEquals(fnG.getCoordinates()[0].x, 2.1);
        assertEquals(fnG.getCoordinates()[0].y, 102.1);

        /* Check for fourth point*/
        assertEquals(fnG.getCoordinates()[3].x, 3.1);
        assertEquals(fnG.getCoordinates()[3].y, 102.1);

        /* Check for sixth point*/
        assertEquals(fnG.getCoordinates()[5].x, 0.1);
        assertEquals(fnG.getCoordinates()[5].y, 100.1);

        /* Check for eight point*/
        assertEquals(fnG.getCoordinates()[7].x, 1.1);
        assertEquals(fnG.getCoordinates()[7].y, 101.1);

        /* Check for 12th point*/
        assertEquals(fnG.getCoordinates()[11].x, 0.2);
        assertEquals(fnG.getCoordinates()[11].y, 100.8);

        /* Check for last (15th) point*/
        assertEquals(fnG.getCoordinates()[14].x, 0.2);
        assertEquals(fnG.getCoordinates()[14].y, 100.2);
    }

    
    /**
     * Test Whether the boundingbox params get flipped properly or not
     * @throws IOException
     */
    public void testForBBoxFlipping() throws IOException {
        FeatureJSON fjson = new FeatureJSON();

        SimpleFeature sf = fjson.readFeature(reader(strip(getFeatureWithLineStringGeometry())));

        ArrayList al = (ArrayList) sf.getAttribute("boundedBy");

        SFSDataStoreUtil.flipYXInsideTheBoundingBox(al);

        /* Check for first point*/
        assertEquals(Double.parseDouble(al.get(0).toString()), -1.2);

        assertEquals(Double.parseDouble(al.get(1).toString()), -1.1);

        /* Check for second point*/
        assertEquals(Double.parseDouble(al.get(2).toString()), 1.4);

        assertEquals(Double.parseDouble(al.get(3).toString()), 1.3);
    }
   
    /* Methods used for testing*/
    protected StringReader reader(String json) throws IOException {
        return new StringReader(json);
    }

    /* Method used for testing*/
    protected String strip(String json) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == ' ' || c == '\n') {
                continue;
            }
            if (c == '\'') {
                sb.append("\"");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
