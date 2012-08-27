/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import javax.xml.datatype.Duration;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.HarvestType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Harvest Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.HarvestTypeImpl#getSource <em>Source</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.HarvestTypeImpl#getResourceType <em>Resource Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.HarvestTypeImpl#getResourceFormat <em>Resource Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.HarvestTypeImpl#getHarvestInterval <em>Harvest Interval</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.HarvestTypeImpl#getResponseHandler <em>Response Handler</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HarvestTypeImpl extends RequestBaseTypeImpl implements HarvestType {
    /**
     * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected String source = SOURCE_EDEFAULT;

    /**
     * The default value of the '{@link #getResourceType() <em>Resource Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceType()
     * @generated
     * @ordered
     */
    protected static final String RESOURCE_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getResourceType() <em>Resource Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceType()
     * @generated
     * @ordered
     */
    protected String resourceType = RESOURCE_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getResourceFormat() <em>Resource Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceFormat()
     * @generated
     * @ordered
     */
    protected static final String RESOURCE_FORMAT_EDEFAULT = "application/xml";

    /**
     * The cached value of the '{@link #getResourceFormat() <em>Resource Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceFormat()
     * @generated
     * @ordered
     */
    protected String resourceFormat = RESOURCE_FORMAT_EDEFAULT;

    /**
     * This is true if the Resource Format attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean resourceFormatESet;

    /**
     * The default value of the '{@link #getHarvestInterval() <em>Harvest Interval</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHarvestInterval()
     * @generated
     * @ordered
     */
    protected static final Duration HARVEST_INTERVAL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHarvestInterval() <em>Harvest Interval</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHarvestInterval()
     * @generated
     * @ordered
     */
    protected Duration harvestInterval = HARVEST_INTERVAL_EDEFAULT;

    /**
     * The default value of the '{@link #getResponseHandler() <em>Response Handler</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResponseHandler()
     * @generated
     * @ordered
     */
    protected static final String RESPONSE_HANDLER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getResponseHandler() <em>Response Handler</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResponseHandler()
     * @generated
     * @ordered
     */
    protected String responseHandler = RESPONSE_HANDLER_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected HarvestTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.HARVEST_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSource() {
        return source;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSource(String newSource) {
        String oldSource = source;
        source = newSource;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.HARVEST_TYPE__SOURCE, oldSource, source));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResourceType(String newResourceType) {
        String oldResourceType = resourceType;
        resourceType = newResourceType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.HARVEST_TYPE__RESOURCE_TYPE, oldResourceType, resourceType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getResourceFormat() {
        return resourceFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResourceFormat(String newResourceFormat) {
        String oldResourceFormat = resourceFormat;
        resourceFormat = newResourceFormat;
        boolean oldResourceFormatESet = resourceFormatESet;
        resourceFormatESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.HARVEST_TYPE__RESOURCE_FORMAT, oldResourceFormat, resourceFormat, !oldResourceFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetResourceFormat() {
        String oldResourceFormat = resourceFormat;
        boolean oldResourceFormatESet = resourceFormatESet;
        resourceFormat = RESOURCE_FORMAT_EDEFAULT;
        resourceFormatESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.HARVEST_TYPE__RESOURCE_FORMAT, oldResourceFormat, RESOURCE_FORMAT_EDEFAULT, oldResourceFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetResourceFormat() {
        return resourceFormatESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Duration getHarvestInterval() {
        return harvestInterval;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHarvestInterval(Duration newHarvestInterval) {
        Duration oldHarvestInterval = harvestInterval;
        harvestInterval = newHarvestInterval;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.HARVEST_TYPE__HARVEST_INTERVAL, oldHarvestInterval, harvestInterval));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getResponseHandler() {
        return responseHandler;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResponseHandler(String newResponseHandler) {
        String oldResponseHandler = responseHandler;
        responseHandler = newResponseHandler;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.HARVEST_TYPE__RESPONSE_HANDLER, oldResponseHandler, responseHandler));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.HARVEST_TYPE__SOURCE:
                return getSource();
            case Csw20Package.HARVEST_TYPE__RESOURCE_TYPE:
                return getResourceType();
            case Csw20Package.HARVEST_TYPE__RESOURCE_FORMAT:
                return getResourceFormat();
            case Csw20Package.HARVEST_TYPE__HARVEST_INTERVAL:
                return getHarvestInterval();
            case Csw20Package.HARVEST_TYPE__RESPONSE_HANDLER:
                return getResponseHandler();
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
            case Csw20Package.HARVEST_TYPE__SOURCE:
                setSource((String)newValue);
                return;
            case Csw20Package.HARVEST_TYPE__RESOURCE_TYPE:
                setResourceType((String)newValue);
                return;
            case Csw20Package.HARVEST_TYPE__RESOURCE_FORMAT:
                setResourceFormat((String)newValue);
                return;
            case Csw20Package.HARVEST_TYPE__HARVEST_INTERVAL:
                setHarvestInterval((Duration) newValue);
                return;
            case Csw20Package.HARVEST_TYPE__RESPONSE_HANDLER:
                setResponseHandler((String)newValue);
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
            case Csw20Package.HARVEST_TYPE__SOURCE:
                setSource(SOURCE_EDEFAULT);
                return;
            case Csw20Package.HARVEST_TYPE__RESOURCE_TYPE:
                setResourceType(RESOURCE_TYPE_EDEFAULT);
                return;
            case Csw20Package.HARVEST_TYPE__RESOURCE_FORMAT:
                unsetResourceFormat();
                return;
            case Csw20Package.HARVEST_TYPE__HARVEST_INTERVAL:
                setHarvestInterval(HARVEST_INTERVAL_EDEFAULT);
                return;
            case Csw20Package.HARVEST_TYPE__RESPONSE_HANDLER:
                setResponseHandler(RESPONSE_HANDLER_EDEFAULT);
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
            case Csw20Package.HARVEST_TYPE__SOURCE:
                return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
            case Csw20Package.HARVEST_TYPE__RESOURCE_TYPE:
                return RESOURCE_TYPE_EDEFAULT == null ? resourceType != null : !RESOURCE_TYPE_EDEFAULT.equals(resourceType);
            case Csw20Package.HARVEST_TYPE__RESOURCE_FORMAT:
                return isSetResourceFormat();
            case Csw20Package.HARVEST_TYPE__HARVEST_INTERVAL:
                return HARVEST_INTERVAL_EDEFAULT == null ? harvestInterval != null : !HARVEST_INTERVAL_EDEFAULT.equals(harvestInterval);
            case Csw20Package.HARVEST_TYPE__RESPONSE_HANDLER:
                return RESPONSE_HANDLER_EDEFAULT == null ? responseHandler != null : !RESPONSE_HANDLER_EDEFAULT.equals(responseHandler);
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
        result.append(" (source: ");
        result.append(source);
        result.append(", resourceType: ");
        result.append(resourceType);
        result.append(", resourceFormat: ");
        if (resourceFormatESet) result.append(resourceFormat); else result.append("<unset>");
        result.append(", harvestInterval: ");
        result.append(harvestInterval);
        result.append(", responseHandler: ");
        result.append(responseHandler);
        result.append(')');
        return result.toString();
    }

} //HarvestTypeImpl
