package com.mh.soc.repository;

import com.mh.soc.model.Cart;
import com.mh.soc.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
