import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 *  
 */
public class PlyQuartoHound extends ComputerPlayer
{
    public PlyQuartoHound(String name)
    {
        super(name);

    }
    
    /**
     * for each available Space, place a piece.  If quarto 
     * is made then return that space as the one to place the piece
     * otherwise remove that piece and continue.
     * 
     * If no quartos, then just use superclass's method to randomly 
     * select a space
     */
    public Space selectSpaceForPlacement(Board b, Piece p)
    {

        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        for (Space s : availableSpaces)
        {
            s.receivePiece(p);
            if (b.checkForQuarto())
                return s;
            else
                s.removePiece();
            
        }
        
        //otherwise do as the superclass
        return super.selectSpaceForPlacement( b,  p);
               
         
    }    
}

