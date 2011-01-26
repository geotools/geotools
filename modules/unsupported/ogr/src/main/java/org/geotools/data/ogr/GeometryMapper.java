/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;

import org.gdal.ogr.ogr;
import org.geotools.data.DataSourceException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Converts between JTS and OGR geometries
 * @author Andrea Aime - OpenGeo
 */
public class GeometryMapper {
    /**
     * From ogr_core.h, the byte order constants
     */
    static final int WKB_XDR = 1;

    /**
     * Enables usage of WKB encoding for OGR/Java Geometry conversion. At the time of writing, it
     * cannot be used because it'll bring the virtual machine down (yes, a real crash...)
     */
    static final boolean USE_WKB = true;

    GeometryFactory geomFactory;

    WKBReader wkbReader;

    WKTReader wktReader;

    WKBWriter wkbWriter;

    WKTWriter wktWriter;

    public GeometryMapper(GeometryFactory geomFactory) {
        this.geomFactory = geomFactory;
        if (USE_WKB) {
            this.wkbReader = new WKBReader(geomFactory);
            this.wkbWriter = new WKBWriter();
        } else {
            this.wktReader = new WKTReader(geomFactory);
            this.wktWriter = new WKTWriter();
        }
    }


    /**
     * Reads the current feature's geometry using wkb encoding. A wkbReader should be provided since
     * it's not thread safe by design.
     * 
     * @throws IOException
     */
    Geometry parseOgrGeometry(org.gdal.ogr.Geometry geom) throws IOException {
        // Extract the geometry using either WKT or WKB. Rationale: the SWIG
        // bindings do not provide subclasses. Even if they did, going thru the
        // JNI barrier often is expensive, so it's better to gather the geometry
        // is a single call
        if (USE_WKB) {
            int wkbSize = geom.WkbSize();
            // the gdal interface uses a char* type, maybe because in C it's
            // unsigned and has
            // the same size as a byte, unfortunately this means we have to
            // unpack it
            // to byte format by doing bit masking and shifting
            byte[] byteBuffer = new byte[wkbSize];
            geom.ExportToWkb(byteBuffer, WKB_XDR);
            try {
                Geometry g = wkbReader.read(byteBuffer);
                return g;
            } catch (ParseException pe) {
                throw new RuntimeException(
                        "Could not parse the current Geometry in WKB format.", pe);
            }
        } else {
            String[] stringArray = new String[1];
            geom.ExportToWkt(stringArray);
            try {
                return wktReader.read(stringArray[0]);
            } catch (ParseException pe) {
                throw new RuntimeException(
                        "Could not parse the current Geometry in WKB format.", pe);
            }
        }
    }

    org.gdal.ogr.Geometry parseGTGeometry(Geometry geometry) throws RuntimeException {
        final org.gdal.ogr.Geometry ogrGeom;
        if (USE_WKB) {
            byte[] wkb = wkbWriter.write(geometry);
            ogrGeom = ogr.CreateGeometryFromWkb(wkb, null);
            if (ogrGeom == null)
                throw new RuntimeException(
                        "Could not turn JTS geometry into an OGR one thought WKB");
        } else {
            String wkt = wktWriter.write(geometry);
            ogrGeom = ogr.CreateGeometryFromWkt(wkt, null);
            if (ogrGeom == null)
                throw new RuntimeException(
                        "Could not turn JTS geometry into an OGR one thought WKT");
        }
        return ogrGeom;
    }

}
