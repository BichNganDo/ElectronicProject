package servlets.client;

import com.google.gson.Gson;
import common.APIResult;
import common.Configuration;
import entity.news.News;
import entity.user_register.UserRegister;
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
import javax.servlet.http.HttpSession;
import model.NewsModel;
import model.RegisterModel;
import org.json.JSONObject;
import servlets.client.include.IncludeData;
import templater.PageGenerator;

public class Login extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Configuration.APP_DOMAIN);
        pageVariables.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);

        News aboutBlog = NewsModel.INSTANCE.getNewsByType("about");
        pageVariables.put("about_blog", aboutBlog);

        //HEADER
        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));

        //FOOTER
        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", IncludeData.INSTANCE.buildFooterData()));

        //HEADER MENU
        pageVariables.put("header_menu", PageGenerator.instance().getPage("client/include/header_menu.html", IncludeData.INSTANCE.buildHeaderMenuData(request)));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/login.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String body = HttpHelper.getBodyData(request);
        JSONObject jbody = new JSONObject(body);

        String email = jbody.optString("email");
        String password = SecurityHelper.getMD5Hash(jbody.optString("password"));
        UserRegister checkLogin = RegisterModel.INSTANCE.checkLogin(email, password);
        if (checkLogin != null) {
            HttpSession session = request.getSession();
            session.setAttribute("name", checkLogin.getName());
            session.setAttribute("email", email);
            result.setErrorCode(0);
            result.setMessage("Đăng nhập thành công!");
        } else {
            result.setErrorCode(-3);
            result.setMessage("Email hoặc mật khẩu không đúng");
        }

        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
