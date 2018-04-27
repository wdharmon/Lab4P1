package pkgCore;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import pkgEnum.eGameType;

public class TableTest {

	@Test
	public void TestTable() {
		Player p1 = new Player("Joe",1);
		Player p2 = new Player("Bert",2);
		
		Table t = new Table();
		
		assertNotNull(t);
		
		t.AddPlayerToTable(p1);
		
		List<Player> lstPlayer = t.GetTablePlayers();
		
		assertEquals(1,lstPlayer.size());
		
		Player pGet = t.GetPlayerFromTable(p1);
		
		assertNotNull(pGet);
		
		assertEquals(p1,pGet);
		
		t.ClearTable();
		

		assertEquals(0,t.GetTablePlayers().size());
		
		t = null;
		
		assertNull(t);
		
	}
	
	
	@Test
	public void TestTableDeck() {
	
	
		Table t = new Table();
		assertNotNull(t);
		
		t.CreateDeck(eGameType.BLACKJACK);
		
		assertNotNull(t.getTableDeck());
		
		assertEquals(52 * 6, t.getTableDeck().getiDeckCount());
		
	}
	

}
