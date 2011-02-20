package test;

import java.io.File;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class Explorer1 extends ApplicationWindow {
    public Explorer1() {
        super(null);
    }

    protected Control createContents( Composite parent ) {
        TreeViewer tv = new TreeViewer(parent);
        tv.setContentProvider(new FileTreeContentProvider());
        tv.setLabelProvider(new FileTreeLabelProvider());
        tv.setInput(new File("C:\\"));
        return tv.getTree();
    }

    public static void main( String[] args ) {
        Explorer1 w = new Explorer1();
        w.setBlockOnOpen(true);
        w.open();
        Display.getCurrent().dispose();
    }
}
