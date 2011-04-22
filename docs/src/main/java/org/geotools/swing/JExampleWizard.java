/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.swing;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.geotools.swing.wizard.JPage;
import org.geotools.swing.wizard.JWizard;

/**
 * This is a quick example to show how JWizard works.
 * <p>
 * While having a Swing wizard is fun and everything; we would rather make use of an existing
 * library for this stuff (so if you can recommend something let us know). In the meantime we need
 * this class to show parameters can be handled.
 * <p>
 * 
 * @author Jody
 *
 * @source $URL$
 */
// example wizard start
public class JExampleWizard extends JWizard {    
    private static final long serialVersionUID = 2103905729508952829L;
    
    /** Value collected by the first page */
    Double x = null;
    
    /** Value collected by the first second page */
    Double y = null;
    
    public JExampleWizard() {
        super("Example Wizard");
        
        JPage page1 = new Page1("page1");
        page1.setBackPageIdentifier(null);
        page1.setNextPageIdentifier("page2");                   
        registerWizardPanel( page1 );
        
        JPage page2 = new Page2("page2");
        page2.setBackPageIdentifier("page1");
        page2.setNextPageIdentifier(JPage.FINISH);
        registerWizardPanel(page2);
        
        setCurrentPanel("page1");
    }
    
    public double getAnswer() {
        return x+y;
    }
    // example wizard end

    // page1 start
    class Page1 extends JPage {
        public Page1(String id) {
            super( id);
        }        
        JTextField field;

        @Override
        public JPanel createPanel() {
            JPanel page = new JPanel( new MigLayout() );
            page.add(new JLabel("X:"), "skip");
            page.add( field = new JTextField(15), "span, growx");
            return page;            
        }

        @Override
        public void preDisplayPanel() {
            if( x == null ){
                field.setText("");
            }
            else {
                field.setText( x.toString() );
            }
            field.addKeyListener( getJWizard().getController() );
        };

        @Override
        public void preClosePanel() {
            field.removeKeyListener( getJWizard().getController() );
        };

        public boolean isValid() {
            try {
                String txt = field.getText();
                x = Double.valueOf( txt );
                return true;
            }
            catch (NumberFormatException invalid ){
                return false;
            }
        };
    };
    // page1 end
    // page2 start
    class Page2 extends JPage {
        public Page2( String id) {
            super( id );
        }
        JTextField field;

        @Override
        public JPanel createPanel() {
            JPanel page = new JPanel( new MigLayout() );
            page.add(new JLabel("Y:"), "skip");
            page.add( field = new JTextField(15), "span, growx");
            return page;
        }

        @Override
        public void preDisplayPanel() {
            if( y == null ){
                field.setText("");
            }
            else {
                field.setText( y.toString() );
            }
            field.addKeyListener( getJWizard().getController() );
        };

        @Override
        public void preClosePanel() {
            field.removeKeyListener( getJWizard().getController() );
        };

        @Override
        public boolean isValid() {
            try {
                String txt = field.getText();
                y = Double.valueOf( txt );
                return true;
            }
            catch (NumberFormatException invalid ){
                return false;
            }
        };
    };
    // page2 end
    
    public static void main(String args[]) {
        // use wizard start
        JExampleWizard wizard = new JExampleWizard();

        System.out.println("Show wizard " + wizard.getTitle());
        int result = wizard.showModalDialog();
        System.out.print("Wizard completed with:");
        switch (result) {
        case JWizard.CANCEL:
            System.out.println("CANEL");
            break;
        case JWizard.FINISH:
            System.out.println("FINISH "+wizard.getAnswer());
            break;
        case JWizard.ERROR:
            System.out.println("ERROR");
            break;
        default:
            System.out.println("unexpected " + result);
        }
        // use wizard end
    }
    
}
