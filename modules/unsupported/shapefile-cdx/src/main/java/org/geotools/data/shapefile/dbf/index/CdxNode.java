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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.shapefile.dbf.index.Node;
import org.geotools.data.shapefile.dbf.index.NodeKey;
import org.geotools.data.shapefile.dbf.index.NodeVisitor;
import org.geotools.data.shapefile.dbf.index.NodeVisitorArgs;
import org.geotools.data.shapefile.dbf.index.SeekableByteChannel;

/**
 * Implements an index node of a Dbase CDX-index file.
 * 
 * @author Alvaro Huarte
 */
class CdxNode implements Node {
    
    static int INDEX_OPTIONS_INTERIOR_NODE = 0;
    static int INDEX_OPTIONS_ROOT_NODE = 1;
    static int INDEX_OPTIONS_LEAF_NODE = 2;
    static int INDEX_OPTIONS_COMPACT_INDEX_FORMAT = 32;
    static int INDEX_OPTIONS_COMPACT_INDEX_HEADER = 64;
    static int INDEX_OPTIONS_STRUCTURE_INDEX = 128;
    
    static int INDEX_PAGE_FULLSIZE = 512;
    
    protected CdxDbaseFileIndex dbaseIndex;
    protected long filePos;
    
    protected int nodeAttributes = INDEX_OPTIONS_INTERIOR_NODE;
    protected int numberOfKeys = -1;
    
    protected List<Integer> records;
    protected CdxNodeKey key;
    
