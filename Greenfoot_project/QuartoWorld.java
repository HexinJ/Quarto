import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
//import javax.swing.*;

/**

 */
public class QuartoWorld extends World
{
    private ArrayList<Piece> availablePieces = new ArrayList<Piece>();
    private ArrayList<Line> lines = new ArrayList<Line>();
    private Space[][] spaces = new Space[4][4];
    private Board officialBoard;
    private GreenfootSound battleMusic;
    
    private ArrayList<Actor> moves = new ArrayList<Actor>();
    private Player[] players = new Player[2];
    private GraphicPiece pieceToPlace = null;
    private int currPlayer = 1;
    private boolean splashShowing = false;
    private boolean playMusic = false;
    public final static boolean DEBUG = false;

    public final static int SELECT = 0;
    public final static int PLACE = 1;
    private int currAction = SELECT;
    private boolean animate = true;
    private int games = 0;
    private int compVcompGamesToPlay = -1;
    private int initialcompVcompGamesToPlay = compVcompGamesToPlay;
    private static final Class[] PlayerClasses = {Player.class, ComputerPlayer.class, 
            PlyQuartoHound.class, MyComputerPlayer.class, MyOtherComputerPlayer.class};
    public static final String[] PlayerClassNames = { "Dumb Computer Player", "Quarto Hound", 
        "Better than Ricky Player designed and coded by Frani and Julie" ,  
        "Love Shack Fancy designed and coded by Gloria and Maygala" ,  
        "\"Can we have some candy?\" designed and coded by Gloria and Maygala" ,  
        "Xitler2077 designed and coded by Jimmy" ,  
        "Varsity Coders designed and coded by Jonah and Ronan" ,  
        "[̲̅B][̲̅u][̲̅r][̲̅g][̲̅e][̲̅r] designed and coded by Nik and Ryan" ,  
        "Obnoxious Blocker designed and coded by Mr Leib" ,  
        "Swifty designed and coded by Shinyi" ,  
        "Better than Yahtzee designed and coded by Ben" ,  
    };
    public static final String[] PlayerClassShortNames = { "Dumb", "Q-Hound",
                                                           ">Ricky", "LoveSF", "Candy", "Xitler",
                                                           "Varsity", "Burger", "Block", "Swifty", "Yahtz+"};
    public static final String   PlayerClassNamesInitials = "DQRLCXVBOSY";
    //public static final String[] PlayerClassNames = { "Dumb AI", "Quarto Hound", "Smart AI", "Other AI"};
    //public static final String[] PlayerClassShortNames = { "Dumb AI", "Q-Hound", "Smart AI", "Other AI"};
    //public static final String   PlayerClassNamesInitials = "DQSO";
    private static final int REGSPEED = 50;
    private static final int FASTSPEED = 100;
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public QuartoWorld()
    {    
        // Create a new world  .
        super(800, 500, 1);
        Greenfoot.setSpeed(REGSPEED);
        setPaintOrder(PlayerTypeMsg.class, BattleSplash.class, GraphicPiece.class,Space.class);
        GreenfootImage gi = new GreenfootImage(getWidth(), getHeight());
        gi.setColor(GraphicSpace.BOARDCOLOR);
        gi.fill();
        gi.setColor(GraphicSpace.BOARDCOLOR);
        gi.fillOval(252,44,540,440);
        gi.setColor(GraphicSpace.SPACECOLOR);
        gi.drawOval(252,44,540,440);
        setBackground(gi);
        int vspace = (getHeight() - 4 * (GraphicPiece.TALLHEIGHT+GraphicPiece.HILITEWIDTH))/5;
        int hspace = 10;
        int xStart = 20;
        int yStart = vspace + GraphicPiece.TALLHEIGHT+GraphicPiece.HILITEWIDTH;
        int y = 20; 
        for (int row = 0; row < 4; row++)
        {
            int x = xStart;
            y = row * (GraphicPiece.TALLHEIGHT + vspace) + yStart ;
            for (int col = 0; col < 4; col++)
            {
                boolean dark = row < 2;
                boolean tall = col < 2;
                boolean round = row % 2 == 0;
                boolean hollow = col % 2 == 0;
                GraphicPiece p = new GraphicPiece(dark, round, tall, hollow);
                availablePieces.add(p);
                addObject(p, x + p.getImage().getWidth()/2, y - p.getImage().getHeight()/2);
                x += GraphicPiece.WIDTH + hspace;
            }
        }
        xStart = 300;
        yStart = 190; 
        vspace = 24;
        hspace = 30;
        int xmin = Integer.MAX_VALUE;
        int xmax = Integer.MIN_VALUE;
        int ymin = Integer.MAX_VALUE;
        int ymax = Integer.MIN_VALUE;
        int SpaceWidth = (int)(GraphicPiece.WIDTH*1.7);
        for (int row = 0; row < 4; row++)
        {
            int x = xStart + hspace  * row;
            y = row * (SpaceWidth + vspace) + yStart ;
            for (int col = 0; col < 4; col++)
            {

                GraphicSpace s = new GraphicSpace(row,col);
                spaces[row][col] = s;
                int xcoord = x + s.getImage().getWidth()/2;
                int ycoord =  y - s.getImage().getHeight()/2 - vspace * col;
                addObject(s, x + s.getImage().getWidth()/2, y - s.getImage().getHeight()/2 - vspace * col);
                x += SpaceWidth + hspace;

            }

        }
        addObject(new Button("Undo"),700,470);
        addObject(new Button("New Game"),750,470);
        if (DEBUG)
        {
            addObject(new Button("Debug"),40,484);
        }
        addObject(new MsgBox(MsgBox.STATUS),400,15);
        addObject(new MsgBox(MsgBox.SCORE),722,33);
        officialBoard = new Board(availablePieces, spaces);
        lines = officialBoard.getLines();

        currPlayer = ((int)(Math.random() * 100))%2;

    }

