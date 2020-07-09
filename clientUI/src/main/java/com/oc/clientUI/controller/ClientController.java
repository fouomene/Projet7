package com.oc.clientUI.controller;

import com.oc.clientUI.beans.BookBean;
import com.oc.clientUI.beans.LoanBean;
import com.oc.clientUI.beans.Search;
import com.oc.clientUI.beans.UserBean;
import com.oc.clientUI.enums.Role;
import com.oc.clientUI.proxies.MicroserviceProxy;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClientController {
    @Autowired
    MicroserviceProxy microserviceProxy;

    private UserBean getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute("userCurrent");
        if (user == null)
            user = new UserBean();
        return user;
    }

    @GetMapping(name = "/")
    public String homepage(Model model, HttpServletRequest request) {
        Search search = new Search();
        model.addAttribute("search", search);
        List<BookBean> books = microserviceProxy.ListBook();
        model.addAttribute("books", books);
        UserBean userCurrent = getUserSession(request);
        model.addAttribute("userCurrent", userCurrent);
        return "Homepage";
    }

    @RequestMapping("/logIn")
    public String viewLogInPage(Model model, HttpServletRequest request) {
        UserBean userCurrent = getUserSession(request);
        model.addAttribute("userCurrent", userCurrent);
        if (userCurrent.getRole() == Role.NOT_CONNECTED)
            return "logIn";
        return "redirect:/";
    }

    @RequestMapping(value = "/logInCheck", method = RequestMethod.POST)
    public String viewLogInCheckPage(
            HttpServletRequest request,
            Model model,
            @ModelAttribute("user") UserBean user) {

        HttpSession session = request.getSession();
        UserBean userCurrent = (UserBean) session.getAttribute("userCurrent");
        if (userCurrent == null) {
            UserBean userSearch = microserviceProxy.findByUsername(user.getUsername());
            if (userSearch != null) {
                if (user.getPassword().equals(userSearch.getPassword())) {
                    model.addAttribute("userCurrent", userSearch);
                    session.setAttribute("userCurrent", userSearch);
                    return "redirect:/";
                }
            }
            model.addAttribute("userCurrent", new UserBean());
            model.addAttribute("message", " Connexion échoué ! réessayer !");
            return "logIn";
        }
        return "redirect:/";

    }

    @RequestMapping("/logOut")
    public String logoutAndViewHomepage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("userCurrent");
        return "redirect:/";
    }

    @GetMapping("book/{id}")
    public ModelAndView showBookPage(
            @PathVariable(name = "id") long id,
            HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("book");
        UserBean userCurrent = getUserSession(request);
        modelAndView.addObject("userCurrent", userCurrent);
        BookBean book = microserviceProxy.displayBook(id);
        modelAndView.addObject("book", book);
        return modelAndView;
    }

    @GetMapping(value = "LoansSearch")
    public String showListLoanByUser(Model model, HttpServletRequest request) {
        UserBean userCurrent = getUserSession(request);
        model.addAttribute("userCurrent", userCurrent);
        if (userCurrent.getRole() == Role.USER) {
            List<LoanBean> loans = microserviceProxy.findLoanByUser(userCurrent.getId());
            model.addAttribute("loans", loans);
            return "loans";
        }
        return "redirect:/";

    }

    @GetMapping("/extension/{id}")
    public String addExtension(@PathVariable(name = "id") Long id) {
        microserviceProxy.addExtension(id);
        return "redirect:/LoansSearch";
    }

    @PostMapping("/search")
    public String viewSearchPage(
            @ModelAttribute("search") Search search,
            Model model,
            HttpServletRequest request) {

        UserBean userCurrent = getUserSession(request);
        model.addAttribute("userCurrent", userCurrent);
        if (search.getName().equals("")) {
            model.addAttribute("search", search);
            return "redirect:/";
        }
        List<BookBean> books = microserviceProxy.findBookByKeyword(search.getName());
        if (books.isEmpty())
            return "redirect:/";
        model.addAttribute("books", books);
        model.addAttribute("search", search);
        search.init();
        return "Homepage";
    }

}
