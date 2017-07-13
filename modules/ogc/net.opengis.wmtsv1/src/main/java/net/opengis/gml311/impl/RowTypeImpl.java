/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.DirectPositionListType;
import net.opengis.gml311.DirectPositionType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.PointPropertyType;
import net.opengis.gml311.RowType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Row Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.RowTypeImpl#getPosList <em>Pos List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RowTypeImpl#getGeometricPositionGroup <em>Geometric Position Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RowTypeImpl#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.RowTypeImpl#getPointProperty <em>Point Property</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RowTypeImpl extends MinimalEObjectImpl.Container implements RowType {
    /**
     * The cached value of the '{@link #getPosList() <em>Pos List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPosList()
     * @generated
     * @ordered
     */
    protected DirectPositionListType posList;

    /**
     * The cached value of the '{@link #getGeometricPositionGroup() <em>Geometric Position Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeometricPositionGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap geometricPositionGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RowTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getRowType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectPositionListType getPosList() {
        return posList;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPosList(DirectPositionListType newPosList, NotificationChain msgs) {
        DirectPositionListType oldPosList = posList;
        posList = newPosList;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ROW_TYPE__POS_LIST, oldPosList, newPosList);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPosList(DirectPositionListType newPosList) {
        if (newPosList != posList) {
            NotificationChain msgs = null;
            if (posList != null)
                msgs = ((InternalEObject)posList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ROW_TYPE__POS_LIST, null, msgs);
            if (newPosList != null)
                msgs = ((InternalEObject)newPosList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ROW_TYPE__POS_LIST, null, msgs);
            msgs = basicSetPosList(newPosList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ROW_TYPE__POS_LIST, newPosList, newPosList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGeometricPositionGroup() {
        if (geometricPositionGroup == null) {
            geometricPositionGroup = new BasicFeatureMap(this, Gml311Package.ROW_TYPE__GEOMETRIC_POSITION_GROUP);
        }
        return geometricPositionGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectPositionType> getPos() {
        return getGeometricPositionGroup().list(Gml311Package.eINSTANCE.getRowType_Pos());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PointPropertyType> getPointProperty() {
        return getGeometricPositionGroup().list(Gml311Package.eINSTANCE.getRowType_PointProperty());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ROW_TYPE__POS_LIST:
                return basicSetPosList(null, msgs);
            case Gml311Package.ROW_TYPE__GEOMETRIC_POSITION_GROUP:
                return ((InternalEList<?>)getGeometricPositionGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.ROW_TYPE__POS:
                return ((InternalEList<?>)getPos()).basicRemove(otherEnd, msgs);
            case Gml311Package.ROW_TYPE__POINT_PROPERTY:
                return ((InternalEList<?>)getPointProperty()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.ROW_TYPE__POS_LIST:
                return getPosList();
            case Gml311Package.ROW_TYPE__GEOMETRIC_POSITION_GROUP:
                if (coreType) return getGeometricPositionGroup();
                return ((FeatureMap.Internal)getGeometricPositionGroup()).getWrapper();
            case Gml311Package.ROW_TYPE__POS:
                return getPos();
            case Gml311Package.ROW_TYPE__POINT_PROPERTY:
                return getPointProperty();
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
            case Gml311Package.ROW_TYPE__POS_LIST:
                setPosList((DirectPositionListType)newValue);
                return;
            case Gml311Package.ROW_TYPE__GEOMETRIC_POSITION_GROUP:
                ((FeatureMap.Internal)getGeometricPositionGroup()).set(newValue);
                return;
            case Gml311Package.ROW_TYPE__POS:
                getPos().clear();
                getPos().addAll((Collection<? extends DirectPositionType>)newValue);
                return;
            case Gml311Package.ROW_TYPE__POINT_PROPERTY:
                getPointProperty().clear();
                getPointProperty().addAll((Collection<? extends PointPropertyType>)newValue);
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
            case Gml311Package.ROW_TYPE__POS_LIST:
                setPosList((DirectPositionListType)null);
                return;
            case Gml311Package.ROW_TYPE__GEOMETRIC_POSITION_GROUP:
                getGeometricPositionGroup().clear();
                return;
            case Gml311Package.ROW_TYPE__POS:
                getPos().clear();
                return;
            case Gml311Package.ROW_TYPE__POINT_PROPERTY:
                getPointProperty().clear();
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
            case Gml311Package.ROW_TYPE__POS_LIST:
                return posList != null;
            case Gml311Package.ROW_TYPE__GEOMETRIC_POSITION_GROUP:
                return geometricPositionGroup != null && !geometricPositionGroup.isEmpty();
            case Gml311Package.ROW_TYPE__POS:
                return !getPos().isEmpty();
            case Gml311Package.ROW_TYPE__POINT_PROPERTY:
                return !getPointProperty().isEmpty();
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
        result.append(" (geometricPositionGroup: ");
        result.append(geometricPositionGroup);
        result.append(')');
        return result.toString();
    }

} //RowTypeImpl
