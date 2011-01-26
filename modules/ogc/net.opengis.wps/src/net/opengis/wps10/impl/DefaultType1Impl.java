/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import javax.measure.unit.Unit;
import net.opengis.ows11.DomainMetadataType;

import net.opengis.wps10.DefaultType1;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Default Type1</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.DefaultType1Impl#getUOM <em>UOM</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DefaultType1Impl extends EObjectImpl implements DefaultType1 {
    /**
     * The cached value of the '{@link #getUOM() <em>UOM</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUOM()
     * @generated
     * @ordered
     */
    protected Unit uOM;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DefaultType1Impl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.DEFAULT_TYPE1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Unit getUOM() {
        if (uOM != null && ((EObject)uOM).eIsProxy()) {
            InternalEObject oldUOM = (InternalEObject)uOM;
            uOM = (Unit)eResolveProxy(oldUOM);
            if (uOM != oldUOM) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wps10Package.DEFAULT_TYPE1__UOM, oldUOM, uOM));
            }
        }
        return uOM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Unit basicGetUOM() {
        return uOM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUOM(Unit newUOM) {
        Unit oldUOM = uOM;
        uOM = newUOM;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.DEFAULT_TYPE1__UOM, oldUOM, uOM));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.DEFAULT_TYPE1__UOM:
                if (resolve) return getUOM();
                return basicGetUOM();
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
            case Wps10Package.DEFAULT_TYPE1__UOM:
                if (newValue instanceof String) {
                    throw new ClassCastException("Unable to cast String \"" + newValue + "\" to a Unit");
                }
                setUOM((Unit) newValue);
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
            case Wps10Package.DEFAULT_TYPE1__UOM:
                setUOM((Unit)null);
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
            case Wps10Package.DEFAULT_TYPE1__UOM:
                return uOM != null;
        }
        return super.eIsSet(featureID);
    }

} //DefaultType1Impl
