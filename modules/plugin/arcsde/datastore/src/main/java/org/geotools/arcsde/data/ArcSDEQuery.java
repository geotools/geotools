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
 *
 */
package org.geotools.arcsde.data;

import static org.opengis.filter.sort.SortBy.NATURAL_ORDER;
import static org.opengis.filter.sort.SortBy.REVERSE_ORDER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jsqlparser.statement.select.PlainSelect;

import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.data.FIDReader.SdeManagedFidReader;
import org.geotools.arcsde.data.FIDReader.UserManagedFidReader;
import org.geotools.arcsde.filter.FilterToSQLSDE;
import org.geotools.arcsde.filter.GeometryEncoderException;
import org.geotools.arcsde.filter.GeometryEncoderSDE;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.SdeRow;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.feature.SchemaException;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor.FIDValidator;
import org.geotools.util.logging.Logging;
import org.hsqldb.Session;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeExtent;
import com.esri.sde.sdk.client.SeFilter;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.client.SeTable;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Wrapper class for SeQuery to hold a SeConnection until close() is called and provide utility
 * methods.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/ArcSDEQuery.java $
 * @version $Id$
 */
@SuppressWarnings("deprecation")
class ArcSDEQuery {
    /** Shared package's logger */
    private static final Logger LOGGER = Logging.getLogger(ArcSDEQuery.class.getName());

    /**
     * The connection to the ArcSDE server obtained when first created the SeQuery in
     * <code>getSeQuery</code>. It is retained until <code>close()</code> is called. Do not use it
     * directly, but through <code>getConnection()</code>.
     * <p>
     * NOTE: this member is package visible only for unit test pourposes
     * </p>
     */
    final ISession session;

    /**
     * The exact feature type this query is about to request from the arcsde server. It could have
     * less attributes than the ones of the actual table schema, in which case only those attributes
     * will be requested.
     */
    private final SimpleFeatureType schema;

    /**
     * The query built using the constraints given by the geotools Query. It must not be accessed
     * directly, but through <code>getSeQuery()</code>, since it is lazyly created
     */
    private SeQuery query;

    /**
     * Holds the geotools Filter that originated this query from which can parse the sql where
     * clause and the set of spatial filters for the ArcSDE Java API
     */
    private ArcSDEQuery.FilterSet filters;

    private final FIDReader fidReader;

    private Object[] previousRowValues;

    private final ArcSdeVersionHandler versioningHandler;

    private final String sortByClause;

    /**
     * Creates a new SDEQuery object.
     * 
     * @param session
     *            the session attached to the life cycle of this query
     * @param schema
     *            the schema with all the attributes as expected.
     * @param filterSet
     * @param versioningHandler
     *            used to transparently set up SeQuery streams pointing to the propper version edit
     *            state when appropriate
     * @throws DataSourceException
     * @see prepareQuery
     */
    private ArcSDEQuery(final ISession session, final SimpleFeatureType schema,
            final FilterSet filterSet, final String sortByClause, final FIDReader fidReader,
            ArcSdeVersionHandler versioningHandler) throws DataSourceException {
        this.session = session;
        this.schema = schema;
        this.filters = filterSet;
        this.fidReader = fidReader;
        this.versioningHandler = versioningHandler;
        this.sortByClause = sortByClause;
    }

    /**
     * Creates a Query to be executed over a registered ArcSDE layer (whether it is from a table or
     * a spatial view).
     * 
     * @param session
     *            the session the query works over. As its managed by the calling code its the
     *            calling code responsibility to close it when done.
     * @param fullSchema
     * @param query
     * @param isMultiversioned
     *            whether the table is versioned, if so, the default version and current state will
     *            be used for the SeQuery
     * @return
     * @throws IOException
     */
    public static ArcSDEQuery createQuery(final ISession session,
            final SimpleFeatureType fullSchema, final Query query, final FIDReader fidReader,
            final ArcSdeVersionHandler versioningHandler) throws IOException {

        Filter filter = query.getFilter();

        LOGGER.fine("Creating new ArcSDEQuery");

        final String typeName = fullSchema.getTypeName();
        final SeTable sdeTable = session.getTable(typeName);
        final SeLayer sdeLayer;
        if (fullSchema.getGeometryDescriptor() == null) {
            sdeLayer = null;
        } else {
            sdeLayer = session.getLayer(typeName);
        }
        // create the set of filters to work over
        final ArcSDEQuery.FilterSet filters = new ArcSDEQuery.FilterSet(sdeTable, sdeLayer, filter,
                fullSchema, null, null, fidReader, session);

        final Filter unsupportedFilter = filters.getUnsupportedFilter();
        final String[] queryProperties = query.getPropertyNames();
        final SimpleFeatureType querySchema = getQuerySchema(queryProperties, unsupportedFilter,
                fullSchema);

        final String sortByClause = buildSortByClause(fullSchema, query.getSortBy(), fidReader);
        final ArcSDEQuery sdeQuery;
        sdeQuery = new ArcSDEQuery(session, querySchema, filters, sortByClause, fidReader,
                versioningHandler);
        return sdeQuery;
    }

