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

import org.eclipse.xsd.XSDSchemaContent;
import org.picocontainer.MutablePicoContainer;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.Node;


/**
 * Class implementing this interface serve has handlers for content of an
 * instance document as it is parsed.
 *
 * <p>
 * A handler is repsonsible for parsing and validating content. Upon a
 * successful parse and validation, the handler must return the "parsed"
 * content from a call to {@link #getValue}.
 * </p>
 *
 * <p>
 * A handler corresponds to a specific component in a schema. Processing is
 * delegated to the handler when an instance of the component is encountered in
 * an instance document.
 * </p>
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 *
 *
 * @source $URL$
 */
public interface Handler {
    /**
     * @return The entity of the schema that corresponds to the handler.
     */
    XSDSchemaContent getSchemaContent();

    /**
     * @return The instance of the schema content that is currently being
     * handled.
     */
    InstanceComponent getComponent();

    /**
     * @return The parse tree for the handler.
     */
    Node getParseNode();

    /**
     * @return A value which corresponds to an instance of the entity of the
     * handler.
     */

    //Object getValue();

    /**
     * @return The context or container in which the instance is to be parsed in.
     *
     */
    MutablePicoContainer getContext();

    /**
     * @param context The context in which the the instance is to be parsed in.
     */
    void setContext(MutablePicoContainer context);

    /**
     * @return The parent handler.
     *
     * @see Handler#getChildHandler(QName, SchemaBuilder)
     */
    Handler getParentHandler();

    /**
     * Returns a handler for a component in the schema which is a child of
     * this component.
     * <p>
     * This method will return null in two situations:
     * <ol>
     *         <li>The schema component being handled does not support children (for
     * example, an attribute).</li>
     *         <li>A child with the specified qName could not be found.
     * </ol>
     *
     * @param qName The qualified name of the schema component.
     *
     * @return A new handler, or null if one cannot be created.
     */
    Handler createChildHandler(QName qName);

    /**
     * @return The current list of child handlers executing.
     */

    //List getChildHandlers();

    /**
     * Called when a child handler is started, on the leading edge of the
     * child element.
     *
     * @param child The executing child handler.
     */
    void startChildHandler(Handler child);

    /**
     * Called when a child handler is finished, on the trailing edge of the
     * child element.
     *
     * @param child The executing child handler.
     */
    void endChildHandler(Handler child);
}
