package servlets.client;

import common.Config;
import entity.news.News;
import entity.setting.Setting;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.NewsModel;
import model.SettingModel;
import templater.PageGenerator;

public class AboutBlog extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        News aboutBlog = NewsModel.INSTANCE.getNewsByType("about");
        pageVariables.put("about_blog", aboutBlog);

        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Config.STATIC_CLIENT_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));

        Map<String, Object> pageVariablesFooter = new HashMap<>();
        pageVariablesFooter.put("app_domain", Config.APP_DOMAIN);
        pageVariablesFooter.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        List<Setting> listSettingByKeys = SettingModel.INSTANCE.getListSettingByKey("'Địa chỉ', 'Điện thoại', 'Email'");
        pageVariablesFooter.put("list_setting_by_keys", listSettingByKeys);

        Setting settingFb = SettingModel.INSTANCE.getSettingByKey("Facebook");
        pageVariablesFooter.put("setting_facebook", settingFb);

        Setting settingTw = SettingModel.INSTANCE.getSettingByKey("Twitter");
        pageVariablesFooter.put("setting_twitter", settingTw);

        Setting settingGg = SettingModel.INSTANCE.getSettingByKey("Google");
        pageVariablesFooter.put("setting_google", settingGg);

        Setting settingYb = SettingModel.INSTANCE.getSettingByKey("Youtube");
        pageVariablesFooter.put("setting_youtube", settingYb);

        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", pageVariablesFooter));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/about-blog.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
