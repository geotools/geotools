/*
 *    GeoTools - The Open Source Java GIS Toolkit
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
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;


/**
 * The <code>@source</code> tag. This tag expects an URL to the source in the SVN repository.
 * The SVN URL keyword is ignored.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class Source implements Taglet {
    /**
     * Register this taglet.
     *
     * @param tagletMap the map to register this tag to.
     */
    public static void register(final Map<String,Taglet> tagletMap) {
       final Source tag = new Source();
       tagletMap.put(tag.getName(), tag);
    }

    /**
     * The delimiter for SVN keywords.
     */
    static final char SVN_KEYWORD_DELIMITER = '$';

    /**
     * The base URL for Maven repository.
     */
    public static final String SVN_REPO_URL = "http://svn.osgeo.org/geotools/";

    /**
     * The pattern to use for fetching the URL. The bit we want is in capture group 2
     */
    final Pattern findURL = Pattern.compile(
            "\\s*(\\" + SVN_KEYWORD_DELIMITER + "URL\\s*\\:)?\\s*(.+)\\s*"
            + "(\\" + SVN_KEYWORD_DELIMITER + "\\s*)?");

    static final int URL_CAPTURE_GROUP = 2;

    /**
     * The pattern to use for fetching the module name from an URL.
     */
    final Pattern findModule;

    /**
     * Constructs a default <code>@source</code> taglet.
     */
    Source() {
        super();
        findModule = Pattern.compile(
                "https?\\Q://\\E" +   // http or https
                "[a-zA-Z\\.\\-]+" + // host e.g. svn.osgeo.org
                "\\/geotools" + // /geotools
                "\\/[a-z]+" + // trunk or tags or branches
                "(\\/[a-zA-Z0-9\\-\\_\\.]+)?" +  // group 1: tag or branch name or null if trunk
                "\\/(((modules)\\/(library|plugin|extension|unsupported))|demo)" + // groups 2 - 5
                "\\/(.+)" +  // group 6: module name
                "\\/src.*");
    }

    /**
     * Returns the name of this custom tag.
     *
     * @return The tag name.
     */
    public String getName() {
        return "source";
    }

    /**
     * Returns {@code true} since <code>@source</code> can be used in overview.
     *
     * @return Always {@code true}.
     */
    public boolean inOverview() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@source</code> can be used in package documentation.
     *
     * @return Always {@code true}.
     */
    public boolean inPackage() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@source</code> can be used in type documentation
     * (classes or interfaces).
     *
     * @return Always {@code true}.
     */
    public boolean inType() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@source</code> can be used in constructor
     *
     * @return Always {@code true}.
     */
    public boolean inConstructor() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@source</code> can be used in method documentation.
     *
     * @return Always {@code true}.
     */
    public boolean inMethod() {
        return true;
    }

    /**
     * Returns {@code true} since <code>@source</code> can be used in field documentation.
     *
     * @return Always {@code true}.
     */
    public boolean inField() {
        return true;
    }

    /**
     * Returns {@code false} since <code>@source</code> is not an inline tag.
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
        final StringBuilder buffer = new StringBuilder("\n<DT><B>Module:</B></DT>");
        for (int i=0; i<tags.length; i++) {
            final Matcher matchURL = findURL.matcher(tags[i].text());
            if (!matchURL.matches()) {
                continue;
            }
            final String url = matchURL.group(URL_CAPTURE_GROUP).trim();
            final Matcher matchModule = findModule.matcher(url);
            if (!matchModule.matches()) {
                continue;
            }

            final String modulePath = matchModule.group(6);
            int pos = modulePath.indexOf('/');
            final String module;
            if (pos == -1) {
                module = modulePath;
            } else {
                module = modulePath.substring(pos+1);
            }

            final String group, category;
            if (matchModule.group(2).equals("demo")) {
                group = matchModule.group(2);
                category = null;

            } else {
                group = matchModule.group(4);
                category = matchModule.group(5);
            }

            /*
             * Module path e.g. modules/library/main
             */
            buffer.append('\n').append(i == 0 ? "<DD>" : "    ").append("<CODE><B>");

            buffer.append(group).append('/');
            if (category != null) {
                buffer.append(category).append('/');
            }
            buffer.append(module);

            /*
             * Jar name in brackets e.g. (gt-main.jar)
             */
            buffer.append(" (gt-").append(module).append(".jar)");
            
            buffer.append("</B></CODE>");
        }

        return buffer.append("</DD>\n").toString();
    }
}
