package com.example.demo_.controllers;

import com.example.demo_.models.Post;
import com.example.demo_.models.User;
import com.example.demo_.service.LoginService;
import com.example.demo_.service.PostService;
import com.example.demo_.service.SearchService;
import com.example.demo_.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private LoginService loginService;



    @GetMapping("/error")
    public String error(Model model) {
        return "error";
    }



    // admin mode
    @GetMapping("/profile/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin";
    }

    @GetMapping("/profile/admin/{id}")
    public String otherProfile(@PathVariable(value = "id") int id, Model model) {
        model.addAttribute("user", userService.findByIdOptional(id).get());
        model.addAttribute("posts", postService.findAllUserPosts(userService.findByIdOptional(id).get().getId()));
        return "profile-2";
    }

    @PostMapping("/admin/remove/{id}")
    public String adminPostDelete(@PathVariable(value = "id") int id, Model model) {
        postService.postDelete(id);
        return ("redirect:/profile/admin");
    }

    @GetMapping("/admin/edit/{id}")
    public String adminEdit(@PathVariable(value = "id") int id, Model model) {
        if(!postService.postExistById(id)) { return "redirect:/profile/admin"; }
        model.addAttribute("post", postService.findById(id));
        return "edit-2";
    }

    @PostMapping("/admin/edit/{id}")
    public String adminEditPost(@PathVariable(value = "id") int id, @ModelAttribute("post") @Valid Post post, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) { return "redirect:/admin/edit/" +id; }
        postService.postEdit(id, post);
        return "redirect:/profile/admin";
    }



    // read only mode
    @PostMapping("/search")
    public String search_1(Model model, @RequestParam String search_) {
        model.addAttribute("posts", searchService.postSearch(search_));
        return "search-1";
    }

    @GetMapping("/")
    public String home_1(Model model) {
        model.addAttribute("posts", postService.findAll());;
        return "home-1";
    }

    @GetMapping("/film")
    public String film_1(Model model) {
        model.addAttribute("posts", postService.findAllByCategory(1));
        return "film-1";
    }

    @GetMapping("/book")
    public String book_1(Model model) {
        model.addAttribute("posts", postService.findAllByCategory(2));
        return "book-1";
    }

    @GetMapping("/music")
    public String music_1(Model model) {
        model.addAttribute("posts", postService.findAllByCategory(3));
        return "music-1";
    }

    @GetMapping("/game")
    public String game_1(Model model) {
        model.addAttribute("posts", postService.findAllByCategory(4));
        return "game-1";
    }

    @GetMapping("/post-about/{id}")
    public String postAbout(@PathVariable(value = "id") int id, Model model) {
        if(!postService.postExistById(id)){ return "redirect:/"; }

        model.addAttribute("post", postService.findById(id));
        model.addAttribute("user", userService.findByIdArrayList(id));
        return "post-about-1";
    }

    @GetMapping("/login")
    public String log_in(Model model) { return "log-in"; }

    @GetMapping("/signin")
    public String sign_in(Model model) { return "sign-in"; }

    @PostMapping("/login")
    public String log_in_post(@RequestParam String email, @RequestParam String password, Model model) {
        return loginService.login(email, password);
    }

    @PostMapping("/signin")
    public String sign_in_post(@RequestParam String name, @RequestParam String surname, @RequestParam String email, @RequestParam String password, Model model) {
        User user = new User(name, surname, email, password);
        return userService.signupCheck(user);
    }



    // login mode
    @PostMapping("/search/{email}")
    public String search_2(@PathVariable(value = "email") String email, Model model, @RequestParam String search_) {
        model.addAttribute("posts", searchService.postSearch(search_));
        return "search-2";
    }

    @GetMapping("/{email}")
    public String home_2(@PathVariable(value = "email") String email, Model model) {
        model.addAttribute("posts", postService.findAll());
        return "home-2";
    }

    @GetMapping("/film/{email}")
    public String film_2(@PathVariable(value = "email") String email, Model model) {
        model.addAttribute("posts", postService.findAllByCategory(1));
        return "film-2";
    }

    @GetMapping("/book/{email}")
    public String book_2(@PathVariable(value = "email") String email, Model model) {
        model.addAttribute("posts", postService.findAllByCategory(2));
        return "book-2";
    }

    @GetMapping("/music/{email}")
    public String music_2(@PathVariable(value = "email") String email, Model model) {
        model.addAttribute("posts", postService.findAllByCategory(3));
        return "music-2";
    }

    @GetMapping("/game/{email}")
    public String game_2(@PathVariable(value = "email") String email, Model model) {
        model.addAttribute("posts", postService.findAllByCategory(4));
        return "game-2";
    }

    @GetMapping("/{email}/post-about/{id}")
    public String postAbout(@PathVariable(value = "email") String email, @PathVariable(value = "id") int id, Model model) {
        if(!postService.postExistById(id)){ return "redirect:/" +email; }

        model.addAttribute("post", postService.findById(id));
        model.addAttribute("user", userService.findByIdArrayList(id));
        return "post-about-2";
    }

    @GetMapping("/profile/{email}")
    public String profile(@PathVariable(value = "email") String email, Model model) {
        if(!userService.findEmailIsEmpty(email)){
            model.addAttribute("user", userService.findUserByEmail(email));
            model.addAttribute("posts", postService.findAllUserPosts(userService.findPostUser(email).getId()));
            return "profile-1";
        }  else { return "redirect:/login"; }
    }

    @GetMapping("/profile/add/{email}")
    public String add(@PathVariable(value = "email") String email, Model model) {
        model.addAttribute(new Post());
        if(!userService.findEmailIsEmpty(email)) { return "add"; }
        else { return "redirect:/error"; }
    }

    @PostMapping("/profile/add/{email}")
    public String addPost(@PathVariable(value = "email") String email, @ModelAttribute("post") @Valid Post post, BindingResult bindingResult) {
        if(bindingResult.hasErrors())  { return "redirect:/profile/add/" +email; }
        postService.postAdd(post, userService.userPostAdd(email));
        return ("redirect:/profile/"+email);
    }

    @PostMapping("/profile/remove/{email}/{id}")
    public String postDelete(@PathVariable(value = "id") int id, @PathVariable(value = "email") String email, Model model) {
        postService.postDelete(id);
        return ("redirect:/profile/"+email);
    }

    @GetMapping("/profile/edit/{email}/{id}")
    public String edit(@PathVariable(value = "email") String email, @PathVariable(value = "id") int id, Model model) {
        if(!postService.postExistById(id)) { return "redirect:/profile/edit/" +email +'/' +id; }
        model.addAttribute("post", postService.findById(id));
        return "edit-1";
    }

    @PostMapping("/profile/edit/{email}/{id}")
    public String editPost(@PathVariable(value = "email") String email, @PathVariable(value = "id") int id, @ModelAttribute("post") @Valid Post post, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) { return "redirect:/profile/edit/" +email +'/' +id; }
        postService.postEdit(id, post);
        return ("redirect:/profile/"+email);
    }

    @GetMapping("/profile/preview/{email}/{id}")
    public String preview(@PathVariable(value = "email") String email, @PathVariable(value = "id") int id, Model model) {
        model.addAttribute("post", postService.findById(id));
        model.addAttribute("user", userService.findByIdArrayList(postService.findById(id).get(0).getUserId()));
        return "post-preview";
    }
}