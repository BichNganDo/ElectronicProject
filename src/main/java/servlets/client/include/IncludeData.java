package servlets.client.include;

import common.Configuration;
import entity.category_product.CategoryProduct;
import entity.item.CartItem;
import entity.product.Product;
import entity.setting.Setting;
import entity.user_register.UserRegister;
import helper.SessionHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.CategoryModel;
import model.ProductModel;
import model.SettingModel;

public class IncludeData {

    public static IncludeData INSTANCE = new IncludeData();

    public Map<String, Object> buildFooterData() {
        Map<String, Object> pageVariablesFooter = new HashMap<>();
        pageVariablesFooter.put("app_domain", Configuration.APP_DOMAIN);
        pageVariablesFooter.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);

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

        return pageVariablesFooter;
    }

    public Map<String, Object> buildHeaderMenuData(HttpServletRequest request) {
        Map<String, Object> pageVariablesHeaderMenu = new HashMap<>();
        pageVariablesHeaderMenu.put("app_domain", Configuration.APP_DOMAIN);
        pageVariablesHeaderMenu.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);

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

        return pageVariablesHeaderMenu;
    }
}
