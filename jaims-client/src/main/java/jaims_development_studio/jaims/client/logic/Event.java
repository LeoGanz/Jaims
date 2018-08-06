package jaims_development_studio.jaims.client.logic;

import java.util.Date;

public class Event {

	private EEventType	event;
	private Date		dateReceived;

	public Event(EEventType event, Date dateReceived) {

		this.event = event;
		this.dateReceived = dateReceived;
	}

	public EEventType getType() {

		return event;
	}

	public Date getDateReceived() {

		return dateReceived;
	}

}
