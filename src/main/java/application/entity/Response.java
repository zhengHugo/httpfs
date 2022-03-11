package application.entity;

public class Response {
    String status;
    Header headers;
    String body;

    public Response(String status, Header headers, String body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
        this.headers.addEntry("content-length", String.valueOf(this.body.length()));
        this.headers.addEntry("server", "Concordia/COMP445A2");
    }

    public Response() {
    }

    public String getStatus() {
        return this.status;
    }

    public Header getHeaders() {
        return this.headers;
    }

    public String getBody() {
        return this.body;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHeaders(Header headers) {
        this.headers = headers;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        String statusLine = "HTTP/1.0 " + status + "\r\n";
        String headerLines = "";
        String body = this.getBody();
        String res = "";

        headerLines = headers.toString();
        headerLines = headerLines.concat("\r\n");
        res = statusLine.concat(headerLines).concat(body);

        return res;
    }

}
