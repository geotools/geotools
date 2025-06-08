/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2015, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.data.csv;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import org.apache.commons.io.FilenameUtils;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.csv.parse.CSVIterator;
import org.geotools.data.csv.parse.CSVStrategy;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.wkt.Formattable;

/**
 * Iterator supporting writing of feature content.
 *
 * @author Jody Garnett (Boundless)
 * @author Lee Breisacher
 * @author Ian Turton (Astun)
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
        this.csvWriter = new CSVWriter(
                new FileWriter(this.temp, StandardCharsets.UTF_8),
                csvStrategy.getSeparator(),
                csvStrategy.getQuotechar(),
                csvStrategy.getEscapechar(),
                csvStrategy.getLineSeparator());
        this.csvWriter.writeNext(this.csvFileState.getCSVHeaders(), csvStrategy.isQuoteAllFields());
    }

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
    @Override
    public void remove() throws IOException {
        this.currentFeature = null; // just mark it done which means it will not get written out.
    }
    // remove end

    // write start
    @Override
    public void write() throws IOException {
        if (this.currentFeature == null) {
            return; // current feature has been deleted
        }
        this.csvWriter.writeNext(this.csvStrategy.encode(this.currentFeature), csvStrategy.isQuoteAllFields());
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

        if (csvStrategy.isWritePrj()) {
            File prjFile = new File(file.getParent(), FilenameUtils.getBaseName(file.getName()) + ".prj");
            if (prjFile.exists()) {
                prjFile.delete();
            }
            try (FileWriter writer = new FileWriter(prjFile, StandardCharsets.UTF_8)) {
                writer.write(((Formattable) csvFileState.getCrs()).toWKT(Citations.EPSG, 2));
            }
        }
        temp.delete();
    }
}
