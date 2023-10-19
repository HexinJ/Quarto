import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class CPBen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPBen extends ComputerPlayer
{
    private Piece lastPiecePlayed;
    private int phase;
    public CPBen(String name)
    {
        super(name);
        //System.out.println("Instantiating Ben's Computer Player");
    }

    public Piece selectPieceToPlace(Board b)
    {
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        ArrayList<Piece> availablePieces = b.getAvailablePieces();
        ArrayList<Line> lines = b.getLines();
        //dictionaries mapping lines with same attributes to the tyes of attributes
        Map<Line, ArrayList<Integer>> linesWithTwo = populate(lines, 2);
        Map<Line, ArrayList<Integer>> linesWithThree = populate(lines, 3);

        Boolean[] restrictedAttributes = getRestrictedAttributes(linesWithThree); //key = category, value = true or false
        //System.out.println("Restricted Attributes: " + Arrays.toString(restrictedAttributes));

        if(availableSpaces.size() >= 15 || (linesWithTwo.size() < 1 && linesWithThree.size() < 1))//early game
        {
            phase = 0;            
        }
        else
        {
            phase = 1;            
        }

        if(phase == 0)
        {            
            if(availableSpaces.size() <= 15 && lastPiecePlayed != null)
            {
                //giving the opponent the exact opposite piece
                Piece oppositePiece = new Piece(!lastPiecePlayed.is(0), !lastPiecePlayed.is(1), !lastPiecePlayed.is(2), !lastPiecePlayed.is(3));
                if(isAvailable(b, oppositePiece))
                {
                    return oppositePiece;
                }
            }
            else
            {
                return super.selectPieceToPlace(b);
            }            
        }
        else if(phase == 1)
        {
            HashMap<Piece, Integer> piecesAndScores = generateScores(b, availableSpaces, availablePieces, restrictedAttributes);
            Piece pieceWithHighestScore = getHighestScore(piecesAndScores);
            return pieceWithHighestScore;
        }
        //System.out.println("I AM ERROR");
        return super.selectPieceToPlace(b);
    }

    public Space selectSpaceForPlacement(Board b, Piece p)
    {
        ArrayList<Space> availableSpaces = b.getAvailableSpaces();
        ArrayList<Piece> availablePieces = b.getAvailablePieces();
        ArrayList<Line> lines = b.getLines();
        //dictionaries mapping lines with same attributes to the tyes of attributes
        Map<Line, ArrayList<Integer>> linesWithTwo = populate(lines, 2);
        Map<Line, ArrayList<Integer>> linesWithThree = populate(lines, 3);
        Boolean[] restrictedAttributes = getRestrictedAttributes(linesWithThree);
        if(availableSpaces.size() >= 15 || (linesWithTwo.size() < 1 && linesWithThree.size() < 1))//early game
        {
            phase = 0;
        }
        else
        {
            phase = 1;
        }

        if(phase == 0)
        //in the early game, place the piece randomly
        {
            return super.selectSpaceForPlacement(b, p);
        }
        else if(phase == 1 || phase == 2)
        //in the mid game, place the piece that can create the most lines of two and three without leaving myself with
        //a two line situation
        {
            HashMap <Space, Integer> spacesAndScores = generateScoreForSpaces(b, p);
            //System.out.println(spacesAndScores.toString());
            return getHighestScoreForSpace(spacesAndScores);
        }
        //System.out.println("I AM ERROR");
        return super.selectSpaceForPlacement(b, p);
    }

    public Space getHighestScoreForSpace(HashMap <Space, Integer> spacesAndScores)
    {
        Space[] spaces = new Space[spacesAndScores.size()];
        int[] scores =  new int[spacesAndScores.size()];
        int index = 0;
        Iterator myIterator = spacesAndScores.entrySet().iterator();
        while (myIterator.hasNext()) 
        {
            Map.Entry mapElement = (Map.Entry)myIterator.next();
            spaces[index] = (Space)mapElement.getKey();
            scores[index] = (int)mapElement.getValue();
            index++;
        }

        int largestIdx = 0;
        for ( int i = 1; i < scores.length; i++ )
        {
            if ( scores[i] > scores[largestIdx] ) 
            {
                largestIdx = i;
            }
        }

        int largestScore = scores[largestIdx];
        //System.out.println("Largest score for space: " + largestScore);
        return spaces[largestIdx];
    }

    public HashMap <Space, Integer> generateScoreForSpaces(Board b, Piece p)
    {
        //blocking space thing
        //has two contradicting lines
        HashMap <Space, Integer> toReturn = new HashMap <Space, Integer>();
        ArrayList <Space> s = b.getAvailableSpaces();
        ArrayList <Line> availableLines = b.getLines();
        Map<Line, ArrayList<Integer>> threeLines = populate(availableLines, 3);
        Boolean[] restrictedAttributes = getRestrictedAttributes(threeLines);

        ArrayList <Space> blockingSpaces = new ArrayList <Space>();
        //No matter what I will find all the spaces I can select to block with, and add score for all the spaces
        //to increase their priority. This could be an empty arraylist

        blockingSpaces = getBlockingSpaces(b, p);

        for(Space space:s)
        {
            int score = 0;
            if(blockingSpaces.indexOf(space) != -1)
            {
                //System.out.println("Detected a blocking space needed: " + space.toString());
                score += 10000;
            }
            //cloning the board and placing the piece
            Board clonedBoard = b.clone();            
            clonedBoard.tryToPlacePiece(p, space);
            ArrayList<Line> lines = clonedBoard.getLines();
            //if there is a quarto on the board then it will skip this piece
            if(clonedBoard.checkForQuarto())
            {
                score += 1000000;
                //System.out.println("Quarto!");
                toReturn.put(space, score);
                break;
            }
            else if(hasTwoContradictingLines(clonedBoard))
            {
                //System.out.println("Detected potential twoLine" + space.toString());
                score -= 1000000;
                toReturn.put(space, score);

            }
            else
            {
                //for each line of two that this piece could form when put into one space, score is added
                //the category that is same in the line of two * 2
                Map<Line, ArrayList<Integer>> linesWithTwo = populate(lines, 2);
                Iterator twoIterator = linesWithTwo.entrySet().iterator();
                while (twoIterator.hasNext()) 
                {
                    Map.Entry mapElement = (Map.Entry)twoIterator.next();
                    ArrayList <Integer> numCat = (ArrayList)mapElement.getValue();
                    score += numCat.size() * 2;
                }
                //for each line of three that this piece could form when put into one space, score is added
                //the category that is same in the line of three * 5
                Map<Line, ArrayList<Integer>> linesWithThree = populate(lines, 3);         
                Iterator threeIterator = linesWithThree.entrySet().iterator();
                while (threeIterator.hasNext()) 
                {
                    Map.Entry mapElement = (Map.Entry)threeIterator.next();
                    ArrayList <Integer> numCat = (ArrayList)mapElement.getValue();
                    score += numCat.size() * 5;
                }                    
            }
            toReturn.put(space, score);
        }
        return toReturn;
    }

    public ArrayList <Space> getBlockingSpaces(Board b, Piece p)
    {        
        ArrayList <Line> availableLines = b.getLines();
        Map<Line, ArrayList<Integer>> linesWithThree = populate(availableLines, 3);
        Boolean[] restrictedAttributes = getRestrictedAttributes(linesWithThree);

        //the arraylist of leftover pieces
        ArrayList <Piece> availablePieces = b.getAvailablePieces();

        //trimming the restrictedAttributes so that the non-null elements are the elements that needs to be blocked
        for(int i = 0; i < restrictedAttributes.length && restrictedAttributes[i] != null; i++)
        {
            for(Piece piece:availablePieces)
            {
                if(p.is(0) != piece.is(0) || p.is(1) != piece.is(1) || p.is(2) != piece.is(2) || p.is(3) != piece.is(3))
                {
                    if(piece.is(i) != restrictedAttributes[i])
                    {
                        restrictedAttributes[i] = null;
                        break;
                    }
                }
            }
        }
        //lines that I need to block
        ArrayList <Line> linesToBlock = new ArrayList <Line>();
        //detecting which lines has those non-null elements that needs to be blocked and returning the space to put
        for(int i = 0; i < restrictedAttributes.length && restrictedAttributes[i] != null; i++)
        {
            for (Map.Entry<Line,ArrayList<Integer>> entry : linesWithThree.entrySet()) 
            {
                Line thisLine = (Line) entry.getKey();
                ArrayList <Integer> attributes = (ArrayList) entry.getValue();
                if(attributes.indexOf(i) != -1 && linesToBlock.indexOf(thisLine) == -1)
                {
                    linesToBlock.add(thisLine);
                }
            }
        }
        //spaces that I need to choose one from to block
        ArrayList <Space> toReturn = new ArrayList <Space>();
        for(Line line:linesToBlock)
        {
            Space[] spaces = line.getSpaces();
            for(Space space:spaces)
            {
                if(space.isEmpty() && toReturn.indexOf(space) == -1)
                {
                    toReturn.add(space);
                    break;
                }
            }
        }
        return toReturn;
    }

    public Boolean[] getRestrictedAttributes(Map<Line, ArrayList<Integer>> linesWithThree)
    {
        Boolean[] toReturn = new Boolean[4];
        Iterator myIterator = linesWithThree.entrySet().iterator();
        while(myIterator.hasNext())
        {
            Map.Entry mapElement = (Map.Entry)myIterator.next();
            ArrayList <Integer> categories = (ArrayList) mapElement.getValue();
            Line line = (Line) mapElement.getKey();
            Space[] spaces = line.getSpaces();
            Piece firstPiece = new Piece(false,false,false,false);
            for(Space space:spaces)
            {
                if(space.isOccupied())
                {
                    firstPiece = space.getPiece();
                    break;
                }
            }
            for(int i:categories)
            {
                toReturn[i] = firstPiece.is(i);
            }
        }
        return toReturn;
    }

    public HashMap <Piece, Integer> generateScores(Board b, ArrayList <Space> s, ArrayList <Piece> p, Boolean[] restrictedAttributes)
    {
        HashMap<Piece, Integer> toReturn = new HashMap<Piece, Integer>();
        for(Piece piece:p)
        {
            int score = 0;
            boolean isBlocking = isForceBlocking(b, piece, restrictedAttributes);
            if(isBlocking)
            {
                //System.out.println("Detected a force block situation");
                Board blockedBoard = getBlockedBoard(b, piece, restrictedAttributes);
                ArrayList <Piece> availablePiecesForMe = b.getAvailablePieces();

                if(quartoNoMatterWhat(blockedBoard, availablePiecesForMe, piece))
                {
                    //System.out.println("Quarto for me no matter what");
                    score += 1000000;
                }
                else
                {
                    score += 50;
                }
            }   
            for(Space space:s)
            {
                //cloning the board and placing the piece
                Board clonedBoard = b.clone();            
                clonedBoard.tryToPlacePiece(piece, space);
                ArrayList<Line> lines = clonedBoard.getLines();
                //if there is a quarto on the board then it will skip this piece
                if(clonedBoard.checkForQuarto())
                {
                    score = -1000000;
                    break;
                }
                else
                {
                    //for each line of two that this piece could form when put into one space, score is added
                    //the category that is same in the line of two * 2
                    Map<Line, ArrayList<Integer>> linesWithTwo = populate(lines, 2);
                    Iterator twoIterator = linesWithTwo.entrySet().iterator();
                    while (twoIterator.hasNext()) 
                    {
                        Map.Entry mapElement = (Map.Entry)twoIterator.next();
                        ArrayList <Integer> numCat = (ArrayList)mapElement.getValue();
                        score += numCat.size() * 2;
                    }
                    //for each line of three that this piece could form when put into one space, score is added
                    //the category that is same in the line of three * 5
                    Map<Line, ArrayList<Integer>> linesWithThree = populate(lines, 3);         
                    Iterator threeIterator = linesWithThree.entrySet().iterator();
                    while (threeIterator.hasNext()) 
                    {
                        Map.Entry mapElement = (Map.Entry)threeIterator.next();
                        ArrayList <Integer> numCat = (ArrayList)mapElement.getValue();
                        score += numCat.size() * 5;
                    }                    
                }
            }
            //adding the pair of Piece and its score to the map
            toReturn.put(piece, score);
        }
        return toReturn;
    }

    public Map<Line, ArrayList<Integer>> populate(ArrayList<Line> lines, int numSame)
    {
        Map<Line, ArrayList<Integer>> toReturn = new HashMap<Line, ArrayList<Integer>>();
        for(Line line:lines)
        {
            ArrayList<Piece> pieces = new ArrayList<Piece>();
            Space[] spaces = line.getSpaces();
            for(Space space:spaces)
            {
                if(space.isOccupied())
                {
                    pieces.add(space.getPiece());
                }
            }
            ArrayList <Integer> sameAttributes = sameAttributes(pieces);
            if(pieces.size() == numSame && sameAttributes.size() > 0)
            {
                toReturn.put(line, sameAttributes);
            }           
        }
        return toReturn;
    }

    public Piece getHighestScore(HashMap <Piece, Integer> piecesAndScores)
    {
        Piece[] pieces = new Piece[piecesAndScores.size()];
        int[] scores =  new int[piecesAndScores.size()];
        int index = 0;
        Iterator myIterator = piecesAndScores.entrySet().iterator();
        while (myIterator.hasNext()) 
        {
            Map.Entry mapElement = (Map.Entry)myIterator.next();
            pieces[index] = (Piece)mapElement.getKey();
            scores[index] = (int)mapElement.getValue();
            index++;
        }

        int largestIdx = 0;
        for ( int i = 1; i < scores.length; i++ )
        {
            if ( scores[i] > scores[largestIdx] ) largestIdx = i;
        }

        int largestScore = scores[largestIdx];
        //System.out.println("Largest score for piece: " + largestScore);
        return pieces[largestIdx];
    }

    public ArrayList<Integer> sameAttributes(ArrayList<Piece> pieces)
    {
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        boolean[] isAttribute = {true, true, true, true};
        for(Piece piece:pieces)

            for (int i = Piece.DARK; i <= Piece.HOLLOW; i++)
                if(piece.is(i) != pieces.get(0).is(i))
                    isAttribute[i] = false;
        for (int i = 0; i < isAttribute.length; i++)
            if(isAttribute[i])
                toReturn.add(i);

        return toReturn;
    }

    public boolean isAvailable(Board b, Piece piece)
    {
        return b.getAvailablePieces().contains(piece);
    }

    public boolean isForceBlocking(Board b, Piece piece, Boolean[] r)//r is restricted attributes
    {
        ArrayList <Piece> availablePieces = b.getAvailablePieces();

        //check if there is a block situation on the board
        for(Piece p:availablePieces)
        {
            if(p.is(0) != piece.is(0) || p.is(1) != piece.is(1) || p.is(2) != piece.is(2) || p.is(3) != piece.is(3))
            {
                boolean hasRestrictedAttribute = false;
                for(int i = 0; i < r.length; i++)
                {
                    if(r[i] != null && p.is(i) == r[i])
                    {
                        hasRestrictedAttribute = true;
                    }
                }
                if(!hasRestrictedAttribute)
                {
                    return false;
                }

            }
        }
        return true;
    }

    public Board getBlockedBoard(Board b, Piece p, Boolean[] r)
    {
        ArrayList <Piece> availablePieces = b.getAvailablePieces();
        ArrayList <Line> lines = b.getLines();
        Space theSpace = new Space(0,0);
        for(Line l:lines)
        {
            Space[] spaces = l.getSpaces();
            for(Space s:spaces)
            {
                Board clonedBoard = b.clone();
                if(availablePieces.get(0) != p)
                    clonedBoard.tryToPlacePiece(availablePieces.get(0), s);
                else
                    clonedBoard.tryToPlacePiece(availablePieces.get(1), s);
                if(clonedBoard.checkForQuarto())
                {
                    theSpace = s;
                    Board newClone = b.clone();
                    newClone.tryToPlacePiece(p, theSpace);
                    return newClone;
                }
            }
        }
        //System.out.println("I AM ERROR WHEN GETTING BLOCKED BOARD");
        return b;
    }

    public boolean quartoNoMatterWhat(Board b, ArrayList <Piece> p, Piece currentPiece)
    {
        ArrayList <Line> lines = b.getLines();
        Map<Line, ArrayList<Integer>> linesWithThree = populate(lines, 3);
        if(linesWithThree.size() < 1)
        {
            return false;
        }
        Boolean[] restrictedAttributes = getRestrictedAttributes(linesWithThree);
        if(hasTwoContradictingLines(b))
        {
            return true;
        }
        for(Piece piece:p)
        {
            if(currentPiece.is(0) != piece.is(0) || currentPiece.is(1) != piece.is(1) || currentPiece.is(2) != piece.is(2) || currentPiece.is(3) != piece.is(3))
            {
                boolean canGetQuarto = false;
                for(int i = 0; i < restrictedAttributes.length; i++)
                {
                    if(restrictedAttributes[i] != null && piece.is(i) == restrictedAttributes[i])
                    {
                        canGetQuarto = true;
                        break;
                    }
                }
                if(!canGetQuarto)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasTwoContradictingLines(Board b)
    {
        ArrayList <Line> lines = b.getLines();
        Map <Line, ArrayList <Integer>> linesWithThree = populate(lines, 3);
        ARBoolean[] attributes = new ARBoolean[4];

        // ArrayList <Boolean> [] attributes = new ArrayList <Boolean>() [4];
        for(int i = 0; i < attributes.length; i++)
        {
            attributes[i] = new ARBoolean();

            //new ArrayList <Boolean>();
        }
        Iterator myIterator = linesWithThree.entrySet().iterator();
        while(myIterator.hasNext())
        {
            Map.Entry mapElement = (Map.Entry)myIterator.next();
            ArrayList <Integer> a = (ArrayList) mapElement.getValue();
            Space[] spaces = ((Line) mapElement.getKey()).getSpaces();
            Piece firstPiece = new Piece(false,false,false,false);
            for(Space space:spaces)
            {
                if(space.isOccupied())
                {
                    firstPiece = space.getPiece();
                    break;
                }
            }
            for(int i:a)
            {
                attributes[i].arboolean.add(firstPiece.is(i));
            }
        }

        for(int i = 0; i < attributes.length; i++)
        {
            if(attributes[i].indexOf(true) != -1 && attributes[i].indexOf(false) != -1)
            {
                return true;
            }
        }

        return false;
    }

    private class ARBoolean
    {
        public ArrayList<Boolean> arboolean = new ArrayList<Boolean>();

        public int indexOf(Boolean bool)
        {
            return arboolean.indexOf(bool);
        }
    }
}
