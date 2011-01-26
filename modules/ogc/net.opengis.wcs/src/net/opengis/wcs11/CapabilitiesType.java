/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import net.opengis.ows11.CapabilitiesBaseType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.CapabilitiesType#getContents <em>Contents</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getCapabilitiesType()
 * @model extendedMetaData="name='Capabilities_._type' kind='elementOnly'"
 * @generated
 */
public interface CapabilitiesType extends CapabilitiesBaseType {
    /**
     * Returns the value of the '<em><b>Contents</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Contents section of WCS service metadata (or Capabilities) XML document. For the WCS, these contents are brief metadata about the coverages available from this server, or a reference to another source from which this metadata is available. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Contents</em>' containment reference.
     * @see #setContents(ContentsType)
     * @see net.opengis.wcs11.Wcs111Package#getCapabilitiesType_Contents()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Contents' namespace='##targetNamespace'"
     * @generated
     */
    ContentsType getContents();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.CapabilitiesType#getContents <em>Contents</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Contents</em>' containment reference.
     * @see #getContents()
     * @generated
     */
    void setContents(ContentsType value);

} // CapabilitiesType
