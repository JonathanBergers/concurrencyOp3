package implementation;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jonathan on 9-1-16.
 */
public class AutoRai {


    public final AccessController accessController;


    public AutoRai() {
        this.accessController = new AccessController(this);
    }


    public void enter(AutoRaiFan fan){
        //fan is inside.
        // do nothing, its a simulation.

    }

    public void leave(AutoRaiFan fan){

    }






}
