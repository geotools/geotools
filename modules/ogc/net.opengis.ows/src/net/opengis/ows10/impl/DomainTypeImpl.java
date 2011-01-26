/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import java.util.Collection;

import net.opengis.ows10.DomainType;
import net.opengis.ows10.MetadataType;
import net.opengis.ows10.Ows10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows10.impl.DomainTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.DomainTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows10.impl.DomainTypeImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DomainTypeImpl extends EObjectImpl implements DomainType {
	/**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
	protected EList value;

	/**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
	protected EList metadata;

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
	protected DomainTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return Ows10Package.eINSTANCE.getDomainType();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getValue() {
        if (value == null) {
            value = new EDataTypeEList(String.class, this, Ows10Package.DOMAIN_TYPE__VALUE);
        }
        return value;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getMetadata() {
        if (metadata == null) {
            metadata = new EObjectContainmentEList(MetadataType.class, this, Ows10Package.DOMAIN_TYPE__METADATA);
        }
        return metadata;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows10Package.DOMAIN_TYPE__NAME, oldName, name));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows10Package.DOMAIN_TYPE__METADATA:
                return ((InternalEList)getMetadata()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows10Package.DOMAIN_TYPE__VALUE:
                return getValue();
            case Ows10Package.DOMAIN_TYPE__METADATA:
                return getMetadata();
            case Ows10Package.DOMAIN_TYPE__NAME:
                return getName();
        }
        return super.eGet(featureID, resolve, coreType);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Ows10Package.DOMAIN_TYPE__VALUE:
                getValue().clear();
                getValue().addAll((Collection)newValue);
                return;
            case Ows10Package.DOMAIN_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection)newValue);
                return;
            case Ows10Package.DOMAIN_TYPE__NAME:
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
	public void eUnset(int featureID) {
        switch (featureID) {
            case Ows10Package.DOMAIN_TYPE__VALUE:
                getValue().clear();
                return;
            case Ows10Package.DOMAIN_TYPE__METADATA:
                getMetadata().clear();
                return;
            case Ows10Package.DOMAIN_TYPE__NAME:
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
	public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Ows10Package.DOMAIN_TYPE__VALUE:
                return value != null && !value.isEmpty();
            case Ows10Package.DOMAIN_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
            case Ows10Package.DOMAIN_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
        }
        return super.eIsSet(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (value: ");
        result.append(value);
        result.append(", name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }

} //DomainTypeImpl
