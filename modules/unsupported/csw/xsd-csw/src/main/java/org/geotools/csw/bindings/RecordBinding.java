package org.geotools.csw.bindings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.RecordType;
import net.opengis.cat.csw20.SimpleLiteral;
import net.opengis.ows10.BoundingBoxType;

import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.geotools.csw.CSW;
import org.geotools.csw.DC;
import org.geotools.csw.DCT;
import org.geotools.ows.OWS;
import org.geotools.xml.ComplexEMFBinding;

public class RecordBinding extends ComplexEMFBinding {

    public RecordBinding() {
        super(Csw20Factory.eINSTANCE, CSW.RecordType);
    }
    
    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        RecordType record = (RecordType) object;
        
        List result = new ArrayList();
        for (SimpleLiteral sl : record.getDCElement()) {
            XSDSchema dctSchema = DCT.getInstance().getSchema();
            XSDElementDeclaration declaration = dctSchema.resolveElementDeclaration(sl.getName());
            if(declaration.getTypeDefinition() == null) {
                XSDSchema dcSchema = DC.getInstance().getSchema();
                declaration = dcSchema.resolveElementDeclaration(sl.getName());
            }
            if(declaration != null) {
                XSDParticle particle = buildParticle(declaration);
                result.add(new Object[] {particle, sl});
            }
        }
        
        if(record.getBoundingBox() != null && record.getBoundingBox().size() > 0) {
            XSDElementDeclaration bboxElement  = OWS.getInstance().getSchema().resolveElementDeclaration("BoundingBox");
            XSDParticle particle = buildParticle(bboxElement);
            for (Object box : record.getBoundingBox()) {
                result.add(new Object[] {particle, box});
            }
        }
        
        return result;
    }

    private XSDParticle buildParticle(XSDElementDeclaration declaration) {
        XSDParticle particle = XSDFactory.eINSTANCE.createXSDParticle();
        particle.setContent(declaration);
        particle.setMinOccurs(0);
        particle.setMaxOccurs(-1);
        return particle;
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        return null;
    }

}
