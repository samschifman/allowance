package me.samschifman.allowance.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

public class DataManagerImpl implements DataManager {

  private static DataManager instance;
  public static DataManager getInstance() {
    if (instance == null) { instance = new DataManagerImpl(); }
    return instance;
  }
  
  private Objectify ofy;
  
  private DataManagerImpl() { }
  
  public void open() {
    ObjectifyService.register(Bear.class);
//    ObjectifyService.register(Transaction.class);
    ofy = ObjectifyService.ofy();

  }
  
  public void close() {
    ofy = null;
  }
  
  public boolean isOpen() {
    return (ofy != null);
  }
  
  public Bear getBear(String email) {
    Bear bear = null;
    if (!isOpen()) {
      open();
    }
    
//    bear = ofy.query(Bear.class).filter("email", email).get();
    
//    Query<Bear> query = ofy.load().type(Bear.class);
//    query = query.filter("email", email);
//    bear = query.first().now();
    
    // TODO: I should be using the query above, but it didn't work and I don't know why.
    for (Bear temp : getBears()) {
      if (StringUtils.equalsIgnoreCase(temp.getEmail(), email)) {
        bear = temp;
        break;
      }
    }
    
    return bear;
  }
  
  public Bear getBear(Long id) {
    Bear bear = null;
    if (!isOpen()) {
      open();
    }
    
    bear = ofy.load().type(Bear.class).id(id).now();
    
    return bear;
  }
  
  public List<Bear> getBears() {
    if (!isOpen()) {
      open();
    }
    
//    List<Bear> bears = ofy.query(Bear.class).list();
    List<Bear> bears = ofy.load().type(Bear.class).list();
    return bears;
  }
  
  public void saveBear(Bear bear) {
    if (!isOpen()) {
      open();
    }
    
//    ofy.put(bear);
    ofy.save().entity(bear).now();
  }
}










