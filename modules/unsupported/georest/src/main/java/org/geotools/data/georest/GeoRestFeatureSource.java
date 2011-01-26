/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.georest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * FeatureSource implementation for the {@link GeoRestDataStore}. This means that this model is
 * read-only.
 * </p>
 * 
 * @author Pieter De Graef, Geosparc
 */
public class GeoRestFeatureSource extends ContentFeatureSource implements SimpleFeatureSource {

    private URL url;

    public GeoRestFeatureSource(ContentEntry entry, Query query) throws IOException {
        super(entry, query);

        String base = ((GeoRestDataStore) entry.getDataStore()).getUrl().toString();
        if (base.endsWith("/")) {
            url = new URL(base + entry.getName());
        } else {
            url = new URL(base + "/" + entry.getName());
        }
    }

    @Override
    public GeoRestDataStore getDataStore() {
        return (GeoRestDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        ReferencedEnvelope env = new ReferencedEnvelope(query.getCoordinateSystem());
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReaderInternal(query);
        if (reader != null) {
            while (reader.hasNext()) {
                Geometry geometry = (Geometry) reader.next().getDefaultGeometry();
                env.expandToInclude(geometry.getEnvelopeInternal());
            }
        }
        return env;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (query.getFilter() == null || query.getFilter().equals(Filter.INCLUDE)) {
            URL countUrl = new URL(url.toString() + "/count");
            String count = streamToString(countUrl.openStream());
            try {
                return Integer.parseInt(count);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReaderInternal(query);
        int count = 0;
        while (reader.hasNext()) {
            reader.next();
            count++;
        }
        reader.close();
        return count;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return new GeoRestFeatureReader(getState(), query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        FeatureJSON fjson = new FeatureJSON();
        InputStream in = new URL(url.toString() + "?limit=1").openStream();
        SimpleFeature feature = fjson.readFeature(new StringReader(streamToString(in)));
        return feature.getFeatureType();
    }

    // Added methods:

    protected URL getUrl() {
        return url;
    }

    protected String streamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } finally {
                is.close();
            }
            return sb.toString();
        }
        return "";
    }
}
