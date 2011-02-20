package test;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class Hello extends ApplicationWindow
{
  public Hello()
  {
    super(null);
  }

  protected Control createContents(Composite parent)
  {
    Button b = new Button(parent, SWT.PUSH);
    b.setText("Hello World");
    return b;
  }

  public static void main(String[] args)
  {
    Hello w = new Hello();
    w.setBlockOnOpen(true);
    w.open();
    Display.getCurrent().dispose();
  }
}


