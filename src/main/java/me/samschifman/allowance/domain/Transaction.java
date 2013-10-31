package me.samschifman.allowance.domain;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Unindex;

@Embed
public class Transaction implements Comparable<Transaction> {
    @Id Long id;
    TransactionType type;
    Date date;
    double amount;
    double runningTotal;
    @Unindex String comments;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRunningTotal() {
        return runningTotal;
    }

    public void setRunningTotal(double runningTotal) {
        this.runningTotal = runningTotal;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public int compareTo(Transaction trans) {
      int i = (trans == null) ? 1 : 0 - this.getDate().compareTo(trans.getDate()); 
      return i;
    }

    public String toTableRow() {
      String row = "<tr>";
      
      row += "<td>" + getType() + "</td>";
      row += "<td>" + DateFormatUtils.format(getDate(), "MM/dd/yy h:mm a") + "</td>";
      row += "<td>" + getAmount() + "</td>";
      row += "<td>" + getRunningTotal() + "</td>";
      row += "<td>" + getComments() + "</td>";
      
      return row + "</tr>";
    }
    

}














