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
package org.geotools.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.PropertyName;

/**
 * Represents the joining of two feature types within a {@link Query}.
 * <p>
 * The Join class is similar to Query in that it allows one to specify a FeatureType name, a set of
 * properties, and a filter. A Join must specify:
 * <ol>
 *   <li>A type name that references the feature type to join to, see {@link #getTypeName()}
 *   <li>A join filter that describes how to join, see {@link #getJoinFilter()}
 * </ol> 
 * Optionally a Join may also specify:
 * <ul>
 *   <li>A set of property names constraining the attributes of joined features, see {@link #getProperties()}
 *   <li>A secondary filter used to constrained features from the joined feature type, see {@link #getFilter()}
 *   <li>An alias for the joined feature type, which can be used in the join filter to disambiguate
 *    attributes of the feature types being joined, see {@link #getAlias()}
 *   <li>A join type specifying what type of join (inner, outer, etc...) should be performed, see {@link #getType()}
 * </ul>
 * </p>
 * 
 * @author Justin Deoliveira, OpenGeo
 * 
 * @since 8.0
 */
public class Join {

    /**
     * filter factory
     */
    static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    /**
     * type of join
     */
    public static enum Type {
        INNER, OUTER;
    }

    /** join type */
    Type type;

    /**
     * the feature type name being joined to
     */
    String typeName;

    /**
     * attributes to fetch for this feature type 
     */
    List<PropertyName> properties = Query.ALL_PROPERTIES;

    /** 
     * the join predicate
     */
    Filter join;

    /**
     * additional predicate against the target of the join 
     */
    Filter filter;

    /**
     * The alias to be used for the typeName in this join
     */
    String alias;

    /**
     * Constructs a join.
     * 
     * @param typeName The name of the feature type to join to.
     * @param join The filter specifying the join condition between the two feature types being 
     *  joined.
     */
    public Join(String typeName, Filter join) {
        this.typeName = typeName;
        this.join = join;
        this.type = Type.INNER;
        this.properties = Query.ALL_PROPERTIES;
        this.filter = Filter.INCLUDE;
        this.alias = null;
    }

    /**
     * Constructs a join from another.
     */
    public Join(Join other) {
        this.typeName = other.getTypeName();
        this.join = other.getJoinFilter();
        this.filter = other.getFilter();
        this.type = other.getType();
        this.properties = other.getProperties();
        this.filter = other.getFilter();
        this.alias = other.getAlias();
    }

    /**
     * The name of the feature type being joined to.
     * <p>
     * This name may be the same as the name of the primary feature type, this is how a self join is
     * specified.
     * </p>
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * The filter defining the join condition between the primary feature type and the feature 
     * type being joined to.
     * <p>
     * This filter should be a comparison operator whose contents are two {@link PropertyName} 
     * instances. For example:
     * <pre>
     * new Join("theOtherType", propertyIsEqualTo(propertyName("foo"), propertyName("bar")));
     * </pre>
     * In instances where the two property names involved in the join are the same a prefix or 
     * alias must be used to differentiate:
     * <pre>
     * Join j = new Join("theOtherType", propertyIsEqualTo(propertyName("foo"), propertyName("other.bar")));
     * j.alias("other");
     * </pre>
     * </p>
     */
    public Filter getJoinFilter() {
        return join;
    }

    /**
     * Sets the join type.
     * @see #getType()
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * The type of the join.
     * <p>
     * {@link Type#INNER} is the default join type.
     * </p>
     * 
     */
    public Type getType() {
        return type;
    }

    /**
     * List of properties specifying which attributes of joined features to obtain.
     * <p>
     * This method has the same purpose as {@link Query#getProperties()}.
     * </p>
     */
    public List<PropertyName> getProperties() {
        if (properties == Query.ALL_PROPERTIES) {
            return properties;
        }
        return Collections.unmodifiableList(properties);
    }

    /**
     * Sets list of properties specifying which attributes of joined features to obtain.
     * <p>
     * This method has the same purpose as {@link Query#setProperties(List)}.
     * </p>
     */
    public void setProperties(List<PropertyName> properties) {
        this.properties = properties;
    }

    /**
     * List of property names specifying which attributes of joined features to obtain.
     * <p>
     * This method has the same purpose as {@link Query#getPropertyNames()}.
     * </p>
     */
    public String[] getPropertyNames() {
        if (properties == Query.ALL_PROPERTIES) {
            return Query.ALL_NAMES;
        }
        
        String[] names = new String[properties.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = properties.get(i).getPropertyName();
        }
        return names;
    }

    /**
     * Sets the filter used to constrain which features from the joined feature type to return. 
     * 
     * @see #getFilter()
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Filter used to constrain which features from the joined feature type to return.
     * <p>
     * This filter must only reference attributes from the joined feature type, and not of any other
     * feature types involved in the join.
     * </p>
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Sets an alias for the feature type being joined to.
     * @see #getAlias()
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * An alias for the feature type being joined to.
     * <p>
     * This method is useful in cases where the two feature types being joined contain attributes 
     * identically named, or in cases where a self join is being performed:
     * <pre>
     * Join j = new Join("theOtherType", PropertyIsEqualTo(PropertyName("foo"), PropertyName("other.foo")));
     * j.setAlias("other");
     * </pre>
     * </p>
     * 
     * @see #getJoinFilter()
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Convenience method that returns the attribute name to be used for this join.
     * <p>
     * Convenience for:
     * <code>
     * <pre>
     *  return getAlias() != null ? getAlias() : getTypeName();
     * </pre>
     * </code>
     * </p>
     */
    public String attributeName() {
        return getAlias() != null ? getAlias() : getTypeName();
    }

    /**
     * Chaining method for {@link #getProperties()}
     */
    public Join properties(String... properties) {
        this.properties = new ArrayList();
        for (String p : properties) {
            this.properties.add(ff.property(p));
        }
        return this;
    }

    /**
     * Chaining method for {@link #setFilter(Filter)}
     */
    public Join filter(Filter filter) {
        setFilter(filter);
        return this;
    }

    /**
     * Chaining method for {@link #setAlias(String)}
     */
    public Join alias(String alias) {
        setAlias(alias);
        return this;
    }

    /**
     * Chaining method for {@link #setType(Type)}
     */
    public Join type(Type type) {
        setType(type);
        return this;
    }
}
