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
package org.geotools.jdbc;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.factory.Hints;
import org.geotools.util.logging.Logging;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Describes a virtual table, that is, a feature type created starting from a generic SQL query.
 * This class also carries information about the primary key (to generate stable feature ids) and
 * the geometry type and native srid (as in most databases those informations are not available on.
 * 
 * The sql query can contain named parameters. Each parameter has a name, a default value and a way
 * to validate its contents to prevent sql injection
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
public class VirtualTable implements Serializable {
    static final Logger LOGGER = Logging.getLogger(VirtualTable.class);
    
    String name;

    String sql;

    List<String> primaryKeyColumns = new CopyOnWriteArrayList<String>();

    Map<String, Class<? extends Geometry>> geometryTypes = new ConcurrentHashMap<String, Class<? extends Geometry>>();

    Map<String, Integer> nativeSrids = new ConcurrentHashMap<String, Integer>();

    Map<String, VirtualTableParameter> parameters = new ConcurrentHashMap<String, VirtualTableParameter>();

    /**
     * Builds a new virtual table stating its name and the query to be executed to work on it
     * 
     * @param name
     * @param sql
     */
    public VirtualTable(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }
    
    /**
     * Clone a virtual table under a different name
     * @param name
     * @param other
     */
    public VirtualTable(String name, VirtualTable other) {
        this.name = name;
        this.sql = other.sql;
        this.geometryTypes = new ConcurrentHashMap<String, Class<? extends Geometry>>(other.geometryTypes);
        this.nativeSrids = new ConcurrentHashMap<String, Integer>(other.nativeSrids);
        this.parameters = new ConcurrentHashMap<String, VirtualTableParameter>(other.parameters);
        this.primaryKeyColumns = new ArrayList<String>(other.primaryKeyColumns);
    }
    
    /**
     * Clone a virtual table 
     * @param name
     * @param other
     */
    public VirtualTable(VirtualTable other) {
        this.name = other.name;
        this.sql = other.sql;
        this.geometryTypes = new ConcurrentHashMap<String, Class<? extends Geometry>>(other.geometryTypes);
        this.nativeSrids = new ConcurrentHashMap<String, Integer>(other.nativeSrids);
        this.parameters = new ConcurrentHashMap<String, VirtualTableParameter>(other.parameters);
        this.primaryKeyColumns = new ArrayList<String>(other.primaryKeyColumns);
    }

    /**
     * Returns the virtual table primary key columns. It should refer to fields returned by the
     * query, if that is not true the behavior is undefined
     */
    public List<String> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    /**
     * Sets the virtual table primary key
     * 
     * @param primaryKeyColumns
     */
    public void setPrimaryKeyColumns(List<String> primaryKeyColumns) {
        this.primaryKeyColumns.clear();
        if (primaryKeyColumns != null) {
            this.primaryKeyColumns.addAll(primaryKeyColumns);
        }
    }

    /**
     * The virtual table name
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * The virtual table sql (raw, without parameter expansion)
     * 
     * @return
     */
    public String getSql() {
        return sql;
    }

    public String expandParameters(Hints hints) throws SQLException {
        // no need for expansion if we don't have parameters
        if (parameters.size() == 0) {
            return sql;
        }

        // grab the parameter values
        Map<String, String> values = null;
        if(hints != null) {
            values = (Map<String, String>) hints.get(Hints.VIRTUAL_TABLE_PARAMETERS);
        }
        if (values == null) {
            values = Collections.emptyMap();
        }

        // perform the expansion, checking for validity and applying default values as needed
        String result = sql;
        for (VirtualTableParameter param : parameters.values()) {
            String value = values.get(param.getName());
            if(value == null) {
                // use the default value and eventually prepare to expand the empty string
                value = param.getDefaultValue();
                if(value == null) {
                    value = "";
                }
            } else {
                if(param.getValidator() != null) {
                    try {
                        param.getValidator().validate(value);
                    } catch(IllegalArgumentException e) {
                        // fully log the exception, but only rethrow a more generic description as
                        // the message could be exposed to attackers
                        LOGGER.log(Level.SEVERE, "Invalid value for parameter " + param.getName(), e);
                        throw new SQLException("Invalid value for parameter " + param.getName());
                    }
                }
            }
            
            result = result.replace("%" + param.getName() + "%", value);
        }
        
        return result;
    }

    /**
     * Adds geometry metadata to the virtual table. This is important to get the datastore working,
     * often that is not the case if the right native srid is not in place
     * 
     * @param geometry
     * @param binding
     * @param nativeSrid
     */
    public void addGeometryMetadatata(String geometry, Class<? extends Geometry> binding,
            int nativeSrid) {
        geometryTypes.put(geometry, binding);
        nativeSrids.put(geometry, nativeSrid);
    }
    
    /**
     * Adds a parameter to the virtual table 
     * @param param
     */
    public void addParameter(VirtualTableParameter param) {
        parameters.put(param.getName(), param);
    }
    
    /**
     * Removes a parameter from the virtual table
     * @param paramName
     */
    public void removeParameter(String paramName) {
        parameters.remove(paramName);
    }
    
    /**
     * The current parameter names
     * @return
     */
    public Collection<String> getParameterNames() {
        return new ArrayList(parameters.keySet());
    }
    
    /**
     * Returns the requested parameter, or null if it could not be found
     * @return
     */
    public VirtualTableParameter getParameter(String name) {
        return parameters.get(name);
    }

    /**
     * Returns the geometry's specific type, or null if not known
     * 
     * @param geometryName
     * @return
     */
    public Class<? extends Geometry> getGeometryType(String geometryName) {
        return geometryTypes.get(geometryName);
    }

    /**
     * Returns the name of the geometry colums declared in this virtual table
     * 
     * @return
     */
    public Set<String> getGeometries() {
        return geometryTypes.keySet();
    }

    /**
     * Returns the geometry native srid, or -1 if not known
     * 
     * @param geometryName
     * @return
     */
    public int getNativeSrid(String geometryName) {
        Integer srid = nativeSrids.get(geometryName);
        if (srid == null) {
            srid = -1;
        }
        return srid;
    }

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((geometryTypes == null) ? 0 : geometryTypes.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((nativeSrids == null) ? 0 : nativeSrids.hashCode());
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((primaryKeyColumns == null) ? 0 : primaryKeyColumns.hashCode());
        result = prime * result + ((sql == null) ? 0 : sql.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VirtualTable other = (VirtualTable) obj;
        if (geometryTypes == null) {
            if (other.geometryTypes != null)
                return false;
        } else if (!geometryTypes.equals(other.geometryTypes))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (nativeSrids == null) {
            if (other.nativeSrids != null)
                return false;
        } else if (!nativeSrids.equals(other.nativeSrids))
            return false;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!parameters.equals(other.parameters))
            return false;
        if (primaryKeyColumns == null) {
            if (other.primaryKeyColumns != null)
                return false;
        } else if (!primaryKeyColumns.equals(other.primaryKeyColumns))
            return false;
        if (sql == null) {
            if (other.sql != null)
                return false;
        } else if (!sql.equals(other.sql))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "VirtualTable [name=" + name + ", sql=" + sql + "]";
    }
   
}
