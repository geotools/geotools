/**
 */
package net.opengis.wcs20;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getContents <em>Contents</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getCoverageDescription <em>Coverage Description</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getCoverageDescriptions <em>Coverage Descriptions</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getCoverageId <em>Coverage Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getCoverageOfferings <em>Coverage Offerings</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getCoverageSubtype <em>Coverage Subtype</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getCoverageSummary <em>Coverage Summary</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getDimensionSlice <em>Dimension Slice</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getDimensionSubset <em>Dimension Subset</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getDimensionTrim <em>Dimension Trim</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getExtension <em>Extension</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getGetCoverage <em>Get Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getOfferedCoverage <em>Offered Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getServiceMetadata <em>Service Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs20.DocumentRoot#getServiceParameters <em>Service Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
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
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_XMLNSPrefixMap()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
     * @generated
     */
    EMap<String, String> getXMLNSPrefixMap();

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
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap<String, String> getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * XML encoded WCS GetCapabilities operation response. The Capabilities document provides clients with service metadata about a specific service instance, including metadata about the coverages served. If the server does not implement the updateSequence parameter, the server shall always return the Capabilities document, without the updateSequence parameter. When the server implements the updateSequence parameter and the GetCapabilities operation request included the updateSequence parameter with the current value, the server shall return this element with only the "version" and "updateSequence" attributes. Otherwise, all optional sections shall be included or not depending on the actual value of the Contents parameter in the GetCapabilities operation request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Capabilities</em>' containment reference.
     * @see #setCapabilities(CapabilitiesType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_Capabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    CapabilitiesType getCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getCapabilities <em>Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Capabilities</em>' containment reference.
     * @see #getCapabilities()
     * @generated
     */
    void setCapabilities(CapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Contents</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element redefines the OWS Common [OGC 06-121r9] Contents section with a CoverageSummary, in accordance with the rules for modification laid down there. In addition it allows WCS extensions or application profiles to extend its content.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Contents</em>' containment reference.
     * @see #setContents(ContentsType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_Contents()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Contents' namespace='##targetNamespace'"
     * @generated
     */
    ContentsType getContents();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getContents <em>Contents</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Contents</em>' containment reference.
     * @see #getContents()
     * @generated
     */
    void setContents(ContentsType value);

    /**
     * Returns the value of the '<em><b>Coverage Description</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description of a coverage available from a WCS server. This description shall include sufficient information to allow all valid GetCoverage operation requests to be prepared by a WCS client.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Description</em>' containment reference.
     * @see #setCoverageDescription(CoverageDescriptionType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_CoverageDescription()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoverageDescription' namespace='##targetNamespace' affiliation='http://www.opengis.net/gml/3.2#AbstractFeature'"
     * @generated
     */
    CoverageDescriptionType getCoverageDescription();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getCoverageDescription <em>Coverage Description</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Description</em>' containment reference.
     * @see #getCoverageDescription()
     * @generated
     */
    void setCoverageDescription(CoverageDescriptionType value);

    /**
     * Returns the value of the '<em><b>Coverage Descriptions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Response from a WCS DescribeCoverage operation, containing one or more coverage descriptions.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Descriptions</em>' containment reference.
     * @see #setCoverageDescriptions(CoverageDescriptionsType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_CoverageDescriptions()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoverageDescriptions' namespace='##targetNamespace'"
     * @generated
     */
    CoverageDescriptionsType getCoverageDescriptions();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getCoverageDescriptions <em>Coverage Descriptions</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Descriptions</em>' containment reference.
     * @see #getCoverageDescriptions()
     * @generated
     */
    void setCoverageDescriptions(CoverageDescriptionsType value);

    /**
     * Returns the value of the '<em><b>Coverage Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element represents coverage identifiers. It uses the same type as gml:id to allow for identifier values to be used in both contexts.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Id</em>' attribute.
     * @see #setCoverageId(String)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_CoverageId()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoverageId' namespace='##targetNamespace'"
     * @generated
     */
    String getCoverageId();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getCoverageId <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Id</em>' attribute.
     * @see #getCoverageId()
     * @generated
     */
    void setCoverageId(String value);

    /**
     * Returns the value of the '<em><b>Coverage Offerings</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * CoverageOfferings is the virtual document that a WCS offers. It consists of service metadata and a set of offered coverages. The CoverageOfferings element is never delivered to the client, however, responses of WCS requests are composed of constituents of the CoverageOfferings tree. Hence, CoverageOfferings serves to define responses.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Offerings</em>' containment reference.
     * @see #setCoverageOfferings(CoverageOfferingsType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_CoverageOfferings()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoverageOfferings' namespace='##targetNamespace'"
     * @generated
     */
    CoverageOfferingsType getCoverageOfferings();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getCoverageOfferings <em>Coverage Offerings</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Offerings</em>' containment reference.
     * @see #getCoverageOfferings()
     * @generated
     */
    void setCoverageOfferings(CoverageOfferingsType value);

    /**
     * Returns the value of the '<em><b>Coverage Subtype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * CoverageSubtype characterizes the type of a coverage. This element shall contain the name of the XML root element that would be delivered if a GML encoded result were requested from the GetCoverage operation. The content model of the named element shall be described by a schema that is either normatively referenced by the WCS core specification or by a requirement in a WCS extension, the associated conformance class for which has been included in the ows:Profiles of the server's GetCapabilities response.
     * This CoverageSubtype is delivered in GetCapabilities and DescribeCoverage to allow clients an estimation of the amount of data to be expected in the domain and range set. For example, a GridCoverage has a small domain set structure, but typically a large range set; a MultiSolidCoverage, on the other hand, tends to have large domain sets and small range sets.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Subtype</em>' attribute.
     * @see #setCoverageSubtype(QName)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_CoverageSubtype()
     * @model unique="false" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoverageSubtype' namespace='##targetNamespace'"
     */
    QName getCoverageSubtype();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getCoverageSubtype <em>Coverage Subtype</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Subtype</em>' attribute.
     * @see #getCoverageSubtype()
     * @generated
     */
    void setCoverageSubtype(QName value);

    /**
     * Returns the value of the '<em><b>Coverage Subtype Parent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Subtype Parent</em>' containment reference.
     * @see #setCoverageSubtypeParent(CoverageSubtypeParentType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_CoverageSubtypeParent()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoverageSubtypeParent' namespace='##targetNamespace'"
     * @generated
     */
    CoverageSubtypeParentType getCoverageSubtypeParent();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Subtype Parent</em>' containment reference.
     * @see #getCoverageSubtypeParent()
     * @generated
     */
    void setCoverageSubtypeParent(CoverageSubtypeParentType value);

    /**
     * Returns the value of the '<em><b>Coverage Summary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A CoverageSummary contains information essential for accessing a coverage served by a WCS. The CoverageId is the identifier used to address a particular coverage. The CoverageSubtype is the name of the root of this coverage when expressed in XML.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Summary</em>' containment reference.
     * @see #setCoverageSummary(CoverageSummaryType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_CoverageSummary()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoverageSummary' namespace='##targetNamespace'"
     * @generated
     */
    CoverageSummaryType getCoverageSummary();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getCoverageSummary <em>Coverage Summary</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Summary</em>' containment reference.
     * @see #getCoverageSummary()
     * @generated
     */
    void setCoverageSummary(CoverageSummaryType value);

    /**
     * Returns the value of the '<em><b>Describe Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Request to a WCS to perform the DescribeCoverage operation. This operation allows a client to retrieve descriptions of one or more coverages. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Describe Coverage</em>' containment reference.
     * @see #setDescribeCoverage(DescribeCoverageType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_DescribeCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DescribeCoverage' namespace='##targetNamespace'"
     * @generated
     */
    DescribeCoverageType getDescribeCoverage();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Describe Coverage</em>' containment reference.
     * @see #getDescribeCoverage()
     * @generated
     */
    void setDescribeCoverage(DescribeCoverageType value);

    /**
     * Returns the value of the '<em><b>Dimension Slice</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Describes the slicing of a coverage's domain axis at a particular point.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dimension Slice</em>' containment reference.
     * @see #setDimensionSlice(DimensionSliceType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_DimensionSlice()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DimensionSlice' namespace='##targetNamespace' affiliation='DimensionSubset'"
     * @generated
     */
    DimensionSliceType getDimensionSlice();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getDimensionSlice <em>Dimension Slice</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dimension Slice</em>' containment reference.
     * @see #getDimensionSlice()
     * @generated
     */
    void setDimensionSlice(DimensionSliceType value);

    /**
     * Returns the value of the '<em><b>Dimension Subset</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of the desired subset of the domain of the coverage. This is either a Trim operation, or a Slice operation.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dimension Subset</em>' containment reference.
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_DimensionSubset()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DimensionSubset' namespace='##targetNamespace'"
     * @generated
     */
    DimensionSubsetType getDimensionSubset();

    /**
     * Returns the value of the '<em><b>Dimension Trim</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Describes the trimming of a coverage's domain axis, between two values.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dimension Trim</em>' containment reference.
     * @see #setDimensionTrim(DimensionTrimType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_DimensionTrim()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DimensionTrim' namespace='##targetNamespace' affiliation='DimensionSubset'"
     * @generated
     */
    DimensionTrimType getDimensionTrim();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getDimensionTrim <em>Dimension Trim</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dimension Trim</em>' containment reference.
     * @see #getDimensionTrim()
     * @generated
     */
    void setDimensionTrim(DimensionTrimType value);

    /**
     * Returns the value of the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Extension element used to hook in additional content e.g. in extensions or application profiles.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Extension</em>' containment reference.
     * @see #setExtension(ExtensionType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_Extension()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Extension' namespace='##targetNamespace'"
     * @generated
     */
    ExtensionType getExtension();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getExtension <em>Extension</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extension</em>' containment reference.
     * @see #getExtension()
     * @generated
     */
    void setExtension(ExtensionType value);

    /**
     * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Request to a WCS server to perform the GetCapabilities operation. This operation allows a client to retrieve a Capabilities XML document providing metadata for the specific WCS server. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get Capabilities</em>' containment reference.
     * @see #setGetCapabilities(GetCapabilitiesType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_GetCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
     * @generated
     */
    GetCapabilitiesType getGetCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
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
     * Request to a WCS to perform the GetCoverage operation. This operation allows a client to retrieve a subset of one coverage.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get Coverage</em>' containment reference.
     * @see #setGetCoverage(GetCoverageType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_GetCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetCoverage' namespace='##targetNamespace'"
     * @generated
     */
    GetCoverageType getGetCoverage();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getGetCoverage <em>Get Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Coverage</em>' containment reference.
     * @see #getGetCoverage()
     * @generated
     */
    void setGetCoverage(GetCoverageType value);

    /**
     * Returns the value of the '<em><b>Offered Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An OfferedCoverage is the information set about a specific coverage offered by the WCS service on hand. It consists of a coverage, as defined in the GML Application Schema for Coverages [OGC 09-146r2] and coverage specific service parameters. Like CoverageOfferings, an OfferedCoverage element is never delivered to the client, but defines the response of coverage access requests.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Offered Coverage</em>' containment reference.
     * @see #setOfferedCoverage(OfferedCoverageType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_OfferedCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='OfferedCoverage' namespace='##targetNamespace'"
     * @generated
     */
    OfferedCoverageType getOfferedCoverage();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getOfferedCoverage <em>Offered Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Offered Coverage</em>' containment reference.
     * @see #getOfferedCoverage()
     * @generated
     */
    void setOfferedCoverage(OfferedCoverageType value);

    /**
     * Returns the value of the '<em><b>Service Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * ServiceMetadata contains information describing the WCS service on hand. Extension elements allow WCS extension standards to define their individual extra service metadata.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Metadata</em>' containment reference.
     * @see #setServiceMetadata(ServiceMetadataType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_ServiceMetadata()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ServiceMetadata' namespace='##targetNamespace'"
     * @generated
     */
    ServiceMetadataType getServiceMetadata();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getServiceMetadata <em>Service Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Metadata</em>' containment reference.
     * @see #getServiceMetadata()
     * @generated
     */
    void setServiceMetadata(ServiceMetadataType value);

    /**
     * Returns the value of the '<em><b>Service Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * ServiceParameters further define how the corresponding coverage is accessible. CoverageSubtype helps identifying the type of coverage on hand, in particular with respect to the potential size of its domainSet and rangeSet components. Extension elements allow WCS extensions to plug in their particular coverage-specific service information.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Parameters</em>' containment reference.
     * @see #setServiceParameters(ServiceParametersType)
     * @see net.opengis.wcs20.Wcs20Package#getDocumentRoot_ServiceParameters()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ServiceParameters' namespace='##targetNamespace'"
     * @generated
     */
    ServiceParametersType getServiceParameters();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DocumentRoot#getServiceParameters <em>Service Parameters</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Parameters</em>' containment reference.
     * @see #getServiceParameters()
     * @generated
     */
    void setServiceParameters(ServiceParametersType value);

} // DocumentRoot
