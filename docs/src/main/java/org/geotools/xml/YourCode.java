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
 *
 */

package org.geotools.xml;

import java.util.ArrayList;
import java.util.List;
import org.geotools.gml.GMLHandlerFeature;
import org.opengis.feature.simple.SimpleFeature;
import org.xml.sax.helpers.XMLFilterImpl;

// yourcode start
public class YourCode extends XMLFilterImpl implements GMLHandlerFeature {

    private List<SimpleFeature> features = new ArrayList<SimpleFeature>();

    public void feature(SimpleFeature feature) {
        features.add(feature);
    }

    public List<SimpleFeature> getFeatures() {
        return features;
    }
}
// yourcode end
