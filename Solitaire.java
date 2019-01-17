import java.util.*;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
/**
 * @author Brandon Domash
 * Creates the Solitaire game. Draws the screen and handles what to do when the screen is clicked
 */
public class Solitaire extends JPanel implements Runnable, KeyListener
{
    private static final int WIDTH=700, HEIGHT=550;
    private Stack<Card> cardPile, spadesPile, heartsPile, clubsPile,diamondsPile; //each of the top 5 stacks
    private Stack<Card> col1,col2,col3,col4,col5,col6,col7;//each of the up card stacks
    private Stack<Card> col1Down,col2Down,col3Down,col4Down,col5Down,col6Down,col7Down; //down card stacks
    private final Location deckLoc,cardPileLoc, spadesPileLoc,heartsPileLoc,clubsPileLoc,diamondsPileLoc,col1Loc,col2Loc,col3Loc,col4Loc,col5Loc,col6Loc,col7Loc; //the location of upper-left corner of the furthest down card
    private Deck deck;
    private BufferedImage cardBack, spadesImage, heartsImage, clubsImage,diamondsImage;
    private final ArrayList<Location> allColLocs; //arrayList that stores each of the columns location. 
    private final int CARDPILE, SPADESPILE,HEARTSPILE,CLUBSPILE,DIAMONDSPILE,COL1,COL2,COL3,COL4,COL5,COL6,COL7, DECK, RESET;
    
    private boolean run;
    private int x,y; //x and y coordinates of the frame that have been clicked
    private boolean pileClicked,spadesPileClicked,heartsPileClicked,clubsPileClicked,diamondsPileClicked; //whether or not a column was the last thing to be clicked
    private boolean col1Clicked,col2Clicked,col3Clicked,col4Clicked,col5Clicked,col6Clicked,col7Clicked;
    private int numExtraCards;//the number of cards removed from the top up-card that have been selected
    private boolean sameCard;//whether or not the same card in a column has been clicked twice in a row
    private boolean rightClick;//whether or not the user has right-clicked the mouse
    private long timer;//records the beginning time of the game
    private int count,score;//how many moves the user has made, score of the user's game, 
    private int movesCount; 
    private boolean usedUndo;
    private ArrayList<Moves> movesList;
    private Color background;
    
    private boolean deal3;
    private int cardsTurned;
    private int cardsLeft;
    private String cardImage;
    private int scoreType;
    private boolean hasUsedUndo;
    /**
     * Creates a no-args constructor for the solitaire game. Initializes each stack and the JPanel for the game
     */
    public Solitaire()
    {
        setFocusable(true);
        requestFocusInWindow();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //setSize(new Dimension(WIDTH,HEIGHT));
        //setExtendedState(Frame.MAXIMIZED_BOTH);  
        cardPile = new Stack<Card>();
        spadesPile = new Stack<Card>();
        heartsPile = new Stack<Card>();
        clubsPile = new Stack<Card>();
        diamondsPile = new Stack<Card>();
        col1 = new Stack<Card>();
        col2 = new Stack<Card>();
        col3 = new Stack<Card>();
        col4 = new Stack<Card>();
        col5 = new Stack<Card>();
        col6 = new Stack<Card>();
        col7 = new Stack<Card>();
        col1Down = new Stack<Card>();
        col2Down = new Stack<Card>();
        col3Down = new Stack<Card>();
        col4Down = new Stack<Card>();
        col5Down = new Stack<Card>();
        col6Down = new Stack<Card>();
        col7Down = new Stack<Card>();
        deck = new Deck();
        
        deckLoc = new Location(30,20);
        cardPileLoc = new Location(125,20);
        spadesPileLoc = new Location(315,20);
        heartsPileLoc = new Location(410,20);
        clubsPileLoc = new Location(505,20);
        diamondsPileLoc = new Location(600,20);
        col1Loc = new Location(30,154);
        col2Loc = new Location(125,154);
        col3Loc = new Location(220,154);
        col4Loc = new Location(315,154);
        col5Loc = new Location(410,154);
        col6Loc = new Location(505,154);
        col7Loc = new Location(600,154);
        
        allColLocs = new ArrayList<Location>();
        allColLocs.add(col1Loc);
        allColLocs.add(col2Loc);
        allColLocs.add(col3Loc);
        allColLocs.add(col4Loc);
        allColLocs.add(col5Loc);
        allColLocs.add(col6Loc);
        allColLocs.add(col7Loc);
        RESET = -2;
        DECK = -1;
        CARDPILE = 0;
        COL1 = 1;
        COL2 = 2;
        COL3 = 3;
        COL4 = 4;
        COL5 = 5;
        COL6 = 6;
        COL7 = 7;
        SPADESPILE = 8;
        HEARTSPILE = 9;
        CLUBSPILE = 10;
        DIAMONDSPILE = 11;
        usedUndo = false;
        
        rightClick = false;
        hasUsedUndo = false;
        addMouseListener(new MouseAdapter(){
                /**
                 * Sets the (x,y) coordinate of the location that the mouse has clicked and determines if the mouse has been right-clicked
                 */
                public void mousePressed(MouseEvent e){
                    if(e.getButton()==MouseEvent.BUTTON1)
                    {   
                        x = e.getX();
                        y = e.getY();
                    }
                    if(e.getButton()==MouseEvent.BUTTON3)
                    {
                        if(!rightClick)
                            rightClick = true;
                        else
                            rightClick = false;
                    }
                }
        });
        
        pileClicked = false;
        col1Clicked = false;
        col2Clicked = false;
        col3Clicked = false;
        col4Clicked = false;
        col5Clicked = false;
        col6Clicked = false;
        col7Clicked = false;
        spadesPileClicked = false;
        heartsPileClicked = false;
        clubsPileClicked = false;
        diamondsPileClicked = false;
        run = true;
        numExtraCards = 0;
        sameCard = false;
        timer = 0;
        count = 0;
        score = -52;
        
        //resets the moves log
        background = new Color(0,150,0);
        movesList = new ArrayList<Moves>();
        PrintWriter writer;
        
        //starts a new file for moves.txt
        try
        {
            writer = new PrintWriter(new File("moves.txt"));
     
            writer.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Moves file not found");
        }
        
        try {                
            spadesImage = ImageIO.read(new File("spade.png"));
            heartsImage = ImageIO.read(new File("heart.png"));
            clubsImage = ImageIO.read(new File("club.png"));
            diamondsImage = ImageIO.read(new File("diamond.png"));
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Image not found");
        }
        cardsTurned = 0;
        cardsLeft = 0;
        
        FileReader filereader;
        BufferedReader reader;
        String line;
        try{
            filereader = new FileReader("settings.txt");
            reader = new BufferedReader(filereader);
            
            line = reader.readLine();
            if(Integer.parseInt(line)==3)
                deal3 = true;
            else
                deal3 = false;
            scoreType = Integer.parseInt(reader.readLine());
            cardImage = reader.readLine();
        }
        catch(IOException e){
            System.out.println("There was a problem reading settings.txt");
        }
        cardImage += ".png";
        try{
            cardBack = ImageIO.read(new File(cardImage));
            //cardBack = ImageIO.read(getClass().getResource("BlueCardBack.png"));
            //cardBack = ImageIO.read(getClass().getResource("back-fish-on-light-blue.png"));
            
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Error. Card back image not found");
        }
        
        //JOptionPane.showOptionDialo
    }
    public void settings()
    {
        String[] arr = {"Blue Standard","Black Castle", "Dark Blue Fish", "Light Blue Fish", "Hands", "Black Ivy",
            "Blue Ivy","Palm Tree","Robot","Roses","Sea Shell","Red Weave","Yellow Weave"};
        JPanel panel = new JPanel(new GridLayout(0,3));
        panel.add(new JLabel(""));
        panel.add(new JLabel("Game Type"));
        panel.add(new JLabel(""));
        JRadioButton button1 = new JRadioButton("Deal 1");
        JRadioButton button2 = new JRadioButton("Deal 3");
       
        ButtonGroup group = new ButtonGroup();
        group.add(button1);
        group.add(button2);
        
        
        FileReader filereader;
        BufferedReader reader;
        String line;
        int currGameType = 0;
        String currCardBack = "";
        int currScoringType = 0;
        try{
            filereader = new FileReader("settings.txt");
            reader = new BufferedReader(filereader);
            
            line = reader.readLine();
            currGameType = Integer.parseInt(line);
            
            currScoringType = Integer.parseInt(reader.readLine());
            currCardBack = reader.readLine();
        }
        catch(IOException e){
            System.out.println("There was a problem reading settings.txt");
        }
       
        if(currGameType==3)
            button2.setSelected(true);
        else
            button1.setSelected(true);
        
        
        
        //panel.add(add(new JLabel("")));
        panel.add(button1);
        panel.add(button2);
        
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel("Scoring Type"));
        panel.add(new JLabel(""));
        JRadioButton button3 = new JRadioButton("Standard");
        JRadioButton button4 = new JRadioButton("Vegas");
        JRadioButton button7= new JRadioButton("Timed");
        button4.setEnabled(false);
        button7.setEnabled(false);
        if(currScoringType==0)
            button3.setSelected(true);
        else if(currScoringType==1)
            button4.setSelected(true);
        else if(currScoringType==2)
            button7.setSelected(true);
        
        ButtonGroup group2 = new ButtonGroup();
        group2.add(button3);
        group2.add(button4);
        group2.add(button7);

        //panel.add(add(new JLabel("")));
        panel.add(button3);
        panel.add(button4);
        panel.add(button7);
       
        panel.add(new JLabel(""));
        panel.add(new JLabel("Deck Type"));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
     
        JComboBox<String> backs = new JComboBox<String>(arr);
        int defaultCardIndex = 1;
        
