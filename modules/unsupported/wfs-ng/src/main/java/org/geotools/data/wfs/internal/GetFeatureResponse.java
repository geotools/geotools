package org.geotools.data.wfs.internal;

import java.io.IOException;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;

public class GetFeatureResponse extends WFSResponse {

    private final GetFeatureParser features;

    private boolean featuresReturned;

    public GetFeatureResponse(WFSRequest originatingRequest, HTTPResponse httpResponse,
            GetFeatureParser features) throws ServiceException, IOException {

        super(originatingRequest, httpResponse);
        this.features = features;

    }

    public GetFeatureParser getFeatures() {
        return features;
    }

    public GetFeatureParser getFeatures(GeometryFactory geometryFactory) {
        if (featuresReturned) {
            throw new IllegalStateException("getFeatures can be called only once");
        }
        GetFeatureParser features = getFeatures();
        if (geometryFactory != null) {
            features.setGeometryFactory(geometryFactory);
        }
        featuresReturned = true;
        return features;
    }

    public GetFeatureParser getSimpleFeatures(GeometryFactory geometryFactory) {
        GetFeatureParser rawFeatures = getFeatures(geometryFactory);
        FeatureType featureType = rawFeatures.getFeatureType();
        if (featureType instanceof SimpleFeatureType) {
            return rawFeatures;
        }

        throw new UnsupportedOperationException("implementa adapting to SimpleFeature");
    }

}
