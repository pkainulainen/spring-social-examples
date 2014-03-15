package net.petrikainulainen.spring.social.signinmvc.security;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

/**
 * @author Petri Kainulainen
 */
public class CsrfTokenBuilder {

    private String headerName;
    private String requestParameterName;
    private String tokenValue;

    public CsrfTokenBuilder() {

    }

    public CsrfTokenBuilder headerName(String headerName) {
        this.headerName = headerName;
        return this;
    }

    public CsrfTokenBuilder requestParameterName(String requestParameterName) {
        this.requestParameterName = requestParameterName;
        return this;
    }

    public CsrfTokenBuilder tokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
        return this;
    }

    public CsrfToken build() {
        return new DefaultCsrfToken(headerName, requestParameterName, tokenValue);
    }
}
