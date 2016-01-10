package implementation;

/**
 * Created by jonathan on 9-1-16.
 */
public class Test {


    //TODO testcases



    public static void main(String[] args) {

        AutoRai a = new AutoRai();



        for (int i = 0; i < 10; i++) {

            new Visitor(a).start();
            new Visitor(a).start();
            //new Buyer(a).start();
            //new Buyer(a).start();
        }

        //new Visitor(a).start();
        new Buyer(a).start();

    }
}
