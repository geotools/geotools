/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.complex;

import java.util.List;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.annotation.Association;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A {@linkplain Complex complex} with all the geometric properties of a curve. Thus, this
 * composite can be considered as a type of {@linkplain OrientableCurve orientable curve}.
 * Essentially, a composite curve is a list of {@linkplain OrientableCurve orientable curves}
 * agreeing in orientation in a manner such that each curve (except the first) begins where
 * the previous one ends.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/complex/CompositeCurve.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @todo This interface extends (indirectly) both {@link org.opengis.geometry.primitive.Primitive} and
 *       {@link org.opengis.geometry.complex.Complex}. Concequently, there is a clash in the semantics
 *       of some set theoretic operation. Specifically, {@code Primitive.contains(...)}
 *       (returns FALSE for end points) is different from {@code Complex.contains(...)}
 *       (returns TRUE for end points).
 */
@UML(identifier="GM_CompositeCurve", specification=ISO_19107)
public interface CompositeCurve extends Composite, OrientableCurve {
    /**
     * Returns the list of orientable curves in this composite.
     * To get a full representation of the elements in the {@linkplain Complex complex},
     * the {@linkplain org.opengis.geometry.primitive.Point points} on the boundary of the
     * generator set of {@linkplain org.opengis.geometry.primitive.Curve curve} would be
     * added to the curves in the generator list.
     *
     * @return The list of orientable curves in this composite.
     *
     * @see OrientableCurve#getComposite
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Composition")
    @UML(identifier="generator", obligation=MANDATORY, specification=ISO_19107)
    List<OrientableCurve> getGenerators();
}
