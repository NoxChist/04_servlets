package ru.netology.service;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class PostService {
    private AtomicLong cnt;
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
        var maxId = repository.all().stream().max(Comparator.comparingLong(Post::getId));
        cnt = maxId.isPresent() ? new AtomicLong(maxId.get().getId()) : new AtomicLong(0);
    }

    public List<Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        return repository.getById(id).orElseThrow(NotFoundException::new);
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            cnt.getAndSet(cnt.get() + 1);
            post.setId(cnt.get());
            return repository.save(post);
        } else {
            var postToUpdate = repository.getById(post.getId());
            if (postToUpdate.isPresent()) {
                return repository.save(post);
            }
        }
        return null;
    }

    public void removeById(long id) {
        var postToRemove = repository.getById(id);
        if (postToRemove.isPresent()) {
            repository.removeById(id);
        }
    }
}

