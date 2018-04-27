package pkgException;

import java.util.ArrayList;

import pkgCore.Card;

public class HandException extends Exception {

	private ArrayList<Card> CardsInHand = new ArrayList<Card>();

	public HandException(ArrayList<Card> cardsInHand) {
		super();
		CardsInHand = cardsInHand;
	}

	protected ArrayList<Card> getCardsInHand() {
		return CardsInHand;
	}
	
}
