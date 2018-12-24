
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import java.util.*;
import javax.swing.JFrame;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import sun.audio.*;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
/**
 * @author Brandon Domash
 * Creates the frame for the Solitaire game
 */
public class Frame extends JFrame 
{
    private static Frame f;
    private JMenuItem undo;
    private Solitaire s;
    
    /**
     * Creates a new frame
     */
    public Frame()   
    {
        s = new Solitaire();
        //undo = new JMenuItem("Undo");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//do nothing on close only because what to do on close is getting overridden below
        addMenuBar();
        ImageIcon icon = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("1Spades.png"))); //Ace of Spades is the icon for the game
        setIconImage(icon.getImage());
        if(s.getGameType())
            setTitle("Solitaire Deal 3");   
        else
            setTitle("Solitaire Deal 1");   
        addWindowListener(new java.awt.event.WindowAdapter() {
            /**
             * Overrides what happens when the x is clicked in the upper-right corner. Will prompt user with an "Are You Sure" message before exiting
             * @param windowEvent The windowEvent
             */
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                String[] options= {"Yes","No"};
                int exit = 0;
                if(s.elapsedTime()!=0)
                    exit = JOptionPane.showOptionDialog(null,"Are you sure you want to exit? Stats will be recorded","Exit?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                else
                    System.exit(0);
                    //exit = JOptionPane.showOptionDialog(null,"Are you sure you want to exit? Stats will NOT be recorded","Exit?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if(exit==0)
                {
                    //record stats
                    if(s.elapsedTime()!=0)
                        s.recordGameLog(false);
                    System.exit(0);
                }
                else
                {
                    //do nothing
                }
            }
        });
        
        init();
    }
    /**
     * Adds the menu bar to the top of the JFrame with "Game" and "Help" tabs. Both tabs have various selections as well
     */
    public void addMenuBar()
    {
        JMenuBar menubar = new JMenuBar();
        this.setJMenuBar(menubar);
        
        //cols
        JMenu game = new JMenu("Game");
        JMenu help = new JMenu("Help");
        
        //add cols
        menubar.add(game);
        menubar.add(help);
        
        //options
        JMenuItem exit = new JMenuItem("Exit                 ⌘W");
        JMenuItem newGame = new JMenuItem("New Game      ⌘N");
        JMenuItem helpOption = new JMenuItem("View Help      ⌘H");
        JMenuItem about = new JMenuItem("About            ⌘A");
        JMenuItem stats = new JMenuItem("Statistics         ⌘F");
        JMenuItem undo = new JMenuItem("Undo               ⌘Z");
        JMenuItem settings = new JMenuItem("Settings          ⌘S");
        
        //adds options
        game.add(newGame);
        game.add(settings);
        game.add(undo);
        game.add(stats);
        game.add(exit);
        
        help.add(helpOption);
        help.add(about);
        
        /**
         * Determines what to do if the exit button is selected
         */
        class ExitAction implements ActionListener{
            /**
             * Determines what to do if the exit action is selected
             * @param e The ActionEvent
             */
            public void actionPerformed(ActionEvent e)
            {
                /*
                String[] options= {"Yes","No"};
                 
                int exit = 0;
                if(s.elapsedTime()!=0)
                    exit = JOptionPane.showOptionDialog(null,"Are you sure you want to exit? Stats will be recorded","Exit?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                else
                    System.exit(0);
                    //exit = JOptionPane.showOptionDialog(null,"Are you sure you want to exit? Stats will NOT be recorded","Exit?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                
                if(exit==0)
                {
                    //record stats
                    if(s.elapsedTime()!=0)
                        s.recordGameLog(false);
                    System.exit(0);
                }
                else
                {
                    //do nothing
                }
                */
               s.quitAction();
            }
        }
        /**
         * Determines what to do if the new game button is selected
         */
        class NewGame implements ActionListener{
            /**
             * Stops the current game and starts a new game when the new game button is selected
             * @param e The ActionEvent
             */
            public void actionPerformed(ActionEvent e)
            {
                 /*
                 String[] options= {"Yes","No"};
                 int newGame = 0;
                 if(s.elapsedTime()!=0)
                    newGame = JOptionPane.showOptionDialog(null,"Are you sure you want to start a new game? Stats will be recorded","Restart?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
                 else //no elapsed time
                 {
                     s.run(false);
                     if(s.elapsedTime()!=0)
                        s.recordGameLog(false);
                 }   
                 
                    //newGame = JOptionPane.showOptionDialog(null,"Are you sure you want to start a new game? Stats will NOT be recorded","Restart?",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
                 if(newGame == 0)
                 {
                     s.run(false);
                     if(s.elapsedTime()!=0)
                        s.recordGameLog(false);
                 }
                 else if(newGame == 1)
                 {
                     //do nothing
                 }
                 */
                 s.newGame();
            }
        }
        /**
         * Determines what to do when the help button is selected
         */
        class HelpAction implements ActionListener
        {
            /**
             * Displays directions for the game when the help button is selected
             * @param e The ActionEvent
             */
            public void actionPerformed(ActionEvent e)
            {
                s.helpAction();
            }
        }
        /**
         * Determines what to do when the about button is selected
         */
        class AboutAction implements ActionListener{
            /**
             * Displays some useless information about the game
             * @param e The ActionEvent
             */
            public void actionPerformed(ActionEvent e)
            {
                s.aboutAction();
            }
        }
        /**
         * Determines what to when the stats button is selected
         */
        class StatsAction implements ActionListener{
            /**
             * Calls the displayStats() method from the Solitaire class when the stats button is selected
             */
            public void actionPerformed(ActionEvent e)
            {
                s.displayStats();
                //JOptionPane.showMessageDialog(null,"Stats");
            }
            
        }
        class UndoAction implements ActionListener{
            public void actionPerformed(ActionEvent e)
            {
                s.undoMove();
                
            }
        }
        class SettingsAction implements ActionListener{
            public void actionPerformed(ActionEvent e)
            {
                s.settings();
                
            }
        }
        //adds each of the actionlisteners to the buttons
        
        newGame.addActionListener(new NewGame());
        exit.addActionListener(new ExitAction());
        helpOption.addActionListener(new HelpAction());
        about.addActionListener(new AboutAction());
        stats.addActionListener(new StatsAction());
        undo.addActionListener(new UndoAction());
        settings.addActionListener(new SettingsAction());
        //undo.setEnabled(false);
        
    }
    
    /**
     * Adds the screen JPanel to the frame and proceeds to run the solitaire game. Centers the frame and does the dirty work to make the frame look good.
     */
    public void init()
    {
        setLayout(new GridLayout(1,1,0,0));  
        //setLayout(new BorderLayout());
               
        addKeyListener(s);
        
        add(s);  
        
        pack();
        setBounds(0,0,700,580);      
        
        setFocusable(true);
        requestFocusInWindow();
        setLocationRelativeTo(null);    //Centers frame on middle of screen        
        setResizable(false);
       
        setVisible(true);        
        s.run();     
       
        
    }
    /**
     * Resets the frame
     */
    public static void reset()      
    {
        f = new Frame();        
    }
   
    /**
     * The main method
     */
    public static void main(String[] args)
    throws Exception
    {
          
        Frame f = new Frame();       
        
    }    
}