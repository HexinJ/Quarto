import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Quarto Piece
 * 
 */
public class GraphicPiece extends Piece
{
    
    public final static int TALLHEIGHT = 100;
    public final static int SMALLHEIGHT = 70;
    public final static int WIDTH = 40;
    public final static int HILITEWIDTH = 3;
    public final static int IMAGEWIDTH = WIDTH + HILITEWIDTH;
    public final static int PERSPECTIVE = 30;
    public final static Color DARKCOLOR = new Color(164,84,36); //Color.BLUE;
    public final static Color LIGHTCOLOR = new Color(246,208,127); //Color.ORANGE;
    public final static Color HILITECOLOR = Color.YELLOW;

    private boolean selected;
    private boolean available;
    private int originalX;
    private int originalY;
    private GreenfootImage baseImage;
    private GreenfootImage selectedImage;
    private int animateClockwise = 1;
    private int animateX = -1;
    private int animateY = -1; 
    private int animateStep = 1;
    public GraphicPiece(boolean isDark, boolean isRound, boolean isTall, boolean isHollow)
    {
        super(isDark, isRound,  isTall,  isHollow);

        selected = false;
        available = true;
        baseImage = generateImage(false);
        selectedImage = generateImage(true);
        setImage(baseImage);

    }

    public void select()
    {   selected = true;  }

    public void unselect()
    {   selected = false; }

    public void setSelected(boolean sel)
    {
        selected = sel;
        
    }

