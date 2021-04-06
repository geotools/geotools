/**
 */
package org.w3._2001.schema;

import java.math.BigInteger;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *    group type for explicit groups, named top-level groups and
 *    group references
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.Group#getParticle <em>Particle</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getElement <em>Element</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getAll <em>All</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getChoice <em>Choice</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getSequence <em>Sequence</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getAny <em>Any</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getMaxOccurs <em>Max Occurs</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getMinOccurs <em>Min Occurs</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getName <em>Name</em>}</li>
 *   <li>{@link org.w3._2001.schema.Group#getRef <em>Ref</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getGroup()
 * @model abstract="true"
 *        extendedMetaData="name='group' kind='elementOnly'"
 * @generated
 */
public interface Group extends Annotated {
	/**
	 * Returns the value of the '<em><b>Particle</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Particle</em>' attribute list.
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_Particle()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='Particle:3'"
	 * @generated
	 */
	FeatureMap getParticle();

	/**
	 * Returns the value of the '<em><b>Element</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.LocalElement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_Element()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='element' namespace='##targetNamespace' group='#Particle:3'"
	 * @generated
	 */
	EList<LocalElement> getElement();

	/**
	 * Returns the value of the '<em><b>Group</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.GroupRef}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_Group()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='group' namespace='##targetNamespace' group='#Particle:3'"
	 * @generated
	 */
	EList<GroupRef> getGroup();

	/**
	 * Returns the value of the '<em><b>All</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.All}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>All</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_All()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='all' namespace='##targetNamespace' group='#Particle:3'"
	 * @generated
	 */
	EList<All> getAll();

	/**
	 * Returns the value of the '<em><b>Choice</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.ExplicitGroup}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Choice</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_Choice()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='choice' namespace='##targetNamespace' group='#Particle:3'"
	 * @generated
	 */
	EList<ExplicitGroup> getChoice();

	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.ExplicitGroup}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Sequence</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_Sequence()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='sequence' namespace='##targetNamespace' group='#Particle:3'"
	 * @generated
	 */
	EList<ExplicitGroup> getSequence();

	/**
	 * Returns the value of the '<em><b>Any</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.AnyType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Any</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_Any()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='any' namespace='##targetNamespace' group='#Particle:3'"
	 * @generated
	 */
	EList<AnyType> getAny();

	/**
	 * Returns the value of the '<em><b>Max Occurs</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Occurs</em>' attribute.
	 * @see #isSetMaxOccurs()
	 * @see #unsetMaxOccurs()
	 * @see #setMaxOccurs(Object)
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_MaxOccurs()
	 * @model default="1" unsettable="true" dataType="org.w3._2001.schema.AllNNI"
	 *        extendedMetaData="kind='attribute' name='maxOccurs'"
	 * @generated
	 */
	Object getMaxOccurs();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Group#getMaxOccurs <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Occurs</em>' attribute.
	 * @see #isSetMaxOccurs()
	 * @see #unsetMaxOccurs()
	 * @see #getMaxOccurs()
	 * @generated
	 */
	void setMaxOccurs(Object value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.Group#getMaxOccurs <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxOccurs()
	 * @see #getMaxOccurs()
	 * @see #setMaxOccurs(Object)
	 * @generated
	 */
	void unsetMaxOccurs();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Group#getMaxOccurs <em>Max Occurs</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Occurs</em>' attribute is set.
	 * @see #unsetMaxOccurs()
	 * @see #getMaxOccurs()
	 * @see #setMaxOccurs(Object)
	 * @generated
	 */
	boolean isSetMaxOccurs();

	/**
	 * Returns the value of the '<em><b>Min Occurs</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Occurs</em>' attribute.
	 * @see #isSetMinOccurs()
	 * @see #unsetMinOccurs()
	 * @see #setMinOccurs(BigInteger)
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_MinOccurs()
	 * @model default="1" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
	 *        extendedMetaData="kind='attribute' name='minOccurs'"
	 * @generated
	 */
	BigInteger getMinOccurs();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Group#getMinOccurs <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Occurs</em>' attribute.
	 * @see #isSetMinOccurs()
	 * @see #unsetMinOccurs()
	 * @see #getMinOccurs()
	 * @generated
	 */
	void setMinOccurs(BigInteger value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.Group#getMinOccurs <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinOccurs()
	 * @see #getMinOccurs()
	 * @see #setMinOccurs(BigInteger)
	 * @generated
	 */
	void unsetMinOccurs();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Group#getMinOccurs <em>Min Occurs</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Occurs</em>' attribute is set.
	 * @see #unsetMinOccurs()
	 * @see #getMinOccurs()
	 * @see #setMinOccurs(BigInteger)
	 * @generated
	 */
	boolean isSetMinOccurs();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NCName"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Group#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref</em>' attribute.
	 * @see #setRef(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getGroup_Ref()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
	 *        extendedMetaData="kind='attribute' name='ref'"
	 * @generated
	 */
	QName getRef();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Group#getRef <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref</em>' attribute.
	 * @see #getRef()
	 * @generated
	 */
	void setRef(QName value);

} // Group
