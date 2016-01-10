package implementation;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jonathan on 9-1-16.
 */
public class AccessController extends Thread{

    //TODO , max visitors
    public static final int MAXVISITORSINSIDEAUTORAI = 10;
    private int buyersInLine, visitorsInLine = 0;


    private  int visitorsInAutoRai = 0;
    private  int buyersVisited = 0;
    private final AutoRai autoRai;
    private Lock lock;
    public boolean buyerInside;
    public Condition visitorAllowed, buyerAllowed;


    public AccessController( AutoRai autoRai) {

        this.autoRai = autoRai;

        lock = new ReentrantLock();
        visitorAllowed = lock.newCondition();
        buyerAllowed = lock.newCondition();
        buyerInside = false;

    }

    public void join(AutoRaiFan fan) throws InterruptedException {

        lock.lock();
        try{
            if(fan instanceof Visitor){

                visitorsInLine ++;
                System.out.println(fan.toString() + " wants to join");
                while(whoIsNextInLine() != NextInLine.VISITOR){
                    visitorAllowed.await();
                }
                while(!mayGoIn(NextInLine.VISITOR)){
                    visitorAllowed.await();
                }

                assert whoIsNextInLine() == NextInLine.VISITOR;
                assert mayGoIn(NextInLine.VISITOR);

                System.out.println(fan.toString() + " may enter");
                autoRai.enter(fan);

                visitorsInLine --;
                visitorsInAutoRai ++;
                return;

            }

            if(fan instanceof Buyer){

                buyersInLine ++;
                System.out.println(fan.toString() + "wants to join");


                while(whoIsNextInLine() != NextInLine.BUYER){
                    System.out.println(fan.toString() + " is not next in line and has to wait for his turn");
                    buyerAllowed.await();
                }


                while(!mayGoIn(NextInLine.BUYER)){
                    System.out.println(fan.toString() + "may not go in and has to wait");
                    buyerAllowed.await();
                }

                assert whoIsNextInLine() == NextInLine.BUYER;
                assert mayGoIn(NextInLine.BUYER);

                System.out.println(fan.toString() + "may enter");
                autoRai.enter(fan);
                buyersInLine --;
                buyerInside = true;


            }

        }finally {
            lock.unlock();
        }


    }




    /**Deze methode wordt aangeroepen wanneer een fan de autorai verlaat.
     *
     * @param fan
     * @return if the fan can leave
     * @throws InterruptedException
     */
    public boolean onLeave(AutoRaiFan fan) throws InterruptedException {
        lock.lock();
        try{

            if(fan instanceof Visitor){

                visitorsInAutoRai --;

            }
            if(fan instanceof Buyer){
                if(((Buyer) fan).didBoughtAExpensiveEnoughCar()){
                    buyersVisited ++;
                    buyerInside = false;
                }else{
                    return false;
                }

            }


            NextInLine nextInLine = whoIsNextInLine();

            switch (nextInLine){
                case VISITOR: if(mayGoIn(nextInLine)){ visitorAllowed.signal(); }
                    break;
                case BUYER: if(mayGoIn(nextInLine)){  buyerAllowed.signal(); }
                    break;
                case ANYONE:
                    break;
            }


        }finally {
            lock.unlock();
        }

        return true;

    }


    /**Checks if the person next inline may go in
     *
     * @param nextInLine
     * @return
     */
    private boolean mayGoIn(NextInLine nextInLine){

        if(nextInLine == NextInLine.VISITOR){

            // buyer inside?
            if(buyerInside){
                return false;
            }
            // is the autoRai full?
            if(visitorsInAutoRai >= MAXVISITORSINSIDEAUTORAI){
                return false;
            }else {
                // the autoRai isnt full so the visitor may enter
                return true;
            }


        }
        if(nextInLine == NextInLine.BUYER){
            // is the autoRai full? if so, return false, else return true
            return !(buyerInside || visitorsInAutoRai >= MAXVISITORSINSIDEAUTORAI);


        }

        assert nextInLine == NextInLine.ANYONE;
        return true;


    }

    private enum NextInLine{
        VISITOR, BUYER, ANYONE;
    }

    /**Checks who is next in line
     *
     * @return
     */
    private NextInLine whoIsNextInLine(){

        //any buyers waiting?
        if(buyersInLine >0){
            //are there visitors wanting to go in too?
            if(visitorsInLine>0){
                // didnt 4 buyers already enter?
                if(buyersVisited %4 != 0){
                    return NextInLine.BUYER;
                    // buyer is next inline


                }else {
                    // already 4 buyers entered
                    return NextInLine.VISITOR;
                }
            }else {
                // no visitors are waiting
                // so the buyer is next in line.
                return NextInLine.BUYER;
            }

        }else if(visitorsInLine == 0){
            //no one is in line, so anyone is next
            return NextInLine.ANYONE;

        }else{
            // visitors are in line waiting, so they are nextt
            return NextInLine.VISITOR;

        }


    }



}
