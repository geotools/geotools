/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.gml3.ApplicationSchemaConfiguration;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.AppSchemaConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;

/**
 * Parses an application schema given by a gtxml {@link Configuration} into a set of
 * {@link AttributeType}s and {@link AttributeDescriptor}s.
 * <p>
 * All the XSD schema locations that comprise the application schema are obtained from the main
 * {@link Configuration} and its dependencies.
 * </p>
 * <p>
 * Of particular interest might be the {@link ApplicationSchemaConfiguration} object, which allows
 * to provide the location of the root xsd schema for a given application schema.
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 *
 *
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/branches/2.4.x/modules/unsupported/community-schemas
 *         /community
 *         -schema-ds/src/main/java/org/geotools/data/complex/config/EmfAppSchemaReader.java $
 * @since 2.4
 */
public class EmfAppSchemaReader extends EmfComplexFeatureReader {

    private EmfAppSchemaReader() {
        // do nothing
    }
    
    public static EmfAppSchemaReader newInstance() {
        return new EmfAppSchemaReader();
    }
    
    /**
     * Map of the qualified-name of a known type in each supported GML version to the {@link Configuration} for that GML version.
     */
    @SuppressWarnings("serial")
    private static final Map<QName, Class<? extends Configuration>> SUPPORTED_GML_KNOWN_TYPE_TO_CONFIGURATION_MAP //
    = new LinkedHashMap<QName, Class<? extends Configuration>>() {
        {
            // GML 3.1
            put(GML.AbstractFeatureType, GMLConfiguration.class);
            // GML 3.2
            put(org.geotools.gml3.v3_2.GML.AbstractFeatureType,
                    org.geotools.gml3.v3_2.GMLConfiguration.class);
        }
    };

    public static Configuration findGmlConfiguration(AppSchemaConfiguration configuration) {
        SchemaIndex index = null;
        try {
            index = Schemas.findSchemas(configuration);
            for (QName name : SUPPORTED_GML_KNOWN_TYPE_TO_CONFIGURATION_MAP.keySet()) {
                XSDTypeDefinition type = index.getTypeDefinition(name);
                if (type != null) {
                    try {
                        return SUPPORTED_GML_KNOWN_TYPE_TO_CONFIGURATION_MAP.get(name)
                                .newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            for (XSDSchema schema : index.getSchemas()) {
                String ns = schema.getTargetNamespace();
                if (ns != null && ns.startsWith("http://www.opengis.net/gml")) {
                    throw new RuntimeException("Unsupported GML version for schema at "
                            + configuration.getSchemaLocation());
                }
            }
        } finally {
            if (index != null) {
                index.destroy();
            }
        }
        return null;
    }

}
