package demo;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBridgeProvider {
	/**
	 * This is a hack. It should be replaced with a service injection mechanism, but I didn't understand enough
	 * about how to do it with Jersey's auto constructed resources to do it in time. It should either go through
	 * that or utilize a spring container to inject the dependencies. But to fit the time
	 * constraints, this hack has been tolerated...
	 */
	public static DataBridge dataBridge;

	static {
		try {
			dataBridge = new SimpleDataBridge();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