    public void reset()
    {
        currPlayer = ((int)(Math.random() * 100))%2;  
        currPlayer = 1;
        currAction = SELECT;
        for (Space[] row : spaces)
            for (Space s : row)
            {

                Piece p = s.removePiece();
                if (p != null)
                {
                    GraphicPiece gp = (GraphicPiece) p;
                    gp.returnToStart(false);
                    availablePieces.add(gp);
                }
                GraphicSpace gs = (GraphicSpace) s;
                gs.unselect();
            }
        for (Piece p : availablePieces)
        {
            ((GraphicPiece) p).unselect();
            //System.out.println( "Avail:"+  ((GraphicPiece) p).isAvailable() + "  Selected:"+  ((GraphicPiece) p).isSelected() + " " + p );
        }

        if (players[currPlayer] instanceof ComputerPlayer)       
            letComputerPlay((ComputerPlayer) players[currPlayer]);
        moves = new ArrayList<Actor>();
        // System.out.println("In RESET.  availablePieces = " + availablePieces.size() +
        // " ");
        repaint();
    }

    public void debugOptions()
    {

       
    }

    public void testScenarios()
    {
        System.out.print("\f");
        String[] scenarios = IO.readFile("SavedBoards.txt");
        if (scenarios.length == 0) return;
        for (int s = 0; s < scenarios.length; s += 18)
        {
            String[] oneScenario = new String[18];
            for (int l = 0; l < 18; l++)
                oneScenario[l] = scenarios[s + l];
            testOneScenario(oneScenario);
        }
        reset();

    }

    public void testOneScenario(String[] linesFromFile)
    {
        String movesStr = linesFromFile[1];
        System.out.println(linesFromFile[0]);
        System.out.println("Available Pieces:  L/D=Lite/Dark  R/Q=Round/Square  T/S=Tall/Short   O/X=Hollow/Solid");
        for (int i = 2; i < 7; i++)
            System.out.println(linesFromFile[i]);
        System.out.println("Lines AS THEY SHOULD BE:");
        for (int i = 8; i < linesFromFile.length; i++)
            System.out.println(linesFromFile[i]);
        System.out.println("\nLines AS THEY ARE:");
        debugRestore( movesStr);

        for (Line l : lines)
        {
            System.out.println(l);

        }
        System.out.println("\n---------------------------------------------------------------------------");
    }

