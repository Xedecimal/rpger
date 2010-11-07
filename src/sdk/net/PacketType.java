package sdk.net;

/**
 * For client/server communication, all the commands that the client can
 * perform.
 * @author Xedecimal
 */
public enum PacketType
{ 
	None,
	/** An error has occured. */
	Error,
	ProvideUsername,
	Username,
	ProvidePassword,
	Password,
	ID,
	Area,
	GotArea,
	ChatSay,
	AddPlayer,
	RemovePlayer,
	MoveState
};
