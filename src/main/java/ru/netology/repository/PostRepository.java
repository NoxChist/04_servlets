package ru.netology.repository;

import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Stub
public class PostRepository {
    private ConcurrentHashMap<Long, Post> postMap = new ConcurrentHashMap<>();

    public List<Post> all() {
        return List.copyOf(postMap.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.of(postMap.get(id));
    }

    public Post save(Post post) {
        postMap.put(post.getId(), post);
        return post;
    }

    public void removeById(long id) {
        postMap.remove(id);
    }
}
