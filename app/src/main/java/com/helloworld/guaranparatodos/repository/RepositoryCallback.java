package com.helloworld.guaranparatodos.repository;

public interface RepositoryCallback<T> {

    void onSuccess(T resultado);

    void onError(Exception e);
}