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
package org.geotools.data.shapefile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.TestData;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;

/**
 * 
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/test/java/org/geotools/data/shapefile/DbaseFileTest.java $
 * @version $Id$
 * @author Ian Schneider
 * @author James Macgill
 */
public class DbaseFileTest extends TestCaseSupport {

    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.shapefile");

    static final String TEST_FILE = "shapes/statepop.dbf";

    private DbaseFileReader dbf = null;

    private ShpFiles shpFiles;

    public DbaseFileTest(String testName) throws IOException {
        super(testName);
    }

    public static void main(String[] args) {
        // verbose = true;
        junit.textui.TestRunner.run(suite(DbaseFileTest.class));
    }

    protected void setUp() throws Exception {
        super.setUp();
        shpFiles = new ShpFiles(TestData.url(TEST_FILE));
        dbf = new DbaseFileReader(shpFiles, false,
                ShapefileDataStore.DEFAULT_STRING_CHARSET);
    }

    public void testNumberofColsLoaded() {
        assertEquals("Number of attributes found incorect", 252, dbf
                .getHeader().getNumFields());
    }

    protected void tearDown() throws Exception {
        dbf.close();
        super.tearDown();
    }

    public void testNumberofRowsLoaded() {
        assertEquals("Number of rows", 49, dbf.getHeader().getNumRecords());
    }

    public void testDataLoaded() throws Exception {
        Object[] attrs = new Object[dbf.getHeader().getNumFields()];
        dbf.readEntry(attrs);
        assertEquals("Value of Column 0 is wrong", "Illinois", attrs[0]);
        assertEquals("Value of Column 4 is wrong", 143986.61,
                ((Double) attrs[4]).doubleValue(), 0.001);
    }

    public void testRowVsEntry() throws Exception {
        Object[] attrs = new Object[dbf.getHeader().getNumFields()];
        DbaseFileReader dbf2 = new DbaseFileReader(shpFiles, false,
                ShapefileDataStore.DEFAULT_STRING_CHARSET);
        while (dbf.hasNext()) {
            dbf.readEntry(attrs);
            DbaseFileReader.Row r = dbf2.readRow();
            for (int i = 0, ii = attrs.length; i < ii; i++) {
                assertNotNull(attrs[i]);
                assertNotNull(r.read(i));
                assertEquals(attrs[i], r.read(i));
            }
        }
        dbf2.close();
    }

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
            assertTrue(length != header.getRecordLength());
            header.addColumn("emptyDate", 'D', 20, 0);
            assertTrue(length == header.getRecordLength());
            header.removeColumn("billy");
            assertTrue(length == header.getRecordLength());
        } finally {
            LOGGER.setLevel(before);
        }
    }

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

    public void testEmptyFields() throws Exception {
        DbaseFileHeader header = new DbaseFileHeader();
        header.addColumn("emptyString", 'C', 20, 0);
        header.addColumn("emptyInt", 'N', 20, 0);
        header.addColumn("emptyDouble", 'N', 20, 5);
        header.addColumn("emptyFloat", 'F', 20, 5);
        header.addColumn("emptyLogical", 'L', 1, 0);
        header.addColumn("emptyDate", 'D', 20, 0);
        header.setNumRecords(20);
        File f = new File(System.getProperty("java.io.tmpdir"),
                "scratchDBF.dbf");
        f.deleteOnExit();
        FileOutputStream fout = new FileOutputStream(f);
        DbaseFileWriter dbf = new DbaseFileWriter(header, fout.getChannel(), Charset.defaultCharset());
        for (int i = 0; i < header.getNumRecords(); i++) {
            dbf.write(new Object[6]);
        }
        dbf.close();
        ShpFiles tempShpFiles = new ShpFiles(f);
        DbaseFileReader r = new DbaseFileReader(tempShpFiles, false,
                ShapefileDataStore.DEFAULT_STRING_CHARSET);
        int cnt = 0;
        while (r.hasNext()) {
            cnt++;
            Object[] o = r.readEntry();
            assertTrue(o.length == r.getHeader().getNumFields());
        }
        assertEquals("Bad number of records", cnt, 20);
        r.close(); // make sure the channel is closed
        f.delete();
    }

    public void testFieldFormatter() throws Exception {
        DbaseFileWriter.FieldFormatter formatter = new DbaseFileWriter.FieldFormatter(Charset.defaultCharset());

        String stringWithInternationChars = "hello " + '\u20ac';
        // if (verbose) {
        // System.out.println(stringWithInternationChars);
        // }
        String formattedString = formatter.getFieldString(10,
                stringWithInternationChars);

        assertEquals("          ".getBytes().length,
                formattedString.getBytes().length);

        // test when the string is too big.
        stringWithInternationChars = '\u20ac' + "1234567890";
        formattedString = formatter.getFieldString(10,
                stringWithInternationChars);

        assertEquals("          ".getBytes().length,
                formattedString.getBytes().length);

    }

}
