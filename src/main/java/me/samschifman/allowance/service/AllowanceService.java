package me.samschifman.allowance.service;

import java.io.InputStream;
import java.util.Properties;

import me.samschifman.allowance.domain.Bear;
import me.samschifman.allowance.domain.DataManager;
import me.samschifman.allowance.domain.Role;

public class AllowanceService {

  private static AllowanceService instance;
  public static AllowanceService getInstance() {
    if (instance == null) { instance = new AllowanceService(); }
    return instance;
  }

  private DataManager dataManager;
  private Properties properties;
  
  private AllowanceService() { }
  
  public Bear getBear(String email) {
    Bear bear = getDataManager().getBear(email);
    if (bear == null) {
      if (getProperties().getProperty("admins", "").contains(email)) {
        bear = new Bear();
        bear.setEmail(email);
        bear.setRole(Role.ADMIN);
        getDataManager().saveBear(bear);
      }
    }
    
    return bear;
  }
  
  private Properties getProperties() {
    if (properties == null) {
      try {
        properties = new Properties();
        InputStream in = getClass().getResourceAsStream("/me/samschifman/allowance/settings.properties");
        properties.load(in);
        in.close();
      } catch (Exception e) {
        throw new RuntimeException("Unable to load settings.properties file.", e);
      }
    }
    return properties;
  }
  
  
  private DataManager getDataManager() {
    if (dataManager == null) { dataManager = DataManager.getInstance(); }
    return dataManager;
  }
  
}