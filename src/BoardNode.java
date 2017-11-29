public class BoardNode {
    private BoardNode parentBoardNode;
    private Board currentBoard;
    private BoardNode[] childBoardNodes;

    private Side nextPlayerTurn;
    private Move parentMove;
    private boolean swapAvailable;

    private double currentHeuristic;
    private static final int terminalDepth = 7; //can be chosen
    private int depth; //TODO agree when to increase this. now: only when player turn swaps

    private double alpha;
    private double beta;

    public BoardNode(Board currentBoard, boolean swapAvailable, Side nextPlayerTurn){
        this.parentBoardNode = null;
        this.currentBoard = currentBoard;

        this.nextPlayerTurn = nextPlayerTurn;
        parentMove = null;
        this.swapAvailable = swapAvailable;

        setHeuristic();
        this.depth = 0;

        alpha = Double.NEGATIVE_INFINITY;
        beta = Double.POSITIVE_INFINITY;
    }

    public BoardNode(BoardNode parentBoardNode, Move move, int depth){
        this.parentBoardNode = parentBoardNode;
        currentBoard = new Board(parentBoardNode.getCurrentBoard());

        nextPlayerTurn = Kalah.makeMove(currentBoard, parentMove); // TODO need a similar function that does not report.
        this.parentMove = move;
        this.swapAvailable = false;

        setHeuristic();
        this.depth = depth;

        this.alpha = parentBoardNode.getAlpha();
        this.beta = parentBoardNode.getBeta();
        if (depth < terminalDepth) {
            findChildren();
            sortChildren();
        }
        else {
            childBoardNodes = null;
        }
    }

    private void findChildren(){
        childBoardNodes = new BoardNode[currentBoard.getNoOfHoles() + 1];
        int childNo = 0;
        for(int hole = 1; hole <= currentBoard.getNoOfHoles(); hole++){
            Move childMove = new Move(nextPlayerTurn, hole);
            if(Kalah.isLegalMove(currentBoard, childMove)){
                if(parentMove.getSide() == nextPlayerTurn){
                    //childBoardNodes[childNo] = new BoardNode(this, childMove, false, depth);

                }
                else{
                    //childBoardNodes[childNo] = new BoardNode(this, childMove, false, depth + 1);
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
    }

    private void sortChildren(){
        //TODO sort children based on currentHeuristic by some simple insertion sort or something.
    }


    private void setHeuristic(){
        //currentHeuristic = Heuristic.advancedHeurisitic(currentBoard, parentMove.getSide());
        currentHeuristic = 0;
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }
}
