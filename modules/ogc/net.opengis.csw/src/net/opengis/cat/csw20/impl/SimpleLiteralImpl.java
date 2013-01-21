/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.net.URI;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.SimpleLiteral;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Literal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.SimpleLiteralImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SimpleLiteralImpl#getScheme <em>Scheme</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SimpleLiteralImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SimpleLiteralImpl extends EObjectImpl implements SimpleLiteral {
    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final Object VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected Object value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getScheme() <em>Scheme</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScheme()
     * @generated
     * @ordered
     */
    protected static final URI SCHEME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getScheme() <em>Scheme</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScheme()
     * @generated
     * @ordered
     */
    protected URI scheme = SCHEME_EDEFAULT;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SimpleLiteralImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.SIMPLE_LITERAL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(Object newValue) {
        Object oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SIMPLE_LITERAL__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public URI getScheme() {
        return scheme;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScheme(URI newScheme) {
        URI oldScheme = scheme;
        scheme = newScheme;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SIMPLE_LITERAL__SCHEME, oldScheme, scheme));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String basicGetName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SIMPLE_LITERAL__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.SIMPLE_LITERAL__VALUE:
                return getValue();
            case Csw20Package.SIMPLE_LITERAL__SCHEME:
                return getScheme();
            case Csw20Package.SIMPLE_LITERAL__NAME:
                if (resolve) return getName();
                return basicGetName();
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
            case Csw20Package.SIMPLE_LITERAL__VALUE:
                setValue(newValue);
                return;
            case Csw20Package.SIMPLE_LITERAL__SCHEME:
                setScheme((URI)newValue);
                return;
            case Csw20Package.SIMPLE_LITERAL__NAME:
                setName((String)newValue);
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
            case Csw20Package.SIMPLE_LITERAL__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case Csw20Package.SIMPLE_LITERAL__SCHEME:
                setScheme(SCHEME_EDEFAULT);
                return;
            case Csw20Package.SIMPLE_LITERAL__NAME:
                setName((String)null);
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
            case Csw20Package.SIMPLE_LITERAL__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case Csw20Package.SIMPLE_LITERAL__SCHEME:
                return SCHEME_EDEFAULT == null ? scheme != null : !SCHEME_EDEFAULT.equals(scheme);
            case Csw20Package.SIMPLE_LITERAL__NAME:
                return name != null;
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
        result.append(" (value: ");
        result.append(value);
        result.append(", scheme: ");
        result.append(scheme);
        result.append(')');
        return result.toString();
    }

} //SimpleLiteralImpl
