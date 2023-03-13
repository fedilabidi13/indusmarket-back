package tn.esprit.usermanagement.servicesImpl;

import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.enumerations.Category;
import tn.esprit.usermanagement.enumerations.OrdersStatus;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderImpl implements IOrderService {
    private ProductRequestRepo productRequestRepo;
    private OrderRepo orderRepo;
    private AuthenticationService authenticationService;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private ShoppingCartRepo shoppingCartRepo;


    //************************************** order Creation ************************************//

    @Override
   public Orders createOrder( ) {

        User u = authenticationService.currentlyAuthenticatedUser();


        Orders order = new Orders();
        order.setCreationDate(LocalDateTime.now());
        order.setUser(userRepo.getReferenceById(u.getId()));
        order.setPaid(false);
        order.setDilevryAdresse(u.getAdresse());
        order.setOrdersStatus(OrdersStatus.AVECLIVRAISON);
        order.setSecondCartItemList(new ArrayList<>());

        float amount =0;

        for (CartItem cartItem: order.getUser().getShoppingCart().getCartItemList())
        {

            if ( cartItem.getProduct().getStock().getCurrentQuantity()>= cartItem.getQuantity() ) {
                order.getSecondCartItemList().add(cartItem);
                if (cartItem.getProduct().getPriceAfterDiscount()==0){
                    amount+= cartItem.getProduct().getPrice()* cartItem.getQuantity();
                   // order.getUser().getShoppingCart().getCartItemList().remove(cartItem);
                   }
                else  { amount+= cartItem.getProduct().getPriceAfterDiscount()* cartItem.getQuantity();
                              //order.getUser().getShoppingCart().getCartItemList().remove(cartItem);
                    }
            } else {
                //throw new RuntimeException("Error: this product is not available anymore");
                System.err.println("sorry now the product is out of stock");
            }
        }
        order.setTotalAmount(amount);
        return  orderRepo.save(order);
    }

    //************************************* Update Order ********************************************//

    @Override
    public void updateOrder(Integer idOrder , String dilevryAdresse) {
        User usr = authenticationService.currentlyAuthenticatedUser();
        Orders order = orderRepo.getReferenceById(idOrder);
        LocalDateTime creationTime = order.getCreationDate();
        LocalDateTime currentTime = LocalDateTime.now();
        long hoursSinceCreation = ChronoUnit.HOURS.between(creationTime, currentTime);


        if (order.getUser().getId()== usr.getId() && hoursSinceCreation >= 24 ) {
            order.setDilevryAdresse(dilevryAdresse);
        }
        orderRepo.save(order);

    }

    //**************************************** delete order ********************************//


    @Override
    public void deleteOrder(Integer orderId)  {

        Orders order = orderRepo.findById(orderId).get();
        User usr = authenticationService.currentlyAuthenticatedUser();
        LocalDateTime creationTime = order.getCreationDate();
        LocalDateTime currentTime = LocalDateTime.now();
        long hoursSinceCreation = ChronoUnit.HOURS.between(creationTime, currentTime);

        if (order.getUser().getId()== usr.getId() && hoursSinceCreation >= 24 ) {
            System.err.println("the order is in charge you can not delete it");
        }
        else {
        orderRepo.delete(order); }
    }

    //***************************** recommendProduct ****************************************//


       @Override
        public Product recommendProduct( ) {
            User usr = authenticationService.currentlyAuthenticatedUser();
            List<Orders> orders = usr.getOrdersList();
            List<Category> categories = new ArrayList<>();
            for (Orders order : orders) {
                List<CartItem> items = order.getSecondCartItemList();
                for (CartItem item : items) {
                    Product product = item.getProduct();
                    Category category =  product.getCategory();
                    categories.add(category);
                }
            }
            Map<Category, Long> categoryCounts = new HashMap<>();
            for (Category category : categories) {
                Long count = categoryCounts.get(category);
                if (count == null) {
                    count = 0L;
                }
                categoryCounts.put(category, count + 1);
            }

            Category favoriteCategory = null;
            Long maxCount = 0L;
            for (Map.Entry<Category, Long> entry : categoryCounts.entrySet()) {
                Category category = entry.getKey();
                Long count = entry.getValue();
                if (count > maxCount) {
                    favoriteCategory = category;
                    maxCount = count;
                }
            }
            List<Product> products = productRepo.findByCategory(favoriteCategory);
            int index = new Random().nextInt(products.size());
            return products.get(index);
        }




        //*************************** make the stock quantity less *******************************//

    @Override
      public void lessQuantity (String orderId){

        Orders orderpaid = orderRepo.getReferenceById(Integer.valueOf(orderId));

           for (CartItem cartItem: orderpaid.getSecondCartItemList()){

               int productQuantityInStock = cartItem.getProduct().getStock().getCurrentQuantity();
               int productQuantityTaking = cartItem.getQuantity();
               int theNewQuantity = productQuantityInStock-productQuantityTaking;

               cartItem.getProduct().getStock().setCurrentQuantity(theNewQuantity);
               productRepo.save(cartItem.getProduct());


           }


           orderRepo.save(orderpaid);

       }

    }


