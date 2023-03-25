package tn.esprit.usermanagement.services;

public interface EmailSender {
    void send(String to, String email);
    void sendForProductRestock(String to, String email);

    void sendForProductRequest(String to, String email);

    public void sendClaimEmail(String to, String email);
    public void sendEventEmail(String to, String email);
}