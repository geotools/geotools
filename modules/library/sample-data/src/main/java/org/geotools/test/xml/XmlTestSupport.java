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
package org.geotools.test.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.matchers.EvaluateXPathMatcher;

public abstract class XmlTestSupport {

    /**
     * Get the namespaces needed for this test class.
     *
     * @return the namespaces needed for this test class.
     */
    protected Map<String, String> getNamespaces() {
        return Collections.emptyMap();
    }

    /** Simple tuple for holding a namespace. */
    public static class Namespace {
        final String prefix;
        final String uri;

        /**
         * @param prefix the namespace prefix.
         * @param uri the namespace URI.
         */
        protected Namespace(String prefix, String uri) {
            this.prefix = prefix;
            this.uri = uri;
        }
    }

    /**
     * Construct a Namespace. Just a shortcut for {@code new Namespace(String, String)}.
     *
     * @param prefix the namespace prefix.
     * @param uri the namespace URI.
     * @return the Namespace.
     */
    public static Namespace Namespace(String prefix, String uri) {
        return new Namespace(prefix, uri);
    }

    /**
     * Construct a Map of Namespace(s).
     *
     * @param namespaces the namespaces.
     * @return a namespaces map.
     */
    public static Map<String, String> namespaces(Namespace... namespaces) {
        Map<String, String> result = new HashMap<>();
        for (Namespace namespace : namespaces) {
            result.put(namespace.prefix, namespace.uri);
        }
        return result;
    }

    /**
     * Simply a wrapper around {@link EvaluateXPathMatcher#hasXPath(String, Matcher)} that sets the namespaces here, so
     * that by omitting them from the assertions used in the tests, those assertions are more compact and hopefully more
     * readable.
     *
     * @param xPath the xpath to evaluate
     * @param valueMatcher the result of the xpath evaluation to match
     * @return an XPath Matcher
     */
    protected EvaluateXPathMatcher hasXPath(String xPath, Matcher<String> valueMatcher) {
        EvaluateXPathMatcher evaluateXPathMatcher = EvaluateXPathMatcher.hasXPath(xPath, valueMatcher);
        return evaluateXPathMatcher.withNamespaceContext(getNamespaces());
    }

    protected Diff diffSimilar(final Object expected, final Object actual) {
        return DiffBuilder.compare(expected)
                .withTest(actual)
                .checkForSimilar()
                .withNamespaceContext(getNamespaces())
                .build();
    }

    /**
     * Assert throwable message contains expected string.
     *
     * <p>Intended to search for fragments when testing the contents of Locale or platform specific error messages.
     *
     * @param throwable Throwable
     * @param expected Expected string
     */
    public static void assertMessageContains(Throwable throwable, String... expected) {
        String message = (throwable != null && throwable.getMessage() != null) ? throwable.getMessage() : "";
        for (String fragment : expected) {
            if (!message.contains(fragment)) {
                Assert.fail("Expected " + String.join(",", expected) + ": " + message);
            }
        }
    }
}
