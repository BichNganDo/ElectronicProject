package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.product.FilterProduct;
import entity.product.ListProduct;
import entity.product.Product;
import helper.ServletUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ProductModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class APIProductServlet extends HttpServlet {

    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement("C:\\Users\\Ngan Do\\Documents\\NetBeansProjects\\ElectronicProject\\upload\\product");

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getproduct": {
                FilterProduct filterProduct = new FilterProduct();
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchCate = NumberUtils.toInt(request.getParameter("search_cate"));
                int searchSupplier = NumberUtils.toInt(request.getParameter("search_supplier"));
                int searchProperty = NumberUtils.toInt(request.getParameter("search_property"));
                int orderView = NumberUtils.toInt(request.getParameter("order_view"));
                int offset = (pageIndex - 1) * limit;

                filterProduct.setLimit(limit);
                filterProduct.setOffset(offset);
                filterProduct.setSearchQuery(searchQuery);
                filterProduct.setSearchCate(searchCate);
                filterProduct.setSearchSupplier(searchSupplier);
                filterProduct.setSearchProperty(searchProperty);
                filterProduct.setOrderView(orderView);

                List<Product> sliceProduct = ProductModel.INSTANCE.getSliceProduct(filterProduct);
                int totalProduct = ProductModel.INSTANCE.getTotalProduct(filterProduct);

                ListProduct listProduct = new ListProduct();
                listProduct.setTotal(totalProduct);
                listProduct.setListProduct(sliceProduct);
                listProduct.setItemPerPage(10);

                if (sliceProduct.size() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listProduct);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getProductById": {
                int idProduct = Integer.parseInt(request.getParameter("id_product"));
                Product productByID = ProductModel.INSTANCE.getProductByID(idProduct);

                if (productByID.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(productByID);
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
                try {
                    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                    if (isMultipart) {
                        Product productData = new Product();
                        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                        upload.setHeaderEncoding("UTF-8");

                        List<FileItem> items = upload.parseRequest(request);
                        for (FileItem item : items) {
                            if (item.isFormField()) {
                                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                                String fieldname = item.getFieldName();
                                String fieldvalue = item.getString("UTF-8");

                                switch (fieldname) {
                                    case "category": {
                                        productData.setId_cate(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "supplier": {
                                        productData.setId_supplier(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "name": {
                                        productData.setName(fieldvalue);
                                        break;
                                    }
                                    case "price": {
                                        productData.setPrice(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "price_sale": {
                                        productData.setPrice_sale(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "quantity": {
                                        productData.setQuantity(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "content": {
                                        productData.setContent(fieldvalue);
                                        break;
                                    }
                                    case "warranty": {
                                        productData.setWarranty(fieldvalue);
                                        break;
                                    }
                                    case "property": {
                                        productData.setProperty(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }

                                }

                            } else {
                                // Process form file field (input type="file").
                                String filename = FilenameUtils.getName(item.getName());
                                InputStream a = item.getInputStream();
                                Path uploadDir = Paths.get("upload/product/" + filename);
                                Files.copy(a, uploadDir, StandardCopyOption.REPLACE_EXISTING);
                                productData.setImage_url("upload/product/" + filename);
                            }
                        }

                        int addProduct = ProductModel.INSTANCE.addProduct(productData);

                        if (addProduct >= 0) {
                            result.setErrorCode(0);
                            result.setMessage("Thêm product thành công!");
                        } else {
                            result.setErrorCode(-1);
                            result.setMessage("Thêm product thất bại!");
                        }
                    } else {
                        result.setErrorCode(-4);
                        result.setMessage("Có lỗi");
                    }

//                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            }

            case "edit": {
                try {
                    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                    if (isMultipart) {
                        Product productData = new Product();
                        String oldImage = "";
                        String newImage = "";
                        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                        upload.setHeaderEncoding("UTF-8");

                        List<FileItem> items = upload.parseRequest(request);
                        for (FileItem item : items) {
                            if (item.isFormField()) {
                                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                                String fieldname = item.getFieldName();
                                String fieldvalue = item.getString("UTF-8");

                                switch (fieldname) {
                                    case "id": {
                                        productData.setId(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "category": {
                                        productData.setId_cate(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "supplier": {
                                        productData.setId_supplier(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "name": {
                                        productData.setName(fieldvalue);
                                        break;
                                    }
                                    case "price": {
                                        productData.setPrice(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "price_sale": {
                                        productData.setPrice_sale(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "quantity": {
                                        productData.setQuantity(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "image_url": {
                                        oldImage = fieldvalue;
                                        break;
                                    }
                                    case "content": {
                                        productData.setContent(fieldvalue);
                                        break;
                                    }
                                    case "warranty": {
                                        productData.setWarranty(fieldvalue);
                                        break;
                                    }
                                    case "property": {
                                        productData.setProperty(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }

                                }

                            } else {
                                // Process form file field (input type="file").
                                String filename = FilenameUtils.getName(item.getName());
                                InputStream a = item.getInputStream();
                                Path uploadDir = Paths.get("C:\\Users\\Ngan Do\\Documents\\NetBeansProjects\\ElectronicProject\\upload\\product\\" + filename);
                                Files.copy(a, uploadDir, StandardCopyOption.REPLACE_EXISTING);
                                newImage = "upload\\product\\" + filename;
                            }
                        }

                        if (StringUtils.isNotEmpty(newImage)) {
                            productData.setImage_url(newImage);
                        } else {
                            productData.setImage_url(oldImage);
                        }

                        Product productByID = ProductModel.INSTANCE.getProductByID(productData.getId());
                        if (productByID.getId() == 0) {
                            result.setErrorCode(-1);
                            result.setMessage("Thất bại!");
                            return;
                        }

                        int editProduct = ProductModel.INSTANCE.editProduct(productData);

                        if (editProduct >= 0) {
                            result.setErrorCode(0);
                            result.setMessage("Thêm product thành công!");
                        } else {
                            result.setErrorCode(-1);
                            result.setMessage("Thêm product thất bại!");
                        }
                    } else {
                        result.setErrorCode(-4);
                        result.setMessage("Có lỗi");
                    }

//                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteProduct = ProductModel.INSTANCE.deleteProduct(id);
                if (deleteProduct >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa product thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa product thất bại!");
                }
                break;
            }

            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }

}
