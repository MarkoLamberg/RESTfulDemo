package com.bookinggo.RESTfulDemo;

import com.bookinggo.RESTfulDemo.Service.TourPackageService;
import com.bookinggo.RESTfulDemo.Service.TourService;
import com.bookinggo.RESTfulDemo.entity.Region;
import com.bookinggo.RESTfulDemo.entity.TourPackage;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

import static com.bookinggo.RESTfulDemo.RestfulDemoApplication.TourFromFile.importTours;

@SpringBootApplication
public class RestfulDemoApplication implements CommandLineRunner {
	@Autowired
	private TourPackageService tourPackageService;

	@Autowired
	private TourService tourService;

	@Value("/ExploreCalifornia.json")
	private String jsonFile;

	public static void main(String[] args) {
		SpringApplication.run(RestfulDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Create the default tour packages
		tourPackageService.createTourPackage("BC", "Backpack Cal");
		tourPackageService.createTourPackage("CC", "California Calm");
		tourPackageService.createTourPackage("CH", "California Hot springs");
		tourPackageService.createTourPackage("CY", "Cycle California");
		tourPackageService.createTourPackage("DS", "From Desert to Sea");
		tourPackageService.createTourPackage("KC", "Kids California");
		tourPackageService.createTourPackage("NW", "Nature Watch");
		tourPackageService.createTourPackage("SC", "Snowboard Cali");
		tourPackageService.createTourPackage("TC", "Taste of California");
		System.out.println("Number of tours packages =" + tourPackageService.total());

		for(TourPackage p : tourPackageService.lookup()){
			System.out.println("Code: " + p.getCode() + ", Name: " + p.getName());
		}

		//Persist the Tours to the database
		importTours(jsonFile).forEach(t-> tourService.createTour(
				t.title,
				Integer.parseInt(t.price),
				t.length,
				t.packageType,
				Region.findByLabel(t.region)));
		System.out.println("Number of tours =" + tourService.total());
	}

	static class TourFromFile {
		//attributes as listed in the .json file
		private String packageType, title, length, price, region;

		static List<TourFromFile> importTours(String jsonFile) throws IOException {
			return new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).
					readValue(TourFromFile.class.getResourceAsStream(jsonFile),new TypeReference<List<TourFromFile>>(){});
		}
	}
}



