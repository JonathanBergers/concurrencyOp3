package implementation;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jonathan on 9-1-16.
 */
public class AccessController extends Thread{


    private int buyersInLine = 0;


    private  int visitorsInAutoRai = 0;
    public  int buyersVisited = 0;
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
                while(!userMayEnter()){
                    System.out.println(fan.toString() +"must wait ");
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
                while(visitorsInside){
                    System.out.println(fan.toString() +"must wait ");
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


    private boolean userMayEnter(){



        if(buyerInside){
            return false;
        }
        // buyer waiting, & allowed to go in first
        if(buyersInLine>0 && buyersVisited % 5 == 0){
            return false;
        }

        return true;

    }


    public void onLeave(AutoRaiFan fan) throws InterruptedException {


        lock.lock();
        try{
            if(fan instanceof Visitor){
                visitorsInAutoRai --;
                if(visitorsInAutoRai == 0){
                    visitorsInside = false;
                    if(buyersVisited % 5 == 0){
                        buyerAllowed.signal();

                    }

                }
            return;

            }

            if(fan instanceof Buyer){
                buyerInside = false;
                if(buyersVisited % 5 != 0){
                    buyerAllowed.signal();

                }else{
                    visitorAllowed.signalAll();
                }

            }
        }finally {
            lock.unlock();
        }



    }




}
