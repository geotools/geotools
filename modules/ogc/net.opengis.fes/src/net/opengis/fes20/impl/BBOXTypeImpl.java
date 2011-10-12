/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.BBOXType;
import net.opengis.fes20.Fes20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>BBOX Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.BBOXTypeImpl#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.BBOXTypeImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.BBOXTypeImpl#getAny <em>Any</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BBOXTypeImpl extends SpatialOpsTypeImpl implements BBOXType {
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
    protected BBOXTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.BBOX_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getExpressionGroup() {
        if (expressionGroup == null) {
            expressionGroup = new BasicFeatureMap(this, Fes20Package.BBOX_TYPE__EXPRESSION_GROUP);
        }
        return expressionGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getExpression() {
        return (EObject)getExpressionGroup().get(Fes20Package.Literals.BBOX_TYPE__EXPRESSION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExpression(EObject newExpression, NotificationChain msgs) {
        return ((FeatureMap.Internal)getExpressionGroup()).basicAdd(Fes20Package.Literals.BBOX_TYPE__EXPRESSION, newExpression, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAny() {
        if (any == null) {
            any = new BasicFeatureMap(this, Fes20Package.BBOX_TYPE__ANY);
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
            case Fes20Package.BBOX_TYPE__EXPRESSION_GROUP:
                return ((InternalEList<?>)getExpressionGroup()).basicRemove(otherEnd, msgs);
            case Fes20Package.BBOX_TYPE__EXPRESSION:
                return basicSetExpression(null, msgs);
            case Fes20Package.BBOX_TYPE__ANY:
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
            case Fes20Package.BBOX_TYPE__EXPRESSION_GROUP:
                if (coreType) return getExpressionGroup();
                return ((FeatureMap.Internal)getExpressionGroup()).getWrapper();
            case Fes20Package.BBOX_TYPE__EXPRESSION:
                return getExpression();
            case Fes20Package.BBOX_TYPE__ANY:
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
            case Fes20Package.BBOX_TYPE__EXPRESSION_GROUP:
                ((FeatureMap.Internal)getExpressionGroup()).set(newValue);
                return;
            case Fes20Package.BBOX_TYPE__ANY:
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
            case Fes20Package.BBOX_TYPE__EXPRESSION_GROUP:
                getExpressionGroup().clear();
                return;
            case Fes20Package.BBOX_TYPE__ANY:
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
            case Fes20Package.BBOX_TYPE__EXPRESSION_GROUP:
                return expressionGroup != null && !expressionGroup.isEmpty();
            case Fes20Package.BBOX_TYPE__EXPRESSION:
                return getExpression() != null;
            case Fes20Package.BBOX_TYPE__ANY:
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
        result.append(" (expressionGroup: ");
        result.append(expressionGroup);
        result.append(", any: ");
        result.append(any);
        result.append(')');
        return result.toString();
    }

} //BBOXTypeImpl
