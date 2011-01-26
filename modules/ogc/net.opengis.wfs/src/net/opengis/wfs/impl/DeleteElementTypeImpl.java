/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import javax.xml.namespace.QName;

import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.opengis.filter.Filter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Delete Element Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.DeleteElementTypeImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DeleteElementTypeImpl#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DeleteElementTypeImpl#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DeleteElementTypeImpl extends EObjectImpl implements DeleteElementType {
	/**
     * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
	protected static final Filter FILTER_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
	protected Filter filter = FILTER_EDEFAULT;

	/**
     * The default value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
	protected static final String HANDLE_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
	protected String handle = HANDLE_EDEFAULT;

	/**
     * The default value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
	protected static final QName TYPE_NAME_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
	protected QName typeName = TYPE_NAME_EDEFAULT;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected DeleteElementTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.DELETE_ELEMENT_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Filter getFilter() {
        return filter;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setFilter(Filter newFilter) {
        Filter oldFilter = filter;
        filter = newFilter;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.DELETE_ELEMENT_TYPE__FILTER, oldFilter, filter));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getHandle() {
        return handle;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setHandle(String newHandle) {
        String oldHandle = handle;
        handle = newHandle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.DELETE_ELEMENT_TYPE__HANDLE, oldHandle, handle));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public QName getTypeName() {
        return typeName;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTypeName(QName newTypeName) {
        QName oldTypeName = typeName;
        typeName = newTypeName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.DELETE_ELEMENT_TYPE__TYPE_NAME, oldTypeName, typeName));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.DELETE_ELEMENT_TYPE__FILTER:
                return getFilter();
            case WfsPackage.DELETE_ELEMENT_TYPE__HANDLE:
                return getHandle();
            case WfsPackage.DELETE_ELEMENT_TYPE__TYPE_NAME:
                return getTypeName();
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
            case WfsPackage.DELETE_ELEMENT_TYPE__FILTER:
                setFilter((Filter)newValue);
                return;
            case WfsPackage.DELETE_ELEMENT_TYPE__HANDLE:
                setHandle((String)newValue);
                return;
            case WfsPackage.DELETE_ELEMENT_TYPE__TYPE_NAME:
                setTypeName((QName)newValue);
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
            case WfsPackage.DELETE_ELEMENT_TYPE__FILTER:
                setFilter(FILTER_EDEFAULT);
                return;
            case WfsPackage.DELETE_ELEMENT_TYPE__HANDLE:
                setHandle(HANDLE_EDEFAULT);
                return;
            case WfsPackage.DELETE_ELEMENT_TYPE__TYPE_NAME:
                setTypeName(TYPE_NAME_EDEFAULT);
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
            case WfsPackage.DELETE_ELEMENT_TYPE__FILTER:
                return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
            case WfsPackage.DELETE_ELEMENT_TYPE__HANDLE:
                return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
            case WfsPackage.DELETE_ELEMENT_TYPE__TYPE_NAME:
                return TYPE_NAME_EDEFAULT == null ? typeName != null : !TYPE_NAME_EDEFAULT.equals(typeName);
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
        result.append(" (filter: ");
        result.append(filter);
        result.append(", handle: ");
        result.append(handle);
        result.append(", typeName: ");
        result.append(typeName);
        result.append(')');
        return result.toString();
    }

} //DeleteElementTypeImpl