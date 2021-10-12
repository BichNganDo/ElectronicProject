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
import model.InfoBuyerModel;
import org.json.JSONObject;

public class InfoBuyer extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String body = HttpHelper.getBodyData(request);
        JSONObject jbody = new JSONObject(body);

        String name = jbody.optString("name");
        String email = jbody.optString("email");
        String phone = jbody.optString("phone");
        String address = jbody.optString("address");
        String note = jbody.optString("note");
        int addInfoBuyer = InfoBuyerModel.INSTANCE.addInfoBuyer(name, email, phone, address, note);
        if (addInfoBuyer >= 0) {
            result.setErrorCode(0);
            result.setMessage("Thanh toán thành công!");
        } else {
            result.setErrorCode(-1);
            result.setMessage("Thanh toán thất bại!");
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
