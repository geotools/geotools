/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.data.shapefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.geotools.TestData;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.files.ShpFiles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @version $Id$
 * @author Ian Schneider
 * @author James Macgill
 */
public class DbaseFileTest extends TestCaseSupport {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DbaseFileTest.class);

    static final String TEST_FILE = "shapes/statepop.dbf";

    private DbaseFileReader dbf = null;

    private ShpFiles shpFiles;

    @Before
    public void setUp() throws Exception {
        shpFiles = new ShpFiles(TestData.url(TEST_FILE));
        dbf = new DbaseFileReader(shpFiles, false, ShapefileDataStore.DEFAULT_STRING_CHARSET);
    }

    @Test
    public void testNumberofColsLoaded() {
        assertEquals("Number of attributes found incorect", 252, dbf.getHeader().getNumFields());
    }

    @Override
    @After
    public void tearDown() throws Exception {
        dbf.close();
        super.tearDown();
    }

    @Test
    public void testNumberofRowsLoaded() {
        assertEquals("Number of rows", 49, dbf.getHeader().getNumRecords());
    }

    @Test
    public void testDataLoaded() throws Exception {
        Object[] attrs = new Object[dbf.getHeader().getNumFields()];
        dbf.readEntry(attrs);
        assertEquals("Value of Column 0 is wrong", "Illinois", attrs[0]);
        assertEquals("Value of Column 4 is wrong", 143986.61, ((Double) attrs[4]).doubleValue(), 0.001);
    }

    @Test
    public void testRowVsEntry() throws Exception {
        Object[] attrs = new Object[dbf.getHeader().getNumFields()];
        try (DbaseFileReader dbf2 = new DbaseFileReader(shpFiles, false, ShapefileDataStore.DEFAULT_STRING_CHARSET)) {
            while (dbf.hasNext()) {
                dbf.readEntry(attrs);
                DbaseFileReader.Row r = dbf2.readRow();
                for (int i = 0, ii = attrs.length; i < ii; i++) {
                    assertNotNull(attrs[i]);
                    assertNotNull(r.read(i));
                    assertEquals(attrs[i], r.read(i));
                }
            }
        }
    }

    @Test
    public void testHeader() throws Exception {
        DbaseFileHeader header = new DbaseFileHeader();

        Level before = LOGGER.getLevel();
        try {
            LOGGER.setLevel(Level.INFO);

            header.addColumn("emptyString", 'C', 20, 0);
            header.addColumn("emptyInt", 'N', 20, 0);
            header.addColumn("emptyDouble", 'N', 20, 5);
            header.addColumn("emptyFloat", 'F', 20, 5);
            header.addColumn("emptyLogical", 'L', 1, 0);
            header.addColumn("emptyDate", 'D', 20, 0);
            int length = header.getRecordLength();
            header.removeColumn("emptyDate");
            assertNotEquals(length, header.getRecordLength());
            header.addColumn("emptyDate", 'D', 20, 0);
            assertEquals(length, header.getRecordLength());
            header.removeColumn("billy");
            assertEquals(length, header.getRecordLength());
        } finally {
            LOGGER.setLevel(before);
        }
    }

    @Test
    public void testAddColumn() throws Exception {
        DbaseFileHeader header = new DbaseFileHeader();

        Level before = LOGGER.getLevel();
        try {
            LOGGER.setLevel(Level.INFO);

            header.addColumn("emptyInt", 'N', 9, 0);
            assertSame(Integer.class, header.getFieldClass(0));
            assertEquals(9, header.getFieldLength(0));

            header.addColumn("emptyString", 'C', 20, 0);
            assertSame(String.class, header.getFieldClass(1));
            assertEquals(20, header.getFieldLength(1));
        } finally {
            LOGGER.setLevel(before);
        }
    }

    @Test
    public void testEmptyFields() throws Exception {
        DbaseFileHeader header = new DbaseFileHeader();
        header.addColumn("emptyString", 'C', 20, 0);
        header.addColumn("emptyInt", 'N', 20, 0);
        header.addColumn("emptyDouble", 'N', 20, 5);
        header.addColumn("emptyFloat", 'F', 20, 5);
        header.addColumn("emptyLogical", 'L', 1, 0);
        header.addColumn("emptyDate", 'D', 20, 0);
        header.setNumRecords(20);
        File f = new File(System.getProperty("java.io.tmpdir"), "scratchDBF.dbf");
        f.deleteOnExit();
        try (FileOutputStream fout = new FileOutputStream(f);
                DbaseFileWriter dbf = new DbaseFileWriter(header, fout.getChannel(), Charset.defaultCharset())) {

            for (int i = 0; i < header.getNumRecords(); i++) {
                dbf.write(new Object[6]);
            }
        }
        ShpFiles tempShpFiles = new ShpFiles(f);
        try (DbaseFileReader r = new DbaseFileReader(tempShpFiles, false, ShapefileDataStore.DEFAULT_STRING_CHARSET)) {
            int cnt = 0;
            while (r.hasNext()) {
                cnt++;
                Object[] o = r.readEntry();
                assertEquals(o.length, r.getHeader().getNumFields());
            }
            assertEquals("Bad number of records", cnt, 20);
        } finally {
            f.delete();
        }
    }

    @Test
    public void testFieldFormatter() throws Exception {
        DbaseFileWriter.FieldFormatter formatter =
                new DbaseFileWriter.FieldFormatter(StandardCharsets.UTF_8, TimeZone.getDefault(), false);

        int sizeInBytes = 8;

        // A null string should result in padding
        String formattedString = formatter.getFieldString(sizeInBytes, null);
        assertEquals(sizeInBytes, formattedString.getBytes(StandardCharsets.UTF_8).length);
        assertEquals("        ", formattedString);

        // A short string will be padded
        formattedString = formatter.getFieldString(sizeInBytes, "cat");
        assertEquals(sizeInBytes, formattedString.getBytes(StandardCharsets.UTF_8).length);
        assertEquals("cat     ", formattedString);

        // A string that has the right number of bytes needs no padding
        formattedString = formatter.getFieldString(sizeInBytes, "12345678");
        assertEquals(sizeInBytes, formattedString.getBytes(StandardCharsets.UTF_8).length);
        assertEquals("12345678", formattedString);

        // larger strings get trucated
        formattedString = formatter.getFieldString(sizeInBytes, "12345678910");
        assertEquals(sizeInBytes, formattedString.getBytes(StandardCharsets.UTF_8).length);
        assertEquals("12345678", formattedString);
    }

    @Test
    public void testUTF8Chars() throws Exception {
        DbaseFileWriter.FieldFormatter formatter =
                new DbaseFileWriter.FieldFormatter(StandardCharsets.UTF_8, TimeZone.getDefault(), false);

        int sizeInBytes = 4;

        // a short string will be padded
        String formattedString = formatter.getFieldString(sizeInBytes, "\u0412");
        assertEquals(sizeInBytes, formattedString.getBytes(StandardCharsets.UTF_8).length);
        assertEquals("\u0412  ", formattedString);

        // a string of size btyes need no padding
        formattedString = formatter.getFieldString(sizeInBytes, "\u0412\u0412");
        assertEquals(sizeInBytes, formattedString.getBytes(StandardCharsets.UTF_8).length);
        assertEquals("\u0412\u0412", formattedString);

        // large strings get trucated
        formattedString = formatter.getFieldString(sizeInBytes, "\u0412\u0412\u0412");
        assertEquals(sizeInBytes, formattedString.getBytes(StandardCharsets.UTF_8).length);
        assertEquals("\u0412\u0412", formattedString);

        // if a multi-byte character is the last to be removed then padding may be required to
        formattedString = formatter.getFieldString(sizeInBytes, "\u0412A\u0412\u0412");
        assertEquals(sizeInBytes, formattedString.getBytes(StandardCharsets.UTF_8).length);
        assertEquals("\u0412A ", formattedString);
    }

    @Test
    public void testVeryLongStrings() throws Exception {
        // formatter.setFieldString will truncate input to the desired size. But it should do this
        // in a reasonably performant manner.
        DbaseFileWriter.FieldFormatter formatter =
                new DbaseFileWriter.FieldFormatter(StandardCharsets.UTF_8, TimeZone.getDefault(), false);

        // build up a very large input string. The test string is also formed so that the 8th char
        // is also multibyte
        String test = IntStream.range(0, 100000)
                .mapToObj(i -> "\u0412A cat\u0412jumped over the dog")
                .collect(Collectors.joining(","));

        String formattedString = formatter.getFieldString(8, test);
        assertEquals("\u0412A cat ", formattedString);
        assertEquals(8, formattedString.getBytes(StandardCharsets.UTF_8).length);
    }
}
