/* GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.data.csv;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.data.Query;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.vividsolutions.jts.geom.Point;

public class CSVDataStore extends ContentDataStore {
    File file;

    public CSVDataStore(File file) {
        this.file = file;
    }

    /**
     * Allow read access to file; for our package visible "friends". Please close the reader when done.
     * 
     * @return CsvReader for file
     */
    CsvReader read() throws IOException {
        Reader reader = new FileReader(file);
        CsvReader csvReader = new CsvReader(reader);
        return csvReader;
    }
    

    protected List<Name> createTypeNames() throws IOException {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));

        Name typeName = new NameImpl(name);
        return Collections.singletonList(typeName);
    }

    // createSchema start
    /**
     * Allow read access to file; for our package visible "friends". Please close the reader when done.
     * 
     * @return CsvReader for file
     */
    CsvWriter write() throws IOException {
        Writer reader = new FileWriter(file);
        CsvWriter csvWriter = new CsvWriter(reader,',');
        return csvWriter;
    }
    
    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        List<String> header = new ArrayList<String>();
        GeometryDescriptor geometryDescrptor = featureType.getGeometryDescriptor();
        if (geometryDescrptor != null
                && CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84,
                        geometryDescrptor.getCoordinateReferenceSystem())
                && geometryDescrptor.getType().getBinding().isAssignableFrom(Point.class)) {
            header.add("LAT");
            header.add("LON");
        } else {
            throw new IOException("Unable use LAT/LON to represent " + geometryDescrptor);
        }
        for (AttributeDescriptor descriptor : featureType.getAttributeDescriptors()) {
            if (descriptor instanceof GeometryDescriptor)
                continue;
            header.add(descriptor.getLocalName());
        }
        CsvWriter writer = write();
        try {
            writer.writeRecord( header.toArray(new String[header.size()]));
        }
        finally {
            writer.close();
        }
    }
    // createSchema end

    // createFeatureSource start
    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        if (file.canWrite()) {
            return new CSVFeatureStore(entry, Query.ALL);
        } else {
            return new CSVFeatureSource(entry, Query.ALL);
        }
    }
    // createFeatureSource end
}
