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
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

class SFSFeatureSource extends ContentFeatureSource implements SimpleFeatureSource {

    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.simplefeatureservice");
    Map<String, String> hint = Collections.emptyMap();
    ContentEntry contentEntry;
    SFSDataStore ods;
    SFSLayer layer;

    /**
     * Constructor for OpenDataStoreFeatureSource
     * @param fnEntry
     * @param fnQuery
     * @throws IOException
     */
    public SFSFeatureSource(ContentEntry fnEntry) throws IOException {

        super(fnEntry, Query.ALL);

        this.contentEntry = fnEntry;

        ods = (SFSDataStore) contentEntry.getDataStore();
        this.layer = ods.getLayer(contentEntry.getName());
    }

    /**
     * This method get the Bound of the Layer -- using /mode=bounds
     * portions of code has been borrowed from
     * https://svn.osgeo.org/geotools/trunk/modules/library/jdbc/src/main/java/org/geotools/jdbc/JDBCFeatureSource.java
     * @param fnQuery
     * @return bounds in terms of reference Envelope
     * @throws IOException
     */
    @Override
    public ReferencedEnvelope getBoundsInternal(Query fnQuery) throws IOException {
        ReferencedEnvelope env = new ReferencedEnvelope(layer.getCoordinateReferenceSystem());

        /* Spilt the filter into two parts pre and post */
        Filter[] split = splitFilter(fnQuery.getFilter());

        Filter preFilter = split[0];
        Filter postFilter = split[1];

        if ((postFilter != null) && (postFilter != Filter.INCLUDE) || preFilter == Filter.EXCLUDE) {
            return null;
        } else {
            Query tmpPreQ = new Query(fnQuery);
            tmpPreQ.setFilter(preFilter);
            String strQuery = SFSDataStoreUtil.encodeQuery(tmpPreQ, getSchema());

            /* Create the URL */
            String bboxString = ods.resourceToString("data/" + layer.getTypeName().getLocalPart(), "mode=bounds&" + strQuery);
            JSONArray bbox;
            try {
                bbox = (JSONArray) new JSONParser().parse(bboxString);
            } catch(org.json.simple.parser.ParseException e) {
                throw (IOException) new IOException("Failed to parse the bbox JSON array:" + bboxString).initCause(e);
            }

            if (bbox.size() == 4) {
                if (layer.isXYOrder()) {
                    env.init(((Number) bbox.get(0)).doubleValue(),
                            ((Number) bbox.get(2)).doubleValue(),
                            ((Number) bbox.get(1)).doubleValue(),
                            ((Number) bbox.get(3)).doubleValue());
                } else {
                    env.init(((Number) bbox.get(1)).doubleValue(),
                            ((Number) bbox.get(3)).doubleValue(),
                            ((Number) bbox.get(0)).doubleValue(),
                            ((Number) bbox.get(2)).doubleValue());
                }
            } else {
                throw new IOException("The returned bound was not of size 4 but of size: " + bbox.size());
            }


        }
        return env;
    }

    /**
     * This method get the counts of the feature-set which satisfy the fnQuery
     * portions of this code has been borrowed from
     * https://svn.osgeo.org/geotools/trunk/modules/library/jdbc/src/main/java/org/geotools/jdbc/JDBCFeatureSource.java
     * @param fnQuery
     * @return count
     * @throws IOException
     */
    @Override
    protected int getCountInternal(Query fnQuery) throws IOException {

        int count = 0;
        /* Spilt the filter into two parts pre and post */
        Filter[] split = splitFilter(fnQuery.getFilter());

        Filter preFilter = split[0];
        Filter postFilter = split[1];

        if ((postFilter != null) && (postFilter != Filter.INCLUDE)) {

            return -1;

        } else {

            Query tmpPreQ = new Query(fnQuery);
            tmpPreQ.setFilter(preFilter);
            /**/
            String strQuery = SFSDataStoreUtil.encodeQuery(tmpPreQ, getSchema());

            /* Create the URL */
            String strCount = ods.resourceToString("data/" + layer.getTypeName().getLocalPart(), "mode=count&" + strQuery);

            try {
                count = Integer.parseInt(strCount);
            } catch (NumberFormatException nfe) {

                LOGGER.log(Level.SEVERE, "Number format Exception in getCountInternal : FeatureSource -- getCount --" + nfe.getMessage(), nfe);
                return 0;
            }
        }
        /* Check if the count greater than maxFeature Limit*/
        if (fnQuery.getMaxFeatures() > 0 && count > fnQuery.getMaxFeatures()) {
            count = fnQuery.getMaxFeatures();
        }
        return count;
    }

