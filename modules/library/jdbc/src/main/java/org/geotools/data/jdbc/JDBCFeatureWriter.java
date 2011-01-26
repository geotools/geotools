/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * JDBCDataStore implementation of the FeatureWriter interface
 *
 * @author aaime
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class JDBCFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    /** The logger for the jdbc module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.jdbc");
    protected QueryData queryData;
    protected  FeatureReader<SimpleFeatureType, SimpleFeature> reader;
    protected SimpleFeature live; // current for FeatureWriter
    protected SimpleFeature current; // copy of live returned to user
    protected boolean closed;
    protected Object[] fidAttributes;

    public JDBCFeatureWriter(FeatureReader <SimpleFeatureType, SimpleFeature> reader, QueryData queryData) {
        this.reader = reader;
        this.queryData = queryData;        
    }

    /**
     * @see org.geotools.data.FeatureWriter#getFeatureType()
     */
    public SimpleFeatureType getFeatureType() {
        return reader.getFeatureType();
    }

    /**
     * @see org.geotools.data.FeatureWriter#next()
     */
    public SimpleFeature next() throws IOException {
        if (reader == null) {
            throw new IOException("FeatureWriter has been closed");
        }

        SimpleFeatureType featureType = getFeatureType();

        if (hasNext()) {
            try {
                live = reader.next();
                current = SimpleFeatureBuilder.copy(live);
                LOGGER.finer("Calling next on writer");
            } catch (IllegalAttributeException e) {
                throw new DataSourceException("Unable to edit " + live.getID()
                    + " of " + featureType.getTypeName(), e);
            }
        } else {
            //			new content
            live = null;

            try {
                SimpleFeature temp = SimpleFeatureBuilder.template(featureType, null);

                /* Here we create a Feature with a Mutable FID.
                 * We use data utilities to create a default set of attributes
                 * for the feature and these are copied into the a new
                 * MutableFIDFeature.  Thsi can probably be improved later,
                 * there is also a dependency on DefaultFeatureType here since
                 * DefaultFeature depends on it and MutableFIDFeature extends default
                 * feature.  This may be an issue if someone reimplements the Feature
                 * interfaces.  It could address by providing a full implementation
                 * of Feature in MutableFIDFeature at a later date.
                 *
                 */
                current = new MutableFIDFeature((List) temp.getAttributes(), featureType, null);

                if (useQueryDataForInsert()) {
                    queryData.startInsert();
                }
            } catch (IllegalAttributeException e) {
                throw new DataSourceException(
                    "Unable to add additional Features of "
                    + featureType.getTypeName(), e);
            } catch (SQLException e) {
                throw new DataSourceException("Unable to move to insert row. "
                    + e.getMessage(), e);
            }
        }

        return current;
    }

    /**
     * Returns true if QueryData is used to insert rows, false if some other
     * means is used
     *
     */
    protected boolean useQueryDataForInsert() {
        return true;
    }

    /**
     * @see org.geotools.data.FeatureWriter#remove()
     */
    public void remove() throws IOException {
        if (closed) {
            throw new IOException("FeatureWriter has been closed");
        }

        if (current == null) {
            throw new IOException("No feature available to remove");
        }

        if (live != null) {
            LOGGER.fine("Removing " + live);

            ReferencedEnvelope bounds = ReferencedEnvelope.reference(live.getBounds());
            live = null;
            current = null;

            Transaction transaction = queryData.getTransaction();
            try {
                queryData.deleteCurrentRow();
                queryData.fireChangeRemoved(bounds, false);
            } catch (SQLException sqle) {
                String message = "problem deleting row";

                if (transaction != Transaction.AUTO_COMMIT) {
                    transaction.rollback();
                    message += "(transaction canceled)";
                }

                throw new DataSourceException(message, sqle);
            }
        } else {
            // cancel add new content
            current = null;
        }
    }

    /**
     * @see org.geotools.data.FeatureWriter#write()
     */
    public void write() throws IOException {
        if (closed) {
            throw new IOException("FeatureWriter has been closed");
        }

        if (current == null) {
            throw new IOException("No feature available to write");
        }

        LOGGER.fine("write called, live is " + live + " and cur is " + current);

        if (live != null) {
            if (live.equals(current)) {
                // no modifications made to current
                live = null;
                current = null;
            } else {
                try {
                    doUpdate(live, current);
                    
                    ReferencedEnvelope bounds = new ReferencedEnvelope();
                    bounds.include(live.getBounds());
                    bounds.include(current.getBounds());                
                    
                    queryData.fireFeaturesChanged(bounds, false);
                    
                } catch (SQLException sqlException) {
                    queryData.close(sqlException);
                    throw new DataSourceException("Error updating row",
                        sqlException);
                }
                live = null;
                current = null;
            }
        } else {
            LOGGER.fine("doing insert in jdbc featurewriter");

            try {
                doInsert((MutableFIDFeature) current);
                queryData.fireFeaturesAdded(ReferencedEnvelope.reference(current.getBounds()), false );
            } catch (SQLException e) {
                throw new DataSourceException("Row adding failed.", e);
            }
            current = null;
        }
    }

    protected void doUpdate(SimpleFeature live, SimpleFeature current)
        throws IOException, SQLException {
        try {
            // Can we create for array getAttributes more efficiently?
            for (int i = 0; i < current.getAttributeCount(); i++) {
                Object currAtt = current.getAttribute(i);
                Object liveAtt = live.getAttribute(i);

                if ((live == null)
                        || !DataUtilities.attributesEqual(liveAtt, currAtt)) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("modifying att# " + i + " to " + currAtt);
                    }

                    queryData.write(i, currAtt);
                }
            }
        } catch (IOException ioe) {
            String message = "problem modifying row";

            if (queryData.getTransaction() != Transaction.AUTO_COMMIT) {
                queryData.getTransaction().rollback();
                message += "(transaction canceled)";
            }

            throw ioe;
        }
        queryData.updateRow();
    }

    /**
     * Inserts a feature into the database.
     * 
     * <p>
     * This method should both insert a Feature, and update its FID in case the
     * FIDMapper works over database generated ids like  autoincrement fields,
     * sequences, and object ids.
     * </p>
     * 
     * <p>
     * Postgis needs to do this seperately.  With updates it can just override
     * the geometry stuff, using a direct sql update statement, but for
     * inserts it can't update a row that doesn't exist yet.
     * </p>
     *
     * @param mutable
     *
     * @throws IOException
     * @throws SQLException
     */
    protected void doInsert(MutableFIDFeature mutable)
        throws IOException, SQLException {
        queryData.startInsert();

        // primary key generation            
        FIDMapper mapper = queryData.getMapper();

        // read the new fid into the Feature 
        Set autoincrementColumns = null;
        if ((mapper.getColumnCount() > 0)
                && !mapper.returnFIDColumnsAsAttributes()) {
            autoincrementColumns = Collections.EMPTY_SET;
            String ID = mapper.createID(queryData.getConnection(), mutable, null);
            fidAttributes = mapper.getPKAttributes(ID);

            if (fidAttributes != null) {
                mutable.setID(ID);

                for (int i = 0; i < fidAttributes.length; i++) {
                    Object fidAttribute = fidAttributes[i];

                    // if a column is of type auto increment, we should not update it
                    if (!mapper.isAutoIncrement(i)) {
                        queryData.writeFidColumn(i, fidAttribute);
                    }
                }
            }
        } else {
            autoincrementColumns = new HashSet();
            for (int i = 0; i < mapper.getColumnCount(); i++) {
                if (mapper.isAutoIncrement(i)) {
                    autoincrementColumns.add(mapper.getColumnName(i));
                }
            }
        }

        // set up attributes and write row
        for (int i = 0; i < current.getAttributeCount(); i++) {
            Object currAtt = current.getAttribute(i);
            String attName = current.getFeatureType().getDescriptor(i).getLocalName();
            if(!autoincrementColumns.contains(attName)) 
                queryData.write(i, currAtt);
        }

        queryData.doInsert();

        // should the ID be generated during an insert, we need to read it back
        // and set it into the feature
        if (((mapper.getColumnCount() > 0) && mapper.hasAutoIncrementColumns())) {
            fidAttributes = new Object[mapper.getColumnCount()];

            for (int i = 0; i < fidAttributes.length; i++) {
                fidAttributes[i] = queryData.readFidColumn(i);
            }

            mutable.setID(mapper.getID(fidAttributes));
        }
    }

    /**
     * @see org.geotools.data.FeatureWriter#hasNext()
     */
    public boolean hasNext() throws IOException {
        if (queryData.isClosed()) {
            throw new IOException("Feature writer is closed");
        }

        return reader.hasNext();
    }

    /**
     * @see org.geotools.data.FeatureWriter#close()
     */
    public void close() throws IOException {
        //changed this from throwing an exception if already closed to just
        //issuing a warning.  Mysql was having trouble with this, but I see
        //no great harm in not throwing an exception, since this will only
        //be in clean-up.
        if (queryData.isClosed()) {
            LOGGER.warning("Feature writer calling close when queryData is " +
                        " already closed");
        } else {
            reader.close();
        }
    }    
    
    /**
     * Encodes the tableName, default is to do nothing, but postgis will
     * override and put double quotes around the tablename.
     */
    protected String encodeName(String tableName) {
        return tableName;
    }
    
    /**
     * Encodes the colName, default just calls {@link #encodeName(String)}.
     */
    protected String encodeColumnName(String colName) {
        return encodeName(colName);
    }
}
