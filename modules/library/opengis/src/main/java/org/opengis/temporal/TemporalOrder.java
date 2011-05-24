/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.temporal;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Provides an operation for determining the position of this {@linkplain TemporalPrimitive
 * temporal primitive} relative to another {@linkplain TemporalPrimitive temporal primitive}.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 *
 * @todo The javadoc suggests that this interface should extends some kind of
 *       {@link TemporalPrimitive}.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/temporal/TemporalOrder.java $
 */
@UML(identifier="TM_Order", specification=ISO_19108)
public interface TemporalOrder {
    /**
     * Determines the position of this primitive relative to another primitive.
     */
    @UML(identifier="relativePosition", obligation=MANDATORY, specification=ISO_19108)
    RelativePosition relativePosition(TemporalPrimitive other);
}
