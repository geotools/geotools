/**
 */
package net.opengis.ows20.impl;

import java.math.BigInteger;

import java.util.List;

import net.opengis.ows20.BoundingBoxType;
import net.opengis.ows20.Ows20Package;

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
 *   <li>{@link net.opengis.ows20.impl.BoundingBoxTypeImpl#getLowerCorner <em>Lower Corner</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.BoundingBoxTypeImpl#getUpperCorner <em>Upper Corner</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.BoundingBoxTypeImpl#getCrs <em>Crs</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.BoundingBoxTypeImpl#getDimensions <em>Dimensions</em>}</li>
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
    protected static final List<Double> LOWER_CORNER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLowerCorner() <em>Lower Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLowerCorner()
     * @generated
     * @ordered
     */
    protected List<Double> lowerCorner = LOWER_CORNER_EDEFAULT;

    /**
     * The default value of the '{@link #getUpperCorner() <em>Upper Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUpperCorner()
     * @generated
     * @ordered
     */
    protected static final List<Double> UPPER_CORNER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUpperCorner() <em>Upper Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUpperCorner()
     * @generated
     * @ordered
     */
    protected List<Double> upperCorner = UPPER_CORNER_EDEFAULT;

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
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.BOUNDING_BOX_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Double> getLowerCorner() {
        return lowerCorner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLowerCorner(List<Double> newLowerCorner) {
        List<Double> oldLowerCorner = lowerCorner;
        lowerCorner = newLowerCorner;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.BOUNDING_BOX_TYPE__LOWER_CORNER, oldLowerCorner, lowerCorner));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Double> getUpperCorner() {
        return upperCorner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUpperCorner(List<Double> newUpperCorner) {
        List<Double> oldUpperCorner = upperCorner;
        upperCorner = newUpperCorner;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.BOUNDING_BOX_TYPE__UPPER_CORNER, oldUpperCorner, upperCorner));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.BOUNDING_BOX_TYPE__CRS, oldCrs, crs));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.BOUNDING_BOX_TYPE__DIMENSIONS, oldDimensions, dimensions));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows20Package.BOUNDING_BOX_TYPE__LOWER_CORNER:
                return getLowerCorner();
            case Ows20Package.BOUNDING_BOX_TYPE__UPPER_CORNER:
                return getUpperCorner();
            case Ows20Package.BOUNDING_BOX_TYPE__CRS:
                return getCrs();
            case Ows20Package.BOUNDING_BOX_TYPE__DIMENSIONS:
                return getDimensions();
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
            case Ows20Package.BOUNDING_BOX_TYPE__LOWER_CORNER:
                setLowerCorner((List<Double>)newValue);
                return;
            case Ows20Package.BOUNDING_BOX_TYPE__UPPER_CORNER:
                setUpperCorner((List<Double>)newValue);
                return;
            case Ows20Package.BOUNDING_BOX_TYPE__CRS:
                setCrs((String)newValue);
                return;
            case Ows20Package.BOUNDING_BOX_TYPE__DIMENSIONS:
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
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Ows20Package.BOUNDING_BOX_TYPE__LOWER_CORNER:
                setLowerCorner(LOWER_CORNER_EDEFAULT);
                return;
            case Ows20Package.BOUNDING_BOX_TYPE__UPPER_CORNER:
                setUpperCorner(UPPER_CORNER_EDEFAULT);
                return;
            case Ows20Package.BOUNDING_BOX_TYPE__CRS:
                setCrs(CRS_EDEFAULT);
                return;
            case Ows20Package.BOUNDING_BOX_TYPE__DIMENSIONS:
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
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Ows20Package.BOUNDING_BOX_TYPE__LOWER_CORNER:
                return LOWER_CORNER_EDEFAULT == null ? lowerCorner != null : !LOWER_CORNER_EDEFAULT.equals(lowerCorner);
            case Ows20Package.BOUNDING_BOX_TYPE__UPPER_CORNER:
                return UPPER_CORNER_EDEFAULT == null ? upperCorner != null : !UPPER_CORNER_EDEFAULT.equals(upperCorner);
            case Ows20Package.BOUNDING_BOX_TYPE__CRS:
                return CRS_EDEFAULT == null ? crs != null : !CRS_EDEFAULT.equals(crs);
            case Ows20Package.BOUNDING_BOX_TYPE__DIMENSIONS:
                return DIMENSIONS_EDEFAULT == null ? dimensions != null : !DIMENSIONS_EDEFAULT.equals(dimensions);
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
