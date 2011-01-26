package org.geotools.wps.bindings;

import javax.xml.namespace.QName;

import net.opengis.wps10.DefaultType2;
import net.opengis.wps10.LanguagesType;
import net.opengis.wps10.LanguagesType1;
import net.opengis.wps10.Wps10Factory;

import org.geotools.ows.v1_1.OWS;
import org.geotools.wps.WPS;
import org.geotools.wps.WPSTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LanguagesBindingTest extends WPSTestSupport {

    public void testEncode() throws Exception {
        Wps10Factory f = Wps10Factory.eINSTANCE;
        LanguagesType1 languages = f.createLanguagesType1();
        
        DefaultType2 defaultLanguage = f.createDefaultType2();
        languages.setDefault(defaultLanguage);
        defaultLanguage.setLanguage("en-US");
        
        LanguagesType supportedLanguages = f.createLanguagesType();
        languages.setSupported( supportedLanguages );
        supportedLanguages.getLanguage().add( "en-US");
        
        Document dom = encode( languages, WPS.Languages );
        
        Element def = getElementByQName( dom.getDocumentElement(), new QName( WPS.NAMESPACE, "Default") ) ;
        assertNotNull( def );
       
        assertNotNull( getElementByQName( def, OWS.Language ) );
        assertEquals( "en-US", getElementByQName( def, OWS.Language ).getFirstChild().getTextContent() );
        
        assertEquals( "en-US",  getElementByQName( dom.getDocumentElement(), new QName( WPS.NAMESPACE, "Default")).getFirstChild().getTextContent() );
        assertNotNull( getElementByQName( dom.getDocumentElement(), new QName( WPS.NAMESPACE, "Supported") ) );
    }
}
