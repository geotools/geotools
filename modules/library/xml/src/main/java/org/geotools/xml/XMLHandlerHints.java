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
package org.geotools.xml;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.geotools.util.NullEntityResolver;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.xml.sax.EntityResolver;

/**
 * Hint object with known parameters for XML parsing.
 *
 * @author Jesse
 */
public class XMLHandlerHints implements Map<String, Object> {

    /**
     * Declares the schemas to use for parsing. Value must be a java.util.Map of <String,URI>
     * objects where String is the Namespace and URI is the URL to use to load the schema.
     */
    public static final String NAMESPACE_MAPPING = "NAMESPACE_MAPPING";
    /** Declares a FlowHandler for the parser to use */
    public static final String FLOW_HANDLER_HINT = "FLOW_HANDLER_HINT";
    /** Tells the parser to "Stream" */
    public static final String STREAM_HINT = "org.geotools.xml.gml.STREAM_HINT";
    /** Sets the level of compliance that the filter encoder should use */
    public static final String FILTER_COMPLIANCE_STRICTNESS =
            "org.geotools.xml.filter.FILTER_COMPLIANCE_STRICTNESS";
    /** Supplied {@link EntityResolver} for Schema and/or DTD validation */
    public static final String ENTITY_RESOLVER = GeoTools.ENTITY_RESOLVER;
    /** Supplied {@link SaxParserFactory} */
    public static final String SAX_PARSER_FACTORY = "javax.xml.parsers.SAXParserFactory";

    /** The value so that the parser will encode all Geotools filters with no modifications. */
    public static final Integer VALUE_FILTER_COMPLIANCE_LOW = Integer.valueOf(0);
    /**
     * The value so the parser will be slightly more compliant to the Filter 1.0.0 spec. It will
     * encode:
     *
     * <pre><code>
     *  BBoxFilter
     *  	or
     *  FidFilter
     *  </code></pre>
     *
     * as
     *
     * <pre><code>
     *  &lt;Filter&gt;&lt;BBo&gt;...&lt;/BBox&gt;&lt;FidFilter fid="fid"/&gt;&lt;/Filter&gt;
     *  </code></pre>
     *
     * It will encode:
     *
     * <pre><code>
     *  BBoxFilter
     *  	and
     *  FidFilter
     *  </code></pre>
     *
     * as
     *
     * <pre><code>
     *  &lt;Filter&gt;&lt;FidFilter fid="fid"/&gt;&lt;/Filter&gt;
     *  </code></pre>
     *
     * <p><b>IMPORTANT:</b> If this compliance level is used and a non-strict FilterFactory is used
     * to create the filter then the original filter must be ran on the retrieved feature because
     * this hint will sometimes cause more features to be returned than is requested. Consider the
     * following filter:
     *
     * <p>not(fidFilter).
     *
     * <p>this will return all features and so the filtering must be done on the client.
     */
    public static final Integer VALUE_FILTER_COMPLIANCE_MEDIUM = Integer.valueOf(1);

    /**
     * The value so the parser will be compliant with the Filter 1.0.0 spec.
     *
     * <p>It will throw an exception with filters like: BBoxFilter or FidFilter
     *
     * <p>It will encode:
     *
     * <pre><code>
     *  BBoxFilter
     *  	and
     *  FidFilter
     *  </code></pre>
     *
     * as
     *
     * <pre><code>
     *  &lt;Filter&gt;&lt;FidFilter fid="fid"/&gt;&lt;/Filter&gt;
     *  </code></pre>
     *
     * <p><b>IMPORTANT:</b> If this compliance level is used and a non-strict FilterFactory is used
     * to create the filter then the original filter must be ran on the retrieved feature because
     * this hint will sometimes cause more features to be returned than is requested. Consider the
     * following filter:
     *
     * <p>not(fidFilter). this will return all features and so the filtering must be done on the
     * client.
     */
    public static final Integer VALUE_FILTER_COMPLIANCE_HIGH = Integer.valueOf(2);

    private Map<String, Object> map = new HashMap<String, Object>();

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    public boolean equals(Object o) {
        return map.equals(o);
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public int hashCode() {
        return map.hashCode();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends Object> other) {
        map.putAll(other);
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public Collection<Object> values() {
        return map.values();
    }

    /**
     * Looks up {@link #ENTITY_RESOLVER} instance in provided hints, defaulting to setting provided
     * by {@link GeoTools#getEntityResolver(org.geotools.util.factory.Hints)} (usually {@link
     * PreventLocalEntityResolver} unless otherwise configured).
     *
     * @return EntityResolver provided by hints, or non-null default provided by {@link
     *     Hints#ENTITY_RESOLVER}.
     */
    public static EntityResolver toEntityResolver(Map<String, Object> hints) {
        if (hints != null && hints.containsKey(GeoTools.ENTITY_RESOLVER)) {
            Object resolver = hints.get(GeoTools.ENTITY_RESOLVER);
            if (resolver == null) { // use null instance rather than check each time
                return NullEntityResolver.INSTANCE;
            } else if (resolver instanceof EntityResolver) {
                return (EntityResolver) resolver;
            }
        }
        return GeoTools.getEntityResolver(null);
    }
}
