
/**
 * Write a description of class Moves here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Moves
{
    // instance variables - replace the example below with your own
    private int num;
    private int src;
    private int dest;
    private int numCards;
    private int cardsFlipped;//HOW MANY CARDS ARE TURNED OVER IN THE PILE BEFORE THE MOVE
    //UNDO-ING SHOULD RETURN GAME TO THIS AMOUNT OF CARDS TURNED
    /**
     * Constructor for objects of class Moves
     */
    public Moves()
    {
        num = 0;
        src = 0;
        dest = 0;
        numCards = 0;
    }
    /**
     * Constructor used for 1-card solitaire
     */
    public Moves(int n, int s, int d, int nc){
        num = n;
        src = s;
        dest = d;
        numCards = nc;
        cardsFlipped = 1;
    }
    /**
     * Constructor used for 3-card solitaire
     */
    public Moves(int n, int s, int d, int nc, int cf){
        num = n;
        src = s;
        dest = d;
        numCards = nc;
        cardsFlipped = cf;
    }
    public int getCardsFlipped(){
        return cardsFlipped;
    }
    public int getNum(){
        return num;
    }
    public int getSrc(){
        return src;
    }
    public int getDest(){
        return dest;
    }
    public int getNumCards(){
        return numCards;
    }
    public String toString(){
        return num+":"+src+":"+dest+":"+numCards+":"+cardsFlipped;
    }
}
