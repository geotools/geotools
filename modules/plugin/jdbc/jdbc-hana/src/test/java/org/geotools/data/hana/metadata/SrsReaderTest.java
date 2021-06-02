/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.metadata;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Assert;
import org.junit.Test;

/** @author Stefan Uhrig, SAP SE */
public class SrsReaderTest {

    @Test
    public void testSrsReading() throws IOException {
        String csv =
                "\"WGS 84 / Pseudo-Mercator\",3857,\"EPSG\",3857,\"PROJCS[\"\"WGS 84 / Pseudo-Mercator\"\",GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]],PROJECTION[\"\"Mercator_1SP\"\"],PARAMETER[\"\"central_meridian\"\",0],PARAMETER[\"\"scale_factor\"\",1],PARAMETER[\"\"false_easting\"\",0],PARAMETER[\"\"false_northing\"\",0],UNIT[\"\"metre\"\",1,AUTHORITY[\"\"EPSG\"\",\"\"9001\"\"]],AXIS[\"\"X\"\",EAST],AXIS[\"\"Y\"\",NORTH],EXTENSION[\"\"PROJ4\"\",\"\"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs\"\"],AUTHORITY[\"\"EPSG\"\",\"\"3857\"\"]]\",\"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs\",\"meter\",\"degree\",\"projected\",null,null,null,-2.0037508342789248E7,2.0037508342789248E7,-2.0048966104014635E7,2.0048966104014624E7\n"
                        + "\"WGS 84\",4326,\"EPSG\",4326,\"GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]]\",\"+proj=longlat +datum=WGS84 +no_defs \",\"metre\",\"degree\",\"geographic\",6378137.0,null,298.257223563,-180.0,180.0,-90.0,90.0\n"
                        + "\"PRS92\",4683,\"EPSG\",4683,\"GEOGCS[\"\"PRS92\"\",DATUM[\"\"Philippine_Reference_System_1992\"\",SPHEROID[\"\"Clarke 1866\"\",6378206.4,294.9786982138982,AUTHORITY[\"\"EPSG\"\",\"\"7008\"\"]],TOWGS84[-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06],AUTHORITY[\"\"EPSG\"\",\"\"6683\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4683\"\"]]\",\"+proj=longlat +ellps=clrk66 +towgs84=-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06 +no_defs \",\"metre\",\"degree\",\"geographic\",6378206.4,6356583.8,null,116.04,129.95,3.0,22.18\n"
                        + "\"WGS 84 (planar)\",1000004326,\"EPSG\",4326,\"GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]]\",\"+proj=longlat +datum=WGS84 +no_defs \",\"planar degree\",\"degree\",\"flat\",6378137.0,null,298.257223563,-180.0,180.0,-90.0,90.0";
        InputStream is = new ByteArrayInputStream(csv.getBytes(UTF_8));
        SrsReader reader = new SrsReader(is);

        Srs srs = reader.readNextSrs();
        Assert.assertNotNull(srs);
        Assert.assertEquals("WGS 84 / Pseudo-Mercator", srs.getName());
        Assert.assertEquals(3857, srs.getSrid());
        Assert.assertEquals("EPSG", srs.getOrganization());
        Assert.assertEquals(3857, srs.getOrganizationId());
        Assert.assertEquals(
                "PROJCS[\"WGS 84 / Pseudo-Mercator\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\","
                        + "SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],"
                        + "AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\","
                        + "\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\","
                        + "\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]],PROJECTION[\"Mercator_1SP\"],"
                        + "PARAMETER[\"central_meridian\",0],PARAMETER[\"scale_factor\",1],"
                        + "PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],"
                        + "UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"X\",EAST],"
                        + "AXIS[\"Y\",NORTH],EXTENSION[\"PROJ4\",\"+proj=merc +a=6378137 +b=6378137"
                        + " +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null "
                        + "+wktext +no_defs\"],AUTHORITY[\"EPSG\",\"3857\"]]",
                srs.getWkt());
        Assert.assertEquals(
                "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 "
                        + "+units=m +nadgrids=@null +wktext +no_defs",
                srs.getProj4());
        Assert.assertEquals("meter", srs.getLinearUom());
        Assert.assertEquals("degree", srs.getAngularUom());
        Assert.assertEquals(Srs.Type.PROJECTED, srs.getType());
        Assert.assertNull(srs.getMajorAxis());
        Assert.assertNull(srs.getMinorAxis());
        Assert.assertNull(srs.getInverseFlattening());
        Assert.assertEquals(-2.0037508342789248E7, srs.getMinX(), 0d);
        Assert.assertEquals(2.0037508342789248E7, srs.getMaxX(), 0d);
        Assert.assertEquals(-2.0048966104014635E7, srs.getMinY(), 0d);
        Assert.assertEquals(2.0048966104014624E7, srs.getMaxY(), 0d);

        srs = reader.readNextSrs();
        Assert.assertNotNull(srs);
        Assert.assertEquals("WGS 84", srs.getName());
        Assert.assertEquals(4326, srs.getSrid());
        Assert.assertEquals("EPSG", srs.getOrganization());
        Assert.assertEquals(4326, srs.getOrganizationId());
        Assert.assertEquals(
                "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]",
                srs.getWkt());
        Assert.assertEquals("+proj=longlat +datum=WGS84 +no_defs ", srs.getProj4());
        Assert.assertEquals("metre", srs.getLinearUom());
        Assert.assertEquals("degree", srs.getAngularUom());
        Assert.assertEquals(Srs.Type.GEOGRAPHIC, srs.getType());
        Assert.assertEquals(6378137.0, srs.getMajorAxis(), 0d);
        Assert.assertNull(srs.getMinorAxis());
        Assert.assertEquals(298.257223563, srs.getInverseFlattening(), 0d);
        Assert.assertEquals(-180.0, srs.getMinX(), 0d);
        Assert.assertEquals(180.0, srs.getMaxX(), 0d);
        Assert.assertEquals(-90.0, srs.getMinY(), 0d);
        Assert.assertEquals(90.0, srs.getMaxY(), 0d);

        srs = reader.readNextSrs();
        Assert.assertNotNull(srs);
        Assert.assertEquals("PRS92", srs.getName());
        Assert.assertEquals(4683, srs.getSrid());
        Assert.assertEquals("EPSG", srs.getOrganization());
        Assert.assertEquals(4683, srs.getOrganizationId());
        Assert.assertEquals(
                "GEOGCS[\"PRS92\",DATUM[\"Philippine_Reference_System_1992\",SPHEROID[\"Clarke "
                        + "1866\",6378206.4,294.9786982138982,AUTHORITY[\"EPSG\",\"7008\"]],"
                        + "TOWGS84[-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06],"
                        + "AUTHORITY[\"EPSG\",\"6683\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\","
                        + "\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122"
                        + "\"]],AUTHORITY[\"EPSG\",\"4683\"]]",
                srs.getWkt());
        Assert.assertEquals(
                "+proj=longlat +ellps=clrk66 +towgs84=-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1"
                        + ".06 +no_defs ",
                srs.getProj4());
        Assert.assertEquals("metre", srs.getLinearUom());
        Assert.assertEquals("degree", srs.getAngularUom());
        Assert.assertEquals(Srs.Type.GEOGRAPHIC, srs.getType());
        Assert.assertEquals(6378206.4, srs.getMajorAxis(), 0d);
        Assert.assertEquals(6356583.8, srs.getMinorAxis(), 0d);
        Assert.assertNull(srs.getInverseFlattening());
        Assert.assertEquals(116.04, srs.getMinX(), 0d);
        Assert.assertEquals(129.95, srs.getMaxX(), 0d);
        Assert.assertEquals(3.0, srs.getMinY(), 0d);
        Assert.assertEquals(22.18, srs.getMaxY(), 0d);

        srs = reader.readNextSrs();
        Assert.assertNotNull(srs);
        Assert.assertEquals("WGS 84 (planar)", srs.getName());
        Assert.assertEquals(1000004326, srs.getSrid());
        Assert.assertEquals("EPSG", srs.getOrganization());
        Assert.assertEquals(4326, srs.getOrganizationId());
        Assert.assertEquals(
                "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,"
                        + "AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],"
                        + "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]",
                srs.getWkt());
        Assert.assertEquals("+proj=longlat +datum=WGS84 +no_defs ", srs.getProj4());
        Assert.assertEquals("planar degree", srs.getLinearUom());
        Assert.assertEquals("degree", srs.getAngularUom());
        Assert.assertEquals(Srs.Type.FLAT, srs.getType());
        Assert.assertEquals(6378137.0, srs.getMajorAxis(), 0d);
        Assert.assertNull(srs.getMinorAxis());
        Assert.assertEquals(298.257223563, srs.getInverseFlattening(), 0d);
        Assert.assertEquals(-180.0, srs.getMinX(), 0d);
        Assert.assertEquals(180.0, srs.getMaxX(), 0d);
        Assert.assertEquals(-90.0, srs.getMinY(), 0d);
        Assert.assertEquals(90.0, srs.getMaxY(), 0d);

        Assert.assertNull(reader.readNextSrs());
    }

    @Test
    public void testWrongEntryCount() throws IOException {
        String csv =
                "\"WGS 84\",4326,\"EPSG\",4326,\"GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]]\",\"+proj=longlat +datum=WGS84 +no_defs \",\"metre\",\"degree\",\"geographic\",6378137.0,null,298.257223563,-180.0,180.0,-90.0\n";
        InputStream is = new ByteArrayInputStream(csv.getBytes(UTF_8));
        SrsReader reader = new SrsReader(is);
        try {
            reader.readNextSrs();
            Assert.fail("Expected RuntimeException");
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void testInvalidType() throws IOException {
        String csv =
                "\"WGS 84\",4326,\"EPSG\",4326,\"GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]]\",\"+proj=longlat +datum=WGS84 +no_defs \",\"metre\",\"degree\",\"geographc\",6378137.0,null,298.257223563,-180.0,180.0,-90.0,90.0\n";
        InputStream is = new ByteArrayInputStream(csv.getBytes(UTF_8));
        SrsReader reader = new SrsReader(is);
        try {
            reader.readNextSrs();
            Assert.fail("Expected RuntimeException");
        } catch (RuntimeException e) {
        }
    }
}
