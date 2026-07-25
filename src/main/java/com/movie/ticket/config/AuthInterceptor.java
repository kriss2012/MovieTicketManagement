package com.movie.ticket.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.movie.ticket.entity.User;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String path = request.getRequestURI();
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        // Allow admin login page without authentication check
        if ("/admin/login".equals(path) || path.startsWith("/admin/login")) {
            return true;
        }

        // Admin paths protection
        if (path.startsWith("/admin") || path.startsWith("/movies/add") || path.startsWith("/movies/edit")
                || path.startsWith("/movies/delete") || path.startsWith("/theatres/add")
                || path.startsWith("/theatres/edit") || path.startsWith("/shows/add")) {

            if (user == null) {
                response.sendRedirect("/admin/login?error=auth_required");
                return false;
            }

            if (!user.isAdmin()) {
                response.sendRedirect("/admin/login?error=access_denied");
                return false;
            }
        }

        // User protected booking routes
        if (path.startsWith("/bookings/summary") || path.startsWith("/bookings/confirm") || path.startsWith("/my-bookings")) {
            if (user == null) {
                String redirect = path;
                if (request.getQueryString() != null) {
                    redirect += "?" + request.getQueryString();
                }
                session = request.getSession(true);
                session.setAttribute("redirectAfterLogin", redirect);
                response.sendRedirect("/login?error=login_required");
                return false;
            }
        }

        return true;
    }
}
