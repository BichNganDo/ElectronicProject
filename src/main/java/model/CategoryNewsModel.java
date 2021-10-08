package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.category_news.CategoryNews;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CategoryNewsModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "category_news";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static CategoryNewsModel INSTANCE = new CategoryNewsModel();

    public List<CategoryNews> getSliceNews(int offset, int limit, String searchQuery, int searchStatus) {
        List<CategoryNews> resultListNews = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListNews;

            }
            String sql = "SELECT * FROM `" + NAMETABLE
                    + "` WHERE 1 = 1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND name LIKE ? ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND status = ? ";
            }

            sql = sql + " LIMIT ? OFFSET ? ";

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchStatus > 0) {
                ps.setInt(param++, searchStatus);
            }

            ps.setInt(param++, limit);
            ps.setInt(param++, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CategoryNews categoryNews = new CategoryNews();
                categoryNews.setId(rs.getInt("id"));
                categoryNews.setName(rs.getString("name"));
                categoryNews.setOrders(rs.getInt("orders"));
                categoryNews.setStatus(rs.getInt("status"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                categoryNews.setCreated_date(dateString);

                long currentTimeUpdated = rs.getLong("updated_date");
                Date dateUpdated = new Date(currentTimeUpdated);
                String dateStringUpdated = sdf.format(dateUpdated);
                categoryNews.setUpdated_date(dateStringUpdated);

                resultListNews.add(categoryNews);
            }

            return resultListNews;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListNews;
    }

    public int getTotalNews(String searchQuery, int searchStatus) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM category_news WHERE 1=1";
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND name LIKE ? ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND status = ? ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchStatus > 0) {
                ps.setInt(param++, searchStatus);
            }

            ResultSet rs = ps.executeQuery();

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

    public CategoryNews getNewsByID(int id) {
        CategoryNews result = new CategoryNews();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getNewsByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getNewsByIdStmt.setInt(1, id);

            ResultSet rs = getNewsByIdStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setOrders(rs.getInt("orders"));
                result.setStatus(rs.getInt("status"));

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

    public int addCategoryNews(String name, int orders, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (name, orders, status,"
                    + "created_date, updated_date) "
                    + "VALUES (?, ?, ?, ?, ?)");
            addStmt.setString(1, name);
            addStmt.setInt(2, orders);
            addStmt.setInt(3, status);
            addStmt.setString(4, System.currentTimeMillis() + "");
            addStmt.setString(5, System.currentTimeMillis() + "");

            int rs = addStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editCategoryNews(int id, String name, int orders, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET name = ?, orders = ?, "
                    + "status = ?, updated_date = ? WHERE id = ? ");
            editStmt.setString(1, name);
            editStmt.setInt(2, orders);
            editStmt.setInt(3, status);
            editStmt.setString(4, System.currentTimeMillis() + "");
            editStmt.setInt(5, id);

            int rs = editStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public int deleteCategoryNews(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            CategoryNews newsByID = getNewsByID(id);
            if (newsByID.getId() == 0) {
                return ErrorCode.NOT_EXIST.getValue();
            }
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM `" + NAMETABLE + "` WHERE id = ?");
            deleteStmt.setInt(1, id);
            int rs = deleteStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }
}
