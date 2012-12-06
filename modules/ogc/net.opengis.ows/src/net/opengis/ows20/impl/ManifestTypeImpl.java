/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.ManifestType;
import net.opengis.ows20.ReferenceGroupType;
import net.opengis.ows20.Ows20Package;

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
 *   <li>{@link net.opengis.ows20.impl.ManifestTypeImpl#getReferenceGroup <em>Reference Group</em>}</li>
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
    protected EList<ReferenceGroupType> referenceGroup;

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
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.MANIFEST_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ReferenceGroupType> getReferenceGroup() {
        if (referenceGroup == null) {
            referenceGroup = new EObjectContainmentEList<ReferenceGroupType>(ReferenceGroupType.class, this, Ows20Package.MANIFEST_TYPE__REFERENCE_GROUP);
        }
        return referenceGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.MANIFEST_TYPE__REFERENCE_GROUP:
                return ((InternalEList<?>)getReferenceGroup()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows20Package.MANIFEST_TYPE__REFERENCE_GROUP:
                return getReferenceGroup();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Ows20Package.MANIFEST_TYPE__REFERENCE_GROUP:
                getReferenceGroup().clear();
                getReferenceGroup().addAll((Collection<? extends ReferenceGroupType>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Ows20Package.MANIFEST_TYPE__REFERENCE_GROUP:
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
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Ows20Package.MANIFEST_TYPE__REFERENCE_GROUP:
                return referenceGroup != null && !referenceGroup.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ManifestTypeImpl
