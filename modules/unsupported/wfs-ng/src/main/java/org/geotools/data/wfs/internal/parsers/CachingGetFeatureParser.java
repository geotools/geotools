/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.parsers;

import java.io.IOException;
import java.util.Queue;

import org.geotools.data.wfs.internal.GetFeatureParser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;

public class CachingGetFeatureParser implements GetFeatureParser {

    private final GetFeatureParser streamingParser;

    private final int queueSize;

    private Queue<SimpleFeature> queue;

    public CachingGetFeatureParser(final GetFeatureParser streamingParser, final int queueSize) {
        this.streamingParser = streamingParser;
        this.queueSize = queueSize;
    }

    @Override
    public int getNumberOfFeatures() {
        return streamingParser.getNumberOfFeatures();
    }

    @Override
    public void close() throws IOException {
        streamingParser.close();
    }

    @Override
    public FeatureType getFeatureType() {
        return streamingParser.getFeatureType();
    }

    @Override
    public void setGeometryFactory(GeometryFactory geometryFactory) {
        streamingParser.setGeometryFactory(geometryFactory);
    }

    @Override
    public SimpleFeature parse() throws IOException {
        SimpleFeature feature = queue.poll();
        return feature;
    }
}
