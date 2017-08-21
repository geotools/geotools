/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.DefinitionProxyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.ReferenceType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Definition Proxy Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.DefinitionProxyTypeImpl#getDefinitionRef <em>Definition Ref</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DefinitionProxyTypeImpl extends DefinitionTypeImpl implements DefinitionProxyType {
    /**
     * The cached value of the '{@link #getDefinitionRef() <em>Definition Ref</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefinitionRef()
     * @generated
     * @ordered
     */
    protected ReferenceType definitionRef;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DefinitionProxyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getDefinitionProxyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceType getDefinitionRef() {
        return definitionRef;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefinitionRef(ReferenceType newDefinitionRef, NotificationChain msgs) {
        ReferenceType oldDefinitionRef = definitionRef;
        definitionRef = newDefinitionRef;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.DEFINITION_PROXY_TYPE__DEFINITION_REF, oldDefinitionRef, newDefinitionRef);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefinitionRef(ReferenceType newDefinitionRef) {
        if (newDefinitionRef != definitionRef) {
            NotificationChain msgs = null;
            if (definitionRef != null)
                msgs = ((InternalEObject)definitionRef).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DEFINITION_PROXY_TYPE__DEFINITION_REF, null, msgs);
            if (newDefinitionRef != null)
                msgs = ((InternalEObject)newDefinitionRef).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DEFINITION_PROXY_TYPE__DEFINITION_REF, null, msgs);
            msgs = basicSetDefinitionRef(newDefinitionRef, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DEFINITION_PROXY_TYPE__DEFINITION_REF, newDefinitionRef, newDefinitionRef));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.DEFINITION_PROXY_TYPE__DEFINITION_REF:
                return basicSetDefinitionRef(null, msgs);
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
            case Gml311Package.DEFINITION_PROXY_TYPE__DEFINITION_REF:
                return getDefinitionRef();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Gml311Package.DEFINITION_PROXY_TYPE__DEFINITION_REF:
                setDefinitionRef((ReferenceType)newValue);
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
            case Gml311Package.DEFINITION_PROXY_TYPE__DEFINITION_REF:
                setDefinitionRef((ReferenceType)null);
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
            case Gml311Package.DEFINITION_PROXY_TYPE__DEFINITION_REF:
                return definitionRef != null;
        }
        return super.eIsSet(featureID);
    }

} //DefinitionProxyTypeImpl
