/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.api.data;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.identity.ResourceId;
import org.geotools.api.filter.identity.Version;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;

/**
 * Encapsulates a request for data, typically as:
 *
 * <pre><code>
 * Query query = ...
 * myFeatureSource.getFeatures(query);
 * </code></pre>
 *
 * The query class is based on the Web Feature Server specification and offers a few interesting capabilities such as
 * the ability to sort results and use a filter (similar to the WHERE clause in SQL).
 *
 * <p>Additional capabilities:
 *
 * <ul>
 *   <li>{@linkplain #setMaxFeatures(int)} and {@linkplain #setStartIndex(Integer)} can be used implement 'paging'
 *       through the data source's content. This is useful if, for example, the FeatureSource has an upper limit on the
 *       number of features it can return in a single request or you are working with limited memory.
 *   <li>{@linkplain #setHandle(String)} can be used to give the query a mnemonic name which will appear in error
 *       reporing and logs.
 *   <li>{@linkplain #setCoordinateSystem(CoordinateReferenceSystem)} is used to to specify the coordinate system that
 *       retrieved features will be "forced" into. This is often used to correct a feature source when the application
 *       and the data format have different ideas about the coordinate system (for example, the "axis order" issue).
 *   <li>{@linkplain #setCoordinateSystemReproject(CoordinateReferenceSystem)} is used to ask for the retrieved features
 *       to be reproejcted.
 * </ul>
 *
 * Vendor specific:
 *
 * <ul>
 *   <li>{@linkplain setHints(Hints)} is used to specify venfor specific capabilities provided by a feature source
 *       implementation.
 * </ul>
 *
 * Joins:
 *
 * <p>The Query class supports the concepts of joins in that a query can result in a join of the feature type to other
 * feature types in the same datastore. For example, the following would be a spatial join that selected the country
 * that contain a particular city.
 *
 * <pre><code>
 * Query query = new Query("countries");
 * Join join = new Join("cities", CQL.toFilter("CONTAINS(geometry, b.geometry)"));
 * join.setAlias("b");
 * join.setFilter(CQL.toFilter("CITY_NAME = 'Canmore'"))
 * query.getJoins().add(join);
 * </code></pre>
 *
 * Example:
 *
 * <pre><code>
 * Filter filter = CQL.toFilter("NAME like '%land'");
 * Query query = new Query( "countries", filter );
 *
 * FeatureCollection features = featureSource.getFeatures( query );
 * </code></pre>
 *
 * @author Chris Holmes
 * @version $Id$
 */
public class Query {

    /**
     * When specifying properties to select, setting this hint flag true tells the datastore to include mandatory
     * properties (i.e. properties with minOccurs &gt;= 1) in the end result, irrespective of whether they are not
     * included in the list of properties.
     *
     * <p>Datastores may implement adding all mandatory properties to the end result when this flag is set to true. For
     * example:
     *
     * <p>{@code Object includeProps = query.getHints().get(Query.INCLUDE_MANDATORY_PROPS); if (includeProps instanceof
     * Boolean && ((Boolean)includeProps).booleanValue()) { query.setProperties
     * (DataUtilities.addMandatoryProperties(type, query.getProperties())); } }
     */
    public static Hints.Key INCLUDE_MANDATORY_PROPS = new Hints.Key(Boolean.class);

    /**
     * Constant (actually null) used to represent no namespace restrictions on the returned result, should be considered
     * ANY_URI
     */
    public static final URI NO_NAMESPACE = null;

    /** So getMaxFeatures does not return null we use a very large number. */
    public static final int DEFAULT_MAX = Integer.MAX_VALUE;

    /**
     * Implements a query that will fetch all features from a datasource. This query should retrieve all properties,
     * with no maxFeatures, no filtering, and the default featureType.
     */
    @SuppressWarnings("ClassInitializationDeadlock")
    public static final Query ALL = new ALLQuery();

    /**
     * Implements a query that will fetch all the FeatureIDs from a datasource. This query should retrieve no
     * properties, with no maxFeatures, no filtering, and the a featureType with no attribtues.
     */
    @SuppressWarnings("ClassInitializationDeadlock")
    public static final Query FIDS = new FIDSQuery();

