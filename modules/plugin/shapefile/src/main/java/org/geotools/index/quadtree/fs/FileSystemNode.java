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
package org.geotools.index.quadtree.fs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

import org.geotools.index.quadtree.Node;
import org.geotools.index.quadtree.StoreException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author Tommaso Nolli
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/main/java/org/geotools/index/quadtree/fs/FileSystemNode.java $
 */
public class FileSystemNode extends Node {
    private ScrollingBuffer buffer;
    private ByteOrder order;
    private int subNodeStartByte;
    private int subNodesLength;
    private int numSubNodes;

    /**
     * DOCUMENT ME!
     * 
     * @param bounds
     * @param channel
     *                DOCUMENT ME!
     * @param order
     *                DOCUMENT ME!
     * @param startByte
     *                DOCUMENT ME!
     * @param subNodesLength
     *                DOCUMENT ME!
     */
    FileSystemNode(Envelope bounds, int id, ScrollingBuffer buffer, int startByte, int subNodesLength) {
        super(bounds, id);
        this.buffer = buffer;
        this.subNodeStartByte = startByte;
        this.subNodesLength = subNodesLength;
    }

    public Node copy() throws IOException {
        FileSystemNode copy = new FileSystemNode(getBounds(), id, buffer, subNodeStartByte, subNodesLength);
        copy.numShapesId = numShapesId;
        copy.shapesId = new int[numShapesId];
        System.arraycopy(shapesId, 0, copy.shapesId, 0, numShapesId);
        copy.numSubNodes = numSubNodes;
        return copy;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return Returns the numSubNodes.
     */
    public int getNumSubNodes() {
        return this.numSubNodes;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param numSubNodes
     *                The numSubNodes to set.
     */
    public void setNumSubNodes(int numSubNodes) {
        this.numSubNodes = numSubNodes;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return Returns the subNodeStartByte.
     */
    public int getSubNodeStartByte() {
        return this.subNodeStartByte;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return Returns the subNodesLength.
     */
    public int getSubNodesLength() {
        return this.subNodesLength;
    }

    /**
     * @see org.geotools.index.quadtree.Node#getSubNode(int)
     */
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
                offset = subNode.getSubNodeStartByte()
                        + subNode.getSubNodesLength();
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

    /**
     * DOCUMENT ME!
     * 
     * @param channel
     * @param order
     *                DOCUMENT ME!
     * 
     * 
     * @throws IOException
     */
    public static FileSystemNode readNode(int id, Node parent,
            FileChannel channel, ByteOrder order) throws IOException {
        ScrollingBuffer buffer = new ScrollingBuffer(channel, order);
        return readNode(id, parent, buffer);
    }

    static FileSystemNode readNode(int id, Node parent, ScrollingBuffer buf)
            throws IOException {
        // offset
        int offset = buf.getInt();
        double x1;
        double y1;
        double x2;
        double y2;

        // envelope
        x1 = buf.getDouble();
        y1 = buf.getDouble();
        x2 = buf.getDouble();
        y2 = buf.getDouble();
        Envelope env = new Envelope(x1, x2, y1, y2);

        // shapes in this node
        int numShapesId = buf.getInt();
        int[] ids = new int[numShapesId];
        buf.getIntArray(ids);
        int numSubNodes = buf.getInt();

        // let's create the new node
        FileSystemNode node = new FileSystemNode(env, id, buf,
                (int) buf.getPosition(), offset);
        node.setShapesId(ids);
        node.setNumSubNodes(numSubNodes);

        return node;
    }

    /**
     * A utility class to access file contents by using a single scrolling
     * buffer reading file contents with a minimum of 8kb per access
     */
    private static class ScrollingBuffer {
        FileChannel channel;
        ByteOrder order;
        ByteBuffer buffer;
        /** the initial position of the buffer in the channel */
        long bufferStart;

        public ScrollingBuffer(FileChannel channel, ByteOrder order)
                throws IOException {
            this.channel = channel;
            this.order = order;
            this.bufferStart = channel.position();

            // start with an 8kb buffer
            this.buffer = ByteBuffer.allocateDirect(8 * 1024);
            this.buffer.order(order);
            channel.read(buffer);
            buffer.flip();
        }

        public int getInt() throws IOException {
            if (buffer.remaining() < 4)
                refillBuffer(4);
            return buffer.getInt();
        }

        public double getDouble() throws IOException {
            if (buffer.remaining() < 8)
                refillBuffer(8);
            return buffer.getDouble();
        }

        public void getIntArray(int[] array) throws IOException {
            int size = array.length * 4;
            if (buffer.remaining() < size)
                refillBuffer(size);
            // read the array using a view
            IntBuffer intView = buffer.asIntBuffer();
            intView.limit(array.length);
            intView.get(array);
            // don't forget to update the original buffer position, since the
            // view is independent
            buffer.position(buffer.position() + size);
        }

        /**
         * 
         * @param requiredSize
         * @throws IOException
         */
        void refillBuffer(int requiredSize) throws IOException {
            // compute the actual position up to we have read something
            long currentPosition = bufferStart + buffer.position();
            // if the buffer is not big enough enlarge it
            if (buffer.capacity() < requiredSize) {
                int size = buffer.capacity();
                while (size < requiredSize)
                    size *= 2;
                buffer = ByteBuffer.allocateDirect(size);
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

        /**
         * Jumps the buffer to the specified position in the file
         * 
         * @param newPosition
         * @throws IOException
         */
        public void goTo(long newPosition) throws IOException {
            // if the new position is already in the buffer, just move the
            // buffer position
            // otherwise we have to reload it
            if (newPosition >= bufferStart
                    && newPosition <= bufferStart + buffer.limit()) {
                buffer.position((int) (newPosition - bufferStart));
            } else {
                readBuffer(newPosition);
            }
        }

        /**
         * Returns the absolute position of the next byte that will be read
         * 
         * @return
         */
        public long getPosition() {
            return bufferStart + buffer.position();
        }
    }
}
