package org.geotools.gml3.v3_2.gco;


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

public class GCOSchema extends SchemaImpl {

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Measure_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Measure"/&gt;
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
    public static final ComplexType MEASURE_PROPERTYTYPE_TYPE = build_MEASURE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MEASURE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Measure_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Integer_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Integer"/&gt;
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
    public static final ComplexType INTEGER_PROPERTYTYPE_TYPE = build_INTEGER_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_INTEGER_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Integer_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CharacterString_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:CharacterString"/&gt;
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
    public static final ComplexType CHARACTERSTRING_PROPERTYTYPE_TYPE = build_CHARACTERSTRING_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CHARACTERSTRING_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","CharacterString_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Number_PropertyType"&gt;
     *      &lt;xs:choice minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Real"/&gt;
     *          &lt;xs:element ref="gco:Decimal"/&gt;
     *          &lt;xs:element ref="gco:Integer"/&gt;
     *      &lt;/xs:choice&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType NUMBER_PROPERTYTYPE_TYPE = build_NUMBER_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_NUMBER_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Number_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ObjectReference_PropertyType"&gt;
     *      &lt;xs:sequence/&gt;
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
    public static final ComplexType OBJECTREFERENCE_PROPERTYTYPE_TYPE = build_OBJECTREFERENCE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_OBJECTREFERENCE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","ObjectReference_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractObject_Type"&gt;
     *      &lt;xs:sequence/&gt;
     *      &lt;xs:attributeGroup ref="gco:ObjectIdentification"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTOBJECT_TYPE_TYPE = build_ABSTRACTOBJECT_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTOBJECT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","AbstractObject_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MultiplicityRange_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;A component of a multiplicity, consisting of an non-negative lower bound, and a potentially infinite upper bound.&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="lower" type="gco:Integer_PropertyType"/&gt;
     *                  &lt;xs:element name="upper" type="gco:UnlimitedInteger_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MULTIPLICITYRANGE_TYPE_TYPE = build_MULTIPLICITYRANGE_TYPE_TYPE();
    
    private static ComplexType build_MULTIPLICITYRANGE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","MultiplicityRange_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="TypeName_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:TypeName"/&gt;
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
    public static final ComplexType TYPENAME_PROPERTYTYPE_TYPE = build_TYPENAME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_TYPENAME_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","TypeName_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MultiplicityRange_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:MultiplicityRange"/&gt;
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
    public static final ComplexType MULTIPLICITYRANGE_PROPERTYTYPE_TYPE = build_MULTIPLICITYRANGE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MULTIPLICITYRANGE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","MultiplicityRange_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Date_PropertyType"&gt;
     *      &lt;xs:choice minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Date"/&gt;
     *          &lt;xs:element ref="gco:DateTime"/&gt;
     *      &lt;/xs:choice&gt;
     *      &lt;xs:attribute ref="gco:nilReason"/&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DATE_PROPERTYTYPE_TYPE = build_DATE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DATE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Date_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomAngle_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;!--xs:element ref="gml:UnitDefinition"/--&gt;
     *          &lt;xs:element name="UnitDefinition" type="xs:anyType"/&gt;
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
    public static final ComplexType UOMANGLE_PROPERTYTYPE_TYPE = build_UOMANGLE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UOMANGLE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UomAngle_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomArea_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;!--xs:element ref="gml:UnitDefinition"/--&gt;
     *          &lt;xs:element name="UnitDefinition" type="xs:anyType"/&gt;
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
    public static final ComplexType UOMAREA_PROPERTYTYPE_TYPE = build_UOMAREA_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UOMAREA_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UomArea_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MemberName_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;A MemberName is a LocalName that references either an attribute slot in a record or  recordType or an attribute, operation, or association role in an object instance or  type description in some form of schema. The stored value "aName" is the returned value for the "aName()" operation.&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="aName" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="attributeType" type="gco:TypeName_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MEMBERNAME_TYPE_TYPE = build_MEMBERNAME_TYPE_TYPE();
    
    private static ComplexType build_MEMBERNAME_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","MemberName_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LocalName_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:LocalName"/&gt;
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
    public static final ComplexType LOCALNAME_PROPERTYTYPE_TYPE = build_LOCALNAME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_LOCALNAME_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","LocalName_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="RecordType_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:RecordType"/&gt;
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
    public static final ComplexType RECORDTYPE_PROPERTYTYPE_TYPE = build_RECORDTYPE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_RECORDTYPE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","RecordType_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Scale_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Scale"/&gt;
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
    public static final ComplexType SCALE_PROPERTYTYPE_TYPE = build_SCALE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_SCALE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Scale_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeListValue_Type"&gt;
     *      &lt;xs:simpleContent&gt;
     *          &lt;xs:extension base="xs:string"&gt;
     *              &lt;xs:attribute name="codeList" type="xs:anyURI" use="required"/&gt;
     *              &lt;xs:attribute name="codeListValue" type="xs:anyURI" use="required"/&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:simpleContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODELISTVALUE_TYPE_TYPE = build_CODELISTVALUE_TYPE_TYPE();
    
