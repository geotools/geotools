/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

import java.util.regex.Pattern;

/**
 * Represents text encountered in the parse tree.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-core/src/main/java/org/geotools/xml/Text.java $
 */
public class Text {

    static Pattern WHITESPACE = Pattern.compile("\\s+", Pattern.MULTILINE);
    static Pattern LEADING = Pattern.compile("^\\s+");
    static Pattern TRAILING = Pattern.compile("\\s+$");
    static Pattern INNER = Pattern.compile("\\s{2,}");
    
    String value;

    public Text() {
        this(null);
    }

    public Text(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public boolean isWhitespace() {
        return WHITESPACE.matcher(value).matches();
    }
    
    public void trimLeading() {
        value = LEADING.matcher(value).replaceAll("");
    }
    
    public void trimTrailing() {
        value = TRAILING.matcher(value).replaceAll("");
    }
    
    public void trimInner() {
        value = INNER.matcher(value).replaceAll(" ");
    }
    
    @Override
    public String toString() {
        return value;
    }
}
