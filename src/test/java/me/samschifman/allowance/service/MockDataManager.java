package me.samschifman.allowance.service;

import java.util.ArrayList;
import java.util.List;

import me.samschifman.allowance.domain.Bear;
import me.samschifman.allowance.domain.DataManager;

public class MockDataManager implements DataManager {

    List<Bear> bears;
    int lastId;

    @Override
    public void close() {
      bears = null;
      lastId = 0;    
    }

    @Override
    public boolean isOpen() {
        return bears != null;
    }

    @Override
    public void open() {
      bears = new ArrayList<Bear>();
      lastId = 0;
    }

    @Override
    public Bear getBear(String email) {
        if (!isOpen()) {
          open();
        }
        
        Bear bear = null;
        for (Bear other : bears) {
          if (other.getEmail().equals(email)) {
            bear = other;
            break;
          }
        }
        return bear;
    }

    @Override
    public Bear getBear(Long id) {
        if (!isOpen()) {
          open();
        }
        return bears.get(id.intValue());
    }

    @Override
    public List<Bear> getBears() {
        if (!isOpen()) {
          open();
        }
        return bears;
    }
    
    @Override
    public void saveBear(Bear bear) {
        if (!isOpen()) {
          open();
        }
        
        if (bear.getId() == null) {
          bear.setId(new Long(lastId++));
          bears.add(bear);
        } else {
          bears.set(bear.getId().intValue(), bear);
        }

    }
    
    

}