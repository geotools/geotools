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
package org.geotools.ows.bindings;

import java.math.BigInteger;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.Ows11Factory;

import org.eclipse.emf.ecore.EFactory;
import org.geotools.ows.OWS;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/ows:BoundingBoxType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="BoundingBoxType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML encoded minimum rectangular bounding box (or region) parameter, surrounding all the associated data. &lt;/documentation&gt;
 *          &lt;documentation&gt;This type is adapted from the EnvelopeType of GML 3.1, with modified contents and documentation for encoding a MINIMUM size box SURROUNDING all associated data. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element name="LowerCorner" type="ows:PositionType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Position of the bounding box corner at which the value of each coordinate normally is the algebraic minimum within this bounding box. In some cases, this position is normally displayed at the top, such as the top left for some image coordinates. For more information, see Subclauses 10.2.5 and C.13. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *          &lt;element name="UpperCorner" type="ows:PositionType"&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;Position of the bounding box corner at which the value of each coordinate normally is the algebraic maximum within this bounding box. In some cases, this position is normally displayed at the bottom, such as the bottom right for some image coordinates. For more information, see Subclauses 10.2.5 and C.13. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/element&gt;
 *      &lt;/sequence&gt;
 *      &lt;attribute name="crs" type="anyURI" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;Usually references the definition of a CRS, as specified in [OGC Topic 2]. Such a CRS definition can be XML encoded using the gml:CoordinateReferenceSystemType in [GML 3.1]. For well known references, it is not required that a CRS definition exist at the location the URI points to. If no anyURI value is included, the applicable CRS must be either:
 *  a)        Specified outside the bounding box, but inside a data structure that includes this bounding box, as specified for a specific OWS use of this bounding box type.
 *  b)        Fixed and specified in the Implementation Specification for a specific OWS use of the bounding box type. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *      &lt;attribute name="dimensions" type="positiveInteger" use="optional"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;The number of dimensions in this CRS (the length of a coordinate sequence in this use of the PositionType). This number is specified by the CRS definition, but can also be specified here. &lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attribute&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class BoundingBoxTypeBinding extends AbstractComplexEMFBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS.BoundingBoxType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BoundingBoxType.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        BoundingBoxType bbox = Ows11Factory.eINSTANCE.createBoundingBoxType();
        bbox.setLowerCorner((List) node.getChildValue("LowerCorner"));
        bbox.setUpperCorner((List) node.getChildValue("UpperCorner"));
        if(node.getAttributeValue("crs") != null) {
            bbox.setCrs(node.getAttributeValue("crs").toString());
        }
        if(node.getAttributeValue("dimensions") != null) {
            bbox.setDimensions((BigInteger) node.getAttributeValue("dimensions"));
        }
        
        return bbox;
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("LowerCorner".equals(name.getLocalPart()) || "UpperCorner".equals(name.getLocalPart())) {
            //JD: this is a hack to get around the fact that the encoder won't match up simple list
            // types with a binding
            Object value = super.getProperty(object, name);
            if (value instanceof List) {
                return new PositionTypeBinding().encode(value, value.toString());
            }
        }
        
        return super.getProperty(object, name);    
        
    }
}
