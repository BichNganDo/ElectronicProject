package servlets.API;

import com.google.gson.Gson;
import common.APIResult;
import helper.HttpHelper;
import helper.SecurityHelper;
import helper.ServletUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AdminModel;
import org.json.JSONObject;

public class APILoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "login": {
                String phone = request.getParameter("phone");
                String password = SecurityHelper.getMD5Hash(request.getParameter("password"));
                boolean checkLogin = AdminModel.INSTANCE.checkLogin(phone, password);
                if (checkLogin) {
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
