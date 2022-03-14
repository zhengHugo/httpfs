package lib;
import lib.entity.Request;
import lib.entity.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RunServer {

  public static void listenAndServe(int port, RequestHandler requestHandler, boolean verbose) throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    while (true) {
      Socket socket = serverSocket.accept();
      System.out.println("Connection built");
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

      String line = reader.readLine();
      StringBuilder stringBuilder = new StringBuilder();
      boolean readLineEnd = false;
      while (line != null) {
        if (line.isEmpty()) {
          stringBuilder.append("\n");
          break;
        }
        stringBuilder.append(line);
        stringBuilder.append("\n");
        line = reader.readLine();
      }
      if (verbose) {
        System.out.println("Request:");
        System.out.println(stringBuilder);
      }

      Request request = Request.fromString(stringBuilder.toString());
      StringBuilder bodyBuilder = new StringBuilder();
      if (request.getHeader().getValue("Content-Length").isPresent()) {
        // read body
        int sizeLeft = Integer.parseInt(request.getHeader().getValue("Content-Length").get());
        while(sizeLeft > 0) {
          String nextChar = String.valueOf((char) reader.read());
          sizeLeft -= nextChar.getBytes(StandardCharsets.UTF_8).length;
          bodyBuilder.append(nextChar);
        }
        request.setBody(bodyBuilder.toString());
      }

      Response response = requestHandler.handleRequest(request);
      if (verbose) {
        System.out.println("Response: ");
        System.out.println(response);
      }
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
      writer.println(response.toString());
      writer.flush();
      writer.close();
    }
  }
}
