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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.Query;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.Filter;

/**
 * Describes a virtual table, that is, a feature type created starting from a generic SQL query.
 * This class also carries information about the primary key (to generate stable feature ids) and
 * the geometry type and native srid (as in most databases those informations are not available on.
 *
 * <p>The sql query can contain named parameters. Each parameter has a name, a default value and a
 * way to validate its contents to prevent sql injection.
 *
 * <p>As well as passing validation, parameters are also passed through a function to escape double
 * quotes, single quotes and strip backslashes to guard against the cases where quotes are desired
 * in the parameters or backslashes have been allowed by an overly lax regular expression.
 *
 * <p>Escaping is enabled by default and can be controlled by a constructor argument or via the
 * setEscapeSql() method.
 *
 * @author Andrea Aime - OpenGeo
 */
public class VirtualTable implements Serializable {

    private static final Hints.Key KEEP_WHERE_CLAUSE_PLACE_HOLDER_KEY =
            new Hints.Key(Boolean.class);

    public static String WHERE_CLAUSE_PLACE_HOLDER = ":where_clause:";
    public static int WHERE_CLAUSE_PLACE_HOLDER_LENGTH = 14;

    static final Logger LOGGER = Logging.getLogger(VirtualTable.class);

    String name;

    String sql;

    List<String> primaryKeyColumns = new CopyOnWriteArrayList<String>();

    Map<String, Class<? extends Geometry>> geometryTypes =
            new ConcurrentHashMap<String, Class<? extends Geometry>>();

    Map<String, Integer> nativeSrids = new ConcurrentHashMap<String, Integer>();

    Map<String, Integer> dimensions = new ConcurrentHashMap<String, Integer>();

    Map<String, VirtualTableParameter> parameters =
            new ConcurrentHashMap<String, VirtualTableParameter>();

    boolean escapeSql = false;

    /**
     * If the provided query has a filter of a where clause place holder exists it will be
     * preserved.
     *
     * @param query the query to test
     * @return a query hints map that will contain an entry specifying if the the where clause place
     *     holder should be keep or not
     */
    public static Hints setKeepWhereClausePlaceHolderHint(Query query) {
        Filter filter = query.getFilter();
        return setKeepWhereClausePlaceHolderHint(
                query.getHints(), filter != null && filter != Filter.INCLUDE);
    }

    /**
     * Will add an entry to query hints specifying if the the where clause place holder should be
     * keep or not. If the provided hints is NULL a new one will be instantiated and returned.
     *
     * @param hints query hints to update, if NULL a new hints map will be created
     * @param keepWhereClausePlaceHolder TRUE if the where clause place holder should be keep
     * @return a query hints map that will contain an entry specifying if the the where clause place
     *     holder should be keep or not
     */
    public static Hints setKeepWhereClausePlaceHolderHint(
            Hints hints, boolean keepWhereClausePlaceHolder) {
        if (hints == null) {
            // create the hints map
            hints = new Hints();
        }
        // just put the provide value in the hints overriding any existing value
        hints.put(KEEP_WHERE_CLAUSE_PLACE_HOLDER_KEY, keepWhereClausePlaceHolder);
        return hints;
    }

    /** Builds a new virtual table stating its name and the query to be executed to work on it */
    public VirtualTable(String name, String sql) {
        this.name = name;
        // make sure we end the query with a newline to handle eventual comments in the last line
        if (!sql.endsWith("\n") && !sql.endsWith("\r")) {
            sql = sql + "\n";
        }
        this.sql = sql;
    }

    /**
     * Builds a new virtual table stating its name, the query to be executed to work on it and a
     * flag to indicate if SQL special characters should be escaped.
     */
    public VirtualTable(String name, String sql, boolean escapeSql) {
        this(name, sql);
        this.escapeSql = escapeSql;
    }

