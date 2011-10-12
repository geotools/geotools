/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.BinarySpatialOpType;
import net.opengis.fes20.Fes20Package;

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
 * An implementation of the model object '<em><b>Binary Spatial Op Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.BinarySpatialOpTypeImpl#getValueReference <em>Value Reference</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.BinarySpatialOpTypeImpl#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.BinarySpatialOpTypeImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.BinarySpatialOpTypeImpl#getAny <em>Any</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BinarySpatialOpTypeImpl extends SpatialOpsTypeImpl implements BinarySpatialOpType {
    /**
     * The default value of the '{@link #getValueReference() <em>Value Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueReference()
     * @generated
     * @ordered
     */
    protected static final String VALUE_REFERENCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValueReference() <em>Value Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueReference()
     * @generated
     * @ordered
     */
    protected String valueReference = VALUE_REFERENCE_EDEFAULT;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BinarySpatialOpTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.BINARY_SPATIAL_OP_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValueReference() {
        return valueReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValueReference(String newValueReference) {
        String oldValueReference = valueReference;
        valueReference = newValueReference;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.BINARY_SPATIAL_OP_TYPE__VALUE_REFERENCE, oldValueReference, valueReference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getExpressionGroup() {
        if (expressionGroup == null) {
            expressionGroup = new BasicFeatureMap(this, Fes20Package.BINARY_SPATIAL_OP_TYPE__EXPRESSION_GROUP);
        }
        return expressionGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getExpression() {
        return (EObject)getExpressionGroup().get(Fes20Package.Literals.BINARY_SPATIAL_OP_TYPE__EXPRESSION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExpression(EObject newExpression, NotificationChain msgs) {
        return ((FeatureMap.Internal)getExpressionGroup()).basicAdd(Fes20Package.Literals.BINARY_SPATIAL_OP_TYPE__EXPRESSION, newExpression, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAny() {
        if (any == null) {
            any = new BasicFeatureMap(this, Fes20Package.BINARY_SPATIAL_OP_TYPE__ANY);
        }
        return any;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__EXPRESSION_GROUP:
                return ((InternalEList<?>)getExpressionGroup()).basicRemove(otherEnd, msgs);
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__EXPRESSION:
                return basicSetExpression(null, msgs);
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__ANY:
                return ((InternalEList<?>)getAny()).basicRemove(otherEnd, msgs);
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
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__VALUE_REFERENCE:
                return getValueReference();
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__EXPRESSION_GROUP:
                if (coreType) return getExpressionGroup();
                return ((FeatureMap.Internal)getExpressionGroup()).getWrapper();
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__EXPRESSION:
                return getExpression();
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__ANY:
                if (coreType) return getAny();
                return ((FeatureMap.Internal)getAny()).getWrapper();
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
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__VALUE_REFERENCE:
                setValueReference((String)newValue);
                return;
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__EXPRESSION_GROUP:
                ((FeatureMap.Internal)getExpressionGroup()).set(newValue);
                return;
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__ANY:
                ((FeatureMap.Internal)getAny()).set(newValue);
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
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__VALUE_REFERENCE:
                setValueReference(VALUE_REFERENCE_EDEFAULT);
                return;
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__EXPRESSION_GROUP:
                getExpressionGroup().clear();
                return;
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__ANY:
                getAny().clear();
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
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__VALUE_REFERENCE:
                return VALUE_REFERENCE_EDEFAULT == null ? valueReference != null : !VALUE_REFERENCE_EDEFAULT.equals(valueReference);
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__EXPRESSION_GROUP:
                return expressionGroup != null && !expressionGroup.isEmpty();
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__EXPRESSION:
                return getExpression() != null;
            case Fes20Package.BINARY_SPATIAL_OP_TYPE__ANY:
                return any != null && !any.isEmpty();
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
        result.append(" (valueReference: ");
        result.append(valueReference);
        result.append(", expressionGroup: ");
        result.append(expressionGroup);
        result.append(", any: ");
        result.append(any);
        result.append(')');
        return result.toString();
    }

} //BinarySpatialOpTypeImpl
