/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.net.URI;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.wfs.PropertyType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.opengis.filter.Filter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Update Element Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.UpdateElementTypeImpl#getProperty <em>Property</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.UpdateElementTypeImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.UpdateElementTypeImpl#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.UpdateElementTypeImpl#getInputFormat <em>Input Format</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.UpdateElementTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.UpdateElementTypeImpl#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UpdateElementTypeImpl extends EObjectImpl implements UpdateElementType {
	/**
     * The cached value of the '{@link #getProperty() <em>Property</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getProperty()
     * @generated
     * @ordered
     */
	protected EList property;

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
     * The default value of the '{@link #getInputFormat() <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getInputFormat()
     * @generated
     * @ordered
     */
	protected static final String INPUT_FORMAT_EDEFAULT = "x-application/gml:3";

	/**
     * The cached value of the '{@link #getInputFormat() <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getInputFormat()
     * @generated
     * @ordered
     */
	protected String inputFormat = INPUT_FORMAT_EDEFAULT;

	/**
     * This is true if the Input Format attribute has been set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	protected boolean inputFormatESet;

	/**
     * The default value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
	protected static final URI SRS_NAME_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
	protected URI srsName = SRS_NAME_EDEFAULT;

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
	protected UpdateElementTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.UPDATE_ELEMENT_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getProperty() {
        if (property == null) {
            property = new EObjectContainmentEList(PropertyType.class, this, WfsPackage.UPDATE_ELEMENT_TYPE__PROPERTY);
        }
        return property;
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.UPDATE_ELEMENT_TYPE__FILTER, oldFilter, filter));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.UPDATE_ELEMENT_TYPE__HANDLE, oldHandle, handle));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getInputFormat() {
        return inputFormat;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setInputFormat(String newInputFormat) {
        String oldInputFormat = inputFormat;
        inputFormat = newInputFormat;
        boolean oldInputFormatESet = inputFormatESet;
        inputFormatESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT, oldInputFormat, inputFormat, !oldInputFormatESet));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void unsetInputFormat() {
        String oldInputFormat = inputFormat;
        boolean oldInputFormatESet = inputFormatESet;
        inputFormat = INPUT_FORMAT_EDEFAULT;
        inputFormatESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT, oldInputFormat, INPUT_FORMAT_EDEFAULT, oldInputFormatESet));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean isSetInputFormat() {
        return inputFormatESet;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public URI getSrsName() {
        return srsName;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setSrsName(URI newSrsName) {
        URI oldSrsName = srsName;
        srsName = newSrsName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.UPDATE_ELEMENT_TYPE__SRS_NAME, oldSrsName, srsName));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME, oldTypeName, typeName));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
                return ((InternalEList)getProperty()).basicRemove(otherEnd, msgs);
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
            case WfsPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
                return getProperty();
            case WfsPackage.UPDATE_ELEMENT_TYPE__FILTER:
                return getFilter();
            case WfsPackage.UPDATE_ELEMENT_TYPE__HANDLE:
                return getHandle();
            case WfsPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT:
                return getInputFormat();
            case WfsPackage.UPDATE_ELEMENT_TYPE__SRS_NAME:
                return getSrsName();
            case WfsPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME:
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
            case WfsPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
                getProperty().clear();
                getProperty().addAll((Collection)newValue);
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__FILTER:
                setFilter((Filter)newValue);
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__HANDLE:
                setHandle((String)newValue);
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT:
                setInputFormat((String)newValue);
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__SRS_NAME:
                setSrsName((URI)newValue);
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME:
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
            case WfsPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
                getProperty().clear();
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__FILTER:
                setFilter(FILTER_EDEFAULT);
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__HANDLE:
                setHandle(HANDLE_EDEFAULT);
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT:
                unsetInputFormat();
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__SRS_NAME:
                setSrsName(SRS_NAME_EDEFAULT);
                return;
            case WfsPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME:
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
            case WfsPackage.UPDATE_ELEMENT_TYPE__PROPERTY:
                return property != null && !property.isEmpty();
            case WfsPackage.UPDATE_ELEMENT_TYPE__FILTER:
                return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
            case WfsPackage.UPDATE_ELEMENT_TYPE__HANDLE:
                return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
            case WfsPackage.UPDATE_ELEMENT_TYPE__INPUT_FORMAT:
                return isSetInputFormat();
            case WfsPackage.UPDATE_ELEMENT_TYPE__SRS_NAME:
                return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
            case WfsPackage.UPDATE_ELEMENT_TYPE__TYPE_NAME:
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
        result.append(", inputFormat: ");
        if (inputFormatESet) result.append(inputFormat); else result.append("<unset>");
        result.append(", srsName: ");
        result.append(srsName);
        result.append(", typeName: ");
        result.append(typeName);
        result.append(')');
        return result.toString();
    }

} //UpdateElementTypeImpl