/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 Open Source Geospatial Foundation (OSGeo)
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

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
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

// docs start CSVFileState
/** Details from comma separated value file. */
public class CSVFileState {
    private char separator = ',';
    private char quotechar = '"';
    private char escapechar = '\\';
    private String lineSeparator = System.lineSeparator();
    private boolean quoteAllFields = false;

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
    public CSVReader openCSVReader() throws IOException, CsvValidationException {
        Reader reader = null;

        if (file != null) {
            reader = new BufferedReader(new FileReader(file));
        } else {
            reader = new StringReader(dataInput);
        }

        final CSVParser parser =
                new CSVParserBuilder()
                        .withSeparator(separator)
                        .withEscapeChar(escapechar)
                        .withQuoteChar(quotechar)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withIgnoreQuotations(true)
                        .build();
        final CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();

        String[] tnames;
        if ((tnames = csvReader.readNext()) == null) {
            throw new IOException("Error reading csv headers");
        } else {
            if (headers == null) {
                synchronized (this) {
                    for (int i = 0; i < tnames.length; ++i) {
                        tnames[i] = tnames[i].trim();
                    }
                    headers = tnames;
                }
            }
        }
        return csvReader;
    }
    // docs end openCSVReader

    // docs start getCSVHeaders
    public String[] getCSVHeaders() {
        if (headers == null) {
            throw new RuntimeException("Attempting to access unopened CSV Reader");
        }
        return headers;
    }

    /** @return the quotechar */
    public char getQuotechar() {
        return quotechar;
    }

    /** @param quotechar the quotechar to set */
    public void setQuotechar(char quotechar) {
        this.quotechar = quotechar;
    }

    /** @param escapechar2 */
    public void setEscapechar(char escapechar2) {
        this.escapechar = escapechar2;
    }

    /** @return */
    public char getEscapechar() {
        return escapechar;
    }

    /** @return the separator */
    public char getSeparator() {
        return separator;
    }

    /** @param separator the separator to set */
    public void setSeparator(char separator) {
        this.separator = separator;
    }

    /** @return the lineSeparator */
    public String getLineSeparator() {
        return lineSeparator;
    }

    /** @param lineSeparator the lineSeparator to set */
    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    /** @return the quoteAllFields */
    public boolean isQuoteAllFields() {
        return quoteAllFields;
    }

    /** @param quoteAllFields the quoteAllFields to set */
    public void setQuoteAllFields(boolean quoteAllFields) {
        this.quoteAllFields = quoteAllFields;
    }

    // docs end readCSVHeaders
}
