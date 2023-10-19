import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class CPFraniJulie here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPFraniJulie extends ComputerPlayer
{
    public CPFraniJulie(String name)
    {
        super(name);
        //System.out.println("Instantiating Frani and Julie's Computer Player");
    }

    public Piece selectPieceToPlace(Board b)
    {

        ArrayList<Piece> availablePieces = b.getAvailablePieces();

        if (availablePieces.size() > 0){
            for(int i = 0; i<availablePieces.size(); i++){
                if(!q(b, availablePieces.get(i)))
                    return availablePieces.get(i);

            }

            return availablePieces.get((int)(Math.random()*availablePieces.size()));
        }
        return null;
    }

    public boolean q(Board b, Piece p){
        ArrayList<Piece> availablePieces = b.getAvailablePieces();
        ArrayList<Piece> gp = new ArrayList<Piece>();
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();

        for(int i = 0; i<availablePieces.size(); i++){
            for(int j = 0; j<availableSpaces.size(); j++){
                availableSpaces.get(j).receivePiece(availablePieces.get(i));
                if(b.checkForQuarto()){
                    gp.add( availablePieces.get(i));

                    availableSpaces.get(j).removePiece();

                    break;
                }
                availableSpaces.get(j).removePiece();  
            }
        }

        if(gp!=null){
            for(int i = 0; i<gp.size(); i++){
                if(p == gp.get(i))
                    return true;
            }
        }
        return false;  

    }

    /**
     * Smartly select an empty Space on the board
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

        for (Line l : b.getLines())
        {
            Space blockingSpace = spaceToBlock(l, p);
            if ( blockingSpace !=  null) return blockingSpace;
        }

        return super.selectSpaceForPlacement( b,  p);

    }    

    /**
     * given line l and piece p
     * if the line has three in a row that are the same attribute, and p is of opposite
     * attribute return the space in the line tp place p
     * otherwise return null;
     */
    public Space spaceToBlock(Line l, Piece p)
    {
        //Create and fill arraylist of pieces from full spaces in the line.
        ArrayList <Piece> piece = new ArrayList <Piece>();
        Space empty = null;
        for (Space s: l.getSpaces()){
            if (!s.isEmpty()){
                piece.add(s.getPiece());
            }
            else
                empty = s;
        }
        //if not 3 then return null.
        if (piece.size() != 3){
            return null;
        } 
        // //Find the empty space and save it....
        //for each attribute from Tall to Hollow (0 to 3)
        //  get the value (true or false) for that attribute in first piece
        boolean[] firstAtt = new boolean[4];
        for (int att = 0; att <= 3; att++){
            firstAtt[att] = piece.get(0).is(att);
        }
        //check if that value is the same in second and third piece
        for (int j = 0; j < firstAtt.length; j++){
            if (piece.get(1).is(j) == firstAtt[j] && 
            piece.get(2).is(j) == firstAtt[j]) 
            //System.out.println("blockable on attribute number " + j);
            {
                //if piece p can block then return empty, 
                //System.out.println(p + " is("+ j + ") = " + p.is(j) + " want != " +firstAtt[j]); 
                if (p.is(j) != firstAtt[j])
                    return empty;
            }

        }
        return null;
        //  if it is the same AND it is different from the attribute in piece P
        //  YAY!! this our block....
        //return the empty space from earlier
    }
}
