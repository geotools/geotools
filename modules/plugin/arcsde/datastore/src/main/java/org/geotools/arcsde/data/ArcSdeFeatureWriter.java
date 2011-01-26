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
package org.geotools.arcsde.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.hsqldb.Session;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.BoundingBox;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeDelete;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeInsert;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeObjectId;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeStreamOp;
import com.esri.sde.sdk.client.SeTable;
import com.esri.sde.sdk.client.SeTable.SeTableIdRange;
import com.esri.sde.sdk.client.SeUpdate;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.operation.valid.IsValidOp;
import com.vividsolutions.jts.operation.valid.TopologyValidationError;

abstract class ArcSdeFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {

    protected static final Logger LOGGER = Logging.getLogger(ArcSdeFeatureWriter.class.getName());

    /**
     * Fid prefix used for just created and not yet committed features
     */
    private static final String NEW_FID_PREFIX = "@NEW_";

    /**
     * Complete feature type this writer acts upon
     */
    protected final SimpleFeatureType featureType;

    /**
     * Connection to hold while this feature writer is alive.
     */
    protected ISession session;

    /**
     * Reader for streamed access to filtered content this writer acts upon.
     */
    protected FeatureReader<SimpleFeatureType, SimpleFeature> filteredContent;

    /**
     * Builder for new Features this writer creates when next() is called and hasNext() == false
     */
    protected final SimpleFeatureBuilder featureBuilder;

    /**
     * Map of {row index/mutable column names} in the SeTable structure. Not to be accessed
     * directly, but through {@link #getMutableColumnNames(Session)}
     */
    private LinkedHashMap<Integer, String> mutableColumnNames;

    private LinkedHashMap<Integer, String> insertableColumnNames;

    /**
     * Not to be accessed directly, but through {@link #getLayer()}
     */
    private SeLayer cachedLayer;

    /**
     * Not to be accessed directly, but through {@link #getTable()}
     */
    private SeTable cachedTable;

    /**
     * The feature at the current index. No need to maintain any sort of collection of features as
     * this writer works a feature at a time.
     */
    protected SimpleFeature feature;

    /**
     * Provides row_id column index
     */
    protected final FIDReader fidReader;

    protected final FeatureListenerManager listenerManager;

    /**
     * version handler to delegate setting up and handling database version states
     */
    private final ArcSdeVersionHandler versionHandler;

    public ArcSdeFeatureWriter(final FIDReader fidReader, final SimpleFeatureType featureType,
            final FeatureReader<SimpleFeatureType, SimpleFeature> filteredContent,
            final ISession session, final FeatureListenerManager listenerManager,
            final ArcSdeVersionHandler versionHandler) throws IOException {

        assert fidReader != null;
        assert featureType != null;
        assert filteredContent != null;
        assert session != null;
        assert listenerManager != null;
        assert versionHandler != null;

        if (!(fidReader instanceof FIDReader.SdeManagedFidReader || fidReader instanceof FIDReader.UserManagedFidReader)) {
            throw new DataSourceException("fid reader is not user nor sde managed: " + fidReader);
        }

        this.fidReader = fidReader;
        this.featureType = featureType;
        this.filteredContent = filteredContent;
        this.session = session;
        this.listenerManager = listenerManager;
        this.featureBuilder = new SimpleFeatureBuilder(featureType);
        this.versionHandler = versionHandler;
    }

    /**
     * Creates the type of arcsde stream operation specified by the {@code streamType} class and,if
     * the working layer is of a versioned table, sets up the stream to being editing the default
     * database version.
     * 
     * @param streamType
     * @return
     * @throws IOException
     */
    private SeStreamOp createStream(Class<? extends SeStreamOp> streamType) throws IOException {
        SeStreamOp streamOp;

        if (SeInsert.class == streamType) {
            streamOp = session.createSeInsert();
        } else if (SeUpdate.class == streamType) {
            streamOp = session.createSeUpdate();
        } else if (SeDelete.class == streamType) {
            streamOp = session.createSeDelete();
        } else {
            throw new IllegalArgumentException("Unrecognized stream type: " + streamType);
        }

        versionHandler.setUpStream(session, streamOp);

        return streamOp;
    }

