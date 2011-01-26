/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Difference Query Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.DifferenceQueryType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfsv.DifferenceQueryType#getFromFeatureVersion <em>From Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfsv.DifferenceQueryType#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfsv.DifferenceQueryType#getToFeatureVersion <em>To Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfsv.DifferenceQueryType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getDifferenceQueryType()
 * @model extendedMetaData="name='DifferenceQueryType' kind='elementOnly'"
 * @generated
 */
public interface DifferenceQueryType extends EObject {
    /**
     * Returns the value of the '<em><b>Filter</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *             The Filter element is used to define spatial and/or non-spatial constraints on query.
     *             Spatial constrains use GML3 to specify the constraining geometry. A full description of
     *             the Filter element can be found in the Filter Encoding Implementation Specification.
     *           
     * <!-- end-model-doc -->
     * @return the value of the '<em>Filter</em>' attribute.
     * @see #setFilter(Object)
     * @see net.opengis.wfsv.WfsvPackage#getDifferenceQueryType_Filter()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='Filter' namespace='http://www.opengis.net/ogc'"
     * @generated
     */
    Object getFilter();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DifferenceQueryType#getFilter <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter</em>' attribute.
     * @see #getFilter()
     * @generated
     */
    void setFilter(Object value);

    /**
     * Returns the value of the '<em><b>From Feature Version</b></em>' attribute.
     * The default value is <code>"FIRST"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *           Same as featureVersion in QueryType, but this indicates a starting feature version for a
     *           difference and log operations.
     *         
     * <!-- end-model-doc -->
     * @return the value of the '<em>From Feature Version</em>' attribute.
     * @see #isSetFromFeatureVersion()
     * @see #unsetFromFeatureVersion()
     * @see #setFromFeatureVersion(String)
     * @see net.opengis.wfsv.WfsvPackage#getDifferenceQueryType_FromFeatureVersion()
     * @model default="FIRST" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='fromFeatureVersion'"
     * @generated
     */
    String getFromFeatureVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DifferenceQueryType#getFromFeatureVersion <em>From Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>From Feature Version</em>' attribute.
     * @see #isSetFromFeatureVersion()
     * @see #unsetFromFeatureVersion()
     * @see #getFromFeatureVersion()
     * @generated
     */
    void setFromFeatureVersion(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfsv.DifferenceQueryType#getFromFeatureVersion <em>From Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetFromFeatureVersion()
     * @see #getFromFeatureVersion()
     * @see #setFromFeatureVersion(String)
     * @generated
     */
    void unsetFromFeatureVersion();

    /**
     * Returns whether the value of the '{@link net.opengis.wfsv.DifferenceQueryType#getFromFeatureVersion <em>From Feature Version</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>From Feature Version</em>' attribute is set.
     * @see #unsetFromFeatureVersion()
     * @see #getFromFeatureVersion()
     * @see #setFromFeatureVersion(String)
     * @generated
     */
    boolean isSetFromFeatureVersion();

    /**
     * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *           This attribute is used to specify a specific WFS-supported SRS that should be used for
     *           returned feature geometries. The value may be the WFS StorageSRS value,
     *           DefaultRetrievalSRS value, or one of AdditionalSRS values. If no srsName value is
     *           supplied, then the features will be returned using either the DefaultRetrievalSRS, if
     *           specified, and StorageSRS otherwise. For feature types with no spatial properties, this
     *           attribute must not be specified or ignored if it is specified.
     *         
     * <!-- end-model-doc -->
     * @return the value of the '<em>Srs Name</em>' attribute.
     * @see #setSrsName(String)
     * @see net.opengis.wfsv.WfsvPackage#getDifferenceQueryType_SrsName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='srsName'"
     * @generated
     */
    String getSrsName();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DifferenceQueryType#getSrsName <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' attribute.
     * @see #getSrsName()
     * @generated
     */
    void setSrsName(String value);

    /**
     * Returns the value of the '<em><b>To Feature Version</b></em>' attribute.
     * The default value is <code>"LAST"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *           Same as featureVersion in QueryType, indicates the second version to be used for
     *           performing a difference of log operation.
     *         
     * <!-- end-model-doc -->
     * @return the value of the '<em>To Feature Version</em>' attribute.
     * @see #isSetToFeatureVersion()
     * @see #unsetToFeatureVersion()
     * @see #setToFeatureVersion(String)
     * @see net.opengis.wfsv.WfsvPackage#getDifferenceQueryType_ToFeatureVersion()
     * @model default="LAST" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='toFeatureVersion'"
     * @generated
     */
    String getToFeatureVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DifferenceQueryType#getToFeatureVersion <em>To Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>To Feature Version</em>' attribute.
     * @see #isSetToFeatureVersion()
     * @see #unsetToFeatureVersion()
     * @see #getToFeatureVersion()
     * @generated
     */
    void setToFeatureVersion(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfsv.DifferenceQueryType#getToFeatureVersion <em>To Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetToFeatureVersion()
     * @see #getToFeatureVersion()
     * @see #setToFeatureVersion(String)
     * @generated
     */
    void unsetToFeatureVersion();

    /**
     * Returns whether the value of the '{@link net.opengis.wfsv.DifferenceQueryType#getToFeatureVersion <em>To Feature Version</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>To Feature Version</em>' attribute is set.
     * @see #unsetToFeatureVersion()
     * @see #getToFeatureVersion()
     * @see #setToFeatureVersion(String)
     * @generated
     */
    boolean isSetToFeatureVersion();

    /**
     * Returns the value of the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *           The typeName attribute is a single feature type name that indicates which type of feature
     *           instances should be included in the reponse set. The names must be a valid type that
     *           belong to this query's feature content as defined by the GML Application Schema.
     *         
     * <!-- end-model-doc -->
     * @return the value of the '<em>Type Name</em>' attribute.
     * @see #setTypeName(QName)
     * @see net.opengis.wfsv.WfsvPackage#getDifferenceQueryType_TypeName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='attribute' name='typeName'"
     * @generated
     */
    QName getTypeName();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DifferenceQueryType#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
    void setTypeName(QName value);

} // DifferenceQueryType