    public void dumpCurrentBoard()
    {
        System.out.print("\f");
        officialBoard.print();
        System.out.println("\nMoves:");
        System.out.println(movesToString());
        System.out.println("\nCurrent Player is " + currPlayer + " (Name=" + players[currPlayer].getName()+")");
        System.out.println("Player 0 (Name="+ players[0].getName() + ") optional debug dump: " );        
        players[0].printDebugInfo();
        System.out.println("Player 1 (Name="+ players[1].getName() + ") optional debug dump: " );        
        players[1].printDebugInfo();    
    }

    public void togglePlayer()
    {
        currPlayer = (currPlayer + 1) % 2;
    }

    public void toggleAction()
    {
        currAction = (currAction + 1) % 2;
    }

    private void letComputerPlay(ComputerPlayer cp)
    {
        Greenfoot.setSpeed(FASTSPEED);
        String msg = cp.getName();
        int action = getAction();
        //System.out.println("Official Board:");
        //officialBoard.print();
        Board cloneBoard = officialBoard.clone();
        //System.out.println("Cloned Board:");
        //cloneBoard.print();
        if (action == QuartoWorld.SELECT)
        {

            Piece toPlace = cp.selectPieceToPlace(cloneBoard);
            Piece correspondingPiece = null;
            for (Piece p : availablePieces)
                if (p.equals(toPlace))
                {
                    correspondingPiece = p;
                    break;
                }
            if (correspondingPiece != null)  
            {
                GraphicPiece gp = (GraphicPiece) correspondingPiece;
                choosePieceToPlace(gp);
            }
            else
                System.out.println( "Error with " + msg + " selecting piece");  

        }
        else //place
        {

            Space selSpace = cp.selectSpaceForPlacement(cloneBoard,pieceToPlace);
            Space correspondingSpace = null;
            for (Space[] row : spaces)
                for (Space s : row)
                    if (s.equals(selSpace))
                    {
                        correspondingSpace = s; 
                        break;
                    }

            if (correspondingSpace != null)  
            {
                GraphicSpace gs = (GraphicSpace) correspondingSpace;
                boolean isQuarto = placePiece(gs);

            }
            else
                System.out.println("Error with " + msg + " placing piece");  

        }
        Greenfoot.setSpeed(REGSPEED);
    }

    public int getPlayer() 
    {   return currPlayer; }

    public Player[] getPlayersArray()
    {   return players; }

    public int getAction()
    {   return currAction; }



    public void getPlayers()
    {
        players = promptForPlayers();
        
        if (splashShowing && players[0] instanceof ComputerPlayer && players[1] instanceof ComputerPlayer)
        {
            BattleSplash bs = new BattleSplash(this);
            addObject(bs,getWidth()/2 ,getHeight()/2+10);
            if (playMusic)
            {
                if (players[0].getName().indexOf("Swift") >= 0
                 || players[1].getName().indexOf("Swift") >= 0)
                 battleMusic = new GreenfootSound("BattleMusic2.mp3");
                else if (Math.random() <= 0.5)
                
                    battleMusic = new GreenfootSound("BattleMusic.mp3");
                else
                    battleMusic = new GreenfootSound("BattleMusic1.mp3");
                
                battleMusic.play();
            }
            repaint();    
            // while(!getObjects(BattleSplash.class).isEmpty())
            // {
                // System.out.println("have battle splash");
            // }
        }
        else if (players[currPlayer] instanceof ComputerPlayer)  
        {     
            letComputerPlay((ComputerPlayer) players[currPlayer]);
            repaint();
        }
    }
    public void startBattle()
    {
        if (players[currPlayer] instanceof ComputerPlayer)  
        {     
            letComputerPlay((ComputerPlayer) players[currPlayer]);
            repaint();
        }
    }
    private int convertToInt(String str)
    {
        int convertedInt = -1;
        try {
            convertedInt = Integer.parseInt(str);
        } 
        catch (NumberFormatException e) {

            convertedInt = -1;
        }
        return convertedInt;
    }

    public boolean noPiecesAvailable()
    {
        return availablePieces.size() == 0;
    }

    public boolean checkForQuarto()
    {
        if (!officialBoard.checkForQuarto())
            return false;
        //boolean quartoFound = false;
        for (Line l : lines)
            if (l.isQuarto())
            {
                //quartoFound = true;
                highLightLine(l);
            }
        return true;
        //return quartoFound;
    }

