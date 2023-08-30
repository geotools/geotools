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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import javax.swing.Icon;
import org.geotools.api.metadata.citation.OnLineResource;

/**
 * Points to an external file that contains an image of some kind, such as a CGM, JPG, or SVG.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface ExternalGraphic extends GraphicalSymbol, Symbol {

    ExternalGraphic[] EXTERNAL_GRAPHICS_EMPTY = new ExternalGraphic[0];

    /**
     * Returns a OnlineResource to a file (perhaps a local file) that contains an image. This can be
     * null if the image is already loaded locally and the {@link #getInlineContent InlineContent}
     * property is set.
     *
     * @return OnlineResource
     */
    OnLineResource getOnlineResource();

    /**
     * Returns the InlineContent that comprise the image. This overrides the {@link
     * #getOnlineResource OnlineResource} property, if it is set.
     */
    Icon getInlineContent();

    /**
     * Returns the mime type of the onlineResource/InlineContent
     *
     * @return mime type
     */
    String getFormat();

    /**
     * The ColorReplacement element, which may occur multiple times, allows to replace a color in
     * the ExternalGraphic, the color specified in the OriginalColor sub-element, by another color
     * as a result of a recode function as defined in {@link Interpolate} .
     */
    Collection<ColorReplacement> getColorReplacements();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /**
     * Converts a URI in a string to the location URL
     *
     * @param uri the uri of the external graphic
     */
    void setURI(String uri);

    /**
     * Returns the un-parsed URI for the mark (useful if the uri is using transformations or
     * relative locations)
     */
    String getURI();

    /**
     * Provides the URL for where the external graphic resource can be located.
     *
     * <p>This method will be replaced by getOnlineResource().getLinkage() in 2.6.x
     *
     * @return The URL of the ExternalGraphic
     * @throws MalformedURLException If the url held in the ExternalGraphic is malformed.
     */
    URL getLocation() throws MalformedURLException;

    /**
     * Provides the URL for where the external graphic resource can be located.
     *
     * @param url The URL of the ExternalGraphic
     */
    void setLocation(URL url);

    /**
     * Provides the format of the external graphic.
     *
     * @param format The format of the external graphic. Reported as its MIME type in a String
     *     object.
     */
    void setFormat(String format);

    /** Custom properties; renderer may consult these values when drawing graphic. */
    void setCustomProperties(Map<String, Object> properties);

    /**
     * Custom user supplied properties available when working with an external graphic.
     *
     * @return properties
     */
    Map<String, Object> getCustomProperties();
}
