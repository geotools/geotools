/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/geometry/GeometryFactoryImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.geometry;

// J2SE direct dependencies

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The {@code GeometryFactoryImpl} class/interface...
 *
 * @author SYS Technologies
 * @author crossley
 * @version $Revision $
 * @deprecated Use GeometryFactoryFinder
 */
public class GeometryFactoryImpl extends JTSGeometryFactory {
    public GeometryFactoryImpl(CoordinateReferenceSystem crs) {
        super(crs);
    }
}
