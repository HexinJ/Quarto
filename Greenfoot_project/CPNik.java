import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class CPNik here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPNik extends ComputerPlayer
{
  public CPNik(String name)
    {
        super(name);

    }
 
    public Piece selectPieceToPlace(Board b)
    {
        ArrayList<Piece> availablePieces=b.getAvailablePieces();
        ArrayList<Space> availableSpaces=b.getAvailableSpaces();
        for (Piece i:availablePieces){
            boolean safe=true;
            for (Space j:availableSpaces){
                j.receivePiece(i);
                if(b.checkForQuarto()){
                    safe=false;
                    j.removePiece();}
                else
                    j.removePiece();
            }
            if(safe)
                return i;
        }

        return super.selectPieceToPlace(b);
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
        Space randSpace = super.selectSpaceForPlacement( b,  p);
        randSpace.receivePiece(p);

        Board clonedBoard = b.clone();
        randSpace.removePiece();
        int numTrys = 10;
        while (IsQuartoNext(clonedBoard) && numTrys > 0)
        {

            //otherwise do as the superclass
            numTrys--;
            randSpace = super.selectSpaceForPlacement( b,  p);
            randSpace.receivePiece(p);

            clonedBoard = b.clone();
            randSpace.removePiece();
        }
        return randSpace;

    }    
    private boolean IsQuartoNext (Board b) 
    {
        ArrayList<Piece> availablePieces=b.getAvailablePieces();
        ArrayList<Space> availableSpaces=b.getAvailableSpaces();
        for (Piece i:availablePieces){
            boolean safe=true;
            for (Space j:availableSpaces){
                j.receivePiece(i);
                if(b.checkForQuarto()){
                    return true;
                }
            }

        }
        return false;
    }
}
