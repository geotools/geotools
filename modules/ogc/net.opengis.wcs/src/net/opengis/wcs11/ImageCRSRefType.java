/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Image CRS Ref Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Association to an image coordinate reference system, either referencing or containing the definition of that reference system. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.ImageCRSRefType#getImageCRS <em>Image CRS</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getImageCRSRefType()
 * @model extendedMetaData="name='ImageCRSRefType' kind='elementOnly'"
 * @generated
 */
public interface ImageCRSRefType extends EObject {
    /**
     * Returns the value of the '<em><b>Image CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Image CRS</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Image CRS</em>' attribute.
     * @see #setImageCRS(Object)
     * @see net.opengis.wcs11.Wcs111Package#getImageCRSRefType_ImageCRS()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='ImageCRS' namespace='http://www.opengis.net/gml'"
     * @generated
     */
    Object getImageCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.ImageCRSRefType#getImageCRS <em>Image CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Image CRS</em>' attribute.
     * @see #getImageCRS()
     * @generated
     */
    void setImageCRS(Object value);

} // ImageCRSRefType
