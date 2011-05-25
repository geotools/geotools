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
import com.vividsolutions.jts.geom.Polygon;

/**
 * Binding object for the type http://www.opengis.net/gml:PolygonPatchType.
 * 
 * <p>
 * 
 * <pre>
 *  &lt;code&gt;
 *  &lt;complexType name=&quot;PolygonPatchType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;
 *              A PolygonPatch is a surface patch that is defined by
 *              a set of boundary curves and an underlying surface to
 *              which these curves adhere. The curves are coplanar and
 *              the polygon uses planar interpolation in its interior.
 *              Implements GM_Polygon of ISO 19107. 
 *           &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base=&quot;gml:AbstractSurfacePatchType&quot;&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs=&quot;0&quot; ref=&quot;gml:exterior&quot;/&gt;
 *                  &lt;element maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot; ref=&quot;gml:interior&quot;/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute fixed=&quot;planar&quot; name=&quot;interpolation&quot; type=&quot;gml:SurfaceInterpolationType&quot;&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;
 *                       The attribute &quot;interpolation&quot; specifies the
 *                       interpolation mechanism used for this surface
 *                       patch. Currently only planar surface patches
 *                       are defined in GML 3, the attribute is fixed
 *                       to &quot;planar&quot;, i.e. the interpolation method
 *                       shall return points on a single plane. The
 *                       boundary of the patch shall be contained within
 *                       that plane.
 *                    &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
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
 *
 * @source $URL$
 */
public class PolygonPatchTypeBinding extends AbstractComplexBinding {

    protected GeometryFactory gf;
    
    public PolygonPatchTypeBinding(GeometryFactory gf) {
        this.gf = gf;
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return GML.PolygonPatchType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return Polygon.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return new PolygonTypeBinding( gf ).parse( instance, node, value);
    }

}
