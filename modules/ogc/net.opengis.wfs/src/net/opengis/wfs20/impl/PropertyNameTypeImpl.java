/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.math.BigInteger;

import javax.xml.namespace.QName;

import net.opengis.wfs20.PropertyNameType;
import net.opengis.wfs20.ResolveValueType;
import net.opengis.wfs20.Wfs20Factory;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property Name Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.PropertyNameTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.PropertyNameTypeImpl#getResolve <em>Resolve</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.PropertyNameTypeImpl#getResolveDepth <em>Resolve Depth</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.PropertyNameTypeImpl#getResolvePath <em>Resolve Path</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.PropertyNameTypeImpl#getResolveTimeout <em>Resolve Timeout</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PropertyNameTypeImpl extends EObjectImpl implements PropertyNameType {
    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final QName VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected QName value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getResolve() <em>Resolve</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolve()
     * @generated
     * @ordered
     */
    protected static final ResolveValueType RESOLVE_EDEFAULT = ResolveValueType.NONE;

    /**
     * The cached value of the '{@link #getResolve() <em>Resolve</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolve()
     * @generated
     * @ordered
     */
    protected ResolveValueType resolve = RESOLVE_EDEFAULT;

    /**
     * This is true if the Resolve attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean resolveESet;

    /**
     * The default value of the '{@link #getResolveDepth() <em>Resolve Depth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolveDepth()
     * @generated
     * @ordered
     */
    protected static final Object RESOLVE_DEPTH_EDEFAULT = Wfs20Factory.eINSTANCE.createFromString(Wfs20Package.eINSTANCE.getPositiveIntegerWithStar(), "*");

    /**
     * The cached value of the '{@link #getResolveDepth() <em>Resolve Depth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolveDepth()
     * @generated
     * @ordered
     */
    protected Object resolveDepth = RESOLVE_DEPTH_EDEFAULT;

    /**
     * This is true if the Resolve Depth attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean resolveDepthESet;

    /**
     * The default value of the '{@link #getResolvePath() <em>Resolve Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolvePath()
     * @generated
     * @ordered
     */
    protected static final String RESOLVE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getResolvePath() <em>Resolve Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolvePath()
     * @generated
     * @ordered
     */
    protected String resolvePath = RESOLVE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #getResolveTimeout() <em>Resolve Timeout</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolveTimeout()
     * @generated
     * @ordered
     */
    protected static final BigInteger RESOLVE_TIMEOUT_EDEFAULT = new BigInteger("300");

    /**
     * The cached value of the '{@link #getResolveTimeout() <em>Resolve Timeout</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolveTimeout()
     * @generated
     * @ordered
     */
    protected BigInteger resolveTimeout = RESOLVE_TIMEOUT_EDEFAULT;

    /**
     * This is true if the Resolve Timeout attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean resolveTimeoutESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PropertyNameTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.PROPERTY_NAME_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(QName newValue) {
        QName oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.PROPERTY_NAME_TYPE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResolveValueType getResolve() {
        return resolve;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResolve(ResolveValueType newResolve) {
        ResolveValueType oldResolve = resolve;
        resolve = newResolve == null ? RESOLVE_EDEFAULT : newResolve;
        boolean oldResolveESet = resolveESet;
        resolveESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE, oldResolve, resolve, !oldResolveESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetResolve() {
        ResolveValueType oldResolve = resolve;
        boolean oldResolveESet = resolveESet;
        resolve = RESOLVE_EDEFAULT;
        resolveESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE, oldResolve, RESOLVE_EDEFAULT, oldResolveESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetResolve() {
        return resolveESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getResolveDepth() {
        return resolveDepth;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResolveDepth(Object newResolveDepth) {
        Object oldResolveDepth = resolveDepth;
        resolveDepth = newResolveDepth;
        boolean oldResolveDepthESet = resolveDepthESet;
        resolveDepthESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_DEPTH, oldResolveDepth, resolveDepth, !oldResolveDepthESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetResolveDepth() {
        Object oldResolveDepth = resolveDepth;
        boolean oldResolveDepthESet = resolveDepthESet;
        resolveDepth = RESOLVE_DEPTH_EDEFAULT;
        resolveDepthESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_DEPTH, oldResolveDepth, RESOLVE_DEPTH_EDEFAULT, oldResolveDepthESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetResolveDepth() {
        return resolveDepthESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getResolvePath() {
        return resolvePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResolvePath(String newResolvePath) {
        String oldResolvePath = resolvePath;
        resolvePath = newResolvePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_PATH, oldResolvePath, resolvePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getResolveTimeout() {
        return resolveTimeout;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResolveTimeout(BigInteger newResolveTimeout) {
        BigInteger oldResolveTimeout = resolveTimeout;
        resolveTimeout = newResolveTimeout;
        boolean oldResolveTimeoutESet = resolveTimeoutESet;
        resolveTimeoutESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_TIMEOUT, oldResolveTimeout, resolveTimeout, !oldResolveTimeoutESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetResolveTimeout() {
        BigInteger oldResolveTimeout = resolveTimeout;
        boolean oldResolveTimeoutESet = resolveTimeoutESet;
        resolveTimeout = RESOLVE_TIMEOUT_EDEFAULT;
        resolveTimeoutESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_TIMEOUT, oldResolveTimeout, RESOLVE_TIMEOUT_EDEFAULT, oldResolveTimeoutESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetResolveTimeout() {
        return resolveTimeoutESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wfs20Package.PROPERTY_NAME_TYPE__VALUE:
                return getValue();
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE:
                return getResolve();
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_DEPTH:
                return getResolveDepth();
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_PATH:
                return getResolvePath();
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_TIMEOUT:
                return getResolveTimeout();
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
            case Wfs20Package.PROPERTY_NAME_TYPE__VALUE:
                setValue((QName)newValue);
                return;
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE:
                setResolve((ResolveValueType)newValue);
                return;
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_DEPTH:
                setResolveDepth(newValue);
                return;
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_PATH:
                setResolvePath((String)newValue);
                return;
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_TIMEOUT:
                setResolveTimeout((BigInteger)newValue);
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
            case Wfs20Package.PROPERTY_NAME_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE:
                unsetResolve();
                return;
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_DEPTH:
                unsetResolveDepth();
                return;
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_PATH:
                setResolvePath(RESOLVE_PATH_EDEFAULT);
                return;
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_TIMEOUT:
                unsetResolveTimeout();
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
            case Wfs20Package.PROPERTY_NAME_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE:
                return isSetResolve();
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_DEPTH:
                return isSetResolveDepth();
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_PATH:
                return RESOLVE_PATH_EDEFAULT == null ? resolvePath != null : !RESOLVE_PATH_EDEFAULT.equals(resolvePath);
            case Wfs20Package.PROPERTY_NAME_TYPE__RESOLVE_TIMEOUT:
                return isSetResolveTimeout();
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
        result.append(", resolve: ");
        if (resolveESet) result.append(resolve); else result.append("<unset>");
        result.append(", resolveDepth: ");
        if (resolveDepthESet) result.append(resolveDepth); else result.append("<unset>");
        result.append(", resolvePath: ");
        result.append(resolvePath);
        result.append(", resolveTimeout: ");
        if (resolveTimeoutESet) result.append(resolveTimeout); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //PropertyNameTypeImpl
