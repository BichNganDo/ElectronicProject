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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import templater.PageGenerator;

public class CateProduct extends HttpServlet {

    private int DEFAULT_ITEM_PER_PAGE = 4;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        List<CategoryProduct> allCategory = CategoryModel.INSTANCE.getAllCategory();
        pageVariables.put("list_category", allCategory);

        int itemPerPage = NumberUtils.toInt(request.getParameter("limit"), DEFAULT_ITEM_PER_PAGE);
        if (itemPerPage < 0 && itemPerPage > 50) {
            itemPerPage = DEFAULT_ITEM_PER_PAGE;
        }
        pageVariables.put("item_per_page", itemPerPage);

        int id = NumberUtils.toInt(request.getParameter("id"));
        pageVariables.put("id_cate", id);

        String sortBy = StringUtils.defaultIfEmpty(request.getParameter("sort"), "");
        pageVariables.put("sort_by", sortBy);
        FilterProduct totalProduct = new FilterProduct();
        totalProduct.setSearchCate(id);
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
        allProduct.setSearchCate(id);
        if ("hot".equals(sortBy)) {
            allProduct.setSearchProperty(1);
        } else if ("promo".equals(sortBy)) {
            allProduct.setSearchProperty(4);
        } else if ("priceLowtoHight".equals(sortBy)) {
            allProduct.setSortByPrice(2);
        } else if ("priceHightToLow".equals(sortBy)) {
            allProduct.setSortByPrice(1);
        }
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
