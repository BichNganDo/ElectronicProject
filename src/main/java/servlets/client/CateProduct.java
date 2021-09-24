package servlets.client;

import common.Config;
import entity.category_product.CategoryProduct;
import entity.product.FilterProduct;
import entity.product.Product;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryModel;
import model.ProductModel;
import org.apache.commons.lang3.math.NumberUtils;
import templater.PageGenerator;

public class CateProduct extends HttpServlet {

    private int ITEM_PER_PAGE = 4;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        List<CategoryProduct> allCategory = CategoryModel.INSTANCE.getAllCategory();
        pageVariables.put("list_category", allCategory);

        int id = NumberUtils.toInt(request.getParameter("id"));
        pageVariables.put("id_cate", id);

        FilterProduct totalProduct = new FilterProduct();
        totalProduct.setSearchCate(id);
        int totalProductByCate = ProductModel.INSTANCE.getTotalProduct(totalProduct);
        pageVariables.put("total_product_by_cate", totalProductByCate);

        int totalPage = (int) Math.ceil((double) totalProductByCate / ITEM_PER_PAGE);
        pageVariables.put("total_page", totalPage);

        int page = NumberUtils.toInt(request.getParameter("page"), 1);
        if (page > totalPage) {
            page = totalPage;
        }
        int offset = (page - 1) * ITEM_PER_PAGE;
        pageVariables.put("current_page", page);

        FilterProduct allProduct = new FilterProduct();
        allProduct.setOffset(offset);
        allProduct.setLimit(ITEM_PER_PAGE);
        allProduct.setSearchCate(id);
        List<Product> listProductByCate = ProductModel.INSTANCE.getSliceProduct(allProduct);
        pageVariables.put("list_product_by_cate", listProductByCate);

        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Config.STATIC_CLIENT_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));
        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", pageVariablesHeader));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/shop-grid.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
