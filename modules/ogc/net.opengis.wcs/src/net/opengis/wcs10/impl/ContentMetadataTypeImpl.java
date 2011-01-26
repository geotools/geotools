/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.wcs10.ContentMetadataType;
import net.opengis.wcs10.CoverageOfferingBriefType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Content Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getCoverageOfferingBrief <em>Coverage Offering Brief</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getActuate <em>Actuate</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getRemoteSchema <em>Remote Schema</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getShow <em>Show</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ContentMetadataTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContentMetadataTypeImpl extends EObjectImpl implements ContentMetadataType {
    /**
	 * The cached value of the '{@link #getCoverageOfferingBrief() <em>Coverage Offering Brief</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCoverageOfferingBrief()
	 * @generated
	 * @ordered
	 */
    protected EList coverageOfferingBrief;

    /**
	 * The default value of the '{@link #getActuate() <em>Actuate</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getActuate()
	 * @generated
	 * @ordered
	 */
    protected static final ActuateType ACTUATE_EDEFAULT = ActuateType.ON_LOAD_LITERAL;

    /**
	 * The cached value of the '{@link #getActuate() <em>Actuate</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getActuate()
	 * @generated
	 * @ordered
	 */
    protected ActuateType actuate = ACTUATE_EDEFAULT;

    /**
	 * This is true if the Actuate attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean actuateESet;

    /**
	 * The default value of the '{@link #getArcrole() <em>Arcrole</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getArcrole()
	 * @generated
	 * @ordered
	 */
    protected static final String ARCROLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getArcrole() <em>Arcrole</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getArcrole()
	 * @generated
	 * @ordered
	 */
    protected String arcrole = ARCROLE_EDEFAULT;

    /**
	 * The default value of the '{@link #getHref() <em>Href</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getHref()
	 * @generated
	 * @ordered
	 */
    protected static final String HREF_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getHref() <em>Href</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getHref()
	 * @generated
	 * @ordered
	 */
    protected String href = HREF_EDEFAULT;

    /**
	 * The default value of the '{@link #getRemoteSchema() <em>Remote Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRemoteSchema()
	 * @generated
	 * @ordered
	 */
    protected static final String REMOTE_SCHEMA_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getRemoteSchema() <em>Remote Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRemoteSchema()
	 * @generated
	 * @ordered
	 */
    protected String remoteSchema = REMOTE_SCHEMA_EDEFAULT;

    /**
	 * The default value of the '{@link #getRole() <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRole()
	 * @generated
	 * @ordered
	 */
    protected static final String ROLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getRole() <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRole()
	 * @generated
	 * @ordered
	 */
    protected String role = ROLE_EDEFAULT;

    /**
	 * The default value of the '{@link #getShow() <em>Show</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getShow()
	 * @generated
	 * @ordered
	 */
    protected static final ShowType SHOW_EDEFAULT = ShowType.NEW_LITERAL;

    /**
	 * The cached value of the '{@link #getShow() <em>Show</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getShow()
	 * @generated
	 * @ordered
	 */
    protected ShowType show = SHOW_EDEFAULT;

    /**
	 * This is true if the Show attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean showESet;

    /**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
    protected static final String TITLE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
    protected String title = TITLE_EDEFAULT;

    /**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected static final String TYPE_EDEFAULT = "simple";

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected String type = TYPE_EDEFAULT;

    /**
	 * This is true if the Type attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean typeESet;

    /**
	 * The default value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getUpdateSequence()
	 * @generated
	 * @ordered
	 */
    protected static final String UPDATE_SEQUENCE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getUpdateSequence()
	 * @generated
	 * @ordered
	 */
    protected String updateSequence = UPDATE_SEQUENCE_EDEFAULT;

    /**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
    protected static final String VERSION_EDEFAULT = "1.0.0";

    /**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
    protected String version = VERSION_EDEFAULT;

    /**
	 * This is true if the Version attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean versionESet;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected ContentMetadataTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.CONTENT_METADATA_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getCoverageOfferingBrief() {
		if (coverageOfferingBrief == null) {
			coverageOfferingBrief = new EObjectContainmentEList(CoverageOfferingBriefType.class, this, Wcs10Package.CONTENT_METADATA_TYPE__COVERAGE_OFFERING_BRIEF);
		}
		return coverageOfferingBrief;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ActuateType getActuate() {
		return actuate;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setActuate(ActuateType newActuate) {
		ActuateType oldActuate = actuate;
		actuate = newActuate == null ? ACTUATE_EDEFAULT : newActuate;
		boolean oldActuateESet = actuateESet;
		actuateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__ACTUATE, oldActuate, actuate, !oldActuateESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetActuate() {
		ActuateType oldActuate = actuate;
		boolean oldActuateESet = actuateESet;
		actuate = ACTUATE_EDEFAULT;
		actuateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.CONTENT_METADATA_TYPE__ACTUATE, oldActuate, ACTUATE_EDEFAULT, oldActuateESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetActuate() {
		return actuateESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getArcrole() {
		return arcrole;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setArcrole(String newArcrole) {
		String oldArcrole = arcrole;
		arcrole = newArcrole;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__ARCROLE, oldArcrole, arcrole));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getHref() {
		return href;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHref(String newHref) {
		String oldHref = href;
		href = newHref;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__HREF, oldHref, href));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getRemoteSchema() {
		return remoteSchema;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRemoteSchema(String newRemoteSchema) {
		String oldRemoteSchema = remoteSchema;
		remoteSchema = newRemoteSchema;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__REMOTE_SCHEMA, oldRemoteSchema, remoteSchema));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getRole() {
		return role;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRole(String newRole) {
		String oldRole = role;
		role = newRole;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__ROLE, oldRole, role));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ShowType getShow() {
		return show;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setShow(ShowType newShow) {
		ShowType oldShow = show;
		show = newShow == null ? SHOW_EDEFAULT : newShow;
		boolean oldShowESet = showESet;
		showESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__SHOW, oldShow, show, !oldShowESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetShow() {
		ShowType oldShow = show;
		boolean oldShowESet = showESet;
		show = SHOW_EDEFAULT;
		showESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.CONTENT_METADATA_TYPE__SHOW, oldShow, SHOW_EDEFAULT, oldShowESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetShow() {
		return showESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTitle() {
		return title;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTitle(String newTitle) {
		String oldTitle = title;
		title = newTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__TITLE, oldTitle, title));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getType() {
		return type;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(String newType) {
		String oldType = type;
		type = newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__TYPE, oldType, type, !oldTypeESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetType() {
		String oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.CONTENT_METADATA_TYPE__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetType() {
		return typeESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getUpdateSequence() {
		return updateSequence;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUpdateSequence(String newUpdateSequence) {
		String oldUpdateSequence = updateSequence;
		updateSequence = newUpdateSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__UPDATE_SEQUENCE, oldUpdateSequence, updateSequence));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getVersion() {
		return version;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		boolean oldVersionESet = versionESet;
		versionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.CONTENT_METADATA_TYPE__VERSION, oldVersion, version, !oldVersionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetVersion() {
		String oldVersion = version;
		boolean oldVersionESet = versionESet;
		version = VERSION_EDEFAULT;
		versionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.CONTENT_METADATA_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetVersion() {
		return versionESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.CONTENT_METADATA_TYPE__COVERAGE_OFFERING_BRIEF:
				return ((InternalEList)getCoverageOfferingBrief()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wcs10Package.CONTENT_METADATA_TYPE__COVERAGE_OFFERING_BRIEF:
				return getCoverageOfferingBrief();
			case Wcs10Package.CONTENT_METADATA_TYPE__ACTUATE:
				return getActuate();
			case Wcs10Package.CONTENT_METADATA_TYPE__ARCROLE:
				return getArcrole();
			case Wcs10Package.CONTENT_METADATA_TYPE__HREF:
				return getHref();
			case Wcs10Package.CONTENT_METADATA_TYPE__REMOTE_SCHEMA:
				return getRemoteSchema();
			case Wcs10Package.CONTENT_METADATA_TYPE__ROLE:
				return getRole();
			case Wcs10Package.CONTENT_METADATA_TYPE__SHOW:
				return getShow();
			case Wcs10Package.CONTENT_METADATA_TYPE__TITLE:
				return getTitle();
			case Wcs10Package.CONTENT_METADATA_TYPE__TYPE:
				return getType();
			case Wcs10Package.CONTENT_METADATA_TYPE__UPDATE_SEQUENCE:
				return getUpdateSequence();
			case Wcs10Package.CONTENT_METADATA_TYPE__VERSION:
				return getVersion();
		}
		return super.eGet(featureID, resolve, coreType);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Wcs10Package.CONTENT_METADATA_TYPE__COVERAGE_OFFERING_BRIEF:
				getCoverageOfferingBrief().clear();
				getCoverageOfferingBrief().addAll((Collection)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__ACTUATE:
				setActuate((ActuateType)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__ARCROLE:
				setArcrole((String)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__HREF:
				setHref((String)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__REMOTE_SCHEMA:
				setRemoteSchema((String)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__ROLE:
				setRole((String)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__SHOW:
				setShow((ShowType)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__TITLE:
				setTitle((String)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__TYPE:
				setType((String)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence((String)newValue);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__VERSION:
				setVersion((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
		switch (featureID) {
			case Wcs10Package.CONTENT_METADATA_TYPE__COVERAGE_OFFERING_BRIEF:
				getCoverageOfferingBrief().clear();
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__ACTUATE:
				unsetActuate();
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__ARCROLE:
				setArcrole(ARCROLE_EDEFAULT);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__HREF:
				setHref(HREF_EDEFAULT);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__REMOTE_SCHEMA:
				setRemoteSchema(REMOTE_SCHEMA_EDEFAULT);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__ROLE:
				setRole(ROLE_EDEFAULT);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__SHOW:
				unsetShow();
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__TYPE:
				unsetType();
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence(UPDATE_SEQUENCE_EDEFAULT);
				return;
			case Wcs10Package.CONTENT_METADATA_TYPE__VERSION:
				unsetVersion();
				return;
		}
		super.eUnset(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Wcs10Package.CONTENT_METADATA_TYPE__COVERAGE_OFFERING_BRIEF:
				return coverageOfferingBrief != null && !coverageOfferingBrief.isEmpty();
			case Wcs10Package.CONTENT_METADATA_TYPE__ACTUATE:
				return isSetActuate();
			case Wcs10Package.CONTENT_METADATA_TYPE__ARCROLE:
				return ARCROLE_EDEFAULT == null ? arcrole != null : !ARCROLE_EDEFAULT.equals(arcrole);
			case Wcs10Package.CONTENT_METADATA_TYPE__HREF:
				return HREF_EDEFAULT == null ? href != null : !HREF_EDEFAULT.equals(href);
			case Wcs10Package.CONTENT_METADATA_TYPE__REMOTE_SCHEMA:
				return REMOTE_SCHEMA_EDEFAULT == null ? remoteSchema != null : !REMOTE_SCHEMA_EDEFAULT.equals(remoteSchema);
			case Wcs10Package.CONTENT_METADATA_TYPE__ROLE:
				return ROLE_EDEFAULT == null ? role != null : !ROLE_EDEFAULT.equals(role);
			case Wcs10Package.CONTENT_METADATA_TYPE__SHOW:
				return isSetShow();
			case Wcs10Package.CONTENT_METADATA_TYPE__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			case Wcs10Package.CONTENT_METADATA_TYPE__TYPE:
				return isSetType();
			case Wcs10Package.CONTENT_METADATA_TYPE__UPDATE_SEQUENCE:
				return UPDATE_SEQUENCE_EDEFAULT == null ? updateSequence != null : !UPDATE_SEQUENCE_EDEFAULT.equals(updateSequence);
			case Wcs10Package.CONTENT_METADATA_TYPE__VERSION:
				return isSetVersion();
		}
		return super.eIsSet(featureID);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (actuate: ");
		if (actuateESet) result.append(actuate); else result.append("<unset>");
		result.append(", arcrole: ");
		result.append(arcrole);
		result.append(", href: ");
		result.append(href);
		result.append(", remoteSchema: ");
		result.append(remoteSchema);
		result.append(", role: ");
		result.append(role);
		result.append(", show: ");
		if (showESet) result.append(show); else result.append("<unset>");
		result.append(", title: ");
		result.append(title);
		result.append(", type: ");
		if (typeESet) result.append(type); else result.append("<unset>");
		result.append(", updateSequence: ");
		result.append(updateSequence);
		result.append(", version: ");
		if (versionESet) result.append(version); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ContentMetadataTypeImpl
