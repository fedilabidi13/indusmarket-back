package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.Role;

import java.util.List;

public interface AdminService {
    String addMod(String email);
    String banUser(String email);
    List<User> getUsers (Role role);
}
