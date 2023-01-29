import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Sim {

  public static void run_sim(MBTA mbta, Log log) {
    mbta.setLog(log);

    // launch train threads
      for (Thread t: mbta.train_workers) {
        t.start();
      }

    // launch passenger threads

      for (Thread t: mbta.pas_workers) {
        t.start();
      }


    // wait for the threads to finish
    for (Thread t: mbta.train_workers) {
      try {
        t.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
//    for (Thread t: mbta.pas_workers) {
//      try {
//        t.join();
//      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
//    }

  }

  public static void main(String[] args) throws Exception {
    System.out.println("In main of Sim");
    if (args.length != 1) {
      System.out.println("usage: ./sim <config file>");
      System.exit(1);
    }

    MBTA mbta = new MBTA();
    mbta.loadConfig(args[0]);

    Log log = new Log();

    run_sim(mbta, log);

    String s = new LogJson(log).toJson();
    PrintWriter out = new PrintWriter("log.json");
    out.print(s);
    out.close();

    mbta.reset();
    mbta.loadConfig(args[0]);
    Verify.verify(mbta, log);
  }
}
