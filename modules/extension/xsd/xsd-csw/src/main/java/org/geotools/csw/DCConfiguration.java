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
import net.opengis.cat.csw20.Csw20Factory;
import org.geotools.csw.bindings.SimpleLiteralBinding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SimpleContentComplexEMFBinding;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://purl.org/dc/elements/1.1/ schema.
 *
 * @generated
 */
public class DCConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public DCConfiguration() {
        super(DC.getInstance());

        // TODO: add dependencies here
    }

    /** Registers the bindings for the configuration. */
    protected void registerBindings(Map bindings) {
        bindings.put(
                DC.elementContainer,
                new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.elementContainer));
        bindings.put(
                DC.SimpleLiteral,
                new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DC.contributor, new SimpleLiteralBinding(DC.contributor));
        bindings.put(DC.coverage, new SimpleLiteralBinding(DC.coverage));
        bindings.put(DC.creator, new SimpleLiteralBinding(DC.creator));
        bindings.put(DC.date, new SimpleLiteralBinding(DC.date));
        bindings.put(
                DC.DCelement,
                new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.DCelement));
        bindings.put(DC.description, new SimpleLiteralBinding(DC.description));
        bindings.put(DC.format, new SimpleLiteralBinding(DC.format));
        bindings.put(DC.identifier, new SimpleLiteralBinding(DC.identifier));
        bindings.put(DC.language, new SimpleLiteralBinding(DC.language));
        bindings.put(DC.publisher, new SimpleLiteralBinding(DC.publisher));
        bindings.put(DC.relation, new SimpleLiteralBinding(DC.relation));
        bindings.put(DC.rights, new SimpleLiteralBinding(DC.rights));
        bindings.put(DC.source, new SimpleLiteralBinding(DC.source));
        bindings.put(DC.subject, new SimpleLiteralBinding(DC.subject));
        bindings.put(DC.title, new SimpleLiteralBinding(DC.title));
        bindings.put(DC.type, new SimpleLiteralBinding(DC.type));
    }

    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Csw20Factory.eINSTANCE);
    }

    /** Generates the bindings registrations for this class */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args) {
        for (Field f : DC.class.getFields()) {
            if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0
                    && f.getType().equals(QName.class)) {
                System.out.println(
                        "bindings.put(DC."
                                + f.getName()
                                + ", new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC."
                                + f.getName()
                                + "));");
            }
        }
    }
}