    /**
     * A constant (empty String array) that can be used with {@linkplain #setPropertyNames(String[])} to indicate that
     * no properties are to be retrieved.
     *
     * <p>Note the query will still return a result - limited to FeatureIDs.
     */
    public static final String[] NO_NAMES = new String[0];

    /**
     * A constant (value {@code null}) that can be used with {@linkplain #setPropertyNames(String[])} to indicate that
     * all properties are to be retrieved.
     */
    public static final String[] ALL_NAMES = null;

    /**
     * A constant (empty String array) that can be used with {@linkplain #setProperties} to indicate that no properties
     * are to be retrieved.
     *
     * <p>Note the query will still return a result - limited to FeatureIDs.
     */
    public static final List<PropertyName> NO_PROPERTIES = Collections.emptyList();

    /**
     * A constant (value {@code null}) that can be used with {@linkplain #setProperties} to indicate that all properties
     * are to be retrieved.
     */
    public static final List<PropertyName> ALL_PROPERTIES = null;

    /** The properties to fetch */
    protected List<PropertyName> properties;

    /** The maximum numbers of features to fetch */
    protected int maxFeatures = Query.DEFAULT_MAX;

    /** The index of the first feature to process */
    protected Integer startIndex = null;

    /** The filter to constrain the request. */
    protected Filter filter = Filter.INCLUDE;

    /** The typeName to get */
    protected String typeName;

    /** The optional alias for type name */
    protected String alias;

    /** The namespace to get */
    protected URI namespace = Query.NO_NAMESPACE;

    /** The handle associated with this query. */
    protected String handle;

    /** Coordinate System associated with this query */
    protected CoordinateReferenceSystem coordinateSystem;

    /** Reprojection associated associated with this query */
    protected CoordinateReferenceSystem coordinateSystemReproject;

    /** Sorting for the query */
    protected SortBy[] sortBy;

    /** The version according to WFS 1.0 and 1.1 specs */
    protected String version;

    /** The hints to be used during query execution */
    protected Hints hints;

    /** join clauses for this query */
    protected List<Join> joins = new ArrayList<>();

    /**
     * Default constructor. Use setter methods to configure the Query before use (the default Query will retrieve all
     * features).
     */
    public Query() {
        // no arg
    }

    /**
     * Constructor.
     *
     * @param typeName the name of the featureType to retrieve
     */
    public Query(String typeName) {
        this(typeName, Filter.INCLUDE);
    }

    /**
     * Constructor.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param filter the OGC filter to constrain the request.
     */
    public Query(String typeName, Filter filter) {
        this(typeName, filter, Query.ALL_NAMES);
    }

    /**
     * Constructor.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param filter the OGC filter to constrain the request.
     * @param properties an array of the properties to fetch.
     */
    public Query(String typeName, Filter filter, String... properties) {
        this(typeName, null, filter, Query.DEFAULT_MAX, properties, null);
    }

    /**
     * Constructor.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param filter the OGC filter to constrain the request.
     * @param properties a list of the properties to fetch.
     */
    public Query(String typeName, Filter filter, List<PropertyName> properties) {
        this(typeName, null, filter, Query.DEFAULT_MAX, properties, null);
    }

    /**
     * Constructor.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param filter the OGC filter to constrain the request.
     * @param maxFeatures the maximum number of features to be returned.
     * @param propNames an array of the properties to fetch.
     * @param handle the name to associate with this query.
     */
    public Query(String typeName, Filter filter, int maxFeatures, String[] propNames, String handle) {
        this(typeName, null, filter, maxFeatures, propNames, handle);
    }

    /**
     * Constructor.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param filter the OGC filter to constrain the request.
     * @param maxFeatures the maximum number of features to be returned.
     * @param properties a list of the properties to fetch.
     * @param handle the name to associate with this query.
     */
    public Query(String typeName, Filter filter, int maxFeatures, List<PropertyName> properties, String handle) {
        this(typeName, null, filter, maxFeatures, properties, handle);
    }

    /**
     * Constructor.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param namespace Namespace for provided typeName, or null if unspecified
     * @param filter the OGC filter to constrain the request.
     * @param maxFeatures the maximum number of features to be returned.
     * @param propNames an array of the properties to fetch.
     * @param handle the name to associate with the query.
     */
    public Query(String typeName, URI namespace, Filter filter, int maxFeatures, String[] propNames, String handle) {
        this.typeName = typeName;
        this.filter = filter;
        this.namespace = namespace;
        this.maxFeatures = maxFeatures;
        this.handle = handle;
        setPropertyNames(propNames);
    }

