package servlets.client;

import common.Config;
import entity.category_product.CategoryProduct;
import entity.item.CartItem;
import entity.product.FilterProduct;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import templater.PageGenerator;

public class SearchProduct extends HttpServlet {

    private int DEFAULT_ITEM_PER_PAGE = 12;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        int itemPerPage = NumberUtils.toInt(request.getParameter("limit"), DEFAULT_ITEM_PER_PAGE);
        if (itemPerPage < 0 && itemPerPage > 50) {
            itemPerPage = DEFAULT_ITEM_PER_PAGE;
        }
        pageVariables.put("item_per_page", itemPerPage);

        int id = NumberUtils.toInt(request.getParameter("id"));
        pageVariables.put("id_cate", id);

        String searchQuery = StringUtils.defaultIfEmpty(request.getParameter("query"), "");
        pageVariables.put("query", searchQuery);
        String sortBy = StringUtils.defaultIfEmpty(request.getParameter("sort"), "");
        pageVariables.put("sort_by", sortBy);
        FilterProduct totalProduct = new FilterProduct();
        totalProduct.setSearchQuery(searchQuery);
        if ("hot".equals(sortBy)) {
            totalProduct.setSearchProperty(1);
        } else if ("promo".equals(sortBy)) {
            totalProduct.setSearchProperty(4);
        }
        int totalProductByCate = ProductModel.INSTANCE.getTotalProduct(totalProduct);
        pageVariables.put("total_product_by_cate", totalProductByCate);

        int totalPage = (int) Math.ceil((double) totalProductByCate / itemPerPage);
        pageVariables.put("total_page", totalPage);

        int page = NumberUtils.toInt(request.getParameter("page"), 1);
        if (page > totalPage) {
            page = totalPage;
        }
        if (page <= 0) {
            page = 1;
        }
        pageVariables.put("current_page", page);

        int offset = (page - 1) * itemPerPage;
        pageVariables.put("offset", offset);
        int showingLast = offset + itemPerPage;
        if (showingLast > totalProductByCate) {
            showingLast = totalProductByCate;
        }
        pageVariables.put("showing_last", showingLast);

        FilterProduct allProduct = new FilterProduct();
        allProduct.setOffset(offset);
        allProduct.setLimit(itemPerPage);
        allProduct.setSearchQuery(searchQuery);
        if ("hot".equals(sortBy)) {
            allProduct.setSearchProperty(1);
        } else if ("promo".equals(sortBy)) {
            allProduct.setSearchProperty(4);
        } else if ("priceLowtoHight".equals(sortBy)) {
            allProduct.setSortByPrice(2);
        } else if ("priceHightToLow".equals(sortBy)) {
            allProduct.setSortByPrice(1);
        }
        List<Product> listProductBySearchQuery = ProductModel.INSTANCE.getSliceProduct(allProduct);
        pageVariables.put("list_product_by_search_query", listProductBySearchQuery);

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
        response.getWriter().println(PageGenerator.instance().getPage("client/search.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
