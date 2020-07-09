package com.oc.microservice.controller;

import com.oc.microservice.dao.BookDao;
import com.oc.microservice.dao.LoanDao;
import com.oc.microservice.model.Book;
import com.oc.microservice.model.Loan;
import com.oc.microservice.model.User;
import com.oc.microservice.service.ExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@RestController
public class LoanController {
    @Autowired
    private LoanDao loanDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private ExtensionService extensionService;

    @PostMapping(value = "LoansSearch")
    public List<Loan> findLoanByUser(@RequestBody Long idUser) {
        return loanDao.findByUser(idUser);
    }

    @GetMapping(value = "Loans/{id}")
    public Optional<Loan> displayLoan(@PathVariable Long id) {
        return loanDao.findById(id);
    }


    @RequestMapping(value = "/createLoan/{id}", method = RequestMethod.POST)
    public Boolean createLoan(@PathVariable(name = "id") Long id,User user) {
        Optional<Book> book = bookDao.findById(id);
        if(!book.isPresent() || book.get().getNbRemaining() <= 0){
            return false;
        }
        book.get().setNbRemaining(book.get().getNbRemaining()-1);
        Loan loan = new Loan();
        loan.setReturned(false);
        loan.setBook(book.get());
        loan.setUser(user);
        loan.setDateStart(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        loan.setExtension(false);
        loan.setDateEnd(extensionService.addDate(loan.getDateStart()));
        loanDao.save(loan);
        return true;
    }

    @RequestMapping(value = "/LoanReturned/{id}")
    public void loanReturned(@PathVariable(name = "id") Long id) {
        Optional<Loan> loan = loanDao.findById(id);
        if(loan.isPresent()){
            loan.get().getBook().setNbRemaining(loan.get().getBook().getNbRemaining()+1);
            loan.get().setReturned(true);
            loanDao.save(loan.get());
        }
    }

    @GetMapping(value = "Loans")
    public List<Loan> ListLoan(){
        return loanDao.findAll();
    }

    @PostMapping(value = "Loan/{id}")
    public void addExtension(@PathVariable Long id){
        Optional<Loan> loan= loanDao.findById(id);
        if (loan.isPresent()) {
            extensionService.addExtension(loan.get());
            loanDao.save(loan.get());
        }
    }
}