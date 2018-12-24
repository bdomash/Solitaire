import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
public class Settings
{
    public static void main(String[] args){
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
        boolean selected3 = true;
        try{
            filereader = new FileReader("settings.txt");
            reader = new BufferedReader(filereader);
            
            line = reader.readLine();
            if(Integer.parseInt(line)==3)
                selected3 = true;
            else
                selected3 = false;
        }
        catch(IOException e){
            System.out.println("There was a problem reading settings.txt");
        }
       
        if(selected3)
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
        button3.setSelected(true);
        
        ButtonGroup group2 = new ButtonGroup();
        group2.add(button3);
        group2.add(button4);
        group2.add(button7);
        
        
        
        
        

        //panel.add(add(new JLabel("")));
        panel.add(button3);
        panel.add(button4);
        panel.add(button7);
        //panel.setFocusable(true);
        /*
        panel.add(new JLabel("Deck Type"));
        panel.add(new JLabel(""));
        JRadioButton button5 = new JRadioButton("Blue");
        JRadioButton button6 = new JRadioButton("Red");
        panel.add(new JLabel(""));
        
        ButtonGroup group3 = new ButtonGroup();
        group3.add(button5);
        group3.add(button6);
        
        //panel.add(add(new JLabel("")));
        panel.add(button5);
        panel.add(button6);
        //panel.setFocusable(true);
        */
       
        panel.add(new JLabel(""));
        panel.add(new JLabel("Deck Type"));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        
        HashMap<String,String> map = new HashMap<String,String>();
        
        
        String[] arr = {"Blue Standard","Black Castle", "Dark Blue Fish", "Light Blue Fish", "Hands", "Black Ivy", "Blue Ivy","Palm Tree",
            "Robot","Roses","Sea Shell","Red Weave","Yellow Weave"};
        JComboBox<String> backs = new JComboBox<String>(arr);
        panel.add(backs);
        ImageIcon icon = new ImageIcon("1Spades.png");
        JOptionPane.showMessageDialog(null, panel, "Settings", 0, icon);
       
       
       
        
        
    }
}
