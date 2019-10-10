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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;

/** @author Stefan Uhrig, SAP SE */
public class SrsReaderTest extends TestCase {

    public void testSrsReading() throws IOException {
        String csv =
                "\"WGS 84 / Pseudo-Mercator\",3857,\"EPSG\",3857,\"PROJCS[\"\"WGS 84 / Pseudo-Mercator\"\",GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]],PROJECTION[\"\"Mercator_1SP\"\"],PARAMETER[\"\"central_meridian\"\",0],PARAMETER[\"\"scale_factor\"\",1],PARAMETER[\"\"false_easting\"\",0],PARAMETER[\"\"false_northing\"\",0],UNIT[\"\"metre\"\",1,AUTHORITY[\"\"EPSG\"\",\"\"9001\"\"]],AXIS[\"\"X\"\",EAST],AXIS[\"\"Y\"\",NORTH],EXTENSION[\"\"PROJ4\"\",\"\"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs\"\"],AUTHORITY[\"\"EPSG\"\",\"\"3857\"\"]]\",\"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs\",\"meter\",\"degree\",\"projected\",null,null,null,-2.0037508342789248E7,2.0037508342789248E7,-2.0048966104014635E7,2.0048966104014624E7\n"
                        + "\"WGS 84\",4326,\"EPSG\",4326,\"GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]]\",\"+proj=longlat +datum=WGS84 +no_defs \",\"metre\",\"degree\",\"geographic\",6378137.0,null,298.257223563,-180.0,180.0,-90.0,90.0\n"
                        + "\"PRS92\",4683,\"EPSG\",4683,\"GEOGCS[\"\"PRS92\"\",DATUM[\"\"Philippine_Reference_System_1992\"\",SPHEROID[\"\"Clarke 1866\"\",6378206.4,294.9786982138982,AUTHORITY[\"\"EPSG\"\",\"\"7008\"\"]],TOWGS84[-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06],AUTHORITY[\"\"EPSG\"\",\"\"6683\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4683\"\"]]\",\"+proj=longlat +ellps=clrk66 +towgs84=-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06 +no_defs \",\"metre\",\"degree\",\"geographic\",6378206.4,6356583.8,null,116.04,129.95,3.0,22.18\n"
                        + "\"WGS 84 (planar)\",1000004326,\"EPSG\",4326,\"GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]]\",\"+proj=longlat +datum=WGS84 +no_defs \",\"planar degree\",\"degree\",\"flat\",6378137.0,null,298.257223563,-180.0,180.0,-90.0,90.0";
        InputStream is = new ByteArrayInputStream(csv.getBytes("UTF-8"));
        SrsReader reader = new SrsReader(is);
        Srs srs;

        srs = reader.readNextSrs();
        assertNotNull(srs);
        assertEquals("WGS 84 / Pseudo-Mercator", srs.getName());
        assertEquals(3857, srs.getSrid());
        assertEquals("EPSG", srs.getOrganization());
        assertEquals(3857, srs.getOrganizationId());
        assertEquals(
                "PROJCS[\"WGS 84 / Pseudo-Mercator\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]],PROJECTION[\"Mercator_1SP\"],PARAMETER[\"central_meridian\",0],PARAMETER[\"scale_factor\",1],PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],EXTENSION[\"PROJ4\",\"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs\"],AUTHORITY[\"EPSG\",\"3857\"]]",
                srs.getWkt());
        assertEquals(
                "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs",
                srs.getProj4());
        assertEquals("meter", srs.getLinearUom());
        assertEquals("degree", srs.getAngularUom());
        assertEquals(Srs.Type.PROJECTED, srs.getType());
        assertNull(srs.getMajorAxis());
        assertNull(srs.getMinorAxis());
        assertNull(srs.getInverseFlattening());
        assertEquals(-2.0037508342789248E7, srs.getMinX());
        assertEquals(2.0037508342789248E7, srs.getMaxX());
        assertEquals(-2.0048966104014635E7, srs.getMinY());
        assertEquals(2.0048966104014624E7, srs.getMaxY());

        srs = reader.readNextSrs();
        assertNotNull(srs);
        assertEquals("WGS 84", srs.getName());
        assertEquals(4326, srs.getSrid());
        assertEquals("EPSG", srs.getOrganization());
        assertEquals(4326, srs.getOrganizationId());
        assertEquals(
                "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]",
                srs.getWkt());
        assertEquals("+proj=longlat +datum=WGS84 +no_defs ", srs.getProj4());
        assertEquals("metre", srs.getLinearUom());
        assertEquals("degree", srs.getAngularUom());
        assertEquals(Srs.Type.GEOGRAPHIC, srs.getType());
        assertEquals(6378137.0, srs.getMajorAxis());
        assertNull(srs.getMinorAxis());
        assertEquals(298.257223563, srs.getInverseFlattening());
        assertEquals(-180.0, srs.getMinX());
        assertEquals(180.0, srs.getMaxX());
        assertEquals(-90.0, srs.getMinY());
        assertEquals(90.0, srs.getMaxY());

        srs = reader.readNextSrs();
        assertNotNull(srs);
        assertEquals("PRS92", srs.getName());
        assertEquals(4683, srs.getSrid());
        assertEquals("EPSG", srs.getOrganization());
        assertEquals(4683, srs.getOrganizationId());
        assertEquals(
                "GEOGCS[\"PRS92\",DATUM[\"Philippine_Reference_System_1992\",SPHEROID[\"Clarke 1866\",6378206.4,294.9786982138982,AUTHORITY[\"EPSG\",\"7008\"]],TOWGS84[-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06],AUTHORITY[\"EPSG\",\"6683\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4683\"]]",
                srs.getWkt());
        assertEquals(
                "+proj=longlat +ellps=clrk66 +towgs84=-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06 +no_defs ",
                srs.getProj4());
        assertEquals("metre", srs.getLinearUom());
        assertEquals("degree", srs.getAngularUom());
        assertEquals(Srs.Type.GEOGRAPHIC, srs.getType());
        assertEquals(6378206.4, srs.getMajorAxis());
        assertEquals(6356583.8, srs.getMinorAxis());
        assertNull(srs.getInverseFlattening());
        assertEquals(116.04, srs.getMinX());
        assertEquals(129.95, srs.getMaxX());
        assertEquals(3.0, srs.getMinY());
        assertEquals(22.18, srs.getMaxY());

        srs = reader.readNextSrs();
        assertNotNull(srs);
        assertEquals("WGS 84 (planar)", srs.getName());
        assertEquals(1000004326, srs.getSrid());
        assertEquals("EPSG", srs.getOrganization());
        assertEquals(4326, srs.getOrganizationId());
        assertEquals(
                "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]",
                srs.getWkt());
        assertEquals("+proj=longlat +datum=WGS84 +no_defs ", srs.getProj4());
        assertEquals("planar degree", srs.getLinearUom());
        assertEquals("degree", srs.getAngularUom());
        assertEquals(Srs.Type.FLAT, srs.getType());
        assertEquals(6378137.0, srs.getMajorAxis());
        assertNull(srs.getMinorAxis());
        assertEquals(298.257223563, srs.getInverseFlattening());
        assertEquals(-180.0, srs.getMinX());
        assertEquals(180.0, srs.getMaxX());
        assertEquals(-90.0, srs.getMinY());
        assertEquals(90.0, srs.getMaxY());

        assertNull(reader.readNextSrs());
    }

