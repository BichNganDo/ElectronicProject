package servlets.client;

import common.Config;
import entity.item.CartItem;
import entity.product.Product;
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
import model.ProductModel;
import org.apache.commons.lang3.math.NumberUtils;
import servlets.client.include.IncludeData;
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
        if (listResult.size() > 0) {
            pageVariables.put("shipping_fee", Config.SHIPPING_FEE);
        } else {
            pageVariables.put("shipping_fee", 0);
        }
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

        //HEADER
        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Config.STATIC_CLIENT_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));

        //FOOTER
        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", IncludeData.INSTANCE.buildFooterData()));

        //HEADER MENU
        pageVariables.put("header_menu", PageGenerator.instance().getPage("client/include/header_menu.html", IncludeData.INSTANCE.buildHeaderMenuData(request)));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/cart.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
