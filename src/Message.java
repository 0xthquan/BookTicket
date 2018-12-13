import java.io.Serializable;

public class Message implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;
	private int lamport_J;
	private int serverID_J;
	private int lamport_I;
	private int serverID_I;
	private int x;
	private int y;
	
	public Message(){};
	
	public Message(String msg, int lamport_J, int serverID_J, int lamport_I,
			int serverID_I, int x, int y) {
		super();
		this.msg = msg;
		this.lamport_J = lamport_J;
		this.serverID_J = serverID_J;
		this.lamport_I = lamport_I;
		this.serverID_I = serverID_I;
		this.x = x;
		this.y = y;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getLamport_J() {
		return lamport_J;
	}

	public void setLamport_J(int lamport_J) {
		this.lamport_J = lamport_J;
	}

	public int getServerID_J() {
		return serverID_J;
	}

	public void setServerID_J(int serverID_J) {
		this.serverID_J = serverID_J;
	}

	public int getLamport_I() {
		return lamport_I;
	}

	public void setLamport_I(int lamport_I) {
		this.lamport_I = lamport_I;
	}

	public int getServerID_I() {
		return serverID_I;
	}

	public void setServerID_I(int serverID_I) {
		this.serverID_I = serverID_I;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
