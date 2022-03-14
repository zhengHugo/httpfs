package application;

import lib.RunServer;
import picocli.CommandLine;

import java.io.IOException;

/*@CommandLine.Command(name = "httpc")
public Integer value {
  return 0;
        }*/
public class Httpfs {

  public static void main(String[] args) {
    try {
      RunServer.listenAndServe(80, new RequestHandlerImp());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
