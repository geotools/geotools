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

import org.geotools.geometry.jts.coordinatesequence.CoordinateSequences;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml2.bindings.GMLEncodingUtils;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.Configuration;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Binding object for the type http://www.opengis.net/gml:AbstractGeometryType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType abstract="true" name="AbstractGeometryType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;All geometry elements are derived directly or indirectly from this abstract supertype. A geometry element may
 *                          have an identifying attribute ("gml:id"), a name (attribute "name") and a description (attribute "description"). It may be associated
 *                          with a spatial reference system (attribute "srsName"). The following rules shall be adhered: - Every geometry type shall derive
 *                          from this abstract type. - Every geometry element (i.e. an element of a geometry type) shall be directly or indirectly in the
 *                          substitution group of _Geometry.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGMLType"&gt;
 *              &lt;attribute name="gid" type="string" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;This attribute is included for backward compatibility with GML 2 and is deprecated with GML 3.
 *                                                  This identifer is superceded by "gml:id" inherited from AbstractGMLType. The attribute "gid" should not be used
 *                                                  anymore and may be deleted in future versions of GML without further notice.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *              &lt;attributeGroup ref="gml:SRSReferenceGroup"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class AbstractGeometryTypeBinding extends AbstractComplexBinding {
    Configuration config;
    SrsSyntax srsSyntax;

    public AbstractGeometryTypeBinding(Configuration config, SrsSyntax srsSyntax) {
        this.config = config;
        this.srsSyntax = srsSyntax;
    }

    public void setConfiguration(Configuration config) {
        this.config = config;
    }

    public void setSrsSyntax(SrsSyntax srsSyntax) {
        this.srsSyntax = srsSyntax;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.AbstractGeometryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Geometry.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //set the crs
        if (value instanceof Geometry) {
            CoordinateReferenceSystem crs = GML3ParsingUtils.crs(node);

            if (crs != null) {
                Geometry geometry = (Geometry) value;
                geometry.setUserData(crs);
            }
        }

        return value;
    }
    
    public Object getProperty(Object object, QName name)
        throws Exception {
        Geometry geometry = (Geometry) object;
        
        if ("srsName".equals(name.getLocalPart())) {
            CoordinateReferenceSystem crs = GML3EncodingUtils.getCRS(geometry);
            if (crs != null) {
                return GML3EncodingUtils.toURI(crs, srsSyntax);
            }
        }

        if ("srsDimension".equals(name.getLocalPart())) {
            if (GMLEncodingUtils.isEmpty(geometry)) {
                return null;
            }
            
            //check if srsDimension is turned off
            if (config.hasProperty(GMLConfiguration.NO_SRS_DIMENSION)) {
                return null;
            }

            /**
             * For the dimension, use the actual dimension of the geometry. Using
             * the dimension of the CRS is not sufficient, since currently CRSes
             * don't support 3D.
             */
            return CoordinateSequences.coordinateDimension(geometry);
        }

        // FIXME: should be gml:id, but which GML?
        // Refactor bindings or introduce a new one for GML 3.2
        if ("id".equals(name.getLocalPart())) {
            return GML3EncodingUtils.getID(geometry);
        }

        // FIXME: should be gml:name, but which GML?
        // Refactor bindings or introduce a new one for GML 3.2
        if ("name".equals(name.getLocalPart())) {
            return GML3EncodingUtils.getName(geometry);
        }
        
        // FIXME: should be gml:description, but which GML?
        // Refactor bindings or introduce a new one for GML 3.2
        if ("description".equals(name.getLocalPart())) {
            return GML3EncodingUtils.getDescription(geometry);
        }   
        if ("uomLabels".equals(name.getLocalPart())) {
            return GML3EncodingUtils.getUomLabels(geometry);
        }

        if ("axisLabels".equals(name.getLocalPart())) {
            return GML3EncodingUtils.getAxisLabels(geometry);
        }

        return null;
    }
}
