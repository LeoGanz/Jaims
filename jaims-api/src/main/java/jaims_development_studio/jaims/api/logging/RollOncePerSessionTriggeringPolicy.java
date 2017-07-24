package jaims_development_studio.jaims.api.logging;

import java.io.File;

import ch.qos.logback.core.rolling.TriggeringPolicyBase;


public class RollOncePerSessionTriggeringPolicy<E> extends TriggeringPolicyBase<E> {

	private static boolean doRolling = true;

	@Override
	public boolean isTriggeringEvent(File activeFile, E event) {
		if (doRolling) {
			doRolling = false;
			return true;
		}
		return false;
	}

}
