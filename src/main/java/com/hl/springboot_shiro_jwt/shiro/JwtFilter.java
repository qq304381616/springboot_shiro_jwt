package com.hl.springboot_shiro_jwt.shiro;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 所有的请求都会先经过Filter
 * 执行流程 preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 此方法调用登陆，验证逻辑
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String header = getAuthzHeader(request);
        if (header != null && !"".equals(header)) {
            try {
                JwtToken token = new JwtToken(header);
                getSubject(request, response).login(token);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}