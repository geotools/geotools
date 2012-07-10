/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.transform;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.FeatureSource;
import org.geotools.data.ResourceInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A default implementaiton of a {@link ResourceInfo} based solely on the information that can be
 * obtained from {@link FeatureSource} (schema and bounds)
 * 
 * @author Andrea Aime - GeoSolutions
 */
class DefaultResourceInfo implements ResourceInfo {

    FeatureSource fs;

    Set<String> words;

    public DefaultResourceInfo(FeatureSource fs) {
        this.fs = fs;
        words = new HashSet<String>();
        {
            words.add("features");
            words.add(fs.getSchema().getName().toString());
        }
    }

    @Override
    public ReferencedEnvelope getBounds() {
        try {
            return fs.getBounds();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public CoordinateReferenceSystem getCRS() {
        return fs.getSchema().getCoordinateReferenceSystem();
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Set<String> getKeywords() {
        return words;
    }

    @Override
    public String getName() {
        return fs.getSchema().getName().getLocalPart();
    }

    @Override
    public URI getSchema() {
        Name name = fs.getSchema().getName();
        URI namespace;
        try {
            namespace = new URI(name.getNamespaceURI());
            return namespace;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    public String getTitle() {
        Name name = fs.getSchema().getName();
        return name.getLocalPart();
    }

}
