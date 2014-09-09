/* GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.data.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.csvreader.CsvWriter;
import com.vividsolutions.jts.geom.Point;

/**
 * Iterator supporting writing of feature content.
 *
 * @author Jody Garnett (Boundless)
 * @author Lee Breisacher
 */
public class CSVFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    private CSVFeatureReader delegate;

    private CsvWriter csvWriter;

    private File temp;

    private SimpleFeature currentFeature;

    private ContentState state;

    private boolean appending = false;

//    private boolean copying = false;

    public CSVFeatureWriter(ContentState state, Query query, boolean append) throws IOException {
        this.state = state;

        String typeName = query.getTypeName();
        File file = ((CSVDataStore) state.getEntry().getDataStore()).file;
        File directory = file.getParentFile();
        this.temp = File.createTempFile(typeName + System.currentTimeMillis(), "csv", directory);
        this.csvWriter = new CsvWriter(new FileWriter(this.temp), ',');
        if (append) {
            Files.copy(file.toPath(), temp.toPath(), StandardCopyOption.REPLACE_EXISTING );
            // skip through the contents to the end
//            this.copying = true;
//            try {
//                while (reader.hasNext()) {
//                    this.currentFeature = reader.next();
//                    this.write();
//                }
//            }
//            finally {
//                this.copying = false;
//            }
        }
        else {
            this.delegate = new CSVFeatureReader(state,query);
            this.csvWriter.writeRecord(delegate.reader.getHeaders());
        }
        this.appending = append;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return state.getFeatureType();
    }

    @Override
    public boolean hasNext() throws IOException {
        if( csvWriter == null ){
            throw new IOException("Writer has been closed");
        }
        if (this.appending) {
            return false; // reader has no more contents
        }
        return delegate.hasNext();
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        if( csvWriter == null ){
            throw new IOException("Writer has been closed");
        }
        if (this.currentFeature != null) {
            this.write(); // the previous one was not written, so do it now.
        }
        try {
            if( !appending ){
                if( delegate.reader != null && delegate.hasNext() ){
                    this.currentFeature = delegate.next();
                    return this.currentFeature;
                }
                else {
                    this.appending = true;
                }
            }
            SimpleFeatureType featureType = state.getFeatureType();
            String fid = featureType.getTypeName()+"."+System.currentTimeMillis();
            Object values[] = DataUtilities.defaultValues( featureType );
            
            this.currentFeature = SimpleFeatureBuilder.build( featureType, values, fid );
            return this.currentFeature;
        }
        catch (IllegalArgumentException invalid ){
            throw new IOException("Unable to create feature:"+invalid.getMessage(),invalid);
        }
    }

    /**
     * Mark our {@link #currentFeature} feature as null, it will be skipped when written effectively removing it.
     */
    public void remove() throws IOException {
        this.currentFeature = null; // just mark it done which means it will not get written out.
    }

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
                this.csvWriter.write(Double.toString(point.getX()));
                this.csvWriter.write(Double.toString(point.getY()));
            } else {
                String txt = value.toString();
                this.csvWriter.write(txt);
            }
        }
        this.csvWriter.endRecord();
        this.currentFeature = null; // indicate that it has been written
    }

    @Override
    public void close() throws IOException {
        if (this.currentFeature != null) {
            this.write(); // the previous one was not written, so do it now.
        }
        if( delegate != null ){
            this.delegate.close();
        }
        this.csvWriter.close();
        
        File file = ((CSVDataStore) state.getEntry().getDataStore()).file;
        
        Files.copy(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING );
    }


}
