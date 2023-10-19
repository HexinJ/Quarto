import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class CPJimmy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPJimmy extends ComputerPlayer
{
    public CPJimmy(String name)
    {
        super(name);

    }

    public Space selectSpaceForPlacement(Board b, Piece p)
    {
        ArrayList<Space> marginalSpaces = new ArrayList<Space>();
        ArrayList<Space> marginalmidSpaces = new ArrayList<Space>();
        // #1 Quarto 
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        for (Space s : availableSpaces)
        {
            s.receivePiece(p);
            if (b.checkForQuarto())
                return s;
            else
                s.removePiece();
        }

        // #2defensive 
        ArrayList<Piece> availablePieces = b.getAvailablePieces();
        for(Piece x:availablePieces){
            if(x==p)
                availablePieces.remove(x);
        }
        for(Piece x:availablePieces)
        {             
            for(Space s:availableSpaces){
                s.receivePiece(x);
                if(b.checkForQuarto()){
                    s.removePiece();
                    return s;}
                else {
                    s.removePiece();}
            }
        }

        // #3 Defense System "Opposition"
        ArrayList<Space> doubles = new ArrayList<Space>();
        ArrayList<Line> lines = b.getLines();
        Piece opp = new Piece(!p.is(0), !p.is(1), !p.is(2), !p.is(3));
        boolean hasOpp = false;
        //for (Space s: )
        for (Line l: lines){
            if (!l.isFull()) {
                Space[] spaces = l.getSpaces();
                boolean inLine = false;
                for (Space y: spaces){
                    if (y.isOccupied() && y.getPiece().equals(opp))
                        inLine = true;
                }
                if (inLine) {
                    for (Space sp: spaces){
                        if (sp.isEmpty())
                            return sp;
                    }
                }
            }
        }

        //stop tripto
        for(Piece x:availablePieces)
        {             
            for(Space s:availableSpaces){
                s.receivePiece(x);
                int count=Hopeless(b);
                s.removePiece();
                s.receivePiece(p);
                int count2=Hopeless(b);
                if(count2<count){
                    s.removePiece();
                    return s;}
                else{
                    s.removePiece();}
            }
        }
        // #4 place it on the margin 
        for (int row=0;row<b.getSpaces().length;row++){
            for (int col=0;col<b.getSpaces()[0].length;col++){
                if (b.getSpaces()[row][col].isEmpty()&&row==0){
                    marginalSpaces.add(b.getSpaces()[row][col]);}
                else if(b.getSpaces()[row][col].isEmpty()&&col==0){
                    marginalSpaces.add(b.getSpaces()[row][col]);
                }
                if (b.getSpaces()[row][col].isEmpty()&&row==0&&col==1)
                    marginalmidSpaces.add(b.getSpaces()[row][col]);
                if (b.getSpaces()[row][col].isEmpty()&&row==0&&col==2)
                    marginalmidSpaces.add(b.getSpaces()[row][col]);
                if (b.getSpaces()[row][col].isEmpty()&&row==1&&col==0)
                    marginalmidSpaces.add(b.getSpaces()[row][col]);
                if (b.getSpaces()[row][col].isEmpty()&&row==1&&col==3)
                    marginalmidSpaces.add(b.getSpaces()[row][col]);
                if (b.getSpaces()[row][col].isEmpty()&&row==2&&col==0)
                    marginalmidSpaces.add(b.getSpaces()[row][col]);
                if (b.getSpaces()[row][col].isEmpty()&&row==2&&col==3)
                    marginalmidSpaces.add(b.getSpaces()[row][col]);
                if (b.getSpaces()[row][col].isEmpty()&&row==3&&col==1)
                    marginalmidSpaces.add(b.getSpaces()[row][col]);
                if (b.getSpaces()[row][col].isEmpty()&&row==3&&col==2)
                    marginalmidSpaces.add(b.getSpaces()[row][col]);
            }
        }     
        if(marginalSpaces.size()>0){
            int random= (int)(Math.random()*marginalSpaces.size());
            return marginalSpaces.get(random);} 

        //otherwise do as the superclass
        return super.selectSpaceForPlacement( b,  p);
        // if (availableSpaces.size() > 0)
        // return availableSpaces.get((int)(Math.random()*availableSpaces.size()));
        // else
        // return null;        

    }  
    //choose a piece for your opponent 
    public Piece selectPieceToPlace(Board b)
    {
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        ArrayList<Piece> avaPieces = b.getAvailablePieces();

        for(Piece x:avaPieces){
            boolean safe = true;
            for(Space shit:availableSpaces){
                int count3=Hopeless(b);
                shit.receivePiece(x);
                int count4=Hopeless(b);
                if(b.checkForQuarto()||count4==count3){
                    safe=false;                     
                    shit.removePiece();}
                else 
                    shit.removePiece();
            }
            if(safe)
                return x;
        }        
        for(Piece x:avaPieces){
            boolean safe = true;
            for(Space shit:availableSpaces){

                shit.receivePiece(x);

                if(b.checkForQuarto()){
                    safe=false;                     
                    shit.removePiece();}
                else 
                    shit.removePiece();
            }
            if(safe)
                return x;
        }  
        //give a piece so that the opponent might be able to make a tripto. 
        return super.selectPieceToPlace(b);}

    public int Hopeless(Board b)
    {
        int count=0;
        ArrayList<Line> avaLines = b.getLines();
        for (Line x : avaLines){
            if(isTripto(x))
                count++;
        }   
        return count;
    }

    public boolean isTripto(Line l){
        if(l.isFull())
            return false;
        Space[] fuckSpaces = l.getSpaces();
        int Dark=0; int notDark=0;int Round=0; int notRound=0; int Tall=0; 
        int notTall=0; int Hollow=0; int notHollow=0;
        for(Space x:fuckSpaces){
            if (!x.isEmpty()&&x.getPiece().is(Piece.DARK))
                Dark++;
            else if(!x.isEmpty())
                notDark++;
            if (!x.isEmpty()&&x.getPiece().is(Piece.ROUND))
                Round++;
            else if(!x.isEmpty())
                notRound++;
            if (!x.isEmpty()&&x.getPiece().is(Piece.TALL))
                Tall++;
            else if(!x.isEmpty())
                notTall++;
            if (!x.isEmpty()&&x.getPiece().is(Piece.HOLLOW))
                Hollow++;
            else if(!x.isEmpty())
                notHollow++;
        }
        return Dark==3||notDark==3||Round==3||notRound==3||Tall==3||notTall==3||Hollow==3||notHollow==3;
    }
}
