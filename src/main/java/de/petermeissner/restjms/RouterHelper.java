package de.petermeissner.restjms;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RouterHelper {

    public static String getHTTPEndpoinst(Class<?> c) {
        List<String> endpoints = new ArrayList<String>();

        for (Method m : c.getMethods()) {

            // get annotations
            Annotation[] anot = m.getDeclaredAnnotations();

            // check for get/post annotations
            boolean has_get = Arrays.stream(anot).anyMatch(a -> a.annotationType().getName().equals("jakarta.ws.rs.GET"));
            boolean has_post = Arrays.stream(anot).anyMatch(a -> a.annotationType().getName().equals("jakarta.ws.rs.POST"));

            String http_method = "";
            if (has_get) {
                http_method = "GET";
            } else if (has_post) {
                http_method = "POST";
            } else {
                continue;
            }

            for (Annotation a : anot) {

                if (a.annotationType().getName().equals("jakarta.ws.rs.Path")) {
                    final String path = a.toString().split("\"")[1];
                    endpoints.add(http_method + ": <a href=\"api" + path + "\">" + path + "</a>");
                }
            }
        }

        Collections.sort(endpoints);
        return String.join("<br>\n", endpoints);
    }
}
