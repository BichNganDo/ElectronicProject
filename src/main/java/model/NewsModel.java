package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.news.News;
import entity.product.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class NewsModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "news";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static NewsModel INSTANCE = new NewsModel();

    public List<News> getSliceNews(int offset, int limit, String searchQuery, int searchCategoryNews) {
        List<News> resultListNews = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListNews;

            }
            String sql = "SELECT news.*, category_news.name AS `categoryNews`, "
                    + "FROM news "
                    + "INNER JOIN category_news ON news.id_cate_news= category_news.id  WHERE 1=1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND product.name LIKE '%" + searchQuery + "%' ";
            }

            if (searchCategoryNews > 0) {
                sql = sql + " AND product.id_cate = " + searchCategoryNews;
            }

            sql = sql + " LIMIT " + limit + " OFFSET " + offset;

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                News news = new News();
                news.setId(rs.getInt("id"));
                news.setIdCategoryNews(rs.getInt("id_cate_news"));
                news.setCategoryNews(rs.getString("categoryNews"));
                news.setTitle(rs.getString("title"));
                news.setInfo(rs.getString("info"));
                news.setContent(rs.getString("content"));
                news.setImage(rs.getString("image"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                news.setCreated_date(dateString);

                long currentTimeUpdated = rs.getLong("updated_date");
                Date dateUpdated = new Date(currentTimeUpdated);
                String dateStringUpdated = sdf.format(dateUpdated);
                news.setUpdated_date(dateStringUpdated);

                resultListNews.add(news);
            }

            return resultListNews;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListNews;
    }

    public int getTotalNews(String searchQuery, int searchCategoryNews) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1 = 1";
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND name LIKE '%" + searchQuery + "%' ";
            }
            if (searchCategoryNews > 0) {
                sql = sql + " AND product.id_cate = " + searchCategoryNews;
            }

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return total;
    }

    public News getNewsByID(int id) {
        News result = new News();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            String sql = "SELECT * FROM `" + NAMETABLE + "` WHERE `id`='" + id + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setIdCategoryNews(rs.getInt("id_cate_news"));
                result.setCategoryNews(rs.getString("categoryNews"));
                result.setTitle(rs.getString("title"));
                result.setInfo(rs.getString("info"));
                result.setContent(rs.getString("content"));
                result.setImage(rs.getString("image"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                result.setCreated_date(dateString);

                long currentTimeUpdated = rs.getLong("updated_date");
                Date dateUpdated = new Date(currentTimeUpdated);
                String dateStringUpdated = sdf.format(dateUpdated);
                result.setUpdated_date(dateStringUpdated);

            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public int addNews(int idCategoryNews, String title, String info, String content, String image, String created_date, String updated_date) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            String sql = "INSERT INTO `" + NAMETABLE + "`"
                    + "(`id_cate_news`, `title`, `info`, `content`, `image`, `created_date`, `updated_date`) "
                    + "VALUES "
                    + "('" + idCategoryNews + "', '" + title + "', '" + info + "', '" + content + "', '" + image + "',"
                    + " '" + System.currentTimeMillis() + "', '" + System.currentTimeMillis() + "')";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int rs = preparedStatement.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

//    public int editProduct(int id, int id_cate, int id_supplier, String name, int price,
//            int price_sale, int quantity, String image_url, String content, String warranty, String hot) {
//        Connection conn = null;
//        try {
//            conn = dbClient.getDbConnection();
//            if (null == conn) {
//                return ErrorCode.CONNECTION_FAIL.getValue();
//            }
//
//            String sql = "UPDATE `" + NAMETABLE + "` "
//                    + "SET `id_cate`='" + id_cate + "' , "
//                    + "`id_supplier`='" + id_supplier + "', "
//                    + "`name`='" + name + "', "
//                    + "`price`='" + price + "', "
//                    + "`price_sale`='" + price_sale + "', "
//                    + "`quantity`='" + quantity + "', "
//                    + "`image_url`='" + image_url + "', "
//                    + "`warranty`='" + warranty + "', "
//                    + "`hot`='" + hot + "', "
//                    + "`content`='" + content + "'"
//                    + "WHERE `id`='" + id + "'";
//
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            int rs = preparedStatement.executeUpdate();
//
//            return rs;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            dbClient.releaseDbConnection(conn);
//        }
//        return ErrorCode.FAIL.getValue();
//    }
//
//    public int deleteProduct(int id) {
//        Connection conn = null;
//        try {
//            conn = dbClient.getDbConnection();
//            if (null == conn) {
//                return ErrorCode.CONNECTION_FAIL.getValue();
//            }
//
//            Product productByID = getProductByID(id);
//            if (productByID.getId() == 0) {
//                return ErrorCode.NOT_EXIST.getValue();
//            }
//            String sql = "DELETE FROM `" + NAMETABLE + "` WHERE `id`='" + id + "'";
//
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            int rs = preparedStatement.executeUpdate();
//
//            return rs;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            dbClient.releaseDbConnection(conn);
//        }
//        return ErrorCode.FAIL.getValue();
//    }
}
