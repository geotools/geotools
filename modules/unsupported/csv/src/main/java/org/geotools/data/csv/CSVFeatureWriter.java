package org.geotools.data.csv;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

// Obviously, WIP...
public class CSVFeatureWriter extends CSVFeatureReader implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    public CSVFeatureWriter(ContentState contentState) throws IOException {
        super(contentState);
    }

    public CSVFeatureWriter(ContentState contentState, Query query, int flags) throws IOException {
        this(contentState);
    }

    public void remove() throws IOException {
        // TODO Auto-generated method stub
        System.out.println("remove");
    }

    public void write() throws IOException {
        // TODO Auto-generated method stub
        System.out.println("write");
    }

    @Override
    public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
        System.out.println("next");
        return super.next();
    }

}
