import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GraphicSpace here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GraphicSpace extends Space
{
    
    public final static Color HILITECOLOR = Color.YELLOW;
    public final static Color SPACECOLOR = new Color(240,187,120);
    public final static Color BOARDCOLOR = new Color(51,79,77);
    public final static int SPACEWIDTH = 5;
    private boolean selected;
    private final GreenfootImage circle;
    private final GreenfootImage circleHighlighted;
    
    
     public GraphicSpace(int row, int col)
     {
        super(row, col);
        selected = false;
        circle = getCircle(false);
        circleHighlighted = getCircle(true);
        setImage(circle);   
     }
     
    public void select()
    {   selected = true;  }

    public void unselect()
    {   selected = false; }
    
       public GreenfootImage getImage()
    {
        if (selected)
            return circleHighlighted;
        else
            return circle;

    }
    
    public GraphicPiece undo()
    {
        GraphicPiece temp = (GraphicPiece) myPiece;
        myPiece = null;
        return temp;
    }
    private GreenfootImage getCircle(boolean highlighted)
    {
        GreenfootImage circle = new GreenfootImage((int)(GraphicPiece.WIDTH*1.5),(int)(GraphicPiece.WIDTH*1.3));
        if (highlighted)
            circle.setColor(HILITECOLOR);   
        else
            circle.setColor(SPACECOLOR);                
        circle.fillOval(0,0, circle.getWidth()-1, circle.getHeight()-1);   
        circle.setColor(BOARDCOLOR); 
        circle.fillOval(SPACEWIDTH,SPACEWIDTH, circle.getWidth()-1-2*SPACEWIDTH, circle.getHeight()-1-2*SPACEWIDTH);     
        return circle;
    }
         /**
     * Act - do whatever the Space wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (Greenfoot.mouseMoved(this) && myPiece == null)

            ((QuartoWorld) getWorld()).highlightSpaceToPlace(this);
        else if (Greenfoot.mouseMoved(getWorld()) )
            ((QuartoWorld) getWorld()).highlightSpaceToPlace(null);
        if (Greenfoot.mouseClicked(this) && myPiece == null)
        {

            ((QuartoWorld) getWorld()).placePiece(this);
        }
    }
}
