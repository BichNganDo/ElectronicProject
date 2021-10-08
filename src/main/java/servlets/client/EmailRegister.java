package servlets.client;

import com.google.gson.Gson;
import common.APIResult;
import helper.HttpHelper;
import helper.ServletUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.EmailRegisterModel;
import org.json.JSONObject;

public class EmailRegister extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String body = HttpHelper.getBodyData(request);
        JSONObject jbody = new JSONObject(body);

        String email = jbody.optString("email");
        int addEmail = EmailRegisterModel.INSTANCE.addEmail(email);
        if (addEmail >= 0) {
            result.setErrorCode(0);
            result.setMessage("Đăng ký thành công!");
        } else {
            result.setErrorCode(-1);
            result.setMessage("Đăng ký thất bại!");
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
