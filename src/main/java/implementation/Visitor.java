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
    protected void signUp() {

        assert getAutoRaiFanState() == AutoRaiFanState.CRAVING_COOL_CARS;
        autoRai.accessController.signUpVisitor(this);
        setAutoRaiFanState(AutoRaiFanState.IN_LINE);
    }

    @Override
    protected void signOut() {
        // tell the controller that you left
        autoRai.accessController.signOutVisitor(this);

        // set state
        setAutoRaiFanState(AutoRaiFanState.CRAVING_COOL_CARS);


        // crave some sexy cars
    }

    @Override
    protected void enjoyAutoRai() {
        assert getAutoRaiFanState() == AutoRaiFanState.INSIDE_AUTORAI;
        System.out.println("omg dez carz");
    }
}
