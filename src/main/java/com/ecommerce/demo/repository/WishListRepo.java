package com.ecommerce.demo.repository;

import java.util.List;

import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepo extends JpaRepository<WishList, Integer> {

    List<WishList> findAllByUserOrderByCreatedDateDesc(User user);

}