    /**
     * Creates a query to be executed over an inprocess view (a view defined by a SQL SELECT
     * statement at the datastore configuration)
     * 
     * @return the newly created ArcSDEQuery.
     * @throws IOException
     *             see <i>throws DataSourceException</i> bellow.
     * @see ArcSDEDataStore#registerView(String, PlainSelect)
     */
    public static ArcSDEQuery createInprocessViewQuery(final ISession session,
            final SimpleFeatureType fullSchema, final Query query,
            final SeQueryInfo definitionQuery, final PlainSelect viewSelectStatement)
            throws IOException {

        final Filter filter = query.getFilter();
        final FIDReader fidReader = FIDReader.NULL_READER;

        // the first table has to be the main layer
        final SeSqlConstruct construct;
        try {
            construct = definitionQuery.getConstruct();
        } catch (SeException e) {
            throw new ArcSdeException("shouldn't happen: " + e.getMessage(), e);
        }
        final String[] tables = construct.getTables();
        String layerName = tables[0];
        // @REVISIT: HACK HERE!, look how to get rid of alias in
        // query info, or
        // better stop using queryinfo as definition query and use
        // the PlainSelect,
        // then construct the query info dynamically when needed?
        if (layerName.indexOf(" AS") > 0) {
            layerName = layerName.substring(0, layerName.indexOf(" AS"));
        }
        final SeTable sdeTable = session.getTable(layerName);
        final SeLayer sdeLayer;
        if (fullSchema.getGeometryDescriptor() == null) {
            sdeLayer = null;
        } else {
            sdeLayer = session.getLayer(layerName);
        }

        // create the set of filters to work over
        final ArcSDEQuery.FilterSet filters = new ArcSDEQuery.FilterSet(sdeTable, sdeLayer, filter,
                fullSchema, definitionQuery, viewSelectStatement, fidReader, session);

        final Filter unsupportedFilter = filters.getUnsupportedFilter();
        final String[] queryProperties = query.getPropertyNames();
        final SimpleFeatureType querySchema = getQuerySchema(queryProperties, unsupportedFilter,
                fullSchema);

        final ArcSDEQuery sdeQuery;
        sdeQuery = new ArcSDEQuery(session, querySchema, filters, null, fidReader,
                ArcSdeVersionHandler.NONVERSIONED_HANDLER);
        return sdeQuery;
    }

    /**
     * Returns a {@link SimpleFeatureType} whichs a "view" of the <code>fullSchema</code> adapted as
     * per the required query property names.
     * 
     * @param queryProperties
     *            the query containing the list of property names required by the output schema and
     *            the {@link Filter query predicate} from which to fetch required properties to be
     *            used at runtime filter evaluation.
     * @param unsupportedFilter
     * @param fullSchema
     *            a feature type representing an ArcSDE layer full schema.
     * @return a FeatureType derived from <code>fullSchema</code> which contains the property names
     *         required by the <code>query</code> and the ones referenced in the query filter.
     * @throws DataSourceException
     */
    public static SimpleFeatureType getQuerySchema(final String[] queryProperties,
            Filter unsupportedFilter, final SimpleFeatureType fullSchema)
            throws DataSourceException {
        // guess which properties need to actually be retrieved.
        final List<String> queryColumns = getQueryColumns(queryProperties, unsupportedFilter,
                fullSchema);
        final String[] attNames = queryColumns.toArray(new String[queryColumns.size()]);

        try {
            // create the resulting feature type for the real attributes to
            // retrieve
            SimpleFeatureType querySchema = DataUtilities.createSubType(fullSchema, attNames);
            return querySchema;
        } catch (SchemaException ex) {
            throw new DataSourceException(
                    "Some requested attributes do not match the table schema: " + ex.getMessage(),
                    ex);
        }
    }