    public void highLightLine(Line line)
    {
        for (Space s : line.getSpaces())
            if (s.getPiece() != null)
                ((GraphicPiece) s.getPiece()).select();
    }

    public void unhighLightLine(Line line)
    {
        for (Space s : line.getSpaces())
            if (s.getPiece() != null)
                ((GraphicPiece) s.getPiece()).unselect();
    }   

    public ArrayList<Piece> getAvailablePieces() 
    {   return availablePieces; }

    public void debugRestore(String movesStr)
    {
        int restoreCurrPlayer = Integer.parseInt(movesStr.substring(0,1));
        if (movesStr.length() < 2)
            movesStr = "";
        else
            movesStr = movesStr.substring(2);
        String[] moveStrings = movesStr.split("-");
        reset();

        GraphicPiece pieceToPlace = null;
        for (String m : moveStrings)
        {
            if (m.indexOf("(") != 0)  // select
                pieceToPlace = debugFindPieceToPlace(m);
            else
                debugPlacePiece(m, pieceToPlace);
        }
        currPlayer = restoreCurrPlayer;
    }

    public void debugRestore()
    {
        String[] lines = IO.readFile("BoardSaved.txt");
        if (lines.length == 0) return;
        String movesStr = lines[0];
        debugRestore(movesStr);

        // int restoreCurrPlayer = Integer.parseInt(movesStr.substring(0,1));
        // movesStr = movesStr.substring(2);
        // String[] moveStrings = movesStr.split("-");
        // reset();
        // GraphicPiece pieceToPlace = null;
        // for (String m : moveStrings)
        // {
        // if (m.indexOf("(") != 0)  // select
        // pieceToPlace = debugFindPieceToPlace(m);
        // else
        // debugPlacePiece(m, pieceToPlace);
        // }
        // currPlayer = restoreCurrPlayer;

    }

    public void debugPlacePiece(String spaceStr, GraphicPiece pieceToPlace)
    {
        spaceStr = spaceStr.trim();
        spaceStr = spaceStr.substring(1,spaceStr.length()-1);
        String[] coordStr = spaceStr.split(",");
        int xcoord = Integer.parseInt(coordStr[0]);
        int ycoord = Integer.parseInt(coordStr[1]);
        Space searchForSpace = new Space(xcoord, ycoord);
        GraphicSpace  selectedSpace = null;
        for (int r = 0; r < 4; r++)
            for (int c = 0; c < 4; c++)
                if (spaces[r][c].equals(searchForSpace))
                {
                    selectedSpace = (GraphicSpace) spaces[r][c];                    
                }

        if (pieceToPlace == null) return ;
        if (selectedSpace == null) return ;

        pieceToPlace.unselect();
        selectedSpace.unselect();
        pieceToPlace.setAvailable(false);
        availablePieces.remove(pieceToPlace);
        selectedSpace.receivePiece(pieceToPlace);
        int y  = selectedSpace.getY() + selectedSpace.getImage().getHeight() / 2; 
        y -= pieceToPlace.getImage().getHeight() / 2;
        y -= 7;
        pieceToPlace.setLocation(selectedSpace.getX(), y);
        //pieceToPlace.startAnimation​(selectedSpace.getX(), y);

        pieceToPlace = null;
        moves.add(selectedSpace);

        toggleAction();
        repaint();

    }

    public GraphicPiece debugFindPieceToPlace(String pieceID)
    {
        Piece pieceToFind = new Piece(pieceID);
        GraphicPiece gp = null;
        for (Piece p : availablePieces)
        {
            if (! (p instanceof GraphicPiece) )  break;

            gp = (GraphicPiece) p;
            if (gp.equals(pieceToFind))
            {
                //System.out.println("Looking for " + pieceID + ".  Found it " +
                //    " in piece " + gp.toString());
                gp.select();
                pieceToPlace = gp;
            }
            else
                gp.unselect();
        }
        moves.add(pieceToPlace);
        toggleAction();
        return (GraphicPiece) pieceToPlace;
    }

