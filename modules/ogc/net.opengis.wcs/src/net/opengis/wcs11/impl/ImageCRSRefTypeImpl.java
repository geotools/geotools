/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.wcs11.ImageCRSRefType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Image CRS Ref Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.ImageCRSRefTypeImpl#getImageCRS <em>Image CRS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ImageCRSRefTypeImpl extends EObjectImpl implements ImageCRSRefType {
    /**
     * The default value of the '{@link #getImageCRS() <em>Image CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImageCRS()
     * @generated
     * @ordered
     */
    protected static final Object IMAGE_CRS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getImageCRS() <em>Image CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImageCRS()
     * @generated
     * @ordered
     */
    protected Object imageCRS = IMAGE_CRS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ImageCRSRefTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.IMAGE_CRS_REF_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getImageCRS() {
        return imageCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setImageCRS(Object newImageCRS) {
        Object oldImageCRS = imageCRS;
        imageCRS = newImageCRS;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.IMAGE_CRS_REF_TYPE__IMAGE_CRS, oldImageCRS, imageCRS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs111Package.IMAGE_CRS_REF_TYPE__IMAGE_CRS:
                return getImageCRS();
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
            case Wcs111Package.IMAGE_CRS_REF_TYPE__IMAGE_CRS:
                setImageCRS(newValue);
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
            case Wcs111Package.IMAGE_CRS_REF_TYPE__IMAGE_CRS:
                setImageCRS(IMAGE_CRS_EDEFAULT);
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
            case Wcs111Package.IMAGE_CRS_REF_TYPE__IMAGE_CRS:
                return IMAGE_CRS_EDEFAULT == null ? imageCRS != null : !IMAGE_CRS_EDEFAULT.equals(imageCRS);
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
        result.append(" (imageCRS: ");
        result.append(imageCRS);
        result.append(')');
        return result.toString();
    }

} //ImageCRSRefTypeImpl
