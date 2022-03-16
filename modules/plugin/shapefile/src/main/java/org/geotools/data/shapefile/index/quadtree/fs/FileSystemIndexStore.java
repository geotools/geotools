/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.index.quadtree.fs;

import static org.geotools.data.shapefile.files.ShpFileType.QIX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.shapefile.files.FileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.index.quadtree.IndexStore;
import org.geotools.data.shapefile.index.quadtree.Node;
import org.geotools.data.shapefile.index.quadtree.QuadTree;
import org.geotools.data.shapefile.index.quadtree.StoreException;
import org.geotools.data.shapefile.shp.IndexFile;
import org.locationtech.jts.geom.Envelope;

/** @author Tommaso Nolli */
public class FileSystemIndexStore implements FileReader, IndexStore {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(FileSystemIndexStore.class);
    private File file;
    private byte byteOrder;
    private ShpFiles shpFiles;

    /** Constructor. The byte order defaults to NEW_MSB_ORDER */
    public FileSystemIndexStore(File file) {
        this.file = file;
        this.byteOrder = IndexHeader.NEW_MSB_ORDER;
    }

    /** Constructor. The byte order defaults to NEW_MSB_ORDER */
    public FileSystemIndexStore(File file, ShpFiles shpFiles) {
        this.file = file;
        this.byteOrder = IndexHeader.NEW_MSB_ORDER;
        this.shpFiles = shpFiles;
    }

    /** Constructor */
    public FileSystemIndexStore(File file, byte byteOrder) {
        this.file = file;
        this.byteOrder = byteOrder;
    }

    /**
     * @see org.geotools.index.quadtree.IndexStore#dataStore(org.geotools.index.quadtree.QuadTree)
     */
    @Override
    public void store(QuadTree tree) throws StoreException {
        // For efficiency, trim the tree
        tree.trim();

        // Open the stream...

        try (FileOutputStream fos = new FileOutputStream(file);
                FileChannel channel = fos.getChannel()) {

            ByteBuffer buf = ByteBuffer.allocate(8);

            if (this.byteOrder > IndexHeader.NATIVE_ORDER) {
                LOGGER.finest("Writing file header");

                IndexHeader header = new IndexHeader(byteOrder);
                header.writeTo(buf);
                buf.flip();
                channel.write(buf);
            }

            ByteOrder order = byteToOrder(this.byteOrder);

            buf.clear();
            buf.order(order);

            buf.putInt(tree.getNumShapes());
            buf.putInt(tree.getMaxDepth());
            buf.flip();

            channel.write(buf);

            this.writeNode(tree.getRoot(), channel, order);
        } catch (IOException e) {
            throw new StoreException(e);
        }
    }

    /**
     * Wites a tree node to the qix file
     *
     * @param node The node
     * @param order byte order
     */
    private void writeNode(Node node, FileChannel channel, ByteOrder order)
            throws IOException, StoreException {
        int offset = this.getSubNodeOffset(node);

        ByteBuffer buf = ByteBuffer.allocate((4 * 8) + (3 * 4) + (node.getNumShapeIds() * 4));

        buf.order(order);
        buf.putInt(offset);

        Envelope env = node.getBounds();
        buf.putDouble(env.getMinX());
        buf.putDouble(env.getMinY());
        buf.putDouble(env.getMaxX());
        buf.putDouble(env.getMaxY());

        buf.putInt(node.getNumShapeIds());

        for (int i = 0; i < node.getNumShapeIds(); i++) {
            buf.putInt(node.getShapeId(i));
        }

        buf.putInt(node.getNumSubNodes());
        buf.flip();

        channel.write(buf);

        for (int i = 0; i < node.getNumSubNodes(); i++) {
            this.writeNode(node.getSubNode(i), channel, order);
        }
    }

    /** Calculates the offset */
    private int getSubNodeOffset(Node node) throws StoreException {
        int offset = 0;
        Node tmp = null;

        for (int i = 0; i < node.getNumSubNodes(); i++) {
            tmp = node.getSubNode(i);
            offset += (4 * 8); // Envelope size
            offset += ((tmp.getNumShapeIds() + 3) * 4); // Entries size + 3
            offset += this.getSubNodeOffset(tmp);
        }

        return offset;
    }

    /**
     * Loads a quadrtee stored in a '.qix' file. <b>WARNING:</b> The resulting quadtree will be
     * immutable; if you perform an insert, an <code>UnsupportedOperationException</code> will be
     * thrown.
     *
     * @see org.geotools.index.quadtree.IndexStore#load()
     */
    @Override
    @SuppressWarnings("PMD.CloseResource") // channel is managed in the returned value
    public QuadTree load(IndexFile indexfile, boolean useMemoryMapping) throws StoreException {
        boolean initialized = false;
        FileInputStream fis = null;
        FileChannel channel = null;
        try {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("Opening QuadTree " + this.file.getCanonicalPath());
            }
            if (shpFiles != null) {
                // the QIX file must be a local file for this class to be used so cast is safe
                channel = (FileChannel) shpFiles.getReadChannel(QIX, this);
            } else {
                // just for backwards compatibility
                fis = new FileInputStream(file);
                channel = fis.getChannel();
            }
            QuadTree tree = doLoad(fis, channel, indexfile, useMemoryMapping);
            initialized = true;
            return tree;
        } catch (IOException e) {
            throw new StoreException(e);
        } finally {
            if (!initialized) {
                try {
                    indexfile.close();
                } catch (IOException e) {
                    // do nothing
                }
                try {
                    if (channel != null) {
                        channel.close();
                    }
                } catch (IOException e) {
                    // do nothing
                }
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    private QuadTree doLoad(
            FileInputStream fis, FileChannel channel, IndexFile indexfile, boolean useMemoryMapping)
            throws IOException {
        IndexHeader header = new IndexHeader(channel);

        ByteOrder order = byteToOrder(header.getByteOrder());
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.order(order);
        channel.read(buf);
        ((Buffer) buf).flip();

        QuadTree tree =
                new QuadTree(buf.getInt(), buf.getInt(), indexfile) {
                    @Override
                    public void insert(int recno, Envelope bounds) {
                        throw new UnsupportedOperationException("File quadtrees are immutable");
                    }

                    @Override
                    public boolean trim() {
                        return false;
                    }

                    @Override
                    public void close() throws StoreException {
                        super.close();
                        try {
                            channel.close();
                            if (fis != null) {
                                fis.close();
                            }
                        } catch (IOException e) {
                            throw new StoreException(e);
                        }
                    }
                };

        tree.setRoot(FileSystemNode.readNode(0, null, channel, order, useMemoryMapping));

        LOGGER.finest("QuadTree opened");

        return tree;
    }

    /** @param order */
    private static ByteOrder byteToOrder(byte order) {
        ByteOrder ret = null;

        switch (order) {
            case IndexHeader.NATIVE_ORDER:
                ret = ByteOrder.nativeOrder();

                break;

            case IndexHeader.LSB_ORDER:
            case IndexHeader.NEW_LSB_ORDER:
                ret = ByteOrder.LITTLE_ENDIAN;

                break;

            case IndexHeader.MSB_ORDER:
            case IndexHeader.NEW_MSB_ORDER:
                ret = ByteOrder.BIG_ENDIAN;

                break;
        }

        return ret;
    }

    /** @return Returns the byteOrder. */
    public int getByteOrder() {
        return this.byteOrder;
    }

    /** @param byteOrder The byteOrder to set. */
    public void setByteOrder(byte byteOrder) {
        this.byteOrder = byteOrder;
    }

    @Override
    public String id() {
        return getClass().getName();
    }
}
