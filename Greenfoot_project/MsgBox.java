import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Status here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MsgBox extends Actor
{
    public static final int STATUS = 0;
    public static final int SCORE = 1;    
    private int type;
    public MsgBox(int type)
    {
        this.type = type;
        setImage(getImage());
    }
    public MsgBox()
    {
        type = STATUS;
    }

    public GreenfootImage getImage()
    {
        if (type == STATUS)
        {
            GreenfootImage gi = new GreenfootImage(400,25);
            gi.setColor(GraphicSpace.SPACECOLOR);
            gi.fill();
            gi.setColor(Color.DARK_GRAY);
            gi.drawRect(0,0,gi.getWidth()-1,gi.getHeight()-1);
            gi.drawRect(1,1,gi.getWidth()-3,gi.getHeight()-3);
            gi.setColor(Color.BLACK);
            QuartoWorld qw = (QuartoWorld) getWorld();

            Player currPlayer = null;
            if (qw != null) 
                currPlayer = qw.getCurrPlayer();
            if (qw == null || currPlayer == null) 
            {
                String msg = "Welcome to Pennington Quarto";
                int lng  =  msgLength(msg, gi.getFont());
                gi.drawString(msg,(gi.getWidth()-lng)/2,17);  //gi.drawString(msg,5,17);  
                return gi;

            }

            int action = qw.getAction();
            String msg = qw.getCurrPlayer().getName() ;
            if (qw.checkForQuarto())
            {

                msg = "QUARTO!  " + msg + " wins!   Click on New Game button";
            }
            else
            {
                if (qw.noPiecesAvailable())
                    msg = "Game ends in a DRAW ¯\\_(ツ)_/¯   Click on New Game button";
                else if (action == QuartoWorld.SELECT)
                    msg += ", select a piece for " + qw.getOtherPlayer().getName() + " to place";
                else
                    msg += ", place piece selected by " + qw.getOtherPlayer().getName() + " on board";
            }
            int lng  =  msgLength(msg, gi.getFont());
            gi.drawString(msg,(gi.getWidth()-lng)/2,17);  //gi.drawString(msg,5,17);  
            return gi;
        }
        else if (type == SCORE)
        {
            GreenfootImage gi = new GreenfootImage(150,60);
            gi.setColor(GraphicSpace.SPACECOLOR);
            gi.fill();
            gi.setColor(Color.DARK_GRAY);
            gi.drawRect(0,0,gi.getWidth()-1,gi.getHeight()-1);
            gi.drawRect(1,1,gi.getWidth()-3,gi.getHeight()-3);
            gi.setColor(Color.BLACK);
            QuartoWorld qw = (QuartoWorld) getWorld();

            Player[] players = null;
            if (qw != null) 
                players = qw.getPlayersArray();
            if (qw == null || players == null || players[0] == null || players[1] == null) 
            {

                return gi;

            }
            int games = qw.getGamesPlayed();

            gi.drawString("Games Played: " +  games,5,17);
            gi.drawString(players[0].getName() + ": " + players[0].getWins() + " wins" ,5,34);
            gi.drawString(players[1].getName() + ": " + players[1].getWins() + " wins" ,5,51);
 

            return gi;
        }
        else
        {
            GreenfootImage gi = new GreenfootImage(400,25);
            return gi;
        }

    }

    public static void guessLength()
    {
        GreenfootImage gi = new GreenfootImage(400,25);
        String letters = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String t = "";
        int lng  =  msgLength(t, gi.getFont());
        for (int x = 0; x < 200; x ++)
        {
            t = letters.substring(0,x);
            lng  =  msgLength(t, gi.getFont());
            System.out.println("Num Chars = " + x + "\tLength = " + lng);
        }
        System.out.println("Length / Char = " + 200.0/lng);
    }
    public static int msgLength(String text, greenfoot.Font gfFont)
    {
        return (int) (text.length() / 0.17);
        
 
    } 
}
