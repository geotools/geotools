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
 * A representation of the model object '<em><b>Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Asks for the GetCoverage response to be expressed in a particular CRS and encoded in a particular format. Can also ask for the response coverage to be stored remotely from the client at a URL, instead of being returned in the operation response. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.OutputType#getGridCRS <em>Grid CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.OutputType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wcs11.OutputType#isStore <em>Store</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getOutputType()
 * @model extendedMetaData="name='OutputType' kind='elementOnly'"
 * @generated
 */
public interface OutputType extends EObject {
    /**
     * Returns the value of the '<em><b>Grid CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional definition of the GridCRS in which the GetCoverage response shall be expressed. When this GridCRS is not included, the output shall be in the ImageCRS or GridCRS of the offered image, as identified in the CoverageDescription.  To request no interpolation, this GridCRS should be omitted. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid CRS</em>' containment reference.
     * @see #setGridCRS(GridCrsType)
     * @see net.opengis.wcs11.Wcs111Package#getOutputType_GridCRS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='GridCRS' namespace='##targetNamespace'"
     * @generated
     */
    GridCrsType getGridCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.OutputType#getGridCRS <em>Grid CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid CRS</em>' containment reference.
     * @see #getGridCRS()
     * @generated
     */
    void setGridCRS(GridCrsType value);

    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of the format in which GetCoverage response shall be encoded. This identifier value shall be among those listed as SupportedFormats in the DescribeCoverage operation response. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see net.opengis.wcs11.Wcs111Package#getOutputType_Format()
     * @model dataType="net.opengis.ows11.MimeType"
     *        extendedMetaData="kind='attribute' name='format'"
     * @generated
     */
    String getFormat();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.OutputType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat(String value);

    /**
     * Returns the value of the '<em><b>Store</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specifies if the output coverage should be stored, remotely from the client at a network URL, instead of being returned with the operation response. This parameter should be included only if this operation parameter is supported by server, as encoded in the OperationsMetadata section of the Capabilities document. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Store</em>' attribute.
     * @see #isSetStore()
     * @see #unsetStore()
     * @see #setStore(boolean)
     * @see net.opengis.wcs11.Wcs111Package#getOutputType_Store()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='store'"
     * @generated
     */
    boolean isStore();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.OutputType#isStore <em>Store</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Store</em>' attribute.
     * @see #isSetStore()
     * @see #unsetStore()
     * @see #isStore()
     * @generated
     */
    void setStore(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wcs11.OutputType#isStore <em>Store</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetStore()
     * @see #isStore()
     * @see #setStore(boolean)
     * @generated
     */
    void unsetStore();

    /**
     * Returns whether the value of the '{@link net.opengis.wcs11.OutputType#isStore <em>Store</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Store</em>' attribute is set.
     * @see #unsetStore()
     * @see #isStore()
     * @see #setStore(boolean)
     * @generated
     */
    boolean isSetStore();

} // OutputType
