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

    public synchronized void signUpBuyer(){

    }

    public synchronized void signUpVisitor(){

    }

    public synchronized void signOutBuyer(){

    }

    public synchronized void signOutVisitor(Visitor visitor){
        visitorsInside.remove(visitor);
        if(visitorsInside.size() == maxVisitorsInside){
            //visitor was not inside, maybe an assert?
            return;
        }
        if(!isBuyerNextInLine()){
            giveAccessToVisitor(visitorsLine.remove(0));
        } else {
            giveAccessToBuyer(buyersLine.remove(0));
            buyersInsideCombo ++;
        }
    }

    private void giveAccessToBuyer(Buyer buyer){

    }

    private void giveAccessToVisitor(Visitor visitor){

    }

    private boolean isBuyerNextInLine(){
        if(buyersInsideCombo == 4){
            if(visitorsLine.size() == 0){
                buyersInsideCombo = 0;
                return true;
            } else {
                return false;
            }
        }
        return true;
    }


}
