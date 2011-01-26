/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Features Not Locked Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.FeaturesNotLockedType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.FeaturesNotLockedType#getFeatureId <em>Feature Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getFeaturesNotLockedType()
 * @model extendedMetaData="name='FeaturesNotLockedType' kind='elementOnly'"
 * @generated
 */
public interface FeaturesNotLockedType extends EObject {
	/**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see net.opengis.wfs.WfsPackage#getFeaturesNotLockedType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Feature Id</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Object}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Id</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Id</em>' attribute list.
	 * @see net.opengis.wfs.WFSPackage#getFeaturesNotLockedType_FeatureId()
	 * @model type="org.opengis.filter.identity.FeatureId"
	 */
	EList getFeatureId();

} // FeaturesNotLockedType
