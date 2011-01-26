/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.ComplexDataCombinationType;
import net.opengis.wps10.ComplexDataDescriptionType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Complex Data Combination Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ComplexDataCombinationTypeImpl#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComplexDataCombinationTypeImpl extends EObjectImpl implements ComplexDataCombinationType {
    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected ComplexDataDescriptionType format;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ComplexDataCombinationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.COMPLEX_DATA_COMBINATION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComplexDataDescriptionType getFormat() {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFormat(ComplexDataDescriptionType newFormat, NotificationChain msgs) {
        ComplexDataDescriptionType oldFormat = format;
        format = newFormat;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.COMPLEX_DATA_COMBINATION_TYPE__FORMAT, oldFormat, newFormat);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat(ComplexDataDescriptionType newFormat) {
        if (newFormat != format) {
            NotificationChain msgs = null;
            if (format != null)
                msgs = ((InternalEObject)format).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.COMPLEX_DATA_COMBINATION_TYPE__FORMAT, null, msgs);
            if (newFormat != null)
                msgs = ((InternalEObject)newFormat).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.COMPLEX_DATA_COMBINATION_TYPE__FORMAT, null, msgs);
            msgs = basicSetFormat(newFormat, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.COMPLEX_DATA_COMBINATION_TYPE__FORMAT, newFormat, newFormat));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.COMPLEX_DATA_COMBINATION_TYPE__FORMAT:
                return basicSetFormat(null, msgs);
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
            case Wps10Package.COMPLEX_DATA_COMBINATION_TYPE__FORMAT:
                return getFormat();
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
            case Wps10Package.COMPLEX_DATA_COMBINATION_TYPE__FORMAT:
                setFormat((ComplexDataDescriptionType)newValue);
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
            case Wps10Package.COMPLEX_DATA_COMBINATION_TYPE__FORMAT:
                setFormat((ComplexDataDescriptionType)null);
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
            case Wps10Package.COMPLEX_DATA_COMBINATION_TYPE__FORMAT:
                return format != null;
        }
        return super.eIsSet(featureID);
    }

} //ComplexDataCombinationTypeImpl
