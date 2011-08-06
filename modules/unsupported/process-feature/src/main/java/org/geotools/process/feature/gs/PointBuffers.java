/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.feature.gs;

import java.awt.geom.Point2D;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Generates a set of polygons, each representing the set of points 
 * within a given distance from the central point The data layer must
 * be a point layer, the reference layer must be a polygonal one"
 */
@DescribeProcess(title = "pointBuffers", description = "Generates a set of polygons, each representing the set of points " +
		"within a given distance from the central point"
        + "The data layer must be a point layer, the reference layer must be a polygonal one")
public class PointBuffers implements GSProcess {

    @DescribeResult(name = "buffers", description = "The buffers. Each feature has a 'geom' attribute and a 'radius' attribute")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "center", description = "The buffers center") Point center,
            @DescribeParameter(name = "crs", description = "The coordinate reference system "
                    + "in which the point expressed and the points will be generated", min = 0) CoordinateReferenceSystem crs,
            @DescribeParameter(name = "distances", description = "The buffer distances, in meters") double[] distances,
            @DescribeParameter(name = "quadrantSegments", description = "Number of segments per quadrant "
                    + "in the generated buffers (8 by default)", min = 0) Integer quadrantSegments,
            ProgressListener listener) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Polygon.class, crs);
        tb.add("radius", Double.class);
        tb.setName("buffers");
        SimpleFeatureType schema = tb.buildFeatureType();

        if (quadrantSegments == null) {
            quadrantSegments = 8;
        }

        // build the buffer geometry generator
        BufferGenerator generator;
        if (crs != null) {
            CoordinateReferenceSystem hor = CRS.getHorizontalCRS(crs);
            if (hor instanceof GeographicCRS) {
                generator = new GeographicGenerator(center, quadrantSegments, crs);
            } else {
                Unit unit = hor.getCoordinateSystem().getAxis(0).getUnit();
                UnitConverter converter = SI.METER.getConverterTo(unit);
                generator = new MetricGenerator(center, quadrantSegments, converter);
            }
        } else {
            generator = new MetricGenerator(center, quadrantSegments, UnitConverter.IDENTITY);
        }

        // we don't expect million of directions, so we use a simple in memory collection
        SimpleFeatureCollection result = new ListFeatureCollection(schema);
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        for (int i = 0; i < distances.length; i++) {
            fb.add(generator.getBuffer(distances[i]));
            fb.add(distances[i]);
            result.add(fb.buildFeature("buffers." + (i + 1)));
        }

        return result;
    }

    /**
     * Generates a buffer
     * 
     * @author Andrea Aime - GeoSolutions
     * 
     */
    static abstract class BufferGenerator {
        Point center;

        int quadrantSegments;

        public abstract Polygon getBuffer(double distance);
    }

    /**
     * A generator that uses JTS buffer to create the buffer polygons
     */
    public class MetricGenerator extends BufferGenerator {
        UnitConverter converter;

        public MetricGenerator(Point center, Integer quadrantSegments, UnitConverter converter) {
            this.center = center;
            this.quadrantSegments = quadrantSegments;
            this.converter = converter;
        }

        @Override
        public Polygon getBuffer(double distance) {
            // convert to the target unit
            distance = converter.convert(distance);

            // buffer and return
            return (Polygon) center.buffer(distance, quadrantSegments);
        }

    }

    /**
     * Builds the appropriate buffer polygons sampling the actual buffer shape with the
     * GeodeticCalculator
     * 
     * @author Andrea Aime - GeoSolutions
     */
    public class GeographicGenerator extends BufferGenerator {
        GeometryFactory gf = new GeometryFactory();

        GeodeticCalculator calculator;

        boolean latLon;

        public GeographicGenerator(Point center, int quadrantSegments, CoordinateReferenceSystem crs) {
            this.quadrantSegments = quadrantSegments;
            this.center = center;
            this.calculator = new GeodeticCalculator(crs);
            latLon = isLatLonOrder(crs.getCoordinateSystem());
            if (latLon) {
                calculator.setStartingGeographicPoint(center.getY(), center.getX());
            } else {
                calculator.setStartingGeographicPoint(center.getX(), center.getY());
            }
        }

        @Override
        public Polygon getBuffer(double distance) {
            CoordinateSequence cs = new LiteCoordinateSequence(quadrantSegments * 4 + 1, 2);

            for (int i = 0; i < (cs.size() - 1); i++) {
                double azimuth = 360.0 * i / cs.size() - 180;
                calculator.setDirection(azimuth, distance);
                Point2D dp = calculator.getDestinationGeographicPoint();
                if (latLon) {
                    cs.setOrdinate(i, 0, dp.getY());
                    cs.setOrdinate(i, 1, dp.getX());
                } else {
                    cs.setOrdinate(i, 0, dp.getX());
                    cs.setOrdinate(i, 1, dp.getY());
                }
            }
            cs.setOrdinate(cs.size() - 1, 0, cs.getOrdinate(0, 0));
            cs.setOrdinate(cs.size() - 1, 1, cs.getOrdinate(0, 1));

            return gf.createPolygon(gf.createLinearRing(cs), null);
        }

    }

    boolean isLatLonOrder(CoordinateSystem cs) {
        int dimension = cs.getDimension();
        int longitudeDim = -1;
        int latitudeDim = -1;

        for (int i = 0; i < dimension; i++) {
            AxisDirection dir = cs.getAxis(i).getDirection().absolute();

            if (dir.equals(AxisDirection.EAST)) {
                longitudeDim = i;
            }

            if (dir.equals(AxisDirection.NORTH)) {
                latitudeDim = i;
            }
        }

        if ((longitudeDim >= 0) && (latitudeDim >= 0)) {
            if (longitudeDim > latitudeDim) {
                return true;
            }
        }

        return false;
    }

}
