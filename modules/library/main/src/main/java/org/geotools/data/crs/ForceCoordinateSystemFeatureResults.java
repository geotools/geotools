/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.crs;

import java.io.IOException;
import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * ForceCoordinateSystemFeatureResults provides a CoordinateReferenceSystem for
 * FeatureTypes.
 * 
 * <p>
 * ForceCoordinateSystemFeatureReader is a wrapper used to force
 * GeometryAttributes to a user supplied CoordinateReferenceSystem rather then
 * the default supplied by the DataStore.
 * </p>
 * 
 * <p>
 * Example Use:
 * <pre><code>
 * ForceCoordinateSystemFeatureResults results =
 *     new ForceCoordinateSystemFeatureResults( originalResults, forceCS );
 * 
 * CoordinateReferenceSystem originalCS =
 *     originalResults.getFeatureType().getDefaultGeometry().getCoordinateSystem();
 * 
 * CoordinateReferenceSystem newCS =
 *     reader.getFeatureType().getDefaultGeometry().getCoordinateSystem();
 * 
 * assertEquals( forceCS, newCS );
 * </code></pre>
 * </p>
 *
 * @author aaime
 * @source $URL$
 * @version $Id$
 */
public class ForceCoordinateSystemFeatureResults extends AbstractFeatureCollection {
    
    FeatureCollection<SimpleFeatureType,SimpleFeature> results;
    
    public ForceCoordinateSystemFeatureResults(FeatureCollection<SimpleFeatureType,SimpleFeature> results,
            CoordinateReferenceSystem forcedCS) throws IOException, SchemaException {
        this(results, forcedCS, false);
    }

    public ForceCoordinateSystemFeatureResults(FeatureCollection<SimpleFeatureType,SimpleFeature> results,            
        CoordinateReferenceSystem forcedCS, boolean forceOnlyMissing) throws IOException, SchemaException {
        super( forceType( origionalType( results ), forcedCS, forceOnlyMissing ));
        
        this.results = results;
    }
    
    private static SimpleFeatureType origionalType( FeatureCollection<SimpleFeatureType,SimpleFeature> results ){
        while( true ){
            if ( results instanceof ReprojectFeatureResults ) {
                results = ((ReprojectFeatureResults) results).getOrigin();
            }        
            if ( results instanceof ForceCoordinateSystemFeatureResults ) {
                results = ((ForceCoordinateSystemFeatureResults) results).getOrigin();
            }
            break;
        }
        return results.getSchema();
    }
    
	 public Iterator<SimpleFeature> openIterator() {
	        return new ForceCoordinateSystemIterator( results.features(), getSchema() );
	  }
	 public void closeIterator( Iterator close ) {
		  	if( close == null ) return;
	        if( close instanceof ForceCoordinateSystemIterator){
	            ForceCoordinateSystemIterator iterator = (ForceCoordinateSystemIterator) close;
	            iterator.close();
	        }
	  }
		
	  public int size() {
	        return results.size();
	  }
    
    private static SimpleFeatureType forceType( SimpleFeatureType startingType, CoordinateReferenceSystem forcedCS, boolean forceOnlyMissing ) throws SchemaException{
        if (forcedCS == null) {
            throw new NullPointerException("CoordinateSystem required");
        }
        CoordinateReferenceSystem originalCs = startingType.getGeometryDescriptor() != null ? 
            startingType.getGeometryDescriptor().getCoordinateReferenceSystem() : null;
        
        if (forcedCS.equals(originalCs)) {
            return startingType;
        }
        else {
            return FeatureTypes.transform(startingType, forcedCS, forceOnlyMissing);
        }
    }
   
  

    /**
     * @see org.geotools.data.FeatureResults#getBounds()
     */
    public ReferencedEnvelope getBounds() {
        ReferencedEnvelope env = results.getBounds();
        if(env == null) {
            return null;
        }
        env = new ReferencedEnvelope(env, getSchema().getCoordinateReferenceSystem());
        return env;
    }

   
    /**
     * @see org.geotools.data.FeatureResults#collection()
     */
//    public SimpleFeatureCollection collection() throws IOException {
//        SimpleFeatureCollection collection = FeatureCollections.newCollection();
//
//        try {
//             FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader();
//
//            while (reader.hasNext()) {
//                collection.add(reader.next());
//            }
//        } catch (NoSuchElementException e) {
//            throw new DataSourceException("This should not happen", e);
//        } catch (IllegalAttributeException e) {
//            throw new DataSourceException("This should not happen", e);
//        }
//
//        return collection;
//    }

    /**
     * Returns the feature results wrapped by this
     * ForceCoordinateSystemFeatureResults
     *
     */
    public FeatureCollection<SimpleFeatureType,SimpleFeature> getOrigin() {
        return results;
    }
}
