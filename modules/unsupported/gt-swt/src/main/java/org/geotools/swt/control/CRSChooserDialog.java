/**
 * 
 */
package org.geotools.swt.control;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swt.utils.Messages;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CRSChooserDialog extends Dialog {
    private final CRSChooser chooser = new CRSChooser();
    private final CoordinateReferenceSystem initialValue;
    private CoordinateReferenceSystem result;

    public CRSChooserDialog( Shell parentShell, CoordinateReferenceSystem initialValue ) {
        super(parentShell);
        this.initialValue = initialValue;
    }

    @Override
    protected Control createDialogArea( Composite parent ) {
        getShell().setText(Messages.getString("CRSChooserDialog_title"));
        chooser.setController(new Controller(){

            public void handleClose() {
                close();
            }

            public void handleOk() {
                result = chooser.getCRS();
            }

        });

        Control control = chooser.createControl(parent, initialValue);
        chooser.setFocus();
        return control;
    }

    @Override
    public boolean close() {
        result = chooser.getCRS();
        return super.close();
    }

    public CoordinateReferenceSystem getResult() {
        return result;
    }

    public static void main( String[] args ) {
        CRSChooserDialog dialog = new CRSChooserDialog(new Shell(Display.getDefault()), DefaultGeographicCRS.WGS84);
        dialog.setBlockOnOpen(true);
        dialog.open();

        CoordinateReferenceSystem crs = dialog.getResult();
        System.out.println(crs);

    }
}