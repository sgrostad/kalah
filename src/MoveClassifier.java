public class MoveClassifier {

    public static boolean isFreeTurnMove(Board board, Side side, int hole){
        int stonesInHole = board.getSeeds(side, hole);
        int noOfHoles = board.getNoOfHoles();
        if(stonesInHole % (noOfHoles*2 + 1) == numHolesToStore(board, side, hole)){
            return true;
        }
        return false;
    }

    public static boolean isStealSeedsMove(Board board, Side side, int hole){
        int stealFromHole = MoveClassifier.findEndHole(board, side, hole);
        if (!enoughSeedsForOverFullRound(board, side, hole)
                && board.getSeeds(side, hole) > 0
                && stealFromHole > 0
                && board.getSeedsOp(side, stealFromHole) > 0){
            return true;
        }
        return false;
    }

    public static int findEndHole(Board board, Side side, int hole){
        int seeds = board.getSeeds(side, hole);
        int distanceToStore = MoveClassifier.numHolesToStore(board, side, hole);
        int noHoles = board.getNoOfHoles();
        seeds = seeds % (noHoles * 2 + 1);
        if(side == Side.NORTH){
            if(seeds < distanceToStore){
                return hole - seeds;
            }
            else if(seeds > distanceToStore + noHoles){ // to many seeds already handled
                return noHoles - (seeds - distanceToStore - noHoles);
            }
        }
        else{ //Side.South
            if (seeds < distanceToStore){
                return hole + seeds;
            }
            else if(seeds > distanceToStore + noHoles){// to many seeds already handled
                return seeds - distanceToStore - noHoles;
            }
        }
        return 0;
    }

    private static int numHolesToStore(Board board, Side side, int hole){
        if (side == Side.NORTH){
            return hole;
        }
        else{
            return board.getNoOfHoles() - hole + 1;
        }
    }

    private static boolean enoughSeedsForOverFullRound(Board board, Side side, int hole){
        int seeds = board.getSeeds(side, hole);
        int noHoles = board.getNoOfHoles();
        if(seeds > noHoles * 2 + 1){
            return true;
        }
        return false;
    }
}
