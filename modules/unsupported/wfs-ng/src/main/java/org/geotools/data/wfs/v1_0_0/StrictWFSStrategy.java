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
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.ows.FeatureSetDescription;
import org.geotools.data.ows.WFSCapabilities;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.Filters;
import org.geotools.filter.visitor.WFSBBoxFilterVisitor;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.xml.XMLHandlerHints;
import org.geotools.xml.filter.FilterEncodingPreProcessor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;


/**
 * A version that is very strict about its filter compliance.
 * @author Jesse
 *
 */
class StrictWFSStrategy extends NonStrictWFSStrategy {

    /**
     * This just splits fid filters from non-fid filters.  The {@link StrictFeatureReader} is what does the rest of the 
     * compliance to high compliance.
     */
    protected static final Integer COMPLIANCE_LEVEL = XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM;

    public StrictWFSStrategy(WFS_1_0_0_DataStore store) {
        super(store);
    }

    protected  FeatureReader<SimpleFeatureType, SimpleFeature> wrapWithFilteringFeatureReader(Filter postFilter,  FeatureReader<SimpleFeatureType, SimpleFeature> reader, Filter processedFilter) {
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(COMPLIANCE_LEVEL);
        Filters.accept( processedFilter, visitor);
        
        if( visitor.requiresPostProcessing() )
            return new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, processedFilter);
        else
            return new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, postFilter);
            
    }

    protected  FeatureReader<SimpleFeatureType, SimpleFeature> createFeatureReader(Transaction transaction, Query query) throws IOException {
        return new StrictFeatureReader(transaction, query, 
                COMPLIANCE_LEVEL);
    }

    protected CoordinateReferenceSystem correctFilterForServer( String typeName, Filter serverFilter) {
        // TODO modify bbox requests here
        FeatureSetDescription fsd = WFSCapabilities.getFeatureSetDescription(store.capabilities,
                typeName);

        Envelope maxbbox = null;
        CoordinateReferenceSystem dataCRS = null;
        
        if (fsd.getSRS() != null) {
            // reproject this filter!
            try {
                dataCRS = CRS.decode( fsd.getSRS() );
                MathTransform toDataCRS = CRS.findMathTransform(DefaultGeographicCRS.WGS84, dataCRS);
                maxbbox = JTS.transform(fsd.getLatLongBoundingBox(), null, toDataCRS, 10);
            } catch (FactoryException e) {
                WFS_1_0_0_DataStore.LOGGER.warning(e.getMessage());
                maxbbox = null;
            } catch (MismatchedDimensionException e) {
                WFS_1_0_0_DataStore.LOGGER.warning(e.getMessage());
                maxbbox = null;
            } catch (TransformException e) {
                WFS_1_0_0_DataStore.LOGGER.warning(e.getMessage());
                maxbbox = null;
            }
        } else {
            maxbbox = fsd.getLatLongBoundingBox();
        }
        
        // Rewrite request if we have a maxbox
        if (maxbbox != null) {
            WFSBBoxFilterVisitor clipVisitor = new WFSBBoxFilterVisitor(maxbbox);
            Filters.accept(serverFilter, clipVisitor);
        } else { // give up an request everything
            WFS_1_0_0_DataStore.LOGGER.log(Level.FINE,
                    "Unable to clip your query against the latlongboundingbox element");
            // filters[0] = Filter.EXCLUDE; // uncoment this line to just give
            // up
        }
        return dataCRS;
    }
    
    /**
     * Makes seperate requests between fetching the features using a normal filter and a seperate request for fetching features using
     * the FID filter.
     *  
     * @author Jesse
     */
    protected class StrictFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature>{

        private FeatureReader<SimpleFeatureType, SimpleFeature> delegate;
        protected Filter filter;
        private Query query;
        private Transaction transaction;
        private Set foundFids=new HashSet();
        private SimpleFeature next;

        public StrictFeatureReader(Transaction transaction, Query query, Integer level) throws IOException {
            init(transaction, query, level);
        }

        protected void init( Transaction transaction, Query query, Integer level ) throws IOException {
            FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(level);
            Filters.accept( query.getFilter(), visitor );
            
            this.transaction=transaction;
            if( visitor.requiresPostProcessing() && query.getPropertyNames()!=Query.ALL_NAMES){
                FilterAttributeExtractor attributeExtractor=new FilterAttributeExtractor();
                query.getFilter().accept( attributeExtractor, null );
                Set properties=new HashSet(attributeExtractor.getAttributeNameSet());
                properties.addAll(Arrays.asList(query.getPropertyNames()));
                this.query=new DefaultQuery(query.getTypeName(), query.getFilter(), query.getMaxFeatures(),
                        (String[]) properties.toArray(new String[0]), query.getHandle());
            }else
                this.query=query;
            
            this.filter=visitor.getFilter();

            DefaultQuery nonFidQuery=new DefaultQuery(query);
            Id fidFilter = visitor.getFidFilter();
            nonFidQuery.setFilter(fidFilter);
            if( fidFilter.getIDs().size()>0 ){
                delegate = StrictWFSStrategy.super.createFeatureReader(transaction, nonFidQuery);
            }else{
                delegate=nextReader();
            }
        }

        public void close() throws IOException {
            if( delegate!=null )
                delegate.close();
        }

        public SimpleFeatureType getFeatureType() {
            return delegate.getFeatureType();
        }

        public boolean hasNext() throws IOException {
            if( next!=null )
                return true;
            
            if( delegate==null )
                return false;
            
            if ( !delegate.hasNext() ){
                delegate.close();
                delegate=null;
                delegate=nextReader();
                if( delegate==null )
                    return false;
            }
            
            try{
                while( next==null ){
                    if( !delegate.hasNext() )
                        return false;
                
                    next=delegate.next();
                    if( this.foundFids.contains(next.getID()) )
                        next=null;
                }
            }catch( IllegalAttributeException e){
                throw new IOException(e.getLocalizedMessage());
            } 
            
            return next!=null;
        }

        private  FeatureReader<SimpleFeatureType, SimpleFeature> nextReader() throws IOException {
            if( filter==null || filter==Filter.EXCLUDE )
                return null;

            DefaultQuery query2=new DefaultQuery(query);
            query2.setFilter(filter);
            
             FeatureReader<SimpleFeatureType, SimpleFeature> nextReader = StrictWFSStrategy.super.createFeatureReader(transaction, query2);

            filter=null;
            return nextReader;
        }


        public SimpleFeature next() throws IOException, IllegalAttributeException, NoSuchElementException {
            if( !hasNext() )
                throw new NoSuchElementException();
            
            SimpleFeature tmp = next;
            foundFids.add(tmp.getID());
            next=null;
            return tmp;
        }
        
    }

}
