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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import org.geotools.data.shapefile.index.quadtree.Node;
import org.geotools.data.shapefile.index.quadtree.StoreException;
import org.geotools.util.NIOUtilities;
import org.locationtech.jts.geom.Envelope;

/** @author Tommaso Nolli */
public class FileSystemNode extends Node {
    static final int[] ZERO = new int[0];

    private ScrollingBuffer buffer;
    private int subNodeStartByte;
    private int subNodesLength;
    private int numSubNodes;

    FileSystemNode(Envelope bounds, ScrollingBuffer buffer, int startByte, int subNodesLength) {
        super(bounds);
        this.buffer = buffer;
        this.subNodeStartByte = startByte;
        this.subNodesLength = subNodesLength;
    }

    public Node copy() throws IOException {
        FileSystemNode copy =
                new FileSystemNode(getBounds(), buffer, subNodeStartByte, subNodesLength);
        copy.numShapesId = numShapesId;
        copy.shapesId = new int[numShapesId];
        System.arraycopy(shapesId, 0, copy.shapesId, 0, numShapesId);
        copy.numSubNodes = numSubNodes;
        return copy;
    }

    /** @return Returns the numSubNodes. */
    public int getNumSubNodes() {
        return this.numSubNodes;
    }

    /** @param numSubNodes The numSubNodes to set. */
    public void setNumSubNodes(int numSubNodes) {
        this.numSubNodes = numSubNodes;
    }

    /** @return Returns the subNodeStartByte. */
    public int getSubNodeStartByte() {
        return this.subNodeStartByte;
    }

    /** @return Returns the subNodesLength. */
    public int getSubNodesLength() {
        return this.subNodesLength;
    }

    /** @see org.geotools.index.quadtree.Node#getSubNode(int) */
    public Node getSubNode(int pos) throws StoreException {
        if (this.subNodes.size() > pos) {
            return super.getSubNode(pos);
        }

        try {
            FileSystemNode subNode = null;

            // Getting prec subNode...
            int offset = this.subNodeStartByte;

            if (pos > 0) {
                subNode = (FileSystemNode) getSubNode(pos - 1);
                offset = subNode.getSubNodeStartByte() + subNode.getSubNodesLength();
            }

            buffer.goTo(offset);
            for (int i = 0, ii = subNodes.size(); i < ((pos + 1) - ii); i++) {
                subNode = readNode(pos, this, buffer);
                this.addSubNode(subNode);
            }
        } catch (IOException e) {
            throw new StoreException(e);
        }

        return super.getSubNode(pos);
    }

    /** */
    public static FileSystemNode readNode(
            int id, Node parent, FileChannel channel, ByteOrder order, boolean useMemoryMapping)
            throws IOException {
        ScrollingBuffer buffer = new ScrollingBuffer(channel, order, useMemoryMapping);
        return readNode(id, parent, buffer);
    }

    static FileSystemNode readNode(int id, Node parent, ScrollingBuffer buf) throws IOException {
        // offset
        int offset = buf.getInt();

        // envelope
        Envelope env = buf.getEnvelope();

        // shapes in this node
        int numShapesId = buf.getInt();
        int[] ids = null;
        if (numShapesId > 0) {
            ids = new int[numShapesId];
            buf.getIntArray(ids);
        } else {
            ids = ZERO;
        }
        int numSubNodes = buf.getInt();

        // let's create the new node
        FileSystemNode node = new FileSystemNode(env, buf, (int) buf.getPosition(), offset);
        node.setShapesId(ids);
        node.setNumSubNodes(numSubNodes);

        return node;
    }

    @Override
    public void close() {
        if (buffer != null) {
            buffer.close();
        }
        buffer = null;
    }

    /**
     * A utility class to access file contents by using a single scrolling buffer reading file
     * contents with a minimum of 8kb per access
     */
    private static class ScrollingBuffer {
        FileChannel channel;
        ByteOrder order;
        ByteBuffer buffer;
        /** the initial position of the buffer in the channel */
        long bufferStart;

        double[] envelope = new double[4];
        boolean useMemoryMapping;

        public ScrollingBuffer(FileChannel channel, ByteOrder order, boolean useMemoryMapping)
                throws IOException {
            this.channel = channel;
            this.order = order;
            this.useMemoryMapping = useMemoryMapping;

            this.bufferStart = channel.position();
            if (useMemoryMapping) {
                this.buffer =
                        channel.map(
                                MapMode.READ_ONLY,
                                channel.position(),
                                channel.size() - channel.position());
                this.buffer.order(order);
            } else {
                // start with an 8kb buffer
                this.buffer = NIOUtilities.allocate(8 * 1024);
                this.buffer.order(order);
                channel.read(buffer);
                buffer.flip();
            }
        }

        public void close() {
            if (buffer != null) {
                NIOUtilities.clean(buffer, useMemoryMapping);
                buffer = null;
            }
        }

        public int getInt() throws IOException {
            if (!useMemoryMapping && buffer.remaining() < 4) {
                refillBuffer(4);
            }
            return buffer.getInt();
        }

        public Envelope getEnvelope() throws IOException {
            if (!useMemoryMapping && buffer.remaining() < 32) refillBuffer(32);

            buffer.asDoubleBuffer().get(envelope);
            buffer.position(buffer.position() + 32);
            return new Envelope(envelope[0], envelope[2], envelope[1], envelope[3]);
        }

        public void getIntArray(int[] array) throws IOException {
            int size = array.length * 4;
            if (buffer.remaining() < size) refillBuffer(size);
            // read the array using a view
            IntBuffer intView = buffer.asIntBuffer();
            intView.limit(array.length);
            intView.get(array);
            // don't forget to update the original buffer position, since the
            // view is independent
            buffer.position(buffer.position() + size);
        }

        /** */
        void refillBuffer(int requiredSize) throws IOException {
            // compute the actual position up to we have read something
            long currentPosition = bufferStart + buffer.position();
            // if the buffer is not big enough enlarge it
            if (buffer.capacity() < requiredSize) {
                int size = buffer.capacity();
                while (size < requiredSize) size *= 2;
                buffer = NIOUtilities.allocate(size);
                buffer.order(order);
            }
            readBuffer(currentPosition);
        }

        private void readBuffer(long currentPosition) throws IOException {
            channel.position(currentPosition);
            buffer.clear();
            channel.read(buffer);
            buffer.flip();
            bufferStart = currentPosition;
        }

        /** Jumps the buffer to the specified position in the file */
        public void goTo(long newPosition) throws IOException {
            // if the new position is already in the buffer, just move the
            // buffer position
            // otherwise we have to reload it
            if (useMemoryMapping
                    || newPosition >= bufferStart && newPosition <= bufferStart + buffer.limit()) {
                buffer.position((int) (newPosition - bufferStart));
            } else {
                readBuffer(newPosition);
            }
        }

        /** Returns the absolute position of the next byte that will be read */
        public long getPosition() {
            return bufferStart + buffer.position();
        }
    }
}
