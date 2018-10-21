/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex;

import java.util.List;
import org.geotools.data.FeatureSource;
import org.geotools.data.complex.config.MultipleValue;
import org.opengis.feature.type.AttributeDescriptor;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 */
public class FeatureTypeMappingFactory {

    public static FeatureTypeMapping getInstance(
            FeatureSource source,
            FeatureSource indexSource,
            AttributeDescriptor target,
            String defaultGeometryXPath,
            List<AttributeMapping> mappings,
            NamespaceSupport namespaces,
            String itemXpath,
            boolean isXmlDataStore,
            boolean isDenormalised) {
        FeatureTypeMapping featureTypeMapping;
        if (isXmlDataStore) {
            featureTypeMapping =
                    new XmlFeatureTypeMapping(source, target, mappings, namespaces, itemXpath);
        } else {
            featureTypeMapping =
                    new FeatureTypeMapping(
                            source,
                            indexSource,
                            target,
                            defaultGeometryXPath,
                            mappings,
                            namespaces,
                            isDenormalised);
        }
        featureTypeMapping
                .getAttributeMappings()
                .forEach(
                        attributeMapping -> {
                            MultipleValue multipleValue = attributeMapping.getMultipleValue();
                            if (multipleValue != null) {
                                multipleValue.setFeatureTypeMapping(featureTypeMapping);
                                multipleValue.setAttributeMapping(attributeMapping);
                            }
                        });
        return featureTypeMapping;
    }
}
