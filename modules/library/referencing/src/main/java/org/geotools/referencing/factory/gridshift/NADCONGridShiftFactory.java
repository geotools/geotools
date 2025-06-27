/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.gridshift;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import org.geotools.api.referencing.FactoryException;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.factory.ReferencingFactory;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.URLs;
import org.geotools.util.factory.BufferedFactory;
import org.geotools.util.logging.Logging;

/**
 * Loads and caches NADCON grid shifts
 *
 * @author Andrea Aime - GeoSolutions
 */
public class NADCONGridShiftFactory extends ReferencingFactory implements BufferedFactory {

    static final class NADCONKey {
        String latFile;

        String longFile;

        public NADCONKey(String latFile, String longFile) {
            super();
            this.latFile = latFile;
            this.longFile = longFile;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (latFile == null ? 0 : latFile.hashCode());
            result = prime * result + (longFile == null ? 0 : longFile.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            NADCONKey other = (NADCONKey) obj;
            if (latFile == null) {
                if (other.latFile != null) return false;
            } else if (!latFile.equals(other.latFile)) return false;
            if (longFile == null) {
                if (other.longFile != null) return false;
            } else if (!longFile.equals(other.longFile)) return false;
            return true;
        }
    }

    /** The number of hard references to hold internally. */
    private static final int GRID_CACHE_HARD_REFERENCES = 10;

    /** Logger. */
    protected static final Logger LOGGER = Logging.getLogger(NADCONGridShiftFactory.class);

    /** The soft cache that holds loaded grids. */
    private final SoftValueHashMap<NADCONKey, NADConGridShift> gridCache;

    /** Constructs a factory with the default priority. */
    public NADCONGridShiftFactory() {
        gridCache = new SoftValueHashMap<>(GRID_CACHE_HARD_REFERENCES);
    }

    public NADConGridShift loadGridShift(URL latGridURL, URL longGridURL) throws FactoryException {
        NADCONKey key = new NADCONKey(latGridURL.toExternalForm(), longGridURL.toExternalForm());
        synchronized (gridCache) { // Prevent simultaneous threads trying to load same grid
            NADConGridShift grid = gridCache.get(key);
            if (grid != null) { // Cached:
                return grid; // - Return
            } else { // Not cached:
                grid = loadGridShiftInternal(latGridURL, longGridURL); // - Load
                if (grid != null) {
                    gridCache.put(key, grid); // - Cache
                    return grid; // - Return
                }
            }
            throw new FactoryException("NTv2 Grid " + latGridURL + ", " + longGridURL + " could not be created.");
        }
    }

    private NADConGridShift loadGridShiftInternal(URL latGridURL, URL longGridURL) throws FactoryException {
        // decide if text or binary grid will be used
        String latGridName = URLs.urlToFile(latGridURL).getPath();
        String longGridName = URLs.urlToFile(longGridURL).getPath();
        try {
            if (latGridName.endsWith(".las") && longGridName.endsWith(".los")
                    || latGridName.endsWith(".LAS") && longGridName.endsWith(".LOS")) {
                return loadBinaryGrid(latGridURL, longGridURL);
            } else if (latGridName.endsWith(".laa") && longGridName.endsWith(".loa")
                    || latGridName.endsWith(".LAA") && longGridName.endsWith(".LOA")) {
                return loadTextGrid(latGridURL, longGridURL);
            } else {
                throw new FactoryException(MessageFormat.format(
                        ErrorKeys.UNSUPPORTED_FILE_TYPE_$2,
                        latGridName.substring(latGridName.lastIndexOf('.') + 1),
                        longGridName.substring(longGridName.lastIndexOf('.') + 1)));
                // Note: the +1 above hide the dot, but also make sure that the code is
                // valid even if the path do not contains '.' at all (-1 + 1 == 0).
            }
        } catch (IOException exception) {
            final Throwable cause = exception.getCause();
            if (cause instanceof FactoryException) {
                throw (FactoryException) cause;
            }
            throw new FactoryException(exception.getLocalizedMessage(), exception);
        }
    }

