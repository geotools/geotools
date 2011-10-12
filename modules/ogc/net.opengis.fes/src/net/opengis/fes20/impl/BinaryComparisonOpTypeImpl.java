/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.BinaryComparisonOpType;
import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.MatchActionType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binary Comparison Op Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.BinaryComparisonOpTypeImpl#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.BinaryComparisonOpTypeImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.BinaryComparisonOpTypeImpl#getMatchAction <em>Match Action</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.BinaryComparisonOpTypeImpl#isMatchCase <em>Match Case</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BinaryComparisonOpTypeImpl extends ComparisonOpsTypeImpl implements BinaryComparisonOpType {
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
     * The default value of the '{@link #getMatchAction() <em>Match Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMatchAction()
     * @generated
     * @ordered
     */
    protected static final MatchActionType MATCH_ACTION_EDEFAULT = MatchActionType.ANY;

    /**
     * The cached value of the '{@link #getMatchAction() <em>Match Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMatchAction()
     * @generated
     * @ordered
     */
    protected MatchActionType matchAction = MATCH_ACTION_EDEFAULT;

    /**
     * This is true if the Match Action attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean matchActionESet;

    /**
     * The default value of the '{@link #isMatchCase() <em>Match Case</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isMatchCase()
     * @generated
     * @ordered
     */
    protected static final boolean MATCH_CASE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isMatchCase() <em>Match Case</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isMatchCase()
     * @generated
     * @ordered
     */
    protected boolean matchCase = MATCH_CASE_EDEFAULT;

    /**
     * This is true if the Match Case attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean matchCaseESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BinaryComparisonOpTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.BINARY_COMPARISON_OP_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getExpressionGroup() {
        if (expressionGroup == null) {
            expressionGroup = new BasicFeatureMap(this, Fes20Package.BINARY_COMPARISON_OP_TYPE__EXPRESSION_GROUP);
        }
        return expressionGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<EObject> getExpression() {
        return getExpressionGroup().list(Fes20Package.Literals.BINARY_COMPARISON_OP_TYPE__EXPRESSION);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MatchActionType getMatchAction() {
        return matchAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMatchAction(MatchActionType newMatchAction) {
        MatchActionType oldMatchAction = matchAction;
        matchAction = newMatchAction == null ? MATCH_ACTION_EDEFAULT : newMatchAction;
        boolean oldMatchActionESet = matchActionESet;
        matchActionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_ACTION, oldMatchAction, matchAction, !oldMatchActionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetMatchAction() {
        MatchActionType oldMatchAction = matchAction;
        boolean oldMatchActionESet = matchActionESet;
        matchAction = MATCH_ACTION_EDEFAULT;
        matchActionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_ACTION, oldMatchAction, MATCH_ACTION_EDEFAULT, oldMatchActionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetMatchAction() {
        return matchActionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isMatchCase() {
        return matchCase;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMatchCase(boolean newMatchCase) {
        boolean oldMatchCase = matchCase;
        matchCase = newMatchCase;
        boolean oldMatchCaseESet = matchCaseESet;
        matchCaseESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_CASE, oldMatchCase, matchCase, !oldMatchCaseESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetMatchCase() {
        boolean oldMatchCase = matchCase;
        boolean oldMatchCaseESet = matchCaseESet;
        matchCase = MATCH_CASE_EDEFAULT;
        matchCaseESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_CASE, oldMatchCase, MATCH_CASE_EDEFAULT, oldMatchCaseESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetMatchCase() {
        return matchCaseESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__EXPRESSION_GROUP:
                return ((InternalEList<?>)getExpressionGroup()).basicRemove(otherEnd, msgs);
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__EXPRESSION:
                return ((InternalEList<?>)getExpression()).basicRemove(otherEnd, msgs);
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
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__EXPRESSION_GROUP:
                if (coreType) return getExpressionGroup();
                return ((FeatureMap.Internal)getExpressionGroup()).getWrapper();
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__EXPRESSION:
                return getExpression();
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_ACTION:
                return getMatchAction();
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_CASE:
                return isMatchCase();
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
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__EXPRESSION_GROUP:
                ((FeatureMap.Internal)getExpressionGroup()).set(newValue);
                return;
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_ACTION:
                setMatchAction((MatchActionType)newValue);
                return;
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_CASE:
                setMatchCase((Boolean)newValue);
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
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__EXPRESSION_GROUP:
                getExpressionGroup().clear();
                return;
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_ACTION:
                unsetMatchAction();
                return;
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_CASE:
                unsetMatchCase();
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
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__EXPRESSION_GROUP:
                return expressionGroup != null && !expressionGroup.isEmpty();
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__EXPRESSION:
                return !getExpression().isEmpty();
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_ACTION:
                return isSetMatchAction();
            case Fes20Package.BINARY_COMPARISON_OP_TYPE__MATCH_CASE:
                return isSetMatchCase();
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
        result.append(", matchAction: ");
        if (matchActionESet) result.append(matchAction); else result.append("<unset>");
        result.append(", matchCase: ");
        if (matchCaseESet) result.append(matchCase); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //BinaryComparisonOpTypeImpl
