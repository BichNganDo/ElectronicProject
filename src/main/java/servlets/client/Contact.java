package servlets.client;

import com.google.gson.Gson;
import common.APIResult;
import common.Configuration;
import entity.setting.Setting;
import helper.HttpHelper;
import helper.ServletUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ContactModel;
import model.SettingModel;
import org.json.JSONObject;
import servlets.client.include.IncludeData;
import templater.PageGenerator;

public class Contact extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Configuration.APP_DOMAIN);
        pageVariables.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);

        List<Setting> listSettingByKeyss = SettingModel.INSTANCE.getListSettingByKey("'Địa chỉ', 'Điện thoại', 'Email'");
        pageVariables.put("list_setting_by_keyss", listSettingByKeyss);

        Setting settingMap = SettingModel.INSTANCE.getSettingByKey("Map");
        pageVariables.put("setting_map", settingMap);

        //HEADER
        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));

        //FOOTER
        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", IncludeData.INSTANCE.buildFooterData()));

        //HEADER MENU
        pageVariables.put("header_menu", PageGenerator.instance().getPage("client/include/header_menu.html", IncludeData.INSTANCE.buildHeaderMenuData(request)));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/contact.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String body = HttpHelper.getBodyData(request);
        JSONObject jbody = new JSONObject(body);

        String name = jbody.optString("name");
        String email = jbody.optString("email");
        String phone = jbody.optString("phone");
        String message = jbody.optString("message");
        int status = 1; // 1 là new, 2 là close

        int addContact = ContactModel.INSTANCE.addContact(name, email, phone, message, status);
        if (addContact >= 0) {
            result.setErrorCode(0);
            result.setMessage("Bạn đã gửi thành công!");
        } else {
            result.setErrorCode(-1);
            result.setMessage("Bạn gửi thất bại!");
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
