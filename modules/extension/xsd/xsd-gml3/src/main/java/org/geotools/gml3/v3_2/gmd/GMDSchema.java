package org.geotools.gml3.v3_2.gmd;

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
import org.geotools.gml3.v3_2.gco.GCOSchema;
import org.geotools.gml3.v3_2.gsr.GSRSchema;
import org.geotools.gml3.v3_2.gss.GSSSchema;
import org.geotools.gml3.v3_2.gts.GTSSchema;
import org.geotools.xlink.XLINKSchema;
import org.geotools.xs.XSSchema;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.feature.type.Schema;

public class GMDSchema extends SchemaImpl {

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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Completeness_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_ELEMENT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Element_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","nameOfMeasure"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_IDENTIFIER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","measureIdentification"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","measureDescription"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DQ_EVALUATIONMETHODTYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","evaluationMethodType"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","evaluationMethodDescription"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","evaluationProcedure"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DATETIME_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dateTime"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DQ_RESULT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","result"),
                        1, 2, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_LogicalConsistency_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_ELEMENT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_PositionalAccuracy_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_ELEMENT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Result_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_TemporalAccuracy_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_ELEMENT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_ThematicAccuracy_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_ELEMENT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDS_Aggregate_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DS_DATASET_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","composedOf"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_METADATA_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","seriesMetadata"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DS_AGGREGATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","subset"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DS_AGGREGATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","superset"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractEX_GeographicExtent_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","extentTypeCode"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_ContentInformation_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_Identification_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","citation"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","abstract"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","purpose"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","credit"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_PROGRESSCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","status"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","pointOfContact"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_MAINTENANCEINFORMATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","resourceMaintenance"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_BROWSEGRAPHIC_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","graphicOverview"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_FORMAT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","resourceFormat"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_KEYWORDS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","descriptiveKeywords"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_USAGE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","resourceSpecificUsage"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_CONSTRAINTS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","resourceConstraints"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_AGGREGATEINFORMATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","aggregationInfo"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
        return builtType;
    }

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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_SpatialRepresentation_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","AbstractRS_ReferenceSystem_Type"),
                false, true, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        RS_IDENTIFIER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","name"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        EX_EXTENT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","domainOfValidity"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Address_PropertyType"),
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
                        CI_ADDRESS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_Address"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Address_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","deliveryPoint"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","city"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","administrativeArea"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","postalCode"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","country"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","electronicMailAddress"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Citation_PropertyType"),
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
                        CI_CITATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_Citation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Citation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","title"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","alternateTitle"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_DATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","date"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","edition"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","editionDate"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_IDENTIFIER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","identifier"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","citedResponsibleParty"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_PRESENTATIONFORMCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","presentationForm"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_SERIES_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","series"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","otherCitationDetails"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","collectiveTitle"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","ISBN"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","ISSN"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Contact_PropertyType"),
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
                        CI_CONTACT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_Contact"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Contact_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_TELEPHONE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","phone"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_ADDRESS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","address"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_ONLINERESOURCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","onlineResource"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","hoursOfService"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","contactInstructions"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_DateTypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_DateTypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Date_PropertyType"),
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
                        CI_DATE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_Date"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Date_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","date"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_DATETYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dateType"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnLineFunctionCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnLineFunctionCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnlineResource_PropertyType"),
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
                        CI_ONLINERESOURCE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnlineResource"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_OnlineResource_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        URL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","linkage"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","protocol"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","applicationProfile"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","name"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","description"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_ONLINEFUNCTIONCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","function"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_PresentationFormCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_PresentationFormCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty_PropertyType"),
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
                        CI_RESPONSIBLEPARTY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","individualName"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","organisationName"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","positionName"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CONTACT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","contactInfo"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_ROLECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","role"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_RoleCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_RoleCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Series_PropertyType"),
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
                        CI_SERIES_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_Series"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Series_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","name"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","issueIdentification"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","page"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Telephone_PropertyType"),
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
                        CI_TELEPHONE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","CI_Telephone"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","CI_Telephone_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","voice"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","facsimile"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","Country_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","Country"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy_PropertyType"),
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
                        DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement_PropertyType"),
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
                        DQ_ACCURACYOFATIMEMEASUREMENT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission_PropertyType"),
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
                        DQ_COMPLETENESSCOMMISSION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_COMPLETENESS_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission_PropertyType"),
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
                        DQ_COMPLETENESSOMISSION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_COMPLETENESS_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Completeness_PropertyType"),
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
                        ABSTRACTDQ_COMPLETENESS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Completeness"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency_PropertyType"),
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
                        DQ_CONCEPTUALCONSISTENCY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult_PropertyType"),
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
                        DQ_CONFORMANCERESULT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_RESULT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","specification"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","explanation"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","pass"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DataQuality_PropertyType"),
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
                        DQ_DATAQUALITY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DataQuality"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DataQuality_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DQ_SCOPE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","scope"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DQ_ELEMENT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","report"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        LI_LINEAGE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","lineage"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency_PropertyType"),
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
                        DQ_DOMAINCONSISTENCY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Element_PropertyType"),
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
                        ABSTRACTDQ_ELEMENT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Element"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_EvaluationMethodTypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_EvaluationMethodTypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency_PropertyType"),
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
                        DQ_FORMATCONSISTENCY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy_PropertyType"),
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
                        DQ_GRIDDEDDATAPOSITIONALACCURACY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_LogicalConsistency_PropertyType"),
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
                        ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_LogicalConsistency"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy_PropertyType"),
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
                        DQ_NONQUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_PositionalAccuracy_PropertyType"),
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
                        ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_PositionalAccuracy"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy_PropertyType"),
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
                        DQ_QUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult_PropertyType"),
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
                        DQ_QUANTITATIVERESULT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_RESULT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.RECORDTYPE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","valueType"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.UNITOFMEASURE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","valueUnit"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","errorStatistic"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.RECORD_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","value"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy_PropertyType"),
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
                        DQ_RELATIVEINTERNALPOSITIONALACCURACY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Result_PropertyType"),
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
                        ABSTRACTDQ_RESULT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_Result"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Scope_PropertyType"),
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
                        DQ_SCOPE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Scope"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_Scope_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_SCOPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","level"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        EX_EXTENT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","extent"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_SCOPEDESCRIPTION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","levelDescription"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalAccuracy_PropertyType"),
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
                        ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_TemporalAccuracy"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency_PropertyType"),
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
                        DQ_TEMPORALCONSISTENCY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity_PropertyType"),
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
                        DQ_TEMPORALVALIDITY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicAccuracy_PropertyType"),
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
                        ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDQ_ThematicAccuracy"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness_PropertyType"),
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
                        DQ_THEMATICCLASSIFICATIONCORRECTNESS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency_PropertyType"),
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
                        DQ_TOPOLOGICALCONSISTENCY_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Aggregate_PropertyType"),
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
                        ABSTRACTDS_AGGREGATE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractDS_Aggregate"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_AssociationTypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_AssociationTypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Association_PropertyType"),
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
                        DS_ASSOCIATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_Association"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Association_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_DataSet_PropertyType"),
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
                        DS_DATASET_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_DataSet"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_DataSet_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_METADATA_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","has"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DS_AGGREGATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","partOf"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_InitiativeTypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_InitiativeTypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Initiative_PropertyType"),
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
                        DS_INITIATIVE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_Initiative"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Initiative_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDS_AGGREGATE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_OtherAggregate_PropertyType"),
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
                        DS_OTHERAGGREGATE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_OtherAggregate"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_OtherAggregate_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDS_AGGREGATE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Platform_PropertyType"),
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
                        DS_PLATFORM_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_Platform"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Platform_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return DS_SERIES_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_ProductionSeries_PropertyType"),
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
                        DS_PRODUCTIONSERIES_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_ProductionSeries"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_ProductionSeries_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return DS_SERIES_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Sensor_PropertyType"),
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
                        DS_SENSOR_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_Sensor"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Sensor_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return DS_SERIES_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Series_PropertyType"),
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
                        DS_SERIES_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_Series"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_Series_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTDS_AGGREGATE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_StereoMate_PropertyType"),
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
                        DS_STEREOMATE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","DS_StereoMate"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","DS_StereoMate_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return DS_OTHERAGGREGATE_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon_PropertyType"),
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
                        EX_BOUNDINGPOLYGON_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GSSSchema.GM_OBJECT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","polygon"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_Extent_PropertyType"),
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
                        EX_EXTENT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","EX_Extent"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_Extent_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","description"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        EX_GEOGRAPHICEXTENT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","geographicElement"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        EX_TEMPORALEXTENT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","temporalElement"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        EX_VERTICALEXTENT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","verticalElement"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox_PropertyType"),
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
                        EX_GEOGRAPHICBOUNDINGBOX_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DECIMAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","westBoundLongitude"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DECIMAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","eastBoundLongitude"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DECIMAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","southBoundLatitude"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DECIMAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","northBoundLatitude"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicDescription_PropertyType"),
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
                        EX_GEOGRAPHICDESCRIPTION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicDescription"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicDescription_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_IDENTIFIER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","geographicIdentifier"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_GeographicExtent_PropertyType"),
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
                        ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractEX_GeographicExtent"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent_PropertyType"),
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
                        EX_SPATIALTEMPORALEXTENT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return EX_TEMPORALEXTENT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        EX_GEOGRAPHICEXTENT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","spatialExtent"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_TemporalExtent_PropertyType"),
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
                        EX_TEMPORALEXTENT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","EX_TemporalExtent"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_TemporalExtent_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GTSSchema.TM_PRIMITIVE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","extent"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_VerticalExtent_PropertyType"),
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
                        EX_VERTICALEXTENT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","EX_VerticalExtent"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","EX_VerticalExtent_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","minimumValue"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","maximumValue"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GSRSchema.SC_CRS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","verticalCRS"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","LI_Lineage_PropertyType"),
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
                        LI_LINEAGE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","LI_Lineage"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","LI_Lineage_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","statement"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        LI_PROCESSSTEP_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","processStep"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        LI_SOURCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","source"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","LI_ProcessStep_PropertyType"),
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
                        LI_PROCESSSTEP_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","LI_ProcessStep"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","LI_ProcessStep_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","description"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","rationale"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DATETIME_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dateTime"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","processor"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        LI_SOURCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","source"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","LI_Source_PropertyType"),
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
                        LI_SOURCE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","LI_Source"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","LI_Source_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","description"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_REPRESENTATIVEFRACTION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","scaleDenominator"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_REFERENCESYSTEM_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","sourceReferenceSystem"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","sourceCitation"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        EX_EXTENT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","sourceExtent"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        LI_PROCESSSTEP_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","sourceStep"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","LanguageCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","LanguageCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","LocalisedCharacterString_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.OBJECTREFERENCE_PROPERTYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        LOCALISEDCHARACTERSTRING_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","LocalisedCharacterString"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","LocalisedCharacterString_Type"),
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
                        XSSchema.ID_TYPE,
                        new NameImpl("id"),
                        0, 1, true, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("locale"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_AggregateInformation_PropertyType"),
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
                        MD_AGGREGATEINFORMATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_AggregateInformation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_AggregateInformation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","aggregateDataSetName"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_IDENTIFIER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","aggregateDataSetIdentifier"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DS_ASSOCIATIONTYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","associationType"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DS_INITIATIVETYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","initiativeType"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation_PropertyType"),
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
                        MD_APPLICATIONSCHEMAINFORMATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","name"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","schemaLanguage"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","constraintLanguage"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","schemaAscii"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BINARY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","graphicsFile"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BINARY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","softwareDevelopmentFile"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","softwareDevelopmentFileFormat"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Band_PropertyType"),
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
                        MD_BAND_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Band"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Band_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return MD_RANGEDIMENSION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","maxValue"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","minValue"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.UOMLENGTH_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","units"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","peakResponse"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","bitsPerValue"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","toneGradation"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","scaleFactor"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","offset"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic_PropertyType"),
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
                        MD_BROWSEGRAPHIC_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","fileName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","fileDescription"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","fileType"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_CellGeometryCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_CellGeometryCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_CharacterSetCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_CharacterSetCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ClassificationCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ClassificationCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Constraints_PropertyType"),
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
                        MD_CONSTRAINTS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Constraints"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Constraints_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","useLimitation"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ContentInformation_PropertyType"),
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
                        ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_ContentInformation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageContentTypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageContentTypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageDescription_PropertyType"),
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
                        MD_COVERAGEDESCRIPTION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageDescription"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_CoverageDescription_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.RECORDTYPE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","attributeDescription"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_COVERAGECONTENTTYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","contentType"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_RANGEDIMENSION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dimension"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_DataIdentification_PropertyType"),
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
                        MD_DATAIDENTIFICATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_DataIdentification"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_DataIdentification_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTMD_IDENTIFICATION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_SPATIALREPRESENTATIONTYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","spatialRepresentationType"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_RESOLUTION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","spatialResolution"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","language"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_CHARACTERSETCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","characterSet"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_TOPICCATEGORYCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","topicCategory"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","environmentDescription"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        EX_EXTENT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","extent"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","supplementalInformation"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_DatatypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_DatatypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions_PropertyType"),
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
                        MD_DIGITALTRANSFEROPTIONS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","unitsOfDistribution"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","transferSize"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_ONLINERESOURCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","onLine"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_MEDIUM_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","offLine"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_DimensionNameTypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_DimensionNameTypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Dimension_PropertyType"),
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
                        MD_DIMENSION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Dimension"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Dimension_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_DIMENSIONNAMETYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dimensionName"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dimensionSize"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.MEASURE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","resolution"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_DistributionUnits_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_DistributionUnits"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distribution_PropertyType"),
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
                        MD_DISTRIBUTION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distribution"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distribution_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_FORMAT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","distributionFormat"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_DISTRIBUTOR_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","distributor"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_DIGITALTRANSFEROPTIONS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","transferOptions"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distributor_PropertyType"),
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
                        MD_DISTRIBUTOR_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distributor"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Distributor_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","distributorContact"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_STANDARDORDERPROCESS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","distributionOrderProcess"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_FORMAT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","distributorFormat"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_DIGITALTRANSFEROPTIONS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","distributorTransferOptions"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation_PropertyType"),
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
                        MD_EXTENDEDELEMENTINFORMATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","name"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","shortName"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","domainCode"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","definition"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_OBLIGATIONCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","obligation"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","condition"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_DATATYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dataType"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","maximumOccurrence"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","domainValue"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","parentEntity"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","rule"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","rationale"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","source"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription_PropertyType"),
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
                        MD_FEATURECATALOGUEDESCRIPTION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","complianceCode"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","language"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","includedWithDataset"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.GENERICNAME_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","featureTypes"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","featureCatalogueCitation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Format_PropertyType"),
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
                        MD_FORMAT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Format"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Format_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","name"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","version"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","amendmentNumber"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","specification"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","fileDecompressionTechnique"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_DISTRIBUTOR_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","formatDistributor"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjectTypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjectTypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjects_PropertyType"),
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
                        MD_GEOMETRICOBJECTS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjects"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_GeometricObjects_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_GEOMETRICOBJECTTYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","geometricObjectType"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","geometricObjectCount"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georectified_PropertyType"),
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
                        MD_GEORECTIFIED_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georectified"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georectified_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","checkPointAvailability"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","checkPointDescription"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GSSSchema.GM_POINT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","cornerPoints"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GSSSchema.GM_POINT_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","centerPoint"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_PIXELORIENTATIONCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","pointInPixel"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","transformationDimensionDescription"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","transformationDimensionMapping"),
                        0, 2, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georeferenceable_PropertyType"),
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
                        MD_GEOREFERENCEABLE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georeferenceable"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Georeferenceable_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","controlPointAvailability"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","orientationParameterAvailability"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","orientationParameterDescription"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.RECORD_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","georeferencedParameters"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","parameterCitation"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation_PropertyType"),
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
                        MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","numberOfDimensions"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_DIMENSION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","axisDimensionProperties"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_CELLGEOMETRYCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","cellGeometry"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","transformationParameterAvailability"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identification_PropertyType"),
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
                        ABSTRACTMD_IDENTIFICATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_Identification"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identifier_PropertyType"),
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
                        MD_IDENTIFIER_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identifier"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Identifier_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","authority"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","code"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImageDescription_PropertyType"),
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
                        MD_IMAGEDESCRIPTION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImageDescription"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImageDescription_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return MD_COVERAGEDESCRIPTION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","illuminationElevationAngle"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","illuminationAzimuthAngle"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_IMAGINGCONDITIONCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","imagingCondition"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_IDENTIFIER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","imageQualityCode"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","cloudCoverPercentage"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_IDENTIFIER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","processingLevelCode"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","compressionGenerationQuantity"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","triangulationIndicator"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","radiometricCalibrationDataAvailability"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","cameraCalibrationInformationAvailability"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","filmDistortionInformationAvailability"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.BOOLEAN_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","lensDistortionInformationAvailability"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImagingConditionCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ImagingConditionCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_KeywordTypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_KeywordTypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Keywords_PropertyType"),
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
                        MD_KEYWORDS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Keywords"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Keywords_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","keyword"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_KEYWORDTYPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","type"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","thesaurusName"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_LegalConstraints_PropertyType"),
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
                        MD_LEGALCONSTRAINTS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_LegalConstraints"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_LegalConstraints_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return MD_CONSTRAINTS_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_RESTRICTIONCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","accessConstraints"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_RESTRICTIONCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","useConstraints"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","otherConstraints"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceFrequencyCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceFrequencyCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation_PropertyType"),
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
                        MD_MAINTENANCEINFORMATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_MAINTENANCEFREQUENCYCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","maintenanceAndUpdateFrequency"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dateOfNextUpdate"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GTSSchema.TM_PERIODDURATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","userDefinedMaintenanceFrequency"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_SCOPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","updateScope"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_SCOPEDESCRIPTION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","updateScopeDescription"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","maintenanceNote"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","contact"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_MediumFormatCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_MediumFormatCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_MediumNameCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_MediumNameCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Medium_PropertyType"),
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
                        MD_MEDIUM_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Medium"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Medium_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_MEDIUMNAMECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","name"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.REAL_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","density"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","densityUnits"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","volumes"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_MEDIUMFORMATCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","mediumFormat"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","mediumNote"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation_PropertyType"),
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
                        MD_METADATAEXTENSIONINFORMATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_ONLINERESOURCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","extensionOnLineResource"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_EXTENDEDELEMENTINFORMATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","extendedElementInformation"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Metadata_PropertyType"),
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
                        MD_METADATA_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Metadata"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Metadata_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","fileIdentifier"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","language"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_CHARACTERSETCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","characterSet"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","parentIdentifier"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_SCOPECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","hierarchyLevel"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","hierarchyLevelName"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","contact"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dateStamp"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","metadataStandardName"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","metadataStandardVersion"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dataSetURI"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","locale"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_SPATIALREPRESENTATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","spatialRepresentationInfo"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_REFERENCESYSTEM_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","referenceSystemInfo"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_METADATAEXTENSIONINFORMATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","metadataExtensionInfo"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_IDENTIFICATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","identificationInfo"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_CONTENTINFORMATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","contentInfo"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_DISTRIBUTION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","distributionInfo"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DQ_DATAQUALITY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dataQualityInfo"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_PORTRAYALCATALOGUEREFERENCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","portrayalCatalogueInfo"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_CONSTRAINTS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","metadataConstraints"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_APPLICATIONSCHEMAINFORMATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","applicationSchemaInfo"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_MAINTENANCEINFORMATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","metadataMaintenance"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DS_AGGREGATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","series"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        DS_DATASET_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","describes"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.OBJECTREFERENCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","propertyType"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.OBJECTREFERENCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","featureType"),
                        0, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.OBJECTREFERENCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","featureAttribute"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ObligationCode_PropertyType"),
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
                        MD_OBLIGATIONCODE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ObligationCode"),
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
        AttributeType builtType = new AbstractLazyAttributeTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ObligationCode_Type"),
                java.lang.Object.class, false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.STRING_TYPE;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode_PropertyType"),
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
                        MD_PIXELORIENTATIONCODE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode"),
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
        AttributeType builtType = new AbstractLazyAttributeTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode_Type"),
                java.lang.Object.class, false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.STRING_TYPE;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference_PropertyType"),
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
                        MD_PORTRAYALCATALOGUEREFERENCE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_CITATION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","portrayalCatalogueCitation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ProgressCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ProgressCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_RangeDimension_PropertyType"),
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
                        MD_RANGEDIMENSION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_RangeDimension"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_RangeDimension_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.MEMBERNAME_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","sequenceIdentifier"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","descriptor"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem_PropertyType"),
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
                        MD_REFERENCESYSTEM_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        RS_IDENTIFIER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","referenceSystemIdentifier"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction_PropertyType"),
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
                        MD_REPRESENTATIVEFRACTION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.INTEGER_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","denominator"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Resolution_PropertyType"),
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
                        MD_RESOLUTION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Resolution"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Resolution_Type"),
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
                        MD_REPRESENTATIVEFRACTION_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","equivalentScale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DISTANCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","distance"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_RestrictionCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_RestrictionCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeDescription_PropertyType"),
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
                        MD_SCOPEDESCRIPTION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeDescription"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ScopeDescription_Type"),
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
                        GCOSchema.OBJECTREFERENCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","attributes"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.OBJECTREFERENCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","features"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.OBJECTREFERENCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","featureInstances"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.OBJECTREFERENCE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","attributeInstances"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","dataset"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","other"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints_PropertyType"),
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
                        MD_SECURITYCONSTRAINTS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return MD_CONSTRAINTS_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_CLASSIFICATIONCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","classification"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","userNote"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","classificationSystem"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","handlingDescription"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification_PropertyType"),
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
                        MD_SERVICEIDENTIFICATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTMD_IDENTIFICATION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                return null;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentationTypeCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentationTypeCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentation_PropertyType"),
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
                        ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractMD_SpatialRepresentation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess_PropertyType"),
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
                        MD_STANDARDORDERPROCESS_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","fees"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DATETIME_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","plannedAvailableDateTime"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","orderingInstructions"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","turnaround"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode_PropertyType"),
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
                        MD_TOPICCATEGORYCODE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode"),
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
        AttributeType builtType = new AbstractLazyAttributeTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode_Type"),
                java.lang.Object.class, false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return XSSchema.STRING_TYPE;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopologyLevelCode_PropertyType"),
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
                        GCOSchema.CODELISTVALUE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_TopologyLevelCode"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Usage_PropertyType"),
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
                        MD_USAGE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_Usage"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_Usage_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","specificUsage"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.DATETIME_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","usageDateTime"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","userDeterminedLimitations"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","userContactInfo"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation_PropertyType"),
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
                        MD_VECTORSPATIALREPRESENTATION_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_TOPOLOGYLEVELCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","topologyLevel"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_GEOMETRICOBJECTS_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","geometricObjects"),
                        0, 2147483647, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","PT_FreeText_PropertyType"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        PT_FREETEXT_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","PT_FreeText"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","PT_FreeText_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        LOCALISEDCHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","textGroup"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","PT_LocaleContainer_PropertyType"),
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
                        PT_LOCALECONTAINER_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","PT_LocaleContainer"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","PT_LocaleContainer_Type"),
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
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","description"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        PT_LOCALE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","locale"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_DATE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","date"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","responsibleParty"),
                        1, 2147483647, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        LOCALISEDCHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","localisedString"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","PT_Locale_PropertyType"),
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
                        PT_LOCALE_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","PT_Locale"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","PT_Locale_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return GCOSchema.ABSTRACTOBJECT_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        LANGUAGECODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","languageCode"),
                        1, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        COUNTRY_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","country"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        MD_CHARACTERSETCODE_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","characterEncoding"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","RS_Identifier_PropertyType"),
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
                        RS_IDENTIFIER_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","RS_Identifier"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","RS_Identifier_Type"),
                false, false, null, null) {
            @Override
            public AttributeType buildSuper() {
                return MD_IDENTIFIER_TYPE_TYPE;
            }
            @Override
            public Collection<PropertyDescriptor> buildDescriptors() {
                List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","codeSpace"),
                        0, 1, false, null));
                descriptors.add(
                    new AttributeDescriptorImpl(
                        GCOSchema.CHARACTERSTRING_PROPERTYTYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","version"),
                        0, 1, false, null));
                return descriptors;
            }
        };
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","RS_ReferenceSystem_PropertyType"),
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
                        ABSTRACTRS_REFERENCESYSTEM_TYPE_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","AbstractRS_ReferenceSystem"),
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
        ComplexType builtType = new AbstractLazyComplexTypeImpl(
                new NameImpl("http://www.isotc211.org/2005/gmd","URL_PropertyType"),
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
                        XSSchema.ANYURI_TYPE,
                        new NameImpl("http://www.isotc211.org/2005/gmd","URL"),
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


    public GMDSchema() {
        super("http://www.isotc211.org/2005/gmd");
        put(ABSTRACTDQ_COMPLETENESS_TYPE_TYPE);
        put(ABSTRACTDQ_ELEMENT_TYPE_TYPE);
        put(ABSTRACTDQ_LOGICALCONSISTENCY_TYPE_TYPE);
        put(ABSTRACTDQ_POSITIONALACCURACY_TYPE_TYPE);
        put(ABSTRACTDQ_RESULT_TYPE_TYPE);
        put(ABSTRACTDQ_TEMPORALACCURACY_TYPE_TYPE);
        put(ABSTRACTDQ_THEMATICACCURACY_TYPE_TYPE);
        put(ABSTRACTDS_AGGREGATE_TYPE_TYPE);
        put(ABSTRACTEX_GEOGRAPHICEXTENT_TYPE_TYPE);
        put(ABSTRACTMD_CONTENTINFORMATION_TYPE_TYPE);
        put(ABSTRACTMD_IDENTIFICATION_TYPE_TYPE);
        put(ABSTRACTMD_SPATIALREPRESENTATION_TYPE_TYPE);
        put(ABSTRACTRS_REFERENCESYSTEM_TYPE_TYPE);
        put(CI_ADDRESS_PROPERTYTYPE_TYPE);
        put(CI_ADDRESS_TYPE_TYPE);
        put(CI_CITATION_PROPERTYTYPE_TYPE);
        put(CI_CITATION_TYPE_TYPE);
        put(CI_CONTACT_PROPERTYTYPE_TYPE);
        put(CI_CONTACT_TYPE_TYPE);
        put(CI_DATETYPECODE_PROPERTYTYPE_TYPE);
        put(CI_DATE_PROPERTYTYPE_TYPE);
        put(CI_DATE_TYPE_TYPE);
        put(CI_ONLINEFUNCTIONCODE_PROPERTYTYPE_TYPE);
        put(CI_ONLINERESOURCE_PROPERTYTYPE_TYPE);
        put(CI_ONLINERESOURCE_TYPE_TYPE);
        put(CI_PRESENTATIONFORMCODE_PROPERTYTYPE_TYPE);
        put(CI_RESPONSIBLEPARTY_PROPERTYTYPE_TYPE);
        put(CI_RESPONSIBLEPARTY_TYPE_TYPE);
        put(CI_ROLECODE_PROPERTYTYPE_TYPE);
        put(CI_SERIES_PROPERTYTYPE_TYPE);
        put(CI_SERIES_TYPE_TYPE);
        put(CI_TELEPHONE_PROPERTYTYPE_TYPE);
        put(CI_TELEPHONE_TYPE_TYPE);
        put(COUNTRY_PROPERTYTYPE_TYPE);
        put(DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE);
        put(DQ_ABSOLUTEEXTERNALPOSITIONALACCURACY_TYPE_TYPE);
        put(DQ_ACCURACYOFATIMEMEASUREMENT_PROPERTYTYPE_TYPE);
        put(DQ_ACCURACYOFATIMEMEASUREMENT_TYPE_TYPE);
        put(DQ_COMPLETENESSCOMMISSION_PROPERTYTYPE_TYPE);
        put(DQ_COMPLETENESSCOMMISSION_TYPE_TYPE);
        put(DQ_COMPLETENESSOMISSION_PROPERTYTYPE_TYPE);
        put(DQ_COMPLETENESSOMISSION_TYPE_TYPE);
        put(DQ_COMPLETENESS_PROPERTYTYPE_TYPE);
        put(DQ_CONCEPTUALCONSISTENCY_PROPERTYTYPE_TYPE);
        put(DQ_CONCEPTUALCONSISTENCY_TYPE_TYPE);
        put(DQ_CONFORMANCERESULT_PROPERTYTYPE_TYPE);
        put(DQ_CONFORMANCERESULT_TYPE_TYPE);
        put(DQ_DATAQUALITY_PROPERTYTYPE_TYPE);
        put(DQ_DATAQUALITY_TYPE_TYPE);
        put(DQ_DOMAINCONSISTENCY_PROPERTYTYPE_TYPE);
        put(DQ_DOMAINCONSISTENCY_TYPE_TYPE);
        put(DQ_ELEMENT_PROPERTYTYPE_TYPE);
        put(DQ_EVALUATIONMETHODTYPECODE_PROPERTYTYPE_TYPE);
        put(DQ_FORMATCONSISTENCY_PROPERTYTYPE_TYPE);
        put(DQ_FORMATCONSISTENCY_TYPE_TYPE);
        put(DQ_GRIDDEDDATAPOSITIONALACCURACY_PROPERTYTYPE_TYPE);
        put(DQ_GRIDDEDDATAPOSITIONALACCURACY_TYPE_TYPE);
        put(DQ_LOGICALCONSISTENCY_PROPERTYTYPE_TYPE);
        put(DQ_NONQUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE);
        put(DQ_NONQUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE);
        put(DQ_POSITIONALACCURACY_PROPERTYTYPE_TYPE);
        put(DQ_QUANTITATIVEATTRIBUTEACCURACY_PROPERTYTYPE_TYPE);
        put(DQ_QUANTITATIVEATTRIBUTEACCURACY_TYPE_TYPE);
        put(DQ_QUANTITATIVERESULT_PROPERTYTYPE_TYPE);
        put(DQ_QUANTITATIVERESULT_TYPE_TYPE);
        put(DQ_RELATIVEINTERNALPOSITIONALACCURACY_PROPERTYTYPE_TYPE);
        put(DQ_RELATIVEINTERNALPOSITIONALACCURACY_TYPE_TYPE);
        put(DQ_RESULT_PROPERTYTYPE_TYPE);
        put(DQ_SCOPE_PROPERTYTYPE_TYPE);
        put(DQ_SCOPE_TYPE_TYPE);
        put(DQ_TEMPORALACCURACY_PROPERTYTYPE_TYPE);
        put(DQ_TEMPORALCONSISTENCY_PROPERTYTYPE_TYPE);
        put(DQ_TEMPORALCONSISTENCY_TYPE_TYPE);
        put(DQ_TEMPORALVALIDITY_PROPERTYTYPE_TYPE);
        put(DQ_TEMPORALVALIDITY_TYPE_TYPE);
        put(DQ_THEMATICACCURACY_PROPERTYTYPE_TYPE);
        put(DQ_THEMATICCLASSIFICATIONCORRECTNESS_PROPERTYTYPE_TYPE);
        put(DQ_THEMATICCLASSIFICATIONCORRECTNESS_TYPE_TYPE);
        put(DQ_TOPOLOGICALCONSISTENCY_PROPERTYTYPE_TYPE);
        put(DQ_TOPOLOGICALCONSISTENCY_TYPE_TYPE);
        put(DS_AGGREGATE_PROPERTYTYPE_TYPE);
        put(DS_ASSOCIATIONTYPECODE_PROPERTYTYPE_TYPE);
        put(DS_ASSOCIATION_PROPERTYTYPE_TYPE);
        put(DS_ASSOCIATION_TYPE_TYPE);
        put(DS_DATASET_PROPERTYTYPE_TYPE);
        put(DS_DATASET_TYPE_TYPE);
        put(DS_INITIATIVETYPECODE_PROPERTYTYPE_TYPE);
        put(DS_INITIATIVE_PROPERTYTYPE_TYPE);
        put(DS_INITIATIVE_TYPE_TYPE);
        put(DS_OTHERAGGREGATE_PROPERTYTYPE_TYPE);
        put(DS_OTHERAGGREGATE_TYPE_TYPE);
        put(DS_PLATFORM_PROPERTYTYPE_TYPE);
        put(DS_PLATFORM_TYPE_TYPE);
        put(DS_PRODUCTIONSERIES_PROPERTYTYPE_TYPE);
        put(DS_PRODUCTIONSERIES_TYPE_TYPE);
        put(DS_SENSOR_PROPERTYTYPE_TYPE);
        put(DS_SENSOR_TYPE_TYPE);
        put(DS_SERIES_PROPERTYTYPE_TYPE);
        put(DS_SERIES_TYPE_TYPE);
        put(DS_STEREOMATE_PROPERTYTYPE_TYPE);
        put(DS_STEREOMATE_TYPE_TYPE);
        put(EX_BOUNDINGPOLYGON_PROPERTYTYPE_TYPE);
        put(EX_BOUNDINGPOLYGON_TYPE_TYPE);
        put(EX_EXTENT_PROPERTYTYPE_TYPE);
        put(EX_EXTENT_TYPE_TYPE);
        put(EX_GEOGRAPHICBOUNDINGBOX_PROPERTYTYPE_TYPE);
        put(EX_GEOGRAPHICBOUNDINGBOX_TYPE_TYPE);
        put(EX_GEOGRAPHICDESCRIPTION_PROPERTYTYPE_TYPE);
        put(EX_GEOGRAPHICDESCRIPTION_TYPE_TYPE);
        put(EX_GEOGRAPHICEXTENT_PROPERTYTYPE_TYPE);
        put(EX_SPATIALTEMPORALEXTENT_PROPERTYTYPE_TYPE);
        put(EX_SPATIALTEMPORALEXTENT_TYPE_TYPE);
        put(EX_TEMPORALEXTENT_PROPERTYTYPE_TYPE);
        put(EX_TEMPORALEXTENT_TYPE_TYPE);
        put(EX_VERTICALEXTENT_PROPERTYTYPE_TYPE);
        put(EX_VERTICALEXTENT_TYPE_TYPE);
        put(LI_LINEAGE_PROPERTYTYPE_TYPE);
        put(LI_LINEAGE_TYPE_TYPE);
        put(LI_PROCESSSTEP_PROPERTYTYPE_TYPE);
        put(LI_PROCESSSTEP_TYPE_TYPE);
        put(LI_SOURCE_PROPERTYTYPE_TYPE);
        put(LI_SOURCE_TYPE_TYPE);
        put(LANGUAGECODE_PROPERTYTYPE_TYPE);
        put(LOCALISEDCHARACTERSTRING_PROPERTYTYPE_TYPE);
        put(LOCALISEDCHARACTERSTRING_TYPE_TYPE);
        put(MD_AGGREGATEINFORMATION_PROPERTYTYPE_TYPE);
        put(MD_AGGREGATEINFORMATION_TYPE_TYPE);
        put(MD_APPLICATIONSCHEMAINFORMATION_PROPERTYTYPE_TYPE);
        put(MD_APPLICATIONSCHEMAINFORMATION_TYPE_TYPE);
        put(MD_BAND_PROPERTYTYPE_TYPE);
        put(MD_BAND_TYPE_TYPE);
        put(MD_BROWSEGRAPHIC_PROPERTYTYPE_TYPE);
        put(MD_BROWSEGRAPHIC_TYPE_TYPE);
        put(MD_CELLGEOMETRYCODE_PROPERTYTYPE_TYPE);
        put(MD_CHARACTERSETCODE_PROPERTYTYPE_TYPE);
        put(MD_CLASSIFICATIONCODE_PROPERTYTYPE_TYPE);
        put(MD_CONSTRAINTS_PROPERTYTYPE_TYPE);
        put(MD_CONSTRAINTS_TYPE_TYPE);
        put(MD_CONTENTINFORMATION_PROPERTYTYPE_TYPE);
        put(MD_COVERAGECONTENTTYPECODE_PROPERTYTYPE_TYPE);
        put(MD_COVERAGEDESCRIPTION_PROPERTYTYPE_TYPE);
        put(MD_COVERAGEDESCRIPTION_TYPE_TYPE);
        put(MD_DATAIDENTIFICATION_PROPERTYTYPE_TYPE);
        put(MD_DATAIDENTIFICATION_TYPE_TYPE);
        put(MD_DATATYPECODE_PROPERTYTYPE_TYPE);
        put(MD_DIGITALTRANSFEROPTIONS_PROPERTYTYPE_TYPE);
        put(MD_DIGITALTRANSFEROPTIONS_TYPE_TYPE);
        put(MD_DIMENSIONNAMETYPECODE_PROPERTYTYPE_TYPE);
        put(MD_DIMENSION_PROPERTYTYPE_TYPE);
        put(MD_DIMENSION_TYPE_TYPE);
        put(MD_DISTRIBUTIONUNITS_PROPERTYTYPE_TYPE);
        put(MD_DISTRIBUTION_PROPERTYTYPE_TYPE);
        put(MD_DISTRIBUTION_TYPE_TYPE);
        put(MD_DISTRIBUTOR_PROPERTYTYPE_TYPE);
        put(MD_DISTRIBUTOR_TYPE_TYPE);
        put(MD_EXTENDEDELEMENTINFORMATION_PROPERTYTYPE_TYPE);
        put(MD_EXTENDEDELEMENTINFORMATION_TYPE_TYPE);
        put(MD_FEATURECATALOGUEDESCRIPTION_PROPERTYTYPE_TYPE);
        put(MD_FEATURECATALOGUEDESCRIPTION_TYPE_TYPE);
        put(MD_FORMAT_PROPERTYTYPE_TYPE);
        put(MD_FORMAT_TYPE_TYPE);
        put(MD_GEOMETRICOBJECTTYPECODE_PROPERTYTYPE_TYPE);
        put(MD_GEOMETRICOBJECTS_PROPERTYTYPE_TYPE);
        put(MD_GEOMETRICOBJECTS_TYPE_TYPE);
        put(MD_GEORECTIFIED_PROPERTYTYPE_TYPE);
        put(MD_GEORECTIFIED_TYPE_TYPE);
        put(MD_GEOREFERENCEABLE_PROPERTYTYPE_TYPE);
        put(MD_GEOREFERENCEABLE_TYPE_TYPE);
        put(MD_GRIDSPATIALREPRESENTATION_PROPERTYTYPE_TYPE);
        put(MD_GRIDSPATIALREPRESENTATION_TYPE_TYPE);
        put(MD_IDENTIFICATION_PROPERTYTYPE_TYPE);
        put(MD_IDENTIFIER_PROPERTYTYPE_TYPE);
        put(MD_IDENTIFIER_TYPE_TYPE);
        put(MD_IMAGEDESCRIPTION_PROPERTYTYPE_TYPE);
        put(MD_IMAGEDESCRIPTION_TYPE_TYPE);
        put(MD_IMAGINGCONDITIONCODE_PROPERTYTYPE_TYPE);
        put(MD_KEYWORDTYPECODE_PROPERTYTYPE_TYPE);
        put(MD_KEYWORDS_PROPERTYTYPE_TYPE);
        put(MD_KEYWORDS_TYPE_TYPE);
        put(MD_LEGALCONSTRAINTS_PROPERTYTYPE_TYPE);
        put(MD_LEGALCONSTRAINTS_TYPE_TYPE);
        put(MD_MAINTENANCEFREQUENCYCODE_PROPERTYTYPE_TYPE);
        put(MD_MAINTENANCEINFORMATION_PROPERTYTYPE_TYPE);
        put(MD_MAINTENANCEINFORMATION_TYPE_TYPE);
        put(MD_MEDIUMFORMATCODE_PROPERTYTYPE_TYPE);
        put(MD_MEDIUMNAMECODE_PROPERTYTYPE_TYPE);
        put(MD_MEDIUM_PROPERTYTYPE_TYPE);
        put(MD_MEDIUM_TYPE_TYPE);
        put(MD_METADATAEXTENSIONINFORMATION_PROPERTYTYPE_TYPE);
        put(MD_METADATAEXTENSIONINFORMATION_TYPE_TYPE);
        put(MD_METADATA_PROPERTYTYPE_TYPE);
        put(MD_METADATA_TYPE_TYPE);
        put(MD_OBLIGATIONCODE_PROPERTYTYPE_TYPE);
        put(MD_OBLIGATIONCODE_TYPE_TYPE);
        put(MD_PIXELORIENTATIONCODE_PROPERTYTYPE_TYPE);
        put(MD_PIXELORIENTATIONCODE_TYPE_TYPE);
        put(MD_PORTRAYALCATALOGUEREFERENCE_PROPERTYTYPE_TYPE);
        put(MD_PORTRAYALCATALOGUEREFERENCE_TYPE_TYPE);
        put(MD_PROGRESSCODE_PROPERTYTYPE_TYPE);
        put(MD_RANGEDIMENSION_PROPERTYTYPE_TYPE);
        put(MD_RANGEDIMENSION_TYPE_TYPE);
        put(MD_REFERENCESYSTEM_PROPERTYTYPE_TYPE);
        put(MD_REFERENCESYSTEM_TYPE_TYPE);
        put(MD_REPRESENTATIVEFRACTION_PROPERTYTYPE_TYPE);
        put(MD_REPRESENTATIVEFRACTION_TYPE_TYPE);
        put(MD_RESOLUTION_PROPERTYTYPE_TYPE);
        put(MD_RESOLUTION_TYPE_TYPE);
        put(MD_RESTRICTIONCODE_PROPERTYTYPE_TYPE);
        put(MD_SCOPECODE_PROPERTYTYPE_TYPE);
        put(MD_SCOPEDESCRIPTION_PROPERTYTYPE_TYPE);
        put(MD_SCOPEDESCRIPTION_TYPE_TYPE);
        put(MD_SECURITYCONSTRAINTS_PROPERTYTYPE_TYPE);
        put(MD_SECURITYCONSTRAINTS_TYPE_TYPE);
        put(MD_SERVICEIDENTIFICATION_PROPERTYTYPE_TYPE);
        put(MD_SERVICEIDENTIFICATION_TYPE_TYPE);
        put(MD_SPATIALREPRESENTATIONTYPECODE_PROPERTYTYPE_TYPE);
        put(MD_SPATIALREPRESENTATION_PROPERTYTYPE_TYPE);
        put(MD_STANDARDORDERPROCESS_PROPERTYTYPE_TYPE);
        put(MD_STANDARDORDERPROCESS_TYPE_TYPE);
        put(MD_TOPICCATEGORYCODE_PROPERTYTYPE_TYPE);
        put(MD_TOPICCATEGORYCODE_TYPE_TYPE);
        put(MD_TOPOLOGYLEVELCODE_PROPERTYTYPE_TYPE);
        put(MD_USAGE_PROPERTYTYPE_TYPE);
        put(MD_USAGE_TYPE_TYPE);
        put(MD_VECTORSPATIALREPRESENTATION_PROPERTYTYPE_TYPE);
        put(MD_VECTORSPATIALREPRESENTATION_TYPE_TYPE);
        put(PT_FREETEXT_PROPERTYTYPE_TYPE);
        put(PT_FREETEXT_TYPE_TYPE);
        put(PT_LOCALECONTAINER_PROPERTYTYPE_TYPE);
        put(PT_LOCALECONTAINER_TYPE_TYPE);
        put(PT_LOCALE_PROPERTYTYPE_TYPE);
        put(PT_LOCALE_TYPE_TYPE);
        put(RS_IDENTIFIER_PROPERTYTYPE_TYPE);
        put(RS_IDENTIFIER_TYPE_TYPE);
        put(RS_REFERENCESYSTEM_PROPERTYTYPE_TYPE);
        put(URL_PROPERTYTYPE_TYPE);
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
        Schema schema = new GMDSchema();
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