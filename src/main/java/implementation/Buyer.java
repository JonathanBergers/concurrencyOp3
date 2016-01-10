package implementation;

import interfaces.AutoRaiFanState;

/**
 * Created by falco on 9-1-16.
 *
 */
public class Buyer extends AutoRaiFan{

    private int receipt = 0;

    //TODO MOVE ALL METHODS TO AUTORAIFAN EXCEPT TOSTRING
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


            setAutoRaiFanState(AutoRaiFanState.INSIDE_AUTORAI);


    }


    @Override
    public void run() {

        while (true){
            try {
                onJoin();
                autoRai.accessController.join(this);
                onEnter();
                lookAround();
                while (!autoRai.accessController.onLeave(this)){
                    lookAround();
                }
                onLeave();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            onJoin();
        }


    }

    @Override
    public String toString() {
        return "Buyer: "+ id + "      ";
    }

    protected void lookAround(){

        try {
            if(autoRaiFanState.equals(AutoRaiFanState.INSIDE_AUTORAI)){
                System.out.println(toString() + "Looking at cars to buy");
                sleep(5000);
                buyCar();
            } else {
                System.out.println(toString() + "I ant to look at cars, but I am not inside the autoRai");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void buyCar(){
        receipt = (int) (Math.random()*100+1) * 500;
        System.out.println(toString() + "just bought a car for "+receipt+" euro's");
    }

    public boolean didBoughtAExpensiveEnoughCar(){
        return receipt>=25000;
    }
}
