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

import junit.framework.TestCase;
import org.geotools.data.hana.metadata.Srs.Type;

/** @author Stefan Uhrig, SAP SE */
public class MetadataDdlTest extends TestCase {

    public void testUomDdlGeneration() {
        Uom meter = new Uom("kilometer", Uom.Type.LINEAR, 1000.0);
        Uom degree = new Uom("degree", Uom.Type.ANGULAR, 0.017453292519943278);

        assertEquals(
                "CREATE SPATIAL UNIT OF MEASURE \"kilometer\" TYPE LINEAR CONVERT USING 1000.0",
                MetadataDdl.getUomDdl(meter));
        assertEquals(
                "CREATE SPATIAL UNIT OF MEASURE \"degree\" TYPE ANGULAR CONVERT USING 0.017453292519943278",
                MetadataDdl.getUomDdl(degree));
    }

    public void testSrsDdlGeneration() {
        Srs srs4326 =
                new Srs(
                        "WGS 84",
                        4326,
                        "EPSG",
                        4326,
                        "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]",
                        "+proj=longlat +datum=WGS84 +no_defs",
                        "meter",
                        "degree",
                        Type.GEOGRAPHIC,
                        6378137.0,
                        null,
                        298.257223563,
                        -180.0,
                        180.0,
                        -90.0,
                        90.0);
        Srs srs3857 =
                new Srs(
                        "WGS 84 / Pseudo-Mercator",
                        3857,
                        "EPSG",
                        3857,
                        "PROJCS[\"WGS 84 / Pseudo-Mercator\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]],PROJECTION[\"Mercator_1SP\"],PARAMETER[\"central_meridian\",0],PARAMETER[\"scale_factor\",1],PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],EXTENSION[\"PROJ4\",\"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs\"],AUTHORITY[\"EPSG\",\"3857\"]]",
                        "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs",
                        "meter",
                        null,
                        Type.PROJECTED,
                        6378137.0,
                        null,
                        298.257223563,
                        -20037508.342789248,
                        20037508.342789248,
                        -20048966.104014635,
                        20048966.104014624);
        Srs srs4683 =
                new Srs(
                        "PRS92",
                        4683,
                        "EPSG",
                        4683,
                        "GEOGCS[\"PRS92\",DATUM[\"Philippine_Reference_System_1992\",SPHEROID[\"Clarke 1866\",6378206.4,294.9786982138982,AUTHORITY[\"EPSG\",\"7008\"]],TOWGS84[-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06],AUTHORITY[\"EPSG\",\"6683\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4683\"]]",
                        "+proj=longlat +ellps=clrk66 +towgs84=-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06 +no_defs ",
                        "metre",
                        "degree",
                        Srs.Type.GEOGRAPHIC,
                        6378206.4,
                        6356583.8,
                        null,
                        116.04,
                        129.95,
                        3.0,
                        22.18);

        assertEquals(
                "CREATE SPATIAL REFERENCE SYSTEM \"WGS 84\" IDENTIFIED BY 4326 ORGANIZATION \"EPSG\" IDENTIFIED BY 4326 DEFINITION 'GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]' TRANSFORM DEFINITION '+proj=longlat +datum=WGS84 +no_defs' LINEAR UNIT OF MEASURE \"meter\" ANGULAR UNIT OF MEASURE \"degree\" TYPE ROUND EARTH ELLIPSOID SEMI MAJOR AXIS 6378137.0 INVERSE FLATTENING 298.257223563 COORDINATE LONGITUDE BETWEEN -180.0 AND 180.0 COORDINATE LATITUDE BETWEEN -90.0 AND 90.0 TOLERANCE 0  SNAP TO GRID 0  POLYGON FORMAT 'EvenOdd'  STORAGE FORMAT 'Mixed'",
                MetadataDdl.getSrsDdl(srs4326));
        assertEquals(
                "CREATE SPATIAL REFERENCE SYSTEM \"WGS 84 / Pseudo-Mercator\" IDENTIFIED BY 3857 ORGANIZATION \"EPSG\" IDENTIFIED BY 3857 DEFINITION 'PROJCS[\"WGS 84 / Pseudo-Mercator\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]],PROJECTION[\"Mercator_1SP\"],PARAMETER[\"central_meridian\",0],PARAMETER[\"scale_factor\",1],PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],EXTENSION[\"PROJ4\",\"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs\"],AUTHORITY[\"EPSG\",\"3857\"]]' TRANSFORM DEFINITION '+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs' LINEAR UNIT OF MEASURE \"meter\" TYPE PLANAR ELLIPSOID SEMI MAJOR AXIS 6378137.0 INVERSE FLATTENING 298.257223563 COORDINATE X BETWEEN -2.0037508342789248E7 AND 2.0037508342789248E7 COORDINATE Y BETWEEN -2.0048966104014635E7 AND 2.0048966104014624E7 TOLERANCE 0  SNAP TO GRID 0  POLYGON FORMAT 'EvenOdd'  STORAGE FORMAT 'Internal'",
                MetadataDdl.getSrsDdl(srs3857));
        assertEquals(
                "CREATE SPATIAL REFERENCE SYSTEM \"PRS92\" IDENTIFIED BY 4683 ORGANIZATION \"EPSG\" IDENTIFIED BY 4683 DEFINITION 'GEOGCS[\"PRS92\",DATUM[\"Philippine_Reference_System_1992\",SPHEROID[\"Clarke 1866\",6378206.4,294.9786982138982,AUTHORITY[\"EPSG\",\"7008\"]],TOWGS84[-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06],AUTHORITY[\"EPSG\",\"6683\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4683\"]]' TRANSFORM DEFINITION '+proj=longlat +ellps=clrk66 +towgs84=-127.62,-67.24,-47.04,-3.068,4.903,1.578,-1.06 +no_defs ' LINEAR UNIT OF MEASURE \"metre\" ANGULAR UNIT OF MEASURE \"degree\" TYPE ROUND EARTH ELLIPSOID SEMI MAJOR AXIS 6378206.4 SEMI MINOR AXIS 6356583.8 COORDINATE LONGITUDE BETWEEN 116.04 AND 129.95 COORDINATE LATITUDE BETWEEN 3.0 AND 22.18 TOLERANCE 0  SNAP TO GRID 0  POLYGON FORMAT 'EvenOdd'  STORAGE FORMAT 'Mixed'",
                MetadataDdl.getSrsDdl(srs4683));
    }
}
