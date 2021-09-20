package servlets.client;

import common.Config;
import entity.category_product.CategoryProduct;
import entity.product.Product;
import entity.slides.Slides;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryProductModel;
import model.ProductModel;
import model.SlidesModel;
import templater.PageGenerator;

public class ManageIndexServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

//        List<CategoryProduct> listCategoryStatusShow = CategoryProductModel.INSTANCE.getSliceCategory(0, 100, "", 1);
//        pageVariables.put("list_category", listCategoryStatusShow);
        List<Slides> listSlideStatusShow = SlidesModel.INSTANCE.getSliceSlides(0, 50, "", 1);
        pageVariables.put("list_slides", listSlideStatusShow);

        List<Product> listProductPromo = ProductModel.INSTANCE.getSliceProduct(0, 12, "", 0, 0, 4);
        pageVariables.put("list_products", listProductPromo);

        List<Product> listProductHot = ProductModel.INSTANCE.getSliceProduct(0, 12, "", 0, 0, 1);
        pageVariables.put("list_products_hot", listProductHot);

        List<Product> listProductNew = ProductModel.INSTANCE.getSliceProduct(0, 12, "", 0, 0, 2);
        pageVariables.put("list_products_new", listProductNew);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/index.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
