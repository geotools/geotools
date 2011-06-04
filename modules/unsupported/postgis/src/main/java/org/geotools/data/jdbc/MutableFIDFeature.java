/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc;

import java.util.List;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author Sean Geoghegan, Defence Science and Technology Organisation
 * @author $Author: aaime $
 *
 * @source $URL$
 * @version $Id$
 * Last Modified: $Date: 2004/04/09 15:30:52 $ 
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class MutableFIDFeature extends SimpleFeatureImpl {

  public MutableFIDFeature(List<Object> values, SimpleFeatureType ft, String fid)
    throws IllegalAttributeException {
    super(values, ft, createDefaultFID(fid) );
  }

  private static FeatureIdImpl createDefaultFID(String id){
      if( id == null ){
    	  id = SimpleFeatureBuilder.createDefaultFeatureId();
      }
      return new FeatureIdImpl(id){
    	  public void setID( String id ){
    			if ( fid == null ) {
    				throw new NullPointerException( "fid must not be null" );
    			}		
    			if( origionalFid == null ){
    				origionalFid = fid;    						
    			}
    			fid = id;
    		}
      };
  }

  /**
   * Sets the FID, used by datastores only.
   * 
   * I would love to protect this for safety reason, i.e. so client classes can't
   *  use it by casting to it.
   * 
   * @param id The fid to set.
   */
  public void setID(String fid) {
	  ((FeatureIdImpl)id).setID( fid );
  }
}
