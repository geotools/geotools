/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.operation;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A pass-through operation specifies that a subset of a coordinate tuple is subject to a specific
 * coordinate operation.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/operation/PassThroughOperation.java $
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 */
@UML(identifier="CC_PassThroughOperation", specification=ISO_19111)
public interface PassThroughOperation extends SingleOperation {
    /**
     * Returns the operation to apply on the subset of a coordinate tuple.
     *
     * @return The operation to apply on the subset of a coordinate tuple.
     */
    @UML(identifier="usesOperation", obligation=MANDATORY, specification=ISO_19111)
    Operation getOperation();

    /**
     * Ordered sequence of positive integers defining the positions in a coordinate
     * tuple of the coordinates affected by this pass-through operation.
     *
     * @return The modified coordinates.
     */
    @UML(identifier="modifiedCoordinate", obligation=MANDATORY, specification=ISO_19111)
    int[] getModifiedCoordinates();
}
