package org.geotools.data.efeature;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author kengu
 *
 */
public interface EFeatureContext {

    /**
     * Check if this {@link EFeatureContext context} is a prototype.
     * <p>
     * Prototype contexts only contain {@link EFeatureInfo structure} 
     * information about {@link EClass classes} that: 
     * <nl>
     *  <li>define {@link EObject}s implementing {@link EFeature}</li>
     *  <li>define {@link EObject}s containing {@link Geometry EFeature compatible data}</li>
     * </nl>
     * </p>
     * @see {@link EFeatureInfo#create(EFeatureContext, EObject, EFeatureHints)}   
     */    
    public boolean isPrototype();
    
    public String eContextID();
    
    public EFeatureContextInfo eStructure();
    
    public boolean containsDomain(String eDomainID);
    
    public List<String> eDomainIDs();

    public EditingDomain eGetDomain(String eDomainID);
    
    public boolean containsPackage(String eNsURI);
    
    public List<String> eNsURIs();

    public EPackage eGetPackage(String eNsURI);
    
    public Resource eGetResource(String eDomainID, URI eURI, boolean loadOnDemand);

    /**
     * Get {@link EFeatureContextFactory} instance which created this context
     * </p>
     * @see {@link EFeatureContext#eContextID() }
     * @see {@link EFeatureContextFactory#eContext(String) }
     * @see {@link EFeatureContextFactory#create(EFeatureContext) }
     */
    public EFeatureContextFactory eContextFactory();
    
    /**
     * Check if given {@link EPackage package} is contained in context.
     * @param ePackage - given {@link EPackage} instance
     * @return <code>true</code> if contained
     */
    public boolean contains(EPackage ePackage);

    /**
     * Add {@link EPackage} instance to context.
     * <p>
     * @param ePackage - the {@link EPackage} instance
     * @return <code>null</code> if not already registered, or the 
     * {@link EPackage} replaced by given instance.
     * @throws IllegalStateException If no 
     * {@link EPackage#getNsURI() EPackage name space URI} is specified
     */
    public EPackage eAdd(EPackage ePackage) throws IllegalArgumentException;
    
    /**
     * Check if given {@link EditingDomain editing domain} with given ID is contained in context.
     * @param eDomainID - given {@link EditingDomain} id
     * @param eDomain - given {@link EditingDomain} instance
     * @return <code>true</code> if contained
     */
    public boolean contains(String eDomainID, EditingDomain eDomain);
    
    /**
     * Add {@link EditingDomain} instance to context.
     * <p>
     * @param eDomainID - the {@link EditingDomain} instance id
     * @param eDomain - the {@link EditingDomain} instance
     * @return <code>null</code> if not already registered, or the 
     * {@link EditingDomain} replaced by given instance.
     * @throws IllegalStateException If no {@link EditingDomain} id is specified
     */
    public EditingDomain eAdd(String eDomainID, EditingDomain eDomain) throws IllegalArgumentException;    

    /**
     * Get {@link EFeatureIDFactory} instance 
     */
    public EFeatureIDFactory eIDFactory();
    
    /**
     * Claim given {@link EFeature} (from any {@link EFeatureContext context}).
     * @param eFeature - {@link EFeature} instance to be moved into this context
     * @param copy TODO
     * @return the {@link EFeatureInfo structure} of moved feature, 
     * or <code>null</code> if not moved
     */
    public EFeatureInfo eAdapt(EFeature eFeature, boolean copy);
    
    
}