    /** Clone a virtual table under a different name */
    public VirtualTable(String name, VirtualTable other) {
        this(name, other.sql);
        this.geometryTypes =
                new ConcurrentHashMap<String, Class<? extends Geometry>>(other.geometryTypes);
        this.nativeSrids = new ConcurrentHashMap<String, Integer>(other.nativeSrids);
        this.dimensions = new ConcurrentHashMap<String, Integer>(other.dimensions);
        this.parameters = new ConcurrentHashMap<String, VirtualTableParameter>(other.parameters);
        this.primaryKeyColumns = new ArrayList<String>(other.primaryKeyColumns);
        this.escapeSql = other.escapeSql;
    }

    /** Clone a virtual table */
    public VirtualTable(VirtualTable other) {
        this(other.name, other.sql);
        this.geometryTypes =
                new ConcurrentHashMap<String, Class<? extends Geometry>>(other.geometryTypes);
        this.nativeSrids = new ConcurrentHashMap<String, Integer>(other.nativeSrids);
        this.dimensions = new ConcurrentHashMap<String, Integer>(other.dimensions);
        this.parameters = new ConcurrentHashMap<String, VirtualTableParameter>(other.parameters);
        this.primaryKeyColumns = new ArrayList<String>(other.primaryKeyColumns);
        this.escapeSql = other.escapeSql;
    }

    /**
     * Returns the virtual table primary key columns. It should refer to fields returned by the
     * query, if that is not true the behavior is undefined
     */
    public List<String> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    /** Sets the virtual table primary key */
    public void setPrimaryKeyColumns(List<String> primaryKeyColumns) {
        this.primaryKeyColumns.clear();
        if (primaryKeyColumns != null) {
            this.primaryKeyColumns.addAll(primaryKeyColumns);
        }
    }

    /** The virtual table name */
    public String getName() {
        return name;
    }

    /** The virtual table sql (raw, without parameter expansion) */
    public String getSql() {
        return sql;
    }

