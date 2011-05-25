/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2009, Open Source Geospatial Foundation (OSGeo)
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
import org.opengis.feature.type.AttributeDescriptor;
import org.xml.sax.helpers.NamespaceSupport;
/**
 * @author Russell Petty, GSV
 * @version $Id$
 *
 * @source $URL$
 */
public class FeatureTypeMappingFactory {

   public static  FeatureTypeMapping getInstance(FeatureSource source, AttributeDescriptor target,
           List<AttributeMapping> mappings, NamespaceSupport namespaces,
           String itemXpath, boolean isXmlDataStore) {
       
       if(isXmlDataStore) {
           return new XmlFeatureTypeMapping(source, target,
                   mappings, namespaces, itemXpath);           
       } else {
           return new FeatureTypeMapping(source, target,
                   mappings, namespaces, itemXpath);
       }       
   }
}
