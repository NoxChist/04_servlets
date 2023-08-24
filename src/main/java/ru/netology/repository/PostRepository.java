package ru.netology.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {
    private ConcurrentHashMap<Long, Post> postMap = new ConcurrentHashMap<>();
    private AtomicLong cnt;

    @Autowired
    public PostRepository() {
        this.postMap = new ConcurrentHashMap<>();
        cnt = new AtomicLong(0);
    }

    public PostRepository(ConcurrentHashMap<Long, Post> postMap) {
        this.postMap = postMap;
        var maxId = all().stream().max(Comparator.comparingLong(Post::getId));
        cnt = maxId.isPresent() ? new AtomicLong(maxId.get().getId()) : new AtomicLong(0);
    }

    public List<Post> all() {
        return List.copyOf(postMap.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.of(postMap.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(cnt.incrementAndGet());
            postMap.put(post.getId(), post);
            return post;
        } else if (postMap.containsKey(post.getId())) {
            postMap.put(post.getId(), post);
            return post;
        }
        return null;
    }

    public void removeById(long id) {
        postMap.remove(id);
    }
}
