/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import java.io.IOException;

import org.geotools.data.DefaultQuery;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ForceCoordinateSystemFeatureReader;
import org.geotools.data.ows.FeatureSetDescription;
import org.geotools.data.ows.WFSCapabilities;
import org.geotools.feature.SchemaException;
import org.geotools.filter.Filters;
import org.geotools.filter.visitor.WFSBBoxFilterVisitor;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.SAXException;

/**
 * A version that is not strict about its filter compliance. It can be used with
 * geoserver but no other servers.
 * 
 * @author Jesse
 */
class NonStrictWFSStrategy implements WFSStrategy {

    protected WFS_1_0_0_DataStore store;

    public NonStrictWFSStrategy(WFS_1_0_0_DataStore store) {
        this.store = store;
    }
    
    public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query2, Transaction transaction) throws IOException {
        Query query = new DefaultQuery(query2);
        Filter processedFilter = store.processFilter(query.getFilter());
        // process the filter to update fidfilters using the transaction.
        ((DefaultQuery) query).setFilter(processedFilter);
        Filter serverFilter;
        Filter postFilter;
        {
            Filter[] filters = store.splitFilters(query, transaction); // [server][post]
            serverFilter = filters[0];
            postFilter = filters[1];
        }

        CoordinateReferenceSystem dataCRS = correctFilterForServer( query.getTypeName(), serverFilter);

        ((DefaultQuery) query).setFilter(serverFilter);
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = createFeatureReader(transaction, query);

        if (reader.hasNext()) { // opportunity to throw exception

            if (reader.getFeatureType() != null) {
                reader = wrapWithFilteringFeatureReader(postFilter, reader, processedFilter);
                reader = applyReprojectionDecorator(reader, query, dataCRS);
                return reader;
            }
            throw new IOException("There are features but no feature type ... odd");
        }

        return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(store.getSchema(query.getTypeName()));
    }

    protected  FeatureReader<SimpleFeatureType, SimpleFeature> wrapWithFilteringFeatureReader(Filter postFilter,  FeatureReader<SimpleFeatureType, SimpleFeature> reader,
            Filter processedFilter) {
        if (!postFilter.equals(Filter.INCLUDE)) {
            return new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, postFilter);
        }
        return reader;
    }

    protected  FeatureReader<SimpleFeatureType, SimpleFeature> createFeatureReader(Transaction transaction, Query query)
            throws IOException {
        Data data;
        data = createFeatureReaderPOST(query, transaction);

        if (data.reader == null)
            data = createFeatureReaderGET(query, transaction);

        if (data.reader == null && data.saxException != null)
            throw (IOException) new IOException(data.saxException.toString()).initCause(data.saxException);
        if (data.reader == null && data.ioException != null)
            throw data.ioException;

        return data.reader;
    }

    protected Data createFeatureReaderPOST(Query query, Transaction transaction) {
        Data data = new Data();
        try {
            data.reader = store.getFeatureReaderPost(query, transaction);
            if (data.reader != null)
                data.reader.hasNext(); // throws spot
        } catch (SAXException e) {
            data.reader = null;
            WFS_1_0_0_DataStore.LOGGER.warning(e.toString());
            data.saxException = e;
        } catch (IOException e) {
            data.reader = null;
            WFS_1_0_0_DataStore.LOGGER.warning(e.toString());
            data.ioException = e;
        }
        return data;
    }

    protected Data createFeatureReaderGET(Query query, Transaction transaction) {
        Data data = new Data();
        try {
            data.reader = store.getFeatureReaderGet(query, transaction);
            if (data.reader != null)
                data.reader.hasNext(); // throws spot
        } catch (SAXException e) {
            data.reader = null;
            WFS_1_0_0_DataStore.LOGGER.warning(e.toString());
            data.saxException = e;
        } catch (IOException e) {
            data.reader = null;
            WFS_1_0_0_DataStore.LOGGER.warning(e.toString());
            data.ioException = e;
        }
        return data;
    }

    protected  FeatureReader<SimpleFeatureType, SimpleFeature> applyReprojectionDecorator(FeatureReader <SimpleFeatureType, SimpleFeature> reader, Query query,
            CoordinateReferenceSystem dataCRS) {
         FeatureReader<SimpleFeatureType, SimpleFeature> tmp = reader;
        if (query.getCoordinateSystem() != null
                && !query.getCoordinateSystem().equals(reader.getFeatureType().getCoordinateReferenceSystem())) {
            try {
                reader = new ForceCoordinateSystemFeatureReader(reader, query.getCoordinateSystem());
            } catch (SchemaException e) {
                WFS_1_0_0_DataStore.LOGGER.warning(e.toString());
                reader = tmp;
            }
        } else {
            if (reader.getFeatureType().getGeometryDescriptor() != null && dataCRS != null
                    && reader.getFeatureType().getCoordinateReferenceSystem() == null) {
                // set up crs
                try {
                    reader = new ForceCoordinateSystemFeatureReader(reader, dataCRS);
                } catch (SchemaException e) {
                    WFS_1_0_0_DataStore.LOGGER.warning(e.toString());
                    reader = tmp;
                }
            }
        }
        return reader;
    }
    
    /**
     * If we are being exacting about folowing the WFS Capabilities.
     * 
     * @return true if we are being exacting about following the WFS Capabilities
     */
    protected boolean isStrict(){
        return false;
    }
    /**
     * Using the provided query; obtain a FeatureSetDescriptor and modify the provided serverFilter
     * to be correct.
     * <p>
     * If we are being strict the implementation may also clip any geometry or bbox 
     * to the valid bounds advertised as valid by the server (or by the data CRS).
     * <p>
     * @param query
     * @param serverFilter
     * @return CoordinateReferenceSystem to use when making the request (usually the data CRS for a WFS 1.0 Datastore)
     */
    protected CoordinateReferenceSystem correctFilterForServer( String typeName, Filter serverFilter) {
        // TODO modify bbox requests here
        FeatureSetDescription fsd = WFSCapabilities.getFeatureSetDescription(store.capabilities,
                typeName);

        CoordinateReferenceSystem dataCRS = null;
        
        if (fsd.getSRS() != null) {
            // reproject this filter!
            try {
                dataCRS = CRS.decode( fsd.getSRS() );
            } catch (FactoryException e) {
                WFS_1_0_0_DataStore.LOGGER.warning(e.getMessage());
            } catch (MismatchedDimensionException e) {
                WFS_1_0_0_DataStore.LOGGER.warning(e.getMessage());
            }
        }
        // we are going to assume that to assume that we don't need to clip or
        // anything; and just return the dataCRS
        // Rewrite request if we have a maxbox
        WFSBBoxFilterVisitor visitor = new WFSBBoxFilterVisitor(null);
        Filters.accept(serverFilter, visitor);
        return dataCRS;
    }

    protected class Data {
        IOException ioException;

        SAXException saxException;

         FeatureReader<SimpleFeatureType, SimpleFeature> reader;
    }

}
