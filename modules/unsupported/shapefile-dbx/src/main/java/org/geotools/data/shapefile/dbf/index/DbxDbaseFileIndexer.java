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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.resources.Arguments;
import org.opengis.util.ProgressListener;

/**
 * Implements a Dbase attribute indexer for DBX-files.
 * 
 * @author Alvaro Huarte
 */
public class DbxDbaseFileIndexer extends DbaseFileIndexer {
    
    private RandomAccessFile offsetsFile;
    private Map<Integer,HashMap<Object,Map.Entry<Long,Long>>> offsetsHash;
    private long offsetsFlen;
    private DbaseFileHeader header;
    private boolean writeCompressed;
    
    /**
     * Creates a new DbxDbaseFileIndexer object.
     */
    public DbxDbaseFileIndexer(File dbaseFile, Charset charset, TimeZone timeZone, boolean writeCompressed) {
        super(dbaseFile, charset, timeZone);
        this.writeCompressed = writeCompressed;
    }
    
    @Override
    protected void startJob(DbaseFileHeader header, List<Integer> columnList, ProgressListener listener) throws IOException {
        
        String fname = dbaseFile.getPath();
        int spos = fname.lastIndexOf('.');
        File tempsFile = new File(fname.substring(0, spos) + ".tmpdbx");
        if (tempsFile.exists()) tempsFile.delete();
        tempsFile.deleteOnExit();
        
        // Initialize buffers.
        offsetsFile = new RandomAccessFile(tempsFile, "rw");
        offsetsHash = new HashMap<Integer,HashMap<Object,Map.Entry<Long,Long>>>();
        offsetsFlen = 0;
        
        for (Integer column : columnList) {
            offsetsHash.put(column, new HashMap<Object,Map.Entry<Long,Long>>());
        }
        this.header = header;
    }
    
    /**
     * Write the specified DBX-index node.
     * @throws IOException
     */
    private long writeIndexNode(long offsetPos, RandomAccessFile file, long filePos, Class<?> binding, Object key, ByteArrayOutputStream buffer) throws IOException {
        
        // Write data of the header node (filePos of next node and number of records).
        long currentPos = filePos;
        file.writeLong(-1);
        file.writeInt(0);
        filePos += 12;
        
        // Write the key or value of the attribute.
        filePos += DbxNodeKey.writeKey(file, binding, key, charset);
        
        buffer.reset();
        int recordCount = readRecordList(offsetPos, buffer);
        
        // Write full record collection (Compress the list when there are 'too many' records).
        if (writeCompressed && recordCount > DbxDbaseFileIndex.DBX_COMPRESS_RECORD_LIMIT) {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final GZIPOutputStream os = new GZIPOutputStream(baos);
            os.write(buffer.toByteArray());
            os.close();
            byte[] b = baos.toByteArray();
            file.writeInt(b.length);
            file.write(b, 0, b.length);
            filePos += 4 + b.length;
            baos.close();
        } else {
            file.write(buffer.toByteArray());
            filePos += recordCount * 4;
        }
        
        // Update data of header node.
        file.seek(currentPos + 8);
        file.writeInt(recordCount);
        file.seek(filePos);
        
        return filePos;
    }
    /**
     * Write the specified record list.
     * @throws IOException
     */
    private int readRecordList(long offsetPos, ByteArrayOutputStream buffer) throws IOException {
        
        // Read the current data saved in temporal file.
        offsetsFile.seek(offsetPos);
        long nextBrotherPos = offsetsFile.readLong();
        int recordCount = offsetsFile.readInt();
        
        // Write record collection.
        byte[] b = new byte[4 * recordCount];
        offsetsFile.read(b, 0, b.length);
        buffer.write(b);
        
        // Write next brother node.
        if (nextBrotherPos!=-1) {
            recordCount += readRecordList(nextBrotherPos, buffer);
        }
        return recordCount;
    }
    
    @Override
    protected void endJob(ProgressListener listener) {
        
        try {
            // Write DBX-index file.
            if (offsetsFile != null && offsetsHash != null && offsetsHash.size() > 0) {
                String fname = dbaseFile.getPath();
                int spos = fname.lastIndexOf('.');
                
                File indexFile = new File(fname.substring(0, spos) + ".dbx");
                if (indexFile.exists()) indexFile.delete();
                RandomAccessFile file = new RandomAccessFile(indexFile,"rw");
                
                // Write header.
                byte[] headerb = new byte[512];
                file.write(headerb, 0, headerb.length);
                long filePos = headerb.length;
                
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                List<Long> offsetsList = new ArrayList<Long>();
                int offsetsIndex = 0;
                int offsetsCount = 0;
                
                // Write each attribute + (key-record) collection.
                for (Map.Entry<Integer,HashMap<Object,Map.Entry<Long,Long>>> entryOb : offsetsHash.entrySet()) {
                    
                    // If key count coincides with number of records, we save a special flag to avoid
                    // to write and query all data. As a result this field is considered not indexed.
                    if (entryOb.getValue().size() == header.getNumRecords()) {
                        offsetsList.add(-1L);
                    }
                    else {
                        offsetsList.add(filePos);
                        offsetsCount++;
                        
                        Class<?> binding = header.getFieldClass(entryOb.getKey());
                        long lastFilePos = -1;
                        
                        for (Map.Entry<Object,Map.Entry<Long,Long>> itemOb : entryOb.getValue().entrySet()) {
                            long newFilePos = writeIndexNode(itemOb.getValue().getKey(), file, filePos, binding, itemOb.getKey(), buffer);
                            if (lastFilePos > 0) {
                                file.seek(lastFilePos);
                                file.writeLong(filePos);
                                file.seek(newFilePos);
                            }
                            lastFilePos = filePos;
                            filePos = newFilePos;
                        }
                    }
                }
                
                // Write attribute header.
                file.seek(0);
                file.writeInt(DbxDbaseFileIndex.DBX_MAGIC_CODE);
                file.writeInt(writeCompressed ? 1 : 0);
                file.writeInt(offsetsCount);
                
                for (Map.Entry<Integer,HashMap<Object,Map.Entry<Long,Long>>> entryOb : offsetsHash.entrySet()) {
                    long offsetPos = offsetsList.get(offsetsIndex++);
                    if (offsetPos != -1) {
                        String fieldName = header.getFieldName(entryOb.getKey());
                        DbxNodeKey.writeString(file, fieldName, 11, charset);
                        file.writeLong(offsetPos);
                    }
                }
                file.close();
            }
        }
        catch (IOException exception) {
            listener.exceptionOccurred(exception);
        }
        finally {
            if (offsetsFile != null) {
                try {
                    offsetsFile.close();
                }
                catch (IOException e) {
                    listener.exceptionOccurred(e);
                }
                offsetsFile = null;
            }
            if (offsetsHash != null) {
                for (HashMap<Object,Map.Entry<Long,Long>> entryOb : offsetsHash.values()) {
                    entryOb.clear();
                }
                offsetsHash.clear();
                offsetsHash = null;
            }
            offsetsFlen = 0;
            header = null;
        }
    }
    
