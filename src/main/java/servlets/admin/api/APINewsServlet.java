package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.news.ListNews;
import entity.news.News;
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
import model.NewsModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class APINewsServlet extends HttpServlet {

    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement("C:\\Users\\Ngan Do\\Documents\\NetBeansProjects\\ElectronicProject\\upload\\news");

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
            case "getAboutBlog": {
                News newsByType = NewsModel.INSTANCE.getNewsByType("about");

                if (newsByType.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(newsByType);
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
                        News newsData = new News();
                        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                        upload.setHeaderEncoding("UTF-8");

                        List<FileItem> items = upload.parseRequest(request);
                        for (FileItem item : items) {
                            if (item.isFormField()) {
                                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                                String fieldname = item.getFieldName();
                                String fieldvalue = item.getString("UTF-8");

                                switch (fieldname) {
                                    case "categoryNews": {
                                        newsData.setIdCategoryNews(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "title": {
                                        newsData.setTitle(fieldvalue);
                                        break;
                                    }
                                    case "info": {
                                        newsData.setInfo(fieldvalue);
                                        break;
                                    }
                                    case "content": {
                                        newsData.setContent(fieldvalue);
                                        break;
                                    }

                                }

                            } else {
                                // Process form file field (input type="file").
                                String filename = FilenameUtils.getName(item.getName());
                                InputStream a = item.getInputStream();
                                Path uploadDir = Paths.get("upload/news/" + filename);
                                Files.copy(a, uploadDir, StandardCopyOption.REPLACE_EXISTING);
                                newsData.setImage("upload/news/" + filename);
                            }
                        }
                        newsData.setType("article");
                        int addNews = NewsModel.INSTANCE.addNews(newsData);

                        if (addNews >= 0) {
                            result.setErrorCode(0);
                            result.setMessage("Thêm news thành công!");
                        } else {
                            result.setErrorCode(-1);
                            result.setMessage("Thêm news thất bại!");
                        }
                    } else {
                        result.setErrorCode(-4);
                        result.setMessage("Có lỗi");
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            }

            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int id_cate_news = NumberUtils.toInt(request.getParameter("categoryNews"));
                String title = request.getParameter("title");
                String info = request.getParameter("info");
                String content = request.getParameter("content");
                String image = request.getParameter("image");

                News newsByID = NewsModel.INSTANCE.getNewsByID(id);
                if (newsByID.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editNews = NewsModel.INSTANCE.editNews(id, id_cate_news, title, info, content, image);
                if (editNews >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa news thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa news thất bại!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteNews = NewsModel.INSTANCE.deleteNews(id);
                if (deleteNews >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa news thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa news thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
