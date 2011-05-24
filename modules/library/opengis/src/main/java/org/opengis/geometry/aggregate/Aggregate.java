/*$************************************************************************************************
 **
 ** $Id: Aggregate.java 1356 2009-02-20 10:02:26Z desruisseaux $
 **
 ** $URL: https://geoapi.svn.sourceforge.net/svnroot/geoapi/tags/2.3-M2/geoapi-pending/src/main/java/org/opengis/geometry/aggregate/Aggregate.java $
 **
 ** Copyright (C) 2003-2005 Open GIS Consortium, Inc.
 ** All Rights Reserved. http://www.opengis.org/legal/
 **
 *************************************************************************************************/
package org.opengis.geometry.aggregate;

import java.util.Set;
import org.opengis.geometry.Geometry;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Geometry that is an aggregate of other geometries.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/aggregate/Aggregate.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @since GeoAPI 1.0
 */
@UML(identifier="GM_Aggregate", specification=ISO_19107)
public interface Aggregate extends Geometry {
    /**
     * Returns the set containing the elements that compose this aggregate. The
     * set may be modified if this geometry {@linkplain #isMutable is mutable}.
     *
     * @return The set containing the elements that compose this aggregate.
     */
    @UML(identifier="element", obligation=MANDATORY, specification=ISO_19107)
    Set<? extends Geometry> getElements();
}
