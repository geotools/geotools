package org.geotools.gml3.v3_2.gmx;


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

import org.geotools.gml3.v3_2.gts.GTSSchema;
import org.geotools.gml3.v3_2.gco.GCOSchema;
import org.geotools.xs.XSSchema;
import org.geotools.gml3.v3_2.GMLSchema;
import org.geotools.gml3.v3_2.gsr.GSRSchema;
import org.geotools.gml3.v3_2.gmd.GMDSchema;
import org.geotools.xlink.XLINKSchema;

public class GMXSchema extends SchemaImpl {

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DatumAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:AbstractDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType DATUMALT_TYPE_TYPE = build_DATUMALT_TYPE_TYPE();
    
    private static ComplexType build_DATUMALT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","DatumAlt_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.ABSTRACTDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CylindricalCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CylindricalCS"/&gt;
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
    public static final ComplexType ML_CYLINDRICALCS_PROPERTYTYPE_TYPE = build_ML_CYLINDRICALCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CYLINDRICALCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CylindricalCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_PrimeMeridian_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:PrimeMeridian"/&gt;
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
    public static final ComplexType CT_PRIMEMERIDIAN_PROPERTYTYPE_TYPE = build_CT_PRIMEMERIDIAN_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_PRIMEMERIDIAN_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_PrimeMeridian_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:VerticalCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_VERTICALCS_TYPE_TYPE = build_ML_VERTICALCS_TYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALCS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.VERTICALCSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_GeodeticCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:GeodeticCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_GEODETICCRS_TYPE_TYPE = build_ML_GEODETICCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_GEODETICCRS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.GEODETICCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CylindricalCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CylindricalCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_CYLINDRICALCS_TYPE_TYPE = build_ML_CYLINDRICALCS_TYPE_TYPE();
    
    private static ComplexType build_ML_CYLINDRICALCS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CylindricalCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.CYLINDRICALCSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PrimeMeridianAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType PRIMEMERIDIANALT_TYPE_TYPE = build_PRIMEMERIDIANALT_TYPE_TYPE();
    
    private static ComplexType build_PRIMEMERIDIANALT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ConventionalUnit_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ConventionalUnitType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:UomAlternativeExpression_PropertyType"/&gt;
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
    public static final ComplexType ML_CONVENTIONALUNIT_TYPE_TYPE = build_ML_CONVENTIONALUNIT_TYPE_TYPE();
    
    private static ComplexType build_ML_CONVENTIONALUNIT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.CONVENTIONALUNITTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ImageCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ImageCRS"/&gt;
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
    public static final ComplexType ML_IMAGECRS_PROPERTYTYPE_TYPE = build_ML_IMAGECRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_IMAGECRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageCRS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_SphericalCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:SphericalCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_SPHERICALCS_TYPE_TYPE = build_ML_SPHERICALCS_TYPE_TYPE();
    
    private static ComplexType build_ML_SPHERICALCS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_SphericalCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.SPHERICALCSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CartesianCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CartesianCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_CARTESIANCS_TYPE_TYPE = build_ML_CARTESIANCS_TYPE_TYPE();
    
    private static ComplexType build_ML_CARTESIANCS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CartesianCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.CARTESIANCSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TemporalDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_TemporalDatum"/&gt;
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
    public static final ComplexType ML_TEMPORALDATUM_PROPERTYTYPE_TYPE = build_ML_TEMPORALDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_TEMPORALDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalDatum_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PassThroughOperation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_PassThroughOperation"/&gt;
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
    public static final ComplexType ML_PASSTHROUGHOPERATION_PROPERTYTYPE_TYPE = build_ML_PASSTHROUGHOPERATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_PASSTHROUGHOPERATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TemporalDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:TemporalDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_TEMPORALDATUM_TYPE_TYPE = build_ML_TEMPORALDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_TEMPORALDATUM_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalDatum_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.TEMPORALDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CompoundCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CompoundCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_COMPOUNDCRS_TYPE_TYPE = build_ML_COMPOUNDCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_COMPOUNDCRS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CompoundCRS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.COMPOUNDCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CrsAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CrsAlt"/&gt;
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
    public static final ComplexType CRSALT_PROPERTYTYPE_TYPE = build_CRSALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CRSALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CrsAlt_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:AbstractCoordinateOperationType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType OPERATIONALT_TYPE_TYPE = build_OPERATIONALT_TYPE_TYPE();
    
    private static ComplexType build_OPERATIONALT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","OperationAlt_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.ABSTRACTCOORDINATEOPERATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomAlternativeExpression_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:UnitDefinitionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType UOMALTERNATIVEEXPRESSION_TYPE_TYPE = build_UOMALTERNATIVEEXPRESSION_TYPE_TYPE();
    
    private static ComplexType build_UOMALTERNATIVEEXPRESSION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","UomAlternativeExpression_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.UNITDEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationParameter_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:OperationParameterType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationParameterAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_OPERATIONPARAMETER_TYPE_TYPE = build_ML_OPERATIONPARAMETER_TYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONPARAMETER_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameter_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.OPERATIONPARAMETERTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PolarCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_PolarCS"/&gt;
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
    public static final ComplexType ML_POLARCS_PROPERTYTYPE_TYPE = build_ML_POLARCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_POLARCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_PolarCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeAlternativeExpression_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DefinitionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType CODEALTERNATIVEEXPRESSION_TYPE_TYPE = build_CODEALTERNATIVEEXPRESSION_TYPE_TYPE();
    