    private static List<String> getQueryColumns(final String[] queryProperties,
            final Filter unsupportedFilter, final SimpleFeatureType fullSchema)
            throws DataSourceException {
        final List<String> columnNames = new ArrayList<String>();

        if ((queryProperties == null) || (queryProperties.length == 0)) {
            final List<AttributeDescriptor> attNames = fullSchema.getAttributeDescriptors();
            for (Iterator<AttributeDescriptor> it = attNames.iterator(); it.hasNext();) {
                AttributeDescriptor att = it.next();
                String attName = att.getLocalName();
                // de namespace-ify the names
                // REVISIT: this shouldnt be needed!
                if (attName.indexOf(":") != -1) {
                    attName = attName.substring(attName.indexOf(":") + 1);
                }
                columnNames.add(attName);
            }
        } else {
            columnNames.addAll(Arrays.asList(queryProperties));

            // Ok, say we don't support the full filter natively and it references
            // some properties, then they have to be retrieved in order to evaluate
            // the filter at runtime
            if (unsupportedFilter != null) {
                final FilterAttributeExtractor attExtractor;
                attExtractor = new FilterAttributeExtractor(fullSchema);
                unsupportedFilter.accept(attExtractor, null);
                final String[] filterAtts = attExtractor.getAttributeNames();
                for (String attName : filterAtts) {
                    if (!columnNames.contains(attName)) {
                        columnNames.add(attName);
                    }
                }
            }
        }

        return columnNames;
    }

    /**
     * Returns the FID strategy used
     * 
     */
    public FIDReader getFidReader() {
        return this.fidReader;
    }

    /**
     * Returns the stream used to fetch rows, creating it if it was not yet created.
     * 
     * @throws SeException
     * @throws IOException
     */
    private SeQuery getSeQuery() throws IOException {
        if (this.query == null) {
            try {
                String[] propsToQuery = fidReader.getPropertiesToFetch(this.schema);
                this.query = createSeQueryForFetch(propsToQuery);
            } catch (SeException e) {
                throw new ArcSdeException(e);
            }
        }
        return this.query;
    }

    /**
     * creates an SeQuery with the filters provided to the constructor and returns it. Queries
     * created with this method can be used to execute and fetch results. They cannot be used for
     * other operations, such as calculating layer extents, or result count.
     * <p>
     * Difference with {@link #createSeQueryForFetch(Session, String[])} is tha this function tells
     * <code>SeQuery.setSpatialConstraints</code> to NOT return geometry based bitmasks, which are
     * needed for calculating the query extent and result count, but not for fetching SeRows.
     * </p>
     * 
     * @param propertyNames
     *            names of attributes to build the query for, respecting order
     * @throws SeException
     *             if the ArcSDE Java API throws it while creating the SeQuery or setting it the
     *             spatial constraints.
     * @throws IOException
     */
    private SeQuery createSeQueryForFetch(String[] propertyNames) throws SeException, IOException {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("constructing new sql query with connection: " + session
                    + ", propnames: " + java.util.Arrays.asList(propertyNames)
                    + " sqlConstruct where clause: '" + this.filters.getSeSqlConstruct().getWhere()
                    + "'");
        }

        final SeQueryInfo qInfo = filters.getQueryInfo(propertyNames);
        if (sortByClause != null) {
            qInfo.setByClause(sortByClause);
        }

        final SeFilter[] spatialConstraints = this.filters.getSpatialFilters();
        if (LOGGER.isLoggable(Level.FINER)) {
            String msg = "ArcSDE query is: " + toString(qInfo);
            LOGGER.finer(msg);
        }

        final SeQuery seQuery;

        seQuery = session.prepareQuery(qInfo, spatialConstraints, versioningHandler);

