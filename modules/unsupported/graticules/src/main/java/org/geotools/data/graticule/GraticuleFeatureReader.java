/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.graticule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureReader;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.graticule.gridsupport.LineFeatureBuilder;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.ortholine.LineOrientation;
import org.geotools.grid.ortholine.OrthoLineBuilder;
import org.geotools.grid.ortholine.OrthoLineDef;

public class GraticuleFeatureReader implements SimpleFeatureReader {
    private final List<Double> steps;
    private SimpleFeatureSource currentGrid;
    private SimpleFeatureIterator delegate;
    SimpleFeatureType schema;
    Query query;
    int level = 0;

    public GraticuleFeatureReader(GraticuleFeatureSource fs, Query query) throws IOException {
        this.query = query;
        this.steps = fs.getSteps();
        schema = fs.getSchema();
        currentGrid = buildGrid(fs.getBounds());
        delegate = currentGrid.getFeatures(query).features();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return schema;
    }

    public SimpleFeatureIterator getDelegate() {
        return delegate;
    }

    @Override
    public SimpleFeature next() throws IllegalArgumentException, NoSuchElementException {
        SimpleFeature next = delegate.next();
        return next;
    }

    @Override
    public boolean hasNext() throws IOException {
        return delegate.hasNext();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    private SimpleFeatureSource buildGrid(ReferencedEnvelope box) {
        List<SimpleFeature> lines = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            double step = steps.get(i);

            // vertical (longitude) lines
            List<OrthoLineDef> vertLines = new ArrayList<>();
            vertLines.add(new OrthoLineDef(LineOrientation.VERTICAL, i, step));
            // horizontal (latitude) lines
            List<OrthoLineDef> horLines = new ArrayList<>();
            horLines.add(new OrthoLineDef(LineOrientation.HORIZONTAL, i, step));

            OrthoLineBuilder lineBuilder = new OrthoLineBuilder(box);
            LineFeatureBuilder gridBuilder = new LineFeatureBuilder(schema);

            final ListFeatureCollection fc = new ListFeatureCollection(schema);
            lineBuilder.buildGrid(vertLines, gridBuilder, -1, fc);
            lines.addAll(updatePosition(fc));
            fc.clear();
            lineBuilder.buildGrid(horLines, gridBuilder, -1, fc);
            lines.addAll(updatePosition(fc));
        }

        return DataUtilities.source(new ListFeatureCollection(schema, lines));
    }

    private List<SimpleFeature> updatePosition(ListFeatureCollection fc) {
        List<SimpleFeature> list = DataUtilities.list(fc);
        int i = 0;
        for (SimpleFeature f : list) {
            String position = LineFeatureBuilder.SEQUENCE_MID;
            if (i == 0) {
                position = LineFeatureBuilder.SEQUENCE_START;
            } else if (i == list.size() - 1) {
                position = LineFeatureBuilder.SEQUENCE_END;
            }
            f.setAttribute(LineFeatureBuilder.SEQUENCE, position);
            i++;
        }

        return list;
    }
}