    private static ComplexType build_CODEALTERNATIVEEXPRESSION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:VerticalDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_VERTICALDATUM_TYPE_TYPE = build_ML_VERTICALDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALDATUM_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalDatum_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.VERTICALDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CrsCatalogue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CT_CrsCatalogue"/&gt;
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
    public static final ComplexType CT_CRSCATALOGUE_PROPERTYTYPE_TYPE = build_CT_CRSCATALOGUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CRSCATALOGUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ProjectedCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ProjectedCRS"/&gt;
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
    public static final ComplexType ML_PROJECTEDCRS_PROPERTYTYPE_TYPE = build_ML_PROJECTEDCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_PROJECTEDCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EllipsoidalCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:EllipsoidalCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_ELLIPSOIDALCS_TYPE_TYPE = build_ML_ELLIPSOIDALCS_TYPE_TYPE();
    
    private static ComplexType build_ML_ELLIPSOIDALCS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.ELLIPSOIDALCSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CoordinateSystemAxis_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CoordinateSystemAxisType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAxisAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_COORDINATESYSTEMAXIS_TYPE_TYPE = build_ML_COORDINATESYSTEMAXIS_TYPE_TYPE();
    
    private static ComplexType build_ML_COORDINATESYSTEMAXIS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.COORDINATESYSTEMAXISTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EllipsoidAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:EllipsoidAlt"/&gt;
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
    public static final ComplexType ELLIPSOIDALT_PROPERTYTYPE_TYPE = build_ELLIPSOIDALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ELLIPSOIDALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","EllipsoidAlt_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractCT_Catalogue_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="name" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="scope" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="fieldOfApplication" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="versionNumber" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="versionDate" type="gco:Date_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="language" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="characterSet" type="gmd:MD_CharacterSetCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="subCatalogue" type="gmx:CT_Catalogue_PropertyType"/&gt;
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
    public static final ComplexType ABSTRACTCT_CATALOGUE_TYPE_TYPE = build_ABSTRACTCT_CATALOGUE_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTCT_CATALOGUE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","AbstractCT_Catalogue_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CrsCatalogue_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractCT_Catalogue_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="crs" type="gmx:CT_CRS_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="coordinateSystem" type="gmx:CT_CoordinateSystem_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="axis" type="gmx:CT_CoordinateSystemAxis_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="datum" type="gmx:CT_Datum_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="ellipsoid" type="gmx:CT_Ellipsoid_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="primeMeridian" type="gmx:CT_PrimeMeridian_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="operation" type="gmx:CT_Operation_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="operationMethod" type="gmx:CT_OperationMethod_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="parameters" type="gmx:CT_OperationParameters_PropertyType"/&gt;
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
    public static final ComplexType CT_CRSCATALOGUE_TYPE_TYPE = build_CT_CRSCATALOGUE_TYPE_TYPE();
    
    private static ComplexType build_CT_CRSCATALOGUE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCT_CATALOGUE_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationMethod_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_OperationMethod"/&gt;
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
    public static final ComplexType ML_OPERATIONMETHOD_PROPERTYTYPE_TYPE = build_ML_OPERATIONMETHOD_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONMETHOD_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationMethod_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="BaseUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:BaseUnit"/&gt;
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
    public static final ComplexType BASEUNIT_PROPERTYTYPE_TYPE = build_BASEUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_BASEUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","BaseUnit_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ConventionalUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ConventionalUnit"/&gt;
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
    public static final ComplexType ML_CONVENTIONALUNIT_PROPERTYTYPE_TYPE = build_ML_CONVENTIONALUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CONVENTIONALUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UomAlternativeExpression_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:UomAlternativeExpression"/&gt;
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
    public static final ComplexType UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE = build_UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","UomAlternativeExpression_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:VerticalCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_VERTICALCRS_TYPE_TYPE = build_ML_VERTICALCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALCRS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCRS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.VERTICALCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_VerticalCS"/&gt;
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
    public static final ComplexType ML_VERTICALCS_PROPERTYTYPE_TYPE = build_ML_VERTICALCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Ellipsoid_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:EllipsoidType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:EllipsoidAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_ELLIPSOID_TYPE_TYPE = build_ML_ELLIPSOID_TYPE_TYPE();
    
    private static ComplexType build_ML_ELLIPSOID_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_Ellipsoid_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.ELLIPSOIDTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CodelistValue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeDefinition"/&gt;
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
    public static final ComplexType CT_CODELISTVALUE_PROPERTYTYPE_TYPE = build_CT_CODELISTVALUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CODELISTVALUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistValue_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_VerticalDatum"/&gt;
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
    public static final ComplexType ML_VERTICALDATUM_PROPERTYTYPE_TYPE = build_ML_VERTICALDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalDatum_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CodelistCatalogue_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractCT_Catalogue_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="codelistItem" type="gmx:CT_Codelist_PropertyType"/&gt;
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
    public static final ComplexType CT_CODELISTCATALOGUE_TYPE_TYPE = build_CT_CODELISTCATALOGUE_TYPE_TYPE();
    
    private static ComplexType build_CT_CODELISTCATALOGUE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCT_CATALOGUE_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Conversion_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ConversionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_CONVERSION_TYPE_TYPE = build_ML_CONVERSION_TYPE_TYPE();
    
    private static ComplexType build_ML_CONVERSION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_Conversion_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.CONVERSIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_UserDefinedCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_UserDefinedCS"/&gt;
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
    public static final ComplexType ML_USERDEFINEDCS_PROPERTYTYPE_TYPE = build_ML_USERDEFINEDCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_USERDEFINEDCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_DerivedCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_DerivedCRS"/&gt;
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
    public static final ComplexType ML_DERIVEDCRS_PROPERTYTYPE_TYPE = build_ML_DERIVEDCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_DERIVEDCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedCRS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EngineeringDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:EngineeringDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_ENGINEERINGDATUM_TYPE_TYPE = build_ML_ENGINEERINGDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_ENGINEERINGDATUM_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.ENGINEERINGDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationMethodAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:OperationMethodAlt"/&gt;
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
    public static final ComplexType OPERATIONMETHODALT_PROPERTYTYPE_TYPE = build_OPERATIONMETHODALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONMETHODALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","OperationMethodAlt_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractCRS"/&gt;
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
    public static final ComplexType CT_CRS_PROPERTYTYPE_TYPE = build_CT_CRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_CRS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_DataSet_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:DS_DataSet_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="dataFile" type="gmx:MX_DataFile_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="datasetCatalogue" type="gmx:CT_Catalogue_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="supportFile" type="gmx:MX_SupportFile_PropertyType"/&gt;
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
    public static final ComplexType MX_DATASET_TYPE_TYPE = build_MX_DATASET_TYPE_TYPE();
    
