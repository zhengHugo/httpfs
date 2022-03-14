package lib;

import application.entity.Request;
import application.entity.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RunServer {

  public static void listenAndServe(int port, RequestHandler requestHandler) throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    while (true) {
      Socket socket = serverSocket.accept();
      System.out.println("Connection built");
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

      String line = reader.readLine();
      System.out.println(line);
      StringBuilder stringBuilder = new StringBuilder();
      boolean readLineEnd = false;
      while (line != null) {
        if (line.isEmpty()) {
          if (readLineEnd) break;
          else readLineEnd = true;
        }else {
          readLineEnd = false;
        }
        stringBuilder.append(line);
        stringBuilder.append("\n");
        line = reader.readLine();
      }
      System.out.println(stringBuilder);
      Request request = Request.fromString(stringBuilder.toString());
      Response response = requestHandler.handleRequest(request);
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
      writer.println(response.toString());
      writer.flush();
      writer.close();
    }
  }
}
