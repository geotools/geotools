/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geotools.data.AttributeReader;
import org.geotools.data.AttributeWriter;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * QueryData holds the ResultSet obtained from the sql query and has the following responsibilities:
 * 
 * <ul>
 * <li> acts as the attribute reader by using the AttributeIO objects </li>
 * <li> acts as the attribute writer by using the AttributeIO objects </li>
 * <li> manages the resulset, statement and transaction and closes them cleanly if needed </li>
 * <li> provides methods for creating a new row, as well as inserting new ones, that are used by the
 * JDBCFeatureWriter </li>
 * <li> holds the FIDMapper for feature reader and writer to use when building new features </li>
 * </ul>
 * 
 * 
 * @author aaime
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/library/jdbc/src/main/java/org/geotools/data/jdbc/QueryData.java $
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class QueryData implements AttributeReader, AttributeWriter {
    /** The logger for the data module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc");

    protected Object[] fidAttributes;

    protected FeatureTypeInfo featureTypeInfo;

    protected ResultSet resultSet;

    protected Connection connection;

    protected Transaction transaction;

    protected Statement statement;

    protected FIDMapper mapper;

    protected AttributeIO[] attributeHandlers;

    protected int baseIndex;

    boolean hasNextCalled = false;

    boolean lastNext;

    protected FeatureListenerManager listenerManager;
    
    protected Hints hints;
    
    /**
     * Creates a new QueryData object.
     * 
     * @param featureTypeInfo
     * @param parentDataStore
     * @param connection
     * @param statement
     * @param resultSet
     * @param transaction
     */
    public QueryData(FeatureTypeInfo featureTypeInfo, JDBC1DataStore parentDataStore,
            Connection connection, Statement statement, ResultSet resultSet, Transaction transaction)
            throws IOException {
        this(featureTypeInfo, parentDataStore, connection, statement, resultSet, transaction, null);
    }

    /**
     * Creates a new QueryData object.
     * 
     * @param featureTypeInfo
     * @param parentDataStore
     * @param connection
     * @param statement
     * @param resultSet
     * @param transaction
     */
    public QueryData(FeatureTypeInfo featureTypeInfo, JDBC1DataStore parentDataStore,
            Connection connection, Statement statement, ResultSet resultSet, Transaction transaction, Hints hints)
            throws IOException {
        this.featureTypeInfo = featureTypeInfo;
        this.mapper = featureTypeInfo.getFIDMapper();
        this.baseIndex = mapper.getColumnCount() + 1;
        this.resultSet = resultSet;
        this.statement = statement;
        this.connection = connection;
        this.transaction = transaction;
        this.fidAttributes = new Object[mapper.getColumnCount()];
        this.listenerManager = parentDataStore.listenerManager;
        this.hints = hints;

        List<AttributeDescriptor> attributeTypes = featureTypeInfo.getSchema().getAttributeDescriptors();

        this.attributeHandlers = new AttributeIO[attributeTypes.size()];

        for (int i = 0; i < attributeHandlers.length; i++) {
            if (attributeTypes.get(i) instanceof GeometryDescriptor) {
                attributeHandlers[i] = parentDataStore.getGeometryAttributeIO(attributeTypes.get(i),
                        this);
            } else {
                attributeHandlers[i] = parentDataStore.getAttributeIO(attributeTypes.get(i));
            }
        }
    }

    /**
     * 
     * @see org.geotools.data.AttributeWriter#getAttributeCount()
     */
    public int getAttributeCount() {
        return attributeHandlers.length;
    }

    /**
     * Returns the AttributeIO objects used to parse and encode the column values stored in the
     * database
     * 
     */
    public AttributeIO[] getAttributeHandlers() {
        return attributeHandlers;
    }

    /**
     * DOCUMENT ME!
     * 
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Returns the FID mapper to be used when reading/writing features
     * 
     */
    public FIDMapper getMapper() {
        return mapper;
    }

    /**
     * Returns the current transation
     * 
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * 
     * @see org.geotools.data.AttributeWriter#close()
     */
    public void close() {
        close(null);
    }

    /**
     * Closes the JDBC objects associated to the queryData and reports the sqlException on the LOG
     * 
     * @param sqlException
     */
    public void close(SQLException sqlException) {
        JDBCUtils.close(resultSet);
        JDBCUtils.close(statement);
        JDBCUtils.close(connection, transaction, sqlException);
        resultSet = null;
        statement = null;
        connection = null;
        transaction = null;
    }

    /**
     * @see org.geotools.data.AttributeReader#read(int)
     */
    public Object read(int index) throws IOException, ArrayIndexOutOfBoundsException {
        AttributeIO reader = attributeHandlers[index];

        return reader.read(resultSet, index + baseIndex);
    }

    /**
     * @see org.geotools.data.AttributeWriter#write(int, java.lang.Object)
     */
    public void write(int i, Object currAtt) throws IOException {
        AttributeIO attributeHandler = attributeHandlers[i];
        attributeHandler.write(resultSet, baseIndex + i, currAtt);
    }

    /**
     * Reads a column of the primary key
     * 
     * @param index
     *            the column index among the primary key columns (as reported by the FIDMapper)
     * 
     * @return fid value
     * 
     * @throws IOException
     * @throws DataSourceException
     */
    public Object readFidColumn(int index) throws IOException {
        try {
            return resultSet.getString(index + 1); // we turn it into a string anyhow...
        } catch (SQLException e) {
            throw new DataSourceException("Error reading fid column " + index, e);
        }
    }

    /**
     * Writes a column of the primary key
     * 
     * @param index
     *            the FID column index among the primary key columns (as reported by the FIDMapper)
     * @param value
     *            the column value
     * 
     * @throws IOException
     * @throws DataSourceException
     */
    public void writeFidColumn(int index, Object value) throws IOException {
        try {
            if (value == null) {
                resultSet.updateNull(index + 1);
            } else {
                resultSet.updateObject(index + 1, value);
            }
        } catch (SQLException e) {
            throw new DataSourceException("Error writing fid column " + index, e);
        }
    }

    /**
     * Returns the current feature type
     * 
     */
    public SimpleFeatureType getFeatureType() {
        return featureTypeInfo.getSchema();
    }

    /**
     * Moves the result set to the insert row. Must be called before writing the attribute values
     * for the new Feature
     * 
     * @throws SQLException
     */
    public void startInsert() throws SQLException {
        resultSet.moveToInsertRow();
    }

    /**
     * Deletes the current record in the result set
     * 
     * @throws SQLException
     */
    public void deleteCurrentRow() throws SQLException {
        this.resultSet.deleteRow();
    }

    /**
     * Update the current record
     * 
     * @throws SQLException
     */
    public void updateRow() throws SQLException {
        resultSet.updateRow();
    }

    /**
     * Insert a record in the current result set
     * 
     * @throws SQLException
     */
    public void doInsert() throws SQLException {
        resultSet.insertRow();
    }

    /**
     * DOCUMENT ME!
     * 
     */
    public FeatureTypeInfo getFeatureTypeInfo() {
        return featureTypeInfo;
    }

    /**
     * @return true if the QueryData has been closed, false otherwise
     */
    public boolean isClosed() {
        return resultSet == null;
    }

    /**
     * 
     * @see org.geotools.data.AttributeWriter#next()
     */
    public void next() throws IOException {
        if ((!hasNextCalled && !hasNext()) || !lastNext) {
            throw new NoSuchElementException("No feature to read, hasNext did return false");
        }

        hasNextCalled = false;
    }

    /**
     * 
     * @see org.geotools.data.AttributeWriter#hasNext()
     */
    public boolean hasNext() throws IOException {
        try {
            if (!hasNextCalled) {
                hasNextCalled = true;
                lastNext = resultSet.next();
            }
        } catch (Exception e) {
            throw new DataSourceException("Problem moving on to the next attribute", e);
        }

        return lastNext;
    }

    /**
     * @see org.geotools.data.AttributeReader#getAttributeType(int)
     */
    public AttributeDescriptor getAttributeType(int index) throws ArrayIndexOutOfBoundsException {
        return featureTypeInfo.getSchema().getDescriptor(index);
    }

    public FeatureListenerManager getListenerManager() {
        return listenerManager;
    }

    /** Call after deleteCurrentRow() */
    public void fireChangeRemoved(ReferencedEnvelope bounds, boolean isCommit) {
        String typeName = featureTypeInfo.getFeatureTypeName();
        listenerManager.fireFeaturesRemoved(typeName, transaction, bounds, isCommit);
    }

    /** Call after updateRow */
    public void fireFeaturesChanged(ReferencedEnvelope bounds, boolean isCommit) {
        String typeName = featureTypeInfo.getFeatureTypeName();
        listenerManager.fireFeaturesChanged(typeName, transaction, bounds, isCommit);
    }

    /** Call after doUpdate */
    public void fireFeaturesAdded(ReferencedEnvelope bounds, boolean isCommit) {
        String typeName = featureTypeInfo.getFeatureTypeName();
        listenerManager.fireFeaturesAdded(typeName, transaction, bounds, isCommit);
    }

    protected void finalize() throws Throwable {
        if (!isClosed()) {
            LOGGER.severe("There's code leaving readers, writers or iterators unclosed "
                    + "(you got an unclosed QueryData object, which is usually "
                    + "held by a reader or a writer).\n"
                    + "Call reader/writer.close() or FeatureCollection.close(iterator) " 
                    + "after using them to ensure "
                    + "they do not hold state such as JDCB connections.\n"
                    + "QueryData was open against feature type: "
                    + featureTypeInfo.getFeatureTypeName());
            close();
        }
        super.finalize();
    }
    
    public Hints getHints() {
        if(hints == null) {
            hints = new Hints(Collections.EMPTY_MAP);
        }
        return hints;
    }
}
