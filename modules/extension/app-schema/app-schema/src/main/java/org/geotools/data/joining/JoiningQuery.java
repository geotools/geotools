/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.joining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.complex.FeatureTypeMapping;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.sort.SortBy;

/**
 * Special Query that includes joining information
 *
 * @author Niels Charlier (Curtin University of Technology)
 */
public class JoiningQuery extends Query {

    public static class QueryJoin extends JoiningQuery {
        protected String joiningTypeName;
        protected Expression foreignKeyName;
        protected Expression joiningKeyName;
        protected SortBy[] sortBy;

        public String getJoiningTypeName() {
            return joiningTypeName;
        }

        public void setJoiningTypeName(String joiningTypeName) {
            this.joiningTypeName = joiningTypeName;
        }

        public Expression getForeignKeyName() {
            return foreignKeyName;
        }

        public void setForeignKeyName(Expression foreignKeyName) {
            this.foreignKeyName = foreignKeyName;
        }

        public Expression getJoiningKeyName() {
            return joiningKeyName;
        }

        public void setJoiningKeyName(Expression joiningKeyName) {
            this.joiningKeyName = joiningKeyName;
        }
    }

    protected List<QueryJoin> queryJoins;

    /*
     * True if the query shouldn't join to the table to find other rows with same id. This is in
     * case of there's a filter for multi-valued properties for timeseries. This is a requirement
     * for timeseries to return a subset instead of full features.
     */
    private boolean isSubset;

    private boolean isDenormalised;

    protected List<String> ids;

    FeatureTypeMapping rootMapping;

    public JoiningQuery(JoiningQuery query) {
        super(query);
        setQueryJoins(query.getQueryJoins());
        setSubset(query.isSubset);
        isDenormalised = query.isDenormalised;
        ids = query.ids;
    }

    public JoiningQuery(Query query) {
        super(query);
        ids = new ArrayList<String>();
    }

    public JoiningQuery() {
        ids = new ArrayList<String>();
    }

    public void setQueryJoins(List<QueryJoin> queryJoins) {
        this.queryJoins = queryJoins;
    }

    public List<QueryJoin> getQueryJoins() {
        if (queryJoins == null) {
            return Collections.EMPTY_LIST;
        }
        return queryJoins;
    }

    public void setSubset(boolean isSubset) {
        this.isSubset = isSubset;
    }

    public boolean isSubset() {
        return isSubset;
    }

    public boolean hasIdColumn() {
        return !ids.isEmpty();
    }

    public void addId(String pn) {
        this.ids.add(pn);
    }

    public List<String> getIds() {
        return ids;
    }

    public boolean isDenormalised() {
        return isDenormalised;
    }

    public void setDenormalised(boolean isDenormalised) {
        this.isDenormalised = isDenormalised;
    }

    public FeatureTypeMapping getRootMapping() {
        return rootMapping;
    }

    public void setRootMapping(FeatureTypeMapping rootMapping) {
        this.rootMapping = rootMapping;
    }
}
