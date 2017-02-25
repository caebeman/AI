/****************************************************************
 * caebePlayer.java
 * Implements MiniMax search with A-B pruning and iterative deepening search (IDS). The static board
 * evaluator (SBE) function is simple: the # of stones in studPlayer's
 * mancala minus the # in opponent's mancala.
 * -----------------------------------------------------------------------------------------------------------------
 * Licensing Information: You are free to use or extend these projects for educational purposes provided that
 * (1) you do not distribute or publish solutions, (2) you retain the notice, and (3) you provide clear attribution to UW-Madison
 *
 * Attribute Information: The Mancala Game was developed at UW-Madison.
 *
 * The initial project was developed by Chuck Dyer(dyer@cs.wisc.edu) and his TAs.
 *
 * Current Version with GUI was developed by Fengan Li(fengan@cs.wisc.edu).
 * Some GUI componets are from Mancala Project in Google code.
 */




//################################################################
// studPlayer class
//################################################################

public class caebePlayer extends Player {


    private static final int NUM_BINS = 6;

    /*Use IDS search to find the best move. The step starts from 1 and keeps incrementing by step 1 until
	 * interrupted by the time limit. The best move found in each step should be stored in the
	 * protected variable move of class Player.
     */
    public void move(GameState state)
    {
        int maxDepth = 1;
        // repeated kick off
        while(true){
            if(maxDepth % 10 == 0){
                System.out.println("Max depth: " + maxDepth);
            }
            move = maxAction(state, maxDepth++);
//            System.out.println("Best Move So Far: " + move);
        }



    }

    // Return best move for max player. Note that this is a wrapper function created for ease to use.
	// In this function, you may do one step of search. Thus you can decide the best move by comparing the 
	// sbe values returned by maxSBE. This function should call minAction with 5 parameters.
    public int maxAction(GameState state, int maxDepth)
    {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int currentDepth = 0;


        return maxAction(state, currentDepth, maxDepth, alpha, beta).getBinNo();

//        for(int i = 0; i < NUM_BINS; i++){
//            s = new GameState(state);
//            // only generate child if the move is valid
//            if(!s.illegalMove(i)){
//                // applyMove call at end updates state and passes whether we get another turn  or not
////                int val = maxAction(s, currentDepth, maxDepth, alpha, beta, s.applyMove(i));
//                int val = maxAction(s, currentDepth, maxDepth, alpha, beta).getValue();
//                if(val >= curMax){
//                    curMax = val;
//                    binNo = i;
//                }
//            }
//        }
//        return binNo; // returns bin number of max value of the children
    }
    
	//return sbe value related to the best move for max player
    public Move maxAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
    {
        // TODO add pruning
        int curMax = Integer.MIN_VALUE;
        Move bestMove = null;
//        System.out.println("Max with cur depth: " + currentDepth + " maxDepth: " + maxDepth);
        if(currentDepth == maxDepth || state.gameOver()){
            return new Move(sbe(state));
//            return sbe(state); // at the max depth so we eval board state here
        } else if(currentDepth < maxDepth){
            currentDepth++; // we are going to recurse a level deeper if we can
            for(int i = 0; i < NUM_BINS; i++){
                GameState s = new GameState(state);
                if(!s.illegalMove(i)){
                    if(s.applyMove(i)){
                        int val = maxAction(s, currentDepth, maxDepth, alpha, beta).getValue();
                        if(curMax < val){
                            bestMove = new Move(i, val);
                            curMax = val;
                        }
                    } else {
                        int val = minAction(s, currentDepth, maxDepth, alpha, beta);
                        if(curMax < val){
                            bestMove = new Move(i, val);
                            curMax = val;
                        }

                    }
                    if(curMax >= beta){
                        return bestMove;
                    }
                    alpha = Math.max(alpha, curMax);
                }
            }
            return bestMove;


//            if(anotherTurn){
//                // we want to max our score since we go 2 times in a row
//                for(int i = 0; i < NUM_BINS; i++){
//                    GameState s = new GameState(state);
//                    // only generate child if the move is valid
//                    if(!s.illegalMove(i)){
//                        // applyMove call at end updates state and passes whether we get another turn  or not
//                        curMax = Math.max(curMax, maxAction(s, currentDepth, maxDepth, alpha, beta, s.applyMove(i)).getValue());
//                    }
//                }
//
//            } else {
//                // opponent wants to min our score
//                curMax = Math.max(curMax, minAction(state, currentDepth, maxDepth, alpha, beta));
//            }

        } else {
            // THIS SHOULD NEVER HAPPEN
            // because min value calls this we want to ensure minvalue never uses this value
            // if we are past the maxDepth
            System.out.println("Error, we've gone too deep (MAX)");
            return new Move(Integer.MAX_VALUE);
        }


    }
    //return sbe value related to the best move for min player
    public int minAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
    {
        int currentMin = Integer.MAX_VALUE;
        int val;
//        System.out.println("Min with cur depth: " + currentDepth + " maxDepth: " + maxDepth);

        if(currentDepth == maxDepth || state.gameOver()){

            return sbe(state);
        } else if(currentDepth < maxDepth){
            currentDepth++;
            for(int i = 0; i < NUM_BINS; i++){
                GameState s = new GameState(state);
                if(!s.illegalMove(i)){
                    if(s.applyMove(i)){
                        // get to go again, we call self again
                        currentMin = Math.min(currentMin, minAction(s, currentDepth, maxDepth, alpha, beta));

                    } else {
                        currentMin = Math.min(currentMin, maxAction(s, currentDepth,maxDepth,alpha, beta).getValue());
                    }
                    if(currentMin <= alpha){
                        return currentMin;
                    }
                    beta = Math.min(beta, currentMin);
                }
            }
            return currentMin;
        }  else {
            // THIS SHOULD NEVER HAPPEN
            // but we return the lowest poss value here, so maxValue wont ever take this value
            System.out.println("Error, we've gone too deep (MIN)");
            return Integer.MIN_VALUE;
        }
    }

    //the sbe function for game state. Note that in the game state, the bins for current player are always in the bottom row.
    private int sbe(GameState state)
    {

        //TODO Make a better SBE
        // calc end bin and see if we get to go again or get to capture an enemies stones
        // max num stones we capture a turn
//        System.out.println("SBE: " + (state.stoneCount(6) - state.stoneCount(13)));

        return state.stoneCount(6) - state.stoneCount(13);
    }
}

