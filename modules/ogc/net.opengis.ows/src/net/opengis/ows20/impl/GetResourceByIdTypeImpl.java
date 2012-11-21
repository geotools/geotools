/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.GetResourceByIdType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Resource By Id Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.GetResourceByIdTypeImpl#getResourceID <em>Resource ID</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.GetResourceByIdTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.GetResourceByIdTypeImpl#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.GetResourceByIdTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetResourceByIdTypeImpl extends EObjectImpl implements GetResourceByIdType {
    /**
     * The default value of the '{@link #getResourceID() <em>Resource ID</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceID()
     * @generated
     * @ordered
     */
    protected static final String RESOURCE_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getResourceID() <em>Resource ID</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceID()
     * @generated
     * @ordered
     */
    protected String resourceID = RESOURCE_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected String outputFormat = OUTPUT_FORMAT_EDEFAULT;

    /**
     * The default value of the '{@link #getService() <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getService()
     * @generated
     * @ordered
     */
    protected static final String SERVICE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getService() <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getService()
     * @generated
     * @ordered
     */
    protected String service = SERVICE_EDEFAULT;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetResourceByIdTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.GET_RESOURCE_BY_ID_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getResourceID() {
        return resourceID;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResourceID(String newResourceID) {
        String oldResourceID = resourceID;
        resourceID = newResourceID;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.GET_RESOURCE_BY_ID_TYPE__RESOURCE_ID, oldResourceID, resourceID));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputFormat(String newOutputFormat) {
        String oldOutputFormat = outputFormat;
        outputFormat = newOutputFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.GET_RESOURCE_BY_ID_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getService() {
        return service;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setService(String newService) {
        String oldService = service;
        service = newService;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.GET_RESOURCE_BY_ID_TYPE__SERVICE, oldService, service));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.GET_RESOURCE_BY_ID_TYPE__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__RESOURCE_ID:
                return getResourceID();
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__SERVICE:
                return getService();
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__VERSION:
                return getVersion();
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
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__RESOURCE_ID:
                setResourceID((String)newValue);
                return;
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__SERVICE:
                setService((String)newValue);
                return;
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__VERSION:
                setVersion((String)newValue);
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
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__RESOURCE_ID:
                setResourceID(RESOURCE_ID_EDEFAULT);
                return;
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__OUTPUT_FORMAT:
                setOutputFormat(OUTPUT_FORMAT_EDEFAULT);
                return;
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__SERVICE:
                setService(SERVICE_EDEFAULT);
                return;
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__VERSION:
                setVersion(VERSION_EDEFAULT);
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
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__RESOURCE_ID:
                return RESOURCE_ID_EDEFAULT == null ? resourceID != null : !RESOURCE_ID_EDEFAULT.equals(resourceID);
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__OUTPUT_FORMAT:
                return OUTPUT_FORMAT_EDEFAULT == null ? outputFormat != null : !OUTPUT_FORMAT_EDEFAULT.equals(outputFormat);
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__SERVICE:
                return SERVICE_EDEFAULT == null ? service != null : !SERVICE_EDEFAULT.equals(service);
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
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
        result.append(" (resourceID: ");
        result.append(resourceID);
        result.append(", outputFormat: ");
        result.append(outputFormat);
        result.append(", service: ");
        result.append(service);
        result.append(", version: ");
        result.append(version);
        result.append(')');
        return result.toString();
    }

} //GetResourceByIdTypeImpl
