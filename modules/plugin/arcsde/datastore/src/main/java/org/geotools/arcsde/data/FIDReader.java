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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.SdeRow;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeRegistration;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeTable;

/**
 * Strategy object used to manage the different ways an ArcSDE server handles row identity.
 * <p>
 * The supported strategies are:
 * <ul>
 * <li>SDE managed mode: a column is assigned by the sde engine to be the feature id (it uses to be
 * called OBJECTID)
 * <li>User managed: a user specified row is used as the fid column.
 * <li>Shape fid: if none of the above, the fid happens to be the identifier of the geometry column
 * </ul>
 * </p>
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/FIDReader.java $
 */
public abstract class FIDReader {

    protected String layerName;

    /** column name holding the feature id attribute */
    private String fidColumn;

    private int columnIndex;

    /**
     * Creates a new FIDStrategy object.
     * 
     * @param fidColumns
     * 
     */
    private FIDReader(String layerName, String fidColumn) {
        this.layerName = layerName;
        this.fidColumn = fidColumn;
    }

    public String getFidColumn() {
        return fidColumn;
    }

    public void setColumnIndex(int fidIndex) {
        this.columnIndex = fidIndex;
    }

    public int getColumnIndex() {
        return this.columnIndex;
    }

    public long readFid(SdeRow row) throws IOException {
        Object fid = row.getObject(this.columnIndex);
        return ((Number) fid).longValue();
    }

    /**
     * Returns the attribute names of the FeatureType passed to the constructor.
     * 
     * @param the
     *            feature type containing the properties the client code is interested in. May well
     *            be a subset of the full set of attributes in the SeLayer
     * @return the list of property names to actually fetch for a given feature type, taking into
     *         account the ones that possibly need to be fetched to generate the feature id, even if
     *         they're not part of the schema.
     * @throws IOException
     *             if an arcsde exception is thrown somehow.
     */
    public String[] getPropertiesToFetch(SimpleFeatureType schema) throws IOException {

        List<String> attNames = new ArrayList<String>(schema.getAttributeCount() + 1);

        // /List attDescriptors = Descriptors.nodes(schema.getDescriptor());
        List<AttributeDescriptor> attDescriptors = schema.getAttributeDescriptors();

        for (AttributeDescriptor property : attDescriptors) {
            attNames.add(property.getLocalName());
        }
        String fidColumn = getFidColumn();
        int fidIndex = attNames.indexOf(fidColumn);
        if (fidColumn != null && fidIndex == -1) {
            attNames.add(fidColumn);
            fidIndex = attNames.size() - 1;
        }
        setColumnIndex(fidIndex);

        return attNames.toArray(new String[attNames.size()]);
    }

    /**
     * Returns a FID strategy appropriate for the given SeLayer
     * 
     * @param session
     * @param tableName
     * @return
     * @throws IOException
     */
    public static FIDReader getFidReader(final ISession session, final SeTable table,
            final SeLayer layer, final SeRegistration reg) throws IOException {
        return session.issue(new Command<FIDReader>() {
            @Override
            public FIDReader execute(final ISession session, final SeConnection connection)
                    throws SeException, IOException {
                return getFidReaderInternal(session, table, layer, reg);
            }
        });
    }

