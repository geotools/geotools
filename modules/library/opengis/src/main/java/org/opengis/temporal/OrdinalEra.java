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

import java.util.Collection;
import java.util.Date;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Provides a reference to the ordinal era in which the instant occurs.
 *
 * @author Alexander Petkov
 */
@UML(identifier="TM_OrdinalEra", specification=ISO_19108)
public interface OrdinalEra {
    /**
     * The unique name of the ordinal era within the ordinal reference system.
     */
    @UML(identifier="name", obligation=MANDATORY, specification=ISO_19108)
    InternationalString getName();

    /**
     * The beginning at which the ordinal era began if it's known.
     */
    @UML(identifier="begin", obligation=OPTIONAL, specification=ISO_19108)
    Date getBeginning();

    /**
     * The end at which the ordinal era began if it's known.
     */
    @UML(identifier="end", obligation=OPTIONAL, specification=ISO_19108)
    Date getEnd();

    /**
     * Ordinal eras that subdivide this ordinal era.
     */
    @UML(identifier="Composition", obligation=MANDATORY, specification=ISO_19108)
    Collection<OrdinalEra> getComposition();
}
