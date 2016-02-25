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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.index.DbaseFileIndex;
import org.geotools.data.shapefile.dbf.index.Node;
import org.geotools.data.shapefile.dbf.index.SeekableByteChannel;

/**
 * Manages the use of CDX Dbase index files for speedup alphanumeric filters to a DBF-file.
 *  
 * @see http://www.clicketyclick.dk/databases/xbase/format/cdx.html
 * 
 * @author Alvaro Huarte
 */
class CdxDbaseFileIndex extends DbaseFileIndex {
    
    private SeekableByteChannel file;
    private Map<String,Node> indexNodeMap;
    private DbaseFileHeader header;
    
    /** 
     * Creates a new CdxDbaseFileIndex object.
     */
    public CdxDbaseFileIndex(URL urlDbaseFile, URL urlIndexFile, Charset charset, TimeZone timeZone) throws IOException {
        super(charset, timeZone);
        
        Map<String,Node> nodeMap = new TreeMap<String,Node>(String.CASE_INSENSITIVE_ORDER);
        this.file = getSeekableReadChannel(urlIndexFile, 0);
        
        DbaseFileReader dbf = new DbaseFileReader(getReadChannel(urlDbaseFile), false, charset, timeZone);
        this.header = dbf.getHeader();
        dbf.close();
        
        if (extractRootNodesFromIndexFile(0, nodeMap) && nodeMap.size()>0) {
            this.indexNodeMap = nodeMap;
        }
    }
    /**
     * Closes its stream and releases any system resources associated with it.
     */
    public void close() throws IOException {
        
        if (indexNodeMap!=null) {
            indexNodeMap.clear();
            indexNodeMap = null;
        }
        if (file!=null) {
            file.close();
            file = null;
        }
        header = null;
    }
    
    /**
     * Returns the byte channel managed. 
     */
    public SeekableByteChannel getChannel() {
        return file;
    }
    
    /**
     * Extract the root Nodes from the current index File.
     */
    private boolean extractRootNodesFromIndexFile(long filePos, Map<String,Node> indexNodeMap) throws IOException {
        
        file.seek(filePos);
        int start_page_pos = Util.x86(file.readInt());
        
        file.seek(filePos + 12);
        int key_length = Util.x86(file.readShort());
        int index_options = file.readByte();
        
        CdxRootNode headerNode = new CdxRootNode(this, "", start_page_pos, new CdxNodeKey(String.class, key_length, null));
        boolean keysReaded = false;
        
        for (CdxRootNode node : headerNode.getRootChildren(header, index_options)) {
            indexNodeMap.put(node.getKeyExpression(), node);
            keysReaded = true;
        }
        return keysReaded;
    }
    
    /**
     * Gets the root index-node collection managed by this Dbase index file.
     */
    @Override
    public Map<String,Node> getIndexNodeMap() {
        return indexNodeMap;
    }
}
