public class BoardNode {

    private Board currentBoard;

    private Move parentMove;
    private Move minMaxMove;

    private Side maximizingPlayer; //TODO look for better solution?

    private Side nextPlayerTurn;
    private int depth; //TODO Agree how we define depth. Now it is decreasing when changing turn.

    private boolean swapAvailable;

    private double currentHeuristic; // Heuristic for the nextPlayerTurn

    // Only called with root node:
    public BoardNode(Board currentBoard, boolean swapAvailable, Side maximizingPlayer, int searchDepth){
        this.currentBoard = currentBoard;

        this.maximizingPlayer = maximizingPlayer;
        this.nextPlayerTurn = maximizingPlayer;

        this.depth = searchDepth;
        this.swapAvailable = swapAvailable;

        this.currentHeuristic = 0;
    }

    public BoardNode(BoardNode parentBoardNode, Move parentMove, Side maximizingPlayer, int depth){
        currentBoard = new Board(parentBoardNode.getCurrentBoard());

        this.parentMove = parentMove;

        this.maximizingPlayer = maximizingPlayer;

        nextPlayerTurn = Kalah.makeMove(currentBoard, parentMove, false);

        this.depth = depth;
        this.swapAvailable = false;

        this.currentHeuristic = Heuristic.advancedHeuristic(currentBoard, maximizingPlayer);
    }

    public BoardNode[] findChildren(){
        BoardNode[] childBoardNodes = new BoardNode[currentBoard.getNoOfHoles() + 1];
        int childNum = 0;
        for(int hole = 1; hole <= currentBoard.getNoOfHoles(); hole++){
            Move move = new Move(nextPlayerTurn, hole);
            if(Kalah.isLegalMove(currentBoard, move)){
                if(MoveClassifier.isFreeTurnMove(currentBoard, move)){
                    childBoardNodes[childNum] = new BoardNode(this, move, maximizingPlayer, depth);
                }
                else{
                childBoardNodes[childNum] = new BoardNode(this, move, maximizingPlayer,depth - 1);
                }
                childNum++;
            }
        }
        if(swapAvailable){
            //TODO Something like this?
        //childBoardNodes[childNum] = new BoardNode(this, null, maximizingPlayer.opposite(), depth-1);
        childNum++;
        }
        for(; childNum < childBoardNodes.length; childNum++){
            childBoardNodes[childNum] = null;
        }
        insertionSortChildren(childBoardNodes);
        return childBoardNodes;
    }

    private void insertionSortChildren(BoardNode[] childBoardNodes){
        for(int i = 1; i < childBoardNodes.length; i++){
            boolean sorting = true;
            int currentPlace = i;
            while (sorting){
                if(currentPlace == 0){
                    sorting = false;
                }
                else if (childBoardNodes[currentPlace] != null &&
                        (childBoardNodes[currentPlace - 1] == null ||
                         childBoardNodes[currentPlace].getCurrentHeuristic() >
                         childBoardNodes[currentPlace - 1].getCurrentHeuristic()) ){
                    BoardNode tempNode = childBoardNodes[currentPlace-1];
                    childBoardNodes[currentPlace - 1] = childBoardNodes[currentPlace];
                    childBoardNodes[currentPlace] = tempNode;
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

    public double getCurrentHeuristic(){ return currentHeuristic; }
}
