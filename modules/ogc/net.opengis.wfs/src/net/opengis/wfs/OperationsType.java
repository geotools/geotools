/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operations Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.OperationsType#getOperation <em>Operation</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getOperationsType()
 * @model extendedMetaData="name='OperationsType' kind='elementOnly'"
 * @generated
 */
public interface OperationsType extends EObject {
	/**
     * Returns the value of the '<em><b>Operation</b></em>' attribute list.
     * The list contents are of type {@link net.opengis.wfs.OperationType}.
     * The literals are from the enumeration {@link net.opengis.wfs.OperationType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operation</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Operation</em>' attribute list.
     * @see net.opengis.wfs.OperationType
     * @see net.opengis.wfs.WfsPackage#getOperationsType_Operation()
     * @model default="Insert" unique="false" dataType="net.opengis.wfs.OperationType" required="true"
     *        extendedMetaData="kind='element' name='Operation' namespace='##targetNamespace'"
     * @generated
     */
	EList getOperation();

} // OperationsType
