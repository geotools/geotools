/**
 */
package net.opengis.wmts.v_1.impl;

import java.util.Collection;

import net.opengis.ows11.CodeType;

import net.opengis.ows11.impl.DescriptionTypeImpl;

import net.opengis.wmts.v_1.ThemeType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Theme Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.ThemeTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.ThemeTypeImpl#getTheme <em>Theme</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.ThemeTypeImpl#getLayerRef <em>Layer Ref</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ThemeTypeImpl extends DescriptionTypeImpl implements ThemeType {
    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected CodeType identifier;

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
     * The cached value of the '{@link #getLayerRef() <em>Layer Ref</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLayerRef()
     * @generated
     * @ordered
     */
    protected EList<String> layerRef;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ThemeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.THEME_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        CodeType oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.THEME_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(CodeType newIdentifier) {
        if (newIdentifier != identifier) {
            NotificationChain msgs = null;
            if (identifier != null)
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.THEME_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.THEME_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.THEME_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ThemeType> getTheme() {
        if (theme == null) {
            theme = new EObjectContainmentEList<ThemeType>(ThemeType.class, this, wmtsv_1Package.THEME_TYPE__THEME);
        }
        return theme;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getLayerRef() {
        if (layerRef == null) {
            layerRef = new EDataTypeEList<String>(String.class, this, wmtsv_1Package.THEME_TYPE__LAYER_REF);
        }
        return layerRef;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.THEME_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case wmtsv_1Package.THEME_TYPE__THEME:
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
            case wmtsv_1Package.THEME_TYPE__IDENTIFIER:
                return getIdentifier();
            case wmtsv_1Package.THEME_TYPE__THEME:
                return getTheme();
            case wmtsv_1Package.THEME_TYPE__LAYER_REF:
                return getLayerRef();
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
            case wmtsv_1Package.THEME_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case wmtsv_1Package.THEME_TYPE__THEME:
                getTheme().clear();
                getTheme().addAll((Collection<? extends ThemeType>)newValue);
                return;
            case wmtsv_1Package.THEME_TYPE__LAYER_REF:
                getLayerRef().clear();
                getLayerRef().addAll((Collection<? extends String>)newValue);
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
            case wmtsv_1Package.THEME_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case wmtsv_1Package.THEME_TYPE__THEME:
                getTheme().clear();
                return;
            case wmtsv_1Package.THEME_TYPE__LAYER_REF:
                getLayerRef().clear();
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
            case wmtsv_1Package.THEME_TYPE__IDENTIFIER:
                return identifier != null;
            case wmtsv_1Package.THEME_TYPE__THEME:
                return theme != null && !theme.isEmpty();
            case wmtsv_1Package.THEME_TYPE__LAYER_REF:
                return layerRef != null && !layerRef.isEmpty();
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
        result.append(" (layerRef: ");
        result.append(layerRef);
        result.append(')');
        return result.toString();
    }

} //ThemeTypeImpl
