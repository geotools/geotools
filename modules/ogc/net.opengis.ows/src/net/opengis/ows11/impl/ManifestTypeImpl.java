/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.util.Collection;

import net.opengis.ows11.ManifestType;
import net.opengis.ows11.Ows11Package;
import net.opengis.ows11.ReferenceGroupType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Manifest Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.ManifestTypeImpl#getReferenceGroup <em>Reference Group</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ManifestTypeImpl extends BasicIdentificationTypeImpl implements ManifestType {
    /**
     * The cached value of the '{@link #getReferenceGroup() <em>Reference Group</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferenceGroup()
     * @generated
     * @ordered
     */
    protected EList referenceGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ManifestTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.MANIFEST_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getReferenceGroup() {
        if (referenceGroup == null) {
            referenceGroup = new EObjectContainmentEList(ReferenceGroupType.class, this, Ows11Package.MANIFEST_TYPE__REFERENCE_GROUP);
        }
        return referenceGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.MANIFEST_TYPE__REFERENCE_GROUP:
                return ((InternalEList)getReferenceGroup()).basicRemove(otherEnd, msgs);
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
            case Ows11Package.MANIFEST_TYPE__REFERENCE_GROUP:
                return getReferenceGroup();
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
            case Ows11Package.MANIFEST_TYPE__REFERENCE_GROUP:
                getReferenceGroup().clear();
                getReferenceGroup().addAll((Collection)newValue);
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
            case Ows11Package.MANIFEST_TYPE__REFERENCE_GROUP:
                getReferenceGroup().clear();
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
            case Ows11Package.MANIFEST_TYPE__REFERENCE_GROUP:
                return referenceGroup != null && !referenceGroup.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ManifestTypeImpl
