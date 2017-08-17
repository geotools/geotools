/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.PointPropertyType;
import net.opengis.gml311.RectifiedGridType;
import net.opengis.gml311.VectorType;

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
 * An implementation of the model object '<em><b>Rectified Grid Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.RectifiedGridTypeImpl#getOrigin <em>Origin</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RectifiedGridTypeImpl#getOffsetVector <em>Offset Vector</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RectifiedGridTypeImpl extends GridTypeImpl implements RectifiedGridType {
    /**
     * The cached value of the '{@link #getOrigin() <em>Origin</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrigin()
     * @generated
     * @ordered
     */
    protected PointPropertyType origin;

    /**
     * The cached value of the '{@link #getOffsetVector() <em>Offset Vector</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOffsetVector()
     * @generated
     * @ordered
     */
    protected EList<VectorType> offsetVector;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RectifiedGridTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getRectifiedGridType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PointPropertyType getOrigin() {
        return origin;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOrigin(PointPropertyType newOrigin, NotificationChain msgs) {
        PointPropertyType oldOrigin = origin;
        origin = newOrigin;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.RECTIFIED_GRID_TYPE__ORIGIN, oldOrigin, newOrigin);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOrigin(PointPropertyType newOrigin) {
        if (newOrigin != origin) {
            NotificationChain msgs = null;
            if (origin != null)
                msgs = ((InternalEObject)origin).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RECTIFIED_GRID_TYPE__ORIGIN, null, msgs);
            if (newOrigin != null)
                msgs = ((InternalEObject)newOrigin).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.RECTIFIED_GRID_TYPE__ORIGIN, null, msgs);
            msgs = basicSetOrigin(newOrigin, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.RECTIFIED_GRID_TYPE__ORIGIN, newOrigin, newOrigin));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<VectorType> getOffsetVector() {
        if (offsetVector == null) {
            offsetVector = new EObjectContainmentEList<VectorType>(VectorType.class, this, Gml311Package.RECTIFIED_GRID_TYPE__OFFSET_VECTOR);
        }
        return offsetVector;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.RECTIFIED_GRID_TYPE__ORIGIN:
                return basicSetOrigin(null, msgs);
            case Gml311Package.RECTIFIED_GRID_TYPE__OFFSET_VECTOR:
                return ((InternalEList<?>)getOffsetVector()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.RECTIFIED_GRID_TYPE__ORIGIN:
                return getOrigin();
            case Gml311Package.RECTIFIED_GRID_TYPE__OFFSET_VECTOR:
                return getOffsetVector();
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
            case Gml311Package.RECTIFIED_GRID_TYPE__ORIGIN:
                setOrigin((PointPropertyType)newValue);
                return;
            case Gml311Package.RECTIFIED_GRID_TYPE__OFFSET_VECTOR:
                getOffsetVector().clear();
                getOffsetVector().addAll((Collection<? extends VectorType>)newValue);
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
            case Gml311Package.RECTIFIED_GRID_TYPE__ORIGIN:
                setOrigin((PointPropertyType)null);
                return;
            case Gml311Package.RECTIFIED_GRID_TYPE__OFFSET_VECTOR:
                getOffsetVector().clear();
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
            case Gml311Package.RECTIFIED_GRID_TYPE__ORIGIN:
                return origin != null;
            case Gml311Package.RECTIFIED_GRID_TYPE__OFFSET_VECTOR:
                return offsetVector != null && !offsetVector.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //RectifiedGridTypeImpl
