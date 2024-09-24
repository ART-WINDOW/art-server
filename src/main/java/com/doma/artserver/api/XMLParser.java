package com.doma.artserver.api;

import org.springframework.stereotype.Component;

import java.util.List;

public interface XMLParser<T> {
    List<T> parse(String xmlData) throws Exception;
}
