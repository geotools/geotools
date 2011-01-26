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
 * A representation of the model object '<em><b>Feature Type List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             A list of feature types available from  this server.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.FeatureTypeListType#getOperations <em>Operations</em>}</li>
 *   <li>{@link net.opengis.wfs.FeatureTypeListType#getFeatureType <em>Feature Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getFeatureTypeListType()
 * @model extendedMetaData="name='FeatureTypeListType' kind='elementOnly'"
 * @generated
 */
public interface FeatureTypeListType extends EObject {
	/**
     * Returns the value of the '<em><b>Operations</b></em>' containment reference.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operations</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Operations</em>' containment reference.
     * @see #setOperations(OperationsType)
     * @see net.opengis.wfs.WfsPackage#getFeatureTypeListType_Operations()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Operations' namespace='##targetNamespace'"
     * @generated
     */
	OperationsType getOperations();

	/**
     * Sets the value of the '{@link net.opengis.wfs.FeatureTypeListType#getOperations <em>Operations</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operations</em>' containment reference.
     * @see #getOperations()
     * @generated
     */
	void setOperations(OperationsType value);

	/**
     * Returns the value of the '<em><b>Feature Type</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.FeatureTypeType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Type</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Type</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getFeatureTypeListType_FeatureType()
     * @model type="net.opengis.wfs.FeatureTypeType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='FeatureType' namespace='##targetNamespace'"
     * @generated
     */
	EList getFeatureType();

} // FeatureTypeListType