    public void returnToStart(boolean select)
    {
        selected = select;
        available = true;
       
        setLocation(originalX,originalY);

    }    
    public void returnToStart()
    {
        returnToStart(true);


    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setAvailable(boolean available)
    {   this.available = available; }

    public void setPlacedOnBoard()
    {   available = false;      }

    public void resetAvailable()
    {   available = true;  }

    public boolean isAvailable()
    {
        return available;
    }



    public GreenfootImage getImage()
    {
        if (selected)
            return selectedImage;
        else
            return baseImage;

    }

    private GreenfootImage generateImage(boolean highlighted)
    {
        Color pieceColor = LIGHTCOLOR;
        Color outlineColor = DARKCOLOR;

        Color hollowColor = DARKCOLOR; //Color.DARK_GRAY;
        int height = SMALLHEIGHT;
        int halfHeight  = TALLHEIGHT/2;
        int notchHeight = 3;
        int holeOffset = 8;
        if (is(TALL)) 
            height = TALLHEIGHT;
        GreenfootImage gi = new GreenfootImage(WIDTH+2*HILITEWIDTH,height+2*HILITEWIDTH);

        if (is(DARK))
        {
            pieceColor = DARKCOLOR;
            outlineColor = new Color(85,44,29);
            hollowColor = outlineColor;
        }

        if (is(ROUND))
        {
            if (highlighted)
            {
                gi.setColor(HILITECOLOR);                
                gi.fillOval(0,height-PERSPECTIVE-1+2*HILITEWIDTH, WIDTH-1+2*HILITEWIDTH, PERSPECTIVE);    
                gi.fillOval(0,0, WIDTH-1+2*HILITEWIDTH, PERSPECTIVE);    
                gi.fillRect(0,PERSPECTIVE/2,WIDTH-1+2*HILITEWIDTH , height+2*HILITEWIDTH-PERSPECTIVE);    

            }
            gi.setColor(pieceColor);
            gi.fillOval(HILITEWIDTH,height-PERSPECTIVE+HILITEWIDTH, WIDTH, PERSPECTIVE);    
            gi.setColor(outlineColor);
            gi.drawOval(HILITEWIDTH,height-PERSPECTIVE-1+HILITEWIDTH, WIDTH-1, PERSPECTIVE);    
            gi.setColor(pieceColor);
            gi.fillRect(HILITEWIDTH,PERSPECTIVE/2+HILITEWIDTH,WIDTH,height-PERSPECTIVE);
            gi.setColor(outlineColor);
            for (int n = 0; n < notchHeight; n++)
                gi.drawOval(HILITEWIDTH,height-TALLHEIGHT/2 - n+HILITEWIDTH, WIDTH-1, PERSPECTIVE);

            gi.setColor(pieceColor);
            gi.fillRect(HILITEWIDTH,height-TALLHEIGHT/2-8+HILITEWIDTH,WIDTH, PERSPECTIVE-notchHeight);
            //gi.setColor(outlineColor);
            // gi.drawOval(0,height-PERSPECTIVE-1, WIDTH-1, PERSPECTIVE);    
            gi.setColor(pieceColor);
            gi.fillOval(HILITEWIDTH,HILITEWIDTH, WIDTH, PERSPECTIVE);            
            gi.setColor(outlineColor);
            gi.drawOval(HILITEWIDTH, HILITEWIDTH, WIDTH-1, PERSPECTIVE); 
            gi.drawLine(HILITEWIDTH,PERSPECTIVE/2+HILITEWIDTH, HILITEWIDTH,height-PERSPECTIVE/2+HILITEWIDTH);
            gi.drawLine(HILITEWIDTH+WIDTH-1,PERSPECTIVE/2+HILITEWIDTH,WIDTH-1+HILITEWIDTH,height-PERSPECTIVE/2+HILITEWIDTH);
            //gi.setColor(outlineColor);
            //gi.drawOval(0,height-PERSPECTIVE-1, WIDTH, PERSPECTIVE);    

            if (is(HOLLOW))
            {
                gi.setColor(hollowColor);
                gi.fillOval(HILITEWIDTH+holeOffset+2,HILITEWIDTH+holeOffset, WIDTH-2*holeOffset-4, PERSPECTIVE-2*holeOffset);            
            }  
        }

        else  //Square Piece
        {
            if (highlighted)
            {
                int[] hx = new int[6];
                int[] hy = new int[6];
                hx[0] = 0;             hx[1] = (WIDTH+2*HILITEWIDTH)/2; hx[2] = WIDTH+2*HILITEWIDTH-1;   hx[3] = WIDTH+2*HILITEWIDTH-1;                                  
                hy[0] = PERSPECTIVE/2; hy[1] = 0;                       hy[2] = PERSPECTIVE/2 ;          hy[3] = height - PERSPECTIVE/2+2*HILITEWIDTH; 
                hx[4] = (WIDTH+2*HILITEWIDTH)/2;       hx[5] = 0;
                hy[4] = height-1+2*HILITEWIDTH;        hy[5] = height - PERSPECTIVE/2+2*HILITEWIDTH;  
                gi.setColor(HILITECOLOR);

                gi.fillPolygon(hx,hy,6); 
            }
            int[] x = new int[4];
            int[] y = new int[4];

            
            x[0] = HILITEWIDTH;   x[1] = WIDTH/2+HILITEWIDTH; x[2] = WIDTH/2+HILITEWIDTH; x[3] = HILITEWIDTH;                        
            y[0] = PERSPECTIVE/2+HILITEWIDTH; y[1] = PERSPECTIVE+HILITEWIDTH;  y[2] = height+HILITEWIDTH;  y[3] = height - PERSPECTIVE/2+HILITEWIDTH; 

            gi.setColor(pieceColor);
            gi.fillPolygon(x,y,4); 
            gi.setColor(outlineColor);
            gi.drawPolygon(x,y,4);  

            x[0] = WIDTH/2+HILITEWIDTH; x[1] = WIDTH/2+HILITEWIDTH; x[2] = WIDTH-1+HILITEWIDTH; x[3] = WIDTH-1+HILITEWIDTH;                      
            y[0] = PERSPECTIVE+HILITEWIDTH; y[1] = height+HILITEWIDTH;  y[2] = height - PERSPECTIVE/2+HILITEWIDTH;  y[3] = PERSPECTIVE/2+HILITEWIDTH; 
            gi.setColor(pieceColor);
            gi.fillPolygon(x,y,4); 
            gi.setColor(outlineColor);
            gi.drawPolygon(x,y,4);  

            x[0] = HILITEWIDTH;   x[1] = WIDTH/2+HILITEWIDTH;  x[2] = WIDTH+HILITEWIDTH ;  x[3] = WIDTH/2+HILITEWIDTH;                      
            y[0] = PERSPECTIVE/2+HILITEWIDTH; y[1] = PERSPECTIVE+HILITEWIDTH; y[2] = PERSPECTIVE/2+HILITEWIDTH;  y[3] =  HILITEWIDTH; 
            gi.setColor(pieceColor);
            gi.fillPolygon(x,y,4); 
            gi.setColor(outlineColor);
            gi.drawPolygon(x,y,4);  
            for (int n = 0; n < notchHeight; n++)
            {
                gi.drawLine( HILITEWIDTH,height-TALLHEIGHT/2+PERSPECTIVE/2 - n+HILITEWIDTH, 
                    WIDTH/2+HILITEWIDTH,  height-TALLHEIGHT/2 - n+PERSPECTIVE+HILITEWIDTH);
                gi.drawLine(WIDTH-1+HILITEWIDTH,height-TALLHEIGHT/2+PERSPECTIVE/2 - n+HILITEWIDTH, 
                    WIDTH/2+HILITEWIDTH, height-TALLHEIGHT/2 - n+PERSPECTIVE+HILITEWIDTH);
            }
            if (is(HOLLOW))
            {
                gi.setColor(hollowColor);
                gi.fillOval(holeOffset+2+HILITEWIDTH,holeOffset+HILITEWIDTH, 
                    WIDTH-2*holeOffset-4, PERSPECTIVE-2*holeOffset);            
            }  

        }

        return gi;
    }

    /**
     * Act - do whatever the Piece wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        

        if (Greenfoot.mouseClicked(this) && available &&
            !((QuartoWorld) getWorld()).pieceAlreadySelected())
        {
             
            ((QuartoWorld) getWorld()).choosePieceToPlace(this);
        }
        else if (selected && available && ((QuartoWorld) getWorld()).doAnimation())
        {
            int angle = getRotation();
            if (Math.abs(angle) >= 5)
            {
                animateClockwise = animateClockwise  * -1;
            }
            setRotation(angle + animateClockwise);
        }
        else 
            setRotation(0);
        // else if (animateX != -1 && animateY != -1 &&  ((QuartoWorld) getWorld()).doAnimation())
        // {
            // if (placementComplete(animateX, animateY))
            // {
                // animateX = -1;
                // animateY = -1;
            // }
            // else
            // {
                // if (getX() < 250)
                    // setLocation(Math.min(getX() + animateStep, 250), getY());
                // else if (getY() < animateY)
                    // setLocation(getX(), Math.min(getY() + animateStep, animateY) );
                // else if (getY() > animateY)
                    // setLocation(getX(), Math.max(getY() - animateStep, animateY) );
                // else if (getX() < animateX)
                    // setLocation(Math.min(getX() + animateStep, animateX), getY());
                // //getWorld().repaint();

            // }
        // }

    }
    public void startAnimation​(int x, int y)
    {
        animateX = x;
        animateY = y;
    }
    public boolean placementComplete(int x, int y)
    {
        return getX() == x && getY() == y;
    }
    protected void addedToWorld(World world)
    {
        originalX = getX();
        originalY = getY();
    }
}
