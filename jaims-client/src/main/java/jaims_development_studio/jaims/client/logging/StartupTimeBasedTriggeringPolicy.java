package jaims_development_studio.jaims.client.logging;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.RolloverFailure;

@NoAutoStart
public class StartupTimeBasedTriggeringPolicy<E> extends DefaultTimeBasedFileNamingAndTriggeringPolicy<E> {
	
	@Override
	public void start() {
		super.start();
		nextCheck = 0;
		isTriggeringEvent(null, null);
		try {
			tbrp.rollover();
		} catch (@SuppressWarnings("unused") RolloverFailure e) {}
	}

}
