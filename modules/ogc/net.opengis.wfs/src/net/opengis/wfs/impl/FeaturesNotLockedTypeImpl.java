/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.FeaturesNotLockedType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.opengis.filter.identity.FeatureId;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Features Not Locked Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.FeaturesNotLockedTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeaturesNotLockedTypeImpl#getFeatureId <em>Feature Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeaturesNotLockedTypeImpl extends EObjectImpl implements FeaturesNotLockedType {
	/**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getGroup()
     * @generated
     * @ordered
     */
	protected FeatureMap group;

	/**
     * The cached value of the '{@link #getFeatureId() <em>Feature Id</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFeatureId()
     * @generated
     * @ordered
     */
	protected EList featureId;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected FeaturesNotLockedTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.FEATURES_NOT_LOCKED_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, WfsPackage.FEATURES_NOT_LOCKED_TYPE__GROUP);
        }
        return group;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getFeatureId() {
        if (featureId == null) {
            featureId = new EDataTypeUniqueEList(FeatureId.class, this, WfsPackage.FEATURES_NOT_LOCKED_TYPE__FEATURE_ID);
        }
        return featureId;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
                return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
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
            case WfsPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case WfsPackage.FEATURES_NOT_LOCKED_TYPE__FEATURE_ID:
                return getFeatureId();
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
            case WfsPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case WfsPackage.FEATURES_NOT_LOCKED_TYPE__FEATURE_ID:
                getFeatureId().clear();
                getFeatureId().addAll((Collection)newValue);
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
            case WfsPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
                getGroup().clear();
                return;
            case WfsPackage.FEATURES_NOT_LOCKED_TYPE__FEATURE_ID:
                getFeatureId().clear();
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
            case WfsPackage.FEATURES_NOT_LOCKED_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case WfsPackage.FEATURES_NOT_LOCKED_TYPE__FEATURE_ID:
                return featureId != null && !featureId.isEmpty();
        }
        return super.eIsSet(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (group: ");
        result.append(group);
        result.append(", featureId: ");
        result.append(featureId);
        result.append(')');
        return result.toString();
    }

} //FeaturesNotLockedTypeImpl