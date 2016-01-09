package implementation;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jonathan on 9-1-16.
 */
public class AccessController extends Thread{


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
        fan.onJoin();
        try{
            if(fan instanceof Visitor){

                System.out.println("Visitor wants to join");
                System.out.println(fan.toString());
                while(buyerInside){
                    visitorAllowed.await();
                }
                autoRai.enter(fan);
                fan.onEnter();
                visitorsInside = true;
                visitorsInAutoRai ++;

                return;

            }

            if(fan instanceof Buyer){

                System.out.println("Buyer wants to join");
                while(visitorsInside){
                    buyerAllowed.await();
                }
                autoRai.enter(fan);
                fan.onEnter();
                buyerInside = true;
                buyersVisited++;
                return;


            }

        }finally {
            lock.unlock();
        }


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
                if(buyersVisited % 5 == 0){
                    buyerAllowed.signal();

                }else{
                    visitorAllowed.signalAll();
                }

            }
        }finally {
            fan.onLeave();
            lock.unlock();
        }



    }




}
