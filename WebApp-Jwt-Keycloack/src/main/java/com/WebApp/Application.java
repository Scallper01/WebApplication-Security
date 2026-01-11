package com.WebApp;

import com.WebApp.entities.Product;
import com.WebApp.repository.productRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	private final static Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		logger.info("******* Application Context initialized successfully **********");
		productRepository productRepository = context.getBean(productRepository.class);
		Long p = productRepository.count();
		logger.info("Numberr of Exiting products in DB : " +String.valueOf(p));
		if(p==0){
			productRepository.save(Product.builder().name("Laptop").quantity(10).build());
			productRepository.save(Product.builder().name("Mousse").quantity(20).build());
			productRepository.save(Product.builder().name("Keyboard").quantity(30).build());
		}
	}

}
