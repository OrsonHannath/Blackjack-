import java.util.ArrayList;

public class Dealer {

    public static ArrayList<Card> dealersCards = new ArrayList<Card>();

    public static int currentCountDealer;
    public static int knownCurrCountDealer;
    public static int numOfAcesDealer;

    public static void printCurrentCardsDealer(){

        int currCardInd = 0;
        StringBuilder outputString = new StringBuilder();

        for(Card c : dealersCards){
            if(!c.faceDown) {
                outputString.append("|").append(dealersCards.get(currCardInd).getCardStringInfo()).append("|   ");
                currCardInd++;
            }else{
                outputString.append("|Face Down Card|   ");
                currCardInd++;
            }
        }

        System.out.println(outputString.toString());
    }
}
