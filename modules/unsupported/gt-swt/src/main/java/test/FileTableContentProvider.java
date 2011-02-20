package test;

import java.io.*;

import org.eclipse.jface.viewers.*;

public class FileTableContentProvider implements IStructuredContentProvider {
    public Object[] getElements( Object element ) {
        Object[] kids = null;
        kids = ((File) element).listFiles();
        return kids == null ? new Object[0] : kids;
    }

    public void dispose() {
    }

    public void inputChanged( Viewer viewer, Object old_object, Object new_object ) {
    }
}
