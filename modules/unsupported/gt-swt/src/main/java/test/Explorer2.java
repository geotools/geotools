package test;
import java.io.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;

public class Explorer2 extends ApplicationWindow {
    public Explorer2() {
        super(null);
    }

    protected Control createContents( Composite parent ) {
        SashForm sash_form = new SashForm(parent, SWT.HORIZONTAL | SWT.NULL);

        TreeViewer tv = new TreeViewer(sash_form);
        tv.setContentProvider(new FileTreeContentProvider());
        tv.setLabelProvider(new FileTreeLabelProvider());
        tv.setInput(new File("C:\\"));

        final TableViewer tbv = new TableViewer(sash_form, SWT.BORDER);
        tbv.setContentProvider(new FileTableContentProvider());

        tv.addSelectionChangedListener(new ISelectionChangedListener(){
            public void selectionChanged( SelectionChangedEvent event ) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();

                Object selected_file = selection.getFirstElement();
                tbv.setInput(selected_file);
            }
        });

        return sash_form;
    }

    public static void main( String[] args ) {
        Explorer2 w = new Explorer2();
        w.setBlockOnOpen(true);
        w.open();
        Display.getCurrent().dispose();
    }
}
