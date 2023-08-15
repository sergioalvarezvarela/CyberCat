package es.cybercatapp.web.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;

public class AdminChecker {
    public static void isAdmin (Model model, Authentication authentication){
        boolean isAdmin = false;
        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }
        }
        model.addAttribute("isAdmin", isAdmin);
    }
}
