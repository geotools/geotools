/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import javax.xml.namespace.QName;

import net.opengis.fes20.ArgumentsType;
import net.opengis.fes20.AvailableFunctionType;
import net.opengis.fes20.Fes20Package;

import net.opengis.ows11.MetadataType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Available Function Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.AvailableFunctionTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.AvailableFunctionTypeImpl#getReturns <em>Returns</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.AvailableFunctionTypeImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link net.opengis.fes20.impl.AvailableFunctionTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AvailableFunctionTypeImpl extends EObjectImpl implements AvailableFunctionType {
    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected MetadataType metadata;

    /**
     * The default value of the '{@link #getReturns() <em>Returns</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReturns()
     * @generated
     * @ordered
     */
    protected static final QName RETURNS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReturns() <em>Returns</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReturns()
     * @generated
     * @ordered
     */
    protected QName returns = RETURNS_EDEFAULT;

    /**
     * The cached value of the '{@link #getArguments() <em>Arguments</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getArguments()
     * @generated
     * @ordered
     */
    protected ArgumentsType arguments;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AvailableFunctionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.AVAILABLE_FUNCTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MetadataType getMetadata() {
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMetadata(MetadataType newMetadata, NotificationChain msgs) {
        MetadataType oldMetadata = metadata;
        metadata = newMetadata;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.AVAILABLE_FUNCTION_TYPE__METADATA, oldMetadata, newMetadata);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMetadata(MetadataType newMetadata) {
        if (newMetadata != metadata) {
            NotificationChain msgs = null;
            if (metadata != null)
                msgs = ((InternalEObject)metadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.AVAILABLE_FUNCTION_TYPE__METADATA, null, msgs);
            if (newMetadata != null)
                msgs = ((InternalEObject)newMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.AVAILABLE_FUNCTION_TYPE__METADATA, null, msgs);
            msgs = basicSetMetadata(newMetadata, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.AVAILABLE_FUNCTION_TYPE__METADATA, newMetadata, newMetadata));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getReturns() {
        return returns;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReturns(QName newReturns) {
        QName oldReturns = returns;
        returns = newReturns;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.AVAILABLE_FUNCTION_TYPE__RETURNS, oldReturns, returns));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ArgumentsType getArguments() {
        return arguments;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetArguments(ArgumentsType newArguments, NotificationChain msgs) {
        ArgumentsType oldArguments = arguments;
        arguments = newArguments;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Fes20Package.AVAILABLE_FUNCTION_TYPE__ARGUMENTS, oldArguments, newArguments);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setArguments(ArgumentsType newArguments) {
        if (newArguments != arguments) {
            NotificationChain msgs = null;
            if (arguments != null)
                msgs = ((InternalEObject)arguments).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Fes20Package.AVAILABLE_FUNCTION_TYPE__ARGUMENTS, null, msgs);
            if (newArguments != null)
                msgs = ((InternalEObject)newArguments).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Fes20Package.AVAILABLE_FUNCTION_TYPE__ARGUMENTS, null, msgs);
            msgs = basicSetArguments(newArguments, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.AVAILABLE_FUNCTION_TYPE__ARGUMENTS, newArguments, newArguments));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
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
            eNotify(new ENotificationImpl(this, Notification.SET, Fes20Package.AVAILABLE_FUNCTION_TYPE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__METADATA:
                return basicSetMetadata(null, msgs);
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__ARGUMENTS:
                return basicSetArguments(null, msgs);
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
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__METADATA:
                return getMetadata();
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__RETURNS:
                return getReturns();
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__ARGUMENTS:
                return getArguments();
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__NAME:
                return getName();
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
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__METADATA:
                setMetadata((MetadataType)newValue);
                return;
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__RETURNS:
                setReturns((QName)newValue);
                return;
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__ARGUMENTS:
                setArguments((ArgumentsType)newValue);
                return;
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__NAME:
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
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__METADATA:
                setMetadata((MetadataType)null);
                return;
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__RETURNS:
                setReturns(RETURNS_EDEFAULT);
                return;
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__ARGUMENTS:
                setArguments((ArgumentsType)null);
                return;
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__NAME:
                setName(NAME_EDEFAULT);
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
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__METADATA:
                return metadata != null;
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__RETURNS:
                return RETURNS_EDEFAULT == null ? returns != null : !RETURNS_EDEFAULT.equals(returns);
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__ARGUMENTS:
                return arguments != null;
            case Fes20Package.AVAILABLE_FUNCTION_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
        result.append(" (returns: ");
        result.append(returns);
        result.append(", name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }

} //AvailableFunctionTypeImpl
