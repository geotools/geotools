package org.geotools.data.csv;

import java.io.IOException;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.csvreader.CsvReader;
import com.vividsolutions.jts.geom.Point;

public class CSVFeatureSource extends ContentFeatureSource {
    
    public CSVFeatureSource(ContentEntry entry, Query query) {
        super(entry,query);
    }
    /**
     * Access parent CSVDataStore
     */
    public CSVDataStore getDataStore(){
        return (CSVDataStore) super.getDataStore();
    }
    
    /**
     * Implementation that generates the total bounds
     * (many file formats record this information in the header)
     */
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        ReferencedEnvelope bounds = new ReferencedEnvelope( getSchema().getCoordinateReferenceSystem() );
        
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader = getReaderInternal(query);
        try {
            while( featureReader.hasNext() ){
                SimpleFeature feature = featureReader.next();
                bounds.include( feature.getBounds() );
            }
        }
        finally {
            featureReader.close();
        }
        return bounds;
    }

    protected int getCountInternal(Query query) throws IOException {
        CsvReader reader = getDataStore().read();
        try {
            boolean connect = reader.readHeaders();
            if( connect == false ){
                throw new IOException("Unable to connect");
            }
            int count = 0;
            while( reader.readRecord() ){
                count += 1;
            }
            return count;
        }
        finally {
            reader.close();
        }
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return new CSVFeatureReader( getState(), query );
    }

    protected SimpleFeatureType buildFeatureType() throws IOException {
        
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName( entry.getName() );
        
        // read headers
        CsvReader reader = getDataStore().read();
        try {
            boolean success = reader.readHeaders();
            if( success == false ){
                throw new IOException("Header of CSV file not available");
            }
            
            // we are going to hard code a point location
            // columns like lat and lon will be gathered into a
            // Point called Location
            builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system
            builder.add("Location", Point.class );
            
            for( String column : reader.getHeaders() ){
                if( "lat".equalsIgnoreCase(column)){
                    continue; // skip as it is part of Location
                }
                if( "lon".equalsIgnoreCase(column)){
                    continue; // skip as it is part of Location
                }
                builder.add(column, String.class);
            }
            
            // build the type (it is immutable and cannot be modified)
            final SimpleFeatureType SCHEMA = builder.buildFeatureType();
            return SCHEMA;
        }
        finally {
            reader.close();
        }
    }

}
