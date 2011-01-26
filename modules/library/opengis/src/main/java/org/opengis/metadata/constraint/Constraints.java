/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.constraint;

import java.util.Collection;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Restrictions on the access and use of a resource or metadata.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_Constraints", specification=ISO_19115)
public interface Constraints {
    /**
     * Limitation affecting the fitness for use of the resource.
     * Example: "not to be used for navigation".
     *
     * @return Limitation affecting the fitness for use of the resource.
     */
    @UML(identifier="useLimitation", obligation=OPTIONAL, specification=ISO_19115)
    Collection<? extends InternationalString> getUseLimitation();
}
