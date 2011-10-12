/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import net.opengis.fes20.FilterCapabilitiesType;

import net.opengis.ows11.impl.CapabilitiesBaseTypeImpl;

import net.opengis.wfs20.FeatureTypeListType;
import net.opengis.wfs20.WFSCapabilitiesType;
import net.opengis.wfs20.WSDLType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>WFS Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.WFSCapabilitiesTypeImpl#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.WFSCapabilitiesTypeImpl#getFeatureTypeList <em>Feature Type List</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.WFSCapabilitiesTypeImpl#getFilterCapabilities <em>Filter Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WFSCapabilitiesTypeImpl extends CapabilitiesBaseTypeImpl implements WFSCapabilitiesType {
    /**
     * The cached value of the '{@link #getWSDL() <em>WSDL</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWSDL()
     * @generated
     * @ordered
     */
    protected WSDLType wSDL;

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
     * The cached value of the '{@link #getFilterCapabilities() <em>Filter Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilterCapabilities()
     * @generated
     * @ordered
     */
    protected FilterCapabilitiesType filterCapabilities;

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
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.WFS_CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WSDLType getWSDL() {
        return wSDL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWSDL(WSDLType newWSDL, NotificationChain msgs) {
        WSDLType oldWSDL = wSDL;
        wSDL = newWSDL;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.WFS_CAPABILITIES_TYPE__WSDL, oldWSDL, newWSDL);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWSDL(WSDLType newWSDL) {
        if (newWSDL != wSDL) {
            NotificationChain msgs = null;
            if (wSDL != null)
                msgs = ((InternalEObject)wSDL).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.WFS_CAPABILITIES_TYPE__WSDL, null, msgs);
            if (newWSDL != null)
                msgs = ((InternalEObject)newWSDL).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.WFS_CAPABILITIES_TYPE__WSDL, null, msgs);
            msgs = basicSetWSDL(newWSDL, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.WFS_CAPABILITIES_TYPE__WSDL, newWSDL, newWSDL));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST, oldFeatureTypeList, newFeatureTypeList);
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
                msgs = ((InternalEObject)featureTypeList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST, null, msgs);
            if (newFeatureTypeList != null)
                msgs = ((InternalEObject)newFeatureTypeList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST, null, msgs);
            msgs = basicSetFeatureTypeList(newFeatureTypeList, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST, newFeatureTypeList, newFeatureTypeList));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FilterCapabilitiesType getFilterCapabilities() {
        return filterCapabilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFilterCapabilities(FilterCapabilitiesType newFilterCapabilities, NotificationChain msgs) {
        FilterCapabilitiesType oldFilterCapabilities = filterCapabilities;
        filterCapabilities = newFilterCapabilities;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES, oldFilterCapabilities, newFilterCapabilities);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFilterCapabilities(FilterCapabilitiesType newFilterCapabilities) {
        if (newFilterCapabilities != filterCapabilities) {
            NotificationChain msgs = null;
            if (filterCapabilities != null)
                msgs = ((InternalEObject)filterCapabilities).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES, null, msgs);
            if (newFilterCapabilities != null)
                msgs = ((InternalEObject)newFilterCapabilities).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES, null, msgs);
            msgs = basicSetFilterCapabilities(newFilterCapabilities, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES, newFilterCapabilities, newFilterCapabilities));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.WFS_CAPABILITIES_TYPE__WSDL:
                return basicSetWSDL(null, msgs);
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                return basicSetFeatureTypeList(null, msgs);
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                return basicSetFilterCapabilities(null, msgs);
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
            case Wfs20Package.WFS_CAPABILITIES_TYPE__WSDL:
                return getWSDL();
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                return getFeatureTypeList();
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                return getFilterCapabilities();
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
            case Wfs20Package.WFS_CAPABILITIES_TYPE__WSDL:
                setWSDL((WSDLType)newValue);
                return;
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                setFeatureTypeList((FeatureTypeListType)newValue);
                return;
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                setFilterCapabilities((FilterCapabilitiesType)newValue);
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
            case Wfs20Package.WFS_CAPABILITIES_TYPE__WSDL:
                setWSDL((WSDLType)null);
                return;
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                setFeatureTypeList((FeatureTypeListType)null);
                return;
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                setFilterCapabilities((FilterCapabilitiesType)null);
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
            case Wfs20Package.WFS_CAPABILITIES_TYPE__WSDL:
                return wSDL != null;
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FEATURE_TYPE_LIST:
                return featureTypeList != null;
            case Wfs20Package.WFS_CAPABILITIES_TYPE__FILTER_CAPABILITIES:
                return filterCapabilities != null;
        }
        return super.eIsSet(featureID);
    }

} //WFSCapabilitiesTypeImpl
