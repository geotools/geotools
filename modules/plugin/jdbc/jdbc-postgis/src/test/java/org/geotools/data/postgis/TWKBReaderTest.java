/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequenceComparator;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKTReader;

/**
 * Tests for reading WKB.
 *
 * @author James Hughes
 * @author Andrea Aime
 */
public class TWKBReaderTest {

    private static CoordinateSequenceComparator COMP2 = new CoordinateSequenceComparator(2);
    private static CoordinateSequenceComparator COMP3 = new CoordinateSequenceComparator(3);
    private static CoordinateSequenceComparator COMP4 = new CoordinateSequenceComparator(4);

    @Test
    public void testPointGeometries() throws ParseException, IOException {
        checkTWKBGeometry("01000204", "POINT(1 2)");
        checkTWKBGeometry("01080302040608", "POINT(1 2 3)", 3);

        // Written with precision = 5
        checkTWKBGeometry("a100c09a0c80b518", "POINT(1 2)");
        checkTWKBGeometry("a10080a8d6b90780d0acf30e", "POINT(10000 20000)");

        // With bounding boxes
        checkTWKBGeometry("0101020004000204", "POINT(1 2)");
        checkTWKBGeometry("010903020004000600080002040608", "POINT(1 2 3)", 3);
    }

    @Test
    public void testGeometriesWithDecimalDigits() throws ParseException, IOException {
        checkTWKBGeometry(
                "020002b88f75ba928c061600", "LINESTRING(959452 6390941,959463 6390941)", 2);

        checkTWKBGeometry(
                "220002b89a9309d6b8f93ce40103",
                "LINESTRING(959452.4 6390941.9,959463.8 6390941.7)",
                2);

        checkTWKBGeometry(
                "420002c088c05be6b6bee104ea1127",
                "LINESTRING(959452.48 6390941.95,959463.89 6390941.75)",
                2);

        checkTWKBGeometry(
                "62000286d58093078ea4f0ce2fa2b201a103",
                "LINESTRING(959452.483 6390941.959,959463.892 6390941.750)",
                2);
    }

    @Test
    public void testNonPointGeometries() throws ParseException, IOException {
        checkTWKBGeometry("a20002c09a0c80b51880b51880b518", "LINESTRING(1 2, 3 4)");
        checkTWKBGeometry(
                "a3000104c09a0c80b51880b51880b51880b51880b518ffe930ffe930",
                "POLYGON((1 2,3 4,5 6,1 2))");
        checkTWKBGeometry(
                "a3000204c09a0c80b51880b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe930",
                "POLYGON((1 2,3 4,5 6,1 2),(11 12,13 14,15 16,11 12))");
        checkTWKBGeometry(
                "a3000304c09a0c80b51880b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe930",
                "POLYGON((1 2,3 4,5 6,1 2),(11 12,13 14,15 16,11 12),(21 22,23 24,25 26,21 22))");

        checkTWKBGeometry("a40001c09a0c80b518", "MULTIPOINT(1 2)");
        checkTWKBGeometry(
                "a3000104c09a0c80b51880b51880b51880b51880b518ffe930ffe930",
                "POLYGON((1 2,3 4,5 6,1 2))");
        checkTWKBGeometry(
                "a3000204c09a0c80b51880b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe930",
                "POLYGON((1 2,3 4,5 6,1 2),(11 12,13 14,15 16,11 12))");
        checkTWKBGeometry(
                "a3000304c09a0c80b51880b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe930",
                "POLYGON((1 2,3 4,5 6,1 2),(11 12,13 14,15 16,11 12),(21 22,23 24,25 26,21 22))");
    }

