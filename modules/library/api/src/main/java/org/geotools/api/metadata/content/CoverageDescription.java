/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.content;

import java.util.Collection;
import org.geotools.api.util.RecordType;

/**
 * Information about the content of a grid data cell.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
public interface CoverageDescription extends ContentInformation {
    /**
     * Description of the attribute described by the measurement value.
     *
     * @return Description of the attribute.
     */
    RecordType getAttributeDescription();

    /**
     * Type of information represented by the cell value.
     *
     * @return Type of information represented by the cell value.
     */
    CoverageContentType getContentType();

    /**
     * Information on the dimensions of the cell measurement value.
     *
     * @return Dimensions of the cell measurement value.
     * @since GeoAPI 2.1
     */
    Collection<? extends RangeDimension> getDimensions();
}
