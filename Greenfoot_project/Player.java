import greenfoot.*;
import java.util.*;
/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends Actor 
{
    // instance variables - replace the example below with your own
    public final String name;
    private int wins = 0;
    private boolean isComputerPlayer;

    /**
     * Constructor for objects of class Player
     */
    public Player(String name, boolean isComputerPlayer)
    {
        name = name.trim();
        if (name.length() > 8) name = name.substring(0,8);
        this.name = name;
        this.isComputerPlayer = isComputerPlayer;
    }
 
    public String getName()
    {   return name; }
    
    public Piece selectPiece()
    {
        return null;
    }
    public void addWin()
    {   wins++; }
    public int getWins()
    {   return wins; }
    
    public void printDebugInfo()
    {
        
    }
}
