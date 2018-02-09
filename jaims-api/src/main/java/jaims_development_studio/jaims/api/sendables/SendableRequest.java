package jaims_development_studio.jaims.api.sendables;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A SendableRequest can be used for different types of request by the client. The kind of request has to be specified
 * via an enum constant. This type contains some universal fields like a {@link Date}, a {@link UUID} and a
 * {@link String} field the purpose of which differs depending on the type of request used. For further details on how
 * to use this class see the information provided in the description of each field's getter and setter methods as well
 * as the descriptions of the constructors. All relevant information is linked below. <br>
 * </br>
 * Information on which constructor to use can be obtained through the documentations of all relevant constructors
 * linked below.
 *
 * @author WilliGross
 * @see ERequestType
 * @see #getUniversalDate()
 * @see #setUniversalDate(Date)
 * @see #getUniversalUuid()
 * @see #setUniversalUuid(UUID)
 * @see #getUniversalString()
 * @see #setUniversalString(String) setUniversalString(String) <br>
 *      </br>
 * @see #SendableRequest(ERequestType)
 * @see #SendableRequest(ERequestType, Date)
 * @see #SendableRequest(ERequestType, UUID)
 * @see #SendableRequest(ERequestType, String)
 * @see #SendableRequest(ERequestType, Date, UUID)
 * @see #SendableRequest(ERequestType, Date, String)
 * @see #SendableRequest(ERequestType, Date, UUID, String)
 */
@Entity(name = "SendableRequest")
@DiscriminatorValue(value = ESendableType.Values.REQUEST)
public class SendableRequest extends Sendable {
	
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * This field specifies the type of {@link SendableRequest}.
	 *
	 * @see ERequestType
	 * @see #getRequestType()
	 */
	@Column(name = "REQUEST_TYPE", columnDefinition = "VARCHAR(64)")
	@Enumerated(EnumType.STRING)
	private final ERequestType	requestType;
	
