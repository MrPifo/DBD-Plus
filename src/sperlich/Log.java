package sperlich;

import org.tinylog.Logger;

class Log {
	public static void out(Object o) {
		System.out.println(o);
		Logger.info(o);
	}
}
