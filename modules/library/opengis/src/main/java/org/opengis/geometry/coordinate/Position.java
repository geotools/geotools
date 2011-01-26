/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.coordinate;

import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.primitive.Point;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A type consisting of either a {@linkplain DirectPosition direct position} or of a
 * {@linkplain Point point} from which a {@linkplain DirectPosition direct position}
 * shall be obtained. The use of this data type allows the identification of a position
 * either directly as a coordinate (variant direct) or indirectly as a {@linkplain Point point}
 * (variant indirect).
 *
 * @departure
 *   ISO 19111 defines {@code Position} like a C/C++ {@code union} of {@code DirectPosition} and
 *   {@code Point}. Since unions are not allowed in Java, GeoAPI defines {@code Position} as the
 *   base interface of the above. This leads to a slightly different semantic since ISO defines
 *   {@link #getDirectPosition} as conditional, while GeoAPI defines it as mandatory by allowing
 *   the method to return {@code this}.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @issue http://jira.codehaus.org/browse/GEO-87
 */
@UML(identifier="GM_Position", specification=ISO_19107)
public interface Position {
    /**
     * @deprecated Renamed as {@link #getDirectPosition} for avoiding ambiguity.
     *
     * @return The direct position.
     */
    @Deprecated
    @UML(identifier="direct", obligation=CONDITIONAL, specification=ISO_19107)
    DirectPosition getPosition();

    /**
     * Returns the direct position. This method shall never returns {@code null}, but may returns
     * {@code this} if invoked on an object which is already a {@code DirectPosition} instance.
     *
     * @return The direct position (may be {@code this}).
     *
     * @since GeoAPI 2.2
     */
    @UML(identifier="direct", obligation=CONDITIONAL, specification=ISO_19107)
    DirectPosition getDirectPosition();
}
