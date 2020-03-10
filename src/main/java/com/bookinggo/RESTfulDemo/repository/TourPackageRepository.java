package com.bookinggo.RESTfulDemo.repository;

import com.bookinggo.RESTfulDemo.entity.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "packages", path = "packages")
public interface TourPackageRepository extends JpaRepository<TourPackage, String> {

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    <S extends TourPackage> S save(S s);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    <S extends TourPackage> List<S> saveAll(Iterable<S> iterable);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    void deleteById(String s);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    void delete(TourPackage tourPackage);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends TourPackage> iterable);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    void deleteAll();
}

