/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.data.hana.metadata.Srs;

/** @author Stefan Uhrig, SAP SE */
public class HanaTestSetup extends HanaTestSetupBase {

    private static final String TABLE1 = "ft1";

    private static final String TABLE2 = "ft2";

    private static final String TABLE3 = "ft3";

    private static final String TABLE4 = "ft4";

    @Override
    protected void setUpData() throws Exception {
        try (Connection conn = getConnection()) {
            HanaTestUtil htu = new HanaTestUtil(conn);

            Srs srs =
                    new Srs(
                            "NAD27 / UTM zone 13N",
                            26713,
                            "EPSG",
                            26713,
                            "PROJCS[\"NAD27 / UTM zone 13N\",GEOGCS[\"NAD27\",DATUM[\"North_American_Datum_1927\",SPHEROID[\"Clarke 1866\",6378206.4,294.9786982139006,AUTHORITY[\"EPSG\",\"7008\"]],AUTHORITY[\"EPSG\",\"6267\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4267\"]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",-105],PARAMETER[\"scale_factor\",0.9996],PARAMETER[\"false_easting\",500000],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"Easting\",EAST],AXIS[\"Northing\",NORTH],AUTHORITY[\"EPSG\",\"26713\"]]",
                            "+proj=utm +zone=13 +datum=NAD27 +units=m +no_defs ",
                            "meter",
                            "degree",
                            Srs.Type.PROJECTED,
                            6378206.4,
                            null,
                            294.9786982139006,
                            182039.12436568446,
                            561468.8948154468,
                            1977132.0405078614,
                            8818299.223581277);
            htu.createSrs(srs);

            htu.createTestSchema();

            htu.dropTestTableCascade(TABLE1);
            htu.dropTestTableCascade(TABLE2);
            htu.dropTestTableCascade(TABLE3);
            htu.dropTestTableCascade(TABLE4);

            String[][] ft1cols = {
                {"id", "INT PRIMARY KEY"},
                {"geometry", "ST_Geometry(1000004326)"},
                {"intProperty", "INT"},
                {"doubleProperty", "DOUBLE"},
                {"stringProperty", "VARCHAR(255)"}
            };
            htu.createRegisteredTestTable(TABLE1, ft1cols);

            htu.insertIntoTestTable(
                    TABLE1,
                    htu.nextTestSequenceValueForColumn(TABLE1, "id"),
                    htu.geometry("POINT(0 0)", 1000004326),
                    0,
                    0.0,
                    "zero");
            htu.insertIntoTestTable(
                    TABLE1,
                    htu.nextTestSequenceValueForColumn(TABLE1, "id"),
                    htu.geometry("POINT(1 1)", 1000004326),
                    1,
                    1.1,
                    "one");
            htu.insertIntoTestTable(
                    TABLE1,
                    htu.nextTestSequenceValueForColumn(TABLE1, "id"),
                    htu.geometry("POINT(2 2)", 1000004326),
                    2,
                    2.2,
                    "two");

            String[][] ft3cols = {
                {"id", "INT PRIMARY KEY"},
                {"gEoMeTrY", "ST_Geometry(1000004326)"},
                {"intProperty", "INT"},
                {"doubleProperty", "DOUBLE"},
                {"stringProperty", "VARCHAR(255)"}
            };
            htu.createRegisteredTestTable(TABLE3, ft3cols);

            htu.insertIntoTestTable(
                    TABLE3,
                    htu.nextTestSequenceValueForColumn(TABLE3, "id"),
                    htu.geometry("POINT(0 0)", 1000004326),
                    0,
                    0.0,
                    "zero");

            insertFt4(htu);
        }
    }

    private void insertFt4(HanaTestUtil htu) throws SQLException {
        String[][] ft3cols = {
            {"id", "INT PRIMARY KEY"},
            {"gEoMeTrY", "ST_Geometry(1000004326)"},
            {"intProperty", "INT"},
            {"doubleProperty", "DOUBLE"},
            {"stringProperty", "VARCHAR(255)"}
        };
        htu.createRegisteredTestTable(TABLE4, ft3cols);

        htu.insertIntoTestTable(
                TABLE4,
                htu.nextTestSequenceValueForColumn(TABLE4, "id"),
                htu.geometry("POINT(0 0)", 1000004326),
                0,
                0.0,
                "zero");

        htu.insertIntoTestTable(
                TABLE4,
                htu.nextTestSequenceValueForColumn(TABLE4, "id"),
                htu.geometry("POINT(1 1)", 1000004326),
                1,
                1.1,
                "one");

        htu.insertIntoTestTable(
                TABLE4,
                htu.nextTestSequenceValueForColumn(TABLE4, "id"),
                htu.geometry("POINT(2 2)", 1000004326),
                1,
                1.1,
                "one_2");

        htu.insertIntoTestTable(
                TABLE4,
                htu.nextTestSequenceValueForColumn(TABLE4, "id"),
                htu.geometry("POINT(3 3)", 1000004326),
                1,
                1.1,
                "one_2");

        htu.insertIntoTestTable(
                TABLE4,
                htu.nextTestSequenceValueForColumn(TABLE4, "id"),
                htu.geometry("POINT(4 4)", 1000004326),
                2,
                2.2,
                "two");

        htu.insertIntoTestTable(
                TABLE4,
                htu.nextTestSequenceValueForColumn(TABLE4, "id"),
                htu.geometry("POINT(5 5)", 1000004326),
                2,
                2.2,
                "two_2");

        htu.insertIntoTestTable(
                TABLE4,
                htu.nextTestSequenceValueForColumn(TABLE4, "id"),
                htu.geometry("POINT(6 6)", 1000004326),
                3,
                3.3,
                "three");
    }
}
