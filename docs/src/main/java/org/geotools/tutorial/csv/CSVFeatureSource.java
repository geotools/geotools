/* GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
// header start
package org.geotools.tutorial.csv;

import com.csvreader.CsvReader;
import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Read-only access to CSV File.
 *
 * @author Jody Garnett (Boundless)
 */
public class CSVFeatureSource extends ContentFeatureSource {

    public CSVFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
    }

    // getDataStore start
    /** Access parent CSVDataStore. */
    public CSVDataStore getDataStore() {
        return (CSVDataStore) super.getDataStore();
    }
    // getDataStore end

    // reader start
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return new CSVFeatureReader(getState(), query);
    }

    // reader end

    // count start
    protected int getCountInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) {
            CsvReader reader = getDataStore().read();
            try {
                boolean connect = reader.readHeaders();
                if (connect == false) {
                    throw new IOException("Unable to connect");
                }
                int count = 0;
                while (reader.readRecord()) {
                    count += 1;
                }
                return count;
            } finally {
                reader.close();
            }
        }
        return -1; // feature by feature scan required to count records
    }
    // count end

    // bounds start
    /**
     * Implementation that generates the total bounds (many file formats record this information in
     * the header)
     */
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        return null; // feature by feature scan required to establish bounds
    }
    // bounds end

    // schema start
    protected SimpleFeatureType buildFeatureType() throws IOException {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(entry.getName());

        // read headers
        CsvReader reader = getDataStore().read();
        try {
            boolean success = reader.readHeaders();
            if (success == false) {
                throw new IOException("Header of CSV file not available");
            }

            // we are going to hard code a point location
            // columns like lat and lon will be gathered into a
            // Point called Location
            builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system
            builder.add("Location", Point.class);

            for (String column : reader.getHeaders()) {
                if ("lat".equalsIgnoreCase(column)) {
                    continue; // skip as it is part of Location
                }
                if ("lon".equalsIgnoreCase(column)) {
                    continue; // skip as it is part of Location
                }
                builder.add(column, String.class);
            }

            // build the type (it is immutable and cannot be modified)
            final SimpleFeatureType SCHEMA = builder.buildFeatureType();
            return SCHEMA;
        } finally {
            reader.close();
        }
    }
    // schema end

}
