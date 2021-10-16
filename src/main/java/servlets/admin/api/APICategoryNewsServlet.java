package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.category_news.CategoryNews;
import entity.category_news.ListCategoryNews;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryNewsModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APICategoryNewsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getnews": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchStatus = NumberUtils.toInt(request.getParameter("search_status"));

                int offset = (pageIndex - 1) * limit;
                List<CategoryNews> listCateNews = CategoryNewsModel.INSTANCE.getSliceNews(offset, limit, searchQuery, searchStatus);
                int totalNews = CategoryNewsModel.INSTANCE.getTotalNews(searchQuery, searchStatus);

                ListCategoryNews listCategoryNews = new ListCategoryNews();
                listCategoryNews.setTotal(totalNews);
                listCategoryNews.setListCategoryNews(listCateNews);
                listCategoryNews.setItemPerPage(10);

                if (listCateNews.size() >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listCategoryNews);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getNewsById": {
                int idNews = NumberUtils.toInt(request.getParameter("id_news"));

                CategoryNews newsByID = CategoryNewsModel.INSTANCE.getNewsByID(idNews);

                if (newsByID.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(newsByID);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
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
                String name = request.getParameter("name");
                int orders = NumberUtils.toInt(request.getParameter("orders"));
                int status = NumberUtils.toInt(request.getParameter("status"));

                int addNews = CategoryNewsModel.INSTANCE.addCategoryNews(name, orders, status);

                if (addNews >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm news thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm news thất bại!");
                }
                break;
            }
            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                String name = request.getParameter("name");
                int orders = NumberUtils.toInt(request.getParameter("orders"));
                int status = NumberUtils.toInt(request.getParameter("status"));

                CategoryNews newsByID = CategoryNewsModel.INSTANCE.getNewsByID(id);
                if (newsByID.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editCategoryNews = CategoryNewsModel.INSTANCE.editCategoryNews(id, name, orders, status);

                if (editCategoryNews >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa category news thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa category news thất bại!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteCategoryNews = CategoryNewsModel.INSTANCE.deleteCategoryNews(id);
                if (deleteCategoryNews >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa category news thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa category news thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }
}
