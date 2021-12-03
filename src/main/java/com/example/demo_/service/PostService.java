package com.example.demo_.service;

import com.example.demo_.models.Post;
import com.example.demo_.models.User;
import com.example.demo_.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) { this.postRepository = postRepository; }

    public List<Post> findAll() { return postRepository.findAll(); };

    public List<Post> findAllByCategory(int categoryId) { return postRepository.findAllByCategoryId(categoryId); }

    public List<Post> findPostByTags(String Tag) { return postRepository.findAllByTagsContaining(Tag); };

    public Boolean postExistById(int id) { return postRepository.existsById(id); }

    public ArrayList<Post> findById(int id) {
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        return result;
    }

    public List<Post> findAllUserPosts(int id) { return postRepository.findAllByUserId(id); }

    public void postAdd(Post post, Optional<User> user) {
        Optional<User> user2 = user;
        post.setUserId(user2.get().getId());
        post.setShortText(post.getPostText().substring(0, 25) +"...");
        postRepository.save(post);
    }

    public void postDelete(int id) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
    }

    public void postEdit(int id, Post post) {
        Post newPost = postRepository.findById(id).orElseThrow();
        newPost.setCategoryId(post.getCategoryId());
        newPost.setPostText(post.getPostText());
        newPost.setShortText(post.getPostText().substring(0, 25) +"...");
        newPost.setTitle(post.getTitle());
        newPost.setImgUrl(post.getImgUrl());
        newPost.setTags(post.getTags());
        newPost.setMark(post.getMark());

        postRepository.save(newPost);
    }


}
