package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.news.News;
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
            String sql = "SELECT news.*, category_news.name AS `categoryNews` "
                    + "FROM news "
                    + "INNER JOIN category_news ON news.id_cate_news= category_news.id  WHERE 1=1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND news.title LIKE ? ";
            }

            if (searchCategoryNews > 0) {
                sql = sql + " AND news.id_cate_news = ? ";
            }

            sql = sql + " LIMIT ? OFFSET ? ";

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchCategoryNews > 0) {
                ps.setInt(param++, searchCategoryNews);
            }

            ps.setInt(param++, limit);
            ps.setInt(param++, offset);
            ResultSet rs = ps.executeQuery();

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
                sql = sql + " AND news.title LIKE ? ";
            }

            if (searchCategoryNews > 0) {
                sql = sql + " AND news.id_cate_news = ? ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchCategoryNews > 0) {
                ps.setInt(param++, searchCategoryNews);
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

    public News getNewsByID(int id) {
        News result = new News();
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
                result.setIdCategoryNews(rs.getInt("id_cate_news"));
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

    public int addNews(int idCategoryNews, String title, String info, String content, String image) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (id_cate_news, title, info, content, image, "
                    + "created_date, updated_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            addStmt.setInt(1, idCategoryNews);
            addStmt.setString(2, title);
            addStmt.setString(3, info);
            addStmt.setString(4, content);
            addStmt.setString(5, image);
            addStmt.setString(6, System.currentTimeMillis() + "");
            addStmt.setString(7, System.currentTimeMillis() + "");

            int rs = addStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editNews(int id, int id_cate_news, String title, String info, String content, String image) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET id_cate_news = ?, title = ?, info = ?, "
                    + "content = ?, image = ?, updated_date = ? WHERE id = ? ");
            editStmt.setInt(1, id_cate_news);
            editStmt.setString(2, title);
            editStmt.setString(3, info);
            editStmt.setString(4, content);
            editStmt.setString(5, image);
            editStmt.setString(6, System.currentTimeMillis() + "");
            editStmt.setInt(7, id);

            int rs = editStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public int deleteNews(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            News newsByID = getNewsByID(id);
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
