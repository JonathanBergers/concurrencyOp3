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
    private int buyersInLine = 0;


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

                System.out.println(fan.toString() + " wants to join");
                while(!visitorMayEnter() || isAutoRaiFull()){

                    //tetsing purpose
                    if(isAutoRaiFull()){
                        System.out.println(fan.toString() + "waits in line because autoRai is full");
                    } else {
                        System.out.println(fan.toString() + "waits in line because there is a buyer inside autoRai");
                    }


                    visitorAllowed.await();
                }
                System.out.println(fan.toString() +" may enter");
                autoRai.enter(fan);
                visitorsInside = true;
                visitorsInAutoRai ++;

                return;

            }

            if(fan instanceof Buyer){

                buyersInLine ++;
                System.out.println(fan.toString() + "wants to join");
                while(visitorsInside || isAutoRaiFull()){

                    //tetsing purpose
                    if(isAutoRaiFull()){
                        System.out.println(fan.toString() + "waits in line because autoRai is full");
                    } else {
                        System.out.println(fan.toString() + "waits in line because there is a visitor inside autoRai");
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


    private boolean visitorMayEnter(){
        // buyer waiting, & allowed to go in first
        if(buyersInLine>0 && buyersVisited % 5 == 0){
            return false;
        }

        return true;

    }

    private boolean isAutoRaiFull(){
        return buyerInside || visitorsInAutoRai > MAXVISITORSINSIDEAUTORAI;
    }


    /**Deze methode wordt aangeroepen wanneer een fan de autorai verlaat.
     *
     * @param fan
     * @throws InterruptedException
     */
    public boolean onLeave(AutoRaiFan fan) throws InterruptedException {
        lock.lock();
        try{

            autoRai.leave(fan);
            if(fan instanceof Visitor){
                visitorsInAutoRai --;
                if(visitorsInAutoRai == 0){
                    visitorsInside = false;
                    if(buyersVisited % 5 == 0){
                        buyerAllowed.signal();

                    }

                }
                return true;

            }

            if(fan instanceof Buyer){
                Buyer buyer = (Buyer) fan;
                if(!buyer.didBoughtAExpensiveEnoughCar()){
                    System.out.println(buyer.toString()+"Did not bougt a car for 2500 or more. He/she has to buy an new one");
                    return false;
                }
                buyerInside = false;
                if(buyersVisited % 5 != 0){
                    buyerAllowed.signal();

                }else{
                    visitorAllowed.signalAll();
                }
                return true;

            }
        }finally {
            lock.unlock();
        }

        return true;

    }




}
