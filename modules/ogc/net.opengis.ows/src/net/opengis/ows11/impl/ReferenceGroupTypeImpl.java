/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import net.opengis.ows11.Ows11Package;
import net.opengis.ows11.ReferenceGroupType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference Group Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.ReferenceGroupTypeImpl#getAbstractReferenceBaseGroup <em>Abstract Reference Base Group</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.ReferenceGroupTypeImpl#getAbstractReferenceBase <em>Abstract Reference Base</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferenceGroupTypeImpl extends BasicIdentificationTypeImpl implements ReferenceGroupType {
    /**
     * The cached value of the '{@link #getAbstractReferenceBaseGroup() <em>Abstract Reference Base Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractReferenceBaseGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap abstractReferenceBaseGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ReferenceGroupTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.REFERENCE_GROUP_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAbstractReferenceBaseGroup() {
        if (abstractReferenceBaseGroup == null) {
            abstractReferenceBaseGroup = new BasicFeatureMap(this, Ows11Package.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP);
        }
        return abstractReferenceBaseGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getAbstractReferenceBase() {
        return getAbstractReferenceBaseGroup().list(Ows11Package.Literals.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP:
                return ((InternalEList)getAbstractReferenceBaseGroup()).basicRemove(otherEnd, msgs);
            case Ows11Package.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE:
                return ((InternalEList)getAbstractReferenceBase()).basicRemove(otherEnd, msgs);
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
            case Ows11Package.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP:
                if (coreType) return getAbstractReferenceBaseGroup();
                return ((FeatureMap.Internal)getAbstractReferenceBaseGroup()).getWrapper();
            case Ows11Package.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE:
                return getAbstractReferenceBase();
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
            case Ows11Package.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP:
                ((FeatureMap.Internal)getAbstractReferenceBaseGroup()).set(newValue);
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
            case Ows11Package.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP:
                getAbstractReferenceBaseGroup().clear();
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
            case Ows11Package.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE_GROUP:
                return abstractReferenceBaseGroup != null && !abstractReferenceBaseGroup.isEmpty();
            case Ows11Package.REFERENCE_GROUP_TYPE__ABSTRACT_REFERENCE_BASE:
                return !getAbstractReferenceBase().isEmpty();
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
        result.append(" (abstractReferenceBaseGroup: ");
        result.append(abstractReferenceBaseGroup);
        result.append(')');
        return result.toString();
    }

} //ReferenceGroupTypeImpl
