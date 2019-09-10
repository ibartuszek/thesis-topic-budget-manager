package hu.elte.bm.authenticationservice.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;

import hu.elte.bm.authenticationservice.dal.BlackListDao;

public class TokenFilter implements Filter {

    private final BlackListDao blackListDao;

    public TokenFilter(final BlackListDao blackListDao) {
        this.blackListDao = blackListDao;
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        String tokenToCheck = ((HttpServletRequest) servletRequest).getHeader("Authorization");
        if (blackListDao.isExpired(tokenToCheck)) {
            throw new InvalidTokenException("Token is invalid. User logged out before");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
