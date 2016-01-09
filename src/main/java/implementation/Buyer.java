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
    protected void signUp() {
        assert getAutoRaiFanState() == AutoRaiFanState.CRAVING_COOL_CARS;
        autoRai.accessController.signUpBuyer(this);
        setAutoRaiFanState(AutoRaiFanState.IN_LINE);

    }

    @Override
    protected void signOut() {
        //tel them you are off
        autoRai.accessController.signOutBuyer(this);

        //greedy ass b* wants more cars
        setAutoRaiFanState(AutoRaiFanState.CRAVING_COOL_CARS);

        // crave cars

    }

    @Override
    protected void enjoyAutoRai() {
        assert getAutoRaiFanState() == AutoRaiFanState.INSIDE_AUTORAI;
        System.out.println("omg dez carz");


    }


}
