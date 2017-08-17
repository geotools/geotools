/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.List;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.GridFunctionType;
import net.opengis.gml311.SequenceRuleType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grid Function Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GridFunctionTypeImpl#getSequenceRule <em>Sequence Rule</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GridFunctionTypeImpl#getStartPoint <em>Start Point</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GridFunctionTypeImpl extends MinimalEObjectImpl.Container implements GridFunctionType {
    /**
     * The cached value of the '{@link #getSequenceRule() <em>Sequence Rule</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSequenceRule()
     * @generated
     * @ordered
     */
    protected SequenceRuleType sequenceRule;

    /**
     * The default value of the '{@link #getStartPoint() <em>Start Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartPoint()
     * @generated
     * @ordered
     */
    protected static final List<BigInteger> START_POINT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStartPoint() <em>Start Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartPoint()
     * @generated
     * @ordered
     */
    protected List<BigInteger> startPoint = START_POINT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GridFunctionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGridFunctionType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SequenceRuleType getSequenceRule() {
        return sequenceRule;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSequenceRule(SequenceRuleType newSequenceRule, NotificationChain msgs) {
        SequenceRuleType oldSequenceRule = sequenceRule;
        sequenceRule = newSequenceRule;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_FUNCTION_TYPE__SEQUENCE_RULE, oldSequenceRule, newSequenceRule);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSequenceRule(SequenceRuleType newSequenceRule) {
        if (newSequenceRule != sequenceRule) {
            NotificationChain msgs = null;
            if (sequenceRule != null)
                msgs = ((InternalEObject)sequenceRule).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GRID_FUNCTION_TYPE__SEQUENCE_RULE, null, msgs);
            if (newSequenceRule != null)
                msgs = ((InternalEObject)newSequenceRule).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GRID_FUNCTION_TYPE__SEQUENCE_RULE, null, msgs);
            msgs = basicSetSequenceRule(newSequenceRule, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_FUNCTION_TYPE__SEQUENCE_RULE, newSequenceRule, newSequenceRule));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<BigInteger> getStartPoint() {
        return startPoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStartPoint(List<BigInteger> newStartPoint) {
        List<BigInteger> oldStartPoint = startPoint;
        startPoint = newStartPoint;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_FUNCTION_TYPE__START_POINT, oldStartPoint, startPoint));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.GRID_FUNCTION_TYPE__SEQUENCE_RULE:
                return basicSetSequenceRule(null, msgs);
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
            case Gml311Package.GRID_FUNCTION_TYPE__SEQUENCE_RULE:
                return getSequenceRule();
            case Gml311Package.GRID_FUNCTION_TYPE__START_POINT:
                return getStartPoint();
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
            case Gml311Package.GRID_FUNCTION_TYPE__SEQUENCE_RULE:
                setSequenceRule((SequenceRuleType)newValue);
                return;
            case Gml311Package.GRID_FUNCTION_TYPE__START_POINT:
                setStartPoint((List<BigInteger>)newValue);
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
            case Gml311Package.GRID_FUNCTION_TYPE__SEQUENCE_RULE:
                setSequenceRule((SequenceRuleType)null);
                return;
            case Gml311Package.GRID_FUNCTION_TYPE__START_POINT:
                setStartPoint(START_POINT_EDEFAULT);
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
            case Gml311Package.GRID_FUNCTION_TYPE__SEQUENCE_RULE:
                return sequenceRule != null;
            case Gml311Package.GRID_FUNCTION_TYPE__START_POINT:
                return START_POINT_EDEFAULT == null ? startPoint != null : !START_POINT_EDEFAULT.equals(startPoint);
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
        result.append(" (startPoint: ");
        result.append(startPoint);
        result.append(')');
        return result.toString();
    }

} //GridFunctionTypeImpl
