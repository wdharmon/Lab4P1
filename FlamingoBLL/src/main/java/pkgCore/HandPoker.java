package pkgCore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import pkgEnum.eCardNo;
import pkgEnum.eHandStrength;
import pkgEnum.eRank;
import pkgEnum.eSuit;
import pkgException.HandException;

public class HandPoker extends Hand implements Comparable {

	private final int FOURROWS = 4;
	private final int THREEROWS = 3;
	private final int TWOROWS = 2;

	private final int ROW1 = 0;
	private final int ROW2 = 1;
	private final int ROW3 = 2;
	private final int ROW4 = 2;

	private ArrayList<CardRankCount> CRC = null;

	public HandPoker() {
		this.setHS(new HandScorePoker());
	}

	protected ArrayList<CardRankCount> getCRC() {
		return CRC;
	}

	public HandScorePoker getHandScorePoker() {
		return (HandScorePoker) this.getHS();
	}
	
	public HandScorePoker getHSP() {
		return (HandScorePoker) this.getHS();

	}

	@Override
	public HandScore ScoreHand() throws HandException {

		// If the hand isn't 5 cards... throw an exception
		if (this.getCards().size() != 5) {
			throw new HandException(this.getCards());
		}

		// Sort the hand by rank
		Collections.sort(super.getCards());

		// Count the frequence of cards, store in CRC ArrayList
		Frequency();

		// Score the hand using Java Reflections
		return ScoreHandReflections();
	}

	private HandScorePoker ScoreHandReflections() {

		HandScorePoker HSP = null;
		try {

			// c = structure of class 'Hand'
			Class<?> c = Class.forName("pkgCore.HandPoker");

			Object o = null;

			for (eHandStrength eHandStrength : eHandStrength.values()) {
				String strEvalMethod = eHandStrength.getEvalMethod();
				Method mEval = c.getDeclaredMethod(strEvalMethod, null);
				mEval.setAccessible(true);
				o = mEval.invoke(this, null);

				if ((boolean) o) {
					break;
				}

			}
			HSP = (HandScorePoker) this.getHandScorePoker();

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (IllegalAccessException x) {
			x.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return HSP;
	}

	private void Frequency() {

		CRC = new ArrayList<CardRankCount>();

		int iCnt = 0;
		int iPos = 0;

		for (eRank eRank : eRank.values()) {
			iCnt = (CountRank(eRank));
			if (iCnt > 0) {
				iPos = FindCardRank(eRank);
				CRC.add(new CardRankCount(eRank, iCnt, iPos));
			}
		}
		Collections.sort(CRC);
	}

	private int CountRank(eRank eRank) {
		int iCnt = 0;
		for (Card c : super.getCards()) {
			if (c.geteRank() == eRank) {
				iCnt++;
			}
		}
		return iCnt;
	}

	private int FindCardRank(eRank eRank) {
		int iPos = 0;

		for (iPos = 0; iPos < super.getCards().size(); iPos++) {
			if (super.getCards().get(iPos).geteRank() == eRank) {
				break;
			}
		}
		return iPos;
	}

	private boolean isRoyalFlush() {
		boolean bIsRoyalFlush = false;

		if ((this.getCards().get(0).geteRank() == eRank.ACE) && (this.getCards().get(1).geteRank() == eRank.KING)
				&& isStraightFlush()) {
			bIsRoyalFlush = true;
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.RoyalFlush);
			this.setHS(HSP);
		}

		return bIsRoyalFlush;
	}

	private boolean isStraightFlush() {
		boolean bisStraightFlush = false;

		if ((isFlush()) && (isStraight())) {
			bisStraightFlush = true;
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.StraightFlush);
			this.setHS(HSP);
		}
		return bisStraightFlush;
	}

