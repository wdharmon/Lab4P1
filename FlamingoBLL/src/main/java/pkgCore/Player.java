package pkgCore;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable{

	private UUID PlayerID;
	private String PlayerName;
	private int iPlayerPosition;
	private int iPokerClientID;
	
	public Player(String playerName, int iPokerClientID) {
		
		PlayerID = UUID.randomUUID();
		this.PlayerName = playerName;
		this.iPokerClientID = iPokerClientID;
	}
	public String getPlayerName() {
		return PlayerName;
	}
	public void setPlayerName(String playerName) {
		PlayerName = playerName;
	}
	public UUID getPlayerID() {
		return PlayerID;
	}
	public int getiPlayerPosition() {
		return iPlayerPosition;
	}
	public void setiPlayerPosition(int iPlayerPosition) {
		this.iPlayerPosition = iPlayerPosition;
	}
	public int getiPokerClientID() {
		return iPokerClientID;
	}
	public void setiPokerClientID(int iPokerClientID) {
		this.iPokerClientID = iPokerClientID;
	}
	
	
	
}
