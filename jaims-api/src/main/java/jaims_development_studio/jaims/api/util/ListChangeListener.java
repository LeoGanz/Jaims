package jaims_development_studio.jaims.api.util;

import java.util.EventListener;

public interface ListChangeListener extends EventListener {

	public void emlementAdded(ListChangeEvent event);
	
	public void elementRemoved(ListChangeEvent event);
	
}
