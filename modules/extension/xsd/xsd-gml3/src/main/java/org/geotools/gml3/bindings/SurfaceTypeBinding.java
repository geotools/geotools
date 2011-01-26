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
package org.geotools.gml3.bindings;

import javax.xml.namespace.QName;

import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * Binding object for the type http://www.opengis.net/gml:SurfaceType.
 * 
 * <p>
 * 
 * <pre>
 *  &lt;code&gt;
 *  &lt;complexType name=&quot;SurfaceType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;
 *              A Surface is a 2-dimensional primitive and is composed
 *              of one or more surface patches. The surface patches are
 *              connected to one another.
 *              The orientation of the surface is positive (&quot;up&quot;). The
 *              orientation of a surface chooses an &quot;up&quot; direction
 *              through the choice of the upward normal, which, if the
 *              surface is not a cycle, is the side of the surface from
 *              which the exterior boundary appears counterclockwise.
 *              Reversal of the surface orientation reverses the curve
 *              orientation of each boundary component, and interchanges
 *              the conceptual &quot;up&quot; and &quot;down&quot; direction of the surface.
 *              If the surface is the boundary of a solid, the &quot;up&quot;
 *              direction is usually outward. For closed surfaces, which
 *              have no boundary, the up direction is that of the surface
 *              patches, which must be consistent with one another. Its
 *              included surface patches describe the interior structure
 *              of the Surface.
 *           &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:AbstractSurfaceType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element ref=&quot;gml:patches&quot;&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;
 *                          This element encapsulates the patches of the
 *                          surface.
 *                       &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 * 	
 *   &lt;/code&gt;
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL$
 */
public class SurfaceTypeBinding extends AbstractComplexBinding implements Comparable {

    GeometryFactory gf;
    
    public SurfaceTypeBinding(GeometryFactory gf) {
        this.gf = gf;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.SurfaceType;
    }
    
    @Override
    public int getExecutionMode() {
        return BEFORE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return MultiPolygon.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return new MultiPolygonTypeBinding(gf).parse(instance, node, value);
    }

    public int compareTo(Object o) {
        //JD: HACK here, since we map SurfaceType and MultiSurfaceType to MultiPolygon, there is a 
        // conflict when it comes to encoding where the actual type is not specifically specifid.
        // this comparison is made to ensure backwards compatability and favor MultiSurfaceTypeBinding 
        if ( o instanceof MultiSurfaceTypeBinding ) {
            return 1;
        }
        return 0;
    }

}
