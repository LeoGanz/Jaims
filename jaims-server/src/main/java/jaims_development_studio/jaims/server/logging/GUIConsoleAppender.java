package jaims_development_studio.jaims.server.logging;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;

public class GUIConsoleAppender extends OutputStreamAppender<ILoggingEvent> {

	private static final int				MAX_CAPACITY	= 250;
	private static BlockingQueue<String>	queue;
	
	@Override
	public void start() {

		if (queue == null)
			queue = new LinkedBlockingQueue<>();
		
		setOutputStream(System.out);
		
		super.start();
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		if (queue.size() >= MAX_CAPACITY)
			queue.clear();
		queue.add(((PatternLayoutEncoder) getEncoder()).getLayout().doLayout(eventObject));
	}
	
	public static String getNextLogEvent() {

		if (queue != null)
			try {
				return queue.take();
			} catch (@SuppressWarnings("unused") final InterruptedException ignored) {
			}

		return null;
	}

}
