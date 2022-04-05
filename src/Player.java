import java.util.ArrayList;

public class Player {

    public static ArrayList<Card> playersCards = new ArrayList<Card>();

    public static int currentCountPlayer;
    public static int numOfAcesPlayer;

    public static float playerBalance = 1000;

    public static void printCurrentCards(){

        int currCardInd = 0;
        StringBuilder outputString = new StringBuilder();

        for(Card c : playersCards){
            outputString.append("|").append(playersCards.get(currCardInd).getCardStringInfo()).append("|   ");
            currCardInd++;
        }

        System.out.println(outputString.toString());
    }
}
