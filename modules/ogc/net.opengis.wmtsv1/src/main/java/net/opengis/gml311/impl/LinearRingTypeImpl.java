/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.CoordType;
import net.opengis.gml311.CoordinatesType;
import net.opengis.gml311.DirectPositionListType;
import net.opengis.gml311.DirectPositionType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LinearRingType;
import net.opengis.gml311.PointPropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Linear Ring Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.LinearRingTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.LinearRingTypeImpl#getPos <em>Pos</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.LinearRingTypeImpl#getPointProperty <em>Point Property</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.LinearRingTypeImpl#getPointRep <em>Point Rep</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.LinearRingTypeImpl#getPosList <em>Pos List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.LinearRingTypeImpl#getCoordinates <em>Coordinates</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.LinearRingTypeImpl#getCoord <em>Coord</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LinearRingTypeImpl extends AbstractRingTypeImpl implements LinearRingType {
    /**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap group;

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
     * The cached value of the '{@link #getCoordinates() <em>Coordinates</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoordinates()
     * @generated
     * @ordered
     */
    protected CoordinatesType coordinates;

    /**
     * The cached value of the '{@link #getCoord() <em>Coord</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoord()
     * @generated
     * @ordered
     */
    protected EList<CoordType> coord;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LinearRingTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getLinearRingType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, Gml311Package.LINEAR_RING_TYPE__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectPositionType> getPos() {
        return getGroup().list(Gml311Package.eINSTANCE.getLinearRingType_Pos());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PointPropertyType> getPointProperty() {
        return getGroup().list(Gml311Package.eINSTANCE.getLinearRingType_PointProperty());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PointPropertyType> getPointRep() {
        return getGroup().list(Gml311Package.eINSTANCE.getLinearRingType_PointRep());
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.LINEAR_RING_TYPE__POS_LIST, oldPosList, newPosList);
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
                msgs = ((InternalEObject)posList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.LINEAR_RING_TYPE__POS_LIST, null, msgs);
            if (newPosList != null)
                msgs = ((InternalEObject)newPosList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.LINEAR_RING_TYPE__POS_LIST, null, msgs);
            msgs = basicSetPosList(newPosList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.LINEAR_RING_TYPE__POS_LIST, newPosList, newPosList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoordinatesType getCoordinates() {
        return coordinates;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCoordinates(CoordinatesType newCoordinates, NotificationChain msgs) {
        CoordinatesType oldCoordinates = coordinates;
        coordinates = newCoordinates;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.LINEAR_RING_TYPE__COORDINATES, oldCoordinates, newCoordinates);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCoordinates(CoordinatesType newCoordinates) {
        if (newCoordinates != coordinates) {
            NotificationChain msgs = null;
            if (coordinates != null)
                msgs = ((InternalEObject)coordinates).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.LINEAR_RING_TYPE__COORDINATES, null, msgs);
            if (newCoordinates != null)
                msgs = ((InternalEObject)newCoordinates).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.LINEAR_RING_TYPE__COORDINATES, null, msgs);
            msgs = basicSetCoordinates(newCoordinates, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.LINEAR_RING_TYPE__COORDINATES, newCoordinates, newCoordinates));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CoordType> getCoord() {
        if (coord == null) {
            coord = new EObjectContainmentEList<CoordType>(CoordType.class, this, Gml311Package.LINEAR_RING_TYPE__COORD);
        }
        return coord;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.LINEAR_RING_TYPE__GROUP:
                return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.LINEAR_RING_TYPE__POS:
                return ((InternalEList<?>)getPos()).basicRemove(otherEnd, msgs);
            case Gml311Package.LINEAR_RING_TYPE__POINT_PROPERTY:
                return ((InternalEList<?>)getPointProperty()).basicRemove(otherEnd, msgs);
            case Gml311Package.LINEAR_RING_TYPE__POINT_REP:
                return ((InternalEList<?>)getPointRep()).basicRemove(otherEnd, msgs);
            case Gml311Package.LINEAR_RING_TYPE__POS_LIST:
                return basicSetPosList(null, msgs);
            case Gml311Package.LINEAR_RING_TYPE__COORDINATES:
                return basicSetCoordinates(null, msgs);
            case Gml311Package.LINEAR_RING_TYPE__COORD:
                return ((InternalEList<?>)getCoord()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.LINEAR_RING_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case Gml311Package.LINEAR_RING_TYPE__POS:
                return getPos();
            case Gml311Package.LINEAR_RING_TYPE__POINT_PROPERTY:
                return getPointProperty();
            case Gml311Package.LINEAR_RING_TYPE__POINT_REP:
                return getPointRep();
            case Gml311Package.LINEAR_RING_TYPE__POS_LIST:
                return getPosList();
            case Gml311Package.LINEAR_RING_TYPE__COORDINATES:
                return getCoordinates();
            case Gml311Package.LINEAR_RING_TYPE__COORD:
                return getCoord();
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
            case Gml311Package.LINEAR_RING_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case Gml311Package.LINEAR_RING_TYPE__POS:
                getPos().clear();
                getPos().addAll((Collection<? extends DirectPositionType>)newValue);
                return;
            case Gml311Package.LINEAR_RING_TYPE__POINT_PROPERTY:
                getPointProperty().clear();
                getPointProperty().addAll((Collection<? extends PointPropertyType>)newValue);
                return;
            case Gml311Package.LINEAR_RING_TYPE__POINT_REP:
                getPointRep().clear();
                getPointRep().addAll((Collection<? extends PointPropertyType>)newValue);
                return;
            case Gml311Package.LINEAR_RING_TYPE__POS_LIST:
                setPosList((DirectPositionListType)newValue);
                return;
            case Gml311Package.LINEAR_RING_TYPE__COORDINATES:
                setCoordinates((CoordinatesType)newValue);
                return;
            case Gml311Package.LINEAR_RING_TYPE__COORD:
                getCoord().clear();
                getCoord().addAll((Collection<? extends CoordType>)newValue);
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
            case Gml311Package.LINEAR_RING_TYPE__GROUP:
                getGroup().clear();
                return;
            case Gml311Package.LINEAR_RING_TYPE__POS:
                getPos().clear();
                return;
            case Gml311Package.LINEAR_RING_TYPE__POINT_PROPERTY:
                getPointProperty().clear();
                return;
            case Gml311Package.LINEAR_RING_TYPE__POINT_REP:
                getPointRep().clear();
                return;
            case Gml311Package.LINEAR_RING_TYPE__POS_LIST:
                setPosList((DirectPositionListType)null);
                return;
            case Gml311Package.LINEAR_RING_TYPE__COORDINATES:
                setCoordinates((CoordinatesType)null);
                return;
            case Gml311Package.LINEAR_RING_TYPE__COORD:
                getCoord().clear();
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
            case Gml311Package.LINEAR_RING_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case Gml311Package.LINEAR_RING_TYPE__POS:
                return !getPos().isEmpty();
            case Gml311Package.LINEAR_RING_TYPE__POINT_PROPERTY:
                return !getPointProperty().isEmpty();
            case Gml311Package.LINEAR_RING_TYPE__POINT_REP:
                return !getPointRep().isEmpty();
            case Gml311Package.LINEAR_RING_TYPE__POS_LIST:
                return posList != null;
            case Gml311Package.LINEAR_RING_TYPE__COORDINATES:
                return coordinates != null;
            case Gml311Package.LINEAR_RING_TYPE__COORD:
                return coord != null && !coord.isEmpty();
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
        result.append(" (group: ");
        result.append(group);
        result.append(')');
        return result.toString();
    }

} //LinearRingTypeImpl
