/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sfs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.store.ContentState;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.GeoJSON;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Reads a json stream using the specified layer and query
 * @author 
 */
class SFSFeatureReader implements SimpleFeatureReader {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.simplefeatureservice");
    ContentState contentState;
    FeatureJSON fjson;
    FeatureIterator<SimpleFeature> featureIterator;
    InputStream jsonStream;
    SFSLayer layer;
    SimpleFeatureBuilder fbuilder;
    Boolean hasNext;

    /**
     * Constructor 
     * @param contentState
     * @param fnQuery
     * @throws IOException
     */
    public SFSFeatureReader(ContentState contentState, SFSLayer layer, Query fnQuery, SimpleFeatureType targetSchema) throws IOException {

        this.contentState = contentState;
        this.layer = layer;

        /*Get datastore from the contentState */
        SFSDataStore ods = (SFSDataStore) contentState.getEntry().getDataStore();
        
        /* Encode the query into a protocol filter specification */
        String queryURL = null;
        if (fnQuery != null) {
            queryURL = SFSDataStoreUtil.encodeQuery(fnQuery, targetSchema);
        }

        // read the feature collection with a streaming reader
        jsonStream = ods.resourceToStream("data/" + layer.getTypeName().getLocalPart(), "mode=features&" + queryURL);
        fjson = new FeatureJSON();
        fjson.setFeatureType(contentState.getFeatureType());
        featureIterator = fjson.streamFeatureCollection(jsonStream);
        fbuilder = new SimpleFeatureBuilder(targetSchema);
    }

    /**
     * Returns the schema
     * @return SimpleFeatureType
     */
    public SimpleFeatureType getFeatureType() {

        return contentState.getFeatureType();
    }

    /**
     * Returns the next feature -- also if the axis is flipped (i.e. YX instead
     * of XY) then it is brought back to XY
     * @return SimpleFeature
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws NoSuchElementException
     */
    public SimpleFeature next() throws IOException, IllegalArgumentException, NoSuchElementException {
        try {
            /*Read in next feature*/
            SimpleFeature sf = (SimpleFeature) featureIterator.next();
    
            Geometry fg;
    
            if (!layer.isXYOrder()) {
                /* flip the geometry */
                fg = (Geometry) sf.getDefaultGeometry();
                if(fg != null) {
                    SFSDataStoreUtil.flipFeatureYX(fg);
                }
                /* flip the bounding box*/
                ArrayList al = (ArrayList) sf.getAttribute("boundedBy");
                if(al != null) {
                    SFSDataStoreUtil.flipYXInsideTheBoundingBox(al);
                }
            }
    
            // the json reader does not know about the namespaces and puts the geometry in a "geometry" attribute
            // so we need to clone the returned feature and adapt it a bit
            for (AttributeDescriptor at : fbuilder.getFeatureType().getAttributeDescriptors()) {
                if(at instanceof GeometryDescriptor) {
                    fbuilder.add(sf.getDefaultGeometry());
                } else {
                    fbuilder.add(sf.getAttribute(at.getLocalName()));
                }
            }
            return fbuilder.buildFeature(sf.getID());
        } finally {
            hasNext = null;
        }
    }

    /**
     * If there is another feature in the collection return true
     * @return boolean
     * @throws IOException
     */
    public boolean hasNext() throws IOException {
        if(hasNext == null) {
            // work around a bug in the json reader
            hasNext = featureIterator.hasNext();
        } 
        return hasNext;
    }

    /**
     * close the iterator
     * @throws IOException
     */
    public void close() throws IOException {
        jsonStream.close();
        featureIterator.close();
    }
    
    public static void main(String[] args) throws Exception {
        String json = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[43.3599,-11.6515]},\"properties\": {},\"id\":\"null\"}]}";
        FeatureJSON fj = new FeatureJSON();
//        URL url = new URL("http://www.glews-test.net/eisurvtest/data/outbreakMarker?mode=features&bbox=-218.20632812500003%2C-85.6734453125%2C361.860109375%2C116.8704140625&limit=1");
//        InputStream is = url.openStream();
        System.out.println(fj.readFeature(json));
    }
}
