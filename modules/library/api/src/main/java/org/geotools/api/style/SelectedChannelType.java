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
     * Returns the channel's name.
     *
     * @return Source channel name
     */
    Expression getChannelName();

    ContrastEnhancement getContrastEnhancement();

    void accept(StyleVisitor visitor);
}