    private static ComplexType build_CODELISTVALUE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","CodeListValue_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Distance_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Distance"/&gt;
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
    public static final ComplexType DISTANCE_PROPERTYTYPE_TYPE = build_DISTANCE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DISTANCE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Distance_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="Date_Type"&gt;
     *      &lt;xs:union memberTypes="xs:date xs:gYearMonth xs:gYear"/&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType DATE_TYPE_TYPE = build_DATE_TYPE_TYPE();
     
    private static AttributeType build_DATE_TYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Date_Type"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYSIMPLETYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Angle_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Angle"/&gt;
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
    public static final ComplexType ANGLE_PROPERTYTYPE_TYPE = build_ANGLE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ANGLE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Angle_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomScale_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;!--xs:element ref="gml:UnitDefinition"/--&gt;
     *          &lt;xs:element name="UnitDefinition" type="xs:anyType"/&gt;
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
    public static final ComplexType UOMSCALE_PROPERTYTYPE_TYPE = build_UOMSCALE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UOMSCALE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UomScale_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomVolume_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;!--xs:element ref="gml:UnitDefinition"/--&gt;
     *          &lt;xs:element name="UnitDefinition" type="xs:anyType"/&gt;
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
    public static final ComplexType UOMVOLUME_PROPERTYTYPE_TYPE = build_UOMVOLUME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UOMVOLUME_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UomVolume_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Length_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Length"/&gt;
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
    public static final ComplexType LENGTH_PROPERTYTYPE_TYPE = build_LENGTH_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_LENGTH_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Length_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UnlimitedInteger_Type"&gt;
     *      &lt;xs:simpleContent&gt;
     *          &lt;xs:extension base="xs:nonNegativeInteger"&gt;
     *              &lt;xs:attribute name="isInfinite" type="xs:boolean"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:simpleContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType UNLIMITEDINTEGER_TYPE_TYPE = build_UNLIMITEDINTEGER_TYPE_TYPE();
    
    private static ComplexType build_UNLIMITEDINTEGER_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UnlimitedInteger_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.NONNEGATIVEINTEGER_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="GenericName_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:AbstractGenericName"/&gt;
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
    public static final ComplexType GENERICNAME_PROPERTYTYPE_TYPE = build_GENERICNAME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_GENERICNAME_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","GenericName_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="TypeName_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;A TypeName is a LocalName that references either a recordType or object type in some form of schema. The stored value "aName" is the returned value for the "aName()" operation. This is the types name.  - For parsing from types (or objects) the parsible name normally uses a "." navigation separator, so that it is of the form  [class].[member].[memberOfMember]. ...)&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="aName" type="gco:CharacterString_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType TYPENAME_TYPE_TYPE = build_TYPENAME_TYPE_TYPE();
    
    private static ComplexType build_TYPENAME_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","TypeName_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ScopedName_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:ScopedName"/&gt;
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
    public static final ComplexType SCOPEDNAME_PROPERTYTYPE_TYPE = build_SCOPEDNAME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_SCOPEDNAME_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","ScopedName_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomVelocity_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;!--xs:element ref="gml:UnitDefinition"/--&gt;
     *          &lt;xs:element name="UnitDefinition" type="xs:anyType"/&gt;
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
    public static final ComplexType UOMVELOCITY_PROPERTYTYPE_TYPE = build_UOMVELOCITY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UOMVELOCITY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UomVelocity_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomTime_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;!--xs:element ref="gml:UnitDefinition"/--&gt;
     *          &lt;xs:element name="UnitDefinition" type="xs:anyType"/&gt;
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
    public static final ComplexType UOMTIME_PROPERTYTYPE_TYPE = build_UOMTIME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UOMTIME_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UomTime_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Multiplicity_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Multiplicity"/&gt;
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
    public static final ComplexType MULTIPLICITY_PROPERTYTYPE_TYPE = build_MULTIPLICITY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MULTIPLICITY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Multiplicity_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DateTime_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:DateTime"/&gt;
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
    public static final ComplexType DATETIME_PROPERTYTYPE_TYPE = build_DATETIME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DATETIME_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","DateTime_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Record_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Record"/&gt;
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
    public static final ComplexType RECORD_PROPERTYTYPE_TYPE = build_RECORD_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_RECORD_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Record_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UnitOfMeasure_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;!--xs:element ref="gml:UnitDefinition"/--&gt;
     *          &lt;xs:element name="UnitDefinition" type="xs:anyType"/&gt;
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
    public static final ComplexType UNITOFMEASURE_PROPERTYTYPE_TYPE = build_UNITOFMEASURE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UNITOFMEASURE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UnitOfMeasure_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Binary_Type"&gt;
     *      &lt;xs:simpleContent&gt;
     *          &lt;xs:extension base="xs:string"&gt;
     *              &lt;xs:attribute name="src" type="xs:anyURI"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:simpleContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType BINARY_TYPE_TYPE = build_BINARY_TYPE_TYPE();
    
