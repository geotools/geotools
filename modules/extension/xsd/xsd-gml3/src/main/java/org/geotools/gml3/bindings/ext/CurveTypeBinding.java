/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gml3.bindings.ext;

import org.geotools.gml3.bindings.LineStringTypeBinding;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Binding object for the type http://www.opengis.net/gml:CurveType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="CurveType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Curve is a 1-dimensional primitive. Curves are continuous, connected, and have a measurable length in terms of the coordinate system.
 *                                  A curve is composed of one or more curve segments. Each curve segment within a curve may be defined using a different interpolation method. The curve segments are connected to one another, with the end point of each segment except the last being the start point of the next segment in the segment list.
 *                                  The orientation of the curve is positive.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractCurveType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref="gml:segments"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;This element encapsulates the segments of the curve.&lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 * @source $URL$
 */
public class CurveTypeBinding extends org.geotools.gml3.bindings.CurveTypeBinding
        implements Comparable {

    public CurveTypeBinding(GeometryFactory gf) {
        super(gf);
    }

    public int compareTo(Object o) {
        if (o instanceof LineStringTypeBinding) {
            return -1;
        } else {
            return 0;
        }
    }
}
