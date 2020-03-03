package com.bookinggo.RESTfulDemo.Service;

import com.bookinggo.RESTfulDemo.entity.TourPackage;
import com.bookinggo.RESTfulDemo.repository.TourPackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TourPackageService {
    private TourPackageRepository tourPackageRepository;

    public TourPackage createTourPackage(String code, String name){
        if(!tourPackageRepository.existsById(code)){
            return tourPackageRepository.save(new TourPackage(code, name));
        }

        return null;
    }

    public Iterable<TourPackage> lookup(){
        return tourPackageRepository.findAll();
    }

    public long total() {
        return tourPackageRepository.count();
    }
}
