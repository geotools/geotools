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
 * A representation of the model object '<em><b>Available Keys Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.AvailableKeysType#getKey <em>Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getAvailableKeysType()
 * @model extendedMetaData="name='AvailableKeys_._type' kind='elementOnly'"
 * @generated
 */
public interface AvailableKeysType extends EObject {
    /**
     * Returns the value of the '<em><b>Key</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Valid key value for this axis. There will normally be more than one key value for an axis, but one is allowed for special circumstances. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Key</em>' attribute list.
     * @see net.opengis.wcs11.Wcs111Package#getAvailableKeysType_Key()
     * @model unique="false" dataType="net.opengis.wcs11.IdentifierType" required="true"
     *        extendedMetaData="kind='element' name='Key' namespace='##targetNamespace'"
     * @generated
     */
    EList getKey();

} // AvailableKeysType
