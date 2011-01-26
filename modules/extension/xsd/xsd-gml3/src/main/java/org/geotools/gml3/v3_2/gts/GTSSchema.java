package org.geotools.gml3.v3_2.gts;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.feature.type.SchemaImpl;

import org.geotools.xs.XSSchema;
import org.geotools.xlink.XLINKSchema;

public class GTSSchema extends SchemaImpl {

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="TM_Primitive_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractTimePrimitive"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectReference"/&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TM_PRIMITIVE_PROPERTYTYPE_TYPE = build_TM_PRIMITIVE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_TM_PRIMITIVE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gts","TM_Primitive_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="TM_PeriodDuration_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gts:TM_PeriodDuration"/&gt;
     *      &lt;/xs:sequence&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TM_PERIODDURATION_PROPERTYTYPE_TYPE = build_TM_PERIODDURATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_TM_PERIODDURATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gts","TM_PeriodDuration_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }


    public GTSSchema() {
        super("http://www.isotc211.org/2005/gts");

        put(new NameImpl("http://www.isotc211.org/2005/gts","TM_Primitive_PropertyType"),TM_PRIMITIVE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gts","TM_PeriodDuration_PropertyType"),TM_PERIODDURATION_PROPERTYTYPE_TYPE);
    }
    
}