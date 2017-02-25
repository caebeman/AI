import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Keifer on 2/22/17.
 */
public class RandPlayer extends Player {


    private ArrayList<Integer> moves = new ArrayList<>();

    /**
     *  Working AI for mancala game that simply chooses a random valid move and takes that move
     *  Handles randomly choosing moves that allow for additional moves after the first
     * @param context the current position in the game
     *
     */
    @Override
    public void move(GameState context) {
        // if our moves array has any elements in it, means we already chose moves to use
        // basically we got to go again
        if(moves.size() > 0) {
            move = moves.remove(0); // get move and then decrement movesLeft
            return;
        } else {
            // choose a random move and make sure its a valid move
            Random r =  new Random();
            int bin;
            do {
                bin = r.nextInt(6);
                while(context.illegalMove(bin)){
                    bin = r.nextInt(6);
                }
                moves.add(bin);
            } while(context.applyMove(bin));
//            System.out.println(context.toString());
            move = moves.remove(0);
        }





    }
}
