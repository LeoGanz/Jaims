package jaims_development_studio.jaims.api.util;

import java.util.EventObject;

public class ListChangeEvent extends EventObject {
	
	private static final long	serialVersionUID	= 1L;
	public static final int		ELEMENT_ADDED		= 1;
	public static final int		ELEMENT_REMOVED		= 2;
	
	private final int			code;
	
	public ListChangeEvent(Object source, int code) {
		super(source);
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
