/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import java.io.File;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.awt.RenderingHints;

import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.io.TableWriter;
import org.geotools.measure.Units;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.extent.ExtentImpl;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.metadata.iso.quality.AbsoluteExternalPositionalAccuracyImpl;
import org.geotools.metadata.iso.quality.QuantitativeResultImpl;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.factory.AbstractCachedAuthorityFactory;
import org.geotools.referencing.factory.BufferedAuthorityFactory;
import org.geotools.referencing.factory.DirectAuthorityFactory;
import org.geotools.referencing.factory.IdentifiedObjectFinder;
import org.geotools.referencing.operation.DefaultConcatenatedOperation;
import org.geotools.referencing.operation.DefaultOperation;
import org.geotools.referencing.operation.DefaultOperationMethod;
import org.geotools.referencing.operation.DefiningConversion;
import org.geotools.resources.CRSUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.LocalName;
import org.geotools.util.ObjectCache;
import org.geotools.util.ScopedName;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.Version;
import org.geotools.util.logging.Logging;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.quality.EvaluationMethodType;
import org.opengis.metadata.quality.PositionalAccuracy;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeneralDerivedCRS;
import org.opengis.referencing.crs.GeocentricCRS;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.cs.SphericalCS;
import org.opengis.referencing.cs.VerticalCS;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.EngineeringDatum;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.datum.VerticalDatum;
import org.opengis.referencing.datum.VerticalDatumType;
import org.opengis.referencing.operation.ConcatenatedOperation;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.Transformation;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;


/**
 * A coordinate reference system factory backed by the EPSG database tables.
 * <p>
 * The EPSG database is freely available at <A HREF="http://www.epsg.org">http://www.epsg.org</a>.
 * Current version of this class requires EPSG database version 6.6 or above.
 * <p>
 * This factory makes use of a provided {@link ObjectCache}, and may be deployed in stand aline fashion;
 * or as a worker for a {@link MultiEpsgFactory}.
 * <p>
 * This class is abstract - please see the subclasses for dialect specific implementations:
 * <ul>
 *   <li>{@link AccessDialectEpsgFactory}</li>
 *   <li>{@link AnsiDialectEpsgFactory}</li>
 *   <li>{@link OracleDialectEpsgFactory}</li>
 * </ul>
 *
 * These factories accepts names as well as numerical identifiers. For example
 * "<cite>NTF (Paris) / France I</cite>" and {@code "27581"} both fetchs the same object.
 * However, names may be ambiguous since the same name may be used for more than one object.
 * This is the case of "WGS 84" for example. If such an ambiguity is found, an exception
 * will be thrown. If names are not wanted as a legal EPSG code, subclasses can override the
 * {@link #isPrimaryKey} method.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Yann Cézard
 * @author Martin Desruisseaux (IRD)
 * @author Rueben Schulz
 * @author Matthias Basler
 * @author Andrea Aime
 */
public abstract class AbstractEpsgFactory extends AbstractCachedAuthorityFactory {
    /// Datum shift operation methods
    /** First Bursa-Wolf method.   */
    private static final int BURSA_WOLF_MIN_CODE = 9603;

    /** Last Bursa-Wolf method.   */
    private static final int BURSA_WOLF_MAX_CODE = 9607;

    /** Rotation frame method.   */
    private static final int ROTATION_FRAME_CODE = 9607;

    /** Dummy operation to ignore. */
    private static final int DUMMY_OPERATION     =    1;

    /**
     * The name for the transformation accuracy metadata.
     */
    private static final InternationalString TRANSFORMATION_ACCURACY =
            Vocabulary.formatInternational(VocabularyKeys.TRANSFORMATION_ACCURACY);

    /**
     * The calendar instance for creating {@link java.util.Date} objects from a year
     * (the "epoch" in datum definition). We use the local timezone, which may not be
     * quite accurate. But there is no obvious timezone for "epoch", and the "epoch"
     * is approximative anyway.
     */
    private final Calendar calendar = Calendar.getInstance(); // FIXME: move into factories?

    /**
     * The authority for this database. Will be created only when first needed.
     * This authority will contains the database version in the {@linkplain Citation#getEdition
     * edition} attribute, together with the {@linkplain Citation#getEditionDate edition date}.
     */
    private transient Citation authority; //FIXME: always EPSG

    /**
     * A DataSource to the EPSG database being used.
     */
    protected javax.sql.DataSource dataSource;

    /**
     * The connection to the EPSG database - this is create in a lazy manner from the DataSource.
     * <p>
     * This field is managed as part of our connection lifecycle.
     */
    private Connection connection;

    /**
     * A pool of prepared statements. Key are {@link String} object related to their
     * originating method name (for example "Ellipsoid" for {@link #createEllipsoid},
     * while values are {@link PreparedStatement} objects.
     * <p>
     * <strong>Note:</strong> It is okay to use {@link IdentityHashMap} instead of {@link HashMap}
     * because the keys will always be the exact same object, namely the hard-coded argument given
     * to calls to {@link #prepareStatement} in this class.
     * <p>
     * This field is managed as part of our connection lifecycle.
     */
    private final Map<String,PreparedStatement> statements = new IdentityHashMap<String,PreparedStatement>();

    /**
     * Last object type returned by {@link #createObject}, or -1 if none.
     * This type is an index in the {@link #TABLES_INFO} array and is
     * strictly for {@link #createObject} internal use.
     */
    private int lastObjectType = -1;

    /**
     * The last table in which object name were looked for. This is for internal use
     * by {@link #toPrimaryKey} only.
     */
    private transient String lastTableForName;

    /**
     * Cache for axis names. This service is not provided by {@link BufferedAuthorityFactory}
     * since {@link AxisName} object are particular to the EPSG database.
     *
     * @see #getAxisName
     */
    private final Map<String,AxisName> axisNames = new HashMap<String,AxisName>();

    /**
     * Cache for axis numbers. This service is not provided by {@link BufferedAuthorityFactory}
     * since the number of axis is used internally in this class.
     *
     * @see #getDimensionForCRS
     */
    private final Map<String,Short> axisCounts = new HashMap<String,Short>();

    /**
     * Cache for projection checks. This service is not provided by {@link BufferedAuthorityFactory}
     * since the check that a transformation is a projection is used internally in this class.
     *
     * @see #isProjection
     */
    private final Map<String,Boolean> codeProjection = new HashMap<String,Boolean>();

    /**
     * Pool of naming systems, used for caching.
     * There is usually few of them (about 15).
     */
    private final Map<String,LocalName> scopes = new HashMap<String,LocalName>();

    /**
     * The properties to be given the objects to construct.
     * Reused every time {@link #createProperties} is invoked.
     */
    private final Map<String,Object> properties = new HashMap<String,Object>();

    /**
     * A safety guard for preventing never-ending loops in recursive calls to
     * {@link #createDatum}. This is used by {@link #createBursaWolfParameters},
     * which need to create a target datum. The target datum could have its own
     * Bursa-Wolf parameters, with one of them pointing again to the source datum.
     */
    private final Set<String> safetyGuard = new HashSet<String>();

