/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.csv3;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.tutorial.csv3.parse.CSVIterator;
import org.geotools.tutorial.csv3.parse.CSVStrategy;

/**
 * Iterator supporting writing of feature content.
 *
 * @author Jody Garnett (Boundless)
 * @author Lee Breisacher
 */
public class CSVFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    private SimpleFeatureType featureType;
    private CSVStrategy csvStrategy;
    private CSVFileState csvFileState;

    /** Temporary file used to stage output */
    private File temp;

    /** iterator handling reading of original file */
    private CSVIterator iterator;

    /** CsvWriter used for temp file output */
    private CSVWriter csvWriter;

    /** Flag indicating we have reached the end of the file */
    private boolean appending = false;

    /** Row count used to generate FeatureId when appending */
    int nextRow = 0;

    /** Current feature available for modification. May be null if feature removed */
    private SimpleFeature currentFeature;

    // docs start CSVFeatureWriter
    public CSVFeatureWriter(CSVFileState csvFileState, CSVStrategy csvStrategy) throws IOException {
        this(csvFileState, csvStrategy, Query.ALL);
    }

    public CSVFeatureWriter(CSVFileState csvFileState, CSVStrategy csvStrategy, Query query) throws IOException {
        this.csvFileState = csvFileState;
        File file = csvFileState.getFile();
        File directory = file.getParentFile();
        String typeName = query.getTypeName();
        this.temp = File.createTempFile(typeName + System.currentTimeMillis(), ".csv", directory);
        this.temp.deleteOnExit();
        this.featureType = csvStrategy.getFeatureType();
        this.iterator = csvStrategy.iterator();
        this.csvStrategy = csvStrategy;
        this.csvWriter = new CSVWriter(new FileWriter(this.temp, StandardCharsets.UTF_8));
        this.csvWriter.writeNext(this.csvFileState.getCSVHeaders(), false);
    }
    // docs end CSVFeatureWriter

    // featureType start
    @Override
    public SimpleFeatureType getFeatureType() {
        return this.featureType;
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
        return iterator.hasNext();
    }
    // hasNext end

    // next start
    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
        if (csvWriter == null) {
            throw new IOException("Writer has been closed");
        }
        if (this.currentFeature != null) {
            this.write(); // the previous one was not written, so do it now.
        }
        try {
            if (!appending) {
                if (iterator.hasNext()) {
                    this.currentFeature = iterator.next();
                    return this.currentFeature;
                } else {
                    this.appending = true;
                }
            }
            String fid = featureType.getTypeName() + "-fid" + nextRow;
            Object[] values = DataUtilities.defaultValues(featureType);

            this.currentFeature = SimpleFeatureBuilder.build(featureType, values, fid);
            return this.currentFeature;
        } catch (IllegalArgumentException invalid) {
            throw new IOException("Unable to create feature:" + invalid.getMessage(), invalid);
        }
    }
    // next end

    // remove start
    /** Mark our {@link #currentFeature} feature as null, it will be skipped when written effectively removing it. */
    public void remove() throws IOException {
        this.currentFeature = null; // just mark it done which means it will not get written out.
    }
    // remove end

    // write start
    public void write() throws IOException {
        if (this.currentFeature == null) {
            return; // current feature has been deleted
        }

        this.csvWriter.writeNext(this.csvStrategy.encode(this.currentFeature), false);
        nextRow++;
        this.currentFeature = null; // indicate that it has been written
    }
    // write end

    // close start
    @Override
    public void close() throws IOException {
        if (csvWriter == null) {
            throw new IOException("Writer already closed");
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
        if (this.iterator != null) {
            this.iterator.close();
            this.iterator = null;
        }
        // Step 2: Replace file contents
        File file = this.csvFileState.getFile();

        Files.copy(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        temp.delete();
    }
}
