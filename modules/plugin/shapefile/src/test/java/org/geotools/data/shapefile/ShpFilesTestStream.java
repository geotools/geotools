/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import static org.geotools.data.shapefile.ShpFileType.PRJ;
import static org.geotools.data.shapefile.ShpFileType.SHP;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.geotools.TestData;

public class ShpFilesTestStream extends TestCase implements
        org.geotools.data.shapefile.FileWriter {

    private String typeName;
    private Map<ShpFileType, File> map;
    private ShpFiles files;

    @Override
    protected void setUp() throws Exception {
        map = ShpFilesTest.createFiles("shpFiles", ShpFileType.values(), false);

        typeName = map.get(SHP).getName();
        typeName = typeName.substring(0, typeName.lastIndexOf("."));

        files = new ShpFiles(map.get(SHP));
    }

    private void writeDataToFiles() throws IOException {
        Set<Entry<ShpFileType, File>> entries = map.entrySet();
        for (Entry<ShpFileType, File> entry : entries) {
            FileWriter out = new FileWriter(entry.getValue());
            try {
                out.write(entry.getKey().name());
            } finally {
                out.close();
            }
        }
    }

    public void testIsLocalURL() throws IOException {
        ShpFiles files = new ShpFiles("http://someurl.com/file.shp");
        assertFalse(files.isLocal());
    }

    public void testIsLocalFiles() throws IOException {
        assertTrue(files.isLocal());
    }

    public void testDelete() throws IOException {

        assertTrue(files.delete());

        for (File file : map.values()) {
            assertFalse(file.exists());
        }
    }

    public void testExceptionGetInputStream() throws Exception {
        ShpFiles shpFiles = new ShpFiles(new URL("http://blah/blah.shp"));
        try{
            shpFiles.getInputStream(SHP, this);
            fail("maybe test is bad?  We want an exception here");
        }catch(Throwable e){
            assertEquals(0, shpFiles.numberOfLocks());
        }
    }

    public void testExceptionGetOutputStream() throws Exception {
        ShpFiles shpFiles = new ShpFiles(new URL("http://blah/blah.shp"));
        try{
            shpFiles.getOutputStream(SHP, this);
            fail("maybe test is bad?  We want an exception here");
        }catch(Throwable e){
            assertEquals(0, shpFiles.numberOfLocks());
        }
    }

    public void testExceptionGetWriteChannel() throws Exception {
        ShpFiles shpFiles = new ShpFiles(new URL("http://blah/blah.shp"));
        try{
            shpFiles.getWriteChannel(SHP, this);
            fail("maybe test is bad?  We want an exception here");
        }catch(Throwable e){
            assertEquals(0, shpFiles.numberOfLocks());
        }
    }

    public void testExceptionGetReadChannel() throws Exception {
        ShpFiles shpFiles = new ShpFiles(new URL("http://blah/blah.shp"));
        try{
            shpFiles.getReadChannel(SHP, this);
            fail("maybe test is bad?  We want an exception here");
        }catch(Throwable e){
            assertEquals(0, shpFiles.numberOfLocks());
        }
    }
    
    public void testGetInputStream() throws IOException {
        writeDataToFiles();

        ShpFileType[] types = ShpFileType.values();
        for (ShpFileType shpFileType : types) {
            String read = "";
            InputStream in = files.getInputStream(shpFileType, this);
            InputStreamReader reader = new InputStreamReader(in);
            assertEquals(1, files.numberOfLocks());
            try {
                int current = reader.read();
                while (current != -1) {
                    read += (char) current;
                    current = reader.read();
                }
            } finally {
                reader.close();
                in.close();
                assertEquals(0, files.numberOfLocks());
            }
            assertEquals(shpFileType.name(), read);
        }
    }

    public void testGetWriteStream() throws IOException {

        ShpFileType[] types = ShpFileType.values();
        for (ShpFileType shpFileType : types) {
            
            OutputStream out = files.getOutputStream(shpFileType, this);
            assertEquals(1, files.numberOfLocks());
            try {
                out.write((byte)2);
            } finally {
                out.close();
                assertEquals(0, files.numberOfLocks());
            }
        }
    }

    public void testGetReadChannelFileChannel() throws IOException {
        writeDataToFiles();

        ShpFileType[] types = ShpFileType.values();
        for (ShpFileType shpFileType : types) {
            doRead(shpFileType);
        }
    }

    public void testGetReadChannelURL() throws IOException {
        ShpFiles files = new ShpFiles(TestData.url("shapes/statepop.shp"));
        
        assertFalse(files.isLocal());
        
        ReadableByteChannel read = files.getReadChannel(SHP, this);
        
        assertEquals(1, files.numberOfLocks());
        
        read.close();
        
        assertEquals(0, files.numberOfLocks());
    }
    
    private void doRead(ShpFileType shpFileType) throws IOException {
        ReadableByteChannel in = files.getReadChannel(shpFileType, this);
        assertEquals(1, files.numberOfLocks());
        assertTrue(in instanceof FileChannel);

        ByteBuffer buffer = ByteBuffer.allocate(10);
        in.read(buffer);
        buffer.flip();
        String read = "";
        try {
            while (buffer.hasRemaining()) {
                read += (char) buffer.get();
            }
        } finally {
            in.close();
            // verify that you can close multiple times without bad things
            // happening
            in.close();
        }
        assertEquals(0, files.numberOfLocks());
        assertEquals(shpFileType.name(), read);
    }

    private void doWrite(ShpFileType shpFileType) throws IOException {
        WritableByteChannel out = files.getWriteChannel(shpFileType, this);
        assertEquals(1, files.numberOfLocks());
        assertTrue(out instanceof FileChannel);

        try {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put(shpFileType.name().getBytes());
            buffer.flip();
            out.write(buffer);
        } finally {
            out.close();
            // verify that you can close multiple times without bad things
            // happening
            out.close();
        }
        assertEquals(0, files.numberOfLocks());
    }

    public void testGetWriteChannel() throws IOException {

        ShpFileType[] types = ShpFileType.values();
        for (ShpFileType shpFileType : types) {
            doWrite(shpFileType);
            doRead(shpFileType);
        }
    }

    public void testGetStorageFile() throws Exception {
        StorageFile prj = files.getStorageFile(PRJ);
        assertTrue(prj.getFile().getName().startsWith(typeName));
        assertTrue(prj.getFile().getName().endsWith(".prj"));
    }

    public void testGetTypeName() throws Exception {
        assertEquals(typeName, files.getTypeName());
    }

    public String id() {
        return getClass().getName();
    }

}