	/**
	 * This field can have different purposes depending on the request's type. For usage information see linked methods.
	 *
	 * @see #setUniversalDate(Date)
	 * @see #getUniversalDate()
	 */
	@Column(name = "UNIVERSAL_DATE", columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				universalDate;

	/**
	 * This field can have different purposes depending on the request's type. For usage information see linked methods.
	 *
	 * @see #setUniversalUuid(UUID)
	 * @see #getUniversalUuid()
	 */
	@Column(name = "UNIVERSAL_UUID", columnDefinition = "BINARY(16)")
	private UUID				universalUuid;

	/**
	 * This field can have different purposes depending on the request's type. For usage information see linked methods.
	 *
	 * @see #setUniversalString(String)
	 * @see #getUniversalString()
	 */
	@Column(name = "UNIVERSAL_STRING", columnDefinition = "VARCHAR(256)")
	private String				universalString;
	
	/**
	 * This constructs a bare bones SendableRequest. This is only used for Hibernate.
	 */
	@SuppressWarnings("unused")
	private SendableRequest() {
		this(ERequestType.OTHER);
	}
	
	/**
	 * This constructs a SendableRequest without any uuid, string or date parameters. It can be used for delete account
	 * requests and profile/settings requests for the logged-in user's profile/settings. Since no date is provided for
	 * the profile/settings request the server will sent a profile/settings object regardless of last update times if
	 * there is one available.
	 *
	 * @param requestType the type of request
	 * @see ERequestType
	 */
	public SendableRequest(ERequestType requestType) {
		this(requestType, null, null, null);
	}
	
	/**
	 * This constructs a SendableRequest with a date but without uuid and string parameters. It can be used for
	 * profile/settings requests that return the profile/settings for the logged-in user. <br>
	 * </br>
	 * (This constructor will also work for delete account requests but unnecessary network traffic will be caused as
	 * the date field is not used for this kind of request. Use {@link #SendableRequest(ERequestType)} instead.)
	 *
	 * @param requestType the type of request
	 * @param universalDate the date to be stored. For details see also {@link #setUniversalDate(Date)}
	 * @see ERequestType
	 */
	public SendableRequest(ERequestType requestType, Date universalDate) {
		this(requestType, universalDate, null, null);
	}
	
	/**
	 * This constructs a SendableRequest with a uuid but without string and date parameters. It can be used for
	 * profile/settings requests that return the profile/settings for the user specified by the uuid parameter. If a
	 * profile is available it will be sent regardless of last update times. Settings do not need to be provided by the
	 * client and therefore will be sent even if the server does not have custom settings for a user.<br>
	 * </br>
	 * (This constructor will also work for delete account requests but unnecessary network traffic will be caused as
	 * the uuid field will be overwritten by the server for security reasons. Use {@link #SendableRequest(ERequestType)}
	 * instead.)
	 *
	 * @param requestType the type of request
	 * @param universalUuid the uuid to be stored. For details see also {@link #setUniversalUuid(UUID)}
	 * @see ERequestType
	 */
	public SendableRequest(ERequestType requestType, UUID universalUuid) {
		this(requestType, null, universalUuid, null);
	}

	/**
	 * This constructs a SendableRequest with a string but without uuid and date parameters. It can be used for
	 * profile/settings requests that return the profile/settings for the user specified by the string parameter which
	 * resembles a username or any substring of a username. If a profile is available it will be sent regardless of last
	 * update times.<br>
	 * </br>
	 * (This constructor will also work for delete account requests but unnecessary network traffic will be caused as
	 * the uuid field will be overwritten by the server for security reasons. Uuid identification will be preferred to
	 * usernames. Use {@link #SendableRequest(ERequestType)} instead.)
	 *
	 * @param requestType the type of request
	 * @param universalString the string to be stored. For details see also {@link #setUniversalString(String)}
	 * @see ERequestType
	 */
	public SendableRequest(ERequestType requestType, String universalString) {
		this(requestType, null, null, universalString);
	}
	
	/**
	 * This constructs a SendableRequest with uuid and date parameters but without a string parameter. It can be used
	 * for profile/settings requests that return the profile/settings for the user specified by the uuid parameter. A
	 * profile/the settings will only be returned if the server has got a newer version of the user's profile/settings
	 * as determined by the date parameter.<br>
	 * </br>
	 * (This constructor will also work for delete account requests but unnecessary network traffic will be caused as
	 * the date field is not used this kind of request and the uuid field will be overwritten by the server for security
	 * reasons. Use {@link #SendableRequest(ERequestType)} instead.)
	 *
	 * @param requestType the type of request
	 * @param universalDate the date to be stored. For details see also {@link #setUniversalDate(Date)}
	 * @param universalUuid the uuid to be stored. For details see also {@link #setUniversalUuid(UUID)}
	 * @see ERequestType
	 */
	public SendableRequest(ERequestType requestType, Date universalDate, UUID universalUuid) {
		this(requestType, universalDate, universalUuid, null);
	}

	/**
	 * This constructs a SendableRequest with string and date parameters but without a uuid parameter. It can be used
	 * for profile/settings requests that return the profile/settings for the user specified by the string parameter
	 * which resembles a username or any substring of a username. A profile/the settings will only be returned if the
	 * server has got a newer version of the user's profile/settings as determined by the date parameter.<br>
	 * </br>
	 * (This constructor will also work for delete account requests but unnecessary network traffic will be caused as
	 * the date field is not used this kind of request and the uuid field will be overwritten by the server for security
	 * reasons. Use {@link #SendableRequest(ERequestType)} instead.)
	 *
	 * @param requestType the type of request
	 * @param universalDate the date to be stored. For details see also {@link #setUniversalDate(Date)}
	 * @param universalString the string to be stored. For details see also {@link #setUniversalString(String)}
	 * @see ERequestType
	 */
	public SendableRequest(ERequestType requestType, Date universalDate, String universalString) {
		this(requestType, universalDate, null, universalString);
	}
	
	/**
	 * <b>Please use either of the other constructors. This one is intended for internal use!</b><br>
	 * </br>
	 * Refer to the type's documentation ({@link SendableRequest}) to see links to all available constructors.<br>
	 * </br>
	 *
	 * This constructs a SendableRequest with uuid, string and date parameters. It can be used for profile/settings
	 * requests that return the profile/settings for the user specified by the uuid or string parameter which resembles
	 * a username or any substring of a username. If both are provided the uuid will be preferred. A profile/the
	 * settings will only be returned if the server has got a newer version of the user's profile/settings as determined
	 * by the date parameter (unless the date paramter is null).<br>
	 * </br>
	 * (This constructor will also work for delete account requests but unnecessary network traffic will be caused as
	 * the date field is not used this kind of request and the uuid field will be overwritten by the server for security
	 * reasons. Use {@link #SendableRequest(ERequestType)} instead.)
	 *
	 * @param requestType the type of request
	 * @param universalDate the date to be stored. For details see also {@link #setUniversalDate(Date)}
	 * @param universalUuid the uuid to be stored. For details see also {@link #setUniversalUuid(UUID)}
	 * @param universalString the string to be stored. For details see also {@link #setUniversalString(String)}
	 * @see SendableRequest
	 * @see ERequestType
	 */
	public SendableRequest(ERequestType requestType, Date universalDate, UUID universalUuid, String universalString) {
		super(ESendableType.REQUEST, 10);
		this.requestType = requestType;
		this.universalDate = universalDate;
		this.universalUuid = universalUuid;
		this.universalString = universalString;
	}
	
	/**
	 * This method is used to differentiate between the different kinds of SendableRequests. A constant from enum type
	 * {@link ERequestType} will be returned. See its documentation for all available values.
	 *
	 * @return the type of request
	 * @see ERequestType
	 */
	public ERequestType getRequestType() {
		return requestType;
	}

	/**
	 * Depending on the type of request the purpose of the universal date field differs:
	 * <ul>
	 * <li>For profile/settings requests the date field represents the lastUpdated field of the client's current
	 * profile.</li>
	 * <li>For delete account requests the date field is not used.</li>
	 * </ul>
	 *
	 * @return the request's universalDate field. For usage details see description above.
	 * @see ERequestType
	 */
	public Date getUniversalDate() {
		return universalDate;
	}

	/**
	 * Depending on the type of request the purpose of the universal date field differs:
	 * <ul>
	 * <li>For profile/settings requests the date field can be set to the lastUpdated field of the client's current
	 * profile/settings. Only if the server can find a newer profile/settings a
	 * {@link SendableProfile}/{@link SendableSettings} will be sent back to the client. If the client's
	 * profile/settings is/are up to date nothing will be sent. If no date is set the server will sent a
	 * profile/settings regardless of last update times.</li>
	 * <li>For delete account requests the date field is not used.</li>
	 * </ul>
	 *
	 * @param universalDate the date that will be stored. For usage details see description above.
	 * @see ERequestType
	 */
	public void setUniversalDate(Date universalDate) {
		this.universalDate = universalDate;
	}
	
	/**
	 * Depending on the type of request the purpose of the universal uuid field differs:
	 * <ul>
	 * <li>For profile/settings requests the uuid field specifies the profile/settings that is/are requested.</li>
	 * <li>For delete account requests the uuid field represents the account that is to be deleted.</li>
	 * </ul>
	 *
	 * @return the request's universalUuid field. For usage details see description above.
	 * @see ERequestType
	 */
	public UUID getUniversalUuid() {
		return universalUuid;
	}

	/**
	 * Depending on the type of request the purpose of the universal uuid field differs:
	 * <ul>
	 * <li>For profile/settings requests the uuid field specifies the profile/settings that is/are requested. If
	 * universalUuid is set to or left as null AND the universalString field is null as well the logged-in user's uuid
	 * will be inserted automatically.</li>
	 * <li>For delete account requests the uuid field represents the account that is to be deleted. There is no need to
	 * set universalUuid as the server overwrites the uuid field with the logged-in user's uuid for security reasons
	 * anyways.</li>
	 * </ul>
	 *
	 * @param universalUuid the uuid that will be stored. For usage details see description above.
	 * @see ERequestType
	 */
	public void setUniversalUuid(UUID universalUuid) {
		this.universalUuid = universalUuid;
	}
	
	/**
	 * Depending on the type of request the purpose of the universal string field differs:
	 * <ul>
	 * <li>For profile/settings requests the string field specifies the profile/settings that is/are requested. It
	 * resembles a whole username or any substring of a username. It is only taken into account if the uuid field is
	 * null!</li>
	 * <li>For delete account requests the string field is not used.</li>
	 * </ul>
	 *
	 * @return the request's universalString field. For usage details see description above.
	 * @see ERequestType
	 */
	public String getUniversalString() {
		return universalString;
	}
	
	/**
	 * Depending on the type of request the purpose of the universal string field differs:
	 * <ul>
	 * <li>For profile/settings requests the string field specifies the profile/settings that is/are requested. It
	 * resembles a whole username or any substring of a username. It is only taken into account if the uuid field is
	 * null!</li>
	 * <li>For delete account requests the string field is not used.</li>
	 * </ul>
	 *
	 * @param universalString the string to be stored. For usage details see description above.
	 * @see ERequestType
	 */
	public void setUniversalString(String universalString) {
		this.universalString = universalString;
	}
	
}
