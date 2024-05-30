/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd.impl.jxpath;

import java.util.Enumeration;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.JXPathContext;
import org.xml.sax.helpers.NamespaceSupport;

/** Contains utility methods to create safe {@code JXPathContext} objects. */
public class JXPathUtils {

    /**
     * Creates a {@code JXPathContext} that disables calling Java methods from XPath expressions.
     *
     * @param contextBean the root node object
     * @param lenient whether the context is in lenient mode
     * @return the context
     */
    public static JXPathContext newSafeContext(Object contextBean, boolean lenient) {
        return newSafeContext(contextBean, lenient, null, true);
    }

    /**
     * Creates a {@code JXPathContext} that disables calling Java methods from XPath expressions.
     *
     * @param contextBean the root node object
     * @param lenient whether the context is in lenient mode
     * @param ns the namespaces
     * @param declared true to include all declared prefixes; false for all active prefixes
     * @return the context
     */
    @SuppressWarnings("unchecked")
    public static JXPathContext newSafeContext(
            Object contextBean, boolean lenient, NamespaceSupport ns, boolean declared) {
        JXPathContext context = JXPathContext.newContext(contextBean);
        context.setLenient(lenient);
        if (ns != null) {
            Enumeration<String> prefixes = declared ? ns.getDeclaredPrefixes() : ns.getPrefixes();
            while (prefixes.hasMoreElements()) {
                String prefix = prefixes.nextElement();
                String uri = ns.getURI(prefix);
                context.registerNamespace(prefix, uri);
            }
        }
        // Set empty function library to prevent calling functions
        context.setFunctions(new FunctionLibrary());
        return context;
    }
}
