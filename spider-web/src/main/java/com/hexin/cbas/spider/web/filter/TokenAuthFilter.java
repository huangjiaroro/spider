package com.hexin.cbas.spider.web.filter;

import com.alibaba.fastjson.JSON;
import com.hexin.cbas.spider.constants.LogConstant;
import com.hexin.cbas.spider.constants.StatusCodeConstant;
import com.hexin.cbas.spider.dal.po.metacollection.User;
import com.hexin.cbas.spider.domain.metacollection.repository.IUserRepository;
import com.hexin.cbas.spider.utils.LogUtil;
import com.hexin.cbas.spider.web.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 权限认证过滤器
 *
 * @author wanghujia
 * @since 2021/9/18
 */
@WebFilter
@Slf4j
@Component
public class TokenAuthFilter implements Filter {

    @Value("${api.auth:false}")
    private boolean apiAuth;

    @Value("${api.auth-include}")
    private Set<String> apiAuthExclude;

    static final String DM_USER = "DM_USER";

    @Autowired
    private IUserRepository userRepository;

    @Override
    public void init(FilterConfig filterConfig) {
        if (apiAuthExclude == null) {
            apiAuthExclude = new HashSet<>();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        MDC.put(LogConstant.BEGIN_TIME, String.valueOf(System.currentTimeMillis()));
        MDC.put(LogConstant.TRACE_ID, UUID.randomUUID().toString());
        MDC.put(LogConstant.URI, ((HttpServletRequest) request).getRequestURI());

        if (!apiAuth) {
            User user = new User();
            user.setEmail("admin@myhexin.com");
            user.setRole("ADMIN");
            request.setAttribute(DM_USER, user);
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().replaceFirst(req.getContextPath(), "");

        if (skipAuth(path)) {
            chain.doFilter(request, response);
            return;
        }

        String cert = req.getHeader("kyc_client_cert");
        // 用于涉及到用户的接口问题排查
        String dmEmail = req.getHeader("dm_email");
        User user = getUser(cert, dmEmail);

        if (user == null) {
            LogUtil.warn("认证失败, 用户不存在 cert: {}", cert);
            returnJson(res, StatusCodeConstant.AUTH_ERROR, "用户不存在");
            return;
        }

        MDC.put(LogConstant.LOGIN_USER, user.getChineseName());

        String dpAuth = getCookie(req, "DPAuth");
        String sessionId = getCookie(req, "PHPSESSID");
        if (!StringUtils.isEmpty(dpAuth) && !StringUtils.isEmpty(sessionId)) {
            String key = sessionId + "#" + user.getEmail();
            String md5Value = DigestUtils.md5Hex(key);
            if (!dpAuth.equals(md5Value)) {
                LogUtil.warn("会话ID和邮箱不一致, email: {}, session: {}, md5: {}", user.getEmail(), sessionId, md5Value);
                returnJson(res, StatusCodeConstant.AUTH_ERROR, "会话ID和邮箱不一致");
                return;
            }
        }

        req.setAttribute(DM_USER, user);
        chain.doFilter(request, response);
    }

    private User getUser(String cert, String dmEmail) {
        if (!StringUtils.isEmpty(cert)) {
            if (log.isDebugEnabled()) {
                log.debug("client cert: {}", cert);
            }
            String userCert = CertUtils.getMd5Cert(cert);
            return userRepository.getByCert(userCert);
        }

        if (!StringUtils.isEmpty(dmEmail)) {
            if (log.isDebugEnabled()) {
                log.debug("dm email: {}", dmEmail);
            }
            return userRepository.getByEmail(dmEmail);
        }
        return null;
    }

    private void returnJson(HttpServletResponse res, String code, String msg) {
        res.setContentType("application/json");
        res.setCharacterEncoding("utf8");

        ResponseDTO response = ResponseDTO.error(code, msg);
        String retJson = JSON.toJSONString(response);

        try (PrintWriter out = res.getWriter()) {
            out.print(retJson);
            out.flush();
        } catch (Exception e) {
            LogUtil.error("返回 Json: {} 异常", retJson, e);
        }
    }

    private String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean skipAuth(String path) {
        if (CollectionUtils.isEmpty(apiAuthExclude)) {
            return false;
        } else {
            for (String startUri : apiAuthExclude) {
                if (path.startsWith(startUri)) {
                    return true;
                }
            }
        }
        return false;
    }
}
