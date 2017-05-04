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
package org.geotools.renderer.style;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.Icon;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.geotools.factory.Factory;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.util.Converters;
import org.geotools.util.SoftValueHashMap;
import org.geotools.xml.NullEntityResolver;
import org.geotools.xml.PreventLocalEntityResolver;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * External graphic factory accepting an Expression that can be evaluated to a URL pointing to a SVG
 * file. The <code>format</code> must be <code>image/svg+xml</code>, thought for backwards
 * compatibility <code>image/svg-xml</code> and <code>image/svg</code> are accepted as well.
 * 
 * @author Andrea Aime - TOPP
 * 
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/branches/2.6.x/modules/plugin/svg/src/main/java/org/geotools
 *         /renderer/style/SVGGraphicFactory.java $
 */
public class SVGGraphicFactory implements Factory, ExternalGraphicFactory, GraphicCache {

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("param\\((.+)\\).*");

    /** Parsed SVG glyphs cache */
    static Map<String, RenderableSVG> glyphCache = Collections.synchronizedMap(new SoftValueHashMap<String, RenderableSVG>());

    /** The possible mime types for SVG */
    static final Set<String> formats = new HashSet<String>();
    
    static {
        formats.add("image/svg");
        formats.add("image/svg-xml");
        formats.add("image/svg+xml");
    }

    /** Hints we care about */
    final private Map<Key, Object> implementationHints = new HashMap<>();
    
    private EntityResolver resolver;
    
    public SVGGraphicFactory(){
        this( null );
    }
    
    public SVGGraphicFactory(Map<Key, Object> hints){
        if( hints != null && hints.containsKey(Hints.ENTITY_RESOLVER)){
            // use entity resolver provided (even if null)
            this.resolver = (EntityResolver) hints.get(Hints.ENTITY_RESOLVER);
            this.implementationHints.put( Hints.ENTITY_RESOLVER, this.resolver );

            if( this.resolver == null ){ // use null instance rather than check each time
                this.resolver = NullEntityResolver.INSTANCE;
            }
        }
        else {
            this.resolver = GeoTools.getEntityResolver(null);
        }
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return implementationHints;
    }
    public Icon getIcon(Feature feature, Expression url, String format, int size) throws Exception {
        // check we do support the declared format
        if (format == null || !formats.contains(format.toLowerCase()))
            return null;

        // grab the url
        String svgfile = url.evaluate(feature, String.class);
        if (svgfile == null) {
            throw new IllegalArgumentException(
                    "The specified expression could not be turned into an URL");
        } else {
            // just for validation parse the URL
            if(Converters.convert(svgfile, URL.class) == null) {
                throw new IllegalArgumentException(
                        "Invalid URL: " + svgfile);
            }
        }

        // turn the svg into a document and cache results
        RenderableSVG svg = glyphCache.get(svgfile);
        if(svg == null) {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser) {
                @Override
                public InputSource resolveEntity(String publicId, String systemId)
                        throws SAXException {
                    InputSource source = super.resolveEntity(publicId, systemId);
                    if (source == null) {
                        try {
                            return resolver.resolveEntity(publicId, systemId);
                        } catch (IOException e) {
                            throw new SAXException(e);
                        }
                    }
                    return source;
                }
            };
            Document doc = f.createDocument(svgfile);
            Map<String, String> parameters = getParametersFromUrl(svgfile);
            if(!parameters.isEmpty() || hasParameters(doc.getDocumentElement())) {
                replaceParameters(doc.getDocumentElement(), parameters);
            }
            svg = new RenderableSVG(doc);
            glyphCache.put(svgfile, svg);
        }

