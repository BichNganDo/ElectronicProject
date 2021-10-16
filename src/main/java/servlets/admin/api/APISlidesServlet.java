package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.slides.ListSlides;
import entity.slides.Slides;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.SlidesModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APISlidesServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getslides": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchStatus = NumberUtils.toInt(request.getParameter("search_status"));

                int offset = (pageIndex - 1) * limit;
                List<Slides> sliceSlides = SlidesModel.INSTANCE.getSliceSlides(offset, limit, searchQuery, searchStatus);
                int totalSlides = SlidesModel.INSTANCE.getTotalSlides(searchQuery, searchStatus);

                ListSlides listSlides = new ListSlides();
                listSlides.setTotal(totalSlides);
                listSlides.setListSlides(sliceSlides);
                listSlides.setItemPerPage(10);

                if (sliceSlides.size() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listSlides);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getSlidesById": {
                int idSlides = Integer.parseInt(request.getParameter("id_slides"));
                Slides slidesByID = SlidesModel.INSTANCE.getSlidesByID(idSlides);

                if (slidesByID.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(slidesByID);
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
                String name = request.getParameter("name");
                String image = request.getParameter("image");
                String link = request.getParameter("link");
                int orders = NumberUtils.toInt(request.getParameter("orders"));
                int status = NumberUtils.toInt(request.getParameter("status"));

                int addSlides = SlidesModel.INSTANCE.addSlides(name, image, link, orders, status);
                if (addSlides >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm slides thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm slides thất bại!");
                }
                break;
            }

            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String image = request.getParameter("image");
                String link = request.getParameter("link");
                int orders = NumberUtils.toInt(request.getParameter("orders"));
                int status = NumberUtils.toInt(request.getParameter("status"));

                Slides slidesByID = SlidesModel.INSTANCE.getSlidesByID(id);
                if (slidesByID.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editSlides = SlidesModel.INSTANCE.editSlides(id, name, image, link, orders, status);
                if (editSlides >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa slides thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa slides thất bại!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteSlides = SlidesModel.INSTANCE.deleteSlides(id);
                if (deleteSlides >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa slides thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa slides thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
