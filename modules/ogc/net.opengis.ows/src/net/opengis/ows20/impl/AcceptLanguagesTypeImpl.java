/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.AcceptLanguagesType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Accept Languages Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.AcceptLanguagesTypeImpl#getLanguage <em>Language</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AcceptLanguagesTypeImpl extends EObjectImpl implements AcceptLanguagesType {
    /**
     * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected EList<String> language;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AcceptLanguagesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.ACCEPT_LANGUAGES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getLanguage() {
        if (language == null) {
            language = new EDataTypeEList<String>(String.class, this, Ows20Package.ACCEPT_LANGUAGES_TYPE__LANGUAGE);
        }
        return language;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows20Package.ACCEPT_LANGUAGES_TYPE__LANGUAGE:
                return getLanguage();
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
            case Ows20Package.ACCEPT_LANGUAGES_TYPE__LANGUAGE:
                getLanguage().clear();
                getLanguage().addAll((Collection<? extends String>)newValue);
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
            case Ows20Package.ACCEPT_LANGUAGES_TYPE__LANGUAGE:
                getLanguage().clear();
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
            case Ows20Package.ACCEPT_LANGUAGES_TYPE__LANGUAGE:
                return language != null && !language.isEmpty();
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
        result.append(" (language: ");
        result.append(language);
        result.append(')');
        return result.toString();
    }

} //AcceptLanguagesTypeImpl
