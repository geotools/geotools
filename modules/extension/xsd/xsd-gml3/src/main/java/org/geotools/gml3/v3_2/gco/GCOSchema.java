package org.geotools.gml3.v3_2.gco;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AbstractLazyAttributeTypeImpl;
import org.geotools.feature.type.AbstractLazyComplexTypeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.gml3.v3_2.GMLSchema;
import org.geotools.xlink.XLINKSchema;
import org.geotools.xs.XSSchema;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.Schema;

/**
 * 
 *
 * @source $URL$
 */
public class GCOSchema extends SchemaImpl {

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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","AbstractObject_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ID_TYPE,
                        new NameImpl("id"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuid"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Angle_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.ANGLETYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Angle"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Binary_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        BINARY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Binary"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Binary_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.STRING_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("src"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Boolean_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.BOOLEAN_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Boolean"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","CharacterString_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","CharacterString"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","CodeListValue_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.STRING_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeList"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeListValue"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("codeSpace"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","DateTime_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.DATETIME_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","DateTime"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Date_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DATE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Date"),
                        1, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.DATETIME_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","DateTime"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        AttributeType builtType = new AbstractLazyAttributeTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Date_Type"),
                java.lang.Object.class, false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYSIMPLETYPE_TYPE;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Decimal_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.DECIMAL_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Decimal"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Distance_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.LENGTHTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Distance"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","GenericName_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.CODETYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","AbstractGenericName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Integer_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.INTEGER_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Integer"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Length_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.LENGTHTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Length"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","LocalName_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.CODETYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","LocalName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Measure_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.MEASURETYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Measure"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","MemberName_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MEMBERNAME_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","MemberName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","MemberName_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","aName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        TYPENAME_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","attributeType"),
                        1, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","MultiplicityRange_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MULTIPLICITYRANGE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","MultiplicityRange"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","MultiplicityRange_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","lower"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        UNLIMITEDINTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","upper"),
                        1, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Multiplicity_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MULTIPLICITY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Multiplicity"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Multiplicity_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MULTIPLICITYRANGE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","range"),
                        1, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Number_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.DOUBLE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Real"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.DECIMAL_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Decimal"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.INTEGER_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Integer"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","ObjectReference_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Real_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.DOUBLE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Real"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","RecordType_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        RECORDTYPE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","RecordType"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","RecordType_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.STRING_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Record_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Record"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","Scale_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.SCALETYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","Scale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","ScopedName_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.CODETYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","ScopedName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","TypeName_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        TYPENAME_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","TypeName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","TypeName_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","aName"),
                        1, 1, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UnitOfMeasure_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:UnitDefinition"/&gt;
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UnitOfMeasure_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.UNITDEFINITIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UnlimitedInteger_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        UNLIMITEDINTEGER_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","UnlimitedInteger"),
                        1, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UnlimitedInteger_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.NONNEGATIVEINTEGER_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.BOOLEAN_TYPE,
                        new NameImpl("isInfinite"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomAngle_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:UnitDefinition"/&gt;
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UomAngle_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.UNITDEFINITIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomArea_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:UnitDefinition"/&gt;
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UomArea_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.UNITDEFINITIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomLength_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:UnitDefinition"/&gt;
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UomLength_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.UNITDEFINITIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomScale_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:UnitDefinition"/&gt;
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UomScale_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.UNITDEFINITIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomTime_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:UnitDefinition"/&gt;
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UomTime_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.UNITDEFINITIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomVelocity_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:UnitDefinition"/&gt;
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UomVelocity_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.UNITDEFINITIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomVolume_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:UnitDefinition"/&gt;
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gco","UomVolume_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.ANYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.UNITDEFINITIONTYPE_TYPE,
                        new NameImpl("http://www.opengis.net/gml/3.2","UnitDefinition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._ACTUATE_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","actuate"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","arcrole"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","href"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","role"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XLINKSchema._SHOW_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","show"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","title"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("http://www.w3.org/1999/xlink","type"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.STRING_TYPE,
                        new NameImpl("uuidref"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GMLSchema.NILREASONTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gco","nilReason"),
                        0, 1, true, null));
                return descriptors;
            }
        };
        return builtType;
    }


    public GCOSchema() {
        super("http://www.isotc211.org/2005/gco");
        put(ABSTRACTOBJECT_TYPE_TYPE);
        put(ANGLE_PROPERTYTYPE_TYPE);
        put(BINARY_PROPERTYTYPE_TYPE);
        put(BINARY_TYPE_TYPE);
        put(BOOLEAN_PROPERTYTYPE_TYPE);
        put(CHARACTERSTRING_PROPERTYTYPE_TYPE);
        put(CODELISTVALUE_TYPE_TYPE);
        put(DATETIME_PROPERTYTYPE_TYPE);
        put(DATE_PROPERTYTYPE_TYPE);
        put(DATE_TYPE_TYPE);
        put(DECIMAL_PROPERTYTYPE_TYPE);
        put(DISTANCE_PROPERTYTYPE_TYPE);
        put(GENERICNAME_PROPERTYTYPE_TYPE);
        put(INTEGER_PROPERTYTYPE_TYPE);
        put(LENGTH_PROPERTYTYPE_TYPE);
        put(LOCALNAME_PROPERTYTYPE_TYPE);
        put(MEASURE_PROPERTYTYPE_TYPE);
        put(MEMBERNAME_PROPERTYTYPE_TYPE);
        put(MEMBERNAME_TYPE_TYPE);
        put(MULTIPLICITYRANGE_PROPERTYTYPE_TYPE);
        put(MULTIPLICITYRANGE_TYPE_TYPE);
        put(MULTIPLICITY_PROPERTYTYPE_TYPE);
        put(MULTIPLICITY_TYPE_TYPE);
        put(NUMBER_PROPERTYTYPE_TYPE);
        put(OBJECTREFERENCE_PROPERTYTYPE_TYPE);
        put(REAL_PROPERTYTYPE_TYPE);
        put(RECORDTYPE_PROPERTYTYPE_TYPE);
        put(RECORDTYPE_TYPE_TYPE);
        put(RECORD_PROPERTYTYPE_TYPE);
        put(SCALE_PROPERTYTYPE_TYPE);
        put(SCOPEDNAME_PROPERTYTYPE_TYPE);
        put(TYPENAME_PROPERTYTYPE_TYPE);
        put(TYPENAME_TYPE_TYPE);
        put(UNITOFMEASURE_PROPERTYTYPE_TYPE);
        put(UNLIMITEDINTEGER_PROPERTYTYPE_TYPE);
        put(UNLIMITEDINTEGER_TYPE_TYPE);
        put(UOMANGLE_PROPERTYTYPE_TYPE);
        put(UOMAREA_PROPERTYTYPE_TYPE);
        put(UOMLENGTH_PROPERTYTYPE_TYPE);
        put(UOMSCALE_PROPERTYTYPE_TYPE);
        put(UOMTIME_PROPERTYTYPE_TYPE);
        put(UOMVELOCITY_PROPERTYTYPE_TYPE);
        put(UOMVOLUME_PROPERTYTYPE_TYPE);
    }

    /**
     * Complete the definition of a type and store it in the schema.
     * 
     * <p>
     * 
     * This method calls {@link AttributeType#getSuper()} (and {@link ComplexType#getDescriptors()}
     * where applicable) to ensure the construction of the type (a concrete
     * {@link AbstractLazyAttributeTypeImpl} or {@link AbstractLazyComplexTypeImpl} sublass) is
     * complete. This should be sufficient to avoid any nasty thread-safety surprises in code using
     * this schema.
     * 
     * @param type
     *            the type to complete and store
     */
    private void put(AttributeType type) {
        type.getSuper();
        if (type instanceof ComplexType) {
            ((ComplexType) type).getDescriptors();
        }
        put(type.getName(), type);
    }

    /**
     * Test that this class can be loaded.
     */
    public static void main(String[] args) {
        Schema schema = new GCOSchema();
        for (Entry<Name, AttributeType> entry : new TreeMap<Name, AttributeType>(schema).entrySet()) {
            System.out.println("Type: " + entry.getValue().getName());
            System.out.println("    Super type: " + entry.getValue().getSuper().getName());
            if (entry.getValue() instanceof ComplexType) {
                for (PropertyDescriptor descriptor : ((ComplexType) entry.getValue())
                        .getDescriptors()) {
                    System.out.println("    Property descriptor: " + descriptor.getName());
                    System.out.println("        Property type: " + descriptor.getType().getName());
                }
            }
        }
    }

}
