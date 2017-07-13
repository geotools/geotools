/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.EdgeType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.IsolatedPropertyType;
import net.opengis.gml311.NodeType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;
import org.w3.xlink.TypeType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Isolated Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getNode <em>Node</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getEdge <em>Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getActuate <em>Actuate</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getRemoteSchema <em>Remote Schema</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getShow <em>Show</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.IsolatedPropertyTypeImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IsolatedPropertyTypeImpl extends MinimalEObjectImpl.Container implements IsolatedPropertyType {
    /**
     * The cached value of the '{@link #getNode() <em>Node</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNode()
     * @generated
     * @ordered
     */
    protected NodeType node;

    /**
     * The cached value of the '{@link #getEdge() <em>Edge</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEdge()
     * @generated
     * @ordered
     */
    protected EdgeType edge;

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
    protected static final TypeType TYPE_EDEFAULT = TypeType.SIMPLE_LITERAL;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected TypeType type = TYPE_EDEFAULT;

    /**
     * This is true if the Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean typeESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IsolatedPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getIsolatedPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NodeType getNode() {
        return node;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetNode(NodeType newNode, NotificationChain msgs) {
        NodeType oldNode = node;
        node = newNode;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__NODE, oldNode, newNode);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNode(NodeType newNode) {
        if (newNode != node) {
            NotificationChain msgs = null;
            if (node != null)
                msgs = ((InternalEObject)node).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ISOLATED_PROPERTY_TYPE__NODE, null, msgs);
            if (newNode != null)
                msgs = ((InternalEObject)newNode).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ISOLATED_PROPERTY_TYPE__NODE, null, msgs);
            msgs = basicSetNode(newNode, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__NODE, newNode, newNode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EdgeType getEdge() {
        return edge;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEdge(EdgeType newEdge, NotificationChain msgs) {
        EdgeType oldEdge = edge;
        edge = newEdge;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__EDGE, oldEdge, newEdge);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEdge(EdgeType newEdge) {
        if (newEdge != edge) {
            NotificationChain msgs = null;
            if (edge != null)
                msgs = ((InternalEObject)edge).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ISOLATED_PROPERTY_TYPE__EDGE, null, msgs);
            if (newEdge != null)
                msgs = ((InternalEObject)newEdge).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ISOLATED_PROPERTY_TYPE__EDGE, null, msgs);
            msgs = basicSetEdge(newEdge, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__EDGE, newEdge, newEdge));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__ACTUATE, oldActuate, actuate, !oldActuateESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ISOLATED_PROPERTY_TYPE__ACTUATE, oldActuate, ACTUATE_EDEFAULT, oldActuateESet));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__ARCROLE, oldArcrole, arcrole));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__HREF, oldHref, href));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__REMOTE_SCHEMA, oldRemoteSchema, remoteSchema));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__ROLE, oldRole, role));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__SHOW, oldShow, show, !oldShowESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ISOLATED_PROPERTY_TYPE__SHOW, oldShow, SHOW_EDEFAULT, oldShowESet));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__TITLE, oldTitle, title));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TypeType getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setType(TypeType newType) {
        TypeType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        boolean oldTypeESet = typeESet;
        typeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ISOLATED_PROPERTY_TYPE__TYPE, oldType, type, !oldTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetType() {
        TypeType oldType = type;
        boolean oldTypeESet = typeESet;
        type = TYPE_EDEFAULT;
        typeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ISOLATED_PROPERTY_TYPE__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
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
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ISOLATED_PROPERTY_TYPE__NODE:
                return basicSetNode(null, msgs);
            case Gml311Package.ISOLATED_PROPERTY_TYPE__EDGE:
                return basicSetEdge(null, msgs);
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
            case Gml311Package.ISOLATED_PROPERTY_TYPE__NODE:
                return getNode();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__EDGE:
                return getEdge();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ACTUATE:
                return getActuate();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ARCROLE:
                return getArcrole();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__HREF:
                return getHref();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__REMOTE_SCHEMA:
                return getRemoteSchema();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ROLE:
                return getRole();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__SHOW:
                return getShow();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__TITLE:
                return getTitle();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__TYPE:
                return getType();
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
            case Gml311Package.ISOLATED_PROPERTY_TYPE__NODE:
                setNode((NodeType)newValue);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__EDGE:
                setEdge((EdgeType)newValue);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ACTUATE:
                setActuate((ActuateType)newValue);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ARCROLE:
                setArcrole((String)newValue);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__HREF:
                setHref((String)newValue);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__REMOTE_SCHEMA:
                setRemoteSchema((String)newValue);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ROLE:
                setRole((String)newValue);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__SHOW:
                setShow((ShowType)newValue);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__TITLE:
                setTitle((String)newValue);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__TYPE:
                setType((TypeType)newValue);
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
            case Gml311Package.ISOLATED_PROPERTY_TYPE__NODE:
                setNode((NodeType)null);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__EDGE:
                setEdge((EdgeType)null);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ACTUATE:
                unsetActuate();
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ARCROLE:
                setArcrole(ARCROLE_EDEFAULT);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__HREF:
                setHref(HREF_EDEFAULT);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__REMOTE_SCHEMA:
                setRemoteSchema(REMOTE_SCHEMA_EDEFAULT);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ROLE:
                setRole(ROLE_EDEFAULT);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__SHOW:
                unsetShow();
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__TITLE:
                setTitle(TITLE_EDEFAULT);
                return;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__TYPE:
                unsetType();
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
            case Gml311Package.ISOLATED_PROPERTY_TYPE__NODE:
                return node != null;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__EDGE:
                return edge != null;
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ACTUATE:
                return isSetActuate();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ARCROLE:
                return ARCROLE_EDEFAULT == null ? arcrole != null : !ARCROLE_EDEFAULT.equals(arcrole);
            case Gml311Package.ISOLATED_PROPERTY_TYPE__HREF:
                return HREF_EDEFAULT == null ? href != null : !HREF_EDEFAULT.equals(href);
            case Gml311Package.ISOLATED_PROPERTY_TYPE__REMOTE_SCHEMA:
                return REMOTE_SCHEMA_EDEFAULT == null ? remoteSchema != null : !REMOTE_SCHEMA_EDEFAULT.equals(remoteSchema);
            case Gml311Package.ISOLATED_PROPERTY_TYPE__ROLE:
                return ROLE_EDEFAULT == null ? role != null : !ROLE_EDEFAULT.equals(role);
            case Gml311Package.ISOLATED_PROPERTY_TYPE__SHOW:
                return isSetShow();
            case Gml311Package.ISOLATED_PROPERTY_TYPE__TITLE:
                return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
            case Gml311Package.ISOLATED_PROPERTY_TYPE__TYPE:
                return isSetType();
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
        result.append(')');
        return result.toString();
    }

} //IsolatedPropertyTypeImpl
