/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import net.opengis.ows10.impl.CapabilitiesBaseTypeImpl;

import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.GMLObjectTypeListType;
import net.opengis.wfs.WFSCapabilitiesType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.opengis.filter.capability.FilterCapabilities;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>WFS Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.WFSCapabilitiesTypeImpl#getFeatureTypeList <em>Feature Type List</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.WFSCapabilitiesTypeImpl#getServesGMLObjectTypeList <em>Serves GML Object Type List</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.WFSCapabilitiesTypeImpl#getSupportsGMLObjectTypeList <em>Supports GML Object Type List</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.WFSCapabilitiesTypeImpl#getFilterCapabilities <em>Filter Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WFSCapabilitiesTypeImpl extends CapabilitiesBaseTypeImpl implements WFSCapabilitiesType {
	/**
     * The cached value of the '{@link #getFeatureTypeList() <em>Feature Type List</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFeatureTypeList()
     * @generated
     * @ordered
     */
	protected FeatureTypeListType featureTypeList;

	/**
     * The cached value of the '{@link #getServesGMLObjectTypeList() <em>Serves GML Object Type List</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getServesGMLObjectTypeList()
     * @generated
     * @ordered
     */
	protected GMLObjectTypeListType servesGMLObjectTypeList;

	/**
     * The cached value of the '{@link #getSupportsGMLObjectTypeList() <em>Supports GML Object Type List</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getSupportsGMLObjectTypeList()
     * @generated
     * @ordered
     */
	protected GMLObjectTypeListType supportsGMLObjectTypeList;

	/**
     * The default value of the '{@link #getFilterCapabilities() <em>Filter Capabilities</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFilterCapabilities()
     * @generated
     * @ordered
     */
	protected static final FilterCapabilities FILTER_CAPABILITIES_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getFilterCapabilities() <em>Filter Capabilities</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFilterCapabilities()
     * @generated
     * @ordered
     */
	protected FilterCapabilities filterCapabilities = FILTER_CAPABILITIES_EDEFAULT;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected WFSCapabilitiesTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.WFS_CAPABILITIES_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeatureTypeListType getFeatureTypeList() {
        return featureTypeList;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetFeatureTypeList(FeatureTypeListType newFeatureTypeList, NotificationChain msgs) {
        FeatureTypeListType oldFeatureTypeList = featureTypeList;
        featureTypeList = newFeatureTypeList;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST, oldFeatureTypeList, newFeatureTypeList);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setFeatureTypeList(FeatureTypeListType newFeatureTypeList) {
        if (newFeatureTypeList != featureTypeList) {
            NotificationChain msgs = null;
            if (featureTypeList != null)
                msgs = ((InternalEObject)featureTypeList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST, null, msgs);
            if (newFeatureTypeList != null)
                msgs = ((InternalEObject)newFeatureTypeList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST, null, msgs);
            msgs = basicSetFeatureTypeList(newFeatureTypeList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST, newFeatureTypeList, newFeatureTypeList));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GMLObjectTypeListType getServesGMLObjectTypeList() {
        return servesGMLObjectTypeList;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetServesGMLObjectTypeList(GMLObjectTypeListType newServesGMLObjectTypeList, NotificationChain msgs) {
        GMLObjectTypeListType oldServesGMLObjectTypeList = servesGMLObjectTypeList;
        servesGMLObjectTypeList = newServesGMLObjectTypeList;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST, oldServesGMLObjectTypeList, newServesGMLObjectTypeList);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setServesGMLObjectTypeList(GMLObjectTypeListType newServesGMLObjectTypeList) {
        if (newServesGMLObjectTypeList != servesGMLObjectTypeList) {
            NotificationChain msgs = null;
            if (servesGMLObjectTypeList != null)
                msgs = ((InternalEObject)servesGMLObjectTypeList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST, null, msgs);
            if (newServesGMLObjectTypeList != null)
                msgs = ((InternalEObject)newServesGMLObjectTypeList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST, null, msgs);
            msgs = basicSetServesGMLObjectTypeList(newServesGMLObjectTypeList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST, newServesGMLObjectTypeList, newServesGMLObjectTypeList));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GMLObjectTypeListType getSupportsGMLObjectTypeList() {
        return supportsGMLObjectTypeList;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetSupportsGMLObjectTypeList(GMLObjectTypeListType newSupportsGMLObjectTypeList, NotificationChain msgs) {
        GMLObjectTypeListType oldSupportsGMLObjectTypeList = supportsGMLObjectTypeList;
        supportsGMLObjectTypeList = newSupportsGMLObjectTypeList;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST, oldSupportsGMLObjectTypeList, newSupportsGMLObjectTypeList);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setSupportsGMLObjectTypeList(GMLObjectTypeListType newSupportsGMLObjectTypeList) {
        if (newSupportsGMLObjectTypeList != supportsGMLObjectTypeList) {
            NotificationChain msgs = null;
            if (supportsGMLObjectTypeList != null)
                msgs = ((InternalEObject)supportsGMLObjectTypeList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST, null, msgs);
            if (newSupportsGMLObjectTypeList != null)
                msgs = ((InternalEObject)newSupportsGMLObjectTypeList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST, null, msgs);
            msgs = basicSetSupportsGMLObjectTypeList(newSupportsGMLObjectTypeList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST, newSupportsGMLObjectTypeList, newSupportsGMLObjectTypeList));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FilterCapabilities getFilterCapabilities() {
        return filterCapabilities;
    }

	/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilterCapabilities(FilterCapabilities newFilterCapabilities) {
        FilterCapabilities oldFilterCapabilities = filterCapabilities;
        filterCapabilities = newFilterCapabilities;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES, oldFilterCapabilities, filterCapabilities));
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                return basicSetFeatureTypeList(null, msgs);
            case WfsPackage.WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST:
                return basicSetServesGMLObjectTypeList(null, msgs);
            case WfsPackage.WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST:
                return basicSetSupportsGMLObjectTypeList(null, msgs);
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
            case WfsPackage.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                return getFeatureTypeList();
            case WfsPackage.WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST:
                return getServesGMLObjectTypeList();
            case WfsPackage.WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST:
                return getSupportsGMLObjectTypeList();
            case WfsPackage.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                return getFilterCapabilities();
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
            case WfsPackage.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                setFeatureTypeList((FeatureTypeListType)newValue);
                return;
            case WfsPackage.WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST:
                setServesGMLObjectTypeList((GMLObjectTypeListType)newValue);
                return;
            case WfsPackage.WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST:
                setSupportsGMLObjectTypeList((GMLObjectTypeListType)newValue);
                return;
            case WfsPackage.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                setFilterCapabilities((FilterCapabilities)newValue);
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
            case WfsPackage.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                setFeatureTypeList((FeatureTypeListType)null);
                return;
            case WfsPackage.WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST:
                setServesGMLObjectTypeList((GMLObjectTypeListType)null);
                return;
            case WfsPackage.WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST:
                setSupportsGMLObjectTypeList((GMLObjectTypeListType)null);
                return;
            case WfsPackage.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                setFilterCapabilities(FILTER_CAPABILITIES_EDEFAULT);
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
            case WfsPackage.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                return featureTypeList != null;
            case WfsPackage.WFS_CAPABILITIES_TYPE__SERVES_GML_OBJECT_TYPE_LIST:
                return servesGMLObjectTypeList != null;
            case WfsPackage.WFS_CAPABILITIES_TYPE__SUPPORTS_GML_OBJECT_TYPE_LIST:
                return supportsGMLObjectTypeList != null;
            case WfsPackage.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                return FILTER_CAPABILITIES_EDEFAULT == null ? filterCapabilities != null : !FILTER_CAPABILITIES_EDEFAULT.equals(filterCapabilities);
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
        result.append(" (filterCapabilities: ");
        result.append(filterCapabilities);
        result.append(')');
        return result.toString();
    }

} //WFSCapabilitiesTypeImpl