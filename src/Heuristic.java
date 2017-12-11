
public class Heuristic {

    //The following coefficients needs to be found by experimenting
    private double stonesInStoreCoefficient = 0.2;
    private double stonesInHolesCoefficient = 0.5;
    private double freeTurnMovesCoefficient = 0.5;
    private double maxStealSeedMoveCoefficient = 0.5;

    public Heuristic (double givenStoresInHoles, double givenFreeTurnMoves, double givenStealMove)
    {
      this.stonesInHolesCoefficient = givenStoresInHoles;
      this.freeTurnMovesCoefficient = givenFreeTurnMoves;
      this.maxStealSeedMoveCoefficient = givenStealMove;
    }

    // Every function ending with "Diff" are used to make heuristics. Actual heuristic in the bottom
    private int stonesInStoreDiff(Board board, Side side){
        int stonesInOwnStore = board.getSeedsInStore(side);
        int stonesInOppStore = board.getSeedsInStore(side.opposite());

        return (stonesInOwnStore - stonesInOppStore);
    }

    private int stonesInHolesDiff(Board board, Side side) {
        int stonesInOwnHoles = numOfStonesInHoles(board, side);
        int stonesInOppHoles = numOfStonesInHoles(board, side.opposite());
        return (stonesInOwnHoles - stonesInOppHoles);
    }

    private int numOfStonesInHoles(Board board, Side side){
        int stones = 0;
        for(int hole = 1; hole <= board.getNoOfHoles(); hole++){
            stones += board.getSeeds(side, hole);
        }
        return stones;
    }

    private int freeTurnMovesDiff(Board board, Side side){
        int ownFreeTurnMoves = countFreeMoves(board, side);
        int oppFreeTurnMoves = countFreeMoves(board, side.opposite());
        return ownFreeTurnMoves - oppFreeTurnMoves;
    }

    private int countFreeMoves(Board board, Side side){
        int count = 0;
        for(int hole = 1; hole <= board.getNoOfHoles(); hole++){
            Move move = new Move(side,hole);
            if(MoveClassifier.isFreeTurnMove(board, move)){
                count++;
            }
        }
        return count;
    }

    private int maxStealMoveDiff(Board board, Side side){
        int ownMaxStealMove = maxStealMove(board, side);
        int oppMaxStealMove = maxStealMove(board, side.opposite());
        return ownMaxStealMove - oppMaxStealMove;
    }

    private int maxStealMove(Board board, Side side){
        int maxSteal = 0;
        for(int hole = 1; hole <= board.getNoOfHoles(); hole++){
            Move move = new Move(side,hole);
            if(MoveClassifier.isStealSeedsMove(board, move)){
                int endHole = MoveClassifier.findEndHole(board, move);
                int stealValue = board.getSeedsOp(side, endHole);
                if (stealValue > maxSteal){
                    maxSteal = stealValue;
                }
            }
        }
        return maxSteal;
    }

    private int calculateLinearIncreasingCoefficient(Board board, int minCoefficientValue, int maxCoefficientValue){
        int maxSeedsInStore = Math.max(board.getSeedsInStore(Side.NORTH), board.getSeedsInStore(Side.SOUTH));
        int seedsNeededToWin = board.getNoSeedsInHolesAtStart() * board.getNoOfHoles() + 1;
        return (int) ( ( (double)maxSeedsInStore / seedsNeededToWin) * (maxCoefficientValue + 1 - minCoefficientValue) + minCoefficientValue );
    }

    public double simpleHeuristic(Board board, Side side){
        return stonesInStoreCoefficient * stonesInStoreDiff(board, side)
                + stonesInHolesCoefficient * stonesInHolesDiff(board, side);
    }

    public double advancedHeuristic(Board board, Side side){
     //   if (Kalah.gameOver(board)){
            //if(board.getSeedsInStore(side) > board.getSeedsInStore(side.opposite())){
           //     return Double.POSITIVE_INFINITY;
           // }
           // return Double.NEGATIVE_INFINITY;
     //   }
        int minCoefficientValue = 1;
        int maxCoefficientValue = 10;
        int linearIncreasingCoefficient = calculateLinearIncreasingCoefficient(board, minCoefficientValue, maxCoefficientValue);
        return linearIncreasingCoefficient * stonesInStoreDiff(board, side)
                + this.stonesInHolesCoefficient /*linearIncreasingCoefficient*/ * stonesInHolesDiff(board, side)
                + this.freeTurnMovesCoefficient /*linearIncreasingCoefficient*/ * freeTurnMovesDiff(board, side)
                + this.maxStealSeedMoveCoefficient /*linearIncreasingCoefficient*/ * maxStealMoveDiff(board, side);
    }
}