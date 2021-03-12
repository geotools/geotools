/**
 */
package net.opengis.wps20.impl;

import java.math.BigInteger;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.wps20.StatusInfoType;
import net.opengis.wps20.Wps20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Status Info Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.impl.StatusInfoTypeImpl#getJobID <em>Job ID</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.StatusInfoTypeImpl#getStatus <em>Status</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.StatusInfoTypeImpl#getExpirationDate <em>Expiration Date</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.StatusInfoTypeImpl#getEstimatedCompletion <em>Estimated Completion</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.StatusInfoTypeImpl#getNextPoll <em>Next Poll</em>}</li>
 *   <li>{@link net.opengis.wps20.impl.StatusInfoTypeImpl#getPercentCompleted <em>Percent Completed</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StatusInfoTypeImpl extends MinimalEObjectImpl.Container implements StatusInfoType {
	/**
	 * The default value of the '{@link #getJobID() <em>Job ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJobID()
	 * @generated
	 * @ordered
	 */
	protected static final String JOB_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getJobID() <em>Job ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJobID()
	 * @generated
	 * @ordered
	 */
	protected String jobID = JOB_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
	protected static final Object STATUS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
	protected Object status = STATUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getExpirationDate() <em>Expiration Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpirationDate()
	 * @generated
	 * @ordered
	 */
	protected static final XMLGregorianCalendar EXPIRATION_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExpirationDate() <em>Expiration Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpirationDate()
	 * @generated
	 * @ordered
	 */
	protected XMLGregorianCalendar expirationDate = EXPIRATION_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getEstimatedCompletion() <em>Estimated Completion</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEstimatedCompletion()
	 * @generated
	 * @ordered
	 */
	protected static final XMLGregorianCalendar ESTIMATED_COMPLETION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEstimatedCompletion() <em>Estimated Completion</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEstimatedCompletion()
	 * @generated
	 * @ordered
	 */
	protected XMLGregorianCalendar estimatedCompletion = ESTIMATED_COMPLETION_EDEFAULT;

	/**
	 * The default value of the '{@link #getNextPoll() <em>Next Poll</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextPoll()
	 * @generated
	 * @ordered
	 */
	protected static final XMLGregorianCalendar NEXT_POLL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNextPoll() <em>Next Poll</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextPoll()
	 * @generated
	 * @ordered
	 */
	protected XMLGregorianCalendar nextPoll = NEXT_POLL_EDEFAULT;

	/**
	 * The default value of the '{@link #getPercentCompleted() <em>Percent Completed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercentCompleted()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger PERCENT_COMPLETED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPercentCompleted() <em>Percent Completed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercentCompleted()
	 * @generated
	 * @ordered
	 */
	protected BigInteger percentCompleted = PERCENT_COMPLETED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StatusInfoTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Wps20Package.Literals.STATUS_INFO_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getJobID() {
		return jobID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJobID(String newJobID) {
		String oldJobID = jobID;
		jobID = newJobID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.STATUS_INFO_TYPE__JOB_ID, oldJobID, jobID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getStatus() {
		return status;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStatus(Object newStatus) {
		Object oldStatus = status;
		status = newStatus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.STATUS_INFO_TYPE__STATUS, oldStatus, status));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XMLGregorianCalendar getExpirationDate() {
		return expirationDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpirationDate(XMLGregorianCalendar newExpirationDate) {
		XMLGregorianCalendar oldExpirationDate = expirationDate;
		expirationDate = newExpirationDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.STATUS_INFO_TYPE__EXPIRATION_DATE, oldExpirationDate, expirationDate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XMLGregorianCalendar getEstimatedCompletion() {
		return estimatedCompletion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEstimatedCompletion(XMLGregorianCalendar newEstimatedCompletion) {
		XMLGregorianCalendar oldEstimatedCompletion = estimatedCompletion;
		estimatedCompletion = newEstimatedCompletion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.STATUS_INFO_TYPE__ESTIMATED_COMPLETION, oldEstimatedCompletion, estimatedCompletion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XMLGregorianCalendar getNextPoll() {
		return nextPoll;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNextPoll(XMLGregorianCalendar newNextPoll) {
		XMLGregorianCalendar oldNextPoll = nextPoll;
		nextPoll = newNextPoll;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.STATUS_INFO_TYPE__NEXT_POLL, oldNextPoll, nextPoll));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getPercentCompleted() {
		return percentCompleted;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPercentCompleted(BigInteger newPercentCompleted) {
		BigInteger oldPercentCompleted = percentCompleted;
		percentCompleted = newPercentCompleted;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wps20Package.STATUS_INFO_TYPE__PERCENT_COMPLETED, oldPercentCompleted, percentCompleted));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wps20Package.STATUS_INFO_TYPE__JOB_ID:
				return getJobID();
			case Wps20Package.STATUS_INFO_TYPE__STATUS:
				return getStatus();
			case Wps20Package.STATUS_INFO_TYPE__EXPIRATION_DATE:
				return getExpirationDate();
			case Wps20Package.STATUS_INFO_TYPE__ESTIMATED_COMPLETION:
				return getEstimatedCompletion();
			case Wps20Package.STATUS_INFO_TYPE__NEXT_POLL:
				return getNextPoll();
			case Wps20Package.STATUS_INFO_TYPE__PERCENT_COMPLETED:
				return getPercentCompleted();
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
			case Wps20Package.STATUS_INFO_TYPE__JOB_ID:
				setJobID((String)newValue);
				return;
			case Wps20Package.STATUS_INFO_TYPE__STATUS:
				setStatus(newValue);
				return;
			case Wps20Package.STATUS_INFO_TYPE__EXPIRATION_DATE:
				setExpirationDate((XMLGregorianCalendar)newValue);
				return;
			case Wps20Package.STATUS_INFO_TYPE__ESTIMATED_COMPLETION:
				setEstimatedCompletion((XMLGregorianCalendar)newValue);
				return;
			case Wps20Package.STATUS_INFO_TYPE__NEXT_POLL:
				setNextPoll((XMLGregorianCalendar)newValue);
				return;
			case Wps20Package.STATUS_INFO_TYPE__PERCENT_COMPLETED:
				setPercentCompleted((BigInteger)newValue);
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
			case Wps20Package.STATUS_INFO_TYPE__JOB_ID:
				setJobID(JOB_ID_EDEFAULT);
				return;
			case Wps20Package.STATUS_INFO_TYPE__STATUS:
				setStatus(STATUS_EDEFAULT);
				return;
			case Wps20Package.STATUS_INFO_TYPE__EXPIRATION_DATE:
				setExpirationDate(EXPIRATION_DATE_EDEFAULT);
				return;
			case Wps20Package.STATUS_INFO_TYPE__ESTIMATED_COMPLETION:
				setEstimatedCompletion(ESTIMATED_COMPLETION_EDEFAULT);
				return;
			case Wps20Package.STATUS_INFO_TYPE__NEXT_POLL:
				setNextPoll(NEXT_POLL_EDEFAULT);
				return;
			case Wps20Package.STATUS_INFO_TYPE__PERCENT_COMPLETED:
				setPercentCompleted(PERCENT_COMPLETED_EDEFAULT);
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
			case Wps20Package.STATUS_INFO_TYPE__JOB_ID:
				return JOB_ID_EDEFAULT == null ? jobID != null : !JOB_ID_EDEFAULT.equals(jobID);
			case Wps20Package.STATUS_INFO_TYPE__STATUS:
				return STATUS_EDEFAULT == null ? status != null : !STATUS_EDEFAULT.equals(status);
			case Wps20Package.STATUS_INFO_TYPE__EXPIRATION_DATE:
				return EXPIRATION_DATE_EDEFAULT == null ? expirationDate != null : !EXPIRATION_DATE_EDEFAULT.equals(expirationDate);
			case Wps20Package.STATUS_INFO_TYPE__ESTIMATED_COMPLETION:
				return ESTIMATED_COMPLETION_EDEFAULT == null ? estimatedCompletion != null : !ESTIMATED_COMPLETION_EDEFAULT.equals(estimatedCompletion);
			case Wps20Package.STATUS_INFO_TYPE__NEXT_POLL:
				return NEXT_POLL_EDEFAULT == null ? nextPoll != null : !NEXT_POLL_EDEFAULT.equals(nextPoll);
			case Wps20Package.STATUS_INFO_TYPE__PERCENT_COMPLETED:
				return PERCENT_COMPLETED_EDEFAULT == null ? percentCompleted != null : !PERCENT_COMPLETED_EDEFAULT.equals(percentCompleted);
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (jobID: ");
		result.append(jobID);
		result.append(", status: ");
		result.append(status);
		result.append(", expirationDate: ");
		result.append(expirationDate);
		result.append(", estimatedCompletion: ");
		result.append(estimatedCompletion);
		result.append(", nextPoll: ");
		result.append(nextPoll);
		result.append(", percentCompleted: ");
		result.append(percentCompleted);
		result.append(')');
		return result.toString();
	}

} //StatusInfoTypeImpl
