/**
 */
package net.opengis.wmts.v_1.impl;

import java.util.Collection;

import net.opengis.ows11.CodeType;

import net.opengis.ows11.impl.DescriptionTypeImpl;

import net.opengis.wmts.v_1.LegendURLType;
import net.opengis.wmts.v_1.StyleType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Style Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.StyleTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.StyleTypeImpl#getLegendURL <em>Legend URL</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.StyleTypeImpl#isIsDefault <em>Is Default</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StyleTypeImpl extends DescriptionTypeImpl implements StyleType {
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
     * The cached value of the '{@link #getLegendURL() <em>Legend URL</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLegendURL()
     * @generated
     * @ordered
     */
    protected EList<LegendURLType> legendURL;

    /**
     * The default value of the '{@link #isIsDefault() <em>Is Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDefault()
     * @generated
     * @ordered
     */
    protected static final boolean IS_DEFAULT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsDefault() <em>Is Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDefault()
     * @generated
     * @ordered
     */
    protected boolean isDefault = IS_DEFAULT_EDEFAULT;

    /**
     * This is true if the Is Default attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean isDefaultESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected StyleTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.STYLE_TYPE;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.STYLE_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
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
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.STYLE_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.STYLE_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.STYLE_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<LegendURLType> getLegendURL() {
        if (legendURL == null) {
            legendURL = new EObjectContainmentEList<LegendURLType>(LegendURLType.class, this, wmtsv_1Package.STYLE_TYPE__LEGEND_URL);
        }
        return legendURL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isIsDefault() {
        return isDefault;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsDefault(boolean newIsDefault) {
        boolean oldIsDefault = isDefault;
        isDefault = newIsDefault;
        boolean oldIsDefaultESet = isDefaultESet;
        isDefaultESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.STYLE_TYPE__IS_DEFAULT, oldIsDefault, isDefault, !oldIsDefaultESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetIsDefault() {
        boolean oldIsDefault = isDefault;
        boolean oldIsDefaultESet = isDefaultESet;
        isDefault = IS_DEFAULT_EDEFAULT;
        isDefaultESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.STYLE_TYPE__IS_DEFAULT, oldIsDefault, IS_DEFAULT_EDEFAULT, oldIsDefaultESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetIsDefault() {
        return isDefaultESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.STYLE_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case wmtsv_1Package.STYLE_TYPE__LEGEND_URL:
                return ((InternalEList<?>)getLegendURL()).basicRemove(otherEnd, msgs);
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
            case wmtsv_1Package.STYLE_TYPE__IDENTIFIER:
                return getIdentifier();
            case wmtsv_1Package.STYLE_TYPE__LEGEND_URL:
                return getLegendURL();
            case wmtsv_1Package.STYLE_TYPE__IS_DEFAULT:
                return isIsDefault();
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
            case wmtsv_1Package.STYLE_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case wmtsv_1Package.STYLE_TYPE__LEGEND_URL:
                getLegendURL().clear();
                getLegendURL().addAll((Collection<? extends LegendURLType>)newValue);
                return;
            case wmtsv_1Package.STYLE_TYPE__IS_DEFAULT:
                setIsDefault((Boolean)newValue);
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
            case wmtsv_1Package.STYLE_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case wmtsv_1Package.STYLE_TYPE__LEGEND_URL:
                getLegendURL().clear();
                return;
            case wmtsv_1Package.STYLE_TYPE__IS_DEFAULT:
                unsetIsDefault();
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
            case wmtsv_1Package.STYLE_TYPE__IDENTIFIER:
                return identifier != null;
            case wmtsv_1Package.STYLE_TYPE__LEGEND_URL:
                return legendURL != null && !legendURL.isEmpty();
            case wmtsv_1Package.STYLE_TYPE__IS_DEFAULT:
                return isSetIsDefault();
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
        result.append(" (isDefault: ");
        if (isDefaultESet) result.append(isDefault); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //StyleTypeImpl
