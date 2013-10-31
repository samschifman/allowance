package me.samschifman.allowance.service;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import me.samschifman.allowance.domain.Bear;
import me.samschifman.allowance.domain.DataManager;
import me.samschifman.allowance.domain.DataManagerImpl;
import me.samschifman.allowance.domain.Role;
import me.samschifman.allowance.domain.Transaction;
import me.samschifman.allowance.domain.TransactionType;

public class AllowanceService {

    private static AllowanceService instance;

    public static AllowanceService getInstance() {
        if (instance == null) {
            instance = new AllowanceService();
        }
        return instance;
    }

    public static AllowanceService getInstance(DataManager dataManager) {
        if (instance == null) {
            instance = new AllowanceService();
            instance.dataManager = dataManager;
        }
        return instance;
    }

    private DataManager dataManager;
    private Properties properties;

    private AllowanceService() {
    }

    public Bear getBear(String email) {
        Bear bear = getDataManager().getBear(email);
        if (bear == null) {
            if (getProperties().getProperty("admins", "").toUpperCase().contains(email.toUpperCase())) {
                bear = new Bear();
                bear.setEmail(email);
                bear.setRole(Role.ADMIN);
                getDataManager().saveBear(bear);
            }
        }
        
        if (bear != null) {
          reconcileBear(bear);
        }

        return bear;
    }

    public Collection<Bear> getBears() {
        Collection<Bear> bears = getDataManager().getBears();
        for (Bear bear : bears) {
          reconcileBear(bear);
        }
        return bears;
    }

    public Bear getCub(Long id) {
        Bear bear = getDataManager().getBear(id);
        reconcileBear(bear);
        return bear;
    }
    
    public void saveBear(Bear bear) {
       getDataManager().saveBear(bear);
    }
    
    public void debit(Bear bear, double amount, String comments) {
      if (amount > 0) { amount = 0 - amount; }
      creditBear(bear, TransactionType.DEBIT, amount, comments);
    }
    
    public void credit(Bear bear, double amount, String comments) {
      creditBear(bear, TransactionType.OTHER_CREDIT, amount, comments);
    }
    
    public boolean isTest() {
      boolean result = false;
      
      String test = getProperties().getProperty("test");
      if (!StringUtils.isEmpty(test)) {
        result = Boolean.valueOf(test);
      }
      
      return result;
    }

    protected void reconcileBear(Bear bear) {
        Transaction lastAllowance = findLastAllowance(bear);
        if (lastAllowance == null) {
            creditBearWithAllowance(bear, 0);
        } else {
          int weeksToCredit = numberOfWeeks(lastAllowance.getDate());
          for (int i = weeksToCredit-1; i >= 0; i--) {
            creditBearWithAllowance(bear, i);
          }
        }
        
        Transaction lastTrans = findLastTransaction(bear);
        bear.setBallance((lastTrans != null) ? lastTrans.getRunningTotal() : 0.0);
    }

    private Transaction findLastAllowance(Bear bear) {
        Transaction trans = null;

        for (int i = bear.getTransactions().size() - 1; i >= 0; i--) {
            Transaction aTrans = (Transaction) bear.getTransactions().get(i);
            if (aTrans.getType().equals(TransactionType.ALLOWANCE_CREDIT)) {
                trans = aTrans;
                break;
            }
        }

        return trans;
    }

    private int numberOfWeeks(Date date) {
        int weeks = 0;
        LocalDate localDate = new LocalDate(date.getTime());
        LocalDate now = new LocalDate();

        weeks = Weeks.weeksBetween(localDate, now).getWeeks();

        return weeks;
    }

    private void creditBearWithAllowance(Bear bear, int weeksAgo) {
        LocalDate localDate = new LocalDate();
        localDate = localDate.minusWeeks(weeksAgo);
        localDate = localDate.withDayOfWeek(DateTimeConstants.MONDAY);

        creditBear(bear, TransactionType.ALLOWANCE_CREDIT, localDate.toDate(),
                bear.getAllowance(), "Allowance payment.");
    }

    private void creditBearWithAllowance(Bear bear, Date date) {
        LocalDate localDate = new LocalDate(date.getTime());
        localDate = localDate.withDayOfWeek(DateTimeConstants.MONDAY);

        creditBear(bear, TransactionType.ALLOWANCE_CREDIT, localDate.toDate(),
                bear.getAllowance(), "Allowance payment.");
    }

    private void creditBear(Bear bear, TransactionType type, double amount, String comments) {
        Transaction lastTrans = findLastTransaction(bear);
        double runningTotal = lastTrans.getRunningTotal() + amount;
        creditBear(bear, type, new Date(), amount, runningTotal, comments);
    }

    private void creditBear(Bear bear, TransactionType type, Date date,
            double amount, String comments) {
        Transaction lastTrans = findLastTransaction(bear);
        double runningTotal = (lastTrans != null) ? lastTrans.getRunningTotal() + amount : amount;
        creditBear(bear, type, date, amount, runningTotal, comments);
    }

    private void creditBear(Bear bear, TransactionType type, Date date,
            double amount, double runningTotal, String comments) {
        if (!isZero(amount)) {
            Transaction trans = new Transaction();
            trans.setType(type);
            trans.setDate(date);
            trans.setAmount(amount);
            trans.setRunningTotal(runningTotal);
            trans.setComments(comments);
            bear.getTransactions().add(trans);
            getDataManager().saveBear(bear);
        }
    }

    private boolean isZero(double amount) {
        boolean result = false;

        double epsilon = 0.001;
        result = ((amount < (0 + epsilon)) && (amount > (0 - epsilon)));

        return result;
    }

    private Transaction findLastTransaction(Bear bear) {
        Transaction trans = null;
        if (!bear.getTransactions().isEmpty()) {
          trans = (Transaction) bear.getTransactions().get(
                  bear.getTransactions().size() - 1);
        }
        return trans;
    }

    private Properties getProperties() {
        if (properties == null) {
            try {
                properties = new Properties();
                InputStream in = getClass().getResourceAsStream(
                        "/me/samschifman/allowance/settings.properties");
                properties.load(in);
                in.close();
            } catch (Exception e) {
                throw new RuntimeException(
                        "Unable to load settings.properties file.", e);
            }
        }
        return properties;
    }

    private DataManager getDataManager() {
        if (dataManager == null) {
            dataManager = DataManagerImpl.getInstance();
        }
        return dataManager;
    }

}
