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
 * A representation of the model object '<em><b>Insert Results Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             Reports the list of identifiers of all features created
 *             by a transaction request.  New features are created using
 *             the Insert action and the list of idetifiers must be
 *             presented in the same order as the Insert actions were
 *             encountered in the transaction request.  Features may
 *             optionally be correlated with identifiers using the
 *             handle attribute (if it was specified on the Insert
 *             element).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.InsertResultsType#getFeature <em>Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getInsertResultsType()
 * @model extendedMetaData="name='InsertResultsType' kind='elementOnly'"
 * @generated
 */
public interface InsertResultsType extends EObject {
	/**
     * Returns the value of the '<em><b>Feature</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.InsertedFeatureType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Feature</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getInsertResultsType_Feature()
     * @model type="net.opengis.wfs.InsertedFeatureType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Feature' namespace='##targetNamespace'"
     * @generated
     */
	EList getFeature();

} // InsertResultsType
