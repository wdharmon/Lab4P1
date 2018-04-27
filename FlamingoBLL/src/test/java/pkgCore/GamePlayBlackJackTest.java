package pkgCore;

import static org.junit.Assert.*;

import org.junit.Test;

import pkgEnum.eGameType;

public class GamePlayBlackJackTest {

	@Test
	public void GamePlayBlackJackTest1() {
		
		Player p1 = new Player("Joe",1);
		Player p2 = new Player("Bert",2);
		
		Table t = new Table();
		
		t.AddPlayerToTable(p1);
		t.AddPlayerToTable(p2);
		
		GamePlayBlackJack gpbj = new GamePlayBlackJack(t);
		assertNotNull(gpbj);
		gpbj.AddPlayersToGame(t.GetTablePlayers());
		
		assertEquals(2,gpbj.GetPlayersInGame().size());
		
		
	}

}
