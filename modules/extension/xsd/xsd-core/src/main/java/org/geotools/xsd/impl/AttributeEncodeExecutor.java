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
package org.geotools.xsd.impl;

import java.util.logging.Logger;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.geotools.util.Converters;
import org.geotools.xsd.Binding;
import org.geotools.xsd.SimpleBinding;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;

public class AttributeEncodeExecutor implements BindingWalker.Visitor {
    /** the object being encoded * */
    Object object;

    /** the attribute being encoded * */
    XSDAttributeDeclaration attribute;

    /** the encoded value * */
    Attr encoding;

    /** the document / factory * */
    Document document;

    /** logger */
    Logger logger;

    public AttributeEncodeExecutor(
            Object object, XSDAttributeDeclaration attribute, Document document, Logger logger) {
        this.object = object;
        this.attribute = attribute;
        this.document = document;
        this.logger = logger;

        encoding = document.createAttributeNS(attribute.getTargetNamespace(), attribute.getName());
    }

    public Attr getEncodedAttribute() {
        return encoding;
    }

    public void visit(Binding binding) {
        // ensure the object type matches the type declared on the bindign
        if (binding.getType() == null) {
            logger.fine("Binding: " + binding.getTarget() + " does not declare a target type");

            return;
        }

        if (!binding.getType().isAssignableFrom(object.getClass())) {
            // try to convert
            Object converted = Converters.convert(object, binding.getType());

            if (converted != null) {
                object = converted;
            } else {
                logger.fine(
                        object
                                + "[ "
                                + object.getClass()
                                + " ] is not of type "
                                + binding.getType());

                return;
            }
        }

        if (binding instanceof SimpleBinding) {
            SimpleBinding simple = (SimpleBinding) binding;

            try {
                encoding.setValue(simple.encode(object, encoding.getValue()));
            } catch (Throwable t) {
                String msg =
                        "Encode failed for "
                                + attribute.getName()
                                + ". Cause: "
                                + t.getLocalizedMessage();
                throw new RuntimeException(msg, t);
            }
        }
    }
}
