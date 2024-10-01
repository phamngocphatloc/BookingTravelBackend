package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.*;
import com.example.BookingTravelBackend.entity.CartDetails;
import com.example.BookingTravelBackend.entity.MenuDetails;
import com.example.BookingTravelBackend.entity.RestaurantCart;
import com.example.BookingTravelBackend.entity.User;
import com.example.BookingTravelBackend.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpls implements CartService {
    private final RestaurantCartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartDetailsRepository cartDetailsRepository;
    private final MenuRepository menuRepository;
    private final MenuDetailsRepository menuDetailsRepository;
    @Override
    public String addTocart(CartDetails item, int orderId) {
        MenuDetails productDetails = null;
        if (menuDetailsRepository.findMenuDetailsByMenuIdAndSize(item.getProduct().getId(),
                item.getSize())!=null) {
            productDetails = menuDetailsRepository.findMenuDetailsByMenuIdAndSize(
                    item.getProduct().getId(),
                    item.getSize());
        }else{
            return "Sản Phẩm Không Tồn Tại";
        }
        String valueItem = item.getProduct().getId()+item.getSize();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        RestaurantCart cart = cartRepository.findByUserAndBill(userLogin.getId(),orderId).get();
        if (cart.getListItems().get(valueItem)==null){
                Map<String,CartDetails> listitem = cart.getListItems();
                listitem.put(valueItem,item);
                cart.setListItems(listitem);
                cartDetailsRepository.save(item);
        }else{
            CartDetails itemCart = cart.getListItems().get(valueItem);
            itemCart.setAmount(itemCart.getAmount()+item.getAmount());
                Map<String,CartDetails> listitem = cart.getListItems();
                listitem.put(valueItem,itemCart);
                cart.setListItems(listitem);
                cartDetailsRepository.save(itemCart);
            }
        cartRepository.save(cart);
        return "Thêm Vào Giỏ Hàng Thành Công";
    }

    @Override
    @Transactional
    public String removeToCart(CartDetails item, int billId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        String itemValue = item.getProduct().getId()+item.getSize();
        RestaurantCart cart = cartRepository.findByUserAndBill(userLogin.getId(),billId).get();
        if (cart.getListItems().get(itemValue)!=null){
            cart.getListItems().remove(itemValue);
            cartDetailsRepository.deleteCartDetailsByCartIdAndKey(cart.getCartId(),itemValue);
            return "xóa giỏ hàng thành công";
        }
        return "Không Thấy SP Này";
    }

    @Override
    public void save(RestaurantCart cart) {
        cartRepository.save(cart);
    }

    @Override
    public String setQtyTocart(CartDetails item, int billId, int quantityset) {
        MenuDetails productDetails = null;
        if (menuDetailsRepository.findMenuDetailsByMenuIdAndSize(item.getProduct().getId(),
                item.getSize())!=null) {
            productDetails = menuDetailsRepository.findMenuDetailsByMenuIdAndSize(
                    item.getProduct().getId(),
                    item.getSize());
        }else{
            return "Sản Phẩm Không Tồn Tại";
        }
        String valueItem = item.getProduct().getId()+item.getSize();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        User userLogin = userRepository.findById(principal.getId()).get();
        RestaurantCart cart = cartRepository.findByUserAndBill(userLogin.getId(),billId).get();
        if (cart.getListItems().get(valueItem)==null){
                Map<String,CartDetails> listitem = cart.getListItems();
                item.setAmount(quantityset);
                listitem.put(valueItem,item);
                cart.setListItems(listitem);
                cartDetailsRepository.save(item);
        }else{
            CartDetails itemCart = cart.getListItems().get(valueItem);
            itemCart.setAmount(itemCart.getAmount()+item.getAmount());
                Map<String,CartDetails> listitem = cart.getListItems();
                itemCart.setAmount(quantityset);
                listitem.put(valueItem,itemCart);
                cart.setListItems(listitem);
                cartDetailsRepository.save(itemCart);
            }
        cartRepository.save(cart);
        return "Thêm Vào Giỏ Hàng Thành Công";
    }

    @Override
    public RestaurantCart findCartByUserIdAndBillId(int userId, int billId) {
        if (cartRepository.findByUserAndBill(userId,billId).isEmpty()){
            throw new IllegalArgumentException("Không Có Cart Này");
        }
        return cartRepository.findByUserAndBill(userId,billId).get();
    }
}
