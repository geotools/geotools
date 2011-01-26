/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import net.opengis.ows11.ReferenceGroupType;

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
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getAvailableKeys <em>Available Keys</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getAxisSubset <em>Axis Subset</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getContents <em>Contents</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getCoverage <em>Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getCoverageDescriptions <em>Coverage Descriptions</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getCoverages <em>Coverages</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getCoverageSummary <em>Coverage Summary</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getGetCoverage <em>Get Coverage</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getGridBaseCRS <em>Grid Base CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getGridCRS <em>Grid CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getGridCS <em>Grid CS</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getGridOffsets <em>Grid Offsets</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getGridOrigin <em>Grid Origin</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getGridType <em>Grid Type</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getInterpolationMethods <em>Interpolation Methods</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getTemporalDomain <em>Temporal Domain</em>}</li>
 *   <li>{@link net.opengis.wcs11.DocumentRoot#getTemporalSubset <em>Temporal Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot()
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
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_Mixed()
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
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_XMLNSPrefixMap()
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
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Available Keys</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of all the available (valid) key values for this axis. For numeric keys, signed values should be ordered from negative infinity to positive infinity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Available Keys</em>' containment reference.
     * @see #setAvailableKeys(AvailableKeysType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_AvailableKeys()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AvailableKeys' namespace='##targetNamespace'"
     * @generated
     */
    AvailableKeysType getAvailableKeys();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getAvailableKeys <em>Available Keys</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Available Keys</em>' containment reference.
     * @see #getAvailableKeys()
     * @generated
     */
    void setAvailableKeys(AvailableKeysType value);

    /**
     * Returns the value of the '<em><b>Axis Subset</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of selected Keys for this axis, to be used for selecting values in a vector range field. TBD. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Axis Subset</em>' containment reference.
     * @see #setAxisSubset(AxisSubsetType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_AxisSubset()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AxisSubset' namespace='##targetNamespace'"
     * @generated
     */
    AxisSubsetType getAxisSubset();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getAxisSubset <em>Axis Subset</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis Subset</em>' containment reference.
     * @see #getAxisSubset()
     * @generated
     */
    void setAxisSubset(AxisSubsetType value);

    /**
     * Returns the value of the '<em><b>Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * XML encoded WCS GetCapabilities operation response. The Capabilities document provides clients with service metadata about a specific service instance, including metadata about the coverages served. If the server does not implement the updateSequence parameter, the server shall always return the Capabilities document, without the updateSequence parameter. When the server implements the updateSequence parameter and the GetCapabilities operation request included the updateSequence parameter with the current value, the server shall return this element with only the "version" and "updateSequence" attributes. Otherwise, all optional sections shall be included or not depending on the actual value of the Contents parameter in the GetCapabilities operation request. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Capabilities</em>' containment reference.
     * @see #setCapabilities(CapabilitiesType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_Capabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    CapabilitiesType getCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getCapabilities <em>Capabilities</em>}' containment reference.
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
     * Contents section of WCS service metadata (or Capabilities) XML document. For the WCS, these contents are brief metadata about the coverages available from this server, or a reference to another source from which this metadata is available. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Contents</em>' containment reference.
     * @see #setContents(ContentsType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_Contents()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Contents' namespace='##targetNamespace'"
     * @generated
     */
    ContentsType getContents();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getContents <em>Contents</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Contents</em>' containment reference.
     * @see #getContents()
     * @generated
     */
    void setContents(ContentsType value);

    /**
     * Returns the value of the '<em><b>Coverage</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Complete data for one coverage, referencing each coverage file either remotely or locally in the same message. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage</em>' containment reference.
     * @see #setCoverage(ReferenceGroupType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_Coverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Coverage' namespace='##targetNamespace' affiliation='http://www.opengis.net/ows/1.1#ReferenceGroup'"
     * @generated
     */
    ReferenceGroupType getCoverage();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getCoverage <em>Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage</em>' containment reference.
     * @see #getCoverage()
     * @generated
     */
    void setCoverage(ReferenceGroupType value);

    /**
     * Returns the value of the '<em><b>Coverage Descriptions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Response from a WCS DescribeCoverage operation, containing one or more coverage descriptions. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Descriptions</em>' containment reference.
     * @see #setCoverageDescriptions(CoverageDescriptionsType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_CoverageDescriptions()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoverageDescriptions' namespace='##targetNamespace'"
     * @generated
     */
    CoverageDescriptionsType getCoverageDescriptions();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getCoverageDescriptions <em>Coverage Descriptions</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Descriptions</em>' containment reference.
     * @see #getCoverageDescriptions()
     * @generated
     */
    void setCoverageDescriptions(CoverageDescriptionsType value);

    /**
     * Returns the value of the '<em><b>Coverages</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverages</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coverages</em>' containment reference.
     * @see #setCoverages(CoveragesType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_Coverages()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Coverages' namespace='##targetNamespace'"
     * @generated
     */
    CoveragesType getCoverages();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getCoverages <em>Coverages</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverages</em>' containment reference.
     * @see #getCoverages()
     * @generated
     */
    void setCoverages(CoveragesType value);

    /**
     * Returns the value of the '<em><b>Coverage Summary</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverage Summary</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Coverage Summary</em>' containment reference.
     * @see #setCoverageSummary(CoverageSummaryType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_CoverageSummary()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CoverageSummary' namespace='##targetNamespace'"
     * @generated
     */
    CoverageSummaryType getCoverageSummary();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getCoverageSummary <em>Coverage Summary</em>}' containment reference.
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
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_DescribeCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DescribeCoverage' namespace='##targetNamespace'"
     * @generated
     */
    DescribeCoverageType getDescribeCoverage();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getDescribeCoverage <em>Describe Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Describe Coverage</em>' containment reference.
     * @see #getDescribeCoverage()
     * @generated
     */
    void setDescribeCoverage(DescribeCoverageType value);

    /**
     * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Request to a WCS server to perform the GetCapabilities operation. This operation allows a client to retrieve a Capabilities XML document providing metadata for the specific WCS server. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get Capabilities</em>' containment reference.
     * @see #setGetCapabilities(GetCapabilitiesType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_GetCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
     * @generated
     */
    GetCapabilitiesType getGetCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
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
     * Request to a WCS to perform the GetCoverage operation. This operation allows a client to retrieve a subset of one coverage. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get Coverage</em>' containment reference.
     * @see #setGetCoverage(GetCoverageType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_GetCoverage()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetCoverage' namespace='##targetNamespace'"
     * @generated
     */
    GetCoverageType getGetCoverage();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getGetCoverage <em>Get Coverage</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Coverage</em>' containment reference.
     * @see #getGetCoverage()
     * @generated
     */
    void setGetCoverage(GetCoverageType value);

    /**
     * Returns the value of the '<em><b>Grid Base CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the coordinate reference system (CRS) in which this Grid CRS is specified. A GridCRS can use any type of baseCRS, including GeographicCRS, ProjectedCRS, ImageCRS, or a different GridCRS. 
     * For a GridCRS, this association is limited to a remote definition of the baseCRS (not encoded in-line). 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid Base CRS</em>' attribute.
     * @see #setGridBaseCRS(String)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_GridBaseCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GridBaseCRS' namespace='##targetNamespace'"
     * @generated
     */
    String getGridBaseCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getGridBaseCRS <em>Grid Base CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Base CRS</em>' attribute.
     * @see #getGridBaseCRS()
     * @generated
     */
    void setGridBaseCRS(String value);

    /**
     * Returns the value of the '<em><b>Grid CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Grid CRS</em>' containment reference.
     * @see #setGridCRS(GridCrsType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_GridCRS()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GridCRS' namespace='##targetNamespace'"
     * @generated
     */
    GridCrsType getGridCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getGridCRS <em>Grid CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid CRS</em>' containment reference.
     * @see #getGridCRS()
     * @generated
     */
    void setGridCRS(GridCrsType value);

    /**
     * Returns the value of the '<em><b>Grid CS</b></em>' attribute.
     * The default value is <code>"urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the (Cartesian) grid coordinate system used by this Grid CRS. In this use of a (Cartesian) grid coordinate system, the grid positions shall be in the centers of the image or other grid coverage values (not between the grid values), as specified in ISO 19123. Also, the grid point indices at the origin shall be 0, 0 (not 1,1), as specified in ISO 19123. This GridCS defaults to the most commonly used grid coordinate system, which is referenced by the URN "urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS". 
     * For a GridCRS, this association is limited to a remote definition of the GridCS (not encoded in-line). 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid CS</em>' attribute.
     * @see #setGridCS(String)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_GridCS()
     * @model default="urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS" unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GridCS' namespace='##targetNamespace'"
     * @generated
     */
    String getGridCS();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getGridCS <em>Grid CS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid CS</em>' attribute.
     * @see #getGridCS()
     * @generated
     */
    void setGridCS(String value);

    /**
     * Returns the value of the '<em><b>Grid Offsets</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Two or more grid position offsets from the grid origin in the GridBaseCRS of this GridCRS. Example: For the grid2dIn2dCRS OperationMethod, this Offsets element shall contain four values, the first two values shall specify the grid offset for the first grid axis in the 2D base CRS, and the second pair of values shall specify the grid offset for the second grid axis. In this case, the middle two values are zero for un-rotated and un-skewed grids. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid Offsets</em>' attribute.
     * @see #setGridOffsets(Object)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_GridOffsets()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GridOffsets' namespace='##targetNamespace'"
     * @generated
     */
    Object getGridOffsets();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getGridOffsets <em>Grid Offsets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Offsets</em>' attribute.
     * @see #getGridOffsets()
     * @generated
     */
    void setGridOffsets(Object value);

    /**
     * Returns the value of the '<em><b>Grid Origin</b></em>' attribute.
     * The default value is <code>"0 0"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Coordinates of the grid origin position in the GridBaseCRS of this GridCRS. This origin defaults be the most commonly used origin in a GridCRS used in the output part of a GetCapabilities operation request, namely "0 0". 
     * This element is adapted from gml:pos. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid Origin</em>' attribute.
     * @see #setGridOrigin(Object)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_GridOrigin()
     * @model default="0 0" unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GridOrigin' namespace='##targetNamespace'"
     * @generated
     */
    Object getGridOrigin();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getGridOrigin <em>Grid Origin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Origin</em>' attribute.
     * @see #getGridOrigin()
     * @generated
     */
    void setGridOrigin(Object value);

    /**
     * Returns the value of the '<em><b>Grid Type</b></em>' attribute.
     * The default value is <code>"urn:ogc:def:method:WCS:1.1:2dSimpleGrid"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the OperationMethod used to define this Grid CRS. This association defaults to an association to the most commonly used method, which is referenced by the URN "urn:ogc:def:method:WCS:1.1:2dSimpleGrid". 
     * For a GridCRS, this association is limited to a remote definition of a grid definition Method (not encoded in-line) that encodes a variation on the method implied by the CV_RectifiedGrid class in ISO 19123, without the inheritance from CV_Grid. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid Type</em>' attribute.
     * @see #setGridType(String)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_GridType()
     * @model default="urn:ogc:def:method:WCS:1.1:2dSimpleGrid" unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GridType' namespace='##targetNamespace'"
     * @generated
     */
    String getGridType();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getGridType <em>Grid Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Type</em>' attribute.
     * @see #getGridType()
     * @generated
     */
    void setGridType(String value);

    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Identifier</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(String)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_Identifier()
     * @model unique="false" dataType="net.opengis.wcs11.IdentifierType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    String getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getIdentifier <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(String value);

    /**
     * Returns the value of the '<em><b>Interpolation Methods</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of the interpolation method(s) that can uses when continuous grid coverage resampling is needed. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interpolation Methods</em>' containment reference.
     * @see #setInterpolationMethods(InterpolationMethodsType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_InterpolationMethods()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='InterpolationMethods' namespace='##targetNamespace'"
     * @generated
     */
    InterpolationMethodsType getInterpolationMethods();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getInterpolationMethods <em>Interpolation Methods</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interpolation Methods</em>' containment reference.
     * @see #getInterpolationMethods()
     * @generated
     */
    void setInterpolationMethods(InterpolationMethodsType value);

    /**
     * Returns the value of the '<em><b>Temporal Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of the temporal domain of a coverage, the times for which valid data are available. The times should to be ordered from the oldest to the newest. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Temporal Domain</em>' containment reference.
     * @see #setTemporalDomain(TimeSequenceType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_TemporalDomain()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TemporalDomain' namespace='##targetNamespace'"
     * @generated
     */
    TimeSequenceType getTemporalDomain();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getTemporalDomain <em>Temporal Domain</em>}' containment reference.
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
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of subset of coverage temporal domain. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Temporal Subset</em>' containment reference.
     * @see #setTemporalSubset(TimeSequenceType)
     * @see net.opengis.wcs11.Wcs111Package#getDocumentRoot_TemporalSubset()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TemporalSubset' namespace='##targetNamespace'"
     * @generated
     */
    TimeSequenceType getTemporalSubset();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DocumentRoot#getTemporalSubset <em>Temporal Subset</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Subset</em>' containment reference.
     * @see #getTemporalSubset()
     * @generated
     */
    void setTemporalSubset(TimeSequenceType value);

} // DocumentRoot
