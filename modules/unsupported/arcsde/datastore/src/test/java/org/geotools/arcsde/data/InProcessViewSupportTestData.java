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

import com.esri.sde.sdk.client.SDEPoint;
import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeInsert;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeTable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Data setup and utilities for testing the support of in-process views
 *
 * @author Gabriel Roldan, Axios Engineering
 * @since 2.4.x
 */
public class InProcessViewSupportTestData {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(InProcessViewSupportTestData.class);

    public static final String MASTER_UNQUALIFIED = "GT_SDE_TEST_MASTER";

    public static final String CHILD_UNQUALIFIED = "GT_SDE_TEST_CHILD";

    private static CoordinateReferenceSystem testCrs;

    public static String MASTER;

    public static String CHILD;

    public static String masterChildSql;

    /**
     * Extra datastore creation parameters to set up {@link #typeName} as a FeatureType defined by
     * {@link #masterChildSql}
     */
    public static Map<String, String> registerViewParams;

    public static final String typeName = "MasterChildTest";

    public static void setUp(ISession session, TestData td)
            throws IOException, UnavailableConnectionException {

        testCrs = DefaultGeographicCRS.WGS84;

        /** Remember, shape field has to be the last one */
        masterChildSql =
                "SELECT "
                        + MASTER_UNQUALIFIED
                        + ".ID, "
                        + MASTER_UNQUALIFIED
                        + ".NAME, "
                        + CHILD_UNQUALIFIED
                        + ".DESCRIPTION, "
                        + MASTER_UNQUALIFIED
                        + ".SHAPE "
                        + "FROM "
                        + MASTER_UNQUALIFIED
                        + ", "
                        + CHILD_UNQUALIFIED
                        + " WHERE "
                        + CHILD_UNQUALIFIED
                        + ".MASTER_ID = "
                        + MASTER_UNQUALIFIED
                        + ".ID ORDER BY "
                        + MASTER_UNQUALIFIED
                        + ".ID";

        final String user = session.getUser();
        MASTER = user + "." + MASTER_UNQUALIFIED;
        CHILD = user + "." + CHILD_UNQUALIFIED;
        createMasterTable(session, td);
        createChildTable(session, td);

        registerViewParams = new HashMap<String, String>();
        registerViewParams.put("sqlView.1.typeName", typeName);
        registerViewParams.put("sqlView.1.sqlQuery", masterChildSql);
    }

    private static void createMasterTable(final ISession session, final TestData td)
            throws IOException, UnavailableConnectionException {

        final SeTable table = session.createSeTable(MASTER);

        final Command<SeLayer> createLayerCmd =
                new Command<SeLayer>() {

                    @Override
                    public SeLayer execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        SeLayer layer;
                        try {
                            table.delete();
                        } catch (SeException e) {
                            // no-op, table didn't existed
                        }

                        SeColumnDefinition[] colDefs = new SeColumnDefinition[2];

                        colDefs[0] =
                                new SeColumnDefinition(
                                        "ID", SeColumnDefinition.TYPE_INT32, 10, 0, false);
                        colDefs[1] =
                                new SeColumnDefinition(
                                        "NAME", SeColumnDefinition.TYPE_STRING, 255, 0, false);

                        layer = new SeLayer(connection);
                        layer.setTableName(MASTER);
                        table.create(colDefs, td.getConfigKeyword());

                        layer.setSpatialColumnName("SHAPE");
                        layer.setShapeTypes(SeLayer.SE_POINT_TYPE_MASK);
                        layer.setGridSizes(1100.0, 0.0, 0.0);
                        layer.setDescription(
                                "Geotools sde pluing join support testing master table");
                        SeCoordinateReference coordref = new SeCoordinateReference();
                        coordref.setCoordSysByDescription(testCrs.toWKT());

                        layer.setCreationKeyword(td.getConfigKeyword());
                        layer.create(3, 4);
                        return layer;
                    }
                };

        SeLayer layer = session.issue(createLayerCmd);

