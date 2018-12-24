import java.util.*;
/**
 * @author Brandon Domash
 * A deck is a stack of 52 different cards
 */
public class Deck
{
   
    private Stack<Card> deck = new Stack<Card>();

    /**
     * Creates a no-args constructor for the deck, adding all 52 cards to the deck.
     */
    public Deck()
    {
        for(int j = 0;j<4;j++)
        {
            for(int i =1;i<=13;i++)
            {
                String suit = "";
                if(j==0)
                {
                    suit = "Spades";
                }
                else if(j==1)
                {
                    suit = "Hearts";
                }
                else if(j==2)
                {
                    suit = "Clubs";
                }
                else if(j==3)
                {
                    suit = "Diamonds";
                }
                deck.push(new Card(i,suit));
            }
        }
        
    }
    /**
     * Adds a card to the top of the deck
     * @param c the card to be added to the top of the deck
     */
    public void addCard(Card c)
    {
        deck.push(c);
    }
    /**
     * Returns and removes the top card of the deck
     * @return The top card of the deck
     */
    public Card removeTopCard()
    {
        return deck.pop();
    }
    /**
     * Returns the amount of cards in the deck
     * @return the size of the deck
     */
    public int getSize()
    {
        return deck.size();
    }
    /**
     * Returns the deck of cards
     * @return The deck in the form of a Stack<Card> 
     */
    public Stack<Card> getDeck()
    {
        return deck;
    }
    /**
     * Shuffles the deck of cards
     */
    public void shuffle()
    {
        Collections.shuffle(deck);
    }
}
