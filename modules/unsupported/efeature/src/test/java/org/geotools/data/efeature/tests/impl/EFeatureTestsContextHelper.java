package org.geotools.data.efeature.tests.impl;

import java.util.Set;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.EFeatureContextFactory;
import org.geotools.data.efeature.EFeatureFactory;
import org.geotools.data.efeature.EFeatureHints;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.impl.EFeatureIDFactoryImpl;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;
import org.geotools.data.efeature.tests.provider.EFeatureTestsItemProviderAdapterFactory;
import org.geotools.data.efeature.tests.util.EFeatureTestsResourceFactoryImpl;

/**
 * This helper class creates a {@link EFeatureContext} for testing purposed.
 * 
 * @author kengu - 19. mai 2011  
 *
 */
public class EFeatureTestsContextHelper {
        

    public static final String eNS_URI = EFeatureTestsPackage.eNS_URI;
    public static final String eDOMAIN_ID = EFeatureFactory.eDOMAIN_ID;
    public static final String eCONTEXT_ID = EFeatureFactory.eCONTEXT_ID;
    
    private boolean binary;
    private EditingDomain eDomain;
    private ResourceSet eResourceSet;
    private EFeatureContext eContext;
    private EFeatureContextFactory eContextFactory;
          
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    public EFeatureTestsContextHelper() {
        this(true,false);
    }
   
    public EFeatureTestsContextHelper(boolean validate, boolean binary) {
        this(EFeatureContextFactory.eDefault(), validate, binary);
    }
    
    @SuppressWarnings("unchecked")
    public EFeatureTestsContextHelper(EFeatureContextFactory eFactory, boolean validate, boolean binary) {
        //
        // Save flags
        //
        this.binary = binary;
        //
        // Prepare hints
        //
        EFeatureHints eHints = new EFeatureHints();
        Set<EAttribute> eAttrSet = (Set<EAttribute>)eHints.get(EFeatureHints.EFEATURE_ID_ATTRIBUTES);
        eAttrSet.add(EFeatureTestsPackage.eINSTANCE.getEFeatureCompatibleData_ID());
        eAttrSet = (Set<EAttribute>)eHints.get(EFeatureHints.EFEATURE_SRID_ATTRIBUTES);
        eAttrSet.add(EFeatureTestsPackage.eINSTANCE.getEFeatureCompatibleData_SRID());
        eAttrSet = (Set<EAttribute>)eHints.get(EFeatureHints.EFEATURE_DEFAULT_ATTRIBUTES);
        eAttrSet.add(EFeatureTestsPackage.eINSTANCE.getEFeatureCompatibleData_Default());
        Set<String> eStrSet = (Set<String>)eHints.get(EFeatureHints.EFEATURE_DEFAULT_GEOMETRY_NAMES);
        eStrSet.add("geometry");
        
        //
        // Forward to default implementation
        //
        this.eContextFactory = eFactory;
        if(eFactory.contains(eCONTEXT_ID)) {
            this.eContext = eFactory.eContext(eCONTEXT_ID);
        } else {
            this.eContext = eFactory.create(eCONTEXT_ID,new EFeatureIDFactoryImpl(),eHints);
        }
        //
        // Add domain and package to context
        //
        this.eContext.eAdd(EFeatureTestsPackage.eINSTANCE);
        this.eContext.eAdd(eDOMAIN_ID,getEditingDomain());
        //
        // Validate context structure?
        //
        if(validate) {
            this.eContext.eStructure().validate();
        }
    }
    
    // ----------------------------------------------------- 
    //  EFeatureTestsContextHelper methods
    // -----------------------------------------------------
    
    public EFeatureContext eContext() {
        return eContext; 
    }
    
    public EFeatureContextFactory eContextFactory() {
        return eContextFactory;
    }
    
    public ResourceSet getResourceSet() {        
        //
        // Initialize?
        //
        if(eResourceSet==null) {
            //
            // Get editing domain
            //
            EditingDomain eDomain = getEditingDomain();
            //
            // Get resource set from domain
            //
            eResourceSet = eDomain.getResourceSet();        
            //
            // Register the package to ensure it is available during loading.
            //
            eResourceSet.getPackageRegistry().
                put(eNS_URI,EFeatureTestsPackage.eINSTANCE); 
            //
            // Register the EFeature resource factory to allow loading and saving XMI documents.
            //
            eResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().
                put((binary ? "bin": "xmi"), new EFeatureTestsResourceFactoryImpl(binary));
            //
            // Map resource
            //
        }
        return eResourceSet;
    }
    
    public EditingDomain getEditingDomain() {
        //
        // Initialize?
        //
        if(eDomain==null) {            
            //
            // Create an adapter factory that yields item providers.
            //
            ComposedAdapterFactory eFactory = new ComposedAdapterFactory(
                    ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
            //
            // Add generated item adapter to factory
            //
            eFactory.addAdapterFactory(new EFeatureTestsItemProviderAdapterFactory());
            //
            // Create a command stack
            //
            BasicCommandStack commandStack = new BasicCommandStack();
            //
            // Create and return the editing domain
            //
            eDomain = new AdapterFactoryEditingDomain(eFactory,commandStack);
        }
        return eDomain;
    }
    
    public EFeatureInfo eGetFeatureInfo(String eType) {
        return eContext().eStructure().eGetPackageInfo(eNS_URI).eGetFeatureInfo(eType);
    }

}
