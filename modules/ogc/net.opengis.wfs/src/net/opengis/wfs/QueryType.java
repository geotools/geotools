/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.net.URI;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             The Query element is of type QueryType.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.QueryType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getXlinkPropertyName <em>Xlink Property Name</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getFunction <em>Function</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getSortBy <em>Sort By</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getFeatureVersion <em>Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfs.QueryType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getQueryType()
 * @model extendedMetaData="name='QueryType' kind='elementOnly'"
 * @generated
 */
public interface QueryType extends EObject {
	/**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see net.opengis.wfs.WfsPackage#getQueryType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Property Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                    The Property element is used to specify one or more
	 *                    properties of a feature whose values are to be retrieved
	 *                    by a Web Feature Service.
	 * 
	 *                    While a Web Feature Service should endeavour to satisfy
	 *                    the exact request specified, in some instance this may
	 *                    not be possible.  Specifically, a Web Feature Service
	 *                    must generate a valid GML3 response to a Query operation.
	 *                    The schema used to generate the output may include
	 *                    properties that are mandatory.  In order that the output
	 *                    validates, these mandatory properties must be specified
	 *                    in the request.  If they are not, a Web Feature Service
	 *                    may add them automatically to the Query before processing
	 *                    it.  Thus a client application should, in general, be
	 *                    prepared to receive more properties than it requested.
	 * 
	 *                    Of course, using the DescribeFeatureType request, a client
	 *                    application can determine which properties are mandatory
	 *                    and request them in the first place.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Property Name</em>' attribute.
	 * @see #setPropertyName(String)
	 * @see net.opengis.wfs.WFSPackage#getQueryType_PropertyName()
	 * @model type="java.lang.String"
	 */
	EList getPropertyName();

	/**
     * Returns the value of the '<em><b>Xlink Property Name</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.XlinkPropertyNameType}.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Xlink Property Name</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Xlink Property Name</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getQueryType_XlinkPropertyName()
     * @model type="net.opengis.wfs.XlinkPropertyNameType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='XlinkPropertyName' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
	EList getXlinkPropertyName();

	/**
	 * Returns the value of the '<em><b>Function</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Object}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                    A function may be used as a select item in a query.
	 *                    However, if a function is used, care must be taken
	 *                    to ensure that the result type matches the type in the 
	 *  
	 *                 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Function</em>' attribute list.
	 * @see net.opengis.wfs.WFSPackage#getQueryType_Function()
	 * @model type="org.opengis.filter.expression.Function"
	 */
	EList getFunction();

	/**
	 * Returns the value of the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                 The Filter element is used to define spatial and/or non-spatial
	 *                 constraints on query.  Spatial constrains use GML3 to specify
	 *                 the constraining geometry.  A full description of the Filter
	 *                 element can be found in the Filter Encoding Implementation
	 *                 Specification.
	 *              
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Filter</em>' attribute.
	 * @see #setFilter(Object)
	 * @see net.opengis.wfs.WFSPackage#getQueryType_Filter()
	 * @model 
	 */
	Filter getFilter();

	/**
     * Sets the value of the '{@link net.opengis.wfs.QueryType#getFilter <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter</em>' attribute.
     * @see #getFilter()
     * @generated
     */
	void setFilter(Filter value);

	/**
	 * Returns the value of the '<em><b>Sort By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                 The SortBy element is used specify property names whose
	 *                 values should be used to order (upon presentation) the
	 *                 set of feature instances that satisfy the query.
	 *              
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Sort By</em>' attribute.
	 * @see #setSortBy(Object)
	 * @see net.opengis.wfs.WFSPackage#getQueryType_SortBy()
	 * @model type="org.opengis.filter.sort.SortBy"
	 */
	EList getSortBy();

	/**
     * Returns the value of the '<em><b>Feature Version</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *               For systems that implement versioning, the featureVersion
     *               attribute is used to specify which version of a particular
     *               feature instance is to be retrieved.  A value of ALL means
     *               that all versions should be retrieved.  An integer value
     *               'i', means that the ith version should be retrieve if it
     *               exists or the most recent version otherwise.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Feature Version</em>' attribute.
     * @see #setFeatureVersion(String)
     * @see net.opengis.wfs.WfsPackage#getQueryType_FeatureVersion()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='featureVersion'"
     * @generated
     */
	String getFeatureVersion();

	/**
     * Sets the value of the '{@link net.opengis.wfs.QueryType#getFeatureVersion <em>Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Version</em>' attribute.
     * @see #getFeatureVersion()
     * @generated
     */
	void setFeatureVersion(String value);

	/**
     * Returns the value of the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                The handle attribute allows a client application
     *                to assign a client-generated identifier for the
     *                Query.  The handle is included to facilitate error
     *                reporting.  If one Query in a GetFeature request
     *                causes an exception, a WFS may report the handle
     *                to indicate which query element failed.  If the a
     *                handle is not present, the WFS may use other means
     *                to localize the error (e.g. line numbers).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.wfs.WfsPackage#getQueryType_Handle()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
	String getHandle();

	/**
     * Sets the value of the '{@link net.opengis.wfs.QueryType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
	void setHandle(String value);

	/**
	 * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *               This attribute is used to specify a specific WFS-supported SRS
	 *               that should be used for returned feature geometries.  The value
	 *               may be the WFS StorageSRS value, DefaultRetrievalSRS value, or
	 *               one of AdditionalSRS values.  If no srsName value is supplied,
	 *               then the features will be returned using either the
	 *               DefaultRetrievalSRS, if specified, and StorageSRS otherwise.
	 *               For feature types with no spatial properties, this attribute
	 *               must not be specified or ignored if it is specified.
	 *            
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Srs Name</em>' attribute.
	 * @see #setSrsName(String)
	 * @see net.opengis.wfs.WFSPackage#getQueryType_SrsName()
	 * @model 
	 */
	URI getSrsName();

	/**
     * Sets the value of the '{@link net.opengis.wfs.QueryType#getSrsName <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' attribute.
     * @see #getSrsName()
     * @generated
     */
	void setSrsName(URI value);

	/**
     * Returns the value of the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *               The typeName attribute is a list of one or more
     *               feature type names that indicate which types
     *               of feature instances should be included in the
     *               reponse set.  Specifying more than one typename
     *               indicates that a join operation is being performed.
     *               All the names in the typeName list must be valid
     *               types that belong to this query's feature content
     *               as defined by the GML Application Schema.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Type Name</em>' attribute.
     * @see #setTypeName(List)
     * @see net.opengis.wfs.WfsPackage#getQueryType_TypeName()
     * @model unique="false" dataType="net.opengis.wfs.TypeNameListType" required="true" many="false"
     *        extendedMetaData="kind='attribute' name='typeName'"
     * @generated
     */
	List getTypeName();

	/**
     * Sets the value of the '{@link net.opengis.wfs.QueryType#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
	void setTypeName(List value);

} // QueryType
