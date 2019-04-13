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
package org.geotools.data.wfs.internal;

import java.io.IOException;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

public class GetFeatureResponse extends WFSResponse {

    private final GetParser<SimpleFeature> features;

    private boolean featuresReturned;

    public GetFeatureResponse(
            WFSRequest originatingRequest,
            HTTPResponse httpResponse,
            GetParser<SimpleFeature> features)
            throws ServiceException, IOException {

        super(originatingRequest, httpResponse);
        this.features = features;
    }

    public GetParser<SimpleFeature> getFeatures() {
        return features;
    }

    public GetParser<SimpleFeature> getFeatures(GeometryFactory geometryFactory) {
        if (featuresReturned) {
            throw new IllegalStateException("getFeatures can be called only once");
        }
        GetParser<SimpleFeature> features = getFeatures();
        if (geometryFactory != null) {
            features.setGeometryFactory(geometryFactory);
        }
        featuresReturned = true;
        return features;
    }

    public GetParser<SimpleFeature> getSimpleFeatures(GeometryFactory geometryFactory) {
        GetParser<SimpleFeature> rawFeatures = getFeatures(geometryFactory);
        FeatureType featureType = rawFeatures.getFeatureType();
        if (featureType instanceof SimpleFeatureType) {
            return rawFeatures;
        }

        throw new UnsupportedOperationException("implementa adapting to SimpleFeature");
    }
}
