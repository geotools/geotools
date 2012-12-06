/**
 */
package net.opengis.wcs20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dimension Subset Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.DimensionSubsetType#getDimension <em>Dimension</em>}</li>
 *   <li>{@link net.opengis.wcs20.DimensionSubsetType#getCRS <em>CRS</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getDimensionSubsetType()
 * @model abstract="true"
 *        extendedMetaData="name='DimensionSubsetType' kind='elementOnly'"
 * @generated
 */
public interface DimensionSubsetType extends EObject {
    /**
     * Returns the value of the '<em><b>Dimension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dimension</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dimension</em>' attribute.
     * @see #setDimension(String)
     * @see net.opengis.wcs20.Wcs20Package#getDimensionSubsetType_Dimension()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" required="true"
     *        extendedMetaData="kind='element' name='Dimension' namespace='##targetNamespace'"
     * @generated
     */
    String getDimension();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DimensionSubsetType#getDimension <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dimension</em>' attribute.
     * @see #getDimension()
     * @generated
     */
    void setDimension(String value);
    
    /**
     * The CRS, which can only be specified in the WCS 2.0 KVP protocol (but not in the POST one!!!)
     * @model  
     */
    String getCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DimensionSubsetType#getCRS <em>CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>CRS</em>' attribute.
     * @see #getCRS()
     * @generated
     */
    void setCRS(String value);
    
    

} // DimensionSubsetType
