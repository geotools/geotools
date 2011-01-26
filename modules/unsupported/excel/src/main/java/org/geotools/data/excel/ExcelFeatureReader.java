package org.geotools.data.excel;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.data.FeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

class ExcelFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    private ExcelFeatureSource source;
    
    private Iterator<SimpleFeature> iterator;

    private ArrayList<SimpleFeature> features;

    public ExcelFeatureReader(ArrayList<SimpleFeature> features2, ExcelFeatureSource excelFeatureSource) {
        features = features2;
        source = excelFeatureSource;
        iterator = features.iterator();
    }

    

    public SimpleFeature next() throws IOException, IllegalArgumentException,
            NoSuchElementException {
        // TODO Auto-generated method stub
        return iterator.next();
    }

    public boolean hasNext() throws IOException {
        // TODO Auto-generated method stub
        return iterator.hasNext();
    }

    public void close() throws IOException {
        // TODO Auto-generated method stub
        iterator = features.iterator();
    }



    /* (non-Javadoc)
     * @see org.geotools.data.FeatureReader#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        // TODO Auto-generated method stub
        return source.getSchema();
    }

}
