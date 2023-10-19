import java.util.*;
/**
 * The 
 */
public class Line  
{
    // instance variables - replace the example below with your own
    private Space[] spaces ;
    private int lineNumber;

    /**
     * Constructor for objects of class Line
     */
    public Line(Space[] spaces)
    {
        this.spaces = spaces;
        for (Space s : spaces)
            s.addToMyLines(this);
    }

    public Line()
    {
        spaces = new Space[4];
    }

    /**
     * STUDENTS MUST COMPLETE THIS METHOD
     * 
     * Return true if this line is full.
     * Full is defined as all spaces in the line contain pieces
     * the .isEmpty() method of Space objects will be
     * helpful to determine if a space has a piece.
     * 
     */
    public boolean isFull()
    {
        for (Space s : spaces)
            if (s.isEmpty()) 
                return false;
        return true;
    }

    /**
     * STUDENTS MUST COMPLETE THIS METHOD
     * 
     * Return true if this line has a quarto in it
     * To have a quarto, a line must be full AND
     * all the pieces in all its spaces must share at least one common
     * attribute.
     * 
     * Helpful methods in other classes include: 
     * 
     * Space class:
     *  .getPiece()  return its Piece object (Null if empty)
     *  
     * Piece class:
     *  .is(int attributeConstant)   return true if this piece has the 
     *          specified attribute
     *  Attributes are given in the final static public variables
     *      Piece.DARK = 0, Piece.ROUND = 1, Piece.TALL = 2, Piece.HOLLOW = 3;
     *  for example a Dark, Square, Short, Hollow Piece in variable myPiece
     *      would return
     *          is(Piece.DARK)  returns true
     *          is(Piece.ROUND) returns false
     *          is(Piece.TALL)  returns false
     *          is(Piece.HOLLOW)returns true
     */    
    public boolean isQuarto()
    {
        if (!isFull()) 
            return false;

        for (int attrib = Piece.DARK; attrib <= Piece.HOLLOW; attrib++)
        {
            boolean attributeValue = spaces[0].getPiece().is(attrib);
            boolean allSame = true;
            for (Space s : spaces)
                if (s.getPiece().is(attrib) != attributeValue)
                    allSame = false;
            if (allSame)
                return true;
        }
        return false;
    }

    public void addSpace(Space s)
    {
        for (int i = 0; i < spaces.length; i++)
            if (spaces[i] == null)
            {
                spaces[i] = s;
                spaces[i].addToMyLines(this);
                return;
            }
    }
    
    public Space[] getSpaces()
    {
        return spaces;
    }
    

    public void setLineNumber(int ln)
    {
        lineNumber = ln;
    }
    public String toString()
    {
            String retStr = "line #" + lineNumber + ": ";
            
            for (Space s : spaces)
                retStr +=  s+"\t" ;
            retStr += "isFull = " + isFull() + "\t";
            retStr += "isQuarto = " + isQuarto() + "\t";
            return retStr;
    }
}
