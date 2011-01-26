/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This element either references or contains more metadata about the element that includes this element. To reference metadata stored remotely, at least the xlinks:href attribute in xlink:simpleLink shall be included. Either at least one of the attributes in xlink:simpleLink or a substitute for the AbstractMetaData element shall be included, but not both. An Implementation Specification can restrict the contents of this element to always be a reference or always contain metadata. (Informative: This element was adapted from the metaDataProperty element in GML 3.0.)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows10.MetadataType#getAbstractMetaDataGroup <em>Abstract Meta Data Group</em>}</li>
 *   <li>{@link net.opengis.ows10.MetadataType#getAbstractMetaData <em>Abstract Meta Data</em>}</li>
 *   <li>{@link net.opengis.ows10.MetadataType#getAbout <em>About</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows10.Ows10Package#getMetadataType()
 * @model extendedMetaData="name='MetadataType' kind='elementOnly'"
 * @generated
 */
public interface MetadataType extends EObject {
	/**
	 * Returns the value of the '<em><b>Abstract Meta Data Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abstract Meta Data Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Abstract Meta Data Group</em>' attribute list.
	 * @see net.opengis.ows10.Ows10Package#getMetadataType_AbstractMetaDataGroup()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='AbstractMetaData:group' namespace='##targetNamespace'"
	 * @generated
	 */
	FeatureMap getAbstractMetaDataGroup();

	/**
	 * Returns the value of the '<em><b>Abstract Meta Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abstract Meta Data</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Abstract Meta Data</em>' containment reference.
	 * @see net.opengis.ows10.Ows10Package#getMetadataType_AbstractMetaData()
	 * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='AbstractMetaData' namespace='##targetNamespace' group='AbstractMetaData:group'"
	 * @generated
	 */
	EObject getAbstractMetaData();

	/**
	 * Returns the value of the '<em><b>About</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional reference to the aspect of the element which includes this "metadata" element that this metadata provides more information about.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>About</em>' attribute.
	 * @see #setAbout(String)
	 * @see net.opengis.ows10.Ows10Package#getMetadataType_About()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='about'"
	 * @generated
	 */
	String getAbout();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.MetadataType#getAbout <em>About</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>About</em>' attribute.
	 * @see #getAbout()
	 * @generated
	 */
	void setAbout(String value);

} // MetadataType
