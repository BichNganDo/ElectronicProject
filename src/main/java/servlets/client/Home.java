package servlets.client;

import common.Config;
import entity.category_product.CategoryProduct;
import entity.news.News;
import entity.product.FilterProduct;
import entity.product.Product;
import entity.setting.Setting;
import entity.slides.Slides;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryModel;
import model.NewsModel;
import model.ProductModel;
import model.SettingModel;
import model.SlidesModel;
import templater.PageGenerator;

public class Home extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        List<CategoryProduct> allCategory = CategoryModel.INSTANCE.getAllCategory();
        pageVariables.put("list_category", allCategory);

        List<News> listNews = NewsModel.INSTANCE.getSliceNews(0, 5, "", 0);
        pageVariables.put("list_news", listNews);

        List<CategoryProduct> listCategoryHot = CategoryModel.INSTANCE.getSliceCategory(0, 3, "", 1, 1);
        pageVariables.put("list_category_hot", listCategoryHot);
        Map<Integer, List<Product>> listProductByCategory = ProductModel.INSTANCE.multiGetProductByCategory(listCategoryHot);
        pageVariables.put("list_product_by_cate", listProductByCategory);

        List<Slides> listSlideStatusShow = SlidesModel.INSTANCE.getSliceSlides(0, 50, "", 1);
        pageVariables.put("list_slides", listSlideStatusShow);

        FilterProduct filterProductPromo = new FilterProduct();
        filterProductPromo.setOffset(0);
        filterProductPromo.setLimit(12);
        filterProductPromo.setSearchQuery("");
        filterProductPromo.setSearchCate(0);
        filterProductPromo.setSearchSupplier(0);
        filterProductPromo.setSearchProperty(4);
        filterProductPromo.setOrderView(0);
        List<Product> listProductPromo = ProductModel.INSTANCE.getSliceProduct(filterProductPromo);
        pageVariables.put("list_products", listProductPromo);

        FilterProduct filterProductHot = new FilterProduct();
        filterProductHot.setOffset(0);
        filterProductHot.setLimit(12);
        filterProductHot.setSearchQuery("");
        filterProductHot.setSearchCate(0);
        filterProductHot.setSearchSupplier(0);
        filterProductHot.setSearchProperty(1);
        filterProductHot.setOrderView(0);
        List<Product> listProductHot = ProductModel.INSTANCE.getSliceProduct(filterProductHot);
        pageVariables.put("list_products_hot", listProductHot);

        FilterProduct filterProductNew = new FilterProduct();
        filterProductNew.setOffset(0);
        filterProductNew.setLimit(12);
        filterProductNew.setSearchQuery("");
        filterProductNew.setSearchCate(0);
        filterProductNew.setSearchSupplier(0);
        filterProductNew.setSearchProperty(2);
        filterProductNew.setOrderView(0);
        List<Product> listProductNew = ProductModel.INSTANCE.getSliceProduct(filterProductNew);
        pageVariables.put("list_products_new", listProductNew);

        FilterProduct filterProductView = new FilterProduct();
        filterProductView.setOffset(0);
        filterProductView.setLimit(12);
        filterProductView.setSearchQuery("");
        filterProductView.setSearchCate(0);
        filterProductView.setSearchSupplier(0);
        filterProductView.setSearchProperty(0);
        filterProductView.setOrderView(1);
        List<Product> listProductView = ProductModel.INSTANCE.getSliceProduct(filterProductView);
        pageVariables.put("list_products_view", listProductView);

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
        response.getWriter().println(PageGenerator.instance().getPage("client/index.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
