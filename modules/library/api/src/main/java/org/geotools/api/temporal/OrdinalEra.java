/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.temporal;

import java.util.Collection;
import java.util.Date;
import org.geotools.api.util.InternationalString;

/**
 * Provides a reference to the ordinal era in which the instant occurs.
 *
 * @author Alexander Petkov
 */
public interface OrdinalEra {
    /** The unique name of the ordinal era within the ordinal reference system. */
    InternationalString getName();

    /** The beginning at which the ordinal era began if it's known. */
    Date getBeginning();

    /** The end at which the ordinal era began if it's known. */
    Date getEnd();

    /** Ordinal eras that subdivide this ordinal era. */
    Collection<OrdinalEra> getComposition();
}
