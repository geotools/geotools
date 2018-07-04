/* GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.tutorial.csv2;

import com.csvreader.CsvWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.cs.AxisDirection;

/**
 * Iterator supporting writing of feature content.
 *
 * @author Jody Garnett (Boundless)
 * @author Lee Breisacher
 */
public class CSVFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    /** State of current transaction */
    private ContentState state;

    /** Delegate handing reading of original file */
    private CSVFeatureReader delegate;

    /** Temporary file used to stage output */
    private File temp;

    /** CsvWriter used for temp file output */
    private CsvWriter csvWriter;

    /** Current feature available for modification, may be null if feature removed */
    private SimpleFeature currentFeature;

    /** Flag indicating we have reached the end of the file */
    private boolean appending = false;

    /** flag to keep track of lat/lon order */
    private boolean latlon =
            DefaultGeographicCRS.WGS84
                    .getCoordinateSystem()
                    .getAxis(0)
                    .getDirection()
                    .equals(AxisDirection.NORTH);

    int latIndex = 0;
    int lngIndex = 0;
    /** Row count used to generate FeatureId when appending */
    int nextRow = 0;
    // header end
    // constructor start
    public CSVFeatureWriter(ContentState state, Query query) throws IOException {
        this.state = state;
        String typeName = query.getTypeName();
        File file = ((CSVDataStore) state.getEntry().getDataStore()).file;
        File directory = file.getParentFile();
        this.temp = File.createTempFile(typeName + System.currentTimeMillis(), "csv", directory);
        this.csvWriter = new CsvWriter(new FileWriter(this.temp), ',');
        this.delegate = new CSVFeatureReader(state, query);
        this.csvWriter.writeRecord(delegate.reader.getHeaders());
        String latField = "lat";
        String lngField = "lon";
        String[] headers = delegate.reader.getHeaders();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase(latField)) {
                latIndex = i;
            }
            if (headers[i].equalsIgnoreCase(lngField)) {
                lngIndex = i;
            }
        }
    }
    // constructor end

    // featureType start
    @Override
    public SimpleFeatureType getFeatureType() {
        return state.getFeatureType();
    }
    // featureType end

    // hasNext start
    @Override
    public boolean hasNext() throws IOException {
        if (csvWriter == null) {
            return false;
        }
        if (this.appending) {
            return false; // reader has no more contents
        }
        return delegate.hasNext();
    }
    // hasNext end

    // next start
    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        if (csvWriter == null) {
            throw new IOException("FeatureWriter has been closed");
        }
        if (this.currentFeature != null) {
            this.write(); // the previous one was not written, so do it now.
        }
        try {
            if (!appending) {
                if (delegate.reader != null && delegate.hasNext()) {
                    this.currentFeature = delegate.next();
                    return this.currentFeature;
                } else {
                    this.appending = true;
                }
            }
            SimpleFeatureType featureType = state.getFeatureType();
            String fid = featureType.getTypeName() + "." + nextRow;
            Object values[] = DataUtilities.defaultValues(featureType);

            this.currentFeature = SimpleFeatureBuilder.build(featureType, values, fid);
            return this.currentFeature;
        } catch (IllegalArgumentException invalid) {
            throw new IOException("Unable to create feature:" + invalid.getMessage(), invalid);
        }
    }
    // next end

    // remove start
    /**
     * Mark our {@link #currentFeature} feature as null, it will be skipped when written effectively
     * removing it.
     */
    public void remove() throws IOException {
        this.currentFeature = null; // just mark it done which means it will not get written out.
    }
    // remove end

    // write start
    public void write() throws IOException {
        if (this.currentFeature == null) {
            return; // current feature has been deleted
        }
        for (Property property : currentFeature.getProperties()) {
            Object value = property.getValue();
            if (value == null) {
                this.csvWriter.write("");
            } else if (value instanceof Point) {
                Point point = (Point) value;
                if (latlon && latIndex <= lngIndex) {
                    this.csvWriter.write(Double.toString(point.getX()));
                    this.csvWriter.write(Double.toString(point.getY()));
                } else {
                    this.csvWriter.write(Double.toString(point.getY()));
                    this.csvWriter.write(Double.toString(point.getX()));
                }
            } else {
                String txt = value.toString();
                this.csvWriter.write(txt);
            }
        }
        this.csvWriter.endRecord();
        nextRow++;
        this.currentFeature = null; // indicate that it has been written
    }
    // write end

    // close start
    @Override
    public void close() throws IOException {
        if (csvWriter == null) {
            throw new IOException("Writer alread closed");
        }
        if (this.currentFeature != null) {
            this.write(); // the previous one was not written, so do it now.
        }
        // Step 1: Write out remaining contents (if applicable)
        while (hasNext()) {
            next();
            write();
        }
        csvWriter.close();
        csvWriter = null;
        if (delegate != null) {
            this.delegate.close();
            this.delegate = null;
        }
        // Step 2: Replace file contents
        File file = ((CSVDataStore) state.getEntry().getDataStore()).file;

        Files.copy(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    // close end

}
