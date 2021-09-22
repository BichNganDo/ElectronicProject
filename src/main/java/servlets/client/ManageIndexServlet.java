package servlets.client;

import common.Config;
import entity.category_product.CategoryProduct;
import entity.news.News;
import entity.product.FilterProduct;
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
import model.NewsModel;
import model.ProductModel;
import model.SlidesModel;
import templater.PageGenerator;

public class ManageIndexServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_CLIENT_DOMAIN);

        List<News> listNews = NewsModel.INSTANCE.getSliceNews(0, 5, "", 0);
        pageVariables.put("list_news", listNews);

        List<CategoryProduct> listCategoryHot = CategoryProductModel.INSTANCE.getSliceCategory(0, 3, "", 1, 1);
        pageVariables.put("list_category_hot", listCategoryHot);
        Map<Integer, List<Product>> listProductByCategory = ProductModel.INSTANCE.multiGetProductByCategory(listCategoryHot);
        pageVariables.put("list_product_by_cate", listProductByCategory);

        List<Slides> listSlideStatusShow = SlidesModel.INSTANCE.getSliceSlides(0, 50, "", 1);
        pageVariables.put("list_slides", listSlideStatusShow);

        FilterProduct filterProductPromo = new FilterProduct();
        filterProductPromo.setOffset(0);
        filterProductPromo.setLimit(12);
        filterProductPromo.setSearchQuery("");
        filterProductPromo.setSearchCate(0);
        filterProductPromo.setSearchSupplier(0);
        filterProductPromo.setSearchProperty(4);
        filterProductPromo.setOrderView(0);
        List<Product> listProductPromo = ProductModel.INSTANCE.getSliceProduct(filterProductPromo);
        pageVariables.put("list_products", listProductPromo);

        FilterProduct filterProductHot = new FilterProduct();
        filterProductHot.setOffset(0);
        filterProductHot.setLimit(12);
        filterProductHot.setSearchQuery("");
        filterProductHot.setSearchCate(0);
        filterProductHot.setSearchSupplier(0);
        filterProductHot.setSearchProperty(1);
        filterProductHot.setOrderView(0);
        List<Product> listProductHot = ProductModel.INSTANCE.getSliceProduct(filterProductHot);
        pageVariables.put("list_products_hot", listProductHot);

        FilterProduct filterProductNew = new FilterProduct();
        filterProductNew.setOffset(0);
        filterProductNew.setLimit(12);
        filterProductNew.setSearchQuery("");
        filterProductNew.setSearchCate(0);
        filterProductNew.setSearchSupplier(0);
        filterProductNew.setSearchProperty(2);
        filterProductNew.setOrderView(0);
        List<Product> listProductNew = ProductModel.INSTANCE.getSliceProduct(filterProductNew);
        pageVariables.put("list_products_new", listProductNew);

        FilterProduct filterProductView = new FilterProduct();
        filterProductView.setOffset(0);
        filterProductView.setLimit(12);
        filterProductView.setSearchQuery("");
        filterProductView.setSearchCate(0);
        filterProductView.setSearchSupplier(0);
        filterProductView.setSearchProperty(0);
        filterProductView.setOrderView(1);
        List<Product> listProductView = ProductModel.INSTANCE.getSliceProduct(filterProductView);
        pageVariables.put("list_products_view", listProductView);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/index.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
