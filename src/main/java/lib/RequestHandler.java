package lib;



import lib.entity.Request;
import lib.entity.Response;

import java.io.IOException;

public interface RequestHandler {
  Response handleRequest(Request request) throws IOException;
}
