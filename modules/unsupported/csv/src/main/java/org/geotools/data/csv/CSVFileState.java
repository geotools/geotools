/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 * 	  (c) 2014 Open Source Geospatial Foundation - all rights reserved
 * 	  (c) 2012 - 2014 OpenPlans
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
package org.geotools.data.csv;

import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import org.apache.commons.io.FilenameUtils;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CSVFileState {

    private static CoordinateReferenceSystem DEFAULT_CRS() throws FactoryException {
        return CRS.decode("EPSG:4326");
    };

    private final File file;

    private final String typeName;

    private final CoordinateReferenceSystem crs;

    private final URI namespace;

    private final String dataInput;

    private volatile String[] headers = null;

    public CSVFileState(File file) {
        this(file, null, null, null);
    }

    public CSVFileState(File file, URI namespace) {
        this(file, namespace, null, null);
    }

    public CSVFileState(File file, URI namespace, String typeName, CoordinateReferenceSystem crs) {
        this.file = file;
        this.typeName = typeName;
        this.crs = crs;
        this.namespace = namespace;
        this.dataInput = null;
    }

    // used by unit tests
    public CSVFileState(String dataInput) {
        this(dataInput, null);
    }

    public CSVFileState(String dataInput, String typeName) {
        this.dataInput = dataInput;
        this.typeName = typeName;
        this.crs = null;
        this.namespace = null;
        this.file = null;
    }

    public URI getNamespace() {
        return namespace;
    }

    public File getFile() {
        return file;
    }

    public String getTypeName() {
        return typeName != null ? typeName : FilenameUtils.getBaseName(file.getPath());
    }

    public CoordinateReferenceSystem getCrs() {
        if (crs != null) {
            return crs;
        }

        try {
            return CSVFileState.DEFAULT_CRS();
        } catch (FactoryException e) {
            return null;
        }
    }

    // docs start openCSVReader
    public CsvReader openCSVReader() throws IOException {
        Reader reader;
        if (file != null) {
            reader = new BufferedReader(new FileReader(file));
        } else {
            reader = new StringReader(dataInput);
        }
        CsvReader csvReader = new CsvReader(reader);
        if (!csvReader.readHeaders()) {
            throw new IOException("Error reading csv headers");
        }
        return csvReader;
    }
    // docs end openCSVReader

    // docs start getCSVHeaders
    public String[] getCSVHeaders() {
        if (headers == null) {
            synchronized (this) {
                if (headers == null) {
                    headers = readCSVHeaders();
                }
            }
        }
        return headers;
    }

    private String[] readCSVHeaders() {
        CsvReader csvReader = null;
        try {
            csvReader = openCSVReader();
            return csvReader.getHeaders();
        } catch (IOException e) {
            throw new RuntimeException("Failure reading csv headers", e);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }
    }
    // docs end readCSVHeaders
}
