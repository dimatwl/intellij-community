package org.jetbrains.ether.dependencyView;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectProcedure;
import gnu.trove.TIntProcedure;

/**
 * Created by IntelliJ IDEA.
 * User: db
 * Date: 08.03.11
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
class IntIntTransientMultiMaplet extends IntIntMultiMaplet {
  private final TIntObjectHashMap<TIntHashSet> myMap = new TIntObjectHashMap<TIntHashSet>();


  @Override
  public boolean containsKey(final int key) {
    return myMap.containsKey(key);
  }

  @Override
  public TIntHashSet get(final int key) {
    return myMap.get(key);
  }

  @Override
  public void putAll(IntIntMultiMaplet m) {
    m.forEachEntry(new TIntObjectProcedure<TIntHashSet>() {
      @Override
      public boolean execute(int key, TIntHashSet values) {
        put(key, values);
        return true;
      }
    });
  }

  @Override
  public void put(final int key, final TIntHashSet value) {
    final TIntHashSet x = myMap.get(key);
    if (x == null) {
      myMap.put(key, value);
    }
    else {
      value.forEach(new TIntProcedure() {
        @Override
        public boolean execute(int value) {
          x.add(value);
          return true;
        }
      });
    }
  }

  @Override
  public void replace(int key, TIntHashSet value) {
    if (value == null) {
      myMap.remove(key);
    }
    else {
      myMap.put(key, value);
    }
  }

  @Override
  public void put(final int key, final int value) {
    final TIntHashSet collection = myMap.get(key);
    if (collection == null) {
      final TIntHashSet x = new TIntHashSet();
      x.add(value);
      myMap.put(key, x);
    }
    else {
      collection.add(value);
    }
  }

  @Override
  public void removeFrom(final int key, final int value) {
    final TIntHashSet collection = myMap.get(key);
    if (collection != null) {
      if (collection.remove(value)) {
        if (collection.isEmpty()) {
          myMap.remove(key);
        }
      }
    }
  }

  @Override
  public void removeAll(int key, TIntHashSet values) {
    final TIntHashSet collection = myMap.get(key);
    if (collection != null) {
      values.forEach(new TIntProcedure() {
        @Override
        public boolean execute(int value) {
          collection.remove(value);
          return true;
        }
      });
      if (collection.isEmpty()) {
        myMap.remove(key);
      }
    }
  }

  @Override
  public void remove(final int key) {
    myMap.remove(key);
  }

  @Override
  public void replaceAll(IntIntMultiMaplet m) {
    m.forEachEntry(new TIntObjectProcedure<TIntHashSet>() {
      @Override
      public boolean execute(int key, TIntHashSet value) {
        replace(key, value);
        return true;
      }
    });
  }

  @Override
  public void forEachEntry(TIntObjectProcedure<TIntHashSet> procedure) {
    myMap.forEachEntry(procedure);
  }

  @Override
  public void close(){
    myMap.clear(); // free memory
  }

  public void flush(boolean memoryCachesOnly) {
  }
}
