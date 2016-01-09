package implementation;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jonathan on 9-1-16.
 */
public class AutoRai {


    private LinkedBlockingQueue<AutoRaiFan> fans = new LinkedBlockingQueue<AutoRaiFan>();
    public final AccessController accessController;


    public AutoRai() {
        this.accessController = new AccessController(this);
    }


    public void enter(AutoRaiFan fan){
//        fan.onEnter();
        fans.add(fan);
    }

    public void leave(AutoRaiFan fan){
//        fan.onLeave();
        fans.remove(fan);
    }



    public synchronized boolean fansInside(){
        return fans.size() == 0;
    }

    public synchronized boolean buyerInside(){
        if(fans.size() != 1){
            return false;
        }
        return fans.peek() instanceof Buyer;
    }




}
