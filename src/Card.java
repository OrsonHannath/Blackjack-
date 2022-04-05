import java.awt.*;

public class Card {

    public String cardName;
    public int cardNumber;
    public int cardSuitInt;
    public String cardSuitString;
    public String cardColour;
    public String cardColourHex;
    public boolean faceDown;

    public Card(String cName, int cNum, int cSuitInt, String cSuitStr, String cColor, String cColorHex, boolean fDown){

        this.cardName = cName;
        this.cardNumber = cNum;
        this.cardSuitInt = cSuitInt;
        this.cardSuitString = cSuitStr;
        this.cardColour = cColor;
        this.cardColourHex = cColorHex;
        this.faceDown = fDown;
    }

    public String getCardStringInfo(){

        String cardInfo = "";
        if(cardName != "Ace") {

            cardInfo = cardName + " of " + cardSuitString + " (" + cardNumber + ")";
        }else{

            cardInfo = cardName + " of " + cardSuitString;
        }

        return cardInfo;
    }
}
