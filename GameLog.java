
/**
 * @author Brandon Domash
 * Creates a game log, which stores information about a completed solitaire game
 */
public class GameLog
{
    private boolean win;
    private int time,moves,score;
    /**
     * Default constructor for a GameLog, in which the user lost the game in 0 seconds, making 0 moves and having a score of -52 (the starting score of a game)
     */
    public GameLog()
    {
        win = false;
        time = 0;
        moves = 0;
        score = -52;
    }
    /**
     * Parameterized constructor for a GameLog, in which the data from the user's game is input
     * @param w Whether or not the user won the game
     * @param t Amount of seconds it took to complete the game
     * @param m The number of moves the user made
     * @param s The user's score during the game
     */
    public GameLog(boolean w,int t, int m, int s)
    {
        win = w;
        time = t;
        moves = m;
        score = s;
    }
    /**
     * Returns whether or not the user has won the game
     * @return whether or not the user has won the game
     */
    public boolean win()
    {
        return win;
    }
    /**
     * Returns the amount of time (in seconds) that the user's game was completed in
     * @return the amount of time (in seconds) that the user's game was completed in
     */
     public int getTime()
    {
        return time;
    }
    /**
     * Returns the number of moves in the user's game
     * @return the number of moves in the user's game
     */
    public int getMoves()
    {
        return moves;
    }
    /**
     * Returns the score of the user's game
     * @return the score of the user's game
     */
    public int getScore()
    {
        return score;
    }
    /**
     * Returns a String representation of a GameLog, in the form that it will be written in in the text file
     * Uses colons so that a user can easily split the String into parts when retrieving and organizing the data
     * @return a String representation of a GameLog
     */
    public String toString()
    {
        return win()+":"+getTime()+":"+getMoves()+":"+getScore();
    }
}
