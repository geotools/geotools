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
import java.util.List;

import org.geotools.data.Query;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.sort.SortBy;

/**
 * 
 * Special Query that includes joining information
 * 
 * @author Niels Charlier (Curtin University of Technology)
 *
 *
 * @source $URL$
 */
public class JoiningQuery extends Query {
    
    // true if idExpression has been mapped to a database column
    // false if it's a string constant or has been omitted, and PK should be used if available
    private boolean hasIdColumn;
    
    public static class QueryJoin {
        protected String joiningTypeName;    
        protected Expression foreignKeyName;    
        protected Expression joiningKeyName;
        protected SortBy[] sortBy;
        protected List<String> ids = new ArrayList<String>(); 
                
        public String getJoiningTypeName() {
            return joiningTypeName;
        }

        public void setJoiningTypeName(String joiningTypeName) {
            this.joiningTypeName = joiningTypeName;
        }

        public Expression getForeignKeyName() {
            return foreignKeyName;
        }
        
        public SortBy[] getSortBy() {
            return sortBy;
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
        
        public void setSortBy(SortBy[] sortBy){
            this.sortBy = sortBy;
        }
        
        public void addId(String pn) {
            this.ids.add(pn);
        }
        
        public List<String> getIds() {
            return ids;
        }
    }
    
    protected List<QueryJoin> queryJoins;
    
    /*
     * True if the query shouldn't join to the table to find other rows with same id. This is in
     * case of there's a filter for multi-valued properties for timeseries. This is a requirement
     * for timeseries to return a subset instead of full features.
     */
    private boolean isSubset;
    
    public JoiningQuery(JoiningQuery query) {
        super(query);
        this.hasIdColumn = query.hasIdColumn;
        setQueryJoins(query.getQueryJoins());
        setSubset(query.isSubset);
    }
    
    public JoiningQuery(Query query, boolean hasIdColumn){
        super(query);
        this.hasIdColumn = hasIdColumn;
    }
    
    public JoiningQuery() {
    }   
    
    public void setQueryJoins(List<QueryJoin> queryJoins){
        this.queryJoins = queryJoins;
    }
    
    public List<QueryJoin> getQueryJoins(){
        return queryJoins;
    }
    
    public void setSubset(boolean isSubset) {
        this.isSubset = isSubset;
    }
    
    public boolean isSubset() {
        return isSubset;
    }
    
    public boolean hasIdColumn() {
        return hasIdColumn;
    }

}
