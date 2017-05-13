/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.dbf.index;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.geotools.data.shapefile.dbf.index.Node;
import org.geotools.data.shapefile.dbf.index.NodeKey;
import org.geotools.data.shapefile.dbf.index.NodeVisitor;
import org.geotools.data.shapefile.dbf.index.NodeVisitorArgs;
import org.geotools.data.shapefile.dbf.index.SeekableByteChannel;
import org.opengis.filter.PropertyIsEqualTo;

/**
 * Implements an index node of a Dbase DBX-index file.
 * 
 * @author Alvaro Huarte
 */
class DbxNode implements Node {
    
    protected DbxDbaseFileIndex dbaseIndex;
    protected long filePos;
    
    protected long nextNodeFilePos = -1;
    protected int numberOfKeys = -1;
    protected int recordOffset;
    
    protected DbxNodeKey key;
    
    /**
     * Creates a new CdxNode object.
     * @throws IOException
     */
    public DbxNode(DbxDbaseFileIndex dbaseIndex, long filePos, DbxNodeKey key) {
        this.dbaseIndex = dbaseIndex;
        this.filePos = filePos;
        this.key = key;
    }
    
    /**
     * Gets the key of the Node.
     */
    @Override
    public NodeKey getKey() {
        return key;
    }
    
    /**
     * Reads the data header of the Node when needed.
     * @throws IOException
     */
    protected void readHeader(NodeVisitorArgs visitorArgs) throws IOException {
        
        if (numberOfKeys==-1) {
            SeekableByteChannel file = dbaseIndex.getChannel();
            file.seek(filePos);
            nextNodeFilePos = file.readLong();
            numberOfKeys = file.readInt();
            
            Class<?> binding = key.getBinding();
            
            if (binding==Integer.class) {
                key.setValue(file.readInt());
                recordOffset = 4;
            }
            else
            if (binding==Long.class) {
                key.setValue(file.readLong());
                recordOffset = 8;
            }
            else
            if (binding==Double.class) {
                key.setValue(file.readDouble());
                recordOffset = 8;
            }
            else
            if (binding==Date.class) {
                key.setValue(new java.util.Date(file.readLong()));
                recordOffset = 8;
            }
            else
            if (binding==Boolean.class) {
                key.setValue(file.readByte()!=0 ? true : false);
                recordOffset = 1;
            }
            else {
                int byteCount = file.readShort();
                
                if (visitorArgs!=null) {
                    byte[] bytes = visitorArgs.sharedBuffer(byteCount);
                    file.readBytes(bytes, 0, byteCount);
                    key.setValue(Util.getString(bytes, 0, byteCount, dbaseIndex.charset, true));
                }
                else {
                    key.setValue(file.readString(byteCount, dbaseIndex.charset));
                }
                recordOffset = 2 + byteCount;
            }
        }
    }
    
    /**
     * Returns a list containing all of the child Nodes of this object (Null when it hasn't children).
     * @throws IOException
     */
    @Override
    public Node[] getChildren(NodeVisitorArgs visitorArgs) throws IOException {
        return null;
    }
    
    /**
     * Fill a list containing all of the records managed by this object.
     * @throws IOException
     * @return count of records fetched.
     */
    @Override
    public int fillRecordIds(List<Integer> recordList, NodeVisitorArgs visitorArgs) throws IOException {
        
        readHeader(visitorArgs);
        
        // We can stop ?
        if (visitorArgs!=null && visitorArgs.filter() instanceof PropertyIsEqualTo) {
            visitorArgs.setCanceled(true);
        }
        
        // It is a no-empty 'Leaf' Node with records.
        if (numberOfKeys>0) {
            SeekableByteChannel file = dbaseIndex.getChannel();
            file.seek(filePos + 12 + recordOffset);
            
            if (dbaseIndex.isCompressed() && numberOfKeys > DbxDbaseFileIndex.DBX_COMPRESS_RECORD_LIMIT) {
                int bytesLen = file.readInt();
                byte[] bytes = visitorArgs!=null ? visitorArgs.sharedBuffer(bytesLen) : new byte[bytesLen];
                file.readBytes(bytes, 0, bytesLen);
                
                final ByteArrayInputStream bais = new ByteArrayInputStream(bytes, 0, bytes.length);
                final GZIPInputStream is = new GZIPInputStream(bais);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[512];
                while ((bytesLen = is.read(buffer, 0, 512)) != -1) baos.write(buffer, 0, bytesLen);
                bais.close();
                is.close();
                bytes = baos.toByteArray();
                baos.close();
                
                for (int i = 0, c = 0; i < numberOfKeys; i++, c+=4) {
                    int id = bytes[c] << 24 | (bytes[c+1] & 0xFF) << 16 | (bytes[c+2] & 0xFF) << 8 | (bytes[c+3] & 0xFF);
                    recordList.add(id);
                }
            }
            else {
                int bytesLen = numberOfKeys * 4;
                byte[] bytes = visitorArgs!=null ? visitorArgs.sharedBuffer(bytesLen) : new byte[bytesLen];
                file.readBytes(bytes, 0, bytesLen);
                
                for (int i = 0, c = 0; i < numberOfKeys; i++, c+=4) {
                    int id = bytes[c] << 24 | (bytes[c+1] & 0xFF) << 16 | (bytes[c+2] & 0xFF) << 8 | (bytes[c+3] & 0xFF);
                    recordList.add(id);
                }
            }
        }
        return numberOfKeys;
    }
    
    /**
     * Accepts a visitor.
     * @throws IOException
     */
    @Override
    public Object accept(NodeVisitor visitor, Object extraData, NodeVisitorArgs visitorArgs) throws IOException {
        
        DbxNode currentNode = this;
        
        // We only visit no-empty Nodes with records.
        while (currentNode!=null) {
            currentNode.readHeader(visitorArgs);
            
            // Stop ?
            if (visitorArgs!=null && visitorArgs.isCanceled())
                return extraData;
            
            // Execute node.
            if (currentNode.numberOfKeys>0) {
                extraData = visitor.visit(currentNode, extraData);
                if (extraData==null) return null;
            }
            
            // Next node.
            if (currentNode.nextNodeFilePos != -1) {
                currentNode = new DbxNode(dbaseIndex, currentNode.nextNodeFilePos, new DbxNodeKey(key.getBinding(), null));
            }
            else {
                currentNode = null;
            }
        }
        return extraData;
    }
    
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return "key=(" + key.toString() + "), FilePos=" + filePos + ", numberOfKeys=" + numberOfKeys;
    }
}
