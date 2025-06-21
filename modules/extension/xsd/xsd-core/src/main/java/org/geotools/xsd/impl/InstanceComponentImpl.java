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

import org.eclipse.xsd.XSDNamedComponent;
import org.geotools.xsd.InstanceComponent;

public abstract class InstanceComponentImpl implements InstanceComponent {
    /** namespace * */
    String namespace;

    /** name * */
    String name;

    /** text * */
    StringBuffer text;

    @Override
    public XSDNamedComponent getDeclaration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getText() {
        return text != null ? text.toString() : "";
    }

    @Override
    public void setText(String text) {
        this.text = text != null ? new StringBuffer(text) : new StringBuffer();
    }

    public void addText(String text) {
        if (this.text != null) {
            this.text.append(text);
        } else {
            this.text = new StringBuffer(text);
        }
    }

    public void addText(char[] ch, int start, int length) {
        if (text == null) {
            text = new StringBuffer();
        }

        text.append(ch, start, length);
    }

    /** By default indicate the elements name */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(name);
        buf.append(" ");
        buf.append(namespace);
        if (text != null) {
            buf.append("\n\t");
            buf.append(text);
        }
        return buf.toString();
    }
}
