public class BoardNode {

    private BoardNode parentBoardNode; //TODO for the time being not used. Remove if not needed.
    private Board currentBoard;

    private Move parentMove;
    private Move minMaxMove;

    private static Side maximizingPlayer; //TODO look for better solution?

    private Side nextPlayerTurn;
    private int depth; //TODO Agree how we define depth. Now it is decreasing when changing turn.

    private boolean swapAvailable;

    public BoardNode(Board currentBoard, boolean swapAvailable, Side maximizingPlayer, int searchDepth){
        this.parentBoardNode = null;
        this.currentBoard = currentBoard;

        this.maximizingPlayer = maximizingPlayer;
        this.nextPlayerTurn = maximizingPlayer;

        this.depth = searchDepth;
        this.swapAvailable = swapAvailable;
    }

    public BoardNode(BoardNode parentBoardNode, Move parentMove, int depth){
        this.parentBoardNode = parentBoardNode;
        currentBoard = new Board(parentBoardNode.getCurrentBoard());

        this.parentMove = parentMove;

        nextPlayerTurn = Kalah.makeMove(currentBoard, parentMove); // TODO need a similar function that does not report.

        this.depth = depth;
        this.swapAvailable = false;
    }

    public BoardNode[] findChildren(){
        BoardNode[] childBoardNodes = new BoardNode[currentBoard.getNoOfHoles() + 1];
        int childNo = 0;
        for(int hole = 1; hole <= currentBoard.getNoOfHoles(); hole++){
            Move move = new Move(nextPlayerTurn, hole);
            if(Kalah.isLegalMove(currentBoard, move)){
                if(true/*MoveClassifier.isFreeTurnMove(board, move)*/){
                    childBoardNodes[childNo] = new BoardNode(this, move, depth);
                }
                else{
                childBoardNodes[childNo] = new BoardNode(this, move, depth - 1);
                }
                childNo++;
            }
        }
        if(swapAvailable){
        //TODO find out how to handle this.
        }
        for(; childNo <= childBoardNodes.length; childNo++){ // Is this necessary?
        childBoardNodes[childNo] = null;
        }
        sortChildren();
        return childBoardNodes;
    }

    private void sortChildren(){
        //TODO sort children based on currentHeuristic by some simple insertion sort or something.
        //double currentHeuristic = Heuristic.advancedHeurisitic(currentBoard, nextPlayerTurn.getSide()); With this heuristic, always sort decreasing
    }

    public static Side getMaximizingPlayer() {
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
}
