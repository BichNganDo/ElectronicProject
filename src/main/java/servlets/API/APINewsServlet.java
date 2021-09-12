package servlets.API;

import com.google.gson.Gson;
import common.APIResult;
import entity.news.ListNews;
import entity.news.News;
import entity.product.ListProduct;
import entity.product.Product;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.NewsModel;
import model.ProductModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APINewsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getnews": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchCategoryNews = NumberUtils.toInt(request.getParameter("search_categoryNews"));

                int offset = (pageIndex - 1) * limit;
                List<News> sliceNews = NewsModel.INSTANCE.getSliceNews(offset, limit, searchQuery, searchCategoryNews);
                int totalNews = NewsModel.INSTANCE.getTotalNews(searchQuery, searchCategoryNews);

                ListNews listNews = new ListNews();
                listNews.setTotal(totalNews);
                listNews.setListNews(sliceNews);
                listNews.setItemPerPage(10);

                if (sliceNews.size() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listNews);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getNewsById": {
                int idNews = Integer.parseInt(request.getParameter("id_news"));
                News newsByID = NewsModel.INSTANCE.getNewsByID(idNews);

                if (newsByID.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(newsByID);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Error");
                }
                break;
            }

            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "add": {
                int id_categoryNews = NumberUtils.toInt(request.getParameter("categoryNews"));
                String title = request.getParameter("title");
                String info = request.getParameter("info");
                String content = request.getParameter("content");
                String image = request.getParameter("image");

                int addNews = NewsModel.INSTANCE.addNews(id_categoryNews, title, info, content, image, content, content);
                if (addNews >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm news thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm news thất bại!");
                }
                break;
            }

//            case "edit": {
//                int id = NumberUtils.toInt(request.getParameter("id"));
//                int id_cate = NumberUtils.toInt(request.getParameter("category"));
//                int id_supplier = NumberUtils.toInt(request.getParameter("supplier"));
//                String name = request.getParameter("name");
//                int price = NumberUtils.toInt(request.getParameter("price"));
//                int price_sale = NumberUtils.toInt(request.getParameter("price_sale"));
//                int quantity = NumberUtils.toInt(request.getParameter("quantity"));
//                String image_url = request.getParameter("image_url");
//                String content = request.getParameter("content");
//                String warranty = request.getParameter("warranty");
//                String hot = request.getParameter("hot");
//
//                Product productByID = ProductModel.INSTANCE.getProductByID(id);
//                if (productByID.getId() == 0) {
//                    result.setErrorCode(-1);
//                    result.setMessage("Thất bại!");
//                    return;
//                }
//
//                int editProduct = ProductModel.INSTANCE.editProduct(id, id_cate, id_supplier, name, price, price_sale, quantity, image_url, content, warranty, hot);
//                if (editProduct >= 0) {
//                    result.setErrorCode(0);
//                    result.setMessage("Sửa product thành công!");
//                } else {
//                    result.setErrorCode(-1);
//                    result.setMessage("Sửa product thất bại!");
//                }
//                break;
//            }
//
//            case "delete": {
//                int id = NumberUtils.toInt(request.getParameter("id"));
//                int deleteProduct = ProductModel.INSTANCE.deleteProduct(id);
//                if (deleteProduct >= 0) {
//                    result.setErrorCode(0);
//                    result.setMessage("Xóa product thành công!");
//                } else {
//                    result.setErrorCode(-2);
//                    result.setMessage("Xóa product thất bại!");
//                }
//                break;
//            }
            default:
                throw new AssertionError();
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
