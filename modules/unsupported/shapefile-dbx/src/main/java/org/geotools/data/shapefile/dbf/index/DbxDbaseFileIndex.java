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
 * Manages the use of DBX Dbase index files for speedup alphanumeric filters to a DBF-file.
 * 
 * @author Alvaro Huarte
 */
class DbxDbaseFileIndex extends DbaseFileIndex {
    
    /**
     * Header magic number of a DBX-index file.
     */
    public final static int DBX_MAGIC_CODE = 0x8e3f;
    
    /**
     * Minimum number of records to compress in a DBX-index file.
     */
    public final static int DBX_COMPRESS_RECORD_LIMIT = 32;
    
    private SeekableByteChannel file;
    private Map<String,Node> indexNodeMap;
    private DbaseFileHeader header;
    private boolean compressed;
    
    /** 
     * Creates a new CdxDbaseFileIndex object.
     */
    public DbxDbaseFileIndex(URL urlDbaseFile, URL urlIndexFile, Charset charset, TimeZone timeZone) throws IOException {
        super(charset, timeZone);
        
        Map<String,Node> nodeMap = new TreeMap<String,Node>(String.CASE_INSENSITIVE_ORDER);
        this.file = getSeekableReadChannel(urlIndexFile, 4096);
        
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
     * Returns if the byte channel manage records compressed.
     */
    public boolean isCompressed() {
        return compressed;
    }
    
    /**
     * Extract the root Nodes from the current index File.
     */
    private boolean extractRootNodesFromIndexFile(long filePos, Map<String,Node> indexNodeMap) throws IOException {
        
        file.seek(filePos);
        
        if (file.readInt() == DbxDbaseFileIndex.DBX_MAGIC_CODE) {
            compressed = file.readInt() == 1;
            
            for (int i = 0, attributeCount = file.readInt(); i < attributeCount; i++) {
                String name = file.readString(11, charset);
                long start_page_pos = file.readLong();
                if (start_page_pos == -1) continue;
                
                for (int j = 0, fieldCount = header.getNumFields(); j < fieldCount; j++) {
                    if (header.getFieldName(j).equalsIgnoreCase(name)) {
                        name = header.getFieldName(j);
                        DbxNodeKey rootKey = new DbxNodeKey(header.getFieldClass(j), null);
                        indexNodeMap.put(name, new DbxNode(this, start_page_pos, rootKey));
                        break;
                    }
                }
            }
        }
        return indexNodeMap.size()>0;
    }
    
    /**
     * Gets the root index-node collection managed by this Dbase index file.
     */
    @Override
    public Map<String,Node> getIndexNodeMap() {
        return indexNodeMap;
    }
}
