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

package org.geotools.arcsde.session;

import java.io.IOException;
import java.util.List;

import org.geotools.arcsde.versioning.ArcSdeVersionHandler;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeDBMSInfo;
import com.esri.sde.sdk.client.SeDelete;
import com.esri.sde.sdk.client.SeFilter;
import com.esri.sde.sdk.client.SeInsert;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeObjectId;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeRasterColumn;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeRelease;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.client.SeState;
import com.esri.sde.sdk.client.SeStreamOp;
import com.esri.sde.sdk.client.SeTable;
import com.esri.sde.sdk.client.SeUpdate;

/**
 * A pure session wrapper to aid in creating session decorators by extending this class.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 *
 * @source $URL$
 *         http://svn.geotools.org/trunk/modules/plugin/arcsde/datastore/src/main/java/org/geotools
 *         /arcsde/pool/SessionWrapper.java $
 */
public class SessionWrapper implements ISession {

    protected final ISession wrapped;

    public SessionWrapper(final ISession wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * @see org.geotools.arcsde.session.ISession#testServer()
     */
    public void testServer() throws IOException {
        this.wrapped.testServer();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#close(com.esri.sde.sdk.client.SeState)
     */
    public void close(SeState state) throws IOException {
        wrapped.close(state);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#close(com.esri.sde.sdk.client.SeStreamOp)
     */
    public void close(SeStreamOp stream) throws IOException {
        wrapped.close(stream);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#commitTransaction()
     */
    public void commitTransaction() throws IOException {
        wrapped.commitTransaction();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#createAndExecuteQuery(java.lang.String[],
     *      com.esri.sde.sdk.client.SeSqlConstruct)
     */
    public SeQuery createAndExecuteQuery(String[] propertyNames, SeSqlConstruct sql)
            throws IOException {
        return wrapped.createAndExecuteQuery(propertyNames, sql);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#createSeDelete()
     */
    public SeDelete createSeDelete() throws IOException {
        return wrapped.createSeDelete();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#createSeInsert()
     */
    public SeInsert createSeInsert() throws IOException {
        return wrapped.createSeInsert();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#createSeRegistration(java.lang.String)
     */
    public SeRegistration createSeRegistration(String typeName) throws IOException {
        return wrapped.createSeRegistration(typeName);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#createSeTable(java.lang.String)
     */
    public SeTable createSeTable(String qualifiedName) throws IOException {
        return wrapped.createSeTable(qualifiedName);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#createSeUpdate()
     */
    public SeUpdate createSeUpdate() throws IOException {
        return wrapped.createSeUpdate();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#createState(com.esri.sde.sdk.client.SeObjectId)
     */
    public SeState createState(SeObjectId stateId) throws IOException {
        return wrapped.createState(stateId);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#describe(java.lang.String)
     */
    public SeColumnDefinition[] describe(String tableName) throws IOException {
        return wrapped.describe(tableName);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#describe(com.esri.sde.sdk.client.SeTable)
     */
    public SeColumnDefinition[] describe(SeTable table) throws IOException {
        return wrapped.describe(table);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#dispose()
     */
    public void dispose() throws IllegalStateException {
        wrapped.dispose();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#fetch(com.esri.sde.sdk.client.SeQuery)
     */
    public SdeRow fetch(SeQuery query) throws IOException {
        return wrapped.fetch(query);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#fetch(SeQuery, SdeRow)
     */
    public SdeRow fetch(SeQuery query, SdeRow currentRow) throws IOException {
        return wrapped.fetch(query, currentRow);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getDatabaseName()
     */
    public String getDatabaseName() throws IOException {
        return wrapped.getDatabaseName();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getDBMSInfo()
     */
    public SeDBMSInfo getDBMSInfo() throws IOException {
        return wrapped.getDBMSInfo();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getLayer(java.lang.String)
     */
    public SeLayer getLayer(String layerName) throws IOException {
        return wrapped.getLayer(layerName);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getLayers()
     */
    public List<SeLayer> getLayers() throws IOException {
        return wrapped.getLayers();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getRasterColumn(java.lang.String)
     */
    public SeRasterColumn getRasterColumn(String rasterName) throws IOException {
        return wrapped.getRasterColumn(rasterName);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getRasterColumns()
     */
    public List<String> getRasterColumns() throws IOException {
        return wrapped.getRasterColumns();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getRelease()
     */
    public SeRelease getRelease() throws IOException {
        return wrapped.getRelease();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getTable(java.lang.String)
     */
    public SeTable getTable(String tableName) throws IOException {
        return wrapped.getTable(tableName);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#getUser()
     */
    public String getUser() throws IOException {
        return wrapped.getUser();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#isClosed()
     */
    public boolean isClosed() {
        return wrapped.isClosed();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#isDisposed()
     */
    public boolean isDisposed() {
        return wrapped.isDisposed();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#isTransactionActive()
     */
    public boolean isTransactionActive() {
        return wrapped.isTransactionActive();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#issue(org.geotools.arcsde.session.Command)
     */
    public <T> T issue(Command<T> command) throws IOException {
        return wrapped.issue(command);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#rollbackTransaction()
     */
    public void rollbackTransaction() throws IOException {
        wrapped.rollbackTransaction();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#startTransaction()
     */
    public void startTransaction() throws IOException {
        wrapped.startTransaction();
    }

    /**
     * @see org.geotools.arcsde.session.ISession#createChildState(long)
     */
    public SeState createChildState(long parentStateId) throws IOException {
        return wrapped.createChildState(parentStateId);
    }

    /**
     * @see org.geotools.arcsde.session.ISession#prepareQuery(SeQueryInfo, SeFilter[],
     *      ArcSdeVersionHandler)
     */
    public SeQuery prepareQuery(final SeQueryInfo qInfo, final SeFilter[] spatialConstraints,
            final ArcSdeVersionHandler version) throws IOException {
        return wrapped.prepareQuery(qInfo, spatialConstraints, version);
    }

}
