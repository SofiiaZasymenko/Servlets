package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss z");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, List<String>> queryParams = Utils.parseQueryString(request.getQueryString());
        List<String> timezones = queryParams.getOrDefault("timezone", List.of("UTC"));

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("<html><body>");

        out.print("Requested timezones: " + timezones);
        timezones.forEach(timezone -> out.write(String.format("<p>%s</p>", ZonedDateTime.now(ZoneId.of(timezone))
                .format(FORMAT))));

        out.print("</body></html>");
        out.close();
    }
}