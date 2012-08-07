package org.geotools.data.wfs.impl;

import org.geotools.filter.identity.FeatureIdVersionedImpl;

class MutableFeatureId extends FeatureIdVersionedImpl {

    public MutableFeatureId(String fid) {
        super(fid, null);
    }

    public MutableFeatureId(String fid, String featureVersion) {
        super(fid, featureVersion);
    }

    public void setFeatureVersion(final String version) {
        super.featureVersion = version;
    }
}
