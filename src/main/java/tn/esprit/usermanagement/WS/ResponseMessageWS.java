package tn.esprit.usermanagement.WS;

public class ResponseMessageWS {
    private String content;

    public ResponseMessageWS() {
    }

    public ResponseMessageWS(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
