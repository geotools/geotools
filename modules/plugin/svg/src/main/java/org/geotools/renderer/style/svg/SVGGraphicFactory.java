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
package org.geotools.renderer.style.svg;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.*;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.geotools.renderer.style.ExternalGraphicFactory;
import org.geotools.renderer.style.GraphicCache;
import org.geotools.util.CanonicalSet;
import org.geotools.util.factory.Factory;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * External graphic factory accepting an Expression that can be evaluated to a URL pointing to a SVG
 * file. The <code>format</code> must be <code>image/svg+xml</code>, thought for backwards
 * compatibility <code>image/svg-xml</code> and <code>image/svg</code> are accepted as well.
 *
 * @author Andrea Aime - TOPP
 */
public class SVGGraphicFactory implements Factory, ExternalGraphicFactory, GraphicCache {

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("param\\((.+)\\).*");

    /** Parsed SVG glyphs cache */
    RenderableSVGCache glyphCache;

    /** The possible mime types for SVG */
    static final Set<String> formats = new HashSet<String>();

    static final CanonicalSet<String> CANONICAL_PATHS = CanonicalSet.newInstance(String.class);

    static {
        formats.add("image/svg");
        formats.add("image/svg-xml");
        formats.add("image/svg+xml");
    }

    /** Hints we care about */
    private final Map<Key, Object> implementationHints = new HashMap<>();

    public SVGGraphicFactory() {
        this(null);
    }

    public SVGGraphicFactory(Map<Key, Object> hints) {
        this.glyphCache = new RenderableSVGCache(hints);
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return implementationHints;
    }

    public Icon getIcon(Feature feature, Expression url, String format, int size) throws Exception {
        // check we do support the declared format
        if (format == null || !formats.contains(format.toLowerCase())) return null;

        RenderableSVG svg = glyphCache.getRenderableSVG(feature, url, format);

        return new SVGIcon(svg, size);
    }

    protected RenderableSVG toRenderableSVG(String svgfile, URL svgUrl)
            throws SAXException, IOException {
        RenderableSVG svg;
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
        String svgUri = svgfile;
        // Remove parameters from file URLs, as it is not supported by Windows
        if ("file".equals(svgUrl.getProtocol()) && svgUrl.getQuery() != null) {
            int idx = svgfile.indexOf('?');
            if (idx > -1) {
                svgUri = svgfile.substring(0, idx);
            }
        }
        Document doc = f.createDocument(svgUri);
        Map<String, String> parameters = getParametersFromUrl(svgfile);
        if (!parameters.isEmpty() || hasParameters(doc.getDocumentElement())) {
            replaceParameters(doc.getDocumentElement(), parameters);
        }
        svg = new RenderableSVG(doc);
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

    static class SVGIcon implements Icon {

        private int width;

        private int height;

        RenderableSVG svg;

        public SVGIcon(RenderableSVG svg, int size) {
            this.svg = svg;

            // defines target width and height for render, based on the SVG bounds
            // and the specified desired height (if height is not provided, then
            // SVG bounds are used)
            Rectangle2D bounds = svg.bounds;
            double targetWidth = bounds.getWidth();
            double targetHeight = bounds.getHeight();
            if (size > 0) {
                double shapeAspectRatio =
                        (bounds.getHeight() > 0 && bounds.getWidth() > 0)
                                ? bounds.getWidth() / bounds.getHeight()
                                : 1.0;
                targetWidth = shapeAspectRatio * size;
                targetHeight = size;
            }
            this.width = (int) Math.round(targetWidth);
            this.height = (int) Math.round(targetHeight);
        }

        public int getIconHeight() {
            return height;
        }

        public int getIconWidth() {
            return width;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            svg.paint((Graphics2D) g, width, height, x, y);
        }
    }

    /** Forcefully drops the SVG cache */
    public static void resetCache() {
        RenderableSVGCache.resetCache();
    }

    @Override
    public void clearCache() {
        RenderableSVGCache.resetCache();
    }
}
