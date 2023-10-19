import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Button here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Button extends Actor
{
    private static int NORMAL = 0, HOVER = 1, PRESSED = 2;
    private static int[] RGB = {217,243,146};
    private GreenfootImage[] images;
    private String message;
    public Button(String message)
    {
        this.message = message;
        images = new GreenfootImage[3];
        for (int i = 0; i < 3; i++)
            images[i] = getImage(i);
        setImage(images[NORMAL]);
    }
    /**
     * Act - do whatever the Button wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
         if (Greenfoot.mouseClicked(this))
         {
            if (message.equals("Undo"))
                ((QuartoWorld) getWorld()).undo();
                
            else if (message.equals("New Game"))
            {
                ((QuartoWorld) getWorld()).resetCompVcompGamesToPlay();
                ((QuartoWorld) getWorld()).reset();
            }   
            else if (message.equals("Debug"))
                ((QuartoWorld) getWorld()).debugOptions();
               
            ((QuartoWorld) getWorld()).repaint();
         }         
         else if (Greenfoot.mouseMoved(this) )
         {
            setImage(images[HOVER]);
            ((QuartoWorld) getWorld()).repaint();
         }
         else if (Greenfoot.mouseMoved(getWorld())&& getImage() != images[NORMAL])
         {
            setImage(images[NORMAL]);
            ((QuartoWorld) getWorld()).repaint();
         }        
         
    }
    private GreenfootImage getImage(int state)
    {
        int width = 45;
        if (message.equals("New Game"))
            width = 70;
        GreenfootImage gi = new GreenfootImage(width,25);
        gi.setColor(new Color(RGB[state],RGB[state], RGB[state]));
        gi.fill();
        gi.setColor(Color.BLACK);
        //int lng  =  MsgBox.msgLength(message, gi.getFont());
        gi.drawString(message,5,17);  //gi.drawString(msg,5,17);  
        //gi.drawString(message,(gi.getWidth()-lng)/2,17);  //gi.drawString(msg,5,17);  
 
        //gi.drawString(message,5,17);
        gi.setColor(Color.DARK_GRAY);
        gi.drawRect(0,0,gi.getWidth()-1,gi.getHeight()-1);
        gi.drawRect(1,1,gi.getWidth()-3,gi.getHeight()-3);
       return gi;
        
         
    }
    public static void test()
    {
        System.out.println(Greenfoot.ask("Enter Something"));
    }
    
}
