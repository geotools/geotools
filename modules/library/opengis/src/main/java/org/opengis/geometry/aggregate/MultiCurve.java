/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.aggregate;

import java.util.Set;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * An aggregate class containing only instances of {@link OrientableCurve}.
 * The association role {@link #getElements element} shall be the set of
 * {@linkplain OrientableCurve orientable curves} contained in this {@code MultiCurve}.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Sanjay Jena
 * @author Prof. Dr. Jackson Roehrig
 * @since GeoAPI 2.1
 */
@UML(identifier="GM_MultiCurve", specification=ISO_19107)
public interface MultiCurve extends MultiPrimitive {
    /**
     * Returns the set containing the {@linkplain OrientableCurve orientable curves}
     * that compose this {@code MultiCurve}. The set may be modified if this geometry
     * {@linkplain #isMutable is mutable}.
     *
     * @return The set containing the orientable curves.
     */
    @UML(identifier="element", obligation=MANDATORY, specification=ISO_19107)
    Set<OrientableCurve> getElements();

    /**
     * Returns the accumulated length of all {@linkplain OrientableCurve orientable curves}
     * contained in this {@code MultiCurve}.
     *
     * @return The accumulated length.
     */
    @UML(identifier="length", obligation=MANDATORY, specification=ISO_19107)
    double length();
}
