/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MultiSolidType;
import net.opengis.gml311.SolidArrayPropertyType;
import net.opengis.gml311.SolidPropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multi Solid Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MultiSolidTypeImpl#getSolidMember <em>Solid Member</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MultiSolidTypeImpl#getSolidMembers <em>Solid Members</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiSolidTypeImpl extends AbstractGeometricAggregateTypeImpl implements MultiSolidType {
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
     * The cached value of the '{@link #getSolidMembers() <em>Solid Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSolidMembers()
     * @generated
     * @ordered
     */
    protected SolidArrayPropertyType solidMembers;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiSolidTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMultiSolidType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SolidPropertyType> getSolidMember() {
        if (solidMember == null) {
            solidMember = new EObjectContainmentEList<SolidPropertyType>(SolidPropertyType.class, this, Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBER);
        }
        return solidMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SolidArrayPropertyType getSolidMembers() {
        return solidMembers;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSolidMembers(SolidArrayPropertyType newSolidMembers, NotificationChain msgs) {
        SolidArrayPropertyType oldSolidMembers = solidMembers;
        solidMembers = newSolidMembers;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBERS, oldSolidMembers, newSolidMembers);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSolidMembers(SolidArrayPropertyType newSolidMembers) {
        if (newSolidMembers != solidMembers) {
            NotificationChain msgs = null;
            if (solidMembers != null)
                msgs = ((InternalEObject)solidMembers).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBERS, null, msgs);
            if (newSolidMembers != null)
                msgs = ((InternalEObject)newSolidMembers).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBERS, null, msgs);
            msgs = basicSetSolidMembers(newSolidMembers, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBERS, newSolidMembers, newSolidMembers));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBER:
                return ((InternalEList<?>)getSolidMember()).basicRemove(otherEnd, msgs);
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBERS:
                return basicSetSolidMembers(null, msgs);
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
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBER:
                return getSolidMember();
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBERS:
                return getSolidMembers();
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
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBER:
                getSolidMember().clear();
                getSolidMember().addAll((Collection<? extends SolidPropertyType>)newValue);
                return;
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBERS:
                setSolidMembers((SolidArrayPropertyType)newValue);
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
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBER:
                getSolidMember().clear();
                return;
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBERS:
                setSolidMembers((SolidArrayPropertyType)null);
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
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBER:
                return solidMember != null && !solidMember.isEmpty();
            case Gml311Package.MULTI_SOLID_TYPE__SOLID_MEMBERS:
                return solidMembers != null;
        }
        return super.eIsSet(featureID);
    }

} //MultiSolidTypeImpl
