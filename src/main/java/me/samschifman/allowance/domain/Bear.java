package me.samschifman.allowance.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Bear {

    @Id Long id;
    String name;
    String email;
    Role role;
    double allowance;
    List<Transaction> transactions = new ArrayList<Transaction>();
    @Transient double ballance; 


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }
    
    public double getBallance() {
      return ballance;
   }

   public void setBallance(double ballance) {
      this.ballance = ballance;
   }

   public List<Transaction> getTransactions() {
        return transactions;
    }
}
