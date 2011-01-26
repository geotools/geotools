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

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * An operation on coordinates that usually includes a change of Datum. The parameters
 * of a coordinate transformation are empirically derived from data containing the coordinates
 * of a series of points in both coordinate reference systems. This computational process
 * is usually "over-determined", allowing derivation of error (or accuracy) estimates
 * for the transformation. Also, the stochastic nature of the parameters may result
 * in multiple (different) versions of the same coordinate transformation.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see Conversion
 */
@UML(identifier="CC_Transformation", specification=ISO_19111)
public interface Transformation extends Operation {
    /**
     * Returns the source CRS.
     *
     * @return The source CRS (never {@code null}).
     */
    @UML(identifier="sourceCRS", obligation=MANDATORY, specification=ISO_19111)
    CoordinateReferenceSystem getSourceCRS();

    /**
     * Returns the target CRS.
     *
     * @return The target CRS (never {@code null}).
     */
    @UML(identifier="targetCRS", obligation=MANDATORY, specification=ISO_19111)
    CoordinateReferenceSystem getTargetCRS();

    /**
     * Version of the coordinate transformation (i.e., instantiation due to the stochastic
     * nature of the parameters). This attribute is mandatory in a Transformation.
     *
     * @return The coordinate operation version.
     */
    @UML(identifier="operationVersion", obligation=MANDATORY, specification=ISO_19111)
    String getOperationVersion();
}
