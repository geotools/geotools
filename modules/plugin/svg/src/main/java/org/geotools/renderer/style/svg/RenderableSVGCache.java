/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style.svg;

import java.awt.RenderingHints.Key;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.geotools.util.Converters;
import org.geotools.util.SoftValueHashMap;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Cache for RenderableSVG instances */
public class RenderableSVGCache {

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("param\\((.+)\\).*");

    /** Parsed SVG glyphs cache */
    static Map<String, RenderableSVG> glyphCache =
            Collections.synchronizedMap(new SoftValueHashMap<>());

    /** The possible mime types for SVG */
    static final Set<String> formats = new HashSet<String>();

    static {
        formats.add("image/svg");
        formats.add("image/svg-xml");
        formats.add("image/svg+xml");
    }

    public RenderableSVGCache() {
        this(null);
    }

    public RenderableSVGCache(Map<Key, Object> hints) {}

    public RenderableSVG getRenderableSVG(Feature feature, Expression url, String format)
            throws Exception {
        // check we do support the declared format
        if (format == null || !formats.contains(format.toLowerCase())) return null;

        // grab the url
        String svgfile = url.evaluate(feature, String.class);
        if (svgfile == null) {
            throw new IllegalArgumentException(
                    "The specified expression could not be turned into an URL");
        } else {
            // just for validation parse the URL
            if (Converters.convert(svgfile, URL.class) == null) {
                throw new IllegalArgumentException("Invalid URL: " + svgfile);
            }
        }

        // turn the svg into a document and cache results
        RenderableSVG svg = glyphCache.get(svgfile);
        if (svg == null) {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            Document doc;
            int queryIdx = svgfile.indexOf("?");
            if (svgfile.startsWith("file:/") && queryIdx > 0) {
                String localPath = svgfile.substring(0, queryIdx);
                doc = f.createDocument(localPath);
            } else {
                doc = f.createDocument(svgfile);
            }
            Map<String, String> parameters = getParametersFromUrl(svgfile);
            if (!parameters.isEmpty() || hasParameters(doc.getDocumentElement())) {
                replaceParameters(doc.getDocumentElement(), parameters);
            }
            svg = new RenderableSVG(doc);
            glyphCache.put(svgfile, svg);
        }

        return svg;
    }

    /** Splits the query string in */
    Map<String, String> getParametersFromUrl(String url) {
        // url.getQuery won't work on file addresses
        int idx = url.indexOf("?");
        if (idx == -1 || idx == url.length() - 1) {
            return Collections.emptyMap();
        }
        String query = url.substring(idx + 1);
        return Arrays.stream(query.split("&"))
                .map(this::splitQueryParameter)
                .filter(e -> e.getValue() != null)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (v1, v2) -> v2));
    }

    SimpleImmutableEntry<String, String> splitQueryParameter(String parameter) {
        final int idx = parameter.indexOf("=");
        final String key = idx > 0 ? parameter.substring(0, idx) : parameter;

        try {
            String value = null;
            if (idx > 0 && parameter.length() > idx + 1) {
                final String encodedValue = parameter.substring(idx + 1);
                value = URLDecoder.decode(encodedValue, "UTF-8");
            }
            return new SimpleImmutableEntry<>(key, value);
        } catch (UnsupportedEncodingException e) {
            // UTF-8 not supported??
            throw new RuntimeException(e);
        }
    }

    private boolean hasParameters(Element root) {
        // check if any attribute is parametric
        NamedNodeMap attributes = root.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            final String nv = attribute.getNodeValue();
            if (nv != null && nv.contains("param(")) {
                return true;
            }
        }

        // recurse
        if (root.hasChildNodes()) {
            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node n = childNodes.item(i);
                if (n != null && n instanceof Element) {
                    if (hasParameters((Element) n)) {
                        return true;
                    }
                }
            }
        }
        return true;
    }

    private void replaceParameters(Element root, Map<String, String> parameters) {
        if (root.hasChildNodes()) {
            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node n = childNodes.item(i);
                if (n != null && n instanceof Element) {
                    replaceParameters((Element) n, parameters);
                }
            }
        }

        NamedNodeMap attributes = root.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            if ("style".equalsIgnoreCase(attribute.getNodeName())) {
                String[] keyValues = attribute.getNodeValue().split("\\s*;\\s*");
                StringBuilder newAttribute = new StringBuilder();
                for (String keyValue : keyValues) {
                    String[] kv = keyValue.split("\\s*:\\s*");
                    if (kv.length < 2) {
                        continue;
                    }
                    String key = kv[0];
                    String value = replaceValue(kv[1], parameters);
                    newAttribute.append(key).append(":").append(value).append(";");
                }
                attribute.setNodeValue(newAttribute.toString());
            } else {
                String value = replaceValue(attribute.getNodeValue(), parameters);
                attribute.setNodeValue(value);
            }
        }
    }

    private String replaceValue(String value, Map<String, String> parameters) {
        Matcher m = PARAMETER_PATTERN.matcher(value);
        String newValue;
        if (m.matches()) {
            final String key = m.group(1);
            newValue = parameters.get(key);

            // Batik unfortunately writes to log.err when it finds property it does not
            // recognize, that's pretty bad, so we try to fit in some valid value
            // for the "well known" cases
            if (newValue != null) {
                value = newValue;
            } else if (key.contains("width")) {
                value = "0";
            } else if (key.contains("opacity") || key.contains("alpha")) {
                value = "1";
            } else {
                value = "#000000";
            }
        }
        return value;
    }

    /** Forcefully drops the SVG cache */
    public static void resetCache() {
        glyphCache.clear();
    }
}