    @Test
    public void testTWKB() throws ParseException, IOException {
        checkTWKBGeometry("a110", "POINT EMPTY");
        checkTWKBGeometry("a100c09a0c80b518", "POINT(1 2)");
        checkTWKBGeometry("a210", "LINESTRING EMPTY");
        checkTWKBGeometry("a20002c09a0c80b51880b51880b518", "LINESTRING(1 2,3 4)");
        checkTWKBGeometry("a310", "POLYGON EMPTY");
        checkTWKBGeometry(
                "a3000104c09a0c80b51880b51880b51880b51880b518ffe930ffe930",
                "POLYGON((1 2,3 4,5 6,1 2))");
        checkTWKBGeometry(
                "a3000204c09a0c80b51880b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe930",
                "POLYGON((1 2,3 4,5 6,1 2),(11 12,13 14,15 16,11 12))");
        checkTWKBGeometry(
                "a3000304c09a0c80b51880b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe930",
                "POLYGON((1 2,3 4,5 6,1 2),(11 12,13 14,15 16,11 12),(21 22,23 24,25 26,21 22))");
        checkTWKBGeometry("a410", "MULTIPOINT EMPTY");
        checkTWKBGeometry("a40001c09a0c80b518", "MULTIPOINT(1 2)");
        checkTWKBGeometry("a40002c09a0c80b51880b51880b518", "MULTIPOINT(1 2,3 4)");
        checkTWKBGeometry("a510", "MULTILINESTRING EMPTY");
        checkTWKBGeometry("a5000102c09a0c80b51880b51880b518", "MULTILINESTRING((1 2,3 4))");
        checkTWKBGeometry(
                "a5000202c09a0c80b51880b51880b5180280b51880b51880b51880b518",
                "MULTILINESTRING((1 2,3 4),(5 6,7 8))");
        checkTWKBGeometry("a610", "MULTIPOLYGON EMPTY");
        checkTWKBGeometry(
                "a600010104c09a0c80b51880b51880b51880b51880b518ffe930ffe930",
                "MULTIPOLYGON(((1 2,3 4,5 6,1 2)))");
        checkTWKBGeometry(
                "a600020104c09a0c80b51880b51880b51880b51880b518ffe930ffe9300304000080b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe930",
                "MULTIPOLYGON(((1 2,3 4,5 6,1 2)),((1 2,3 4,5 6,1 2),(11 12,13 14,15 16,11 12),(21 22,23 24,25 26,21 22)))");
        checkTWKBGeometry("a710", "GEOMETRYCOLLECTION EMPTY");
        checkTWKBGeometry("a70001a100c09a0c80b518", "GEOMETRYCOLLECTION(POINT(1 2))");
        checkTWKBGeometry(
                "a70002a100c09a0c80b518a20002c09a0c80b51880b51880b518",
                "GEOMETRYCOLLECTION(POINT(1 2),LINESTRING(1 2,3 4))");
        checkTWKBGeometry(
                "a70003a100c09a0c80b518a20002c09a0c80b51880b51880b518a3000304c09a0c80b51880b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe9300480897a80897a80b51880b51880b51880b518ffe930ffe930",
                "GEOMETRYCOLLECTION(POINT(1 2),LINESTRING(1 2,3 4),POLYGON((1 2,3 4,5 6,1 2),(11 12,13 14,15 16,11 12),(21 22,23 24,25 26,21 22)))");
    }

    @Test
    public void testTWKBM() throws ParseException, IOException {
        checkTWKBGeometry("a11802", "POINT M EMPTY", 3);
        checkTWKBGeometry("a10802c09a0c80b51806", "POINT M (1 2 3)", 3);
        checkTWKBGeometry("a21802", "LINESTRING M EMPTY", 3);
        checkTWKBGeometry("a2080202c09a0c80b5180280b51880b51802", "LINESTRING M (1 2 1,3 4 2)", 3);
        checkTWKBGeometry("a31802", "POLYGON M EMPTY", 3);
        checkTWKBGeometry(
                "a308020104c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe93003",
                "POLYGON M ((1 2 1,3 4 2,5 6 3,1 2 1))",
                3);
        checkTWKBGeometry(
                "a308020204c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe93003",
                "POLYGON M ((1 2 1,3 4 2,5 6 3,1 2 1),(11 12 4,13 14 5,15 16 6,11 12 4))",
                3);
        checkTWKBGeometry(
                "a308020304c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe93003",
                "POLYGON M ((1 2 1,3 4 2,5 6 3,1 2 1),(11 12 4,13 14 5,15 16 6,11 12 4),(21 22 7,23 24 8,25 26 9,21 22 7))",
                3);
        checkTWKBGeometry("a41802", "MULTIPOINT M EMPTY", 3);
        checkTWKBGeometry("a4080201c09a0c80b51802", "MULTIPOINT M (1 2 1)", 3);
        checkTWKBGeometry("a4080202c09a0c80b5180280b51880b51802", "MULTIPOINT M (1 2 1,3 4 2)", 3);
        checkTWKBGeometry("a51802", "MULTILINESTRING M EMPTY", 3);
        checkTWKBGeometry(
                "a508020102c09a0c80b5180280b51880b51802", "MULTILINESTRING M ((1 2 1,3 4 2))", 3);
        checkTWKBGeometry(
                "a508020202c09a0c80b5180280b51880b518020280b51880b5180280b51880b51802",
                "MULTILINESTRING M ((1 2 1,3 4 2),(5 6 3,7 8 4))",
                3);
        checkTWKBGeometry("a61802", "MULTIPOLYGON M EMPTY", 3);
        checkTWKBGeometry(
                "a60802010104c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe93003",
                "MULTIPOLYGON M (((1 2 1,3 4 2,5 6 3,1 2 1)))",
                3);
        checkTWKBGeometry(
                "a60802020104c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe93003030400000080b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe93003",
                "MULTIPOLYGON M (((1 2 1,3 4 2,5 6 3,1 2 1)),((1 2 1,3 4 2,5 6 3,1 2 1),(11 12 4,"
                        + "13 14 5,15 16 6,11 12 4),(21 22 7,23 24 8,25 26 9,21 22 7)))",
                3);
        checkTWKBGeometry("a71802", "GEOMETRYCOLLECTION M EMPTY", 3);
        checkTWKBGeometry(
                "a7080201a10802c09a0c80b51802", "GEOMETRYCOLLECTION M (POINT M (1 2 1))", 3);
        checkTWKBGeometry(
                "a7080202a10802c09a0c80b51802a2080202c09a0c80b5180280b51880b51802",
                "GEOMETRYCOLLECTION M (POINT M (1 2 1),LINESTRING M (1 2 1,3 4 2))",
                3);
        checkTWKBGeometry(
                "a7080203a10802c09a0c80b51802a2080202c09a0c80b5180280b51880b51802a308020304c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe93003",
                "GEOMETRYCOLLECTION M (POINT M (1 2 1),LINESTRING M (1 2 1,3 4 2),POLYGON M ((1 2 1,3 4 2,5 6 3,1 2 1),(11 12 4,13 14 5,15 16 6,11 12 4),(21 22 7,23 24 8,25 26 9,21 22 7)))",
                3);
    }

