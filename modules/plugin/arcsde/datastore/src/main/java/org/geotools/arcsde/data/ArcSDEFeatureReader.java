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
package org.geotools.arcsde.data;

import java.io.IOException;

import org.geotools.data.AttributeReader;
import org.geotools.data.DefaultFeatureReader;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * FeatureReader<SimpleFeatureType, SimpleFeature> optimized for ArcSDE access.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/ArcSDEFeatureReader.java $
 */
public class ArcSDEFeatureReader extends DefaultFeatureReader {

    private SimpleFeatureType featureType;

    private SimpleFeatureBuilder featureBuilder;

    public ArcSDEFeatureReader(final ArcSDEAttributeReader attributeReader) throws SchemaException {
        super(attributeReader, attributeReader.getFeatureType());

        this.featureType = attributeReader.getFeatureType();
        this.featureBuilder = new SimpleFeatureBuilder(featureType);
    }

    @Override
    protected SimpleFeature readFeature(final AttributeReader atts)
            throws IllegalAttributeException, IOException {
        final ArcSDEAttributeReader sdeAttReader = (ArcSDEAttributeReader) atts;

        final int attCount = sdeAttReader.getAttributeCount();
        Object value;
        for (int index = 0; index < attCount; index++) {
            value = sdeAttReader.read(index);
            featureBuilder.set(index, value);
        }
        String fid = sdeAttReader.readFID();
        SimpleFeature feature = featureBuilder.buildFeature(fid);
        return feature;
    }

}
