/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.csv3;

import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FilenameUtils;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;

// docs start CSVFileState
/** Details from comma separated value file. */
public class CSVFileState {

    private static CoordinateReferenceSystem DEFAULT_CRS() throws FactoryException {
        return CRS.decode("EPSG:4326");
    }
    ;

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
    @SuppressWarnings("PMD.CloseResource") // reader is wrapped and returned
    public CsvReader openCSVReader() throws IOException {
        Reader reader;
        if (file != null) {
            reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
        } else {
            reader = new StringReader(dataInput);
        }
        CsvReader csvReader = new CsvReader(reader);
        if (!csvReader.readHeaders()) {
            reader.close();
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