    @Test
    public void testTWKBZ() throws ParseException, IOException {
        checkTWKBGeometry("a11801", "POINT Z EMPTY", 3);
        checkTWKBGeometry("a10801c09a0c80b51806", "POINT Z (1 2 3)", 3);
        checkTWKBGeometry("a21801", "LINESTRING Z EMPTY", 3);
        checkTWKBGeometry("a2080102c09a0c80b5180280b51880b51802", "LINESTRING Z (1 2 1,3 4 2)", 3);
        checkTWKBGeometry("a31801", "POLYGON Z EMPTY", 3);
        checkTWKBGeometry(
                "a308010104c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe93003",
                "POLYGON Z ((1 2 1,3 4 2,5 6 3,1 2 1))",
                3);
        checkTWKBGeometry(
                "a308010204c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe93003",
                "POLYGON Z ((1 2 1,3 4 2,5 6 3,1 2 1),(11 12 4,13 14 5,15 16 6,11 12 4))",
                3);
        checkTWKBGeometry(
                "a308010304c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe93003",
                "POLYGON Z ((1 2 1,3 4 2,5 6 3,1 2 1),(11 12 4,13 14 5,15 16 6,11 12 4),(21 22 7,23 24 8,25 26 9,21 22 7))",
                3);
        checkTWKBGeometry("a41801", "MULTIPOINT Z EMPTY", 3);
        checkTWKBGeometry("a4080101c09a0c80b51802", "MULTIPOINT Z (1 2 1)", 3);
        checkTWKBGeometry("a4080102c09a0c80b5180280b51880b51802", "MULTIPOINT Z (1 2 1,3 4 2)", 3);
        checkTWKBGeometry("a51801", "MULTILINESTRING Z EMPTY", 3);
        checkTWKBGeometry(
                "a508010102c09a0c80b5180280b51880b51802", "MULTILINESTRING Z ((1 2 1,3 4 2))", 3);
        checkTWKBGeometry(
                "a508010202c09a0c80b5180280b51880b518020280b51880b5180280b51880b51802",
                "MULTILINESTRING Z ((1 2 1,3 4 2),(5 6 3,7 8 4))",
                3);
        checkTWKBGeometry("a61801", "MULTIPOLYGON Z EMPTY", 3);
        checkTWKBGeometry(
                "a60801010104c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe93003",
                "MULTIPOLYGON Z (((1 2 1,3 4 2,5 6 3,1 2 1)))",
                3);
        checkTWKBGeometry(
                "a60801020104c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe93003030400000080b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe93003",
                "MULTIPOLYGON Z (((1 2 1,3 4 2,5 6 3,1 2 1)),((1 2 1,3 4 2,5 6 3,1 2 1),(11 12 4,13 14 5,15 16 6,11 12 4),(21 22 7,23 24 8,25 26 9,21 22 7)))",
                3);
        checkTWKBGeometry("a71801", "GEOMETRYCOLLECTION Z EMPTY", 3);
        checkTWKBGeometry(
                "a7080101a10801c09a0c80b51802", "GEOMETRYCOLLECTION Z (POINT Z (1 2 1))", 3);
        checkTWKBGeometry(
                "a7080102a10801c09a0c80b51802a2080102c09a0c80b5180280b51880b51802",
                "GEOMETRYCOLLECTION Z (POINT Z (1 2 1),LINESTRING Z (1 2 1,3 4 2))",
                3);
        checkTWKBGeometry(
                "a7080103a10801c09a0c80b51802a2080102c09a0c80b5180280b51880b51802a308010304c09a0c80b5180280b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe930030480897a80897a0680b51880b5180280b51880b51802ffe930ffe93003",
                "GEOMETRYCOLLECTION Z (POINT Z (1 2 1),LINESTRING Z (1 2 1,3 4 2),POLYGON Z ((1 2 1,3 4 2,5 6 3,1 2 1),(11 12 4,13 14 5,15 16 6,11 12 4),(21 22 7,23 24 8,25 26 9,21 22 7)))",
                3);
    }

