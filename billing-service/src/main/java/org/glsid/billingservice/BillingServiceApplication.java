package org.glsid.billingservice;

import org.glsid.billingservice.entities.Bill;
import org.glsid.billingservice.entities.ProductItem;
import org.glsid.billingservice.feignClient.CustomerServiceClient;
import org.glsid.billingservice.feignClient.InventoryServiceClient;
import org.glsid.billingservice.model.Customer;
import org.glsid.billingservice.model.Product;
import org.glsid.billingservice.repository.BillRepository;
import org.glsid.billingservice.repository.ProductItemRepository;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.PagedModel;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
@EnableKafka
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository, ProductItemRepository productItemRepository,
                            CustomerServiceClient customerServiceClient, InventoryServiceClient inventoryServiceClient,
                            RepositoryRestConfiguration repositoryRestConfiguration){
        return args -> {
            repositoryRestConfiguration.exposeIdsFor(Bill.class);
            System.out.println("**************************");
            //Customer customer=customerServiceClient.getCustomerById(1L);
            //customer.getId()
            //Bill bill1=
            billRepository.save(new Bill(null, new Date(), null,(long) 1 , null));
            //PagedModel<Product> productPagedModel=inventoryServiceClient.pageProducts();
            /*productPagedModel.forEach(p->{
                ProductItem productItem=new ProductItem();
                productItem.setPrice(p.getPrice());
                productItem.setQuantity(1+new Random().nextInt(100));
                productItem.setBill(bill1);
                productItem.setProductID(p.getId());
                productItemRepository.save(productItem);
            });*/
            //billRepository.findAll().forEach(System.out::println);
            System.out.println("**************************");
        };
    }


}
