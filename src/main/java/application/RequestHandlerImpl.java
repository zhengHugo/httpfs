package application;

import java.nio.file.Path;
import java.nio.file.Paths;
import lib.RequestHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import lib.entity.HttpMethod;
import lib.entity.Request;
import lib.entity.Response;

public class RequestHandlerImpl implements RequestHandler {

  private String rootDir = "src/main/resources/data";

  public void setRootDir(String rootDir) {
    this.rootDir = rootDir;
  }

  @Override
  public Response handleRequest(Request request) {
    if (request.getMethod().equals(HttpMethod.GET)) {
      return handleGet(request.getFile());
    }
    if (request.getMethod().equals(HttpMethod.POST)) {
      return handlePost(request);
    }
    return null;
  }

  private Response handleGet(String file) {
    Response response = new Response();
    if (file.equals("") || file.equals("/")) {
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
      return response;
    } else {
      Path root = Paths.get(rootDir);
      Path filePath = Paths.get(rootDir + file);
      if (!filePath.startsWith(root)) {
        response.setStatus("403 Forbidden");
        response.setBody("Unable to access");
        response.addHeader("Content-Type", "text/plain");
        response.addHeader(
            "Content-Length",
            String.valueOf(response.getBody().getBytes(StandardCharsets.UTF_8).length));
        return response;
      } else if (!new File(rootDir + file).exists()) {
        response.setStatus("404 Not Found");
        response.setBody("File not exist");
        response.addHeader("Content-Type", "text/plain");
        response.addHeader(
            "Content-Length",
            String.valueOf(response.getBody().getBytes(StandardCharsets.UTF_8).length));
        return response;
      } else {

        BufferedReader reader;
        try {
          reader = new BufferedReader(new FileReader(rootDir + file));
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
        } catch (IOException e) {
          response.setStatus("500 Internal Server Error");
          response.setBody("Unable to read file");
          response.addHeader("Content-Type", "text/plain; charset=UTF-8");
          response.addHeader(
              "Content-Length",
              String.valueOf(response.getBody().getBytes(StandardCharsets.UTF_8).length));
        }
      }
      System.out.println(response);
      return response;
    }
  }

  private Response handlePost(Request request) {
    Response response = new Response();
    File requestedFile = new File(rootDir + request.getFile());
    PrintWriter fileWriter;
    try {
      fileWriter = new PrintWriter(new FileOutputStream(requestedFile, false));
      fileWriter.print(request.getBody());
      fileWriter.flush();
      fileWriter.close();
      response.setStatus("200 OK");
    } catch (FileNotFoundException e) {
      response.setStatus("500 Internal Server Error");
      response.setBody("Unable to read file");
      response.addHeader("Content-Type", "text/plain; charset=UTF-8");
      response.addHeader(
          "Content-Length",
          String.valueOf(response.getBody().getBytes(StandardCharsets.UTF_8).length));
    }

    return response;
  }
}
