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
package org.geotools.process.vector;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.DecoratingSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.vector.AggregateProcess.AggregationFunction;
import org.geotools.process.vector.AggregateProcess.Results;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Provides statistics for the distribution of a certain quantity in a set of reference areas. The
 * data layer must be a point layer, the reference layer must be a polygonal one
 */
@DescribeProcess(
        title = "Vector Zonal Statistics",
        description =
                "Computes statistics for the distribution of a given attribute in a set of polygonal zones.  Input must be points.")
public class VectorZonalStatistics implements VectorProcess {

    @DescribeResult(
            name = "statistics",
            description =
                    "A feature collection with the attributes of the zone layer (prefixed by 'z_') and the statistics fields count,min,max,sum,avg,stddev")
    public SimpleFeatureCollection execute(
            @DescribeParameter(name = "data", description = "Input collection of point features")
                    SimpleFeatureCollection data,
            @DescribeParameter(
                            name = "dataAttribute",
                            description = "Attribute to use for computing statistics")
                    String dataAttribute,
            @DescribeParameter(
                            name = "zones",
                            description = "Zone polygon features for which to compute statistics")
                    SimpleFeatureCollection zones) {

        AttributeDescriptor dataDescriptor = data.getSchema().getDescriptor(dataAttribute);
        if (dataDescriptor == null) {
            throw new IllegalArgumentException(
                    "Attribute " + dataAttribute + " not found in " + data.getSchema());
        }

        return new ZonalStatisticsCollection(data, dataAttribute, zones);
    }

    /**
     * A feature collection that computes zonal statitics in a streaming fashion
     *
     * @author Andrea Aime - OpenGeo
     */
    static class ZonalStatisticsCollection extends DecoratingSimpleFeatureCollection {
        SimpleFeatureCollection data;

        String dataAttribute;

        SimpleFeatureType targetSchema;

        public ZonalStatisticsCollection(
                SimpleFeatureCollection data, String dataAttribute, SimpleFeatureCollection zones) {
            super(zones);
            this.dataAttribute = dataAttribute;
            this.data = data;

            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            for (AttributeDescriptor att : zones.getSchema().getAttributeDescriptors()) {
                tb.minOccurs(att.getMinOccurs());
                tb.maxOccurs(att.getMaxOccurs());
                tb.restrictions(att.getType().getRestrictions());
                if (att instanceof GeometryDescriptor) {
                    GeometryDescriptor gatt = (GeometryDescriptor) att;
                    tb.crs(gatt.getCoordinateReferenceSystem());
                }
                tb.add("z_" + att.getLocalName(), att.getType().getBinding());
            }
            tb.add("count", Long.class);
            tb.add("min", Double.class);
            tb.add("max", Double.class);
            tb.add("sum", Double.class);
            tb.add("avg", Double.class);
            tb.add("stddev", Double.class);
            tb.setName(zones.getSchema().getName());
            targetSchema = tb.buildFeatureType();
        }

        @Override
        public SimpleFeatureType getSchema() {
            return targetSchema;
        }

        @Override
        public SimpleFeatureIterator features() {
            return new ZonalStatisticsIterator(
                    delegate.features(), dataAttribute, data, targetSchema);
        }
    }

    /** An iterator computing statistics as we go */
    static class ZonalStatisticsIterator implements SimpleFeatureIterator {
        Set<AggregationFunction> FUNCTIONS =
                new HashSet<AggregationFunction>() {
                    {
                        add(AggregationFunction.Count);
                        add(AggregationFunction.Max);
                        add(AggregationFunction.Min);
                        add(AggregationFunction.Sum);
                        add(AggregationFunction.Average);
                        add(AggregationFunction.StdDev);
                    }
                };

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        SimpleFeatureIterator zones;

        String dataAttribute;

        SimpleFeatureCollection data;

        SimpleFeatureBuilder builder;

        String dataGeomName;

        public ZonalStatisticsIterator(
                SimpleFeatureIterator zones,
                String dataAttribute,
                SimpleFeatureCollection data,
                SimpleFeatureType targetSchema) {
            this.zones = zones;
            this.dataAttribute = dataAttribute;
            this.data = data;
            this.builder = new SimpleFeatureBuilder(targetSchema);
            this.dataGeomName = data.getSchema().getGeometryDescriptor().getLocalName();
        }

        @Override
        public void close() {
            zones.close();
        }

        @Override
        public boolean hasNext() {
            return zones.hasNext();
        }

        @Override
        public SimpleFeature next() throws NoSuchElementException {
            // grab the current zone
            SimpleFeature zone = zones.next();

            try {
                // grab the geometry and eventually reproject it
                Geometry zoneGeom = (Geometry) zone.getDefaultGeometry();
                CoordinateReferenceSystem dataCrs = data.getSchema().getCoordinateReferenceSystem();
                CoordinateReferenceSystem zonesCrs =
                        builder.getFeatureType()
                                .getGeometryDescriptor()
                                .getCoordinateReferenceSystem();
                if (!CRS.equalsIgnoreMetadata(zonesCrs, dataCrs)) {
                    zoneGeom =
                            JTS.transform(zoneGeom, CRS.findMathTransform(zonesCrs, dataCrs, true));
                }

                // build the filter and gather the statistics
                Filter areaFilter = ff.within(ff.property(dataGeomName), ff.literal(zoneGeom));
                SimpleFeatureCollection zoneCollection = data.subCollection(areaFilter);
                Results stats =
                        new AggregateProcess()
                                .execute(zoneCollection, dataAttribute, FUNCTIONS, true, null);

                // build the resulting feature
                builder.addAll(zone.getAttributes());
                if (stats != null) {
                    builder.add(stats.getCount());
                    builder.add(stats.getMin());
                    builder.add(stats.getMax());
                    builder.add(stats.getSum());
                    builder.add(stats.getAverage());
                    builder.add(stats.getStandardDeviation());
                }
                return builder.buildFeature(zone.getID());
            } catch (Exception e) {
                throw new ProcessException("Failed to compute statistics on feature " + zone, e);
            }
        }
    }
}
