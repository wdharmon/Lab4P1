package pkgCore;

import java.util.HashMap;
import java.util.UUID;
import pkgEnum.eGameType ;
import pkgException.DeckException;

public class GamePlayBlackJack extends GamePlay {

	private HashMap<UUID, HandBlackJack> hmGameHands = new HashMap<UUID, HandBlackJack>();
	
	public GamePlayBlackJack(Table t) {
		
		super(t, eGameType.BLACKJACK);
		
		if (t.getTableDeck().getiDeckCount() == 0)
		{
			t.CreateDeck(pkgEnum.eGameType.BLACKJACK);
		}
				
		
		
		
		for (Player pAdd: t.GetTablePlayers())
		{
			hmGameHands.put(pAdd.getPlayerID(), new HandBlackJack());
		}	
	}
	
	public void DrawCardToHand(Player p) throws DeckException
	{
		HandBlackJack hBP = (HandBlackJack)hmGameHands.get(p.getPlayerID());
		hBP.Draw(this.getT().getTableDeck());
		
		hmGameHands.put(p.getPlayerID(), hBP);
		
	}
	
	
	

}
