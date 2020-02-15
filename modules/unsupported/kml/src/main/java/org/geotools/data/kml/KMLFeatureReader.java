/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.kml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.geotools.data.FeatureReader;
import org.geotools.kml.KML;
import org.geotools.kml.KMLConfiguration;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Read a KML file directly.
 *
 * @author Niels Charlier, Scitus Development
 */
public class KMLFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {
    private static final Logger LOGGER = Logging.getLogger(KMLFeatureReader.class);

    SimpleFeatureType type = null;
    SimpleFeature f = null;
    // PullParser parser;
    org.geotools.xsd.StreamingParser parser;
    FileInputStream fis;

    public KMLFeatureReader(String namespace, File file, QName name) throws IOException {
        fis = new FileInputStream(file);
        try {
            parser =
                    new org.geotools.xsd.StreamingParser(
                            new KMLConfiguration(), fis, KML.Placemark);
        } catch (Exception e) {
            throw new IOException("Error processing KML file", e);
        }
        forward();
        if (f != null) type = f.getType();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return type;
    }

    /**
     * Grab the next feature from the property file.
     *
     * @return feature
     * @throws NoSuchElementException Check hasNext() to avoid reading off the end of the file
     */
    @Override
    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        SimpleFeature next = f;
        forward();
        return next;
    }

    public void forward() throws IOException {
        try {
            f = (SimpleFeature) parser.parse();
        } catch (Exception e) {
            throw new IOException("Error processing KML file", e);
        }
    }

    /** */
    @Override
    public boolean hasNext() throws IOException {
        return f != null;
    }

    /**
     * Be sure to call close when you are finished with this reader; as it must close the file it
     * has open.
     *
     */
    @Override
    public void close() throws IOException {
        fis.close();
    }
}
