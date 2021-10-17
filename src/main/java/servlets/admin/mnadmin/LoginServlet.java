package servlets.admin.mnadmin;

import com.google.gson.Gson;
import common.APIResult;
import common.Configuration;
import entity.admin.Admin;
import helper.HttpHelper;
import helper.SecurityHelper;
import helper.ServletUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AdminModel;
import model.JWTModel;
import templater.PageGenerator;

public class LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Configuration.APP_DOMAIN);
        pageVariables.put("static_domain", Configuration.STATIC_ADMIN_DOMAIN);
        pageVariables.put("message", "hello word");

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("admin/mnadmin/login.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "login": {
                String phone = request.getParameter("phone");
                String password = SecurityHelper.getMD5Hash(request.getParameter("password"));
                Admin checkAdmin = AdminModel.INSTANCE.checkLogin(phone, password);
                if (checkAdmin != null) {
                    String jwtToken = JWTModel.INSTANCE.genJWT(checkAdmin.getUsername(), checkAdmin.getPhone(), checkAdmin.getRole());
                    HttpHelper.setCookie(response, "authen", jwtToken, 86400);
                    result.setErrorCode(0);
                    result.setMessage("Đăng nhập thành công!");
                } else {
                    result.setErrorCode(-3);
                    result.setMessage("Số điện thoại hoặc mật khẩu không đúng");
                }
                break;
            }

            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
