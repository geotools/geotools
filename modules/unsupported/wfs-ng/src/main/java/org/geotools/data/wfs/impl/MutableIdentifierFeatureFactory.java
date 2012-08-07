package org.geotools.data.wfs.impl;

import java.util.Collection;

import org.geotools.feature.AbstractFeatureFactoryImpl;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory2;

/**
 * If only {@link AbstractFeatureFactoryImpl}'s filter factory were setteable this class wouldn't be
 * needed....
 */
class MutableIdentifierFeatureFactory extends AbstractFeatureFactoryImpl {

    private static FilterFactory2 MUTABLE_FIDS_FILTER_FACTORY = new FilterFactoryImpl() {
        @Override
        public MutableFeatureId featureId(String id) {
            return new MutableFeatureId(id);
        }

        @Override
        public MutableFeatureId featureId(String fid, String featureVersion) {
            return new MutableFeatureId(fid, featureVersion);
        }

    };

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Feature createFeature(Collection value, AttributeDescriptor descriptor, String id) {
        return new FeatureImpl(value, descriptor, MUTABLE_FIDS_FILTER_FACTORY.featureId(id));
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Feature createFeature(Collection value, FeatureType type, String id) {
        return new FeatureImpl(value, type, MUTABLE_FIDS_FILTER_FACTORY.featureId(id));
    }

    @Override
    public SimpleFeature createSimpleFeature(Object[] array, SimpleFeatureType type, String id) {
        if (type.isAbstract()) {
            throw new IllegalArgumentException(
                    "Cannot create an feature of an abstract FeatureType " + type.getTypeName());
        }
        return new SimpleFeatureImpl(array, type, MUTABLE_FIDS_FILTER_FACTORY.featureId(id), false);
    }
}
