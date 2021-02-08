package org.glsid.billingservice.controller;

import org.glsid.billingservice.entities.Bill;
import org.glsid.billingservice.entities.ProductItem;
import org.glsid.billingservice.feignClient.CustomerServiceClient;
import org.glsid.billingservice.feignClient.InventoryServiceClient;
import org.glsid.billingservice.model.Customer;
import org.glsid.billingservice.model.Product;
import org.glsid.billingservice.repository.BillRepository;
import org.glsid.billingservice.repository.ProductItemRepository;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin("*")
public class BillController {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private CustomerServiceClient customerServiceClient;
    @Autowired
    private InventoryServiceClient inventoryServiceClient;
    //@Autowired
    //private KeycloakRestTemplate keycloakRestTemplate;
    @Autowired
    private KafkaTemplate<String, Bill> kafkaTemplate;
    private final String topic="Bills-f";

    @GetMapping("/bills/full")
    public List<Bill> getBills(HttpServletRequest request){
        List<Bill> bills=billRepository.findAll();
        bills.forEach(bill->{
            bill.setCustomer(customerServiceClient.getCustomerById("Bearer "+this.getToken(request), bill.getCustomerid()));
            //bill.setCustomer(keycloakRestTemplate.getForObject("http://localhost:8081/customers/"
            //     +billRepository.findById(bill.getId()).get().getCustomerid(), Customer.class));
            bill.setProductItems(productItemRepository.findByBillId(bill.getId()));
            bill.getProductItems().forEach(pi->{
                pi.setProduct(inventoryServiceClient.getProductById("Bearer "+this.getToken(request), pi.getId()));
                //pi.setProduct(keycloakRestTemplate.getForObject("http://localhost:8082/products/"
                //  +billRepository.findById(bill.getId()).get().getProductItems(), Product.class));
            });
        });
        return bills;
    }

    private String getToken(HttpServletRequest request) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext context = principal.getKeycloakSecurityContext();
        return context.getTokenString();
    }

    @GetMapping("/bills/full/{id}")
    public Bill getBill(HttpServletRequest request, @PathVariable(name = "id") Long id){
        Bill bill=billRepository.findById(id).get();
        bill.setCustomer(customerServiceClient.getCustomerById("Bearer "+this.getToken(request), bill.getCustomerid()));
        //bill.setCustomer(keycloakRestTemplate.getForObject("http://localhost:8081/customers/"
        //      +billRepository.findById(id).get().getCustomerid(), Customer.class));
        bill.setProductItems(productItemRepository.findByBillId(id));
        for(ProductItem productItem : bill.getProductItems()){
            productItem.setProduct(inventoryServiceClient.getProductById("Bearer "+this.getToken(request),
                    productItem.getId()));
            productItemRepository.save(productItem);
        }
        //kafkaTemplate.send(topic,bill.getCustomer().getName(),bill);
        return bill;
    }

    @DeleteMapping("/bills/full/{id}")
    public void saveBill(@PathVariable long id){
        Bill bill=billRepository.findById(id).get();
        kafkaTemplate.send(topic,bill.getCustomer().getName(),bill);
        billRepository.deleteById(id);
    }

    @PostMapping(value="/bills/full")
    public Bill saveBill(HttpServletRequest request, @RequestBody Bill bill){
        /*bill.getProductItems().forEach(productItem -> {
            productItemRepository.save(productItem);
            productItem.setProduct(inventoryServiceClient.getProductById("Bearer "+this.getToken(request),
                productItem.getProductID()));
        });*/
        bill.setCustomer(customerServiceClient.getCustomerById("Bearer "+this.getToken(request), bill.getCustomerid()));
        PagedModel<Product> productPagedModel=inventoryServiceClient.pageProducts("Bearer "+this.getToken(request));
        productPagedModel.forEach(p->{
                ProductItem productItem=new ProductItem();
                productItem.setPrice(p.getPrice());
                productItem.setQuantity(1+new Random().nextInt(100));
                productItem.setBill(bill);
                productItem.setProductID(p.getId());
                productItemRepository.save(productItem);
        });
        kafkaTemplate.send(topic,bill.getCustomer().getName(),bill);
        billRepository.save(bill);
        return bill;
    }

    @GetMapping("/bills/{id}/customer")
    public Customer getCustomer(HttpServletRequest request, @PathVariable(name = "id") Long id){
        Customer customer=customerServiceClient.getCustomerById("Bearer "+this.getToken(request), id);
        return customer;
    }

    @GetMapping("/jwt")
    @ResponseBody
    public Map<String, String > map(HttpServletRequest request){
        KeycloakAuthenticationToken token=(KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal=(KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext=principal.getKeycloakSecurityContext();
        Map<String, String> map=new HashMap<>();
        map.put("access_token", keycloakSecurityContext.getIdTokenString());
        return map;
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e){
        return e.getMessage();
    }
}

