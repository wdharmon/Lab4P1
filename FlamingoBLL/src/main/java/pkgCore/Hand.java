package pkgCore;

import java.util.ArrayList;
import java.util.Collections;

import pkgException.DeckException;
import pkgException.HandException;

public abstract class Hand {

	private ArrayList<Card> cards;
	private HandScore HS;

	public Hand() {
		cards = new ArrayList<Card>();
	}

	protected ArrayList<Card> getCards() {
		return cards;
	}

	public void Draw(Deck d) throws DeckException {
		cards.add(d.Draw());
	}
	
	protected HandScore getHS() {
		return HS;
	}

	protected void setHS(HandScore hS) {
		HS = hS;
	}

	public abstract HandScore ScoreHand() throws HandException;

	protected void AddCard(Card c) {
		cards.add(c);
	}
	

}
