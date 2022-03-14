package application;

import lib.entity.HttpMethod;
import lib.entity.Request;
import lib.entity.Response;
import lib.RequestHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class RequestHandlerImpl implements RequestHandler {
  private String rootDir = "src/main/resources/data";

  public void setRootDir(String rootDir) {
    this.rootDir = rootDir;
  }

  @Override
  public Response handleRequest(Request request) throws IOException {
    if (request.getMethod().equals(HttpMethod.GET)) {
      return handleGet(request.getFile());
    }
    if (request.getMethod().equals(HttpMethod.POST)) {
      return handlePost(request);
    }
    return null;
  }

  private Response handleGet(String file) throws IOException {
    Response response = new Response();
    if (file.equals("/")) {
      StringBuilder bodyBuilder = new StringBuilder();
      File dir = new File(rootDir);
      File[] files = dir.listFiles();
      if (files != null) {
        for (File value : files) {
          bodyBuilder.append(value.getName()).append("\n");
        }
        response.setStatus("200 OK");
        response.setBody(bodyBuilder.toString());
      } else {
        response.setStatus("404 Not Found");
        response.setBody("File not exist");
        response.addHeader("Content-Type", "text/plain");
        response.addHeader(
                "Content-Length",
                String.valueOf(response.getBody().getBytes(StandardCharsets.UTF_8).length));
      }
    } else {
      File requestedFile = new File(rootDir + file);
      if (!requestedFile.exists()) {
        response.setStatus("404 Not Found");
        response.setBody("File not exist");
        response.addHeader("Content-Type", "text/plain");
        response.addHeader(
                "Content-Length",
                String.valueOf(response.getBody().getBytes(StandardCharsets.UTF_8).length));
        return response;
      }
      BufferedReader reader = new BufferedReader(new FileReader(rootDir + file));
      StringBuilder bodyBuilder = new StringBuilder();
      String line = reader.readLine();
      while (line != null) {
        bodyBuilder.append(line);
        bodyBuilder.append("\n");
        line = reader.readLine();
      }
      response.setBody(bodyBuilder.toString());
      response.setStatus("200 OK");
      response.addHeader("Content-Type", "text/plain; charset=UTF-8");
      response.addHeader(
          "Content-Length",
          String.valueOf(response.getBody().getBytes(StandardCharsets.UTF_8).length));
    }
    System.out.println(response);
    return response;
  }

  private Response handlePost(Request request) throws FileNotFoundException {
    Response response = new Response();
    File requestedFile = new File(rootDir + request.getFile());
    PrintWriter fileWriter = new PrintWriter(new FileOutputStream(requestedFile, false));
    fileWriter.print(request.getBody());
    fileWriter.flush();
    fileWriter.close();
    response.setStatus("200 OK");

    return response;
  }
}
