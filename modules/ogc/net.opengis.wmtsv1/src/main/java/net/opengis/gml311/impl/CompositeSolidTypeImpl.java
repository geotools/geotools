/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.CompositeSolidType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.SolidPropertyType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composite Solid Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.CompositeSolidTypeImpl#getSolidMember <em>Solid Member</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompositeSolidTypeImpl extends AbstractSolidTypeImpl implements CompositeSolidType {
    /**
     * The cached value of the '{@link #getSolidMember() <em>Solid Member</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSolidMember()
     * @generated
     * @ordered
     */
    protected EList<SolidPropertyType> solidMember;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CompositeSolidTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getCompositeSolidType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SolidPropertyType> getSolidMember() {
        if (solidMember == null) {
            solidMember = new EObjectContainmentEList<SolidPropertyType>(SolidPropertyType.class, this, Gml311Package.COMPOSITE_SOLID_TYPE__SOLID_MEMBER);
        }
        return solidMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.COMPOSITE_SOLID_TYPE__SOLID_MEMBER:
                return ((InternalEList<?>)getSolidMember()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.COMPOSITE_SOLID_TYPE__SOLID_MEMBER:
                return getSolidMember();
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
            case Gml311Package.COMPOSITE_SOLID_TYPE__SOLID_MEMBER:
                getSolidMember().clear();
                getSolidMember().addAll((Collection<? extends SolidPropertyType>)newValue);
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
            case Gml311Package.COMPOSITE_SOLID_TYPE__SOLID_MEMBER:
                getSolidMember().clear();
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
            case Gml311Package.COMPOSITE_SOLID_TYPE__SOLID_MEMBER:
                return solidMember != null && !solidMember.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //CompositeSolidTypeImpl
