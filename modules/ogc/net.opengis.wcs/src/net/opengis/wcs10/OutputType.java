/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import net.opengis.gml.CodeType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Asks for the GetCoverage response to be expressed in a particular Coordinate Reference System (crs) and encoded in a particular format.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.OutputType#getCrs <em>Crs</em>}</li>
 *   <li>{@link net.opengis.wcs10.OutputType#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getOutputType()
 * @model extendedMetaData="name='OutputType' kind='elementOnly'"
 * @generated
 */
public interface OutputType extends EObject {
    /**
	 * Returns the value of the '<em><b>Crs</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Identifier of the Coordinate Reference System (crs) in which GetCoverage response shall be expressed. Identifier shall be among those listed under supportedCRSs in the DescribeCoverage XML response.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Crs</em>' containment reference.
	 * @see #setCrs(CodeType)
	 * @see net.opengis.wcs10.Wcs10Package#getOutputType_Crs()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='crs' namespace='##targetNamespace'"
	 * @generated
	 */
    CodeType getCrs();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.OutputType#getCrs <em>Crs</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Crs</em>' containment reference.
	 * @see #getCrs()
	 * @generated
	 */
    void setCrs(CodeType value);

    /**
	 * Returns the value of the '<em><b>Format</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Identifier of the format in which GetCoverage response shall be encoded. Identifier shall be among those listed under supportedFormats in the DescribeCoverage XML response.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Format</em>' containment reference.
	 * @see #setFormat(CodeType)
	 * @see net.opengis.wcs10.Wcs10Package#getOutputType_Format()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='format' namespace='##targetNamespace'"
	 * @generated
	 */
    CodeType getFormat();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.OutputType#getFormat <em>Format</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Format</em>' containment reference.
	 * @see #getFormat()
	 * @generated
	 */
    void setFormat(CodeType value);

} // OutputType
