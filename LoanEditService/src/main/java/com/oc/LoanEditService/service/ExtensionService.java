package com.oc.LoanEditService.service;

import com.oc.LoanEditService.model.Loan;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class ExtensionService {
    public void addExtension(Loan loan){
        if(!loan.isExtension()) {
            loan.setExtension(true);
            loan.setDateEnd(addDate(loan.getDateEnd()));
        }
    }
    public Date addDate(Date date){
        return Date.valueOf(date.toLocalDate().plusDays(28));
    }
}
