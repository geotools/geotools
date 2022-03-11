/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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
package org.geotools.feature.collection;

import java.util.logging.Logger;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * SimpleFeatureCollection wrapper that clip (crops) features according to the clip geometry passed.
 * Can preserve the Z dimension.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ClippedFeatureCollection extends DecoratingSimpleFeatureCollection {
    protected Geometry clip;
    protected SimpleFeatureType targetSchema;
    protected boolean preserveZ;

    static final Logger LOGGER = Logging.getLogger(ClippedFeatureCollection.class);

    public ClippedFeatureCollection(
            SimpleFeatureCollection delegate, Geometry clip, boolean preserveZ) {
        super(delegate);
        this.clip = clip;
        this.targetSchema = buildTargetSchema(delegate.getSchema());
        this.preserveZ = preserveZ;
    }

    @Override
    public SimpleFeatureType getSchema() {
        return targetSchema;
    }

    /** When clipping lines and polygons can turn into multilines and multipolygons */
    private SimpleFeatureType buildTargetSchema(SimpleFeatureType schema) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            if (ad instanceof GeometryDescriptor) {
                GeometryDescriptor gd = (GeometryDescriptor) ad;
                Class<?> binding = ad.getType().getBinding();
                if (Point.class.isAssignableFrom(binding)
                        || GeometryCollection.class.isAssignableFrom(binding)) {
                    tb.add(ad);
                } else {
                    Class target;
                    if (LineString.class.isAssignableFrom(binding)) {
                        target = MultiLineString.class;
                    } else if (Polygon.class.isAssignableFrom(binding)) {
                        target = MultiPolygon.class;
                    } else {
                        target = Geometry.class;
                    }
                    tb.minOccurs(ad.getMinOccurs());
                    tb.maxOccurs(ad.getMaxOccurs());
                    tb.nillable(ad.isNillable());
                    tb.add(ad.getLocalName(), target, gd.getCoordinateReferenceSystem());
                }
            } else {
                tb.add(ad);
            }
        }
        tb.setName(schema.getName());
        return tb.buildFeatureType();
    }

    @Override
    public SimpleFeatureIterator features() {
        return new ClippedFeatureIterator(delegate.features(), clip, getSchema(), preserveZ);
    }

    @Override
    public int size() {
        try (SimpleFeatureIterator fi = features()) {
            int count = 0;
            while (fi.hasNext()) {
                fi.next();
                count++;
            }
            return count;
        }
    }
}
