/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.IsSphereType;
import net.opengis.gml311.MeasureType;
import net.opengis.gml311.SecondDefiningParameterType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Second Defining Parameter Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.SecondDefiningParameterTypeImpl#getInverseFlattening <em>Inverse Flattening</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.SecondDefiningParameterTypeImpl#getSemiMinorAxis <em>Semi Minor Axis</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.SecondDefiningParameterTypeImpl#getIsSphere <em>Is Sphere</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SecondDefiningParameterTypeImpl extends MinimalEObjectImpl.Container implements SecondDefiningParameterType {
    /**
     * The cached value of the '{@link #getInverseFlattening() <em>Inverse Flattening</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInverseFlattening()
     * @generated
     * @ordered
     */
    protected MeasureType inverseFlattening;

    /**
     * The cached value of the '{@link #getSemiMinorAxis() <em>Semi Minor Axis</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSemiMinorAxis()
     * @generated
     * @ordered
     */
    protected MeasureType semiMinorAxis;

    /**
     * The default value of the '{@link #getIsSphere() <em>Is Sphere</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIsSphere()
     * @generated
     * @ordered
     */
    protected static final IsSphereType IS_SPHERE_EDEFAULT = IsSphereType.SPHERE;

    /**
     * The cached value of the '{@link #getIsSphere() <em>Is Sphere</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIsSphere()
     * @generated
     * @ordered
     */
    protected IsSphereType isSphere = IS_SPHERE_EDEFAULT;

    /**
     * This is true if the Is Sphere attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean isSphereESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SecondDefiningParameterTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getSecondDefiningParameterType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType getInverseFlattening() {
        return inverseFlattening;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInverseFlattening(MeasureType newInverseFlattening, NotificationChain msgs) {
        MeasureType oldInverseFlattening = inverseFlattening;
        inverseFlattening = newInverseFlattening;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__INVERSE_FLATTENING, oldInverseFlattening, newInverseFlattening);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInverseFlattening(MeasureType newInverseFlattening) {
        if (newInverseFlattening != inverseFlattening) {
            NotificationChain msgs = null;
            if (inverseFlattening != null)
                msgs = ((InternalEObject)inverseFlattening).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__INVERSE_FLATTENING, null, msgs);
            if (newInverseFlattening != null)
                msgs = ((InternalEObject)newInverseFlattening).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__INVERSE_FLATTENING, null, msgs);
            msgs = basicSetInverseFlattening(newInverseFlattening, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__INVERSE_FLATTENING, newInverseFlattening, newInverseFlattening));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType getSemiMinorAxis() {
        return semiMinorAxis;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSemiMinorAxis(MeasureType newSemiMinorAxis, NotificationChain msgs) {
        MeasureType oldSemiMinorAxis = semiMinorAxis;
        semiMinorAxis = newSemiMinorAxis;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__SEMI_MINOR_AXIS, oldSemiMinorAxis, newSemiMinorAxis);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSemiMinorAxis(MeasureType newSemiMinorAxis) {
        if (newSemiMinorAxis != semiMinorAxis) {
            NotificationChain msgs = null;
            if (semiMinorAxis != null)
                msgs = ((InternalEObject)semiMinorAxis).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__SEMI_MINOR_AXIS, null, msgs);
            if (newSemiMinorAxis != null)
                msgs = ((InternalEObject)newSemiMinorAxis).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__SEMI_MINOR_AXIS, null, msgs);
            msgs = basicSetSemiMinorAxis(newSemiMinorAxis, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__SEMI_MINOR_AXIS, newSemiMinorAxis, newSemiMinorAxis));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IsSphereType getIsSphere() {
        return isSphere;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsSphere(IsSphereType newIsSphere) {
        IsSphereType oldIsSphere = isSphere;
        isSphere = newIsSphere == null ? IS_SPHERE_EDEFAULT : newIsSphere;
        boolean oldIsSphereESet = isSphereESet;
        isSphereESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__IS_SPHERE, oldIsSphere, isSphere, !oldIsSphereESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetIsSphere() {
        IsSphereType oldIsSphere = isSphere;
        boolean oldIsSphereESet = isSphereESet;
        isSphere = IS_SPHERE_EDEFAULT;
        isSphereESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__IS_SPHERE, oldIsSphere, IS_SPHERE_EDEFAULT, oldIsSphereESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetIsSphere() {
        return isSphereESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__INVERSE_FLATTENING:
                return basicSetInverseFlattening(null, msgs);
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__SEMI_MINOR_AXIS:
                return basicSetSemiMinorAxis(null, msgs);
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
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__INVERSE_FLATTENING:
                return getInverseFlattening();
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__SEMI_MINOR_AXIS:
                return getSemiMinorAxis();
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__IS_SPHERE:
                return getIsSphere();
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
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__INVERSE_FLATTENING:
                setInverseFlattening((MeasureType)newValue);
                return;
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__SEMI_MINOR_AXIS:
                setSemiMinorAxis((MeasureType)newValue);
                return;
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__IS_SPHERE:
                setIsSphere((IsSphereType)newValue);
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
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__INVERSE_FLATTENING:
                setInverseFlattening((MeasureType)null);
                return;
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__SEMI_MINOR_AXIS:
                setSemiMinorAxis((MeasureType)null);
                return;
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__IS_SPHERE:
                unsetIsSphere();
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
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__INVERSE_FLATTENING:
                return inverseFlattening != null;
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__SEMI_MINOR_AXIS:
                return semiMinorAxis != null;
            case Gml311Package.SECOND_DEFINING_PARAMETER_TYPE__IS_SPHERE:
                return isSetIsSphere();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (isSphere: ");
        if (isSphereESet) result.append(isSphere); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //SecondDefiningParameterTypeImpl
