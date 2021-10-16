package servlets.admin.filter;

import helper.HttpHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.JWTModel;
import org.apache.commons.lang3.StringUtils;

public class AuthenFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;

            String path = req.getServletPath();

            if (!"/admin/login".equals(path)) {
                String authenCookie = HttpHelper.getCookie(req, "authen");
                if (StringUtils.isNotEmpty(authenCookie)) {
                    Jws<Claims> admin = JWTModel.INSTANCE.parseJwt(authenCookie);
                    if (admin != null) {
                        String phone = admin.getBody().get("phone", String.class);
                        if (phone != null) {
                            fc.doFilter(request, response);
                            return;
                        }
                    }
                    resp.sendRedirect("http://localhost:8080/admin/login");
                    return;
                } else {
                    resp.sendRedirect("http://localhost:8080/admin/login");
                    return;
                }
            }
            fc.doFilter(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void destroy() {
    }

}
