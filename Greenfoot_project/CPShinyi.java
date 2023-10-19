import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class CPShinyi here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPShinyi extends ComputerPlayer
{
    public CPShinyi(String name)
    {
        super(name);

    }

    public Piece selectPieceToPlace(Board b)
    {
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        ArrayList<Piece> availablePieces = b.getAvailablePieces();
        ArrayList<Piece> smartPieces = new ArrayList<Piece>();
        //ArrayList<Piece> 
        for (Piece p : availablePieces) {
            boolean Safe = true;
            for (Space s : availableSpaces)
            {
                s.receivePiece(p);
                if (!Safe || b.checkForQuarto())
                    Safe = false;
                s.removePiece();
            }
            if (Safe){
                smartPieces.add(p);
                //return p;
            }
        }
        //no thirtos allowed
        for (Piece f: smartPieces) {
            boolean thirSafe = true;
            for (Space sp : availableSpaces)
            {
                sp.receivePiece(f);
                if (!thirSafe || isBoardThirto(b))
                    thirSafe = false;
                sp.removePiece();
            }
            if (thirSafe){
                return f;
            }
        }
        //pick out of no quarto pieces
        if (smartPieces.size()>0) {
            int rand = (int)(Math.random()*smartPieces.size());
            return smartPieces.get(rand);
        }
        //otherwise return super
        return super.selectPieceToPlace(b);
    }

    public Space selectSpaceForPlacement(Board b, Piece p)
    {
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        ArrayList<Piece> avPieces = b.getAvailablePieces();
        for (Space s: availableSpaces)
        {
            s.receivePiece(p);
            if (b.checkForQuarto())
                return s;
            else
                s.removePiece();

        }
        //blocks possible spaces for quarto in future
        for (Piece e: avPieces){
            for (Space i: availableSpaces){
                i.receivePiece(e);
                if (b.checkForQuarto())
                    return i;
                else
                    i.removePiece();
            }
        }
        ArrayList<Line> lines = b.getLines();
        //no thirtos
        for (Line n: lines){
            if (!n.isFull()){
                for (Space o: n.getSpaces()){
                    if (o.isEmpty()){
                        o.receivePiece(p);
                        if (!n.isFull()){
                            if (!isBoardThirto(b)){
                                o.removePiece();
                                return o;
                            }
                        }
                        o.removePiece();
                    }
                }
            }
        }
        //opp piece magic
        Piece opp = new Piece(!p.is(0), !p.is(1), !p.is(2), !p.is(3));
        boolean hasOpp = false;
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
        return super.selectSpaceForPlacement( b,  p);
    }

    public boolean isBoardThirto(Board b) {
        int isD = 0;
        int isR = 0;
        int isT = 0;
        int isH = 0;
        ArrayList<Line> lines = b.getLines();
        int countF = 0;
        for (Line l: lines){
            Space[] spaces = l.getSpaces();
            countF = 0;
            for (Space sp: spaces)
                if (sp.isOccupied())
                    countF++;
            if (countF == 3) {
                for (Space s: spaces){
                    if (s.isOccupied()){
                        if (s.getPiece().is(0))
                            isD ++;
                        if (s.getPiece().is(1))
                            isR ++;
                        if (s.getPiece().is(2))
                            isT ++;
                        if (s.getPiece().is(3))
                            isH ++;
                    }
                }
                return (isD==3||isD==0||isR==3||isR==0||
                    isT==3||isT==0||isH==3||isH==0);
            }
        }
        if (countF != 3) return false;
        return (isD==3||isD==0||isR==3||isR==0||
            isT==3||isT==0||isH==3||isH==0);
    }
}
