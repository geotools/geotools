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
 *    This file is based on an origional contained in the GISToolkit project:
 *    http://gistoolkit.sourceforge.net/
 */
package org.geotools.data.shapefile.dbf;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.TimeZone;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.util.NIOUtilities;

/**
 * A DbaseFileReader is used to read a dbase III format file. <br>
 * The general use of this class is: <CODE><PRE>
 * FileChannel in = new FileInputStream(&quot;thefile.dbf&quot;).getChannel();
 * DbaseFileReader r = new DbaseFileReader( in )
 * Object[] fields = new Object[r.getHeader().getNumFields()];
 * while (r.hasNext()) {
 *    r.readEntry(fields);
 *    // do stuff
 * }
 * r.close();
 * </PRE></CODE> For consumers who wish to be a bit more selective with their reading of rows, the
 * Row object has been added. The semantics are the same as using the readEntry method, but remember
 * that the Row object is always the same. The values are parsed as they are read, so it pays to
 * copy them out (as each call to Row.read() will result in an expensive String parse). <br>
 * <b>EACH CALL TO readEntry OR readRow ADVANCES THE FILE!</b><br>
 * An example of using the Row method of reading: <CODE><PRE>
 * FileChannel in = new FileInputStream(&quot;thefile.dbf&quot;).getChannel();
 * DbaseFileReader r = new DbaseFileReader( in )
 * int fields = r.getHeader().getNumFields();
 * while (r.hasNext()) {
 *   DbaseFileReader.Row row = r.readRow();
 *   for (int i = 0; i &lt; fields; i++) {
 *     // do stuff
 *     Foo.bar( row.read(i) );
 *   }
 * }
 * r.close();
 * </PRE></CODE>
 *
 * @author Ian Schneider
 * @author Tommaso Nolli
 */
public class IndexedDbaseFileReader extends DbaseFileReader {

    /** Like calling DbaseFileReader(ReadableByteChannel, true); */
    public IndexedDbaseFileReader(ShpFiles shpFiles) throws IOException {
        this(shpFiles, false);
    }

    /**
     * Creates a new instance of DBaseFileReader
     *
     * @param useMemoryMappedBuffer Wether or not map the file in memory
     * @throws IOException If an error occurs while initializing.
     */
    public IndexedDbaseFileReader(ShpFiles shpFiles, boolean useMemoryMappedBuffer)
            throws IOException {
        super(
                shpFiles,
                useMemoryMappedBuffer,
                (Charset) ShapefileDataStoreFactory.DBFCHARSET.getDefaultValue(),
                TimeZone.getDefault());
    }

    public IndexedDbaseFileReader(
            ShpFiles shpFiles, boolean useMemoryMappedBuffer, Charset stringCharset)
            throws IOException {
        super(shpFiles, useMemoryMappedBuffer, stringCharset, TimeZone.getDefault());
    }

    public IndexedDbaseFileReader(
            ShpFiles shpFiles,
            boolean useMemoryMappedBuffer,
            Charset stringCharset,
            TimeZone timeZone)
            throws IOException {
        super(shpFiles, useMemoryMappedBuffer, stringCharset, timeZone);
    }

    @SuppressWarnings("PMD.CloseResource") // this.channel is managed as a field
    public void goTo(int recno) throws IOException, UnsupportedOperationException {

        if (this.randomAccessEnabled) {
            long newPosition =
                    this.header.getHeaderLength()
                            + this.header.getRecordLength() * (long) (recno - 1);

            if (this.useMemoryMappedBuffer) {
                if (newPosition < this.currentOffset
                        || (this.currentOffset + buffer.limit())
                                < (newPosition + header.getRecordLength())) {
                    NIOUtilities.clean(buffer);
                    FileChannel fc = (FileChannel) channel;
                    if (fc.size() > newPosition + Integer.MAX_VALUE) {
                        currentOffset = newPosition;
                    } else {
                        currentOffset = fc.size() - Integer.MAX_VALUE;
                    }
                    buffer = fc.map(MapMode.READ_ONLY, currentOffset, Integer.MAX_VALUE);
                    buffer.position((int) (newPosition - currentOffset));
                } else {
                    buffer.position((int) (newPosition - currentOffset));
                }
            } else {
                if (this.currentOffset <= newPosition
                        && this.currentOffset + buffer.limit() >= newPosition) {
                    buffer.position((int) (newPosition - this.currentOffset));
                    // System.out.println("Hit");
                } else {
                    // System.out.println("Jump");
                    FileChannel fc = (FileChannel) this.channel;
                    fc.position(newPosition);
                    this.currentOffset = newPosition;
                    buffer.limit(buffer.capacity());
                    buffer.position(0);
                    fill(buffer, fc);
                    buffer.position(0);
                }
            }
        } else {
            throw new UnsupportedOperationException("Random access not enabled!");
        }
    }

    public boolean IsRandomAccessEnabled() {
        return this.randomAccessEnabled;
    }

    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args) throws Exception {
        try (IndexedDbaseFileReader reader =
                new IndexedDbaseFileReader(new ShpFiles(args[0]), false)) {
            System.out.println(reader.getHeader());
            int r = 0;
            while (reader.hasNext()) {
                System.out.println(++r + "," + java.util.Arrays.asList(reader.readEntry()));
            }
        }
    }
}
