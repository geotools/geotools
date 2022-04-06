/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.process;

import java.util.Map;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.feature.FeatureToFeatureProcess;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * Process which buffers an entire feature collection.
 *
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 */
public class BufferFeatureCollectionProcess extends FeatureToFeatureProcess {

    /** Constructor */
    public BufferFeatureCollectionProcess(BufferFeatureCollectionFactory factory) {
        super(factory);
    }

    @Override
    protected void processFeature(SimpleFeature feature, Map<String, Object> input)
            throws Exception {
        Double buffer = (Double) input.get(BufferFeatureCollectionFactory.BUFFER.key);

        Geometry g = (Geometry) feature.getDefaultGeometry();
        g = g.buffer(buffer);

        if (g instanceof Polygon) {
            g = g.getFactory().createMultiPolygon(new Polygon[] {(Polygon) g});
        }

        feature.setDefaultGeometry(g);
    }

    @Override
    protected SimpleFeatureType getTargetSchema(
            SimpleFeatureType sourceSchema, Map<String, Object> input) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        for (AttributeDescriptor ad : sourceSchema.getAttributeDescriptors()) {
            GeometryDescriptor defaultGeometry = sourceSchema.getGeometryDescriptor();
            if (ad == defaultGeometry) {
                tb.add(
                        ad.getName().getLocalPart(),
                        MultiPolygon.class,
                        defaultGeometry.getCoordinateReferenceSystem());
            } else {
                tb.add(ad);
            }
        }
        tb.setName(sourceSchema.getName());
        return tb.buildFeatureType();
    }
}
