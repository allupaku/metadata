package me.althaf.parsers;

public interface IRecordParser<T> {

    public T doParse() throws Exception;

}
