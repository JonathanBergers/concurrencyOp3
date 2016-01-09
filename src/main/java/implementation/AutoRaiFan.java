package implementation;

import interfaces.AutoRaiFanState;

/**
 * Created by jonathan on 9-1-16.
 */
public abstract class AutoRaiFan extends Thread{

    protected final AutoRai autoRai;
    public AutoRaiFanState autoRaiFanState;


    protected AutoRaiFan(AutoRai autoRai) {
        this.autoRai = autoRai;
    }



    /**called to join the line at autorai
     *
     */
    protected abstract void onJoin();

    /** called when fan gets out of autorai
     *
     */
    protected abstract void onLeave();


    /**called after te user enters the autorai
     *
     *
     */
    protected abstract void onEnter();



    public AutoRaiFanState getAutoRaiFanState() {
        return autoRaiFanState;
    }

    public void setAutoRaiFanState(AutoRaiFanState autoRaiFanState) {
        this.autoRaiFanState = autoRaiFanState;
    }


    public boolean hasState(AutoRaiFanState state){
        return this.autoRaiFanState.equals(state);
    }



}
