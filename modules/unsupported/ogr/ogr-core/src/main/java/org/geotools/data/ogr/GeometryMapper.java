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
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

/**
 * Converts between JTS and OGR geometries
 *
 * @author Andrea Aime - GeoSolutions
 */
@SuppressWarnings("rawtypes")
abstract class GeometryMapper {

    protected OGR ogr;

    protected GeometryMapper(OGR ogr) {
        this.ogr = ogr;
    }

    abstract Geometry parseOgrGeometry(Object geom) throws IOException;

    abstract Object parseGTGeometry(Geometry geometry) throws IOException;

    static class WKB extends GeometryMapper {
        GeometryFactory geomFactory;

        WKBReader wkbReader;

        WKBWriter wkbWriter;

        public WKB(GeometryFactory geomFactory, OGR ogr) {
            super(ogr);
            this.geomFactory = geomFactory;
            this.wkbReader = new WKBReader(geomFactory);
            this.wkbWriter = new WKBWriter();
        }

        /**
         * Reads the current feature's geometry using wkb encoding. A wkbReader should be provided
         * since it's not thread safe by design.
         */
        Geometry parseOgrGeometry(Object geom) throws IOException {
            int wkbSize = ogr.GeometryGetWkbSize(geom);
            byte[] wkb = new byte[wkbSize];
            ogr.CheckError(ogr.GeometryExportToWkb(geom, wkb));
            try {
                Geometry g = wkbReader.read(wkb);
                return g;
            } catch (ParseException pe) {
                throw new IOException("Could not parse the current Geometry in WKB format.", pe);
            }
        }

        Object parseGTGeometry(Geometry geometry) throws IOException {
            byte[] wkb = wkbWriter.write(geometry);
            int[] ret = new int[1];
            Object result = ogr.GeometryCreateFromWkb(wkb, ret);
            ogr.CheckError(ret[0]);
            return result;
        }
    }

    static class WKT extends GeometryMapper {
        GeometryFactory geomFactory;

        WKTReader wktReader;

        WKTWriter wktWriter;

        public WKT(GeometryFactory geomFactory, OGR ogr) {
            super(ogr);
            this.geomFactory = geomFactory;
            this.wktReader = new WKTReader(geomFactory);
            this.wktWriter = new WKTWriter();
        }

        /**
         * Reads the current feature's geometry using wkb encoding. A wkbReader should be provided
         * since it's not thread safe by design.
         */
        Geometry parseOgrGeometry(Object geom) throws IOException {
            int[] ret = new int[1];
            String wkt = ogr.GeometryExportToWkt(geom, ret);
            ogr.CheckError(ret[0]);
            try {
                return wktReader.read(wkt);
            } catch (ParseException pe) {
                throw new IOException("Could not parse the current Geometry in WKT format.", pe);
            }
        }

        Object parseGTGeometry(Geometry geometry) throws IOException {
            String wkt = wktWriter.write(geometry);
            int[] ret = new int[1];
            Object result = ogr.GeometryCreateFromWkt(wkt, ret);
            ogr.CheckError(ret[0]);
            return result;
        }
    }
}
