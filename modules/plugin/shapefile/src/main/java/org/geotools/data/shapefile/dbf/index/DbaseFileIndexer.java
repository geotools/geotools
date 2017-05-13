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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileReader.Row;
import org.geotools.util.SimpleInternationalString;

import org.opengis.util.ProgressListener;

/**
 * Utility class that provides a generic Dbase attribute indexer.
 * 
 * @author Alvaro Huarte
 */
public abstract class DbaseFileIndexer {
    
    protected File dbaseFile;
    protected Charset charset;
    protected TimeZone timeZone;
    
    /**
     * Creates a new DbaseFileIndexer object.
     */
    public DbaseFileIndexer(File dbaseFile, Charset charset, TimeZone timeZone) {
        this.dbaseFile = dbaseFile;
        this.charset = charset;
        this.timeZone = timeZone;
    }
    
    /**
     * Defines an key-records entry manager.
     */
    class AttributeEntry {
        
        DbaseFileHeader header;
        int column;
        HashMap<Object,List<Integer>> keyRecords = new HashMap<Object,List<Integer>>();
        
        public AttributeEntry(DbaseFileHeader header, int column) {
            this.header = header;
            this.column = column;
        }
        
        public int column() {
            return column;
        }
        public HashMap<Object,List<Integer>> keyRecords() {
            return keyRecords;
        }
        
        public void addValue(Row row, Integer rowIndex) throws IOException {
            Object value = row.read(column);
            List<Integer> records = keyRecords.get(value);
            
            if (records == null) {
                records = new ArrayList<Integer>();
                keyRecords.put(value, records);
            }
            records.add(rowIndex);
        }
        public void flush(DbaseFileIndexer indexer) throws IOException {
            indexer.flush(column, header.getFieldName(column), header.getFieldClass(column), keyRecords);
            clear();
        }
        public void clear() {
            for (List<Integer> records : keyRecords.values()) {
                records.clear();
            }
            keyRecords.clear();
        }
    }
    
    /**
     * Flush the current key-record pair collection accumulated.
     */
    private boolean flushAttributeCache(List<AttributeEntry> attributeList) throws IOException {
        if (attributeList.size() > 0) {
            for (AttributeEntry attributeEntry : attributeList) attributeEntry.flush(this);
            return true;
        }
        return false;
    }
    /**
     * Flush the current key-record pair collection.
     */
    protected abstract void flush(int fieldIndex, String fieldName, Class<?> binding, Map<Object,List<Integer>> keyRecords) throws IOException;
    
    /**
     * Started the current job.
     */
    protected void startJob(DbaseFileHeader header, List<Integer> columnList, ProgressListener listener) throws IOException {
        // Do nothing
    }
    /**
     * Finish the current job.
     */
    protected void endJob(ProgressListener listener) {
        // Do nothing
    }
    
    /**
     * Returns true when the name exist in the specified list of attributes.
     */
    private static boolean containsIgnoreCaseName(List<String> attributeNames, String name) {
        for (String attributeName : attributeNames) {
            if (attributeName.equalsIgnoreCase(name)) return true;
        }
        return false;
    }
    
    /**
     * Builds a dbase-index of the specified DbaseFile and attribute collection.
     * 
     * @throws IOException
     */
    public boolean buildIndex(List<String> attributeNames, int maximumCacheSize, ProgressListener listener) throws IOException {
        
        listener.setTask(SimpleInternationalString.wrap("Indexing '" + dbaseFile .getPath() + "'"));
        RandomAccessFile raf = null;
        DbaseFileReader dbf = null;
        
        try {
            List<AttributeEntry> attributeList = new ArrayList<AttributeEntry>();
            List<Integer> columnList = new ArrayList<Integer>();
            
            raf = new RandomAccessFile(dbaseFile, "r");
            dbf = new DbaseFileReader(raf.getChannel(), false, charset, timeZone);
            DbaseFileHeader header = dbf.getHeader();
            
            // Initialize the list of attributes to index.
            for (int i = 0, fieldCount = header.getNumFields(); i < fieldCount; i++) {
                if (attributeNames == null || attributeNames.size() == 0 || containsIgnoreCaseName(attributeNames, header.getFieldName(i))) {
                    attributeList.add(new AttributeEntry(header, i));
                    columnList.add(i);
                }
            }
            if (attributeList.size() == 0) {
                return false;
            }
            startJob(header, columnList, listener);
            columnList.clear();
            
            Integer recordCount = header.getNumRecords(), recordIndex = 0;
            Integer cacheSize = 0;
            
            float scale = 100f / recordCount;
            listener.started();
            
            // Build the index attributes of the current Dbase file.
            while (dbf.hasNext()) {
                Row row = dbf.readRow();
                
                for (AttributeEntry attributeEntry : attributeList) {
                    attributeEntry.addValue(row, recordIndex);
                    cacheSize++;
                }
                if (maximumCacheSize > 0 && cacheSize >= maximumCacheSize) {
                    flushAttributeCache(attributeList);
                    cacheSize = 0;
                }
                if (listener.isCanceled()) {
                    break;
                }
                listener.progress(scale * recordIndex);
                recordIndex++;
            }
            flushAttributeCache(attributeList);
            attributeList.clear();
            
            listener.complete();
        }
        catch (IOException exception) {
            listener.exceptionOccurred(exception);
            throw exception;
        }
        finally {
            if (dbf != null) {
                dbf.close();
            }
            if (raf != null) {
                raf.close();
            }
            endJob(listener);
        }
        return true;
    }
}
