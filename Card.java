import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * @author Brandon Domash
 * Creates a card for the Solitaire game
 */
public class Card
{
    private BufferedImage pic;
    private final String suit;
    private final int value;
    private String rank;
    /**
     * Constructor for a card with a given value and suit. Also assigns an image to the card given the value/suit
     * @param v The value of the card (1-13)
     * @param s The suit of the card
     */
    public Card(int v,String s)
    {
        suit = s;
        value = v;
        rank = Integer.toString(v);
        if(v==11)
            rank = "Jack";
        else if(v==12)
            rank = "Queen";
        else if(v==13)
            rank = "King";
        else if(v==1)
            rank = "Ace";
        try{
            //pic = ImageIO.read(new File(value+suit+".png"));
            pic = ImageIO.read(getClass().getResource("/" + value+suit+".png"));
        }
        catch(IOException e)
        {
             JOptionPane.showMessageDialog(null,"Image not found for the " + rank + " of " + suit);
        }
    }
    /**
     * Returns the image of the card
     * @return the image of the card
     */
    public BufferedImage getImage()
    {
        return pic;
    }
    /**
     * Returns the suit of the card
     * @return the suit of the card
     */
    public String getSuit()
    {
        return suit;
    }
    /**
     * Returns the value of the card
     * @return the value of the card
     */
    public int getValue()
    {
        return value;
    }
    /**
     * Returns the rank of the card
     * @return the rank of the card
     */
    public String getRank()
    {
        return rank;
    }
    /**
     * Returns a String representation of the card Class
     * @return a String representation of the card Class
     */
    public String toString()
    {
        return getRank() + " of " + getSuit();
    }
}