    /**
     * Constructor.
     *
     * @param typeName the name of the featureType to retrieve.
     * @param namespace Namespace for provided typeName, or null if unspecified
     * @param filter the OGC filter to constrain the request.
     * @param maxFeatures the maximum number of features to be returned.
     * @param properties a list of the property names to fetch.
     * @param handle the name to associate with the query.
     */
    public Query(
            String typeName,
            URI namespace,
            Filter filter,
            int maxFeatures,
            List<PropertyName> properties,
            String handle) {
        this.typeName = typeName;
        this.filter = filter;
        this.namespace = namespace;
        this.maxFeatures = maxFeatures;
        this.handle = handle;
        this.properties = properties == null ? null : new ArrayList<>(properties);
    }

    /**
     * Copy contructor.
     *
     * @param query the query to copy
     */
    public Query(Query query) {
        this(
                query.getTypeName(),
                query.getNamespace(),
                query.getFilter(),
                query.getMaxFeatures(),
                query.getProperties(),
                query.getHandle());
        this.sortBy = query.getSortBy();
        this.coordinateSystem = query.getCoordinateSystem();
        this.coordinateSystemReproject = query.getCoordinateSystemReproject();
        this.version = query.getVersion();
        this.hints = query.getHints() != null ? new Hints(query.getHints()) : null;
        this.startIndex = query.getStartIndex();
        this.alias = query.getAlias();
        this.joins = new ArrayList<>();
        for (Join j : query.getJoins()) {
            this.joins.add(new Join(j));
        }
    }

    /**
     * Get the names of the properties that this Query will retrieve as part of the returned
     * {@linkplain org.geotools.feature.FeatureCollection}.
     *
     * @return the attributes to be used in the returned FeatureCollection.
     * @see #retrieveAllProperties()
     * @task REVISIT: make a FidProperties object, instead of an array size 0. I think Query.FIDS fills this role to
     *     some degree. Query.FIDS.equals( filter ) would meet this need?
     */
    public String[] getPropertyNames() {
        if (properties == null) {
            return null;
        }

        String[] propertyNames = new String[properties.size()];
        for (int i = 0; i < properties.size(); i++) {
            PropertyName propertyName = properties.get(i);
            if (propertyName != null) {
                String xpath = propertyName.getPropertyName();
                propertyNames[i] = xpath;
            }
        }
        return propertyNames;
    }

    /**
     * Set the names of the properties that this Query should retrieve as part of the returned
     * {@linkplain org.geotools.feature.FeatureCollection}. As well as an array of names, the following constants can be
     * used:
     *
     * <ul>
     *   <li>{@linkplain #ALL_NAMES} to retrieve all properties.
     *   <li>{@linkplain #NO_NAMES} to indicate no properties are required, just feature IDs.
     * </ul>
     *
     * The available properties can be determined with {@linkplain FeatureSource#getSchema()}. If properties that are
     * not part of the source's schema are requested an exception will be thrown.
     *
     * @param propNames the names of the properties to retrieve or one of {@linkplain #ALL_NAMES} or
     *     {@linkplain #NO_NAMES}.
     */
    public void setPropertyNames(String... propNames) {
        if (propNames == null) {
            properties = ALL_PROPERTIES;
            return;
        }

        final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        properties = new ArrayList<>(propNames.length);
        for (String xpath : propNames) {
            if (xpath != null) {
                properties.add(ff.property(xpath));
            }
        }
    }

    /**
     * Get the names of the properties that this Query will retrieve values for as part of the returned
     * {@linkplain org.geotools.feature.FeatureCollection}.
     *
     * @return the xpath expressions to be used in the returned FeatureCollection.
     * @see #retrieveAllProperties()
     */
    public List<PropertyName> getProperties() {
        if (properties == ALL_PROPERTIES) {
            return ALL_PROPERTIES;
        }
        return Collections.unmodifiableList(properties);
    }

