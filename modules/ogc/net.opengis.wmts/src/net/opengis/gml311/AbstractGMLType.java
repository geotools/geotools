/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract GML Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * All complexContent GML elements are directly or indirectly derived from this abstract supertype 
 * 	to establish a hierarchy of GML types that may be distinguished from other XML types by their ancestry. 
 * 	Elements in this hierarchy may have an ID and are thus referenceable.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractGMLType#getMetaDataProperty <em>Meta Data Property</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGMLType#getDescription <em>Description</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGMLType#getNameGroup <em>Name Group</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGMLType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGMLType#getId <em>Id</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractGMLType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractGMLType' kind='elementOnly'"
 * @generated
 */
public interface AbstractGMLType extends EObject {
    /**
     * Returns the value of the '<em><b>Meta Data Property</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.MetaDataPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Contains or refers to a metadata package that contains metadata properties.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meta Data Property</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractGMLType_MetaDataProperty()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='metaDataProperty' namespace='##targetNamespace'"
     * @generated
     */
    EList<MetaDataPropertyType> getMetaDataProperty();

    /**
     * Returns the value of the '<em><b>Description</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Contains a simple text description of the object, or refers to an external description.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Description</em>' containment reference.
     * @see #setDescription(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getAbstractGMLType_Description()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getDescription();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGMLType#getDescription <em>Description</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Description</em>' containment reference.
     * @see #getDescription()
     * @generated
     */
    void setDescription(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Name Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Multiple names may be provided.  These will often be distinguished by being assigned by different authorities, as indicated by the value of the codeSpace attribute.  In an instance document there will usually only be one name per authority.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Name Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getAbstractGMLType_NameGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='name:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getNameGroup();

    /**
     * Returns the value of the '<em><b>Name</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CodeType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Multiple names may be provided.  These will often be distinguished by being assigned by different authorities, as indicated by the value of the codeSpace attribute.  In an instance document there will usually only be one name per authority.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Name</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractGMLType_Name()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='name' namespace='##targetNamespace' group='name:group'"
     * @generated
     */
    EList<CodeType> getName();

    /**
     * Returns the value of the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Database handle for the object.  It is of XML type ID, so is constrained to be unique in the XML document within which it occurs.  An external identifier for the object in the form of a URI may be constructed using standard XML and XPointer methods.  This is done by concatenating the URI for the document, a fragment separator, and the value of the id attribute.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Id</em>' attribute.
     * @see #setId(String)
     * @see net.opengis.gml311.Gml311Package#getAbstractGMLType_Id()
     * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
     *        extendedMetaData="kind='attribute' name='id' namespace='##targetNamespace'"
     * @generated
     */
    String getId();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGMLType#getId <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' attribute.
     * @see #getId()
     * @generated
     */
    void setId(String value);

} // AbstractGMLType