        insertMasterData(session, layer);
        LOGGER.info("successfully created master table " + MASTER);
    }

    private static void createChildTable(final ISession session, final TestData td)
            throws IOException, UnavailableConnectionException {
        final SeTable table = session.createSeTable(CHILD);
        Command<Void> createCmd =
                new Command<Void>() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        try {
                            table.delete();
                        } catch (SeException e) {
                            // no-op, table didn't existed
                        }

                        SeColumnDefinition[] colDefs = new SeColumnDefinition[4];

                        colDefs[0] =
                                new SeColumnDefinition(
                                        "ID", SeColumnDefinition.TYPE_INTEGER, 10, 0, false);
                        colDefs[1] =
                                new SeColumnDefinition(
                                        "MASTER_ID", SeColumnDefinition.TYPE_INTEGER, 10, 0, false);
                        colDefs[2] =
                                new SeColumnDefinition(
                                        "NAME", SeColumnDefinition.TYPE_STRING, 255, 0, false);
                        colDefs[3] =
                                new SeColumnDefinition(
                                        "DESCRIPTION",
                                        SeColumnDefinition.TYPE_STRING,
                                        255,
                                        0,
                                        false);

                        table.create(colDefs, td.getConfigKeyword());
                        return null;
                    }
                };

        session.issue(createCmd);

        /*
         * SeRegistration tableRegistration = new SeRegistration(conn, CHILD);
         * tableRegistration.setRowIdColumnType
         * (SeRegistration.SE_REGISTRATION_ROW_ID_COLUMN_TYPE_USER);
         * tableRegistration.setRowIdColumnName("ID"); tableRegistration.alter();
         */
        insertChildData(session, table);

        LOGGER.info("successfully created child table " + CHILD);
    }

    /**
     *
     *
     * <pre>
     * &lt;code&gt;
     *  -----------------------------------------------
     *  |            GT_SDE_TEST_MASTER               |
     *  -----------------------------------------------
     *  |  ID(int)  | NAME (string)  | SHAPE (Point)  |
     *  -----------------------------------------------
     *  |     1     |   name1        |  POINT(1, 1)   |
     *  -----------------------------------------------
     *  |     2     |   name2        |  POINT(2, 2)   |
     *  -----------------------------------------------
     *  |     3     |   name3        |  POINT(3, 3)   |
     *  -----------------------------------------------
     * &lt;/code&gt;
     * </pre>
     */
    private static void insertMasterData(final ISession session, final SeLayer layer)
            throws IOException {
        Command<Void> insertCmd =
                new Command<Void>() {

                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        SeInsert insert = null;
                        SeCoordinateReference coordref = layer.getCoordRef();
                        final String[] columns = {"ID", "NAME", "SHAPE"};

                        for (int i = 1; i < 4; i++) {
                            insert = new SeInsert(connection);
                            insert.intoTable(layer.getName(), columns);
                            insert.setWriteMode(true);

                            SeRow row = insert.getRowToSet();
                            SeShape shape = new SeShape(coordref);
                            SDEPoint[] points = {new SDEPoint(i, i)};
                            shape.generatePoint(1, points);

                            row.setInteger(0, Integer.valueOf(i));
                            row.setString(1, "name" + i);
                            row.setShape(2, shape);
                            insert.execute();
                            insert.close();
                        }
                        return null;
                    }
                };

        session.issue(insertCmd);
        session.commitTransaction();
    }

    /**
     *
     *
     * <pre>
     * &lt;code&gt;
     *  ---------------------------------------------------------------------
     *  |                     GT_SDE_TEST_CHILD                             |
     *  ---------------------------------------------------------------------
     *  | ID(int)   | MASTER_ID      | NAME (string)  | DESCRIPTION(string  |
     *  ---------------------------------------------------------------------
     *  |    1      |      1         |   child1       |    description1     |
     *  ---------------------------------------------------------------------
     *  |    2      |      2         |   child2       |    description2     |
     *  ---------------------------------------------------------------------
     *  |    3      |      2         |   child3       |    description3     |
     *  ---------------------------------------------------------------------
     *  |    4      |      3         |   child4       |    description4     |
     *  ---------------------------------------------------------------------
     *  |    5      |      3         |   child5       |    description5     |
     *  ---------------------------------------------------------------------
     *  |    6      |      3         |   child6       |    description6     |
     *  ---------------------------------------------------------------------
     *  |    7      |      3         |   child6       |    description7     |
     *  ---------------------------------------------------------------------
     * &lt;/code&gt;
     * </pre>
     *
     * Note last row has the same name than child6, for testing group by.
     */
    private static void insertChildData(final ISession session, final SeTable table)
            throws IOException {

        Command<Void> insertCmd =
                new Command<Void>() {

                    @Override
                    public Void execute(ISession session, SeConnection connection)
                            throws SeException, IOException {
                        final String[] columns = {"ID", "MASTER_ID", "NAME", "DESCRIPTION"};
                        int childId = 0;

                        for (int master = 1; master < 4; master++) {
                            for (int child = 0; child < master; child++) {
                                childId++;

                                SeInsert insert = new SeInsert(connection);
                                insert.intoTable(table.getName(), columns);
                                insert.setWriteMode(true);

                                SeRow row = insert.getRowToSet();

                                row.setInteger(0, Integer.valueOf(childId));
                                row.setInteger(1, Integer.valueOf(master));
                                row.setString(2, "child" + (childId));
                                row.setString(3, "description" + (childId));
                                insert.execute();
                                // insert.close();
                            }
                        }
                        // add the 7th row to test group by
                        SeInsert insert = new SeInsert(connection);
                        insert.intoTable(table.getName(), columns);
                        insert.setWriteMode(true);
                        SeRow row = insert.getRowToSet();

                        row.setInteger(0, Integer.valueOf(7));
                        row.setInteger(1, Integer.valueOf(3));
                        row.setString(2, "child6");
                        row.setString(3, "description7");
                        insert.execute();
                        // insert.close();
                        return null;
                    }
                };
        session.issue(insertCmd);
        session.commitTransaction();
    }
}
