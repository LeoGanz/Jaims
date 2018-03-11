package jaims_development_studio.jaims.api.sendables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * @author WilliGross
 */
@Entity(name = "SendableSendableGroup")
@DiscriminatorValue(value = ESendableType.Values.SENDABLE_GROUP)
public class SendableSendableGroup extends Sendable {

	private static final long	serialVersionUID	= 1L;
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "SENDABLEGROUP_SENDABLE", joinColumns = @JoinColumn(name = "SENDABLE_GROUP"),
	inverseJoinColumns = @JoinColumn(name = "SENDABLE"))
	private final List<Sendable>		sendables;

	@SuppressWarnings("unused")
	private SendableSendableGroup() {
		this(new Sendable[] {});
	}
	
	public SendableSendableGroup(Sendable... sendables) {
		super(ESendableType.SENDABLE_GROUP, 1);
		this.sendables = new ArrayList<>(sendables.length);
	}
	
	public SendableSendableGroup(List<Sendable> sendables) {
		super(ESendableType.SENDABLE_GROUP, 1);
		this.sendables = new ArrayList<>(sendables);
	}
	
	public List<Sendable> getSendables() {
		return sendables;
	}

	public void addSendables(Sendable... newSendables) {
		sendables.addAll(Arrays.asList(newSendables).stream().filter((c) -> c != null).collect(Collectors.toList()));
	}
	
	public void addSendables(List<Sendable> newSendables) {
		sendables.addAll(newSendables);
	}

}
