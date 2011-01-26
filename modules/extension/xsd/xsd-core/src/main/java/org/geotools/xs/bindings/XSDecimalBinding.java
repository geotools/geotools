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
package org.geotools.xs.bindings;

import java.math.BigDecimal;
import java.util.Calendar;
import javax.xml.namespace.QName;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.SimpleBinding;
import org.geotools.xs.XS;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:decimal.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xs:simpleType name="decimal" id="decimal"&gt;
 *      &lt;xs:annotation&gt;
 *          &lt;xs:appinfo&gt;
 *              &lt;hfp:hasFacet name="totalDigits"/&gt;
 *              &lt;hfp:hasFacet name="fractionDigits"/&gt;
 *              &lt;hfp:hasFacet name="pattern"/&gt;
 *              &lt;hfp:hasFacet name="whiteSpace"/&gt;
 *              &lt;hfp:hasFacet name="enumeration"/&gt;
 *              &lt;hfp:hasFacet name="maxInclusive"/&gt;
 *              &lt;hfp:hasFacet name="maxExclusive"/&gt;
 *              &lt;hfp:hasFacet name="minInclusive"/&gt;
 *              &lt;hfp:hasFacet name="minExclusive"/&gt;
 *              &lt;hfp:hasProperty name="ordered" value="total"/&gt;
 *              &lt;hfp:hasProperty name="bounded" value="false"/&gt;
 *              &lt;hfp:hasProperty name="cardinality" value="countably infinite"/&gt;
 *              &lt;hfp:hasProperty name="numeric" value="true"/&gt;
 *          &lt;/xs:appinfo&gt;
 *          &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-2/#decimal"/&gt;
 *      &lt;/xs:annotation&gt;
 *      &lt;xs:restriction base="xs:anySimpleType"&gt;
 *          &lt;xs:whiteSpace value="collapse" fixed="true" id="decimal.whiteSpace"/&gt;
 *      &lt;/xs:restriction&gt;
 *  &lt;/xs:simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class XSDecimalBinding implements SimpleBinding {
    /**
     * @generated
     */
    public QName getTarget() {
        return XS.DECIMAL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * This binding returns objects of type {@link BigDecimal}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BigDecimal.class;
    }

    /**
     *         /**
     * <!-- begin-user-doc -->
     * This is AFTER so value contains element.text after processing by AnySimpleType.
     * This binding returns objects of type {@link Calendar}.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        //DatatypeConverter.setDatatypeConverter(DatatypeConverterImpl.theInstance);
        //BigDecimal decimal = DatatypeConverter.parseDecimal((String) value);
        String text = (String) value;

        if (text.startsWith("+")) {
            text = text.substring(1);
        }

        BigDecimal decimal = new BigDecimal(text);

        //		// TODO: facet checks to be done by framework 
        //		XSDSimpleTypeDefinition simple = (XSDSimpleTypeDefinition) instance.getTypeDefinition();
        //		
        //		BigDecimal maxInc = (BigDecimal) simple.getMaxInclusiveFacet().getValue();
        //		if (decimal.compareTo(maxInc) > 0) {
        //			throw new ValidationException("Decimal value is outside the inclusive max bounds of " + maxInc);
        //		}
        //		
        //		BigDecimal maxExc = (BigDecimal) simple.getMaxExclusiveFacet().getValue();
        //		if (decimal.compareTo(maxExc) >= 0) {
        //			throw new ValidationException("Decimal value is outside the exclusive max bounds of " + maxExc);
        //		}
        //		
        //		BigDecimal minInc = (BigDecimal) simple.getMinInclusiveFacet().getValue();
        //		if (decimal.compareTo(minInc) < 0) {
        //			throw new ValidationException("Decimal value is outside the inclusive min bounds of " + minInc);
        //		}
        //		
        //		BigDecimal minExc = (BigDecimal) simple.getMinExclusiveFacet().getValue();
        //		if (decimal.compareTo(minExc) <= 0) {
        //			throw new ValidationException("Decimal value is outside the exclusive min bounds of " + minExc);
        //		}

        //		int precision = decimal.precision();
        //		int totalDigits = simple.getTotalDigitsFacet().getValue();
        //		if (precision > totalDigits) {
        //			throw new ValidationException("Decimal value's precision ("+precision+
        //					") is higher than allowed ("+totalDigits+")");
        //		}
        return decimal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public String encode(Object object, String value) {
        BigDecimal decimal = (BigDecimal) object;

        return decimal.toString();
    }
}
