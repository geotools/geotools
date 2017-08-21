/**
 */
package net.opengis.wmts.v_1.impl;

import java.util.Collection;

import net.opengis.wmts.v_1.ThemeType;
import net.opengis.wmts.v_1.ThemesType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Themes Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.ThemesTypeImpl#getTheme <em>Theme</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ThemesTypeImpl extends MinimalEObjectImpl.Container implements ThemesType {
    /**
     * The cached value of the '{@link #getTheme() <em>Theme</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTheme()
     * @generated
     * @ordered
     */
    protected EList<ThemeType> theme;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ThemesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.THEMES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ThemeType> getTheme() {
        if (theme == null) {
            theme = new EObjectContainmentEList<ThemeType>(ThemeType.class, this, wmtsv_1Package.THEMES_TYPE__THEME);
        }
        return theme;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.THEMES_TYPE__THEME:
                return ((InternalEList<?>)getTheme()).basicRemove(otherEnd, msgs);
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
            case wmtsv_1Package.THEMES_TYPE__THEME:
                return getTheme();
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
            case wmtsv_1Package.THEMES_TYPE__THEME:
                getTheme().clear();
                getTheme().addAll((Collection<? extends ThemeType>)newValue);
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
            case wmtsv_1Package.THEMES_TYPE__THEME:
                getTheme().clear();
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
            case wmtsv_1Package.THEMES_TYPE__THEME:
                return theme != null && !theme.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ThemesTypeImpl
