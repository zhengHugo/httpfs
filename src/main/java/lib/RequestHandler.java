package lib;

import application.entity.Request;
import application.entity.Response;

import java.io.IOException;

public interface RequestHandler {
  Response handleRequest(Request request) throws IOException;
}
