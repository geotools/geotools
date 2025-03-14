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
package org.geotools.xsd;

import java.util.List;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Base class for complex bindings.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public abstract class AbstractComplexBinding implements ComplexBinding {
    /** Does nothing, subclasses should override this method. */
    @Override
    public void initializeChildContext(ElementInstance childInstance, Node node, MutablePicoContainer context) {
        // does nothing, subclasses should override
    }

    /** Does nothing, subclasses should override this method. */
    @Override
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
        // does nothing, subclasses should override
    }

    /**
     * This implementation returns {@link Binding#OVERRIDE}.
     *
     * <p>Subclasses should override to change this behaviour.
     */
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /** Subclasses should override this method, the default implementation return <code>null</code>. */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return null;
    }

    /**
     * Subclasses should override this method if need be, the default implementation returns <param>value</param>.
     *
     * @see ComplexBinding#encode(Object, Document, Element).
     */
    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        return value;
    }

    /**
     * Subclasses should override this method if need be, the default implementation returns <code>
     * null</code>.
     *
     * @see ComplexBinding#getProperty(Object, QName)
     */
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        // do nothing, subclasses should override
        return null;
    }

    /**
     * Subclasses should override this method if need be, the default implementation returns <code>
     * null</code>.
     *
     * <p>Note that this method only needs to be implemented for schema types which are open-ended in which the contents
     * are not specifically specified by the schema.
     */
    @Override
    public List<Object[]> getProperties(Object object, XSDElementDeclaration element) throws Exception {
        // do nothing, subclasses should override
        return null;
    }
}