    public void testWrongEntryCount() throws IOException {
        String csv =
                "\"WGS 84\",4326,\"EPSG\",4326,\"GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]]\",\"+proj=longlat +datum=WGS84 +no_defs \",\"metre\",\"degree\",\"geographic\",6378137.0,null,298.257223563,-180.0,180.0,-90.0\n";
        InputStream is = new ByteArrayInputStream(csv.getBytes("UTF-8"));
        SrsReader reader = new SrsReader(is);
        try {
            reader.readNextSrs();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
        }
    }

    public void testInvalidType() throws IOException {
        String csv =
                "\"WGS 84\",4326,\"EPSG\",4326,\"GEOGCS[\"\"WGS 84\"\",DATUM[\"\"WGS_1984\"\",SPHEROID[\"\"WGS 84\"\",6378137,298.257223563,AUTHORITY[\"\"EPSG\"\",\"\"7030\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"6326\"\"]],PRIMEM[\"\"Greenwich\"\",0,AUTHORITY[\"\"EPSG\"\",\"\"8901\"\"]],UNIT[\"\"degree\"\",0.0174532925199433,AUTHORITY[\"\"EPSG\"\",\"\"9122\"\"]],AUTHORITY[\"\"EPSG\"\",\"\"4326\"\"]]\",\"+proj=longlat +datum=WGS84 +no_defs \",\"metre\",\"degree\",\"geographc\",6378137.0,null,298.257223563,-180.0,180.0,-90.0,90.0\n";
        InputStream is = new ByteArrayInputStream(csv.getBytes("UTF-8"));
        SrsReader reader = new SrsReader(is);
        try {
            reader.readNextSrs();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
        }
    }
}
