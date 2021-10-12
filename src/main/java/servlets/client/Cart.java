package servlets.client;

import common.Config;
import entity.category_product.CategoryProduct;
import entity.item.CartItem;
import entity.product.Product;
import entity.setting.Setting;
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
import model.ProductModel;
import model.SettingModel;
import org.apache.commons.lang3.math.NumberUtils;
import templater.PageGenerator;

public class Cart extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_CLIENT_DOMAIN);
        pageVariables.put("shipping_fee", Config.SHIPPING_FEE);

        String action = request.getParameter("action");
        if ("remove-cart-item".equals(action)) {
            int id_product = NumberUtils.toInt(request.getParameter("product_id"));//
            SessionHelper.INSTANCE.removeSession(request, id_product);
        } else if ("add-to-card".equals(action)) {
            int id_product = NumberUtils.toInt(request.getParameter("product_id"));
            int quantity = NumberUtils.toInt(request.getParameter("quantity"));
            SessionHelper.INSTANCE.addToCart(request, quantity, id_product);
        }
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
        pageVariables.put("list_product_item", listProductItem);
        pageVariables.put("pay_total", payTotal);

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

        //HEADER MENU
        Map<String, Object> pageVariablesHeaderMenu = new HashMap<>();
        pageVariablesHeaderMenu.put("app_domain", Config.APP_DOMAIN);
        pageVariablesHeaderMenu.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        pageVariablesHeaderMenu.put("number_item", numberItem);
        pageVariablesHeaderMenu.put("list_product_item", listProductItem);
        pageVariablesHeaderMenu.put("pay_total", payTotal);

        List<CategoryProduct> allCategory = CategoryModel.INSTANCE.getAllCategory();
        pageVariablesHeaderMenu.put("list_category", allCategory);

        List<Setting> listSettingByKey = SettingModel.INSTANCE.getListSettingByKey("'Điện thoại', 'Email'");
        pageVariablesHeaderMenu.put("list_setting_by_key", listSettingByKey);

        Setting settingFacebook = SettingModel.INSTANCE.getSettingByKey("Facebook");
        pageVariablesHeaderMenu.put("setting_facebook", settingFacebook);

        Setting settingTwitter = SettingModel.INSTANCE.getSettingByKey("Twitter");
        pageVariablesHeaderMenu.put("setting_twitter", settingTwitter);

        Setting settingGoogle = SettingModel.INSTANCE.getSettingByKey("Google");
        pageVariablesHeaderMenu.put("setting_google", settingGoogle);

        Setting settingYoutube = SettingModel.INSTANCE.getSettingByKey("Youtube");
        pageVariablesHeaderMenu.put("setting_youtube", settingYoutube);

        Setting settingIns = SettingModel.INSTANCE.getSettingByKey("Instagram");
        pageVariablesHeaderMenu.put("setting_instagram", settingIns);

        pageVariables.put("header_menu", PageGenerator.instance().getPage("client/include/header_menu.html", pageVariablesHeaderMenu));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/cart.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
