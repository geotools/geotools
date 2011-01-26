/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import net.opengis.gml.CodeListType;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getAxisDescription <em>Axis Description</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getAxisDescription1 <em>Axis Description1</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getCapability <em>Capability</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getContentMetadata <em>Content Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getCoverageDescription <em>Coverage Description</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getCoverageOffering <em>Coverage Offering</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getCoverageOfferingBrief <em>Coverage Offering Brief</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getDescription <em>Description</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getDomainSet <em>Domain Set</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getFormats <em>Formats</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getGetCoverage <em>Get Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getInterpolationMethod <em>Interpolation Method</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getInterval <em>Interval</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getLonLatEnvelope <em>Lon Lat Envelope</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getMetadataLink <em>Metadata Link</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getRangeSet <em>Range Set</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getRangeSet1 <em>Range Set1</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getSingleValue <em>Single Value</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getSpatialDomain <em>Spatial Domain</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getSpatialSubset <em>Spatial Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getSupportedCRSs <em>Supported CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getSupportedFormats <em>Supported Formats</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getSupportedInterpolations <em>Supported Interpolations</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getTemporalDomain <em>Temporal Domain</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getTemporalSubset <em>Temporal Subset</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getTimePeriod <em>Time Period</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getTimeSequence <em>Time Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getWCSCapabilities <em>WCS Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getClosure <em>Closure</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getSemantic <em>Semantic</em>}</li>
 *   <li>{@link net.opengis.wcs10.DocumentRoot#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
    /**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
    FeatureMap getMixed();

    /**
	 * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
    EMap getXMLNSPrefixMap();

    /**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
    EMap getXSISchemaLocation();

    /**
	 * Returns the value of the '<em><b>Axis Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * GML property containing one AxisDescription GML object.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Axis Description</em>' containment reference.
	 * @see #setAxisDescription(AxisDescriptionType1)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_AxisDescription()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='axisDescription' namespace='##targetNamespace'"
	 * @generated
	 */
    AxisDescriptionType1 getAxisDescription();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getAxisDescription <em>Axis Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Axis Description</em>' containment reference.
	 * @see #getAxisDescription()
	 * @generated
	 */
    void setAxisDescription(AxisDescriptionType1 value);

    /**
	 * Returns the value of the '<em><b>Axis Description1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Axis Description1</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Axis Description1</em>' containment reference.
	 * @see #setAxisDescription1(AxisDescriptionType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_AxisDescription1()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='AxisDescription' namespace='##targetNamespace' affiliation='http://www.opengis.net/gml#_GML'"
	 * @generated
	 */
    AxisDescriptionType getAxisDescription1();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getAxisDescription1 <em>Axis Description1</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Axis Description1</em>' containment reference.
	 * @see #getAxisDescription1()
	 * @generated
	 */
    void setAxisDescription1(AxisDescriptionType value);

    /**
	 * Returns the value of the '<em><b>Capability</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Capability</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Capability</em>' containment reference.
	 * @see #setCapability(WCSCapabilityType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Capability()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Capability' namespace='##targetNamespace'"
	 * @generated
	 */
    WCSCapabilityType getCapability();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getCapability <em>Capability</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capability</em>' containment reference.
	 * @see #getCapability()
	 * @generated
	 */
    void setCapability(WCSCapabilityType value);

    /**
	 * Returns the value of the '<em><b>Content Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of brief descriptions of all coverages avaialble from this WCS, or a reference to another service from which this information is available.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Content Metadata</em>' containment reference.
	 * @see #setContentMetadata(ContentMetadataType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_ContentMetadata()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='ContentMetadata' namespace='##targetNamespace'"
	 * @generated
	 */
    ContentMetadataType getContentMetadata();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getContentMetadata <em>Content Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content Metadata</em>' containment reference.
	 * @see #getContentMetadata()
	 * @generated
	 */
    void setContentMetadata(ContentMetadataType value);

    /**
	 * Returns the value of the '<em><b>Coverage Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reply from a WCS that performed the DescribeCoverage operation, containing one or more full coverage offering descriptions.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Coverage Description</em>' containment reference.
	 * @see #setCoverageDescription(CoverageDescriptionType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_CoverageDescription()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='CoverageDescription' namespace='##targetNamespace'"
	 * @generated
	 */
    CoverageDescriptionType getCoverageDescription();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getCoverageDescription <em>Coverage Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Coverage Description</em>' containment reference.
	 * @see #getCoverageDescription()
	 * @generated
	 */
    void setCoverageDescription(CoverageDescriptionType value);

    /**
	 * Returns the value of the '<em><b>Coverage Offering</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverage Offering</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Coverage Offering</em>' containment reference.
	 * @see #setCoverageOffering(CoverageOfferingType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_CoverageOffering()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='CoverageOffering' namespace='##targetNamespace' affiliation='http://www.opengis.net/gml#_GML'"
	 * @generated
	 */
    CoverageOfferingType getCoverageOffering();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getCoverageOffering <em>Coverage Offering</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Coverage Offering</em>' containment reference.
	 * @see #getCoverageOffering()
	 * @generated
	 */
    void setCoverageOffering(CoverageOfferingType value);

    /**
	 * Returns the value of the '<em><b>Coverage Offering Brief</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverage Offering Brief</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Coverage Offering Brief</em>' containment reference.
	 * @see #setCoverageOfferingBrief(CoverageOfferingBriefType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_CoverageOfferingBrief()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='CoverageOfferingBrief' namespace='##targetNamespace' affiliation='http://www.opengis.net/gml#_GML'"
	 * @generated
	 */
    CoverageOfferingBriefType getCoverageOfferingBrief();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getCoverageOfferingBrief <em>Coverage Offering Brief</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Coverage Offering Brief</em>' containment reference.
	 * @see #getCoverageOfferingBrief()
	 * @generated
	 */
    void setCoverageOfferingBrief(CoverageOfferingBriefType value);

    /**
	 * Returns the value of the '<em><b>Describe Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Request to a WCS to perform the DescribeCoverage operation. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Describe Coverage</em>' containment reference.
	 * @see #setDescribeCoverage(DescribeCoverageType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_DescribeCoverage()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='DescribeCoverage' namespace='##targetNamespace'"
	 * @generated
	 */
    DescribeCoverageType getDescribeCoverage();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Describe Coverage</em>' containment reference.
	 * @see #getDescribeCoverage()
	 * @generated
	 */
    void setDescribeCoverage(DescribeCoverageType value);

    /**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Contains a simple text description of the object.
	 * For WCS use, removed optional AssociationAttributeGroup from gml:description.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Description()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
    String getDescription();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);

    /**
	 * Returns the value of the '<em><b>Domain Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Domain Set</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Domain Set</em>' containment reference.
	 * @see #setDomainSet(DomainSetType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_DomainSet()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='domainSet' namespace='##targetNamespace'"
	 * @generated
	 */
    DomainSetType getDomainSet();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getDomainSet <em>Domain Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Domain Set</em>' containment reference.
	 * @see #getDomainSet()
	 * @generated
	 */
    void setDomainSet(DomainSetType value);

    /**
	 * Returns the value of the '<em><b>Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Identifiers of one or more formats in which coverage content can be retrieved. The codeSpace optional attribute can reference the semantic of the format identifiers.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Formats</em>' containment reference.
	 * @see #setFormats(CodeListType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Formats()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='formats' namespace='##targetNamespace'"
	 * @generated
	 */
    CodeListType getFormats();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getFormats <em>Formats</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Formats</em>' containment reference.
	 * @see #getFormats()
	 * @generated
	 */
    void setFormats(CodeListType value);

    /**
	 * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Request to a WCS to perform the GetCapabilities operation. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Get Capabilities</em>' containment reference.
	 * @see #setGetCapabilities(GetCapabilitiesType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_GetCapabilities()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
	 * @generated
	 */
    GetCapabilitiesType getGetCapabilities();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Capabilities</em>' containment reference.
	 * @see #getGetCapabilities()
	 * @generated
	 */
    void setGetCapabilities(GetCapabilitiesType value);

    /**
	 * Returns the value of the '<em><b>Get Coverage</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Request to a WCS to perform the GetCoverage operation. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Get Coverage</em>' containment reference.
	 * @see #setGetCoverage(GetCoverageType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_GetCoverage()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='GetCoverage' namespace='##targetNamespace'"
	 * @generated
	 */
    GetCoverageType getGetCoverage();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getGetCoverage <em>Get Coverage</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Coverage</em>' containment reference.
	 * @see #getGetCoverage()
	 * @generated
	 */
    void setGetCoverage(GetCoverageType value);

    /**
	 * Returns the value of the '<em><b>Interpolation Method</b></em>' attribute.
	 * The default value is <code>"nearest neighbor"</code>.
	 * The literals are from the enumeration {@link net.opengis.wcs10.InterpolationMethodType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Interpolation Method</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Interpolation Method</em>' attribute.
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @see #setInterpolationMethod(InterpolationMethodType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_InterpolationMethod()
	 * @model default="nearest neighbor" unique="false" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='interpolationMethod' namespace='##targetNamespace'"
	 * @generated
	 */
    InterpolationMethodType getInterpolationMethod();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getInterpolationMethod <em>Interpolation Method</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interpolation Method</em>' attribute.
	 * @see net.opengis.wcs10.InterpolationMethodType
	 * @see #getInterpolationMethod()
	 * @generated
	 */
    void setInterpolationMethod(InterpolationMethodType value);

    /**
	 * Returns the value of the '<em><b>Interval</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Interval</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Interval</em>' containment reference.
	 * @see #setInterval(IntervalType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Interval()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='interval' namespace='##targetNamespace'"
	 * @generated
	 */
    IntervalType getInterval();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getInterval <em>Interval</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interval</em>' containment reference.
	 * @see #getInterval()
	 * @generated
	 */
    void setInterval(IntervalType value);

    /**
	 * Returns the value of the '<em><b>Keywords</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of one or more commonly used or formalised word(s) or phrase(s) used to describe the subject. When needed, the optional "type" can name the type of the associated list of keywords that shall all have the same type. Also when needed, the codeSpace attribute of that "type" can also reference the type name authority and/or thesaurus. (Largely based on MD_Keywords class in ISO 19115.)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Keywords</em>' containment reference.
	 * @see #setKeywords(KeywordsType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Keywords()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='keywords' namespace='##targetNamespace'"
	 * @generated
	 */
    KeywordsType getKeywords();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getKeywords <em>Keywords</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Keywords</em>' containment reference.
	 * @see #getKeywords()
	 * @generated
	 */
    void setKeywords(KeywordsType value);

    /**
	 * Returns the value of the '<em><b>Lon Lat Envelope</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lon Lat Envelope</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Lon Lat Envelope</em>' containment reference.
	 * @see #setLonLatEnvelope(LonLatEnvelopeType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_LonLatEnvelope()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='lonLatEnvelope' namespace='##targetNamespace'"
	 * @generated
	 */
    LonLatEnvelopeType getLonLatEnvelope();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getLonLatEnvelope <em>Lon Lat Envelope</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lon Lat Envelope</em>' containment reference.
	 * @see #getLonLatEnvelope()
	 * @generated
	 */
    void setLonLatEnvelope(LonLatEnvelopeType value);

    /**
	 * Returns the value of the '<em><b>Metadata Link</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata Link</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Metadata Link</em>' containment reference.
	 * @see #setMetadataLink(MetadataLinkType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_MetadataLink()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='metadataLink' namespace='##targetNamespace' affiliation='http://www.opengis.net/gml#metaDataProperty'"
	 * @generated
	 */
    MetadataLinkType getMetadataLink();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getMetadataLink <em>Metadata Link</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Metadata Link</em>' containment reference.
	 * @see #getMetadataLink()
	 * @generated
	 */
    void setMetadataLink(MetadataLinkType value);

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Identifier for the object, normally a descriptive name.
	 * For WCS use, removed optional CodeSpace attribute from gml:name.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Name()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='name' namespace='##targetNamespace'"
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Range Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * GML property containing one RangeSet GML object.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Range Set</em>' containment reference.
	 * @see #setRangeSet(RangeSetType1)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_RangeSet()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='rangeSet' namespace='##targetNamespace'"
	 * @generated
	 */
    RangeSetType1 getRangeSet();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getRangeSet <em>Range Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Range Set</em>' containment reference.
	 * @see #getRangeSet()
	 * @generated
	 */
    void setRangeSet(RangeSetType1 value);

    /**
	 * Returns the value of the '<em><b>Range Set1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range Set1</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Range Set1</em>' containment reference.
	 * @see #setRangeSet1(RangeSetType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_RangeSet1()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='RangeSet' namespace='##targetNamespace' affiliation='http://www.opengis.net/gml#_GML'"
	 * @generated
	 */
    RangeSetType getRangeSet1();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getRangeSet1 <em>Range Set1</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Range Set1</em>' containment reference.
	 * @see #getRangeSet1()
	 * @generated
	 */
    void setRangeSet1(RangeSetType value);

    /**
	 * Returns the value of the '<em><b>Service</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Service</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Service</em>' containment reference.
	 * @see #setService(ServiceType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Service()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Service' namespace='##targetNamespace' affiliation='http://www.opengis.net/gml#_GML'"
	 * @generated
	 */
    ServiceType getService();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getService <em>Service</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service</em>' containment reference.
	 * @see #getService()
	 * @generated
	 */
    void setService(ServiceType value);

    /**
	 * Returns the value of the '<em><b>Single Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A single value for a quantity.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Single Value</em>' containment reference.
	 * @see #setSingleValue(TypedLiteralType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_SingleValue()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='singleValue' namespace='##targetNamespace'"
	 * @generated
	 */
    TypedLiteralType getSingleValue();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getSingleValue <em>Single Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Single Value</em>' containment reference.
	 * @see #getSingleValue()
	 * @generated
	 */
    void setSingleValue(TypedLiteralType value);

    /**
	 * Returns the value of the '<em><b>Spatial Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Spatial Domain</em>' containment reference.
	 * @see #setSpatialDomain(SpatialDomainType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_SpatialDomain()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='spatialDomain' namespace='##targetNamespace'"
	 * @generated
	 */
    SpatialDomainType getSpatialDomain();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getSpatialDomain <em>Spatial Domain</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spatial Domain</em>' containment reference.
	 * @see #getSpatialDomain()
	 * @generated
	 */
    void setSpatialDomain(SpatialDomainType value);

    /**
	 * Returns the value of the '<em><b>Spatial Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Subset</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Spatial Subset</em>' containment reference.
	 * @see #setSpatialSubset(SpatialSubsetType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_SpatialSubset()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='spatialSubset' namespace='##targetNamespace'"
	 * @generated
	 */
    SpatialSubsetType getSpatialSubset();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getSpatialSubset <em>Spatial Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spatial Subset</em>' containment reference.
	 * @see #getSpatialSubset()
	 * @generated
	 */
    void setSpatialSubset(SpatialSubsetType value);

    /**
	 * Returns the value of the '<em><b>Supported CR Ss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Supported CR Ss</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Supported CR Ss</em>' containment reference.
	 * @see #setSupportedCRSs(SupportedCRSsType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_SupportedCRSs()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='supportedCRSs' namespace='##targetNamespace'"
	 * @generated
	 */
    SupportedCRSsType getSupportedCRSs();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getSupportedCRSs <em>Supported CR Ss</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Supported CR Ss</em>' containment reference.
	 * @see #getSupportedCRSs()
	 * @generated
	 */
    void setSupportedCRSs(SupportedCRSsType value);

    /**
	 * Returns the value of the '<em><b>Supported Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Supported Formats</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Supported Formats</em>' containment reference.
	 * @see #setSupportedFormats(SupportedFormatsType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_SupportedFormats()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='supportedFormats' namespace='##targetNamespace'"
	 * @generated
	 */
    SupportedFormatsType getSupportedFormats();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getSupportedFormats <em>Supported Formats</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Supported Formats</em>' containment reference.
	 * @see #getSupportedFormats()
	 * @generated
	 */
    void setSupportedFormats(SupportedFormatsType value);

    /**
	 * Returns the value of the '<em><b>Supported Interpolations</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Supported Interpolations</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Supported Interpolations</em>' containment reference.
	 * @see #setSupportedInterpolations(SupportedInterpolationsType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_SupportedInterpolations()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='supportedInterpolations' namespace='##targetNamespace'"
	 * @generated
	 */
    SupportedInterpolationsType getSupportedInterpolations();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getSupportedInterpolations <em>Supported Interpolations</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Supported Interpolations</em>' containment reference.
	 * @see #getSupportedInterpolations()
	 * @generated
	 */
    void setSupportedInterpolations(SupportedInterpolationsType value);

    /**
	 * Returns the value of the '<em><b>Temporal Domain</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Defines the temporal domain of a coverage offering, that is, the times for which valid data are available. The times shall to be ordered from the oldest to the newest.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Temporal Domain</em>' containment reference.
	 * @see #setTemporalDomain(TimeSequenceType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_TemporalDomain()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='temporalDomain' namespace='##targetNamespace'"
	 * @generated
	 */
    TimeSequenceType getTemporalDomain();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getTemporalDomain <em>Temporal Domain</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporal Domain</em>' containment reference.
	 * @see #getTemporalDomain()
	 * @generated
	 */
    void setTemporalDomain(TimeSequenceType value);

    /**
	 * Returns the value of the '<em><b>Temporal Subset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Subset</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Temporal Subset</em>' containment reference.
	 * @see #setTemporalSubset(TimeSequenceType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_TemporalSubset()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='temporalSubset' namespace='##targetNamespace'"
	 * @generated
	 */
    TimeSequenceType getTemporalSubset();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getTemporalSubset <em>Temporal Subset</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporal Subset</em>' containment reference.
	 * @see #getTemporalSubset()
	 * @generated
	 */
    void setTemporalSubset(TimeSequenceType value);

    /**
	 * Returns the value of the '<em><b>Time Period</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Period</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Period</em>' containment reference.
	 * @see #setTimePeriod(TimePeriodType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_TimePeriod()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='timePeriod' namespace='##targetNamespace'"
	 * @generated
	 */
    TimePeriodType getTimePeriod();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getTimePeriod <em>Time Period</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Period</em>' containment reference.
	 * @see #getTimePeriod()
	 * @generated
	 */
    void setTimePeriod(TimePeriodType value);

    /**
	 * Returns the value of the '<em><b>Time Sequence</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Sequence</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Sequence</em>' containment reference.
	 * @see #setTimeSequence(TimeSequenceType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_TimeSequence()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='TimeSequence' namespace='##targetNamespace'"
	 * @generated
	 */
    TimeSequenceType getTimeSequence();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getTimeSequence <em>Time Sequence</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Sequence</em>' containment reference.
	 * @see #getTimeSequence()
	 * @generated
	 */
    void setTimeSequence(TimeSequenceType value);

    /**
	 * Returns the value of the '<em><b>WCS Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>WCS Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>WCS Capabilities</em>' containment reference.
	 * @see #setWCSCapabilities(WCSCapabilitiesType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_WCSCapabilities()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='WCS_Capabilities' namespace='##targetNamespace'"
	 * @generated
	 */
    WCSCapabilitiesType getWCSCapabilities();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getWCSCapabilities <em>WCS Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>WCS Capabilities</em>' containment reference.
	 * @see #getWCSCapabilities()
	 * @generated
	 */
    void setWCSCapabilities(WCSCapabilitiesType value);

    /**
	 * Returns the value of the '<em><b>Closure</b></em>' attribute.
	 * The default value is <code>"closed"</code>.
	 * The literals are from the enumeration {@link net.opengis.wcs10.ClosureType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies which of the minimum and maximum values are included in the range. Note that plus and minus infinity are considered closed bounds.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Closure</em>' attribute.
	 * @see net.opengis.wcs10.ClosureType
	 * @see #isSetClosure()
	 * @see #unsetClosure()
	 * @see #setClosure(ClosureType)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Closure()
	 * @model default="closed" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='closure' namespace='##targetNamespace'"
	 * @generated
	 */
    ClosureType getClosure();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getClosure <em>Closure</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Closure</em>' attribute.
	 * @see net.opengis.wcs10.ClosureType
	 * @see #isSetClosure()
	 * @see #unsetClosure()
	 * @see #getClosure()
	 * @generated
	 */
    void setClosure(ClosureType value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.DocumentRoot#getClosure <em>Closure</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetClosure()
	 * @see #getClosure()
	 * @see #setClosure(ClosureType)
	 * @generated
	 */
    void unsetClosure();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.DocumentRoot#getClosure <em>Closure</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Closure</em>' attribute is set.
	 * @see #unsetClosure()
	 * @see #getClosure()
	 * @see #setClosure(ClosureType)
	 * @generated
	 */
    boolean isSetClosure();

    /**
	 * Returns the value of the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Definition of the semantics or meaning of the values in the XML element it belongs to. The value of this "semantic" attribute can be a RDF Property or Class of a taxonomy or ontology.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Semantic</em>' attribute.
	 * @see #setSemantic(String)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Semantic()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='semantic' namespace='##targetNamespace'"
	 * @generated
	 */
    String getSemantic();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getSemantic <em>Semantic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Semantic</em>' attribute.
	 * @see #getSemantic()
	 * @generated
	 */
    void setSemantic(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Datatype of a typed literal value. This URI typically references XSD simple types. It has the same semantic as rdf:datatype.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see net.opengis.wcs10.Wcs10Package#getDocumentRoot_Type()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='type' namespace='##targetNamespace'"
	 * @generated
	 */
    String getType();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.DocumentRoot#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
    void setType(String value);

} // DocumentRoot
