package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.setting.ListSetting;
import entity.setting.Setting;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.SettingModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APISetiingServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getsetting": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");

                int offset = (pageIndex - 1) * limit;
                List<Setting> sliceSetting = SettingModel.INSTANCE.getSliceSetting(offset, limit, searchQuery);
                int totalSetting = SettingModel.INSTANCE.getTotalSetting(searchQuery);

                ListSetting listSetting = new ListSetting();
                listSetting.setTotal(totalSetting);
                listSetting.setListSetting(sliceSetting);
                listSetting.setItemPerPage(10);

                if (sliceSetting.size() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listSetting);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }

            case "getSettingById": {
                int idSetting = Integer.parseInt(request.getParameter("id_setting"));
                Setting settingById = SettingModel.INSTANCE.getSettingById(idSetting);

                if (settingById.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(settingById);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Error");
                }
                break;
            }

            case "getSettingByKey": {
                String searchQuery = request.getParameter("search_query");
                Setting settingById = SettingModel.INSTANCE.getSettingByKey(searchQuery);

                if (settingById.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(settingById);
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
                String key = request.getParameter("key");
                String value = request.getParameter("value");

                int addSetting = SettingModel.INSTANCE.addSetting(key, value);
                if (addSetting >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm setting thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm setting thất bại!");
                }
                break;
            }

            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                String key = request.getParameter("key");
                String value = request.getParameter("value");
                Setting setting = SettingModel.INSTANCE.getSettingById(id);

                if (setting.getId() <= 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editSetting = SettingModel.INSTANCE.editSetting(id, key, value);
                if (editSetting >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa setting thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa setting thất bại!");
                }
                break;
            }
            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteSetting = SettingModel.INSTANCE.deleteSetting(id);
                if (deleteSetting >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa setting thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa setting thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