    /**
     * This method invokes the OpenDataStoreFeatureReader class
     * This method has been borrowed from
     * https://svn.osgeo.org/geotools/trunk/modules/library/jdbc/src/main/java/org/geotools/jdbc/JDBCFeatureSource.java
     * @param fnQuery
     * @return FeatureReader
     * @throws IOException
     */
    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query fnQuery) throws IOException {
        // simplify the filter
        Filter filter = fnQuery.getFilter();
        SimplifyingFilterVisitor simplifier = new SimplifyingFilterVisitor();
        filter = (Filter) filter.accept(simplifier, null);
        
        // Split the filter into two parts, pre and post 
        Filter[] split = splitFilter(filter);

        Filter preFilter = split[0];
        Filter postFilter = split[1];

        /* Getting the preFilter part of the query*/
        Query preQuery = new Query(fnQuery);
        preQuery.setFilter(preFilter);

        /* Check if the schema remains same of do we need to update it*/
        SimpleFeatureType returnedSchema;
        SimpleFeatureType querySchema;

        if (fnQuery.getPropertyNames() == Query.ALL_NAMES) {
            returnedSchema = querySchema = getSchema();
        } else {
            returnedSchema = SimpleFeatureTypeBuilder.retype(getSchema(), fnQuery.getPropertyNames());
            FilterAttributeExtractor extractor = new FilterAttributeExtractor(getSchema());
            // we need to let all attribute pass as we're going to make a full filter evaluation
            // in memory
            filter.accept(extractor, null);
            String[] extraAttributes = extractor.getAttributeNames();
            if (extraAttributes == null || extraAttributes.length == 0) {
                // nothing to do
                querySchema = returnedSchema;
            } else {
                List<String> allAttributes = new ArrayList<String>(Arrays.asList(fnQuery.getPropertyNames()));
                for (String extraAttribute : extraAttributes) {
                    if (!allAttributes.contains(extraAttribute)) {
                        allAttributes.add(extraAttribute);
                    }
                }
                String[] allAttributeArray = (String[]) allAttributes.toArray(new String[allAttributes.size()]);
                preQuery.setPropertyNames(allAttributeArray);
                querySchema = SimpleFeatureTypeBuilder.retype(getSchema(), allAttributeArray);
            }
        }

        /* Get the reader*/
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;


        /* Do the pre part first */
        reader = new SFSFeatureReader(getState(), layer, preQuery, querySchema);

        /* 
         * Normally we should finish off with post filtering, but reality proves that the
         * remote service sometimes does not implement the protocol filtering spec fully.
         * Don't trust the records sent back and apply the whole filter again on top of them
         */
        if(filter != null && !Filter.INCLUDE.equals(filter)) {
            reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, filter);
        }
        
        if(querySchema.getAttributeCount() > returnedSchema.getAttributeCount()) {
            reader = new ReTypeFeatureReader(reader, returnedSchema);
        }
        
        return reader;
    }

    /**
     * This method returns the schema of the layer
     * @return SimpleFeatureType
     * @throws IOException, MalformedURLException
     */
    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException, MalformedURLException {

        /* Get the layer describing URL*/
        String jsonString = ods.resourceToString("describe/" + contentEntry.getName().getLocalPart(), null);

        JSONParser parser = new JSONParser();

        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) parser.parse(jsonString);

        } catch (org.json.simple.parser.ParseException pe) {
            LOGGER.log(Level.SEVERE, "parse Exception : FeatureSource -- buildFeatureType --" + pe.getMessage(), pe);
        }
        /* Lets build the feature type*/
        SimpleFeatureTypeBuilder fbt = new SimpleFeatureTypeBuilder();

        fbt.setName(contentEntry.getName());
        /* set the CRS*/
        fbt.setCRS(layer.getCoordinateReferenceSystem());
        /* First Iterator*/
        Iterator itr = jsonArray.iterator();
        while (itr.hasNext()) {
            HashMap<String, String> tmpMap = (HashMap<String, String>) itr.next();

            /* Iterate Through the hashMap to extract the key-value pairs*/
            for (Map.Entry<String, String> entry : tmpMap.entrySet()) {

                //LOGGER.log(Level.INFO, "{0} - {1} - {2}", new Object[]{entry.getKey(), entry.getValue(), OpenDataStoreUtil.getClass(entry.getValue())});
                fbt.add(entry.getKey(), SFSDataStoreUtil.getClass(entry.getValue()));
            }
        }

        return fbt.buildFeatureType();
    }

    /**
     *
     * @param fnSchema
     * @return QueryCapabilities
     * @throws IOException
     */
    public QueryCapabilities getQueryCapabilities(SimpleFeatureType fnSchema) throws IOException {

        SFSQueryCapabilities odsc = new SFSQueryCapabilities(fnSchema);

        return odsc;
    }

    /**
     *
     * @param fnID
     * @return SimpleFeature
     * @throws MalformedURLException
     * @throws MalformedURLException, IOException, org.json.simple.parser.ParseException
     */
    public SimpleFeature getFeatureWithID(String fnID) throws MalformedURLException, IOException, org.json.simple.parser.ParseException {

        if (fnID == null) {
            throw new IOException("FeatureID cannot be null");
        }
        /* Get the layer describing URL*/
        String jsonString = ods.resourceToString("data/" + contentEntry.getName().getLocalPart() + "/" + fnID, null);

        FeatureJSON fjson = new FeatureJSON();

        SimpleFeature feature = fjson.readFeature(new StringReader(SFSDataStoreUtil.strip(jsonString)));

        return feature;
    }

    /**
     * This method has been borrowed from
     * https://svn.osgeo.org/geotools/trunk/modules/library/jdbc/src/main/java/org/geotools/jdbc/JDBCFeatureSource.java
     * Helper method for splitting a filter.
     */
    Filter[] splitFilter(Filter fnOriginalFilter) {
        /* Split filter into 2 parts */
        Filter[] split = new Filter[2];
        if (fnOriginalFilter != null) {
            //create a filter splitter
            PostPreProcessFilterSplittingVisitor splitter = new PostPreProcessFilterSplittingVisitor(ods.ODS_FILTER_CAPABILITIES, getSchema(), null);

            fnOriginalFilter.accept(splitter, null);
            /* Natively supported*/
            split[0] = splitter.getFilterPre();
            /* Not natively supported*/
            split[1] = splitter.getFilterPost();
        }
        /* Getting simplest possible filter*/
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();

        split[0] = (Filter) split[0].accept(visitor, null);

        split[1] = (Filter) split[1].accept(visitor, null);

        return split;
    }
    
    @Override
    protected boolean canFilter() {
        return true;
    }
    
    @Override
    protected boolean canLimit() {
        return true;
    }
    
    @Override
    protected boolean canOffset() {
        return true;
    }
    
    @Override
    protected boolean canRetype() {
        return true;
    }
    
    @Override
    protected boolean canSort() {
        return true;
    }
}
