/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.wmts.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.DomainMetadataType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.ows.bindings.DescriptionTypeBinding;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:Dimension.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="Dimension" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;
 *  				Metadata about a particular dimension that the tiles of
 *  				a layer are available.
 *  			&lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:DescriptionType"&gt;
 *  					&lt;sequence&gt;
 *  						&lt;element ref="ows:Identifier"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;A name of dimensional axis&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" ref="ows:UOM"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Units of measure of dimensional axis.&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" name="UnitSymbol" type="string"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Symbol of the units.&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" name="Default" type="string"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  									Default value that will be used if a tile request does
 *  									not specify a value or uses the keyword 'default'.
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" name="Current" type="boolean"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  									A value of 1 (or 'true') indicates (a) that temporal data are
 *  									normally kept current and (b) that the request value of this
 *  									dimension accepts the keyword 'current'.
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element maxOccurs="unbounded" name="Value" type="string"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Available value for this dimension.&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  					&lt;/sequence&gt;
 *  				&lt;/extension&gt;
 *  			&lt;/complexContent&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class DimensionBinding extends DescriptionTypeBinding {

    wmtsv_1Factory wmtsv_1Factory;

    public DimensionBinding(wmtsv_1Factory factory) {
        super(Ows10Factory.eINSTANCE);
        this.wmtsv_1Factory = factory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WMTS.Dimension;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class<?> getType() {
        return DimensionType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        if (!(value instanceof DimensionType)) {
            value = wmtsv_1Factory.createDimensionType();
        }

        // Call DescriptionType parser to load the object with the DescriptionType values
        value = super.parse(instance, node, value);

        ((DimensionType) value).setCurrent((boolean) node.getChildValue("Current", false));
        ((DimensionType) value).setDefault((String) node.getChildValue("Default"));
        ((DimensionType) value).setIdentifier((CodeType) node.getChildValue("Identifier"));
        ((DimensionType) value).setUnitSymbol((String) node.getChildValue("UnitSymbol", null));
        @SuppressWarnings("unchecked")
        List<String> values = node.getChildValues("Value");
        ((DimensionType) value).getValue().addAll(values);

        Object UomList = node.getChildValue("UOM");
        if (UomList != null) {

            DomainMetadataType uom = Ows11Factory.eINSTANCE.createDomainMetadataType();
            uom.setValue(UomList.toString());
            ((DimensionType) value).setUOM(uom);
        }
        return value;
    }
}
