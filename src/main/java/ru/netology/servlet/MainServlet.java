package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MainServlet extends HttpServlet {
    private List<String> validPaths;
    private final String GET = "GET";
    private final String POST = "POST";
    private final String DELETE = "DELETE";
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository(new ConcurrentHashMap<>());
        final var service = new PostService(repository);
        controller = new PostController(service);
        validPaths = List.of("/api/posts", "/api/posts/\\d+");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET) && path.equals(validPaths.get(0))) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(validPaths.get(1))) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals(validPaths.get(0))) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(validPaths.get(1))) {
                // easy way
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

