package implementation;

import interfaces.AutoRaiFanState;

/**
 * Created by falco on 9-1-16.
 */
public class Visitor  extends AutoRaiFan{


    protected Visitor(AutoRai autoRai) {
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
        setAutoRaiFanState(AutoRaiFanState.INSIDE_AUTORAI);


    }

    protected void lookAround(){

        try {
            if(autoRaiFanState.equals(AutoRaiFanState.INSIDE_AUTORAI)){
                System.out.println(toString() + "Looking at cars..");
                sleep(5000);
            } else {
                System.out.println(toString() + "I ant to look at cars, but I am not inside the autoRai");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        while (true){
            try {
                onJoin();
                autoRai.accessController.join(this);
                onEnter();
                lookAround();
                autoRai.accessController.onLeave(this);
                onLeave();
                goHomeAnddoThings();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            onJoin();
        }


    }

    @Override
    public String toString() {
        return "Visitor: "+ id + "\t\t";
    }
}