    /**
     * Set the names of the properties that this Query should retrieve as part of the returned
     * {@linkplain org.geotools.feature.FeatureCollection}. As well as an array of names, the following constants can be
     * used:
     *
     * <ul>
     *   <li>{@linkplain #ALL_PROPERTIES} to retrieve all properties.
     *   <li>{@linkplain #NO_PROPERTIES} to indicate no properties are required, just feature IDs.
     * </ul>
     *
     * The available properties can be determined with {@linkplain FeatureSource#getSchema()}. If properties that are
     * not part of the source's schema are requested an exception will be thrown.
     *
     * @param propNames the names of the properties to retrieve or one of {@linkplain #ALL_PROPERTIES} or
     *     {@linkplain #NO_PROPERTIES}.
     */
    public void setProperties(List<PropertyName> propNames) {
        this.properties = propNames == ALL_PROPERTIES ? ALL_PROPERTIES : new ArrayList<>(propNames);
    }

    /**
     * Set the names of the properties that this Query should retrieve as part of the returned
     * {@linkplain org.geotools.feature.FeatureCollection}.
     *
     * <p>The available properties can be determined with {@linkplain FeatureSource#getSchema()}. If properties that are
     * not part of the source's schema are requested an exception will be thrown.
     *
     * @param propNames the names of the properties to retrieve or {@linkplain #ALL_NAMES}; an empty List can be passed
     *     in to indicate that only feature IDs should be retrieved
     * @task REVISIT: This syntax is really obscure. Consider having an fid or featureID propertyName that datasource
     *     implementors look for instead of looking to see if the list size is 0.
     */
    public void setPropertyNames(List<String> propNames) {
        if (propNames == null) {
            this.properties = ALL_PROPERTIES;
            return;
        }

        final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        properties = new ArrayList<>(propNames.size());
        for (String xpath : propNames) {
            if (xpath != null) {
                properties.add(ff.property(xpath));
            }
        }
    }

    /**
     * Convenience method to determine if the query should retrieve all properties defined in the schema of the feature
     * data source. This is equivalent to testing if {@linkplain #getPropertyNames()} returns {@linkplain #ALL_NAMES}.
     *
     * @return true if all properties will be retrieved by this Query; false otherwise
     */
    public boolean retrieveAllProperties() {
        return properties == null;
    }

    /**
     * Get the maximum number of features that will be retrieved by this Query.
     *
     * <p>Note: This is the only method that is not directly out of the Query element in the WFS specification. It is
     * instead a part of a GetFeature request, which can hold one or more queries. But each of those in turn will need a
     * maxFeatures, so it is needed here.
     *
     * <p>If the value returned here is max integer then the number of features should not be limited.
     *
     * @return the maximum number of features that will be retrieved by this query
     */
    public int getMaxFeatures() {
        return this.maxFeatures;
    }

    /**
     * Check if this query allows an unlimited number of features to be returned.
     *
     * <p>
     *
     * @return true maxFeatures is less then zero, or equal to Integer.MAX_VALUE.
     */
    public boolean isMaxFeaturesUnlimited() {
        return maxFeatures < 0 || maxFeatures == Integer.MAX_VALUE;
    }

    /**
     * Sets the maximum number of features that should be retrieved by this query. The default is to retrieve all
     * features.
     *
     * @param maxFeatures the maximum number of features to retrieve
     */
    public void setMaxFeatures(int maxFeatures) {
        this.maxFeatures = maxFeatures;
    }

    /**
     * Get the index of the first feature to retrieve.
     *
     * @return the index of the first feature to retrieve or {@code null} if no start index is defined.
     */
    public Integer getStartIndex() {
        return this.startIndex;
    }

    /**
     * Set the index of the first feature to retrieve. This can be used in conjuction with
     * {@linkplain #setMaxFeatures(int) } to 'page' through a feature data source.
     *
     * @param startIndex index of the first feature to retrieve or {@code null} to indicate no start index
     * @throws IllegalArgumentException if startIndex is less than 0
     */
    public void setStartIndex(Integer startIndex) {
        if (startIndex != null && startIndex.intValue() < 0) {
            throw new IllegalArgumentException("startIndex shall be a positive integer: " + startIndex);
        }
        this.startIndex = startIndex;
    }

    /**
     * Gets the filter used to define constraints on the features that will be retrieved by this Query.
     *
     * @return The filter that defines constraints on the query.
     */
    public Filter getFilter() {
        return this.filter;
    }

