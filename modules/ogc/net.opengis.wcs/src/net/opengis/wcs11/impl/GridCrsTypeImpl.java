/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.wcs11.GridCrsType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grid Crs Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.GridCrsTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.GridCrsTypeImpl#getGridBaseCRS <em>Grid Base CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.GridCrsTypeImpl#getGridType <em>Grid Type</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.GridCrsTypeImpl#getGridOrigin <em>Grid Origin</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.GridCrsTypeImpl#getGridOffsets <em>Grid Offsets</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.GridCrsTypeImpl#getGridCS <em>Grid CS</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.GridCrsTypeImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GridCrsTypeImpl extends EObjectImpl implements GridCrsType {
    /**
     * The default value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected static final Object SRS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected Object srsName = SRS_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getGridBaseCRS() <em>Grid Base CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridBaseCRS()
     * @generated
     * @ordered
     */
    protected static final String GRID_BASE_CRS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGridBaseCRS() <em>Grid Base CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridBaseCRS()
     * @generated
     * @ordered
     */
    protected String gridBaseCRS = GRID_BASE_CRS_EDEFAULT;

    /**
     * The default value of the '{@link #getGridType() <em>Grid Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridType()
     * @generated
     * @ordered
     */
    protected static final String GRID_TYPE_EDEFAULT = "urn:ogc:def:method:WCS:1.1:2dSimpleGrid";

    /**
     * The cached value of the '{@link #getGridType() <em>Grid Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridType()
     * @generated
     * @ordered
     */
    protected String gridType = GRID_TYPE_EDEFAULT;

    /**
     * This is true if the Grid Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean gridTypeESet;

    /**
     * The default value of the '{@link #getGridOrigin() <em>Grid Origin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOrigin()
     * @generated
     * @ordered
     */
    protected static final Object GRID_ORIGIN_EDEFAULT = "0 0";

    /**
     * The cached value of the '{@link #getGridOrigin() <em>Grid Origin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOrigin()
     * @generated
     * @ordered
     */
    protected Object gridOrigin = GRID_ORIGIN_EDEFAULT;

    /**
     * This is true if the Grid Origin attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean gridOriginESet;

    /**
     * The default value of the '{@link #getGridOffsets() <em>Grid Offsets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOffsets()
     * @generated
     * @ordered
     */
    protected static final Object GRID_OFFSETS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGridOffsets() <em>Grid Offsets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridOffsets()
     * @generated
     * @ordered
     */
    protected Object gridOffsets = GRID_OFFSETS_EDEFAULT;

    /**
     * The default value of the '{@link #getGridCS() <em>Grid CS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridCS()
     * @generated
     * @ordered
     */
    protected static final String GRID_CS_EDEFAULT = "urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS";

    /**
     * The cached value of the '{@link #getGridCS() <em>Grid CS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridCS()
     * @generated
     * @ordered
     */
    protected String gridCS = GRID_CS_EDEFAULT;

    /**
     * This is true if the Grid CS attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean gridCSESet;

    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected static final Object ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected Object id = ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GridCrsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.GRID_CRS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getSrsName() {
        return srsName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSrsName(Object newSrsName) {
        Object oldSrsName = srsName;
        srsName = newSrsName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.GRID_CRS_TYPE__SRS_NAME, oldSrsName, srsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGridBaseCRS() {
        return gridBaseCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridBaseCRS(String newGridBaseCRS) {
        String oldGridBaseCRS = gridBaseCRS;
        gridBaseCRS = newGridBaseCRS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.GRID_CRS_TYPE__GRID_BASE_CRS, oldGridBaseCRS, gridBaseCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGridType() {
        return gridType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridType(String newGridType) {
        String oldGridType = gridType;
        gridType = newGridType;
        boolean oldGridTypeESet = gridTypeESet;
        gridTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.GRID_CRS_TYPE__GRID_TYPE, oldGridType, gridType, !oldGridTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetGridType() {
        String oldGridType = gridType;
        boolean oldGridTypeESet = gridTypeESet;
        gridType = GRID_TYPE_EDEFAULT;
        gridTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs111Package.GRID_CRS_TYPE__GRID_TYPE, oldGridType, GRID_TYPE_EDEFAULT, oldGridTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetGridType() {
        return gridTypeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getGridOrigin() {
        return gridOrigin;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridOrigin(Object newGridOrigin) {
        Object oldGridOrigin = gridOrigin;
        gridOrigin = newGridOrigin;
        boolean oldGridOriginESet = gridOriginESet;
        gridOriginESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.GRID_CRS_TYPE__GRID_ORIGIN, oldGridOrigin, gridOrigin, !oldGridOriginESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetGridOrigin() {
        Object oldGridOrigin = gridOrigin;
        boolean oldGridOriginESet = gridOriginESet;
        gridOrigin = GRID_ORIGIN_EDEFAULT;
        gridOriginESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs111Package.GRID_CRS_TYPE__GRID_ORIGIN, oldGridOrigin, GRID_ORIGIN_EDEFAULT, oldGridOriginESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetGridOrigin() {
        return gridOriginESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getGridOffsets() {
        return gridOffsets;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridOffsets(Object newGridOffsets) {
        Object oldGridOffsets = gridOffsets;
        gridOffsets = newGridOffsets;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.GRID_CRS_TYPE__GRID_OFFSETS, oldGridOffsets, gridOffsets));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getGridCS() {
        return gridCS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridCS(String newGridCS) {
        String oldGridCS = gridCS;
        gridCS = newGridCS;
        boolean oldGridCSESet = gridCSESet;
        gridCSESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.GRID_CRS_TYPE__GRID_CS, oldGridCS, gridCS, !oldGridCSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetGridCS() {
        String oldGridCS = gridCS;
        boolean oldGridCSESet = gridCSESet;
        gridCS = GRID_CS_EDEFAULT;
        gridCSESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs111Package.GRID_CRS_TYPE__GRID_CS, oldGridCS, GRID_CS_EDEFAULT, oldGridCSESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetGridCS() {
        return gridCSESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getId() {
        return id;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setId(Object newId) {
        Object oldId = id;
        id = newId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.GRID_CRS_TYPE__ID, oldId, id));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs111Package.GRID_CRS_TYPE__SRS_NAME:
                return getSrsName();
            case Wcs111Package.GRID_CRS_TYPE__GRID_BASE_CRS:
                return getGridBaseCRS();
            case Wcs111Package.GRID_CRS_TYPE__GRID_TYPE:
                return getGridType();
            case Wcs111Package.GRID_CRS_TYPE__GRID_ORIGIN:
                return getGridOrigin();
            case Wcs111Package.GRID_CRS_TYPE__GRID_OFFSETS:
                return getGridOffsets();
            case Wcs111Package.GRID_CRS_TYPE__GRID_CS:
                return getGridCS();
            case Wcs111Package.GRID_CRS_TYPE__ID:
                return getId();
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
            case Wcs111Package.GRID_CRS_TYPE__SRS_NAME:
                setSrsName(newValue);
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_BASE_CRS:
                setGridBaseCRS((String)newValue);
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_TYPE:
                setGridType((String)newValue);
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_ORIGIN:
                setGridOrigin(newValue);
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_OFFSETS:
                setGridOffsets(newValue);
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_CS:
                setGridCS((String)newValue);
                return;
            case Wcs111Package.GRID_CRS_TYPE__ID:
                setId(newValue);
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
            case Wcs111Package.GRID_CRS_TYPE__SRS_NAME:
                setSrsName(SRS_NAME_EDEFAULT);
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_BASE_CRS:
                setGridBaseCRS(GRID_BASE_CRS_EDEFAULT);
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_TYPE:
                unsetGridType();
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_ORIGIN:
                unsetGridOrigin();
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_OFFSETS:
                setGridOffsets(GRID_OFFSETS_EDEFAULT);
                return;
            case Wcs111Package.GRID_CRS_TYPE__GRID_CS:
                unsetGridCS();
                return;
            case Wcs111Package.GRID_CRS_TYPE__ID:
                setId(ID_EDEFAULT);
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
            case Wcs111Package.GRID_CRS_TYPE__SRS_NAME:
                return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
            case Wcs111Package.GRID_CRS_TYPE__GRID_BASE_CRS:
                return GRID_BASE_CRS_EDEFAULT == null ? gridBaseCRS != null : !GRID_BASE_CRS_EDEFAULT.equals(gridBaseCRS);
            case Wcs111Package.GRID_CRS_TYPE__GRID_TYPE:
                return isSetGridType();
            case Wcs111Package.GRID_CRS_TYPE__GRID_ORIGIN:
                return isSetGridOrigin();
            case Wcs111Package.GRID_CRS_TYPE__GRID_OFFSETS:
                return GRID_OFFSETS_EDEFAULT == null ? gridOffsets != null : !GRID_OFFSETS_EDEFAULT.equals(gridOffsets);
            case Wcs111Package.GRID_CRS_TYPE__GRID_CS:
                return isSetGridCS();
            case Wcs111Package.GRID_CRS_TYPE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
        result.append(" (srsName: ");
        result.append(srsName);
        result.append(", gridBaseCRS: ");
        result.append(gridBaseCRS);
        result.append(", gridType: ");
        if (gridTypeESet) result.append(gridType); else result.append("<unset>");
        result.append(", gridOrigin: ");
        if (gridOriginESet) result.append(gridOrigin); else result.append("<unset>");
        result.append(", gridOffsets: ");
        result.append(gridOffsets);
        result.append(", gridCS: ");
        if (gridCSESet) result.append(gridCS); else result.append("<unset>");
        result.append(", id: ");
        result.append(id);
        result.append(')');
        return result.toString();
    }

} //GridCrsTypeImpl
