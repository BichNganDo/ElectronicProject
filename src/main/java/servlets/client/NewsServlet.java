package servlets.client;

import common.Config;
import entity.category_news.CategoryNews;
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
import model.CategoryNewsModel;
import model.NewsModel;
import model.SettingModel;
import org.apache.commons.lang3.math.NumberUtils;
import templater.PageGenerator;

public class NewsServlet extends HttpServlet {

    private int DEFAULT_ITEM_PER_PAGE = 9;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        int totalNews = NewsModel.INSTANCE.getTotalNews("", 0);
        int totalPage = (int) Math.ceil((double) totalNews / DEFAULT_ITEM_PER_PAGE);
        pageVariables.put("total_page", totalPage);

        int page = NumberUtils.toInt(request.getParameter("page"), 1);
        if (page > totalPage) {
            page = totalPage;
        }
        if (page <= 0) {
            page = 1;
        }
        pageVariables.put("current_page", page);

        int offset = (page - 1) * DEFAULT_ITEM_PER_PAGE;
        pageVariables.put("offset", offset);

        List<CategoryNews> listCateNews = CategoryNewsModel.INSTANCE.getSliceNews(0, 20, "", 1);
        pageVariables.put("list_cate_news", listCateNews);

        int idCate = NumberUtils.toInt(request.getParameter("id"));

        List<News> listNews = NewsModel.INSTANCE.getSliceNews(offset, DEFAULT_ITEM_PER_PAGE, "", idCate);
        pageVariables.put("list_news", listNews);

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
        response.getWriter().println(PageGenerator.instance().getPage("client/news.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
