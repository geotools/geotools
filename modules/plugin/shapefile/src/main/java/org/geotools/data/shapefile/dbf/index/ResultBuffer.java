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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Defines a data buffer to manage the results of a Filter evaluation.
 * 
 * @author Alvaro Huarte
 */
public class ResultBuffer {
    
    private static int RESULT_BUFFER_EMPTY  = 0;
    private static int RESULT_BUFFER_NORMAL = 1;
    private static int RESULT_BUFFER_FULL   = 2;
    private static int RESULT_BUFFER_UNDEF  = 3;
    
    /**
     * Buffer of record numbers managed.
     */
    private Map<Integer,Integer> data;
    
    /**
     * ContentType of the Buffer.
     */
    private int contentType;
    
    /**
     * Creates a new ResultBuffer object.
     */
    public ResultBuffer(Map<Integer,Integer> buffer) {
        this.contentType = buffer!=null && buffer.size()>0 ? RESULT_BUFFER_NORMAL : RESULT_BUFFER_EMPTY;
        this.data = buffer!=null ? buffer : new HashMap<Integer,Integer>();
    }
    
    /**
     * Creates a new empty ResultBuffer object.
     */
    public static ResultBuffer makeEmpty() {
        return new ResultBuffer(null);
    }
    /**
     * Creates a new undefined ResultBuffer object.
     */
    public static ResultBuffer makeUndefined() {
        ResultBuffer buffer = new ResultBuffer(null);
        buffer.contentType = RESULT_BUFFER_UNDEF;
        return buffer;
    }
    /**
     * Creates a new full ResultBuffer object.
     */
    public static ResultBuffer makeFull() {
        ResultBuffer buffer = new ResultBuffer(null);
        buffer.contentType = RESULT_BUFFER_FULL;
        return buffer;
    }
    
    /**
     * Returns a set of the record numbers contained by this object.
     */
    public Set<Integer> dataSet() {
        return data.keySet();
    }
    /**
     * Clear the buffer managed.
     */
    public void clear() {
        contentType = RESULT_BUFFER_EMPTY;
        data.clear();
    }
    
    /**
     * Returns whether the Buffer is undefined.
     */
    public boolean isUndefined() {
        return contentType==RESULT_BUFFER_UNDEF;
    }
    /**
     * Returns whether the Buffer is empty.
     */
    public boolean isEmpty() {
        return contentType==RESULT_BUFFER_EMPTY || data.size()==0;
    }
    /**
     * Returns whether the Buffer manages all records.
     */
    public boolean isFull() {
        return contentType==RESULT_BUFFER_FULL;
    }

    /**
     * Apply an OR operation to the specified ResultBuffers.
     */
    public static ResultBuffer makeOr(ResultBuffer a, ResultBuffer b) {
        
        if (a.isEmpty() || b.isFull() || a.isUndefined()) return b;
        if (b.isEmpty() || a.isFull() || b.isUndefined()) return a;
        
        if (b.data.size()>a.data.size()) {
            ResultBuffer temp = a;
            a = b;
            b = temp;
        }
        for (Map.Entry<Integer,Integer> entry : b.data.entrySet()) {
            a.data.put(entry.getKey(), entry.getValue());
        }
        return new ResultBuffer(a.data);
    }
    /**
     * Apply an AND operation to the specified ResultBuffers.
     */
    public static ResultBuffer makeAnd(ResultBuffer a, ResultBuffer b) {
        
        if (a.isUndefined()) return b;
        if (b.isUndefined()) return a;
        if (a.isEmpty() || b.isFull()) return a;
        if (b.isEmpty() || a.isFull()) return b;
        
        Map<Integer,Integer> buffer = new HashMap<Integer,Integer>(Math.min(a.data.size(),b.data.size()));
        
        if (b.data.size()<a.data.size()) {
            ResultBuffer temp = a;
            a = b;
            b = temp;
        }
        for (Map.Entry<Integer,Integer> entry : b.data.entrySet()) {
            if (a.data.containsKey(entry.getKey())) buffer.put(entry.getKey(), entry.getValue());
        }
        return new ResultBuffer(buffer);
    }
}
