import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PlayerTypeMsg here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PlayerTypeMsg extends MsgBox
{
    private int width;
    private int height;
    public PlayerTypeMsg(int w, int h) 
    {
        super();
        width = w;
        height = h;
        setImage(getImage());
    }

    public GreenfootImage getImage()
    {
        GreenfootImage cpPics[] = new GreenfootImage[11];
        cpPics[0] = new ComputerPlayer("").getImage();
        cpPics[1] = new PlyQuartoHound("").getImage();
        cpPics[2] = new CPFraniJulie("").getImage();
        cpPics[3] = new CPGloriaMaygala1("").getImage();
        cpPics[4] = new CPGloriaMaygala2("").getImage();
        cpPics[5] = new CPJimmy("").getImage();
        cpPics[6] = new CPJonahRonan("").getImage();
        cpPics[7] = new CPNik("").getImage();
        cpPics[8] = new CPMrLeib("").getImage();
        cpPics[9] = new CPShinyi("").getImage();
        cpPics[10] = new CPBen("").getImage();
        for (GreenfootImage i : cpPics)
        {
            if (i != null)
            {
                double factor = 38.0/i.getHeight();
                i.scale((int)(factor * i.getWidth()),(int)(factor * i.getHeight()));
            }
        }
        
        //GreenfootImage gi = new GreenfootImage(width,height);
        GreenfootImage gi = new GreenfootImage(600,480);
        gi.setColor(GraphicSpace.SPACECOLOR);
         
        gi.setColor(new Color(222,166,41));
        gi.setColor(GraphicSpace.BOARDCOLOR);
        gi.setColor(GraphicSpace.SPACECOLOR);
        gi.fill();
        gi.setColor(Color.DARK_GRAY);
        gi.drawRect(0,0,gi.getWidth()-1,gi.getHeight()-1);
        gi.drawRect(1,1,gi.getWidth()-3,gi.getHeight()-3);
        gi.setColor(Color.BLACK);
    // public static final String[] PlayerClassNames = { "Dumb AI", "Quarto Hound", "Smart AI", "Other AI"};
    // public static final String[] PlayerClassShortNames = { "Dumb AI", "Q-Hound", "Smart AI", "Other AI"};
    // public static final String   PlayerClassNamesInitials = "DQSO";
        int y = 15;
        int incr = 40;
        int indent = 100;
        gi.drawString("Welcome to Pennington Quarto!.  The computer player types you can choose from are :",20,y);  
        y+= 30;
        int idx = 0;
        for (String cn : QuartoWorld.PlayerClassNames)
        {
            gi.drawString(QuartoWorld.PlayerClassNamesInitials.substring(idx,idx+1) + " for " + cn,20+indent,y);
            if (cpPics.length > idx && cpPics[idx] != null)
            {
                int hOffset = ((indent-20) - cpPics[idx].getWidth())/2;
                if (hOffset < 0) hOffset = 0;
                gi.drawImage(cpPics[idx],20+hOffset,y-cpPics[idx].getHeight()/2);
            }
            y+= incr;
            idx++;
        
        }
        y-= 10;
        gi.drawString("Click here to Choose Player Types",20,y );  
        //System.out.println("y = " + y);
        return gi;

    }

    public void act()
    {
        if (Greenfoot.mouseClicked(this))
        {
            QuartoWorld qw = (QuartoWorld) getWorld();
            qw.removeObject(this);
            qw.repaint();
            qw.getPlayers();
            qw.repaint();
        }
        

    }

}
