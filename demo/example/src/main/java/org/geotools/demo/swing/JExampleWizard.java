/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.swing;

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
public class JExampleWizard extends JWizard {    
    private static final long serialVersionUID = 2103905729508952829L;
    
    /** Value collected by the first page */
    Double x = null;
    
    /** Value collected by the first second page */
    Double y = null;
    
    JPage page1 = new JPage("page1") {
        {
            setBackPageIdentifier(null);
            setNextPageIdentifier("page2");                   
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

        @Override
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

    JPage page2 = new JPage("page2") {
        {
            setBackPageIdentifier("page1");
            setNextPageIdentifier(JPage.FINISH);
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
    
    public JExampleWizard() {
        super("Example Wizard");
        registerWizardPanel(page1);
        registerWizardPanel(page2);
        
        setCurrentPanel("page1");
    }
    
    public double getAnswer() {
        return x+y;
    }
    
    public static void main(String args[]) {
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
    }
    
}
