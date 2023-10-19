import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

 
public class BattleSplash extends MsgBox
{
    private QuartoWorld qw;
    private int animateClockwise = 1;
    public BattleSplash(QuartoWorld qw) 
    {
        super();
        this.qw = qw;
        setImage(getImage());
    }
    public GreenfootImage getImage()
    {
        Player[] players = qw.getPlayersArray();
        GreenfootImage cpPics[] = new GreenfootImage[2];
        cpPics[0] = players[0].getImage();
        cpPics[1] = players[1].getImage();
        for (GreenfootImage i : cpPics)
        {
            if (i != null)
            {
                double factor = 150.0/i.getHeight();
                i.scale((int)(factor * i.getWidth()),(int)(factor * i.getHeight()));
            }
        }
        
       
        GreenfootImage gi = new GreenfootImage(600,480);
        gi.setColor(GraphicSpace.SPACECOLOR);
        gi.setColor(new Color(222,166,41));
        gi.setColor(GraphicSpace.BOARDCOLOR);
        gi.setColor(GraphicSpace.SPACECOLOR);
        gi.setColor(Color.LIGHT_GRAY);
         gi.setColor(Color.WHITE);
       gi.fill();
        gi.setColor(Color.WHITE);
         gi.setColor(Color.DARK_GRAY);
       gi.drawRect(0,0,gi.getWidth()-1,gi.getHeight()-1);
        gi.drawRect(1,1,gi.getWidth()-3,gi.getHeight()-3);
        gi.setColor(Color.BLACK);
       // gi.setColor(Color.WHITE);
 
        gi.drawString("Its a Computer vs Computer Battle! for " + qw.getCompVcompGamesToPlay() + " games!", 200,20);  
        gi.drawString(longVersionOf(players[0].getName()) , 50,130);  
        gi.drawImage(cpPics[0],(gi.getWidth()-cpPics[0].getWidth())/2,130-cpPics[0].getHeight()/2);
        gi.drawString(" versus ", 50,230);  
        gi.drawString(longVersionOf(players[1].getName()) , 50,330);  
       gi.drawImage(cpPics[1],(gi.getWidth()-cpPics[1].getWidth())/2,330-cpPics[1].getHeight()/2);
        
        gi.drawString("Click here to Continue",230,465 );     
        // y+= 30;
        // int idx = 0;
        // for (String cn : QuartoWorld.PlayerClassNames)
        // {
            // gi.drawString(QuartoWorld.PlayerClassNamesInitials.substring(idx,idx+1) + " for " + cn,20+indent,y);
            // if (cpPics.length > idx && cpPics[idx] != null)
            // {
                // int hOffset = ((indent-20) - cpPics[idx].getWidth())/2;
                // if (hOffset < 0) hOffset = 0;
                // gi.drawImage(cpPics[idx],20+hOffset,y-cpPics[idx].getHeight()/2);
            // }
            // y+= incr;
            // idx++;
        
        // }
        // y-= 10;
        return gi;

    }
    private String longVersionOf(String shortN)
    {
     
        // for (int i = 0; i < QuartoWorld.PlayerClassShortNames.length; i++)
            // if (QuartoWorld.PlayerClassShortNames[i].equals(shortN))
                // return QuartoWorld.PlayerClassNames[i];
        return shortN;
    }
    public void act()
    {
        if (Greenfoot.mouseClicked(this))
        {
            QuartoWorld qw = (QuartoWorld) getWorld();
            qw.removeObject(this);
            qw.repaint();
            qw.startBattle();
        }
        
    }
}
