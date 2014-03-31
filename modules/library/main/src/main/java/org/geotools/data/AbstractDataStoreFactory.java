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
package org.geotools.data;

import java.util.Map;

/**
 * A best of toolkit for DataStoreFactory implementors.
 * <p>
 * Will also allow me to mess with the interface API without breaking every
 * last DataStoreFactorySpi out there.
 * </p>
 * <p>
 * The default implementations often hinge around the use of
 * getParameterInfo and the correct use of Param by your subclass.
 * </p>
 * <p>
 * You still have to implement a few methods:
 * </p>
 * <pre><code>
 * public DataSourceMetadataEnity createMetadata( Map params ) throws IOException {
 * 	    String host = (String) HOST.lookUp(params);
 *      String user = (String) USER.lookUp(params);
 *      Integer port = (Integer) PORT.lookUp(params);
 *      String database = (String) DATABASE.lookUp(params);
 *
 *      String description = "Connection to "+getDisplayName()+" on "+host+" as "+user ;
 *      return new DataSourceMetadataEnity( host+":"+port, database, description );
 * }</code></pre>
 *
 * @author Jody Garnett, Refractions Research
 *
 *
 * @source $URL$
 * 
 */
public abstract class AbstractDataStoreFactory extends AbstractDataAccessFactory implements DataStoreFactorySpi {
    @Override
    public boolean canProcess(Map params) {
        return super.canProcess(params);
    }
}
