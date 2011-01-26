/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract GML Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *  The optional attribute "id" is omitted from this profile.
 * All complexContent GML elements are directly or indirectly derived from this abstract supertype to establish a hierarchy of GML types that may be distinguished from other XML types by their ancestry.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.AbstractGMLType#getMetaDataProperty <em>Meta Data Property</em>}</li>
 *   <li>{@link net.opengis.gml.AbstractGMLType#getDescription <em>Description</em>}</li>
 *   <li>{@link net.opengis.gml.AbstractGMLType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getAbstractGMLType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractGMLType' kind='elementOnly'"
 * @generated
 */
public interface AbstractGMLType extends EObject {
    /**
	 * Returns the value of the '<em><b>Meta Data Property</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.MetaDataPropertyType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Contains or refers to a metadata package that contains metadata properties.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Meta Data Property</em>' containment reference list.
	 * @see net.opengis.gml.GmlPackage#getAbstractGMLType_MetaDataProperty()
	 * @model type="net.opengis.gml.MetaDataPropertyType" containment="true"
	 *        extendedMetaData="kind='element' name='metaDataProperty' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getMetaDataProperty();

    /**
	 * Returns the value of the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Contains a simple text description of the object, or refers to an external description.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Description</em>' containment reference.
	 * @see #setDescription(StringOrRefType)
	 * @see net.opengis.gml.GmlPackage#getAbstractGMLType_Description()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
    StringOrRefType getDescription();

    /**
	 * Sets the value of the '{@link net.opengis.gml.AbstractGMLType#getDescription <em>Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' containment reference.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(StringOrRefType value);

    /**
	 * Returns the value of the '<em><b>Name</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.CodeType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Multiple names may be provided.  These will often be distinguished by being assigned by different authorities, as indicated by the value of the codeSpace attribute.  In an instance document there will usually only be one name per authority.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' containment reference list.
	 * @see net.opengis.gml.GmlPackage#getAbstractGMLType_Name()
	 * @model type="net.opengis.gml.CodeType" containment="true"
	 *        extendedMetaData="kind='element' name='name' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getName();

} // AbstractGMLType