    private static ComplexType build_MX_DATASET_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataSet_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMDSchema.DS_DATASET_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CodeDefinition_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CodeDefinition"/&gt;
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
    public static final ComplexType ML_CODEDEFINITION_PROPERTYTYPE_TYPE = build_ML_CODEDEFINITION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CODEDEFINITION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeDefinition_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CrsAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:AbstractCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType CRSALT_TYPE_TYPE = build_CRSALT_TYPE_TYPE();
    
    private static ComplexType build_CRSALT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CrsAlt_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.ABSTRACTCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeListDictionary_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeListDictionary"/&gt;
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
    public static final ComplexType CODELISTDICTIONARY_PROPERTYTYPE_TYPE = build_CODELISTDICTIONARY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CODELISTDICTIONARY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CodeListDictionary_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PassThroughOperation_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:PassThroughOperationType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_PASSTHROUGHOPERATION_TYPE_TYPE = build_ML_PASSTHROUGHOPERATION_TYPE_TYPE();
    
    private static ComplexType build_ML_PASSTHROUGHOPERATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.PASSTHROUGHOPERATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Anchor_Type"&gt;
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
    public static final ComplexType ANCHOR_TYPE_TYPE = build_ANCHOR_TYPE_TYPE();
    
    private static ComplexType build_ANCHOR_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","Anchor_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ProjectedCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ProjectedCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_PROJECTEDCRS_TYPE_TYPE = build_ML_PROJECTEDCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_PROJECTEDCRS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.PROJECTEDCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Datum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractDatum"/&gt;
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
    public static final ComplexType CT_DATUM_PROPERTYTYPE_TYPE = build_CT_DATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_DATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_Datum_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EngineeringCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_EngineeringCRS"/&gt;
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
    public static final ComplexType ML_ENGINEERINGCRS_PROPERTYTYPE_TYPE = build_ML_ENGINEERINGCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_ENGINEERINGCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_UomCatalogue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CT_UomCatalogue"/&gt;
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
    public static final ComplexType CT_UOMCATALOGUE_PROPERTYTYPE_TYPE = build_CT_UOMCATALOGUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_UOMCATALOGUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_UomCatalogue_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_DerivedCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DerivedCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_DERIVEDCRS_TYPE_TYPE = build_ML_DERIVEDCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_DERIVEDCRS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedCRS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.DERIVEDCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_ScopeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_ScopeCode"/&gt;
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
    public static final ComplexType MX_SCOPECODE_PROPERTYTYPE_TYPE = build_MX_SCOPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_SCOPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_ScopeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Transformation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_Transformation"/&gt;
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
    public static final ComplexType ML_TRANSFORMATION_PROPERTYTYPE_TYPE = build_ML_TRANSFORMATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_TRANSFORMATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_Transformation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractMX_File_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="fileName" type="gmx:FileName_PropertyType"/&gt;
     *                  &lt;xs:element name="fileDescription" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="fileType" type="gmx:MimeFileType_PropertyType"/&gt;
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
    public static final ComplexType ABSTRACTMX_FILE_TYPE_TYPE = build_ABSTRACTMX_FILE_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTMX_FILE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","AbstractMX_File_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_DataFile_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractMX_File_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="featureTypes" type="gco:GenericName_PropertyType"/&gt;
     *                  &lt;xs:element name="fileFormat" type="gmd:MD_Format_PropertyType"/&gt;
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
    public static final ComplexType MX_DATAFILE_TYPE_TYPE = build_MX_DATAFILE_TYPE_TYPE();
    
    private static ComplexType build_MX_DATAFILE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataFile_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTMX_FILE_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CoordinateSystemAxisAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CoordinateSystemAxisAlt"/&gt;
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
    public static final ComplexType COORDINATESYSTEMAXISALT_PROPERTYTYPE_TYPE = build_COORDINATESYSTEMAXISALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMAXISALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_LinearCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:LinearCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_LINEARCS_TYPE_TYPE = build_ML_LINEARCS_TYPE_TYPE();
    
    private static ComplexType build_ML_LINEARCS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_LinearCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.LINEARCSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EllipsoidAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType ELLIPSOIDALT_TYPE_TYPE = build_ELLIPSOIDALT_TYPE_TYPE();
    
    private static ComplexType build_ELLIPSOIDALT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","EllipsoidAlt_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Anchor_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:Anchor"/&gt;
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
    public static final ComplexType ANCHOR_PROPERTYTYPE_TYPE = build_ANCHOR_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ANCHOR_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","Anchor_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ConventionalUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:ConventionalUnit"/&gt;
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
    public static final ComplexType CONVENTIONALUNIT_PROPERTYTYPE_TYPE = build_CONVENTIONALUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CONVENTIONALUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ConventionalUnit_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ClAlternativeExpression_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DefinitionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType CLALTERNATIVEEXPRESSION_TYPE_TYPE = build_CLALTERNATIVEEXPRESSION_TYPE_TYPE();
    
    private static ComplexType build_CLALTERNATIVEEXPRESSION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ClAlternativeExpression_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeAlternativeExpression_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeAlternativeExpression"/&gt;
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
    public static final ComplexType CODEALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE = build_CODEALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CODEALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CoordinateSystem_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractCoordinateSystem"/&gt;
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
    public static final ComplexType CT_COORDINATESYSTEM_PROPERTYTYPE_TYPE = build_CT_COORDINATESYSTEM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_COORDINATESYSTEM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_CoordinateSystem_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_LinearCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_LinearCS"/&gt;
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
    public static final ComplexType ML_LINEARCS_PROPERTYTYPE_TYPE = build_ML_LINEARCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_LINEARCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_LinearCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Transformation_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:TransformationType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_TRANSFORMATION_TYPE_TYPE = build_ML_TRANSFORMATION_TYPE_TYPE();
    
