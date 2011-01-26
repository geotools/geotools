/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata URL Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             A Web Feature Server MAY use zero or more MetadataURL
 *             elements to offer detailed, standardized metadata about
 *             the data underneath a particular feature type.  The type
 *             attribute indicates the standard to which the metadata
 *             complies; the format attribute indicates how the metadata is
 *             structured.  Two types are defined at present:
 *             'TC211' or 'ISO19115' = ISO TC211 19115;
 *             'FGDC'                = FGDC CSDGM.
 *             'ISO19139'            = ISO 19139
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.MetadataURLType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wfs.MetadataURLType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wfs.MetadataURLType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getMetadataURLType()
 * @model extendedMetaData="name='MetadataURLType' kind='simple'"
 * @generated
 */
public interface MetadataURLType extends EObject {
	/**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see net.opengis.wfs.WfsPackage#getMetadataURLType_Value()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
	String getValue();

	/**
     * Sets the value of the '{@link net.opengis.wfs.MetadataURLType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
	void setValue(String value);

	/**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Format</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see net.opengis.wfs.WfsPackage#getMetadataURLType_Format()
     * @model unique="false" dataType="net.opengis.wfs.FormatType" required="true"
     *        extendedMetaData="kind='attribute' name='format'"
     * @generated
     */
	String getFormat();

	/**
     * Sets the value of the '{@link net.opengis.wfs.MetadataURLType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
	void setFormat(String value);

	/**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(String)
     * @see net.opengis.wfs.WfsPackage#getMetadataURLType_Type()
     * @model unique="false" dataType="net.opengis.wfs.TypeType" required="true"
     *        extendedMetaData="kind='attribute' name='type'"
     * @generated
     */
	String getType();

	/**
     * Sets the value of the '{@link net.opengis.wfs.MetadataURLType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
	void setType(String value);

} // MetadataURLType
