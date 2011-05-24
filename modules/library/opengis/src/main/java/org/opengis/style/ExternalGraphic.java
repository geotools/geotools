/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import java.util.Collection;
import javax.swing.Icon;
import org.opengis.annotation.Extension;
import org.opengis.annotation.XmlElement;
import org.opengis.metadata.citation.OnLineResource;


/**
 * Points to an external file that contains an image of some kind, such as a CGM, JPG, or SVG.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/ExternalGraphic.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
@XmlElement("ExternalGraphic")
public interface ExternalGraphic extends GraphicalSymbol {

    /**
     * Returns a OnlineResource to a file (perhaps a local file) that contains an image.
     * This can be null if the image is already loaded locally and the
     * {@link #getInlineContent InlineContent} property is set.
     *
     * @return OnlineResource
     */
    @XmlElement("OnlineResource")
    OnLineResource getOnlineResource();

    /**
     * Returns the InlineContent that comprise the image.  This overrides the
     * {@link #getOnlineResource OnlineResource} property, if it is set.
     *
     * @return
     */
    @XmlElement("InlineContent")
    Icon getInlineContent();

    /**
     * Returns the mime type of the onlineResource/InlineContent
     *
     * @return mime type
     */
    @XmlElement("Format")
    String getFormat();

    /**
     * The ColorReplacement element, which may occur multiple times, allows to replace a
     * color in the ExternalGraphic, the color specified in the OriginalColor sub-element, by
     * another color as a result of a recode function as defined in {@link Interpolate} .
     */
    @XmlElement("ColorReplacement")
    Collection<ColorReplacement> getColorReplacements();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    @Extension
    Object accept(StyleVisitor visitor, Object extraData);
    
}
