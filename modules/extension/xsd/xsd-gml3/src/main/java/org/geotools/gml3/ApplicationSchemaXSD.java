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
package org.geotools.gml3;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import org.eclipse.xsd.XSDSchema;
import org.geotools.util.URLs;
import org.geotools.xsd.SchemaLocationResolver;
import org.geotools.xsd.XSD;
import org.xml.sax.EntityResolver;

/**
 * XSD instance for an application schema.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class ApplicationSchemaXSD extends XSD {
    /** application schema namespace */
    private String namespaceURI;

    /** location of the application schema itself */
    private String schemaLocation;

    public ApplicationSchemaXSD(String namespaceURI, String schemaLocation) {
        this(namespaceURI, schemaLocation, null);
    }

    public ApplicationSchemaXSD(String namespaceURI, String schemaLocation, EntityResolver resolver) {
        this.namespaceURI = namespaceURI;
        this.schemaLocation = schemaLocation;
        this.entityResolver = resolver;
    }

    @Override
    protected void addDependencies(Set<XSD> dependencies) {
        dependencies.add(GML.getInstance());
    }

    @Override
    public String getNamespaceURI() {
        return namespaceURI;
    }

    @Override
    public String getSchemaLocation() {
        return schemaLocation;
    }

    /**
     * Uses the <code>schema.getSchemaLocation()</code>'s parent folder as the base folder to resolve <code>location
     * </code> as a relative URI of.
     *
     * <p>This way, application schemas splitted over multiple files can be resolved based on the relative location of a
     * given import or include.
     *
     * @return a file: style uri with the resolved schema location for the given one, or <code>null
     *     </code> if <code>location</code> can't be resolved as a relative path of the <code>schema
     *     </code> location.
     */
    @Override
    public SchemaLocationResolver createSchemaLocationResolver() {
        return new SchemaLocationResolver(this) {
            @Override
            public String resolveSchemaLocation(XSDSchema schema, String uri, String location) {
                String schemaLocation;

                if (schema == null) {
                    schemaLocation = getSchemaLocation();
                } else {
                    schemaLocation = schema.getSchemaLocation();
                }

                String locationUri = null;

                if (null != schemaLocation && !"".equals(schemaLocation)) {
                    String schemaLocationFolder = schemaLocation;
                    int lastSlash = schemaLocation.lastIndexOf('/');

                    if (lastSlash > 0) {
                        schemaLocationFolder = schemaLocation.substring(0, lastSlash);
                    }

                    if (schemaLocationFolder.startsWith("file:")) {
                        try {
                            schemaLocationFolder = URLs.urlToFile(new URL(schemaLocationFolder))
                                    .getPath();
                        } catch (MalformedURLException e) {
                            // this can't be a good outcome, but try anyway
                            schemaLocationFolder = schemaLocationFolder.substring("file:".length());
                        }
                    }

                    File locationFile = new File(schemaLocationFolder, location);

                    if (locationFile.exists()) {
                        locationUri = locationFile.toURI().toString();
                    }

                    if (locationUri == null && location != null && schemaLocationFolder.startsWith("jar:file:")) {
                        // handle schemas included in a JAR file
                        locationUri = schemaLocationFolder + "/" + location;
                    }
                }

                if (locationUri == null && location != null && location.startsWith("http:")) {
                    locationUri = location;
                }

                return locationUri;
            }
        };
    }
}