    @Test
    public void testTWKBZM() throws ParseException, IOException {
        checkTWKBGeometry("a11803", "POINT ZM EMPTY", 4);
        checkTWKBGeometry("a10803c09a0c80b5180608", "POINT ZM (1 2 3 4)", 4);
        checkTWKBGeometry("a21803", "LINESTRING ZM EMPTY", 4);
        checkTWKBGeometry(
                "a2080302c09a0c80b518020480b51880b5180404", "LINESTRING ZM (1 2 1 2,3 4 3 4)", 4);
        checkTWKBGeometry("a31803", "POLYGON ZM EMPTY", 4);

        checkTWKBGeometry(
                "a308030104c09a0c80b518020480b51880b518040480b51880b5180404ffe930ffe9300707",
                "POLYGON ZM ((1 2 1 2,3 4 3 4,5 6 5 6,1 2 1 2))",
                4);

        checkTWKBGeometry(
                "a308030204c09a0c80b518020480b51880b518040480b51880b5180404ffe930ffe93007070480897a80897a141480b51880b518040480b51880b5180404ffe930ffe9300707",
                "POLYGON ZM ((1 2 1 2,3 4 3 4,5 6 5 6,1 2 1 2),(11 12 11 12,13 14 13 14,15 16 15 16,11 12 11 12))",
                4);

        checkTWKBGeometry(
                "a308030304c09a0c80b518020480b51880b518040480b51880b5180404ffe930ffe93007070480897a80897a141480b51880b518040480b51880b5180404ffe930ffe93007070480897a80897a141480b51880b518040480b51880b5180404ffe930ffe9300707",
                "POLYGON ZM ((1 2 1 2,3 4 3 4,5 6 5 6,1 2 1 2),(11 12 11 12,13 14 13 14,15 16 15 16,11 12 11 12),(21 22 21 22,23 24 23 24,25 26 25 26,21 22 21 22))",
                4);
        checkTWKBGeometry("a41803", "MULTIPOINT ZM EMPTY", 4);
        checkTWKBGeometry("a4080301c09a0c80b5180204", "MULTIPOINT ZM (1 2 1 2)", 4);
        checkTWKBGeometry(
                "a4080302c09a0c80b518020480b51880b5180404", "MULTIPOINT ZM (1 2 1 2,3 4 3 4)", 4);
        checkTWKBGeometry("a51803", "MULTILINESTRING ZM EMPTY", 4);
        checkTWKBGeometry(
                "a508030102c09a0c80b518020480b51880b5180404",
                "MULTILINESTRING ZM((1 2 1 2,3 4 3 4))",
                4);

        checkTWKBGeometry(
                "a508030202c09a0c80b518020480b51880b51804040280b51880b518040480b51880b5180404",
                "MULTILINESTRING ZM ((1 2 1 2,3 4 3 4),(5 6 5 6,7 8 7 8))",
                4);
        checkTWKBGeometry("a61803", "MULTIPOLYGON ZM EMPTY", 4);

        checkTWKBGeometry(
                "a60803010104c09a0c80b518020480b51880b518040480b51880b5180404ffe930ffe9300707",
                "MULTIPOLYGON ZM (((1 2 1 2,3 4 3 4,5 6 5 6,1 2 1 2)))",
                4);

        checkTWKBGeometry(
                "a60803020104c09a0c80b518020480b51880b518040480b51880b5180404ffe930ffe930070703040000000080b51880b518040480b51880b5180404ffe930ffe93007070480897a80897a141480b51880b518040480b51880b5180404ffe930ffe93007070480897a80897a141480b51880b518040480b51880b5180404ffe930ffe9300707",
                "MULTIPOLYGON ZM (((1 2 1 2,3 4 3 4,5 6 5 6,1 2 1 2)),((1 2 1 2,3 4 3 4,5 6 5 6,1 2 1 2),(11 12 11 12,13 14 13 14,15 16 15 16,11 12 11 12),(21 22 21 22,23 24 23 24,25 26 25 26,21 22 21 22)))",
                4);
        checkTWKBGeometry("a71803", "GEOMETRYCOLLECTION ZM EMPTY", 4);
        checkTWKBGeometry(
                "a7080301a10803c09a0c80b5180204", "GEOMETRYCOLLECTION ZM (POINT ZM(1 2 1 2))", 4);

        checkTWKBGeometry(
                "a7080302a10803c09a0c80b5180204a2080302c09a0c80b518020480b51880b5180404",
                "GEOMETRYCOLLECTION ZM (POINT ZM (1 2 1 2),LINESTRING ZM (1 2 1 2,3 4 3 4))",
                4);

        checkTWKBGeometry(
                "a7080303a10803c09a0c80b5180204a2080302c09a0c80b518020480b51880b5180404a308030304c09a0c80b518020480b51880b518040480b51880b5180404ffe930ffe93007070480897a80897a141480b51880b518040480b51880b5180404ffe930ffe93007070480897a80897a141480b51880b518040480b51880b5180404ffe930ffe9300707",
                "GEOMETRYCOLLECTION ZM (POINT ZM (1 2 1 2),LINESTRING ZM (1 2 1 2,3 4 3 4),POLYGON ZM ((1 2 1 2,3 4 3 4,5 6 5 6,1 2 1 2),(11 12 11 12,13 14 13 14,15 16 15 16,11 12 11 12),(21 22 21 22,23 24 23 24,25 26 25 26,21 22 21 22)))",
                4);
    }

