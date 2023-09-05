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

import org.geotools.api.filter.expression.Expression;

/**
 * A class to hold Channel information for use in ChannelSelection objects.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface SelectedChannelType {

    /**
     * Set the source channel name.
     *
     * @param name name of the source channel
     */
    void setChannelName(Expression name);

    void setChannelName(String name);

    /**
     * Returns the channel's name.
     *
     * @return Source channel name
     */
    Expression getChannelName();

    void setContrastEnhancement(ContrastEnhancement enhancement);

    ContrastEnhancement getContrastEnhancement();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    void accept(StyleVisitor visitor);
}
