/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.List;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.IndexMapType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Index Map Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.IndexMapTypeImpl#getLookUpTable <em>Look Up Table</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IndexMapTypeImpl extends GridFunctionTypeImpl implements IndexMapType {
    /**
     * The default value of the '{@link #getLookUpTable() <em>Look Up Table</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLookUpTable()
     * @generated
     * @ordered
     */
    protected static final List<BigInteger> LOOK_UP_TABLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLookUpTable() <em>Look Up Table</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLookUpTable()
     * @generated
     * @ordered
     */
    protected List<BigInteger> lookUpTable = LOOK_UP_TABLE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IndexMapTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getIndexMapType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<BigInteger> getLookUpTable() {
        return lookUpTable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLookUpTable(List<BigInteger> newLookUpTable) {
        List<BigInteger> oldLookUpTable = lookUpTable;
        lookUpTable = newLookUpTable;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.INDEX_MAP_TYPE__LOOK_UP_TABLE, oldLookUpTable, lookUpTable));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.INDEX_MAP_TYPE__LOOK_UP_TABLE:
                return getLookUpTable();
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
            case Gml311Package.INDEX_MAP_TYPE__LOOK_UP_TABLE:
                setLookUpTable((List<BigInteger>)newValue);
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
            case Gml311Package.INDEX_MAP_TYPE__LOOK_UP_TABLE:
                setLookUpTable(LOOK_UP_TABLE_EDEFAULT);
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
            case Gml311Package.INDEX_MAP_TYPE__LOOK_UP_TABLE:
                return LOOK_UP_TABLE_EDEFAULT == null ? lookUpTable != null : !LOOK_UP_TABLE_EDEFAULT.equals(lookUpTable);
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
        result.append(" (lookUpTable: ");
        result.append(lookUpTable);
        result.append(')');
        return result.toString();
    }

} //IndexMapTypeImpl