    public AbstractEpsgFactory(final Hints userHints ) throws FactoryException {
        super( MAXIMUM_PRIORITY-20 );
        // The following hints have no effect on this class behaviour,
        // but tell to the user what this factory do about axis order.
        hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE);
        hints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS,   Boolean.FALSE);
        hints.put(Hints.FORCE_STANDARD_AXIS_UNITS,        Boolean.FALSE);

        //
        // We need to obtain our DataSource
        if (userHints != null) {
            Object hint = userHints.get(Hints.EPSG_DATA_SOURCE);
            if( hint instanceof String ){
                String name = (String) hint;
                try {
                    dataSource = (DataSource) GeoTools.getInitialContext(userHints).lookup(name);
                } catch (NamingException e) {
                    throw new FactoryException("A EPSG_DATA_SOURCE hint is required:"+e);
                }
                hints.put(Hints.EPSG_DATA_SOURCE, dataSource );
            }
            else if( hint instanceof DataSource ){
                dataSource = (DataSource) hint;
                hints.put(Hints.EPSG_DATA_SOURCE, dataSource );
            }
            else {
                throw new FactoryException("A EPSG_DATA_SOURCE hint is required.");
            }
        }
        else {
            throw new FactoryException("A EPSG_DATA_SOURCE hint is required.");
        }
    }

    public AbstractEpsgFactory(final Hints userHints, final javax.sql.DataSource dataSource) {
    	super( MAXIMUM_PRIORITY-20 );

    	this.dataSource = dataSource;
        // The following hints have no effect on this class behaviour,
        // but tell to the user what this factory do about axis order.
        hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE);
        hints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS,   Boolean.FALSE);
        hints.put(Hints.FORCE_STANDARD_AXIS_UNITS,        Boolean.FALSE);
        hints.put(Hints.EPSG_DATA_SOURCE, dataSource );
    }
    /**
     * Constructs an authority factory using the specified connection.
     *
     * @param userHints The underlying factories used for objects creation.
     * @param connection The connection to the underlying EPSG database.
     */
    public AbstractEpsgFactory(final Hints userHints, final Connection connection) {
        super(MAXIMUM_PRIORITY-20, userHints );
        // The following hints have no effect on this class behaviour,
        // but tell to the user what this factory do about axis order.
        hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE);
        hints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS,   Boolean.FALSE);
        hints.put(Hints.FORCE_STANDARD_AXIS_UNITS,        Boolean.FALSE);
        this.connection = connection;
        ensureNonNull("connection", connection);
    }

    /**
     * Returns the authority for this EPSG database.
     * This authority will contains the database version in the {@linkplain Citation#getEdition
     * edition} attribute, together with the {@linkplain Citation#getEditionDate edition date}.
     */
    public synchronized Citation getAuthority() {
        if (authority == null) try {
            final String query = adaptSQL("SELECT VERSION_NUMBER, VERSION_DATE FROM [Version History]" +
                                          " ORDER BY VERSION_DATE DESC");
            final DatabaseMetaData metadata  = getConnection().getMetaData();
            final Statement        statement = getConnection().createStatement();
            final ResultSet        result    = statement.executeQuery(query);
            if (result.next()) {
                final String version = result.getString(1);
                final Date   date    = result.getDate  (2);
                final String engine  = metadata.getDatabaseProductName();
                final CitationImpl c = new CitationImpl(Citations.EPSG);
                c.getAlternateTitles().add(Vocabulary.formatInternational(
                        VocabularyKeys.DATA_BASE_$3, "EPSG", version, engine));
                c.setEdition(new SimpleInternationalString(version));
                c.setEditionDate(date);
                authority = (Citation) c.unmodifiable();
                hints.put(Hints.VERSION, new Version(version));  // For getImplementationHints()
            } else {
                authority = Citations.EPSG;
            }
            result.close();
            statement.close();
        } catch (SQLException exception) {
            Logging.unexpectedException(LOGGER, AbstractEpsgFactory.class, "getAuthority", exception);
            return Citations.EPSG;
        }
        return authority;
    }

    /**
     * Returns a description of the database engine.
     *
     * @throws FactoryException if the database's metadata can't be fetched.
     */
    @Override
    public synchronized String getBackingStoreDescription() throws FactoryException {
        final Citation   authority = getAuthority();
        final TableWriter    table = new TableWriter(null, " ");
        final Vocabulary resources = Vocabulary.getResources(null);
        CharSequence cs;
        if ((cs=authority.getEdition()) != null) {
            table.write(resources.getString(VocabularyKeys.VERSION_OF_$1, "EPSG"));
            table.write(':');
            table.nextColumn();
            table.write(cs.toString());
            table.nextLine();
        }
        try {
            String s;
            final DatabaseMetaData metadata = getConnection().getMetaData();
            if ((s=metadata.getDatabaseProductName()) != null) {
                table.write(resources.getLabel(VocabularyKeys.DATABASE_ENGINE));
                table.nextColumn();
                table.write(s);
                if ((s=metadata.getDatabaseProductVersion()) != null) {
                    table.write(' ');
                    table.write(resources.getString(VocabularyKeys.VERSION_$1, s));
                }
                table.nextLine();
            }
            if ((s=metadata.getURL()) != null) {
                table.write(resources.getLabel(VocabularyKeys.DATABASE_URL));
                table.nextColumn();
                table.write(s);
                table.nextLine();
            }
        } catch (SQLException exception) {
            throw new FactoryException(exception);
        }
        return table.toString();
    }

    /**
     * Returns the implementation hints for this factory. The returned map contains all the
     * values specified in {@linkplain DirectAuthorityFactory#getImplementationHints subclass},
     * with the addition of {@link Hints#VERSION VERSION}.
     */
    @Override
    public Map<RenderingHints.Key,?> getImplementationHints() {
        if (authority == null) {
            // For the computation of Hints.VERSION.
            getAuthority();
        }
        return super.getImplementationHints();
    }

    /**
     * Returns the set of authority codes of the given type.
     *
     * @param  type The spatial reference objects type (may be {@code Object.class}).
     * @return The set of authority codes for spatial reference objects of the given type.
     *         If this factory doesn't contains any object of the given type, then this method
     *         returns an empty set.
     * @throws FactoryException if access to the underlying database failed.
     */
    protected synchronized Set<String> generateAuthorityCodes(final Class type) throws FactoryException {
    	Set<String> result = new HashSet<String>();
        for (int i=0; i<TABLES_INFO.length; i++) {
            final TableInfo table = TABLES_INFO[i];
            if (table.isTypeOf(type)) {
                final AuthorityCodeSet codes = new AuthorityCodeSet(TABLES_INFO[i], type);
                result.addAll(codes);
            }
        }
        return result;
    }

    /**
     * Gets a description of the object corresponding to a code.
     *
     * @param  code Value allocated by authority.
     * @return A description of the object, or {@code null} if the object
     *         corresponding to the specified {@code code} has no description.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the query failed for some other reason.
     */
    public InternationalString getDescriptionText(final String code) throws FactoryException {
        IdentifiedObject identifiedObject = createObject(code);
		final Identifier identifier = identifiedObject.getName();
        if (identifier instanceof GenericName) {
            return ((GenericName) identifier).toInternationalString();
        }
        return new SimpleInternationalString( identifier.getCode() );
    }

    /**
     * Returns a prepared statement for the specified name. Most {@link PreparedStatement}
     * creations are performed through this method, except {@link #getNumericalIdentifier}
     * and {@link #createObject}.
     *
     * @param  key A key uniquely identifying the caller
     *         (e.g. {@code "Ellipsoid"} for {@link #createEllipsoid}).
     * @param  sql The SQL statement to use if for creating the {@link PreparedStatement}
     *         object. Will be used only if no prepared statement was already created for
     *         the specified key.
     * @return The prepared statement.
     * @throws SQLException if the prepared statement can't be created.
     */
    private PreparedStatement prepareStatement(final String key, final String sql)
            throws SQLException
    {
        assert Thread.holdsLock(this);
        PreparedStatement stmt = statements.get(key);
        if (stmt == null) {
            stmt = getConnection().prepareStatement(adaptSQL(sql));
            statements.put(key, stmt);
        }
        return stmt;
    }

    /**
     * Gets the string from the specified {@link ResultSet}.
     * The string is required to be non-null. A null string
     * will throw an exception.
     *
     * @param  result The result set to fetch value from.
     * @param  columnIndex The column index (1-based).
     * @param  code The identifier of the record where the string was found.
     * @return The string at the specified column.
     * @throws SQLException if a SQL error occured.
     * @throws FactoryException If a null value was found.
     */
    private static String getString(final ResultSet result, final int columnIndex, final String code)
            throws SQLException, FactoryException
    {
        final String value = result.getString(columnIndex);
        ensureNonNull(result, columnIndex, code);
        return value.trim();
    }

    /**
     * Same as {@link #getString(ResultSet,int,String)}, but report the fault on an alternative
     * column if the value is null.
     */
    private static String getString(final ResultSet result, final int columnIndex,
                                    final String    code,   final int columnFault)
            throws SQLException, FactoryException
    {
        final String str = result.getString(columnIndex);
        if (result.wasNull()) {
            final ResultSetMetaData metadata = result.getMetaData();
            final String column = metadata.getColumnName(columnFault);
            final String table  = metadata.getTableName (columnFault);
            result.close();
            throw new FactoryException(
                    Errors.format(ErrorKeys.NULL_VALUE_IN_TABLE_$3, code, column, table));
        }
        return str.trim();
    }

    /**
     * Gets the value from the specified {@link ResultSet}.
     * The value is required to be non-null. A null value
     * (i.e. blank) will throw an exception.
     *
     * @param  result The result set to fetch value from.
     * @param  columnIndex The column index (1-based).
     * @param  code The identifier of the record where the string was found.
     * @return The double at the specified column.
     * @throws SQLException if a SQL error occured.
     * @throws FactoryException If a null value was found.
     */
    private static double getDouble(final ResultSet result, final int columnIndex, final String code)
            throws SQLException, FactoryException
    {
        final double value = result.getDouble(columnIndex);
        ensureNonNull(result, columnIndex, code);
        return value;
    }

    /**
     * Gets the value from the specified {@link ResultSet}.
     * The value is required to be non-null. A null value
     * (i.e. blank) will throw an exception.
     *
     * @param  result The result set to fetch value from.
     * @param  columnIndex The column index (1-based).
     * @param  code The identifier of the record where the string was found.
     * @return The integer at the specified column.
     * @throws SQLException if a SQL error occured.
     * @throws FactoryException If a null value was found.
     */
    private static int getInt(final ResultSet result, final int columnIndex, final String code)
            throws SQLException, FactoryException
    {
        final int value = result.getInt(columnIndex);
        ensureNonNull(result, columnIndex, code);
        return value;
    }

    /**
     * Make sure that the last result was non-null. Used for {@code getString}, {@code getDouble}
     * and {@code getInt} methods only.
     */
    private static void ensureNonNull(final ResultSet result, final int columnIndex, final String code)
            throws SQLException, FactoryException
    {
        if (result.wasNull()) {
            final ResultSetMetaData metadata = result.getMetaData();
            final String column = metadata.getColumnName(columnIndex);
            final String table  = metadata.getTableName (columnIndex);
            result.close();
            throw new FactoryException(
                    Errors.format(ErrorKeys.NULL_VALUE_IN_TABLE_$3, code, column, table));
        }
    }

    /**
     * Converts a code from an arbitrary name to the numerical identifier (the primary key).
     * If the supplied code is already a numerical value, then it is returned unchanged.
     * If the code is not found in the name column, it is returned unchanged as well so that
     * the caller will produces an appropriate "Code not found" error message. If the code
     * is found more than once, then an exception is thrown.
     * <p>
     * Note that this method includes a call to {@link #trimAuthority}, so there is no need to
     * call it before or after this method.
     *
     * @param  type       The type of object to create.
     * @param  code       The code to check.
     * @param  table      The table where the code should appears.
     * @param  codeColumn The column name for the code.
     * @param  nameColumn The column name for the name.
     * @return The numerical identifier (i.e. the table primary key value).
     * @throws SQLException if an error occured while reading the database.
     */
    private String toPrimaryKey(final Class  type,
                                final String code,
                                final String table,
                                final String codeColumn,
                                final String nameColumn)
            throws SQLException, FactoryException
    {
        assert Thread.holdsLock(this);
        String identifier = trimAuthority(code);
        if (!isPrimaryKey(identifier)) {
            /*
             * The character is not the numerical code. Search the value in the database.
             * If a prepared statement is already available, reuse it providing that it was
             * created for the current table. Otherwise, we will create a new statement.
             */
            final String KEY = "NumericalIdentifier";
            PreparedStatement statement = statements.get(KEY);
            if (statement != null) {
                if (!table.equals(lastTableForName)) {
                    statements.remove(KEY);
                    statement.close();
                    statement        = null;
                    lastTableForName = null;
                }
            }
            if (statement == null) {
                final String query = "SELECT " + codeColumn + " FROM " + table +
                                     " WHERE " + nameColumn + " = ?";
                statement = getConnection().prepareStatement(adaptSQL(query));
                statements.put(KEY, statement);
            }
            statement.setString(1, identifier);
            identifier = null;
            final ResultSet result = statement.executeQuery();
            while (result.next()) {
                identifier = ensureSingleton(result.getString(1), identifier, code);
            }
            result.close();
            if (identifier == null) {
                throw noSuchAuthorityCode(type, code);
            }
        }
        return identifier;
    }

    /**
     * Make sure that an object constructed from the database is not incoherent.
     * If the code supplied to a {@code createFoo} method exists in the database,
     * then we should find only one record. However, we will do a paranoiac check and
     * verify if there is more records, using a {@code while (results.next())}
     * loop instead of {@code if (results.next())}. This method is invoked in
     * the loop for making sure that, if there is more than one record (which should
     * never happen), at least they have identical contents.
     *
     * @param  newValue The newly constructed object.
     * @param  oldValue The object previously constructed, or {@code null} if none.
     * @param  code The EPSG code (for formatting error message).
     * @throws FactoryException if a duplication has been detected.
     *
     * @todo Use generic type when we will be allowed to compile for J2SE 1.5.
     */
    private static <T> T ensureSingleton(final T newValue, final T oldValue, final String code)
            throws FactoryException
    {
        if (oldValue == null) {
            return newValue;
        }
        if (oldValue.equals(newValue)) {
            return oldValue;
        }
        throw new FactoryException(Errors.format(ErrorKeys.DUPLICATED_VALUES_$1, code));
    }

    /**
     * Returns the name for the {@link IdentifiedObject} to construct.
     * This method also search for alias.
     *
     * @param  name The name for the {@link IndentifiedObject} to construct.
     * @param  code The EPSG code of the object to construct.
     * @param  remarks Remarks, or {@code null} if none.
     * @return The name together with a set of properties.
     */
    private Map<String,Object> generateProperties(final String name, final String code, String remarks)
            throws SQLException, FactoryException
    {
        properties.clear();
        final Citation authority = getAuthority();
        if (name != null) {
            properties.put(IdentifiedObject.NAME_KEY,
                           new NamedIdentifier(authority, name.trim()));
        }
        if (code != null) {
            final InternationalString edition = authority.getEdition();
            final String version = (edition!=null) ? edition.toString() : null;
            properties.put(IdentifiedObject.IDENTIFIERS_KEY,
                           new NamedIdentifier(authority, code.trim(), version));
        }
        if (remarks!=null && (remarks=remarks.trim()).length()!=0) {
            properties.put(IdentifiedObject.REMARKS_KEY, remarks);
        }
        /*
         * Search for alias.
         */
        List<GenericName> alias = null;
        final PreparedStatement stmt;
        stmt = prepareStatement("Alias", "SELECT NAMING_SYSTEM_NAME, ALIAS"
                                       + " FROM [Alias] INNER JOIN [Naming System]"
                                       +   " ON [Alias].NAMING_SYSTEM_CODE ="
                                       +      " [Naming System].NAMING_SYSTEM_CODE"
                                       + " WHERE OBJECT_CODE = ?");
        stmt.setString(1, code);
        final ResultSet result = stmt.executeQuery();
        while (result.next()) {
            final String scope = result.getString(1);
            final String local = getString(result, 2, code);
            final GenericName generic;
            if (scope == null) {
                generic = new LocalName(local);
            } else {
                LocalName cached = scopes.get(scope);
                if (cached == null) {
                    cached = new LocalName(scope);
                    scopes.put(scope, cached);
                }
                generic = new ScopedName(cached, local);
            }
            if (alias == null) {
                alias = new ArrayList<GenericName>();
            }
            alias.add(generic);
        }
        result.close();
        if (alias != null) {
            properties.put(IdentifiedObject.ALIAS_KEY,
                           alias.toArray(new GenericName[alias.size()]));
        }
        return properties;
    }

    /**
     * Returns the name for the {@link IdentifiedObject} to construct.
     * This method also search for alias.
     *
     * @param  name  The name for the {@link IndentifiedObject} to construct.
     * @param  code  The EPSG code of the object to construct.
     * @param  area  The area of use, or {@code null} if none.
     * @param  scope The scope, or {@code null} if none.
     * @param  remarks Remarks, or {@code null} if none.
     * @return The name together with a set of properties.
     */
    private Map<String,Object> generateProperties(final String name, final String code,
            String area, String scope, String remarks)
            throws SQLException, FactoryException
    {
        final Map<String,Object> properties = generateProperties(name, code, remarks);
        if (area != null  &&  (area=area.trim()).length() != 0) {
            final Extent extent = generateExtent(area);
            properties.put(Datum.DOMAIN_OF_VALIDITY_KEY, extent);
        }
        if (scope != null &&  (scope=scope.trim()).length() != 0) {
            properties.put(Datum.SCOPE_KEY, scope);
        }
        return properties;
    }

    /**
     * Returns an arbitrary object from a code.
     * The default implementation invokes one of {@link #createCoordinateReferenceSystem},
     * {@link #createCoordinateSystem}, {@link #createDatum}, {@link #createEllipsoid}, or
     * {@link #createUnit} methods according the object type.
     *
     * @param  code The EPSG value.
     * @return The object.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized IdentifiedObject generateObject(final String code) throws FactoryException {
        ensureNonNull("code", code);
        final String       KEY = "IdentifiedObject";
        PreparedStatement stmt = statements.get(KEY); // Null allowed.
        StringBuilder    query = null; // Will be created only if the last statement doesn't suit.
        /*
         * Iterates through all tables listed in TABLES_INFO, starting with the table used during
         * the last call to 'createObject(code)'.  This approach assumes that two consecutive calls
         * will often return the same type of object.  If the object type changed, then this method
         * will have to discard the old prepared statement and prepare a new one, which may be a
         * costly operation. Only the last successful prepared statement is cached, in order to keep
         * the amount of statements low. Unsuccessful statements are immediately disposed.
         */
        final String  epsg         = trimAuthority(code);
        final boolean isPrimaryKey = isPrimaryKey(epsg);
        final int     tupleToSkip  = isPrimaryKey ? lastObjectType : -1;
        int index = -1;
        for (int i=-1; i<TABLES_INFO.length; i++) {
            if (i == tupleToSkip) {
                // Avoid to test the same table twice.  Note that this test also avoid a
                // NullPointerException if 'stmt' is null, since 'lastObjectType' should
                // be -1 in this case.
                continue;
            }
            try {
                if (i >= 0) {
                    final TableInfo table = TABLES_INFO[i];
                    final String column = isPrimaryKey ? table.codeColumn : table.nameColumn;
                    if (column == null) {
                        continue;
                    }
                    if (query == null) {
                        query = new StringBuilder("SELECT ");
                    }
                    query.setLength(7); // 7 is the length of "SELECT " in the line above.
                    query.append(table.codeColumn);
                    query.append(" FROM ");
                    query.append(table.table);
                    query.append(" WHERE ");
                    query.append(column);
                    query.append(" = ?");
                    if (isPrimaryKey) {
                        assert !statements.containsKey(KEY) : table;
                        stmt = prepareStatement(KEY, query.toString());
                    } else {
                        // Do not cache the statement for names.
                        stmt = getConnection().prepareStatement(adaptSQL(query.toString()));
                    }
                }
                /*
                 * Checks if at least one record is found for the code. If the code is the primary
                 * key, then we will stop at the first table found since a well-formed EPSG database
                 * should not contains any duplicate identifiers. In the code is a name, then search
                 * in all tables since duplicate names exist.
                 */
                stmt.setString(1, epsg);
                final ResultSet result = stmt.executeQuery();
                final boolean  present = result.next();
                result.close();
                if (present) {
                    if (index >= 0) {
                        throw new FactoryException(Errors.format(ErrorKeys.DUPLICATED_VALUES_$1, code));
                    }
                    index = (i < 0) ? lastObjectType : i;
                    if (isPrimaryKey) {
                        // Don't scan other tables, since primary keys should be unique.
                        // Note that names may be duplicated, so we don't stop for names.
                        break;
                    }
                }
                if (isPrimaryKey) {
                    if (statements.remove(KEY) == null) {
                        throw new AssertionError(code); // Should never happen.
                    }
                }
                stmt.close();
            } catch (SQLException exception) {
                throw databaseFailure(IdentifiedObject.class, code, exception);
            }
        }
        /*
         * If a record has been found in one table, then delegates to the appropriate method.
         */
        if (isPrimaryKey) {
            lastObjectType = index;
        }
        if (index >= 0) {
            switch (index) {
                case 0:  return createCoordinateReferenceSystem(code);
                case 1:  return createCoordinateSystem         (code);
                case 2:  return createCoordinateSystemAxis     (code);
                case 3:  return createDatum                    (code);
                case 4:  return createEllipsoid                (code);
                case 5:  return createPrimeMeridian            (code);
                case 6:  return createCoordinateOperation      (code);
                case 7:  return generateOperationMethod          (code);
                case 8:  return generateParameterDescriptor      (code);
                case 9:  break; // Can't cast Unit to IdentifiedObject
                default: throw new AssertionError(index); // Should not happen
            }
        }
        return super.createObject(code);
    }

    /**
     * Returns an unit from a code.
     *
     * @param  code Value allocated by authority.
     * @return The unit object.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized Unit<?> generateUnit(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        Unit<?> returnValue = null;
        try {
            final String primaryKey = toPrimaryKey(Unit.class, code,
                    "[Unit of Measure]", "UOM_CODE", "UNIT_OF_MEAS_NAME");
            final PreparedStatement stmt;
            stmt = prepareStatement("Unit", "SELECT UOM_CODE,"
                                          +       " FACTOR_B,"
                                          +       " FACTOR_C,"
                                          +       " TARGET_UOM_CODE"
                                          + " FROM [Unit of Measure]"
                                          + " WHERE UOM_CODE = ?");
            stmt.setString(1, primaryKey);
            final ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final int source = getInt(result,   1, code);
                final double   b = result.getDouble(2);
                final double   c = result.getDouble(3);
                final int target = getInt(result,   4, code);
                final Unit<?> base = getUnit(target);
                if (base == null) {
                    throw noSuchAuthorityCode(Unit.class, String.valueOf(target));
                }
                Unit unit = getUnit(source);
                if (unit != null) {
                    // TODO: check unit consistency here.
                } else if (b!=0 && c!=0) {
                    unit = (b == c) ? base : base.times(b / c);
                } else {
                    // TODO: provide a localized message.
                    throw new FactoryException("Unsupported unit: " + code);
                }
                returnValue = ensureSingleton(unit, returnValue, code);
            }
            result.close();
        }
        catch (SQLException exception) {
            throw databaseFailure(Unit.class, code, exception);
        }
        if (returnValue == null) {
            throw noSuchAuthorityCode(Unit.class, code);
        }
        return returnValue;
    }

    /**
     * Returns an ellipsoid from a code.
     *
     * @param  code The EPSG value.
     * @return The ellipsoid object.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized Ellipsoid generateEllipsoid(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        Ellipsoid returnValue = null;
        try {
            final String primaryKey = toPrimaryKey(Ellipsoid.class, code,
                    "[Ellipsoid]", "ELLIPSOID_CODE", "ELLIPSOID_NAME");
            final PreparedStatement stmt;
            stmt = prepareStatement("Ellipsoid", "SELECT ELLIPSOID_CODE,"
                                               +       " ELLIPSOID_NAME,"
                                               +       " SEMI_MAJOR_AXIS,"
                                               +       " INV_FLATTENING,"
                                               +       " SEMI_MINOR_AXIS,"
                                               +       " UOM_CODE,"
                                               +       " REMARKS"
                                               + " FROM [Ellipsoid]"
                                               + " WHERE ELLIPSOID_CODE = ?");
            stmt.setString(1, primaryKey);
            final ResultSet result = stmt.executeQuery();
            while (result.next()) {
                /*
                 * One of 'semiMinorAxis' and 'inverseFlattening' values can be NULL in
                 * the database. Consequently, we don't use 'getString(ResultSet, int)'
                 * because we don't want to thrown an exception if a NULL value is found.
                 */
                final String epsg              = getString(result, 1, code);
                final String name              = getString(result, 2, code);
                final double semiMajorAxis     = getDouble(result, 3, code);
                final double inverseFlattening = result.getDouble( 4);
                final double semiMinorAxis     = result.getDouble( 5);
                final String unitCode          = getString(result, 6, code);
                final String remarks           = result.getString( 7);
                final Unit   unit              = createUnit(unitCode);
                final Map<String,Object> properties = generateProperties(name, epsg, remarks);
                final Ellipsoid ellipsoid;
                if (inverseFlattening == 0) {
                    if (semiMinorAxis == 0) {
                        // Both are null, which is not allowed.
                        final String column = result.getMetaData().getColumnName(3);
                        result.close();
                        throw new FactoryException(Errors.format(ErrorKeys.NULL_VALUE_IN_TABLE_$3, code, column));
                    } else {
                        // We only have semiMinorAxis defined -> it's OK
                        ellipsoid = factories.getDatumFactory().createEllipsoid(
                                    properties, semiMajorAxis, semiMinorAxis, unit);
                    }
                } else {
                    if (semiMinorAxis != 0) {
                        // Both 'inverseFlattening' and 'semiMinorAxis' are defined.
                        // Log a warning and create the ellipsoid using the inverse flattening.
                        final LogRecord record = Loggings.format(Level.WARNING, LoggingKeys.AMBIGUOUS_ELLIPSOID,code);
                        record.setLoggerName(LOGGER.getName());
                        LOGGER.log(record);
                    }
                    ellipsoid = factories.getDatumFactory().createFlattenedSphere(
                                properties, semiMajorAxis, inverseFlattening, unit);
                }
                /*
                 * Now that we have built an ellipsoid, compare
                 * it with the previous one (if any).
                 */
                returnValue = ensureSingleton(ellipsoid, returnValue, code);
            }
            result.close();
        } catch (SQLException exception) {
            throw databaseFailure(Ellipsoid.class, code, exception);
        }
        if (returnValue == null) {
             throw noSuchAuthorityCode(Ellipsoid.class, code);
        }
        return returnValue;
    }

    /**
     * Returns a prime meridian, relative to Greenwich.
     *
     * @param  code Value allocated by authority.
     * @return The prime meridian object.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized PrimeMeridian generatePrimeMeridian(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        PrimeMeridian returnValue = null;
        try {
            final String primaryKey = toPrimaryKey(PrimeMeridian.class, code,
                    "[Prime Meridian]", "PRIME_MERIDIAN_CODE", "PRIME_MERIDIAN_NAME");
            final PreparedStatement stmt;
            stmt = prepareStatement("PrimeMeridian", "SELECT PRIME_MERIDIAN_CODE,"
                                                   +       " PRIME_MERIDIAN_NAME,"
                                                   +       " GREENWICH_LONGITUDE,"
                                                   +       " UOM_CODE,"
                                                   +       " REMARKS"
                                                   + " FROM [Prime Meridian]"
                                                   + " WHERE PRIME_MERIDIAN_CODE = ?");
            stmt.setString(1, primaryKey);
            final ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final String epsg      = getString(result, 1, code);
                final String name      = getString(result, 2, code);
                final double longitude = getDouble(result, 3, code);
                final String unit_code = getString(result, 4, code);
                final String remarks   = result.getString( 5);
                final Unit unit        = createUnit(unit_code);
                final Map<String,Object> properties = generateProperties(name, epsg, remarks);
                PrimeMeridian primeMeridian = factories.getDatumFactory().createPrimeMeridian(
                                              properties, longitude, unit);
                returnValue = ensureSingleton(primeMeridian, returnValue, code);
            }
            result.close();
        } catch (SQLException exception) {
            throw databaseFailure(PrimeMeridian.class, code, exception);
        }
        if (returnValue == null) {
            throw noSuchAuthorityCode(PrimeMeridian.class, code);
        }
        return returnValue;
    }

    /**
     * Returns an area of use.
     *
     * @param  code Value allocated by authority.
     * @return The area of use.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized Extent generateExtent(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        Extent returnValue = null;
        try {
            final String primaryKey = toPrimaryKey(Extent.class, code,
                    "[Area]", "AREA_CODE", "AREA_NAME");
            final PreparedStatement stmt;
            stmt = prepareStatement("Area", "SELECT AREA_OF_USE,"
                                          +       " AREA_SOUTH_BOUND_LAT,"
                                          +       " AREA_NORTH_BOUND_LAT,"
                                          +       " AREA_WEST_BOUND_LON,"
                                          +       " AREA_EAST_BOUND_LON"
                                          + " FROM [Area]"
                                          + " WHERE AREA_CODE = ?");
            stmt.setString(1, primaryKey);
            final ResultSet result = stmt.executeQuery();
            while (result.next()) {
                ExtentImpl extent = null;
                final String description = result.getString(1);
                if (description != null) {
                    extent = new ExtentImpl();
                    extent.setDescription(new SimpleInternationalString(description));
                }
                final double ymin = result.getDouble(2);
                if (!result.wasNull()) {
                    final double ymax = result.getDouble(3);
                    if (!result.wasNull()) {
                        final double xmin = result.getDouble(4);
                        if (!result.wasNull()) {
                            final double xmax = result.getDouble(5);
                            if (!result.wasNull()) {
                                if (extent == null) {
                                    extent = new ExtentImpl();
                                }
                                extent.setGeographicElements(Collections.singleton(
                                        new GeographicBoundingBoxImpl(xmin, xmax, ymin, ymax)));
                            }
                        }
                    }
                }
                if (extent != null) {
                    returnValue = (Extent) ensureSingleton(extent.unmodifiable(), returnValue, code);
                }
            }
            result.close();
        } catch (SQLException exception) {
            throw databaseFailure(Extent.class, code, exception);
        }
        if (returnValue == null) {
            throw noSuchAuthorityCode(Extent.class, code);
        }
        return returnValue;
    }

    /**
     * Returns Bursa-Wolf parameters for a geodetic datum. If the specified datum has
     * no conversion informations, then this method will returns {@code null}.
     *
     * @param  code The EPSG code of the {@link GeodeticDatum}.
     * @param  toClose The result set to close if this method is going to invokes
     *         {@link #createDatum} recursively. This hack is necessary because many
     *         JDBC drivers do not support multiple result sets for the same statement.
     *         The result set is closed if an only if this method returns a non-null value.
     * @return an array of Bursa-Wolf parameters (in which case {@code toClose} has
     *         been closed), or {@code null} (in which case {@code toClose} has
     *         <strong>not</strong> been closed).
     */
    private BursaWolfParameters[] generateBursaWolfParameters(final String    code,
                                                            final ResultSet toClose)
            throws SQLException, FactoryException
    {
        if (safetyGuard.contains(code)) {
            /*
             * Do not try to create Bursa-Wolf parameters if the datum is already
             * in process of being created. This check avoid never-ending loops in
             * recursive call to 'createDatum'.
             */
            return null;
        }
        PreparedStatement stmt;
        stmt = prepareStatement("BursaWolfParametersSet",
                                         "SELECT CO.COORD_OP_CODE,"
                                 +             " CO.COORD_OP_METHOD_CODE,"
                                 +             " CRS2.DATUM_CODE"
                                 +       " FROM [Coordinate_Operation] AS CO"
                                 + " INNER JOIN [Coordinate Reference System] AS CRS2"
                                 +          " ON CO.TARGET_CRS_CODE = CRS2.COORD_REF_SYS_CODE"
                                 +       " WHERE CO.COORD_OP_METHOD_CODE >= " + BURSA_WOLF_MIN_CODE
                                 +         " AND CO.COORD_OP_METHOD_CODE <= " + BURSA_WOLF_MAX_CODE
                                 +         " AND CO.COORD_OP_CODE <> " + DUMMY_OPERATION // GEOT-1008
                                 +         " AND CO.SOURCE_CRS_CODE IN ("
                                 +      " SELECT CRS1.COORD_REF_SYS_CODE " // GEOT-1129
                                 +        " FROM [Coordinate Reference System] AS CRS1 "
                                 +       " WHERE CRS1.DATUM_CODE = ?)"
                                 +    " ORDER BY CRS2.DATUM_CODE,"
                                 +             " ABS(CO.DEPRECATED), CO.COORD_OP_ACCURACY,"
                                 +             " CO.COORD_OP_CODE DESC"); // GEOT-846 fix
        stmt.setString(1, code);
        ResultSet result = stmt.executeQuery();
        List<Object> bwInfos = null;
        while (result.next()) {
            final String operation = getString(result, 1, code);
            final int    method    = getInt   (result, 2, code);
            final String datum     = getString(result, 3, code);
            if (bwInfos == null) {
                bwInfos = new ArrayList<Object>();
            }
            bwInfos.add(new BursaWolfInfo(operation, method, datum));
        }
        result.close();
        if (bwInfos == null) {
            // Don't close the connection here.
            return null;
        }
        toClose.close();
        /*
         * Sorts the infos in preference order. The "ORDER BY" clause above was not enough;
         * we also need to take the "supersession" table in account. Once the sorting is done,
         * keep only one Bursa-Wolf parameters for each datum.
         */
        int size = bwInfos.size();
        if (size > 1) {
            final BursaWolfInfo[] codes = bwInfos.toArray(new BursaWolfInfo[size]);
            sort(codes);
            bwInfos.clear();
            final Set<String> added = new HashSet<String>();
            for (int i=0; i<codes.length; i++) {
                final BursaWolfInfo candidate = codes[i];
                if (added.add(candidate.target)) {
                    bwInfos.add(candidate);
                }
            }
            size = bwInfos.size();
        }
        /*
         * We got all the needed informations before to built Bursa-Wolf parameters because the
         * 'createDatum(...)' call below may invokes 'createBursaWolfParameters(...)' recursively,
         * and not all JDBC drivers supported multi-result set for the same statement. Now, iterate
         * throw the results and fetch the parameter values for each BursaWolfParameters object.
         */
        stmt = prepareStatement("BursaWolfParameters", "SELECT PARAMETER_CODE,"
                                                     +       " PARAMETER_VALUE,"
                                                     +       " UOM_CODE"
                                                     + " FROM [Coordinate_Operation Parameter Value]"
                                                     + " WHERE COORD_OP_CODE = ?"
                                                     +   " AND COORD_OP_METHOD_CODE = ?");
        for (int i=0; i<size; i++) {
            final BursaWolfInfo info = (BursaWolfInfo) bwInfos.get(i);
            final GeodeticDatum datum;
            try {
                safetyGuard.add(code);
                datum = createGeodeticDatum(info.target);
            } finally {
                safetyGuard.remove(code);
            }
            final BursaWolfParameters parameters = new BursaWolfParameters(datum);
            stmt.setString(1, info.operation);
            stmt.setInt   (2, info.method);
            result = stmt.executeQuery();
            while (result.next()) {
                setBursaWolfParameter(parameters,
                                      getInt   (result, 1, info.operation),
                                      getDouble(result, 2, info.operation),
                  createUnit(getString(result, 3, info.operation)));
            }
            result.close();
            if (info.method == ROTATION_FRAME_CODE) {
                // Coordinate frame rotation (9607): same as 9606,
                // except for the sign of rotation parameters.
                parameters.ex = -parameters.ex;
                parameters.ey = -parameters.ey;
                parameters.ey = -parameters.ey;
            }
            bwInfos.set(i, parameters);
        }
        return bwInfos.toArray(new BursaWolfParameters[size]);
    }

    /**
     * Returns a datum from a code.
     *
     * @param  code Value allocated by authority.
     * @return The datum object.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     *
     * @todo Current implementation maps all "vertical" datum to
     *       {@link VerticalDatumType#GEOIDAL}. We don't know yet how
     *       to maps the exact vertical datum type from the EPSG database.
     */
    public synchronized Datum generateDatum(final String code) throws FactoryException {
        ensureNonNull("code", code);
        Datum returnValue = null;
        try {
            final String primaryKey = toPrimaryKey(Datum.class, code,
                    "[Datum]", "DATUM_CODE", "DATUM_NAME");
            final PreparedStatement stmt;
            stmt = prepareStatement("Datum", "SELECT DATUM_CODE,"
                                           +       " DATUM_NAME,"
                                           +       " DATUM_TYPE,"
                                           +       " ORIGIN_DESCRIPTION,"
                                           +       " REALIZATION_EPOCH,"
                                           +       " AREA_OF_USE_CODE,"
                                           +       " DATUM_SCOPE,"
                                           +       " REMARKS,"
                                           +       " ELLIPSOID_CODE,"     // Only for geodetic type
                                           +       " PRIME_MERIDIAN_CODE" // Only for geodetic type
                                           + " FROM [Datum]"
                                           + " WHERE DATUM_CODE = ?");
            stmt.setString(1, primaryKey);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final String epsg    = getString(result, 1, code);
                final String name    = getString(result, 2, code);
                final String type    = getString(result, 3, code).trim().toLowerCase();
                final String anchor  = result.getString( 4);
                final String epoch   = result.getString( 5);
                final String area    = result.getString( 6);
                final String scope   = result.getString( 7);
                final String remarks = result.getString( 8);
                Map<String,Object> properties = generateProperties(name, epsg, area, scope, remarks);
                if (anchor != null) {
                    properties.put(Datum.ANCHOR_POINT_KEY, anchor);
                }
                if (epoch!=null && epoch.length()!=0) try {
                    calendar.clear();
                    calendar.set(Integer.parseInt(epoch), 0, 1);
                    properties.put(Datum.REALIZATION_EPOCH_KEY, calendar.getTime());
                } catch (NumberFormatException exception) {
                    // Not a fatal error...
                    Logging.unexpectedException(LOGGER, AbstractEpsgFactory.class, "createDatum", exception);
                }
                final DatumFactory factory = factories.getDatumFactory();
                final Datum datum;
                /*
                 * Now build datum according their datum type. Constructions are straightforward,
                 * except for the "geodetic" datum type which need some special processing:
                 *
                 *   - Because it invokes again 'generateProperties' indirectly (through calls to
                 *     'createEllipsoid' and 'createPrimeMeridian'), it must protect 'properties'
                 *     from changes.
                 *
                 *   - Because 'createBursaWolfParameters' may invokes 'createDatum' recursively,
                 *     we must close the result set if Bursa-Wolf parameters are found. In this
                 *     case, we lost our paranoiac check for duplication.
                 */
                if (type.equals("geodetic")) {
                    properties = new HashMap<String,Object>(properties); // Protect from changes
                    final Ellipsoid         ellipsoid = createEllipsoid    (getString(result,  9, code));
                    final PrimeMeridian      meridian = createPrimeMeridian(getString(result, 10, code));
                    final BursaWolfParameters[] param = generateBursaWolfParameters(primaryKey, result);

                    if (param != null) {
                        result = null; // Already closed by createBursaWolfParameters
                        properties.put(DefaultGeodeticDatum.BURSA_WOLF_KEY, param);
                    }
                    datum = factory.createGeodeticDatum(properties, ellipsoid, meridian);
                } else if (type.equals("vertical")) {
                    // TODO: Find the right datum type.
                    datum = factory.createVerticalDatum(properties, VerticalDatumType.GEOIDAL);
                } else if (type.equals("engineering")) {
                    datum = factory.createEngineeringDatum(properties);
                } else {
                    result.close();
                    throw new FactoryException(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, type));
                }
                returnValue = ensureSingleton(datum, returnValue, code);
                if (result == null) {
                    // Bypass the 'result.close()' line below:
                    // the ResultSet has already been closed.
                    return returnValue;
                }
            }
            result.close();
        } catch (SQLException exception) {
            throw databaseFailure(Datum.class, code, exception);
        }
        if (returnValue == null) {
            throw noSuchAuthorityCode(Datum.class, code);
        }
        return returnValue;
    }

    /**
     * Returns the name and description for the specified {@linkplain CoordinateSystemAxis
     * coordinate system axis} code. Many axis share the same name and description, so it
     * is worth to cache them.
     */
    private AxisName getAxisName(final String code) throws FactoryException {
        assert Thread.holdsLock(this);
        AxisName returnValue = axisNames.get(code);
        if (returnValue == null) try {
            final PreparedStatement stmt;
            stmt = prepareStatement("AxisName", "SELECT COORD_AXIS_NAME, DESCRIPTION, REMARKS"
                                    +       " FROM [Coordinate Axis Name]"
                                    +       " WHERE COORD_AXIS_NAME_CODE = ?");
            stmt.setString(1, code);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final String name  = getString(result, 1, code);
                String description = result.getString (2);
                String remarks     = result.getString (3);
                if (description == null) {
                    description = remarks;
                } else if (remarks != null) {
                    description += System.getProperty("line.separator", "\n") + remarks;
                }
                final AxisName axis = new AxisName(name, description);
                returnValue = ensureSingleton(axis, returnValue, code);
            }
            result.close();
            if (returnValue == null) {
                throw noSuchAuthorityCode(AxisName.class, code);
            }
            axisNames.put(code, returnValue);
        } catch (SQLException exception) {
            throw databaseFailure(AxisName.class, code, exception);
        }
        return returnValue;
    }

    /**
     * Returns a {@linkplain CoordinateSystemAxis coordinate system axis} from a code.
     *
     * @param  code Value allocated by authority.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    public synchronized CoordinateSystemAxis generateCoordinateSystemAxis(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        CoordinateSystemAxis returnValue = null;
        try {
            final String primaryKey = trimAuthority(code);
            final PreparedStatement stmt;
            stmt = prepareStatement("Axis", "SELECT COORD_AXIS_CODE,"
                                    +             " COORD_AXIS_NAME_CODE,"
                                    +             " COORD_AXIS_ORIENTATION,"
                                    +             " COORD_AXIS_ABBREVIATION,"
                                    +             " UOM_CODE"
                                    +       " FROM [Coordinate Axis]"
                                    +      " WHERE COORD_AXIS_CODE = ?");
            stmt.setString(1, code);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final String epsg         = getString(result, 1, code);
                final String nameCode     = getString(result, 2, code);
                final String orientation  = getString(result, 3, code);
                final String abbreviation = getString(result, 4, code);
                final String unit         = getString(result, 5, code);
                AxisDirection direction;
                try {
                    direction = DefaultCoordinateSystemAxis.getDirection(orientation);
                } catch (NoSuchElementException exception) {
                    if (orientation.equalsIgnoreCase("Geocentre > equator/PM")) {
                        direction = AxisDirection.OTHER; // TODO: can we choose a more accurate direction?
                    } else if (orientation.equalsIgnoreCase("Geocentre > equator/90dE")) {
                        direction = AxisDirection.EAST;
                    } else if (orientation.equalsIgnoreCase("Geocentre > north pole")) {
                        direction = AxisDirection.NORTH;
                    } else {
                        throw new FactoryException(exception);
                    }
                }
                final AxisName an = getAxisName(nameCode);
                final Map<String,Object> properties = generateProperties(an.name, epsg, an.description);
                final CSFactory factory = factories.getCSFactory();
                final CoordinateSystemAxis axis = factory.createCoordinateSystemAxis(
                        properties, abbreviation, direction, createUnit(unit));
                returnValue = ensureSingleton(axis, returnValue, code);
            }
            result.close();
        } catch (SQLException exception) {
            throw databaseFailure(CoordinateSystemAxis.class, code, exception);
        }
        if (returnValue == null) {
            throw noSuchAuthorityCode(CoordinateSystemAxis.class, code);
        }
        return returnValue;
    }

    /**
     * Returns the coordinate system axis from an EPSG code for a {@link CoordinateSystem}.
     * <p>
     * <strong>WARNING:</strong> The EPSG database uses "{@code ORDER}" as a column name.
     * This is tolerated by Access, but MySQL doesn't accept this name.
     *
     * @param  code the EPSG code for coordinate system owner.
     * @param  dimension of the coordinate system, which is also the size of the returned array.
     * @return An array of coordinate system axis.
     * @throws SQLException if an error occured during database access.
     * @throws FactoryException if the code has not been found.
     */
    private CoordinateSystemAxis[] generateAxisForCoordinateSystem(final String code, final int dimension)
            throws SQLException, FactoryException
    {
        assert Thread.holdsLock(this);
        final CoordinateSystemAxis[] axis = new CoordinateSystemAxis[dimension];
        final PreparedStatement stmt;
        stmt = prepareStatement("AxisOrder", "SELECT COORD_AXIS_CODE"
                                +             " FROM [Coordinate Axis]"
                                +            " WHERE COORD_SYS_CODE = ?"
                                +         " ORDER BY [ORDER]");
                                // WARNING: Be careful about the column name :
                                //          MySQL rejects ORDER as a column name !!!
        stmt.setString(1, code);
        final ResultSet result = stmt.executeQuery();
        int i = 0;
        while (result.next()) {
            final String axisCode = getString(result, 1, code);
            if (i < axis.length) {
                // If 'i' is out of bounds, an exception will be thrown after the loop.
                // We don't want to thrown an ArrayIndexOutOfBoundsException here.
                axis[i] = createCoordinateSystemAxis(axisCode);
            }
            ++i;
        }
        result.close();
        if (i != axis.length) {
            throw new FactoryException(Errors.format(
                    ErrorKeys.MISMATCHED_DIMENSION_$2, axis.length, i));
        }
        return axis;
    }

    /**
     * Returns a coordinate system from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate system object.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized CoordinateSystem generateCoordinateSystem(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        CoordinateSystem returnValue = null;
        final PreparedStatement stmt;
        try {
            final String primaryKey = toPrimaryKey(CoordinateSystem.class, code,
                    "[Coordinate System]", "COORD_SYS_CODE", "COORD_SYS_NAME");
            stmt = prepareStatement("CoordinateSystem", "SELECT COORD_SYS_CODE,"
                                                      +       " COORD_SYS_NAME,"
                                                      +       " COORD_SYS_TYPE,"
                                                      +       " DIMENSION,"
                                                      +       " REMARKS"
                                                      + " FROM [Coordinate System]"
                                                      + " WHERE COORD_SYS_CODE = ?");
            stmt.setString(1, primaryKey);
            final ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final String    epsg = getString(result, 1, code);
                final String    name = getString(result, 2, code);
                final String    type = getString(result, 3, code).trim().toLowerCase();
                final int  dimension = getInt   (result, 4, code);
                final String remarks = result.getString( 5);
                final CoordinateSystemAxis[] axis = generateAxisForCoordinateSystem(primaryKey, dimension);
                final Map<String,Object> properties = generateProperties(name, epsg, remarks); // Must be after axis
                final CSFactory factory = factories.getCSFactory();
                CoordinateSystem cs = null;
                if (type.equals("ellipsoidal")) {
                    switch (dimension) {
                        case 2: cs=factory.createEllipsoidalCS(properties, axis[0], axis[1]); break;
                        case 3: cs=factory.createEllipsoidalCS(properties, axis[0], axis[1], axis[2]); break;
                    }
                } else if (type.equals("cartesian")) {
                    switch (dimension) {
                        case 2: cs=factory.createCartesianCS(properties, axis[0], axis[1]); break;
                        case 3: cs=factory.createCartesianCS(properties, axis[0], axis[1], axis[2]); break;
                    }
                } else if (type.equals("spherical")) {
                    switch (dimension) {
                        case 3: cs=factory.createSphericalCS(properties, axis[0], axis[1], axis[2]); break;
                    }
                } else if (type.equals("vertical") || type.equals("gravity-related")) {
                    switch (dimension) {
                        case 1: cs=factory.createVerticalCS(properties, axis[0]); break;
                    }
                } else if (type.equals("linear")) {
                    switch (dimension) {
                        case 1: cs=factory.createLinearCS(properties, axis[0]); break;
                    }
                } else if (type.equals("polar")) {
                    switch (dimension) {
                        case 2: cs=factory.createPolarCS(properties, axis[0], axis[1]); break;
                    }
                } else if (type.equals("cylindrical")) {
                    switch (dimension) {
                        case 3: cs=factory.createCylindricalCS(properties, axis[0], axis[1], axis[2]); break;
                    }
                } else if (type.equals("affine")) {
                    switch (dimension) {
                        case 2: cs=factory.createAffineCS(properties, axis[0], axis[1]); break;
                        case 3: cs=factory.createAffineCS(properties, axis[0], axis[1], axis[2]); break;
                    }
                } else {
                    result.close();
                    throw new FactoryException(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, type));
                }
                if (cs == null) {
                    result.close();
                    throw new FactoryException(Errors.format(
                                               ErrorKeys.UNEXPECTED_DIMENSION_FOR_CS_$1, type));
                }
                returnValue = ensureSingleton(cs, returnValue, code);
            }
            result.close();
        } catch (SQLException exception) {
            throw databaseFailure(CoordinateSystem.class, code, exception);
        }
        if (returnValue == null) {
            throw noSuchAuthorityCode(CoordinateSystem.class, code);
        }
        return returnValue;

    }

    /**
     * Returns the primary key for a coordinate reference system name.
     * This method is used both by {@link #createCoordinateReferenceSystem}
     * and {@link #createFromCoordinateReferenceSystemCodes}
     */
    private String toPrimaryKeyCRS(final String code) throws SQLException, FactoryException {
        return toPrimaryKey(CoordinateReferenceSystem.class, code,
                "[Coordinate Reference System]", "COORD_REF_SYS_CODE", "COORD_REF_SYS_NAME");
    }

    /**
     * Returns a coordinate reference system from a code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate reference system object.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized CoordinateReferenceSystem generateCoordinateReferenceSystem(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        CoordinateReferenceSystem returnValue = null;
        try {
            final String primaryKey = toPrimaryKeyCRS(code);
            final PreparedStatement stmt;
            stmt = prepareStatement("CoordinateReferenceSystem",
                                            "SELECT COORD_REF_SYS_CODE,"
                                          +       " COORD_REF_SYS_NAME,"
                                          +       " AREA_OF_USE_CODE,"
                                          +       " CRS_SCOPE,"
                                          +       " REMARKS,"
                                          +       " COORD_REF_SYS_KIND,"
                                          +       " COORD_SYS_CODE,"       // Null for CompoundCRS
                                          +       " DATUM_CODE,"           // Null for ProjectedCRS
                                          +       " SOURCE_GEOGCRS_CODE,"  // For ProjectedCRS
                                          +       " PROJECTION_CONV_CODE," // For ProjectedCRS
                                          +       " CMPD_HORIZCRS_CODE,"   // For CompoundCRS only
                                          +       " CMPD_VERTCRS_CODE"     // For CompoundCRS only
                                          + " FROM [Coordinate Reference System]"
                                          + " WHERE COORD_REF_SYS_CODE = ?");
            stmt.setString(1, primaryKey);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final String epsg    = getString(result, 1, code);
                final String name    = getString(result, 2, code);
                final String area    = result.getString( 3);
                final String scope   = result.getString( 4);
                final String remarks = result.getString( 5);
                final String type    = getString(result, 6, code);
                // Note: Do not invoke 'generateProperties' now, even if we have all required
                //       informations, because the 'properties' map is going to overwritten
                //       by calls to 'createDatum', 'createCoordinateSystem', etc.
                final CRSFactory factory = factories.getCRSFactory();
                final CoordinateReferenceSystem crs;
                /* ----------------------------------------------------------------------
                 *   GEOGRAPHIC CRS
                 *
                 *   NOTE: 'generateProperties' MUST be invoked after any call to an other
                 *         'createFoo' method. Consequently, do not factor out.
                 * ---------------------------------------------------------------------- */
                if (type.equalsIgnoreCase("geographic 2D") ||
                    type.equalsIgnoreCase("geographic 3D"))
                {
                    final String csCode    = getString(result, 7, code);
                    final String dmCode    = result.getString( 8);
                    final EllipsoidalCS cs = createEllipsoidalCS(csCode);
                    final GeodeticDatum datum;
                    if (dmCode != null) {
                        datum = createGeodeticDatum(dmCode);
                    } else {
                        final String geoCode = getString(result, 9, code, 8);
                        result.close(); // Must be close before createGeographicCRS
                        result = null;
                        final GeographicCRS baseCRS = createGeographicCRS(geoCode);
                        datum = baseCRS.getDatum();
                    }
                    final Map<String,Object> properties = generateProperties(name, epsg, area, scope, remarks);
                    crs = factory.createGeographicCRS(properties, datum, cs);
                }
                /* ----------------------------------------------------------------------
                 *   PROJECTED CRS
                 *
                 *   NOTE: This method invokes itself indirectly, through createGeographicCRS.
                 *         Consequently, we can't use 'result' anymore. We must close it here.
                 * ---------------------------------------------------------------------- */
                else if (type.equalsIgnoreCase("projected")) {
                    final String csCode  = getString(result,  7, code);
                    final String geoCode = getString(result,  9, code);
                    final String opCode  = getString(result, 10, code);
                    result.close(); // Must be close before createGeographicCRS
                    result = null;
                    final CartesianCS         cs = createCartesianCS(csCode);
                    final GeographicCRS  baseCRS = createGeographicCRS(geoCode);
                    final CoordinateOperation op = createCoordinateOperation(opCode);
                    if (op instanceof Conversion) {
                        final Map<String,Object> properties = generateProperties(name, epsg, area, scope, remarks);
                        crs = factory.createProjectedCRS(properties, baseCRS, (Conversion)op, cs);
                    } else {
                         throw noSuchAuthorityCode(Projection.class, opCode);
                    }
                }
                /* ----------------------------------------------------------------------
                 *   VERTICAL CRS
                 * ---------------------------------------------------------------------- */
                else if (type.equalsIgnoreCase("vertical")) {
                    final String        csCode = getString(result, 7, code);
                    final String        dmCode = getString(result, 8, code);
                    final VerticalCS    cs     = createVerticalCS   (csCode);
                    final VerticalDatum datum  = createVerticalDatum(dmCode);
                    final Map<String,Object> properties = generateProperties(name, epsg, area, scope, remarks);
                    crs = factory.createVerticalCRS(properties, datum, cs);
                }
                /* ----------------------------------------------------------------------
                 *   COMPOUND CRS
                 *
                 *   NOTE: This method invokes itself recursively.
                 *         Consequently, we can't use 'result' anymore.
                 * ---------------------------------------------------------------------- */
                else if (type.equalsIgnoreCase("compound")) {
                    final String code1 = getString(result, 11, code);
                    final String code2 = getString(result, 12, code);
                    result.close();
                    result = null;
                    final CoordinateReferenceSystem crs1, crs2;
                    if (!safetyGuard.add(epsg)) {
                        throw recursiveCall(CompoundCRS.class, epsg);
                    } try {
                        crs1 = createCoordinateReferenceSystem(code1);
                        crs2 = createCoordinateReferenceSystem(code2);
                    } finally {
                        safetyGuard.remove(epsg);
                    }
                    // Note: Don't invoke 'generateProperties' sooner.
                    final Map<String,Object> properties = generateProperties(name, epsg, area, scope, remarks);
                    crs  = factory.createCompoundCRS(properties,
                           new CoordinateReferenceSystem[] {crs1, crs2});
                }
                /* ----------------------------------------------------------------------
                 *   GEOCENTRIC CRS
                 * ---------------------------------------------------------------------- */
                else if (type.equalsIgnoreCase("geocentric")) {
                    final String           csCode = getString(result, 7, code);
                    final String           dmCode = getString(result, 8, code);
                    final CoordinateSystem cs     = createCoordinateSystem(csCode);
                    final GeodeticDatum    datum  = createGeodeticDatum   (dmCode);
                    final Map<String,Object> properties = generateProperties(name, epsg, area, scope, remarks);
                    if (cs instanceof CartesianCS) {
                        crs = factory.createGeocentricCRS(properties, datum, (CartesianCS) cs);
                    } else if (cs instanceof SphericalCS) {
                        crs = factory.createGeocentricCRS(properties, datum, (SphericalCS) cs);
                    } else {
                        result.close();
                        throw new FactoryException(Errors.format(
                                ErrorKeys.ILLEGAL_COORDINATE_SYSTEM_FOR_CRS_$2,
                                cs.getClass(), GeocentricCRS.class));
                    }
                }
                /* ----------------------------------------------------------------------
                 *   ENGINEERING CRS
                 * ---------------------------------------------------------------------- */
                else if (type.equalsIgnoreCase("engineering")) {
                    final String           csCode = getString(result, 7, code);
                    final String           dmCode = getString(result, 8, code);
                    final CoordinateSystem cs     = createCoordinateSystem(csCode);
                    final EngineeringDatum datum  = createEngineeringDatum(dmCode);
                    final Map<String,Object> properties = generateProperties(name, epsg, area, scope, remarks);
                    crs = factory.createEngineeringCRS(properties, datum, cs);
                }
                /* ----------------------------------------------------------------------
                 *   UNKNOW CRS
                 * ---------------------------------------------------------------------- */
                else {
                    result.close();
                    throw new FactoryException(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, type));
                }
                returnValue = ensureSingleton(crs, returnValue, code);
                if (result == null) {
                    // Bypass the 'result.close()' line below:
                    // the ResultSet has already been closed.
                    return returnValue;
                }
            }
            result.close();
        } catch (SQLException exception) {
            throw databaseFailure(CoordinateReferenceSystem.class, code, exception);
        }
        if (returnValue == null) {
             throw noSuchAuthorityCode(CoordinateReferenceSystem.class, code);
        }
        return returnValue;
    }

    /**
     * Returns a parameter descriptor from a code.
     *
     * @param  code The parameter descriptor code allocated by EPSG authority.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized ParameterDescriptor generateParameterDescriptor(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        ParameterDescriptor returnValue = null;
        final PreparedStatement stmt;
        try {
            final String primaryKey = toPrimaryKey(ParameterDescriptor.class, code,
                    "[Coordinate_Operation Parameter]", "PARAMETER_CODE", "PARAMETER_NAME");
            stmt = prepareStatement("ParameterDescriptor", // Must be singular form.
                                        "SELECT PARAMETER_CODE,"
                                      +       " PARAMETER_NAME,"
                                      +       " DESCRIPTION"
                                      + " FROM [Coordinate_Operation Parameter]"
                                      + " WHERE PARAMETER_CODE = ?");
            stmt.setString(1, primaryKey);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final String epsg    = getString(result, 1, code);
                final String name    = getString(result, 2, code);
                final String remarks = result.getString( 3);
                final Unit   unit;
                final Class  type;
                /*
                 * Search for units. We will choose the most commonly used one in parameter values.
                 * If the parameter appears to have at least one non-null value in the "Parameter
                 * File Name" column, then the type is assumed to be URI. Otherwise, the type is a
                 * floating point number.
                 */
                final PreparedStatement units = prepareStatement("ParameterUnit",
                                                  "SELECT MIN(UOM_CODE) AS UOM,"
                                                 +      " MIN(PARAM_VALUE_FILE_REF) AS FILEREF"
                                                 +    " FROM [Coordinate_Operation Parameter Value]"
                                                 +   " WHERE (PARAMETER_CODE = ?)"
                                                 + " GROUP BY UOM_CODE"
                                                 + " ORDER BY COUNT(UOM_CODE) DESC");
                units.setString(1, epsg);
                final ResultSet resultUnits = units.executeQuery();
                if (resultUnits.next()) {
                    String element = resultUnits.getString(1);
                    unit = (element!=null) ? createUnit(element) : null;
                    element = resultUnits.getString(2);
                    type = (element!=null && element.trim().length()!=0) ? URI.class : double.class;
                } else {
                    unit = null;
                    type = double.class;
                }
                resultUnits.close();
                /*
                 * Now creates the parameter descriptor.
                 */
                final ParameterDescriptor descriptor;
                final Map<String,Object> properties = generateProperties(name, epsg, remarks);
                descriptor = new DefaultParameterDescriptor(properties, type,
                                    null, null, null, null, unit, true);
                returnValue = ensureSingleton(descriptor, returnValue, code);
            }
        } catch (SQLException exception) {
            throw databaseFailure(OperationMethod.class, code, exception);
        }
        if (returnValue == null) {
             throw noSuchAuthorityCode(OperationMethod.class, code);
        }
        return returnValue;
    }

    /**
     * Returns all parameter descriptors for the specified method.
     *
     * @param  method The operation method code.
     * @return The parameter descriptors.
     * @throws SQLException if a SQL statement failed.
     */
    private ParameterDescriptor[] generateParameterDescriptors(final String method)
            throws FactoryException, SQLException
    {
        final PreparedStatement stmt;
        stmt = prepareStatement("ParameterDescriptors", // Must be plural form.
                                        "SELECT PARAMETER_CODE"
                                   +    " FROM [Coordinate_Operation Parameter Usage]"
                                   +    " WHERE COORD_OP_METHOD_CODE = ?"
                                   + " ORDER BY SORT_ORDER");
        stmt.setString(1, method);
        final ResultSet results = stmt.executeQuery();
        final List<ParameterDescriptor> descriptors = new ArrayList<ParameterDescriptor>();
        while (results.next()) {
            final String param = getString(results, 1, method);
            descriptors.add(generateParameterDescriptor(param));
        }
        results.close();
        return descriptors.toArray(new ParameterDescriptor[descriptors.size()]);
    }

    /**
     * Fill parameter values in the specified group.
     *
     * @param  method    The EPSG code for the operation method.
     * @param  operation The EPSG code for the operation (conversion or transformation).
     * @param  value     The parameter values to fill.
     * @throws SQLException if a SQL statement failed.
     */
    private void fillParameterValues(final String method,
                                     final String operation,
                                     final ParameterValueGroup parameters)
            throws FactoryException, SQLException
    {
        final PreparedStatement stmt;
        stmt = prepareStatement("ParameterValues",
                                        "SELECT CP.PARAMETER_NAME,"
                                +             " CV.PARAMETER_VALUE,"
                                +             " CV.PARAM_VALUE_FILE_REF,"
                                +             " CV.UOM_CODE"
                                +      " FROM ([Coordinate_Operation Parameter Value] AS CV"
                                + " INNER JOIN [Coordinate_Operation Parameter] AS CP"
                                +          " ON CV.PARAMETER_CODE = CP.PARAMETER_CODE)"
                                + " INNER JOIN [Coordinate_Operation Parameter Usage] AS CU"
                                +         " ON (CP.PARAMETER_CODE = CU.PARAMETER_CODE)"
                                +        " AND (CV.COORD_OP_METHOD_CODE = CU.COORD_OP_METHOD_CODE)"
                                +       " WHERE CV.COORD_OP_METHOD_CODE = ?"
                                +         " AND CV.COORD_OP_CODE = ?"
                                +    " ORDER BY CU.SORT_ORDER");
        stmt.setString(1, method);
        stmt.setString(2, operation);
        final ResultSet result = stmt.executeQuery();
        while (result.next()) {
            final String name  = getString(result, 1, operation);
            final double value = result.getDouble( 2);
            final Unit   unit;
            Object reference;
            if (result.wasNull()) {
                /*
                 * If no numeric values were provided in the database, then the values must
                 * appears in some external file. It may be a file to download from FTP.
                 */
                reference = getString(result, 3, operation);
                try {
                    reference = new URI((String) reference);
                } catch (URISyntaxException exception) {
                    // Ignore: we will stores the reference as a file.
                    reference = new File((String) reference);
                }
                unit = null;
            } else {
                reference = null;
                final String unitCode = result.getString(4);
                unit = (unitCode!=null) ? createUnit(unitCode) : null;
            }
            final ParameterValue param;
            try {
                param = parameters.parameter(name);
            } catch (ParameterNotFoundException exception) {
                /*
                 * Wraps the unchecked ParameterNotFoundException into the checked
                 * NoSuchIdentifierException, which is a FactoryException subclass.
                 * Note that in theory, NoSuchIdentifierException is for MathTransforms rather
                 * than parameters.  However, we are close in spirit here since we are setting
                 * up MathTransform's parameters. Using NoSuchIdentifierException allows users
                 * (including CoordinateOperationSet) to know that the failure is probably
                 * caused by a MathTransform not yet supported in Geotools (or only partially
                 * supported) rather than some more serious failure in the database side.
                 * CoordinateOperationSet uses this information in order to determine if it
                 * should try the next coordinate operation or propagate the exception.
                 */
                final NoSuchIdentifierException e = new NoSuchIdentifierException(
                        Errors.format(ErrorKeys.CANT_SET_PARAMETER_VALUE_$1, name), name);
                e.initCause(exception);
                throw e;
            }
            try {
                if (reference != null) {
                    param.setValue(reference);
                } else if (unit != null) {
                    param.setValue(value, unit);
                } else {
                    param.setValue(value);
                }
            } catch (InvalidParameterValueException exception) {
                throw new FactoryException(
                          Errors.format(ErrorKeys.CANT_SET_PARAMETER_VALUE_$1, name), exception);
            }
        }
        result.close();
    }

    /**
     * Returns an operation method from a code.
     *
     * @param  code The operation method code allocated by EPSG authority.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized OperationMethod generateOperationMethod(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        OperationMethod returnValue = null;
        final PreparedStatement stmt;
        try {
            final String primaryKey = toPrimaryKey(OperationMethod.class, code,
                    "[Coordinate_Operation Method]", "COORD_OP_METHOD_CODE", "COORD_OP_METHOD_NAME");
            stmt = prepareStatement("OperationMethod", "SELECT COORD_OP_METHOD_CODE,"
                                                     +       " COORD_OP_METHOD_NAME,"
                                                     +       " FORMULA,"
                                                     +       " REMARKS"
                                                     +  " FROM [Coordinate_Operation Method]"
                                                     + " WHERE COORD_OP_METHOD_CODE = ?");
            stmt.setString(1, primaryKey);
            final ResultSet result = stmt.executeQuery();
            OperationMethod method = null;
            while (result.next()) {
                final String epsg    = getString(result, 1, code);
                final String name    = getString(result, 2, code);
                final String formula = result.getString( 3);
                final String remarks = result.getString( 4);
                final int encoded = getDimensionsForMethod(epsg);
                final int sourceDimensions = encoded >>> 16;
                final int targetDimensions = encoded & 0xFFFF;
                final ParameterDescriptor[] descriptors = generateParameterDescriptors(epsg);
                final Map<String,Object> properties = generateProperties(name, epsg, remarks);
                if (formula != null) {
                    properties.put(OperationMethod.FORMULA_KEY, formula);
                }
                method = new DefaultOperationMethod(properties, sourceDimensions, targetDimensions,
                         new DefaultParameterDescriptorGroup(properties, descriptors));
                returnValue = ensureSingleton(method, returnValue, code);
            }
        } catch (SQLException exception) {
            throw databaseFailure(OperationMethod.class, code, exception);
        }
        if (returnValue == null) {
             throw noSuchAuthorityCode(OperationMethod.class, code);
        }
        return returnValue;
    }

    /**
     * Returns the must common source and target dimensions for the specified method.
     * Source dimension is encoded in the 16 highest bits and target dimension is encoded
     * in the 16 lowest bits. If this method can't infers the dimensions from the "Coordinate
     * Operation" table, then the operation method is probably a projection, which always have
     * (2,2) dimensions in the EPSG database.
     */
    private int getDimensionsForMethod(final String code) throws SQLException {
        final PreparedStatement stmt;
        stmt = prepareStatement("MethodDimensions", "SELECT SOURCE_CRS_CODE,"
                                                +         " TARGET_CRS_CODE"
                                                +  " FROM [Coordinate_Operation]"
                                                + " WHERE COORD_OP_METHOD_CODE = ?"
                                                +   " AND SOURCE_CRS_CODE IS NOT NULL"
                                                +   " AND TARGET_CRS_CODE IS NOT NULL");
        stmt.setString(1, code);
        final ResultSet result = stmt.executeQuery();
        final Map<Dimensions,Dimensions> dimensions = new HashMap<Dimensions,Dimensions>();
        final Dimensions  temp = new Dimensions((2 << 16) | 2); // Default to (2,2) dimensions.
        Dimensions max = temp;
        while (result.next()) {
            final short sourceDimensions = getDimensionForCRS(result.getString(1));
            final short targetDimensions = getDimensionForCRS(result.getString(2));
            temp.encoded = (sourceDimensions << 16) | (targetDimensions);
            Dimensions candidate = dimensions.get(temp);
            if (candidate == null) {
                candidate = new Dimensions(temp.encoded);
                dimensions.put(candidate, candidate);
            }
            if (++candidate.occurences > max.occurences) {
                max = candidate;
            }
        }
        result.close();
        return max.encoded;
    }

    /** A counter for source and target dimensions (to be kept together). */
    private static final class Dimensions {
        /** The dimensions as an encoded value. */ int encoded;
        /** The occurences of this dimensions.  */ int occurences;
        Dimensions(final int e) {
            encoded = e;
        }
        @Override public int hashCode() {
            return encoded;
        }
        @Override public boolean equals(final Object object) { // MUST ignore 'occurences'.
            return (object instanceof Dimensions) && ((Dimensions) object).encoded == encoded;
        }
        @Override public String toString() {
            return "[(" + (encoded >>> 16) + ',' + (encoded & 0xFFFF) + ")\u00D7" + occurences + ']';
        }
    }

    /**
     * Returns the dimension of the specified CRS. If the CRS is not found (which should not
     * happen, but we don't need to be strict here), then this method assumes a two-dimensional
     * CRS.
     */
    private short getDimensionForCRS(final String code) throws SQLException {
        final PreparedStatement stmt;
        final Short cached = axisCounts.get(code);
        final short dimension;
        if (cached == null) {
        	stmt = prepareStatement("Dimension",
            		"  SELECT COUNT(COORD_AXIS_CODE)"
                    +  " FROM [Coordinate Axis]"
                    + " WHERE COORD_SYS_CODE = (SELECT COORD_SYS_CODE "
                    +                           " FROM [Coordinate Reference System]"
                    +                          " WHERE COORD_REF_SYS_CODE = ?)");
	        stmt.setString(1, code);
	        final ResultSet result = stmt.executeQuery();
	        dimension = result.next() ? result.getShort(1) : 2;
	        axisCounts.put(code, dimension);
	        result.close();
        } else {
        	dimension = cached.shortValue();
        }
        return dimension;
    }

    /**
     * Returns {@code true} if the {@linkplain CoordinateOperation coordinate operation} for the
     * specified code is a {@linkplain Projection projection}. The caller must have ensured that
     * the designed operation is a {@linkplain Conversion conversion} before to invoke this method.
     */
    final synchronized  boolean isProjection(final String code) throws SQLException {
        final PreparedStatement stmt;
        Boolean projection = (Boolean) codeProjection.get(code);
        if (projection == null) {
	        stmt = prepareStatement("isProjection", "SELECT COORD_REF_SYS_CODE"
	                                              +  " FROM [Coordinate Reference System]"
	                                              + " WHERE PROJECTION_CONV_CODE = ?"
	                                              +   " AND COORD_REF_SYS_KIND LIKE 'projected%'");
	        stmt.setString(1, code);
	        final ResultSet result = stmt.executeQuery();
	        final boolean found = result.next();
	        result.close();
	        projection = Boolean.valueOf(found);
	        codeProjection.put(code, projection);
        }
        return projection.booleanValue();
    }

    /**
     * Returns a coordinate operation from a code.
     * The returned object will either be a {@linkplain Conversion conversion} or a
     * {@linkplain Transformation transformation}, depending on the code.
     *
     * @param  code Value allocated by authority.
     * @return The coordinate operation object.
     * @throws NoSuchAuthorityCodeException if this method can't find the requested code.
     * @throws FactoryException if some other kind of failure occured in the backing
     *         store. This exception usually have {@link SQLException} as its cause.
     */
    public synchronized CoordinateOperation generateCoordinateOperation(final String code)
            throws FactoryException
    {
        ensureNonNull("code", code);
        CoordinateOperation returnValue = null;
        try {
            final String primaryKey = toPrimaryKey(CoordinateOperation.class, code,
                    "[Coordinate_Operation]", "COORD_OP_CODE", "COORD_OP_NAME");
            final PreparedStatement stmt;
            stmt = prepareStatement("CoordinateOperation", "SELECT COORD_OP_CODE,"
                                                         +       " COORD_OP_NAME,"
                                                         +       " COORD_OP_TYPE,"
                                                         +       " SOURCE_CRS_CODE,"
                                                         +       " TARGET_CRS_CODE,"
                                                         +       " COORD_OP_METHOD_CODE,"
                                                         +       " COORD_TFM_VERSION,"
                                                         +       " COORD_OP_ACCURACY,"
                                                         +       " AREA_OF_USE_CODE,"
                                                         +       " COORD_OP_SCOPE,"
                                                         +       " REMARKS"
                                                         + " FROM [Coordinate_Operation]"
                                                         + " WHERE COORD_OP_CODE = ?");
            stmt.setString(1, primaryKey);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                final String epsg = getString(result, 1, code);
                final String name = getString(result, 2, code);
                final String type = getString(result, 3, code).trim().toLowerCase();
                final boolean isTransformation = type.equals("transformation");
                final boolean isConversion     = type.equals("conversion");
                final boolean isConcatenated   = type.equals("concatenated operation");
                final String sourceCode, targetCode, methodCode;
                if (isConversion) {
                    // Optional for conversions, mandatory for all others.
                    sourceCode = result.getString(4);
                    targetCode = result.getString(5);
                } else {
                    sourceCode = getString(result, 4, code);
                    targetCode = getString(result, 5, code);
                }
                if (isConcatenated) {
                    // Not applicable to concatenated operation, mandatory for all others.
                    methodCode = result.getString(6);
                } else {
                    methodCode = getString(result, 6, code);
                }
                String version  = result.getString( 7);
                double accuracy = result.getDouble( 8); if (result.wasNull()) accuracy=Double.NaN;
                String area     = result.getString( 9);
                String scope    = result.getString(10);
                String remarks  = result.getString(11);
                /*
                 * Gets the source and target CRS. They are mandatory for transformations (it
                 * was checked above in this method) and optional for conversions. Conversions
                 * are usually "defining conversions" and don't define source and target CRS.
                 * In EPSG database 6.7, all defining conversions are projections and their
                 * dimensions are always 2. However, this is not generalizable to other kind
                 * of operation methods. For example the "Geocentric translation" operation
                 * method has 3-dimensional source and target.
                 */
                final int sourceDimensions, targetDimensions;
                final CoordinateReferenceSystem sourceCRS, targetCRS;
                if (sourceCode != null) {
                    sourceCRS = createCoordinateReferenceSystem(sourceCode);
                    sourceDimensions = sourceCRS.getCoordinateSystem().getDimension();
                } else {
                    sourceCRS = null;
                    sourceDimensions = 2; // Acceptable default for projections only.
                }
                if (targetCode != null) {
                    targetCRS = createCoordinateReferenceSystem(targetCode);
                    targetDimensions = targetCRS.getCoordinateSystem().getDimension();
                } else {
                    targetCRS = null;
                    targetDimensions = 2; // Acceptable default for projections only.
                }
                /*
                 * Gets the operation method. This is mandatory for conversions and transformations
                 * (it was checked above in this method) but optional for concatenated operations.
                 * Fetching parameter values is part of this block.
                 */
                final boolean             isBursaWolf;
                OperationMethod           method;
                final ParameterValueGroup parameters;
                if (methodCode == null) {
                    isBursaWolf = false;
                    method      = null;
                    parameters  = null;
                } else {
                    final int num;
                    try {
                        num = Integer.parseInt(methodCode);
                    } catch (NumberFormatException exception) {
                        result.close();
                        throw new FactoryException(exception);
                    }
                    isBursaWolf = (num>=BURSA_WOLF_MIN_CODE && num<=BURSA_WOLF_MAX_CODE);
                    // Reminder: The source and target dimensions MUST be computed when
                    //           the information is available. Dimension is not always 2!!
                    method = generateOperationMethod(methodCode);
                    if (method.getSourceDimensions() != sourceDimensions ||
                        method.getTargetDimensions() != targetDimensions)
                    {
                        method = new DefaultOperationMethod(method, sourceDimensions, targetDimensions);
                    }
                    /*
                     * Note that some parameters required for MathTransform creation are implicit in
                     * the EPSG database (e.g. semi-major and semi-minor axis length in the case of
                     * map projections). We ask the parameter value group straight from the math
                     * transform factory instead of from the operation method in order to get all
                     * required parameter descriptors, including implicit ones.
                     */
                    final String classe = method.getName().getCode();
                    parameters = factories.getMathTransformFactory().getDefaultParameters(classe);
                    fillParameterValues(methodCode, epsg, parameters);
                }
                /*
                 * Creates common properties. The 'version' and 'accuracy' are usually defined
                 * for transformations only. However, we check them for all kind of operations
                 * (including conversions) and copy the information inconditionnaly if present.
                 *
                 * NOTE: This block must be executed last before object creations below, because
                 *       methods like createCoordinateReferenceSystem and createOperationMethod
                 *       overwrite the properties map.
                 */
                final Map<String,Object> properties = generateProperties(name, epsg, area, scope, remarks);
                if (version!=null && (version=version.trim()).length()!=0) {
                    properties.put(CoordinateOperation.OPERATION_VERSION_KEY, version);
                }
                if (!Double.isNaN(accuracy)) {
                    final QuantitativeResultImpl                 accuracyResult;
                    final AbsoluteExternalPositionalAccuracyImpl accuracyElement;
                    accuracyResult = new QuantitativeResultImpl(new double[]{accuracy});
                    // TODO: Need to invoke something equivalent to:
                    // accuracyResult.setValueType(Float.class);
                    // This is the type declared in the MS-Access database.
                    accuracyResult.setValueUnit(SI.METER); // In meters by definition in the EPSG database.
                    accuracyElement = new AbsoluteExternalPositionalAccuracyImpl(accuracyResult);
                    accuracyElement.setMeasureDescription(TRANSFORMATION_ACCURACY);
                    accuracyElement.setEvaluationMethodType(EvaluationMethodType.DIRECT_EXTERNAL);
                    properties.put(CoordinateOperation.COORDINATE_OPERATION_ACCURACY_KEY,
                                   new PositionalAccuracy[] {
                                       (PositionalAccuracy)accuracyElement.unmodifiable()
                                   });
                }
                /*
                 * Creates the operation. Conversions should be the only operations allowed to
                 * have null source and target CRS. In such case, the operation is a defining
                 * conversion (usually to be used later as part of a ProjectedCRS creation),
                 * and always a projection in the specific case of the EPSG database (which
                 * allowed us to assume 2-dimensional operation method in the code above for
                 * this specific case - not to be generalized to the whole EPSG database).
                 */
                final CoordinateOperation operation;
                if (isConversion && (sourceCRS==null || targetCRS==null)) {
                    // Note: we usually can't resolve sourceCRS and targetCRS because there
                    // is many of them for the same coordinate operation (projection) code.
                    operation = new DefiningConversion(properties, method, parameters);
                } else if (isConcatenated) {
                    /*
                     * Concatenated operation: we need to close the current result set, because
                     * we are going to invoke this method recursively in the following lines.
                     *
                     * Note: we instantiate directly the Geotools's implementation of
                     * ConcatenatedOperation instead of using CoordinateOperationFactory in order
                     * to avoid loading the quite large Geotools's implementation of this factory,
                     * and also because it is not part of FactoryGroup anyway.
                     */
                    result.close();
                    result = null;
                    final PreparedStatement cstmt = prepareStatement("ConcatenatedOperation",
                                                        "SELECT SINGLE_OPERATION_CODE"
                                                   +    " FROM [Coordinate_Operation Path]"
                                                   +   " WHERE (CONCAT_OPERATION_CODE = ?)"
                                                   + " ORDER BY OP_PATH_STEP");
                    cstmt.setString(1, epsg);
                    final ResultSet cr = cstmt.executeQuery();
                    final List<String> codes = new ArrayList<String>();
                    while (cr.next()) {
                        codes.add(cr.getString(1));
                    }
                    cr.close();
                    final CoordinateOperation[] operations = new CoordinateOperation[codes.size()];
                    if (!safetyGuard.add(epsg)) {
                        throw recursiveCall(ConcatenatedOperation.class, epsg);
                    } try {
                        for (int i=0; i<operations.length; i++) {
                            operations[i] = createCoordinateOperation(codes.get(i));
                        }
                    } finally {
                        safetyGuard.remove(epsg);
                    }
                    try {
                        return new DefaultConcatenatedOperation(properties, operations);
                    } catch (IllegalArgumentException exception) {
                        // May happen if there is less than 2 operations to concatenate.
                        // It happen for some deprecated CRS like 8658 for example.
                        throw new FactoryException(exception);
                    }
                } else {
                    /*
                     * Needs to create a math transform. A special processing is performed for
                     * datum shift methods, since the conversion from ellipsoid to geocentric
                     * for "geocentric translations" is implicit in the EPSG database. Even in
                     * the case of Molodenski transforms, the axis length to set are the same.
                     */
                    if (isBursaWolf) try {
                        Ellipsoid ellipsoid = CRSUtilities.getHeadGeoEllipsoid(sourceCRS);
                        if (ellipsoid != null) {
                            final Unit axisUnit = ellipsoid.getAxisUnit();
                            parameters.parameter("src_semi_major").setValue(ellipsoid.getSemiMajorAxis(), axisUnit);
                            parameters.parameter("src_semi_minor").setValue(ellipsoid.getSemiMinorAxis(), axisUnit);
                            parameters.parameter("src_dim").setValue(sourceCRS.getCoordinateSystem().getDimension());
                        }
                        ellipsoid = CRSUtilities.getHeadGeoEllipsoid(targetCRS);
                        if (ellipsoid != null) {
                            final Unit axisUnit = ellipsoid.getAxisUnit();
                            parameters.parameter("tgt_semi_major").setValue(ellipsoid.getSemiMajorAxis(), axisUnit);
                            parameters.parameter("tgt_semi_minor").setValue(ellipsoid.getSemiMinorAxis(), axisUnit);
                            parameters.parameter("tgt_dim").setValue(targetCRS.getCoordinateSystem().getDimension());
                        }
                    } catch (ParameterNotFoundException exception) {
                        result.close();
                        throw new FactoryException(Errors.format(
                                ErrorKeys.GEOTOOLS_EXTENSION_REQUIRED_$1,
                                method.getName().getCode(), exception));
                    }
                    /*
                     * At this stage, the parameters are ready for use. Creates the math transform
                     * and wraps it in the final operation (a Conversion or a Transformation).
                     */
                    final Class expected;
                    if (isTransformation) {
                        expected = Transformation.class;
                    } else if (isConversion) {
                        expected = Conversion.class;
                    } else {
                        result.close();
                        throw new FactoryException(Errors.format(ErrorKeys.UNKNOW_TYPE_$1, type));
                    }
                    final MathTransform mt = factories.getMathTransformFactory().createBaseToDerived(
                            sourceCRS, parameters, targetCRS.getCoordinateSystem());
                    // TODO: uses GeoAPI factory method once available.
                    operation = DefaultOperation.create(properties, sourceCRS, targetCRS,
                                                        mt, method, expected);
                }
                returnValue = ensureSingleton(operation, returnValue, code);
                if (result == null) {
                    // Bypass the 'result.close()' line below:
                    // the ResultSet has already been closed.
                    return returnValue;
                }
            }
            result.close();
        } catch (SQLException exception) {
            throw databaseFailure(CoordinateOperation.class, code, exception);
        }
        if (returnValue == null) {
             throw noSuchAuthorityCode(CoordinateOperation.class, code);
        }
        return returnValue;
    }

    /**
     * Creates operations from coordinate reference system codes.
     * The returned set is ordered with the most accurate operations first.
     *
     * @param sourceCode Coded value of source coordinate reference system.
     * @param targetCode Coded value of target coordinate reference system.
     * @throws FactoryException if the object creation failed.
     *
     * @todo The ordering is not consistent among all database software, because the "accuracy"
     *       column may contains null values. When used in an "ORDER BY" clause, PostgreSQL put
     *       null values last, while Access and HSQL put them first. The PostgreSQL's behavior is
     *       better for what we want (put operations with unknow accuracy last). Unfortunatly,
     *       I don't know yet how to instruct Access to put null values last using standard SQL
     *       ("IIF" is not standard, and Access doesn't seem to understand "CASE ... THEN" clauses).
     */
    public synchronized Set generateFromCoordinateReferenceSystemCodes(final String sourceCode,
                                                                     final String targetCode)
            throws FactoryException
    {
        ensureNonNull("sourceCode", sourceCode);
        ensureNonNull("targetCode", targetCode);
        final String pair = sourceCode + " \u21E8 " + targetCode;
        final CoordinateOperationSet set = new CoordinateOperationSet(this);
        try {
            final String sourceKey = toPrimaryKeyCRS(sourceCode);
            final String targetKey = toPrimaryKeyCRS(targetCode);
            boolean searchTransformations = false;
            do {
                /*
                 * This 'do' loop is executed twice: the first time for searching defining
                 * conversions, and the second time for searching all other kind of operations.
                 * Defining conversions are searched first because they are, by definition, the
                 * most accurate operations.
                 */
                final String key, sql;
                if (searchTransformations) {
                    key = "TransformationFromCRS";
                    sql = "SELECT COORD_OP_CODE"
                        +    " FROM [Coordinate_Operation]"
                        +    " WHERE SOURCE_CRS_CODE = ?"
                        +      " AND TARGET_CRS_CODE = ?"
                        + " ORDER BY ABS(DEPRECATED), COORD_OP_ACCURACY";
                } else {
                    key = "ConversionFromCRS";
                    sql = "SELECT PROJECTION_CONV_CODE"
                        +    " FROM [Coordinate Reference System]"
                        +    " WHERE SOURCE_GEOGCRS_CODE = ?"
                        +      " AND COORD_REF_SYS_CODE = ?";
                }
                final PreparedStatement stmt = prepareStatement(key, sql);
                stmt.setString(1, sourceKey);
                stmt.setString(2, targetKey);
                final ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    final String code = getString(result, 1, pair);
                    set.addAuthorityCode(code, searchTransformations ? null : targetKey);
                }
                result.close();
            } while ((searchTransformations = !searchTransformations) == true);
            /*
             * Search finished. We may have a lot of coordinate operations
             * (e.g. about 40 for "ED50" (EPSG:4230) to "WGS 84" (EPSG:4326)).
             * Alter the ordering using the information supplied in the supersession table.
             */
            final String[] codes = set.getAuthorityCodes();
            sort(codes);
            set.setAuthorityCodes(codes);
        } catch (SQLException exception) {
            throw databaseFailure(CoordinateOperation.class, pair, exception);
        }
        /*
         * Before to return the set, tests the creation of 1 object in order to report early
         * (i.e. now) any problems with SQL statements. Remaining operations will be created
         * only when first needed.
         */
        set.resolve(1);
        return set;
    }

    /**
     * Sorts an array of codes in preference order. This method orders pairwise the codes according
     * the information provided in the supersession table. If the same object is superseded by more
     * than one object, then the most recent one is inserted first. Except for the codes moved as a
     * result of pairwise ordering, this method try to preserve the old ordering of the supplied
     * codes (since deprecated operations should already be last). The ordering is performed in
     * place.
     *
     * @param codes The codes, usually as an array of {@link String}. If the array do not contains
     *              string objects, then the {@link Object#toString} method must returns the code
     *              for each element.
     */
    // TODO: Use generic type for "Object[] codes" with J2SE 1.5.
    private void sort(final Object[] codes) throws SQLException, FactoryException {
        if (codes.length <= 1) {
            return; // Nothing to sort.
        }
        final PreparedStatement stmt;
        stmt = prepareStatement("Supersession", "SELECT SUPERSEDED_BY"
                                              + " FROM [Supersession]"
                                              + " WHERE OBJECT_CODE = ?"
                                              + " ORDER BY SUPERSESSION_YEAR DESC");
        int maxIterations = 15; // For avoiding never-ending loop.
        do {
            boolean changed = false;
            for (int i=0; i<codes.length; i++) {
                final String code = codes[i].toString();
                stmt.setString(1, code);
                final ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    final String replacement = getString(result, 1, code);
                    for (int j=i+1; j<codes.length; j++) {
                        final Object candidate = codes[j];
                        if (replacement.equals(candidate.toString())) {
                            /*
                             * Found a code to move in front of the superceded one.
                             */
                            System.arraycopy(codes, i, codes, i+1, j-i);
                            codes[i++] = candidate;
                            changed = true;
                        }
                    }
                }
                result.close();
            }
            if (!changed) {
                return;
            }
        }
        while (--maxIterations != 0);
        LOGGER.finer("Possible recursivity in supersessions.");
    }

    /**
     * An implementation of {@link IdentifiedObjectFinder} which scans over a smaller set
     * of authority codes.
     * <p>
     * <b>Implementation note:</b> Since this method may be invoked indirectly by
     * {@link LongitudeFirstFactory}, it must be insensitive to axis order.
     */
    private final class Finder extends IdentifiedObjectFinder {
        /**
         * Creates a new finder backed by the specified <em>buffered</em> authority factory.
         */
        Finder( final Class/*<? extends IdentifiedObject>*/ type) {
            super( AbstractEpsgFactory.this, type);
        }

        /**
         * Returns a set of authority codes that <strong>may</strong> identify the same object
         * than the specified one. This implementation tries to get a smaller set than what
         * {@link AbstractEpsgFactory#getAuthorityCodes} would produce.
         */
        @Override
        protected Set getCodeCandidates(final IdentifiedObject object) throws FactoryException {
            String select = "COORD_REF_SYS_CODE";
            String from   = "[Coordinate Reference System]";
            String where, code;
            if (object instanceof Ellipsoid) {
                select = "ELLIPSOID_CODE";
                from   = "[Ellipsoid]";
                where  = "SEMI_MAJOR_AXIS";
                code   = Double.toString(((Ellipsoid) object).getSemiMajorAxis());
            } else {
                IdentifiedObject dependency;
                if (object instanceof GeneralDerivedCRS) {
                    dependency = ((GeneralDerivedCRS) object).getBaseCRS();
                    where      = "SOURCE_GEOGCRS_CODE";
                } else if (object instanceof SingleCRS) {
                    dependency = ((SingleCRS) object).getDatum();
                    where      = "DATUM_CODE";
                } else if (object instanceof GeodeticDatum) {
                    dependency = ((GeodeticDatum) object).getEllipsoid();
                    select     = "DATUM_CODE";
                    from       = "[Datum]";
                    where      = "ELLIPSOID_CODE";
                } else {
                    return super.getCodeCandidates(object);
                }
                dependency = getIdentifiedObjectFinder(dependency.getClass()).find(dependency);
                Identifier id = AbstractIdentifiedObject.getIdentifier(dependency, getAuthority());
                if (id == null || (code = id.getCode()) == null) {
                    return super.getCodeCandidates(object);
                }
            }
            String sql = "SELECT " + select + " FROM " + from + " WHERE " + where + "='" + code + '\'';
            sql = adaptSQL(sql);
            final Set<String> result = new LinkedHashSet<String>();
            try {
                final Statement s = getConnection().createStatement();
                final ResultSet r = s.executeQuery(sql);
                while (r.next()) {
                    result.add(r.getString(1));
                }
                r.close();
                s.close();
            } catch (SQLException exception) {
                throw databaseFailure(Identifier.class, code, exception);
            }
            return result;
        }
    }

    /**
     * Constructs an exception for recursive calls.
     */
    private static FactoryException recursiveCall(final Class type, final String code) {
        return new FactoryException(Errors.format(ErrorKeys.RECURSIVE_CALL_$2, type, code));
    }

    /**
     * Constructs an exception for a database failure.
     */
    private static FactoryException databaseFailure(final Class type, final String code,
                                                    final SQLException cause)
    {
        return new FactoryException(Errors.format(ErrorKeys.DATABASE_FAILURE_$2, type, code), cause);
    }

    /**
     * Invoked when a new {@link PreparedStatement} is about to be created from a SQL string.
     * Since the <A HREF="http://www.epsg.org">EPSG database</A> is available mainly in MS-Access
     * format, SQL statements are formatted using some syntax specific to this particular database
     * software (for example "<code>SELECT * FROM [Coordinate Reference System]</code>"). When
     * prociding subclass targeting another database vendor, then this method should be overridden
     * in order to adapt the local SQL syntax.
     * <p>
     * For example a subclass connecting to a <cite>PostgreSQL</cite> database could replace
     * all spaces ("&nbsp;") between watching braces ("[" and "]") by underscore ("_").
     *
     * @param  statement The statement in MS-Access syntax.
     * @return The SQL statement to use. The default implementation returns the string unchanged.
     */
    protected abstract String adaptSQL(final String statement);

    /**
     * Returns {@code true} if the specified code may be a primary key in some table. This method
     * do not needs to checks any entry in the database. It should just checks from the syntax if
     * the code looks like a valid EPSG identifier. The default implementation returns {@code true}
     * if all non-space characters are {@linkplain Character#isDigit(char) digits}.
     * <p>
     * When this method returns {@code false}, some {@code createFoo(...)} methods look for the
     * code in the name column instead of the primary key column. This allows to accept the
     * "<cite>NTF (Paris) / France I</cite>" string (for example) in addition to the {@code "27581"}
     * primary key. Both should fetch the same object.
     * <p>
     * If this method returns {@code true} in all cases, then this factory never search for matching
     * names. In such case, an appropriate exception will be thrown in {@code createFoo(...)}
     * methods if the code is not found in the primary key column. Subclasses can overrides this
     * method that way if this is the intended behavior.
     *
     * @param  code The code the inspect.
     * @return {@code true} if the code is probably a primary key.
     * @throws FactoryException if an unexpected error occured while inspecting the code.
     */
    protected boolean isPrimaryKey(final String code) throws FactoryException {
        final int length = code.length();
        for (int i=0; i<length; i++) {
            final char c = code.charAt(i);
            if (!Character.isDigit(c) && !Character.isSpaceChar(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if it is safe to dispose this factory. This method is invoked indirectly
     * by {@link ThreadedEpsgFactory} after some timeout in order to release resources. This method will
     * block the disposal if some {@linkplain #getAuthorityCodes set of authority codes} are still
     * in use.
     */
    final synchronized boolean canDispose() {
        return true;
    }

    /**
     * Disposes any resources hold by this object.
     *
     * @throws FactoryException if an error occurred while closing the connection.
     */
    @Override
    public synchronized void dispose() throws FactoryException {
        disconnect();
        super.dispose();
    }

    /**
     * Connect to the database in anticipation of of use.
     * @throws FactoryException
     */
    public void connect() throws FactoryException {
        try {
            getConnection();
        } catch (SQLException e) {
            throw new FactoryException(e);
        }
    }
    /**
     * Disconnect from the database, and remain idle. We will still keep our
     * internal data structures, we are not going to hold onto a database connection
     * unless we are going to be used.
     *
     * @throws FactoryException
     */
    public void disconnect() throws FactoryException {
        if (connection != null) {
            final boolean isClosed;
            try {
                isClosed = connection.isClosed();
                for (final Iterator it=statements.values().iterator(); it.hasNext();) {
                    ((PreparedStatement) it.next()).close();
                    it.remove();
                }
                connection.close();
            } catch (SQLException exception) {
                throw new FactoryException(exception);
            }
            if (!isClosed) {
                /*
                 * The above code was run unconditionally as a safety, even if the connection
                 * was already closed. However we will log a message only if we actually closed
                 * the connection, otherwise the log records are a little bit misleading.
                 */
                final LogRecord record = Loggings.format(Level.FINE, LoggingKeys.CLOSED_EPSG_DATABASE);
                record.setLoggerName(LOGGER.getName());
                LOGGER.log(record);
            }
            connection = null;
        }
    }

    /**
     * Access to the connection used by this EpsgFactory. The connection will
     * be created as needed.
     *
     * @return the connection
     */
    protected synchronized Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
        }
        return connection;
    }
    /**
     * Shutdown the database engine. This method is invoked twice by {@link ThreadedEpsgFactory}
     * at JVM shutdown: one time before the {@linkplain #connection} is closed, and a second
     * time after. This shutdown hook is useful for <cite>embedded</cite> database engine
     * starting a server process in addition to the client process. Just closing the connection
     * is not enough for them. Example:
     * <P>
     * <UL>
     *   <LI>HSQL database engine needs to execute a {@code "SHUTDOWN"} statement using the
     *      {@linkplain #connection} before it is closed.</LI>
     *   <LI>Derby database engine needs to instruct the {@linkplain java.sql.DriverManager driver
     *       manager} after all connections have been closed.</LI>
     * </UL>
     * <P>
     * The default implementation does nothing, which is sufficient for implementations
     * connecting to a distant server (i.e. non-embedded database engine), for example
     * {@linkplain AccessDataSource MS-Access} or {@linkplain PostgreDataSource PostgreSQL}.
     *
     * @param active {@code true} if the {@linkplain #connection} is alive, or {@code false}
     *        otherwise. This method is invoked first with {@code active} set to {@code true},
     *        then a second time with {@code active} set to {@code false}.
     * @throws SQLException if this method failed to shutdown the database engine.
     */
    protected void shutdown(final boolean active) throws SQLException {
    }

    /**
     * Invokes {@link #dispose} when this factory is garbage collected.
     *
     * @throws Throwable if an error occurred while closing the connection.
     */
    @Override
    protected final void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////                                                                                 ///////
    //////   HARD CODED VALUES (other than SQL statements) RELATIVE TO THE EPSG DATABASE   ///////
    //////                                                                                 ///////
    //////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a hard-coded unit from an EPSG code. We do not need to provide all units here,
     * but we must at least provide all base units declared in the [TARGET_UOM_CODE] column
     * of table [Unit of Measure]. Other units will be derived automatically if they are not
     * listed here.
     *
     * @param  code The code.
     * @return The unit, or {@code null} if the code is unrecognized.
     */
    private static Unit<?> getUnit(final int code) {
        switch (code) {
            case 9001: return    SI.METER;
            case 9002: return NonSI.FOOT;
            case 9030: return NonSI.NAUTICAL_MILE;
            case 9036: return    SI.KILO(SI.METER);
            case 9101: return    SI.RADIAN;
            case 9122: // Fall through
            case 9102: return NonSI.DEGREE_ANGLE;
            case 9103: return NonSI.MINUTE_ANGLE;
            case 9104: return NonSI.SECOND_ANGLE;
            case 9105: return NonSI.GRADE;
            case 9107: return Units.DEGREE_MINUTE_SECOND;
            case 9108: return Units.DEGREE_MINUTE_SECOND;
            case 9109: return    SI.MICRO(SI.RADIAN);
            case 9110: return Units.SEXAGESIMAL_DMS;
//TODO      case 9111: return NonSI.SEXAGESIMAL_DM;
            case 9203: // Fall through
            case 9201: return  Unit.ONE;
            case 9202: return Units.PPM;
            default  : return null;
        }
    }

    /**
     * Set a Bursa-Wolf parameter from an EPSG parameter.
     *
     * @param  parameters The Bursa-Wolf parameters to modify.
     * @param  code       The EPSG code for a parameter   from [PARAMETER_CODE]  column.
     * @param  value      The value of the parameter      from [PARAMETER_VALUE] column.
     * @param  unit       The unit of the parameter value from [UOM_CODE]        column.
     * @throws FactoryException if the code is unrecognized.
     */
    private static void setBursaWolfParameter(final BursaWolfParameters parameters,
                                              final int code, double value, final Unit<?> unit)
            throws FactoryException
    {
        Unit<?> target = unit;
        if (code >= 8605) {
            if      (code <= 8607) target = SI   .METER;
            else if (code <= 8710) target = NonSI.SECOND_ANGLE;
            else if (code == 8611) target = Units.PPM;
        }
        if (target != unit) {
            value = unit.getConverterTo(target).convert(value);
        }
        switch (code) {
            case 8605: parameters.dx  = value; break;
            case 8606: parameters.dy  = value; break;
            case 8607: parameters.dz  = value; break;
            case 8608: parameters.ex  = value; break;
            case 8609: parameters.ey  = value; break;
            case 8610: parameters.ez  = value; break;
            case 8611: parameters.ppm = value; break;
            default:   throw new FactoryException(Errors.format(ErrorKeys.UNEXPECTED_PARAMETER_$1, code));
        }
    }

    /**
     * List of tables and columns to test for codes values.
     * This table is used by the {@link #createObject} method in order to detect
     * which of the following methods should be invoked for a given code:
     *
     * {@link #createCoordinateReferenceSystem}
     * {@link #createCoordinateSystem}
     * {@link #createDatum}
     * {@link #createEllipsoid}
     * {@link #createUnit}
     *
     * The order is significant: it is the key for a {@code switch} statement.
     *
     * @see #createObject
     * @see #lastObjectType
     */
    private static final TableInfo[] TABLES_INFO = {
        new TableInfo(CoordinateReferenceSystem.class,
                      "[Coordinate Reference System]",
                      "COORD_REF_SYS_CODE",
                      "COORD_REF_SYS_NAME",
                      "COORD_REF_SYS_KIND",
         new Class[] { ProjectedCRS.class, GeographicCRS.class, GeocentricCRS.class},
         new String[] {"projected",       "geographic",        "geocentric"}),

        new TableInfo(CoordinateSystem.class,
                      "[Coordinate System]",
                      "COORD_SYS_CODE",
                      "COORD_SYS_NAME",
                      "COORD_SYS_TYPE",
         new Class[]  { CartesianCS.class, EllipsoidalCS.class, SphericalCS.class, VerticalCS.class},
         new String[] {"Cartesian",       "ellipsoidal",       "spherical",       "vertical"}),

        new TableInfo(CoordinateSystemAxis.class,
                      "[Coordinate Axis] AS CA INNER JOIN [Coordinate Axis Name] AS CAN"+
                                       " ON CA.COORD_AXIS_NAME_CODE=CAN.COORD_AXIS_NAME_CODE",
                      "COORD_AXIS_CODE",
                      "COORD_AXIS_NAME"),

        new TableInfo(Datum.class,
                      "[Datum]",
                      "DATUM_CODE",
                      "DATUM_NAME",
                      "DATUM_TYPE",
         new Class[] { GeodeticDatum.class, VerticalDatum.class, EngineeringDatum.class},
         new String[]{"geodetic",          "vertical",          "engineering"}),

        new TableInfo(Ellipsoid.class,
                      "[Ellipsoid]",
                      "ELLIPSOID_CODE",
                      "ELLIPSOID_NAME"),

        new TableInfo(PrimeMeridian.class,
                      "[Prime Meridian]",
                      "PRIME_MERIDIAN_CODE",
                      "PRIME_MERIDIAN_NAME"),

        new TableInfo(CoordinateOperation.class,
                      "[Coordinate_Operation]",
                      "COORD_OP_CODE",
                      "COORD_OP_NAME",
                      "COORD_OP_TYPE",
         new Class[] { Projection.class, Conversion.class, Transformation.class},
         new String[]{"conversion",     "conversion",     "transformation"}),
                      // Note: Projection is handle in a special way.

        new TableInfo(OperationMethod.class,
                      "[Coordinate_Operation Method]",
                      "COORD_OP_METHOD_CODE",
                      "COORD_OP_METHOD_NAME"),

        new TableInfo(ParameterDescriptor.class,
                      "[Coordinate_Operation Parameter]",
                      "PARAMETER_CODE",
                      "PARAMETER_NAME"),

        new TableInfo(Unit.class,
                      "[Unit of Measure]",
                      "UOM_CODE",
                      "UNIT_OF_MEAS_NAME")
    };

    ///////////////////////////////////////////////////////////////////////////////
    ////////                                                               ////////
    ////////        E N D   O F   H A R D   C O D E D   V A L U E S        ////////
    ////////                                                               ////////
    ////////    NOTE: 'createFoo(...)' methods may still have hard-coded   ////////
    ////////    values (others than SQL statements) in 'equalsIgnoreCase'  ////////
    ////////    expressions.                                               ////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
     * A set of EPSG authority codes. This set makes use of our connection to the EPSG database.
     * All {@link #iterator} method call creates a new {@link ResultSet} holding the codes. However,
     * call to {@link #contains} map directly to a SQL call.
     * <p>
     * Serialization of this class store a copy of all authority codes. The serialization
     * do not preserve any connection to the database.
     *
     * @since 2.2
     * @source $URL$
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    final class AuthorityCodeSet extends AbstractSet<String> implements Serializable {
        /**
         * For compatibility with different versions.
         */
        private static final long serialVersionUID = 7105664579449680562L;

        /**
         * The type for this code set. This is translated to the most appropriate
         * interface type even if the user supplied an implementation type.
         */
        public final Class<?> type;

        /**
         * {@code true} if {@link #type} is assignable to {@link Projection}.
         */
        private final boolean isProjection;

        /**
         * A view of this set as a map with object's name as values, or {@code null} if none.
         * Will be created only when first needed.
         */
        private transient java.util.Map<String,String> asMap;

        /**
         * The SQL command to use for creating the {@code queryAll} statement.
         * Used for iteration over all codes.
         */
        final String sqlAll;

        /**
         * The SQL command to use for creating the {@code querySingle} statement.
         * Used for fetching the description from a code.
         */
        private final String sqlSingle;

        /**
         * The statement to use for querying all codes.
         * Will be created only when first needed.
         */
        private transient PreparedStatement queryAll;

        /**
         * The statement to use for querying a single code.
         * Will be created only when first needed.
         */
        private transient PreparedStatement querySingle;

        /**
         * The collection's size, or a negative value if not yet computed. The records will be counted
         * only when first needed. The special value -2 if set by {@link #isEmpty} if the size has not
         * yet been computed, but we know that the set is not empty.
         */
        private int size = -1;

        /**
         * Creates a new set of authority codes for the specified type.
         *
         * @param  connection The connection to the EPSG database.
         * @param  table      The table to query.
         * @param  type       The type to query.
         * @param  factory    The factory originator.
         */
        public AuthorityCodeSet(final TableInfo table,final Class type){
            final StringBuilder buffer = new StringBuilder("SELECT ");
            buffer.append(table.codeColumn);
            if (table.nameColumn != null) {
                buffer.append(", ").append(table.nameColumn);
            }
            buffer.append(" FROM ").append(table.table);
            boolean hasWhere = false;
            Class tableType = table.type;
            if (table.typeColumn != null) {
                for (int i=0; i<table.subTypes.length; i++) {
                    final Class<?> candidate = table.subTypes[i];
                    if (candidate.isAssignableFrom(type)) {
                        buffer.append(" WHERE (").append(table.typeColumn)
                              .append(" LIKE '").append(table.typeNames[i]).append("%'");
                        hasWhere = true;
                        tableType = candidate;
                        break;
                    }
                }
                if (hasWhere) {
                    buffer.append(')');
                }
            }
            this.type = tableType;
            isProjection = Projection.class.isAssignableFrom(tableType);
            final int length = buffer.length();
            buffer.append(" ORDER BY ").append(table.codeColumn);
            sqlAll = adaptSQL(buffer.toString());
            buffer.setLength(length);
            buffer.append(hasWhere ? " AND " : " WHERE ").append(table.codeColumn).append(" = ?");
            sqlSingle = adaptSQL(buffer.toString());
        }

        /**
         * Returns all codes.
         */
        private ResultSet getAll() throws SQLException {
            assert Thread.holdsLock(this);
            if (queryAll != null) {
                try {
                    return queryAll.executeQuery();
                } catch (SQLException ignore) {
                    /*
                     * Failed to reuse an existing statement. This problem occurs in some occasions
                     * with the JDBC-ODBC bridge in Java 6 (the error message is "Invalid handle").
                     * I'm not sure where the bug come from (didn't noticed it when using HSQL). We
                     * will try again with a new statement created in the code after this 'catch'
                     * clause. Note that we set 'queryAll' to null first in case of failure during
                     * the 'prepareStatement(...)' execution.
                     */
                    queryAll.close();
                    queryAll = null;
                    recoverableException("getAll", ignore);
                }
            }
            queryAll = getConnection().prepareStatement(sqlAll);
            return queryAll.executeQuery();
        }

        /**
         * Returns a single code.
         */
        private ResultSet getSingle(final Object code) throws SQLException {
            assert Thread.holdsLock(this);
            if (querySingle == null) {
                querySingle = getConnection().prepareStatement(sqlSingle);
            }
            querySingle.setString(1, code.toString());
            return querySingle.executeQuery();
        }

        /**
         * Returns {@code true} if the code in the specified result set is acceptable.
         * This method handle projections in a special way.
         */
        private boolean isAcceptable(final ResultSet results) throws SQLException {
            if (!isProjection) {
                return true;
            }
            final String code = results.getString(1);
            return isProjection(code);
        }

        /**
         * Returns {@code true} if the code in the specified code is acceptable.
         * This method handle projections in a special way.
         */
        private boolean isAcceptable(final String code) throws SQLException {
            if (!isProjection) {
                return true;
            }
            return isProjection(code);
        }

        /**
         * Returns {@code true} if this collection contains no elements.
         * This method fetch at most one row instead of counting all rows.
         */
        @Override
        public synchronized boolean isEmpty() {
            if (size != -1) {
                return size == 0;
            }
            boolean empty = true;
            try {
                final ResultSet results = getAll();
                while (results.next()) {
                    if (isAcceptable(results)) {
                        empty = false;
                        break;
                    }
                }
                results.close();
            } catch (SQLException exception) {
                unexpectedException("isEmpty", exception);
            }
            size = empty ? 0 : -2;
            return empty;
        }

        /**
         * Count the number of elements in the underlying result set.
         */
        public synchronized int size() {
            if (size >= 0) {
                return size;
            }
            int count = 0;
            try {
                final ResultSet results = getAll();
                while (results.next()) {
                    if (isAcceptable(results)) {
                        count++;
                    }
                }
                results.close();
            } catch (SQLException exception) {
                unexpectedException("size", exception);
            }
            size = count; // Stores only on success.
            return count;
        }

        /**
         * Returns {@code true} if this collection contains the specified element.
         */
        @Override
        public synchronized boolean contains(final Object code) {
            boolean exists = false;
            if (code != null) try {
                final ResultSet results = getSingle(code);
                while (results.next()) {
                    if (isAcceptable(results)) {
                        exists = true;
                        break;
                    }
                }
                results.close();
            } catch (SQLException exception) {
                unexpectedException("contains", exception);
            }
            return exists;
        }

        /**
         * Returns an iterator over the codes. The iterator is backed by a living {@link ResultSet},
         * which will be closed as soon as the iterator reach the last element.
         */
        public synchronized java.util.Iterator<String> iterator() {
            try {
                final Iterator iterator = new Iterator(getAll());
                /*
                 * Set the statement to null without closing it, in order to force a new statement
                 * creation if getAll() is invoked before the iterator finish its iteration.  This
                 * is needed because only one ResultSet is allowed for each Statement.
                 */
                queryAll = null;
                return iterator;
            } catch (SQLException exception) {
                unexpectedException("iterator", exception);
                final Set<String> empty = Collections.emptySet();
                return empty.iterator();
            }
        }

        /**
         * Returns a serializable copy of this set. This method is invoked automatically during
         * serialization. The serialised set of authority code is disconnected from the underlying
         * database.
         */
        protected Object writeReplace() throws ObjectStreamException {
            return new LinkedHashSet<String>(this);
        }

        /**
         * Closes the underlying statements. Note: this method is also invoked directly
         * by {@link DirectEpsgFactory#dispose}, which is okay in this particular case since
         * the implementation of this method can be executed an arbitrary amount of times.
         */
        @Override
        protected synchronized void finalize() throws SQLException {
            if (querySingle != null) {
                querySingle.close();
                querySingle = null;
            }
            if (queryAll != null) {
                queryAll.close();
                queryAll = null;
            }
        }

        /**
         * Invoked when an exception occured. This method just log a warning.
         */
        private void unexpectedException(final String       method,
                                                final SQLException exception)
        {
            unexpectedException(AuthorityCodes.class, method, exception);
        }

        /**
         * Invoked when an exception occured. This method just log a warning.
         */
        void unexpectedException(final Class        classe,
                                        final String       method,
                                        final SQLException exception)
        {
            Logging.unexpectedException(LOGGER, classe, method, exception);
        }

        /**
         * Invoked when a recoverable exception occured.
         */
        private void recoverableException(final String method, final SQLException exception) {
            // Uses the FINE level instead of WARNING because it may be a recoverable error.
            LogRecord record = Loggings.format(Level.FINE, LoggingKeys.UNEXPECTED_EXCEPTION);
            record.setSourceClassName(AuthorityCodes.class.getName());
            record.setSourceMethodName(method);
            record.setThrown(exception);
            record.setLoggerName(LOGGER.getName());
            LOGGER.log(record);
        }

        /**
         * The iterator over the codes. This inner class must kept a reference toward the enclosing
         * {@link AuthorityCodes} in order to prevent a call to {@link AuthorityCodes#finalize}
         * before the iteration is finished.
         */
        private final class Iterator implements java.util.Iterator<String> {
            /** The result set, or {@code null} if there is no more elements. */
            private ResultSet results;

            /** The next code. */
            private transient String next;

            /** Creates a new iterator for the specified result set. */
            Iterator(final ResultSet results) throws SQLException {
                assert Thread.holdsLock(AuthorityCodeSet.this);
                this.results = results;
                toNext();
            }

            /** Moves to the next element. */
            private void toNext() throws SQLException {
                while (results.next()) {
                    next = results.getString(1);
                    if (isAcceptable(next)) {
                        return;
                    }
                }
                finalize();
            }

            /** Returns {@code true} if there is more elements. */
            public boolean hasNext() {
                return results != null;
            }

            /** Returns the next element. */
            public String next() {
                if (results == null) {
                    throw new NoSuchElementException();
                }
                final String current = next;
                try {
                    toNext();
                } catch (SQLException exception) {
                    results = null;
                    unexpectedException(Iterator.class, "next", exception);
                }
                return current;
            }

            /** Always throws an exception, since this iterator is read-only. */
            public void remove() {
                throw new UnsupportedOperationException();
            }

            /** Closes the underlying result set. */
            @Override
            protected void finalize() throws SQLException {
                next = null;
                if (results != null) {
                    final PreparedStatement owner = (PreparedStatement) results.getStatement();
                    results.close();
                    results = null;
                    synchronized (AuthorityCodeSet.this) {
                        /*
                         * We don't need the statement anymore. Gives it back to the enclosing class
                         * in order to avoid creating a new one when AuthorityCodes.getAll() will be
                         * invoked again,  or closes the statement if getAll() already created a new
                         * statement anyway.
                         */
                        assert owner != queryAll;
                        if (queryAll == null) {
                            queryAll = owner;
                        } else {
                            owner.close();
                        }
                    }
                }
            }
        }

        /**
         * Returns a view of this set as a map with object's name as value, or {@code null} if none.
         */
        final java.util.Map<String,String> asMap() {
            if (asMap == null) {
                asMap = new Map();
            }
            return asMap;
        }

        /**
         * A view of {@link AuthorityCodes} as a map, with authority codes as key and
         * object names as values.
         */
        private final class Map extends AbstractMap<String,String> {
            /**
             * Returns the number of key-value mappings in this map.
             */
            @Override
            public int size() {
                return AuthorityCodeSet.this.size();
            }

            /**
             * Returns {@code true} if this map contains no key-value mappings.
             */
            @Override
            public boolean isEmpty() {
                return AuthorityCodeSet.this.isEmpty();
            }

            /**
             * Returns the description to which this map maps the specified EPSG code.
             */
            @Override
            public String get(final Object code) {
                String value = null;
                if (code != null) try {
                    synchronized (AuthorityCodeSet.this) {
                        final ResultSet results = getSingle(code);
                        while (results.next()) {
                            if (isAcceptable(results)) {
                                value = results.getString(2);
                                break;
                            }
                        }
                        results.close();
                    }
                } catch (SQLException exception) {
                    unexpectedException("get", exception);
                }
                return value;
            }

            /**
             * Returns {@code true} if this map contains a mapping for the specified EPSG code.
             */
            @Override
            public boolean containsKey(final Object key) {
                return contains(key);
            }

            /**
             * Returns a set view of the keys contained in this map.
             */
            @Override
            public Set<String> keySet() {
                return AuthorityCodeSet.this;
            }

            /**
             * Returns a set view of the mappings contained in this map.
             *
             * @todo Not yet implemented.
             */
            public Set<Map.Entry<String,String>> entrySet() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
