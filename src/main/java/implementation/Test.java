package implementation;

/**
 * Created by jonathan on 9-1-16.
 */
public class Test {


    //TODO testcases


    /**
     *
     */
    private static void testCase1(){

        AutoRai a = new AutoRai();



        for (int i = 0; i < 10; i++) {
            new Visitor(a).start();
            new Visitor(a).start();
            new Buyer(a).start();

        }


    }



    public static void main(String[] args) {

        AutoRai a = new AutoRai();



        for (int i = 0; i < 4; i++) {

            //new Visitor(a).start();
            //new Visitor(a).start();
            new Buyer(a).start();
            new Buyer(a).start();
        }

        new Visitor(a).start();

    }
}
