import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class CPGloriaMaygala1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPGloriaMaygala1 extends ComputerPlayer
{
  public CPGloriaMaygala1(String name)
    {
        super(name);

    }
        /**
     * avoid giving a piece that will make a Quarto.
     */
    public Piece selectPieceToPlace(Board b)
    {
        ArrayList<Piece> availablePieces = b.getAvailablePieces();
        if (availablePieces.size() > 0){
            for (int i = 0; i < availablePieces.size(); i++){
                if(!isQuartoPiece(availablePieces.get(i), b)) return availablePieces.get(i);
            }
            return availablePieces.get(0);
        }
        return null;
    }

    /**
     * place where there's a possibility of Quarto.
     */
    public Space selectSpaceForPlacement(Board b, Piece p)
    {
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        for (Space s : availableSpaces)
        {
            s.receivePiece(p);
            if (isDangerousSpace(s, b))
                return s;
            else
                s.removePiece();

        }
        return super.selectSpaceForPlacement( b,  p);     
    }    

    /**
     * return an arraylist of pieces that will make a Quarto.
     */
    public ArrayList<Piece> PiecesThatMakeQuarto(Board b){
        ArrayList<Piece> availablePieces = b.getAvailablePieces();
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        ArrayList<Piece> output = new ArrayList<Piece>();
        for (Piece p : availablePieces){
            for (Space s : availableSpaces){
                s.receivePiece(p);
                if (b.checkForQuarto()){
                    output.add(p);
                    s.removePiece();
                    break;
                }
                s.removePiece();
            }
        }
        return output;
    }

    /**
     * return true if the piece can make a Quarto.
     */
    public boolean isQuartoPiece(Piece p, Board b){
        if (PiecesThatMakeQuarto(b) != null){
            for (Piece q : PiecesThatMakeQuarto(b)){
                if (p == q) return true;
            } 
        }
        return false;
    }
    
    /**
     * return true if a piece from availablePieces can make a Quarto if place in the space.
     */
    public boolean isDangerousSpace(Space s, Board b){
        if (b.getAvailablePieces() != null){
            for (Piece p: b.getAvailablePieces()){
                s.receivePiece(p);
                if (b.checkForQuarto()){
                    s.removePiece();
                    return true;
                }
            }
        }
        return false;
    }
}
