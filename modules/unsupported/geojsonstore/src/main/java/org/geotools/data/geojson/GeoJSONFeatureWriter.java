package org.geotools.data.geojson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GeoJSONFeatureWriter
        implements FeatureWriter<SimpleFeatureType, SimpleFeature>, AutoCloseable {
    Logger LOGGER = Logging.getLogger("org.geotools.data.geojson");
    /** State of current transaction */
    private ContentState state;

    /** Delegate handing reading of original file */
    private GeoJSONFeatureReader delegate;

    /** Temporary file used to stage output */
    private File temp;

    /** CsvWriter used for temp file output */
    private GeoJSONWriter writer;

    /** Current feature available for modification, may be null if feature removed */
    private SimpleFeature currentFeature;

    /** Flag indicating we have reached the end of the file */
    private boolean appending = false;

    private int nextRow = 0;

    public GeoJSONFeatureWriter(ContentEntry entry, Query query) throws IOException {
        this.state = entry.getState(Transaction.AUTO_COMMIT);
        String typeName = query.getTypeName();
        File file = URLs.urlToFile(((GeoJSONDataStore) state.getEntry().getDataStore()).getUrl());
        File directory = file.getParentFile();
        this.temp =
                File.createTempFile(typeName + System.currentTimeMillis(), "geojson", directory);
        this.writer = new GeoJSONWriter(new FileOutputStream(this.temp));
        this.delegate = new GeoJSONFeatureReader(state, query);
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return state.getFeatureType();
    }

    @Override
    public boolean hasNext() throws IOException {
        if (writer == null) {
            return false;
        }
        if (this.appending) {
            return false;
        }
        return delegate.hasNext();
    }

    @Override
    public SimpleFeature next() throws IOException {
        if (writer == null) {
            throw new IOException("FeatureWriter has been closed");
        }
        if (this.currentFeature != null) {
            LOGGER.info("writing " + currentFeature.getID());
            this.write();
        }
        try {
            if (!appending) {
                if (delegate.reader != null && delegate.hasNext()) {
                    this.currentFeature = delegate.next();
                    return this.currentFeature;
                } else {
                    this.appending = true;
                    LOGGER.info("Now appending");
                }
            }
            SimpleFeatureType featureType = state.getFeatureType();
            String fid = featureType.getTypeName() + "." + nextRow;
            Object values[] = DataUtilities.defaultValues(featureType);

            this.currentFeature = SimpleFeatureBuilder.build(featureType, values, fid);
            return this.currentFeature;
        } catch (IllegalArgumentException e) {
            throw new IOException("Unable to create feature: " + e.getLocalizedMessage(), e);
        }
    }

    @Override
    public void remove() throws IOException {
        this.currentFeature = null;
    }

    @Override
    public void write() throws IOException {
        if (this.currentFeature == null) {
            return; // current feature has been deleted
        }
        writer.write(currentFeature);
        nextRow++;
        this.currentFeature = null; // indicate that it has been written
    }

    @Override
    public void close() throws IOException {
        if (writer == null) {
            throw new IOException("FeatureWriter already closed");
        }
        if (this.currentFeature != null) {
            this.write();
        }
        while (hasNext()) {
            next();
            write();
        }
        if (delegate != null) {
            this.delegate.close();
            this.delegate = null;
        }
        File file = URLs.urlToFile(((GeoJSONDataStore) state.getEntry().getDataStore()).getUrl());

        Files.copy(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