    public void choosePieceToPlace(GraphicPiece selectedPiece)
    {
        if (currAction != SELECT) return;
        pieceToPlace = selectedPiece;
        for (Piece p : availablePieces)
        {
            if (! (p instanceof GraphicPiece) )  break;

            GraphicPiece gp = (GraphicPiece) p;
            if (gp.equals(selectedPiece))
                gp.select();
            else
                gp.unselect();
        }
        moves.add(pieceToPlace);
        toggleAction();
        togglePlayer();
        repaint();
        if (players[currPlayer] instanceof ComputerPlayer)       
            letComputerPlay((ComputerPlayer) players[currPlayer]);
        repaint();

    }

    public void highlightSpaceToPlace(GraphicSpace selectedSpace)
    {
        for (Space[] row : spaces)
            for (Space s : row)

                if (s == selectedSpace)
                    ((GraphicSpace)s).select();
                else
                    ((GraphicSpace)s).unselect();
        repaint();

    }    

    public boolean placePiece(GraphicSpace  selectedSpace)
    {
        if (pieceToPlace == null) return false;
        if (selectedSpace == null) return false;
        if (currAction != PLACE) return false;
        pieceToPlace.unselect();
        selectedSpace.unselect();
        pieceToPlace.setAvailable(false);
        availablePieces.remove(pieceToPlace);
        selectedSpace.receivePiece(pieceToPlace);
        int y  = selectedSpace.getY() + selectedSpace.getImage().getHeight() / 2; 
        y -= pieceToPlace.getImage().getHeight() / 2;
        y -= 7;
        pieceToPlace.setLocation(selectedSpace.getX(), y);
        //pieceToPlace.startAnimation​(selectedSpace.getX(), y);

        pieceToPlace = null;
        moves.add(selectedSpace);
        boolean quarto = checkForQuarto()  ;
        boolean endOfGame = quarto || noPiecesAvailable();
        if ( quarto )
        {
            players[currPlayer].addWin();
            games++;

        }
        else if (endOfGame)
        {
            repaint(); 
            games++;
        }
        else
        {
            toggleAction();
            repaint();
            if (players[currPlayer] instanceof ComputerPlayer && !noPiecesAvailable())       
                letComputerPlay((ComputerPlayer) players[currPlayer]);

        }
        repaint();
        return quarto;

    }

    public int getGamesPlayed()
    {   return games; }

    public Player getCurrPlayer()
    {   return players[currPlayer];  }

    public Player getOtherPlayer()
    {   return players[(currPlayer + 1) % 2];  }

    public void undo()
    {
        if (moves.size() == 0) return;
        if (noPiecesAvailable() || checkForQuarto()) return;
        Actor undoItem = moves.remove(moves.size() - 1);
        Space undoSpace = null;

        if (undoItem instanceof Space)
        {
            GraphicPiece undoPiece = ((GraphicSpace)undoItem).undo();

            if (undoPiece == null) return;
            pieceToPlace = undoPiece;
            availablePieces.add(undoPiece);
            undoPiece.returnToStart();
            toggleAction();
            checkForQuarto();
        }
        else if (undoItem instanceof Piece)
        {
            // ((Piece)undoItem).resetAvailable();
            togglePlayer();
            toggleAction();

            ((GraphicPiece)undoItem).unselect();

        }
    }
    // private ArrayList<Actor> moves = new ArrayList<Actor>();
    // private Player[] players = new Player[2];
    // private GraphicPiece pieceToPlace = null;
    // private int currPlayer = 1;

    public String movesToString()
    {
        String retStr = currPlayer+"";
        for (Actor m : moves)
            retStr += "-" + m.toString(); 

        return retStr;
    }

    public boolean pieceAlreadySelected()
    {
        for (Piece p : availablePieces)
        {
            if (!(p instanceof GraphicPiece)) break;
            GraphicPiece pg = (GraphicPiece) p;
            if (pg.isSelected())
                return true;
        }
        return false;
    }
    public int getCompVcompGamesToPlay()
    {
        return compVcompGamesToPlay;
    }
    public void resetCompVcompGamesToPlay()
    {
        compVcompGamesToPlay = initialcompVcompGamesToPlay;
    }
    public void act()
    {
        if ((checkForQuarto() || noPiecesAvailable()) && compVcompGamesToPlay > 1)
        {
            compVcompGamesToPlay--;
            
            if (compVcompGamesToPlay == 1 && playMusic && battleMusic.isPlaying())
            {
                battleMusic.stop();
            }
            reset();
        }
    }

