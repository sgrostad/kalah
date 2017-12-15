public class GameTreeNode {

    private GameTreeNode bestChildGameTreeNode;

    private Board currentBoard;

    private Move parentMove;

    private Side maximizingPlayer;

    private Side nextPlayerTurn;

    private int depth; //TODO Agree how we define depth. Now it is decreasing only when changing turn.
    private boolean swapAvailable;

    private double currentHeuristic;

    // Only called with root node:

    public GameTreeNode(Board currentBoard, boolean swapAvailable, Side maximizingPlayer, int searchDepth){
        this.bestChildGameTreeNode = null;
        this.currentBoard = currentBoard;
        this.parentMove = null;
        this.maximizingPlayer = maximizingPlayer;
        this.nextPlayerTurn = maximizingPlayer;
        this.depth = searchDepth;
        this.swapAvailable = swapAvailable;
        this.currentHeuristic = 0;
    }
    public GameTreeNode(GameTreeNode parentGameTreeNode, Move parentMove, Side maximizingPlayer, int depth){
        this.bestChildGameTreeNode = null;
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
        this.swapAvailable = (MoveClassifier.isFirstMove(parentGameTreeNode.getCurrentBoard()) );
        this.currentHeuristic = Heuristic.advancedHeuristic(currentBoard, maximizingPlayer);
    }

    public GameTreeNode[] findChildren(){
        GameTreeNode[] childGameTreeNodes = new GameTreeNode[findNumLegalMoves()];
        int childNum = 0;
        for(int hole = 1; hole <= currentBoard.getNoOfHoles(); hole++){
            Move move = new Move(nextPlayerTurn, hole);
            if(Kalah.isLegalMove(currentBoard, move)){
                if(MoveClassifier.isFreeTurnMove(currentBoard, move)){
                    childGameTreeNodes[childNum] = new GameTreeNode(this, move, maximizingPlayer, depth);
                }
                else{
                    childGameTreeNodes[childNum] = new GameTreeNode(this, move, maximizingPlayer,depth - 1);
                }
                childNum++;
            }
        }
        if(swapAvailable && !MoveClassifier.isFirstMove(currentBoard)){
            childGameTreeNodes[childNum] = new GameTreeNode(this, null, maximizingPlayer.opposite(), depth);
            childNum++;
        }
        sortChildren(childGameTreeNodes);
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

    private void sortChildren(GameTreeNode[] childGameTreeNodes){
        int sortDepth;
        if(depth > 8 ) { 
            sortDepth = 2;
        }
        else {
            sortDepth = 1;
        }
        if (depth > 6){
            for (GameTreeNode child : childGameTreeNodes) {
                GameTreeNode smallSearchRoot = MoveDecisionMaker.decideMove(child.currentBoard, false, maximizingPlayer, sortDepth);
                child.setCurrentHeuristic(smallSearchRoot.getBottomHeuristic());
            }
        }
        insertionSortChildren(childGameTreeNodes, maximizingPlayer == nextPlayerTurn);
    }

    private void insertionSortChildren(GameTreeNode[] childGameTreeNodes, boolean heuristicsWillBeDecreasing){
        for(int i = 1; i < childGameTreeNodes.length; i++){
            boolean sorting = true;
            int currentPlace = i;
            while (sorting){
                if(currentPlace == 0){
                    sorting = false;
                }
                else if (childGameTreeNodes[currentPlace] != null && heuristicsWillBeDecreasing &&
                        childGameTreeNodes[currentPlace].getCurrentHeuristic() >
                         childGameTreeNodes[currentPlace - 1].getCurrentHeuristic()){
                    GameTreeNode tempNode = childGameTreeNodes[currentPlace-1];
                    childGameTreeNodes[currentPlace - 1] = childGameTreeNodes[currentPlace];
                    childGameTreeNodes[currentPlace] = tempNode;
                    currentPlace--;
                }
                else if (childGameTreeNodes[currentPlace] != null && !heuristicsWillBeDecreasing &&
                        childGameTreeNodes[currentPlace].getCurrentHeuristic() <
                                childGameTreeNodes[currentPlace - 1].getCurrentHeuristic()){
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

    public GameTreeNode getBestChildGameTreeNode() {
        return bestChildGameTreeNode;
    }

    public void setBestChildGameTreeNode(GameTreeNode bestChildGameTreeNode) {
        this.bestChildGameTreeNode = bestChildGameTreeNode;
        this.currentHeuristic = bestChildGameTreeNode.currentHeuristic;
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
        return bestChildGameTreeNode.parentMove;
    }

    public boolean isSwapAvailable() {
        return swapAvailable;
    }

    public double getCurrentHeuristic(){ return currentHeuristic; }

    public void setCurrentHeuristic(double heuristic){this.currentHeuristic = heuristic;}

    public double getBottomHeuristic(){
        if(bestChildGameTreeNode == null){
            return currentHeuristic;
        }
        else{
            return bestChildGameTreeNode.getBottomHeuristic();
        }
    }

}
