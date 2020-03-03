/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import javax.swing.Icon;
import org.opengis.metadata.citation.OnLineResource;

/**
 * Specify a mark using an image files (svg, png, gif) or using mark index a true type font file.
 *
 * <p>Please note that not all render can handle all image file formats; please organize your marks
 * into a preferred order with the most specific (say SVG) followed by common formats (PNG, GIF) and
 * ending with an appropriate WellKnownName.
 */
public interface ExternalMark extends org.opengis.style.ExternalMark {

    /**
     * Online resource defined by an URI.
     *
     * <p>Only one of OnlineResource or InlineContent can be supplied.
     *
     * @return OnlineResource or <code>null</code>
     */
    OnLineResource getOnlineResource();

    /** @param resource Online resource with format defined by getFormat() */
    void setOnlineResource(OnLineResource resource);

    /**
     * Inline content.
     *
     * <p>Only one of OnlineResource or InlineContent can be supplied.
     *
     * @return InlineContent or <code>null</code>
     */
    Icon getInlineContent();

    /**
     * Icon to use for inline content.
     *
     * <p>This is often a SwingImageIcon with a format defined by getFormat()
     */
    void setInlineContent(Icon inline);

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

    /** @param mimeType Mime type of external (or internal) resource */
    void setFormat(String mimeType);

    /**
     * Returns an integer value that can used for accessing a particular Font character in a TTF
     * file or a catalog for example.
     *
     * @return integer
     */
    int getMarkIndex();

    /** Mark index used to specify true type font character; or frame of an animated gif. */
    void setMarkIndex(int markIndex);
}