    public String expandParameters(Hints hints) throws SQLException {
        // no need for expansion if we don't have parameters
        String result = sql;
        if (!keepWhereClausePlaceHolder(hints)) {
            // remove the where clause place holder
            result = removeWhereClausePlaceHolder(result);
        }
        if (parameters.size() == 0) {
            return result;
        }

        // grab the parameter values
        Map<String, String> values = null;
        if (hints != null) {
            values = (Map<String, String>) hints.get(Hints.VIRTUAL_TABLE_PARAMETERS);
        }
        if (values == null) {
            values = Collections.emptyMap();
        }

        // perform the expansion, checking for validity and applying default values as needed
        for (VirtualTableParameter param : parameters.values()) {
            String value = values.get(param.getName());
            if (value == null) {
                // use the default value and eventually prepare to expand the empty string
                value = param.getDefaultValue();
                if (value == null) {
                    value = "";
                }
            } else {
                if (param.getValidator() != null) {
                    try {
                        param.getValidator().validate(value);

                        // Parameter value has passed validation, perform sql escaping
                        // if enabled
                        if (escapeSql) {
                            value = EscapeSql.escapeSql(value);
                        }
                    } catch (IllegalArgumentException e) {
                        // fully log the exception, but only rethrow a more generic description as
                        // the message could be exposed to attackers
                        LOGGER.log(
                                Level.SEVERE, "Invalid value for parameter " + param.getName(), e);
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
     */
    public void addGeometryMetadatata(
            String geometry, Class<? extends Geometry> binding, int nativeSrid) {
        geometryTypes.put(geometry, binding);
        nativeSrids.put(geometry, nativeSrid);
    }

    /**
     * Adds geometry metadata to the virtual table. This is important to get the datastore working,
     * often that is not the case if the right native srid is not in place
     */
    public void addGeometryMetadatata(
            String geometry, Class<? extends Geometry> binding, int nativeSrid, int dimension) {
        geometryTypes.put(geometry, binding);
        nativeSrids.put(geometry, nativeSrid);
        dimensions.put(geometry, dimension);
    }

    /** Adds a parameter to the virtual table */
    public void addParameter(VirtualTableParameter param) {
        parameters.put(param.getName(), param);
    }

    /** Removes a parameter from the virtual table */
    public void removeParameter(String paramName) {
        parameters.remove(paramName);
    }

    /** The current parameter names */
    public Collection<String> getParameterNames() {
        return new ArrayList(parameters.keySet());
    }

    /** Returns the requested parameter, or null if it could not be found */
    public VirtualTableParameter getParameter(String name) {
        return parameters.get(name);
    }

    /** Returns the geometry's specific type, or null if not known */
    public Class<? extends Geometry> getGeometryType(String geometryName) {
        return geometryTypes.get(geometryName);
    }

    /** Returns the name of the geometry colums declared in this virtual table */
    public Set<String> getGeometries() {
        return geometryTypes.keySet();
    }

    /** Returns the geometry native srid, or -1 if not known */
    public int getNativeSrid(String geometryName) {
        Integer srid = nativeSrids.get(geometryName);
        if (srid == null) {
            srid = -1;
        }
        return srid;
    }

    /** Returns the geometry dimension, or 2 if not known */
    public int getDimension(String geometryName) {
        Integer dimension = dimensions.get(geometryName);
        if (dimension == null) {
            dimension = 2;
        }
        return dimension;
    }

    public boolean isEscapeSql() {
        return escapeSql;
    }

    public void setEscapeSql(boolean escapeSql) {
        this.escapeSql = escapeSql;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((geometryTypes == null) ? 0 : geometryTypes.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((nativeSrids == null) ? 0 : nativeSrids.hashCode());
        result = prime * result + ((dimensions == null) ? 0 : dimensions.hashCode());
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((primaryKeyColumns == null) ? 0 : primaryKeyColumns.hashCode());
        result = prime * result + ((sql == null) ? 0 : sql.hashCode());
        result = prime * result + ((escapeSql) ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VirtualTable other = (VirtualTable) obj;
        if (geometryTypes == null) {
            if (other.geometryTypes != null) return false;
        } else if (!geometryTypes.equals(other.geometryTypes)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (nativeSrids == null) {
            if (other.nativeSrids != null) return false;
        } else if (!nativeSrids.equals(other.nativeSrids)) return false;
        if (dimensions == null) {
            if (other.dimensions != null) return false;
        } else if (!dimensions.equals(other.dimensions)) return false;
        if (parameters == null) {
            if (other.parameters != null) return false;
        } else if (!parameters.equals(other.parameters)) return false;
        if (primaryKeyColumns == null) {
            if (other.primaryKeyColumns != null) return false;
        } else if (!primaryKeyColumns.equals(other.primaryKeyColumns)) return false;
        if (sql == null) {
            if (other.sql != null) return false;
        } else if (!sql.equals(other.sql)) return false;
        if (escapeSql != other.escapeSql) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VirtualTable [name=" + name + ", sql=" + sql + "]";
    }

    /**
     * Helper method that return TRUE if the where clause place holder should be keep, otherwise
     * FALSE if it should be removed.
     */
    private static boolean keepWhereClausePlaceHolder(Hints hints) {
        if (hints == null) {
            // no hints provided, we are done
            return false;
        }
        // let's see if the where clause place holder needs to be removed
        Object value = hints.get(KEEP_WHERE_CLAUSE_PLACE_HOLDER_KEY);
        return value != null && value instanceof Boolean && (boolean) value;
    }

    /**
     * Helper method that will remove, if present, the where clause place holder from the provided
     * SQL query. If multiple where clause place holders are present an exception will be throw.
     */
    private static String removeWhereClausePlaceHolder(String sql) {
        int whereClauseIndex = sql.indexOf(WHERE_CLAUSE_PLACE_HOLDER);
        if (whereClauseIndex < 0) {
            // no where clause place holder provided
            return sql;
        }
        if (whereClauseIndex != sql.lastIndexOf(WHERE_CLAUSE_PLACE_HOLDER)) {
            // only a single where clause place holder is supported
            throw new RuntimeException(
                    String.format("SQL contains multiple where clause placeholders: %s.", sql));
        }
        // remove the where clause place holder
        return sql.replace(WHERE_CLAUSE_PLACE_HOLDER, "");
    }
}
