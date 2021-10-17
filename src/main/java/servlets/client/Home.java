package servlets.client;

import common.Configuration;
import entity.category_product.CategoryProduct;
import entity.item.CartItem;
import entity.news.News;
import entity.product.FilterProduct;
import entity.product.Product;
import entity.setting.Setting;
import entity.slides.Slides;
import entity.user_register.UserRegister;
import helper.SessionHelper;
import java.io.IOException;
import java.util.ArrayList;
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
import servlets.client.include.IncludeData;
import templater.PageGenerator;

public class Home extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Configuration.APP_DOMAIN);
        pageVariables.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);

        UserRegister userRegister = SessionHelper.INSTANCE.getUserSession(request);
        pageVariables.put("user", userRegister);

        List<CartItem> listResult = SessionHelper.INSTANCE.getCartItem(request);
        List<Product> listProductItem = new ArrayList<>();
        int payTotal = 0;
        for (CartItem cartItem : listResult) {
            int product_id = cartItem.getId_product();
            Product productItem = ProductModel.INSTANCE.getProductByID(product_id);
            productItem.setQuantity_buy(cartItem.getQuantity());
            payTotal = payTotal + productItem.getQuantity_buy() * productItem.getPrice_sale();
            listProductItem.add(productItem);
        }
        int numberItem = listProductItem.size();
        pageVariables.put("number_item", numberItem);
        pageVariables.put("list_product_item", listProductItem);
        pageVariables.put("pay_total", payTotal);

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

        List<Setting> listSettingByKey = SettingModel.INSTANCE.getListSettingByKey("'Điện thoại', 'Email'");
        pageVariables.put("list_setting_by_key", listSettingByKey);

        Setting settingFacebook = SettingModel.INSTANCE.getSettingByKey("Facebook");
        pageVariables.put("setting_facebook", settingFacebook);

        Setting settingTwitter = SettingModel.INSTANCE.getSettingByKey("Twitter");
        pageVariables.put("setting_twitter", settingTwitter);

        Setting settingGoogle = SettingModel.INSTANCE.getSettingByKey("Google");
        pageVariables.put("setting_google", settingGoogle);

        Setting settingYoutube = SettingModel.INSTANCE.getSettingByKey("Youtube");
        pageVariables.put("setting_youtube", settingYoutube);

        Setting settingIns = SettingModel.INSTANCE.getSettingByKey("Instagram");
        pageVariables.put("setting_instagram", settingIns);

        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));

        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", IncludeData.INSTANCE.buildFooterData()));
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/index.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