    /**
     * Sets the filter to constrain the features that will be retrieved by this Query. If no filter is set all features
     * will be retrieved (taking into account any bounds set via {@linkplain #setMaxFeatures(int) } and
     * {@linkplain #setStartIndex(java.lang.Integer) }).
     *
     * <p>The default is {@linkplain Filter#INCLUDE}.
     *
     * @param filter the OGC filter which features must pass through to be retrieved by this Query.
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Get the name of the feature type to be queried.
     *
     * @return the name of the feature type to be returned with this query.
     */
    public String getTypeName() {
        return this.typeName;
    }

    /**
     * Sets the name of the feature type to be queried. If no typename is specified, then the data source's default type
     * will be used. When working with sources such as shapefiles that only support one feature type this method can be
     * ignored.
     *
     * @param typeName the name of the featureType to retrieve.
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * An alias substitutable for {@link #getTypeName()}.
     *
     * <p>This value is typically used in a join query in which the join filter requires disambiguation due to property
     * name overlaps between joined types.
     *
     * @since 8.0
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the type name alias.
     *
     * @since 8.0
     * @see #getAlias()
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Get the namespace of the feature type to be queried.
     *
     * @return the gml namespace of the feature type to be returned with this query
     */
    public URI getNamespace() {
        return namespace;
    }

    /** Set the namespace of the feature type to be queried. */
    public void setNamespace(URI namespace) {
        this.namespace = namespace;
    }

    /**
     * Get the handle (mnemonic name) that will be associated with this Query. The handle is used in logging and error
     * reporting.
     *
     * @return the name to refer to this query.
     */
    public String getHandle() {
        return this.handle;
    }

    /**
     * Set the handle (mnemonic name) that will be associated with this Query. The handle is used in logging and error
     * reporting.
     *
     * @param handle the name to refer to this query.
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Defines version or version range requested.
     *
     * <p>From WFS Spec: <i>The version attribute is included in order to accommodate systems that support feature
     * versioning. A value of {@linkplain #ALL} indicates that all versions of a feature should be fetched. Otherwise an
     * integer, n, can be specified to return the n th version of a feature. The version numbers start at '1' which is
     * the oldest version. If a version value larger than the largest version is specified then the latest version is
     * return. The default action shall be for the query to return the latest version. Systems that do not support
     * versioning can ignore the parameter and return the only version that they have.</i>
     *
     * <p>GeoTools employs the following options:
     *
     * <ul>
     *   <li>{@link #setVersion(Date)}: "date: <i>dow mon dd hh:mm:ss zzz yyyy</i>"
     *   <li>{@link #setVersion(int)}: "<i>index</i>"
     *   <li>{@link #setVersion(Version.Action)}: "PREVIOUS", "LAST", "NEXT", "FIRST", "ALL"
     *   <li>{@link #setVersion(Date, Date)}: "start: <i>dow mon dd hh:mm:ss zzz yyyy end: <i>dow mon dd hh:mm:ss zzz
     *       yyyy"</i>
     * </ul>
     *
     * @return the version of the feature to return, or <code>null</code> for LAST.
     */
    public String getVersion() {
        return version;
    }

    public void setVersion(int index) {
        this.version = String.valueOf(index);
    }

    public void setVersion(Date date) {
        this.version = date == null ? null : "date:" + date;
    }

    public void setVersion(Version.Action action) {
        this.version = action == null ? null : action.name();
    }

    public void setVersion(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {
            this.version = null;
        } else {
            this.version = "start:" + startTime + " end:" + endTime;
        }
    }

