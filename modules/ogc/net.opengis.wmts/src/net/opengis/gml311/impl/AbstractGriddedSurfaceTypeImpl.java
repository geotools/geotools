/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.gml311.AbstractGriddedSurfaceType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.RowType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Gridded Surface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractGriddedSurfaceTypeImpl#getRow <em>Row</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractGriddedSurfaceTypeImpl#getRows <em>Rows</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractGriddedSurfaceTypeImpl#getColumns <em>Columns</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AbstractGriddedSurfaceTypeImpl extends AbstractParametricCurveSurfaceTypeImpl implements AbstractGriddedSurfaceType {
    /**
     * The cached value of the '{@link #getRow() <em>Row</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRow()
     * @generated
     * @ordered
     */
    protected EList<RowType> row;

    /**
     * The default value of the '{@link #getRows() <em>Rows</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRows()
     * @generated
     * @ordered
     */
    protected static final BigInteger ROWS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRows() <em>Rows</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRows()
     * @generated
     * @ordered
     */
    protected BigInteger rows = ROWS_EDEFAULT;

    /**
     * The default value of the '{@link #getColumns() <em>Columns</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColumns()
     * @generated
     * @ordered
     */
    protected static final BigInteger COLUMNS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getColumns() <em>Columns</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getColumns()
     * @generated
     * @ordered
     */
    protected BigInteger columns = COLUMNS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractGriddedSurfaceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractGriddedSurfaceType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<RowType> getRow() {
        if (row == null) {
            row = new EObjectContainmentEList<RowType>(RowType.class, this, Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROW);
        }
        return row;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getRows() {
        return rows;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRows(BigInteger newRows) {
        BigInteger oldRows = rows;
        rows = newRows;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROWS, oldRows, rows));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getColumns() {
        return columns;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setColumns(BigInteger newColumns) {
        BigInteger oldColumns = columns;
        columns = newColumns;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__COLUMNS, oldColumns, columns));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROW:
                return ((InternalEList<?>)getRow()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROW:
                return getRow();
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROWS:
                return getRows();
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__COLUMNS:
                return getColumns();
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
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROW:
                getRow().clear();
                getRow().addAll((Collection<? extends RowType>)newValue);
                return;
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROWS:
                setRows((BigInteger)newValue);
                return;
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__COLUMNS:
                setColumns((BigInteger)newValue);
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
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROW:
                getRow().clear();
                return;
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROWS:
                setRows(ROWS_EDEFAULT);
                return;
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__COLUMNS:
                setColumns(COLUMNS_EDEFAULT);
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
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROW:
                return row != null && !row.isEmpty();
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__ROWS:
                return ROWS_EDEFAULT == null ? rows != null : !ROWS_EDEFAULT.equals(rows);
            case Gml311Package.ABSTRACT_GRIDDED_SURFACE_TYPE__COLUMNS:
                return COLUMNS_EDEFAULT == null ? columns != null : !COLUMNS_EDEFAULT.equals(columns);
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
        result.append(" (rows: ");
        result.append(rows);
        result.append(", columns: ");
        result.append(columns);
        result.append(')');
        return result.toString();
    }

} //AbstractGriddedSurfaceTypeImpl
