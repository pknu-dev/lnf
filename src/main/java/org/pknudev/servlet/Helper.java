package org.pknudev.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pknudev.model.User;

public abstract class Helper {
    public static String getStringParameter(HttpServletRequest request, String key, boolean required) throws ServletException {
        String value = request.getParameter(key);
        if (value == null || value.isEmpty()) {
            if (required) {
                throw new ServletException("missing required parameter: " + key);
            }
            return null;
        }
        return value;
    }

    public static Integer getIntegerParameter(HttpServletRequest request, String key, boolean required) throws ServletException {
        String value = getStringParameter(request, key, required);
        if (value == null) {
            return null;
        }
        return Integer.parseInt(value);
    }

    public static Date getDateParameter(HttpServletRequest request, String key, String pattern, boolean required) throws ServletException, ParseException {
        String value = getStringParameter(request, key, required);
        if (value == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).parse(value);
    }

    public static User getLoginUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "you need to login");
            return null;
        }
        return user;
    }
}
