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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.VersioningDataStore;
import org.geotools.data.jdbc.JDBCTransactionState;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * JDBC Transaction state that holds current revision, modified bounding box and the list of dirty
 * feature types. On commit, these are update on the db.
 * 
 * @author aaime
 * @since 2.4
 * 
 */
class VersionedJdbcTransactionState extends JDBCTransactionState {

    /** The logger for the postgis module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.postgis");

    private long revision;

    private ReferencedEnvelope bbox;

    private HashSet dirtyTypes;
    
    private HashMap dirtyFids;

    private WrappedPostgisDataStore wrapped;

    private Transaction transaction;
    
    private static final double EPS = 0.000001;

    public VersionedJdbcTransactionState(Connection connection, WrappedPostgisDataStore wrapped)
            throws IOException {
        super(connection);
        this.wrapped = wrapped;
        reset();
    }

    /**
     * Resets this state so that a new revision information is ready to be built
     */
    private void reset() {
        this.revision = Long.MIN_VALUE;
        this.bbox = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        this.dirtyTypes = new HashSet();
        this.dirtyFids = new HashMap();
    }

    /**
     * Returns the revision currently created during the transaction, eventually creating the
     * changesets record if not available
     * 
     * @throws IOException
     */
    public long getRevision() throws IOException {
        if (revision == Long.MIN_VALUE) {
            revision = writeRevision(transaction, bbox);
            transaction.putProperty(VersionedPostgisDataStore.REVISION, new Long(revision));
            transaction.putProperty(VersionedPostgisDataStore.VERSION, String.valueOf(revision));
        }
        return revision;
    }

    /**
     * Marks the specified type name as dirty, modified during the transaction
     * 
     * @param typeName
     */
    public void setTypeNameDirty(String typeName) {
        dirtyTypes.add(typeName);
    }

    /**
     * Expands the current lat/lon dirty area
     * 
     * @param envelope
     *            a new dirtied area, expressed in EPSG:4326 crs
     */
    public void expandDirtyBounds(Envelope envelope) {
        bbox.expandToInclude(envelope);
    }
    
    /**
     * Marks a specified FID as dirty. This is used to avoid to do versioned operations
     * on the same feature multiple times in the same transaction. The first must create 
     * the new versions, the others should operate against the new record
     * @param ft
     * @param fid
     */
    public void setFidDirty(String typeName, String fid) {
        getCreateDirtyFids(typeName).add(fid);
    }
    
    /**
     * Marks a set of FIDs as dirty. This is used to avoid to do versioned operations
     * on the same feature multiple times in the same transaction. The first must create 
     * the new versions, the others should operate against the new record
     * @param ft
     * @param fid
     */
    public void setFidsDirty(String typeName, Collection<String> fids) {
        getCreateDirtyFids(typeName).addAll(fids);
    }

    /**
     * Returns (and eventually builds) the dirty fid set for the specified type name
     */
    Set<String> getCreateDirtyFids(String typeName) {
        Set fids = (Set) dirtyFids.get(typeName);
        if(fids == null) {
            fids = new HashSet();
            dirtyFids.put(typeName, fids);
        }
        return fids;
    }
        
    /**
     * Returns true if a specific feature has already been modified during this transaction
     * @param typeName
     * @param fid
     * @return
     */
    public boolean isFidDirty(String typeName, String fid) {
        Set fids = (Set) dirtyFids.get(typeName);
        if(fids == null) return false;
        return fids.contains(fid);
    }

    public void setTransaction(Transaction transaction) {
        super.setTransaction(transaction);
        this.transaction = transaction;
        if (transaction == null) {
            // setup for fail fast if anyone tries to keep using this state
            // object
            // afer the transaction has been closed
            bbox = null;
            dirtyTypes = null;
        }
    }

