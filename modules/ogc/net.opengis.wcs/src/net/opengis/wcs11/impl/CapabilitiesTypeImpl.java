/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.ows11.impl.CapabilitiesBaseTypeImpl;

import net.opengis.wcs11.CapabilitiesType;
import net.opengis.wcs11.ContentsType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.CapabilitiesTypeImpl#getContents <em>Contents</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CapabilitiesTypeImpl extends CapabilitiesBaseTypeImpl implements CapabilitiesType {
    /**
     * The cached value of the '{@link #getContents() <em>Contents</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getContents()
     * @generated
     * @ordered
     */
    protected ContentsType contents;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContentsType getContents() {
        return contents;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetContents(ContentsType newContents, NotificationChain msgs) {
        ContentsType oldContents = contents;
        contents = newContents;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.CAPABILITIES_TYPE__CONTENTS, oldContents, newContents);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setContents(ContentsType newContents) {
        if (newContents != contents) {
            NotificationChain msgs = null;
            if (contents != null)
                msgs = ((InternalEObject)contents).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.CAPABILITIES_TYPE__CONTENTS, null, msgs);
            if (newContents != null)
                msgs = ((InternalEObject)newContents).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.CAPABILITIES_TYPE__CONTENTS, null, msgs);
            msgs = basicSetContents(newContents, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.CAPABILITIES_TYPE__CONTENTS, newContents, newContents));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.CAPABILITIES_TYPE__CONTENTS:
                return basicSetContents(null, msgs);
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
            case Wcs111Package.CAPABILITIES_TYPE__CONTENTS:
                return getContents();
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
            case Wcs111Package.CAPABILITIES_TYPE__CONTENTS:
                setContents((ContentsType)newValue);
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
            case Wcs111Package.CAPABILITIES_TYPE__CONTENTS:
                setContents((ContentsType)null);
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
            case Wcs111Package.CAPABILITIES_TYPE__CONTENTS:
                return contents != null;
        }
        return super.eIsSet(featureID);
    }

} //CapabilitiesTypeImpl
