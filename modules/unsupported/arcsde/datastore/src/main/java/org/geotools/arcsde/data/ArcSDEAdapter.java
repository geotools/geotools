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

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeDefs;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeExtent;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.data.DataSourceException;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Utility class to deal with SDE specifics such as creating SeQuery objects from geotool's Query's,
 * mapping SDE types to Java ones and JTS Geometries, etc.
 *
 * @author Gabriel Roldan
 */
@SuppressWarnings("deprecation")
public class ArcSDEAdapter {
    /** Logger for this class' package */
    private static final Logger LOGGER = Logging.getLogger(ArcSDEAdapter.class);

    /** mappings of SDE attribute's types to Java ones */
    private static final Map<Integer, Class<?>> sde2JavaTypes = new HashMap<Integer, Class<?>>();

    /** inverse of sdeTypes, maps Java types to SDE ones */
    private static final Map<Class<?>, SdeTypeDef> java2SDETypes =
            new HashMap<Class<?>, SdeTypeDef>();

    static {
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_NSTRING), String.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_STRING), String.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_INT16), Short.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_INT32), Integer.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_INT64), Long.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_FLOAT32), Float.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_FLOAT64), Double.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_DATE), Date.class);
        // @TODO: not at all, only for capable open table with GeoServer
        // sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_BLOB),
        // byte[].class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_CLOB), String.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_NCLOB), String.class);

        // @TODO sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_CLOB),
        // String.class);
        // @Tested for view
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_UUID), String.class);
        // @TODO sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_XML),
        // org.w3c.dom.Document.class);

        // deprecated codes as for ArcSDE 9.0+. Adding them to maintain < 9.0
        // compatibility
        // though the assigned int codes matched their new counterparts, I let
        // them here as a reminder
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_SMALLINT), Short.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_INTEGER), Integer.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_FLOAT), Float.class);
        sde2JavaTypes.put(Integer.valueOf(SeColumnDefinition.TYPE_DOUBLE), Double.class);

        /**
         * By now keep using the deprecated constants (TYPE_INTEGER, etc.), switching directly to
         * the new ones gives problems with ArcSDE instances prior to version 9.0.
         */
        // SeColumnDefinition.TYPE_RASTER is not supported...
        java2SDETypes.put(String.class, new SdeTypeDef(SeColumnDefinition.TYPE_STRING, 255, 0));
        java2SDETypes.put(Short.class, new SdeTypeDef(SeColumnDefinition.TYPE_SMALLINT, 4, 0));
        java2SDETypes.put(Integer.class, new SdeTypeDef(SeColumnDefinition.TYPE_INTEGER, 10, 0));
        java2SDETypes.put(Float.class, new SdeTypeDef(SeColumnDefinition.TYPE_FLOAT, 5, 2));
        java2SDETypes.put(Double.class, new SdeTypeDef(SeColumnDefinition.TYPE_DOUBLE, 25, 4));
        java2SDETypes.put(Date.class, new SdeTypeDef(SeColumnDefinition.TYPE_DATE, 1, 0));
        java2SDETypes.put(Long.class, new SdeTypeDef(SeColumnDefinition.TYPE_INTEGER, 10, 0));
        // java2SDETypes.put(byte[].class, new
        // SdeTypeDef(SeColumnDefinition.TYPE_BLOB, 1, 0));
        java2SDETypes.put(Number.class, new SdeTypeDef(SeColumnDefinition.TYPE_DOUBLE, 25, 4));
    }

    public static int guessShapeTypes(GeometryDescriptor attribute) {
        if (attribute == null) {
            throw new NullPointerException("a GeometryAttributeType must be provided, got null");
        }

        Class<?> geometryClass = attribute.getType().getBinding();

        int shapeTypes = 0;

        if (attribute.isNillable()) {
            shapeTypes |= SeLayer.SE_NIL_TYPE_MASK;
        }

        if (GeometryCollection.class.isAssignableFrom(geometryClass)) {
            shapeTypes |= SeLayer.SE_MULTIPART_TYPE_MASK;

            if (geometryClass == MultiPoint.class) {
                shapeTypes |= SeLayer.SE_POINT_TYPE_MASK;
            } else if (geometryClass == MultiLineString.class) {
                shapeTypes |= SeLayer.SE_LINE_TYPE_MASK;
            } else if (geometryClass == MultiPolygon.class) {
                shapeTypes |= SeLayer.SE_AREA_TYPE_MASK;
            } else {
                throw new IllegalArgumentException("no SDE geometry mapping for " + geometryClass);
            }
        } else {
            if (geometryClass == Point.class) {
                shapeTypes |= SeLayer.SE_POINT_TYPE_MASK;
            } else if (geometryClass == LineString.class) {
                shapeTypes |= SeLayer.SE_LINE_TYPE_MASK;
            } else if (geometryClass == Polygon.class) {
                shapeTypes |= SeLayer.SE_AREA_TYPE_MASK;
            } else if (geometryClass == Geometry.class) {
                LOGGER.fine("Creating SeShape types for all types of geometries.");
                shapeTypes |=
                        (SeLayer.SE_MULTIPART_TYPE_MASK
                                | SeLayer.SE_POINT_TYPE_MASK
                                | SeLayer.SE_LINE_TYPE_MASK
                                | SeLayer.SE_AREA_TYPE_MASK);
            } else {
                throw new IllegalArgumentException("no SDE geometry mapping for " + geometryClass);
            }
        }

        return shapeTypes;
    }

    /**
     * Creates the column definition as used by the ArcSDE Java API, for the given AttributeType.
     *
     * @param type the source attribute definition.
     * @return an <code>SeColumnDefinition</code> object matching the properties of the source
     *     AttributeType.
     * @throws SeException if the SeColumnDefinition constructor throws it due to some invalid
     *     parameter
     */
    public static SeColumnDefinition createSeColumnDefinition(AttributeDescriptor type)
            throws SeException {
        SeColumnDefinition colDef = null;
        String colName = type.getLocalName();
        boolean nillable = type.isNillable();

        SdeTypeDef def = getSdeType(type.getType().getBinding());

        int sdeColType = def.colDefType;
        int fieldLength = def.size;
        int fieldScale = def.scale;

        colDef = new SeColumnDefinition(colName, sdeColType, fieldLength, fieldScale, nillable);

        return colDef;
    }

    /** @return an SdeTypeDef instance with default values for the given class */
    private static SdeTypeDef getSdeType(Class<?> attClass) throws IllegalArgumentException {
        SdeTypeDef sdeType = java2SDETypes.get(attClass);

        if (sdeType == null) {
            throw new IllegalArgumentException("No SDE type mapping for " + attClass.getName());
        }

        return sdeType;
    }

    // public static FeatureTypeInfo fetchSchema(final String typeName,
    // final String namespace,
    // final SessionPool pool) throws IOException {
    // return pool.issueReadOnly(new Command<FeatureTypeInfo>() {
    // @Override
    // public FeatureTypeInfo execute(Session session, SeConnection connection)
    // throws SeException, IOException {
    // return fetchSchema(typeName, namespace, session);
    // }
    // });
    // }

    /**
     * Fetches the schema of a given ArcSDE featureclass and creates its corresponding Geotools
     * FeatureType
     *
     * @return the feature type info representing the ArcSDE feature class given by the layer and
     *     table.
     * @throws IOException if an exception is caught accessing the sde feature class metadata.
     */
    public static FeatureTypeInfo fetchSchema(
            final String typeName, final String namespace, final ISession session)
            throws IOException {
        final SeTable table = session.getTable(typeName);
        SeLayer layer = null;

        final SeColumnDefinition[] seColumns = session.describe(typeName);
        for (SeColumnDefinition col : seColumns) {
            if (col.getType() == SeColumnDefinition.TYPE_SHAPE) {
                layer = session.getLayer(typeName);
                break;
            }
        }

        final List<AttributeDescriptor> properties =
                createAttributeDescriptors(layer, namespace, seColumns);

        final SimpleFeatureType featureType = createSchema(typeName, namespace, properties);

        SeRegistration registration = session.createSeRegistration(typeName);

        final boolean isMultiVersioned = registration.isMultiVersion();
        final boolean isView = registration.isView();
        final FIDReader fidStrategy;
        fidStrategy = FIDReader.getFidReader(session, table, layer, registration);
        final boolean canDoTransactions;
        {
            final Integer permMask =
                    session.issue(
                            new Command<Integer>() {
                                @Override
                                public Integer execute(ISession session, SeConnection connection)
                                        throws SeException, IOException {
                                    return Integer.valueOf(table.getPermissions());
                                }
                            });
            final boolean hasWritePermissions = userHasWritePermissions(permMask.intValue());
            canDoTransactions =
                    hasWritePermissions
                            && (fidStrategy instanceof FIDReader.SdeManagedFidReader
                                    || fidStrategy instanceof FIDReader.UserManagedFidReader)
                            && !hasReadOnlyColumn(seColumns);
            if (hasWritePermissions && !canDoTransactions) {
                LOGGER.fine(
                        typeName
                                + " is writable bu has no primary key, thus we're using it "
                                + "read-only as can't get a propper feature id out of it");
            }
        }
        FeatureTypeInfo typeInfo =
                new FeatureTypeInfo(
                        featureType, fidStrategy, canDoTransactions, isMultiVersioned, isView);
        return typeInfo;
    }

    /**
     * Check if any of the column types are read-only (such as CLOB).
     *
     * <p>This check should be temporary; currently writing CLOB types is producing a segmentation
     * fault (gasp!) in ArcSDE 9.3. We imagine the java encoding is not quite what ArcSDE expected.
     *
     * @return true if any of the columns are read-only
     */
    private static boolean hasReadOnlyColumn(SeColumnDefinition[] seColumns) {
        for (SeColumnDefinition col : seColumns) {
            if (col.getType() == SeColumnDefinition.TYPE_CLOB
                    || col.getType() == SeColumnDefinition.TYPE_NCLOB) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks wether the user can write to the given {@code table}.
     *
     * <p>Depends on the proviledges of the user the connection the table was created with.
     *
     * @param permissions the sde table permissions mask (as per SeTable.getPermissions())to check
     *     for write permissions
     * @return {@code true} if the table's connection user has both insert, update and delete
     *     priviledges.
     * @throws ArcSdeException if an SeException is thrown asking the table for the permissions
     */
    private static boolean userHasWritePermissions(final int permissions) throws ArcSdeException {

        final int insertMask = SeDefs.SE_INSERT_PRIVILEGE;
        final int updateMask = SeDefs.SE_UPDATE_PRIVILEGE;
        final int deleteMask = SeDefs.SE_DELETE_PRIVILEGE;
        boolean canWrite = false;
        if (((insertMask & permissions) == insertMask)
                && ((updateMask & permissions) == updateMask)
                && ((deleteMask & permissions) == deleteMask)) {
            canWrite = true;
        }
        return canWrite;
    }

    /** Creates a schema for the "SQL SELECT" like view definition */
    public static FeatureTypeInfo createInprocessViewSchema(
            final ISession session,
            final String typeName,
            final String namespace,
            final PlainSelect qualifiedSelect,
            final SeQueryInfo queryInfo)
            throws IOException {

        List<AttributeDescriptor> attributeDescriptors;

        // is the first table is a layer, we'll get it to obtain CRS info
        // from
        String mainTable;
        try {
            mainTable = queryInfo.getConstruct().getTables()[0];
        } catch (SeException e) {
            throw new ArcSdeException(e);
        }

        SeLayer layer = null;
        try {
            layer = session.getLayer(mainTable);
        } catch (NoSuchElementException e) {
            LOGGER.info(mainTable + " is not an SeLayer, so no CRS info will be parsed");
        }
        LOGGER.fine("testing query");

        final Command<SeColumnDefinition[]> testQueryCmd =
                new Command<SeColumnDefinition[]>() {
                    @Override
                    public SeColumnDefinition[] execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        final SeQuery testQuery = new SeQuery(connection);

                        try {
                            testQuery.prepareQueryInfo(queryInfo);
                            testQuery.execute();
                            LOGGER.fine("definition query executed successfully");

                            LOGGER.fine("fetching row to obtain view's types");

                            SeRow testRow = testQuery.fetch();
                            SeColumnDefinition[] colDefs = testRow.getColumns();
                            return colDefs;
                        } finally {
                            try {
                                testQuery.close();
                            } catch (SeException e) {
                                throw new ArcSdeException(e);
                            }
                        }
                    }
                };

        final SeColumnDefinition[] colDefs = session.issue(testQueryCmd);
        attributeDescriptors = createAttributeDescriptors(layer, namespace, colDefs);

        final SimpleFeatureType type = createSchema(typeName, namespace, attributeDescriptors);
        final FIDReader fidStrategy = FIDReader.NULL_READER;

        FeatureTypeInfo typeInfo;
        typeInfo = new FeatureTypeInfo(type, fidStrategy, qualifiedSelect, queryInfo);

        return typeInfo;
    }

    private static List<AttributeDescriptor> createAttributeDescriptors(
            SeLayer sdeLayer, String namespace, SeColumnDefinition[] seColumns)
            throws DataSourceException {
        String attName;
        boolean isNilable;
        int fieldLen;
        Object defValue;
        CoordinateReferenceSystem metadata = null;

        final int nCols = seColumns.length;
        List<AttributeDescriptor> attDescriptors = new ArrayList<AttributeDescriptor>(nCols);

        Class<?> typeClass = null;

        for (int i = 0; i < nCols; i++) {
            SeColumnDefinition colDef = seColumns[i];

            // didn't found in the ArcSDE Java API the way of knowing
            // if an SeColumnDefinition is nillable
            attName = colDef.getName();
            isNilable = colDef.allowsNulls();
            defValue = null;
            fieldLen = colDef.getSize();

            final Integer sdeType = Integer.valueOf(colDef.getType());

            if (sdeType.intValue() == SeColumnDefinition.TYPE_SHAPE) {
                CoordinateReferenceSystem crs = null;

                crs = parseCRS(sdeLayer);
                metadata = crs;

                int seShapeType = sdeLayer.getShapeTypes();
                typeClass = getGeometryTypeFromLayerMask(seShapeType);
                isNilable = (seShapeType & SeLayer.SE_NIL_TYPE_MASK) == SeLayer.SE_NIL_TYPE_MASK;
                defValue = isNilable ? null : ArcSDEGeometryBuilder.defaultValueFor(typeClass);
            } else {
                typeClass = getJavaBinding(sdeType);
                if (typeClass == null) {
                    LOGGER.info(
                            "Found an unsupported ArcSDE data type: "
                                    + sdeType
                                    + " for column "
                                    + attName
                                    + ". Ignoring it.");
                    continue;
                }
                // @TODO: add restrictions once the Restrictions utility methods
                // are implemented
                // Set restrictions = Restrictions.createLength(name, typeClass,
                // fieldLen);
            }
            final int rowIdType = colDef.getRowIdType();
            if (rowIdType == SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE) {
                continue; // skip over things we cannot edit modify or otherwise treat as
                // attributes
            }
            AttributeTypeBuilder b = new AttributeTypeBuilder();
            // call setDefaultValue before setBinding
            b.setDefaultValue(defValue);
            b.setBinding(typeClass);
            b.setName(attName);
            b.setNillable(isNilable);
            if (fieldLen > 0) {
                b.setLength(fieldLen);
            }
            // only set CRS if its a geometry type, otherwise
            // AttributeTypeBuilder
            // creates a GeometryAttributeType disregarding the class binding
            if (Geometry.class.isAssignableFrom(typeClass)) {
                b.setCRS(metadata);
            }

            AttributeDescriptor buildDescriptor = b.buildDescriptor(attName);
            attDescriptors.add(buildDescriptor);
        }

        return attDescriptors;
    }

    /**
     * Returns the Java class binding for a given SDE column type.
     *
     * <p>Mappings are:
     *
     * <ul>
     *   <li>{@link SeColumnDefinition#TYPE_BLOB}: byte[].class <b>this one is pending further
     *       development, not supported currently but just ignored</b>
     *   <li>{@link SeColumnDefinition#TYPE_CLOB}: {@link String}.class
     *   <li>{@link SeColumnDefinition#TYPE_DATE}: {@link Date}.class
     *   <li>{@link SeColumnDefinition#TYPE_FLOAT32}: {@link Float}.class
     *   <li>{@link SeColumnDefinition#TYPE_FLOAT64}: {@link Double}.class
     *   <li>{@link SeColumnDefinition#TYPE_INT16}: {@link Short}.class
     *   <li>{@link SeColumnDefinition#TYPE_INT32}: {@link Integer}.class
     *   <li>{@link SeColumnDefinition#TYPE_INT64}: {@link Long}.class
     *   <li>{@link SeColumnDefinition#TYPE_NCLOB}: {@link String}.class
     *   <li>{@link SeColumnDefinition#TYPE_NSTRING}: {@link String}.class
     *   <li>{@link SeColumnDefinition#TYPE_UUID}: {@link String}.class
     * </ul>
     *
     * <p>Currently <b>there're no</b> bindings defined for:
     *
     * <ul>
     *   <li>{@link SeColumnDefinition#TYPE_XML}
     *   <li>{@link SeColumnDefinition#TYPE_RASTER}
     * </ul>
     *
     * <p>To obtain the JTS Geometry class binding for an sde column of type {@link
     * SeColumnDefinition#TYPE_SHAPE} use {@link #getGeometryTypeFromLayerMask(int)}.
     *
     * @return the java binding for the given sde data type or {@code null} if its not supported
     */
    public static Class<?> getJavaBinding(final Integer sdeType) {
        Class<?> javaClass = sde2JavaTypes.get(sdeType);
        return javaClass;
    }

    private static SimpleFeatureType createSchema(
            final String typeName,
            final String namespace,
            final List<AttributeDescriptor> properties)
            throws IOException {

        // TODO: use factory lookup mechanism once its in place
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

        builder.setName(typeName);
        builder.setNamespaceURI(namespace);

        for (Iterator<AttributeDescriptor> it = properties.iterator(); it.hasNext(); ) {
            AttributeDescriptor attType = it.next();
            builder.add(attType);
        }

        return builder.buildFeatureType();
    }

    /**
     * Obtains the <code>SeCoordinateReference</code> of the given <code>SeLayer</code> and tries to
     * create a <code>org.opengis.referencing.crs.CoordinateReferenceSystem</code> from its WKT.
     *
     * @param sdeLayer the SeLayer from which to query the CRS in ArcSDE form.
     * @return the actual CRS or null if <code>sdeLayer</code> does not defines its coordinate
     *     system.
     * @throws DataSourceException if the WKT can't be parsed to an opengis CRS using the CRSFactory
     */
    private static CoordinateReferenceSystem parseCRS(SeLayer sdeLayer) throws DataSourceException {
        CoordinateReferenceSystem crs = null;
        SeCoordinateReference seCRS = sdeLayer.getCoordRef();
        String WKT = seCRS.getProjectionDescription();
        LOGGER.finer("About to parse CRS for layer " + sdeLayer.getName() + ": " + WKT);

        try {
            LOGGER.fine(sdeLayer.getName() + " has CRS envelope: " + seCRS.getXYEnvelope());
        } catch (SeException e1) {
            // intentionally blank
        }

        if ("UNKNOWN".equalsIgnoreCase(WKT)) {
            LOGGER.fine(
                    "ArcSDE layer "
                            + sdeLayer.getName()
                            + " does not provides a Coordinate Reference System");
        } else {
            try {
                CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null);
                crs = crsFactory.createFromWKT(WKT);
                LOGGER.fine("ArcSDE CRS correctly parsed from layer " + sdeLayer.getName());
            } catch (FactoryException e) {
                String msg =
                        "CRS factory does not knows how to parse the "
                                + "CRS for layer "
                                + sdeLayer.getName()
                                + ": "
                                + WKT;
                LOGGER.log(Level.CONFIG, msg, e);
                // throw new DataSourceException(msg, e);
            }
        }

        return crs;
    }

    /**
     * Returns the mapping JTS geometry type for the ArcSDE Shape type given by the bitmask <code>
     * seShapeType</code>
     *
     * <p>This bitmask is composed of a combination of the following shape types, as defined in the
     * ArcSDE Java API:
     *
     * <pre>
     * SE_NIL_TYPE_MASK = 1;
     * SE_POINT_TYPE_MASK = 2;
     * SE_LINE_TYPE_MASK = 4;
     * SE_AREA_TYPE_MASK = 16;
     * SE_MULTIPART_TYPE_MASK = 262144;
     * </pre>
     *
     * (Note that the type SE_SIMPLE_LINE_TYPE_MASK is not used)
     */
    public static Class<? extends Geometry> getGeometryTypeFromLayerMask(int seShapeType) {
        Class<? extends Geometry> clazz;
        final int MULTIPART_MASK = SeLayer.SE_MULTIPART_TYPE_MASK;
        final int POINT_MASK = SeLayer.SE_POINT_TYPE_MASK;
        final int SIMPLE_LINE_MASK = SeLayer.SE_SIMPLE_LINE_TYPE_MASK;
        final int LINESTRING_MASK = SeLayer.SE_LINE_TYPE_MASK;
        final int AREA_MASK = SeLayer.SE_AREA_TYPE_MASK;

        // if (seShapeType == SeLayer.TYPE_NIL) {
        // // do nothing
        // } else if (seShapeType == SeLayer.TYPE_MULTI_MASK) {
        // clazz = GeometryCollection.class;
        // } else if (seShapeType == SeLayer.TYPE_LINE || seShapeType ==
        // SeLayer.TYPE_SIMPLE_LINE) {
        // clazz = LineString.class;
        // } else if (seShapeType == SeLayer.TYPE_MULTI_LINE
        // || seShapeType == SeLayer.TYPE_MULTI_SIMPLE_LINE) {
        // clazz = MultiLineString.class;
        // } else if (seShapeType == SeLayer.TYPE_MULTI_POINT) {
        // clazz = MultiPoint.class;
        // } else if (seShapeType == SeLayer.TYPE_MULTI_POLYGON) {
        // clazz = MultiPolygon.class;
        // } else if (seShapeType == SeLayer.TYPE_POINT) {
        // clazz = Point.class;
        // } else if (seShapeType == SeLayer.TYPE_POLYGON) {
        // clazz = Polygon.class;
        // } else {
        // in all this assignments, 1 means true and 0 false
        final int isCollection = ((seShapeType & MULTIPART_MASK) == MULTIPART_MASK) ? 1 : 0;

        final int isPoint = ((seShapeType & POINT_MASK) == POINT_MASK) ? 1 : 0;

        final int isLineString =
                (((seShapeType & SIMPLE_LINE_MASK) == SIMPLE_LINE_MASK)
                                || ((seShapeType & LINESTRING_MASK) == LINESTRING_MASK))
                        ? 1
                        : 0;

        final int isPolygon = ((seShapeType & AREA_MASK) == AREA_MASK) ? 1 : 0;

        // first check if the shape type supports more than one geometry
        // type.
        // In that case, it is *highly* recomended that it support all the
        // geometry types, so we can safely return Geometry.class. If this
        // is not the case and the shape type supports just a few geometry types,
        // then we give it a chance and return Geometry.class anyway, but be
        // aware that transactions over that layer could fail if a Geometry that
        // is not supported is tried for insertion.
        if ((isPoint + isLineString + isPolygon) > 1) {
            clazz = Geometry.class;

            if (4 < (isCollection + isPoint + isLineString + isPolygon)) {
                LOGGER.warning(
                        "Be careful!! we're mapping an ArcSDE Shape type "
                                + "to the generic Geometry class, but the shape type "
                                + "does not really allows all geometry types!: "
                                + "isCollection="
                                + isCollection
                                + ", isPoint="
                                + isPoint
                                + ", isLineString="
                                + isLineString
                                + ", isPolygon="
                                + isPolygon);
            } else {
                LOGGER.fine("safely mapping SeShapeType to abstract Geometry");
            }
        } else if (isCollection == 1) {
            if (isPoint == 1) {
                clazz = MultiPoint.class;
            } else if (isLineString == 1) {
                clazz = MultiLineString.class;
            } else if (isPolygon == 1) {
                clazz = MultiPolygon.class;
            } else {
                throw new IllegalStateException("this shouldn't happen!");
            }
        } else {
            if (isPoint == 1) {
                clazz = Point.class;
            } else if (isLineString == 1) {
                clazz = LineString.class;
            } else if (isPolygon == 1) {
                clazz = Polygon.class;
            } else {
                throw new IllegalStateException("this shouldn't happen!");
            }
        }

        // }
        return clazz;
    }

    /**
     * Returns the most appropriate {@link Geometry} class that matches the shape's type.
     *
     * @param shape SeShape instance for which to infer the matching geometry class, can't be null
     * @return the Geometry subclass corresponding to the shape type
     * @throws SeException propagated if thrown by {@link SeShape#getType()}
     * @throws IllegalArgumentException if none of the JTS geometry classes can be matched to the
     *     shape type (shouldnt happen as for the {@link SeShape#getType() types} defined in the
     *     esri arcsde java api 9.0)
     */
    public static Class<? extends Geometry> getGeometryTypeFromSeShape(SeShape shape)
            throws SeException {
        final Class<? extends Geometry> clazz;

        final int seShapeType = shape.getType();

        if (seShapeType == SeShape.TYPE_LINE || seShapeType == SeShape.TYPE_SIMPLE_LINE) {
            clazz = LineString.class;
        } else if (seShapeType == SeShape.TYPE_MULTI_LINE
                || seShapeType == SeShape.TYPE_MULTI_SIMPLE_LINE) {
            clazz = MultiLineString.class;
        } else if (seShapeType == SeShape.TYPE_MULTI_POINT) {
            clazz = MultiPoint.class;
        } else if (seShapeType == SeShape.TYPE_MULTI_POLYGON) {
            clazz = MultiPolygon.class;
        } else if (seShapeType == SeShape.TYPE_POINT) {
            clazz = Point.class;
        } else if (seShapeType == SeShape.TYPE_POLYGON) {
            clazz = Polygon.class;
        } else {
            throw new IllegalArgumentException(
                    "Cannot map the shape type '" + seShapeType + "' to any known SeShape.TYPE_*");
        }
        return clazz;
    }

    /**
     * Returns the numeric identifier of a FeatureId, given by the full qualified name of the
     * featureclass prepended to the ArcSDE feature id. ej: SDE.SDE.SOME_LAYER.1
     *
     * @param id a geotools FeatureID
     * @return an ArcSDE feature ID
     * @throws IllegalArgumentException If the given string is not properly formatted
     *     [anystring].[long value]
     */
    public static long getNumericFid(Identifier id) throws IllegalArgumentException {
        if (!(id instanceof FeatureId))
            throw new IllegalArgumentException(
                    "Only FeatureIds are supported when encoding id filters to SDE.  Not "
                            + id.getClass());

        final String fid = ((FeatureId) id).getID();
        return getNumericFid(fid);
    }

    /**
     * Returns the numeric identifier of a FeatureId, given by the full qualified name of the
     * featureclass prepended to the ArcSDE feature id. ej: SDE.SDE.SOME_LAYER.1
     *
     * @param fid a geotools FeatureID
     * @return an ArcSDE feature ID
     * @throws IllegalArgumentException If the given string is not properly formatted
     *     [anystring].[long value]
     */
    public static long getNumericFid(String fid) throws IllegalArgumentException {
        int dotIndex = fid.lastIndexOf('.');
        try {
            return Long.decode(fid.substring(++dotIndex)).longValue();
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "FeatureID " + fid + " does not seems as a valid ArcSDE FID");
        }
    }

    public static long[] getNumericFids(Set<Identifier> identifiers)
            throws IllegalArgumentException {
        int nfids = identifiers.size();
        long[] fids = new long[nfids];

        Iterator<Identifier> ids = identifiers.iterator();
        for (int i = 0; i < nfids; i++) {
            fids[i] = ArcSDEAdapter.getNumericFid(ids.next());
        }

        return fids;
    }

    /**
     * Holds default values for the properties (size and scale) of a SeColumnDefinition, given by
     * its column type (SeColumnDefinition.SE_STRING, etc).
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Revision: 1.4 $
     */
    private static class SdeTypeDef {
        /**
         * A magic number provided by SeColumnDefinition (example SeColumnDefinition.TYPE_STRING)
         */
        final int colDefType;

        /** size (probably field size?) */
        final int size;

        /** scale */
        final int scale;

        /**
         * Creates a new SdeTypeDef object.
         *
         * @param colDefType Constant provided by SeColumnDefinition
         * @param size Field size
         * @param scale Field scale
         */
        public SdeTypeDef(int colDefType, int size, int scale) {
            this.colDefType = colDefType;
            this.size = size;
            this.scale = scale;
        }

        /**
         * Text representation
         *
         * @return Text represenation for debugging
         */
        @Override
        public String toString() {
            return "SdeTypeDef[colDefType="
                    + this.colDefType
                    + ", size="
                    + this.size
                    + ", scale="
                    + this.scale
                    + "]";
        }
    }

    /**
     * Creates the given featuretype in the underlying ArcSDE database.
     * <p>
     * The common use case to create an ArcSDE layer is to setup the SeTable object with all the
     * non-geometry attributes first, then create the SeLayer and set the geometry column name and
     * its properties. This approach brings a nice problem, since we need to create the attributes
     * in exactly the same order as specified in the passed FeatureType, which means that the
     * geometry attribute needs not to be the last one.
     * </p>
     * <p>
     * To avoid this, the following workaround is performed: instead of creating the schema as
     * described above, we will first create the SeTable with a single, temporary column, since it
     * is not possible to create a table without columns. The, we will iterate over the
     * AttributeTypes and add them as they appear using
     * <code>SeTable.addColumn(SeColumnDefinition)</code>. But if we found that the current
     * AttributeType is geometric, instead of adding the column we just create the SeLayer object.
     * This way, the geometric attribute is inserted at the end, and then we keep iterating and
     * adding the rest of the columns. Finally, the first column is removed, since it was temporary
     * (note that I advertise it, it is a _workaround_).
     * </p>
     * <p>
     * Sometimes some 'extra' information is required to correctly create the underlying ArcSDE
     * SeLayer. For instance, a specific configuration keyword might be required to be used (instead
     * of "DEFAULTS"), or a particular column might need to be marked as the rowid column for the
     * featuretype. A non-null <code>hints</code> parameter contains a mapping from a list of
     * well-known {@link java.lang.String} keys to values. The possible keys are listed in the table
     * below. keys with any other values are ignored.
     * <table>
     * <tr>
     * <td>key name</td>
     * <td>key value type</td>
     * <td>default value (if applicable)</td>
     * </tr>
     * <tr>
     * <td>configuration.keyword</td>
     * <td>{@link java.lang.String}</td>
     * <td>"DEFAULTS"</td>
     * </tr>
     * <tr>
     * <td>rowid.column.type</td>
     * <td>{@link java.lang.String} - "NONE", "USER" and "SDE" are the only valid values</td>
     * <td>"NONE"</td>
     * </tr>
     * <tr>
     * <td>rowid.column.name</td>
     * <td>{@link java.lang.String}</td>
     * <td>null</td>
     * </tr>
     * </p>
     *
     *            the feature type containing the name, attributes and coordinate reference system
     *            of the new ArcSDE layer.
     *            A map containing extra ArcSDE-specific hints about how to create the underlying
     *            ArcS DE SeLayer and SeTable objects from this FeatureType.
     *            connection to use in order to create the layer and table on the server. The
     *            connection shall be managed by this method caller.
     *             see <code>throws DataSourceException</code> bellow
     *             if the passed feature type does not contains at least one geometric attribute, or
     *             if the type name contains '.' (dots).
     *             if <code>featureType</code> is <code>null</code>
     *             if there is <b>not an available (free) connection </b> to the ArcSDE instance(in
     *             that case maybe you need to increase the maximun number of connections for the
     *             connection pool), or an SeException exception is catched while creating the
     *             feature type at the ArcSDE instance (e.g. a table with that name already exists).
     */
    public static void createSchema(
            final SimpleFeatureType featureType,
            final Map<String, String> hints,
            final ISession session)
            throws IOException, IllegalArgumentException {

        if (featureType == null) {
            throw new NullPointerException("You have to provide a FeatureType instance");
        }

        final Command<Void> createSchemaCmd =
                new Command<Void>() {
                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {

                        final String[] typeNameParts = featureType.getTypeName().split("\\.");
                        final String unqualifiedTypeName = typeNameParts[typeNameParts.length - 1];

                        // Create a new SeTable/SeLayer with the specified attributes....
                        SeTable table = null;
                        SeLayer layer = null;

                        // flag to know if the table was created by us when catching an
                        // exception.

                        // table/layer creation hints information
                        int rowIdType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE;
                        String rowIdColumn = null;
                        String configKeyword = "DEFAULTS";
                        if (hints.containsKey("configuration.keyword")) {
                            configKeyword = String.valueOf(hints.get("configuration.keyword"));
                        }
                        if (hints.get("rowid.column.type") instanceof String) {
                            String rowIdStr = (String) hints.get("rowid.column.type");
                            if (rowIdStr.equalsIgnoreCase("NONE")) {
                                rowIdType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE;
                            } else if (rowIdStr.equalsIgnoreCase("USER")) {
                                rowIdType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_USER;
                            } else if (rowIdStr.equalsIgnoreCase("SDE")) {
                                rowIdType = SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE;
                            } else {
                                throw new DataSourceException(
                                        "createSchema hint 'rowid.column.type' must be one of 'NONE', 'USER' or 'SDE'");
                            }
                        }
                        if (hints.get("rowid.column.name") instanceof String) {
                            rowIdColumn = (String) hints.get("rowid.column.name");
                        }

                        try {
                            // create a table with provided username
                            String qualifiedName = null;

                            if (unqualifiedTypeName.indexOf('.') == -1) {
                                // Use the already parsed name (unqualifiedTypeName)
                                qualifiedName =
                                        connection.getUser()
                                                + "."
                                                + unqualifiedTypeName; // featureType.getTypeName();
                                LOGGER.finer("new full qualified type name: " + qualifiedName);
                            } else {
                                qualifiedName = unqualifiedTypeName;
                                LOGGER.finer(
                                        "full qualified type name provided by user: "
                                                + qualifiedName);
                            }

                            layer = new SeLayer(connection);
                            layer.setTableName(qualifiedName);
                            layer.setCreationKeyword(configKeyword);

                            final String HACK_COL_NAME = "gt_workaround_col_";

                            table =
                                    createSeTable(
                                            connection,
                                            qualifiedName,
                                            HACK_COL_NAME,
                                            configKeyword);

                            final List<AttributeDescriptor> atts =
                                    featureType.getAttributeDescriptors();
                            AttributeDescriptor currAtt;

                            for (Iterator<AttributeDescriptor> it = atts.iterator();
                                    it.hasNext(); ) {
                                currAtt = it.next();

                                if (currAtt instanceof GeometryDescriptor) {
                                    GeometryDescriptor geometryAtt = (GeometryDescriptor) currAtt;
                                    createSeLayer(layer, qualifiedName, geometryAtt);
                                } else {
                                    LOGGER.fine("Creating column definition for " + currAtt);

                                    SeColumnDefinition newCol =
                                            ArcSDEAdapter.createSeColumnDefinition(currAtt);

                                    // /////////////////////////////////////////////////////////////
                                    // HACK!!!!: this hack is just to avoid the error that
                                    // occurs //
                                    // when adding a column wich is not nillable. Need to fix
                                    // this//
                                    // but by now it conflicts with the requirement of creating
                                    // //
                                    // the schema with the correct attribute order. //
                                    // /////////////////////////////////////////////////////////////
                                    newCol =
                                            new SeColumnDefinition(
                                                    newCol.getName(),
                                                    newCol.getType(),
                                                    newCol.getSize(),
                                                    newCol.getScale(),
                                                    true);

                                    // /////////////////////////////////////////////////////////////
                                    // END of horrible HACK //
                                    // /////////////////////////////////////////////////////////////
                                    LOGGER.fine(
                                            "Adding column "
                                                    + newCol.getName()
                                                    + " to the actual table.");
                                    table.addColumn(newCol);
                                }
                            }

                            LOGGER.fine("deleting the 'workaround' column...");
                            table.dropColumn(HACK_COL_NAME);

                            LOGGER.fine("setting up table registration with ArcSDE...");
                            SeRegistration reg = new SeRegistration(connection, table.getName());
                            if (rowIdColumn != null) {
                                LOGGER.fine(
                                        "setting rowIdColumnName to "
                                                + rowIdColumn
                                                + " in table "
                                                + reg.getTableName());
                                reg.setRowIdColumnName(rowIdColumn);
                                reg.setRowIdColumnType(rowIdType);
                                reg.alter();
                                reg = null;
                            }

                            LOGGER.fine("Schema correctly created: " + featureType);

                        } catch (SeException e) {
                            LOGGER.log(Level.WARNING, e.getSeError().getErrDesc(), e);
                            throw e;
                        }
                        // TODO: remove table if created and then failed
                        //                        } finally {
                        //                            if ((error != null) && tableCreated) {
                        //
                        //                            }
                        //                        }
                        return null;
                    }
                };

        session.issue(createSchemaCmd);
    }

    /**
     * Creates a new table in the server. Warning warning, this method shall only be called from
     * inside a {@link Command}
     */
    private static SeTable createSeTable(
            final SeConnection connection,
            final String qualifiedName,
            final String hackColName,
            final String configKeyword)
            throws SeException {

        final SeTable table;
        final SeColumnDefinition[] tmpCol = new SeColumnDefinition[1];
        tmpCol[0] = new SeColumnDefinition(hackColName, SeColumnDefinition.TYPE_STRING, 4, 0, true);

        table = new SeTable(connection, qualifiedName);

        LOGGER.info("creating table " + qualifiedName);

        // create the table using DBMS default configuration keyword.
        // valid keywords are defined in the dbtune table.
        table.create(tmpCol, configKeyword);
        LOGGER.info("table " + qualifiedName + " created...");

        return table;
    }

    private static void createSeLayer(
            SeLayer layer, String qualifiedName, GeometryDescriptor geometryAtt)
            throws SeException {
        String spatialColName = geometryAtt.getLocalName();
        LOGGER.info("setting spatial column name: " + spatialColName);
        layer.setSpatialColumnName(spatialColName);

        // Set the shape types that can be inserted into this layer
        int seShapeTypes = ArcSDEAdapter.guessShapeTypes(geometryAtt);
        layer.setShapeTypes(seShapeTypes);
        layer.setGridSizes(1100.0, 0.0, 0.0);
        layer.setDescription("Created with GeoTools");

        // Define the layer's Coordinate Reference
        CoordinateReferenceSystem crs = geometryAtt.getCoordinateReferenceSystem();
        SeCoordinateReference coordref = getGenericCoordRef();
        String WKT = null;

        if (crs == null) {
            LOGGER.warning(
                    "Creating feature type "
                            + qualifiedName
                            + ": the geometry attribute does not supply a coordinate reference system");
        } else {
            LOGGER.info("Creating the SeCoordRef object for CRS " + crs);
            WKT = crs.toWKT();
            coordref.setCoordSysByDescription(WKT);
        }

        SeExtent validCoordRange = null;

        if ((WKT != null) && (WKT.indexOf("GEOGCS") != -1)) {
            validCoordRange = new SeExtent(-180, -90, 180, 90);
        } else {
            validCoordRange = coordref.getXYEnvelope();
        }

        layer.setExtent(validCoordRange);

        LOGGER.info("Applying CRS " + coordref.getCoordSysDescription());
        layer.setCoordRef(coordref);
        LOGGER.info("CRS applyed to the new layer.");

        // /////////////////////////
        // this param is used by ArcSDE for database initialization purposes
        int estInitFeatCount = 4;

        // this param is used by ArcSDE as an estimation of the average number
        // of points the layer's geometries will have, one never will know what
        // for
        int estAvgPointsPerFeature = 4;
        LOGGER.info("Creating the layer...");
        layer.create(estInitFeatCount, estAvgPointsPerFeature);
        LOGGER.info("ArcSDE layer created.");
    }

    /**
     * Creates and returns a <code>SeCoordinateReference</code> CRS, though based on an UNKNOWN CRS,
     * is inclusive enough (in terms of valid coordinate range and presicion) to deal with most
     * coordintates.
     *
     * <p>Actually tested to deal with coordinates with 0.0002 units of separation as well as with
     * large coordinates such as UTM (values greater than 500,000.00)
     *
     * <p>This method is driven by the equally named method in TestData.java
     */
    private static SeCoordinateReference getGenericCoordRef() throws SeException {
        // create a sde CRS with a huge value range and 5 digits of presission
        SeCoordinateReference seCRS = new SeCoordinateReference();
        int shift = 600000;
        SeExtent validRange = new SeExtent(-shift, -shift, shift, shift);
        seCRS.setXYByEnvelope(validRange);
        LOGGER.info("CRS: " + seCRS.getXYEnvelope());

        return seCRS;
    }
}
