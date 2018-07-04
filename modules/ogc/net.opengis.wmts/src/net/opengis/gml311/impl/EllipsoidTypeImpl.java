/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.EllipsoidType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.IdentifierType;
import net.opengis.gml311.MeasureType;
import net.opengis.gml311.SecondDefiningParameterType;
import net.opengis.gml311.StringOrRefType;

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
 * An implementation of the model object '<em><b>Ellipsoid Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.EllipsoidTypeImpl#getEllipsoidID <em>Ellipsoid ID</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EllipsoidTypeImpl#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EllipsoidTypeImpl#getSemiMajorAxis <em>Semi Major Axis</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.EllipsoidTypeImpl#getSecondDefiningParameter <em>Second Defining Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EllipsoidTypeImpl extends EllipsoidBaseTypeImpl implements EllipsoidType {
    /**
     * The cached value of the '{@link #getEllipsoidID() <em>Ellipsoid ID</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEllipsoidID()
     * @generated
     * @ordered
     */
    protected EList<IdentifierType> ellipsoidID;

    /**
     * The cached value of the '{@link #getRemarks() <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRemarks()
     * @generated
     * @ordered
     */
    protected StringOrRefType remarks;

    /**
     * The cached value of the '{@link #getSemiMajorAxis() <em>Semi Major Axis</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSemiMajorAxis()
     * @generated
     * @ordered
     */
    protected MeasureType semiMajorAxis;

    /**
     * The cached value of the '{@link #getSecondDefiningParameter() <em>Second Defining Parameter</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSecondDefiningParameter()
     * @generated
     * @ordered
     */
    protected SecondDefiningParameterType secondDefiningParameter;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EllipsoidTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getEllipsoidType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IdentifierType> getEllipsoidID() {
        if (ellipsoidID == null) {
            ellipsoidID = new EObjectContainmentEList<IdentifierType>(IdentifierType.class, this, Gml311Package.ELLIPSOID_TYPE__ELLIPSOID_ID);
        }
        return ellipsoidID;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StringOrRefType getRemarks() {
        return remarks;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRemarks(StringOrRefType newRemarks, NotificationChain msgs) {
        StringOrRefType oldRemarks = remarks;
        remarks = newRemarks;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ELLIPSOID_TYPE__REMARKS, oldRemarks, newRemarks);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRemarks(StringOrRefType newRemarks) {
        if (newRemarks != remarks) {
            NotificationChain msgs = null;
            if (remarks != null)
                msgs = ((InternalEObject)remarks).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ELLIPSOID_TYPE__REMARKS, null, msgs);
            if (newRemarks != null)
                msgs = ((InternalEObject)newRemarks).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ELLIPSOID_TYPE__REMARKS, null, msgs);
            msgs = basicSetRemarks(newRemarks, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ELLIPSOID_TYPE__REMARKS, newRemarks, newRemarks));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType getSemiMajorAxis() {
        return semiMajorAxis;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSemiMajorAxis(MeasureType newSemiMajorAxis, NotificationChain msgs) {
        MeasureType oldSemiMajorAxis = semiMajorAxis;
        semiMajorAxis = newSemiMajorAxis;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ELLIPSOID_TYPE__SEMI_MAJOR_AXIS, oldSemiMajorAxis, newSemiMajorAxis);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSemiMajorAxis(MeasureType newSemiMajorAxis) {
        if (newSemiMajorAxis != semiMajorAxis) {
            NotificationChain msgs = null;
            if (semiMajorAxis != null)
                msgs = ((InternalEObject)semiMajorAxis).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ELLIPSOID_TYPE__SEMI_MAJOR_AXIS, null, msgs);
            if (newSemiMajorAxis != null)
                msgs = ((InternalEObject)newSemiMajorAxis).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ELLIPSOID_TYPE__SEMI_MAJOR_AXIS, null, msgs);
            msgs = basicSetSemiMajorAxis(newSemiMajorAxis, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ELLIPSOID_TYPE__SEMI_MAJOR_AXIS, newSemiMajorAxis, newSemiMajorAxis));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SecondDefiningParameterType getSecondDefiningParameter() {
        return secondDefiningParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSecondDefiningParameter(SecondDefiningParameterType newSecondDefiningParameter, NotificationChain msgs) {
        SecondDefiningParameterType oldSecondDefiningParameter = secondDefiningParameter;
        secondDefiningParameter = newSecondDefiningParameter;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ELLIPSOID_TYPE__SECOND_DEFINING_PARAMETER, oldSecondDefiningParameter, newSecondDefiningParameter);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSecondDefiningParameter(SecondDefiningParameterType newSecondDefiningParameter) {
        if (newSecondDefiningParameter != secondDefiningParameter) {
            NotificationChain msgs = null;
            if (secondDefiningParameter != null)
                msgs = ((InternalEObject)secondDefiningParameter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ELLIPSOID_TYPE__SECOND_DEFINING_PARAMETER, null, msgs);
            if (newSecondDefiningParameter != null)
                msgs = ((InternalEObject)newSecondDefiningParameter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ELLIPSOID_TYPE__SECOND_DEFINING_PARAMETER, null, msgs);
            msgs = basicSetSecondDefiningParameter(newSecondDefiningParameter, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ELLIPSOID_TYPE__SECOND_DEFINING_PARAMETER, newSecondDefiningParameter, newSecondDefiningParameter));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ELLIPSOID_TYPE__ELLIPSOID_ID:
                return ((InternalEList<?>)getEllipsoidID()).basicRemove(otherEnd, msgs);
            case Gml311Package.ELLIPSOID_TYPE__REMARKS:
                return basicSetRemarks(null, msgs);
            case Gml311Package.ELLIPSOID_TYPE__SEMI_MAJOR_AXIS:
                return basicSetSemiMajorAxis(null, msgs);
            case Gml311Package.ELLIPSOID_TYPE__SECOND_DEFINING_PARAMETER:
                return basicSetSecondDefiningParameter(null, msgs);
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
            case Gml311Package.ELLIPSOID_TYPE__ELLIPSOID_ID:
                return getEllipsoidID();
            case Gml311Package.ELLIPSOID_TYPE__REMARKS:
                return getRemarks();
            case Gml311Package.ELLIPSOID_TYPE__SEMI_MAJOR_AXIS:
                return getSemiMajorAxis();
            case Gml311Package.ELLIPSOID_TYPE__SECOND_DEFINING_PARAMETER:
                return getSecondDefiningParameter();
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
            case Gml311Package.ELLIPSOID_TYPE__ELLIPSOID_ID:
                getEllipsoidID().clear();
                getEllipsoidID().addAll((Collection<? extends IdentifierType>)newValue);
                return;
            case Gml311Package.ELLIPSOID_TYPE__REMARKS:
                setRemarks((StringOrRefType)newValue);
                return;
            case Gml311Package.ELLIPSOID_TYPE__SEMI_MAJOR_AXIS:
                setSemiMajorAxis((MeasureType)newValue);
                return;
            case Gml311Package.ELLIPSOID_TYPE__SECOND_DEFINING_PARAMETER:
                setSecondDefiningParameter((SecondDefiningParameterType)newValue);
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
            case Gml311Package.ELLIPSOID_TYPE__ELLIPSOID_ID:
                getEllipsoidID().clear();
                return;
            case Gml311Package.ELLIPSOID_TYPE__REMARKS:
                setRemarks((StringOrRefType)null);
                return;
            case Gml311Package.ELLIPSOID_TYPE__SEMI_MAJOR_AXIS:
                setSemiMajorAxis((MeasureType)null);
                return;
            case Gml311Package.ELLIPSOID_TYPE__SECOND_DEFINING_PARAMETER:
                setSecondDefiningParameter((SecondDefiningParameterType)null);
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
            case Gml311Package.ELLIPSOID_TYPE__ELLIPSOID_ID:
                return ellipsoidID != null && !ellipsoidID.isEmpty();
            case Gml311Package.ELLIPSOID_TYPE__REMARKS:
                return remarks != null;
            case Gml311Package.ELLIPSOID_TYPE__SEMI_MAJOR_AXIS:
                return semiMajorAxis != null;
            case Gml311Package.ELLIPSOID_TYPE__SECOND_DEFINING_PARAMETER:
                return secondDefiningParameter != null;
        }
        return super.eIsSet(featureID);
    }

} //EllipsoidTypeImpl