    /**
     * @see FeatureWriter#close()
     */
    public void close() throws IOException {

        if (filteredContent != null) {
            filteredContent.close();
            filteredContent = null;
        }

        // let repeatedly calling close() be inoffensive
        if (session != null && !session.isDisposed()) {
            session.dispose();
        }
        session = null;
    }

    /**
     * @see FeatureWriter#getFeatureType()
     */
    public final SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /**
     * @see FeatureWriter#hasNext()
     */
    public final boolean hasNext() throws IOException {
        // filteredContent may be null because we
        // took the precaution of closing it in a previous call
        // to this method
        final boolean hasNext = filteredContent != null && filteredContent.hasNext();
        // be cautious of badly coded clients
        if (!hasNext && filteredContent != null) {
            filteredContent.close();
            filteredContent = null;
        }
        return hasNext;
    }

    /**
     * @see FeatureWriter#next()
     */
    public final SimpleFeature next() throws IOException {
        if (hasNext()) {
            feature = filteredContent.next();
        } else {
            final String newFid = newFid();
            final SimpleFeature newFeature = featureBuilder.buildFeature(newFid);
            final List<Object> properties = newFeature.getAttributes();
            feature = new MutableFIDFeature(properties, featureType, newFid);
        }
        return feature;
    }

    /**
     * @see FeatureWriter#remove()
     */
    public void remove() throws IOException {
        if (isNewlyCreated(feature)) {
            // we're in auto commit, no need to remove anything
            return;
        }
        // deletes are executed immediately. We set up a transaction
        // if in autocommit mode to be committed or rolled back on this same
        // method if something happens bellow.
        final boolean handleTransaction = !session.isTransactionActive();
        if (handleTransaction) {
            session.startTransaction();
        }

        final String id = feature.getID();
        final long featureId = ArcSDEAdapter.getNumericFid(id);
        final SeObjectId objectID = new SeObjectId(featureId);
        final String qualifiedName = featureType.getTypeName();

        final SeDelete seDelete = (SeDelete) createStream(SeDelete.class);

        final Command<Void> deleteCmd = new Command<Void>() {
            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                try {
                    // A call to SeDelete.byId immediately deletes the row from the
                    // database. The application does not need to call execute()
                    // try{
                    seDelete.byId(qualifiedName, objectID);
                    // }catch(SeException e){
                    // final int FID_DOESNT_EXIST = -22;
                    // if(e.getSeError().getSdeError() == FID_DOESNT_EXIST){
                    // //ignore
                    // }else{
                    // throw e;
                    // }
                    // }
                    versionHandler.editOperationWritten(seDelete);
                    if (handleTransaction) {
                        session.commitTransaction();
                    }
                } catch (IOException e) {
                    if (handleTransaction) {
                        try {
                            session.rollbackTransaction();
                        } catch (IOException e1) {
                            LOGGER.log(Level.SEVERE, "Unrecoverable error rolling "
                                    + "back delete transaction", e);
                        }
                    }
                    throw new DataSourceException("Error deleting feature with id:" + featureId, e);
                } finally {
                    if (seDelete != null) {
                        try {
                            seDelete.close();
                        } catch (SeException e) {
                            LOGGER.log(Level.SEVERE,
                                    "Unrecoverable error rolling back delete transaction", e);
                        }
                    }
                }
                return null;
            }
        };