    public boolean doAnimation()
    {   return animate; }

    public void setAnimation(boolean animate)
    {   this.animate = animate; }

    public void toggleAnimation()
    {   animate = !animate; }
    
    public void started()
    {
        splashScreen();
        
       // getPlayers();
    }
    public void splashScreen()
    {
        PlayerTypeMsg ptmsg = new PlayerTypeMsg(getWidth(),getHeight());
        addObject(ptmsg,getWidth()/2 ,getHeight()/2+10);
        repaint();        
    }
    public  Player[] promptForPlayersNoDialog()
    {
        
        Player[] players = new Player[2];
        String[] names = new String[2];
        int[] pTypes = new int[2];
        String validTypes = "H"+ PlayerClassNamesInitials;
        for (int i = 0; i < 2; i++)
        {
            String prompt = "Type for Player " + (i+1) + "  (H=Human";
            for (int c = 0; c < PlayerClassNames.length; c++)
            {

                prompt += ", " + PlayerClassNamesInitials.substring(c, c+1) + "=" + PlayerClassShortNames[c];

            }
            prompt += ")";
            String type = "";
            while(true){
                type = Greenfoot.ask(prompt).toUpperCase();
                if (validTypes.indexOf(type) >= 0)
                    break;
                else if (prompt.indexOf("Invalid") != 0)
                    prompt = "Invalid Entry. " + prompt;
            }
            pTypes[i] = validTypes.indexOf(type) - 1;

        }
        if (pTypes[0] == -1 && pTypes[1] == -1)
        {
            names[0] = "Player 1";
            names[1] = "Player 2"; 
        }
        else
        {
            for (int i = 0; i < 2; i++)
            {
                if (pTypes[i] == -1)
                    names[i] = "Player";
                else
                     
                    names[i] = PlayerClassShortNames[pTypes[i]];

            }
            if (pTypes[0] == pTypes[1])
            {
                
                names[0] = (names[0]+"       ").substring(0,6).trim()  + " 1";  
                names[1] = (names[1]+"       ").substring(0,6).trim()  + " 2";  
            }
        }
        players[0] = customizePlayer( names[0], pTypes[0]);            
        players[1] = customizePlayer( names[1], pTypes[1]);        
        if (pTypes[0] > -1 && pTypes[1] > -1)
        {
            String prompt = "Enter number of automated games: ";

            while(true){
                compVcompGamesToPlay = convertToInt(Greenfoot.ask(prompt).trim());
                initialcompVcompGamesToPlay = compVcompGamesToPlay;
                if (compVcompGamesToPlay >= 0)
                    break;
                else if (prompt.indexOf("Invalid") != 0)
                    prompt = "Invalid Entry. " + prompt;
            }
           
        }
        // private static final String[] PlayerClassNames = { "Dumb Computer", "Quarto Hound", "My Computer Play", "My Other Player"};
        // private static final String   PlayerClassNamesInitials = "DQMO";

        return players;
    }

    public  Player[] promptForPlayers()
    {

            return promptForPlayersNoDialog();


    }

    private static Player customizePlayer(String name, int classIdx)
    {
        // private static final Class[] PlayerClasses = {Player.class, ComputerPlayer.class};
        //private static final String[] PlayerClassNames = {"Human Player", "Dumb Computer Player"};
        if (classIdx == 0)
            return new ComputerPlayer(name);
        else if (classIdx == 1)
            return new PlyQuartoHound(name);
        else if (classIdx == 2)
            return new CPFraniJulie(name);
        else if (classIdx == 3)
            return new CPGloriaMaygala1(name);
        else if (classIdx == 4)
            return new CPGloriaMaygala2(name);
        else if (classIdx == 5)
            return new CPJimmy(name);
        else if (classIdx == 6)
            return new CPJonahRonan(name);
        else if (classIdx == 7)
            return new CPNik(name);
        else if (classIdx == 8)
            return new CPMrLeib(name);
        else if (classIdx == 9)
            return new CPShinyi(name);
        else if (classIdx == 10)
            return new CPBen(name);

        else
            return new Player(name, false);
    }
}
