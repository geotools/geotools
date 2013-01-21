/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Schema Component Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A schema component includes a schema fragment (type
 *          definition) or an entire schema from some target namespace;
 *          the schema language is identified by URI. If the component
 *          is a schema fragment its parent MUST be referenced (parentSchema).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.SchemaComponentType#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SchemaComponentType#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SchemaComponentType#getParentSchema <em>Parent Schema</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SchemaComponentType#getSchemaLanguage <em>Schema Language</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SchemaComponentType#getTargetNamespace <em>Target Namespace</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getSchemaComponentType()
 * @model extendedMetaData="name='SchemaComponentType' kind='mixed'"
 * @generated
 */
public interface SchemaComponentType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getSchemaComponentType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getSchemaComponentType_Any()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='elementWildcard' wildcards='##any' name=':1' processing='lax'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Parent Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parent Schema</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parent Schema</em>' attribute.
     * @see #setParentSchema(String)
     * @see net.opengis.cat.csw20.Csw20Package#getSchemaComponentType_ParentSchema()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='parentSchema'"
     * @generated
     */
    String getParentSchema();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SchemaComponentType#getParentSchema <em>Parent Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parent Schema</em>' attribute.
     * @see #getParentSchema()
     * @generated
     */
    void setParentSchema(String value);

    /**
     * Returns the value of the '<em><b>Schema Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Schema Language</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Schema Language</em>' attribute.
     * @see #setSchemaLanguage(String)
     * @see net.opengis.cat.csw20.Csw20Package#getSchemaComponentType_SchemaLanguage()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='schemaLanguage'"
     * @generated
     */
    String getSchemaLanguage();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SchemaComponentType#getSchemaLanguage <em>Schema Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Schema Language</em>' attribute.
     * @see #getSchemaLanguage()
     * @generated
     */
    void setSchemaLanguage(String value);

    /**
     * Returns the value of the '<em><b>Target Namespace</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Namespace</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Namespace</em>' attribute.
     * @see #setTargetNamespace(String)
     * @see net.opengis.cat.csw20.Csw20Package#getSchemaComponentType_TargetNamespace()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='targetNamespace'"
     * @generated
     */
    String getTargetNamespace();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SchemaComponentType#getTargetNamespace <em>Target Namespace</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Namespace</em>' attribute.
     * @see #getTargetNamespace()
     * @generated
     */
    void setTargetNamespace(String value);

} // SchemaComponentType
