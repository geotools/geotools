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

package org.geotools.data.complex.config;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.geotools.util.CheckedHashMap;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Rini Angreani, Curtin University of Technology
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/config/SourceDataStore.java $
 * @since 2.4
 */
public class SourceDataStore implements Serializable {
    private static final long serialVersionUID = 8540617713675342340L;

    private String id;

    private Map params = Collections.EMPTY_MAP;

    /**
     * True if we have the data store connection params but we want to connect to a data access
     * that's connected to the data store. This requires the data access to be registered in
     * DataAccessRegistry upon creation.
     */
    private boolean isDataAccess;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map getParams() {
        return new HashMap(params);
    }

    public void setParams(Map params) {
        this.params = new CheckedHashMap(Serializable.class, Serializable.class);
        if (params != null) {
            this.params.putAll(params);
        }
    }

    public void setDataAccess(String isDataAccess) {
        this.isDataAccess = Boolean.valueOf(isDataAccess).booleanValue();
    }

    public boolean isDataAccess() {
        return isDataAccess;
    }
}