    /**
     * Only to be called from inside a command
     * 
     * @see #getFidReader(ISession, SeTable, SeLayer, SeRegistration)
     */
    private static FIDReader getFidReaderInternal(ISession session, SeTable table, SeLayer layer,
            SeRegistration reg) throws IOException, ArcSdeException {
        FIDReader fidReader = null;
        final String tableName = reg.getTableName();
        try {
            // final int rowIdAllocationType = reg.getRowIdAllocation();
            final int rowIdColumnType = reg.getRowIdColumnType();
            final String rowIdColumnName = reg.getRowIdColumnName();
            int rowIdColumnIndex = -1;
            SeColumnDefinition[] schema = session.describe(table);
            for (int index = 0; index < schema.length; index++) {
                if (schema[index].getName().equals(rowIdColumnName)) {
                    rowIdColumnIndex = index;
                    break;
                }
            }
            if (rowIdColumnType == SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_SDE) {
                // use column name, value maintained by sde
                fidReader = new SdeManagedFidReader(tableName, rowIdColumnName);
            } else if (rowIdColumnType == SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_USER) {
                // use column name, value maintained by user
                fidReader = new UserManagedFidReader(tableName, rowIdColumnName);
            } else if (rowIdColumnType == SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_NONE) {
                // use geometry id
                String shapeColName = layer.getSpatialColumn();
                String shapeIdColName = layer.getShapeAttributeName(SeLayer.SE_SHAPE_ATTRIBUTE_FID);
                fidReader = new ShapeFidReader(tableName, shapeColName, shapeIdColName);
            } else {
                // may have been returned 0, meaning there is no registered
                // column id
                throw new IllegalStateException("Unkown ArcSDE row ID registration type: "
                        + rowIdColumnType + " for layer " + tableName);
            }
            fidReader.setColumnIndex(rowIdColumnIndex);
            return fidReader;
        } catch (SeException e) {
            throw new ArcSdeException("Obtaining FID strategy for " + tableName, e);
        }
    }

    public static class ShapeFidReader extends FIDReader {
        /**
         * Name of the Shape, populated as a side effect of getPropertiesToFetch()
         */

        private final String shapeColName;

        /**
         * Index of the Shape, populated as a side effect of getPropertiesToFetch()
         */
        private int shapeIndex;

        public ShapeFidReader(final String layerName, final String shapeColName,
                final String shapeIdColName) {
            super(layerName, shapeIdColName);
            this.shapeColName = shapeColName;
            this.shapeIndex = -1;
        }

        @Override
        public long readFid(SdeRow row) throws IOException {
            long longFid;
            if (shapeIndex != -1) {
                // we have the shape, so SHAPE.fid couldn't be retrieved
                // at the same time, need to get the shape and ask it for the id
                try {
                    SeShape shape = row.getShape(shapeIndex);
                    if (shape == null) {
                        throw new NullPointerException("Can't get FID from " + layerName
                                + " as it has SHAPE fid reading strategy and got a null shape");
                    }
                    longFid = shape.getFeatureId().longValue();
                } catch (SeException e) {
                    throw new ArcSdeException("Getting fid from shape", e);
                }
            } else {
                int shapeIdIndex = getColumnIndex();
                Integer id = (Integer) row.getObject(shapeIdIndex);
                longFid = id.longValue();
            }
            return longFid;
        }

        /**
         * Overrides to include the geometry column whether it is required by the {@code schema} or
         * not, since we need to get the fid from the geometry id.
         */
        @Override
        public String[] getPropertiesToFetch(SimpleFeatureType schema) throws IOException {
            List<String> attNames = new ArrayList<String>(schema.getAttributeCount() + 1);

            // /List attDescriptors = Descriptors.nodes(schema.getDescriptor());
            List<AttributeDescriptor> attDescriptors = schema.getAttributeDescriptors();
            for (AttributeDescriptor property : attDescriptors) {
                attNames.add(property.getLocalName());
            }

            shapeIndex = attNames.indexOf(shapeColName);
            if (shapeIndex == -1) {
                String fidColumn = getFidColumn();
                int fidIndex = attNames.indexOf(shapeColName);
                if (fidIndex == -1) {
                    attNames.add(fidColumn);
                    fidIndex = attNames.size() - 1;
                }
                setColumnIndex(fidIndex);
            }
            return attNames.toArray(new String[attNames.size()]);
        }

    }

    public static class SdeManagedFidReader extends FIDReader {

        public SdeManagedFidReader(final String layerName, final String rowIdColName) {
            super(layerName, rowIdColName);
        }
    }

    public static class UserManagedFidReader extends FIDReader {

        public UserManagedFidReader(final String layerName, final String rowIdColName) {
            super(layerName, rowIdColName);
        }
    }

    public static final FIDReader NULL_READER = new FIDReader(null, null) {
        @Override
        public long readFid(SdeRow row) throws IOException {
            return (long) (10000 * Math.random());
        }
    };
}