    /**
     * Creates a new CdxNode object.
     * @throws IOException
     */
    public CdxNode(CdxDbaseFileIndex dbaseIndex, long filePos, CdxNodeKey key) {
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
    protected void readHeader() throws IOException {
        
        if (numberOfKeys==-1) {
            SeekableByteChannel file = dbaseIndex.getChannel();
            file.seek(filePos);
            
            byte bytes[] = new byte[4];
            file.readBytes(bytes, 0,4);
            
            // Read header using file.readBytes(), it is faster than use file.readShort()!
            nodeAttributes = Util.x86((short)((bytes[0] << 8) + (bytes[1] << 0)));
            numberOfKeys   = Util.x86((short)((bytes[2] << 8) + (bytes[3] << 0)));
        }
    }
    
    /**
     * Reorganizes the specified nodes to group key-records for minimize the filter evaluation.
     * @throws IOException
     */
    private static Node[] prepareVirtualPoolWithRecords(Node[] nodeArray, NodeVisitorArgs visitorArgs) throws IOException {
        
        if (nodeArray!=null && nodeArray.length>0) {
            
            List<Node> resultList = new ArrayList<Node>(nodeArray.length);
            Map<CdxNodeKey,List<Integer>> buffer = null;
            
            for (Node node : nodeArray) {
                CdxNode theNode = (CdxNode)node;
                theNode.readHeader();
                if (theNode.numberOfKeys==0) continue;
                
                if ((theNode.nodeAttributes&INDEX_OPTIONS_LEAF_NODE)==INDEX_OPTIONS_LEAF_NODE) {
                    if (buffer==null) buffer = new HashMap<CdxNodeKey,List<Integer>>();
                    theNode.prepareRecordBuffer(buffer, visitorArgs);
                }
                else {
                    resultList.add(node);
                }
            }
            if (buffer!=null && buffer.size()>0) {
                CdxNode firstNode = (CdxNode)nodeArray[0];
                
                for (Map.Entry<CdxNodeKey,List<Integer>> entry : buffer.entrySet()) {
                    CdxNode node = new CdxNode(firstNode.dbaseIndex, -1, entry.getKey());
                    node.nodeAttributes = INDEX_OPTIONS_LEAF_NODE;
                    node.numberOfKeys = entry.getValue().size();
                    node.records = entry.getValue();
                    
                    resultList.add(node);
                }
                buffer.clear();
            }
            return resultList.toArray(new Node[0]);
        }
        return null;
    }
    /**
     * Fill a buffer containing all of the key-records managed by this Node.
     * @throws IOException
     * @return count of records fetched.
     */
    private int prepareRecordBuffer(Map<CdxNodeKey,List<Integer>> buffer, NodeVisitorArgs visitorArgs) throws IOException {
        
        int currOffset = 0, fullOffset = 0;
        List<Integer> lastList = null;
        NodeKey lastKey = null;
        
        int keyLength = key.getKeyLength();
        ByteBuffer byteBuffer = ByteBuffer.allocate(keyLength);
        
        SeekableByteChannel file = dbaseIndex.getChannel();
        file.seek(filePos + 14);
        
        // Read full node page.
        int bytesLen = INDEX_PAGE_FULLSIZE - 14;
        byte[] bytes = visitorArgs!=null ? visitorArgs.sharedBuffer(bytesLen) : new byte[bytesLen];
        file.readBytes(bytes, 0,bytesLen);
        
        // Gets the mask/flag collection of current page.
        int record_number_mask = Util.x86(Util.getIntegerLE(bytes, 0, 4));
        int duplicate_count_mask = bytes[4];
        int trailing_count_mask = bytes[5];
        int record_number_count = bytes[6];
        int duplicate_count = bytes[7];
        int trailing_count = bytes[8];
        int holding_record_number_count = bytes[9];
        
        Charset charset = dbaseIndex.getCharset();
        Calendar calendar = dbaseIndex.getCalendar();
        
        // Parse key-records of the page.
        for (int i = 0, startIndex = 10; i < numberOfKeys; i++, startIndex+=holding_record_number_count) {
            
            int one_item = Util.getIntegerBE(bytes, startIndex, holding_record_number_count);
            int recno = one_item & record_number_mask;
            
            int bytes_of_recno = (int)(record_number_count / 8);
            one_item = Util.getIntegerBE(bytes, startIndex+bytes_of_recno, holding_record_number_count-bytes_of_recno);
            one_item >>= (record_number_count - (8 * bytes_of_recno));
            int dupli = one_item & duplicate_count_mask;
            one_item >>= duplicate_count;
            int trail = one_item & trailing_count_mask;
            one_item >>= trailing_count;
            currOffset = keyLength - trail - dupli;
            
            // Extract the key of current record. 
            if (currOffset>0 || (currOffset==0 && lastList==null)) {
                
                CdxNodeKey recordKey = null;
                
                // ... compose the key-stream adding duplicated bytes.
                if (dupli==0) {
                    recordKey = CdxNodeKey.createKey(key.getBinding(), charset, calendar, bytes, bytesLen-fullOffset-currOffset, currOffset);
                    byteBuffer.clear();
                    byteBuffer.put(bytes, bytesLen-fullOffset-currOffset, currOffset);
                }
                else {
                    byteBuffer.position(dupli);
                    byteBuffer.put(bytes, bytesLen-fullOffset-currOffset, currOffset);
                    recordKey = CdxNodeKey.createKey(key.getBinding(), charset, calendar, byteBuffer.array(), 0, byteBuffer.position());
                }
                fullOffset += currOffset;
                
                // ... add to buffer.
                if (lastKey==null || !recordKey.equals(lastKey)) {
                    lastList = buffer.get(recordKey);
                    if (lastList==null) { buffer.put(recordKey, lastList = new ArrayList<Integer>(numberOfKeys)); }
                }
                lastKey = recordKey;
            }
            if (lastList==null) {
                throw new IOException("Invalid prepareRecordBuffer() call!; FilePos="+filePos);
            }
            lastList.add(recno-1);
        }
        return numberOfKeys;
    }
    
    /**
     * Returns a list containing all of the child Nodes of this object (Null when it hasn't children).
     * @throws IOException
     */
    @Override
    public Node[] getChildren(NodeVisitorArgs visitorArgs) throws IOException {
        
        readHeader();
        
        // It is not a no-empty 'Leaf' Node.
        if (numberOfKeys>0 && (nodeAttributes&INDEX_OPTIONS_LEAF_NODE)!=INDEX_OPTIONS_LEAF_NODE) {
            
            Node[] nodeArray = new Node[numberOfKeys];
            
            SeekableByteChannel file = dbaseIndex.getChannel();
            file.seek(filePos + 12);
            
            int keyLength = key.getKeyLength();
            int bytesLen = numberOfKeys * (keyLength + 8);
            byte[] bytes = visitorArgs!=null ? visitorArgs.sharedBuffer(bytesLen) : new byte[bytesLen];
            file.readBytes(bytes, 0,bytesLen);
            
            for (int i = 0, startIndex = 0; i < numberOfKeys; i++) {
                CdxNodeKey childKey = CdxNodeKey.createKey(key.getBinding(), dbaseIndex.getCharset(), dbaseIndex.getCalendar(), bytes, startIndex, keyLength);
                startIndex += keyLength;
                
                @SuppressWarnings("unused")
                int recno = Util.getIntegerLE(bytes, startIndex, 4);
                startIndex += 4;
                int npage = Util.getIntegerLE(bytes, startIndex, 4);
                startIndex += 4;
                
                nodeArray[i] = new CdxNode(dbaseIndex, npage, childKey);
            }
            return prepareVirtualPoolWithRecords(nodeArray, visitorArgs);
        }
        else
        if (numberOfKeys>0 && records==null) {
            return prepareVirtualPoolWithRecords(new Node[]{ this }, visitorArgs);
        }
        return null;
    }
    
    /**
     * Fill a list containing all of the records managed by this object.
     * @throws IOException
     * @return count of records fetched.
     */
    @Override
    public int fillRecordIds(List<Integer> recordList, NodeVisitorArgs visitorArgs) throws IOException {
        
        readHeader();
        
        // It is a no-empty 'Leaf' Node with records.
        if (numberOfKeys>0 && records!=null) {
            recordList.addAll(records);
            return records.size();
        }
        return 0;
    }
    
    /**
     * Accepts a visitor.
     * @throws IOException
     */
    @Override
    public Object accept(NodeVisitor visitor, Object extraData, NodeVisitorArgs visitorArgs) throws IOException {
        
        readHeader();
        
        Node[] children = getChildren(visitorArgs);

        // Stop ?
        if (visitorArgs!=null && visitorArgs.isCanceled())
            return extraData;
        
        // We only visit no-empty 'Leaf' Nodes with records.
        if (numberOfKeys>0 && records!=null) {
            extraData = visitor.visit(this, extraData);
            if (extraData==null) return null;
        }
        
        // Stop ?
        if (visitorArgs!=null && visitorArgs.isCanceled())
            return extraData;
            
        // Visit children.
        if (children!=null && children.length>0) {
            for (Node node : children) {
                extraData = node.accept(visitor, extraData, visitorArgs);
                if (extraData==null) return null;
            }
        }
        return extraData;
    }
    
    /**
     * Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return "key=(" + key.toString() + "), FilePos=" + filePos + ", Attribs=" + nodeAttributes + ", numberOfKeys=" + numberOfKeys;
    }
}
