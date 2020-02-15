/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.tutorial.csv;

import com.csvreader.CsvReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class CSVFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    /** State used when reading file */
    protected ContentState state;

    /**
     * Current row number - used in the generation of FeatureId. TODO: Subclass ContentState to
     * track row
     */
    private int row;

    protected CsvReader reader;

    /** Utility class used to build features */
    protected SimpleFeatureBuilder builder;

    /** Factory class for geometry creation */
    private GeometryFactory geometryFactory;

    public CSVFeatureReader(ContentState contentState, Query query) throws IOException {
        this.state = contentState;
        CSVDataStore csv = (CSVDataStore) contentState.getEntry().getDataStore();
        reader = csv.read(); // this may throw an IOException if it could not connect
        boolean header = reader.readHeaders();
        if (!header) {
            throw new IOException("Unable to read csv header");
        }
        builder = new SimpleFeatureBuilder(state.getFeatureType());
        geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
        row = 0;
    }
    /** Access FeatureType (documenting available attributes) */
    public SimpleFeatureType getFeatureType() {
        return (SimpleFeatureType) state.getFeatureType();
    }
    // class definition end

    // read start
    /** The next feature */
    private SimpleFeature next;

    /**
     * Access the next feature (if available).
     *
     * @return SimpleFeature read from property file
     * @throws IOException If problem encountered reading file
     * @throws IllegalAttributeException for invalid data
     * @throws NoSuchElementException If hasNext() indicates no more features are available
     */
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        SimpleFeature feature;
        if (next != null) {
            feature = next;
            next = null;
        } else {
            feature = readFeature();
        }
        return feature;
    }
    /**
     * Check if additional content is available.
     *
     * @return <code>true</code> if additional content is available
     */
    public boolean hasNext() throws IOException {
        if (next != null) {
            return true;
        } else {
            next = readFeature(); // read next feature so we can check
            return next != null;
        }
    }
    // read end

    // parse start
    /** Read a line of content from CSVReader and parse into values */
    SimpleFeature readFeature() throws IOException {
        if (reader == null) {
            throw new IOException("FeatureReader is closed; no additional features can be read");
        }
        boolean read = reader.readRecord(); // read the "next" record
        if (read == false) {
            close(); // automatic close to be nice
            return null; // no additional features are available
        }
        Coordinate coordinate = new Coordinate();
        for (String column : reader.getHeaders()) {
            String value = reader.get(column);
            if ("lat".equalsIgnoreCase(column)) {
                coordinate.y = Double.valueOf(value.trim());
            } else if ("lon".equalsIgnoreCase(column)) {
                coordinate.x = Double.valueOf(value.trim());
            } else {
                builder.set(column, value);
            }
        }
        builder.set("Location", geometryFactory.createPoint(coordinate));

        return this.buildFeature();
    }

    /** Build feature using the current row number to generate FeatureId */
    protected SimpleFeature buildFeature() {
        row += 1;
        return builder.buildFeature(state.getEntry().getTypeName() + "." + row);
    }
    // parse end

    // close start
    /** Close the FeatureReader when not in use. */
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
        builder = null;
        geometryFactory = null;
        next = null;
    }
    // close end
}
