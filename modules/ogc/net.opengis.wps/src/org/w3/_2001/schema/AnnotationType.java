/**
 */
package org.w3._2001.schema;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.AnnotationType#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.AnnotationType#getAppinfo <em>Appinfo</em>}</li>
 *   <li>{@link org.w3._2001.schema.AnnotationType#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link org.w3._2001.schema.AnnotationType#getId <em>Id</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getAnnotationType()
 * @model extendedMetaData="name='annotation_._type' kind='elementOnly'"
 * @generated
 */
public interface AnnotationType extends OpenAttrs {
	/**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see org.w3._2001.schema.SchemaPackage#getAnnotationType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:1'"
	 * @generated
	 */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Appinfo</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.AppinfoType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Appinfo</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getAnnotationType_Appinfo()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='appinfo' namespace='##targetNamespace' group='#group:1'"
	 * @generated
	 */
	EList<AppinfoType> getAppinfo();

	/**
	 * Returns the value of the '<em><b>Documentation</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.DocumentationType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Documentation</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getAnnotationType_Documentation()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='documentation' namespace='##targetNamespace' group='#group:1'"
	 * @generated
	 */
	EList<DocumentationType> getDocumentation();

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.w3._2001.schema.SchemaPackage#getAnnotationType_Id()
	 * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
	 *        extendedMetaData="kind='attribute' name='id'"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.AnnotationType#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // AnnotationType