        for(int i =0;i<arr.length;i++)
        {
            if(arr[i].equals(currCardBack)){
                defaultCardIndex = i;
            }
            
        }
        backs.setSelectedIndex(defaultCardIndex);
        panel.add(backs);
        ImageIcon icon = new ImageIcon("1Spades.png");
        JOptionPane.showMessageDialog(null, panel, "Settings", 0, icon);
        
       
        PrintWriter writer;
        try{
            //writer = new PrintWriter(new FileWriter(new File("Settings.txt"),true));
            writer = new PrintWriter("Settings.txt");
            int newGameType = 0;
            int newScoringType = 0;
            String newDeckBack = "";
            if(button1.isSelected()){
                writer.println("1");
                newGameType = 1;
                
            }
            else if(button2.isSelected()){
                writer.println("3");
                newGameType = 3;
               
            }
            
            if(button3.isSelected()){
                writer.println("0");
                newScoringType = 0;
            }
            else if(button4.isSelected()){
                writer.println("1");
                newScoringType = 1;
            }
            else if(button7.isSelected()){
                writer.println("2");
                newScoringType = 2;
            }
            newDeckBack = backs.getSelectedItem().toString();
            //writer.println();
            if(newGameType!=currGameType || newScoringType != currScoringType || !newDeckBack.equals(currCardBack)){
                if(this.elapsedTime()==0)
                    newGame();
                else
                    JOptionPane.showMessageDialog(null,"Settings have been updated for the next game");
            }
            writer.println(backs.getSelectedItem().toString());
            
            
            writer.close();
        }
        catch(IOException e){
            System.out.println("File settings.txt not found");
        }
         
        
        
        
    }
    public boolean getGameType()
    {
        if(deal3)
            return true;
        return false;
    }
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).getKeyCode())
        {
            newGame();
        }
        /*
        else if(e.getKeyCode() == KeyEvent.VK_U)
        {
            undoMove();
        }
        */
        else if(e.getKeyCode()==KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).getKeyCode())
        {
            undoMove();
            hasUsedUndo = true;
        }
        else if(e.getKeyCode()==KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).getKeyCode())
        {
            displayStats();
        }
        else if(e.getKeyCode()==KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).getKeyCode())
        {
            quitAction();
        }
        else if(e.getKeyCode()==KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).getKeyCode())
        {
            helpAction();
        }
        else if(e.getKeyCode()==KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).getKeyCode())
        {
            aboutAction();
        }
        else if(e.getKeyCode()==KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()).getKeyCode())
        {
            settings();
        }
    }
    public void keyReleased(KeyEvent e)
    {

    }
    public void keyTyped(KeyEvent e)
    {

    }    
    public void setMode(int mode)
    {
        if(mode==1)
            deal3 = false;
        else if(mode==0)
            deal3 = true;
    }
    public void newGame(){
        String[] options = {"Yes","No"};
        int newGame = 0;
        if(this.elapsedTime()!=0)
            newGame = JOptionPane.showOptionDialog(null,"Are you sure you want to start a new game? Stats will be recorded","Restart?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        else //no elapsed time
        {
            this.run(false);
            if(this.elapsedTime()!=0)
                this.recordGameLog(false);
        }   
        
        //newGame = JOptionPane.showOptionDialog(null,"Are you sure you want to start a new game? Stats will NOT be recorded","Restart?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if(newGame == 0)
        {
            this.run(false);
            if(this.elapsedTime()!=0)
                this.recordGameLog(false);
        }
        else if(newGame == 1)
        {
            //do nothing
        }
                     
    }
    public void quitAction()
    {
        String[] options= {"Yes","No"};
                 
        int exit = 0;
        if(this.elapsedTime()!=0)
            exit = JOptionPane.showOptionDialog(null,"Are you sure you want to exit? Stats will be recorded","Exit?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        else
            System.exit(0);
            //exit = JOptionPane.showOptionDialog(null,"Are you sure you want to exit? Stats will NOT be recorded","Exit?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        
        if(exit==0)
        {
            //record stats
            if(this.elapsedTime()!=0)
                this.recordGameLog(false);
            System.exit(0);
        }
        else
        {
            //do nothing
        }
    }
    public void helpAction()
    {
        String str = "Solitaire\n\n";
        str+= "The object:\nBuild four stacks of cards, one for each suit, in ascending order, from ace to king.\n\n";
        str+= "The table:\nSolitaire is played with a single deck of 52 cards. The game begins with 28 cards arranged into seven columns. \nThe first column contains one card, the second has two cards, and so on. The top card in each column is face up, the rest are face down.\nFour Home stacks are positioned at the upper-right corner. This is where you build the piles needed to win.\n\n";
        str+= "How to play:\nEach Home stack must start with an ace. If you don't have any, you'll have to move cards between columns until you uncover one.\nYou can't move cards between columns at random, however. Columns must be built in descending order, from king to ace. So you can place a 10 on a jack, but not on a 3.\nCards in columns must also alternate red and black. You aren't limited to moving single cards.\n";
        str+= "You can also move sequentially organized runs of cards between columns. Just click the deepest card in the run and click them all to another column.\nIf you run out of moves, you'll have to draw more cards by clicking the deck in the upper-left corner.\nIf the deck runs out, click its outline on the table to reshuffle it.\n";
        str+= "You can move a card to any stack either by selecting a card and clicking on the proper stack or by simply double-clicking the card you would like to move.\n";
        str+= "Right-clicking will automatically put any cards into the Home stacks when applicable.\n\n";
        str+= "Scoring:\nThe scoring system is simple. You start with -52 points to start each game and gain 3 points for every card that is put into a Home stack.\nThe maximum points you can gain in a single game is 104.";



        //System.out.println(str);
        ImageIcon icon = new ImageIcon(getClass().getResource("Joker_Red_Icon.png"));
        JOptionPane.showMessageDialog(null,str,"How To Play",JOptionPane.INFORMATION_MESSAGE,icon);
    }
    public void aboutAction()
    {
        String str = "PROJECT TITLE: Solitaire\n\n";
        str+= "VERSION: 3.2\n\n";
        str+= "LAST UPDATED: 7/22/17\n\n";
        str+= "SUBMITTED: 12/15/14\n\n";
        str+= "HOW TO START THIS GAME: Right click on the Frame class and click on the main method\n\n";
        str+= "AUTHOR: Brandon Domash\n\n";
        ImageIcon icon = new ImageIcon(getClass().getResource("Joker_Black_Icon.png"));
        JOptionPane.showMessageDialog(null,str,"About",JOptionPane.INFORMATION_MESSAGE,icon);
    }
    
    public int getMoves(){
        return movesCount;
    }
    /**
     * Records whether or not the user has won, the length of the game, how many moves the user made and the user's game score and adds it to the game log text file
     * Only way to bypass this is by manually resetting the JVM
     * @param win True if the user has won the game, otherwise false
     */
    public void recordGameLog(boolean win)
    {
        
        PrintWriter writer;
        GameLog g = new GameLog(win,elapsedTime(),count,score);
        try
        {
            writer = new PrintWriter(new FileWriter(new File("Game Logs.txt"),true));
            writer.println(g);//gamelogs' toString will write this as "win:time:moves:score"
            writer.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Game Logs file not found");
        }
        
    }
    
    /**
     * Displays the stats in a neat JOptionPane after Retrieving data from Game Log text file and making calculations
     */
    public void displayStats()
    {
        ArrayList<GameLog> arr = new ArrayList<GameLog>();
        ArrayList<GameLog> gamesWon = new ArrayList<GameLog>();
        String str;    
        FileReader filereader;
        ImageIcon icon = new ImageIcon((getClass().getResource("Stats.png")));
        try{
            filereader = new FileReader("Game Logs.txt");
            BufferedReader bufferedreader = new BufferedReader(filereader);

            String line = bufferedreader.readLine();
            while(line!=null)
            {
                String b = line.split(":")[0];
                boolean x;
                if(b.equalsIgnoreCase("true"))
                    x = true;
                else 
                    x = false;
                arr.add(new GameLog(x,Integer.parseInt(line.split(":")[1]),Integer.parseInt(line.split(":")[2]),Integer.parseInt(line.split(":")[3])));
                line = bufferedreader.readLine();
            }
            int gamesPlayed = arr.size();
            if(gamesPlayed !=0)
            {
                int wins = 0;
                for(GameLog g: arr)
                {
                    if(g.win())
                    {
                        wins++;
                        gamesWon.add(g);
                    }
                }
                int losses = gamesPlayed - wins;
                double percentWon = Math.round((((double)wins)/gamesPlayed) * 1000)/10.0;
                int totalsecs = 0;
                int totalscore = 0;
                for(GameLog g: arr)
                {
                    totalsecs+=g.getTime();
                    totalscore+=g.getScore();
                }
                double averagescore = Math.round(((double)totalscore)/arr.size()*10)/10.0;
                double averagesecs = Math.round(((double)totalsecs)/arr.size());
                //int averagemins = 0;
                //int totalmins = 0;
                //int totalhours = 0;
                //             while(totalsecs>=60)
                //             {
                //                 totalmins++;
                //                 totalsecs -=60;
                //             }
                //             while(totalmins >= 60)
                //             {
                //                 totalmins -=60;
                //                 totalhours++;
                //             }
                //             while(averagesecs>=60)
                //             {
                //                 averagesecs -=60;
                //                 averagemins++;
                //             }
                int averagewinsecs = 0;
                //int averagewinmins = 0;
                for(GameLog g: gamesWon)
                {
                    averagewinsecs+= g.getTime();
                    
                }
                if(gamesWon.size()!=0)
                    averagewinsecs /= gamesWon.size();
                //             while(averagewinsecs>=60)
                //             {
                //                 averagewinsecs -=60;
                //                 averagewinmins++;
                //             }
                int shortestwintime;
                int longestwintime;
                int fewestwinningmoves;
                int mostwinningmoves;
                if(gamesWon.size()>0)
                {
                    shortestwintime = gamesWon.get(0).getTime();
                    longestwintime = gamesWon.get(0).getTime();
                    fewestwinningmoves = gamesWon.get(0).getMoves();
                    mostwinningmoves = gamesWon.get(0).getMoves();
                    for(int i = 1;i<gamesWon.size();i++)
                    {
                        if(gamesWon.get(i).getTime()<shortestwintime)
                            shortestwintime = gamesWon.get(i).getTime();
                        if(gamesWon.get(i).getTime()>longestwintime)
                            longestwintime = gamesWon.get(i).getTime();
                        if(gamesWon.get(i).getMoves()<fewestwinningmoves)
                            fewestwinningmoves = gamesWon.get(i).getMoves();
                        if(gamesWon.get(i).getMoves()>mostwinningmoves)
                            mostwinningmoves = gamesWon.get(i).getMoves();
                    }
                }
                else 
                {
                    shortestwintime = 0;
                    longestwintime = 0;
                    fewestwinningmoves = 0;
                    mostwinningmoves = 0;
                }
                
                
                boolean lastresult = arr.get(arr.size()-1).win();
                int currentstreak = 1;
                for(int i = arr.size()-2;i>=0;i--)
                {
                    if(arr.get(i).win() == lastresult)
                        currentstreak++;
                    else
                        break;
                }
                int longestwinstreak = 0;
                int longestlossstreak = 0;
                int streak = 0;
                for(int i =0;i<arr.size();i++)
                {
                    
                    if(arr.get(i).win())
                    {
                        streak++;
                    }
                    else
                    {
                        if(streak>longestwinstreak)
                        {
                            longestwinstreak = streak;
                            
                            
                        }
                        streak = 0;
                    }
                    
                }
                if(longestwinstreak ==0)
                {
                    longestwinstreak = streak;
                }
                if(lastresult && currentstreak > longestwinstreak)
                {
                    longestwinstreak = currentstreak;
                }
                streak = 0;
                for(int i =0;i<arr.size();i++)
                {
                    
                    if(!arr.get(i).win())
                    {
                        streak++;
                    }
                    else
                    {
                        if(streak>longestlossstreak)
                        {
                            longestlossstreak = streak;
                            
                            
                        }
                        streak = 0;
                    }
                   
                }  
                if(longestwinstreak ==0)
                {
                    longestlossstreak = streak;
                }
                if(!lastresult && currentstreak > longestlossstreak)
                {
                    longestlossstreak = currentstreak;
                }
            
                str = "Plays\nGames Played: " + gamesPlayed + "\nGames Won: " + gamesWon.size() + "\nGames Lost: " + losses + "\nPercent Won: " + percentWon + "%\n\n";
                str+= "Times\nTotal Time Played: " + displayTime(totalsecs)[0] + ":" + displayTime(totalsecs)[1] +":"+ displayTime(totalsecs)[2]; 
                if(displayTime((int)averagesecs)[0].equals("00"))
                    str+= "\nAverage Game Time: " + displayTime((int)averagesecs)[1] + ":" + displayTime((int)averagesecs)[2];
                else
                    str+= "\nAverage Game Time: " + displayTime((int)averagesecs)[0] + ":"+ displayTime((int)averagesecs)[1] + ":" + displayTime((int)averagesecs)[2];
                if(displayTime((int)averagewinsecs)[0].equals("00"))
                    str+= "\nAverage Win Time: " + displayTime((int)averagewinsecs)[1] + ":" + displayTime((int)averagewinsecs)[2];
                else
                    str+= "\nAverage Win Time: " + displayTime((int)averagewinsecs)[0] + ":"+ displayTime((int)averagewinsecs)[1] + ":" + displayTime((int)averagewinsecs)[2];
                if(displayTime(shortestwintime)[0].equals("00"))
                    str+= "\nShortest Win Time: " + displayTime(shortestwintime)[1] + ":" + displayTime(shortestwintime)[2];
                else
                    str+= "\nShortest Win Time: " + displayTime(shortestwintime)[0] + ":"+ displayTime(shortestwintime)[1] + ":" + displayTime(shortestwintime)[2];
                if(displayTime(longestwintime)[0].equals("00"))
                    str+= "\nLongest Win Time: " + displayTime(longestwintime)[1] + ":" + displayTime(longestwintime)[2];
                else
                    str+= "\nLongest Win Time: " + displayTime(longestwintime)[0] + ":"+ displayTime(longestwintime)[1] + ":" + displayTime(longestwintime)[2];
                str+="\n\nMoves\nFewest Winning Moves: " + fewestwinningmoves + "\nMost Winning Moves: " + mostwinningmoves;
                str+= "\n\nStreaks\nCurrent Streak: " + currentstreak;
                if(lastresult && currentstreak == 1)
                    str+= " win";
                else if(lastresult)
                    str+= " wins";
                else if(!lastresult && currentstreak ==1)
                    str+= " loss";
                else if(!lastresult)
                    str+= " losses";
                str+= "\nLongest Win Streak: " + longestwinstreak + "\nLongest Loss Streak: " + longestlossstreak;
                str+= "\n\nScores\nCurrent Running Score: " + totalscore + "\nAverage Game Score: " + averagescore;
                
                String[] options = {"OK","Reset Stats"};
                
                int s = JOptionPane.showOptionDialog(null,str,"Stats",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,icon,options,options[0]);
                if(s==1)
                {
                    String[] ops = {"Yes","No"};
                    int r = JOptionPane.showOptionDialog(null,"Are you sure you want to reset all stats?","Reset?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,ops,ops[0]); 
                    if(r==0)
                    {
                        PrintWriter writer = new PrintWriter("Game Logs.txt");
                        writer.print("");
                        writer.close();
                        JOptionPane.showMessageDialog(null,"Stats have been reset");
                    }
                }
            }
            else
            {
                 str = "No games have been played. Please check back after playing a game";
                 JOptionPane.showMessageDialog(null,str,"No Stats Available",JOptionPane.INFORMATION_MESSAGE,icon);
            }
            
            //JOptionPane.showMessageDialog(null,str,"Stats",JOptionPane.INFORMATION_MESSAGE,icon);
            
            //JOptionPane.showMessageDialog(null,str);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Something went terribly wrong with the game stats");
        }
    }
    /**
     * A helper method that takes in an amount of seconds and converts it into hours, minutes and seconds, returning the three values in a String[]
     * Values are converted to Strings in order to format the numbers to add 0s to the beginning of each number should they be <10
     * @return a String[] with the converted time
     */
    public String[] displayTime(int sec)
    {
        int min = 0;
        int hour = 0;
        String secs;
        String mins;
        String hours;
        while(sec>=60)
        {
            min++;
            sec-=60;
        }
        while(min>=60)
        {
            hour++;
            min-=60;
        }
        if(min<10)
        {
            mins = "0" + Integer.toString(min);
        }
        else
            mins = Integer.toString(min);
        if(sec<10)
        {
            secs = "0" + Integer.toString(sec);
        }
        else
            secs = Integer.toString(sec);
        if(hour<10)
        {
            hours = "0" + Integer.toString(hour);
        }
        else
            hours = Integer.toString(hour);
        String[] time = {hours,mins,secs};
        return time;
        
    }
        
        
    //     public void keyPressed(KeyEvent e)
    //     {
    //          if(e.getKeyCode() == KeyEvent.VK_M)
    //             System.out.println("M");
    //          
    //     }
    //     public void keyReleased(KeyEvent e)
    //     {
    //         if(e.getKeyCode() == KeyEvent.VK_M)
    //             System.out.println("M");
    //     }
    //      public void keyTyped(KeyEvent e)
    //     {
    //         if(e.getKeyCode() == KeyEvent.VK_M)
    //             System.out.println("M");
    //     }  
    /**
     * Deals the cards to start the game from the top of the deck, with one up card in each column and an increasing number of down cards as the col number increases. 
     * Deals in the order that an actual solitaire game would be dealt
     */
    public void deal()
    {
        //First time through
        col1.push(deck.removeTopCard());
        col2Down.push(deck.removeTopCard());
        col3Down.push(deck.removeTopCard());
        col4Down.push(deck.removeTopCard());
        col5Down.push(deck.removeTopCard());
        col6Down.push(deck.removeTopCard());
        col7Down.push(deck.removeTopCard());
        //2nd time through
        col2.push(deck.removeTopCard());
        col3Down.push(deck.removeTopCard());
        col4Down.push(deck.removeTopCard());
        col5Down.push(deck.removeTopCard());
        col6Down.push(deck.removeTopCard());
        col7Down.push(deck.removeTopCard());
        //3rd time through
        col3.push(deck.removeTopCard());
        col4Down.push(deck.removeTopCard());
        col5Down.push(deck.removeTopCard());
        col6Down.push(deck.removeTopCard());
        col7Down.push(deck.removeTopCard());
        //4th time through
        col4.push(deck.removeTopCard());
        col5Down.push(deck.removeTopCard());
        col6Down.push(deck.removeTopCard());
        col7Down.push(deck.removeTopCard());
        //5th time through
        col5.push(deck.removeTopCard());
        col6Down.push(deck.removeTopCard());
        col7Down.push(deck.removeTopCard());
        //6th time through
        col6.push(deck.removeTopCard());
        col7Down.push(deck.removeTopCard());
        //Final time through
        col7.push(deck.removeTopCard());
        /*
        deck.addCard(new Card(13,"Clubs"));
        deck.addCard(new Card(12,"Hearts"));
        deck.addCard(new Card(11,"Clubs"));
        
        */
    }

    /**
     * Checks to see if one card can be put on top of another (not including putting cards into the top columns)
     * @param s The Stack with the card to put on s2
     * @param s2 The Stack that s will be put on
     * @return whether or not the top card of a stack can be put on top of another stack
     */
    public boolean canPutOnTop(Stack<Card> s, Stack<Card> s2)
    {
        Stack<Card> temp = new Stack<Card>();
        if(s2.equals(col1))
            temp = col1Down;
        else if(s2.equals(col2))
            temp = col2Down;
        else if(s2.equals(col3))
            temp = col3Down;
        else if(s2.equals(col4))
            temp = col4Down;
        else if(s2.equals(col5))
            temp = col5Down;
        else if(s2.equals(col6))
            temp = col6Down;
        else if(s2.equals(col7))
            temp = col7Down;    
        
            
        if(s.peek().getValue()==13 && s2.size()==0 && temp.size()==0)
            return true;
        else if(s.size()==0||s2.size()==0)
            return false;
        else if((s.peek().getSuit().equals("Spades") || s.peek().getSuit().equals("Clubs")) && (s2.peek().getSuit().equals("Hearts")||s2.peek().getSuit().equals("Diamonds")) 
        && s.peek().getValue()+1 == s2.peek().getValue())
        {
            return true;
        }
        else if((s.peek().getSuit().equals("Hearts") || s.peek().getSuit().equals("Diamonds")) && (s2.peek().getSuit().equals("Spades")||s2.peek().getSuit().equals("Clubs")) 
        && s.peek().getValue()+1 == s2.peek().getValue())
        {
            return true;
        }
        else
            return false;
    }
    /**
     * Checks to see if a given number of cards can be put on top of a stack (not including putting cards into the top columns)
     * @param s The Stack with the cards to be put onto s2
     * @param s2 The Stack that the cards will be put on
     * @param numCards The number of cards removed from the top card that are being moved
     * @return whether or not the given number of cards from a given stack can be put on top of another stack
     */
    public boolean canPutOnTop(Stack<Card> s, Stack<Card> s2,int numCards)
    {
        Stack<Card> temp = new Stack<Card>();
        Card c = s.get(s.size()-1-numCards);
        if(s2.equals(col1))
            temp = col1Down;
        else if(s2.equals(col2))
            temp = col2Down;
        else if(s2.equals(col3))
            temp = col3Down;
        else if(s2.equals(col4))
            temp = col4Down;
        else if(s2.equals(col5))
            temp = col5Down;
        else if(s2.equals(col6))
            temp = col6Down;
        else if(s2.equals(col7))
            temp = col7Down;    
        
            
        if(c.getValue()==13 && s2.size()==0 && temp.size()==0)
            return true;
        else if(s.size()==0||s2.size()==0)
            return false;
        else if((c.getSuit().equals("Spades") || c.getSuit().equals("Clubs")) && (s2.peek().getSuit().equals("Hearts")||s2.peek().getSuit().equals("Diamonds")) 
        && c.getValue()+1 == s2.peek().getValue())
        {
            return true;
        }
        else if((c.getSuit().equals("Hearts") || c.getSuit().equals("Diamonds")) && (s2.peek().getSuit().equals("Spades")||s2.peek().getSuit().equals("Clubs")) 
        && c.getValue()+1 == s2.peek().getValue())
        {
            return true;
        }
        else
            return false;
    }
    /**
     * Checks to see if the top card of a stack can be placed onto one of the upper stacks/columns
     * @param s The Stack containing the card to be put on the top card of s2
     * @param s2 The Stack (which will be one of the 4 top stacks) containing the cards that s will be put on top of
     * @return whether or not the top card of the Stack can be put onto an upper column
     */
    public boolean canPutUp(Stack<Card> s, Stack<Card> s2)
    {
        if(numExtraCards != 0)
            return false;
        if(s2.size()==0)
        {
            if(s.peek().getValue() ==1)
            {
                if(s.peek().getSuit().equalsIgnoreCase("Spades")&&s2==spadesPile)
                    return true;
                else if(s.peek().getSuit().equalsIgnoreCase("Hearts")&&s2==heartsPile)
                    return true;
                else if(s.peek().getSuit().equalsIgnoreCase("Clubs")&&s2==clubsPile)
                    return true;
                else if(s.peek().getSuit().equalsIgnoreCase("Diamonds")&&s2==diamondsPile)
                    return true;
                else
                    return false;
            }
            else
                return false;
        }
        else if(s.peek().getSuit().equals(s2.peek().getSuit()) && s.peek().getValue()-1 == s2.peek().getValue())
            return true;
        else
            return false;
    }
    
    /**
     * This method deals with updating the drawing of the game board after something happens
     * @param g 2D Graphics used to draw the game
     */
    public void updateScreen(Graphics g)
    {
        //deck image
        if(deck.getSize()>0)
            g.drawImage(cardBack,deckLoc.getA(),deckLoc.getB(),null);
        //card pile image
        if(cardPile.size()>0)//here 
        {
            if(deal3)
            {
                //3 is standard
                
                    for(int i = cardsTurned-1;i>=0 && cardPile.size()-1-i>=0;i--)
                    {
                        Location loc = new Location(cardPileLoc.getA()+15*(cardsTurned-1-i),cardPileLoc.getB());
                        //debugging
                        if(cardPile.size()-1-i <0)
                        {
                            System.out.println(cardPile.size());
                            System.out.println(i);
                            System.out.println(cardsTurned);
                        }
                        Card c = cardPile.get(cardPile.size()-1-i);
                        
                        g.drawImage(c.getImage(),loc.getA(),loc.getB(),null);
                    }
                
                
               
                
            }
            else
                g.drawImage(cardPile.peek().getImage(),cardPileLoc.getA(),cardPileLoc.getB(),null);
        }
        
        if(spadesPile.size()>0)
        {
            g.drawImage(spadesPile.peek().getImage(),spadesPileLoc.getA(),spadesPileLoc.getB(),null);
        }
        else
        {
            //g.setColor(Color.BLACK);
            //g.drawString("Spades", spadesPileLoc.getA()+15,spadesPileLoc.getB()+94/2);
            g.drawImage(spadesImage, spadesPileLoc.getA()+35-9,spadesPileLoc.getB()+47-12,null);
            //70x94, corner at (315,20), center at (315+35,20+47), spade 25x29
             
        }
        
        if(heartsPile.size()>0)
        {
            g.drawImage(heartsPile.peek().getImage(),heartsPileLoc.getA(),heartsPileLoc.getB(),null);
        }
        else
        {
            g.setColor(Color.BLACK);
            //g.drawString("Hearts", heartsPileLoc.getA()+18,heartsPileLoc.getB()+94/2);
            g.drawImage(heartsImage,heartsPileLoc.getA()+35-10,heartsPileLoc.getB()+47-11,null);
        }
        
        if(clubsPile.size()>0)
        {
            g.drawImage(clubsPile.peek().getImage(),clubsPileLoc.getA(),clubsPileLoc.getB(),null);
        }
        else
        {
            g.setColor(Color.BLACK);
            //g.drawString("Clubs", clubsPileLoc.getA()+19,clubsPileLoc.getB()+94/2);
            g.drawImage(clubsImage,clubsPileLoc.getA()+35-15,clubsPileLoc.getB()+47-15,null);
        }
        
        if(diamondsPile.size()>0)
        {
            g.drawImage(diamondsPile.peek().getImage(),diamondsPileLoc.getA(),diamondsPileLoc.getB(),null);
        }
        else
        {
            g.setColor(Color.BLACK);
            //g.drawString("Diamonds", diamondsPileLoc.getA()+8,diamondsPileLoc.getB()+94/2);
            g.drawImage(diamondsImage, diamondsPileLoc.getA()+35-12,diamondsPileLoc.getB()+47-15,null);
        }
        //draws all down cards
        //         for(int i=0;i<getAllDownCols().size();i++) //traversing through the 7 columns of down cards
        //         {
        //             for(int j = 0;j<getAllDownCols().get(i).size();j++) //traversing through the stack of cards of each column
        //             {
        //                 g.drawImage(cardBack,allColLocs.get(i).getA(),allColLocs.get(i).getB()+8*j,null);
        //             }
        //         }
        
        //updated syntax: draws all 7 seperately
        //col1
        for(int i = 0; i<col1Down.size();i++)
        {
             g.drawImage(cardBack,30,154+8*i,null);
        }
        for(int i = 0; i<col2Down.size();i++)
        {
             g.drawImage(cardBack,125,154+8*i,null);
        }
        for(int i = 0; i<col3Down.size();i++)
        {
             g.drawImage(cardBack,220,154+8*i,null);
        }
        for(int i = 0; i<col4Down.size();i++)
        {
             g.drawImage(cardBack,315,154+8*i,null);
        }
        for(int i = 0; i<col5Down.size();i++)
        {
             g.drawImage(cardBack,410,154+8*i,null);
        }
        for(int i = 0; i<col6Down.size();i++)
        {
             g.drawImage(cardBack,505,154+8*i,null);
        }
        for(int i = 0; i<col7Down.size();i++)
        {
             g.drawImage(cardBack,600,154+8*i,null);
        }
        
        //new algorithm: manually drawing each of the 7 cols up card
        ArrayList<Card> temp = new ArrayList<Card>();
        //col2.add(new Card(6,"Spades"));
       
        for(Card c:col1)
        {
            temp.add(c);
        }
        
        for(int i = 0;i<temp.size();i++)
        {
            g.drawImage(temp.get(i).getImage(),getStartingUpCardLoc(0,col1Down.size()).getA(),getStartingUpCardLoc(0,col1Down.size()).getB() + 17*i,null);
        }
        temp.clear();
        //Collections.reverse(temp);
        //         while(temp.size()>0)
        //         {
            //             col1.push(temp.remove(0));
        //         }
        //col2
        for(Card c:col2)
        {
            temp.add(c);
        }
        for(int i = 0;i<temp.size();i++)
        {
            g.drawImage(temp.get(i).getImage(),getStartingUpCardLoc(1,col2Down.size()).getA(),getStartingUpCardLoc(1,col2Down.size()).getB() + 17*i,null);
        }
        temp.clear();
        //Collections.reverse(temp);
        
        //col3
        for(Card c:col3)
        {
            temp.add(c);
        }
        for(int i = 0;i<temp.size();i++)
        {
            g.drawImage(temp.get(i).getImage(),getStartingUpCardLoc(2,col3Down.size()).getA(),getStartingUpCardLoc(2,col3Down.size()).getB() + 17*i,null);
        }
        temp.clear();
        
        //col4
        for(Card c:col4)
        {
            temp.add(c);
        }
        for(int i = 0;i<temp.size();i++)
        {
            g.drawImage(temp.get(i).getImage(),getStartingUpCardLoc(3,col4Down.size()).getA(),getStartingUpCardLoc(3,col4Down.size()).getB() + 17*i,null);
        }
        temp.clear();
        //col5
        for(Card c:col5)
        {
            temp.add(c);
        }
        for(int i = 0;i<temp.size();i++)
        {
            g.drawImage(temp.get(i).getImage(),getStartingUpCardLoc(4,col5Down.size()).getA(),getStartingUpCardLoc(4,col5Down.size()).getB() + 17*i,null);
        }
        temp.clear();
        //col6
        for(Card c:col6)
        {
            temp.add(c);
        }
        for(int i = 0;i<temp.size();i++)
        {
            g.drawImage(temp.get(i).getImage(),getStartingUpCardLoc(5,col6Down.size()).getA(),getStartingUpCardLoc(5,col6Down.size()).getB() + 17*i,null);
        }
        temp.clear();
        //col7
        for(Card c:col7)
        {
            temp.add(c);
        }
        for(int i = 0;i<temp.size();i++)
        {
            g.drawImage(temp.get(i).getImage(),getStartingUpCardLoc(6,col7Down.size()).getA(),getStartingUpCardLoc(6,col7Down.size()).getB() + 17*i,null);
        }
        temp.clear();
        
        if(pileClicked)
        {
            g.setColor(Color.YELLOW);
            if(deal3)
            {
                g.drawRect(125+15*(cardsTurned-1),20,70,94);
                g.drawRect(124+15*(cardsTurned-1),19,72,96);
            }
            else
            {
                g.drawRect(125,20,70,94);
                g.drawRect(124,19,72,96);
            }
        }
        else if(spadesPileClicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(315,20,70,94);
            g.drawRect(314,19,72,96);
        }
        else if(heartsPileClicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(410,20,70,94);
            g.drawRect(409,19,72,96);
        }
        else if(clubsPileClicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(505,20,70,94);
            g.drawRect(504,19,72,96);
        }
        else if(diamondsPileClicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(600,20,70,94);
            g.drawRect(599,19,72,96);
        }
        
        if(col1Clicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(getEndingUpCardLoc(0).getA(),getEndingUpCardLoc(0).getB()-(17*numExtraCards),70,94+(17*numExtraCards));
            g.drawRect(getEndingUpCardLoc(0).getA()-1,getEndingUpCardLoc(0).getB()-1 - (17*numExtraCards),72,96 + (17*numExtraCards));
        }
        else if(col2Clicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(getEndingUpCardLoc(1).getA(),getEndingUpCardLoc(1).getB()-(17*numExtraCards),70,94+(17*numExtraCards));
            g.drawRect(getEndingUpCardLoc(1).getA()-1,getEndingUpCardLoc(1).getB()-1 - (17*numExtraCards),72,96 + (17*numExtraCards));
        }
        else if(col3Clicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(getEndingUpCardLoc(2).getA(),getEndingUpCardLoc(2).getB()-(17*numExtraCards),70,94+(17*numExtraCards));
            g.drawRect(getEndingUpCardLoc(2).getA()-1,getEndingUpCardLoc(2).getB()-1 - (17*numExtraCards),72,96 + (17*numExtraCards));
        }
        else if(col4Clicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(getEndingUpCardLoc(3).getA(),getEndingUpCardLoc(3).getB()-(17*numExtraCards),70,94+(17*numExtraCards));
            g.drawRect(getEndingUpCardLoc(3).getA()-1,getEndingUpCardLoc(3).getB()-1 - (17*numExtraCards),72,96 + (17*numExtraCards));
        }
        else if(col5Clicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(getEndingUpCardLoc(4).getA(),getEndingUpCardLoc(4).getB()-(17*numExtraCards),70,94+(17*numExtraCards));
            g.drawRect(getEndingUpCardLoc(4).getA()-1,getEndingUpCardLoc(4).getB()-1 - (17*numExtraCards),72,96 + (17*numExtraCards));
        }
        else if(col6Clicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(getEndingUpCardLoc(5).getA(),getEndingUpCardLoc(5).getB()-(17*numExtraCards),70,94+(17*numExtraCards));
            g.drawRect(getEndingUpCardLoc(5).getA()-1,getEndingUpCardLoc(5).getB()-1 - (17*numExtraCards),72,96 + (17*numExtraCards));
        }
        else if(col7Clicked)
        {
            g.setColor(Color.YELLOW);
            g.drawRect(getEndingUpCardLoc(6).getA(),getEndingUpCardLoc(6).getB()-(17*numExtraCards),70,94+(17*numExtraCards));
            g.drawRect(getEndingUpCardLoc(6).getA()-1,getEndingUpCardLoc(6).getB()-1 - (17*numExtraCards),72,96 + (17*numExtraCards));
        }
        g.setColor(Color.BLACK);
        //g.setFont(new Font("Times New Roman",Font.PLAIN, 15));
        int minutes;
        int seconds;
        minutes=0;
        seconds = elapsedTime();
        while(seconds>=60)
        {
            seconds -=60;
            minutes +=1;
        }
        String min = Integer.toString(minutes);
        String sec = Integer.toString(seconds);
        if(minutes<10)
            min = 0+min;
        if(seconds<10)
            sec = 0+sec;
        g.drawString("Stock: " + deck.getSize(),310,520);
        g.drawString("Time: " +min+":"+sec,370,520);
        g.drawString("Moves: " + count,460,520);
        g.drawString("Score: " + score,240,520);
        /*
         * Test: should draw 8 of spades, 7 of hearts, 6 of clubs in the 1st, 2nd and 3rd rows, respectively
        col1.push(new Card(8,"Spades"));
        col2.push(new Card(7,"Hearts"));
        col3.push(new Card(6,"Clubs"));
         */
        // //         //OLD ALGORITHM: draws all up cards
        // //         for(int i = 0;i<getAllUpCols().size();i++)
        // //         //traversing through the 7 columns of up cards
        // //         {
        // //             
        // //             //Collections.copy(getAllUpCols().get(i),temp);
        // //             //int size = getAllUpCols().get(i).size();
        // //             //Stack<Card> t = getAllUpCols().get(i);
        // //             //int count = 0;
        // //             // int size = getAllUpCols().get(i).size();
        // //             for(int j = 0; j<getAllUpCols().get(i).size();j++)
        // //             //traversing through the stacks of cards of each column. get(i) returns a stack<Card>, so get(i).size() will return how many cards are in the given up card column (should be 1 to start the game)
        // //             {
        // //                 //Stack<Card> temp = new Stack<Card>();
        // //                 ArrayList<Card> temp = new ArrayList<Card>(getAllUpCols().get(i));
        // //                 //temp = getAllUpCols().get(i);
        // //                 //Collections.copy(getAllUpCols().get(i),temp);
        // //                 //Collections.reverse(temp);
        // //                 int a = getStartingUpCardLoc(i,getAllDownCols().get(i).size()).getA();
        // //                 int b = getStartingUpCardLoc(i,getAllDownCols().get(i).size()).getB() + 17*j;
        // //                 g.drawImage(temp.get(j).getImage(),a,b,null);
        // //                 // size = getAllUpCols().get(i).size();
        // //                
        // //                 // temp.push(getAllUpCols().get(i).pop());
        // //             }
        // //             
        // //         }
        // //         //getAllUpCols();
    }
    /**
     * Returns the number of seconds that have elapsed since the first move of the game
     * @return the number of seconds that have elapsed since the first move of the game
     */
    public int elapsedTime()
    {
        long now = System.currentTimeMillis();
        if(timer == 0)
            return 0;     
        else
            return (int)((now - timer) /1000.0);
    }
    /**
     * Gets the Location of the upper-left corner of the first card of any given column
     * @param columnNum 0-6, the column number INDEX (for col1, enter 0...)
     * @param numberofDownCards the number of down cards in the column
     * @return the Location of the first up card of the given column
     */
    public Location getStartingUpCardLoc(int columnNum,int numberOfDownCards)
    {
        //Location loc = allColLocs.get(columnNum);
        Location loc = new Location();
        int upCards = 0;
        if(columnNum ==0)
        {
            loc = new Location(30,154);
            upCards = col1.size();
        }
        else if(columnNum ==1)
        {
            loc = new Location(125,154);
            upCards = col2.size();
        }
        else if(columnNum ==2)
        {
            loc = new Location(220,154);
            upCards = col3.size();
        }
        else if(columnNum ==3)
        {
            loc = new Location(315,154);
            upCards = col4.size();
        }
        else if(columnNum ==4)
        {
            loc = new Location(410,154);
            upCards = col5.size();
        }
        else if(columnNum ==5)
        {
            loc = new Location(505,154);
            upCards = col6.size();
        }
        else if(columnNum ==6)
        {
            loc = new Location(600,154);
            upCards = col7.size();
        }
        
        loc.setB(154 + (8*numberOfDownCards));
        return loc;
    }
    /**
     * Gets the Location of the upper-left corner of the upper most card in the stack of cards of any given column
     * @param columnNum 0-6, the column number INDEX
     * @return the Location of the upper-left corner of the upper most card in the stack of cards
     */
    public Location getEndingUpCardLoc(int columnNum)
    {
        Location loc = allColLocs.get(columnNum);
        int upCards = 0;
        int downCards = 0;
        if(columnNum==0)
        {
            upCards = col1.size();
            downCards = col1Down.size();
            
        }
        else if(columnNum==1)
        {
            upCards = col2.size();
            downCards = col2Down.size();
        }
        else if(columnNum==2)
        {
            upCards = col3.size();
            downCards = col3Down.size();
        }
        else if(columnNum==3)
        {
            upCards = col4.size();
            downCards = col4Down.size();
        }
        else if(columnNum==4)
        {
            upCards = col5.size();
            downCards = col5Down.size();
        }
        else if(columnNum==5)
        {
            upCards = col6.size();
            downCards = col6Down.size();
        }
        else if(columnNum==6)
        {
            upCards = col7.size();
            downCards = col7Down.size();
        }
        if(upCards!=0)
            loc.setB((154+(8*downCards)+(17*(upCards-1))));
        else if(downCards!=0)
            loc.setB(154 + 8*(downCards-1));
        else
            loc.setB(154);
        return loc;
    }
    /**
     * Draws the layout for the Solitaire game board, including each column outline and the circle behind the deck
     * @param g 2D Graphics used to graw the layout
     */
    public void drawLayout(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillOval(65-20,67-20,40,40);
        g.setColor(background);
        g.fillOval(65-15,67-15,30,30);
        g.setColor(Color.BLACK);
        //         //6 top card piles
                 g.drawRoundRect(30,20,70,94,20,20);
        //         g.drawString("0",64,15);
        //         
                 g.drawRoundRect(125,20,70,94,20,20);
        //          g.drawString("1",125+35,15);

        //          g.drawString("1",125+35,15);
        //         //spades
                 g.drawRoundRect(315,20,70,94,20,20); //70x94, corner at (315,20), center at (315+35,20+47), spade 25x29
             
        //         g.drawString("2",315+35,15);
        //         //hearts
                 g.drawRoundRect(410,20,70,94,20,20);
        //         g.drawString("3",410+35,15);
        //         //clubs
                 g.drawRoundRect(505,20,70,94,20,20);
        //         g.drawString("4",505+35,15);
        //         //diamonds
                 g.drawRoundRect(600,20,70,94,20,20);
        //         g.drawString("5",600+35,15);
        // 
        //         //7 lower card piles outline
                 g.drawRoundRect(30,154,70,94,20,20);
        //         g.drawString("6",30+35,149);
        //         
                 g.drawRoundRect(125,154,70,94,20,20);
        //         g.drawString("7",125+35,149);
        //         
                 g.drawRoundRect(220,154,70,94,20,20);
        //         g.drawString("8",220+35,149);
        //         
                 g.drawRoundRect(315,154,70,94,20,20);
        //         g.drawString("9",315+35,149);
        //         
                 g.drawRoundRect(410,154,70,94,20,20);
        //         g.drawString("10",410+30,149);
        //         
                 g.drawRoundRect(505,154,70,94,20,20);
        //         g.drawString("11",505+30,149);
        //         
                 g.drawRoundRect(600,154,70,94,20,20);
        //         g.drawString("12",600+30,149);
        
    }
    /**
     * Set each of the 7 lower columns as well as each of the upper 4 columns to false
     */
    public void setColsFalse()
    {
        col1Clicked = false;
        col2Clicked = false;
        col3Clicked = false;
        col4Clicked = false;
        col5Clicked = false;
        col6Clicked = false;
        col7Clicked = false;
        spadesPileClicked   = false;
        heartsPileClicked   = false;
        clubsPileClicked    = false;
        diamondsPileClicked = false;
    }
    /**
     * Sets one of the 7 lower columns to true, and the rest to false (unless that column has no up OR down cards), unless that column has no up or down cards, in which case that column can never be clicked
     * @param colNum The INDEX of the column that will be set to true (col1 = 0...)
     */
    public void setColTrue(int colNum)
    {
        col1Clicked = false;
        col2Clicked = false;
        col3Clicked = false;
        col4Clicked = false;
        col5Clicked = false;
        col6Clicked = false;
        col7Clicked = false;
        if(colNum ==0)
        {
            if(!(col1.size()==0&&col1Down.size()==0))
                col1Clicked = true;
        }
        else if(colNum==1)
        {
            if(!(col2.size()==0&&col2Down.size()==0))
                col2Clicked = true;
        }
        else if(colNum==2)
        {
            if(!(col3.size()==0&&col3Down.size()==0))
                col3Clicked = true;
        }
        else if(colNum==3)
        {
            if(!(col4.size()==0&&col4Down.size()==0))
                col4Clicked = true;
        }
        else if(colNum==4)
        {
            if(!(col5.size()==0&&col5Down.size()==0))
                col5Clicked = true;
        }
        else if(colNum==5)
        {
            if(!(col6.size()==0&&col6Down.size()==0))
                col6Clicked = true;
        }
        else if(colNum==6)
        {
            if(!(col7.size()==0&&col7Down.size()==0))
                col7Clicked = true;
        }
    }
    /**
     * Handles all of the painting of the screen, filling the background with a nice green color and then calling each of the drawing methods.
     */
    public void paint(Graphics g)
    {
        super.paintComponent(g);    //Prevents bugs in the top left corner
        g.setColor(background);
        //g.setColor(new Color(150,0,175));
        g.fillRect(0,0,WIDTH,HEIGHT);    
        drawLayout(g);
        updateScreen(g);
    }
    /**
     * Checks to see if the deck has been clicked using the x and y values from the mouse adapter
     * @return whether or not the deck has been clicked
     */
    public boolean deckClicked()
    {
        if(x>=30 && x <= 100 & y>=20 & y<=114)
            return true;
        return false;
    }
    /**
     * Checks to see if cardPile has been clicked using the x and y values from the mouse adapter
     * @return whether or not cardPile has been clicked
     */
    public boolean cardPileClicked()
    {
        if(deal3)
        {
            if(x>=125+15*(cardsTurned-1) && x<=195+15*(cardsTurned-1) && y>=20 && y<=114)
                return true;
            return false;
        }
        else
        {
            if(x>=125 && x<=195 && y>=20 && y<=114)
                return true;
            return false;
        }
    }
    /**
     * Checks to see if spadesPile has been clicked using the x and y values from the mouse adapter
     * @return whether or not spadesPile has been clicked
     */
    public boolean spadesPileClicked()
    {
        if(x>=315 && x<=385 && y>=20 && y<=114)
            return true;
        return false;
    }
    /**
     * Checks to see if heartsPile has been clicked using the x and y values from the mouse adapter
     * @return whether or not heartsPile has been clicked
     */
    public boolean heartsPileClicked()
    {
        if(x>=410 && x<=480 && y>=20 && y<=114)
            return true;
        return false;
    }
    /**
     * Checks to see if clubsPile has been clicked using the x and y values from the mouse adapter
     * @return whether or not clubsPile has been clicked
     */
    public boolean clubsPileClicked()
    {
        if(x>=505 && x<=575 && y>=20 && y<=114)
            return true;
        return false;
    }
    /**
     * Checks to see if diamondssPile has been clicked using the x and y values from the mouse adapter
     * @return whether or not diamondsPile has been clicked
     */
    public boolean diamondsPileClicked()
    {
        if(x>=600 && x<=670 && y>=20 && y<=114)
            return true;
        return false;
    }
    
    /**
     * Checks to see if col1 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * @return whether or not col1 has been clicked
     */
    public boolean col1Clicked()
    {
        //&& !(col1.size()==0 && col1Down.size()==0)
        int a = getEndingUpCardLoc(0).getA();
        int b = getEndingUpCardLoc(0).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            return true;
           
        }
        for(int i = 1; i<col1.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks to see if col2 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * @return whether or not col2 has been clicked
     */
    public boolean col2Clicked()
    {
        int a = getEndingUpCardLoc(1).getA();
        int b = getEndingUpCardLoc(1).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            return true;
           
        }
        for(int i = 1; i<col2.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                return true;
            }
        }
        return false;
    }
    /**
     * Checks to see if col3 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * @return whether or not col3 has been clicked
     */
    public boolean col3Clicked()
    {
        int a = getEndingUpCardLoc(2).getA();
        int b = getEndingUpCardLoc(2).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            return true;
           
        }
        
        for(int i = 1; i<col3.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                return true;
            }
        }
        return false;
    }
    /**
     * Checks to see if col4 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * @return whether or not col4 has been clicked
     */
    public boolean col4Clicked()
    {
        int a = getEndingUpCardLoc(3).getA();
        int b = getEndingUpCardLoc(3).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            return true;
           
        }
        for(int i = 1; i<col4.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                return true;
            }
        }
        return false;
    }
    /**
     * Checks to see if col5 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * @return whether or not col5 has been clicked
     */
    public boolean col5Clicked()
    {
        int a = getEndingUpCardLoc(4).getA();
        int b = getEndingUpCardLoc(4).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            return true;
           
        }
        for(int i = 1; i<col5.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                return true;
            }
        }
        return false;
    }
    /**
     * Checks to see if col6 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * @return whether or not col6 has been clicked
     */
    public boolean col6Clicked()
    {
        int a = getEndingUpCardLoc(5).getA();
        int b = getEndingUpCardLoc(5).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            return true;
           
        }
        for(int i = 1; i<col6.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                return true;
            }
        }
        return false;
    }
    /**
     * Checks to see if col7 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * @return whether or not col7 has been clicked
     */
    public boolean col7Clicked()
    {
        int a = getEndingUpCardLoc(6).getA();
        int b = getEndingUpCardLoc(6).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            return true;
           
        }
        for(int i = 1; i<col7.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                return true;
            }
        }
        return false;
    }
    /**
     * Checks to see if col1 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * Also checks to see if the card in this column is the same card that had previously been clicked, mutating sameCard appropriately
     * This method will only be called when checked whether or not the same column has been clicked twice in a row
     * @param num The how many cards removed from the top card had previously been clicked
     * @return whether or not col1 has been clicked
     */
    public boolean col1Clicked(int num)
    {
        //&& !(col1.size()==0 && col1Down.size()==0)
        if(col1Clicked()==false)
            return false;
        boolean boo = false;
        int a = getEndingUpCardLoc(0).getA();
        int b = getEndingUpCardLoc(0).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            boo = true;
           
        }
        for(int i = 1; i<col1.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                boo = true;
            }
        }
        if(num == numExtraCards)
            sameCard = true;
        else
            sameCard = false;
        return boo;
    }
    /**
     * Checks to see if col2 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * Also checks to see if the card in this column is the same card that had previously been clicked, mutating sameCard appropriately
     * This method will only be called when checked whether or not the same column has been clicked twice in a row
     * @param num The how many cards removed from the top card had previously been clicked
     * @return whether or not col2 has been clicked
     */
    public boolean col2Clicked(int num)
    {
        //&& !(col1.size()==0 && col1Down.size()==0)
        if(col2Clicked()==false)
            return false;
        boolean boo = false;
        int a = getEndingUpCardLoc(1).getA();
        int b = getEndingUpCardLoc(1).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            boo = true;
           
        }
        for(int i = 1; i<col2.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                boo = true;
            }
        }
        if(num == numExtraCards)
            sameCard = true;
        else
            sameCard = false;
        return boo;
    }
    /**
     * Checks to see if col3 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * Also checks to see if the card in this column is the same card that had previously been clicked, mutating sameCard appropriately
     * This method will only be called when checked whether or not the same column has been clicked twice in a row
     * @param num The how many cards removed from the top card had previously been clicked
     * @return whether or not col3 has been clicked
     */
    public boolean col3Clicked(int num)
    {
        //&& !(col1.size()==0 && col1Down.size()==0)
        if(col3Clicked()==false)
            return false;
        boolean boo = false;
        int a = getEndingUpCardLoc(2).getA();
        int b = getEndingUpCardLoc(2).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            boo = true;
           
        }
        for(int i = 1; i<col3.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                boo = true;
            }
        }
        if(num == numExtraCards)
            sameCard = true;
        else
            sameCard = false;
        return boo;
    }
    /**
     * Checks to see if col4 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * Also checks to see if the card in this column is the same card that had previously been clicked, mutating sameCard appropriately
     * This method will only be called when checked whether or not the same column has been clicked twice in a row
     * @param num The how many cards removed from the top card had previously been clicked
     * @return whether or not col4 has been clicked
     */
    public boolean col4Clicked(int num)
    {
        //&& !(col1.size()==0 && col1Down.size()==0)
        if(col4Clicked()==false)
            return false;
        boolean boo = false;
        int a = getEndingUpCardLoc(3).getA();
        int b = getEndingUpCardLoc(3).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            boo = true;
           
        }
        for(int i = 1; i<col4.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                boo = true;
            }
        }
        if(num == numExtraCards)
            sameCard = true;
        else
            sameCard = false;
        return boo;
    }
    /**
     * Checks to see if col5 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * Also checks to see if the card in this column is the same card that had previously been clicked, mutating sameCard appropriately
     * This method will only be called when checked whether or not the same column has been clicked twice in a row
     * @param num The how many cards removed from the top card had previously been clicked
     * @return whether or not col5 has been clicked
     */
    public boolean col5Clicked(int num)
    {
        //&& !(col1.size()==0 && col1Down.size()==0)
        if(col5Clicked()==false)
            return false;
        boolean boo = false;
        int a = getEndingUpCardLoc(4).getA();
        int b = getEndingUpCardLoc(4).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            boo = true;
           
        }
        for(int i = 1; i<col5.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                boo = true;
            }
        }
        if(num == numExtraCards)
            sameCard = true;
        else
            sameCard = false;
        return boo;
    }
    /**
     * Checks to see if col6 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * Also checks to see if the card in this column is the same card that had previously been clicked, mutating sameCard appropriately
     * This method will only be called when checked whether or not the same column has been clicked twice in a row
     * @param num The how many cards removed from the top card had previously been clicked
     * @return whether or not col6 has been clicked
     */
    public boolean col6Clicked(int num)
    {
        //&& !(col1.size()==0 && col1Down.size()==0)
        if(col6Clicked()==false)
            return false;
        boolean boo = false;
        int a = getEndingUpCardLoc(5).getA();
        int b = getEndingUpCardLoc(5).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            boo = true;
           
        }
        for(int i = 1; i<col6.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                boo = true;
            }
        }
        if(num == numExtraCards)
            sameCard = true;
        else
            sameCard = false;
        return boo;
    }
    /**
     * Checks to see if col7 has been clicked using the x and y values from the mouse adapter
     * Also checks which card has been clicked within the column, mutating numExtraCards appropriately
     * Also checks to see if the card in this column is the same card that had previously been clicked, mutating sameCard appropriately
     * This method will only be called when checked whether or not the same column has been clicked twice in a row
     * @param num The how many cards removed from the top card had previously been clicked
     * @return whether or not col7 has been clicked
     */
    public boolean col7Clicked(int num)
    {
        //&& !(col1.size()==0 && col1Down.size()==0)
        if(col7Clicked()==false)
            return false;
        boolean boo = false;
        int a = getEndingUpCardLoc(6).getA();
        int b = getEndingUpCardLoc(6).getB();
        if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
        {
            numExtraCards = 0;
            boo = true;
           
        }
        for(int i = 1; i<col7.size();i++)
        {
            b-=17;
            if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
            {
                numExtraCards = i;
                //System.out.println("Moving " + i + 1 + " cards");
                boo = true;
            }
        }
        if(num == numExtraCards)
            sameCard = true;
        else
            sameCard = false;
        return boo;
    }
    /**
     * Checks to see if all 7 of the lower columns and all 4 of the upper columns are false
     * @return whether or not all 12 columns are false
     */
    public boolean allFalse()
    {
        if(col1Clicked||col2Clicked||col3Clicked||col4Clicked||col5Clicked||col6Clicked||col7Clicked||spadesPileClicked||heartsPileClicked||clubsPileClicked||diamondsPileClicked)
        {
            return false;
        }
        return true;
    }
    /**
     * Checks if the user has won the game
     * @return whether or not the user has won the game
     */
    public boolean win()
    {
        if(spadesPile.size()==0 ||heartsPile.size()==0||clubsPile.size()==0||diamondsPile.size()==0)
            return false;
        else if(spadesPile.peek().getValue()==13 &&
                heartsPile.peek().getValue()==13 &&
                clubsPile.peek().getValue()==13  &&
                diamondsPile.peek().getValue()==13)
            return true;
        else
            return false;
    }
    /**
     * Returns an ArrayList of each of the 7 stacks of up cards used throughout the game
     * @return an ArrayList of each of the 7 stacks of up cards used throughout the game
     */
    public ArrayList<Stack<Card>> getArr()
    {
        ArrayList<Stack<Card>> arr = new ArrayList<Stack<Card>>();
        arr.add(col1);
        arr.add(col2);
        arr.add(col3);
        arr.add(col4);
        arr.add(col5);
        arr.add(col6);
        arr.add(col7);
        return arr;
        
    }
    /**
     * Returns an ArrayList of each of the 7 stacks of down cards used throughout the game
     * @return an ArrayList of each of the 7 stacks of down cards used throughout the game
     */
    public ArrayList<Stack<Card>> getDownArr()
    {
        ArrayList<Stack<Card>> arr = new ArrayList<Stack<Card>>();
        arr.add(col1Down);
        arr.add(col2Down);
        arr.add(col3Down);
        arr.add(col4Down);
        arr.add(col5Down);
        arr.add(col6Down);
        arr.add(col7Down);
        return arr;
        
    }
    /**
     * Used for recording a move from down pile to down pile, given a number of cards moved
     */
    public void recordMove(int startPile, int endPile, int numCards)
    {
        movesCount++;
        PrintWriter writer;
        String txt = startPile + ":"+endPile+":"+numCards;
        try
        {
            writer = new PrintWriter(new FileWriter(new File("moves.txt"),true));
            writer.println(movesCount+":"+txt);
            writer.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Moves file not found");
        }
        movesList.add(new Moves(movesCount,startPile,endPile,numCards,cardsTurned));
    }
    /**
     * Used for recording a move from down pile to up-pile OR from the card pile to a down-pile
     */
    public void recordMove(int startPile, int upPile)
    {
        movesCount++;
        PrintWriter writer;
        String txt = startPile + ":"+upPile+":1";
        try
        {
            writer = new PrintWriter(new FileWriter(new File("moves.txt"),true));
            writer.println(movesCount+":"+txt+":"+cardsTurned);//gamelogs' toString will write this as "win:time:moves:score"
            writer.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Moves file not found");
        }
        movesList.add(new Moves(movesCount,startPile,upPile,1,cardsTurned));
    }
    /**
     * Used for recording a move when the deck is clicked OR a pile is clicked to flip a card upright
     */
    public void recordMove(int clickedPile)
    {
        if(clickedPile==-1)
            movesCount++;
        PrintWriter writer;
        String txt = clickedPile + ":-1:-1";
        try
        {
            writer = new PrintWriter(new FileWriter(new File("moves.txt"),true));
            writer.println(movesCount+":"+txt+":"+cardsLeft);//gamelogs' toString will write this as "win:time:moves:score"
            writer.close();
            
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null,"Moves file not found");
        }
        
        //movesList.add(new Moves(movesCount,clickedPile,-1,-1,cardsTurned));
        movesList.add(new Moves(movesCount,clickedPile,-1,-1,cardsLeft));
    }
    /**
     * Returns a HashMap that maps the columns integer Index to the column
     */
    public HashMap<Integer,Stack<Card>> getStack()
    {
        HashMap<Integer,Stack<Card>> map = new HashMap<Integer,Stack<Card>>();
        map.put(CARDPILE, cardPile);
        map.put(COL1, col1);
        map.put(COL2, col2);
        map.put(COL3, col3);
        map.put(COL4, col4);
        map.put(COL5, col5);
        map.put(COL6, col6);
        map.put(COL7, col7);
        map.put(SPADESPILE,spadesPile);
        map.put(HEARTSPILE,heartsPile);
        map.put(CLUBSPILE,clubsPile);
        map.put(DIAMONDSPILE,diamondsPile);
        return map;
    }
    /**
     * Returns a HashMap that maps the columns integer Index to the column
     */
    public HashMap<Integer,Stack<Card>> getDownStack()
    {
        HashMap<Integer,Stack<Card>> map = new HashMap<Integer,Stack<Card>>();
        map.put(CARDPILE, cardPile);
        map.put(COL1, col1Down);
        map.put(COL2, col2Down);
        map.put(COL3, col3Down);
        map.put(COL4, col4Down);
        map.put(COL5, col5Down);
        map.put(COL6, col6Down);
        map.put(COL7, col7Down);
        map.put(SPADESPILE,spadesPile);
        map.put(HEARTSPILE,heartsPile);
        map.put(CLUBSPILE,clubsPile);
        map.put(DIAMONDSPILE,diamondsPile);
        return map;
    }
    /**
     * Returns the last move of the user
     */
    public Moves getLastMove()
    {
        return movesList.get(movesList.size()-1);
    }
    /**
     * Undoes the user's last move, adjusting score and movesCount if needed
     */
    public void undoMove()
    {
        
        //should delete the last move in the txt doc
        if(movesCount!=0)
        {
            usedUndo = true;
            setColsFalse();
            pileClicked = false;
            Moves lastMove = getLastMove();
            int a = lastMove.getSrc();
            int b = lastMove.getDest();
            int c = lastMove.getNumCards();
            int n = lastMove.getCardsFlipped();
            
            movesList.remove(movesList.size()-1);
            if(a==-1 && b == -1 && c == -1) //getting next card
            {
                if(!deal3){
                    deck.addCard(cardPile.pop());
                    movesCount--;
                    count--;
                }
                else{//wano
                    for(int i = 0; i<cardsTurned; i++)
                    {
                        deck.addCard(cardPile.pop());
                    }
                    if(deal3)
                        cardsTurned = n;
                    movesCount--;
                    count--;
                }
            }
            else if(a==-2) //resetting the deck
            {
                while(deck.getSize()>0)
                {
                    cardPile.add(deck.removeTopCard());
                }
                if(deal3)
                    cardsTurned = n;
            }
            else if(b==-1 && c==-1)
            {
                Stack<Card> downStack = getDownStack().get(a);
                Stack<Card> upStack = getStack().get(a);
                downStack.push(upStack.pop());
            }
            else //moving cards
            {
                Stack<Card> from = getStack().get(a);
                Stack<Card> to = getStack().get(b);
               
                    
                Stack<Card> temp = new Stack<Card>();
                for(int i = 0;i<c;i++)
                    temp.push(to.pop());
                for(int i = 0;i<c;i++)
                    from.push(temp.pop());
                if(b>7)
                    score-=3;
                cardsTurned = n;
                movesCount--;
                count--;
                
            }
            
            
        }
        else{
            JOptionPane.showMessageDialog(null, "Cannot undo");
        }
        
    }
    /**
     * Monster method that handles what to do when the screen is clicked, depending on a number of factors
     */
    public void move()
    {
        int num;
        if(rightClick)//handles what to do when the mouse has been right-clicked
        {
            boolean run = true;
            rightClick = false;
            setColsFalse();
            pileClicked = false;
            int c = 0;
            while(run)
            {
                run = false;
                c++; //number of times going through the while loop
                
                if(cardPile.size()>0) //automatically puts up any cards from the card pile
                {
                    if(canPutUp(cardPile,spadesPile))
                    {
                        spadesPile.push(cardPile.pop());
                        count++;
                        score+=3;
                        run = true;
                        recordMove(0,8);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                    else if(canPutUp(cardPile,heartsPile))
                    {
                        heartsPile.push(cardPile.pop());
                        count++;
                        score+=3;
                        run = true;
                        recordMove(0,9);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                    else if(canPutUp(cardPile,clubsPile))
                    {
                        clubsPile.push(cardPile.pop());
                        count++;
                        score+=3;
                        run = true;
                        recordMove(0,10);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                    else if(canPutUp(cardPile,diamondsPile))
                    {
                        diamondsPile.push(cardPile.pop());
                        count++;
                        score+=3;
                        run = true;
                        recordMove(0,11);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                }
                for(int i =0;i<getArr().size();i++) //goes through each of the 7 columns and checks if any cards can be put into the upper columns
                {
                    Stack<Card> temp = getArr().get(i);
                    Stack<Card> dcTemp = getDownArr().get(i);
                    if(temp.size()!=0)
                    {
                        if(canPutUp(temp,spadesPile))
                        {
                            spadesPile.push(temp.pop());
                            count++;
                            score+=3;
                            run = true;
                            recordMove(i+1,8);
                        }
                        else if(canPutUp(temp,heartsPile))
                        {
                            heartsPile.push(temp.pop());
                            count++;
                            score+=3;
                            run = true;
                            recordMove(i+1,9);
                        }
                        else if(canPutUp(temp,clubsPile))
                        {
                            clubsPile.push(temp.pop());
                            count++;
                            score+=3;
                            run = true;
                            recordMove(i+1,10);
                        }
                        else if(canPutUp(temp,diamondsPile))
                        {
                            diamondsPile.push(temp.pop());
                            count++;
                            score+=3;
                            run = true;
                            recordMove(i+1,11);
                        }
                    }
                    else if(dcTemp.size()!=0 && c==1)//will only flip over a card from the down pile to the up pile the first time through the loop
                    {
                        temp.push(dcTemp.pop());
                        
                        recordMove(i+1);
                    }
                }
            }
        }
        //sets a boolean true if any of the columns or cards have been selected. only will use this when all cols are already false. Will use algorithms later to set columns true when other columns are true
        for(int i =0;i<7;i++)
        {
            int a = getEndingUpCardLoc(i).getA();
            int b = getEndingUpCardLoc(i).getB();
            if(x>=a && (x<=a+70) && y>=b && y<=b+94 && !pileClicked && allFalse())
            {
                setColsFalse();
                pileClicked = false;
                setColTrue(i);
                numExtraCards = 0;
                x = 0;
                y = 0;
            }
            else if(!pileClicked && allFalse())
            {
                Stack<Card> colX = getArr().get(i);
                for(int j = 1; j<colX.size();j++)
                {
                    b-=17;
                    if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
                    {
                        numExtraCards = j;
                        //System.out.println("Moving " + (j + 1) + " cards in col" + (i+1));
                        setColsFalse();
                      
                        setColTrue(i);
                
                        x = 0;
                        y = 0;
                    }
                }
            }
            
            
            
        }
        //what to do when pile of cards is clicked. wont do anything if it has no cards in it
        if(cardPileClicked() && cardPile.size()>0)
        {
            numExtraCards = 0;
            if(!pileClicked)//when pile hasnt already been selected. 
            {
                pileClicked = true;
                setColsFalse();
                
                x = 0;
                y = 0;
            }
            else //checks if it a double click can assign the top card from the cardpile to any of the cols.
            {
                
                if(canPutUp(cardPile,spadesPile))
                {
                    spadesPile.push(cardPile.pop());
                    count++;
                    score+=3;
                    recordMove(CARDPILE, SPADESPILE);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutUp(cardPile,heartsPile))
                {
                    count++;
                    heartsPile.push(cardPile.pop());
                    score+=3;
                    recordMove(CARDPILE, HEARTSPILE);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutUp(cardPile,clubsPile))
                {
                    count++;
                    clubsPile.push(cardPile.pop());
                    score+=3;
                    recordMove(CARDPILE, CLUBSPILE);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutUp(cardPile,diamondsPile))
                {
                    count++;
                    diamondsPile.push(cardPile.pop());
                    score+=3;
                    recordMove(CARDPILE, DIAMONDSPILE);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutOnTop(cardPile,col1))
                {
                    count++;
                    col1.push(cardPile.pop());
                    recordMove(CARDPILE,COL1);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutOnTop(cardPile,col2))
                {
                    count++;
                    col2.push(cardPile.pop());
                    recordMove(CARDPILE,COL2);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutOnTop(cardPile,col3))
                {
                    count++;
                    col3.push(cardPile.pop()); 
                    recordMove(CARDPILE,COL3);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutOnTop(cardPile,col4))
                {
                    count++;
                    col4.push(cardPile.pop()); 
                    recordMove(CARDPILE,COL4);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutOnTop(cardPile,col5))
                {
                    count++;
                    col5.push(cardPile.pop()); 
                    recordMove(CARDPILE,COL5);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutOnTop(cardPile,col6))
                {
                    count++;
                    col6.push(cardPile.pop()); 
                    recordMove(CARDPILE,COL6);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(canPutOnTop(cardPile,col7))
                {
                    count++;
                    col7.push(cardPile.pop()); 
                    recordMove(CARDPILE,COL7);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                 
                    
                    
                pileClicked = false;
                setColsFalse();
                
                x = 0;
                y = 0;
            }
        }
        else if(spadesPileClicked() && spadesPile.size()>0 && !pileClicked && allFalse())//registers that the spadespile has been clicked (only when everything else hasnt been clicked)
        {
            numExtraCards = 0;
            setColsFalse();
            pileClicked = false;
            spadesPileClicked = true;
           
            
            x = 0;
            y = 0;
            
            
        }
        else if(heartsPileClicked() && heartsPile.size()>0 && !pileClicked && allFalse())
        {
            numExtraCards = 0;
            setColsFalse();
            pileClicked = false;
            heartsPileClicked = true;
           
            
            x = 0;
            y = 0;
            
        }
        else if(clubsPileClicked() && clubsPile.size()>0 && !pileClicked && allFalse())
        {
            numExtraCards = 0;
            setColsFalse();
            pileClicked = false;
            clubsPileClicked = true;
           
            
            x = 0;
            y = 0;
            
        }
        else if(diamondsPileClicked() && diamondsPile.size()>0 && !pileClicked && allFalse())
        {
            numExtraCards = 0;
            setColsFalse();
            pileClicked = false;
            diamondsPileClicked = true;
           
            
            x = 0;
            y = 0;
        
        }
        //the following if-elseif chain is big enough to have its own zip code
        if(deckClicked())
        {
            numExtraCards = 0;
            if(deck.getSize() >0) //turning a card over HERE
            {
                if(deal3)
                {
                    Stack<Card> temp = new Stack<Card>();
                    
                    cardsLeft = cardsTurned;
                    if(cardPile.size()==0)
                        cardsLeft = 0;
                        
                    if(deck.getSize()>=3)
                        cardsTurned = 3;
                    else
                        cardsTurned = deck.getSize();
                    for(int i=0;i<cardsTurned;i++)
                        cardPile.push(deck.removeTopCard());
                    /*
                    for(int i=0;i<cardsTurned;i++)
                        temp.push(deck.removeTopCard());
                    for(int i=0;i<cardsTurned;i++)
                        cardPile.push(temp.pop());
                        */
                    
                }
                else
                    cardPile.push(deck.removeTopCard());
                count++;
                recordMove(DECK);
            }
            else //resetting the deck, maintaining the same order
            {
                while(cardPile.size()>0)
                {
                    deck.addCard(cardPile.pop());
                }
                cardsLeft = cardsTurned;
                recordMove(RESET);
                //cardsLeft = 0;
            }
            pileClicked = false;
            setColsFalse();
            
            x = 0;
            y = 0;
            //run = false;
        }
        else if(pileClicked) //when the pile has been already selected, this deals with what to do when something else has been selected
        {
            if(spadesPileClicked())//when the pile had been selected at first, and now the spadesPile has been clicked...
            {
                if(canPutUp(cardPile,spadesPile) && cardPile.peek().getSuit().equalsIgnoreCase("Spades"))//will put card from pile onto spadesPile if possible
                {
                    spadesPile.push(cardPile.pop());
                    count++;
                    pileClicked = false;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(CARDPILE, SPADESPILE);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(spadesPile.size()!=0)//if cant put card ontop of spadesPile, will merely select the spadesPile
                {
                    pileClicked = false;
                    spadesPileClicked = true;
                    x = 0;
                    y = 0;
                }
            }
            else if(heartsPileClicked())//continues the format above
            {
                if(canPutUp(cardPile,heartsPile) && cardPile.peek().getSuit().equalsIgnoreCase("Hearts"))
                {
                    heartsPile.push(cardPile.pop());
                    count++;
                    pileClicked = false;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(CARDPILE, HEARTSPILE);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(heartsPile.size()!=0)
                {
                    pileClicked = false;
                    heartsPileClicked = true;
                    x = 0;
                    y = 0;
                }
            }
            else if(clubsPileClicked())
            {
                if(canPutUp(cardPile,clubsPile) && cardPile.peek().getSuit().equalsIgnoreCase("Clubs"))
                {
                    clubsPile.push(cardPile.pop());
                    count++;
                    pileClicked = false;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(CARDPILE, CLUBSPILE);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(clubsPile.size()!=0)
                {
                    pileClicked = false;
                    clubsPileClicked = true;
                    x = 0;
                    y = 0;
                }
            }
            else if(diamondsPileClicked())
            {
                if(canPutUp(cardPile,diamondsPile) && cardPile.peek().getSuit().equalsIgnoreCase("Diamonds"))
                {
                    diamondsPile.push(cardPile.pop());
                    count++;
                    pileClicked = false;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(CARDPILE, DIAMONDSPILE);
                    if(cardsTurned>1)
                        cardsTurned--;
                }
                else if(diamondsPile.size()!=0)
                {
                    pileClicked = false;
                    diamondsPileClicked = true;
                    x = 0;
                    y = 0;
                }
            }
            else //deals with 7 cols (when card from pile is clicked)
            {
                //                 for(int i =0;i<7;i++)
                //                 {
                //                     if(x>=getEndingUpCardLoc(i).getA() && (x<=getEndingUpCardLoc(i).getA()+70)&&y>=getEndingUpCardLoc(i).getB()&&y<=getEndingUpCardLoc(i).getB()+94)
                //                     {
                //                         if(canPutOnTop(cardPile,allCols().get(i)))
                //                         {
                //                             allCols().get(i).push(cardPile.pop());
                //                             x = 0;
                //                             y = 0;
                //                             setColsFalse();
                //                             pileClicked = false;
                //                             break;
                //                             
                //                         }
                //                     }
                //                 }
                if(col1Clicked())
                {
                    if(canPutOnTop(cardPile,col1) && numExtraCards ==0)//numextracards has to be 0 b/c you must click the top card in col1
                    {
                        col1.push(cardPile.pop());
                        count++;
                        x = 0;
                        y = 0;
                        setColsFalse();
                        pileClicked = false;
                        recordMove(CARDPILE, COL1);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                    else //will set col1 true
                    {
                        pileClicked = false;
                        setColTrue(0);
                        x = 0;
                        y = 0;
                    }
                }
                else if(col2Clicked())
                {
                    if(canPutOnTop(cardPile,col2) && numExtraCards ==0)
                    {
                        col2.push(cardPile.pop());
                        count++;
                        x = 0;
                        y = 0;
                        setColsFalse();
                        pileClicked = false;
                        recordMove(CARDPILE, COL2);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                    else
                    {
                        pileClicked = false;
                        setColTrue(1);
                        x = 0;
                        y = 0;
                    }
                }
                else if(col3Clicked())
                {
                    if(canPutOnTop(cardPile,col3) && numExtraCards ==0)
                    {
                        col3.push(cardPile.pop());
                        count++;
                        x = 0;
                        y = 0;
                        setColsFalse();
                        pileClicked = false;
                        recordMove(CARDPILE, COL3);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                    else
                    {
                        pileClicked = false;
                        setColTrue(2);
                        x = 0;
                        y = 0;
                    }
                }
                else if(col4Clicked())
                {
                    if(canPutOnTop(cardPile,col4) && numExtraCards ==0)
                    {
                        col4.push(cardPile.pop());
                        count++;
                        x = 0;
                        y = 0;
                        setColsFalse();
                        pileClicked = false;
                        recordMove(CARDPILE, COL4);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                    else
                    {
                        pileClicked = false;
                        setColTrue(3);
                        x = 0;
                        y = 0;
                    }
                }
                else if(col5Clicked() && numExtraCards ==0)
                {
                    if(canPutOnTop(cardPile,col5))
                    {
                        col5.push(cardPile.pop());
                        count++;
                        x = 0;
                        y = 0;
                        setColsFalse();
                        pileClicked = false;
                        recordMove(CARDPILE, COL5);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                    else
                    {
                        pileClicked = false;
                        setColTrue(4);
                        x = 0;
                        y = 0;
                    }
                }
                else if(col6Clicked())
                {
                    if(canPutOnTop(cardPile,col6) && numExtraCards ==0)
                    {
                        col6.push(cardPile.pop());
                        count++;
                        x = 0;
                        y = 0;
                        setColsFalse();
                        pileClicked = false;
                        recordMove(CARDPILE, COL6);
                        if(cardsTurned>1)
                            cardsTurned--;
                    }
                    else
                    {
                        pileClicked = false;
                        setColTrue(5);
                        x = 0;
                        y = 0;
                    }
                }
                else if(col7Clicked())
                {
                    if(canPutOnTop(cardPile,col7) && numExtraCards ==0)
                    {
                        col7.push(cardPile.pop());
                        count++;
                        x = 0;
                        y = 0;
                        setColsFalse();
                        pileClicked = false;
                        recordMove(CARDPILE, COL7);
                        if(cardsTurned>1)
                            cardsTurned--;                       
                    }
                    else
                    {
                        pileClicked = false;
                        setColTrue(6);
                        x = 0;
                        y = 0;
                    }
                }
                //else if(!(x==0 && y==0))
                    //pileClicked = false;
                
            }
        }

        //when col1 is clicked (seen during game when col1 is highlighted)...
        
        else if(col1Clicked)
        {
            num = numExtraCards;//retains the amount of cards in col1 that had been clicked, as numExtraCards will change once another col has been clicked
            if(col1.size() == 0)//if col1 has no up cards
            {
                if(col1Down.size() != 0) //col1Down size will always be 0 but for the sake of consistency...
                {
                    col1.push(col1Down.pop());
                    col1Clicked = false;
                    recordMove(COL1);
                }
                else //if col1 has no up cards and no down cards
                {
                    //col1Clicked = false;
                    
                }
            }
            else if(col1Clicked(numExtraCards))
            {
                if(sameCard)//checks if the same card in col1 has been clicked. If so, will check if those cards can be put onto any other column
                {
                    Stack<Card> temp = new Stack<Card>();
                    if(canPutUp(col1,spadesPile)&& numExtraCards==0)//sees if card can be put onto upper columns, which can only happen when only 1 card is selected (and thus numExtraCards = 0)
                    {
                        spadesPile.push(col1.pop());
                        count++;
                        score+=3;
                        recordMove(COL1,SPADESPILE);
                    }
                    else if(canPutUp(col1,heartsPile)&& numExtraCards==0)
                    {
                        count++;
                        heartsPile.push(col1.pop());
                        score+=3;
                        recordMove(COL1,HEARTSPILE);
                    }
                    else if(canPutUp(col1,clubsPile)&& numExtraCards==0)
                    {
                        count++;
                        clubsPile.push(col1.pop());
                        score+=3;
                        recordMove(COL1,CLUBSPILE);
                    }
                    else if(canPutUp(col1,diamondsPile)&& numExtraCards==0)
                    {
                        diamondsPile.push(col1.pop());
                        count++;
                        score+=3;
                        recordMove(COL1,DIAMONDSPILE);
                    }
                    else if(canPutOnTop(col1,col2,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col1.pop());
                        while(temp.size()>0)
                            col2.push(temp.pop());
                        count++;
                        recordMove(COL1,COL2,numExtraCards+1);
                    }
                    else if(canPutOnTop(col1,col3,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col1.pop());
                        while(temp.size()>0)
                            col3.push(temp.pop());
                        count++;
                       recordMove(COL1,COL3,numExtraCards+1);
                    }
                    else if(canPutOnTop(col1,col4,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col1.pop());
                        while(temp.size()>0)
                            col4.push(temp.pop());
                        count++;
                        recordMove(COL1,COL4,numExtraCards+1);
                    }
                    else if(canPutOnTop(col1,col5,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col1.pop());
                        while(temp.size()>0)
                            col5.push(temp.pop());
                        count++;
                        recordMove(COL1,COL5,numExtraCards+1);
                    }
                    else if(canPutOnTop(col1,col6,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col1.pop());
                        while(temp.size()>0)
                            col6.push(temp.pop());
                        count++;
                        recordMove(COL1,COL6,numExtraCards+1);
                    }
                    else if(canPutOnTop(col1,col7,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col1.pop());
                        while(temp.size()>0)
                            col7.push(temp.pop());
                        count++;
                        recordMove(COL1,COL7,numExtraCards+1);
                    }
                    
                    numExtraCards = 0;
                    x = 0;
                    y = 0;
                    setColsFalse();
                   
                    
                }
                else //if different card within the column has been selected, it will simply highlight the new cards being selected
                {
                    int a = getEndingUpCardLoc(0).getA();
                    int b = getEndingUpCardLoc(0).getB();
                    if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
                    {
                        numExtraCards = 0;
                       
                    }
                    for(int i = 1; i<col1.size();i++)
                    {
                        b-=17;
                        if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
                        {
                            numExtraCards = i;
                            //System.out.println("Moving " + i + 1 + " cards");
                            
                        }
                    }
                    x = 0;
                    y = 0;
                }
                
            }
            
            else if(col2Clicked())
            {
                
                if(canPutOnTop(col1,col2,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col1.pop());
                    while(temp.size()>0)
                        col2.push(temp.pop());
                    //col2.push(col1.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL1,COL2,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(col1,col3,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col1.pop());
                    while(temp.size()>0)
                        col3.push(temp.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    //col3.push(col1.pop());
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL1,COL3,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(col1,col4,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col1.pop());
                    while(temp.size()>0)
                        col4.push(temp.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL1,COL4,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(col1,col5,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col1.pop());
                    while(temp.size()>0)
                        col5.push(temp.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL1,COL5,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(col1,col6,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col1.pop());
                    while(temp.size()>0)
                        col6.push(temp.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL1,COL6,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(5);
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(col1,col7,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col1.pop());
                    while(temp.size()>0)
                        col7.push(temp.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL1,COL7,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(6);
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked())
            {
                if(canPutUp(col1,spadesPile) && col1.peek().getSuit().equalsIgnoreCase("Spades") &&  numExtraCards==0)
                {
                    spadesPile.push(col1.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL1,SPADESPILE);
                }
                else if(spadesPile.size()!=0)
                {
                    setColsFalse();
                    spadesPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(heartsPileClicked())
            {
                if(canPutUp(col1,heartsPile) && col1.peek().getSuit().equalsIgnoreCase("Hearts") &&  numExtraCards==0)
                {
                    heartsPile.push(col1.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL1,HEARTSPILE);
                }
                else if(heartsPile.size()!=0)
                {
                    setColsFalse();
                    heartsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(clubsPileClicked() )
            {
                if(canPutUp(col1,clubsPile) && col1.peek().getSuit().equalsIgnoreCase("Clubs")&& numExtraCards==0)
                {
                    clubsPile.push(col1.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL1,CLUBSPILE);
                }
                else if(clubsPile.size()!=0)
                {
                    setColsFalse();
                    clubsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(diamondsPileClicked())
            {
                if(canPutUp(col1,diamondsPile) && col1.peek().getSuit().equalsIgnoreCase("Diamonds")  && numExtraCards==0)
                {
                    diamondsPile.push(col1.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL1,DIAMONDSPILE);
                }
                else if(diamondsPile.size()!=0)
                {
                    setColsFalse();
                    diamondsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            //else if(!(x==0 &&y==0))
                //setColsFalse();
        }
        else if(col2Clicked)
        {
            num = numExtraCards;
            if(col2.size() == 0)//if col2 has no up cards
            {
                //previous version: if(col2Clicked() && col2Down.size() != 0)
                if(col2Down.size() != 0)
                {
                    col2.push(col2Down.pop());
                    col2Clicked = false;
                    recordMove(COL2);
                    //setColsFalse();
                }
                else //if col1 has no up cards and no down cards
                {
                    //col1Clicked = false;
                }
            }
            else if(col1Clicked())
            {
                if(canPutOnTop(col2,col1,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col2.pop());
                    while(temp.size()>0)
                        col1.push(temp.pop());
                    count++;
                    
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL2,COL1,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(0);
                    x = 0;
                    y = 0;
                }
            }
            else if(col2Clicked(numExtraCards))
            {
                if(sameCard)
                {
                    Stack<Card> temp = new Stack<Card>();
                    if(canPutUp(col2,spadesPile)&& numExtraCards==0)
                    {
                        spadesPile.push(col2.pop());
                        count++;
                        score+=3;
                        recordMove(COL2,SPADESPILE);
                    }
                    else if(canPutUp(col2,heartsPile)&& numExtraCards==0)
                    {
                        count++;
                        heartsPile.push(col2.pop());
                        score+=3;
                        recordMove(COL2,HEARTSPILE);
                    }
                    else if(canPutUp(col2,clubsPile)&& numExtraCards==0)
                    {
                        count++;
                        clubsPile.push(col2.pop());
                        score+=3;
                        recordMove(COL2,CLUBSPILE);
                    }
                    else if(canPutUp(col2,diamondsPile)&& numExtraCards==0)
                    {
                        count++;
                        diamondsPile.push(col2.pop());
                        score+=3;
                        recordMove(COL2,DIAMONDSPILE);
                    }
                    else if(canPutOnTop(col2,col1,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col2.pop());
                        while(temp.size()>0)
                            col1.push(temp.pop());
                        count++;
                        recordMove(COL2,COL1,numExtraCards+1);
                    }
                    else if(canPutOnTop(col2,col3,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col2.pop());
                        while(temp.size()>0)
                            col3.push(temp.pop());
                        count++;
                        recordMove(COL2,COL3,numExtraCards+1);
                    }
                    else if(canPutOnTop(col2,col4,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col2.pop());
                        while(temp.size()>0)
                            col4.push(temp.pop());
                        count++;
                        recordMove(COL2,COL4,numExtraCards+1);
                    }
                    else if(canPutOnTop(col2,col5,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col2.pop());
                        while(temp.size()>0)
                            col5.push(temp.pop());
                        count++;
                        recordMove(COL2,COL5,numExtraCards+1);
                    }
                    else if(canPutOnTop(col2,col6,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col2.pop());
                        while(temp.size()>0)
                            col6.push(temp.pop());
                        count++;
                        recordMove(COL2,COL6,numExtraCards+1);
                    }
                    else if(canPutOnTop(col2,col7,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col2.pop());
                        while(temp.size()>0)
                            col7.push(temp.pop());
                        count++;
                        recordMove(COL2,COL7,numExtraCards+1);
                    }
                    
                    numExtraCards = 0;
                    x = 0;
                    y = 0;
                    setColsFalse();
                    
                    
                }
                else
                {
                    int a = getEndingUpCardLoc(1).getA();
                    int b = getEndingUpCardLoc(1).getB();
                    if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
                    {
                        numExtraCards = 0;
                       
                    }
                    for(int i = 1; i<col2.size();i++)
                    {
                        b-=17;
                        if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
                        {
                            numExtraCards = i;
                            //System.out.println("Moving " + i + 1 + " cards");
                            
                        }
                    }
                    x = 0;
                    y = 0;
                }
                
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(col2,col3,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col2.pop());
                    while(temp.size()>0)
                        col3.push(temp.pop());
                    count++;
                    
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL2,COL3,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(col2,col4,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col2.pop());
                    while(temp.size()>0)
                        col4.push(temp.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL2,COL4,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(col2,col5,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col2.pop());
                    while(temp.size()>0)
                        col5.push(temp.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL2,COL5,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(col2,col6,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col2.pop());
                    while(temp.size()>0)
                        col6.push(temp.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL2,COL6,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(5);
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(col2,col7,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col2.pop());
                    while(temp.size()>0)
                        col7.push(temp.pop());
                    count++;
                    //num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    recordMove(COL2,COL7,num+1);
                    num = 0;
                }
                else
                {
                    setColTrue(6);
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked())
            {
                if(canPutUp(col2,spadesPile) && col2.peek().getSuit().equalsIgnoreCase("Spades") &&  numExtraCards==0)
                {
                    spadesPile.push(col2.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL2,SPADESPILE);
                }
                else if(spadesPile.size()!=0)
                {
                    setColsFalse();
                    spadesPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(heartsPileClicked())
            {
                if(canPutUp(col2,heartsPile) && col2.peek().getSuit().equalsIgnoreCase("Hearts")&&  numExtraCards==0)
                {
                    heartsPile.push(col2.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL2,HEARTSPILE);
                }
                else if(heartsPile.size()!=0)
                {
                    setColsFalse();
                    heartsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(clubsPileClicked())
            {
                if(canPutUp(col2,clubsPile) && col2.peek().getSuit().equalsIgnoreCase("Clubs") &&  numExtraCards==0)
                {
                    clubsPile.push(col2.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL2,CLUBSPILE);
                }
                else if(clubsPile.size()!=0)
                {
                    setColsFalse();
                    clubsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(diamondsPileClicked())
            {
                if(canPutUp(col2,diamondsPile) && col2.peek().getSuit().equalsIgnoreCase("Diamonds") &&  numExtraCards==0)
                {
                    diamondsPile.push(col2.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL2,DIAMONDSPILE);
                }
                 else if(diamondsPile.size()!=0)
                {
                    setColsFalse();
                    diamondsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
        }
        //WANO
        else if(col3Clicked)
        {
            num = numExtraCards;
            if(col3.size() == 0)//if col1 has no up cards
            {
                if(col3Down.size() != 0)
                {
                    col3.push(col3Down.pop());
                    col3Clicked = false;
                    recordMove(COL3);
                    //setColsFalse();
                }
                else //if col1 has no up cards and no down cards
                {
                    //col1Clicked = false;
                }
            }
            else if(col1Clicked())
            {
                if(canPutOnTop(col3,col1,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col3.pop());
                    while(temp.size()>0)
                        col1.push(temp.pop());
                    count++;
                    recordMove(COL3,COL1,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(0);
                    x = 0;
                    y = 0;
                
                }
            }
            else if(col2Clicked())
            {
                if(canPutOnTop(col3,col2,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col3.pop());
                    while(temp.size()>0)
                        col2.push(temp.pop());
                    count++;
                    recordMove(COL3,COL2,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked(numExtraCards))
            {
                if(sameCard)
                {
                    Stack<Card> temp = new Stack<Card>();
                    if(canPutUp(col3,spadesPile)&& numExtraCards==0)
                    {
                        spadesPile.push(col3.pop());
                        count++;
                        score+=3;
                        recordMove(COL3,SPADESPILE);
                    }
                    else if(canPutUp(col3,heartsPile)&& numExtraCards==0)
                    {
                        count++;
                        heartsPile.push(col3.pop());
                        score+=3;
                        recordMove(COL3,HEARTSPILE);
                    }
                    else if(canPutUp(col3,clubsPile)&& numExtraCards==0)
                    {
                        clubsPile.push(col3.pop());
                        count++;
                        score+=3;
                        recordMove(COL3,CLUBSPILE);
                    }
                    else if(canPutUp(col3,diamondsPile)&& numExtraCards==0)
                    {
                        diamondsPile.push(col3.pop());
                        count++;
                        score+=3;
                        recordMove(COL3,DIAMONDSPILE);
                    }
                    else if(canPutOnTop(col3,col1,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col3.pop());
                        while(temp.size()>0)
                            col1.push(temp.pop());
                        count++;
                        recordMove(COL3,COL1,numExtraCards+1);
                    }
                    else if(canPutOnTop(col3,col2,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col3.pop());
                        while(temp.size()>0)
                            col2.push(temp.pop());
                        count++;
                        recordMove(COL3,COL2,numExtraCards+1);
                    }
                    else if(canPutOnTop(col3,col4,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col3.pop());
                        while(temp.size()>0)
                            col4.push(temp.pop());
                        count++;
                        recordMove(COL3,COL4,numExtraCards+1);
                    }
                    else if(canPutOnTop(col3,col5,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col3.pop());
                        while(temp.size()>0)
                            col5.push(temp.pop());
                        count++;
                        recordMove(COL3,COL5,numExtraCards+1);
                    }
                    else if(canPutOnTop(col3,col6,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col3.pop());
                        while(temp.size()>0)
                            col6.push(temp.pop());
                        count++;
                        recordMove(COL3,COL6,numExtraCards+1);
                    }
                    else if(canPutOnTop(col3,col7,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col3.pop());
                        while(temp.size()>0)
                            col7.push(temp.pop());
                        count++;
                        recordMove(COL3,COL7,numExtraCards+1);
                    }
                    
                    numExtraCards = 0;
                    x = 0;
                    y = 0;
                    setColsFalse();
                                    
                }
                else
                {
                    int a = getEndingUpCardLoc(2).getA();
                    int b = getEndingUpCardLoc(2).getB();
                    if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
                    {
                        numExtraCards = 0;
                       
                    }
                    for(int i = 1; i<col3.size();i++)
                    {
                        b-=17;
                        if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
                        {
                            numExtraCards = i;
                            //System.out.println("Moving " + i + 1 + " cards");
                            
                        }
                    }
                    x = 0;
                    y = 0;
                }
                
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(col3,col4,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col3.pop());
                    while(temp.size()>0)
                        col4.push(temp.pop());
                    count++;
                    recordMove(COL4,COL1,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(col3,col5,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col3.pop());
                    while(temp.size()>0)
                        col5.push(temp.pop());
                    count++;
                    recordMove(COL5,COL1,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(col3,col6,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col3.pop());
                    while(temp.size()>0)
                        col6.push(temp.pop());
                    count++;
                    recordMove(COL6,COL1,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(5);
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(col3,col7,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col3.pop());
                    while(temp.size()>0)
                        col7.push(temp.pop());
                    count++;
                    recordMove(COL7,COL1,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(6);
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked())
            {
                if(canPutUp(col3,spadesPile) && col3.peek().getSuit().equalsIgnoreCase("Spades") &&  numExtraCards==0)
                {
                    spadesPile.push(col3.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL3,SPADESPILE);
                }
                else if(spadesPile.size()!=0)
                {
                    setColsFalse();
                    spadesPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(heartsPileClicked())
            {
                if(canPutUp(col3,heartsPile) && col3.peek().getSuit().equalsIgnoreCase("Hearts") &&  numExtraCards==0)
                {
                    heartsPile.push(col3.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL3,HEARTSPILE);
                }
                else if(heartsPile.size()!=0)
                {
                    setColsFalse();
                    heartsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(clubsPileClicked())
            {
                if(canPutUp(col3,clubsPile) && col3.peek().getSuit().equalsIgnoreCase("Clubs") &&  numExtraCards==0)
                {
                    clubsPile.push(col3.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL3,CLUBSPILE);
                }
                else if(clubsPile.size()!=0)
                {
                    setColsFalse();
                    clubsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(diamondsPileClicked())
            {
                if(canPutUp(col3,diamondsPile) && col3.peek().getSuit().equalsIgnoreCase("Diamonds") &&  numExtraCards==0)
                {
                    diamondsPile.push(col3.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL3,DIAMONDSPILE);
                }
                else if(diamondsPile.size()!=0)
                {
                    setColsFalse();
                    diamondsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
        }
        else if(col4Clicked)
        {
            num = numExtraCards;
            if(col4.size() == 0)//if col1 has no up cards
            {
                if(col4Down.size() != 0)
                {
                    col4.push(col4Down.pop());
                    col4Clicked = false;
                    recordMove(COL4);
                    //setColsFalse();
                }
                else //if col1 has no up cards and no down cards
                {
                    //col1Clicked = false;
                }
            }
            else if(col1Clicked())
            {
                if(canPutOnTop(col4,col1,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col4.pop());
                    while(temp.size()>0)
                        col1.push(temp.pop());
                    count++;
                    recordMove(COL4,COL1,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(0);
                    x = 0;
                    y = 0;
                }
            }
            else if(col2Clicked())
            {
                if(canPutOnTop(col4,col2,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col4.pop());
                    while(temp.size()>0)
                        col2.push(temp.pop());
                    count++;
                    recordMove(COL4,COL2,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(col4,col3,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col4.pop());
                    while(temp.size()>0)
                        col3.push(temp.pop());
                    count++;
                    recordMove(COL4,COL3,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked(numExtraCards))
            {
                if(sameCard)
                {
                    Stack<Card> temp = new Stack<Card>();
                    if(canPutUp(col4,spadesPile)&& numExtraCards==0)
                    {
                        spadesPile.push(col4.pop());
                        count++;
                        score+=3;
                        recordMove(COL4,SPADESPILE);
                    }
                    else if(canPutUp(col4,heartsPile)&& numExtraCards==0)
                    {
                        heartsPile.push(col4.pop());
                        count++;
                        score+=3;
                        recordMove(COL4,HEARTSPILE);
                    }
                    else if(canPutUp(col4,clubsPile)&& numExtraCards==0)
                    {
                        count++;
                        clubsPile.push(col4.pop());
                        score+=3;
                        recordMove(COL4,CLUBSPILE);
                    }
                    else if(canPutUp(col4,diamondsPile)&& numExtraCards==0)
                    {
                        diamondsPile.push(col4.pop());
                        count++;
                        score+=3;
                        recordMove(COL4,DIAMONDSPILE);
                    }
                    
                    else if(canPutOnTop(col4,col1,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col4.pop());
                        while(temp.size()>0)
                            col1.push(temp.pop());
                        count++;
                        recordMove(COL4,COL1,numExtraCards+1);
                    }
                    else if(canPutOnTop(col4,col2,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col4.pop());
                        while(temp.size()>0)
                            col2.push(temp.pop());
                        count++;
                        recordMove(COL4,COL2,numExtraCards+1);
                    }
                    else if(canPutOnTop(col4,col3,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col4.pop());
                        while(temp.size()>0)
                            col3.push(temp.pop());
                        count++;
                        recordMove(COL4,COL3,numExtraCards+1);
                    }
                    else if(canPutOnTop(col4,col5,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col4.pop());
                        while(temp.size()>0)
                            col5.push(temp.pop());
                        count++;
                        recordMove(COL4,COL5,numExtraCards+1);
                    }
                    else if(canPutOnTop(col4,col6,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col4.pop());
                        while(temp.size()>0)
                            col6.push(temp.pop());
                        count++;
                        recordMove(COL4,COL6,numExtraCards+1);
                    }
                    else if(canPutOnTop(col4,col7,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col4.pop());
                        while(temp.size()>0)
                            col7.push(temp.pop());
                        count++;
                        recordMove(COL4,COL7,numExtraCards+1);
                    }
                    
                    numExtraCards = 0;
                    x = 0;
                    y = 0;
                    setColsFalse();
                    
                    
                
                }
                else
                {
                    int a = getEndingUpCardLoc(3).getA();
                    int b = getEndingUpCardLoc(3).getB();
                    if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
                    {
                        numExtraCards = 0;
                       
                    }
                    for(int i = 1; i<col4.size();i++)
                    {
                        b-=17;
                        if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
                        {
                            numExtraCards = i;
                            //System.out.println("Moving " + i + 1 + " cards");
                            
                        }
                    }
                    x = 0;
                    y = 0;
                }
                
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(col4,col5,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col4.pop());
                    while(temp.size()>0)
                        col5.push(temp.pop());
                    count++;
                    recordMove(COL4,COL5,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(col4,col6,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col4.pop());
                    while(temp.size()>0)
                        col6.push(temp.pop());
                    count++;
                    recordMove(COL4,COL6,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(5);
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(col4,col7,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col4.pop());
                    while(temp.size()>0)
                        col7.push(temp.pop());
                    count++;
                    recordMove(COL4,COL7,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(6);
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked())
            {
                if(canPutUp(col4,spadesPile) && col4.peek().getSuit().equalsIgnoreCase("Spades")&&  numExtraCards==0)
                {
                    spadesPile.push(col4.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL4,SPADESPILE);
                }
                else if(spadesPile.size()!=0)
                {
                    setColsFalse();
                    spadesPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(heartsPileClicked())
            {
                if(canPutUp(col4,heartsPile) && col4.peek().getSuit().equalsIgnoreCase("Hearts")&&  numExtraCards==0)
                {
                    heartsPile.push(col4.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL4,HEARTSPILE);
                }
                else if(heartsPile.size()!=0)
                {
                    setColsFalse();
                    heartsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(clubsPileClicked())
            {
                if(canPutUp(col4,clubsPile) && col4.peek().getSuit().equalsIgnoreCase("Clubs")&&  numExtraCards==0)
                {
                    clubsPile.push(col4.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL4,CLUBSPILE);
                }
                else if(clubsPile.size()!=0)
                {
                    setColsFalse();
                    clubsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(diamondsPileClicked())
            {
                if(canPutUp(col4,diamondsPile) && col4.peek().getSuit().equalsIgnoreCase("Diamonds")&&  numExtraCards==0)
                {
                    diamondsPile.push(col4.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL4,DIAMONDSPILE);
                }
                else if(diamondsPile.size()!=0)
                {
                    setColsFalse();
                    diamondsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
        }
        else if(col5Clicked)
        {
            num = numExtraCards;
            if(col5.size() == 0)//if col1 has no up cards
            {
                if(col5Down.size() != 0)
                {
                    col5.push(col5Down.pop());
                    col5Clicked = false;
                    recordMove(COL5);
                    //setColsFalse();
                }
                else //if col1 has no up cards and no down cards
                {
                    //col1Clicked = false;
                }
            }
            else if(col1Clicked())
            {
                if(canPutOnTop(col5,col1,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col5.pop());
                    while(temp.size()>0)
                        col1.push(temp.pop());
                    count++;
                    recordMove(COL5,COL1,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(0);
                    x = 0;
                    y = 0;
                }
            }
            else if(col2Clicked())
            {
                if(canPutOnTop(col5,col2,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col5.pop());
                    while(temp.size()>0)
                        col2.push(temp.pop());
                    count++;
                    recordMove(COL5,COL2,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(col5,col3,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col5.pop());
                    while(temp.size()>0)
                        col3.push(temp.pop());
                    count++;
                    recordMove(COL5,COL3,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(col5,col4,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col5.pop());
                    while(temp.size()>0)
                        col4.push(temp.pop());
                    count++;
                    recordMove(COL5,COL4,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked(numExtraCards))
            {
                if(sameCard)
                {
                    Stack<Card> temp = new Stack<Card>();
                    if(canPutUp(col5,spadesPile)&& numExtraCards==0)
                    {
                        spadesPile.push(col5.pop());
                        count++;
                        score+=3;
                        recordMove(COL5,SPADESPILE);
                    }
                    else if(canPutUp(col5,heartsPile)&& numExtraCards==0)
                    {
                        count++;
                        heartsPile.push(col5.pop());
                        score+=3;
                        recordMove(COL5,HEARTSPILE);
                    }
                    else if(canPutUp(col5,clubsPile)&& numExtraCards==0)
                    {
                        clubsPile.push(col5.pop());
                        count++;
                        score+=3;
                        recordMove(COL5,CLUBSPILE);
                    }
                    else if(canPutUp(col5,diamondsPile)&& numExtraCards==0)
                    {
                        diamondsPile.push(col5.pop());
                        count++;
                        score+=3;
                        recordMove(COL5,DIAMONDSPILE);
                    }
                    else if(canPutOnTop(col5,col1,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col5.pop());
                        while(temp.size()>0)
                            col1.push(temp.pop());
                        count++;
                        recordMove(COL5,COL1,numExtraCards+1);
                    }
                    else if(canPutOnTop(col5,col2,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col5.pop());
                        while(temp.size()>0)
                            col2.push(temp.pop());
                        count++;
                        recordMove(COL5,COL2,numExtraCards+1);
                    }
                    else if(canPutOnTop(col5,col3,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col5.pop());
                        while(temp.size()>0)
                            col3.push(temp.pop());
                        count++;
                        recordMove(COL5,COL3,numExtraCards+1);
                    }
                    else if(canPutOnTop(col5,col4,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col5.pop());
                        while(temp.size()>0)
                            col4.push(temp.pop());
                        count++;
                        recordMove(COL5,COL4,numExtraCards+1);
                    }
                    else if(canPutOnTop(col5,col6,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col5.pop());
                        while(temp.size()>0)
                            col6.push(temp.pop());
                        count++;
                        recordMove(COL5,COL6,numExtraCards+1);
                    }
                    else if(canPutOnTop(col5,col7,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col5.pop());
                        while(temp.size()>0)
                            col7.push(temp.pop());
                        count++;
                        recordMove(COL5,COL7,numExtraCards+1);
                    }
                    
                    numExtraCards = 0;
                    x = 0;
                    y = 0;
                    setColsFalse();
                    
                    
                
                }
                else
                {
                    int a = getEndingUpCardLoc(4).getA();
                    int b = getEndingUpCardLoc(4).getB();
                    if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
                    {
                        numExtraCards = 0;
                       
                    }
                    for(int i = 1; i<col5.size();i++)
                    {
                        b-=17;
                        if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
                        {
                            numExtraCards = i;
                            //System.out.println("Moving " + i + 1 + " cards");
                            
                        }
                    }
                    x = 0;
                    y = 0;
                }
                
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(col5,col6,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col5.pop());
                    while(temp.size()>0)
                        col6.push(temp.pop());
                    count++;
                    recordMove(COL5,COL6,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(5);
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(col5,col7,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col5.pop());
                    while(temp.size()>0)
                        col7.push(temp.pop());
                    count++;
                    recordMove(COL5,COL7,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(6);
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked())
            {
                if(canPutUp(col5,spadesPile) && col5.peek().getSuit().equalsIgnoreCase("Spades")&&  numExtraCards==0)
                {
                    spadesPile.push(col5.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL5,SPADESPILE);
                }
                else if(spadesPile.size()!=0)
                {
                    setColsFalse();
                    spadesPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(heartsPileClicked())
            {
                if(canPutUp(col5,heartsPile) && col5.peek().getSuit().equalsIgnoreCase("Hearts")&&  numExtraCards==0)
                {
                    heartsPile.push(col5.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL5,HEARTSPILE);
                }
                else if(heartsPile.size()!=0)
                {
                    setColsFalse();
                    heartsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(clubsPileClicked())
            {
                if(canPutUp(col5,clubsPile) && col5.peek().getSuit().equalsIgnoreCase("Clubs")&&  numExtraCards==0)
                {
                    clubsPile.push(col5.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL5,CLUBSPILE);
                }
                else if(clubsPile.size()!=0)
                {
                    setColsFalse();
                    clubsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(diamondsPileClicked())
            {
                if(canPutUp(col5,diamondsPile) && col5.peek().getSuit().equalsIgnoreCase("Diamonds")&&  numExtraCards==0)
                {
                    diamondsPile.push(col5.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL5,DIAMONDSPILE);
                }
                else if(diamondsPile.size()!=0)
                {
                    setColsFalse();
                    diamondsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
        }
        else if(col6Clicked)
        {
            num = numExtraCards;
            if(col6.size() == 0)//if col1 has no up cards
            {
                if(col6Down.size() != 0)
                {
                    col6.push(col6Down.pop());
                    col6Clicked = false;
                    recordMove(COL6);
                    //setColsFalse();
                }
                else //if col1 has no up cards and no down cards
                {
                    //col1Clicked = false;
                }
            }
            else if(col1Clicked())
            {
                if(canPutOnTop(col6,col1,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col6.pop());
                    while(temp.size()>0)
                        col1.push(temp.pop());
                    count++;
                    recordMove(COL6,COL1,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(0);
                    x = 0;
                    y = 0;
                }
            }
            else if(col2Clicked())
            {
                if(canPutOnTop(col6,col2,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col6.pop());
                    while(temp.size()>0)
                        col2.push(temp.pop());
                    count++;
                    recordMove(COL6,COL2,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(col6,col3,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col6.pop());
                    while(temp.size()>0)
                        col3.push(temp.pop());
                    count++;
                    recordMove(COL6,COL3,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(col6,col4,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col6.pop());
                    while(temp.size()>0)
                        col4.push(temp.pop());
                    count++;
                    recordMove(COL6,COL4,num+1);
                    num = 0;

                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(col6,col5,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col6.pop());
                    while(temp.size()>0)
                        col5.push(temp.pop());
                    count++;
                    recordMove(COL6,COL5,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked(numExtraCards))
            {
                if(sameCard)
                {
                    Stack<Card> temp = new Stack<Card>();
                    if(canPutUp(col6,spadesPile)&& numExtraCards==0)
                    {
                        count++;
                        spadesPile.push(col6.pop());
                        score+=3;
                        recordMove(COL6,SPADESPILE);
                    }
                    else if(canPutUp(col6,heartsPile)&& numExtraCards==0)
                    {
                        heartsPile.push(col6.pop());
                        count++;
                        score+=3;
                        recordMove(COL6,HEARTSPILE);
                    }
                    else if(canPutUp(col6,clubsPile)&& numExtraCards==0)
                    {
                        count++;
                        clubsPile.push(col6.pop());
                        score+=3;
                        recordMove(COL6,CLUBSPILE);
                    }
                    else if(canPutUp(col6,diamondsPile)&& numExtraCards==0)
                    {
                        count++;
                        diamondsPile.push(col6.pop());
                        score+=3;
                        recordMove(COL6,DIAMONDSPILE);
                    }
                    else if(canPutOnTop(col6,col1,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col6.pop());
                        while(temp.size()>0)
                            col1.push(temp.pop());
                        count++;
                        recordMove(COL6,COL1,numExtraCards+1);
                    }
                    else if(canPutOnTop(col6,col2,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col6.pop());
                        while(temp.size()>0)
                            col2.push(temp.pop());
                        count++;
                        recordMove(COL6,COL2,numExtraCards+1);
                    }
                    else if(canPutOnTop(col6,col3,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col6.pop());
                        while(temp.size()>0)
                            col3.push(temp.pop());
                        count++;
                        recordMove(COL6,COL3,numExtraCards+1);
                    }
                    else if(canPutOnTop(col6,col4,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col6.pop());
                        while(temp.size()>0)
                            col4.push(temp.pop());
                        count++;
                        recordMove(COL6,COL4,numExtraCards+1);
                    }
                    else if(canPutOnTop(col6,col5,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col6.pop());
                        while(temp.size()>0)
                            col5.push(temp.pop());
                        count++;
                        recordMove(COL6,COL5,numExtraCards+1);
                    }
                    else if(canPutOnTop(col6,col7,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col6.pop());
                        while(temp.size()>0)
                            col7.push(temp.pop());
                        count++;
                        recordMove(COL6,COL6,numExtraCards+1);
                    }
                    
                    numExtraCards = 0;
                    x = 0;
                    y = 0;
                    setColsFalse();
                    
                    
                
                }
                else
                {
                    int a = getEndingUpCardLoc(5).getA();
                    int b = getEndingUpCardLoc(5).getB();
                    if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
                    {
                        numExtraCards = 0;
                       
                    }
                    for(int i = 1; i<col6.size();i++)
                    {
                        b-=17;
                        if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
                        {
                            numExtraCards = i;
                            //System.out.println("Moving " + i + 1 + " cards");
                            
                        }
                    }
                    x = 0;
                    y = 0;
                }
                
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(col6,col7,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col6.pop());
                    while(temp.size()>0)
                        col7.push(temp.pop());
                    count++;
                    recordMove(COL6,COL7,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(6);
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked())
            {
                if(canPutUp(col6,spadesPile) && col6.peek().getSuit().equalsIgnoreCase("Spades")&&  numExtraCards==0)
                {
                    spadesPile.push(col6.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL6,SPADESPILE);
                }
                else if(spadesPile.size()!=0)
                {
                    setColsFalse();
                    spadesPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(heartsPileClicked())
            {
                if(canPutUp(col6,heartsPile) && col6.peek().getSuit().equalsIgnoreCase("Hearts")&&  numExtraCards==0)
                {
                    heartsPile.push(col6.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL6,HEARTSPILE);
                }
                else if(heartsPile.size()!=0)
                {
                    setColsFalse();
                    heartsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(clubsPileClicked())
            {
                if(canPutUp(col6,clubsPile) && col6.peek().getSuit().equalsIgnoreCase("Clubs")&&  numExtraCards==0)
                {
                    clubsPile.push(col6.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL6,CLUBSPILE);
                }
                else if(clubsPile.size()!=0)
                {
                    setColsFalse();
                    clubsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(diamondsPileClicked())
            {
                if(canPutUp(col6,diamondsPile) && col6.peek().getSuit().equalsIgnoreCase("Diamonds")&&  numExtraCards==0)
                {
                    diamondsPile.push(col6.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                    recordMove(COL6,DIAMONDSPILE);
                }
                else if(diamondsPile.size()!=0)
                {
                    setColsFalse();
                    diamondsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
        }
        else if(col7Clicked)
        {
            num = numExtraCards;
            if(col7.size() == 0)//if col1 has no up cards
            {
                if(col7Down.size() != 0)
                {
                    col7.push(col7Down.pop());
                    col7Clicked = false;
                    recordMove(COL7);
                    //setColsFalse();
                }
                else //if col1 has no up cards and no down cards
                {
                    //col1Clicked = false;
                }
            }
            else if(col1Clicked())
            {
                if(canPutOnTop(col7,col1,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col7.pop());
                    while(temp.size()>0)
                        col1.push(temp.pop());
                    count++;
                    recordMove(COL7,COL1,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(0);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col2Clicked())
            {
                if(canPutOnTop(col7,col2,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col7.pop());
                    while(temp.size()>0)
                        col2.push(temp.pop());
                    count++;
                    recordMove(COL7,COL2,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(col7,col3,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col7.pop());
                    while(temp.size()>0)
                        col3.push(temp.pop());
                    count++;
                    recordMove(COL7,COL3,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(col7,col4,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col7.pop());
                    while(temp.size()>0)
                        col4.push(temp.pop());
                    count++;
                    recordMove(COL7,COL4,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(col7,col5,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col7.pop());
                    while(temp.size()>0)
                        col5.push(temp.pop());
                    count++;
                    recordMove(COL7,COL5,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(col7,col6,num) && numExtraCards==0)
                {
                    Stack<Card> temp = new Stack<Card>();
                    for(int i = 0; i<=num;i++)
                        temp.push(col7.pop());
                    while(temp.size()>0)
                        col6.push(temp.pop());
                    count++;
                    recordMove(COL7,COL6,num+1);
                    num = 0;
                    numExtraCards = 0;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    setColTrue(5);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked(numExtraCards))
            {
                if(sameCard)
                {
                    Stack<Card> temp = new Stack<Card>();
                    if(canPutUp(col7,spadesPile)&& numExtraCards==0)
                    {
                        spadesPile.push(col7.pop());
                        count++;
                        score+=3;
                        recordMove(COL7,SPADESPILE);
                    }
                    else if(canPutUp(col7,heartsPile)&& numExtraCards==0)
                    {
                        heartsPile.push(col7.pop());
                        count++;
                        score+=3;
                        recordMove(COL7,HEARTSPILE);
                    }
                    else if(canPutUp(col7,clubsPile)&& numExtraCards==0)
                    {
                        clubsPile.push(col7.pop());
                        count++;
                        score+=3;
                        recordMove(COL7,CLUBSPILE);
                    }
                    else if(canPutUp(col7,diamondsPile)&& numExtraCards==0)
                    {
                        diamondsPile.push(col7.pop());
                        count++;
                        score+=3;
                        recordMove(COL7,DIAMONDSPILE);
                    }
                    else if(canPutOnTop(col7,col1,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col7.pop());
                        while(temp.size()>0)
                            col1.push(temp.pop());
                        count++;
                        recordMove(COL7,COL1,numExtraCards+1);
                    }
                    else if(canPutOnTop(col7,col2,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col7.pop());
                        while(temp.size()>0)
                            col2.push(temp.pop());
                        count++;
                       recordMove(COL7,COL2,numExtraCards+1);
                    }
                    else if(canPutOnTop(col7,col3,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col7.pop());
                        while(temp.size()>0)
                            col3.push(temp.pop());
                        count++;
                        recordMove(COL7,COL3,numExtraCards+1);
                    }
                    else if(canPutOnTop(col7,col4,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col7.pop());
                        while(temp.size()>0)
                            col4.push(temp.pop());
                        count++;
                        recordMove(COL7,COL4,numExtraCards+1);
                    }
                    else if(canPutOnTop(col7,col5,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col7.pop());
                        while(temp.size()>0)
                            col5.push(temp.pop());
                        count++;
                        recordMove(COL7,COL5,numExtraCards+1);
                    }
                    else if(canPutOnTop(col7,col6,numExtraCards))
                    {
                        for(int i = 0; i<=num;i++)
                            temp.push(col7.pop());
                        while(temp.size()>0)
                            col6.push(temp.pop());
                        count++;
                        recordMove(COL7,COL6,numExtraCards+1);
                    }
                    
                    numExtraCards = 0;
                    x = 0;
                    y = 0;
                    setColsFalse();
                    
                    
                
                }
                else
                {
                    int a = getEndingUpCardLoc(6).getA();
                    int b = getEndingUpCardLoc(6).getB();
                    if(x>=a && (x<=a+70) && y>=b && y<=(b+94))
                    {
                        numExtraCards = 0;
                       
                    }
                    for(int i = 1; i<col7.size();i++)
                    {
                        b-=17;
                        if(x>=a && (x<=a+70) && y>=b && y<=(b+17))
                        {
                            numExtraCards = i;
                            //System.out.println("Moving " + i + 1 + " cards");
                            
                        }
                    }
                    x = 0;
                    y = 0;
                }
                
            }
            else if(spadesPileClicked())
            {
                if(canPutUp(col7,spadesPile) && col7.peek().getSuit().equalsIgnoreCase("Spades")&&  numExtraCards==0)
                {
                    spadesPile.push(col7.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                }
                else if(spadesPile.size()!=0)
                {
                    setColsFalse();
                    spadesPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(heartsPileClicked())
            {
                if(canPutUp(col7,heartsPile) && col7.peek().getSuit().equalsIgnoreCase("Hearts")&&  numExtraCards==0)
                {
                    heartsPile.push(col7.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                }
                else if(heartsPile.size()!=0)
                {
                    setColsFalse();
                    heartsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(clubsPileClicked())
            {
                if(canPutUp(col7,clubsPile) && col7.peek().getSuit().equalsIgnoreCase("Clubs")&&  numExtraCards==0)
                {
                    clubsPile.push(col7.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                }
                else if(clubsPile.size()!=0)
                {
                    setColsFalse();
                    clubsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
            else if(diamondsPileClicked())
            {
                if(canPutUp(col7,diamondsPile) && col7.peek().getSuit().equalsIgnoreCase("Diamonds")&&  numExtraCards==0)
                {
                    diamondsPile.push(col7.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                    score+=3;
                }
                else if(diamondsPile.size()!=0)
                {
                    setColsFalse();
                    diamondsPileClicked = true;
                    x = 0;
                    y = 0;
                    
                }
            }
        }
        else if(spadesPileClicked)
        {
            if(col1Clicked())
            {
                if(canPutOnTop(spadesPile,col1)&&  numExtraCards==0)
                {
                    col1.push(spadesPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    spadesPileClicked = false;
                    setColTrue(0);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col2Clicked())
            {
                if(canPutOnTop(spadesPile,col2)&&  numExtraCards==0)
                {
                    col2.push(spadesPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    spadesPileClicked = false;
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(spadesPile,col3)&&  numExtraCards==0)
                {
                    col3.push(spadesPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    spadesPileClicked = false;
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(spadesPile,col4)&&  numExtraCards==0)
                {
                    col4.push(spadesPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    spadesPileClicked = false;
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(spadesPile,col5)&&  numExtraCards==0)
                {
                    col5.push(spadesPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    spadesPileClicked = false;
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(spadesPile,col6)&&  numExtraCards==0)
                {
                    col6.push(spadesPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    spadesPileClicked = false;
                    setColTrue(5);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(spadesPile,col7)&&  numExtraCards==0)
                {
                    col7.push(spadesPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    spadesPileClicked = false;
                    setColTrue(6);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked())
            {
                if(canPutOnTop(spadesPile,col1))
                {
                    col1.push(spadesPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(spadesPile,col2))
                {
                    col2.push(spadesPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(spadesPile,col3))
                {
                    col3.push(spadesPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(spadesPile,col4))
                {
                    col4.push(spadesPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(spadesPile,col5))
                {
                    col5.push(spadesPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(spadesPile,col6))
                {
                    col6.push(spadesPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(spadesPile,col7))
                {
                    col7.push(spadesPile.pop());
                    count++;
                    score-=3;
                }
                spadesPileClicked = false;
                x = 0;
                y = 0;
            }
            else if(heartsPileClicked() && heartsPile.size()!=0)
            {
                spadesPileClicked = false;
                heartsPileClicked = true;
                x = 0;
                y = 0;
            }
            else if(clubsPileClicked() && clubsPile.size()!=0)
            {
                spadesPileClicked = false;
                clubsPileClicked = true;
                x = 0;
                y = 0;
            }
            else if(diamondsPileClicked() && diamondsPile.size()!=0)
            {
                spadesPileClicked = false;
                diamondsPileClicked = true;
                x = 0;
                y = 0;
            }
        }
        else if(heartsPileClicked)
        {
            if(col1Clicked())
            {
                if(canPutOnTop(heartsPile,col1)&&  numExtraCards==0)
                {
                    col1.push(heartsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    heartsPileClicked = false;
                    setColTrue(0);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col2Clicked())
            {
                if(canPutOnTop(heartsPile,col2)&&  numExtraCards==0)
                {
                    col2.push(heartsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    heartsPileClicked = false;
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(heartsPile,col3)&&  numExtraCards==0)
                {
                    col3.push(heartsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    heartsPileClicked = false;
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(heartsPile,col4)&&  numExtraCards==0)
                {
                    col4.push(heartsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    heartsPileClicked = false;
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(heartsPile,col5)&&  numExtraCards==0)
                {
                    col5.push(heartsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    heartsPileClicked = false;
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(heartsPile,col6)&&  numExtraCards==0)
                {
                    col6.push(heartsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    heartsPileClicked = false;
                    setColTrue(5);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(heartsPile,col7)&&  numExtraCards==0)
                {
                    col7.push(heartsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    heartsPileClicked = false;
                    setColTrue(6);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked() && spadesPile.size()!=0)
            {
                 spadesPileClicked = true;
                 heartsPileClicked = false;
                 x = 0;
                 y = 0;
            }
            else if(heartsPileClicked())
            {
                if(canPutOnTop(heartsPile,col1))
                {
                    col1.push(heartsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(heartsPile,col2))
                {
                    col2.push(heartsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(heartsPile,col3))
                {
                    col3.push(heartsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(heartsPile,col4))
                {
                    col4.push(heartsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(heartsPile,col5))
                {
                    col5.push(heartsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(heartsPile,col6))
                {
                    col6.push(heartsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(heartsPile,col7))
                {
                    col7.push(heartsPile.pop());
                    count++;
                    score-=3;
                }
                heartsPileClicked = false;
                x = 0;
                y = 0;
            }
            else if(clubsPileClicked() && clubsPile.size()!=0)
            {
                heartsPileClicked = false;
                clubsPileClicked = true;
                x = 0;
                y = 0;
            }
            else if(diamondsPileClicked() && diamondsPile.size()!=0)
            {
                heartsPileClicked = false;
                diamondsPileClicked = true;
                x = 0;
                y = 0;
            }
        }
        else if(clubsPileClicked)
        {
            if(col1Clicked())
            {
                if(canPutOnTop(clubsPile,col1)&&  numExtraCards==0)
                {
                    col1.push(clubsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    clubsPileClicked = false;
                    setColTrue(0);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col2Clicked())
            {
                if(canPutOnTop(clubsPile,col2)&&  numExtraCards==0)
                {
                    col2.push(clubsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    clubsPileClicked = false;
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(clubsPile,col3)&&  numExtraCards==0)
                {
                    col3.push(clubsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    clubsPileClicked = false;
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(clubsPile,col4)&&  numExtraCards==0)
                {
                    col4.push(clubsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    clubsPileClicked = false;
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(clubsPile,col5)&&  numExtraCards==0)
                {
                    col5.push(clubsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    clubsPileClicked = false;
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(clubsPile,col6)&&  numExtraCards==0)
                {
                    col6.push(clubsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    clubsPileClicked = false;
                    setColTrue(5);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(clubsPile,col7)&&  numExtraCards==0)
                {
                    col7.push(clubsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    clubsPileClicked = false;
                    setColTrue(6);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked() && spadesPile.size()!=0)
            {
                clubsPileClicked = false;
                spadesPileClicked = true;
                x = 0;
                y = 0;
            }
            else if(heartsPileClicked() && heartsPile.size()!=0)
            {
                clubsPileClicked = false;
                heartsPileClicked = true;
                x = 0;
                y = 0;
            }
            else if(clubsPileClicked())
            {
                if(canPutOnTop(clubsPile,col1))
                {
                    col1.push(clubsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(clubsPile,col2))
                {
                    col2.push(clubsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(clubsPile,col3))
                {
                    col3.push(clubsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(clubsPile,col4))
                {
                    col4.push(clubsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(clubsPile,col5))
                {
                    col5.push(clubsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(clubsPile,col6))
                {
                    col6.push(clubsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(clubsPile,col7))
                {
                    col7.push(clubsPile.pop());
                    count++;
                    score-=3;
                }
                clubsPileClicked = false;
                x = 0;
                y = 0;
            }
            else if(diamondsPileClicked() && diamondsPile.size()!=0)
            {
                clubsPileClicked = false;
                diamondsPileClicked = true;
                x = 0;
                y = 0;
            }
        }
        else if(diamondsPileClicked)
        {
            if(col1Clicked())
            {
                if(canPutOnTop(diamondsPile,col1)&&  numExtraCards==0)
                {
                    col1.push(diamondsPile.pop());
                    count++;
                    setColsFalse();
                    x = 0;
                    y = 0;
                }
                else
                {
                    diamondsPileClicked = false;
                    setColTrue(0);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col2Clicked())
            {
                if(canPutOnTop(diamondsPile,col2)&&  numExtraCards==0)
                {
                    col2.push(diamondsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    diamondsPileClicked = false;
                    setColTrue(1);
                    x = 0;
                    y = 0;
                }
            }
            else if(col3Clicked())
            {
                if(canPutOnTop(diamondsPile,col3)&&  numExtraCards==0)
                {
                    col3.push(diamondsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    diamondsPileClicked = false;
                    setColTrue(2);
                    x = 0;
                    y = 0;
                }
            }
            else if(col4Clicked())
            {
                if(canPutOnTop(diamondsPile,col4)&&  numExtraCards==0)
                {
                    col4.push(diamondsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    diamondsPileClicked = false;
                    setColTrue(3);
                    x = 0;
                    y = 0;
                }
            }
            else if(col5Clicked())
            {
                if(canPutOnTop(diamondsPile,col5)&&  numExtraCards==0)
                {
                    col5.push(diamondsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    diamondsPileClicked = false;
                    setColTrue(4);
                    x = 0;
                    y = 0;
                }
            }
            else if(col6Clicked())
            {
                if(canPutOnTop(diamondsPile,col6)&&  numExtraCards==0)
                {
                    col6.push(diamondsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    diamondsPileClicked = false;
                    setColTrue(5);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(col7Clicked())
            {
                if(canPutOnTop(diamondsPile,col7)&&  numExtraCards==0)
                {
                    col7.push(diamondsPile.pop());
                    setColsFalse();
                    count++;
                    x = 0;
                    y = 0;
                }
                else
                {
                    diamondsPileClicked = false;
                    setColTrue(6);
                    
                    x = 0;
                    y = 0;
                }
            }
            else if(spadesPileClicked()&& spadesPile.size()!=0)
            {
                diamondsPileClicked = false; 
                spadesPileClicked = true;
                x = 0;
                y = 0;
            }
            else if(heartsPileClicked()&& heartsPile.size()!=0)
            {
                diamondsPileClicked = false; 
                heartsPileClicked = true;
                x = 0;
                y = 0;
            }
            else if(clubsPileClicked() && clubsPile.size()!=0)
            {
                diamondsPileClicked = false; 
                clubsPileClicked = true;
                x = 0;
                y = 0;
            }
            else if(diamondsPileClicked())
            {
                if(canPutOnTop(diamondsPile,col1))
                {
                    col1.push(diamondsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(diamondsPile,col2))
                {
                    col2.push(diamondsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(diamondsPile,col3))
                {
                    col3.push(diamondsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(diamondsPile,col4))
                {
                    col4.push(diamondsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(diamondsPile,col5))
                {
                    col5.push(diamondsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(diamondsPile,col6))
                {
                    col6.push(diamondsPile.pop());
                    count++;
                    score-=3;
                }
                else if(canPutOnTop(diamondsPile,col7))
                {
                    col7.push(diamondsPile.pop());
                    count++;
                    score-=3;
                }
                    
                diamondsPileClicked = false; 
                x = 0;
                y = 0;
            }
        }
        //if(x!=0 && y!=0)
                //System.out.println("x,y: (" + x+","+y+")");
        
        repaint();
    }
    /**
     * This method is called from outside this class in order to stop the game (or start the game)
     * Specifically is called from Frame class when the user chooses to start a new game, and thus the current game must be terminated.
     * Sets run to false in order to break the while loop in the run() method which will end the current game and start a new game
     * @param x The boolean that run will be set to. Should be false if the user wishes to end the current game
     */
    public void run(boolean x)
    {
        run = x;
    }
    /**
     * Runs the solitaire game, shuffling the deck, dealing the cards and running until the user has won the game (or closed out of the game).
     * After the game is over, it will record the game's data and will reset the game
     */
    public void run()
    {
        deck.shuffle();
        deal();
        repaint();
        int x = 0;
        
        while(run)
        {
            if(x==0 && count >0) //this will start the timer as soon as the first move has been made. x must = 0 because it should only start the timer once
            {
                x++; 
                timer = System.currentTimeMillis();
            }
            move();
           
            
            //run = false;
            if(win())
                run = false;
            
            
        }
        String[] options= {"Yes","No"};
        if(win())
        {
            recordGameLog(true);
            
            int restart = JOptionPane.showOptionDialog(null,"You Win!!! Play again?","Restart",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
            if(restart==0)
            {            
                reset();         
                
                
            }
            else
            {
                System.exit(0);
            }
        }
        reset(); //there are only two ways to break the above while loop (without terminating the game), either winning the game or the user choosing to reset the game. Thus, if it gets to this part of the code, the user must have chosen to reset the game
        

    }
    /**
     * Resets the Solitaire game, by instantiating a new Frame and setting the previously frame to invisible
     */
    public void reset()
    {
        JFrame frame = (JFrame) SwingUtilities.getRoot(this);
        frame.setVisible(false);
        Frame f = new Frame();
            
    }
}
