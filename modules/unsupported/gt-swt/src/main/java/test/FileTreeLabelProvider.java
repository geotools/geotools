package test;
import java.io.*;
import org.eclipse.jface.viewers.*;

public class FileTreeLabelProvider extends LabelProvider {
    public String getText( Object element ) {
        return ((File) element).getName();
    }
}
