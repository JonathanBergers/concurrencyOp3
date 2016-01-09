package implementation;

import java.util.ArrayList;

/**
 * Created by falco on 9-1-16.
 */
public class AccessController {

    private ArrayList<Buyer> buyersLine = new ArrayList<Buyer>();
    private ArrayList<Visitor> visitorsLine = new ArrayList<Visitor>();


    //max 4
    private int buyersInsideCombo = 0;
    private ArrayList<Buyer> buyersInside = new ArrayList<Buyer>();

    private final int maxVisitorsInside = 10;
    //max 10
    private ArrayList<Visitor> visitorsInside = new ArrayList<Visitor>();

    public AccessController() {

    }

    /**
     * A buyer must first sign up, before he/she can wait in line to go inside
     * @param buyer the buyer whom wants to go inside
     */
    public synchronized void signUpBuyer(Buyer buyer){

        //checks if nobody is inside, or in line and if it is the buyers turn to go inside
        if(isBuyerNextInLine() && buyersInside.isEmpty() && visitorsInside.isEmpty()){
            giveAccessToBuyer(buyer);
        } else {
            buyersLine.add(buyer);
        }
    }

    /**
     * A visitor must first sign up, before he/she can wait in line to go inside
     * @param visitor the visitor whom wants to go inside
     */
    public synchronized void signUpVisitor(Visitor visitor){
        //checks if there is room inside, or in line and if it is the visitors turn to go inside
        if(!isBuyerNextInLine() && visitorsInside.size()<10 && visitorsLine.isEmpty() ){
            giveAccessToVisitor(visitor);
        } else {
            visitorsLine.add(visitor);
        }
    }

    /**
     * when an buyer bought a car and wants to drive it home, he first has to sign out, so others can go inside
     * @param buyer the buyer whom wants to go home
     */
    public synchronized void signOutBuyer(Buyer buyer){
        buyersInside.remove(buyer);
        if(!buyersInside.isEmpty()){
            //buyer was not inside, maybe an assert?
            return;
        }
        if(isBuyerNextInLine()){
            if(visitorsInside.isEmpty()){
                giveAccessToBuyer(buyersLine.poll());
            }
        } else {
            while(visitorsInside.size() < MAXVISITORSINSIDE && !visitorsLine.isEmpty()){
                giveAccessToVisitor(visitorsLine.poll());
            }
        }
    }

    /**
     * when a visitor has seen enough and he want to go home, he/she must first sign out so others can go inside
     * @param visitor
     */
    public synchronized void signOutVisitor(Visitor visitor){
        visitorsInside.remove(visitor);
        if(visitorsInside.size() == MAXVISITORSINSIDE){
            //visitor was not inside, maybe an assert?
            return;
        }
        if(!isBuyerNextInLine()){
            giveAccessToVisitor(visitorsLine.poll());
        } else if(visitorsInside.isEmpty()){
            giveAccessToBuyer(buyersLine.poll());
        }
    }

    private void giveAccessToBuyer(Buyer buyer){
        //something like:
        // buyer.giveAccess();
        //TODO

        buyersWhomWentInsideCount ++;
    }

    private void giveAccessToVisitor(Visitor visitor){
        //something like:
        // visitor.giveAccess();
        //TODO
    }

    private boolean isBuyerNextInLine(){
        if(buyersWhomWentInsideCount % 5 == 0){
            if(visitorsLine.isEmpty() || buyersWhomWentInsideCount == 0){
                return true;
            } else {
                return false;
            }
        }
        return true;
    }


}
