import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * 
 */
public class ComputerPlayer extends Player
{
    public ComputerPlayer(String name)
    {
        super(name, true);

    }

    /**
     * Randomly select a piece from the AvailablePieces in the board
     */
    public Piece selectPieceToPlace(Board b)
    {
        ArrayList<Piece> availablePieces = b.getAvailablePieces();
        if (availablePieces.size() > 0)
            return availablePieces.get((int)(Math.random()*availablePieces.size()));
        else
            return null;

    }
    /**
     * Randomly select an empty Space on the board
     */
    public Space selectSpaceForPlacement(Board b, Piece p)
    {

        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
    
        if (availableSpaces.size() > 0)
            return availableSpaces.get((int)(Math.random()*availableSpaces.size()));
        else
            return null;        
         
    }    
}
