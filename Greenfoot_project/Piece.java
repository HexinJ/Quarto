import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Piece here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Piece  extends Actor
{
    public final static int DARK = 0, ROUND = 1, TALL = 2, HOLLOW = 3;
    protected boolean attributes[];
    public Piece(boolean isDark, boolean isRound, boolean isTall, boolean isHollow)
    {
  
        attributes = new boolean[4];
        attributes[DARK] = isDark;
        attributes[ROUND] = isRound;
        attributes[TALL] = isTall;
        attributes[HOLLOW] = isHollow;    
    }
    public Piece(String descr)
    {
  
        attributes = new boolean[4];
        attributes[DARK] = descr.indexOf("D") >= 0;
        attributes[ROUND] = descr.indexOf("R") >= 0;
        attributes[TALL] = descr.indexOf("T") >= 0;
        attributes[HOLLOW] = descr.indexOf("O") >= 0;
    }
    public boolean is(int attributeType) 
    {
        if (attributeType < DARK || attributeType > HOLLOW)
            return false;
        return attributes[attributeType];
    }

    public String toString()
    {   
        String retString = "L";
        if (is(DARK)) retString = "D";
        if (is(ROUND)) retString += "R";
        else retString +="Q";
        if (is(TALL)) retString += "T";
        else retString +="S";
        if (is(HOLLOW)) retString += "O";
        else retString +="X";
        return retString;
    }

    public Piece clone()
    {
        return new Piece(is(DARK), is(ROUND), is(TALL), is(HOLLOW));
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof Piece))
            return false;
        for (int a = 0; a < 4; a++)
            if (attributes[a] != ((Piece)other).attributes[a])
                return false;
        return true;

}
}
