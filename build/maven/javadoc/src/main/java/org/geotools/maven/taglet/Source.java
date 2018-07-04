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

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The <code>@source</code> tag. This tag expects an URL to the source in the SVN repository. The
 * SVN URL keyword is ignored.
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
    public static void register(final Map<String, Taglet> tagletMap) {
        final Source tag = new Source();
        tagletMap.put(tag.getName(), tag);
    }
    /** The delimiter for SVN keywords. */
    static final char SVN_KEYWORD_DELIMITER = '$';
    /** The base URL for Maven repository. */
    public static final String SVN_REPO_URL = "http://svn.osgeo.org/geotools/";
    /** The pattern to use for fetching the URL. The bit we want is in capture group 2 */
    final Pattern findURL =
            Pattern.compile(
                    "\\s*(\\"
                            + SVN_KEYWORD_DELIMITER
                            + "URL\\s*\\:)?\\s*(.+?)\\s*"
                            + "(\\"
                            + SVN_KEYWORD_DELIMITER
                            + "\\s*)?");

    static final int URL_CAPTURE_GROUP = 2;
    /** The pattern to use for fetching the module name from an URL. */
    final Pattern findModule;

    /** Constructs a default <code>@source</code> taglet. */
    Source() {
        super();
        findModule =
                Pattern.compile(
                        "https?\\Q://\\E"
                                + // http or https
                                "[a-zA-Z\\.\\-]+"
                                + // host e.g. svn.osgeo.org
                                "\\/geotools"
                                + // /geotools
                                "\\/[a-z]+"
                                + // trunk or tags or branches
                                "(\\/[a-zA-Z0-9\\-\\_\\.]+)?"
                                + // group 1: tag or branch name or null if trunk
                                "\\/(((modules)\\/(library|plugin|extension|unsupported))|demo)"
                                + // groups 2 - 5
                                "\\/(.+)"
                                + // group 6: module name
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
     * Returns {@code true} since <code>@source</code> can be used in type documentation (classes or
     * interfaces).
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
     * Given the <code>Tag</code> representation of this custom tag, return its string
     * representation. The default implementation invokes the array variant of this method.
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
        // there should be a single tag
        if (tags == null || tags.length != 1) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("\n<DT><B>Module:</B></DT>");

        String tagText = tags[0].text();
        final Matcher matchURL = findURL.matcher(tagText);
        if (!matchURL.matches()) {
            return ""; // continue;
        }
        final String url = matchURL.group(URL_CAPTURE_GROUP).trim();
        final Matcher matchModule = findModule.matcher(url);
        if (!matchModule.matches()) {
            return ""; // continue;
        }

        final String modulePath = matchModule.group(6);
        int pos = modulePath.indexOf('/');
        final String module;
        if (pos == -1) {
            module = modulePath;
        } else {
            module = modulePath.substring(pos + 1);
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
        sb.append("\n<DD><CODE><B>");
        sb.append(group).append('/');
        if (category != null) {
            sb.append(category).append('/');
        }
        sb.append(module);

        /*
         * Jar name in brackets e.g. (gt-main.jar)
         */
        sb.append(" (gt-").append(module).append(".jar)");

        sb.append("</B></CODE>");

        sb.append("</DD>\n");
        sb.append("\n<DT><B>Source repository:</B></DT>").append('\n');
        sb.append("<DD>   <CODE>").append(url).append("</CODE></DD>\n");

        return sb.toString();
    }
}
