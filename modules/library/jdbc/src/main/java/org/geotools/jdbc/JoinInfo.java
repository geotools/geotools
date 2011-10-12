/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.Join;
import org.geotools.data.Query;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;

/**
 * Holds information about a join query.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class JoinInfo {

    public static JoinInfo create(Query query, JDBCFeatureSource featureSource) throws IOException {
        return create(query, featureSource.getSchema(), featureSource.getDataStore());
    }

    public static JoinInfo create(Query query, SimpleFeatureType featureType, JDBCDataStore dataStore)
        throws IOException {
        
        JoinInfo info = new JoinInfo();
        info.setPrimaryAlias("a");
        
        for (int i = 0; i < query.getJoins().size(); i++) {
            Join j = query.getJoins().get(i);

            JoinPart part = new JoinPart(j);
            info.getParts().add(part);

            //load the feature type being joined to
            JDBCFeatureSource joinFeatureSource = dataStore.getAbsoluteFeatureSource(j.getTypeName());
            part.setFeatureSource(joinFeatureSource);

            //ensure every join as a unique alias
            String alias = String.valueOf((char)('b' + i));
            part.setAlias(alias);

            //hack on the join filter as necessary
            Filter joinFilter = j.getJoinFilter();

            if (query.getAlias() != null) {
                //rewrite any user specified alias with the one we specified
                joinFilter = 
                    (Filter) joinFilter.accept(new JoinPrefixRewriter(query.getAlias(), "a"), null);
            }
            if (j.getAlias() != null) {
                //rewrite any user specified alias with the one we specified
                joinFilter = 
                    (Filter) joinFilter.accept(new JoinPrefixRewriter(j.getAlias(), alias), null);
            }

            //qualify all property names in the join filter so that they known about their 
            // feature type and alias
            joinFilter = (Filter) joinFilter.accept(new JoinQualifier(featureType, "a", 
                    joinFeatureSource.getSchema(), alias), null);
            part.setJoinFilter(joinFilter);

            //split the other filter
            Filter[] prePostFilters = joinFeatureSource.splitFilter(j.getFilter());

            //build the query and return feature types based on the post filter
            SimpleFeatureType[] types = joinFeatureSource.buildQueryAndReturnFeatureTypes(
                joinFeatureSource.getSchema(), j.getPropertyNames(), prePostFilters[1]);

            //alias any attributes in this feature type that clash with attributes in the primary
            // feature type
            types[0] = SimpleFeatureTypeBuilder.copy(types[0]);
            for (AttributeDescriptor att : types[0].getAttributeDescriptors()) {
                if (featureType.getDescriptor(att.getName()) != null) {
                    att.getUserData().put(
                            JDBCDataStore.JDBC_COLUMN_ALIAS, alias + "_" + att.getLocalName());
                }
            }

            part.setQueryFeatureType(types[0]);
            part.setReturnFeatureType(types[1]);

            //qualify the pre filter
            if (prePostFilters[0] != null && prePostFilters[0] != Filter.INCLUDE) {
                prePostFilters[0] = (Filter) prePostFilters[0].accept(
                    new JoinQualifier(joinFeatureSource.getSchema(), alias), null); 
            }
            part.setPreFilter(prePostFilters[0]);
            part.setPostFilter(prePostFilters[1]);
            
            //assign an attribute name in the resulting feature type
            //TODO: we should check to ensure that the joined feature type attribute name are 
            // actually unique
            part.setAttributeName(part.getJoin().getAlias() != null ? 
                part.getJoin().getAlias() : part.getQueryFeatureType().getTypeName());
        }

        //qualify the main query filter
        Filter filter = query.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            filter = (Filter) filter.accept(new JoinQualifier(featureType, "a"), null);
        }
        info.setFilter(filter);
        return info;
    }

    /** primary table alias */
    String primaryAlias;
     
    /** parts of the join */
    List<JoinPart> parts = new ArrayList();

    /** the "joinified" filter of the main query */
    Filter filter;

    private JoinInfo() {
    }

    public String getPrimaryAlias() {
        return primaryAlias;
    }

    public void setPrimaryAlias(String primaryAlias) {
        this.primaryAlias = primaryAlias;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public boolean hasPostFilters() {
        for (JoinPart p : parts) {
            if (p.getPostFilter() != null && !Filter.INCLUDE.equals(p.getPostFilter())) {
                return true;
            }
        }
        return false;
    }

    public List<JoinPart> getParts() {
        return parts;
    }

    public static class JoinPart {

        /** original join object */
        Join join;

        /** assigned alias */
        String alias;

        /** join filter */
        Filter joinFilter;

        /** feature source being joined to */
        JDBCFeatureSource featureSource;

        /** query feature type */
        SimpleFeatureType queryFeatureType;

        /** join feature type */ 
        SimpleFeatureType returnFeatureType;

        /** pre filter */
        Filter preFilter;

        /** post filter */
        Filter postFilter;
        
        /** the attribute in the final feature type this part is assigned */
        String attributeName;
        
        public JoinPart(Join join) {
            this.join = join;
        }

        public Join getJoin() {
            return join;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public Filter getJoinFilter() {
            return joinFilter;
        }

        public void setJoinFilter(Filter joinFilter) {
            this.joinFilter = joinFilter;
        }

        public JDBCFeatureSource getFeatureSource() {
            return featureSource;
        }

        public void setFeatureSource(JDBCFeatureSource featureSource) {
            this.featureSource = featureSource;
        }

        public SimpleFeatureType getQueryFeatureType() {
            return queryFeatureType;
        }

        public void setQueryFeatureType(SimpleFeatureType queryFeatureType) {
            this.queryFeatureType = queryFeatureType;
        }

        public SimpleFeatureType getReturnFeatureType() {
            return returnFeatureType;
        }

        public void setReturnFeatureType(SimpleFeatureType returnFeatureType) {
            this.returnFeatureType = returnFeatureType;
        }

        public Filter getPreFilter() {
            return preFilter;
        }

        public void setPreFilter(Filter preFilter) {
            this.preFilter = preFilter;
        }

        public Filter getPostFilter() {
            return postFilter;
        }

        public void setPostFilter(Filter postFilter) {
            this.postFilter = postFilter;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }
    }

    static class JoinPrefixRewriter extends DuplicatingFilterVisitor {
        String from, to;
        
        public JoinPrefixRewriter(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public Object visit(PropertyName expression, Object extraData) {
            String name = expression.getPropertyName();
            if (name.startsWith(from+".")) {
                name = to + "." + name.substring((from+".").length());
            }
            return getFactory(extraData).property(name, expression.getNamespaceContext());
        }
    }
    
    static class JoinQualifier extends DuplicatingFilterVisitor {

        SimpleFeatureType ft1, ft2;
        String alias1, alias2;
        
        public JoinQualifier(SimpleFeatureType ft, String alias) {
            this(ft, alias, null, null);
        }
        
        public JoinQualifier(SimpleFeatureType ft1, String alias1, SimpleFeatureType ft2, String alias2) {
            this.ft1 = ft1;
            this.ft2 = ft2;
            this.alias1 = alias1;
            this.alias2 = alias2;
        }

        @Override
        public Object visit(PropertyName expression, Object extraData) {
            String name = expression.getPropertyName();
            String[] split = name.split("\\.");

            //if split.length > 2 then join up remaining parts, means the column name itself had a 
            // period in it
            if (split.length > 2) {
                String prefix = split[0];
                StringBuffer sb = new StringBuffer();
                for (int i = 1; i < split.length; i++) {
                    sb.append(split[i]);
                }
                split = new String[]{prefix, sb.toString()};
            }

            JoinPropertyName propertyName = null;

            //if we only have one feature type its easy, use the first feature type
            if (ft2 == null) {
                propertyName = new JoinPropertyName(ft1, alias1, split.length > 1 ? split[1] : split[0]);
            }
            else {
                if (split.length == 1) {
                    //name was unprefixed, figure out what feature type the meant
                    SimpleFeatureType ft = ft1.getDescriptor(split[0]) != null ? ft1 : 
                        ft2.getDescriptor(split[0]) != null ? ft2 : null; 
                    if (ft == null) {
                        throw new IllegalArgumentException(String.format("Attribute '%s' not present in"
                         +   " either type '%s' or '%s'", split[0], ft1.getTypeName(), ft2.getTypeName()));
                    }

                    propertyName = new JoinPropertyName(ft, ft == ft1 ? alias1 : alias2, split[0]); 
                }
                else {
                    //name was prefixed, look up the type based on prefix
                    SimpleFeatureType ft = split[0].equals(alias1) ? ft1 : 
                        split[0].equals(alias2) ? ft2 : null;
                    if (ft == null) {
                        throw new IllegalArgumentException(String.format("Prefix '%s' does not match " +
                            "either alias '%s' or '%s'", split[0], alias1, alias2));
                    }
                    
                    propertyName = new JoinPropertyName(ft, split[0], split[1]); 
                }
            }
            
            return propertyName;
        }
    }

}
