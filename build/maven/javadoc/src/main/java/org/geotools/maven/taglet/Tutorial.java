/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.maven.taglet;

import java.util.Map;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;


/**
 * The <code>@tutorial</code> tag. This tag expects a link toward a tutorial page on the
 * Geotools wiki pages (Confluence).
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class Tutorial implements Taglet {
    /**
     * Register this taglet.
     *
     * @param tagletMap the map to register this tag to.
     */
    public static void register(final Map<String,Taglet> tagletMap) {
       final Tutorial tag = new Tutorial();
       tagletMap.put(tag.getName(), tag);
    }

    /**
     * Constructs a default <code>@tutorial</code> taglet.
     */
    private Tutorial() {
        super();
    }

    /**
     * Returns the name of this custom tag.
     *
     * @return The tag name.
     */
    public String getName() {
        return "tutorial";
    }

    /**
     * Returns {@code true} since <code>@tutorial</code> can be used in overview.
     *
     * @return Always {@code true}.
     */
    public boolean inOverview() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@tutorial</code> can be used in package documentation.
     *
     * @return Always {@code true}.
     */
    public boolean inPackage() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@tutorial</code> can be used in type documentation
     * (classes or interfaces).
     *
     * @return Always {@code true}.
     */
    public boolean inType() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@tutorial</code> can be used in constructor
     *
     * @return Always {@code true}.
     */
    public boolean inConstructor() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@tutorial</code> can be used in method documentation.
     *
     * @return Always {@code true}.
     */
    public boolean inMethod() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@tutorial</code> can be used in field documentation.
     *
     * @return Always {@code true}.
     */
    public boolean inField() {
        return true;
    }

    /**
     * Returns {@code false} since <code>@tutorial</code> is not an inline tag.
     *
     * @return Always {@code false}.
     */
    public boolean isInlineTag() {
        return false;
    }

    /**
     * Given the <code>Tag</code> representation of this custom tag, return its string representation.
     * The default implementation invokes the array variant of this method.
     *
     * @param tag The tag to format.
     * @return A string representation of the given tag.
     */
    public String toString(final Tag tag) {
        return toString(new Tag[] {tag});
    }

    /**
     * Given an array of {@code Tag}s representing this custom tag, return its string
     * representation.
     *
     * @param tags The tags to format.
     * @return A string representation of the given tags.
     */
    public String toString(final Tag[] tags) {
        if (tags==null || tags.length==0) {
            return "";
        }
        final StringBuilder buffer = new StringBuilder("\n<DT><B>Tutorial:</B></DT>");
        for (int i=0; i<tags.length; i++) {
            final String url   = tags[i].text().trim();
            final String title = url.substring(url.lastIndexOf('/')+1).replace('+',' ');
            buffer.append('\n').append(i==0 ? "<DD>" : "    ")
                .append("<A HREF=\"").append(url).append("\">").append(title).append("</A>");
        }
        return buffer.append("</DD>\n").toString();
    }
}
