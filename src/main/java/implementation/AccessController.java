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
    public boolean visitorsInside, buyerInside;
    public Condition visitorAllowed, buyerAllowed;


    public AccessController( AutoRai autoRai) {

        this.autoRai = autoRai;

        lock = new ReentrantLock();
        visitorAllowed = lock.newCondition();
        buyerAllowed = lock.newCondition();
        visitorsInside = false;
        buyerInside = false;

    }

    public void join(AutoRaiFan fan) throws InterruptedException {

        lock.lock();
        try{
            if(fan instanceof Visitor){

                visitorsInLine ++;
                System.out.println(fan.toString() + " wants to join");
                while(!onlyVisitorMayEnter() || isAutoRaiFull()){

                    //tetsing purpose
                    if(isAutoRaiFull()){
                        System.out.println(fan.toString() + "waits in line because autoRai is full");
                    }


                    visitorAllowed.await();
                }
                System.out.println(fan.toString() +" may enter");
                autoRai.enter(fan);
                visitorsInLine --;
                visitorsInside = true;
                visitorsInAutoRai ++;

                return;

            }

            if(fan instanceof Buyer){

                buyersInLine ++;
                System.out.println(fan.toString() + "wants to join");
                while((visitorsInside || isAutoRaiFull() || onlyVisitorMayEnter())){

                    //tetsing purpose
                    if(isAutoRaiFull()){
                        System.out.println(fan.toString() + "waits in line because autoRai is full");
                    } else if(visitorsInside) {
                        System.out.println(fan.toString() + "waits in line because there is a visitor inside autoRai");
                    } else {
                        System.out.println(fan.toString() + "waits in line because only visitors may enter");
                    }


                    buyerAllowed.await();
                }
                System.out.println(fan.toString() +" may enter");
                autoRai.enter(fan);
                buyersInLine --;
                buyerInside = true;
                buyersVisited++;
                return;


            }

        }finally {
            lock.unlock();
        }


    }


    private boolean onlyVisitorMayEnter(){
        // buyer waiting, & allowed to go in first
        return (buyersVisited % 4 == 0 || buyersInLine == 0) && visitorsInLine != 0 ;


    }

    private boolean isAutoRaiFull(){
        return buyerInside || visitorsInAutoRai > MAXVISITORSINSIDEAUTORAI;
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

            autoRai.leave(fan);
            if(fan instanceof Visitor){
                visitorsInAutoRai --;
                if(!onlyVisitorMayEnter()){
                    if(visitorsInAutoRai == 0){
                        visitorsInside = false;
                        buyerAllowed.signal();
                    }
                } else {
                    visitorAllowed.signal();
                }
                return true;

            } else if(fan instanceof Buyer){
                Buyer buyer = (Buyer) fan;
                if(!buyer.didBoughtAExpensiveEnoughCar()){
                    System.out.println(buyer.toString()+"Did not bougt a car for 25000 or more. He/she has to buy an new one");
                    return false;
                }
                buyerInside = false;
                if(buyersVisited % 4 != 0){
                    buyerAllowed.signal();

                }else{
                    for(int i = 0; i<MAXVISITORSINSIDEAUTORAI; i++){
                        visitorAllowed.signal();
                    }
                }
                return true;

            }
        }finally {
            lock.unlock();
        }

        return true;

    }




}
