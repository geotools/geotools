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

/**
 * The ChannelSelection element specifies the false-color channel selection for a multi-spectral
 * raster source (such as a multi-band satellite-imagery source).
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface ChannelSelection {

    /**
     * Set the RGB channels to be used
     *
     * @param red the red channel
     * @param green the green channel
     * @param blue the blue channel
     */
    void setRGBChannels(
            SelectedChannelType red, SelectedChannelType green, SelectedChannelType blue);

    /**
     * get the RGB channels to be used
     *
     * @return array of channels in RGB order
     */
    SelectedChannelType[] getRGBChannels();

    /**
     * Get the gray channel to be used
     *
     * @return the gray channel
     */
    SelectedChannelType getGrayChannel();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    void accept(StyleVisitor visitor);
}
