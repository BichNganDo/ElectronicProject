package servlets.client;

import com.google.gson.Gson;
import common.APIResult;
import common.Config;
import entity.category_product.CategoryProduct;
import entity.item.CartItem;
import entity.news.News;
import entity.product.Product;
import entity.setting.Setting;
import entity.user_register.UserRegister;
import helper.HttpHelper;
import helper.ServletUtil;
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
import model.EmailRegisterModel;
import model.NewsModel;
import model.ProductModel;
import model.RegisterModel;
import model.SettingModel;
import org.json.JSONObject;
import templater.PageGenerator;

public class Register extends HttpServlet {

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

        //HEADER MENU
        Map<String, Object> pageVariablesHeaderMenu = new HashMap<>();
        pageVariablesHeaderMenu.put("app_domain", Config.APP_DOMAIN);
        pageVariablesHeaderMenu.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        UserRegister userRegister = SessionHelper.INSTANCE.getUserSession(request);
        pageVariablesHeaderMenu.put("user", userRegister);

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
        response.getWriter().println(PageGenerator.instance().getPage("client/register.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String body = HttpHelper.getBodyData(request);
        JSONObject jbody = new JSONObject(body);

        String name = jbody.optString("name");
        String email = jbody.optString("email");
        String password = jbody.optString("password");
        String rePassword = jbody.optString("rePassword");
        int addUserRegister = RegisterModel.INSTANCE.addUserRegister(name, email, password, rePassword);
        if (addUserRegister >= 0) {
            result.setErrorCode(0);
            result.setMessage("Đăng ký thành công!");
        } else {
            result.setErrorCode(-4);
            result.setMessage("Email đăng ký đã tồn tại hoặc mật khẩu chưa đúng");
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
