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
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.index.SeekableByteChannel;

/**
 * Implements a root index node of a Dbase CDX-index file.
 * 
 * @author Alvaro Huarte
 */
class CdxRootNode extends CdxNode {
    
    protected String keyExpression;
    
    /**
     * Creates a new CdxRootNode object. 
     */
    public CdxRootNode(CdxDbaseFileIndex dbaseIndex, String keyExpression, long filePos, CdxNodeKey key) {
        super(dbaseIndex, filePos, key);
        this.keyExpression = keyExpression;
    }
    
    /**
     * Returns the key expression of the Node.
     */
    public String getKeyExpression() {
        return keyExpression;
    }
    
    /**
     * Returns a list containing all of the root child Nodes of this object.
     * @throws IOException
     */
    public CdxRootNode[] getRootChildren(DbaseFileHeader header, int indexOptions) throws IOException {
        
        List<CdxRootNode> rootList = new ArrayList<CdxRootNode>();
        
        SeekableByteChannel file = dbaseIndex.getChannel();
        readHeader();
        
        if ((indexOptions&INDEX_OPTIONS_COMPACT_INDEX_HEADER)==INDEX_OPTIONS_COMPACT_INDEX_HEADER) {
            
            file.seek(filePos + 14);
            int record_number_mask = Util.x86(file.readInt());
            
            file.seek(filePos + 23);
            int holding_record_number_count = file.readByte();
            
            int bytesLen = numberOfKeys * holding_record_number_count;
            byte[] bytes = new byte[bytesLen];
            file.readBytes(bytes, 0,bytesLen);
            
            for (int i = 0, startIndex = 0; i < numberOfKeys; i++, startIndex+=holding_record_number_count) {
                
                int one_item = Util.getIntegerBE(bytes, startIndex, holding_record_number_count);
                int recno = one_item & record_number_mask;
                
                file.seek(recno);
                int start_page_pos = Util.x86(file.readInt());
                file.seek(recno + 12);
                int key_length = Util.x86(file.readShort());
                
                file.seek(recno + 510);
                int key_expression_length = Util.x86(file.readShort());
                
                if (key_expression_length>0) {
                    byte b[] = new byte[key_expression_length];
                    file.readBytes(b, 0,key_expression_length);
                    
                    String expression = Util.getString(b, 0, b.length, dbaseIndex.getCharset(), true);
                    if (expression.isEmpty()) continue;
                    
                    for (int j = 0, fieldCount = header.getNumFields(); j < fieldCount; j++) {
                        if (header.getFieldName(j).equalsIgnoreCase(expression)) {
                            expression = header.getFieldName(j);
                            CdxNodeKey rootKey = new CdxNodeKey(header.getFieldClass(j), key_length, null);
                            rootList.add(new CdxRootNode(dbaseIndex, expression, start_page_pos, rootKey));
                            break;
                        }
                    }
                }
            }
        }
        return rootList.toArray(new CdxRootNode[0]);
    }
}
