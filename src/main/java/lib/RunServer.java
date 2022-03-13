package lib;

import application.entity.Request;
import application.entity.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RunServer {

  private void listenAndServe(int port, RequestHandler requestHandler) throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    Socket socket = serverSocket.accept();
    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter writer = new PrintWriter(socket.getOutputStream());
    String line = reader.readLine();
    StringBuilder stringBuilder = new StringBuilder();
    while (line != null) {
      stringBuilder.append(line);
      stringBuilder.append("\n");
      line = reader.readLine();
    }
    Request request = Request.fromString(stringBuilder.toString());
    Response response = requestHandler.handleRequest(request);
    writer.println(response.toString());
    writer.flush();
    writer.close();
  }
}
