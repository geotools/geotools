/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.math.BigInteger;

import java.util.List;

import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.Ows11Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bounding Box Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.BoundingBoxTypeImpl#getLowerCorner <em>Lower Corner</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.BoundingBoxTypeImpl#getUpperCorner <em>Upper Corner</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.BoundingBoxTypeImpl#getCrs <em>Crs</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.BoundingBoxTypeImpl#getDimensions <em>Dimensions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BoundingBoxTypeImpl extends EObjectImpl implements BoundingBoxType {
    /**
     * The default value of the '{@link #getLowerCorner() <em>Lower Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLowerCorner()
     * @generated
     * @ordered
     */
    protected static final List LOWER_CORNER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLowerCorner() <em>Lower Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLowerCorner()
     * @generated
     * @ordered
     */
    protected List lowerCorner = LOWER_CORNER_EDEFAULT;

    /**
     * The default value of the '{@link #getUpperCorner() <em>Upper Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUpperCorner()
     * @generated
     * @ordered
     */
    protected static final List UPPER_CORNER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUpperCorner() <em>Upper Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUpperCorner()
     * @generated
     * @ordered
     */
    protected List upperCorner = UPPER_CORNER_EDEFAULT;

    /**
     * The default value of the '{@link #getCrs() <em>Crs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCrs()
     * @generated
     * @ordered
     */
    protected static final String CRS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCrs() <em>Crs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCrs()
     * @generated
     * @ordered
     */
    protected String crs = CRS_EDEFAULT;

    /**
     * The default value of the '{@link #getDimensions() <em>Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimensions()
     * @generated
     * @ordered
     */
    protected static final BigInteger DIMENSIONS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDimensions() <em>Dimensions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimensions()
     * @generated
     * @ordered
     */
    protected BigInteger dimensions = DIMENSIONS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BoundingBoxTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.BOUNDING_BOX_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List getLowerCorner() {
        return lowerCorner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLowerCorner(List newLowerCorner) {
        List oldLowerCorner = lowerCorner;
        lowerCorner = newLowerCorner;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.BOUNDING_BOX_TYPE__LOWER_CORNER, oldLowerCorner, lowerCorner));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List getUpperCorner() {
        return upperCorner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUpperCorner(List newUpperCorner) {
        List oldUpperCorner = upperCorner;
        upperCorner = newUpperCorner;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.BOUNDING_BOX_TYPE__UPPER_CORNER, oldUpperCorner, upperCorner));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCrs() {
        return crs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCrs(String newCrs) {
        String oldCrs = crs;
        crs = newCrs;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.BOUNDING_BOX_TYPE__CRS, oldCrs, crs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getDimensions() {
        return dimensions;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDimensions(BigInteger newDimensions) {
        BigInteger oldDimensions = dimensions;
        dimensions = newDimensions;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.BOUNDING_BOX_TYPE__DIMENSIONS, oldDimensions, dimensions));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows11Package.BOUNDING_BOX_TYPE__LOWER_CORNER:
                return getLowerCorner();
            case Ows11Package.BOUNDING_BOX_TYPE__UPPER_CORNER:
                return getUpperCorner();
            case Ows11Package.BOUNDING_BOX_TYPE__CRS:
                return getCrs();
            case Ows11Package.BOUNDING_BOX_TYPE__DIMENSIONS:
                return getDimensions();
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
            case Ows11Package.BOUNDING_BOX_TYPE__LOWER_CORNER:
                setLowerCorner((List)newValue);
                return;
            case Ows11Package.BOUNDING_BOX_TYPE__UPPER_CORNER:
                setUpperCorner((List)newValue);
                return;
            case Ows11Package.BOUNDING_BOX_TYPE__CRS:
                setCrs((String)newValue);
                return;
            case Ows11Package.BOUNDING_BOX_TYPE__DIMENSIONS:
                setDimensions((BigInteger)newValue);
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
            case Ows11Package.BOUNDING_BOX_TYPE__LOWER_CORNER:
                setLowerCorner(LOWER_CORNER_EDEFAULT);
                return;
            case Ows11Package.BOUNDING_BOX_TYPE__UPPER_CORNER:
                setUpperCorner(UPPER_CORNER_EDEFAULT);
                return;
            case Ows11Package.BOUNDING_BOX_TYPE__CRS:
                setCrs(CRS_EDEFAULT);
                return;
            case Ows11Package.BOUNDING_BOX_TYPE__DIMENSIONS:
                setDimensions(DIMENSIONS_EDEFAULT);
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
            case Ows11Package.BOUNDING_BOX_TYPE__LOWER_CORNER:
                return LOWER_CORNER_EDEFAULT == null ? lowerCorner != null : !LOWER_CORNER_EDEFAULT.equals(lowerCorner);
            case Ows11Package.BOUNDING_BOX_TYPE__UPPER_CORNER:
                return UPPER_CORNER_EDEFAULT == null ? upperCorner != null : !UPPER_CORNER_EDEFAULT.equals(upperCorner);
            case Ows11Package.BOUNDING_BOX_TYPE__CRS:
                return CRS_EDEFAULT == null ? crs != null : !CRS_EDEFAULT.equals(crs);
            case Ows11Package.BOUNDING_BOX_TYPE__DIMENSIONS:
                return DIMENSIONS_EDEFAULT == null ? dimensions != null : !DIMENSIONS_EDEFAULT.equals(dimensions);
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
        result.append(" (lowerCorner: ");
        result.append(lowerCorner);
        result.append(", upperCorner: ");
        result.append(upperCorner);
        result.append(", crs: ");
        result.append(crs);
        result.append(", dimensions: ");
        result.append(dimensions);
        result.append(')');
        return result.toString();
    }

} //BoundingBoxTypeImpl
