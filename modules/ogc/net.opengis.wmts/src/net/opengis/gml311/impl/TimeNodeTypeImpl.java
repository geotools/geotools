/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TimeEdgePropertyType;
import net.opengis.gml311.TimeInstantPropertyType;
import net.opengis.gml311.TimeNodeType;

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
 * An implementation of the model object '<em><b>Time Node Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TimeNodeTypeImpl#getPreviousEdge <em>Previous Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimeNodeTypeImpl#getNextEdge <em>Next Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimeNodeTypeImpl#getPosition <em>Position</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimeNodeTypeImpl extends AbstractTimeTopologyPrimitiveTypeImpl implements TimeNodeType {
    /**
     * The cached value of the '{@link #getPreviousEdge() <em>Previous Edge</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPreviousEdge()
     * @generated
     * @ordered
     */
    protected EList<TimeEdgePropertyType> previousEdge;

    /**
     * The cached value of the '{@link #getNextEdge() <em>Next Edge</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNextEdge()
     * @generated
     * @ordered
     */
    protected EList<TimeEdgePropertyType> nextEdge;

    /**
     * The cached value of the '{@link #getPosition() <em>Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPosition()
     * @generated
     * @ordered
     */
    protected TimeInstantPropertyType position;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TimeNodeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTimeNodeType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TimeEdgePropertyType> getPreviousEdge() {
        if (previousEdge == null) {
            previousEdge = new EObjectContainmentEList<TimeEdgePropertyType>(TimeEdgePropertyType.class, this, Gml311Package.TIME_NODE_TYPE__PREVIOUS_EDGE);
        }
        return previousEdge;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TimeEdgePropertyType> getNextEdge() {
        if (nextEdge == null) {
            nextEdge = new EObjectContainmentEList<TimeEdgePropertyType>(TimeEdgePropertyType.class, this, Gml311Package.TIME_NODE_TYPE__NEXT_EDGE);
        }
        return nextEdge;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeInstantPropertyType getPosition() {
        return position;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPosition(TimeInstantPropertyType newPosition, NotificationChain msgs) {
        TimeInstantPropertyType oldPosition = position;
        position = newPosition;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_NODE_TYPE__POSITION, oldPosition, newPosition);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPosition(TimeInstantPropertyType newPosition) {
        if (newPosition != position) {
            NotificationChain msgs = null;
            if (position != null)
                msgs = ((InternalEObject)position).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_NODE_TYPE__POSITION, null, msgs);
            if (newPosition != null)
                msgs = ((InternalEObject)newPosition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_NODE_TYPE__POSITION, null, msgs);
            msgs = basicSetPosition(newPosition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_NODE_TYPE__POSITION, newPosition, newPosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TIME_NODE_TYPE__PREVIOUS_EDGE:
                return ((InternalEList<?>)getPreviousEdge()).basicRemove(otherEnd, msgs);
            case Gml311Package.TIME_NODE_TYPE__NEXT_EDGE:
                return ((InternalEList<?>)getNextEdge()).basicRemove(otherEnd, msgs);
            case Gml311Package.TIME_NODE_TYPE__POSITION:
                return basicSetPosition(null, msgs);
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
            case Gml311Package.TIME_NODE_TYPE__PREVIOUS_EDGE:
                return getPreviousEdge();
            case Gml311Package.TIME_NODE_TYPE__NEXT_EDGE:
                return getNextEdge();
            case Gml311Package.TIME_NODE_TYPE__POSITION:
                return getPosition();
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
            case Gml311Package.TIME_NODE_TYPE__PREVIOUS_EDGE:
                getPreviousEdge().clear();
                getPreviousEdge().addAll((Collection<? extends TimeEdgePropertyType>)newValue);
                return;
            case Gml311Package.TIME_NODE_TYPE__NEXT_EDGE:
                getNextEdge().clear();
                getNextEdge().addAll((Collection<? extends TimeEdgePropertyType>)newValue);
                return;
            case Gml311Package.TIME_NODE_TYPE__POSITION:
                setPosition((TimeInstantPropertyType)newValue);
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
            case Gml311Package.TIME_NODE_TYPE__PREVIOUS_EDGE:
                getPreviousEdge().clear();
                return;
            case Gml311Package.TIME_NODE_TYPE__NEXT_EDGE:
                getNextEdge().clear();
                return;
            case Gml311Package.TIME_NODE_TYPE__POSITION:
                setPosition((TimeInstantPropertyType)null);
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
            case Gml311Package.TIME_NODE_TYPE__PREVIOUS_EDGE:
                return previousEdge != null && !previousEdge.isEmpty();
            case Gml311Package.TIME_NODE_TYPE__NEXT_EDGE:
                return nextEdge != null && !nextEdge.isEmpty();
            case Gml311Package.TIME_NODE_TYPE__POSITION:
                return position != null;
        }
        return super.eIsSet(featureID);
    }

} //TimeNodeTypeImpl
