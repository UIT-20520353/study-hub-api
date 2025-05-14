package com.backend.study_hub_api.filter;

import com.backend.study_hub_api.config.jwt.ExtractJwtResult;
import com.backend.study_hub_api.config.jwt.JwtProvider;
import com.backend.study_hub_api.helper.enumeration.CheckJwtResult;
import com.backend.study_hub_api.service.UserService;
import com.backend.study_hub_api.service.UserSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

import static com.backend.study_hub_api.helper.constant.HeaderConstant.*;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserSessionService sessionService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String accessToken = retrieveToken(request);

        if (StringUtils.isNotBlank(accessToken)) {
            ExtractJwtResult extractJwtResult = jwtProvider.extractClaims(accessToken);
            if (extractJwtResult.isValid() && sessionService.checkUserSession(extractJwtResult.getTokenId())) {
                Set<String> author = extractJwtResult.getAuthorities();
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        extractJwtResult.getUserId(),
                        extractJwtResult.getTokenId(),
                        author.stream()
                              .map(SimpleGrantedAuthority::new)
                              .toList()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                if (CheckJwtResult.EXPIRED == extractJwtResult.status()) {
                    response.setHeader(IS_TOKEN_EXPIRED_HEADER, "true");
                }
                setAnonymousContext();
            }
        } else {
            setAnonymousContext();
        }

        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request) {
        final String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(token) || !token.startsWith(TOKEN_PREFIX)) {
            return StringUtils.EMPTY;
        }
        return token.replace(TOKEN_PREFIX, StringUtils.EMPTY).trim();
    }

    private void setAnonymousContext() {
        SecurityContextHolder.clearContext();
        //        SecurityContextHolder.getContext().setAuthentication(SecurityUtils.createAnonymous());
    }
}

