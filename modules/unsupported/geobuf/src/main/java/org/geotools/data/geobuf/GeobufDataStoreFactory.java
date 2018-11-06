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
package org.geotools.data.geobuf;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.util.KVP;
import org.geotools.util.logging.Logging;

public class GeobufDataStoreFactory implements DataStoreFactorySpi {

    private static final Logger LOGGER = Logging.getLogger(GeobufDataStoreFactory.class);

    public static final Param FILE_PARAM =
            new Param(
                    "file",
                    File.class,
                    "The Geobuf file or directory",
                    true,
                    null,
                    new KVP(Param.EXT, "pbf"));

    public static final Param PRECISION_PARAM =
            new Param(
                    "precision",
                    Integer.class,
                    "The coordinate preceision",
                    false,
                    6,
                    new KVP("precision", "6"));

    public static final Param DIMENSION_PARAM =
            new Param(
                    "dimension",
                    Integer.class,
                    "The geometry dimension",
                    false,
                    2,
                    new KVP("precision", "2"));

    public GeobufDataStoreFactory() {}

    @Override
    public DataStore createDataStore(Map<String, Serializable> map) throws IOException {
        File file = (File) FILE_PARAM.lookUp(map);
        Integer precision = (Integer) PRECISION_PARAM.lookUp(map);
        if (precision == null) {
            precision = 6;
        }
        Integer dimension = (Integer) DIMENSION_PARAM.lookUp(map);
        if (dimension == null) {
            dimension = 2;
        }
        if (file.isDirectory()) {
            return new GeobufDirectoryDataStore(file, precision, dimension);
        } else {
            return new GeobufDataStore(file, precision, dimension);
        }
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> map) throws IOException {
        File file = (File) FILE_PARAM.lookUp(map);
        Integer precision = (Integer) PRECISION_PARAM.lookUp(map);
        if (precision == null) {
            precision = 6;
        }
        Integer dimension = (Integer) DIMENSION_PARAM.lookUp(map);
        if (dimension == null) {
            dimension = 2;
        }
        if (file.isDirectory()) {
            return new GeobufDirectoryDataStore(file, precision, dimension);
        } else {
            if (file.exists()) {
                LOGGER.warning("File already exists: " + file);
            }
            return new GeobufDataStore(file, precision, dimension);
        }
    }

    @Override
    public String getDisplayName() {
        return "Geobuf";
    }

    @Override
    public String getDescription() {
        return "A DataStore for reading and writing Geobuf files";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {FILE_PARAM, PRECISION_PARAM, DIMENSION_PARAM};
    }

    @Override
    public boolean canProcess(Map<String, Serializable> map) {
        try {
            File file = (File) FILE_PARAM.lookUp(map);
            if (file != null) {
                return file.isDirectory()
                        || file.getPath().toLowerCase().endsWith(".pbf")
                        || file.getPath().toLowerCase().endsWith(".geobuf");
            }
        } catch (IOException e) {
            // ignore as we are expected to return true or false
        }
        return false;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return null;
    }
}
