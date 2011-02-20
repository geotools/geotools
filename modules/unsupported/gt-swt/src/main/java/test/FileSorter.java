package test;
import java.io.*;

import org.eclipse.jface.viewers.*;

public class FileSorter extends ViewerSorter {
    public int category( Object element ) {
        return ((File) element).isDirectory() ? 0 : 1;
    }
}
