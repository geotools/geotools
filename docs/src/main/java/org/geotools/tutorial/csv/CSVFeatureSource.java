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
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;

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
