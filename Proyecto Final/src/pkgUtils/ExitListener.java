package pkgUtils;

import java.awt.event.*;

/** A listener that you attach to the top-level JFrame of
 *  your application, so that quitting the frame exits the
 *  application.
 */

public class ExitListener extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent event) {
		System.exit(0);
	}
}