    public void commit() throws IOException {
        // first, check we touched at least one versioned table
        if (!dirtyTypes.isEmpty()) {
            // grab author and message, they might have been updated since revsion insertion
            String author = (String) transaction.getProperty(VersioningDataStore.AUTHOR);
            String message = (String) transaction.getProperty(VersioningDataStore.MESSAGE);
            
            // first write down modified envelope
            SimpleFeature f = null;
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;
            try {
                // build filter to extract the appropriate changeset record
                FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
                Filter revisionFilter = ff.id(Collections.singleton(ff.featureId(VersionedPostgisDataStore.TBL_CHANGESETS + "." + getRevision())));

                // get a writer for the changeset record we want to update
                writer = wrapped.getFeatureWriter(VersionedPostgisDataStore.TBL_CHANGESETS,
                        (org.geotools.filter.Filter) revisionFilter, transaction);
                if (!writer.hasNext()) {
                    // who ate my changeset record ?!?
                    throw new IOException("Could not find the changeset record "
                            + "that should have been set in the versioned datastore on "
                            + "versioned jdbc state creation");
                }
                
                // update it
                f = writer.next();
                f.setAttribute("author", author);
                f.setAttribute("message", message);
                f.setDefaultGeometry(toLatLonRectange(bbox));
                writer.write();
            } catch (IllegalAttributeException e) {
                // if this happens there's a programming error
                throw new DataSourceException("Could not set an attribute in changesets, "
                        + "most probably the table schema has been tampered with.", e);
            } finally {
                if (writer != null)
                    writer.close();
            }

            // then write down the modified feature types
            Statement st = null;
            try {
                st = getConnection().createStatement();
                for (Iterator it = dirtyTypes.iterator(); it.hasNext();) {
                    String typeName = (String) it.next();
                    execute(st, "INSERT INTO " + VersionedPostgisDataStore.TBL_TABLESCHANGED + " "
                            + "SELECT " + revision + ", id " + "FROM "
                            + VersionedPostgisDataStore.TBL_VERSIONEDTABLES + " WHERE SCHEMA = '"
                            + wrapped.getConfig().getDatabaseSchemaName() + "' " + "AND NAME = '"
                            + typeName + "'");
                }
            } catch (SQLException e) {
                throw new DataSourceException(
                        "Error occurred while trying to save modified tables for "
                                + "this changeset. This should not happen, probaly there's a "
                                + "bug at work here.", e);
            } finally {
                JDBCUtils.close(st);
            }
        }

        // aah, all right, now we can really commit this transaction and be happy
        super.commit();
        // reset revision, we create a new revision for each new commit
        reset();
    }

    public boolean isRevisionSet() {
        return revision == Long.MIN_VALUE;
    }

    /**
     * Takes a referenced envelope and turns it into a lat/lon Polygon
     * 
     * @param envelope
     * @return
     * @throws TransformException
     */
    Geometry toLatLonRectange(final ReferencedEnvelope env) throws IOException {
        ReferencedEnvelope envelope = new ReferencedEnvelope(env);
        try {
            // since we cannot work with a null geometry in commits to
            // changesets, let's return a very small envelope...
            // an empty envelope gets turned into a point
            if (envelope == null || envelope.isEmpty()) {
                envelope = new ReferencedEnvelope(new Envelope(0, EPS , 0, EPS),
                        DefaultGeographicCRS.WGS84);
            } else {
                envelope = envelope.transform(DefaultGeographicCRS.WGS84, true);
                if(envelope.getHeight() == 0.0 || envelope.getWidth() == 0.0)
                    envelope.expandBy(EPS);
            }
                

            GeometryFactory gf = new GeometryFactory();
            return gf.toGeometry(envelope);
        } catch (Exception e) {
            throw new DataSourceException("An error occurred while trying to builds a "
                    + "lat/lon polygon equivalent to " + envelope, e);
        }
    }

    /**
     * Stores a commit message in the CHANGESETS table and return the associated revision number.
     * 
     * @param conn
     * @return
     * @throws IOException
     */
    protected long writeRevision(Transaction t, ReferencedEnvelope bbox) throws IOException {
        SimpleFeature f = null;
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = null;
        String author = (String) t.getProperty(VersioningDataStore.AUTHOR);
        String message = (String) t.getProperty(VersioningDataStore.MESSAGE);
        Statement st = null;
        try {
            // we need to make sure that revision N+1 is committed after N is committed, otherwise
            // the history will be ruined
            st = getConnection().createStatement();
            st.execute("LOCK TABLE " + VersionedPostgisDataStore.TBL_CHANGESETS + " IN EXCLUSIVE MODE");
            
            writer = wrapped.getFeatureWriterAppend(VersionedPostgisDataStore.TBL_CHANGESETS, t);
            f = writer.next();
            f.setAttribute("author", author);
            f.setAttribute("message", message);
            f.setAttribute("date", new Date());
            
            f.setDefaultGeometry(toLatLonRectange(bbox));
            writer.write();
        } catch (IllegalAttributeException e) {
            // if this happens there's a programming error
            throw new IOException("Could not set an attribute in changesets, "
                    + "most probably the table schema has been tampered with.");
        } catch (SQLException e) {
            throw new DataSourceException("Could not set a lock on the table changesets", e);
        } finally {
            if(st != null)
              JDBCUtils.close(st);  
            if(writer != null)
              writer.close();
        }

        return ((Long) f.getAttribute("revision")).longValue();
    }

    /**
     * Logs the sql at info level, then executes the command
     * 
     * @param st
     * @param sql
     * @throws SQLException
     */
    protected void execute(Statement st, String sql) throws SQLException {
        LOGGER.fine(sql);
        st.execute(sql);
    }

    /**
     * Returns the transaction associated to this state
     * 
     * @return
     */
    Transaction getTransaction() {
        return transaction;
    }
}
