/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CurveSegmentArrayPropertyType;
import net.opengis.gml311.CurveType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Curve Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.CurveTypeImpl#getSegments <em>Segments</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CurveTypeImpl extends AbstractCurveTypeImpl implements CurveType {
    /**
     * The cached value of the '{@link #getSegments() <em>Segments</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSegments()
     * @generated
     * @ordered
     */
    protected CurveSegmentArrayPropertyType segments;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CurveTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getCurveType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurveSegmentArrayPropertyType getSegments() {
        return segments;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSegments(CurveSegmentArrayPropertyType newSegments, NotificationChain msgs) {
        CurveSegmentArrayPropertyType oldSegments = segments;
        segments = newSegments;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.CURVE_TYPE__SEGMENTS, oldSegments, newSegments);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSegments(CurveSegmentArrayPropertyType newSegments) {
        if (newSegments != segments) {
            NotificationChain msgs = null;
            if (segments != null)
                msgs = ((InternalEObject)segments).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CURVE_TYPE__SEGMENTS, null, msgs);
            if (newSegments != null)
                msgs = ((InternalEObject)newSegments).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CURVE_TYPE__SEGMENTS, null, msgs);
            msgs = basicSetSegments(newSegments, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CURVE_TYPE__SEGMENTS, newSegments, newSegments));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.CURVE_TYPE__SEGMENTS:
                return basicSetSegments(null, msgs);
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
            case Gml311Package.CURVE_TYPE__SEGMENTS:
                return getSegments();
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
            case Gml311Package.CURVE_TYPE__SEGMENTS:
                setSegments((CurveSegmentArrayPropertyType)newValue);
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
            case Gml311Package.CURVE_TYPE__SEGMENTS:
                setSegments((CurveSegmentArrayPropertyType)null);
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
            case Gml311Package.CURVE_TYPE__SEGMENTS:
                return segments != null;
        }
        return super.eIsSet(featureID);
    }

} //CurveTypeImpl
