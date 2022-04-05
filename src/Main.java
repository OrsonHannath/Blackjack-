import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    public static boolean playingblackJack = true;
    public static int bjNumOfDecks = 1;
    public static ArrayList<Card> availableCards = new ArrayList<Card>();
    public static ArrayList<Card> discardedCards = new ArrayList<Card>();

    public static boolean playerTurn = true;
    public static boolean dealerTurn = false;
    public static boolean gameComplete = true;

    public static float currentBet = 0;

    public static void main(String args[]){

        while(true) {

            if (gameComplete){

                //Get players bet
                Scanner scan = new Scanner(System.in);
                System.out.println("\n" + "Available Balance = " + Player.playerBalance);
                System.out.print("Enter your bet: ");
                String inString = scan.next();
                currentBet = 0;

                try{
                    currentBet = Float.parseFloat(inString);
                }catch (NumberFormatException e){

                }


                //Make sure player has enough money to make this bet
                boolean betPlaced = true;
                if(currentBet > Player.playerBalance){

                    System.out.println("Insufficient Funds!" + "\n");
                    betPlaced = false;
                }

                //Check if the number is negative
                if(currentBet <= 0){

                    System.out.println("Invalid Bet!" + "\n");
                    betPlaced = false;
                }

                if(betPlaced) {

                    System.out.println("\n" + "Player Bet: " + currentBet); //Formatting Code
                    System.out.println("Player Balance: " + (Player.playerBalance - currentBet) + "\n"); //Formatting Code
                    Player.playerBalance -= currentBet;
                    playRound();
                }
            }
        }
    }

    public static void playRound(){

        //If playing blackjack
        if (playingblackJack) {
            availableCards = Deck.createBlackJackDeck(bjNumOfDecks);

            //Reset the player and dealers hands
            Player.playersCards = new ArrayList<Card>();
            Player.currentCountPlayer = 0;
            Dealer.dealersCards = new ArrayList<Card>();
            Dealer.currentCountDealer = 0;
            Dealer.knownCurrCountDealer = 0;
            Player.numOfAcesPlayer = 0;
            Dealer.numOfAcesDealer = 0;

            playerTurn = true;
            dealerTurn = false;
            gameComplete = false;

            dealCards(); //Deal Cards
            summarizeCurrPosition(); //Summarize the current position

            while (playerTurn){
                //Check if player hits or stands
                Scanner scan = new Scanner(System.in);
                System.out.println("Enter your move: Hit (h) or Stand (s)");
                String inString = scan.next();

                switch(inString){
                    case "h":
                    case "hit":
                    case "Hit":

                        System.out.println(""); //Formatting Code
                        addCardPlayer(hit());
                        summarizeCurrPosition();
                        if(checkBust("player")){
                            playerTurn = false;
                            dealerTurn = false;
                            gameComplete = true;

                            System.out.println("Player Busts --- Player Loses");
                        }
                        break;
                    case "s":
                    case "stand":
                    case "Stand":
                        playerTurn = false;
                        dealerTurn = true;
                        break;
                    default:
                        playerTurn = true;
                        System.out.println("Invalid Entry!" + "\n");
                        break;
                }
            }

            //Sets all dealers cards to face up
            int dealerCount = 0;
            for (Card c : Dealer.dealersCards){
                c.faceDown = false;
                dealerCount += c.cardNumber;
            }
            Dealer.knownCurrCountDealer = dealerCount;

            while (dealerTurn){

                if(!checkDealer17()) {
                    addCardDealer(hit(), true);
                    summarizeCurrPosition();
                }else{

                    playerTurn = false;
                    dealerTurn = false;
                    gameComplete = true;

                    summarizeCurrPosition();

                    //Check how the game was won
                    checkWinner();

                    break;
                }

                if(checkBust("dealer")){

                    playerTurn = false;
                    dealerTurn = false;
                    gameComplete = true;

                    System.out.println("Dealer Busts --- Player Wins");
                    System.out.println("Funds Won: +" + currentBet * 1.5);
                    Player.playerBalance += currentBet * 1.5;
                }else if(checkDealer17()){

                    playerTurn = false;
                    dealerTurn = false;
                    gameComplete = true;

                    checkWinner();
                }

                sleepForMillis(2500);

            }
        }
    }

    public static void summarizeCurrPosition(){

        String playerCardCountStr = "";
        if(Player.numOfAcesPlayer > 0){

            if(Player.currentCountPlayer + 10 <= 21) {

                playerCardCountStr = Integer.toString(Player.currentCountPlayer) + " or " + Integer.toString(Player.currentCountPlayer + 10);
            }else{

                playerCardCountStr = Integer.toString(Player.currentCountPlayer);
            }
        }else{

            playerCardCountStr = Integer.toString(Player.currentCountPlayer);
        }

        String dealerCardCountStr = "";
        if(Dealer.numOfAcesDealer > 0){

            if(Dealer.knownCurrCountDealer + 10 <= 21) {

                dealerCardCountStr = Integer.toString(Dealer.knownCurrCountDealer) + " or " + Integer.toString(Dealer.knownCurrCountDealer + 10);
            }else{

                dealerCardCountStr = Integer.toString(Dealer.knownCurrCountDealer);
            }
        }else{

            dealerCardCountStr = Integer.toString(Dealer.knownCurrCountDealer);
        }

        System.out.println("");
        System.out.println("=========================== Players Hand (" + playerCardCountStr + ") ===========================");
        Player.printCurrentCards();
        System.out.println("=========================== Dealers Hand (" + dealerCardCountStr + ") ===========================");
        Dealer.printCurrentCardsDealer();
        System.out.println("========================================================================");
        System.out.println("");

    }

    public static void checkWinner(){

        if(Dealer.currentCountDealer == 21){

            boolean blackjack = false;
            if(Objects.equals(Dealer.dealersCards.get(0).cardName, "Ace") || Objects.equals(Dealer.dealersCards.get(0).cardName, "Jack") || Objects.equals(Dealer.dealersCards.get(0).cardName, "Queen") || Objects.equals(Dealer.dealersCards.get(0).cardName, "King")){

                String card0Name = Dealer.dealersCards.get(0).cardName;
                if(Objects.equals(Dealer.dealersCards.get(1).cardName, "Ace") || Objects.equals(Dealer.dealersCards.get(1).cardName, "Jack") || Objects.equals(Dealer.dealersCards.get(1).cardName, "Queen") || Objects.equals(Dealer.dealersCards.get(1).cardName, "King")){
                    if(!Objects.equals(Dealer.dealersCards.get(1).cardName, card0Name)){

                        //The pair of cards that add up to 21 are an ace and a picture card
                        System.out.println("Dealer Wins with Blackjack");
                        System.out.println("Funds Lost: -" + currentBet);
                        blackjack = true;
                    }
                }
            }

            if(!blackjack){

                if(Dealer.currentCountDealer > Player.currentCountPlayer){

                    System.out.println("Dealer Wins with Higher Hand");
                    System.out.println("Funds Lost: -" + currentBet);
                }else if(Dealer.currentCountDealer < Player.currentCountPlayer){

                    System.out.println("Player Wins with Higher Hand");
                    System.out.println("Funds Won: +" + currentBet * 1.5);
                    Player.playerBalance += currentBet * 1.5;
                }else{

                    System.out.println("Dealer And Player Draw");
                    System.out.println("Players Balance: +0");
                    Player.playerBalance += currentBet;
                }
            }
        }else if(Player.currentCountPlayer == 21){

            if(Objects.equals(Player.playersCards.get(0).cardName, "Ace") || Objects.equals(Player.playersCards.get(0).cardName, "Jack") || Objects.equals(Player.playersCards.get(0).cardName, "Queen") || Objects.equals(Player.playersCards.get(0).cardName, "King")){

                String card0Name = Player.playersCards.get(0).cardName;
                if(Objects.equals(Player.playersCards.get(1).cardName, "Ace") || Objects.equals(Player.playersCards.get(1).cardName, "Jack") || Objects.equals(Player.playersCards.get(1).cardName, "Queen") || Objects.equals(Player.playersCards.get(1).cardName, "King")){
                    if(!Objects.equals(Player.playersCards.get(1).cardName, card0Name)){

                        //The pair of cards that add up to 21 are an ace and a picture card
                        System.out.println("Player Wins with Blackjack");
                        System.out.println("Funds Won: +" + currentBet * 2.5);
                        Player.playerBalance += currentBet * 2.5;
                    }
                }
            }
        }else if(Player.currentCountPlayer > Dealer.currentCountDealer){

            System.out.println("Player Wins with Higher Hand");
            System.out.println("Funds Won: +" + currentBet * 1.5);
            Player.playerBalance += currentBet * 1.5;
        }else if(Player.currentCountPlayer < Dealer.currentCountDealer){

            System.out.println("Dealer Wins with Higher Hand");
            System.out.println("Funds Lost: -" + currentBet);
        }else {

            System.out.println("Dealer And Player Draw");
            System.out.println("Players Balance: +0");
            Player.playerBalance += currentBet;
        }
    }

    public static void dealCards(){

        //Select the cards that are being dealt
        Card dealtCard1 = availableCards.get(0);
        Card dealtCard2 = availableCards.get(1);
        Card dealtCard3 = availableCards.get(2);
        Card dealtCard4 = availableCards.get(3);

        //Remove the cards from the availableCards and move to discarded Cards
        availableCards.remove(0);
        discardedCards.add(dealtCard1);

        availableCards.remove(0);
        discardedCards.add(dealtCard2);

        availableCards.remove(0);
        discardedCards.add(dealtCard3);

        availableCards.remove(0);
        discardedCards.add(dealtCard4);

        //Give the card to the player
        addCardPlayer(dealtCard1); //Give the card to the player

        //Give a card to the dealer
        addCardDealer(dealtCard2, true); //Give the card to the player

        //Give the second card to the player
        addCardPlayer(dealtCard3); //Give the card to the player

        //Give a second card to the dealer
        addCardDealer(dealtCard4, false); //Give the card to the player
    }

    public static void addCardPlayer(Card c){
        Player.playersCards.add(c);
        Player.currentCountPlayer += c.cardNumber;

        //if the card is an ace update number of aces in player hand
        if(c.cardName == "Ace") {
            Player.numOfAcesPlayer++;
        }

        //Let the player know what card they are dealt
        System.out.println("Player's dealt: " + c.getCardStringInfo());
        sleepForMillis(1000);
    }

    public static void addCardDealer(Card c, boolean faceUp){
        c.faceDown = !faceUp;
        Dealer.dealersCards.add(c);
        Dealer.currentCountDealer += c.cardNumber;

        //if the card is an ace update number of aces in dealer hand
        if(c.cardName == "Ace" && faceUp) {
            Dealer.numOfAcesDealer++;
        }

        if(faceUp){
            Dealer.knownCurrCountDealer += c.cardNumber;

            //Let the player know what card the dealer is dealt
            System.out.println("Dealer's dealt: " + c.getCardStringInfo());
            sleepForMillis(1000);
        }else{

            System.out.println("Dealers dealt: Face Down Card");
            sleepForMillis(1000);
        }
    }

    public static Card hit(){

        //Select the card that is being dealt
        Card dealtCard = availableCards.get(0);

        //Remove the card from the availableCards and move to discarded Cards
        availableCards.remove(0);
        discardedCards.add(dealtCard);
        return dealtCard;
    }

    public static boolean checkBust(String playerordealer){

        //Check if the hand is bust
        boolean bust = false;
        int currentCount = 0;

        if(playerordealer == "player") {
            currentCount = Player.currentCountPlayer;
        }else if(playerordealer == "dealer"){
            currentCount = Dealer.currentCountDealer;
        }

        if (currentCount > 21){
            bust = true;
        }else{
            bust = false;
        }

        return  bust;
    }

    public static boolean checkDealer17(){

        boolean higherorequal = false;

        //If dealer has aces check their decks soft and hard value
        int dealerSoft = -1;
        int dealerHard = -1;
        if(Dealer.numOfAcesDealer > 0){

            //Dealer has some amount of aces so this should be accounted for
            dealerSoft = Dealer.currentCountDealer;
            dealerHard = Dealer.currentCountDealer + 10;
        }else{

            dealerSoft = Dealer.currentCountDealer;
            dealerHard = Dealer.currentCountDealer;
        }

        //Check if hard deck is over 21
        if(dealerHard > 21){

            //Check if soft deck is over 17
            if(dealerSoft >= 17){

                Dealer.currentCountDealer = dealerSoft;
                higherorequal = true;
            }else{

                Dealer.currentCountDealer = dealerSoft;
                higherorequal = false;
            }

            return higherorequal;
        }

        if(dealerHard >= 17){

            Dealer.currentCountDealer = dealerHard;
            higherorequal = true;
            return higherorequal;
        }

        return higherorequal;
    }

    public static void sleepForMillis(int millis){

        try {
            Thread.sleep(millis);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
