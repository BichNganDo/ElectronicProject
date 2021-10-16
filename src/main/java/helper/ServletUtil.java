package helper;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ServletUtil {

    private static final SimpleDateFormat RFC_822_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

    public static void printJson(HttpServletRequest req, HttpServletResponse resp, String content) {
        RFC_822_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String currentDate = RFC_822_DATE_FORMAT.format(new Date(System.currentTimeMillis()));
        resp.setHeader("Date", currentDate);
        resp.setContentType("application/json; charset=utf-8");

        setCrossDomainAllow(resp);

        print(resp, content);
    }

    private static void setCrossDomainAllow(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, Accept, x-auth-token, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, x-requested-with");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
        resp.setHeader("Access-Control-Allow-Origin", "*");
    }

    public static void print(HttpServletResponse resp, String content) {
        PrintWriter out = null;
        try {
            RFC_822_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
            final String currentDate = RFC_822_DATE_FORMAT.format(new Date(System.currentTimeMillis()));
            resp.setHeader("Date", currentDate);
            resp.setCharacterEncoding("utf-8");
            //setCrossDomainAllow(resp);
            out = resp.getWriter();
            out.print(content);
        } catch (Throwable ex) {

        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static List<Integer> convertStringToArray(String s) {
        if (StringUtils.isEmpty(s)) {
            return new ArrayList<>();
        }

        List<Integer> result = new ArrayList<>();

        String[] arr = s.split(",");
        for (String item : arr) {
            result.add(NumberUtils.toInt(item));
        }

        return result;
    }

    public static String convertArrayToString(List<Integer> array) {
        if (array == null || array.size() <= 0) {
            return "";
        }

        String s = "";
        for (Integer item : array) {
            s = s + item + ",";
        }

        return s.substring(0, s.length() - 1);

    }

//    public static void main(String[] args) {
//        String s = "1,2,3";
//        List<Integer> result = ServletUtil.convertStringToArray(s);
//        System.out.println(result);
//
//    }
}