    private static ComplexType build_BINARY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Binary_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Real_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Real"/&gt;
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
    public static final ComplexType REAL_PROPERTYTYPE_TYPE = build_REAL_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_REAL_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Real_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UnlimitedInteger_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:UnlimitedInteger"/&gt;
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
    public static final ComplexType UNLIMITEDINTEGER_PROPERTYTYPE_TYPE = build_UNLIMITEDINTEGER_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UNLIMITEDINTEGER_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UnlimitedInteger_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="RecordType_Type"&gt;
     *      &lt;xs:simpleContent&gt;
     *          &lt;xs:extension base="xs:string"&gt;
     *              &lt;xs:attributeGroup ref="xlink:simpleLink"/&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:simpleContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType RECORDTYPE_TYPE_TYPE = build_RECORDTYPE_TYPE_TYPE();
    
    private static ComplexType build_RECORDTYPE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","RecordType_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Decimal_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Decimal"/&gt;
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
    public static final ComplexType DECIMAL_PROPERTYTYPE_TYPE = build_DECIMAL_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DECIMAL_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Decimal_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MemberName_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:MemberName"/&gt;
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
    public static final ComplexType MEMBERNAME_PROPERTYTYPE_TYPE = build_MEMBERNAME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MEMBERNAME_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","MemberName_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Boolean_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Boolean"/&gt;
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
    public static final ComplexType BOOLEAN_PROPERTYTYPE_TYPE = build_BOOLEAN_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_BOOLEAN_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Boolean_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomLength_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;!--xs:element ref="gml:UnitDefinition"/--&gt;
     *          &lt;xs:element name="UnitDefinition" type="xs:anyType"/&gt;
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
    public static final ComplexType UOMLENGTH_PROPERTYTYPE_TYPE = build_UOMLENGTH_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UOMLENGTH_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","UomLength_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Binary_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gco:Binary"/&gt;
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
    public static final ComplexType BINARY_PROPERTYTYPE_TYPE = build_BINARY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_BINARY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Binary_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Multiplicity_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Use to represent the possible cardinality of a relation. Represented by a set of simple multiplicity ranges.&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="range" type="gco:MultiplicityRange_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *          &lt;/xs:extension&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MULTIPLICITY_TYPE_TYPE = build_MULTIPLICITY_TYPE_TYPE();
    
    private static ComplexType build_MULTIPLICITY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gco","Multiplicity_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }


    public GCOSchema() {
        super("http://www.isotc211.org/2005/gco");

        put(new NameImpl("http://www.isotc211.org/2005/gco","Measure_PropertyType"),MEASURE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Integer_PropertyType"),INTEGER_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","CharacterString_PropertyType"),CHARACTERSTRING_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Number_PropertyType"),NUMBER_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","ObjectReference_PropertyType"),OBJECTREFERENCE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","AbstractObject_Type"),ABSTRACTOBJECT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","MultiplicityRange_Type"),MULTIPLICITYRANGE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","TypeName_PropertyType"),TYPENAME_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","MultiplicityRange_PropertyType"),MULTIPLICITYRANGE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Date_PropertyType"),DATE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UomAngle_PropertyType"),UOMANGLE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UomArea_PropertyType"),UOMAREA_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","MemberName_Type"),MEMBERNAME_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","LocalName_PropertyType"),LOCALNAME_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","RecordType_PropertyType"),RECORDTYPE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Scale_PropertyType"),SCALE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","CodeListValue_Type"),CODELISTVALUE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Distance_PropertyType"),DISTANCE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Date_Type"),DATE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Angle_PropertyType"),ANGLE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UomScale_PropertyType"),UOMSCALE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UomVolume_PropertyType"),UOMVOLUME_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Length_PropertyType"),LENGTH_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UnlimitedInteger_Type"),UNLIMITEDINTEGER_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","GenericName_PropertyType"),GENERICNAME_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","TypeName_Type"),TYPENAME_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","ScopedName_PropertyType"),SCOPEDNAME_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UomVelocity_PropertyType"),UOMVELOCITY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UomTime_PropertyType"),UOMTIME_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Multiplicity_PropertyType"),MULTIPLICITY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","DateTime_PropertyType"),DATETIME_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Record_PropertyType"),RECORD_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UnitOfMeasure_PropertyType"),UNITOFMEASURE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Binary_Type"),BINARY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Real_PropertyType"),REAL_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UnlimitedInteger_PropertyType"),UNLIMITEDINTEGER_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","RecordType_Type"),RECORDTYPE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Decimal_PropertyType"),DECIMAL_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","MemberName_PropertyType"),MEMBERNAME_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Boolean_PropertyType"),BOOLEAN_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","UomLength_PropertyType"),UOMLENGTH_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Binary_PropertyType"),BINARY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gco","Multiplicity_Type"),MULTIPLICITY_TYPE_TYPE);
    }
    
}