package com.example.demo_.service;

import com.example.demo_.models.User;
import com.example.demo_.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private PostService postService;

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public String signupCheck(User user) {
        List<User> testUser;
        String pageStr;
        if(!user.getEmail().isEmpty()) {
            testUser = userRepository.findByEmail(user.getEmail());
            if(testUser.size() == 0) {
                createUser(user);
                pageStr = ("redirect:/profile/" + user.getEmail());
            } else { pageStr = "redirect:/signin"; } }
        else  { pageStr = "redirect:/signin"; }
        return pageStr;
    }

    public void createUser(User user) { userRepository.save(user); }

    public List<User> findAll() { return userRepository.findAll(); }

    public List<User> findByEmailAndPassword(String email, String password) { return userRepository.findByEmailAndPassword(email, password); }

    public ArrayList<User> findByIdArrayList(int id) {
        Optional<User> user = userRepository.findById(postService.findById(id).get(0).getUserId());
        ArrayList<User> result = new ArrayList<>();
        user.ifPresent(result::add);
        return result;
    }

    public Optional<User> findByIdOptional(int id) { return userRepository.findById(id); }

    public Boolean findEmailIsEmpty(String email) { return userRepository.findByEmail(email).isEmpty(); }

    public Iterable<User> findUserByEmail(String email) { return userRepository.findByEmail(email); }

    public User findPostUser(String email) { return userRepository.findByEmail(email).get(0); }

    public Optional<User> userPostAdd(String email) { return userRepository.findById(userRepository.findByEmail(email).get(0).getId()); }
}
