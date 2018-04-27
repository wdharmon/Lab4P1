package pkgCore;

import static org.junit.Assert.*;

import org.junit.Test;

import pkgEnum.eSuit;
import pkgException.DeckException;

public class DeckTest {

	@Test
	public void TestDeck() {
		

		Deck d = new Deck();
		System.out.println(d.getiDeckCount());
		
		try {
			Card c = d.Draw();
		} catch (DeckException e) {		
			e.printStackTrace();
			fail("Deck Exception was raised");
		}
		System.out.println(d.getiDeckCount());
		Card c1 = d.Draw(eSuit.CLUBS);
		System.out.println(d.getiDeckCount());
	}

}
