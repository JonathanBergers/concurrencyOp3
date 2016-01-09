package implementation;

import interfaces.AutoRaiFanState;

/**
 * Created by falco on 9-1-16.
 */
public class Buyer extends AutoRaiFan{




    protected Buyer(AutoRai autoRai) {
        super(autoRai);
    }

    @Override
    protected void onJoin() {


        setAutoRaiFanState(AutoRaiFanState.IN_LINE);
        System.out.println(toString() + "I am waiting to go inside autoRai");


    }

    @Override
    protected void onLeave() {
        // tell the controller that you left
        setAutoRaiFanState(AutoRaiFanState.CRAVING_COOL_CARS);
        System.out.println(toString() + "I Left autoRai");
        // set state



        // crave some sexy cars
    }

    @Override
    protected void onEnter() {



        System.out.println(toString() + "Im inside now for cars");

        try {
            setAutoRaiFanState(AutoRaiFanState.INSIDE_AUTORAI);
            System.out.println(toString() + "Looking for cars..");
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {

        while (true){
            try {
                autoRai.accessController.join(this);
                autoRai.accessController.onLeave(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            onJoin();
        }


    }

    @Override
    public String toString() {
        return "Buyer";
    }
}
