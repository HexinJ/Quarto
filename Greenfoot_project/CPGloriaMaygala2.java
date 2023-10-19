import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class CPGloriaMaygala2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPGloriaMaygala2 extends ComputerPlayer
{
  public CPGloriaMaygala2(String name)
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
     * place if it makes a Quarto. Otherwise place randomly.
     */
    public Space selectSpaceForPlacement(Board b, Piece p)
    {
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        for (Space s : availableSpaces){
            s.receivePiece(p);
            if (b.checkForQuarto())
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
     * return the symbol of a attribute that will make a Quarto in a line
     */
    public String dangerousAttribute(Line line){
        String output = ""; String temp = "";
        for (Space s : line.getSpaces()){
            if (s.getPiece() != null)
                temp += s.getPiece().toString();
        }
        int l = 0; int d = 0; int r = 0; int q = 0; int t = 0; int s = 0; int o = 0; int x = 0;
        for (int i = 0; i < temp.length() - 1; i++){
            if (temp.substring(i,i+1).equals("L")) l++; if (temp.substring(i,i+1).equals("D")) d++;
            if (temp.substring(i,i+1).equals("R")) r++; if (temp.substring(i,i+1).equals("Q")) q++;
            if (temp.substring(i,i+1).equals("T")) t++; if (temp.substring(i,i+1).equals("S")) s++;
            if (temp.substring(i,i+1).equals("O")) o++; if (temp.substring(i,i+1).equals("X")) x++;
        }
        if (l==3) output += "L"; if (d==3) output += "D"; if (r==3) output += "R"; if (q==3) output += "Q";
        if (t==3) output += "T"; if (s==3) output += "S"; if (o==3) output += "O"; if (x==3) output += "X";
        return output;
    }
}
