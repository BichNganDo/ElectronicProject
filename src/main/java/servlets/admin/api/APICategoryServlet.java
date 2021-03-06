package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.category_product.CategoryProduct;
import entity.category_product.ListCategoryProduct;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APICategoryServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getcate": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchStatus = NumberUtils.toInt(request.getParameter("search_status"));
                int hot = NumberUtils.toInt(request.getParameter("hot"));
                int offset = (pageIndex - 1) * limit;
                List<CategoryProduct> listCategory = CategoryModel.INSTANCE.getSliceCategory(offset, limit, searchQuery, searchStatus, hot);
                int totalCategory = CategoryModel.INSTANCE.getTotalCategory(searchQuery, searchStatus);

                ListCategoryProduct listCategoryProduct = new ListCategoryProduct();
                listCategoryProduct.setTotal(totalCategory);
                listCategoryProduct.setListCategory(listCategory);
                listCategoryProduct.setItemPerPage(10);

                if (listCategory.size() >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listCategoryProduct);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getCateById": {
                int idCate = NumberUtils.toInt(request.getParameter("id_cate"));

                CategoryProduct cateById = CategoryModel.INSTANCE.getCategoryByID(idCate);

                if (cateById.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(cateById);
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
                int id_parent = NumberUtils.toInt(request.getParameter("id_parent"));
                int orders = NumberUtils.toInt(request.getParameter("orders"));
                int status = NumberUtils.toInt(request.getParameter("status"));
                int hot = NumberUtils.toInt(request.getParameter("hot"));

                int addCategory = CategoryModel.INSTANCE.addCategory(name, id_parent, orders, status, hot);

                if (addCategory >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Th??m category th??nh c??ng!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Th??m category th???t b???i!");
                }
                break;
            }
            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                String name = request.getParameter("name");
                int id_parent = NumberUtils.toInt(request.getParameter("id_parent"));
                int orders = NumberUtils.toInt(request.getParameter("orders"));
                int status = NumberUtils.toInt(request.getParameter("status"));
                int hot = NumberUtils.toInt(request.getParameter("hot"));

                CategoryProduct categoryByID = CategoryModel.INSTANCE.getCategoryByID(id);
                if (categoryByID.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Th???t b???i!");
                    return;
                }

                int editCategory = CategoryModel.INSTANCE.editCategory(id, name, id_parent, orders, status, hot);

                if (editCategory >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("S???a Category th??nh c??ng!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("S???a Category th???t b???i!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteCategory = CategoryModel.INSTANCE.deleteCategory(id);
                if (deleteCategory >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("X??a Category th??nh c??ng!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("X??a Category th???t b???i!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }

}
