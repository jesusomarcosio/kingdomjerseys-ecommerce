package com.kingdomjerseys.ecommerce.config;

import com.kingdomjerseys.ecommerce.service.AdministradorService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Si un administrador es desactivado (o eliminado) mientras tiene la sesión abierta,
 * se le cierra la sesión en su siguiente petición al panel.
 */
public class AdministradorActivoFilter extends OncePerRequestFilter {

    private final AdministradorService administradorService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public AdministradorActivoFilter(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean autenticado = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);

        if (autenticado && !administradorService.estaActivo(auth.getName())) {
            logoutHandler.logout(request, response, auth);   // invalida la sesión HTTP
            response.sendRedirect(request.getContextPath() + "/admin/login?error");
            return;
        }
        chain.doFilter(request, response);
    }
}