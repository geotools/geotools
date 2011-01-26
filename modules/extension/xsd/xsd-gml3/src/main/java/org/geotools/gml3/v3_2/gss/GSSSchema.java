package org.geotools.gml3.v3_2.gss;


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

public class GSSSchema extends SchemaImpl {

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="GM_Point_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:Point"/&gt;
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
    public static final ComplexType GM_POINT_PROPERTYTYPE_TYPE = build_GM_POINT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_GM_POINT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gss","GM_Point_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="GM_Object_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractGeometry"/&gt;
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
    public static final ComplexType GM_OBJECT_PROPERTYTYPE_TYPE = build_GM_OBJECT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_GM_OBJECT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gss","GM_Object_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }


    public GSSSchema() {
        super("http://www.isotc211.org/2005/gss");

        put(new NameImpl("http://www.isotc211.org/2005/gss","GM_Point_PropertyType"),GM_POINT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gss","GM_Object_PropertyType"),GM_OBJECT_PROPERTYTYPE_TYPE);
    }
    
}