    @Test
    public void testZMDifferentPrecisions() throws IOException, ParseException {
        // precisions as 3, 2, 1
        checkTWKBGeometry(
                "67082b0361082bd00fa01fc8012862082b02d00fa01fc80128a01fa01f90032863082b0304d00fa01fc80128a01fa01f900328a01fa01f900328bf3ebf3e9f064f04a09c01a09c01d00fc801a01fa01f900328a01fa01f900328bf3ebf3e9f064f04a09c01a09c01d00fc801a01fa01f900328a01fa01f900328bf3ebf3e9f064f",
                "GEOMETRYCOLLECTION ZM (POINT ZM (1 2 1 2),LINESTRING ZM (1 2 1 2,3 4 3 4),POLYGON ZM ((1 2 1 2,3 4 3 4,5 6 5 6,1 2 1 2),(11 12 11 12,13 14 13 14,15 16 15 16,11 12 11 12),(21 22 21 22,23 24 23 24,25 26 25 26,21 22 21 22)))",
                4);
    }

    private void checkTWKBGeometry(String wkbHex, String expectedWKT)
            throws IOException, ParseException {
        checkTWKBGeometry(wkbHex, expectedWKT, 2);
    }

    private void checkTWKBGeometry(String wkbHex, String expectedWKT, int dimension)
            throws ParseException, IOException {
        GeometryFactory geomFactory = new GeometryFactory(new LiteCoordinateSequenceFactory());
        WKTReader rdr = new WKTReader(geomFactory);
        TWKBReader twkbReader = new TWKBReader(geomFactory);
        byte[] wkb = WKBReader.hexToBytes(wkbHex);
        Geometry actual = twkbReader.read(wkb);

        Geometry expected = rdr.read(expectedWKT);

        CoordinateSequenceComparator comp = null;
        switch (dimension) {
            case 2:
                comp = COMP2;
                break;
            case 3:
                comp = COMP3;
                break;
            case 4:
                comp = COMP4;
                break;

            default:
                throw new RuntimeException("Never gonna get here!");
        }

        assertEquals(
                "\nExpected:" + expected + "\nActual:  " + actual,
                0,
                expected.compareTo(actual, comp));
    }
}