        try {
            session.issue(deleteCmd);
            fireRemoved(feature);
        } catch (IOException e) {
            versionHandler.editOperationFailed(seDelete);
            throw e;
        }

    }

    private void fireAdded(final SimpleFeature addedFeature) {
        final String typeName = featureType.getTypeName();
        final BoundingBox bounds = addedFeature.getBounds();
        ReferencedEnvelope referencedEnvelope = ReferencedEnvelope.reference(bounds);
        String fid = addedFeature.getID();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.id(Collections.singleton(ff.featureId(fid)));
        doFireFeaturesAdded(typeName, referencedEnvelope, filter);
    }

    private void fireChanged(final SimpleFeature changedFeature) {
        final String typeName = featureType.getTypeName();
        final BoundingBox bounds = changedFeature.getBounds();
        ReferencedEnvelope referencedEnvelope = ReferencedEnvelope.reference(bounds);
        String fid = changedFeature.getID();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.id(Collections.singleton(ff.featureId(fid)));

        doFireFeaturesChanged(typeName, referencedEnvelope, filter);
    }

    private void fireRemoved(final SimpleFeature removedFeature) {
        String typeName = featureType.getTypeName();
        BoundingBox bounds = removedFeature.getBounds();
        ReferencedEnvelope referencedEnvelope = ReferencedEnvelope.reference(bounds);
        String fid = removedFeature.getID();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        Filter filter = ff.id(Collections.singleton(ff.featureId(fid)));
        doFireFeaturesRemoved(typeName, referencedEnvelope, filter);
    }

    protected abstract void doFireFeaturesAdded(String typeName, ReferencedEnvelope bounds,
            Filter filter);

    protected abstract void doFireFeaturesChanged(String typeName, ReferencedEnvelope bounds,
            Filter filter);

    protected abstract void doFireFeaturesRemoved(String typeName, ReferencedEnvelope bounds,
            Filter filter);

    /**
     * @see FeatureWriter#write()
     */
    public void write() throws IOException {

        // make the feature validate against its schema before inserting/updating
        feature.validate();

        if (isNewlyCreated(feature)) {
            Number newId;
            try {
                newId = insertSeRow(feature);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error inserting " + feature + ": " + e.getMessage(), e);
                throw e;
            }
            MutableFIDFeature mutableFidFeature = (MutableFIDFeature) feature;
            String id = featureType.getTypeName() + "." + newId.longValue();
            mutableFidFeature.setID(id);
            fireAdded(mutableFidFeature);
        } else {
            try {
                updateRow(feature);
                fireChanged(feature);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error updating " + feature + ": " + e.getMessage(), e);
                throw e;
            }
        }
    }

    /**
     * Updates the contents of a Feature in the database.
     * <p>
     * The db row to modify is obtained from the feature id.
     * </p>
     * 
     * @param modifiedFeature
     *            the newly create Feature to insert.
     * @param session
     *            the connection to use for the insert operation. Its auto commit mode determines
     *            whether the operation takes effect immediately or not.
     * @throws IOException
     * @throws SeException
     *             if thrown by any sde stream method
     * @throws IOException
     */
    private void updateRow(final SimpleFeature modifiedFeature) throws IOException {

        final SeLayer layer = getLayer();
        final SeCoordinateReference seCoordRef = layer == null ? null : layer.getCoordRef();

        final SeUpdate updateStream = (SeUpdate) createStream(SeUpdate.class);
        // updateStream.setWriteMode(true);

        final LinkedHashMap<Integer, String> mutableColumns = getUpdatableColumnNames();
        final String[] rowColumnNames = new ArrayList<String>(mutableColumns.values())
                .toArray(new String[0]);
        final String typeName = featureType.getTypeName();
        final String fid = modifiedFeature.getID();
        final long numericFid = ArcSDEAdapter.getNumericFid(fid);
        final SeObjectId seObjectId = new SeObjectId(numericFid);

        final Command<Void> updateCmd = new Command<Void>() {
            @Override
            public Void execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                try {
                    final SeRow row = updateStream.singleRow(seObjectId, typeName, rowColumnNames);

                    setRowProperties(modifiedFeature, seCoordRef, mutableColumns, row);
                    updateStream.execute();
                    // updateStream.flushBufferedWrites();
                } finally {
                    updateStream.close();
                }
                return null;
            }
        };

        try {
            session.issue(updateCmd);
            versionHandler.editOperationWritten(updateStream);
        } catch (NoSuchElementException e) {
            versionHandler.editOperationFailed(updateStream);
            throw e;
        } catch (IOException e) {
            versionHandler.editOperationFailed(updateStream);
            throw e;
        }
    }

    /**
     * Inserts a feature into an SeLayer.
     * 
     * @param newFeature
     *            the newly create Feature to insert.
     * @param session
     *            the connection to use for the insert operation. Its auto commit mode determines
     *            whether the operation takes effect immediately or not.
     * @throws IOException
     */
    private Number insertSeRow(final SimpleFeature newFeature) throws IOException {

        // final SeTable table = getTable();
        final SeLayer layer = getLayer();
        final SeCoordinateReference seCoordRef = layer == null ? null : layer.getCoordRef();

        // this returns only the mutable attributes
        final LinkedHashMap<Integer, String> insertColumns = getInsertableColumnNames();

        final Command<Number> insertCmd = new Command<Number>() {

            @Override
            public Number execute(ISession session, SeConnection connection) throws SeException,
                    IOException {

                final SeInsert insertStream = (SeInsert) createStream(SeInsert.class);
                Number newId = null;

                try {
                    final SeRow row;

                    // ensure we get the next sequence id when the fid is user managed
                    // and include it in the attributes to set
                    if (fidReader instanceof FIDReader.UserManagedFidReader) {
                        newId = getNextAvailableUserManagedId();
                        if (newId == null) {
                            LOGGER.finest("There seems not to be a sequence"
                                    + " for the table, not setting a generated id, "
                                    + "user ought to be taking care of it");
                            newId = (Number) newFeature.getAttribute(fidReader.getColumnIndex());
                        } else {
                            final int rowIdIndex = fidReader.getColumnIndex();
                            newFeature.setAttribute(rowIdIndex, newId);
                        }
                    }
                    String[] rowColumnNames = new ArrayList<String>(insertColumns.values())
                            .toArray(new String[0]);
                    String typeName = featureType.getTypeName();
                    insertStream.intoTable(typeName, rowColumnNames);
                    insertStream.setWriteMode(true);
                    row = insertStream.getRowToSet();

                    setRowProperties(newFeature, seCoordRef, insertColumns, row);
                    insertStream.execute();

                    if (fidReader instanceof FIDReader.SdeManagedFidReader) {
                        SeObjectId newRowId = insertStream.lastInsertedRowId();
                        newId = Long.valueOf(newRowId.longValue());
                    }

                    insertStream.flushBufferedWrites(); // jg: my customer wanted this uncommented
                    versionHandler.editOperationWritten(insertStream);
                } catch (Exception e) {
                    versionHandler.editOperationFailed(insertStream);
                    if (e instanceof SeException) {
                        throw (SeException) e;
                    } else if (e instanceof IOException) {
                        throw (IOException) e;
                    }
                    throw new DataSourceException(e);
                }
                insertStream.close();
                return newId;
            }
        };

        final Number newId;

        try {
            newId = session.issue(insertCmd);
        } catch (IOException e) {
            throw e;
        }

        // TODO: handle SHAPE fid strategy (actually such a table shouldn't be
        // editable)
        return newId;
    }

    /**
     * Sets the SeRow property values by index, taking the index from the mutableColumns keys and
     * the values from <code>feature</code>, using the mutableColumns values to get the feature
     * properties by name.
     * <p>
     * This method is intended to be called from inside a
     * {@link Command#execute(Session, SeConnection)} method
     * </p>
     * 
     * @param feature
     *            the Feature where to get the property values from
     * @param seCoordRef
     * @param mutableColumns
     * @param row
     * @throws SeException
     * @throws IOException
     */
    private static void setRowProperties(final SimpleFeature feature,
            final SeCoordinateReference seCoordRef, Map<Integer, String> mutableColumns,
            final SeRow row) throws SeException, IOException {

        // Now set the values for the new row here...
        int seRowIndex;
        String attName;
        Object value;
        for (Map.Entry<Integer, String> entry : mutableColumns.entrySet()) {
            seRowIndex = entry.getKey().intValue();
            attName = entry.getValue();
            value = feature.getAttribute(attName);
            setRowValue(row, seRowIndex, value, seCoordRef, attName);
        }
    }

    /**
     * Called when the layer row id is user managed to ask ArcSDE for the next available ID.
     * 
     * @return
     * @throws IOException
     * @throws SeException
     * @return a new available id if possible, {@code null} if thre seems not to be a sequence for
     *         the table
     */
    private Number getNextAvailableUserManagedId() throws IOException, SeException {

        // TODO: refactor, this is expensive to do for each row to insert
        // TODO: refactor to some sort of strategy object like done for
        // FIDReader
        final SeLayer layer = getLayer();
        final SeTable table = getTable();
        // ArcSDE JavaDoc only says: "Returns a range of row id values"
        // http://edndoc.esri.com/arcsde/9.1/java_api/docs/com/esri/sde/sdk/client/setable.html#getIds
        // (int)
        /*
         * I've checked empirically it is to return a range of available ids. And also found it
         * works for layers but not for registered tables with non spatial layer.. sigh..
         */
        final SeTableIdRange ids = layer == null ? null : table.getIds(1);
        if (ids == null) {
            return null;
        }
        final SeObjectId startId = ids.getStartId();
        final long id = startId.longValue();
        final Long newId = Long.valueOf(id);

        final AttributeDescriptor rowIdAtt = featureType.getDescriptor(fidReader.getFidColumn());
        final Class<?> binding = rowIdAtt.getType().getBinding();
        final Number userFidValue;
        if (Long.class == binding) {
            userFidValue = newId;
        } else if (Integer.class == binding) {
            userFidValue = Integer.valueOf(newId.intValue());
        } else if (Double.class == binding) {
            userFidValue = new Double(newId.doubleValue());
        } else if (Float.class == binding) {
            userFidValue = new Float(newId.floatValue());
        } else {
            throw new IllegalArgumentException("Can't handle a user managed row id of type "
                    + binding);
        }

        return userFidValue;
    }

    /**
     * Used to set a value on an SeRow object. The values is converted to the appropriate type based
     * on an inspection of the SeColumnDefintion object.
     * <p>
     * This method is intended to be called from inside a
     * {@link Command#execute(Session, SeConnection)} method
     * </p>
     * 
     * @param row
     * @param index
     * @param convertedValue
     * @param coordRef
     * @param attName
     *            for feedback purposes only in case of failure
     * @throws IOException
     *             if failed to set the row value
     */
    private static void setRowValue(final SeRow row, final int index, final Object value,
            final SeCoordinateReference coordRef, final String attName) throws IOException {

        try {
            final SeColumnDefinition seColumnDefinition = row.getColumnDef(index);

            final int colType = seColumnDefinition.getType();

            // the actual value to be set, converted to the appropriate type where
            // needed
            Object convertedValue = value;
            if (colType == SeColumnDefinition.TYPE_INT16) {
                convertedValue = Converters.convert(convertedValue, Short.class);
                row.setShort(index, (Short) convertedValue);
            } else if (colType == SeColumnDefinition.TYPE_INT32) {
                convertedValue = Converters.convert(convertedValue, Integer.class);
                row.setInteger(index, (Integer) convertedValue);
            } else if (colType == SeColumnDefinition.TYPE_INT64) {
                convertedValue = Converters.convert(convertedValue, Long.class);
                row.setLong(index, (Long) convertedValue);
            } else if (colType == SeColumnDefinition.TYPE_FLOAT32) {
                convertedValue = Converters.convert(convertedValue, Float.class);
                row.setFloat(index, (Float) convertedValue);
            } else if (colType == SeColumnDefinition.TYPE_FLOAT64) {
                convertedValue = Converters.convert(convertedValue, Double.class);
                row.setDouble(index, (Double) convertedValue);
            } else if (colType == SeColumnDefinition.TYPE_STRING
                    || colType == SeColumnDefinition.TYPE_CLOB
                    || colType == SeColumnDefinition.TYPE_NCLOB) {
                convertedValue = Converters.convert(convertedValue, String.class);
                row.setString(index, (String) convertedValue);
            } else if (colType == SeColumnDefinition.TYPE_NSTRING) {
                convertedValue = Converters.convert(convertedValue, String.class);
                row.setNString(index, (String) convertedValue);
            } else if (colType == SeColumnDefinition.TYPE_DATE) {
                // @todo REVISIT: is converters already ready for date->calendar?
                if (convertedValue != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime((Date) convertedValue);
                    row.setTime(index, calendar);
                } else {
                    row.setTime(index, null);
                }
            } else if (colType == SeColumnDefinition.TYPE_SHAPE) {
                if (convertedValue != null) {
                    final Geometry geom = (Geometry) convertedValue;
                    IsValidOp validator = new IsValidOp(geom);
                    if (!validator.isValid()) {
                        TopologyValidationError validationError = validator.getValidationError();
                        String validationErrorMessage = validationError.getMessage();
                        Coordinate coordinate = validationError.getCoordinate();
                        String errorMessage = "Topology validation error at or near point "
                                + coordinate + ": " + validationErrorMessage;
                        throw new DataSourceException("Invalid geometry passed for " + attName
                                + "\n Geomerty: " + geom + "\n" + errorMessage);
                    }
                    ArcSDEGeometryBuilder geometryBuilder;
                    geometryBuilder = ArcSDEGeometryBuilder.builderFor(geom.getClass());
                    SeShape shape = geometryBuilder.constructShape(geom, coordRef);
                    row.setShape(index, shape);
                } else {
                    row.setShape(index, null);
                }
            }
        } catch (SeException e) {
            throw new ArcSdeException(e);
        }
    }

    /**
     * Returns the row index and column names for all the mutable properties in the sde layer. That
     * is, those properties whose type is not
     * {@link SeRegistration#SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE}, which are used as row id
     * columns managed by arcsde.
     * 
     * @return a map keyed by mutable column name and valued by the index of the mutable column name
     *         in the SeTable structure
     * @throws IOException
     * @throws NoSuchElementException
     */
    private LinkedHashMap<Integer, String> getUpdatableColumnNames() throws NoSuchElementException,
            IOException {
        if (mutableColumnNames == null) {
            // We are going to inspect the column defintions in order to
            // determine which attributes are actually mutable...
            final String typeName = this.featureType.getTypeName();
            final SeColumnDefinition[] columnDefinitions = session.describe(typeName);
            final String shapeAttributeName;
            if (this.featureType.getGeometryDescriptor() == null) {
                // no geometry column, it's a non sptial registered table
                shapeAttributeName = null;
            } else {
                shapeAttributeName = session.issue(new Command<String>() {
                    @Override
                    public String execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        SeLayer layer = session.getLayer(typeName);
                        return layer.getShapeAttributeName(SeLayer.SE_SHAPE_ATTRIBUTE_FID);
                    }
                });
            }

            // use LinkedHashMap to respect column order
            LinkedHashMap<Integer, String> columnList = new LinkedHashMap<Integer, String>();

            SeColumnDefinition columnDefinition;
            String columnName;
            int usedIndex = 0;
            for (int actualIndex = 0; actualIndex < columnDefinitions.length; actualIndex++) {
                columnDefinition = columnDefinitions[actualIndex];
                columnName = columnDefinition.getName();
                // this is an attribute added to the featuretype
                // solely to support FIDs. It isn't an actual attribute
                // on the underlying SDE table, and as such it can't
                // be written to. Skip it!
                if (columnName.equals(shapeAttributeName)) {
                    continue;
                }

                // ignore SeColumns for which we don't have a known mapping
                final int sdeType = columnDefinition.getType();
                if (SeColumnDefinition.TYPE_SHAPE != sdeType
                        && null == ArcSDEAdapter.getJavaBinding(new Integer(sdeType))) {
                    continue;
                }

                // We need to exclude read only types from the set of "mutable"
                // column names.
                final short rowIdType = columnDefinition.getRowIdType();
                if (SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE == rowIdType) {
                    continue;
                }

                columnList.put(Integer.valueOf(usedIndex), columnName);
                // only increment usedIndex if we added a mutable column to
                // the list
                usedIndex++;
            }
            this.mutableColumnNames = columnList;
        }

        return this.mutableColumnNames;
    }

    private LinkedHashMap<Integer, String> getInsertableColumnNames()
            throws NoSuchElementException, IOException {
        if (insertableColumnNames == null) {
            // We are going to inspect the column defintions in order to
            // determine which attributes are actually mutable...
            String typeName = this.featureType.getTypeName();
            final SeColumnDefinition[] columnDefinitions = session.describe(typeName);

            // use LinkedHashMap to respect column order
            LinkedHashMap<Integer, String> columnList = new LinkedHashMap<Integer, String>();

            SeColumnDefinition columnDefinition;
            String columnName;
            int usedIndex = 0;
            for (int actualIndex = 0; actualIndex < columnDefinitions.length; actualIndex++) {
                columnDefinition = columnDefinitions[actualIndex];
                columnName = columnDefinition.getName();

                if (fidReader instanceof FIDReader.SdeManagedFidReader) {
                    if (columnName.equals(fidReader.getFidColumn()))
                        continue;
                }

                // ignore SeColumns for which we don't have a known mapping
                final int sdeType = columnDefinition.getType();
                if (SeColumnDefinition.TYPE_SHAPE != sdeType
                        && null == ArcSDEAdapter.getJavaBinding(Integer.valueOf(sdeType))) {
                    continue;
                }

                columnList.put(Integer.valueOf(usedIndex), columnName);
                usedIndex++;
            }
            this.insertableColumnNames = columnList;
        }

        return this.insertableColumnNames;
    }

    private SeTable getTable() throws IOException {
        if (this.cachedTable == null) {
            final String typeName = this.featureType.getTypeName();
            this.cachedTable = session.getTable(typeName);
        }
        return this.cachedTable;
    }

    private SeLayer getLayer() throws IOException {
        if (this.cachedLayer == null && featureType.getGeometryDescriptor() != null) {
            final String typeName = this.featureType.getTypeName();
            final SeLayer layer = session.getLayer(typeName);
            this.cachedLayer = layer;
        }
        return this.cachedLayer;
    }

    /**
     * Creates a feature id for a new feature; the feature id is compound of the
     * {@value #NEW_FID_PREFIX} plus a UUID.
     * 
     * @return
     */
    private String newFid() {
        return NEW_FID_PREFIX + UUID.randomUUID();
    }

    /**
     * Checks if <code>feature</code> has been created by this writer
     * <p>
     * A Feature is created but not yet inserted if its id starts with {@link #NEW_FID_PREFIX}
     * </p>
     * 
     * @param aFeature
     * @return
     */
    private final boolean isNewlyCreated(SimpleFeature aFeature) {
        final String id = aFeature.getID();
        return id.startsWith(NEW_FID_PREFIX);
    }

    public ISession getSession() {
        return session;
    }

    private static class MutableFIDFeature extends SimpleFeatureImpl {

        public MutableFIDFeature(List<Object> values, SimpleFeatureType ft, String fid)
                throws IllegalAttributeException {
            super(values, ft, createDefaultFID(fid));
        }

        private static FeatureIdImpl createDefaultFID(String id) {
            if (id == null) {
                id = SimpleFeatureBuilder.createDefaultFeatureId();
            }
            return new FeatureIdImpl(id) {
                public void setID(String id) {
                    if (fid == null) {
                        throw new NullPointerException("fid must not be null");
                    }
                    if (origionalFid == null) {
                        origionalFid = fid;
                    }
                    fid = id;
                }
            };
        }

        /**
         * Sets the FID, used by datastores only.
         * 
         * I would love to protect this for safety reason, i.e. so client classes can't use it by
         * casting to it.
         * 
         * @param id
         *            The fid to set.
         */
        public void setID(String fid) {
            ((FeatureIdImpl) id).setID(fid);
        }
    }
}
