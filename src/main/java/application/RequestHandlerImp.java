package application;

import application.entity.Request;
import application.entity.Response;
import lib.RequestHandler;
import lib.RunServer;

import java.io.*;

public class RequestHandlerImp implements RequestHandler {
  String directory = ".";
  Response response;

  @Override
  public Response handleRequest(Request request) throws IOException {

    if (request.getMethod().toString().equals("GET")) {
      return handleGet(request.getFile());
    }
    if (request.getMethod().toString().equals("POST")) {
      return handlePost(request.getFile());
    }
    return null;
  }

  private Response handleGet(String file) throws IOException {
    if (file.equals("/")) {
      StringBuilder bodyBuilder = new StringBuilder();
      File dir = new File(directory + file);
      File[] files = dir.listFiles();

      assert files != null;
      for (File value : files) {
        if (value.isFile()) {
          bodyBuilder.append("File: ");
        } else if (value.isDirectory()) {
          bodyBuilder.append("Directory: ");
        } else {
          continue;
        }
        bodyBuilder.append(value.getName()).append("\n");
      }
      bodyBuilder.append("}");
      response.setBody(bodyBuilder.toString());
      return response;
    } else {
      File fileRequest = new File(file);
      if (!fileRequest.exists()) {
        response.setStatus("405");
        return response;
      }
      BufferedReader reader = new BufferedReader(new FileReader(file));
      StringBuilder bodyBuilder = new StringBuilder();
      String line = reader.readLine();
      while (line != null) {
        bodyBuilder.append(line);
        bodyBuilder.append("\n");
        line = reader.readLine();
      }
      response.setBody(bodyBuilder.toString());
      return response;
    }
  }

  private Response handlePost(String file) throws FileNotFoundException {

    PrintWriter fileWriter = new PrintWriter(new FileOutputStream(file, false));
    fileWriter.println(file);
    fileWriter.flush();
    fileWriter.close();
// don't know how to return it
    return response;
  }
}
