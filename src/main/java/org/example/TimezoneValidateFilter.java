package org.example;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Map<String, List<String>> queryParams = Utils.parseQueryString(request.getQueryString());
        if (queryParams.containsKey("timezone")) {
            List<String> invalidTimezones = queryParams.get("timezone").stream()
                    .filter(timezone -> !Utils.isTimezoneValid(timezone))
                    .collect(Collectors.toList());

            if (!invalidTimezones.isEmpty()) {
                response.setStatus(400);

                response.setContentType("application/json");
                response.getWriter().write(String.format("{\"error\": \"Invalid timezone\", \"invalidValues\": %s }", invalidTimezones));
                response.getWriter().close();
            }
        }
        chain.doFilter(request, response);
    }
}