public class GameTreeNode {

    private GameTreeNode[] childGameTreeNodes;

    private Board currentBoard;

    private Move parentMove;
    private Move minMaxMove;

    private Side maximizingPlayer;

    private Side nextPlayerTurn;
    private int depth; //TODO Agree how we define depth. Now it is decreasing only when changing turn.

    private boolean swapAvailable;

    private double currentHeuristic;
    private Heuristic heuristicObject;

    // Only called with root node:

    public GameTreeNode(Board currentBoard, boolean swapAvailable, Side maximizingPlayer, int searchDepth, Heuristic givenHeuristicObject){
        childGameTreeNodes = null;
        this.currentBoard = currentBoard;
        this.maximizingPlayer = maximizingPlayer;
        this.nextPlayerTurn = maximizingPlayer;
        this.depth = searchDepth;
        this.swapAvailable = swapAvailable;
        this.currentHeuristic = 0;
        this.heuristicObject = givenHeuristicObject;
    }

    public GameTreeNode(GameTreeNode parentGameTreeNode, Move parentMove, Side maximizingPlayer, int depth, Heuristic givenHeuristicObject){
        this.childGameTreeNodes = null;
        currentBoard = new Board(parentGameTreeNode.getCurrentBoard());
        this.parentMove = parentMove;
        this.maximizingPlayer = maximizingPlayer;
        if (parentMove != null) {
            nextPlayerTurn = Kalah.makeMove(currentBoard, parentMove, false);
        }
        else { //We have swapped
            nextPlayerTurn = maximizingPlayer.opposite();
        }
        this.depth = depth;
        this.swapAvailable = (parentGameTreeNode.isSwapAvailable() && MoveClassifier.isFirstMove(parentGameTreeNode.getCurrentBoard()) );
        this.heuristicObject = givenHeuristicObject;
        this.currentHeuristic = givenHeuristicObject.advancedHeuristic(currentBoard, maximizingPlayer);
    }

    public GameTreeNode[] findChildren(Heuristic givenHeuristicObject){
        childGameTreeNodes = new GameTreeNode[findNumLegalMoves()];
        int childNum = 0;
        for(int hole = 1; hole <= currentBoard.getNoOfHoles(); hole++){
            Move move = new Move(nextPlayerTurn, hole);
            if(Kalah.isLegalMove(currentBoard, move)){
                if(MoveClassifier.isFreeTurnMove(currentBoard, move)){
                    childGameTreeNodes[childNum] = new GameTreeNode(this, move, maximizingPlayer, depth, givenHeuristicObject);
                }
                else{
                    childGameTreeNodes[childNum] = new GameTreeNode(this, move, maximizingPlayer,depth - 1, givenHeuristicObject);
                }
                childNum++;
            }
        }
        if(swapAvailable && !MoveClassifier.isFirstMove(currentBoard)){
            childGameTreeNodes[childNum] = new GameTreeNode(this, null, maximizingPlayer.opposite(), depth, givenHeuristicObject);
            childNum++;
        }
        insertionSortChildren(childGameTreeNodes);
        return childGameTreeNodes;
    }

    private int findNumLegalMoves(){
        //first, check how many legal moves there are
        // hint: it's the same as the amount of nodes with seeds in them
        int numberOfLegalMoves = 0;
        for (int i=1; i <= currentBoard.getNoOfHoles(); i++)
        {
            if (currentBoard.getSeeds(nextPlayerTurn, i) !=0)
                numberOfLegalMoves++;
        }
        if(swapAvailable && !MoveClassifier.isFirstMove(currentBoard)){
            numberOfLegalMoves++;
        }
        return numberOfLegalMoves;
    }

    private void insertionSortChildren(GameTreeNode[] childGameTreeNodes){
        for(int i = 1; i < childGameTreeNodes.length; i++){
            boolean sorting = true;
            int currentPlace = i;
            while (sorting){
                if(currentPlace == 0){
                    sorting = false;
                }
                else if (childGameTreeNodes[currentPlace] != null &&
                        (childGameTreeNodes[currentPlace - 1] == null ||
                         childGameTreeNodes[currentPlace].getCurrentHeuristic() >
                         childGameTreeNodes[currentPlace - 1].getCurrentHeuristic()) ){
                    GameTreeNode tempNode = childGameTreeNodes[currentPlace-1];
                    childGameTreeNodes[currentPlace - 1] = childGameTreeNodes[currentPlace];
                    childGameTreeNodes[currentPlace] = tempNode;
                    currentPlace--;
                }
                else {
                    sorting = false;
                }
            }
        }
    }

    public Side getMaximizingPlayer() {
        return maximizingPlayer;
    }

    public Side getNextPlayerTurn() {
        return nextPlayerTurn;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public int getDepth() {
        return depth;
    }

    public Move getParentMove() {
        return parentMove;
    }

    public Move getMinMaxMove() {
        return minMaxMove;
    }

    public void setMinMaxMove(Move minMaxMove) {
        this.minMaxMove = minMaxMove;
    }

    public boolean isSwapAvailable() {
        return swapAvailable;
    }

    public double getCurrentHeuristic(){ return currentHeuristic; }
}
