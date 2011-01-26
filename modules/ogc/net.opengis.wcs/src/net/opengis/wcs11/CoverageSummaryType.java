/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import net.opengis.ows11.DescriptionType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Summary Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Brief metadata describing one or more coverages available from this WCS server. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.CoverageSummaryType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageSummaryType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageSummaryType#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageSummaryType#getSupportedFormat <em>Supported Format</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageSummaryType#getCoverageSummary <em>Coverage Summary</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageSummaryType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageSummaryType#getIdentifier1 <em>Identifier1</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getCoverageSummaryType()
 * @model extendedMetaData="name='CoverageSummaryType' kind='elementOnly'"
 * @generated
 */
public interface CoverageSummaryType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of more metadata elements about this coverage. A list of metadata elements for CoverageSummaries could be specified in a WCS Application Profile. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getCoverageSummaryType_Metadata()
     * @model type="net.opengis.ows11.MetadataType" containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList getMetadata();

    /**
     * Returns the value of the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.WGS84BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of minimum bounding rectangles surrounding coverage data, using WGS 84 CRS with decimal degrees and longitude before latitude. These bounding boxes shall also apply to lower-level CoverageSummaries under this CoverageSummary, unless other bounding boxes are specified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>WGS84 Bounding Box</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getCoverageSummaryType_WGS84BoundingBox()
     * @model type="net.opengis.ows11.WGS84BoundingBoxType" containment="true"
     *        extendedMetaData="kind='element' name='WGS84BoundingBox' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList getWGS84BoundingBox();

    /**
     * Returns the value of the '<em><b>Supported CRS</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of references to CRSs in which GetCoverage operation requests and responses may be expressed. These CRSs shall also apply to all lower-level CoverageSummaries under this CoverageSummary, in addition to any other CRSs referenced. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported CRS</em>' attribute list.
     * @see net.opengis.wcs11.Wcs111Package#getCoverageSummaryType_SupportedCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='SupportedCRS' namespace='##targetNamespace'"
     * @generated
     */
    EList getSupportedCRS();

    /**
     * Returns the value of the '<em><b>Supported Format</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of identifiers of formats in which GetCoverage operation responses may be encoded. These formats shall also apply to all lower-level CoverageSummaries under this CoverageSummary, in addition to any other formats identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported Format</em>' attribute list.
     * @see net.opengis.wcs11.Wcs111Package#getCoverageSummaryType_SupportedFormat()
     * @model unique="false" dataType="net.opengis.ows11.MimeType"
     *        extendedMetaData="kind='element' name='SupportedFormat' namespace='##targetNamespace'"
     * @generated
     */
    EList getSupportedFormat();

    /**
     * Returns the value of the '<em><b>Coverage Summary</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs11.CoverageSummaryType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of lower-level CoverageSummaries under this CoverageSummary. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Summary</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getCoverageSummaryType_CoverageSummary()
     * @model type="net.opengis.wcs11.CoverageSummaryType" containment="true"
     *        extendedMetaData="kind='element' name='CoverageSummary' namespace='##targetNamespace'"
     * @generated
     */
    EList getCoverageSummary();

    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional identifier of this coverage. This identifier shall be included only when this coverage can be accessed by the GetCoverage operation, and the CoverageDescription can be accessed by the DescribeCoverage operation. This identifier must be unique for this WCS server. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(String)
     * @see net.opengis.wcs11.Wcs111Package#getCoverageSummaryType_Identifier()
     * @model dataType="net.opengis.wcs11.IdentifierType"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    String getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CoverageSummaryType#getIdentifier <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(String value);

    /**
     * Returns the value of the '<em><b>Identifier1</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of this coverage. This identifier must be unique for this WCS server. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier1</em>' attribute.
     * @see #setIdentifier1(String)
     * @see net.opengis.wcs11.Wcs111Package#getCoverageSummaryType_Identifier1()
     * @model dataType="net.opengis.wcs11.IdentifierType"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    String getIdentifier1();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CoverageSummaryType#getIdentifier1 <em>Identifier1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier1</em>' attribute.
     * @see #getIdentifier1()
     * @generated
     */
    void setIdentifier1(String value);

} // CoverageSummaryType
