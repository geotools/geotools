/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.wcs11.GridCrsType;
import net.opengis.wcs11.OutputType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.OutputTypeImpl#getGridCRS <em>Grid CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.OutputTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.OutputTypeImpl#isStore <em>Store</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputTypeImpl extends EObjectImpl implements OutputType {
    /**
     * The cached value of the '{@link #getGridCRS() <em>Grid CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGridCRS()
     * @generated
     * @ordered
     */
    protected GridCrsType gridCRS;

    /**
     * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected static final String FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected String format = FORMAT_EDEFAULT;

    /**
     * The default value of the '{@link #isStore() <em>Store</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStore()
     * @generated
     * @ordered
     */
    protected static final boolean STORE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStore() <em>Store</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStore()
     * @generated
     * @ordered
     */
    protected boolean store = STORE_EDEFAULT;

    /**
     * This is true if the Store attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean storeESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OutputTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.OUTPUT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GridCrsType getGridCRS() {
        return gridCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGridCRS(GridCrsType newGridCRS, NotificationChain msgs) {
        GridCrsType oldGridCRS = gridCRS;
        gridCRS = newGridCRS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.OUTPUT_TYPE__GRID_CRS, oldGridCRS, newGridCRS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGridCRS(GridCrsType newGridCRS) {
        if (newGridCRS != gridCRS) {
            NotificationChain msgs = null;
            if (gridCRS != null)
                msgs = ((InternalEObject)gridCRS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.OUTPUT_TYPE__GRID_CRS, null, msgs);
            if (newGridCRS != null)
                msgs = ((InternalEObject)newGridCRS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.OUTPUT_TYPE__GRID_CRS, null, msgs);
            msgs = basicSetGridCRS(newGridCRS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.OUTPUT_TYPE__GRID_CRS, newGridCRS, newGridCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFormat() {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat(String newFormat) {
        String oldFormat = format;
        format = newFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.OUTPUT_TYPE__FORMAT, oldFormat, format));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isStore() {
        return store;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStore(boolean newStore) {
        boolean oldStore = store;
        store = newStore;
        boolean oldStoreESet = storeESet;
        storeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.OUTPUT_TYPE__STORE, oldStore, store, !oldStoreESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetStore() {
        boolean oldStore = store;
        boolean oldStoreESet = storeESet;
        store = STORE_EDEFAULT;
        storeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs111Package.OUTPUT_TYPE__STORE, oldStore, STORE_EDEFAULT, oldStoreESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetStore() {
        return storeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.OUTPUT_TYPE__GRID_CRS:
                return basicSetGridCRS(null, msgs);
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
            case Wcs111Package.OUTPUT_TYPE__GRID_CRS:
                return getGridCRS();
            case Wcs111Package.OUTPUT_TYPE__FORMAT:
                return getFormat();
            case Wcs111Package.OUTPUT_TYPE__STORE:
                return isStore() ? Boolean.TRUE : Boolean.FALSE;
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
            case Wcs111Package.OUTPUT_TYPE__GRID_CRS:
                setGridCRS((GridCrsType)newValue);
                return;
            case Wcs111Package.OUTPUT_TYPE__FORMAT:
                setFormat((String)newValue);
                return;
            case Wcs111Package.OUTPUT_TYPE__STORE:
                setStore(((Boolean)newValue).booleanValue());
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
            case Wcs111Package.OUTPUT_TYPE__GRID_CRS:
                setGridCRS((GridCrsType)null);
                return;
            case Wcs111Package.OUTPUT_TYPE__FORMAT:
                setFormat(FORMAT_EDEFAULT);
                return;
            case Wcs111Package.OUTPUT_TYPE__STORE:
                unsetStore();
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
            case Wcs111Package.OUTPUT_TYPE__GRID_CRS:
                return gridCRS != null;
            case Wcs111Package.OUTPUT_TYPE__FORMAT:
                return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
            case Wcs111Package.OUTPUT_TYPE__STORE:
                return isSetStore();
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
        result.append(" (format: ");
        result.append(format);
        result.append(", store: ");
        if (storeESet) result.append(store); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //OutputTypeImpl
