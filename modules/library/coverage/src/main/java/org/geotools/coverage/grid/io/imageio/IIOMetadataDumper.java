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
package org.geotools.coverage.grid.io.imageio;

import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormat;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Utility class that can be used to dump a DOM tree in a formatted way.
 *
 * <p>It is useful for inspecting the metadata inside GeoTiff files.
 *
 * @author Simone Giannecchini
 * @since 2.3.x
 */
public final class IIOMetadataDumper {

    /** Root of the XML tree to display. */
    private Node root;

    /** The {@link IIOMetadataFormat} name to print out the {@link IIOMetadata} for. */
    private volatile String formatName = "";

    /** The encoded {@link IIOMetadata} as a simple {@link String}. */
    private String metadata;

    /**
     * Constructor for a {@link IIOMetadataDumper} accepting and {@link IIOMetadata} and a {@link
     * String} for the format name of the XML metadata to use.
     *
     * @param metadata The metadta to display.
     * @param name The format of the metaata to display.
     */
    public IIOMetadataDumper(IIOMetadata metadata, String name) {
        this.root = metadata.getAsTree(name);
        StringBuffer buff = new StringBuffer();
        parseMetadata(buff, root, 0);
        this.formatName = name;
        this.metadata = buff.toString();
    }

    /**
     * Constructor for a {@link IIOMetadataDumper} accepting an {@link IIOMetadataNode}. It has no
     * way to choose the format of the metadata to parse since this choice has been already done
     * previously.
     */
    public IIOMetadataDumper(IIOMetadataNode rootNode) {
        this.root = rootNode;
        StringBuffer buff = new StringBuffer();
        parseMetadata(buff, root, 0);
        this.metadata = buff.toString();
    }

    /** Adds indentation to the tree while we build it */
    private void indent(StringBuffer buff, int level) {
        for (int i = 0; i < level; i++) {
            buff.append("  ");
        }
    }

    /** Builds a graphical representation of a certain XML tree. */
    private void parseMetadata(StringBuffer buff, Node node, int level) {
        indent(buff, level); // emit open tag
        buff.append("<").append(node.getNodeName());
        NamedNodeMap map = node.getAttributes();
        if (map != null) { // print attribute values
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
                Node attr = map.item(i);
                buff.append(" ")
                        .append(attr.getNodeName())
                        .append("=\"")
                        .append(attr.getNodeValue())
                        .append("\"");
            }
        }

        Node child = node.getFirstChild();
        if (child != null) {
            buff.append(">\n"); // close current tag
            while (child != null) { // emit child tags recursively
                parseMetadata(buff, child, level + 1);
                child = child.getNextSibling();
            }
            indent(buff, level); // emit close tag
            buff.append("</").append(node.getNodeName()).append(">\n");
        } else {
            buff.append("/>\n");
        }
    }

    /**
     * Allows me to get the generated XML representation for the underlying tree;
     *
     * @return A formatted XML string.
     */
    public synchronized String getMetadata() {
        return metadata;
    }

    /**
     * Retrieves the name of the format we want to get the XML representation for.
     *
     * @return The name of the format we want to get the XML representation for.
     */
    public synchronized String getFormatName() {
        return formatName;
    }

    /**
     * Sets the name of the format we want to get the XML representation for.
     *
     * <p>This method causes a new generation of the string representation if the format is
     * different from the one stored.
     *
     * @param formatName The name of the format we want to get the XML representation for.
     */
    public synchronized void setFormatName(String formatName) {
        if (this.formatName.equalsIgnoreCase(formatName)) return;
        StringBuffer buff = new StringBuffer();
        parseMetadata(buff, root, 0);
        this.formatName = formatName;
        this.metadata = buff.toString();
    }
}