    private static ComplexType build_ML_TRANSFORMATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_Transformation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.TRANSFORMATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="FileName_Type"&gt;
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
    public static final ComplexType FILENAME_TYPE_TYPE = build_FILENAME_TYPE_TYPE();
    
    private static ComplexType build_FILENAME_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","FileName_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ClAlternativeExpression_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ClAlternativeExpression"/&gt;
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
    public static final ComplexType CLALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE = build_CLALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CLALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ClAlternativeExpression_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="UnitDefinition_PropertyType"&gt;
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
    public static final ComplexType UNITDEFINITION_PROPERTYTYPE_TYPE = build_UNITDEFINITION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_UNITDEFINITION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","UnitDefinition_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_Aggregate_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_Aggregate"/&gt;
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
    public static final ComplexType MX_AGGREGATE_PROPERTYTYPE_TYPE = build_MX_AGGREGATE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_AGGREGATE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_Aggregate_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CartesianCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CartesianCS"/&gt;
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
    public static final ComplexType ML_CARTESIANCS_PROPERTYTYPE_TYPE = build_ML_CARTESIANCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CARTESIANCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CartesianCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CodelistCatalogue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CT_CodelistCatalogue"/&gt;
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
    public static final ComplexType CT_CODELISTCATALOGUE_PROPERTYTYPE_TYPE = build_CT_CODELISTCATALOGUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CODELISTCATALOGUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Codelist_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeListDictionary"/&gt;
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
    public static final ComplexType CT_CODELIST_PROPERTYTYPE_TYPE = build_CT_CODELIST_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CODELIST_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_Codelist_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_AffineCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:AffineCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_AFFINECS_TYPE_TYPE = build_ML_AFFINECS_TYPE_TYPE();
    
    private static ComplexType build_ML_AFFINECS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_AffineCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.AFFINECSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_OperationMethod_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:OperationMethod"/&gt;
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
    public static final ComplexType CT_OPERATIONMETHOD_PROPERTYTYPE_TYPE = build_CT_OPERATIONMETHOD_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_OPERATIONMETHOD_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_OperationMethod_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_BaseUnit_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:BaseUnitType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:UomAlternativeExpression_PropertyType"/&gt;
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
    public static final ComplexType ML_BASEUNIT_TYPE_TYPE = build_ML_BASEUNIT_TYPE_TYPE();
    
    private static ComplexType build_ML_BASEUNIT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_BaseUnit_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.BASEUNITTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ImageCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ImageCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_IMAGECRS_TYPE_TYPE = build_ML_IMAGECRS_TYPE_TYPE();
    
    private static ComplexType build_ML_IMAGECRS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageCRS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.IMAGECRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:OperationAlt"/&gt;
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
    public static final ComplexType OPERATIONALT_PROPERTYTYPE_TYPE = build_OPERATIONALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","OperationAlt_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_DataFile_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_DataFile"/&gt;
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
    public static final ComplexType MX_DATAFILE_PROPERTYTYPE_TYPE = build_MX_DATAFILE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_DATAFILE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataFile_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_SupportFile_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractMX_File_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MX_SUPPORTFILE_TYPE_TYPE = build_MX_SUPPORTFILE_TYPE_TYPE();
    
    private static ComplexType build_MX_SUPPORTFILE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_SupportFile_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTMX_FILE_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_Aggregate_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDS_Aggregate_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="aggregateCatalogue" type="gmx:CT_Catalogue_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="aggregateFile" type="gmx:MX_SupportFile_PropertyType"/&gt;
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
    public static final ComplexType MX_AGGREGATE_TYPE_TYPE = build_MX_AGGREGATE_TYPE_TYPE();
    
    private static ComplexType build_MX_AGGREGATE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_Aggregate_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMDSchema.ABSTRACTDS_AGGREGATE_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationParameterAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:OperationParameterType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType OPERATIONPARAMETERALT_TYPE_TYPE = build_OPERATIONPARAMETERALT_TYPE_TYPE();
    
    private static ComplexType build_OPERATIONPARAMETERALT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","OperationParameterAlt_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.OPERATIONPARAMETERTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Ellipsoid_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:Ellipsoid"/&gt;
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
    public static final ComplexType CT_ELLIPSOID_PROPERTYTYPE_TYPE = build_CT_ELLIPSOID_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_ELLIPSOID_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_Ellipsoid_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CoordinateSystemAxisAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:CoordinateSystemAxisType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType COORDINATESYSTEMAXISALT_TYPE_TYPE = build_COORDINATESYSTEMAXISALT_TYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMAXISALT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.COORDINATESYSTEMAXISTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PrimeMeridianAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:PrimeMeridianAlt"/&gt;
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
    public static final ComplexType PRIMEMERIDIANALT_PROPERTYTYPE_TYPE = build_PRIMEMERIDIANALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_PRIMEMERIDIANALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CoordinateSystemAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attributeGroup ref="gml:AggregationAttributeGroup"/&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType COORDINATESYSTEMALT_TYPE_TYPE = build_COORDINATESYSTEMALT_TYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMALT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_UnitDefinition_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:UnitDefinitionType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:UomAlternativeExpression_PropertyType"/&gt;
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
    public static final ComplexType ML_UNITDEFINITION_TYPE_TYPE = build_ML_UNITDEFINITION_TYPE_TYPE();
    
