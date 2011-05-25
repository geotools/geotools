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
package org.geotools.xml;

import org.eclipse.xsd.XSDElementDeclaration;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.List;
import javax.xml.namespace.QName;


/**
 * Base class for complex bindings.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 * @source $URL$
 */
public abstract class AbstractComplexBinding implements ComplexBinding {
    /**
     * Does nothing, subclasses should override this method.
     */
    public void initializeChildContext(ElementInstance childInstance, Node node,
        MutablePicoContainer context) {
        //does nothing, subclasses should override
    }

    /**
     * This implementation returns {@link Binding#OVERRIDE}.
     * <p>
     * Subclasses should override to change this behaviour.
     * </p>
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * Subclasses should override this method, the default implementation
     * return <code>null</code>.
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return null;
    }

    /**
     * Subclasses should ovverride this method if need be, the default implementation
     * returns <param>value</param>.
     *
     * @see ComplexBinding#encode(Object, Document, Element).
     */
    public Element encode(Object object, Document document, Element value)
        throws Exception {
        return value;
    }

    /**
     * Subclasses should override this method if need be, the default implementation
     * returns <code>null</code>.
     *
     * @see ComplexBinding#getProperty(Object, QName)
     */
    public Object getProperty(Object object, QName name)
        throws Exception {
        //do nothing, subclasses should override
        return null;
    }

    /**
     * Subclasses should override this method if need be, the default implementation
     * returns <code>null</code>.
     * <p>
     * Note that this method only needs to be implemented for schema types which
     * are open-ended in which the contents are not specifically specified by
     * the schema.
     * </p>
     *
     * @see ComplexBinding#getProperties(Object)
     * @deprecated use {@link #getProperties(Object, XSDElementDeclaration)}
     */
    public List getProperties(Object object) throws Exception {
        // do nothing, subclasses should override.
        return null;
    }
    
    /**
     * Subclasses should override this method if need be, the default implementation
     * returns <code>null</code>.
     * <p>
     * Note that this method only needs to be implemented for schema types which
     * are open-ended in which the contents are not specifically specified by
     * the schema.
     * </p>
     *
     * @see ComplexBinding#getProperties(Object)
     */
    public List getProperties(Object object, XSDElementDeclaration element)
            throws Exception {
        // do nothing, subclasses should override
        return null;
    }
}
