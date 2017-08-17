/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractTimeTopologyPrimitiveType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.ReferenceType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Time Topology Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractTimeTopologyPrimitiveTypeImpl#getComplex <em>Complex</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractTimeTopologyPrimitiveTypeImpl extends AbstractTimePrimitiveTypeImpl implements AbstractTimeTopologyPrimitiveType {
    /**
     * The cached value of the '{@link #getComplex() <em>Complex</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComplex()
     * @generated
     * @ordered
     */
    protected ReferenceType complex;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractTimeTopologyPrimitiveTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractTimeTopologyPrimitiveType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceType getComplex() {
        return complex;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetComplex(ReferenceType newComplex, NotificationChain msgs) {
        ReferenceType oldComplex = complex;
        complex = newComplex;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE__COMPLEX, oldComplex, newComplex);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setComplex(ReferenceType newComplex) {
        if (newComplex != complex) {
            NotificationChain msgs = null;
            if (complex != null)
                msgs = ((InternalEObject)complex).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE__COMPLEX, null, msgs);
            if (newComplex != null)
                msgs = ((InternalEObject)newComplex).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE__COMPLEX, null, msgs);
            msgs = basicSetComplex(newComplex, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE__COMPLEX, newComplex, newComplex));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE__COMPLEX:
                return basicSetComplex(null, msgs);
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
            case Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE__COMPLEX:
                return getComplex();
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
            case Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE__COMPLEX:
                setComplex((ReferenceType)newValue);
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
            case Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE__COMPLEX:
                setComplex((ReferenceType)null);
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
            case Gml311Package.ABSTRACT_TIME_TOPOLOGY_PRIMITIVE_TYPE__COMPLEX:
                return complex != null;
        }
        return super.eIsSet(featureID);
    }

} //AbstractTimeTopologyPrimitiveTypeImpl