    private static ComplexType build_ML_UNITDEFINITION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_UnitDefinition_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.UNITDEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeListDictionary_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Constraints: - 1) metadataProperty.card = 0 - 2) dictionaryEntry.card = 0&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DictionaryType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="codeEntry" type="gmx:CodeDefinition_PropertyType"/&gt;
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
    public static final ComplexType CODELISTDICTIONARY_TYPE_TYPE = build_CODELISTDICTIONARY_TYPE_TYPE();
    
    private static ComplexType build_CODELISTDICTIONARY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CodeListDictionary_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.DICTIONARYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CodeListDictionary_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Constraint: codeEntry.type = ML_CodeListDefinition&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:CodeListDictionary_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:ClAlternativeExpression_PropertyType"/&gt;
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
    public static final ComplexType ML_CODELISTDICTIONARY_TYPE_TYPE = build_ML_CODELISTDICTIONARY_TYPE_TYPE();
    
    private static ComplexType build_ML_CODELISTDICTIONARY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), CODELISTDICTIONARY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TemporalCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:TemporalCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_TEMPORALCRS_TYPE_TYPE = build_ML_TEMPORALCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_TEMPORALCRS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalCRS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.TEMPORALCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationMethodAlt_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;XML attributes contraints: - 1) Id is mandatory - 2) codeSpace (type xsd:anyURI) is mandatory&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:IdentifiedObjectType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *              &lt;/xs:sequence&gt;
     *              &lt;xs:attribute name="codeSpace" type="xs:anyURI" use="required"/&gt;
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
    public static final ComplexType OPERATIONMETHODALT_TYPE_TYPE = build_OPERATIONMETHODALT_TYPE_TYPE();
    
    private static ComplexType build_OPERATIONMETHODALT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","OperationMethodAlt_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.IDENTIFIEDOBJECTTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ConcatenatedOperation_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ConcatenatedOperationType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_CONCATENATEDOPERATION_TYPE_TYPE = build_ML_CONCATENATEDOPERATION_TYPE_TYPE();
    
    private static ComplexType build_ML_CONCATENATEDOPERATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.CONCATENATEDOPERATIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="FileName_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:FileName"/&gt;
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
    public static final ComplexType FILENAME_PROPERTYTYPE_TYPE = build_FILENAME_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_FILENAME_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","FileName_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationParameter_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_OperationParameter"/&gt;
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
    public static final ComplexType ML_OPERATIONPARAMETER_PROPERTYTYPE_TYPE = build_ML_OPERATIONPARAMETER_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONPARAMETER_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameter_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_AffineCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_AffineCS"/&gt;
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
    public static final ComplexType ML_AFFINECS_PROPERTYTYPE_TYPE = build_ML_AFFINECS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_AFFINECS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_AffineCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TimeCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:TimeCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_TIMECS_TYPE_TYPE = build_ML_TIMECS_TYPE_TYPE();
    
    private static ComplexType build_ML_TIMECS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_TimeCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.TIMECSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CodeListDictionary_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CodeListDictionary"/&gt;
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
    public static final ComplexType ML_CODELISTDICTIONARY_PROPERTYTYPE_TYPE = build_ML_CODELISTDICTIONARY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CODELISTDICTIONARY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PrimeMeridian_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_PrimeMeridian"/&gt;
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
    public static final ComplexType ML_PRIMEMERIDIAN_PROPERTYTYPE_TYPE = build_ML_PRIMEMERIDIAN_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_PRIMEMERIDIAN_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Catalogue_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:AbstractCT_Catalogue"/&gt;
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
    public static final ComplexType CT_CATALOGUE_PROPERTYTYPE_TYPE = build_CT_CATALOGUE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_CATALOGUE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_Catalogue_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CoordinateSystemAxis_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CoordinateSystemAxis"/&gt;
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
    public static final ComplexType ML_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE = build_ML_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_GeodeticDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:GeodeticDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_GEODETICDATUM_TYPE_TYPE = build_ML_GEODETICDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_GEODETICDATUM_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.GEODETICDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_VerticalCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_VerticalCRS"/&gt;
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
    public static final ComplexType ML_VERTICALCRS_PROPERTYTYPE_TYPE = build_ML_VERTICALCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_VERTICALCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCRS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Ellipsoid_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_Ellipsoid"/&gt;
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
    public static final ComplexType ML_ELLIPSOID_PROPERTYTYPE_TYPE = build_ML_ELLIPSOID_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_ELLIPSOID_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_Ellipsoid_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationMethod_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:OperationMethodType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationMethodAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_OPERATIONMETHOD_TYPE_TYPE = build_ML_OPERATIONMETHOD_TYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONMETHOD_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationMethod_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.OPERATIONMETHODTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_UnitDefinition_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_UnitDefinition"/&gt;
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
    public static final ComplexType ML_UNITDEFINITION_PROPERTYTYPE_TYPE = build_ML_UNITDEFINITION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_UNITDEFINITION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_UnitDefinition_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationParameterGroup_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_OperationParameterGroup"/&gt;
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
    public static final ComplexType ML_OPERATIONPARAMETERGROUP_PROPERTYTYPE_TYPE = build_ML_OPERATIONPARAMETERGROUP_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONPARAMETERGROUP_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EngineeringCRS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:EngineeringCRSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CrsAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_ENGINEERINGCRS_TYPE_TYPE = build_ML_ENGINEERINGCRS_TYPE_TYPE();
    
    private static ComplexType build_ML_ENGINEERINGCRS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.ENGINEERINGCRSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_SupportFile_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_SupportFile"/&gt;
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
    public static final ComplexType MX_SUPPORTFILE_PROPERTYTYPE_TYPE = build_MX_SUPPORTFILE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_SUPPORTFILE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_SupportFile_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_DerivedUnit_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DerivedUnitType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:UomAlternativeExpression_PropertyType"/&gt;
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
    public static final ComplexType ML_DERIVEDUNIT_TYPE_TYPE = build_ML_DERIVEDUNIT_TYPE_TYPE();
    
    private static ComplexType build_ML_DERIVEDUNIT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedUnit_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.DERIVEDUNITTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_GeodeticCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_GeodeticCRS"/&gt;
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
    public static final ComplexType ML_GEODETICCRS_PROPERTYTYPE_TYPE = build_ML_GEODETICCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_GEODETICCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ConcatenatedOperation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ConcatenatedOperation"/&gt;
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
    public static final ComplexType ML_CONCATENATEDOPERATION_PROPERTYTYPE_TYPE = build_ML_CONCATENATEDOPERATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CONCATENATEDOPERATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EngineeringDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_EngineeringDatum"/&gt;
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
    public static final ComplexType ML_ENGINEERINGDATUM_PROPERTYTYPE_TYPE = build_ML_ENGINEERINGDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_ENGINEERINGDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_SphericalCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_SphericalCS"/&gt;
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
    public static final ComplexType ML_SPHERICALCS_PROPERTYTYPE_TYPE = build_ML_SPHERICALCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_SPHERICALCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_SphericalCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DatumAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:DatumAlt"/&gt;
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
    public static final ComplexType DATUMALT_PROPERTYTYPE_TYPE = build_DATUMALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DATUMALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","DatumAlt_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TimeCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_TimeCS"/&gt;
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
    public static final ComplexType ML_TIMECS_PROPERTYTYPE_TYPE = build_ML_TIMECS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_TIMECS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_TimeCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="OperationParameterAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:OperationParameterAlt"/&gt;
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
    public static final ComplexType OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE = build_OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","OperationParameterAlt_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MimeFileType_Type"&gt;
     *      &lt;xs:simpleContent&gt;
     *          &lt;xs:extension base="xs:string"&gt;
     *              &lt;xs:attribute name="type" type="xs:string" use="required"/&gt;
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
    public static final ComplexType MIMEFILETYPE_TYPE_TYPE = build_MIMEFILETYPE_TYPE_TYPE();
    
    private static ComplexType build_MIMEFILETYPE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MimeFileType_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_Operation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractCoordinateOperation"/&gt;
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
    public static final ComplexType CT_OPERATION_PROPERTYTYPE_TYPE = build_CT_OPERATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_OPERATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_Operation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DerivedUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:DerivedUnit"/&gt;
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
    public static final ComplexType DERIVEDUNIT_PROPERTYTYPE_TYPE = build_DERIVEDUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DERIVEDUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","DerivedUnit_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_BaseUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_BaseUnit"/&gt;
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
    public static final ComplexType ML_BASEUNIT_PROPERTYTYPE_TYPE = build_ML_BASEUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_BASEUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_BaseUnit_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_TemporalCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_TemporalCRS"/&gt;
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
    public static final ComplexType ML_TEMPORALCRS_PROPERTYTYPE_TYPE = build_ML_TEMPORALCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_TEMPORALCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalCRS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CoordinateSystemAlt_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CoordinateSystemAlt"/&gt;
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
    public static final ComplexType COORDINATESYSTEMALT_PROPERTYTYPE_TYPE = build_COORDINATESYSTEMALT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_COORDINATESYSTEMALT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_EllipsoidalCS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_EllipsoidalCS"/&gt;
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
    public static final ComplexType ML_ELLIPSOIDALCS_PROPERTYTYPE_TYPE = build_ML_ELLIPSOIDALCS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_ELLIPSOIDALCS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ImageDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_ImageDatum"/&gt;
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
    public static final ComplexType ML_IMAGEDATUM_PROPERTYTYPE_TYPE = build_ML_IMAGEDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_IMAGEDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageDatum_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_ImageDatum_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:ImageDatumType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:DatumAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_IMAGEDATUM_TYPE_TYPE = build_ML_IMAGEDATUM_TYPE_TYPE();
    
    private static ComplexType build_ML_IMAGEDATUM_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageDatum_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.IMAGEDATUMTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_OperationParameters_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:AbstractGeneralOperationParameter"/&gt;
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
    public static final ComplexType CT_OPERATIONPARAMETERS_PROPERTYTYPE_TYPE = build_CT_OPERATIONPARAMETERS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_OPERATIONPARAMETERS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_OperationParameters_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PrimeMeridian_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:PrimeMeridianType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:PrimeMeridianAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_PRIMEMERIDIAN_TYPE_TYPE = build_ML_PRIMEMERIDIAN_TYPE_TYPE();
    
    private static ComplexType build_ML_PRIMEMERIDIAN_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.PRIMEMERIDIANTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_GeodeticDatum_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_GeodeticDatum"/&gt;
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
    public static final ComplexType ML_GEODETICDATUM_PROPERTYTYPE_TYPE = build_ML_GEODETICDATUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_GEODETICDATUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_Conversion_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_Conversion"/&gt;
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
    public static final ComplexType ML_CONVERSION_PROPERTYTYPE_TYPE = build_ML_CONVERSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_CONVERSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_Conversion_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_DerivedUnit_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_DerivedUnit"/&gt;
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
    public static final ComplexType ML_DERIVEDUNIT_PROPERTYTYPE_TYPE = build_ML_DERIVEDUNIT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_DERIVEDUNIT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedUnit_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeDefinition_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:DefinitionType"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType CODEDEFINITION_TYPE_TYPE = build_CODEDEFINITION_TYPE_TYPE();
    
    private static ComplexType build_CODEDEFINITION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CodeDefinition_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.DEFINITIONTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CodeDefinition_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:CodeDefinition_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CodeAlternativeExpression_PropertyType"/&gt;
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
    public static final ComplexType ML_CODEDEFINITION_TYPE_TYPE = build_ML_CODEDEFINITION_TYPE_TYPE();
    
    private static ComplexType build_ML_CODEDEFINITION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeDefinition_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), CODEDEFINITION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_UserDefinedCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:UserDefinedCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_USERDEFINEDCS_TYPE_TYPE = build_ML_USERDEFINEDCS_TYPE_TYPE();
    
    private static ComplexType build_ML_USERDEFINEDCS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.USERDEFINEDCSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_DataSet_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MX_DataSet"/&gt;
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
    public static final ComplexType MX_DATASET_PROPERTYTYPE_TYPE = build_MX_DATASET_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_DATASET_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataSet_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_PolarCS_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:PolarCSType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:CoordinateSystemAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_POLARCS_TYPE_TYPE = build_ML_POLARCS_TYPE_TYPE();
    
    private static ComplexType build_ML_POLARCS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_PolarCS_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.POLARCSTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CodeDefinition_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:CodeDefinition"/&gt;
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
    public static final ComplexType CODEDEFINITION_PROPERTYTYPE_TYPE = build_CODEDEFINITION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CODEDEFINITION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CodeDefinition_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_OperationParameterGroup_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gml:OperationParameterGroupType"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="alternativeExpression" type="gmx:OperationParameterAlt_PropertyType"/&gt;
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
    public static final ComplexType ML_OPERATIONPARAMETERGROUP_TYPE_TYPE = build_ML_OPERATIONPARAMETERGROUP_TYPE_TYPE();
    
    private static ComplexType build_ML_OPERATIONPARAMETERGROUP_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GMLSchema.OPERATIONPARAMETERGROUPTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_CoordinateSystemAxis_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gml:CoordinateSystemAxis"/&gt;
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
    public static final ComplexType CT_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE = build_CT_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CT_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_CoordinateSystemAxis_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MimeFileType_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:MimeFileType"/&gt;
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
    public static final ComplexType MIMEFILETYPE_PROPERTYTYPE_TYPE = build_MIMEFILETYPE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MIMEFILETYPE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MimeFileType_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="ML_CompoundCRS_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:ML_CompoundCRS"/&gt;
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
    public static final ComplexType ML_COMPOUNDCRS_PROPERTYTYPE_TYPE = build_ML_COMPOUNDCRS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_ML_COMPOUNDCRS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","ML_CompoundCRS_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MX_File_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmx:AbstractMX_File"/&gt;
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
    public static final ComplexType MX_FILE_PROPERTYTYPE_TYPE = build_MX_FILE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MX_FILE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","MX_File_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CT_UomCatalogue_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmx:AbstractCT_Catalogue_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="uomItem" type="gmx:UnitDefinition_PropertyType"/&gt;
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
    public static final ComplexType CT_UOMCATALOGUE_TYPE_TYPE = build_CT_UOMCATALOGUE_TYPE_TYPE();
    
    private static ComplexType build_CT_UOMCATALOGUE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmx","CT_UomCatalogue_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTCT_CATALOGUE_TYPE_TYPE, null
        );
        return builtType;
    }


    public GMXSchema() {
        super("http://www.isotc211.org/2005/gmx");

        put(new NameImpl("http://www.isotc211.org/2005/gmx","DatumAlt_Type"),DATUMALT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CylindricalCS_PropertyType"),ML_CYLINDRICALCS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_PrimeMeridian_PropertyType"),CT_PRIMEMERIDIAN_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCS_Type"),ML_VERTICALCS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS_Type"),ML_GEODETICCRS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CylindricalCS_Type"),ML_CYLINDRICALCS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt_Type"),PRIMEMERIDIANALT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit_Type"),ML_CONVENTIONALUNIT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageCRS_PropertyType"),ML_IMAGECRS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_SphericalCS_Type"),ML_SPHERICALCS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CartesianCS_Type"),ML_CARTESIANCS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalDatum_PropertyType"),ML_TEMPORALDATUM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation_PropertyType"),ML_PASSTHROUGHOPERATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalDatum_Type"),ML_TEMPORALDATUM_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CompoundCRS_Type"),ML_COMPOUNDCRS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CrsAlt_PropertyType"),CRSALT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","OperationAlt_Type"),OPERATIONALT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","UomAlternativeExpression_Type"),UOMALTERNATIVEEXPRESSION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameter_Type"),ML_OPERATIONPARAMETER_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_PolarCS_PropertyType"),ML_POLARCS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression_Type"),CODEALTERNATIVEEXPRESSION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalDatum_Type"),ML_VERTICALDATUM_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue_PropertyType"),CT_CRSCATALOGUE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS_PropertyType"),ML_PROJECTEDCRS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS_Type"),ML_ELLIPSOIDALCS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis_Type"),ML_COORDINATESYSTEMAXIS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","EllipsoidAlt_PropertyType"),ELLIPSOIDALT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","AbstractCT_Catalogue_Type"),ABSTRACTCT_CATALOGUE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue_Type"),CT_CRSCATALOGUE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationMethod_PropertyType"),ML_OPERATIONMETHOD_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","BaseUnit_PropertyType"),BASEUNIT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit_PropertyType"),ML_CONVENTIONALUNIT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","UomAlternativeExpression_PropertyType"),UOMALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCRS_Type"),ML_VERTICALCRS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCS_PropertyType"),ML_VERTICALCS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_Ellipsoid_Type"),ML_ELLIPSOID_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistValue_PropertyType"),CT_CODELISTVALUE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalDatum_PropertyType"),ML_VERTICALDATUM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue_Type"),CT_CODELISTCATALOGUE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_Conversion_Type"),ML_CONVERSION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS_PropertyType"),ML_USERDEFINEDCS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedCRS_PropertyType"),ML_DERIVEDCRS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum_Type"),ML_ENGINEERINGDATUM_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","OperationMethodAlt_PropertyType"),OPERATIONMETHODALT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_CRS_PropertyType"),CT_CRS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataSet_Type"),MX_DATASET_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeDefinition_PropertyType"),ML_CODEDEFINITION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CrsAlt_Type"),CRSALT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CodeListDictionary_PropertyType"),CODELISTDICTIONARY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation_Type"),ML_PASSTHROUGHOPERATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","Anchor_Type"),ANCHOR_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS_Type"),ML_PROJECTEDCRS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_Datum_PropertyType"),CT_DATUM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS_PropertyType"),ML_ENGINEERINGCRS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_UomCatalogue_PropertyType"),CT_UOMCATALOGUE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedCRS_Type"),ML_DERIVEDCRS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_ScopeCode_PropertyType"),MX_SCOPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_Transformation_PropertyType"),ML_TRANSFORMATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","AbstractMX_File_Type"),ABSTRACTMX_FILE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataFile_Type"),MX_DATAFILE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt_PropertyType"),COORDINATESYSTEMAXISALT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_LinearCS_Type"),ML_LINEARCS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","EllipsoidAlt_Type"),ELLIPSOIDALT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","Anchor_PropertyType"),ANCHOR_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ConventionalUnit_PropertyType"),CONVENTIONALUNIT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ClAlternativeExpression_Type"),CLALTERNATIVEEXPRESSION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression_PropertyType"),CODEALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_CoordinateSystem_PropertyType"),CT_COORDINATESYSTEM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_LinearCS_PropertyType"),ML_LINEARCS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_Transformation_Type"),ML_TRANSFORMATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","FileName_Type"),FILENAME_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ClAlternativeExpression_PropertyType"),CLALTERNATIVEEXPRESSION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","UnitDefinition_PropertyType"),UNITDEFINITION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_Aggregate_PropertyType"),MX_AGGREGATE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CartesianCS_PropertyType"),ML_CARTESIANCS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue_PropertyType"),CT_CODELISTCATALOGUE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_Codelist_PropertyType"),CT_CODELIST_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_AffineCS_Type"),ML_AFFINECS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_OperationMethod_PropertyType"),CT_OPERATIONMETHOD_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_BaseUnit_Type"),ML_BASEUNIT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageCRS_Type"),ML_IMAGECRS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","OperationAlt_PropertyType"),OPERATIONALT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataFile_PropertyType"),MX_DATAFILE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_SupportFile_Type"),MX_SUPPORTFILE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_Aggregate_Type"),MX_AGGREGATE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","OperationParameterAlt_Type"),OPERATIONPARAMETERALT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_Ellipsoid_PropertyType"),CT_ELLIPSOID_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt_Type"),COORDINATESYSTEMAXISALT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt_PropertyType"),PRIMEMERIDIANALT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt_Type"),COORDINATESYSTEMALT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_UnitDefinition_Type"),ML_UNITDEFINITION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CodeListDictionary_Type"),CODELISTDICTIONARY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary_Type"),ML_CODELISTDICTIONARY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalCRS_Type"),ML_TEMPORALCRS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","OperationMethodAlt_Type"),OPERATIONMETHODALT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation_Type"),ML_CONCATENATEDOPERATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","FileName_PropertyType"),FILENAME_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameter_PropertyType"),ML_OPERATIONPARAMETER_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_AffineCS_PropertyType"),ML_AFFINECS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_TimeCS_Type"),ML_TIMECS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary_PropertyType"),ML_CODELISTDICTIONARY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian_PropertyType"),ML_PRIMEMERIDIAN_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_Catalogue_PropertyType"),CT_CATALOGUE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis_PropertyType"),ML_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum_Type"),ML_GEODETICDATUM_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_VerticalCRS_PropertyType"),ML_VERTICALCRS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_Ellipsoid_PropertyType"),ML_ELLIPSOID_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationMethod_Type"),ML_OPERATIONMETHOD_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_UnitDefinition_PropertyType"),ML_UNITDEFINITION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup_PropertyType"),ML_OPERATIONPARAMETERGROUP_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS_Type"),ML_ENGINEERINGCRS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_SupportFile_PropertyType"),MX_SUPPORTFILE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedUnit_Type"),ML_DERIVEDUNIT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS_PropertyType"),ML_GEODETICCRS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation_PropertyType"),ML_CONCATENATEDOPERATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum_PropertyType"),ML_ENGINEERINGDATUM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_SphericalCS_PropertyType"),ML_SPHERICALCS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","DatumAlt_PropertyType"),DATUMALT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_TimeCS_PropertyType"),ML_TIMECS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","OperationParameterAlt_PropertyType"),OPERATIONPARAMETERALT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MimeFileType_Type"),MIMEFILETYPE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_Operation_PropertyType"),CT_OPERATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","DerivedUnit_PropertyType"),DERIVEDUNIT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_BaseUnit_PropertyType"),ML_BASEUNIT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_TemporalCRS_PropertyType"),ML_TEMPORALCRS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt_PropertyType"),COORDINATESYSTEMALT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS_PropertyType"),ML_ELLIPSOIDALCS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageDatum_PropertyType"),ML_IMAGEDATUM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_ImageDatum_Type"),ML_IMAGEDATUM_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_OperationParameters_PropertyType"),CT_OPERATIONPARAMETERS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian_Type"),ML_PRIMEMERIDIAN_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum_PropertyType"),ML_GEODETICDATUM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_Conversion_PropertyType"),ML_CONVERSION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_DerivedUnit_PropertyType"),ML_DERIVEDUNIT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CodeDefinition_Type"),CODEDEFINITION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CodeDefinition_Type"),ML_CODEDEFINITION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS_Type"),ML_USERDEFINEDCS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_DataSet_PropertyType"),MX_DATASET_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_PolarCS_Type"),ML_POLARCS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CodeDefinition_PropertyType"),CODEDEFINITION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup_Type"),ML_OPERATIONPARAMETERGROUP_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_CoordinateSystemAxis_PropertyType"),CT_COORDINATESYSTEMAXIS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MimeFileType_PropertyType"),MIMEFILETYPE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","ML_CompoundCRS_PropertyType"),ML_COMPOUNDCRS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","MX_File_PropertyType"),MX_FILE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmx","CT_UomCatalogue_Type"),CT_UOMCATALOGUE_TYPE_TYPE);
    }
    
}