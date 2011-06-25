package org.geotools.data.shapefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;

/**
 * Verifies that null String, Date, Boolean, Integer, Long, Float, and Double
 * types can be written and read back without losing the proper 'null' Java
 * value.
 * 
 * This is a separate test from the DbaseFileTest#testEmptyFields method
 * since it seems to be checking for something else.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/shapefile/src/test/java/org/geotools/data/shapefile/DbaseNullSupportTest.java $
 */
public class DbaseNullSupportTest extends TestCase {
    /** declare a specific charset for test portability */
    private static final Charset cs;
    private static final TimeZone tz;
    static {
        cs = Charset.forName("ISO-8859-1");
        tz = TimeZone.getTimeZone("UTC");
    }
    private static final char[] types = {'C','N','F','L','D'};
    private static final int[] sizes = {5, 9, 20, 1, 8};
    private static final int[] decimals = {0, 0, 31, 0, 0};
    /**
     * Creates some non-null values to mix in with the nulls so we have variety.
     * Be sure to use powers of two for the numbers to prevent any possible loss
     * of precision when saving/reloading.  Be sure to use a Date with no time
     * component since only the month/day/year are saved.
     */
    private static final Object[] values;
    static {
        Date date;
        try {
            // every jvm should support this, so we should never have an error
            date = new SimpleDateFormat("yyyy-MM-dd z").parse("2010-04-01 UTC");
        } catch (ParseException e) {
            date = null;
        }
        values = new Object[]{"ABCDE", 2<<20, (2<<10)+1d/(2<<4), true, date};
    }
    public static void main(String[] args) throws IOException {
        new DbaseNullSupportTest().testNulls();
    }
    public void testNulls() throws IOException {
        File tmp = File.createTempFile("test", ".dbf");
        if (!tmp.delete()) {
            throw new IllegalStateException("Unable to clear temp file");
        }
        DbaseFileHeader header = new DbaseFileHeader();
        for (int i = 0; i < types.length; i++) {
            header.addColumn(""+types[i], types[i], sizes[i], decimals[i]);
        }
        header.setNumRecords(values.length);
        FileOutputStream fos = new FileOutputStream(tmp);
        WritableByteChannel channel = fos.getChannel();
        tmp.deleteOnExit();
        DbaseFileWriter writer = new DbaseFileWriter(header, channel, cs, tz);
        // write records such that the i-th row has nulls in every column except the i-th column
        for (int row = 0; row < values.length; row++) {
            Object[] current = new Object[values.length];
            Arrays.fill(current, null);
            current[row] = values[row];
            writer.write(current);
        }
        writer.close();
        fos.flush();
        fos.close();
        DbaseFileReader reader = new DbaseFileReader(new FileInputStream(tmp).getChannel(), false, cs, tz);
        assertTrue("Number of records does not match", values.length == reader.getHeader().getNumRecords());
        for (int row = 0; row < values.length; row++) {
            Object[] current = reader.readEntry();
            assertTrue("Number of columns incorrect", current != null && current.length == values.length);
            for (int column = 0; column < values.length; column++) {
                if (column == row) {
                    assertTrue("Column was null and should not have been", current[column] != null);
                    assertTrue("Non-null column value " + current[column] +
                            " did not match original value " + values[column],
                        current[column].equals(values[column]));
                } else {
                    assertTrue("Column that should have been null was not", current[column] == null);
                }
            }
        }
    }
}