    /**
     * Reads latitude and longitude binary grid shift file data into {@link grid}. The file is organized into records,
     * with the first record containing the header information, followed by the shift data. The header values are: text
     * describing grid (64 bytes), num. columns (int), num. rows (int), num. z (int), min x (float), delta x (float),
     * min y (float), delta y (float) and angle (float). Each record is num. columns 4 bytes + 4 byte separator long and
     * the file contains num. rows + 1 (for the header) records. The data records (with the grid shift values) are all
     * floats and have a 4 byte separator (0's) before the data. Row records are organized from low y (latitude) to high
     * and columns are orderd from low longitude to high. Everything is written in low byte order.
     *
     * @param latGridUrl URL to the binary latitude shift file (.las extention).
     * @param longGridUrl URL to the binary longitude shift file (.los extention).
     * @throws IOException if the data files cannot be read.
     * @throws FactoryException if there is an inconsistency in the data
     */
    private NADConGridShift loadBinaryGrid(final URL latGridUrl, final URL longGridUrl)
            throws IOException, FactoryException {
        final int HEADER_BYTES = 96;
        final int SEPARATOR_BYTES = 4;
        final int DESCRIPTION_LENGTH = 64;
        NADConGridShift gridShift = null;
        ByteBuffer latBuffer;
        ByteBuffer longBuffer;

        try (ReadableByteChannel latChannel = getReadChannel(latGridUrl);
                ReadableByteChannel longChannel = getReadChannel(longGridUrl)) {
            // //////////////////////
            // setup
            // //////////////////////
            latBuffer = fillBuffer(latChannel, HEADER_BYTES);
            longBuffer = fillBuffer(longChannel, HEADER_BYTES);

            // //////////////////////
            // read header info
            // //////////////////////
            // skip the header description
            latBuffer.position(latBuffer.position() + DESCRIPTION_LENGTH);

            int nc = latBuffer.getInt();
            int nr = latBuffer.getInt();
            int nz = latBuffer.getInt();

            float xmin = latBuffer.getFloat();
            float dx = latBuffer.getFloat();
            float ymin = latBuffer.getFloat();
            float dy = latBuffer.getFloat();

            float angle = latBuffer.getFloat();
            float xmax = xmin + (nc - 1) * dx;
            float ymax = ymin + (nr - 1) * dy;

            // skip the longitude header description
            longBuffer.position(longBuffer.position() + DESCRIPTION_LENGTH);

            // check that latitude grid header is the same as for latitude grid
            if (nc != longBuffer.getInt()
                    || nr != longBuffer.getInt()
                    || nz != longBuffer.getInt()
                    || xmin != longBuffer.getFloat()
                    || dx != longBuffer.getFloat()
                    || ymin != longBuffer.getFloat()
                    || dy != longBuffer.getFloat()
                    || angle != longBuffer.getFloat()) {
                throw new FactoryException(ErrorKeys.GRID_LOCATIONS_UNEQUAL);
            }

            // //////////////////////
            // read grid shift data into LocalizationGrid
            // //////////////////////
            final int RECORD_LENGTH = nc * 4 + SEPARATOR_BYTES;
            final int NUM_BYTES_LEFT = (nr + 1) * RECORD_LENGTH - HEADER_BYTES;
            final int START_OF_DATA = RECORD_LENGTH - HEADER_BYTES;

            latBuffer = fillBuffer(latChannel, NUM_BYTES_LEFT);
            latBuffer.position(START_OF_DATA); // start of second record (data)

            longBuffer = fillBuffer(longChannel, NUM_BYTES_LEFT);
            longBuffer.position(START_OF_DATA);

            gridShift = new NADConGridShift(xmin, ymin, xmax, ymax, dx, dy, nc, nr);

            int i = 0;
            int j = 0;
            for (i = 0; i < nr; i++) {
                latBuffer.position(latBuffer.position() + SEPARATOR_BYTES); // skip record separator
                longBuffer.position(longBuffer.position() + SEPARATOR_BYTES);

                for (j = 0; j < nc; j++) {
                    gridShift.setLocalizationPoint(j, i, longBuffer.getFloat(), latBuffer.getFloat());
                }
            }

            assert i == nr : i;
            assert j == nc : j;
        }

        return gridShift;
    }

    /**
     * Returns a new bytebuffer, of numBytes length and little endian byte order, filled from the channel.
     *
     * @param channel the channel to fill the buffer from
     * @param numBytes number of bytes to read
     * @return a new bytebuffer filled from the channel
     * @throws IOException if there is a problem reading the channel
     * @throws EOFException if the end of the channel is reached
     */
    private ByteBuffer fillBuffer(ReadableByteChannel channel, int numBytes) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(numBytes);

        if (fill(buf, channel) == -1) {
            throw new EOFException(ErrorKeys.END_OF_DATA_FILE);
        }

        buf.flip();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        return buf;
    }

    /**
     * Fills the bytebuffer from the channel. Code was lifted from ShapefileDataStore.
     *
     * @param buffer bytebuffer to fill from the channel
     * @param channel channel to fill the buffer from
     * @return number of bytes read
     * @throws IOException if there is a problem reading the channel
     */
    private int fill(ByteBuffer buffer, ReadableByteChannel channel) throws IOException {
        int r = buffer.remaining();

        // channel reads return -1 when EOF or other error
        // because they a non-blocking reads, 0 is a valid return value!!
        while (buffer.remaining() > 0 && r != -1) {
            r = channel.read(buffer);
        }

        if (r == -1) {
            buffer.limit(buffer.position());
        }

        return r;
    }

    /**
     * Obtain a ReadableByteChannel from the given URL. If the url protocol is file, a FileChannel will be returned.
     * Otherwise a generic channel will be obtained from the urls input stream. Code swiped from ShapefileDataStore.
     *
     * @param url URL to create the channel from
     * @return a new PeadableByteChannel from the input url
     * @throws IOException if there is a problem creating the channel
     */
    @SuppressWarnings("PMD.CloseResource") // returns a Channel, cannot close its input stream
    private ReadableByteChannel getReadChannel(URL url) throws IOException {
        ReadableByteChannel channel = null;

        if (url.getProtocol().equals("file")) {
            File file = URLs.urlToFile(url);

            if (!file.exists() || !file.canRead()) {
                throw new IOException(MessageFormat.format(ErrorKeys.FILE_DOES_NOT_EXIST_$1, file));
            }

            FileInputStream in = new FileInputStream(file);
            channel = in.getChannel();
        } else {
            InputStream in = url.openConnection().getInputStream();
            channel = Channels.newChannel(in);
        }

        return channel;
    }

