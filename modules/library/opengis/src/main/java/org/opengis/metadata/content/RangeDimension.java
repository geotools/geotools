/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.content;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;

import org.opengis.annotation.UML;
import org.opengis.util.InternationalString;
import org.opengis.util.MemberName;

/**
 * Information on the range of each dimension of a cell measurement value.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
@UML(identifier = "MD_RangeDimension", specification = ISO_19115)
public interface RangeDimension {
    /**
     * Number that uniquely identifies instances of bands of wavelengths on which a sensor operates.
     *
     * @return Identifier of bands on which a sensor operates, or {@code null}.
     */
    @UML(identifier = "sequenceIdentifier", obligation = OPTIONAL, specification = ISO_19115)
    MemberName getSequenceIdentifier();

    /**
     * Description of the range of a cell measurement value.
     *
     * @return Description of the range of a cell measurement value, or {@code null}.
     */
    @UML(identifier = "descriptor", obligation = OPTIONAL, specification = ISO_19115)
    InternationalString getDescriptor();
}
