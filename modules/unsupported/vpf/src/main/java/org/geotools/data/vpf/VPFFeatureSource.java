package org.geotools.data.vpf;

import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * VPFFeature Source
 *
 * @author James Gambale (Alysida AI)
 */
public class VPFFeatureSource extends ContentFeatureSource {

    public VPFFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
    }

    /** Access parent VPFLibrary. */
    public VPFLibrary getDataStore() {
        return (VPFLibrary) super.getDataStore();
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return new VPFFeatureReader(getState(), query);
    }

    protected int getCountInternal(Query query) throws IOException {
        return -1; // feature by feature scan required to count records
    }

    /**
     * Implementation that generates the total bounds (many file formats record this information in
     * the header)
     */
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        return null; // feature by feature scan required to establish bounds
    }

    protected SimpleFeatureType buildFeatureType() throws IOException {
        VPFLibrary vpf = this.getDataStore();
        return vpf.getFeatureType(this.query);
    }
}
