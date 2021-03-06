package org.launchcode.todo.controllers;

//compile("org.thymeleaf.extras:thymeleaf-extras-springsecurity3")

import org.hibernate.Query;
import org.launchcode.todo.models.User;
import org.launchcode.todo.models.Login;
import org.launchcode.todo.models.data.AddEventDao;
import org.launchcode.todo.models.data.LoginDao;
import org.launchcode.todo.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
//@RequestMapping(value = "todo")
public class HomeController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private AddEventDao addEventDao;

    @RequestMapping(value="dashboard")
    public String index(Model model, @ModelAttribute Login login,HttpSession session){

       // model.addAttribute("title","My TODO List");
        System.out.println(login.getUsername());
       // HttpSession session = request.getSession();
        String username=(String)session.getAttribute("username");
        model.addAttribute("username",username);

        return "dashboard";
    }



    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLoginForm(Model model){

        model.addAttribute("title","Login Form");
        model.addAttribute(new User());
        return "login";
    }

    @RequestMapping(value="login", method= RequestMethod.POST)
    public String processLoginForm(HttpSession  session,@RequestParam("username") String username, @RequestParam("password") String password, Model model){

        Login login = loginDao.findOne(username);
        System.out.println(login);

//        if(username == null || password == null){
//
//            model.addAttribute("message", "Please fill the fields");
//            return "login";
//        }
//        else {
//
//            Optional<Login> loginOne = loginDao.findById(username);
//            System.out.println("loginone = " + loginOne);
//            Login login = loginOne.get();
//            System.out.println(login.getUsername());

            if (login == null) {
                model.addAttribute("message", "you are not Registered");
                return "login";
            } else if (login.getPassword().equals(password)) {

                //model.addAttribute("username", login.getUsername());
                   // HttpSession session =request.getSession();
                    session.setAttribute("username",login.getUsername());


                //addEventDao;
            return "redirect:dashboard";
            } else {
                model.addAttribute("message", "Invalid Username and Password");
                return "login";
            }

    }

    @RequestMapping(value="logout", method = RequestMethod.GET)
    public String logout(HttpSession session){

        session.invalidate();
        return "redirect:login";

    }

    @RequestMapping(value = "newuser", method = RequestMethod.GET)
    public String displayNewuserForm(Model model){

        model.addAttribute("title", "SignUp Form");
        model.addAttribute(new User());
        return "newuser";
    }

    @RequestMapping(value="newuser", method=RequestMethod.POST)
    public String processNewuserForm(@ModelAttribute @Valid User newUser, Errors errors, Model model, @RequestParam String username){

        if(errors.hasErrors()){
            model.addAttribute("title","SignUp Form");
            return "newuser";
        }
        Login login = new Login();
        login.setUsername(newUser.getUsername());
        login.setPassword(newUser.getPassword());
        userDao.save(newUser);
        loginDao.save(login);
        return "redirect:/login";
    }

}