    public void setVersion(ResourceId history) {
        if (history.getStartTime() != null && history.getEndTime() != null) {
            setVersion(history.getStartTime(), history.getEndTime());
        } else if (history.getVersion() != null) {
            Version ver = history.getVersion();
            if (ver.isVersionAction()) {
                setVersion(ver.getVersionAction());
            } else if (ver.isDateTime()) {
                setVersion(ver.getDateTime());
            } else if (ver.isIndex()) {
                setVersion(ver.getIndex());
            }
        }
    }
    /**
     * Set the version of features to retrieve where this is supported by the data source being queried.
     *
     * @see #getVersion() getVersion() for explanation
     * @since 2.4
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get the coordinate system to use as an override for features retrieved by this Query.
     *
     * @return The coordinate system to be returned for Features from this Query (override the set coordinate system).
     */
    public CoordinateReferenceSystem getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     * Provide an override coordinate system to apply to features retrieved by this Query.
     *
     * <p>This denotes a request to <b>temporarily</b> override the coordinate system contained in the feature data
     * source being queried. The same coordinate values will be used, but the features retrieved will appear in this
     * Coordinate System.
     *
     * <p>This change is not persistent and only applies to the features returned by this Query. If used in conjunction
     * with {@link #getCoordinateSystemReproject()} the reprojection will occur from {@link #getCoordinateSystem()} to
     * {@link #getCoordinateSystemReproject()}.
     *
     * @param system the coordinate system to apply to features retrieved by this Query
     */
    public void setCoordinateSystem(CoordinateReferenceSystem system) {
        coordinateSystem = system;
    }

    /**
     * If reprojection has been requested, this returns the coordinate system that features retrieved by this Query will
     * be reprojected into.
     *
     * @return the coordinate system that features will be reprojected into (if set)
     * @see #setCoordinateSystemReproject( CoordinateReferenceSystem )
     */
    public CoordinateReferenceSystem getCoordinateSystemReproject() {
        return coordinateSystemReproject;
    }

    /**
     * Request that features retrieved by this Query be reprojected into the given coordinate system.
     *
     * <p>If used in conjunction with {@link #setCoordinateSystem(CoordinateReferenceSystem)} the reprojection will
     * occur from the overridden coordinate system to the system specified here.
     */
    public void setCoordinateSystemReproject(CoordinateReferenceSystem system) {
        coordinateSystemReproject = system;
    }

    /**
     * SortBy results according to indicated property and order.
     *
     * <p>SortBy is part of the Filter 1.1 specification, it is referenced by WFS1.1 and Catalog 2.0.x specifications
     * and is used to organize results. The SortBy's are ment to be applied in order:
     *
     * <ul>
     *   <li>SortBy( year, ascending )
     *   <li>SortBy( month, decsending )
     * </ul>
     *
     * Would produce something like:
     *
     * <pre><code>
     * [year=2002 month=4],[year=2002 month=3],[year=2002 month=2],
     * [year=2002 month=1],[year=2003 month=12],[year=2002 month=4],
     * </code></pre>
     *
     * <p>SortBy should be considered at the same level of abstraction as Filter, and like Filter you may sort using
     * properties not listed in getPropertyNames.
     *
     * <p>At a technical level the interface SortBy2 is used to indicate the additional requirements of a GeoTools
     * implementation. The pure WFS 1.1 specification itself is limited to SortBy.
     *
     * @return SortBy array or order of application
     */
    public SortBy[] getSortBy() {
        return sortBy;
    }

    /** Sets the sort by information. */
    public void setSortBy(SortBy... sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Get hints that have been set to control the query execution.
     *
     * @return hints that are set (may be empty)
     * @see #setHints(Hints) setHints(Hints) for more explanation
     */
    public Hints getHints() {
        if (hints == null) {
            hints = new Hints();
        }
        return hints;
    }

    /**
     * Set hints to control the query execution.
     *
     * <p>Hints can control such things as:
     *
     * <ul>
     *   <li>the GeometryFactory to be used
     *   <li>a generalization distance to be applied
     *   <li>the fetch size to be used in JDBC queries
     * </ul>
     *
     * The set of hints supported can be found by calling {@linkplain FeatureSource#getSupportedHints() }.
     *
     * <p>Note: Data sources may ignore hints (depending on their values) and no mechanism currently exists to discover
     * which hints where actually used during the query's execution.
     *
     * @see Hints#FEATURE_DETACHED
     * @see Hints#JTS_GEOMETRY_FACTORY
     * @see Hints#JTS_COORDINATE_SEQUENCE_FACTORY
     * @see Hints#JTS_PRECISION_MODEL
     * @see Hints#JTS_SRID
     * @see Hints#GEOMETRY_DISTANCE
     * @see Hints#FEATURE_2D
     * @param hints the hints to apply
     */
    public void setHints(Hints hints) {
        this.hints = hints;
    }

    /**
     * Hashcode based on all parameters other than the handle.
     *
     * @return hascode for this Query
     */
    @Override
    public int hashCode() {
        String[] n = getPropertyNames();

        return (n == null ? -1 : n.length == 0 ? 0 : n.length | n[0].hashCode())
                | getMaxFeatures()
                | (getFilter() == null ? 0 : getFilter().hashCode())
                | (getTypeName() == null ? 0 : getTypeName().hashCode())
                | (getVersion() == null ? 0 : getVersion().hashCode())
                | (getCoordinateSystem() == null ? 0 : getCoordinateSystem().hashCode())
                | (getCoordinateSystemReproject() == null
                        ? 0
                        : getCoordinateSystemReproject().hashCode())
                | (getStartIndex() == null ? 0 : getStartIndex());
    }

    /**
     * Equality based on all query parameters other than the handle.
     *
     * @param obj Other object to compare against
     * @return true if this Query matches the other object; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Query)) {
            return false;
        }
        if (this == obj) return true;
        Query other = (Query) obj;

        return Arrays.equals(getPropertyNames(), other.getPropertyNames())
                && retrieveAllProperties() == other.retrieveAllProperties()
                && getMaxFeatures() == other.getMaxFeatures()
                && (getFilter() == null
                        ? other.getFilter() == null
                        : getFilter().equals(other.getFilter()))
                && (getTypeName() == null
                        ? other.getTypeName() == null
                        : getTypeName().equals(other.getTypeName()))
                && (getVersion() == null
                        ? other.getVersion() == null
                        : getVersion().equals(other.getVersion()))
                && (getCoordinateSystem() == null
                        ? other.getCoordinateSystem() == null
                        : getCoordinateSystem().equals(other.getCoordinateSystem()))
                && (getCoordinateSystemReproject() == null
                        ? other.getCoordinateSystemReproject() == null
                        : getCoordinateSystemReproject().equals(other.getCoordinateSystemReproject()))
                && isSortEquals(other)
                && Objects.equals(getStartIndex(), other.getStartIndex())
                && (getHints() == null ? other.getHints() == null : getHints().equals(other.getHints()));
    }

    /** Compares the sortby by their effect (null and empty arrays are considered the same) */
    private boolean isSortEquals(Query other) {
        if (this.sortBy == null || this.sortBy.length == 0) {
            return other.getSortBy() == null || other.getSortBy().length == 0;
        }
        return Arrays.equals(getSortBy(), other.getSortBy());
    }

    /**
     * Return a string representation of this Query.
     *
     * @return a string representation of this Query
     */
    @Override
    public String toString() {
        StringBuffer returnString = new StringBuffer("Query:");

        if (handle != null) {
            returnString.append(" [" + handle + "]");
        }

        returnString.append("\n   feature type: " + typeName);

        if (filter != null) {
            returnString.append("\n   filter: " + filter.toString());
        }

        returnString.append("\n   [properties: ");

        if (properties == null || properties.isEmpty()) {
            returnString.append(" ALL ]");
        } else {
            for (int i = 0; i < properties.size(); i++) {
                returnString.append(properties.get(i));

                if (i < properties.size() - 1) {
                    returnString.append(", ");
                }
            }

            returnString.append("]");
        }
        if (coordinateSystem != null || coordinateSystemReproject != null) {
            returnString.append("\n   [");
            if (coordinateSystem != null) {
                returnString.append(CRS.toSRS(coordinateSystem));
            }
            if (coordinateSystemReproject != null) {
                returnString.append(" --> ");
                returnString.append(CRS.toSRS(coordinateSystemReproject));
            }

            returnString.append("]");
        }

        if (sortBy != null && sortBy.length > 0) {
            returnString.append("\n   [sort by: ");
            for (int i = 0; i < sortBy.length; i++) {
                SortBy sb = sortBy[i];
                if (sb == SortBy.NATURAL_ORDER) {
                    returnString.append("NATURAL");
                } else if (sb == SortBy.REVERSE_ORDER) {
                    returnString.append("REVERSE");
                } else {
                    returnString.append(sb.getPropertyName().getPropertyName());
                    returnString.append(" ");
                    returnString.append(sb.getSortOrder().name());
                }

                if (i < sortBy.length - 1) {
                    returnString.append(", ");
                }
            }

            returnString.append("]");
        }

        return returnString.toString();
    }

    /**
     * The list of joins for this query.
     *
     * <p>Each {@link Join} object specifies a feature type to join to. The join may only reference a feature type from
     * within the same datastore.
     *
     * @see Join
     */
    public List<Join> getJoins() {
        return joins;
    }
}
