package test;
import java.io.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.*;

public class FileTableLabelProvider implements ITableLabelProvider {
    public String getColumnText( Object element, int column_index ) {
        if (column_index == 0) {
            return ((File) element).getName();
        }

        if (column_index == 1) {
            return "" + ((File) element).length();
        }

        return "";

    }

    public void addListener( ILabelProviderListener ilabelproviderlistener ) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty( Object obj, String s ) {
        return false;
    }

    public void removeListener( ILabelProviderListener ilabelproviderlistener ) {
    }

    public Image getColumnImage( Object arg0, int arg1 ) {
        return null;
    }
}
