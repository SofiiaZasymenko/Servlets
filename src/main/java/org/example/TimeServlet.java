package org.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    private static final String COOKIE_ARRAY_DELIMITER = ":";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z");
    private static final String LAST_TIMEZONE_COOKIE_NAME = "lastTimezone";
    private static final String DEFAULT_TIMEZONE = "UTC";
    private TemplateEngine engine;

    @Override
    public void init() {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(getServletContext().getRealPath("templates/"));
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, List<String>> queryParams = Utils.parseQueryString(request.getQueryString());
        List<String> lastTimezonesInCookie = Utils.findCookieByName(LAST_TIMEZONE_COOKIE_NAME, request.getCookies())
                .map(this::parseCookieTimezones)
                .orElse(List.of(DEFAULT_TIMEZONE));
        List<String> timezones = queryParams.getOrDefault("timezone", lastTimezonesInCookie);

        response.setContentType("text/html");
        response.addCookie(new Cookie(LAST_TIMEZONE_COOKIE_NAME, stringifyCookieTimezones(timezones)));

        Map<String, String> timesByTimezones = new LinkedHashMap<>();
        for (String timezone : timezones) {
            timesByTimezones.put(timezone, ZonedDateTime.now(ZoneId.of(timezone)).format(FORMAT));
        }

        Context context = new Context(request.getLocale(), Map.of("timesByTimezones", timesByTimezones));
        engine.process("display-time", context, response.getWriter());
        response.getWriter().close();
    }

    private List<String> parseCookieTimezones(Cookie cookie) {
        return Arrays.asList(cookie.getValue().split(COOKIE_ARRAY_DELIMITER));
    }

    private String stringifyCookieTimezones(List<String> timezones) {
        return String.join(COOKIE_ARRAY_DELIMITER, timezones);
    }
}