package application.entity;

public class Request {
    HttpMethod method;
    String file;

    public static Request fromString(String rawString) {
        Request request = new Request();
        String[] lines = rawString.split("\n");
        String[] line = lines[0].split(" ");
        String requestMethod = line[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String file = line[1];
        request.method = HttpMethod.valueOf(requestMethod);
        request.file = file;

        return request;
    }

    public HttpMethod getMethod() {
        return this.method;
    }
    public String getFile() {return this.file; }

}
