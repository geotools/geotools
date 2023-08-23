/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.constraint;

import java.util.Collection;
import org.geotools.api.util.InternationalString;

/**
 * Restrictions and legal prerequisites for accessing and using the resource.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
public interface LegalConstraints extends Constraints {
    /**
     * Access constraints applied to assure the protection of privacy or intellectual property, and
     * any special restrictions or limitations on obtaining the resource.
     *
     * @return Access constraints applied to assure the protection of privacy or intellectual
     *     property.
     */
    Collection<Restriction> getAccessConstraints();

    /**
     * Constraints applied to assure the protection of privacy or intellectual property, and any
     * special restrictions or limitations or warnings on using the resource.
     *
     * @return Constraints applied to assure the protection of privacy or intellectual property.
     */
    Collection<Restriction> getUseConstraints();

    /**
     * Other restrictions and legal prerequisites for accessing and using the resource. This method
     * should returns a non-empty value only if {@linkplain #getAccessConstraints access
     * constraints} or {@linkplain #getUseConstraints use constraints} declares {@linkplain
     * Restriction#OTHER_RESTRICTIONS other restrictions}.
     *
     * @return Other restrictions and legal prerequisites for accessing and using the resource.
     */
    Collection<? extends InternationalString> getOtherConstraints();
}
