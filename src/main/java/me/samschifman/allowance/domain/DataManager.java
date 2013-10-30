package me.samschifman.allowance.domain;

import java.util.List;

public interface DataManager {

  public void open();
  
  public void close();
  
  public boolean isOpen();
  
  public Bear getBear(String email);
  
  public Bear getBear(Long id);
  
  public List<Bear> getBears();
  
  public void saveBear(Bear bear);
}