    @Override
    protected void flush(int fieldIndex, String fieldName, Class<?> binding, Map<Object,List<Integer>> keyRecords) throws IOException {
        
        HashMap<Object,Map.Entry<Long,Long>> keyHash = offsetsHash.get(fieldIndex);
        Map.Entry<Long,Long> entry = null;
        
        // Write the specified key-records pairs to temporal file and save last filePos.
        for (Map.Entry<Object,List<Integer>> entryOb : keyRecords.entrySet()) {
            
            Object key = entryOb.getKey();
            List<Integer> records = entryOb.getValue();
            Long startPos = offsetsFlen, filePos = offsetsFlen;
            
            if ((entry = keyHash.get(key))!=null) {
                startPos = entry.getKey();
                offsetsFile.seek(entry.getValue());
                offsetsFile.writeLong(filePos);
            }
            
            // Write the next offset node.
            offsetsFile.seek(filePos);
            offsetsFile.writeLong(-1);
            offsetsFlen += 8;
            
            // Write the related DBF-record collection of the key.
            offsetsFile.writeInt(records.size());
            if (records.size() > 0) {
                byte[] buffer = new byte[4 * records.size()];
                for (int i = 0, icount = records.size(), j = 0; i < icount; i++) {
                    int value = records.get(i);
                    buffer[j++] = (byte)(value >> 24);
                    buffer[j++] = (byte)(value >> 16);
                    buffer[j++] = (byte)(value >> 8);
                    buffer[j++] = (byte)(value);
                }
                offsetsFile.write(buffer);
            }
            offsetsFlen += 4 * (1 + records.size());
            
            // Update buffer.
            keyHash.put(key, new AbstractMap.SimpleEntry<Long,Long>(startPos,filePos));
        }
    }
    
    /**
     * Write the DBX-index files of the specified Dbase files.
     *  
     * @param args Command line arguments.
     */
    public static final void main(String[] args) {
        
        long start = System.currentTimeMillis();
        long fileCount = 0;
        
        final Arguments arguments = new Arguments(args);
        final PrintWriter out = arguments.out;
        
        List<String> attributeList = new ArrayList<String>();
        File path = null;
        
        Charset charset = ShapefileDataStore.DEFAULT_STRING_CHARSET;
        TimeZone timeZone = ShapefileDataStore.DEFAULT_TIMEZONE;
        boolean writeCompressed = true;
        
        if ((args.length < 1) || (((args.length - 1) % 2) != 0)) {
            usage(out);
            System.exit(1);
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-a")) {
                for (String s : args[++i].split(",")) attributeList.add(s);
            }
            else
            if (args[i].equals("-c")) {
                charset = Charset.forName(args[++i]);
            }
            else
            if (args[i].equals("-z")) {
                writeCompressed = args[++i] == "1" || args[i].toLowerCase() == "true";
            }
            else {
                path = new File(args[i]);
            }
        }
        
        try {
            PrintWriterProgressListener listener = new PrintWriterProgressListener(out);
            int maximumCacheSize = 1024*1024;
            
            if (path.isDirectory()) {
                File[] files = path.listFiles( new FileFilter(){
                    public boolean accept(File pathname) {
                        return pathname.getName().toLowerCase().endsWith(".dbf");
                    }
                });
                for (File file : files) {
                    DbxDbaseFileIndexer indexer = new DbxDbaseFileIndexer(file, charset, timeZone, writeCompressed);
                    indexer.buildIndex(attributeList, maximumCacheSize, listener);
                    fileCount++;
                }
            }
            else {
                DbxDbaseFileIndexer indexer = new DbxDbaseFileIndexer(path, charset, timeZone, writeCompressed);
                indexer.buildIndex(attributeList, maximumCacheSize, listener);
                fileCount++;
            }
            out.print(fileCount + " files indexed ");
            out.println("in " + (System.currentTimeMillis() - start) + " ms.");
            out.println();
            out.flush();
        }
        catch (IOException e) {
            out.println("ERROR:" +  e.getLocalizedMessage());
            usage(out);
        }
    }
    
    private static void usage(PrintWriter out) {
        out.println("Usage: DbxDbaseFileIndexer [-a <attribute list to index>] [-c <charset>] [-z <compress>] <dbase path>");
        out.flush();
    }
}
