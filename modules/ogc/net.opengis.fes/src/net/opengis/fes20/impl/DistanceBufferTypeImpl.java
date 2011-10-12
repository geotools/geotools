/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.DistanceBufferType;
import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.MeasureType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Distance Buffer Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.DistanceBufferTypeImpl#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DistanceBufferTypeImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DistanceBufferTypeImpl#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.DistanceBufferTypeImpl#getDistance <em>Distance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DistanceBufferTypeImpl extends SpatialOpsTypeImpl implements DistanceBufferType {
    /**
     * The cached value of the '{@link #getExpressionGroup() <em>Expression Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExpressionGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap expressionGroup;

    /**
     * The cached value of the '{@link #getAny() <em>Any</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAny()
     * @generated
     * @ordered
     */
    protected FeatureMap any;

    /**
     * The cached value of the '{@link #getDistance() <em>Distance</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDistance()
     * @generated
     * @ordered
     */
    protected MeasureType distance;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DistanceBufferTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.DISTANCE_BUFFER_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getExpressionGroup() {
        if (expressionGroup == null) {
            expressionGroup = new BasicFeatureMap(this, Fes20Package.DISTANCE_BUFFER_TYPE__EXPRESSION_GROUP);
        }
        return expressionGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getExpression() {
        return (EObject)getExpressionGroup().get(Fes20Package.Literals.DISTANCE_BUFFER_TYPE__EXPRESSION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExpression(EObject newExpression, NotificationChain msgs) {
        return ((FeatureMap.Internal)getExpressionGroup()).basicAdd(Fes20Package.Literals.DISTANCE_BUFFER_TYPE__EXPRESSION, newExpression, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAny() {
        if (any == null) {
            any = new BasicFeatureMap(this, Fes20Package.DISTANCE_BUFFER_TYPE__ANY);
        }
        return any;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType getDistance() {
        return distance;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDistance(MeasureType newDistance, NotificationChain msgs) {
        MeasureType oldDistance = distance;
        distance = newDistance;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.DISTANCE_BUFFER_TYPE__DISTANCE, oldDistance, newDistance);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDistance(MeasureType newDistance) {
        if (newDistance != distance) {
            NotificationChain msgs = null;
            if (distance != null)
                msgs = ((InternalEObject)distance).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.DISTANCE_BUFFER_TYPE__DISTANCE, null, msgs);
            if (newDistance != null)
                msgs = ((InternalEObject)newDistance).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.DISTANCE_BUFFER_TYPE__DISTANCE, null, msgs);
            msgs = basicSetDistance(newDistance, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.DISTANCE_BUFFER_TYPE__DISTANCE, newDistance, newDistance));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.DISTANCE_BUFFER_TYPE__EXPRESSION_GROUP:
                return ((InternalEList<?>)getExpressionGroup()).basicRemove(otherEnd, msgs);
            case Fes20Package.DISTANCE_BUFFER_TYPE__EXPRESSION:
                return basicSetExpression(null, msgs);
            case Fes20Package.DISTANCE_BUFFER_TYPE__ANY:
                return ((InternalEList<?>)getAny()).basicRemove(otherEnd, msgs);
            case Fes20Package.DISTANCE_BUFFER_TYPE__DISTANCE:
                return basicSetDistance(null, msgs);
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
            case Fes20Package.DISTANCE_BUFFER_TYPE__EXPRESSION_GROUP:
                if (coreType) return getExpressionGroup();
                return ((FeatureMap.Internal)getExpressionGroup()).getWrapper();
            case Fes20Package.DISTANCE_BUFFER_TYPE__EXPRESSION:
                return getExpression();
            case Fes20Package.DISTANCE_BUFFER_TYPE__ANY:
                if (coreType) return getAny();
                return ((FeatureMap.Internal)getAny()).getWrapper();
            case Fes20Package.DISTANCE_BUFFER_TYPE__DISTANCE:
                return getDistance();
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
            case Fes20Package.DISTANCE_BUFFER_TYPE__EXPRESSION_GROUP:
                ((FeatureMap.Internal)getExpressionGroup()).set(newValue);
                return;
            case Fes20Package.DISTANCE_BUFFER_TYPE__ANY:
                ((FeatureMap.Internal)getAny()).set(newValue);
                return;
            case Fes20Package.DISTANCE_BUFFER_TYPE__DISTANCE:
                setDistance((MeasureType)newValue);
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
            case Fes20Package.DISTANCE_BUFFER_TYPE__EXPRESSION_GROUP:
                getExpressionGroup().clear();
                return;
            case Fes20Package.DISTANCE_BUFFER_TYPE__ANY:
                getAny().clear();
                return;
            case Fes20Package.DISTANCE_BUFFER_TYPE__DISTANCE:
                setDistance((MeasureType)null);
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
            case Fes20Package.DISTANCE_BUFFER_TYPE__EXPRESSION_GROUP:
                return expressionGroup != null && !expressionGroup.isEmpty();
            case Fes20Package.DISTANCE_BUFFER_TYPE__EXPRESSION:
                return getExpression() != null;
            case Fes20Package.DISTANCE_BUFFER_TYPE__ANY:
                return any != null && !any.isEmpty();
            case Fes20Package.DISTANCE_BUFFER_TYPE__DISTANCE:
                return distance != null;
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
        result.append(" (expressionGroup: ");
        result.append(expressionGroup);
        result.append(", any: ");
        result.append(any);
        result.append(')');
        return result.toString();
    }

} //DistanceBufferTypeImpl
