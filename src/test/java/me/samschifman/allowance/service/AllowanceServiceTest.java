package me.samschifman.allowance.service;

import me.samschifman.allowance.domain.Bear;
import me.samschifman.allowance.domain.Transaction;
import me.samschifman.allowance.domain.TransactionType;

import org.joda.time.LocalDate;
import org.joda.time.Weeks;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AllowanceServiceTest {

  private AllowanceService service;
  
  @Before
  public void setUp() {
    service = AllowanceService.getInstance(new MockDataManager());
  }
  
  @Test
  public void testBlankBear() {
      Bear bear = makeBear(0.0);
      
      service.reconcileBear(bear);
      
      Assert.assertEquals("Bear's ballance should be 0.", 0.0, bear.getBallance(), 0.001);
      Assert.assertTrue("Bear should have no transactions.", bear.getTransactions().isEmpty());
  }
  
  @Test
  public void testBearUpToDate() {
      Bear bear = makeBear(5);
      addCredit(bear, 0, 2, TransactionType.ALLOWANCE_CREDIT);
      
      service.reconcileBear(bear);
      
      Assert.assertEquals("Bear's ballance should be 2.", 2.0, bear.getBallance(), 0.001);
      Assert.assertEquals("Bear should have 1 transaction.", 1, bear.getTransactions().size());
  }
  
  @Test
  public void testBearLastTransFiveWeeksAgo() {
      Bear bear = makeBear(5);
      addCredit(bear, 4, 5, TransactionType.ALLOWANCE_CREDIT);
      
      
      service.reconcileBear(bear);
      
      Assert.assertEquals("Bear's ballance should be 25.", 25.0, bear.getBallance(), 0.001);
      Assert.assertEquals("Bear should have 5 transaction.", 5, bear.getTransactions().size());
  }
  
  private void addCredit(Bear bear, int weeksAgo, double amount, TransactionType type) {
    LocalDate date = new LocalDate();
    date = date.minusWeeks(weeksAgo);
  
    Transaction trans = new Transaction();
    trans.setType(type);
    trans.setDate(date.toDate());
    trans.setAmount(amount);
    
    double runningTotal = amount;
    if (!bear.getTransactions().isEmpty()) {
      Transaction lastTrans = (Transaction) bear.getTransactions().get(bear.getTransactions().size() - 1);
      double lastTotal = lastTrans.getRunningTotal();
      runningTotal = lastTotal + runningTotal;
    }
    trans.setRunningTotal(runningTotal);
    
    bear.getTransactions().add(trans);
  }

  private Bear makeBear(double allowance) {
      Bear bear = new Bear();
      bear.setAllowance(allowance);
      return bear;
  }
}




















