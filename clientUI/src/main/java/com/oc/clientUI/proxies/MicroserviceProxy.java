package com.oc.clientUI.proxies;

import com.oc.clientUI.beans.BookBean;
import com.oc.clientUI.beans.LoanBean;
import com.oc.clientUI.beans.UserBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "microservice",url = "localhost:9001")
public interface MicroserviceProxy {
    @GetMapping(value = "/BooksSearch/{keyword}")
    List<BookBean> findBookByKeyword(@PathVariable("keyword") String keyword);

    @GetMapping(value = "/Books/{id}")
    BookBean displayBook(@PathVariable("id") Long id);

    @GetMapping(value = "Books")
    List<BookBean> ListBook();

    @PostMapping(value = "LoansSearch")
    List<LoanBean> findLoanByUser(@RequestBody Long idUser);

    @GetMapping(value = "Loans/{id}")
    LoanBean displayLoan(@PathVariable Long id);

    @GetMapping(value = "Loans")
    List<LoanBean> ListLoan();

    @PostMapping(value = "Loan/{id}")
    void addExtension(@PathVariable Long id);

    @GetMapping(value = "User/{username}")
    UserBean findByUsername(@PathVariable String username);
}
