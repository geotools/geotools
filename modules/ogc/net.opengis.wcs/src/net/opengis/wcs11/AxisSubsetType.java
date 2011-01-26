/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Axis Subset Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.AxisSubsetType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs11.AxisSubsetType#getKey <em>Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getAxisSubsetType()
 * @model extendedMetaData="name='AxisSubset_._type' kind='elementOnly'"
 * @generated
 */
public interface AxisSubsetType extends EObject {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of this Axis. This identifier must be unique for this Field. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' attribute.
     * @see #setIdentifier(String)
     * @see net.opengis.wcs11.Wcs111Package#getAxisSubsetType_Identifier()
     * @model dataType="net.opengis.wcs11.IdentifierType" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    String getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.AxisSubsetType#getIdentifier <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' attribute.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(String value);

    /**
     * Returns the value of the '<em><b>Key</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of (at least one) Key, to be used for selecting values in a range vector field. The Keys within this list shall be unique. TBD. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Key</em>' attribute list.
     * @see net.opengis.wcs11.Wcs111Package#getAxisSubsetType_Key()
     * @model unique="false" dataType="net.opengis.wcs11.IdentifierType" required="true"
     *        extendedMetaData="kind='element' name='Key' namespace='##targetNamespace'"
     * @generated
     */
    EList getKey();

} // AxisSubsetType
