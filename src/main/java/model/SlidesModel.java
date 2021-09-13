package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.news.News;
import entity.slides.Slides;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class SlidesModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "slides";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static SlidesModel INSTANCE = new SlidesModel();

    public List<Slides> getSliceSlides(int offset, int limit, String searchQuery, int searchStatus) {
        List<Slides> resultListSlides = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListSlides;

            }
            String sql = "SELECT * FROM `" + NAMETABLE + "` WHERE 1=1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND slides.name LIKE ? ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND slides.status = ? ";
            }

            sql = sql + " LIMIT ? OFFSET ?";

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
                Slides slides = new Slides();
                slides.setId(rs.getInt("id"));
                slides.setName(rs.getString("name"));
                slides.setImage(rs.getString("image"));
                slides.setLink(rs.getString("link"));
                slides.setOrders(rs.getInt("orders"));
                slides.setStatus(rs.getInt("status"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                slides.setCreated_date(dateString);

                resultListSlides.add(slides);
            }

            return resultListSlides;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListSlides;
    }
//    public List<Slides> getSliceSlides(int offset, int limit, String searchQuery, int searchStatus) {
//        List<Slides> resultListSlides = new ArrayList<>();
//        Connection conn = null;
//        try {
//            conn = dbClient.getDbConnection();
//            if (null == conn) {
//                return resultListSlides;
//
//            }
//
//            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE "
//                    + "(slides.name = ? or ? is null) "
//                    + "and (slides.status = ? or ? is null) "
//                    + "LIMIT ? OFFSET ?");
//
//            pstmt.setString(1, searchQuery);
//            pstmt.setString(2, searchQuery);
//            pstmt.setInt(3, searchStatus);
//            pstmt.setInt(4, searchStatus);
//            pstmt.setInt(5, limit);
//
//            pstmt.setInt(6, offset);
//
//            ResultSet rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                Slides slides = new Slides();
//                slides.setId(rs.getInt("id"));
//                slides.setName(rs.getString("name"));
//                slides.setImage(rs.getString("image"));
//                slides.setLink(rs.getString("link"));
//                slides.setOrders(rs.getInt("orders"));
//                slides.setStatus(rs.getInt("status"));
//
//                long currentTimeMillis = rs.getLong("created_date");
//                Date date = new Date(currentTimeMillis);
//                String dateString = sdf.format(date);
//                slides.setCreated_date(dateString);
//
//                resultListSlides.add(slides);
//            }
//
//            return resultListSlides;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        } finally {
//            dbClient.releaseDbConnection(conn);
//        }
//
//        return resultListSlides;
//    }

    public int getTotalSlides(String searchQuery, int searchStatus) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "`";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND slides.name LIKE ? ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND slides.status = ? ";
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

    public Slides getSlidesByID(int id) {
        Slides result = new Slides();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getSlideByIdStmt = conn.prepareStatement("SELECT * FROM slides WHERE id = ? ");
            getSlideByIdStmt.setInt(1, id);

            ResultSet rs = getSlideByIdStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setImage(rs.getString("image"));
                result.setLink(rs.getString("link"));
                result.setOrders(rs.getInt("orders"));
                result.setStatus(rs.getInt("status"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                result.setCreated_date(dateString);

            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public int addSlides(String name, String image, String link, int orders, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO slides (name, image, link, orders, status, created_date) VALUES (?, ?, ?, ?, ?, ?)");
            addStmt.setString(1, name);
            addStmt.setString(2, image);
            addStmt.setString(3, link);
            addStmt.setInt(4, orders);
            addStmt.setInt(5, status);
            addStmt.setString(6, System.currentTimeMillis() + "");

            int rs = addStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editSlides(int id, String name, String image, String link, int orders, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            PreparedStatement editStmt = conn.prepareStatement("UPDATE slides SET name = ?, image = ?, link = ?, orders = ?, status = ? WHERE id = ? ");
            editStmt.setString(1, name);
            editStmt.setString(2, image);
            editStmt.setString(3, link);
            editStmt.setInt(4, orders);
            editStmt.setInt(5, status);
            editStmt.setInt(6, id);
            int rs = editStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public int deleteSlides(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            Slides slidesByID = getSlidesByID(id);
            if (slidesByID.getId() == 0) {
                return ErrorCode.NOT_EXIST.getValue();
            }
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM slides WHERE id = ?");
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
