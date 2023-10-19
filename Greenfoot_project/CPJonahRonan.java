import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class CPJonahRonan here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPJonahRonan extends ComputerPlayer
{
    private int cntBlocking = 0;
    public CPJonahRonan(String name)
    {
        super(name);

    }

    public Piece selectPieceToPlace(Board b)
    {
        Board clone = b.clone();
        ArrayList<Piece> availablePieces = clone.getAvailablePieces();
        ArrayList<Piece> ourAvailablePieces = new ArrayList<Piece>();
        ArrayList<Space> availableSpaces = clone.getAvailableSpaces();

        ArrayList<Line> lines = clone.getLines();

        for(Piece p : availablePieces){
            boolean quartoPossible = false;
            for (Space s : availableSpaces){
                s.receivePiece(p);
                if (clone.checkForQuarto()==true){
                    quartoPossible=true;
                    break;
                }
                s.removePiece();
            }
            if (quartoPossible==false)
                ourAvailablePieces.add(p);
        }
        if(ourAvailablePieces.size()==0)
            return super.selectPieceToPlace(clone);
        else
            return ourAvailablePieces.get((int)(Math.random()*ourAvailablePieces.size()));
    }

    public Space selectSpaceForPlacement(Board b, Piece p)
    {
        Board clone = b.clone();
        //System.out.println("before");
        //System.out.println("will print board num avail spaces = " + clone.getAvailableSpaces().size());

        //clone.print();
        for (Space s : clone.getAvailableSpaces())
        {
            s.receivePiece(p);
            if (clone.checkForQuarto())
                return s;
            else
                s.removePiece();

        }

        //loop through each line.... and if any are oneEmptyOthersTheSame then put it there (returned non-Null)
        for(int i=0; i<b.getLines().size(); i++){
            Space s = returnOneEmptyAndOthersSame(b.getLines().get(i));
            if(s!=null)
            {
                return s;
            }

        }

        
        if(remainingPiecesAreQuarto(clone)==true){

            //  System.out.println("in remaining are about to ");
            for(Space s: clone.getAvailableSpaces()){
                s.receivePiece(p);
                //System.out.println("placed at " + s + " #avail=" + clone.getAvailableSpaces().size());

                if(remainingPiecesAreQuarto(clone)==false)
                {
                    cntBlocking++;
                    return s;
                }
                else
                {
                    s.removePiece();
                    //System.out.println("removed at " + s + " #avail=" + clone.getAvailableSpaces().size());

                }

            }
        }
        //System.out.println("block cnt = " + cntBlocking);
        //clone.print();

        //loop through each line
        //
        //try placing the piece

        return clone.getAvailableSpaces().get((int)(Math.random()*clone.getAvailableSpaces().size()));
    }

    public boolean remainingPiecesAreQuarto(Board b){
        boolean quarto = true;
        Board clone = b.clone();
        for(Piece p: clone.getAvailablePieces())
        {
            quarto = false;

            for(Space s: clone.getAvailableSpaces()){

                s.receivePiece(p);
                if (clone.checkForQuarto()==true){
                    s.removePiece();
                    quarto=true;

                    break;
                }
                s.removePiece();
            }
            //after all possibles, if still not quartos
            if (quarto == false)
            {
                //System.out.println("Piece " + p + " not quarto in this:");
                //clone.printJustSpaces();
                return false;
            }
        }
        return true;
    }

    public Space returnOneEmptyAndOthersSame(Line a){
        int empty=0;
        //is th

        Space tempEmpty = null;

        //count empties
        for(int i=0; i<4; i++){
            if(a.getSpaces()[i].isEmpty())
            {
                empty++;
                tempEmpty=a.getSpaces()[i];
            }
        }
        if(empty!=1){
            return null;
        }
        for(int i=0; i<4; i++)
        {

            if(countOfAtt(a,i,true) == 3)
                return tempEmpty;

            if(countOfAtt(a,i,false) == 3)
                return tempEmpty;
        }
        return null;

    }

    public int countOfAtt(Line a, int att, boolean value){
        int count = 0;
        for(Space s: a.getSpaces()){
            if(s.isOccupied())
                if(s.getPiece().is(att)==value)
                    count++;
        }
        return count;
    }

}
