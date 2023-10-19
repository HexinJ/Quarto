import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class Space here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Space extends Actor
{
    private int row;
    private int col;
    private ArrayList<Line> myLines;
    protected Piece myPiece;
 

    public Space(int row, int col)
    {
        this(row, col, null);
 
    }
    public Space(int row, int col, Piece myPiece)
    {
 
        this.row = row;
        this.col = col;
        this.myPiece = myPiece;
        myLines = new ArrayList<Line>();
    }
    public Piece getPiece()
    {   return myPiece; } 
    
    public int getRow() 
    {   return row; }
    
    public int getCol()
    {   return col; }
    
    public ArrayList<Line> getMyLines()
    {
        return myLines;
    }
    public void receivePiece(Piece p)
    {
        myPiece = p;
    }
    public Piece removePiece()
    {
        Piece tempPiece = myPiece;
        myPiece = null;
        return tempPiece;
    }
    public boolean isOccupied()
    {
        return myPiece != null;
    }
    public boolean isEmpty()
    {
        return myPiece == null;
    }    
    public void addToMyLines(Line line)
    {
        myLines.add(line);
    }

    public Space clone()
    {
        if (myPiece == null)
            return new Space(row, col) ;
        else 
            return new Space(row, col, myPiece.clone()) ;
    }
    public boolean equals(Object other)
    {
        if (!(other instanceof Space))
            return false;
        Space otherSpace = (Space) other;
        return row == otherSpace.getRow() && col == otherSpace.getCol();
    }
    public String toString()
    {
        return "("+row+","+col+")";
    }
    
}
