/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.math.BigInteger;
import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Search Results Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Includes representations of result set members if maxRecords > 0.
 *          The items must conform to one of the csw:Record views or a
 *          profile-specific representation.
 * 
 *          resultSetId  - id of the result set (a URI).
 *          elementSet  - The element set that has been returned
 *                        (i.e., "brief", "summary", "full")
 *          recordSchema  - schema reference for included records(URI)
 *          numberOfRecordsMatched  - number of records matched by the query
 *          numberOfRecordsReturned - number of records returned to client
 *          nextRecord - position of next record in the result set
 *                       (0 if no records remain).
 *          expires - the time instant when the result set expires and
 *                    is discarded (ISO 8601 format)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getAbstractRecordGroup <em>Abstract Record Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getAbstractRecord <em>Abstract Record</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getElementSet <em>Element Set</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getExpires <em>Expires</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getNextRecord <em>Next Record</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getNumberOfRecordsMatched <em>Number Of Records Matched</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getNumberOfRecordsReturned <em>Number Of Records Returned</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getRecordSchema <em>Record Schema</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SearchResultsType#getResultSetId <em>Result Set Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType()
 * @model extendedMetaData="name='SearchResultsType' kind='elementOnly'"
 * @generated
 */
public interface SearchResultsType extends EObject {
    /**
     * Returns the value of the '<em><b>Abstract Record Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Record Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Record Group</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_AbstractRecordGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='AbstractRecord:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getAbstractRecordGroup();

    /**
     * Returns the value of the '<em><b>Abstract Record</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.AbstractRecordType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Record</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Record</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_AbstractRecord()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractRecord' namespace='##targetNamespace' group='AbstractRecord:group'"
     * @generated
     */
    EList<AbstractRecordType> getAbstractRecord();

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
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_Any()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':2' processing='strict'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Element Set</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.cat.csw20.ElementSetType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element Set</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Set</em>' attribute.
     * @see net.opengis.cat.csw20.ElementSetType
     * @see #isSetElementSet()
     * @see #unsetElementSet()
     * @see #setElementSet(ElementSetType)
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_ElementSet()
     * @model unsettable="true"
     *        extendedMetaData="kind='attribute' name='elementSet'"
     * @generated
     */
    ElementSetType getElementSet();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SearchResultsType#getElementSet <em>Element Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Set</em>' attribute.
     * @see net.opengis.cat.csw20.ElementSetType
     * @see #isSetElementSet()
     * @see #unsetElementSet()
     * @see #getElementSet()
     * @generated
     */
    void setElementSet(ElementSetType value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.SearchResultsType#getElementSet <em>Element Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetElementSet()
     * @see #getElementSet()
     * @see #setElementSet(ElementSetType)
     * @generated
     */
    void unsetElementSet();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.SearchResultsType#getElementSet <em>Element Set</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Element Set</em>' attribute is set.
     * @see #unsetElementSet()
     * @see #getElementSet()
     * @see #setElementSet(ElementSetType)
     * @generated
     */
    boolean isSetElementSet();

    /**
     * Returns the value of the '<em><b>Expires</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expires</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expires</em>' attribute.
     * @see #setExpires(XMLGregorianCalendar)
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_Expires()
     * @model 
     */
    Calendar getExpires();


    /**
     * Returns the value of the '<em><b>Next Record</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Next Record</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Next Record</em>' attribute.
     * @see #setNextRecord(BigInteger)
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_NextRecord()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
     *        extendedMetaData="kind='attribute' name='nextRecord'"
     * @generated
     */
    BigInteger getNextRecord();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SearchResultsType#getNextRecord <em>Next Record</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Next Record</em>' attribute.
     * @see #getNextRecord()
     * @generated
     */
    void setNextRecord(BigInteger value);

    /**
     * Returns the value of the '<em><b>Number Of Records Matched</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Number Of Records Matched</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Number Of Records Matched</em>' attribute.
     * @see #setNumberOfRecordsMatched(BigInteger)
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_NumberOfRecordsMatched()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" required="true"
     *        extendedMetaData="kind='attribute' name='numberOfRecordsMatched'"
     * @generated
     */
    BigInteger getNumberOfRecordsMatched();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SearchResultsType#getNumberOfRecordsMatched <em>Number Of Records Matched</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Number Of Records Matched</em>' attribute.
     * @see #getNumberOfRecordsMatched()
     * @generated
     */
    void setNumberOfRecordsMatched(BigInteger value);

    /**
     * Returns the value of the '<em><b>Number Of Records Returned</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Number Of Records Returned</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Number Of Records Returned</em>' attribute.
     * @see #setNumberOfRecordsReturned(BigInteger)
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_NumberOfRecordsReturned()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" required="true"
     *        extendedMetaData="kind='attribute' name='numberOfRecordsReturned'"
     * @generated
     */
    BigInteger getNumberOfRecordsReturned();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SearchResultsType#getNumberOfRecordsReturned <em>Number Of Records Returned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Number Of Records Returned</em>' attribute.
     * @see #getNumberOfRecordsReturned()
     * @generated
     */
    void setNumberOfRecordsReturned(BigInteger value);

    /**
     * Returns the value of the '<em><b>Record Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Record Schema</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Record Schema</em>' attribute.
     * @see #setRecordSchema(String)
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_RecordSchema()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='recordSchema'"
     * @generated
     */
    String getRecordSchema();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SearchResultsType#getRecordSchema <em>Record Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Record Schema</em>' attribute.
     * @see #getRecordSchema()
     * @generated
     */
    void setRecordSchema(String value);

    /**
     * Returns the value of the '<em><b>Result Set Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Result Set Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Result Set Id</em>' attribute.
     * @see #setResultSetId(String)
     * @see net.opengis.cat.csw20.Csw20Package#getSearchResultsType_ResultSetId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='resultSetId'"
     * @generated
     */
    String getResultSetId();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SearchResultsType#getResultSetId <em>Result Set Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Result Set Id</em>' attribute.
     * @see #getResultSetId()
     * @generated
     */
    void setResultSetId(String value);

} // SearchResultsType