        return new SVGIcon(svg, size);
    }

    /**
     * Splits the query string in 
     * @param url
     * @return
     */
    Map<String, String> getParametersFromUrl(String url) {
        // url.getQuery won't work on file addresses
        int idx = url.indexOf("?");
        if(idx == -1 || idx == url.length() - 1) {
            return Collections.emptyMap();
        }
        String query = url.substring(idx + 1);
        return Arrays.stream(query.split("&")).map(this::splitQueryParameter).filter(e -> e.getValue() != null)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (v1, v2) -> v2));
    }

    SimpleImmutableEntry<String, String> splitQueryParameter(String parameter) {
        final int idx = parameter.indexOf("=");
        final String key = idx > 0 ? parameter.substring(0, idx) : parameter;
        
        try {
            String value = null;
            if(idx > 0 && parameter.length() > idx + 1) {
                final String encodedValue = parameter.substring(idx + 1);
                value =  URLDecoder.decode(encodedValue, "UTF-8");
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
        for(int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            final String nv = attribute.getNodeValue();
            if(nv != null && nv.contains("param(")) {
                return true;
            }
        }
        
        // recurse
        if(root.hasChildNodes()) {
            NodeList childNodes = root.getChildNodes();
            for(int i = 0; i < childNodes.getLength(); i++) {
                Node n = childNodes.item(i);
                if (n != null && n instanceof Element) {
                    if(hasParameters((Element) n)) {
                        return true;
                    }
                }
            }
        }
        return true;
    }
    
    
    private void replaceParameters(Element root, Map<String, String> parameters) {
        if(root.hasChildNodes()) {
            NodeList childNodes = root.getChildNodes();
            for(int i = 0; i < childNodes.getLength(); i++) {
                Node n = childNodes.item(i);
                if (n != null && n instanceof Element) {
                    replaceParameters((Element) n, parameters);
                }
            }
            
        }
        
        NamedNodeMap attributes = root.getAttributes();
        for(int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            if("style".equalsIgnoreCase(attribute.getNodeName())) {
                String[] keyValues = attribute.getNodeValue().split("\\s*;\\s*");
                StringBuilder newAttribute = new StringBuilder();
                for (String keyValue : keyValues) {
                    String[] kv = keyValue.split("\\s*:\\s*");
                    if(kv.length < 2) {
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
        if(m.matches()) {
            final String key = m.group(1);
            newValue = parameters.get(key);
            
            // Batik unfortunately writes to log.err when it finds property it does not
            // recognize, that's pretty bad, so we try to fit in some valid value
            // for the "well known" cases
            if(newValue != null) {
                value  = newValue;
            } else if(key.contains("width")) {
                value = "0";
            } else if(key.contains("opacity") || key.contains("alpha")) {
                value = "1";
            } else {
                value = "#000000";
            }
        }
        return value;
    }

    private static class SVGIcon implements Icon {

        private int width;

        private int height;

        private RenderableSVG svg;

        public SVGIcon(RenderableSVG svg, int size) {
            this.svg = svg;

            // defines target width and height for render, based on the SVG bounds
            // and the specified desired height (if height is not provided, then
            // SVG bounds are used)
            Rectangle2D bounds = svg.bounds;
            double targetWidth = bounds.getWidth();
            double targetHeight = bounds.getHeight();
            if (size > 0) {
                double shapeAspectRatio = (bounds.getHeight() > 0 && bounds.getWidth() > 0) ? bounds
                        .getWidth()
                        / bounds.getHeight()
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

    private static class RenderableSVG {
        Rectangle2D bounds;

        private GraphicsNode node;

        public RenderableSVG(Document doc) {
            this.node = getGraphicNode(doc);
            this.bounds = getSvgDocBounds(doc);
            if (bounds == null)
                bounds = node.getBounds();
        }

        /**
         * Retrieves an SVG document's specified bounds.
         * 
         * @param svgLocation
         *            an URL that specifies the SVG.
         * @return a {@link Rectangle2D} with the corresponding bounds. If the SVG document does not
         *         specify any bounds, then null is returned.
         * @throws IOException
         * 
         */
        private Rectangle2D getSvgDocBounds(Document doc) {
            NodeList list = doc.getElementsByTagName("svg");
            Node svgNode = list.item(0);

            NamedNodeMap attributes = svgNode.getAttributes();
            Node widthNode = attributes.getNamedItem("width");
            Node heightNode = attributes.getNamedItem("height");

            if (widthNode != null && heightNode != null) {
                double width = parseDouble(widthNode.getNodeValue());
                double height = parseDouble(heightNode.getNodeValue());
                return new Rectangle2D.Double(0.0, 0.0, width, height);
            }

            return null;
        }
        
        private double parseDouble(String value) {
            try {
                return Double.parseDouble(value);
            }
            catch(NumberFormatException e) {
                //strip off any units
                return Double.parseDouble(value.replaceAll("\\D*$", ""));
            }
        }

        /**
         * Retrieves a Batik {@link GraphicsNode} for a given SVG.
         * 
         * @param svgLocation
         *            an URL that specifies the SVG.
         * @return the corresponding GraphicsNode.
         * @throws IOException
         * @throws URISyntaxException
         */
        private GraphicsNode getGraphicNode(Document doc) {
            // instantiates objects needed for building the node
            UserAgent userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamic(true);

            // creates node builder and builds node
            GVTBuilder builder = new GVTBuilder();
            return builder.build(ctx, doc);
        }

        public void paint(Graphics2D g, int width, int height, int x, int y) {
            // saves the old transform;
            AffineTransform oldTransform = g.getTransform();
            try {
                if (oldTransform == null)
                    oldTransform = new AffineTransform();
     
                AffineTransform transform = new AffineTransform(oldTransform);
     
                // adds scaling to the transform so that we respect the declared size
                transform.translate(x, y);
                transform.scale(width / bounds.getWidth(), height / bounds.getHeight());
                g.setTransform(transform);
                node.paint(g);
            } finally {
                g.setTransform(oldTransform);
            }

        }
    }
    
    /**
     * Forcefully drops the SVG cache 
     */
    public static void resetCache() {
        glyphCache.clear();
    }
    
    @Override
    public void clearCache() {
        resetCache();
    }

}
