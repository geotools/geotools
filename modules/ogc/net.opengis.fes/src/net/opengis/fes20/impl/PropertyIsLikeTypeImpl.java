/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.PropertyIsLikeType;

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
 * An implementation of the model object '<em><b>Property Is Like Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.PropertyIsLikeTypeImpl#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.PropertyIsLikeTypeImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.PropertyIsLikeTypeImpl#getEscapeChar <em>Escape Char</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.PropertyIsLikeTypeImpl#getSingleChar <em>Single Char</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.PropertyIsLikeTypeImpl#getWildCard <em>Wild Card</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PropertyIsLikeTypeImpl extends ComparisonOpsTypeImpl implements PropertyIsLikeType {
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
     * The default value of the '{@link #getEscapeChar() <em>Escape Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEscapeChar()
     * @generated
     * @ordered
     */
    protected static final String ESCAPE_CHAR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEscapeChar() <em>Escape Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEscapeChar()
     * @generated
     * @ordered
     */
    protected String escapeChar = ESCAPE_CHAR_EDEFAULT;

    /**
     * The default value of the '{@link #getSingleChar() <em>Single Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSingleChar()
     * @generated
     * @ordered
     */
    protected static final String SINGLE_CHAR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSingleChar() <em>Single Char</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSingleChar()
     * @generated
     * @ordered
     */
    protected String singleChar = SINGLE_CHAR_EDEFAULT;

    /**
     * The default value of the '{@link #getWildCard() <em>Wild Card</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWildCard()
     * @generated
     * @ordered
     */
    protected static final String WILD_CARD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWildCard() <em>Wild Card</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWildCard()
     * @generated
     * @ordered
     */
    protected String wildCard = WILD_CARD_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PropertyIsLikeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.PROPERTY_IS_LIKE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getExpressionGroup() {
        if (expressionGroup == null) {
            expressionGroup = new BasicFeatureMap(this, Fes20Package.PROPERTY_IS_LIKE_TYPE__EXPRESSION_GROUP);
        }
        return expressionGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<EObject> getExpression() {
        return getExpressionGroup().list(Fes20Package.Literals.PROPERTY_IS_LIKE_TYPE__EXPRESSION);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEscapeChar() {
        return escapeChar;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEscapeChar(String newEscapeChar) {
        String oldEscapeChar = escapeChar;
        escapeChar = newEscapeChar;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.PROPERTY_IS_LIKE_TYPE__ESCAPE_CHAR, oldEscapeChar, escapeChar));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSingleChar() {
        return singleChar;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSingleChar(String newSingleChar) {
        String oldSingleChar = singleChar;
        singleChar = newSingleChar;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.PROPERTY_IS_LIKE_TYPE__SINGLE_CHAR, oldSingleChar, singleChar));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getWildCard() {
        return wildCard;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWildCard(String newWildCard) {
        String oldWildCard = wildCard;
        wildCard = newWildCard;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.PROPERTY_IS_LIKE_TYPE__WILD_CARD, oldWildCard, wildCard));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__EXPRESSION_GROUP:
                return ((InternalEList<?>)getExpressionGroup()).basicRemove(otherEnd, msgs);
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__EXPRESSION:
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
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__EXPRESSION_GROUP:
                if (coreType) return getExpressionGroup();
                return ((FeatureMap.Internal)getExpressionGroup()).getWrapper();
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__EXPRESSION:
                return getExpression();
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__ESCAPE_CHAR:
                return getEscapeChar();
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__SINGLE_CHAR:
                return getSingleChar();
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__WILD_CARD:
                return getWildCard();
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
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__EXPRESSION_GROUP:
                ((FeatureMap.Internal)getExpressionGroup()).set(newValue);
                return;
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__ESCAPE_CHAR:
                setEscapeChar((String)newValue);
                return;
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__SINGLE_CHAR:
                setSingleChar((String)newValue);
                return;
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__WILD_CARD:
                setWildCard((String)newValue);
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
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__EXPRESSION_GROUP:
                getExpressionGroup().clear();
                return;
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__ESCAPE_CHAR:
                setEscapeChar(ESCAPE_CHAR_EDEFAULT);
                return;
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__SINGLE_CHAR:
                setSingleChar(SINGLE_CHAR_EDEFAULT);
                return;
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__WILD_CARD:
                setWildCard(WILD_CARD_EDEFAULT);
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
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__EXPRESSION_GROUP:
                return expressionGroup != null && !expressionGroup.isEmpty();
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__EXPRESSION:
                return !getExpression().isEmpty();
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__ESCAPE_CHAR:
                return ESCAPE_CHAR_EDEFAULT == null ? escapeChar != null : !ESCAPE_CHAR_EDEFAULT.equals(escapeChar);
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__SINGLE_CHAR:
                return SINGLE_CHAR_EDEFAULT == null ? singleChar != null : !SINGLE_CHAR_EDEFAULT.equals(singleChar);
            case Fes20Package.PROPERTY_IS_LIKE_TYPE__WILD_CARD:
                return WILD_CARD_EDEFAULT == null ? wildCard != null : !WILD_CARD_EDEFAULT.equals(wildCard);
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
        result.append(", escapeChar: ");
        result.append(escapeChar);
        result.append(", singleChar: ");
        result.append(singleChar);
        result.append(", wildCard: ");
        result.append(wildCard);
        result.append(')');
        return result.toString();
    }

} //PropertyIsLikeTypeImpl
