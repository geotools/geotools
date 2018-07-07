/* (c) 2017 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.renderer.style;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class RenderableSVG {
    Rectangle2D bounds;

    GraphicsNode node;

    public RenderableSVG(Document doc) {
        this.node = getGraphicNode(doc);
        this.bounds = getSvgDocBounds(doc);
        if (bounds == null) bounds = node.getBounds();
    }

    /**
     * Retrieves an SVG document's specified bounds.
     *
     * @param svgLocation an URL that specifies the SVG.
     * @return a {@link Rectangle2D} with the corresponding bounds. If the SVG document does not
     *     specify any bounds, then null is returned.
     * @throws IOException
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
        } catch (NumberFormatException e) {
            // strip off any units
            return Double.parseDouble(value.replaceAll("\\D*$", ""));
        }
    }

    /**
     * Retrieves a Batik {@link GraphicsNode} for a given SVG.
     *
     * @param svgLocation an URL that specifies the SVG.
     * @return the corresponding GraphicsNode.
     * @throws IOException
     * @throws URISyntaxException
     */
    GraphicsNode getGraphicNode(Document doc) {
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
            if (oldTransform == null) oldTransform = new AffineTransform();

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
