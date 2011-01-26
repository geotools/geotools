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
package org.geotools.xml.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.util.Converters;
import org.geotools.xml.Binding;
import org.geotools.xml.ComplexBinding;
import org.geotools.xml.SimpleBinding;
import org.opengis.feature.ComplexAttribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


public class ElementEncodeExecutor implements BindingWalker.Visitor {
    /** the object being encoded **/
    Object object;

    /** the element being encoded **/
    XSDElementDeclaration element;

    /** the encoded value **/
    Element encoding;

    /** the document / factory **/
    Document document;

    /** logger */
    Logger logger;

    public ElementEncodeExecutor(Object object, XSDElementDeclaration element, Document document,
        Logger logger) {
        this.object = object;
        this.element = element;
        this.document = document;
        this.logger = logger;

        //		if ( element.getTargetNamespace() != null ) {
        encoding = document.createElementNS(element.getTargetNamespace(), element.getName());

        //		}
        //		else {
        //			encoding = document.createElementNS( 
        //				element.getSchema().getTargetNamespace(), element.getName() 
        //			);
        //		}
    }

    public Element getEncodedElement() {
        return encoding;
    }

    public void visit(Binding binding) {
        if( object == null ) {
            throw new RuntimeException( "Unable to encode " + element.getName() + ", value is null.");
        }
        
        //ensure that the type of the object being encoded matches the type 
        // of the binding
        if (binding.getType() == null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Binding: " + binding.getTarget() + " does not declare a target type");
            }

            return;
        }

        /*
         * Notes on the nasty ComplexAttribute instanceof check, based on discussion in GEOT-2474.
         * 
         * ElementEncodeExecutor does too much: it calls the encode method of a binding, but also
         * provides automatic conversion services for simple features. This causes encoding of any
         * complexType with simpleContent of xs:string to fail if the input data is a GeoAPI object.
         * ElementEncodeExecutor was written to support encoding of, for example, CodeType from a
         * String. However, CodeType is most fully represented by a ComplexAttribute (it can have a
         * codeSpace attribute). The instanceof check retains the old behaviour so
         * ElementEncodeExecutor can still automagically convert simple features into XML. The
         * instanceof is necessary because ElementEncodeExecutor will otherwise use
         * Converters.convert to turn any ComplexAttribute bound to a String into a String. Because
         * every Java object has a toString method, this will always "succeed", and garbage will be
         * encoded. This breaks XML well-formedness, not to mention being generally useless.
         */
        if (!(object instanceof ComplexAttribute)
                && !binding.getType().isAssignableFrom(object.getClass())) {
            //try to convert 
            Object converted = Converters.convert(object, binding.getType());

            if (converted != null) {
                object = converted;
            } else {
                if (logger.isLoggable(Level.FINE)) {
                    // do not log the object, may be a multi-megabyte feature collection
                    // that can trigger an OOM toStringing itself
                    logger.fine("[ " + object.getClass() + " ] is not of type " + binding.getType());
                }

                return;
            }
        }

        if (binding instanceof ComplexBinding) {
            ComplexBinding complex = (ComplexBinding) binding;

            try {
                Element element = complex.encode(object, document, encoding);

                if (element != null) {
                    encoding = element;
                }
            } catch (Throwable t) {
                String msg = "Encode failed for " + element.getName() + ". Cause: "
                    + t.getLocalizedMessage();
                throw new RuntimeException(msg, t);
            }
        } else {
            SimpleBinding simple = (SimpleBinding) binding;

            //figure out if the node has any text
            Text text = null;

            for (int i = 0; i < encoding.getChildNodes().getLength(); i++) {
                Node node = (Node) encoding.getChildNodes().item(i);

                if (node instanceof Text) {
                    text = (Text) node;

                    break;
                }
            }

            try {
                String value = simple.encode(object, (text != null) ? text.getData() : null);

                if (value != null) {
                    //set the text of the node
                    if (text == null) {
                        text = document.createTextNode(value);
                        encoding.appendChild(text);
                    } else {
                        text.setData(value);
                    }
                }
            } catch (Throwable t) {
                String msg = "Encode failed for " + element.getName() + ". Cause: "
                    + t.getLocalizedMessage();
                throw new RuntimeException(msg, t);
            }
        }
    }
}
