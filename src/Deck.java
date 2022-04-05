import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    public static int deckSize = 52;
    public static int[] cardNumbers = {1,2,3,4,5,6,7,8,9,10,11,12,13};
    public static String[] cardNames = {"Ace","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Jack","Queen","King"};
    public static String[][] cardSuits = {{"0", "Diamonds", "Red", "#FF0000"}, {"1", "Hearts", "Red", "#FF0000"},{"2", "Clubs", "Black", "#000000"}, {"3", "Spades", "Black", "#000000"}};

    public static ArrayList<Card> createBlackJackDeck(int numOfDecks){
        ArrayList<Card> deck = new ArrayList<Card>();

        for (int d = 0; d < numOfDecks; d++) {
            for (int i = 0; i < cardSuits.length; i++) {
                for (int j = 0; j < cardNumbers.length; j++) {

                    //If playing blackjack make sure the picture cards are worth 10
                    int cardNum = 0;
                    if(Main.playingblackJack){
                        if(cardNumbers[j] > 10){
                            cardNum = 10;
                        }else{
                            cardNum = cardNumbers[j];
                        }
                    }

                    Card currCard = new Card(cardNames[j], cardNum, Integer.parseInt(cardSuits[i][0]), cardSuits[i][1], cardSuits[i][2], cardSuits[i][3], false);

                    /*currCard.cardName = cardNames[j];
                    currCard.cardSuitInt = Integer.parseInt(cardSuits[i][0]);
                    currCard.cardSuitString = cardSuits[i][1];
                    currCard.cardColour = cardSuits[i][2];
                    currCard.cardColourHex = cardSuits[i][3];
                    currCard.faceDown = false;*/

                    deck.add(currCard);
                }
            }
        }

        Collections.shuffle(deck);
        return deck;
    }
}
