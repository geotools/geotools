/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.util.Version;

/**
 * Sets up a trigger to the feature insertion target table This setup enable to test the increment
 * of multiple database sequences upon a single feature insertion
 *
 * @author Lorenzo Pini (lorenzo.pini@geo-solutions.it)
 */
public class PostGISFeatureStoreTestSetup extends PostGISTestSetup {

    @Override
    protected void setUpData() throws Exception {
        super.setUpData();

        // Dummy trigger, inserts an empty row on ft3 table
        // The INSERT is needed to increment the ft3 id sequence
        run(
                "CREATE OR REPLACE FUNCTION after_update_ft1_trigger() " //
                        + "RETURNS TRIGGER " //
                        + "AS $BODY$ " //
                        + "BEGIN " //
                        + "INSERT INTO ft3 VALUES(DEFAULT); " //
                        + "RETURN NEW; " //
                        + "END; " //
                        + "$BODY$ " //
                        + "language plpgsql " //
                        + "volatile ; " //
                );

        // Add the trigger to the existing table
        run(
                "CREATE TRIGGER after_insert_trigger " //
                        + "AFTER INSERT " //
                        + "ON ft1 " //
                        + "FOR EACH ROW " //
                        + "EXECUTE PROCEDURE after_update_ft1_trigger();" //
                );
    }

    @Override
    public void tearDown() throws Exception {

        // Removes the trigger from the table
        String sql = "DROP TRIGGER ";

        if (isPgsqlVersionGreaterThanEqualTo(new Version("8.2"))) {
            sql += "IF EXISTS ";
        }
        sql += "after_insert_trigger ON ft1;";

        run(sql);

        super.tearDown();
    }
}
