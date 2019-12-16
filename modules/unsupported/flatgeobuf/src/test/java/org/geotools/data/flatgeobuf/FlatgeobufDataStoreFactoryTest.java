/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataStore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FlatgeobufDataStoreFactoryTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void metadata() {
        FlatgeobufDataStoreFactory dataStoreFactory = new FlatgeobufDataStoreFactory();
        assertEquals("Flatgeobuf", dataStoreFactory.getDisplayName());
        assertEquals(
                "A DataStore for reading and writing Flatgeobuf files",
                dataStoreFactory.getDescription());
        assertTrue(dataStoreFactory.isAvailable());
        assertNull(dataStoreFactory.getImplementationHints());
        DataAccessFactory.Param[] params = dataStoreFactory.getParametersInfo();
        assertEquals(1, params.length);
        assertEquals(FlatgeobufDataStoreFactory.FILE_PARAM, params[0]);
    }

    @Test
    public void canProcess() throws IOException {
        File file = temporaryFolder.newFile("points.fgb");
        Map<String, Serializable> params = new HashMap<>();
        params.put("flatgeobuf-file", file);
        FlatgeobufDataStoreFactory dataStoreFactory = new FlatgeobufDataStoreFactory();
        assertTrue(dataStoreFactory.canProcess(params));
    }

    @Test
    public void cantProcess() throws IOException {
        File file = temporaryFolder.newFile("points.shp");
        Map<String, Serializable> params = new HashMap<>();
        params.put("flatgeobuf-file", file);
        FlatgeobufDataStoreFactory dataStoreFactory = new FlatgeobufDataStoreFactory();
        assertFalse(dataStoreFactory.canProcess(params));
    }

    @Test
    public void cantProcessWrongName() throws IOException {
        File file = temporaryFolder.newFile("points.shp");
        Map<String, Serializable> params = new HashMap<>();
        params.put("wrong name", file);
        FlatgeobufDataStoreFactory dataStoreFactory = new FlatgeobufDataStoreFactory();
        assertFalse(dataStoreFactory.canProcess(params));
    }

    @Test
    public void createDataStoreFromFile() throws IOException {
        File file = temporaryFolder.newFile("points.fgb");
        Map<String, Serializable> params = new HashMap<>();
        params.put("flatgeobuf-file", file);
        FlatgeobufDataStoreFactory dataStoreFactory = new FlatgeobufDataStoreFactory();
        DataStore dataStore = dataStoreFactory.createDataStore(params);
        assertTrue(dataStore instanceof FlatgeobufDataStore);
    }

    @Test
    public void createDataStoreFromDirectory() throws IOException {
        File file = temporaryFolder.newFolder("layers");
        Map<String, Serializable> params = new HashMap<>();
        params.put("flatgeobuf-file", file);
        FlatgeobufDataStoreFactory dataStoreFactory = new FlatgeobufDataStoreFactory();
        DataStore dataStore = dataStoreFactory.createDataStore(params);
        assertTrue(dataStore instanceof FlatgeobufDirectoryDataStore);
    }

    @Test
    public void createNewDataStoreFromFile() throws IOException {
        File file = temporaryFolder.newFile("points.fgb");
        Map<String, Serializable> params = new HashMap<>();
        params.put("flatgeobuf-file", file);
        FlatgeobufDataStoreFactory dataStoreFactory = new FlatgeobufDataStoreFactory();
        DataStore dataStore = dataStoreFactory.createNewDataStore(params);
        assertTrue(dataStore instanceof FlatgeobufDataStore);
    }

    @Test
    public void createNewDataStoreFromDirectory() throws IOException {
        File file = temporaryFolder.newFolder("layers");
        Map<String, Serializable> params = new HashMap<>();
        params.put("flatgeobuf-file", file);
        FlatgeobufDataStoreFactory dataStoreFactory = new FlatgeobufDataStoreFactory();
        DataStore dataStore = dataStoreFactory.createNewDataStore(params);
        assertTrue(dataStore instanceof FlatgeobufDirectoryDataStore);
    }
}
