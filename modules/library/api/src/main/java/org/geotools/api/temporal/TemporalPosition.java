/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.temporal;

/**
 * Used for describing temporal positions referenced to other temporal reference systems.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
public interface TemporalPosition {
    /**
     * This attribute provides the only value for temporal position unless a subtype of {@code
     * TemporalPosition} is used as the data type. When this attribute is used with a subtype of
     * {@code TemporalPosition}, it provides a qualifier to the specific value for temporal position
     * provided by the subtype.
     *
     * @todo Method name doesn't match the return type.
     */
    IndeterminateValue getIndeterminatePosition();
}
