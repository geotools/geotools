package org.geotools.gml3.v3_2.gmd;


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
import org.geotools.gml3.v3_2.gsr.GSRSchema;
import org.geotools.xlink.XLINKSchema;

public class GMDSchema extends SchemaImpl {

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractMD_SpatialRepresentation_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Digital mechanism used to represent spatial information&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence/&gt;
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
    public static final ComplexType ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE = build_ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_SpatialRepresentation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_VectorSpatialRepresentation_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about the vector spatial objects in the dataset&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractMD_SpatialRepresentation_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="topologyLevel" type="gmd:MD_TopologyLevelCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="geometricObjects" type="gmd:MD_GeometricObjects_PropertyType"/&gt;
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
    public static final ComplexType MD_VECTORSPATIALREPRESENTATION_TYPE_TYPE = build_MD_VECTORSPATIALREPRESENTATION_TYPE_TYPE();
    
    private static ComplexType build_MD_VECTORSPATIALREPRESENTATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_RestrictionCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_RestrictionCode"/&gt;
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
    public static final ComplexType MD_RESTRICTIONCODE_PROPERTYTYPE_TYPE = build_MD_RESTRICTIONCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_RESTRICTIONCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_RestrictionCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Telephone_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_Telephone"/&gt;
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
    public static final ComplexType CI_TELEPHONE_PROPERTYTYPE_TYPE = build_CI_TELEPHONE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_TELEPHONE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Telephone_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LI_Source_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:LI_Source"/&gt;
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
    public static final ComplexType LI_SOURCE_PROPERTYTYPE_TYPE = build_LI_SOURCE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_LI_SOURCE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","LI_Source_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Usage_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Usage"/&gt;
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
    public static final ComplexType MD_USAGE_PROPERTYTYPE_TYPE = build_MD_USAGE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_USAGE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Usage_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractDQ_Element_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="nameOfMeasure" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="measureIdentification" type="gmd:MD_Identifier_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="measureDescription" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="evaluationMethodType" type="gmd:DQ_EvaluationMethodTypeCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="evaluationMethodDescription" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="evaluationProcedure" type="gmd:CI_Citation_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="dateTime" type="gco:DateTime_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="2" name="result" type="gmd:DQ_Result_PropertyType"/&gt;
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
    public static final ComplexType ABSTRACTDQ_ELEMENT_TYPE_TYPE = build_ABSTRACTDQ_ELEMENT_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTDQ_ELEMENT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Element_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractDQ_Completeness_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_Element_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTDQ_COMPLETENESS_TYPE_TYPE = build_ABSTRACTDQ_COMPLETENESS_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTDQ_COMPLETENESS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Completeness_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTDQ_ELEMENT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_CompletenessOmission_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_Completeness_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_COMPLETENESSOMISSION_TYPE_TYPE = build_DQ_COMPLETENESSOMISSION_TYPE_TYPE();
    
    private static ComplexType build_DQ_COMPLETENESSOMISSION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_COMPLETENESS_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_GridSpatialRepresentation_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Types and numbers of raster spatial objects in the dataset&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractMD_SpatialRepresentation_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="numberOfDimensions" type="gco:Integer_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="axisDimensionProperties" type="gmd:MD_Dimension_PropertyType"/&gt;
     *                  &lt;xs:element name="cellGeometry" type="gmd:MD_CellGeometryCode_PropertyType"/&gt;
     *                  &lt;xs:element name="transformationParameterAvailability" type="gco:Boolean_PropertyType"/&gt;
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
    public static final ComplexType MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE = build_MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE();
    
    private static ComplexType build_MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Georeferenceable_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:MD_GridSpatialRepresentation_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="controlPointAvailability" type="gco:Boolean_PropertyType"/&gt;
     *                  &lt;xs:element name="orientationParameterAvailability" type="gco:Boolean_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="orientationParameterDescription" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="georeferencedParameters" type="gco:Record_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="parameterCitation" type="gmd:CI_Citation_PropertyType"/&gt;
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
    public static final ComplexType MD_GEOREFERENCEABLE_TYPE_TYPE = build_MD_GEOREFERENCEABLE_TYPE_TYPE();
    
    private static ComplexType build_MD_GEOREFERENCEABLE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georeferenceable_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_GeometricObjects_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_GeometricObjects"/&gt;
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
    public static final ComplexType MD_GEOMETRICOBJECTS_PROPERTYTYPE_TYPE = build_MD_GEOMETRICOBJECTS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_GEOMETRICOBJECTS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjects_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Distribution_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about the distributor of and options for obtaining the dataset&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="distributionFormat" type="gmd:MD_Format_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="distributor" type="gmd:MD_Distributor_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="transferOptions" type="gmd:MD_DigitalTransferOptions_PropertyType"/&gt;
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
    public static final ComplexType MD_DISTRIBUTION_TYPE_TYPE = build_MD_DISTRIBUTION_TYPE_TYPE();
    
    private static ComplexType build_MD_DISTRIBUTION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distribution_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_TopologyLevelCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_TopologyLevelCode"/&gt;
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
    public static final ComplexType MD_TOPOLOGYLEVELCODE_PROPERTYTYPE_TYPE = build_MD_TOPOLOGYLEVELCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_TOPOLOGYLEVELCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopologyLevelCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_VerticalExtent_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:EX_VerticalExtent"/&gt;
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
    public static final ComplexType EX_VERTICALEXTENT_PROPERTYTYPE_TYPE = build_EX_VERTICALEXTENT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_EX_VERTICALEXTENT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_VerticalExtent_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_PresentationFormCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_PresentationFormCode"/&gt;
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
    public static final ComplexType CI_PRESENTATIONFORMCODE_PROPERTYTYPE_TYPE = build_CI_PRESENTATIONFORMCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_PRESENTATIONFORMCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_PresentationFormCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Platform_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_Platform"/&gt;
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
    public static final ComplexType DS_PLATFORM_PROPERTYTYPE_TYPE = build_DS_PLATFORM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_PLATFORM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Platform_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Medium_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Medium"/&gt;
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
    public static final ComplexType MD_MEDIUM_PROPERTYTYPE_TYPE = build_MD_MEDIUM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_MEDIUM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Medium_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_MediumFormatCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_MediumFormatCode"/&gt;
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
    public static final ComplexType MD_MEDIUMFORMATCODE_PROPERTYTYPE_TYPE = build_MD_MEDIUMFORMATCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_MEDIUMFORMATCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_MediumFormatCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_TopicCategoryCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_TopicCategoryCode"/&gt;
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
    public static final ComplexType MD_TOPICCATEGORYCODE_PROPERTYTYPE_TYPE = build_MD_TOPICCATEGORYCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_TOPICCATEGORYCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_AccuracyOfATimeMeasurement_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_AccuracyOfATimeMeasurement"/&gt;
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
    public static final ComplexType DQ_ACCURACYOFATIMEMEASUREMENT_PROPERTYTYPE_TYPE = build_DQ_ACCURACYOFATIMEMEASUREMENT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_ACCURACYOFATIMEMEASUREMENT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_GeographicExtent_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractEX_GeographicExtent"/&gt;
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
    public static final ComplexType EX_GEOGRAPHICEXTENT_PROPERTYTYPE_TYPE = build_EX_GEOGRAPHICEXTENT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_EX_GEOGRAPHICEXTENT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicExtent_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_CompletenessOmission_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_CompletenessOmission"/&gt;
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
    public static final ComplexType DQ_COMPLETENESSOMISSION_PROPERTYTYPE_TYPE = build_DQ_COMPLETENESSOMISSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_COMPLETENESSOMISSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Date_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_Date"/&gt;
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
    public static final ComplexType CI_DATE_PROPERTYTYPE_TYPE = build_CI_DATE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_DATE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Date_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LocalisedCharacterString_Type"&gt;
     *      &lt;xs:simpleContent&gt;
     *          &lt;xs:extension base="xs:string"&gt;
     *              &lt;xs:attribute name="id" type="xs:ID"/&gt;
     *              &lt;xs:attribute name="locale" type="xs:anyURI"/&gt;
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
    public static final ComplexType LOCALISEDCHARACTERSTRING_TYPE_TYPE = build_LOCALISEDCHARACTERSTRING_TYPE_TYPE();
    
    private static ComplexType build_LOCALISEDCHARACTERSTRING_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","LocalisedCharacterString_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Metadata_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Metadata"/&gt;
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
    public static final ComplexType MD_METADATA_PROPERTYTYPE_TYPE = build_MD_METADATA_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_METADATA_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Metadata_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ProgressCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ProgressCode"/&gt;
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
    public static final ComplexType MD_PROGRESSCODE_PROPERTYTYPE_TYPE = build_MD_PROGRESSCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_PROGRESSCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ProgressCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_SpatialTemporalExtent_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:EX_SpatialTemporalExtent"/&gt;
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
    public static final ComplexType EX_SPATIALTEMPORALEXTENT_PROPERTYTYPE_TYPE = build_EX_SPATIALTEMPORALEXTENT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_EX_SPATIALTEMPORALEXTENT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Usage_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Brief description of ways in which the dataset is currently used.&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="specificUsage" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="usageDateTime" type="gco:DateTime_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="userDeterminedLimitations" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="userContactInfo" type="gmd:CI_ResponsibleParty_PropertyType"/&gt;
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
    public static final ComplexType MD_USAGE_TYPE_TYPE = build_MD_USAGE_TYPE_TYPE();
    
    private static ComplexType build_MD_USAGE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Usage_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_RepresentativeFraction_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="denominator" type="gco:Integer_PropertyType"/&gt;
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
    public static final ComplexType MD_REPRESENTATIVEFRACTION_TYPE_TYPE = build_MD_REPRESENTATIVEFRACTION_TYPE_TYPE();
    
    private static ComplexType build_MD_REPRESENTATIVEFRACTION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_GriddedDataPositionalAccuracy_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_GriddedDataPositionalAccuracy"/&gt;
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
    public static final ComplexType DQ_GRIDDEDDATAPOSITIONALACCURACY_PROPERTYTYPE_TYPE = build_DQ_GRIDDEDDATAPOSITIONALACCURACY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_GRIDDEDDATAPOSITIONALACCURACY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ObligationCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ObligationCode"/&gt;
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
    public static final ComplexType MD_OBLIGATIONCODE_PROPERTYTYPE_TYPE = build_MD_OBLIGATIONCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_OBLIGATIONCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ObligationCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractDQ_TemporalAccuracy_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_Element_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE = build_ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_TemporalAccuracy_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTDQ_ELEMENT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_AccuracyOfATimeMeasurement_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_TemporalAccuracy_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_ACCURACYOFATIMEMEASUREMENT_TYPE_TYPE = build_DQ_ACCURACYOFATIMEMEASUREMENT_TYPE_TYPE();
    
    private static ComplexType build_DQ_ACCURACYOFATIMEMEASUREMENT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_AssociationTypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_AssociationTypeCode"/&gt;
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
    public static final ComplexType DS_ASSOCIATIONTYPECODE_PROPERTYTYPE_TYPE = build_DS_ASSOCIATIONTYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_ASSOCIATIONTYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_AssociationTypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_SecurityConstraints_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_SecurityConstraints"/&gt;
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
    public static final ComplexType MD_SECURITYCONSTRAINTS_PROPERTYTYPE_TYPE = build_MD_SECURITYCONSTRAINTS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_SECURITYCONSTRAINTS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Medium_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about the media on which the data can be distributed&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="name" type="gmd:MD_MediumNameCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="density" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="densityUnits" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="volumes" type="gco:Integer_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="mediumFormat" type="gmd:MD_MediumFormatCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="mediumNote" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_MEDIUM_TYPE_TYPE = build_MD_MEDIUM_TYPE_TYPE();
    
