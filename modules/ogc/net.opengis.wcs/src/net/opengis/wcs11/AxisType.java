/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import net.opengis.ows11.DescriptionType;
import net.opengis.ows11.DomainMetadataType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Axis Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of one axis in a field for which there are a vector of values. 
 * This type is largely a subset of the ows:DomainType as needed for a range field axis. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.AxisType#getAvailableKeys <em>Available Keys</em>}</li>
 *   <li>{@link net.opengis.wcs11.AxisType#getMeaning <em>Meaning</em>}</li>
 *   <li>{@link net.opengis.wcs11.AxisType#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wcs11.AxisType#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.wcs11.AxisType#getReferenceSystem <em>Reference System</em>}</li>
 *   <li>{@link net.opengis.wcs11.AxisType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs11.AxisType#getIdentifier <em>Identifier</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getAxisType()
 * @model extendedMetaData="name='AxisType' kind='elementOnly'"
 * @generated
 */
public interface AxisType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Available Keys</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of all the available (valid) key values for this axis. For numeric keys, signed values should be ordered from negative infinity to positive infinity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Available Keys</em>' containment reference.
     * @see #setAvailableKeys(AvailableKeysType)
     * @see net.opengis.wcs11.Wcs111Package#getAxisType_AvailableKeys()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='AvailableKeys' namespace='##targetNamespace'"
     * @generated
     */
    AvailableKeysType getAvailableKeys();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.AxisType#getAvailableKeys <em>Available Keys</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Available Keys</em>' containment reference.
     * @see #getAvailableKeys()
     * @generated
     */
    void setAvailableKeys(AvailableKeysType value);

    /**
     * Returns the value of the '<em><b>Meaning</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Meaning metadata, which should be referenced for this axis. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meaning</em>' containment reference.
     * @see #setMeaning(DomainMetadataType)
     * @see net.opengis.wcs11.Wcs111Package#getAxisType_Meaning()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Meaning' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    DomainMetadataType getMeaning();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.AxisType#getMeaning <em>Meaning</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Meaning</em>' containment reference.
     * @see #getMeaning()
     * @generated
     */
    void setMeaning(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Data Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Data type metadata, which may be referenced for this axis. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Type</em>' containment reference.
     * @see #setDataType(DomainMetadataType)
     * @see net.opengis.wcs11.Wcs111Package#getAxisType_DataType()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DataType' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    DomainMetadataType getDataType();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.AxisType#getDataType <em>Data Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Type</em>' containment reference.
     * @see #getDataType()
     * @generated
     */
    void setDataType(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of unit of measure of this set of values. Should be included then this set of values has units (and not a more complete reference system). 
     * <!-- end-model-doc -->
     * @return the value of the '<em>UOM</em>' containment reference.
     * @see #setUOM(DomainMetadataType)
     * @see net.opengis.wcs11.Wcs111Package#getAxisType_UOM()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='UOM' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    DomainMetadataType getUOM();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.AxisType#getUOM <em>UOM</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>UOM</em>' containment reference.
     * @see #getUOM()
     * @generated
     */
    void setUOM(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Reference System</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of reference system used by this set of values. Should be included then this set of values has a reference system (not just units). 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference System</em>' containment reference.
     * @see #setReferenceSystem(DomainMetadataType)
     * @see net.opengis.wcs11.Wcs111Package#getAxisType_ReferenceSystem()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ReferenceSystem' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    DomainMetadataType getReferenceSystem();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.AxisType#getReferenceSystem <em>Reference System</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference System</em>' containment reference.
     * @see #getReferenceSystem()
     * @generated
     */
    void setReferenceSystem(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of other metadata elements about this axis. A list of required and optional other metadata elements for this quantity can be specified in a WCS Application Profile. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getAxisType_Metadata()
     * @model type="net.opengis.ows11.MetadataType" containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList getMetadata();

    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name or identifier of this axis. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(String)
     * @see net.opengis.wcs11.Wcs111Package#getAxisType_Identifier()
     * @model dataType="net.opengis.wcs11.IdentifierType" required="true"
     *        extendedMetaData="kind='attribute' name='identifier'"
     * @generated
     */
    String getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.AxisType#getIdentifier <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(String value);

} // AxisType
