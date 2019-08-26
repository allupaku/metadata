package me.althaf.models;

public interface IEntryObserver<E,T> {

    public abstract void observe(E data);

    public abstract T getResult();
}
