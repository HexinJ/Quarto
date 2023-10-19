import java.util.*;
/**
 *  
 */
public class Board  
{
    private ArrayList<Piece> availablePieces = new ArrayList<Piece>();
    private Space[][] spaces = new Space[4][4];
    private ArrayList<Line> lines = new ArrayList<Line>();

    /**
     * Constructor for objects of class Board
     */
    public Board(ArrayList<Piece> availablePieces, Space[][] spaces)
    {
        this.availablePieces = availablePieces;
        this.spaces = spaces;
        this.lines = generateLinesFromSpaces(spaces);
        for (int i = 0; i < lines.size(); i++)
            lines.get(i).setLineNumber(i);
    }

    /**
     * STUDENTS MUST COMPLETE THIS METHOD
     * 
     * Given a two dimensional array of Space objects
     * return an Array of lines.
     * 
     * each line should represent one of the ten ways of making quarto
     * - there should be four horizontal lines, 
     *   four vertical lines, and
     *   two diagonal lines.
     *   
     *   You should use the Line(Space[] spaces) constructor when
     *   instantiating a new Line object to add to the ArrayList that
     *   you are returning
     * 
     */
    private ArrayList<Line> generateLinesFromSpaces(Space[][] spaces)
    {
        //Initialize ArrayList of Lines
        ArrayList<Line> lines = new ArrayList<Line>();

        //Generate all the Horizontal Lines
        for (Space[] horizLine : spaces)
            lines.add(new Line(horizLine));

        //Generate all the Vertical Lines
        for (int col = 0; col < 4; col++)
        {
            Line vertLine = new Line();
            for (int row = 0; row < 4; row++)
                vertLine.addSpace(spaces[row][col]);
            lines.add(vertLine);
        }

        //Generate the two diagonal lines
        Line diag1Line = new Line();
        Line diag2Line = new Line();
        for (int i = 0; i < 4; i++)
        {
            diag1Line.addSpace(spaces[i][i]);
            diag2Line.addSpace(spaces[i][3-i]);
        }  
        lines.add(diag1Line);
        lines.add(diag2Line);

        //return the List of lines
        return lines;
    }

    
    /**
     * Don't touch anything beyond here
     */
    public ArrayList<Piece> getAvailablePieces()
    {
        return availablePieces;
    }

    public Space[][] getSpaces()
    {
        return spaces;
    }

    public ArrayList<Space> getAvailableSpaces()
    {

        ArrayList<Space> availableSpaces = new ArrayList<Space>();
        for (Space[] row : spaces)
            for (Space s : row)
                if (s.isEmpty())
                    availableSpaces.add(s);
        return availableSpaces;
    }

    public ArrayList<Line> getLines()
    {
        return lines;
    }

    public boolean checkForQuarto()
    {
        boolean quartoFound = false;
        for (Line l : lines)
            if (l.isQuarto())
            {
                quartoFound = true;

            }

        return quartoFound;
    }

    /**
     * return values
     *  -1 = invalid Piece (either not available or invalid)
     *  -2 = invalid Space (either not empty or invalid)
     */

    public int tryToPlacePiece(Piece pieceToPlace, Space spaceToPlace)
    {
        int pieceIdx = -1;
        for (int i = 0; i < availablePieces.size(); i++)
            if (availablePieces.get(i).equals(pieceToPlace))
            {
                pieceIdx = i;
                break;
            }
        if (pieceIdx == -1)
            return -1;
        pieceToPlace = availablePieces.get(pieceIdx);
        if (spaceToPlace == null)
            return -2;
        int r = spaceToPlace.getRow(); 
        int c = spaceToPlace.getCol();
        if (r < 0 || r > 3 || c < 0 || c > 3)   
            return -2;
        Space landingSpace = spaces[r][c];
        if (!landingSpace.isEmpty())
            return -2;
        landingSpace.receivePiece(availablePieces.remove(pieceIdx));
        return 0;
    }

    /**
     * return values
     *  -1 = invalid Piece (cannot be found in Spaces)
     *  -2 = invalid Space (either not empty or invalid)
     */
    public int tryToRemovePiece(Piece pieceToPlace)
    {
        Space spaceToRemove = null;
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++)
                if (spaces[r][c].getPiece().equals(pieceToPlace))
                {
                      spaceToRemove  =spaces[r][c];
                      break;
                }
        if (spaceToRemove == null)
            return -1; 
        
         
        if (spaceToRemove.isEmpty())
            return -2;
        availablePieces.add(spaceToRemove.removePiece());
        return 0;
    }   
    /**
     * return values
     *  -1 = invalid Piece (either not available or invalid)
     *  -2 = invalid Space (either not empty or invalid)
     */
    public int tryToRemovePiece(Space spaceToRemove)
    {
 
        if (spaceToRemove == null)
            return -2;
        int r = spaceToRemove.getRow(); 
        int c = spaceToRemove.getCol();
        if (r < 0 || r > 3 || c < 0 || c > 3)   
            return -2;
        Space landingSpace = spaces[r][c];
        if (landingSpace.isEmpty())
            return -2;
        availablePieces.add(landingSpace.removePiece());
        return 0;
    }    
    
    public Board clone()
    {
        ArrayList<Piece> cloneAvailablePieces = new ArrayList<Piece>();
        for (Piece p : availablePieces)
            cloneAvailablePieces.add(p.clone());
        Space[][] cloneSpaces = new Space[4][4];
        for (int r = 0; r < 4; r++)
            for (int c = 0; c < 4; c++)
                cloneSpaces[r][c] = spaces[r][c].clone();
        return new Board(cloneAvailablePieces, cloneSpaces);

    }

    public void print()
    {

        System.out.println("Quarto in the board = " + checkForQuarto());
        System.out.println("Available Pieces:  L/D=Lite/Dark  R/Q=Round/Square  T/S=Tall/Short   O/X=Hollow/Solid");
        for (Piece p : availablePieces)
            System.out.println(p);
        System.out.println("\nSpaces:");
        //Space[][] cloneSpaces = new Space[4][4];
        for (int r = 0; r < 4; r++)
        {
            for (int c = 0; c < 4; c++)
            {
                Piece p = spaces[r][c].getPiece();
                if (p != null)
                    System.out.print(spaces[r][c] + ":" + p + " \t");    
                else
                    System.out.print(spaces[r][c] + ":" + "----\t" );
            }
            System.out.println( );
        }
        System.out.println("\nLines:");
        int ln = 0;
        for (Line l : lines)
        {
            System.out.println(l);

        }

        System.out.println("\nLines each Space is part of:");
        for (int r = 0; r < 4; r++)
        {
            for (int c = 0; c < 4; c++)
            {
                System.out.println(spaces[r][c] + " belongs to the following lines:");

                ArrayList<Line> lines = spaces[r][c].getMyLines();
                for (Line l : lines)
                    System.out.println("\t"+ l);
            }

        }

    }
}
