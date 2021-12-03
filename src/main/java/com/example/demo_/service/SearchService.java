package com.example.demo_.service;

import com.example.demo_.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private PostService postService;

    public List<Post> postSearch(String searchQuery) {
        List<Post> posts = null;
        int temp = 0;
        String[] spliter = searchQuery.split(" ");
        for(int i = 0; i < spliter.length; i++) {
            List<Post> tempPost = postService.findPostByTags(spliter[i]);
            if (tempPost.size() > temp){
                temp = tempPost.size();
                posts = tempPost;
            }
        }
        return posts;
    }
}
