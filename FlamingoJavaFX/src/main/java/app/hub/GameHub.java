package app.hub;

import java.io.IOException;

import netgame.common.Hub;
import pkgCore.Action;
import pkgCore.GamePlay;
import pkgCore.GamePlayBlackJack;
import pkgCore.Table;
import pkgEnum.eGameType;

public class GameHub extends Hub {

	private Table HubTable = new Table();

	
	public GameHub(int port) throws IOException {
		super(port);
	}

	@Override
	protected void messageReceived(int playerID, Object message) {
		super.messageReceived(playerID, message);

		
		if (message instanceof Action)
		{
			Action act = (Action)message;
			
			switch (act.getAction())
			{
			case Sit:
				HubTable.AddPlayerToTable(act.getPlayer());
				break;
			case Leave:
				HubTable.RemovePlayerFromTable(act.getPlayer());
				break;
			case TableState:
				break;
			}
		}
		

		resetOutput();
		
		super.sendToAll(HubTable);
	}
	
	

}