    private static ComplexType build_MD_MEDIUM_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Medium_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_CoverageDescription_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_CoverageDescription"/&gt;
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
    public static final ComplexType MD_COVERAGEDESCRIPTION_PROPERTYTYPE_TYPE = build_MD_COVERAGEDESCRIPTION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_COVERAGEDESCRIPTION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageDescription_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_MetadataExtensionInformation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_MetadataExtensionInformation"/&gt;
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
    public static final ComplexType MD_METADATAEXTENSIONINFORMATION_PROPERTYTYPE_TYPE = build_MD_METADATAEXTENSIONINFORMATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_METADATAEXTENSIONINFORMATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_OnLineFunctionCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_OnLineFunctionCode"/&gt;
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
    public static final ComplexType CI_ONLINEFUNCTIONCODE_PROPERTYTYPE_TYPE = build_CI_ONLINEFUNCTIONCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_ONLINEFUNCTIONCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnLineFunctionCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_DimensionNameTypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_DimensionNameTypeCode"/&gt;
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
    public static final ComplexType MD_DIMENSIONNAMETYPECODE_PROPERTYTYPE_TYPE = build_MD_DIMENSIONNAMETYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_DIMENSIONNAMETYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_DimensionNameTypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_StandardOrderProcess_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_StandardOrderProcess"/&gt;
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
    public static final ComplexType MD_STANDARDORDERPROCESS_PROPERTYTYPE_TYPE = build_MD_STANDARDORDERPROCESS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_STANDARDORDERPROCESS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Distributor_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about the distributor&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="distributorContact" type="gmd:CI_ResponsibleParty_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="distributionOrderProcess" type="gmd:MD_StandardOrderProcess_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="distributorFormat" type="gmd:MD_Format_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="distributorTransferOptions" type="gmd:MD_DigitalTransferOptions_PropertyType"/&gt;
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
    public static final ComplexType MD_DISTRIBUTOR_TYPE_TYPE = build_MD_DISTRIBUTOR_TYPE_TYPE();
    
    private static ComplexType build_MD_DISTRIBUTOR_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distributor_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Constraints_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Constraints"/&gt;
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
    public static final ComplexType MD_CONSTRAINTS_PROPERTYTYPE_TYPE = build_MD_CONSTRAINTS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_CONSTRAINTS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Constraints_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_MaintenanceInformation_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about the scope and frequency of updating&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="maintenanceAndUpdateFrequency" type="gmd:MD_MaintenanceFrequencyCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="dateOfNextUpdate" type="gco:Date_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="userDefinedMaintenanceFrequency" type="gts:TM_PeriodDuration_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="updateScope" type="gmd:MD_ScopeCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="updateScopeDescription" type="gmd:MD_ScopeDescription_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="maintenanceNote" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="contact" type="gmd:CI_ResponsibleParty_PropertyType"/&gt;
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
    public static final ComplexType MD_MAINTENANCEINFORMATION_TYPE_TYPE = build_MD_MAINTENANCEINFORMATION_TYPE_TYPE();
    
    private static ComplexType build_MD_MAINTENANCEINFORMATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_ThematicClassificationCorrectness_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_ThematicClassificationCorrectness"/&gt;
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
    public static final ComplexType DQ_THEMATICCLASSIFICATIONCORRECTNESS_PROPERTYTYPE_TYPE = build_DQ_THEMATICCLASSIFICATIONCORRECTNESS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_THEMATICCLASSIFICATIONCORRECTNESS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_GeographicBoundingBox_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:EX_GeographicBoundingBox"/&gt;
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
    public static final ComplexType EX_GEOGRAPHICBOUNDINGBOX_PROPERTYTYPE_TYPE = build_EX_GEOGRAPHICBOUNDINGBOX_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_EX_GEOGRAPHICBOUNDINGBOX_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Dimension_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Dimension"/&gt;
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
    public static final ComplexType MD_DIMENSION_PROPERTYTYPE_TYPE = build_MD_DIMENSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_DIMENSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Dimension_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractEX_GeographicExtent_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Geographic area of the dataset&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="extentTypeCode" type="gco:Boolean_PropertyType"/&gt;
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
    public static final ComplexType ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE = build_ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractEX_GeographicExtent_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_GeographicBoundingBox_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Geographic area of the entire dataset referenced to WGS 84&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractEX_GeographicExtent_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="westBoundLongitude" type="gco:Decimal_PropertyType"/&gt;
     *                  &lt;xs:element name="eastBoundLongitude" type="gco:Decimal_PropertyType"/&gt;
     *                  &lt;xs:element name="southBoundLatitude" type="gco:Decimal_PropertyType"/&gt;
     *                  &lt;xs:element name="northBoundLatitude" type="gco:Decimal_PropertyType"/&gt;
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
    public static final ComplexType EX_GEOGRAPHICBOUNDINGBOX_TYPE_TYPE = build_EX_GEOGRAPHICBOUNDINGBOX_TYPE_TYPE();
    
    private static ComplexType build_EX_GEOGRAPHICBOUNDINGBOX_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_RelativeInternalPositionalAccuracy_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_RelativeInternalPositionalAccuracy"/&gt;
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
    public static final ComplexType DQ_RELATIVEINTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE = build_DQ_RELATIVEINTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_RELATIVEINTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_GeographicDescription_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractEX_GeographicExtent_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="geographicIdentifier" type="gmd:MD_Identifier_PropertyType"/&gt;
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
    public static final ComplexType EX_GEOGRAPHICDESCRIPTION_TYPE_TYPE = build_EX_GEOGRAPHICDESCRIPTION_TYPE_TYPE();
    
    private static ComplexType build_EX_GEOGRAPHICDESCRIPTION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicDescription_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_DataSet_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Identifiable collection of data&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="has" type="gmd:MD_Metadata_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="partOf" type="gmd:DS_Aggregate_PropertyType"/&gt;
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
    public static final ComplexType DS_DATASET_TYPE_TYPE = build_DS_DATASET_TYPE_TYPE();
    
    private static ComplexType build_DS_DATASET_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_DataSet_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_QuantitativeAttributeAccuracy_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_QuantitativeAttributeAccuracy"/&gt;
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
    public static final ComplexType DQ_QUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE = build_DQ_QUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_QUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractRS_ReferenceSystem_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Description of the spatial and temporal reference systems used in the dataset&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="name" type="gmd:RS_Identifier_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="domainOfValidity" type="gmd:EX_Extent_PropertyType"/&gt;
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
    public static final ComplexType ABSTRACTRS_REFERENCESYSTEM_TYPE_TYPE = build_ABSTRACTRS_REFERENCESYSTEM_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTRS_REFERENCESYSTEM_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractRS_ReferenceSystem_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Citation_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Standardized resource reference&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="title" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="alternateTitle" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="date" type="gmd:CI_Date_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="edition" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="editionDate" type="gco:Date_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="identifier" type="gmd:MD_Identifier_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="citedResponsibleParty" type="gmd:CI_ResponsibleParty_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="presentationForm" type="gmd:CI_PresentationFormCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="series" type="gmd:CI_Series_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="otherCitationDetails" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="collectiveTitle" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="ISBN" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="ISSN" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType CI_CITATION_TYPE_TYPE = build_CI_CITATION_TYPE_TYPE();
    
    private static ComplexType build_CI_CITATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Citation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Association_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_Association"/&gt;
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
    public static final ComplexType DS_ASSOCIATION_PROPERTYTYPE_TYPE = build_DS_ASSOCIATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_ASSOCIATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Association_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_EvaluationMethodTypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_EvaluationMethodTypeCode"/&gt;
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
    public static final ComplexType DQ_EVALUATIONMETHODTYPECODE_PROPERTYTYPE_TYPE = build_DQ_EVALUATIONMETHODTYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_EVALUATIONMETHODTYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_EvaluationMethodTypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LI_Source_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="description" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="scaleDenominator" type="gmd:MD_RepresentativeFraction_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="sourceReferenceSystem" type="gmd:MD_ReferenceSystem_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="sourceCitation" type="gmd:CI_Citation_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="sourceExtent" type="gmd:EX_Extent_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="sourceStep" type="gmd:LI_ProcessStep_PropertyType"/&gt;
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
    public static final ComplexType LI_SOURCE_TYPE_TYPE = build_LI_SOURCE_TYPE_TYPE();
    
    private static ComplexType build_LI_SOURCE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","LI_Source_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_DataIdentification_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_DataIdentification"/&gt;
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
    public static final ComplexType MD_DATAIDENTIFICATION_PROPERTYTYPE_TYPE = build_MD_DATAIDENTIFICATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_DATAIDENTIFICATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_DataIdentification_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Address_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_Address"/&gt;
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
    public static final ComplexType CI_ADDRESS_PROPERTYTYPE_TYPE = build_CI_ADDRESS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_ADDRESS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Address_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractDQ_PositionalAccuracy_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_Element_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE = build_ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_PositionalAccuracy_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTDQ_ELEMENT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_AbsoluteExternalPositionalAccuracy_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_PositionalAccuracy_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_TYPE_TYPE = build_DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_TYPE_TYPE();
    
    private static ComplexType build_DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_LegalConstraints_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_LegalConstraints"/&gt;
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
    public static final ComplexType MD_LEGALCONSTRAINTS_PROPERTYTYPE_TYPE = build_MD_LEGALCONSTRAINTS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_LEGALCONSTRAINTS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_LegalConstraints_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_SpatialRepresentationTypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_SpatialRepresentationTypeCode"/&gt;
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
    public static final ComplexType MD_SPATIALREPRESENTATIONTYPECODE_PROPERTYTYPE_TYPE = build_MD_SPATIALREPRESENTATIONTYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_SPATIALREPRESENTATIONTYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentationTypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_SpatialRepresentation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractMD_SpatialRepresentation"/&gt;
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
    public static final ComplexType MD_SPATIALREPRESENTATION_PROPERTYTYPE_TYPE = build_MD_SPATIALREPRESENTATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_SPATIALREPRESENTATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractDS_Aggregate_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Identifiable collection of datasets&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="composedOf" type="gmd:DS_DataSet_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="seriesMetadata" type="gmd:MD_Metadata_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="subset" type="gmd:DS_Aggregate_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="superset" type="gmd:DS_Aggregate_PropertyType"/&gt;
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
    public static final ComplexType ABSTRACTDS_AGGREGATE_TYPE_TYPE = build_ABSTRACTDS_AGGREGATE_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTDS_AGGREGATE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDS_Aggregate_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Series_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDS_Aggregate_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DS_SERIES_TYPE_TYPE = build_DS_SERIES_TYPE_TYPE();
    
    private static ComplexType build_DS_SERIES_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Series_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDS_AGGREGATE_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_ProductionSeries_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:DS_Series_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DS_PRODUCTIONSERIES_TYPE_TYPE = build_DS_PRODUCTIONSERIES_TYPE_TYPE();
    
