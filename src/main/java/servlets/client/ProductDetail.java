package servlets.client;

import common.Configuration;
import entity.product.Product;
import helper.SessionHelper;
import java.io.IOException;
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

public class ProductDetail extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Configuration.APP_DOMAIN);
        pageVariables.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);

        int id = NumberUtils.toInt(request.getParameter("id"));
        pageVariables.put("id_product", id);
        Product productById = ProductModel.INSTANCE.getProductByID(id);
        pageVariables.put("product_by_id", productById);

        SessionHelper.INSTANCE.addRecentIdProduct(request, id);
        List<Integer> listRecentIdProduct = SessionHelper.INSTANCE.getRecentIdProduct(request);
        listRecentIdProduct.remove((Object) id);
        List<Product> listRecentProduct = ProductModel.INSTANCE.multiGetProduct(listRecentIdProduct);
        pageVariables.put("list_recent_product", listRecentProduct);

        //HEADER
        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Configuration.STATIC_CLIENT_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));

        //FOOTER
        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", IncludeData.INSTANCE.buildFooterData()));

        //HEADER MENU
        pageVariables.put("header_menu", PageGenerator.instance().getPage("client/include/header_menu.html", IncludeData.INSTANCE.buildHeaderMenuData(request)));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/product-details.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
