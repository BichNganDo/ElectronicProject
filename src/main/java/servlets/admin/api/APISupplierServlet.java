package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.supplier.ListSupplier;
import entity.supplier.Supplier;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.SupplierModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APISupplierServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getsupplier": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");

                int offset = (pageIndex - 1) * limit;
                List<Supplier> sliceSupplier = SupplierModel.INSTANCE.getSliceSupplier(offset, limit, searchQuery);
                int totalSupplier = SupplierModel.INSTANCE.getTotalSupplier(searchQuery);

                ListSupplier listSupplier = new ListSupplier();
                listSupplier.setTotal(totalSupplier);
                listSupplier.setListSupplier(sliceSupplier);
                listSupplier.setItemPerPage(10);

                if (sliceSupplier.size() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listSupplier);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getSupplierById": {
                int idSupplier = Integer.parseInt(request.getParameter("id_supplier"));
                Supplier supplierByID = SupplierModel.INSTANCE.getSupplierByID(idSupplier);

                if (supplierByID.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(supplierByID);
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
                String address = request.getParameter("address");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                String fax = request.getParameter("fax");

                int addSupplier = SupplierModel.INSTANCE.addSupplier(name, address, phone, email, fax);
                if (addSupplier >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Th??m supplier th??nh c??ng!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Th??m supplier th???t b???i!");
                }
                break;
            }
            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String address = request.getParameter("address");
                String phone = request.getParameter("phone");
                String email = request.getParameter("email");
                String fax = request.getParameter("fax");

                Supplier supplierByID = SupplierModel.INSTANCE.getSupplierByID(id);
                if (supplierByID.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Th???t b???i!");
                    return;
                }

                int editSupplier = SupplierModel.INSTANCE.editSupplier(id, name, address, phone, email, fax);
                if (editSupplier >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("S???a supplier th??nh c??ng!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("S???a supplier th???t b???i!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteSUpplier = SupplierModel.INSTANCE.deleteSUpplier(id);
                if (deleteSUpplier >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("X??a supplier th??nh c??ng!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("X??a supplier th???t b???i!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }

}
