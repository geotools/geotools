/*
 * Copyright (C) 2026 B3Partners B.V.
 *
 * SPDX-License-Identifier: MIT
 */
package org.geotools.data.excel;

import java.io.IOException;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

public class ExcelFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    private SimpleFeatureType featureType;

    @Override
    public SimpleFeatureType getFeatureType() {
        return this.featureType;
    }

    @Override
    public SimpleFeature next() throws IOException {
        return null;
    }

    @Override
    public void remove() throws IOException {}

    @Override
    public void write() throws IOException {}

    @Override
    public boolean hasNext() throws IOException {
        return false;
    }

    @Override
    public void close() throws IOException {}
}
