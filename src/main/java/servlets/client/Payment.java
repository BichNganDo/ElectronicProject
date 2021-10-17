package servlets.client;

import common.Configuration;
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
import servlets.client.include.IncludeData;
import templater.PageGenerator;

public class Payment extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Configuration.APP_DOMAIN);
        pageVariables.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);

        List<CartItem> listResult = SessionHelper.INSTANCE.getCartItem(request);
        if (listResult.size() > 0) {
            pageVariables.put("shipping_fee", Configuration.SHIPPING_FEE);
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
        pageVariablesHeader.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));

        //FOOTER
        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", IncludeData.INSTANCE.buildFooterData()));

        //HEADER MENU
        pageVariables.put("header_menu", PageGenerator.instance().getPage("client/include/header_menu.html", IncludeData.INSTANCE.buildHeaderMenuData(request)));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/payment.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
