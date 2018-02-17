package com.bluetooth.tangwuyang.fantuanlibrary;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 观察者模式实现
 *@author: tangwuyang on 2018/2/12
 */
public class IndexValueBus {
    private static volatile IndexValueBus sEntityBus;
    private EntityObservable mEntityObservable;

    private IndexValueBus() {

        mEntityObservable = new EntityObservable();
    }

    public static IndexValueBus getInstance() {

        if(sEntityBus == null) {
            synchronized (IndexValueBus.class) {
                if(sEntityBus == null) {
                    sEntityBus = new IndexValueBus();
                }
            }
        }
        return sEntityBus;
    }

    public void addObserver(Observer observer) {

        mEntityObservable.addObserver(observer);
    }

    public void clear() {

        mEntityObservable.deleteObservers();
    }

    public void notifyDataSetChanged(List<String> indexValue) {

        mEntityObservable.observaleChanged();
        mEntityObservable.notifyObservers(indexValue);
    }

    public static class EntityObservable extends Observable {

        public void observaleChanged() {

            this.setChanged();
        }
    }
}
