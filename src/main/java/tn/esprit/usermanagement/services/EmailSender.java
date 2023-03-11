package tn.esprit.usermanagement.services;

public interface EmailSender {
    void send(String to, String email);
    public void sendClaimEmail(String to, String email);
}