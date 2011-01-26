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
 * A representation of the model object '<em><b>Coverage Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Full description of one coverage available from a WCS server. This description shall include sufficient information to allow all valid GetCoverage operation requests to be prepared by a WCS client. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.CoverageDescriptionType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageDescriptionType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageDescriptionType#getDomain <em>Domain</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageDescriptionType#getRange <em>Range</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageDescriptionType#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.CoverageDescriptionType#getSupportedFormat <em>Supported Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getCoverageDescriptionType()
 * @model extendedMetaData="name='CoverageDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface CoverageDescriptionType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unambiguous identifier of this coverage, unique for this WCS server. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(String)
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDescriptionType_Identifier()
     * @model dataType="net.opengis.wcs11.IdentifierType" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    String getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CoverageDescriptionType#getIdentifier <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(String value);

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of more metadata elements about this coverage. A list of metadata elements for CoverageDescriptions could be specified in a WCS Application Profile. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDescriptionType_Metadata()
     * @model type="net.opengis.ows11.MetadataType" containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList getMetadata();

    /**
     * Returns the value of the '<em><b>Domain</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Domain</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Domain</em>' containment reference.
     * @see #setDomain(CoverageDomainType)
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDescriptionType_Domain()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Domain' namespace='##targetNamespace'"
     * @generated
     */
    CoverageDomainType getDomain();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CoverageDescriptionType#getDomain <em>Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Domain</em>' containment reference.
     * @see #getDomain()
     * @generated
     */
    void setDomain(CoverageDomainType value);

    /**
     * Returns the value of the '<em><b>Range</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range</em>' containment reference.
     * @see #setRange(RangeType)
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDescriptionType_Range()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Range' namespace='##targetNamespace'"
     * @generated
     */
    RangeType getRange();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CoverageDescriptionType#getRange <em>Range</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range</em>' containment reference.
     * @see #getRange()
     * @generated
     */
    void setRange(RangeType value);

    /**
     * Returns the value of the '<em><b>Supported CRS</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of references to all the coordinate reference systems in which GetCoverage operation requests and responses can be encoded for this coverage. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported CRS</em>' attribute list.
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDescriptionType_SupportedCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
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
     * Unordered list of identifiers of all the formats in which GetCoverage operation responses can be encoded for this coverage. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Supported Format</em>' attribute list.
     * @see net.opengis.wcs11.Wcs111Package#getCoverageDescriptionType_SupportedFormat()
     * @model unique="false" dataType="net.opengis.ows11.MimeType" required="true"
     *        extendedMetaData="kind='element' name='SupportedFormat' namespace='##targetNamespace'"
     * @generated
     */
    EList getSupportedFormat();

} // CoverageDescriptionType
