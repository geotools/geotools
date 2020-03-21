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
 */
package org.geotools.csw;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import javax.xml.namespace.QName;
import org.geotools.csw.bindings.SimpleLiteralBinding;
import org.geotools.xsd.Configuration;

/**
 * Parser configuration for the http://purl.org/dc/terms/ schema.
 *
 * @generated
 */
public class DCTConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public DCTConfiguration() {
        super(DCT.getInstance());

        addDependency(new DCConfiguration());
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected void registerBindings(Map bindings) {
        bindings.put(DCT.recordAbstract, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.accessRights, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.alternative, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.audience, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.available, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.bibliographicCitation, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.conformsTo, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.created, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.dateAccepted, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.dateCopyrighted, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.dateSubmitted, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.educationLevel, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.extent, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.hasFormat, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.hasPart, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.hasVersion, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isFormatOf, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isPartOf, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isReferencedBy, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isReplacedBy, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isRequiredBy, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.issued, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.isVersionOf, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.license, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.mediator, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.medium, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.modified, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.provenance, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.references, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.replaces, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.requires, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.rightsHolder, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.spatial, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.tableOfContents, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.temporal, new SimpleLiteralBinding(DC.SimpleLiteral));
        bindings.put(DCT.valid, new SimpleLiteralBinding(DC.SimpleLiteral));
    }

    /** Generates the bindings registrations for this class */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args) {
        for (Field f : DCT.class.getFields()) {
            if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0
                    && f.getType().equals(QName.class)) {
                System.out.println(
                        "bindings.put(DCT."
                                + f.getName()
                                + ", new SimpleLiteralBinding(DC.SimpleLiteral));");
            }
        }
    }
}