    private static ComplexType build_DS_PRODUCTIONSERIES_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_ProductionSeries_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), DS_SERIES_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Platform_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:DS_Series_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DS_PLATFORM_TYPE_TYPE = build_DS_PLATFORM_TYPE_TYPE();
    
    private static ComplexType build_DS_PLATFORM_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Platform_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), DS_SERIES_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_ResponsibleParty_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Identification of, and means of communication with, person(s) and organisations associated with the dataset&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="individualName" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="organisationName" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="positionName" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="contactInfo" type="gmd:CI_Contact_PropertyType"/&gt;
     *                  &lt;xs:element name="role" type="gmd:CI_RoleCode_PropertyType"/&gt;
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
    public static final ComplexType CI_RESPONSIBLEPARTY_TYPE_TYPE = build_CI_RESPONSIBLEPARTY_TYPE_TYPE();
    
    private static ComplexType build_CI_RESPONSIBLEPARTY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_DigitalTransferOptions_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Technical means and media by which a dataset is obtained from the distributor&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="unitsOfDistribution" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="transferSize" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="onLine" type="gmd:CI_OnlineResource_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="offLine" type="gmd:MD_Medium_PropertyType"/&gt;
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
    public static final ComplexType MD_DIGITALTRANSFEROPTIONS_TYPE_TYPE = build_MD_DIGITALTRANSFEROPTIONS_TYPE_TYPE();
    
    private static ComplexType build_MD_DIGITALTRANSFEROPTIONS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_Extent_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about spatial, vertical, and temporal extent&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="description" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="geographicElement" type="gmd:EX_GeographicExtent_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="temporalElement" type="gmd:EX_TemporalExtent_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="verticalElement" type="gmd:EX_VerticalExtent_PropertyType"/&gt;
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
    public static final ComplexType EX_EXTENT_TYPE_TYPE = build_EX_EXTENT_TYPE_TYPE();
    
    private static ComplexType build_EX_EXTENT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_Extent_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_Completeness_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractDQ_Completeness"/&gt;
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
    public static final ComplexType DQ_COMPLETENESS_PROPERTYTYPE_TYPE = build_DQ_COMPLETENESS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_COMPLETENESS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Completeness_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_DataQuality_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_DataQuality"/&gt;
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
    public static final ComplexType DQ_DATAQUALITY_PROPERTYTYPE_TYPE = build_DQ_DATAQUALITY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_DATAQUALITY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DataQuality_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_MetadataExtensionInformation_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information describing metadata extensions.&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="extensionOnLineResource" type="gmd:CI_OnlineResource_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="extendedElementInformation" type="gmd:MD_ExtendedElementInformation_PropertyType"/&gt;
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
    public static final ComplexType MD_METADATAEXTENSIONINFORMATION_TYPE_TYPE = build_MD_METADATAEXTENSIONINFORMATION_TYPE_TYPE();
    
    private static ComplexType build_MD_METADATAEXTENSIONINFORMATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_RepresentativeFraction_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_RepresentativeFraction"/&gt;
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
    public static final ComplexType MD_REPRESENTATIVEFRACTION_PROPERTYTYPE_TYPE = build_MD_REPRESENTATIVEFRACTION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_REPRESENTATIVEFRACTION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_InitiativeTypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_InitiativeTypeCode"/&gt;
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
    public static final ComplexType DS_INITIATIVETYPECODE_PROPERTYTYPE_TYPE = build_DS_INITIATIVETYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_INITIATIVETYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_InitiativeTypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ContentInformation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractMD_ContentInformation"/&gt;
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
    public static final ComplexType MD_CONTENTINFORMATION_PROPERTYTYPE_TYPE = build_MD_CONTENTINFORMATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_CONTENTINFORMATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ContentInformation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_ResponsibleParty_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_ResponsibleParty"/&gt;
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
    public static final ComplexType CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE = build_CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Keywords_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Keywords"/&gt;
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
    public static final ComplexType MD_KEYWORDS_PROPERTYTYPE_TYPE = build_MD_KEYWORDS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_KEYWORDS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Keywords_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Telephone_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Telephone numbers for contacting the responsible individual or organisation&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="voice" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="facsimile" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType CI_TELEPHONE_TYPE_TYPE = build_CI_TELEPHONE_TYPE_TYPE();
    
    private static ComplexType build_CI_TELEPHONE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Telephone_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ExtendedElementInformation_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;New metadata element, not found in ISO 19115, which is required to describe geographic data&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="name" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="shortName" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="domainCode" type="gco:Integer_PropertyType"/&gt;
     *                  &lt;xs:element name="definition" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="obligation" type="gmd:MD_ObligationCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="condition" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="dataType" type="gmd:MD_DatatypeCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="maximumOccurrence" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="domainValue" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="parentEntity" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="rule" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="rationale" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="source" type="gmd:CI_ResponsibleParty_PropertyType"/&gt;
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
    public static final ComplexType MD_EXTENDEDELEMENTINFORMATION_TYPE_TYPE = build_MD_EXTENDEDELEMENTINFORMATION_TYPE_TYPE();
    
    private static ComplexType build_MD_EXTENDEDELEMENTINFORMATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_GriddedDataPositionalAccuracy_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_PositionalAccuracy_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_GRIDDEDDATAPOSITIONALACCURACY_TYPE_TYPE = build_DQ_GRIDDEDDATAPOSITIONALACCURACY_TYPE_TYPE();
    
    private static ComplexType build_DQ_GRIDDEDDATAPOSITIONALACCURACY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PT_LocaleContainer_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:PT_LocaleContainer"/&gt;
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
    public static final ComplexType PT_LOCALECONTAINER_PROPERTYTYPE_TYPE = build_PT_LOCALECONTAINER_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_PT_LOCALECONTAINER_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","PT_LocaleContainer_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="MD_TopicCategoryCode_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;High-level geospatial data thematic classification to assist in the grouping and search of available geospatial datasets&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:restriction base="xs:string"&gt;
     *          &lt;xs:enumeration value="farming"/&gt;
     *          &lt;xs:enumeration value="biota"/&gt;
     *          &lt;xs:enumeration value="boundaries"/&gt;
     *          &lt;xs:enumeration value="climatologyMeteorologyAtmosphere"/&gt;
     *          &lt;xs:enumeration value="economy"/&gt;
     *          &lt;xs:enumeration value="elevation"/&gt;
     *          &lt;xs:enumeration value="environment"/&gt;
     *          &lt;xs:enumeration value="geoscientificInformation"/&gt;
     *          &lt;xs:enumeration value="health"/&gt;
     *          &lt;xs:enumeration value="imageryBaseMapsEarthCover"/&gt;
     *          &lt;xs:enumeration value="intelligenceMilitary"/&gt;
     *          &lt;xs:enumeration value="inlandWaters"/&gt;
     *          &lt;xs:enumeration value="location"/&gt;
     *          &lt;xs:enumeration value="oceans"/&gt;
     *          &lt;xs:enumeration value="planningCadastre"/&gt;
     *          &lt;xs:enumeration value="society"/&gt;
     *          &lt;xs:enumeration value="structure"/&gt;
     *          &lt;xs:enumeration value="transportation"/&gt;
     *          &lt;xs:enumeration value="utilitiesCommunication"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MD_TOPICCATEGORYCODE_TYPE_TYPE = build_MD_TOPICCATEGORYCODE_TYPE_TYPE();
     
    private static AttributeType build_MD_TOPICCATEGORYCODE_TYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode_Type"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_Scope_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="level" type="gmd:MD_ScopeCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="extent" type="gmd:EX_Extent_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="levelDescription" type="gmd:MD_ScopeDescription_PropertyType"/&gt;
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
    public static final ComplexType DQ_SCOPE_TYPE_TYPE = build_DQ_SCOPE_TYPE_TYPE();
    
    private static ComplexType build_DQ_SCOPE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Scope_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_FeatureCatalogueDescription_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_FeatureCatalogueDescription"/&gt;
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
    public static final ComplexType MD_FEATURECATALOGUEDESCRIPTION_PROPERTYTYPE_TYPE = build_MD_FEATURECATALOGUEDESCRIPTION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_FEATURECATALOGUEDESCRIPTION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_DomainConsistency_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_DomainConsistency"/&gt;
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
    public static final ComplexType DQ_DOMAINCONSISTENCY_PROPERTYTYPE_TYPE = build_DQ_DOMAINCONSISTENCY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_DOMAINCONSISTENCY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_DistributionUnits_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_DistributionUnits"/&gt;
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
    public static final ComplexType MD_DISTRIBUTIONUNITS_PROPERTYTYPE_TYPE = build_MD_DISTRIBUTIONUNITS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_DISTRIBUTIONUNITS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_DistributionUnits_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ClassificationCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ClassificationCode"/&gt;
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
    public static final ComplexType MD_CLASSIFICATIONCODE_PROPERTYTYPE_TYPE = build_MD_CLASSIFICATIONCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_CLASSIFICATIONCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ClassificationCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_StereoMate_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_StereoMate"/&gt;
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
    public static final ComplexType DS_STEREOMATE_PROPERTYTYPE_TYPE = build_DS_STEREOMATE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_STEREOMATE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_StereoMate_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_TemporalExtent_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Time period covered by the content of the dataset&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="extent" type="gts:TM_Primitive_PropertyType"/&gt;
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
    public static final ComplexType EX_TEMPORALEXTENT_TYPE_TYPE = build_EX_TEMPORALEXTENT_TYPE_TYPE();
    
    private static ComplexType build_EX_TEMPORALEXTENT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_TemporalExtent_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_SpatialTemporalExtent_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Extent with respect to date and time&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:EX_TemporalExtent_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="spatialExtent" type="gmd:EX_GeographicExtent_PropertyType"/&gt;
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
    public static final ComplexType EX_SPATIALTEMPORALEXTENT_TYPE_TYPE = build_EX_SPATIALTEMPORALEXTENT_TYPE_TYPE();
    
    private static ComplexType build_EX_SPATIALTEMPORALEXTENT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), EX_TEMPORALEXTENT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_CellGeometryCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_CellGeometryCode"/&gt;
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
    public static final ComplexType MD_CELLGEOMETRYCODE_PROPERTYTYPE_TYPE = build_MD_CELLGEOMETRYCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_CELLGEOMETRYCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_CellGeometryCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Identifier_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="authority" type="gmd:CI_Citation_PropertyType"/&gt;
     *                  &lt;xs:element name="code" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_IDENTIFIER_TYPE_TYPE = build_MD_IDENTIFIER_TYPE_TYPE();
    
    private static ComplexType build_MD_IDENTIFIER_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identifier_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="RS_Identifier_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:MD_Identifier_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="codeSpace" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="version" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType RS_IDENTIFIER_TYPE_TYPE = build_RS_IDENTIFIER_TYPE_TYPE();
    
    private static ComplexType build_RS_IDENTIFIER_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","RS_Identifier_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MD_IDENTIFIER_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="URL_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:URL"/&gt;
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
    public static final ComplexType URL_PROPERTYTYPE_TYPE = build_URL_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_URL_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","URL_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_QuantitativeResult_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_QuantitativeResult"/&gt;
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
    public static final ComplexType DQ_QUANTITATIVERESULT_PROPERTYTYPE_TYPE = build_DQ_QUANTITATIVERESULT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_QUANTITATIVERESULT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractDQ_LogicalConsistency_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_Element_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE = build_ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_LogicalConsistency_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTDQ_ELEMENT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_FormatConsistency_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_LogicalConsistency_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_FORMATCONSISTENCY_TYPE_TYPE = build_DQ_FORMATCONSISTENCY_TYPE_TYPE();
    
    private static ComplexType build_DQ_FORMATCONSISTENCY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_PositionalAccuracy_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractDQ_PositionalAccuracy"/&gt;
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
    public static final ComplexType DQ_POSITIONALACCURACY_PROPERTYTYPE_TYPE = build_DQ_POSITIONALACCURACY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_POSITIONALACCURACY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_PositionalAccuracy_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_BrowseGraphic_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_BrowseGraphic"/&gt;
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
    public static final ComplexType MD_BROWSEGRAPHIC_PROPERTYTYPE_TYPE = build_MD_BROWSEGRAPHIC_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_BROWSEGRAPHIC_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_TemporalExtent_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:EX_TemporalExtent"/&gt;
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
    public static final ComplexType EX_TEMPORALEXTENT_PROPERTYTYPE_TYPE = build_EX_TEMPORALEXTENT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_EX_TEMPORALEXTENT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_TemporalExtent_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="RS_Identifier_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:RS_Identifier"/&gt;
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
    public static final ComplexType RS_IDENTIFIER_PROPERTYTYPE_TYPE = build_RS_IDENTIFIER_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_RS_IDENTIFIER_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","RS_Identifier_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Sensor_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_Sensor"/&gt;
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
    public static final ComplexType DS_SENSOR_PROPERTYTYPE_TYPE = build_DS_SENSOR_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_SENSOR_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Sensor_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractMD_ContentInformation_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence/&gt;
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
    public static final ComplexType ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE = build_ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_ContentInformation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_CoverageDescription_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about the domain of the raster cell&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractMD_ContentInformation_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="attributeDescription" type="gco:RecordType_PropertyType"/&gt;
     *                  &lt;xs:element name="contentType" type="gmd:MD_CoverageContentTypeCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="dimension" type="gmd:MD_RangeDimension_PropertyType"/&gt;
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
    public static final ComplexType MD_COVERAGEDESCRIPTION_TYPE_TYPE = build_MD_COVERAGEDESCRIPTION_TYPE_TYPE();
    
    private static ComplexType build_MD_COVERAGEDESCRIPTION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageDescription_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ImageDescription_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about an image's suitability for use&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:MD_CoverageDescription_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="illuminationElevationAngle" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="illuminationAzimuthAngle" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="imagingCondition" type="gmd:MD_ImagingConditionCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="imageQualityCode" type="gmd:MD_Identifier_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="cloudCoverPercentage" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="processingLevelCode" type="gmd:MD_Identifier_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="compressionGenerationQuantity" type="gco:Integer_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="triangulationIndicator" type="gco:Boolean_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="radiometricCalibrationDataAvailability" type="gco:Boolean_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="cameraCalibrationInformationAvailability" type="gco:Boolean_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="filmDistortionInformationAvailability" type="gco:Boolean_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="lensDistortionInformationAvailability" type="gco:Boolean_PropertyType"/&gt;
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
    public static final ComplexType MD_IMAGEDESCRIPTION_TYPE_TYPE = build_MD_IMAGEDESCRIPTION_TYPE_TYPE();
    
    private static ComplexType build_MD_IMAGEDESCRIPTION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImageDescription_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MD_COVERAGEDESCRIPTION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_PortrayalCatalogueReference_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information identifing the portrayal catalogue used&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="portrayalCatalogueCitation" type="gmd:CI_Citation_PropertyType"/&gt;
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
    public static final ComplexType MD_PORTRAYALCATALOGUEREFERENCE_TYPE_TYPE = build_MD_PORTRAYALCATALOGUEREFERENCE_TYPE_TYPE();
    
    private static ComplexType build_MD_PORTRAYALCATALOGUEREFERENCE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_RoleCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_RoleCode"/&gt;
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
    public static final ComplexType CI_ROLECODE_PROPERTYTYPE_TYPE = build_CI_ROLECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_ROLECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_RoleCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="Country_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:Country"/&gt;
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
    public static final ComplexType COUNTRY_PROPERTYTYPE_TYPE = build_COUNTRY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_COUNTRY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","Country_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ApplicationSchemaInformation_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about the application schema used to build the dataset&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="name" type="gmd:CI_Citation_PropertyType"/&gt;
     *                  &lt;xs:element name="schemaLanguage" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="constraintLanguage" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="schemaAscii" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="graphicsFile" type="gco:Binary_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="softwareDevelopmentFile" type="gco:Binary_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="softwareDevelopmentFileFormat" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_APPLICATIONSCHEMAINFORMATION_TYPE_TYPE = build_MD_APPLICATIONSCHEMAINFORMATION_TYPE_TYPE();
    
    private static ComplexType build_MD_APPLICATIONSCHEMAINFORMATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_AggregateInformation_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Encapsulates the dataset aggregation information&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="aggregateDataSetName" type="gmd:CI_Citation_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="aggregateDataSetIdentifier" type="gmd:MD_Identifier_PropertyType"/&gt;
     *                  &lt;xs:element name="associationType" type="gmd:DS_AssociationTypeCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="initiativeType" type="gmd:DS_InitiativeTypeCode_PropertyType"/&gt;
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
    public static final ComplexType MD_AGGREGATEINFORMATION_TYPE_TYPE = build_MD_AGGREGATEINFORMATION_TYPE_TYPE();
    
    private static ComplexType build_MD_AGGREGATEINFORMATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_AggregateInformation_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_StandardOrderProcess_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Common ways in which the dataset may be obtained or received, and related instructions and fee information&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="fees" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="plannedAvailableDateTime" type="gco:DateTime_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="orderingInstructions" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="turnaround" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_STANDARDORDERPROCESS_TYPE_TYPE = build_MD_STANDARDORDERPROCESS_TYPE_TYPE();
    
    private static ComplexType build_MD_STANDARDORDERPROCESS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Contact_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_Contact"/&gt;
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
    public static final ComplexType CI_CONTACT_PROPERTYTYPE_TYPE = build_CI_CONTACT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_CONTACT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Contact_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_DataQuality_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="scope" type="gmd:DQ_Scope_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="report" type="gmd:DQ_Element_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="lineage" type="gmd:LI_Lineage_PropertyType"/&gt;
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
    public static final ComplexType DQ_DATAQUALITY_TYPE_TYPE = build_DQ_DATAQUALITY_TYPE_TYPE();
    
    private static ComplexType build_DQ_DATAQUALITY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DataQuality_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Format_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Format"/&gt;
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
    public static final ComplexType MD_FORMAT_PROPERTYTYPE_TYPE = build_MD_FORMAT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_FORMAT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Format_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ScopeDescription_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Description of the class of information covered by the information&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:choice&gt;
     *          &lt;xs:element maxOccurs="unbounded" name="attributes" type="gco:ObjectReference_PropertyType"/&gt;
     *          &lt;xs:element maxOccurs="unbounded" name="features" type="gco:ObjectReference_PropertyType"/&gt;
     *          &lt;xs:element maxOccurs="unbounded" name="featureInstances" type="gco:ObjectReference_PropertyType"/&gt;
     *          &lt;xs:element maxOccurs="unbounded" name="attributeInstances" type="gco:ObjectReference_PropertyType"/&gt;
     *          &lt;xs:element name="dataset" type="gco:CharacterString_PropertyType"/&gt;
     *          &lt;xs:element name="other" type="gco:CharacterString_PropertyType"/&gt;
     *      &lt;/xs:choice&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MD_SCOPEDESCRIPTION_TYPE_TYPE = build_MD_SCOPEDESCRIPTION_TYPE_TYPE();
    
    private static ComplexType build_MD_SCOPEDESCRIPTION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeDescription_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PT_LocaleContainer_Type"&gt;
     *      &lt;xs:sequence&gt;
     *          &lt;xs:element name="description" type="gco:CharacterString_PropertyType"/&gt;
     *          &lt;xs:element name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *          &lt;xs:element maxOccurs="unbounded" name="date" type="gmd:CI_Date_PropertyType"/&gt;
     *          &lt;xs:element maxOccurs="unbounded" name="responsibleParty" type="gmd:CI_ResponsibleParty_PropertyType"/&gt;
     *          &lt;xs:element maxOccurs="unbounded" name="localisedString" type="gmd:LocalisedCharacterString_PropertyType"/&gt;
     *      &lt;/xs:sequence&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType PT_LOCALECONTAINER_TYPE_TYPE = build_PT_LOCALECONTAINER_TYPE_TYPE();
    
    private static ComplexType build_PT_LOCALECONTAINER_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","PT_LocaleContainer_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_DataSet_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_DataSet"/&gt;
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
    public static final ComplexType DS_DATASET_PROPERTYTYPE_TYPE = build_DS_DATASET_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_DATASET_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_DataSet_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_ConformanceResult_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_ConformanceResult"/&gt;
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
    public static final ComplexType DQ_CONFORMANCERESULT_PROPERTYTYPE_TYPE = build_DQ_CONFORMANCERESULT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_CONFORMANCERESULT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_VectorSpatialRepresentation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_VectorSpatialRepresentation"/&gt;
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
    public static final ComplexType MD_VECTORSPATIALREPRESENTATION_PROPERTYTYPE_TYPE = build_MD_VECTORSPATIALREPRESENTATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_VECTORSPATIALREPRESENTATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ReferenceSystem_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="referenceSystemIdentifier" type="gmd:RS_Identifier_PropertyType"/&gt;
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
    public static final ComplexType MD_REFERENCESYSTEM_TYPE_TYPE = build_MD_REFERENCESYSTEM_TYPE_TYPE();
    
    private static ComplexType build_MD_REFERENCESYSTEM_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_GeographicDescription_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:EX_GeographicDescription"/&gt;
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
    public static final ComplexType EX_GEOGRAPHICDESCRIPTION_PROPERTYTYPE_TYPE = build_EX_GEOGRAPHICDESCRIPTION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_EX_GEOGRAPHICDESCRIPTION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicDescription_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_OtherAggregate_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDS_Aggregate_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DS_OTHERAGGREGATE_TYPE_TYPE = build_DS_OTHERAGGREGATE_TYPE_TYPE();
    
    private static ComplexType build_DS_OTHERAGGREGATE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_OtherAggregate_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDS_AGGREGATE_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_StereoMate_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:DS_OtherAggregate_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DS_STEREOMATE_TYPE_TYPE = build_DS_STEREOMATE_TYPE_TYPE();
    
    private static ComplexType build_DS_STEREOMATE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_StereoMate_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), DS_OTHERAGGREGATE_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_CoverageContentTypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_CoverageContentTypeCode"/&gt;
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
    public static final ComplexType MD_COVERAGECONTENTTYPECODE_PROPERTYTYPE_TYPE = build_MD_COVERAGECONTENTTYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_COVERAGECONTENTTYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageContentTypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_Result_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractDQ_Result"/&gt;
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
    public static final ComplexType DQ_RESULT_PROPERTYTYPE_TYPE = build_DQ_RESULT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_RESULT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Result_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Metadata_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about the metadata&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="fileIdentifier" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="language" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="characterSet" type="gmd:MD_CharacterSetCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="parentIdentifier" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="hierarchyLevel" type="gmd:MD_ScopeCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="hierarchyLevelName" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="contact" type="gmd:CI_ResponsibleParty_PropertyType"/&gt;
     *                  &lt;xs:element name="dateStamp" type="gco:Date_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="metadataStandardName" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="metadataStandardVersion" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="dataSetURI" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="locale" type="gmd:PT_Locale_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="spatialRepresentationInfo" type="gmd:MD_SpatialRepresentation_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="referenceSystemInfo" type="gmd:MD_ReferenceSystem_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="metadataExtensionInfo" type="gmd:MD_MetadataExtensionInformation_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="identificationInfo" type="gmd:MD_Identification_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="contentInfo" type="gmd:MD_ContentInformation_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="distributionInfo" type="gmd:MD_Distribution_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="dataQualityInfo" type="gmd:DQ_DataQuality_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="portrayalCatalogueInfo" type="gmd:MD_PortrayalCatalogueReference_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="metadataConstraints" type="gmd:MD_Constraints_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="applicationSchemaInfo" type="gmd:MD_ApplicationSchemaInformation_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="metadataMaintenance" type="gmd:MD_MaintenanceInformation_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="series" type="gmd:DS_Aggregate_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="describes" type="gmd:DS_DataSet_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="propertyType" type="gco:ObjectReference_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="featureType" type="gco:ObjectReference_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="featureAttribute" type="gco:ObjectReference_PropertyType"/&gt;
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
    public static final ComplexType MD_METADATA_TYPE_TYPE = build_MD_METADATA_TYPE_TYPE();
    
    private static ComplexType build_MD_METADATA_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Metadata_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Band_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Band"/&gt;
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
    public static final ComplexType MD_BAND_PROPERTYTYPE_TYPE = build_MD_BAND_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_BAND_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Band_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Series_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="name" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="issueIdentification" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="page" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType CI_SERIES_TYPE_TYPE = build_CI_SERIES_TYPE_TYPE();
    
    private static ComplexType build_CI_SERIES_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Series_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_RelativeInternalPositionalAccuracy_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_PositionalAccuracy_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_RELATIVEINTERNALPOSITIONALACCURACY_TYPE_TYPE = build_DQ_RELATIVEINTERNALPOSITIONALACCURACY_TYPE_TYPE();
    
    private static ComplexType build_DQ_RELATIVEINTERNALPOSITIONALACCURACY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_Extent_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:EX_Extent"/&gt;
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
    public static final ComplexType EX_EXTENT_PROPERTYTYPE_TYPE = build_EX_EXTENT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_EX_EXTENT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_Extent_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_TemporalValidity_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_TemporalValidity"/&gt;
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
    public static final ComplexType DQ_TEMPORALVALIDITY_PROPERTYTYPE_TYPE = build_DQ_TEMPORALVALIDITY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_TEMPORALVALIDITY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="MD_ObligationCode_Type"&gt;
     *      &lt;xs:restriction base="xs:string"&gt;
     *          &lt;xs:enumeration value="mandatory"/&gt;
     *          &lt;xs:enumeration value="optional"/&gt;
     *          &lt;xs:enumeration value="conditional"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MD_OBLIGATIONCODE_TYPE_TYPE = build_MD_OBLIGATIONCODE_TYPE_TYPE();
     
    private static AttributeType build_MD_OBLIGATIONCODE_TYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ObligationCode_Type"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_NonQuantitativeAttributeAccuracy_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_NonQuantitativeAttributeAccuracy"/&gt;
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
    public static final ComplexType DQ_NONQUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE = build_DQ_NONQUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_NONQUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_GridSpatialRepresentation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_GridSpatialRepresentation"/&gt;
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
    public static final ComplexType MD_GRIDSPATIALREPRESENTATION_PROPERTYTYPE_TYPE = build_MD_GRIDSPATIALREPRESENTATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_GRIDSPATIALREPRESENTATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_RangeDimension_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Set of adjacent wavelengths in the electro-magnetic spectrum with a common characteristic, such as the visible band&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="sequenceIdentifier" type="gco:MemberName_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="descriptor" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_RANGEDIMENSION_TYPE_TYPE = build_MD_RANGEDIMENSION_TYPE_TYPE();
    
    private static ComplexType build_MD_RANGEDIMENSION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_RangeDimension_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Band_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:MD_RangeDimension_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="maxValue" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="minValue" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="units" type="gco:UomLength_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="peakResponse" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="bitsPerValue" type="gco:Integer_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="toneGradation" type="gco:Integer_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="scaleFactor" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="offset" type="gco:Real_PropertyType"/&gt;
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
    public static final ComplexType MD_BAND_TYPE_TYPE = build_MD_BAND_TYPE_TYPE();
    
    private static ComplexType build_MD_BAND_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Band_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MD_RANGEDIMENSION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_DatatypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_DatatypeCode"/&gt;
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
    public static final ComplexType MD_DATATYPECODE_PROPERTYTYPE_TYPE = build_MD_DATATYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_DATATYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_DatatypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_PixelOrientationCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_PixelOrientationCode"/&gt;
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
    public static final ComplexType MD_PIXELORIENTATIONCODE_PROPERTYTYPE_TYPE = build_MD_PIXELORIENTATIONCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_PIXELORIENTATIONCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Dimension_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="dimensionName" type="gmd:MD_DimensionNameTypeCode_PropertyType"/&gt;
     *                  &lt;xs:element name="dimensionSize" type="gco:Integer_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="resolution" type="gco:Measure_PropertyType"/&gt;
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
    public static final ComplexType MD_DIMENSION_TYPE_TYPE = build_MD_DIMENSION_TYPE_TYPE();
    
    private static ComplexType build_MD_DIMENSION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Dimension_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Constraints_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Restrictions on the access and use of a dataset or metadata&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="useLimitation" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_CONSTRAINTS_TYPE_TYPE = build_MD_CONSTRAINTS_TYPE_TYPE();
    
    private static ComplexType build_MD_CONSTRAINTS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Constraints_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_LegalConstraints_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Restrictions and legal prerequisites for accessing and using the dataset.&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:MD_Constraints_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="accessConstraints" type="gmd:MD_RestrictionCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="useConstraints" type="gmd:MD_RestrictionCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="otherConstraints" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_LEGALCONSTRAINTS_TYPE_TYPE = build_MD_LEGALCONSTRAINTS_TYPE_TYPE();
    
    private static ComplexType build_MD_LEGALCONSTRAINTS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_LegalConstraints_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MD_CONSTRAINTS_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_TopologicalConsistency_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_TopologicalConsistency"/&gt;
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
    public static final ComplexType DQ_TOPOLOGICALCONSISTENCY_PROPERTYTYPE_TYPE = build_DQ_TOPOLOGICALCONSISTENCY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_TOPOLOGICALCONSISTENCY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Distributor_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Distributor"/&gt;
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
    public static final ComplexType MD_DISTRIBUTOR_PROPERTYTYPE_TYPE = build_MD_DISTRIBUTOR_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_DISTRIBUTOR_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distributor_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_TopologicalConsistency_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_LogicalConsistency_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_TOPOLOGICALCONSISTENCY_TYPE_TYPE = build_DQ_TOPOLOGICALCONSISTENCY_TYPE_TYPE();
    
    private static ComplexType build_DQ_TOPOLOGICALCONSISTENCY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PT_Locale_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:PT_Locale"/&gt;
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
    public static final ComplexType PT_LOCALE_PROPERTYTYPE_TYPE = build_PT_LOCALE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_PT_LOCALE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","PT_Locale_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Sensor_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:DS_Series_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DS_SENSOR_TYPE_TYPE = build_DS_SENSOR_TYPE_TYPE();
    
    private static ComplexType build_DS_SENSOR_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Sensor_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), DS_SERIES_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_TemporalConsistency_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_TemporalAccuracy_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_TEMPORALCONSISTENCY_TYPE_TYPE = build_DQ_TEMPORALCONSISTENCY_TYPE_TYPE();
    
    private static ComplexType build_DQ_TEMPORALCONSISTENCY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_VerticalExtent_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Vertical domain of dataset&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="minimumValue" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element name="maximumValue" type="gco:Real_PropertyType"/&gt;
     *                  &lt;xs:element name="verticalCRS" type="gsr:SC_CRS_PropertyType"/&gt;
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
    public static final ComplexType EX_VERTICALEXTENT_TYPE_TYPE = build_EX_VERTICALEXTENT_TYPE_TYPE();
    
    private static ComplexType build_EX_VERTICALEXTENT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_VerticalExtent_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PT_FreeText_PropertyType"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:CharacterString_PropertyType"&gt;
     *              &lt;xs:sequence minOccurs="0"&gt;
     *                  &lt;xs:element ref="gmd:PT_FreeText"/&gt;
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
    public static final ComplexType PT_FREETEXT_PROPERTYTYPE_TYPE = build_PT_FREETEXT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_PT_FREETEXT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","PT_FreeText_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Aggregate_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractDS_Aggregate"/&gt;
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
    public static final ComplexType DS_AGGREGATE_PROPERTYTYPE_TYPE = build_DS_AGGREGATE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_AGGREGATE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Aggregate_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_AbsoluteExternalPositionalAccuracy_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_AbsoluteExternalPositionalAccuracy"/&gt;
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
    public static final ComplexType DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE = build_DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ReferenceSystem_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ReferenceSystem"/&gt;
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
    public static final ComplexType MD_REFERENCESYSTEM_PROPERTYTYPE_TYPE = build_MD_REFERENCESYSTEM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_REFERENCESYSTEM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_PortrayalCatalogueReference_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_PortrayalCatalogueReference"/&gt;
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
    public static final ComplexType MD_PORTRAYALCATALOGUEREFERENCE_PROPERTYTYPE_TYPE = build_MD_PORTRAYALCATALOGUEREFERENCE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_PORTRAYALCATALOGUEREFERENCE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ApplicationSchemaInformation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ApplicationSchemaInformation"/&gt;
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
    public static final ComplexType MD_APPLICATIONSCHEMAINFORMATION_PROPERTYTYPE_TYPE = build_MD_APPLICATIONSCHEMAINFORMATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_APPLICATIONSCHEMAINFORMATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_OnlineResource_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_OnlineResource"/&gt;
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
    public static final ComplexType CI_ONLINERESOURCE_PROPERTYTYPE_TYPE = build_CI_ONLINERESOURCE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_ONLINERESOURCE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnlineResource_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Resolution_Type"&gt;
     *      &lt;xs:choice&gt;
     *          &lt;xs:element name="equivalentScale" type="gmd:MD_RepresentativeFraction_PropertyType"/&gt;
     *          &lt;xs:element name="distance" type="gco:Distance_PropertyType"/&gt;
     *      &lt;/xs:choice&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MD_RESOLUTION_TYPE_TYPE = build_MD_RESOLUTION_TYPE_TYPE();
    
    private static ComplexType build_MD_RESOLUTION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Resolution_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractDQ_ThematicAccuracy_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_Element_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE = build_ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_ThematicAccuracy_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), ABSTRACTDQ_ELEMENT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_ThematicClassificationCorrectness_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_ThematicAccuracy_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_THEMATICCLASSIFICATIONCORRECTNESS_TYPE_TYPE = build_DQ_THEMATICCLASSIFICATIONCORRECTNESS_TYPE_TYPE();
    
    private static ComplexType build_DQ_THEMATICCLASSIFICATIONCORRECTNESS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_BoundingPolygon_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:EX_BoundingPolygon"/&gt;
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
    public static final ComplexType EX_BOUNDINGPOLYGON_PROPERTYTYPE_TYPE = build_EX_BOUNDINGPOLYGON_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_EX_BOUNDINGPOLYGON_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractMD_Identification_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Basic information about data&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="citation" type="gmd:CI_Citation_PropertyType"/&gt;
     *                  &lt;xs:element name="abstract" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="purpose" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="credit" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="status" type="gmd:MD_ProgressCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="pointOfContact" type="gmd:CI_ResponsibleParty_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="resourceMaintenance" type="gmd:MD_MaintenanceInformation_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="graphicOverview" type="gmd:MD_BrowseGraphic_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="resourceFormat" type="gmd:MD_Format_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="descriptiveKeywords" type="gmd:MD_Keywords_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="resourceSpecificUsage" type="gmd:MD_Usage_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="resourceConstraints" type="gmd:MD_Constraints_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="aggregationInfo" type="gmd:MD_AggregateInformation_PropertyType"/&gt;
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
    public static final ComplexType ABSTRACTMD_IDENTIFICATION_TYPE_TYPE = build_ABSTRACTMD_IDENTIFICATION_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTMD_IDENTIFICATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_Identification_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_DataIdentification_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractMD_Identification_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="spatialRepresentationType" type="gmd:MD_SpatialRepresentationTypeCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="spatialResolution" type="gmd:MD_Resolution_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="language" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="characterSet" type="gmd:MD_CharacterSetCode_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="topicCategory" type="gmd:MD_TopicCategoryCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="environmentDescription" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="extent" type="gmd:EX_Extent_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="supplementalInformation" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_DATAIDENTIFICATION_TYPE_TYPE = build_MD_DATAIDENTIFICATION_TYPE_TYPE();
    
    private static ComplexType build_MD_DATAIDENTIFICATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_DataIdentification_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTMD_IDENTIFICATION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:simpleType name="MD_PixelOrientationCode_Type"&gt;
     *      &lt;xs:restriction base="xs:string"&gt;
     *          &lt;xs:enumeration value="center"/&gt;
     *          &lt;xs:enumeration value="lowerLeft"/&gt;
     *          &lt;xs:enumeration value="lowerRight"/&gt;
     *          &lt;xs:enumeration value="upperRight"/&gt;
     *          &lt;xs:enumeration value="upperLeft"/&gt;
     *      &lt;/xs:restriction&gt;
     *  &lt;/xs:simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MD_PIXELORIENTATIONCODE_TYPE_TYPE = build_MD_PIXELORIENTATIONCODE_TYPE_TYPE();
     
    private static AttributeType build_MD_PIXELORIENTATIONCODE_TYPE_TYPE() {
        AttributeType builtType;
        builtType = new AttributeTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode_Type"), java.lang.Object.class, false,
            false, Collections.<Filter>emptyList(), XSSchema.STRING_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Georectified_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:MD_GridSpatialRepresentation_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="checkPointAvailability" type="gco:Boolean_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="checkPointDescription" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="cornerPoints" type="gss:GM_Point_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="centerPoint" type="gss:GM_Point_PropertyType"/&gt;
     *                  &lt;xs:element name="pointInPixel" type="gmd:MD_PixelOrientationCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="transformationDimensionDescription" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="2" minOccurs="0"
     *                      name="transformationDimensionMapping" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_GEORECTIFIED_TYPE_TYPE = build_MD_GEORECTIFIED_TYPE_TYPE();
    
    private static ComplexType build_MD_GEORECTIFIED_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georectified_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ServiceIdentification_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ServiceIdentification"/&gt;
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
    public static final ComplexType MD_SERVICEIDENTIFICATION_PROPERTYTYPE_TYPE = build_MD_SERVICEIDENTIFICATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_SERVICEIDENTIFICATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_TemporalValidity_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_TemporalAccuracy_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_TEMPORALVALIDITY_TYPE_TYPE = build_DQ_TEMPORALVALIDITY_TYPE_TYPE();
    
    private static ComplexType build_DQ_TEMPORALVALIDITY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_DomainConsistency_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_LogicalConsistency_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_DOMAINCONSISTENCY_TYPE_TYPE = build_DQ_DOMAINCONSISTENCY_TYPE_TYPE();
    
    private static ComplexType build_DQ_DOMAINCONSISTENCY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_TemporalAccuracy_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractDQ_TemporalAccuracy"/&gt;
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
    public static final ComplexType DQ_TEMPORALACCURACY_PROPERTYTYPE_TYPE = build_DQ_TEMPORALACCURACY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_TEMPORALACCURACY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalAccuracy_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ImageDescription_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ImageDescription"/&gt;
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
    public static final ComplexType MD_IMAGEDESCRIPTION_PROPERTYTYPE_TYPE = build_MD_IMAGEDESCRIPTION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_IMAGEDESCRIPTION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImageDescription_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LanguageCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:LanguageCode"/&gt;
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
    public static final ComplexType LANGUAGECODE_PROPERTYTYPE_TYPE = build_LANGUAGECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_LANGUAGECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","LanguageCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Series_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_Series"/&gt;
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
    public static final ComplexType DS_SERIES_PROPERTYTYPE_TYPE = build_DS_SERIES_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_SERIES_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Series_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_MediumNameCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_MediumNameCode"/&gt;
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
    public static final ComplexType MD_MEDIUMNAMECODE_PROPERTYTYPE_TYPE = build_MD_MEDIUMNAMECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_MEDIUMNAMECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_MediumNameCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LI_ProcessStep_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="description" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="rationale" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="dateTime" type="gco:DateTime_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="processor" type="gmd:CI_ResponsibleParty_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="source" type="gmd:LI_Source_PropertyType"/&gt;
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
    public static final ComplexType LI_PROCESSSTEP_TYPE_TYPE = build_LI_PROCESSSTEP_TYPE_TYPE();
    
    private static ComplexType build_LI_PROCESSSTEP_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","LI_ProcessStep_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_SecurityConstraints_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Handling restrictions imposed on the dataset because of national security, privacy, or other concerns&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:MD_Constraints_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="classification" type="gmd:MD_ClassificationCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="userNote" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="classificationSystem" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="handlingDescription" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_SECURITYCONSTRAINTS_TYPE_TYPE = build_MD_SECURITYCONSTRAINTS_TYPE_TYPE();
    
    private static ComplexType build_MD_SECURITYCONSTRAINTS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), MD_CONSTRAINTS_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_CompletenessCommission_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_Completeness_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_COMPLETENESSCOMMISSION_TYPE_TYPE = build_DQ_COMPLETENESSCOMMISSION_TYPE_TYPE();
    
    private static ComplexType build_DQ_COMPLETENESSCOMMISSION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_COMPLETENESS_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Identification_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractMD_Identification"/&gt;
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
    public static final ComplexType MD_IDENTIFICATION_PROPERTYTYPE_TYPE = build_MD_IDENTIFICATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_IDENTIFICATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identification_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LI_Lineage_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="statement" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="processStep" type="gmd:LI_ProcessStep_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="source" type="gmd:LI_Source_PropertyType"/&gt;
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
    public static final ComplexType LI_LINEAGE_TYPE_TYPE = build_LI_LINEAGE_TYPE_TYPE();
    
    private static ComplexType build_LI_LINEAGE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","LI_Lineage_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_Scope_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_Scope"/&gt;
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
    public static final ComplexType DQ_SCOPE_PROPERTYTYPE_TYPE = build_DQ_SCOPE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_SCOPE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Scope_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Georectified_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Georectified"/&gt;
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
    public static final ComplexType MD_GEORECTIFIED_PROPERTYTYPE_TYPE = build_MD_GEORECTIFIED_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_GEORECTIFIED_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georectified_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_DateTypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_DateTypeCode"/&gt;
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
    public static final ComplexType CI_DATETYPECODE_PROPERTYTYPE_TYPE = build_CI_DATETYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_DATETYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_DateTypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_ConceptualConsistency_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_LogicalConsistency_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_CONCEPTUALCONSISTENCY_TYPE_TYPE = build_DQ_CONCEPTUALCONSISTENCY_TYPE_TYPE();
    
    private static ComplexType build_DQ_CONCEPTUALCONSISTENCY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Citation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_Citation"/&gt;
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
    public static final ComplexType CI_CITATION_PROPERTYTYPE_TYPE = build_CI_CITATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_CITATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Citation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_CharacterSetCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_CharacterSetCode"/&gt;
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
    public static final ComplexType MD_CHARACTERSETCODE_PROPERTYTYPE_TYPE = build_MD_CHARACTERSETCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_CHARACTERSETCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_CharacterSetCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Series_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:CI_Series"/&gt;
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
    public static final ComplexType CI_SERIES_PROPERTYTYPE_TYPE = build_CI_SERIES_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_CI_SERIES_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Series_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_AggregateInformation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_AggregateInformation"/&gt;
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
    public static final ComplexType MD_AGGREGATEINFORMATION_PROPERTYTYPE_TYPE = build_MD_AGGREGATEINFORMATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_AGGREGATEINFORMATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_AggregateInformation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_GeometricObjects_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="geometricObjectType" type="gmd:MD_GeometricObjectTypeCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="geometricObjectCount" type="gco:Integer_PropertyType"/&gt;
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
    public static final ComplexType MD_GEOMETRICOBJECTS_TYPE_TYPE = build_MD_GEOMETRICOBJECTS_TYPE_TYPE();
    
    private static ComplexType build_MD_GEOMETRICOBJECTS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjects_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_RangeDimension_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_RangeDimension"/&gt;
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
    public static final ComplexType MD_RANGEDIMENSION_PROPERTYTYPE_TYPE = build_MD_RANGEDIMENSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_RANGEDIMENSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_RangeDimension_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PT_Locale_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="languageCode" type="gmd:LanguageCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="country" type="gmd:Country_PropertyType"/&gt;
     *                  &lt;xs:element name="characterEncoding" type="gmd:MD_CharacterSetCode_PropertyType"/&gt;
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
    public static final ComplexType PT_LOCALE_TYPE_TYPE = build_PT_LOCALE_TYPE_TYPE();
    
    private static ComplexType build_PT_LOCALE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","PT_Locale_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Keywords_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Keywords, their type and reference source&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="keyword" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="type" type="gmd:MD_KeywordTypeCode_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="thesaurusName" type="gmd:CI_Citation_PropertyType"/&gt;
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
    public static final ComplexType MD_KEYWORDS_TYPE_TYPE = build_MD_KEYWORDS_TYPE_TYPE();
    
    private static ComplexType build_MD_KEYWORDS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Keywords_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType abstract="true" name="AbstractDQ_Result_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence/&gt;
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
    public static final ComplexType ABSTRACTDQ_RESULT_TYPE_TYPE = build_ABSTRACTDQ_RESULT_TYPE_TYPE();
    
    private static ComplexType build_ABSTRACTDQ_RESULT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Result_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            true, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_QuantitativeResult_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Quantitative_conformance_measure from Quality Procedures.  -  - Renamed to remove implied use limitation -  - OCL - -- result is type specified by valueDomain - result.tupleType = valueDomain&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_Result_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="valueType" type="gco:RecordType_PropertyType"/&gt;
     *                  &lt;xs:element name="valueUnit" type="gco:UnitOfMeasure_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="errorStatistic" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="value" type="gco:Record_PropertyType"/&gt;
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
    public static final ComplexType DQ_QUANTITATIVERESULT_TYPE_TYPE = build_DQ_QUANTITATIVERESULT_TYPE_TYPE();
    
    private static ComplexType build_DQ_QUANTITATIVERESULT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_RESULT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ScopeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ScopeCode"/&gt;
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
    public static final ComplexType MD_SCOPECODE_PROPERTYTYPE_TYPE = build_MD_SCOPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_SCOPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="RS_ReferenceSystem_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractRS_ReferenceSystem"/&gt;
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
    public static final ComplexType RS_REFERENCESYSTEM_PROPERTYTYPE_TYPE = build_RS_REFERENCESYSTEM_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_RS_REFERENCESYSTEM_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","RS_ReferenceSystem_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Contact_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information required enabling contact with the  responsible person and/or organisation&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="phone" type="gmd:CI_Telephone_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="address" type="gmd:CI_Address_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="onlineResource" type="gmd:CI_OnlineResource_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="hoursOfService" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="contactInstructions" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType CI_CONTACT_TYPE_TYPE = build_CI_CONTACT_TYPE_TYPE();
    
    private static ComplexType build_CI_CONTACT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Contact_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_ThematicAccuracy_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractDQ_ThematicAccuracy"/&gt;
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
    public static final ComplexType DQ_THEMATICACCURACY_PROPERTYTYPE_TYPE = build_DQ_THEMATICACCURACY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_THEMATICACCURACY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicAccuracy_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Association_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence/&gt;
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
    public static final ComplexType DS_ASSOCIATION_TYPE_TYPE = build_DS_ASSOCIATION_TYPE_TYPE();
    
    private static ComplexType build_DS_ASSOCIATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Association_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LI_ProcessStep_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:LI_ProcessStep"/&gt;
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
    public static final ComplexType LI_PROCESSSTEP_PROPERTYTYPE_TYPE = build_LI_PROCESSSTEP_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_LI_PROCESSSTEP_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","LI_ProcessStep_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Format_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Description of the form of the data to be distributed&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="name" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="version" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="amendmentNumber" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="specification" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0"
     *                      name="fileDecompressionTechnique" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="formatDistributor" type="gmd:MD_Distributor_PropertyType"/&gt;
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
    public static final ComplexType MD_FORMAT_TYPE_TYPE = build_MD_FORMAT_TYPE_TYPE();
    
    private static ComplexType build_MD_FORMAT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Format_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_ConformanceResult_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;quantitative_result from Quality Procedures -  - renamed to remove implied use limitiation.&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_Result_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="specification" type="gmd:CI_Citation_PropertyType"/&gt;
     *                  &lt;xs:element name="explanation" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="pass" type="gco:Boolean_PropertyType"/&gt;
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
    public static final ComplexType DQ_CONFORMANCERESULT_TYPE_TYPE = build_DQ_CONFORMANCERESULT_TYPE_TYPE();
    
    private static ComplexType build_DQ_CONFORMANCERESULT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_RESULT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_OnlineResource_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information about online sources from which the dataset, specification, or community profile name and extended metadata elements can be obtained.&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="linkage" type="gmd:URL_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="protocol" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="applicationProfile" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="name" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="description" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="function" type="gmd:CI_OnLineFunctionCode_PropertyType"/&gt;
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
    public static final ComplexType CI_ONLINERESOURCE_TYPE_TYPE = build_CI_ONLINERESOURCE_TYPE_TYPE();
    
    private static ComplexType build_CI_ONLINERESOURCE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnlineResource_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Initiative_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDS_Aggregate_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DS_INITIATIVE_TYPE_TYPE = build_DS_INITIATIVE_TYPE_TYPE();
    
    private static ComplexType build_DS_INITIATIVE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Initiative_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDS_AGGREGATE_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_LogicalConsistency_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractDQ_LogicalConsistency"/&gt;
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
    public static final ComplexType DQ_LOGICALCONSISTENCY_PROPERTYTYPE_TYPE = build_DQ_LOGICALCONSISTENCY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_LOGICALCONSISTENCY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_LogicalConsistency_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_KeywordTypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_KeywordTypeCode"/&gt;
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
    public static final ComplexType MD_KEYWORDTYPECODE_PROPERTYTYPE_TYPE = build_MD_KEYWORDTYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_KEYWORDTYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_KeywordTypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_DigitalTransferOptions_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_DigitalTransferOptions"/&gt;
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
    public static final ComplexType MD_DIGITALTRANSFEROPTIONS_PROPERTYTYPE_TYPE = build_MD_DIGITALTRANSFEROPTIONS_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_DIGITALTRANSFEROPTIONS_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_ConceptualConsistency_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_ConceptualConsistency"/&gt;
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
    public static final ComplexType DQ_CONCEPTUALCONSISTENCY_PROPERTYTYPE_TYPE = build_DQ_CONCEPTUALCONSISTENCY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_CONCEPTUALCONSISTENCY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_Element_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:AbstractDQ_Element"/&gt;
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
    public static final ComplexType DQ_ELEMENT_PROPERTYTYPE_TYPE = build_DQ_ELEMENT_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_ELEMENT_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Element_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_MaintenanceFrequencyCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_MaintenanceFrequencyCode"/&gt;
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
    public static final ComplexType MD_MAINTENANCEFREQUENCYCODE_PROPERTYTYPE_TYPE = build_MD_MAINTENANCEFREQUENCYCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_MAINTENANCEFREQUENCYCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceFrequencyCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LI_Lineage_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:LI_Lineage"/&gt;
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
    public static final ComplexType LI_LINEAGE_PROPERTYTYPE_TYPE = build_LI_LINEAGE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_LI_LINEAGE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","LI_Lineage_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_FormatConsistency_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_FormatConsistency"/&gt;
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
    public static final ComplexType DQ_FORMATCONSISTENCY_PROPERTYTYPE_TYPE = build_DQ_FORMATCONSISTENCY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_FORMATCONSISTENCY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Distribution_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Distribution"/&gt;
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
    public static final ComplexType MD_DISTRIBUTION_PROPERTYTYPE_TYPE = build_MD_DISTRIBUTION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_DISTRIBUTION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distribution_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_ProductionSeries_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_ProductionSeries"/&gt;
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
    public static final ComplexType DS_PRODUCTIONSERIES_PROPERTYTYPE_TYPE = build_DS_PRODUCTIONSERIES_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_PRODUCTIONSERIES_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_ProductionSeries_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_QuantitativeAttributeAccuracy_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_ThematicAccuracy_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_QUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE = build_DQ_QUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE();
    
    private static ComplexType build_DQ_QUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="LocalisedCharacterString_PropertyType"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:ObjectReference_PropertyType"&gt;
     *              &lt;xs:sequence minOccurs="0"&gt;
     *                  &lt;xs:element ref="gmd:LocalisedCharacterString"/&gt;
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
    public static final ComplexType LOCALISEDCHARACTERSTRING_PROPERTYTYPE_TYPE = build_LOCALISEDCHARACTERSTRING_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_LOCALISEDCHARACTERSTRING_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","LocalisedCharacterString_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.OBJECTREFERENCE_PROPERTYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Date_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="date" type="gco:Date_PropertyType"/&gt;
     *                  &lt;xs:element name="dateType" type="gmd:CI_DateTypeCode_PropertyType"/&gt;
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
    public static final ComplexType CI_DATE_TYPE_TYPE = build_CI_DATE_TYPE_TYPE();
    
    private static ComplexType build_CI_DATE_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Date_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="PT_FreeText_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="textGroup" type="gmd:LocalisedCharacterString_PropertyType"/&gt;
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
    public static final ComplexType PT_FREETEXT_TYPE_TYPE = build_PT_FREETEXT_TYPE_TYPE();
    
    private static ComplexType build_PT_FREETEXT_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","PT_FreeText_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ServiceIdentification_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;See 19119 for further info&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractMD_Identification_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType MD_SERVICEIDENTIFICATION_TYPE_TYPE = build_MD_SERVICEIDENTIFICATION_TYPE_TYPE();
    
    private static ComplexType build_MD_SERVICEIDENTIFICATION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTMD_IDENTIFICATION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_Initiative_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_Initiative"/&gt;
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
    public static final ComplexType DS_INITIATIVE_PROPERTYTYPE_TYPE = build_DS_INITIATIVE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_INITIATIVE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_Initiative_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Identifier_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Identifier"/&gt;
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
    public static final ComplexType MD_IDENTIFIER_PROPERTYTYPE_TYPE = build_MD_IDENTIFIER_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_IDENTIFIER_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identifier_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_NonQuantitativeAttributeAccuracy_Type"&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractDQ_ThematicAccuracy_Type"/&gt;
     *      &lt;/xs:complexContent&gt;
     *  &lt;/xs:complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final ComplexType DQ_NONQUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE = build_DQ_NONQUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE();
    
    private static ComplexType build_DQ_NONQUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_MaintenanceInformation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_MaintenanceInformation"/&gt;
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
    public static final ComplexType MD_MAINTENANCEINFORMATION_PROPERTYTYPE_TYPE = build_MD_MAINTENANCEINFORMATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_MAINTENANCEINFORMATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ScopeDescription_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ScopeDescription"/&gt;
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
    public static final ComplexType MD_SCOPEDESCRIPTION_PROPERTYTYPE_TYPE = build_MD_SCOPEDESCRIPTION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_SCOPEDESCRIPTION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeDescription_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="CI_Address_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Location of the responsible individual or organisation&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="deliveryPoint" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="city" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="administrativeArea" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="postalCode" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="country" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="electronicMailAddress" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType CI_ADDRESS_TYPE_TYPE = build_CI_ADDRESS_TYPE_TYPE();
    
    private static ComplexType build_CI_ADDRESS_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","CI_Address_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_GeometricObjectTypeCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_GeometricObjectTypeCode"/&gt;
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
    public static final ComplexType MD_GEOMETRICOBJECTTYPECODE_PROPERTYTYPE_TYPE = build_MD_GEOMETRICOBJECTTYPECODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_GEOMETRICOBJECTTYPECODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjectTypeCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DS_OtherAggregate_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DS_OtherAggregate"/&gt;
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
    public static final ComplexType DS_OTHERAGGREGATE_PROPERTYTYPE_TYPE = build_DS_OTHERAGGREGATE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DS_OTHERAGGREGATE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DS_OtherAggregate_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Georeferenceable_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Georeferenceable"/&gt;
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
    public static final ComplexType MD_GEOREFERENCEABLE_PROPERTYTYPE_TYPE = build_MD_GEOREFERENCEABLE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_GEOREFERENCEABLE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georeferenceable_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_CompletenessCommission_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_CompletenessCommission"/&gt;
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
    public static final ComplexType DQ_COMPLETENESSCOMMISSION_PROPERTYTYPE_TYPE = build_DQ_COMPLETENESSCOMMISSION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_COMPLETENESSCOMMISSION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="EX_BoundingPolygon_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Boundary enclosing the dataset expressed as the closed set of (x,y) coordinates of the polygon (last point replicates first point)&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractEX_GeographicExtent_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element maxOccurs="unbounded" name="polygon" type="gss:GM_Object_PropertyType"/&gt;
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
    public static final ComplexType EX_BOUNDINGPOLYGON_TYPE_TYPE = build_EX_BOUNDINGPOLYGON_TYPE_TYPE();
    
    private static ComplexType build_EX_BOUNDINGPOLYGON_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_BrowseGraphic_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Graphic that provides an illustration of the dataset (should include a legend for the graphic)&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gco:AbstractObject_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element name="fileName" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="fileDescription" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element minOccurs="0" name="fileType" type="gco:CharacterString_PropertyType"/&gt;
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
    public static final ComplexType MD_BROWSEGRAPHIC_TYPE_TYPE = build_MD_BROWSEGRAPHIC_TYPE_TYPE();
    
    private static ComplexType build_MD_BROWSEGRAPHIC_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), GCOSchema.ABSTRACTOBJECT_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_FeatureCatalogueDescription_Type"&gt;
     *      &lt;xs:annotation&gt;
     *          &lt;xs:documentation&gt;Information identifing the feature catalogue&lt;/xs:documentation&gt;
     *      &lt;/xs:annotation&gt;
     *      &lt;xs:complexContent&gt;
     *          &lt;xs:extension base="gmd:AbstractMD_ContentInformation_Type"&gt;
     *              &lt;xs:sequence&gt;
     *                  &lt;xs:element minOccurs="0" name="complianceCode" type="gco:Boolean_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="language" type="gco:CharacterString_PropertyType"/&gt;
     *                  &lt;xs:element name="includedWithDataset" type="gco:Boolean_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded" minOccurs="0"
     *                      name="featureTypes" type="gco:GenericName_PropertyType"/&gt;
     *                  &lt;xs:element maxOccurs="unbounded"
     *                      name="featureCatalogueCitation" type="gmd:CI_Citation_PropertyType"/&gt;
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
    public static final ComplexType MD_FEATURECATALOGUEDESCRIPTION_TYPE_TYPE = build_MD_FEATURECATALOGUEDESCRIPTION_TYPE_TYPE();
    
    private static ComplexType build_MD_FEATURECATALOGUEDESCRIPTION_TYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription_Type"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_Resolution_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_Resolution"/&gt;
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
    public static final ComplexType MD_RESOLUTION_PROPERTYTYPE_TYPE = build_MD_RESOLUTION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_RESOLUTION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_Resolution_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="DQ_TemporalConsistency_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:DQ_TemporalConsistency"/&gt;
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
    public static final ComplexType DQ_TEMPORALCONSISTENCY_PROPERTYTYPE_TYPE = build_DQ_TEMPORALCONSISTENCY_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_DQ_TEMPORALCONSISTENCY_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ExtendedElementInformation_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ExtendedElementInformation"/&gt;
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
    public static final ComplexType MD_EXTENDEDELEMENTINFORMATION_PROPERTYTYPE_TYPE = build_MD_EXTENDEDELEMENTINFORMATION_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_EXTENDEDELEMENTINFORMATION_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;xs:complexType name="MD_ImagingConditionCode_PropertyType"&gt;
     *      &lt;xs:sequence minOccurs="0"&gt;
     *          &lt;xs:element ref="gmd:MD_ImagingConditionCode"/&gt;
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
    public static final ComplexType MD_IMAGINGCONDITIONCODE_PROPERTYTYPE_TYPE = build_MD_IMAGINGCONDITIONCODE_PROPERTYTYPE_TYPE();
    
    private static ComplexType build_MD_IMAGINGCONDITIONCODE_PROPERTYTYPE_TYPE() {
        ComplexType builtType;
        builtType = new ComplexTypeImpl(
            new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImagingConditionCode_PropertyType"), Collections.<PropertyDescriptor>emptyList(), false,
            false, Collections.<Filter>emptyList(), XSSchema.ANYTYPE_TYPE, null
        );
        return builtType;
    }


    public GMDSchema() {
        super("http://www.isotc211.org/2005/gmd");

        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_SpatialRepresentation_Type"),ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation_Type"),MD_VECTORSPATIALREPRESENTATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_RestrictionCode_PropertyType"),MD_RESTRICTIONCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Telephone_PropertyType"),CI_TELEPHONE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","LI_Source_PropertyType"),LI_SOURCE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Usage_PropertyType"),MD_USAGE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Element_Type"),ABSTRACTDQ_ELEMENT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Completeness_Type"),ABSTRACTDQ_COMPLETENESS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission_Type"),DQ_COMPLETENESSOMISSION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation_Type"),MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georeferenceable_Type"),MD_GEOREFERENCEABLE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjects_PropertyType"),MD_GEOMETRICOBJECTS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distribution_Type"),MD_DISTRIBUTION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopologyLevelCode_PropertyType"),MD_TOPOLOGYLEVELCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_VerticalExtent_PropertyType"),EX_VERTICALEXTENT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_PresentationFormCode_PropertyType"),CI_PRESENTATIONFORMCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Platform_PropertyType"),DS_PLATFORM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Medium_PropertyType"),MD_MEDIUM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_MediumFormatCode_PropertyType"),MD_MEDIUMFORMATCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode_PropertyType"),MD_TOPICCATEGORYCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement_PropertyType"),DQ_ACCURACYOFATIMEMEASUREMENT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicExtent_PropertyType"),EX_GEOGRAPHICEXTENT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission_PropertyType"),DQ_COMPLETENESSOMISSION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Date_PropertyType"),CI_DATE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","LocalisedCharacterString_Type"),LOCALISEDCHARACTERSTRING_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Metadata_PropertyType"),MD_METADATA_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ProgressCode_PropertyType"),MD_PROGRESSCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent_PropertyType"),EX_SPATIALTEMPORALEXTENT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Usage_Type"),MD_USAGE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction_Type"),MD_REPRESENTATIVEFRACTION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy_PropertyType"),DQ_GRIDDEDDATAPOSITIONALACCURACY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ObligationCode_PropertyType"),MD_OBLIGATIONCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_TemporalAccuracy_Type"),ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement_Type"),DQ_ACCURACYOFATIMEMEASUREMENT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_AssociationTypeCode_PropertyType"),DS_ASSOCIATIONTYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints_PropertyType"),MD_SECURITYCONSTRAINTS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Medium_Type"),MD_MEDIUM_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageDescription_PropertyType"),MD_COVERAGEDESCRIPTION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation_PropertyType"),MD_METADATAEXTENSIONINFORMATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnLineFunctionCode_PropertyType"),CI_ONLINEFUNCTIONCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_DimensionNameTypeCode_PropertyType"),MD_DIMENSIONNAMETYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess_PropertyType"),MD_STANDARDORDERPROCESS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distributor_Type"),MD_DISTRIBUTOR_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Constraints_PropertyType"),MD_CONSTRAINTS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation_Type"),MD_MAINTENANCEINFORMATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness_PropertyType"),DQ_THEMATICCLASSIFICATIONCORRECTNESS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox_PropertyType"),EX_GEOGRAPHICBOUNDINGBOX_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Dimension_PropertyType"),MD_DIMENSION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractEX_GeographicExtent_Type"),ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox_Type"),EX_GEOGRAPHICBOUNDINGBOX_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy_PropertyType"),DQ_RELATIVEINTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicDescription_Type"),EX_GEOGRAPHICDESCRIPTION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_DataSet_Type"),DS_DATASET_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy_PropertyType"),DQ_QUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractRS_ReferenceSystem_Type"),ABSTRACTRS_REFERENCESYSTEM_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Citation_Type"),CI_CITATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Association_PropertyType"),DS_ASSOCIATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_EvaluationMethodTypeCode_PropertyType"),DQ_EVALUATIONMETHODTYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","LI_Source_Type"),LI_SOURCE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_DataIdentification_PropertyType"),MD_DATAIDENTIFICATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Address_PropertyType"),CI_ADDRESS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_PositionalAccuracy_Type"),ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy_Type"),DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_LegalConstraints_PropertyType"),MD_LEGALCONSTRAINTS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentationTypeCode_PropertyType"),MD_SPATIALREPRESENTATIONTYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentation_PropertyType"),MD_SPATIALREPRESENTATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDS_Aggregate_Type"),ABSTRACTDS_AGGREGATE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Series_Type"),DS_SERIES_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_ProductionSeries_Type"),DS_PRODUCTIONSERIES_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Platform_Type"),DS_PLATFORM_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty_Type"),CI_RESPONSIBLEPARTY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions_Type"),MD_DIGITALTRANSFEROPTIONS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_Extent_Type"),EX_EXTENT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Completeness_PropertyType"),DQ_COMPLETENESS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DataQuality_PropertyType"),DQ_DATAQUALITY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation_Type"),MD_METADATAEXTENSIONINFORMATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction_PropertyType"),MD_REPRESENTATIVEFRACTION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_InitiativeTypeCode_PropertyType"),DS_INITIATIVETYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ContentInformation_PropertyType"),MD_CONTENTINFORMATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty_PropertyType"),CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Keywords_PropertyType"),MD_KEYWORDS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Telephone_Type"),CI_TELEPHONE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation_Type"),MD_EXTENDEDELEMENTINFORMATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy_Type"),DQ_GRIDDEDDATAPOSITIONALACCURACY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","PT_LocaleContainer_PropertyType"),PT_LOCALECONTAINER_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode_Type"),MD_TOPICCATEGORYCODE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Scope_Type"),DQ_SCOPE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription_PropertyType"),MD_FEATURECATALOGUEDESCRIPTION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency_PropertyType"),DQ_DOMAINCONSISTENCY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_DistributionUnits_PropertyType"),MD_DISTRIBUTIONUNITS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ClassificationCode_PropertyType"),MD_CLASSIFICATIONCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_StereoMate_PropertyType"),DS_STEREOMATE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_TemporalExtent_Type"),EX_TEMPORALEXTENT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent_Type"),EX_SPATIALTEMPORALEXTENT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_CellGeometryCode_PropertyType"),MD_CELLGEOMETRYCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identifier_Type"),MD_IDENTIFIER_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","RS_Identifier_Type"),RS_IDENTIFIER_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","URL_PropertyType"),URL_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult_PropertyType"),DQ_QUANTITATIVERESULT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_LogicalConsistency_Type"),ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency_Type"),DQ_FORMATCONSISTENCY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_PositionalAccuracy_PropertyType"),DQ_POSITIONALACCURACY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic_PropertyType"),MD_BROWSEGRAPHIC_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_TemporalExtent_PropertyType"),EX_TEMPORALEXTENT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","RS_Identifier_PropertyType"),RS_IDENTIFIER_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Sensor_PropertyType"),DS_SENSOR_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_ContentInformation_Type"),ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageDescription_Type"),MD_COVERAGEDESCRIPTION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImageDescription_Type"),MD_IMAGEDESCRIPTION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference_Type"),MD_PORTRAYALCATALOGUEREFERENCE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_RoleCode_PropertyType"),CI_ROLECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","Country_PropertyType"),COUNTRY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation_Type"),MD_APPLICATIONSCHEMAINFORMATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_AggregateInformation_Type"),MD_AGGREGATEINFORMATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess_Type"),MD_STANDARDORDERPROCESS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Contact_PropertyType"),CI_CONTACT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DataQuality_Type"),DQ_DATAQUALITY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Format_PropertyType"),MD_FORMAT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeDescription_Type"),MD_SCOPEDESCRIPTION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","PT_LocaleContainer_Type"),PT_LOCALECONTAINER_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_DataSet_PropertyType"),DS_DATASET_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult_PropertyType"),DQ_CONFORMANCERESULT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation_PropertyType"),MD_VECTORSPATIALREPRESENTATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem_Type"),MD_REFERENCESYSTEM_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicDescription_PropertyType"),EX_GEOGRAPHICDESCRIPTION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_OtherAggregate_Type"),DS_OTHERAGGREGATE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_StereoMate_Type"),DS_STEREOMATE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageContentTypeCode_PropertyType"),MD_COVERAGECONTENTTYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Result_PropertyType"),DQ_RESULT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Metadata_Type"),MD_METADATA_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Band_PropertyType"),MD_BAND_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Series_Type"),CI_SERIES_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy_Type"),DQ_RELATIVEINTERNALPOSITIONALACCURACY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_Extent_PropertyType"),EX_EXTENT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity_PropertyType"),DQ_TEMPORALVALIDITY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ObligationCode_Type"),MD_OBLIGATIONCODE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy_PropertyType"),DQ_NONQUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation_PropertyType"),MD_GRIDSPATIALREPRESENTATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_RangeDimension_Type"),MD_RANGEDIMENSION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Band_Type"),MD_BAND_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_DatatypeCode_PropertyType"),MD_DATATYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode_PropertyType"),MD_PIXELORIENTATIONCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Dimension_Type"),MD_DIMENSION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Constraints_Type"),MD_CONSTRAINTS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_LegalConstraints_Type"),MD_LEGALCONSTRAINTS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency_PropertyType"),DQ_TOPOLOGICALCONSISTENCY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distributor_PropertyType"),MD_DISTRIBUTOR_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency_Type"),DQ_TOPOLOGICALCONSISTENCY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","PT_Locale_PropertyType"),PT_LOCALE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Sensor_Type"),DS_SENSOR_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency_Type"),DQ_TEMPORALCONSISTENCY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_VerticalExtent_Type"),EX_VERTICALEXTENT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","PT_FreeText_PropertyType"),PT_FREETEXT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Aggregate_PropertyType"),DS_AGGREGATE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy_PropertyType"),DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem_PropertyType"),MD_REFERENCESYSTEM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference_PropertyType"),MD_PORTRAYALCATALOGUEREFERENCE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation_PropertyType"),MD_APPLICATIONSCHEMAINFORMATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnlineResource_PropertyType"),CI_ONLINERESOURCE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Resolution_Type"),MD_RESOLUTION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_ThematicAccuracy_Type"),ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness_Type"),DQ_THEMATICCLASSIFICATIONCORRECTNESS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon_PropertyType"),EX_BOUNDINGPOLYGON_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_Identification_Type"),ABSTRACTMD_IDENTIFICATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_DataIdentification_Type"),MD_DATAIDENTIFICATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode_Type"),MD_PIXELORIENTATIONCODE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georectified_Type"),MD_GEORECTIFIED_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification_PropertyType"),MD_SERVICEIDENTIFICATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity_Type"),DQ_TEMPORALVALIDITY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency_Type"),DQ_DOMAINCONSISTENCY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalAccuracy_PropertyType"),DQ_TEMPORALACCURACY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImageDescription_PropertyType"),MD_IMAGEDESCRIPTION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","LanguageCode_PropertyType"),LANGUAGECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Series_PropertyType"),DS_SERIES_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_MediumNameCode_PropertyType"),MD_MEDIUMNAMECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","LI_ProcessStep_Type"),LI_PROCESSSTEP_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints_Type"),MD_SECURITYCONSTRAINTS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission_Type"),DQ_COMPLETENESSCOMMISSION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identification_PropertyType"),MD_IDENTIFICATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","LI_Lineage_Type"),LI_LINEAGE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Scope_PropertyType"),DQ_SCOPE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georectified_PropertyType"),MD_GEORECTIFIED_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_DateTypeCode_PropertyType"),CI_DATETYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency_Type"),DQ_CONCEPTUALCONSISTENCY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Citation_PropertyType"),CI_CITATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_CharacterSetCode_PropertyType"),MD_CHARACTERSETCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Series_PropertyType"),CI_SERIES_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_AggregateInformation_PropertyType"),MD_AGGREGATEINFORMATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjects_Type"),MD_GEOMETRICOBJECTS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_RangeDimension_PropertyType"),MD_RANGEDIMENSION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","PT_Locale_Type"),PT_LOCALE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Keywords_Type"),MD_KEYWORDS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Result_Type"),ABSTRACTDQ_RESULT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult_Type"),DQ_QUANTITATIVERESULT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeCode_PropertyType"),MD_SCOPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","RS_ReferenceSystem_PropertyType"),RS_REFERENCESYSTEM_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Contact_Type"),CI_CONTACT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicAccuracy_PropertyType"),DQ_THEMATICACCURACY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Association_Type"),DS_ASSOCIATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","LI_ProcessStep_PropertyType"),LI_PROCESSSTEP_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Format_Type"),MD_FORMAT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult_Type"),DQ_CONFORMANCERESULT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnlineResource_Type"),CI_ONLINERESOURCE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Initiative_Type"),DS_INITIATIVE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_LogicalConsistency_PropertyType"),DQ_LOGICALCONSISTENCY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_KeywordTypeCode_PropertyType"),MD_KEYWORDTYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions_PropertyType"),MD_DIGITALTRANSFEROPTIONS_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency_PropertyType"),DQ_CONCEPTUALCONSISTENCY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Element_PropertyType"),DQ_ELEMENT_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceFrequencyCode_PropertyType"),MD_MAINTENANCEFREQUENCYCODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","LI_Lineage_PropertyType"),LI_LINEAGE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency_PropertyType"),DQ_FORMATCONSISTENCY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distribution_PropertyType"),MD_DISTRIBUTION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_ProductionSeries_PropertyType"),DS_PRODUCTIONSERIES_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy_Type"),DQ_QUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","LocalisedCharacterString_PropertyType"),LOCALISEDCHARACTERSTRING_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Date_Type"),CI_DATE_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","PT_FreeText_Type"),PT_FREETEXT_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification_Type"),MD_SERVICEIDENTIFICATION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_Initiative_PropertyType"),DS_INITIATIVE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identifier_PropertyType"),MD_IDENTIFIER_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy_Type"),DQ_NONQUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation_PropertyType"),MD_MAINTENANCEINFORMATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeDescription_PropertyType"),MD_SCOPEDESCRIPTION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","CI_Address_Type"),CI_ADDRESS_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjectTypeCode_PropertyType"),MD_GEOMETRICOBJECTTYPECODE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DS_OtherAggregate_PropertyType"),DS_OTHERAGGREGATE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georeferenceable_PropertyType"),MD_GEOREFERENCEABLE_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission_PropertyType"),DQ_COMPLETENESSCOMMISSION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon_Type"),EX_BOUNDINGPOLYGON_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic_Type"),MD_BROWSEGRAPHIC_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription_Type"),MD_FEATURECATALOGUEDESCRIPTION_TYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_Resolution_PropertyType"),MD_RESOLUTION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency_PropertyType"),DQ_TEMPORALCONSISTENCY_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation_PropertyType"),MD_EXTENDEDELEMENTINFORMATION_PROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImagingConditionCode_PropertyType"),MD_IMAGINGCONDITIONCODE_PROPERTYTYPE_TYPE);
    }
    
}