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
 * A representation of the model object '<em><b>Created Or Modified Feature Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.CreatedOrModifiedFeatureType#getResourceId <em>Resource Id</em>}</li>
 *   <li>{@link net.opengis.wfs20.CreatedOrModifiedFeatureType#getHandle <em>Handle</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getCreatedOrModifiedFeatureType()
 * @model extendedMetaData="name='CreatedOrModifiedFeatureType' kind='elementOnly'"
 * @generated
 */
public interface CreatedOrModifiedFeatureType extends EObject {
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
     * @see net.opengis.wfs20.Wfs20Package#getCreatedOrModifiedFeatureType_ResourceId()
     * @model 
     */
    EList<FeatureId> getResourceId();

    /**
     * Returns the value of the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Handle</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.wfs20.Wfs20Package#getCreatedOrModifiedFeatureType_Handle()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
    String getHandle();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.CreatedOrModifiedFeatureType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
    void setHandle(String value);

} // CreatedOrModifiedFeatureType
