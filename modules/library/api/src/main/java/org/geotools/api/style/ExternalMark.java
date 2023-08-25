/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.style;

import javax.swing.Icon;
import org.geotools.api.metadata.citation.OnLineResource;

/**
 * The alternative to a WellKnownName is an external mark format. The MarkIndex allows an individual
 * mark in a mark archive to be selected. An example format for an external mark archive would be a
 * TrueType font file, with MarkIndex being used to select an individual glyph from that file.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface ExternalMark {

    /**
     * Online resource defined by an URI.
     *
     * <p>Only one of OnlineResource or InlineContent can be supplied.
     *
     * @return OnlineResource or <code>null</code>
     */
    OnLineResource getOnlineResource();

    /**
     * Inline content.
     *
     * <p>Only one of OnlineResource or InlineContent can be supplied.
     *
     * @return InlineContent or <code>null</code>
     */
    Icon getInlineContent();

    /**
     * Mime type of the onlineResource/InlineContent
     *
     * <p>Common examples:
     *
     * <ul>
     *   <li>image/svg
     *   <li>image/png
     *   <li>image/gif
     * </ul>
     *
     * This information is used by a renderer to determine if it can support the image format being
     * supplied.
     *
     * @return mime type
     */
    String getFormat();

    /**
     * Returns an integer value that can used for accessing a particular Font character in a TTF
     * file or a catalog for example.
     *
     * @return integer
     */
    int getMarkIndex();
}
