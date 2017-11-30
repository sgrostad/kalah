public class MoveClassifier {

    public static boolean isFreeTurnMove(Board board, Move move){
        int stonesInHole = board.getSeeds(move.getSide(), move.getHole());
        int noOfHoles = board.getNoOfHoles();
        if(stonesInHole % (noOfHoles*2 + 1) == numHolesToStore(board, move)){
            return true;
        }
        return false;
    }

    public static boolean isStealSeedsMove(Board board, Move move){
        int stealFromHole = MoveClassifier.findEndHole(board, move);
        if (!enoughSeedsForOverFullRound(board, move)
                && board.getSeeds(move.getSide(), move.getHole()) > 0
                && stealFromHole > 0
                && board.getSeedsOp(move.getSide(), stealFromHole) > 0
                && board.getSeeds(move.getSide(), stealFromHole) == 0){
            return true;
        }
        return false;
    }

    public static int findEndHole(Board board, Move move){
        int seeds = board.getSeeds(move.getSide(), move.getHole());
        int distanceToStore = MoveClassifier.numHolesToStore(board, move);
        int noHoles = board.getNoOfHoles();
        seeds = seeds % (noHoles * 2 + 1);
        if(isFreeTurnMove(board, move)){
            return 0;
        }
        else if (seeds < distanceToStore){
            return move.getHole() + seeds;
        }
        else if(seeds > distanceToStore + noHoles){// to many seeds already handled
            return seeds - distanceToStore - noHoles;
        }
        return -1;
    }

    private static int numHolesToStore(Board board, Move move){
        return board.getNoOfHoles() - move.getHole() + 1;
    }

    private static boolean enoughSeedsForOverFullRound(Board board, Move move){
        int seeds = board.getSeeds(move.getSide(), move.getHole());
        int noHoles = board.getNoOfHoles();
        if(seeds > noHoles * 2 + 1){
            return true;
        }
        return false;
    }
}
