/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.math.BigInteger;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.cat.csw20.AbstractRecordType;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.ElementSetType;
import net.opengis.cat.csw20.SearchResultsType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Search Results Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getAbstractRecordGroup <em>Abstract Record Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getAbstractRecord <em>Abstract Record</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getElementSet <em>Element Set</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getExpires <em>Expires</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getNextRecord <em>Next Record</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getNumberOfRecordsMatched <em>Number Of Records Matched</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getNumberOfRecordsReturned <em>Number Of Records Returned</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getRecordSchema <em>Record Schema</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.SearchResultsTypeImpl#getResultSetId <em>Result Set Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SearchResultsTypeImpl extends EObjectImpl implements SearchResultsType {
    /**
     * The cached value of the '{@link #getAbstractRecordGroup() <em>Abstract Record Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractRecordGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap abstractRecordGroup;

    /**
     * The cached value of the '{@link #getAny() <em>Any</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAny()
     * @generated
     * @ordered
     */
    protected FeatureMap any;

    /**
     * The default value of the '{@link #getElementSet() <em>Element Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElementSet()
     * @generated
     * @ordered
     */
    protected static final ElementSetType ELEMENT_SET_EDEFAULT = ElementSetType.BRIEF;

    /**
     * The cached value of the '{@link #getElementSet() <em>Element Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElementSet()
     * @generated
     * @ordered
     */
    protected ElementSetType elementSet = ELEMENT_SET_EDEFAULT;

    /**
     * This is true if the Element Set attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean elementSetESet;

    /**
     * The default value of the '{@link #getExpires() <em>Expires</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExpires()
     * @generated
     * @ordered
     */
    protected static final XMLGregorianCalendar EXPIRES_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getExpires() <em>Expires</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExpires()
     * @generated
     * @ordered
     */
    protected XMLGregorianCalendar expires = EXPIRES_EDEFAULT;

    /**
     * The default value of the '{@link #getNextRecord() <em>Next Record</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNextRecord()
     * @generated
     * @ordered
     */
    protected static final BigInteger NEXT_RECORD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNextRecord() <em>Next Record</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNextRecord()
     * @generated
     * @ordered
     */
    protected BigInteger nextRecord = NEXT_RECORD_EDEFAULT;

    /**
     * The default value of the '{@link #getNumberOfRecordsMatched() <em>Number Of Records Matched</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumberOfRecordsMatched()
     * @generated
     * @ordered
     */
    protected static final BigInteger NUMBER_OF_RECORDS_MATCHED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNumberOfRecordsMatched() <em>Number Of Records Matched</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumberOfRecordsMatched()
     * @generated
     * @ordered
     */
    protected BigInteger numberOfRecordsMatched = NUMBER_OF_RECORDS_MATCHED_EDEFAULT;

    /**
     * The default value of the '{@link #getNumberOfRecordsReturned() <em>Number Of Records Returned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumberOfRecordsReturned()
     * @generated
     * @ordered
     */
    protected static final BigInteger NUMBER_OF_RECORDS_RETURNED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNumberOfRecordsReturned() <em>Number Of Records Returned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumberOfRecordsReturned()
     * @generated
     * @ordered
     */
    protected BigInteger numberOfRecordsReturned = NUMBER_OF_RECORDS_RETURNED_EDEFAULT;

    /**
     * The default value of the '{@link #getRecordSchema() <em>Record Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRecordSchema()
     * @generated
     * @ordered
     */
    protected static final String RECORD_SCHEMA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRecordSchema() <em>Record Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRecordSchema()
     * @generated
     * @ordered
     */
    protected String recordSchema = RECORD_SCHEMA_EDEFAULT;

    /**
     * The default value of the '{@link #getResultSetId() <em>Result Set Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResultSetId()
     * @generated
     * @ordered
     */
    protected static final String RESULT_SET_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getResultSetId() <em>Result Set Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResultSetId()
     * @generated
     * @ordered
     */
    protected String resultSetId = RESULT_SET_ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SearchResultsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.SEARCH_RESULTS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAbstractRecordGroup() {
        if (abstractRecordGroup == null) {
            abstractRecordGroup = new BasicFeatureMap(this, Csw20Package.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD_GROUP);
        }
        return abstractRecordGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractRecordType> getAbstractRecord() {
        return getAbstractRecordGroup().list(Csw20Package.Literals.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAny() {
        if (any == null) {
            any = new BasicFeatureMap(this, Csw20Package.SEARCH_RESULTS_TYPE__ANY);
        }
        return any;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ElementSetType getElementSet() {
        return elementSet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setElementSet(ElementSetType newElementSet) {
        ElementSetType oldElementSet = elementSet;
        elementSet = newElementSet == null ? ELEMENT_SET_EDEFAULT : newElementSet;
        boolean oldElementSetESet = elementSetESet;
        elementSetESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SEARCH_RESULTS_TYPE__ELEMENT_SET, oldElementSet, elementSet, !oldElementSetESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetElementSet() {
        ElementSetType oldElementSet = elementSet;
        boolean oldElementSetESet = elementSetESet;
        elementSet = ELEMENT_SET_EDEFAULT;
        elementSetESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.SEARCH_RESULTS_TYPE__ELEMENT_SET, oldElementSet, ELEMENT_SET_EDEFAULT, oldElementSetESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetElementSet() {
        return elementSetESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XMLGregorianCalendar getExpires() {
        return expires;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExpires(XMLGregorianCalendar newExpires) {
        XMLGregorianCalendar oldExpires = expires;
        expires = newExpires;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SEARCH_RESULTS_TYPE__EXPIRES, oldExpires, expires));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getNextRecord() {
        return nextRecord;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNextRecord(BigInteger newNextRecord) {
        BigInteger oldNextRecord = nextRecord;
        nextRecord = newNextRecord;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SEARCH_RESULTS_TYPE__NEXT_RECORD, oldNextRecord, nextRecord));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getNumberOfRecordsMatched() {
        return numberOfRecordsMatched;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNumberOfRecordsMatched(BigInteger newNumberOfRecordsMatched) {
        BigInteger oldNumberOfRecordsMatched = numberOfRecordsMatched;
        numberOfRecordsMatched = newNumberOfRecordsMatched;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_MATCHED, oldNumberOfRecordsMatched, numberOfRecordsMatched));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getNumberOfRecordsReturned() {
        return numberOfRecordsReturned;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNumberOfRecordsReturned(BigInteger newNumberOfRecordsReturned) {
        BigInteger oldNumberOfRecordsReturned = numberOfRecordsReturned;
        numberOfRecordsReturned = newNumberOfRecordsReturned;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_RETURNED, oldNumberOfRecordsReturned, numberOfRecordsReturned));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRecordSchema() {
        return recordSchema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRecordSchema(String newRecordSchema) {
        String oldRecordSchema = recordSchema;
        recordSchema = newRecordSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SEARCH_RESULTS_TYPE__RECORD_SCHEMA, oldRecordSchema, recordSchema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getResultSetId() {
        return resultSetId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResultSetId(String newResultSetId) {
        String oldResultSetId = resultSetId;
        resultSetId = newResultSetId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.SEARCH_RESULTS_TYPE__RESULT_SET_ID, oldResultSetId, resultSetId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD_GROUP:
                return ((InternalEList<?>)getAbstractRecordGroup()).basicRemove(otherEnd, msgs);
            case Csw20Package.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD:
                return ((InternalEList<?>)getAbstractRecord()).basicRemove(otherEnd, msgs);
            case Csw20Package.SEARCH_RESULTS_TYPE__ANY:
                return ((InternalEList<?>)getAny()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD_GROUP:
                if (coreType) return getAbstractRecordGroup();
                return ((FeatureMap.Internal)getAbstractRecordGroup()).getWrapper();
            case Csw20Package.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD:
                return getAbstractRecord();
            case Csw20Package.SEARCH_RESULTS_TYPE__ANY:
                if (coreType) return getAny();
                return ((FeatureMap.Internal)getAny()).getWrapper();
            case Csw20Package.SEARCH_RESULTS_TYPE__ELEMENT_SET:
                return getElementSet();
            case Csw20Package.SEARCH_RESULTS_TYPE__EXPIRES:
                return getExpires();
            case Csw20Package.SEARCH_RESULTS_TYPE__NEXT_RECORD:
                return getNextRecord();
            case Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_MATCHED:
                return getNumberOfRecordsMatched();
            case Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_RETURNED:
                return getNumberOfRecordsReturned();
            case Csw20Package.SEARCH_RESULTS_TYPE__RECORD_SCHEMA:
                return getRecordSchema();
            case Csw20Package.SEARCH_RESULTS_TYPE__RESULT_SET_ID:
                return getResultSetId();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Csw20Package.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD_GROUP:
                ((FeatureMap.Internal)getAbstractRecordGroup()).set(newValue);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__ANY:
                ((FeatureMap.Internal)getAny()).set(newValue);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__ELEMENT_SET:
                setElementSet((ElementSetType)newValue);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__EXPIRES:
                setExpires((XMLGregorianCalendar) newValue);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__NEXT_RECORD:
                setNextRecord((BigInteger)newValue);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_MATCHED:
                setNumberOfRecordsMatched((BigInteger)newValue);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_RETURNED:
                setNumberOfRecordsReturned((BigInteger)newValue);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__RECORD_SCHEMA:
                setRecordSchema((String)newValue);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__RESULT_SET_ID:
                setResultSetId((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Csw20Package.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD_GROUP:
                getAbstractRecordGroup().clear();
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__ANY:
                getAny().clear();
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__ELEMENT_SET:
                unsetElementSet();
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__EXPIRES:
                setExpires(EXPIRES_EDEFAULT);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__NEXT_RECORD:
                setNextRecord(NEXT_RECORD_EDEFAULT);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_MATCHED:
                setNumberOfRecordsMatched(NUMBER_OF_RECORDS_MATCHED_EDEFAULT);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_RETURNED:
                setNumberOfRecordsReturned(NUMBER_OF_RECORDS_RETURNED_EDEFAULT);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__RECORD_SCHEMA:
                setRecordSchema(RECORD_SCHEMA_EDEFAULT);
                return;
            case Csw20Package.SEARCH_RESULTS_TYPE__RESULT_SET_ID:
                setResultSetId(RESULT_SET_ID_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Csw20Package.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD_GROUP:
                return abstractRecordGroup != null && !abstractRecordGroup.isEmpty();
            case Csw20Package.SEARCH_RESULTS_TYPE__ABSTRACT_RECORD:
                return !getAbstractRecord().isEmpty();
            case Csw20Package.SEARCH_RESULTS_TYPE__ANY:
                return any != null && !any.isEmpty();
            case Csw20Package.SEARCH_RESULTS_TYPE__ELEMENT_SET:
                return isSetElementSet();
            case Csw20Package.SEARCH_RESULTS_TYPE__EXPIRES:
                return EXPIRES_EDEFAULT == null ? expires != null : !EXPIRES_EDEFAULT.equals(expires);
            case Csw20Package.SEARCH_RESULTS_TYPE__NEXT_RECORD:
                return NEXT_RECORD_EDEFAULT == null ? nextRecord != null : !NEXT_RECORD_EDEFAULT.equals(nextRecord);
            case Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_MATCHED:
                return NUMBER_OF_RECORDS_MATCHED_EDEFAULT == null ? numberOfRecordsMatched != null : !NUMBER_OF_RECORDS_MATCHED_EDEFAULT.equals(numberOfRecordsMatched);
            case Csw20Package.SEARCH_RESULTS_TYPE__NUMBER_OF_RECORDS_RETURNED:
                return NUMBER_OF_RECORDS_RETURNED_EDEFAULT == null ? numberOfRecordsReturned != null : !NUMBER_OF_RECORDS_RETURNED_EDEFAULT.equals(numberOfRecordsReturned);
            case Csw20Package.SEARCH_RESULTS_TYPE__RECORD_SCHEMA:
                return RECORD_SCHEMA_EDEFAULT == null ? recordSchema != null : !RECORD_SCHEMA_EDEFAULT.equals(recordSchema);
            case Csw20Package.SEARCH_RESULTS_TYPE__RESULT_SET_ID:
                return RESULT_SET_ID_EDEFAULT == null ? resultSetId != null : !RESULT_SET_ID_EDEFAULT.equals(resultSetId);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (abstractRecordGroup: ");
        result.append(abstractRecordGroup);
        result.append(", any: ");
        result.append(any);
        result.append(", elementSet: ");
        if (elementSetESet) result.append(elementSet); else result.append("<unset>");
        result.append(", expires: ");
        result.append(expires);
        result.append(", nextRecord: ");
        result.append(nextRecord);
        result.append(", numberOfRecordsMatched: ");
        result.append(numberOfRecordsMatched);
        result.append(", numberOfRecordsReturned: ");
        result.append(numberOfRecordsReturned);
        result.append(", recordSchema: ");
        result.append(recordSchema);
        result.append(", resultSetId: ");
        result.append(resultSetId);
        result.append(')');
        return result.toString();
    }

} //SearchResultsTypeImpl
