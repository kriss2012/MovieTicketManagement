package com.movie.ticket.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.movie.ticket.entity.User;
import com.movie.ticket.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // User Login Page
    @GetMapping("/login")
    public String showUserLoginForm(@RequestParam(value = "error", required = false) String error,
                                    @RequestParam(value = "success", required = false) String success,
                                    Model model) {
        if ("invalid".equals(error)) {
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
        } else if ("login_required".equals(error)) {
            model.addAttribute("errorMessage", "Please log in to continue booking your tickets.");
        }
        if ("registered".equals(success)) {
            model.addAttribute("successMessage", "Account created successfully! Please log in.");
        }
        return "auth/login";
    }

    // Process User Login
    @PostMapping("/login")
    public String processUserLogin(@RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   HttpSession session) {

        User user = userService.authenticateUser(email, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);

            String redirect = (String) session.getAttribute("redirectAfterLogin");
            if (redirect != null) {
                session.removeAttribute("redirectAfterLogin");
                return "redirect:" + redirect;
            }
            if (user.isAdmin()) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        }
        return "redirect:/login?error=invalid";
    }

    // User Registration Page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // Process User Registration
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, Model model) {
        try {
            user.setRole("ROLE_USER");
            userService.registerUser(user);
            return "redirect:/login?success=registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }

    // Admin Login Page
    @GetMapping("/admin/login")
    public String showAdminLoginForm(@RequestParam(value = "error", required = false) String error,
                                     Model model) {
        if ("invalid".equals(error)) {
            model.addAttribute("errorMessage", "Invalid admin credentials.");
        } else if ("access_denied".equals(error)) {
            model.addAttribute("errorMessage", "Access denied. Admin rights required.");
        } else if ("auth_required".equals(error)) {
            model.addAttribute("errorMessage", "Admin session required. Please sign in.");
        }
        return "auth/admin-login";
    }

    // Process Admin Login
    @PostMapping("/admin/login")
    public String processAdminLogin(@RequestParam("email") String email,
                                    @RequestParam("password") String password,
                                    HttpSession session) {

        User admin = userService.authenticateAdmin(email, password);
        if (admin != null) {
            session.setAttribute("loggedInUser", admin);
            return "redirect:/admin/dashboard";
        }
        return "redirect:/admin/login?error=invalid";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/?logout=success";
    }
}