	private boolean isFourOfAKind() {
		boolean bisFourOfAKind = false;

		if ((GetCRCSize() == TWOROWS) && ((GetCRCCount(ROW1) == 4) && (GetCRCCount(ROW2) == 1))) {
			bisFourOfAKind = true;
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.FourOfAKind);
			HSP.setHiCard(this.getCards().get(CRC.get(ROW1).getiCardPosition()));
			HSP.setLoCard(null);
			HSP.setKickers(FindTheKickers(this.getCRC()));
			this.setHS(HSP);
		}
		return bisFourOfAKind;
	}

	private boolean isFullHouse() {
		boolean bisFullHouse = false;

		if ((GetCRCSize() == TWOROWS) && ((GetCRCCount(ROW1) == 3) && (GetCRCCount(ROW2) == 2))) {
			bisFullHouse = true;
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.FullHouse);
			HSP.setHiCard(this.getCards().get(CRC.get(ROW1).getiCardPosition()));
			HSP.setLoCard(this.getCards().get(CRC.get(ROW2).getiCardPosition()));
			HSP.setKickers(FindTheKickers(this.getCRC()));
			this.setHS(HSP);
		}

		return bisFullHouse;

	}

	private boolean isFlush() {
		boolean bisFlush = false;

		int iCardCnt = super.getCards().size();
		int iSuitCnt = 0;

		for (eSuit eSuit : eSuit.values()) {
			for (Card c : super.getCards()) {
				if (eSuit == c.geteSuit()) {
					iSuitCnt++;
				}
			}
			if (iSuitCnt > 0)
				break;
		}

		if (iSuitCnt == iCardCnt) {
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.Flush);
			HSP.setHiCard(this.getCards().get(this.getCRC().get(ROW1).getiCardPosition()));
			HSP.setLoCard(null);
			HSP.setKickers(FindTheKickers(this.getCRC()));
			this.setHS(HSP);
			bisFlush = true;
		} else
			bisFlush = false;

		return bisFlush;
	}

	private boolean isStraight() {
		boolean bisStraight = false;
		int iDiff = 0;
		int i = 0;
		if ((this.getCards().get(0).geteRank() == eRank.ACE) && (this.getCards().get(1).geteRank() == eRank.FIVE)) {
			i = 1;
		}
		for (; i < this.getCards().size() - 1; i++) {
			iDiff = (this.getCards().get(i).geteRank().getiRankNbr()
					- this.getCards().get(i + 1).geteRank().getiRankNbr());
			if (iDiff == 1) {

				HandScorePoker HSP = (HandScorePoker) this.getHS();
				HSP.seteHandStrength(eHandStrength.Straight);

				if ((this.getCards().get(0).geteRank() == eRank.ACE)
						&& ((this.getCards().get(1).geteRank() == eRank.FIVE))) {
					HSP.setHiCard(this.getCards().get(1));
				} else {
					HSP.setHiCard(this.getCards().get(0));
				}
				HSP.setLoCard(null);
				HSP.setKickers(null);
				this.setHS(HSP);

				bisStraight = true;
			} else {
				bisStraight = false;
				break;
			}
		}
		return bisStraight;
	}

	private boolean isThreeOfAKind() {
		boolean bisThreeOfAKind = false;

		if ((GetCRCSize() == THREEROWS) && (GetCRCCount(ROW1) == 3)) {
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.ThreeOfAKind);
			HSP.setHiCard(this.getCards().get(this.getCRC().get(ROW1).getiCardPosition()));
			HSP.setLoCard(null);
			HSP.setKickers(FindTheKickers(this.getCRC()));
			this.setHS(HSP);
			bisThreeOfAKind = true;
		}
		return bisThreeOfAKind;
	}

	private boolean isTwoPair() {
		boolean bisTwoPair = false;

		if ((GetCRCSize() == THREEROWS) && ((GetCRCCount(ROW1) == 2) && (GetCRCCount(ROW2) == 2))) {
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.TwoPair);
			HSP.setHiCard(this.getCards().get(this.getCRC().get(ROW1).getiCardPosition()));
			HSP.setLoCard(this.getCards().get(this.getCRC().get(ROW2).getiCardPosition()));
			HSP.setKickers(FindTheKickers(this.getCRC()));
			this.setHS(HSP);
			bisTwoPair = true;
		}
		return bisTwoPair;
	}

	private boolean isPair() {

		boolean bisPair = false;
		if ((GetCRCSize() == FOURROWS) && ((GetCRCCount(ROW1) == 2) && (GetCRCCount(ROW2) == 1)
				&& (GetCRCCount(ROW3) == 1) && (GetCRCCount(ROW4) == 1))) {
			bisPair = true;
			HandScorePoker HSP = (HandScorePoker) this.getHS();
			HSP.seteHandStrength(eHandStrength.Pair);
			HSP.setHiCard(this.getCards().get(this.getCRC().get(ROW1).getiCardPosition()));
			HSP.setLoCard(null);
			HSP.setKickers(FindTheKickers(this.getCRC()));
			this.setHS(HSP);
			bisPair = true;
		}

		return bisPair;
	}

	private boolean isHighCard() {
		boolean bisHighCard = true;

		HandScorePoker HSP = (HandScorePoker) this.getHS();
		HSP.seteHandStrength(eHandStrength.HighCard);
		HSP.setHiCard(this.getCards().get(this.getCRC().get(ROW1).getiCardPosition()));
		HSP.setKickers(FindTheKickers(this.getCRC()));
		this.setHS(HSP);
		return bisHighCard;
	}

	private int GetCRCSize() {
		return CRC.size();
	}

	private int GetCRCCount(int iRow) {
		return CRC.get(iRow).getiCnt();
	}

	private ArrayList<Card> FindTheKickers(ArrayList<CardRankCount> CRC) {
		ArrayList<Card> kickers = new ArrayList<Card>();

		// Start at '1' to skip the first CRC
		for (int i = 1; i < CRC.size(); i++) {
			if (CRC.get(i).getiCnt() == 1) {
				kickers.add(this.getCards().get(CRC.get(i).getiCardPosition()));
			}
		}

		return kickers;
	}

	@Override
	public int compareTo(Object o) {

		HandPoker PassedHP = (HandPoker) o;
		
		HandScorePoker PassedHSP = PassedHP.getHandScorePoker();
		HandScorePoker ThisHSP = this.getHandScorePoker();
		
		//	Sort on Hand Strength
		if (PassedHSP.geteHandStrength().getHandStrength() - 
				ThisHSP.geteHandStrength().getHandStrength() != 0)
			return PassedHSP.geteHandStrength().getHandStrength() - 
					ThisHSP.geteHandStrength().getHandStrength();
		
		//	Then Sort on High Card
		if (PassedHSP.getHiCard().geteRank().getiRankNbr() -
				ThisHSP.getHiCard().geteRank().getiRankNbr() != 0)
			return PassedHSP.getHiCard().geteRank().getiRankNbr() -
					ThisHSP.getHiCard().geteRank().getiRankNbr();
		
		//	Then Sort on Low Card
		if ((PassedHSP.getLoCard() != null) && (ThisHSP.getLoCard() != null))
		{
			if (PassedHSP.getLoCard().geteRank().getiRankNbr() -
					ThisHSP.getLoCard().geteRank().getiRankNbr() != 0)
			{
				return PassedHSP.getLoCard().geteRank().getiRankNbr() -
						ThisHSP.getLoCard().geteRank().getiRankNbr();
			}
		}
		
		//	Then Sort on Kickers
		for (int k = 0; k< 4; k++)
		{
			if ((PassedHSP.getKickers().get(k) != null) && (ThisHSP.getKickers().get(k) != null))
			{
				if (PassedHSP.getKickers().get(k).geteRank().getiRankNbr() -
						PassedHSP.getKickers().get(k).geteRank().getiRankNbr() != 0)
				{
					return PassedHSP.getKickers().get(k).geteRank().getiRankNbr() -
							PassedHSP.getKickers().get(k).geteRank().getiRankNbr();
				}		
			}
		}
		


		return 0;
	}

}
