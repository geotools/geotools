/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.GMLObjectTypeListType;
import net.opengis.wfs.GMLObjectTypeType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>GML Object Type List Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.GMLObjectTypeListTypeImpl#getGMLObjectType <em>GML Object Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GMLObjectTypeListTypeImpl extends EObjectImpl implements GMLObjectTypeListType {
	/**
     * The cached value of the '{@link #getGMLObjectType() <em>GML Object Type</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getGMLObjectType()
     * @generated
     * @ordered
     */
	protected EList gMLObjectType;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected GMLObjectTypeListTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.GML_OBJECT_TYPE_LIST_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getGMLObjectType() {
        if (gMLObjectType == null) {
            gMLObjectType = new EObjectContainmentEList(GMLObjectTypeType.class, this, WfsPackage.GML_OBJECT_TYPE_LIST_TYPE__GML_OBJECT_TYPE);
        }
        return gMLObjectType;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_LIST_TYPE__GML_OBJECT_TYPE:
                return ((InternalEList)getGMLObjectType()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_LIST_TYPE__GML_OBJECT_TYPE:
                return getGMLObjectType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_LIST_TYPE__GML_OBJECT_TYPE:
                getGMLObjectType().clear();
                getGMLObjectType().addAll((Collection)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eUnset(int featureID) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_LIST_TYPE__GML_OBJECT_TYPE:
                getGMLObjectType().clear();
                return;
        }
        super.eUnset(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean eIsSet(int featureID) {
        switch (featureID) {
            case WfsPackage.GML_OBJECT_TYPE_LIST_TYPE__GML_OBJECT_TYPE:
                return gMLObjectType != null && !gMLObjectType.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //GMLObjectTypeListTypeImpl