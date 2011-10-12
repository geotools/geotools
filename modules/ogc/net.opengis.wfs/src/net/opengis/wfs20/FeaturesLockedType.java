/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import net.opengis.fes20.ResourceIdType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;
import org.opengis.filter.identity.FeatureId;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Features Locked Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.FeaturesLockedType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeaturesLockedType#getResourceId <em>Resource Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getFeaturesLockedType()
 * @model extendedMetaData="name='FeaturesLockedType' kind='elementOnly'"
 * @generated
 */
public interface FeaturesLockedType extends EObject {
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
     * @see net.opengis.wfs20.Wfs20Package#getFeaturesLockedType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Resource Id</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.ResourceIdType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resource Id</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resource Id</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getFeaturesLockedType_ResourceId()
     * @model 
     */
    EList<FeatureId> getResourceId();

} // FeaturesLockedType
