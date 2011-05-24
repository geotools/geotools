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

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * One-dimensional topological primitive in time.
 *
 * @author Alexander Petkov
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/temporal/TemporalEdge.java $
 */
@UML(identifier="TM_Edge", specification=ISO_19108)
public interface TemporalEdge extends TemporalTopologicalPrimitive {
    /**
     * An optional association that links this edge to the corresponding period.
     */
    @UML(identifier="Realization", obligation=OPTIONAL, specification=ISO_19108)
    Period getRealization();

    /**
     * Links this edge to the node that is its start.
     */
    @UML(identifier="start", obligation=MANDATORY, specification=ISO_19108)
    TemporalNode getStart();

    /**
     * Links this edge to the node that is its end.
     */
    @UML(identifier="end", obligation=MANDATORY, specification=ISO_19108)
    TemporalNode getEnd();
}