    /**
     * Reads latitude and longitude text grid shift file data into {@link grid}. The first two lines of the shift data
     * file contain the header, with the first being a description of the grid. The second line contains 8 values
     * separated by spaces: num. columns, num. rows, num. z, min x, delta x, min y, delta y and angle. Shift data values
     * follow this and are also separated by spaces. Row records are organized from low y (latitude) to high and columns
     * are orderd from low longitude to high.
     *
     * @param latGridUrl URL to the text latitude shift file (.laa extention).
     * @param longGridUrl URL to the text longitude shift file (.loa extention).
     * @throws IOException if the data files cannot be read.
     * @throws FactoryException if there is an inconsistency in the data
     */
    private NADConGridShift loadTextGrid(URL latGridUrl, URL longGridUrl) throws IOException, FactoryException {
        String latLine;
        String longLine;
        StringTokenizer latSt;
        StringTokenizer longSt;

        // //////////////////////
        // setup
        // //////////////////////
        try (BufferedReader latBr =
                        new BufferedReader(new InputStreamReader(latGridUrl.openStream(), StandardCharsets.UTF_8));
                BufferedReader longBr =
                        new BufferedReader(new InputStreamReader(longGridUrl.openStream(), StandardCharsets.UTF_8))) {
            // //////////////////////
            // read header info
            // //////////////////////
            latBr.readLine(); // skip header description
            latLine = latBr.readLine();
            if (latLine == null) {
                throw new IOException("Invalid lat grid file, does not contain a grid");
            }
            latSt = new StringTokenizer(latLine, " ");

            if (latSt.countTokens() != 8) {
                final Object arg0 = String.valueOf(latSt.countTokens());
                throw new FactoryException(MessageFormat.format(ErrorKeys.HEADER_UNEXPECTED_LENGTH_$1, arg0));
            }

            int nc = Integer.parseInt(latSt.nextToken());
            int nr = Integer.parseInt(latSt.nextToken());
            int nz = Integer.parseInt(latSt.nextToken());

            float xmin = Float.parseFloat(latSt.nextToken());
            float dx = Float.parseFloat(latSt.nextToken());
            float ymin = Float.parseFloat(latSt.nextToken());
            float dy = Float.parseFloat(latSt.nextToken());

            float angle = Float.parseFloat(latSt.nextToken());
            float xmax = xmin + (nc - 1) * dx;
            float ymax = ymin + (nr - 1) * dy;

            // now read long shift grid
            longBr.readLine(); // skip header description
            longLine = longBr.readLine();
            if (longLine == null) {
                throw new IOException("Invalid lon grid file, does not contain a grid");
            }
            longSt = new StringTokenizer(longLine, " ");

            if (longSt.countTokens() != 8) {
                final Object arg0 = String.valueOf(longSt.countTokens());
                throw new FactoryException(MessageFormat.format(ErrorKeys.HEADER_UNEXPECTED_LENGTH_$1, arg0));
            }

            // check that latitude grid header is the same as for latitude grid
            if (nc != Integer.parseInt(longSt.nextToken())
                    || nr != Integer.parseInt(longSt.nextToken())
                    || nz != Integer.parseInt(longSt.nextToken())
                    || xmin != Float.parseFloat(longSt.nextToken())
                    || dx != Float.parseFloat(longSt.nextToken())
                    || ymin != Float.parseFloat(longSt.nextToken())
                    || dy != Float.parseFloat(longSt.nextToken())
                    || angle != Float.parseFloat(longSt.nextToken())) {
                throw new FactoryException(ErrorKeys.GRID_LOCATIONS_UNEQUAL);
            }

            // //////////////////////
            // read grid shift data into LocalizationGrid
            // //////////////////////
            NADConGridShift gridShift = new NADConGridShift(xmin, ymin, xmax, ymax, dx, dy, nc, nr);

            int i = 0;
            int j = 0;
            for (i = 0; i < nr; i++) {
                for (j = 0; j < nc; ) {
                    latLine = latBr.readLine();
                    if (latLine == null) {
                        throw new IOException("Was expecting one more line in the lat file");
                    }
                    latSt = new StringTokenizer(latLine, " ");
                    longLine = longBr.readLine();
                    if (longLine == null) {
                        throw new IOException("Was expecting one more line in the lat file");
                    }
                    longSt = new StringTokenizer(longLine, " ");

                    while (latSt.hasMoreTokens() && longSt.hasMoreTokens()) {
                        gridShift.setLocalizationPoint(
                                j, i, Float.parseFloat(longSt.nextToken()), Float.parseFloat(latSt.nextToken()));
                        ++j;
                    }
                }
            }

            assert i == nr : i;
            assert j == nc : j;

            return gridShift;
        }
    }
}