        return seQuery;
    }

    private static String buildSortByClause(final SimpleFeatureType fullSchema,
            final SortBy[] sortByAttributes, final FIDReader fidReader) {

        if (sortByAttributes == null || sortByAttributes.length == 0) {
            return null;
        }

        StringBuilder byClause = new StringBuilder("ORDER BY ");
        for (int i = 0; i < sortByAttributes.length; i++) {
            SortBy sortAtt = sortByAttributes[i];
            if (sortAtt == NATURAL_ORDER || sortAtt == REVERSE_ORDER) {
                if (fidReader instanceof SdeManagedFidReader
                        || fidReader instanceof UserManagedFidReader) {
                    byClause.append(fidReader.getFidColumn()).append(" ");
                    byClause.append(sortAtt == NATURAL_ORDER ? "ASC" : "DESC");
                } else {
                    throw new IllegalArgumentException(sortAtt
                            + " sorting is not supported for featureclasses"
                            + " with no primary key");
                }
            } else {
                final PropertyName propertyName = sortAtt.getPropertyName();
                final String attName = propertyName.getPropertyName();
                final AttributeDescriptor descriptor = fullSchema.getDescriptor(attName);
                if (descriptor == null) {
                    throw new IllegalArgumentException(attName
                            + " does not exist. Can't sort by it");
                }
                if (descriptor instanceof GeometryDescriptor) {
                    throw new IllegalArgumentException(attName
                            + " is a geometry attribute. Can't sort by it");
                }

                byClause.append(attName).append(" ");
                byClause.append(sortAtt.getSortOrder() == SortOrder.ASCENDING ? "ASC" : "DESC");
            }
            if (i < sortByAttributes.length - 1) {
                byClause.append(", ");
            }
        }
        return byClause.toString();
    }

    private String toString(SeQueryInfo qInfo) {
        StringBuffer sb = new StringBuffer("SeQueryInfo[\n\tcolumns=");
        try {
            SeSqlConstruct sql = qInfo.getConstruct();
            String[] tables = sql.getTables();
            String[] cols = qInfo.getColumns();
            String by = null;
            try {
                by = qInfo.getByClause();
            } catch (NullPointerException npe) {
                // no-op
            }
            String where = sql.getWhere();
            for (int i = 0; cols != null && i < cols.length; i++) {
                sb.append(cols[i]);
                if (i < cols.length - 1)
                    sb.append(", ");
            }
            sb.append("\n\tTables=");
            for (int i = 0; i < tables.length; i++) {
                sb.append(tables[i]);
                if (i < tables.length - 1)
                    sb.append(", ");
            }
            sb.append("\n\tWhere=");
            sb.append(where);
            sb.append("\n\tOrderBy=");
            sb.append(by);
        } catch (SeException e) {
            sb.append("Exception retrieving query info properties: " + e.getMessage());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns the schema of the originating Query
     * 
     * @return the schema of the originating Query
     */
    public SimpleFeatureType getSchema() {
        return this.schema;
    }

    public ArcSDEQuery.FilterSet getFilters() {
        return this.filters;
    }

    /**
     * Convenient method to just calculate the result count of a given query.
     */
    public static int calculateResultCount(final ISession session, final FeatureTypeInfo typeInfo,
            final Query query, final ArcSdeVersionHandler versioningHandler) throws IOException {

        ArcSDEQuery countQuery = null;
        final int count;
        try {
            final SimpleFeatureType fullSchema = typeInfo.getFeatureType();
            if (typeInfo.isInProcessView()) {
                final SeQueryInfo definitionQuery = typeInfo.getSdeDefinitionQuery();
                final PlainSelect viewSelectStatement = typeInfo.getDefinitionQuery();
                countQuery = createInprocessViewQuery(session, fullSchema, query, definitionQuery,
                        viewSelectStatement);
            } else {
                final FIDReader fidStrategy = typeInfo.getFidStrategy();
                countQuery = createQuery(session, fullSchema, query, fidStrategy, versioningHandler);
            }
            count = countQuery.calculateResultCount();
        } finally {
            if (countQuery != null) {
                countQuery.close();
            }
        }
        return count;
    }

    /**
     * Convenient method to just calculate the resulting bound box of a given query.
     */
    public static Envelope calculateQueryExtent(final ISession session,
            final FeatureTypeInfo typeInfo, final Query query,
            final ArcSdeVersionHandler versioningHandler) throws IOException {

        final SimpleFeatureType fullSchema = typeInfo.getFeatureType();
        final GeometryDescriptor geometryDescriptor = fullSchema.getGeometryDescriptor();
        if (geometryDescriptor == null) {
            return null;
        }
        final String defaultGeomAttName = geometryDescriptor.getLocalName();

        // we're calculating the bounds, so we'd better be sure and add the
        // spatial column to the query's propertynames
        final Query realQuery = new Query(query);
        realQuery.setPropertyNames(new String[] { defaultGeomAttName });

        final ArcSDEQuery boundsQuery;

        if (typeInfo.isInProcessView()) {
            final SeQueryInfo definitionQuery = typeInfo.getSdeDefinitionQuery();
            final PlainSelect viewSelectStatement = typeInfo.getDefinitionQuery();
            boundsQuery = createInprocessViewQuery(session, fullSchema, realQuery, definitionQuery,
                    viewSelectStatement);
        } else {
            final FIDReader fidStrategy = typeInfo.getFidStrategy();
            boundsQuery = createQuery(session, fullSchema, realQuery, fidStrategy,
                    versioningHandler);
        }

        Envelope queryExtent = null;
        try {
            Filter unsupportedFilter = boundsQuery.getFilters().getUnsupportedFilter();
            if (unsupportedFilter == Filter.INCLUDE) {
                // we can only use an optimized bounds calculation if the
                // query is fully supported by sde
                queryExtent = boundsQuery.calculateQueryExtent();
            }
        } finally {
            boundsQuery.close();
        }
        return queryExtent;
    }

    /**
     * if the query has been parsed as just a where clause filter, or has no filter at all, the
     * result count calculation is optimized by selecting a <code>count()</code> single row. If the
     * filter involves any kind of spatial filter, such as BBOX, the calculation can't be optimized
     * by this way, because the ArcSDE Java API throws a <code>"DATABASE LEVEL
     * ERROR OCURRED"</code> exception. So, in this case, a query over the shape field is made and
     * the result is traversed counting the number of rows inside a while loop
     * 
     */
    public int calculateResultCount() throws IOException {

        final SimpleFeatureType schema = this.schema;
        final GeometryDescriptor geometryDescriptor = schema.getGeometryDescriptor();

        final String colName;
        if (geometryDescriptor == null) {
            // gemetryless type, use any other column for the query
            colName = schema.getDescriptor(0).getLocalName();
        } else {
            colName = geometryDescriptor.getLocalName();
        }
        final SeQueryInfo qInfo = filters.getQueryInfo(new String[] { colName });

        final SeFilter[] spatialFilters = filters.getSpatialFilters();

        final Command<Integer> countCmd = new Command<Integer>() {
            @Override
            public Integer execute(ISession session, SeConnection connection) throws SeException,
                    IOException {

                SeQuery query = new SeQuery(connection);
                try {
                    versioningHandler.setUpStream(session, query);

                    if (spatialFilters != null && spatialFilters.length > 0) {
                        query.setSpatialConstraints(SeQuery.SE_OPTIMIZE, true, spatialFilters);
                    }

                    SeTable.SeTableStats tableStats = query.calculateTableStatistics("*",
                            SeTable.SeTableStats.SE_COUNT_STATS, qInfo, 0);

                    int actualCount = tableStats.getCount();
                    return new Integer(actualCount);
                } finally {
                    query.close();
                }
            }
        };

        final Integer count = session.issue(countCmd);
        return count.intValue();
    }

    /**
     * Returns the envelope for all features within the layer that pass any SQL construct, state, or
     * spatial constraints for the stream.
     */
    public Envelope calculateQueryExtent() throws IOException {

        LOGGER.fine("Building a new SeQuery to consult it's resulting envelope");

        final SeExtent extent;
        try {
            extent = session.issue(new Command<SeExtent>() {
                @Override
                public SeExtent execute(ISession session, SeConnection connection)
                        throws SeException, IOException {

                    final String[] spatialCol = { schema.getGeometryDescriptor().getLocalName() };
                    // fullConstruct may hold information about multiple tables in case of an
                    // in-process view
                    final SeSqlConstruct fullConstruct = filters.getQueryInfo(spatialCol)
                            .getConstruct();
                    String whereClause = fullConstruct.getWhere();
                    if (whereClause == null) {
                        /*
                         * we really need a where clause or will get a famous -51 DATABASE LEVEL
                         * ERROR with no other explanation on some oracle databases
                         */
                        whereClause = "1 = 1";
                    }
                    final SeFilter[] spatialConstraints = filters.getSpatialFilters();

                    SeExtent extent;

                    final SeQuery extentQuery = new SeQuery(connection);
                    try {
                        versioningHandler.setUpStream(session, extentQuery);

                        if (spatialConstraints.length > 0) {
                            extentQuery.setSpatialConstraints(SeQuery.SE_OPTIMIZE, false,
                                    spatialConstraints);
                        }

                        SeSqlConstruct sqlCons = new SeSqlConstruct();
                        sqlCons.setTables(fullConstruct.getTables());
                        sqlCons.setWhere(whereClause);

                        final SeQueryInfo seQueryInfo = new SeQueryInfo();
                        seQueryInfo.setColumns(spatialCol);
                        seQueryInfo.setConstruct(sqlCons);

                        extent = extentQuery.calculateLayerExtent(seQueryInfo);
                    } finally {
                        extentQuery.close();
                    }

                    return extent;
                }
            });
        } catch (IOException ex) {
            SeSqlConstruct sqlCons = this.filters.getSeSqlConstruct();
            String sql = (sqlCons == null) ? null : sqlCons.getWhere();
            String tables = (sqlCons == null) ? null : Arrays.asList(sqlCons.getTables())
                    .toString();
            if (ex.getCause() instanceof SeException) {
                SeException sdeEx = (SeException) ex.getCause();
                if (sdeEx.getSeError().getSdeError() == -288) {
                    // gah, the dreaded 'LOGFILE SYSTEM TABLES DO NOT EXIST'
                    // error.
                    // this error is worthless. Make it quiet, at least.
                    LOGGER.severe("ArcSDE is complaining that your 'LOGFILE SYSTEM "
                            + "TABLES DO NOT EXIST'.  This is an ignorable error.");
                }
            }
            LOGGER.log(Level.SEVERE, "***********************\ntables: " + tables + "\nfilter: "
                    + this.filters.getGeometryFilter() + "\nSQL: " + sql, ex);
            throw ex;
        }

        Envelope envelope = new Envelope(extent.getMinX(), extent.getMaxX(), extent.getMinY(),
                extent.getMaxY());
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("got extent: " + extent + ", built envelope: " + envelope);
        }
        return envelope;
    }

    /**
     * Silently closes this query.
     * 
     * @param query
     * @throws IOException
     */
    private static void close(final SeQuery query, final ISession session) throws IOException {
        if (query == null) {
            return;
        }
        session.close(query);
    }

    // //////////////////////////////////////////////////////////////////////
    // //////////// RELEVANT METHODS WRAPPED FROM SeStreamOp ////////////////
    // //////////////////////////////////////////////////////////////////////

    /**
     * Closes the query.
     * <p>
     * The {@link Session connection} used by the query is not closed by this operation as it was
     * provided by the calling code and thus it is its responsibility to handle the connection life
     * cycle.
     * </p>
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        close(this.query, session);
        this.query = null;
    }

    /**
     * Tells the server to execute a stream operation.
     * 
     * @throws IOException
     */
    public void execute() throws IOException {
        final SeQuery seQuery = getSeQuery();
        session.issue(new Command<Void>() {
            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                seQuery.execute();
                return null;
            }
        });
    }

    private SdeRow currentRow;

    /**
     * Fetches an SeRow of data.
     * 
     * @throws IOException
     *             (DataSourceException) if the fetching fails
     * @throws IllegalStateException
     *             if the query was already closed or {@link #execute()} hastn't been called yet
     */
    public SdeRow fetch() throws IOException, IllegalStateException {
        if (this.query == null) {
            throw new IllegalStateException("query closed or not yet executed");
        }

        final SeQuery seQuery = getSeQuery();
        // commented out while SeToJTSGeometryFactory is in development
        // if (currentRow == null) {
        // GeometryFactory geomFac = new SeToJTSGeometryFactory();
        // currentRow = new SdeRow(geomFac);
        // int geometryIndex = -1;
        // for (int i = 0; i < schema.getAttributeCount(); i++) {
        // if (schema.getDescriptor(i) instanceof GeometryDescriptor) {
        // geometryIndex = i;
        // break;
        // }
        // }
        // currentRow.setGeometryIndex(geometryIndex);
        // }
        // currentRow = session.fetch(seQuery, currentRow);

        try {
            currentRow = session.fetch(seQuery);
        } catch (IOException e) {
            close();
            String msg = "Error fetching row for " + this.schema.getTypeName() + "[";
            msg += "\nFilter: " + filters.sourceFilter;
            msg += "\n where clause sent: " + filters.sdeSqlConstruct.getWhere();
            msg += "\ngeometry filter:" + filters.geometryFilter;
            LOGGER.log(Level.WARNING, msg, e);
            throw e;
        } catch (Exception e) {
            close();
            LOGGER.log(Level.SEVERE, "fetching row: " + e.getMessage(), e);
            throw new DataSourceException("fetching row: " + e.getMessage(), e);
        }

        if (currentRow != null) {
            currentRow.setPreviousValues(this.previousRowValues);
            previousRowValues = currentRow.getAll();
        }
        return currentRow;
    }

    /**
     * Sets the spatial filters on the query using SE_OPTIMIZE as the policy for spatial index
     * search
     * 
     * @param filters
     *            a set of spatial constraints to filter upon
     * @throws IOException
     */
    public void setSpatialConstraints(SeFilter[] filters) throws IOException {
        try {
            getSeQuery().setSpatialConstraints(SeQuery.SE_OPTIMIZE, false, filters);
        } catch (SeException e) {
            throw new ArcSdeException(e);
        }
    }

    @Override
    public String toString() {
        return "Schema: " + this.schema.getTypeName() + ", query: " + this.query;
    }

    /**
     * Helper class to split out the geotools request filter into the supported non spatial one,
     * supported spatial one and unsupported filters.
     * 
     * @author $author$
     * 
     * 
     * @source $URL$
     *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java
     *         /org/geotools/arcsde/data/ArcSDEQuery.java $
     * @version $Revision: 1.9 $
     */
    public static class FilterSet {

        private SeQueryInfo definitionQuery;

        private PlainSelect layerSelectStatement;

        private FIDReader fidReader;

        private final SeLayer sdeLayer;

        private final SeTable sdeTable;

        private final Filter sourceFilter;

        private Filter _sqlFilter;

        private Filter geometryFilter;

        private Filter unsupportedFilter;

        private FilterToSQLSDE _sqlEncoder;

        /**
         * Holds the ArcSDE Java API definition of the geometry related filters this datastore
         * implementation supports natively.
         */
        private SeFilter[] sdeSpatialFilters;

        /**
         * Holds the ArcSDE Java API definition of the <strong>non</strong> geometry related filters
         * this datastore implementation supports natively.
         */
        private SeSqlConstruct sdeSqlConstruct;

        private SimpleFeatureType featureType;

        private final ISession conn;

        /**
         * Creates a new FilterSet object.
         * 
         * @param sdeLayer
         * @param sourceFilter
         * @param session
         */
        public FilterSet(SeTable table, SeLayer sdeLayer, Filter sourceFilter,
                SimpleFeatureType ft, SeQueryInfo definitionQuery,
                PlainSelect layerSelectStatement, FIDReader fidReader, ISession session) {
            this.conn = session;
            assert table != null;
            // sdeLayer may be null if it is a registered, non spatial table
            // assert sdeLayer != null;
            assert sourceFilter != null;
            assert ft != null;

            this.sdeTable = table;
            this.sdeLayer = sdeLayer;
            this.sourceFilter = sourceFilter;
            this.featureType = ft;
            this.definitionQuery = definitionQuery;
            this.layerSelectStatement = layerSelectStatement;
            this.fidReader = fidReader;
            createGeotoolsFilters();
        }

        /**
         * Given the <code>Filter</code> passed to the constructor, unpacks it to three different
         * filters, one for the supported SQL based filter, another for the supported Geometry based
         * filter, and the last one for the unsupported filter. All of them can be retrieved from
         * its corresponding getter.
         * 
         * @param conn
         */
        private void createGeotoolsFilters() {
            FilterToSQLSDE sqlEncoder = getSqlEncoder();

            PostPreProcessFilterSplittingVisitor unpacker = new PostPreProcessFilterSplittingVisitor(
                    sqlEncoder.getCapabilities(), featureType, null);
            sourceFilter.accept(unpacker, null);

            SimplifyingFilterVisitor filterSimplifier = new SimplifyingFilterVisitor();
            final String typeName = this.featureType.getTypeName();
            FIDValidator validator = new SimplifyingFilterVisitor.TypeNameDotNumberFidValidator(
                    typeName);
            filterSimplifier.setFIDValidator(validator);

            this._sqlFilter = unpacker.getFilterPre();
            this._sqlFilter = (Filter) this._sqlFilter.accept(filterSimplifier, null);

            if (LOGGER.isLoggable(Level.FINE) && _sqlFilter != null)
                LOGGER.fine("SQL portion of SDE Query: '" + _sqlFilter + "'");

            Filter remainingFilter = unpacker.getFilterPost();

            unpacker = new PostPreProcessFilterSplittingVisitor(
                    GeometryEncoderSDE.getCapabilities(), featureType, null);
            remainingFilter.accept(unpacker, null);

            this.geometryFilter = unpacker.getFilterPre();
            this.geometryFilter = (Filter) this.geometryFilter.accept(filterSimplifier, null);
            if (LOGGER.isLoggable(Level.FINE) && geometryFilter != null)
                LOGGER.fine("Spatial-Filter portion of SDE Query: '" + geometryFilter + "'");

            this.unsupportedFilter = unpacker.getFilterPost();
            this.unsupportedFilter = (Filter) this.unsupportedFilter.accept(filterSimplifier, null);
            if (LOGGER.isLoggable(Level.FINE) && unsupportedFilter != null)
                LOGGER.fine("Unsupported (and therefore ignored) portion of SDE Query: '"
                        + unsupportedFilter + "'");
        }

        /**
         * Returns an SeQueryInfo that can be used to retrieve a set of SeRows from an ArcSDE layer
         * or a layer with joins. The SeQueryInfo object lacks the set of column names to fetch. It
         * is the responsibility of the calling code to call setColumns(String []) on the returned
         * object to specify which properties to fetch.
         * 
         * @param unqualifiedPropertyNames
         *            non null, possibly empty, list of property names to fetch
         * @return
         * @throws IOException
         */
        public SeQueryInfo getQueryInfo(final String[] unqualifiedPropertyNames) throws IOException {
            assert unqualifiedPropertyNames != null;
            String[] tables;
            String byClause = null;

            final SeSqlConstruct plainSqlConstruct = getSeSqlConstruct();

            String where = plainSqlConstruct.getWhere();

            try {
                if (definitionQuery == null) {
                    tables = new String[] { this.sdeTable.getQualifiedName() };
                } else {
                    tables = definitionQuery.getConstruct().getTables();
                    String joinWhere = definitionQuery.getConstruct().getWhere();
                    if (where == null) {
                        where = joinWhere;
                    } else {
                        where = joinWhere == null ? where : (joinWhere + " AND " + where);
                    }
                    try {
                        byClause = definitionQuery.getByClause();
                    } catch (NullPointerException e) {
                        // no-op
                    }
                }

                final SeQueryInfo qInfo = new SeQueryInfo();
                final SeSqlConstruct sqlConstruct = new SeSqlConstruct();
                sqlConstruct.setTables(tables);
                if (where != null && where.length() > 0) {
                    sqlConstruct.setWhere(where);
                }

                final int queriedAttCount = unqualifiedPropertyNames.length;

                if (queriedAttCount > 0) {
                    String[] sdeAttNames = new String[queriedAttCount];
                    FilterToSQLSDE sqlEncoder = getSqlEncoder();

                    for (int i = 0; i < queriedAttCount; i++) {
                        String attName = unqualifiedPropertyNames[i];
                        String coldef = sqlEncoder.getColumnDefinition(attName);
                        sdeAttNames[i] = coldef;
                    }
                    qInfo.setColumns(sdeAttNames);
                }

                qInfo.setConstruct(sqlConstruct);
                if (byClause != null) {
                    qInfo.setByClause(byClause);
                }
                return qInfo;
            } catch (SeException e) {
                throw new ArcSdeException(e);
            }
        }

        /**
         * 
         * @return the SeSqlConstruct corresponding to the given SeLayer and SQL based filter.
         *         Should never return null.
         * @throws DataSourceException
         *             if an error occurs encoding the sql filter to a SQL where clause, or creating
         *             the SeSqlConstruct for the given layer and where clause.
         */
        public SeSqlConstruct getSeSqlConstruct() throws DataSourceException {
            if (this.sdeSqlConstruct == null) {
                final String layerName = this.sdeTable.getQualifiedName();
                this.sdeSqlConstruct = new SeSqlConstruct(layerName);

                Filter sqlFilter = getSqlFilter();

                if (!Filter.INCLUDE.equals(sqlFilter)) {
                    String whereClause = null;
                    FilterToSQLSDE sqlEncoder = getSqlEncoder();

                    try {
                        whereClause = sqlEncoder.encodeToString(sqlFilter);
                    } catch (FilterToSQLException sqle) {
                        String message = "Geometry encoder error: " + sqle.getMessage();
                        throw new DataSourceException(message, sqle);
                    }
                    LOGGER.fine("ArcSDE where clause '" + whereClause + "'");

                    this.sdeSqlConstruct.setWhere(whereClause);
                }
            }

            return this.sdeSqlConstruct;
        }

        /**
         * Lazily creates the array of <code>SeShapeFilter</code> objects that map the corresponding
         * geometry related filters included in the original <code>org.geotools.data.Query</code>
         * passed to the constructor.
         * 
         * @return an array with the spatial filters to be applied to the SeQuery, or null if none.
         * @throws DataSourceException
         */
        public SeFilter[] getSpatialFilters() throws DataSourceException {
            if (this.sdeSpatialFilters == null) {
                GeometryEncoderSDE geometryEncoder = new GeometryEncoderSDE(this.sdeLayer,
                        featureType);

                try {
                    geometryEncoder.encode(getGeometryFilter());
                } catch (GeometryEncoderException e) {
                    throw new DataSourceException("Error parsing geometry filters: "
                            + e.getMessage(), e);
                }

                this.sdeSpatialFilters = geometryEncoder.getSpatialFilters();
            }

            return this.sdeSpatialFilters;
        }

        /**
         * 
         * @return the subset, non geometry related, of the original filter this datastore
         *         implementation supports natively, or <code>Filter.INCLUDE</code> if the original
         *         Query does not contains non spatial filters that we can deal with at the ArcSDE
         *         Java API side.
         */
        public Filter getSqlFilter() {
            return (this._sqlFilter == null) ? Filter.INCLUDE : this._sqlFilter;
        }

        /**
         * 
         * @return the geometry related subset of the original filter this datastore implementation
         *         supports natively, or <code>Filter.INCLUDE</code> if the original Query does not
         *         contains spatial filters that we can deal with at the ArcSDE Java API side.
         */
        public Filter getGeometryFilter() {
            return (this.geometryFilter == null) ? Filter.INCLUDE : this.geometryFilter;
        }

        /**
         * 
         * @return the part of the original filter this datastore implementation does not supports
         *         natively, or <code>Filter.INCLUDE</code> if we support the whole Query filter.
         */
        public Filter getUnsupportedFilter() {
            return (this.unsupportedFilter == null) ? Filter.INCLUDE : this.unsupportedFilter;
        }

        private FilterToSQLSDE getSqlEncoder() {
            if (_sqlEncoder == null) {
                final String layerName = sdeTable.getQualifiedName();
                String fidColumn = fidReader.getFidColumn();
                _sqlEncoder = new FilterToSQLSDE(layerName, fidColumn, featureType,
                        layerSelectStatement, this.conn);
            }
            return _sqlEncoder;
        }
    }
}
