/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Identification Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * General metadata identifying and describing a set of data. This type shall be extended if needed for each specific OWS to include additional metadata for each type of dataset. If needed, this type should first be restricted for each specific OWS to change the multiplicity (or optionality) of some elements.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows10.IdentificationType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows10.IdentificationType#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.ows10.IdentificationType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows10.IdentificationType#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows10.IdentificationType#getAvailableCRSGroup <em>Available CRS Group</em>}</li>
 *   <li>{@link net.opengis.ows10.IdentificationType#getAvailableCRS <em>Available CRS</em>}</li>
 *   <li>{@link net.opengis.ows10.IdentificationType#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows10.Ows10Package#getIdentificationType()
 * @model extendedMetaData="name='IdentificationType' kind='elementOnly'"
 * @generated
 */
public interface IdentificationType extends DescriptionType {
	/**
	 * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional unique identifier or name of this dataset.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Identifier</em>' containment reference.
	 * @see #setIdentifier(CodeType)
	 * @see net.opengis.ows10.Ows10Package#getIdentificationType_Identifier()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
	 * @generated
	 */
	CodeType getIdentifier();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.IdentificationType#getIdentifier <em>Identifier</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Identifier</em>' containment reference.
	 * @see #getIdentifier()
	 * @generated
	 */
	void setIdentifier(CodeType value);

	/**
	 * Returns the value of the '<em><b>Bounding Box Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of zero or more bounding boxes whose union describes the extent of this dataset.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bounding Box Group</em>' attribute list.
	 * @see net.opengis.ows10.Ows10Package#getIdentificationType_BoundingBoxGroup()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='BoundingBox:group' namespace='##targetNamespace'"
	 * @generated
	 */
	FeatureMap getBoundingBoxGroup();

	/**
	 * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.ows10.BoundingBoxType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of zero or more bounding boxes whose union describes the extent of this dataset.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bounding Box</em>' containment reference list.
	 * @see net.opengis.ows10.Ows10Package#getIdentificationType_BoundingBox()
	 * @model type="net.opengis.ows10.BoundingBoxType" containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='BoundingBox' namespace='##targetNamespace' group='BoundingBox:group'"
	 * @generated
	 */
	EList getBoundingBox();

	/**
	 * Returns the value of the '<em><b>Output Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of zero or more references to data formats supported for server outputs.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Output Format</em>' attribute.
	 * @see #setOutputFormat(String)
	 * @see net.opengis.ows10.Ows10Package#getIdentificationType_OutputFormat()
	 * @model unique="false" dataType="net.opengis.ows10.MimeType"
	 *        extendedMetaData="kind='element' name='OutputFormat' namespace='##targetNamespace'"
	 * @generated
	 */
	String getOutputFormat();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.IdentificationType#getOutputFormat <em>Output Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output Format</em>' attribute.
	 * @see #getOutputFormat()
	 * @generated
	 */
	void setOutputFormat(String value);

	/**
	 * Returns the value of the '<em><b>Available CRS Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of zero or more available coordinate reference systems.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Available CRS Group</em>' attribute list.
	 * @see net.opengis.ows10.Ows10Package#getIdentificationType_AvailableCRSGroup()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='AvailableCRS:group' namespace='##targetNamespace'"
	 * @generated
	 */
	FeatureMap getAvailableCRSGroup();

	/**
	 * Returns the value of the '<em><b>Available CRS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of zero or more available coordinate reference systems.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Available CRS</em>' attribute.
	 * @see #setAvailableCRS(String)
	 * @see net.opengis.ows10.Ows10Package#getIdentificationType_AvailableCRS()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='AvailableCRS' namespace='##targetNamespace' group='AvailableCRS:group'"
	 * @generated
	 */
	String getAvailableCRS();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.IdentificationType#getAvailableCRS <em>Available CRS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Available CRS</em>' attribute.
	 * @see #getAvailableCRS()
	 * @generated
	 */
	void setAvailableCRS(String value);

	/**
	 * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.ows10.MetadataType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional unordered list of additional metadata about this data(set). A list of optional metadata elements for this data identification could be specified in the Implementation Specification for this service.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Metadata</em>' containment reference list.
	 * @see net.opengis.ows10.Ows10Package#getIdentificationType_Metadata()
	 * @model type="net.opengis.ows10.MetadataType" containment="true"
	 *        extendedMetaData="kind='element' name='Metadata' namespace='##targetNamespace'"
	 * @generated
	 */
	EList getMetadata();

} // IdentificationType
