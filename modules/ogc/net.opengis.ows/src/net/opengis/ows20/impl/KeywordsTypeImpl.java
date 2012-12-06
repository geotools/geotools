/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.CodeType;
import net.opengis.ows20.KeywordsType;
import net.opengis.ows20.LanguageStringType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Keywords Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.KeywordsTypeImpl#getKeyword <em>Keyword</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.KeywordsTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class KeywordsTypeImpl extends EObjectImpl implements KeywordsType {
    /**
     * The cached value of the '{@link #getKeyword() <em>Keyword</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKeyword()
     * @generated
     * @ordered
     */
    protected EList<LanguageStringType> keyword;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected CodeType type;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected KeywordsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.KEYWORDS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LanguageStringType> getKeyword() {
        if (keyword == null) {
            keyword = new EObjectContainmentEList<LanguageStringType>(LanguageStringType.class, this, Ows20Package.KEYWORDS_TYPE__KEYWORD);
        }
        return keyword;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetType(CodeType newType, NotificationChain msgs) {
        CodeType oldType = type;
        type = newType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.KEYWORDS_TYPE__TYPE, oldType, newType);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(CodeType newType) {
        if (newType != type) {
            NotificationChain msgs = null;
            if (type != null)
                msgs = ((InternalEObject)type).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.KEYWORDS_TYPE__TYPE, null, msgs);
            if (newType != null)
                msgs = ((InternalEObject)newType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.KEYWORDS_TYPE__TYPE, null, msgs);
            msgs = basicSetType(newType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.KEYWORDS_TYPE__TYPE, newType, newType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.KEYWORDS_TYPE__KEYWORD:
                return ((InternalEList<?>)getKeyword()).basicRemove(otherEnd, msgs);
            case Ows20Package.KEYWORDS_TYPE__TYPE:
                return basicSetType(null, msgs);
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
            case Ows20Package.KEYWORDS_TYPE__KEYWORD:
                return getKeyword();
            case Ows20Package.KEYWORDS_TYPE__TYPE:
                return getType();
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
            case Ows20Package.KEYWORDS_TYPE__KEYWORD:
                getKeyword().clear();
                getKeyword().addAll((Collection<? extends LanguageStringType>)newValue);
                return;
            case Ows20Package.KEYWORDS_TYPE__TYPE:
                setType((CodeType)newValue);
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
            case Ows20Package.KEYWORDS_TYPE__KEYWORD:
                getKeyword().clear();
                return;
            case Ows20Package.KEYWORDS_TYPE__TYPE:
                setType((CodeType)null);
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
            case Ows20Package.KEYWORDS_TYPE__KEYWORD:
                return keyword != null && !keyword.isEmpty();
            case Ows20Package.KEYWORDS_TYPE__TYPE:
                return type != null;
        }
        return super.eIsSet(featureID);
    }

} //KeywordsTypeImpl
