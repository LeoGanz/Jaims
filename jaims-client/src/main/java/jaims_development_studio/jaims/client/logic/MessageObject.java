package jaims_development_studio.jaims.client.logic;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

public class MessageObject implements Comparable<MessageObject> {

	private UUID sender, recipient;
	private String message;
	private byte[] byteImage;
	private Date timestampDelievered, timestampRead;
	
	
	
	
	
	public void setUUIDSender(UUID sender) {
		this.sender = sender;
	}
	
	public UUID getSender() {
		return sender;
	}
	
	public void setUUIDRecipient(UUID recipient) {
		this.recipient = recipient;
	}
	
	public UUID getRecipient() {
		return recipient;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setImage(byte[] byteImage) {
		this.byteImage = byteImage;
	}
	
	public byte[] getImage() {
		return byteImage;
	}
	
	public void setTimeDelievered(Date timestampDelievered) {
		this.timestampDelievered = timestampDelievered; 
	}
	
	public Date getTimeDelievered() {
		return timestampDelievered;
	}
	
	public void setTimeRead(Date timestampRead) {
		this.timestampRead = timestampRead;
	}
	
	public Date getTimeRead() {
		return timestampRead;
	}
	
	public Object getMessageObject() {
		if (message != null) {
			return message;
		}else if (byteImage != null) {
			Image img = null;
			try {
				img = ImageIO.read(new ByteArrayInputStream(byteImage));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return img;
		}else {
			return null;
		}
	}

	@Override
	public int compareTo(MessageObject o) {
		if (this.timestampDelievered.compareTo(o.getTimeDelievered()) > 0) {
			
		}
		return 0;
	}
	
}
