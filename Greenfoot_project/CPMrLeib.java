import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class CPMrLeib here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPMrLeib extends ComputerPlayer
{
    public CPMrLeib(String name)
    {
        super(name);

    }

    public Piece selectPieceToPlace(Board b)
    {
        ArrayList<Piece> availablePieces = b.getAvailablePieces();
        if (availablePieces.size() == 0) return null;
        Piece firstPiece = availablePieces.get(0);
        for (int i = availablePieces.size() - 1; i >= 0; i--)
        {
            Piece p = availablePieces.get(i);
            ArrayList<Space> availableSpaces = b.getAvailableSpaces();
            for (Space s : availableSpaces)
            {
                s.receivePiece(p);
                boolean quarto = b.checkForQuarto();
                s.removePiece(); 
                if (quarto)
                {
                    availablePieces.remove(i);
                    break;
                }               
            }
        }
        if (availablePieces.size() > 0)
            return availablePieces.get((int)(Math.random()*availablePieces.size()));
        else
            return firstPiece;

       

    }

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

        Space blockSpace = spaceWithMostBlocking( p, b);
        if (blockSpace != null)
            return blockSpace;
        //otherwise do as the superclass
        return super.selectSpaceForPlacement( b,  p);

    } 

    public Space spaceWithMostBlocking(Piece p, Board b)
    {

        ArrayList<Line> blockableLines = linesWithEmpties(3, b);

        for (Line l : blockableLines)
        {
            Space s = emptySpaceInLine(l);
            s.receivePiece(p); 
            if (!b.checkForQuarto())
                return s;
            else
                s.removePiece();
        }

        return null;
    } 

    public Space emptySpaceInLine(Line l)
    {
        for (Space s : l.getSpaces())
        {
            if (s.isEmpty())
                return s;
        }
        return null;
    }

    public ArrayList<Line> linesWithEmpties (int numEmpty, Board b)
    {
        ArrayList<Line> lines = new ArrayList<Line>(); 
        for (Line l : b.getLines())
            if (numEmpties(l) == numEmpty)
                lines.add(l);
        return lines;
    }

    private int numEmpties(Line l)
    {
        int count = 0;
        for (Space s : l.getSpaces())
            if (s.isEmpty())
                count++;
        return count;
    }
}
