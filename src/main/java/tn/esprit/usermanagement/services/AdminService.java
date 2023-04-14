package tn.esprit.usermanagement.services;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.Role;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    String addMod(String email) throws IOException, GeoIp2Exception;
    String banUser(String email);
    List<User> getUsers (Role role);
    String suspendUser(String email);
    String unbanUser(String email);
    String automaticUnbanUser();
    String banIpUser(String email);
    String approveProduct(Integer id);
}
