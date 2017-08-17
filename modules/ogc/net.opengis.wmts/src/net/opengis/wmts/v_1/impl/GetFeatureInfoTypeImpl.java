/**
 */
package net.opengis.wmts.v_1.impl;

import java.math.BigInteger;

import net.opengis.wmts.v_1.GetFeatureInfoType;
import net.opengis.wmts.v_1.GetTileType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Feature Info Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl#getGetTile <em>Get Tile</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl#getJ <em>J</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl#getI <em>I</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl#getInfoFormat <em>Info Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetFeatureInfoTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GetFeatureInfoTypeImpl extends MinimalEObjectImpl.Container implements GetFeatureInfoType {
    /**
     * The cached value of the '{@link #getGetTile() <em>Get Tile</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGetTile()
     * @generated
     * @ordered
     */
    protected GetTileType getTile;

    /**
     * The default value of the '{@link #getJ() <em>J</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJ()
     * @generated
     * @ordered
     */
    protected static final BigInteger J_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getJ() <em>J</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJ()
     * @generated
     * @ordered
     */
    protected BigInteger j = J_EDEFAULT;

    /**
     * The default value of the '{@link #getI() <em>I</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getI()
     * @generated
     * @ordered
     */
    protected static final BigInteger I_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getI() <em>I</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getI()
     * @generated
     * @ordered
     */
    protected BigInteger i = I_EDEFAULT;

    /**
     * The default value of the '{@link #getInfoFormat() <em>Info Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInfoFormat()
     * @generated
     * @ordered
     */
    protected static final String INFO_FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getInfoFormat() <em>Info Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInfoFormat()
     * @generated
     * @ordered
     */
    protected String infoFormat = INFO_FORMAT_EDEFAULT;

    /**
     * The default value of the '{@link #getService() <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getService()
     * @generated
     * @ordered
     */
    protected static final String SERVICE_EDEFAULT = "WMTS";

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
     * This is true if the Service attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean serviceESet;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = "1.0.0";

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
     * This is true if the Version attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean versionESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetFeatureInfoTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.GET_FEATURE_INFO_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetTileType getGetTile() {
        return getTile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetTile(GetTileType newGetTile, NotificationChain msgs) {
        GetTileType oldGetTile = getTile;
        getTile = newGetTile;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_FEATURE_INFO_TYPE__GET_TILE, oldGetTile, newGetTile);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetTile(GetTileType newGetTile) {
        if (newGetTile != getTile) {
            NotificationChain msgs = null;
            if (getTile != null)
                msgs = ((InternalEObject)getTile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.GET_FEATURE_INFO_TYPE__GET_TILE, null, msgs);
            if (newGetTile != null)
                msgs = ((InternalEObject)newGetTile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.GET_FEATURE_INFO_TYPE__GET_TILE, null, msgs);
            msgs = basicSetGetTile(newGetTile, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_FEATURE_INFO_TYPE__GET_TILE, newGetTile, newGetTile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getJ() {
        return j;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setJ(BigInteger newJ) {
        BigInteger oldJ = j;
        j = newJ;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_FEATURE_INFO_TYPE__J, oldJ, j));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getI() {
        return i;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setI(BigInteger newI) {
        BigInteger oldI = i;
        i = newI;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_FEATURE_INFO_TYPE__I, oldI, i));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInfoFormat() {
        return infoFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInfoFormat(String newInfoFormat) {
        String oldInfoFormat = infoFormat;
        infoFormat = newInfoFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_FEATURE_INFO_TYPE__INFO_FORMAT, oldInfoFormat, infoFormat));
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
        boolean oldServiceESet = serviceESet;
        serviceESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_FEATURE_INFO_TYPE__SERVICE, oldService, service, !oldServiceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetService() {
        String oldService = service;
        boolean oldServiceESet = serviceESet;
        service = SERVICE_EDEFAULT;
        serviceESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.GET_FEATURE_INFO_TYPE__SERVICE, oldService, SERVICE_EDEFAULT, oldServiceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetService() {
        return serviceESet;
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
        boolean oldVersionESet = versionESet;
        versionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_FEATURE_INFO_TYPE__VERSION, oldVersion, version, !oldVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetVersion() {
        String oldVersion = version;
        boolean oldVersionESet = versionESet;
        version = VERSION_EDEFAULT;
        versionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.GET_FEATURE_INFO_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetVersion() {
        return versionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__GET_TILE:
                return basicSetGetTile(null, msgs);
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
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__GET_TILE:
                return getGetTile();
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__J:
                return getJ();
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__I:
                return getI();
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__INFO_FORMAT:
                return getInfoFormat();
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__SERVICE:
                return getService();
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__VERSION:
                return getVersion();
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
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__GET_TILE:
                setGetTile((GetTileType)newValue);
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__J:
                setJ((BigInteger)newValue);
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__I:
                setI((BigInteger)newValue);
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__INFO_FORMAT:
                setInfoFormat((String)newValue);
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__SERVICE:
                setService((String)newValue);
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__VERSION:
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
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__GET_TILE:
                setGetTile((GetTileType)null);
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__J:
                setJ(J_EDEFAULT);
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__I:
                setI(I_EDEFAULT);
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__INFO_FORMAT:
                setInfoFormat(INFO_FORMAT_EDEFAULT);
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__SERVICE:
                unsetService();
                return;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__VERSION:
                unsetVersion();
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
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__GET_TILE:
                return getTile != null;
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__J:
                return J_EDEFAULT == null ? j != null : !J_EDEFAULT.equals(j);
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__I:
                return I_EDEFAULT == null ? i != null : !I_EDEFAULT.equals(i);
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__INFO_FORMAT:
                return INFO_FORMAT_EDEFAULT == null ? infoFormat != null : !INFO_FORMAT_EDEFAULT.equals(infoFormat);
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__SERVICE:
                return isSetService();
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE__VERSION:
                return isSetVersion();
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
        result.append(" (j: ");
        result.append(j);
        result.append(", i: ");
        result.append(i);
        result.append(", infoFormat: ");
        result.append(infoFormat);
        result.append(", service: ");
        if (serviceESet) result.append(service); else result.append("<unset>");
        result.append(", version: ");
        if (versionESet) result.append(version); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //GetFeatureInfoTypeImpl
