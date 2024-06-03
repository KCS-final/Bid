package com.kcs3.bid.repository;

import com.kcs3.bid.entity.AuctionCompleteItem;
import com.kcs3.bid.entity.AuctionProgressItem;
import com.kcs3.bid.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface  ItemRepository extends JpaRepository<Item, Long> {
        @Query("SELECT i.seller.userId FROM Item i WHERE i.itemId = :itemId")
        Long findSellerIdByItemId(@Param("itemId") Long itemId);



        /**
         *  진행 경매 아이템 목록 조회
         */
        @Query("SELECT ap FROM AuctionProgressItem ap " +
                "JOIN FETCH ap.item item " +
                "JOIN FETCH item.category category " +
                "JOIN FETCH item.tradingMethod method " +
                "JOIN FETCH item.region region " +
                "WHERE (:category IS NULL OR category.category = :category) " +
                "AND (:method IS NULL OR method.tradingMethod = :method OR method.tradingMethod = 3) " +
                "AND (:region IS NULL OR region.region = :region) " +
                "ORDER BY item.itemId DESC")
        Slice<AuctionProgressItem> findByProgressItemWithLocationAndMethodAndRegion(
                @Param("category") String category,
                @Param("method") Integer method,
                @Param("region") String region,
                Pageable pageable);








        /**
         *  완료된 경매 아이템 목록 조회
         */
        @Query("SELECT ac FROM AuctionCompleteItem ac " +
                "JOIN FETCH ac.item item " +
                "JOIN FETCH item.category category " +
                "JOIN FETCH item.tradingMethod method " +
                "JOIN FETCH item.region region " +
                "WHERE (:category IS NULL OR category.category = :category) " +
                "AND (:method IS NULL OR method.tradingMethod = :method OR method.tradingMethod = 3) " +
                "AND (:region IS NULL OR region.region = :region) " +
                "ORDER BY item.itemId DESC")
        Slice<AuctionCompleteItem> findByCompleteItemWithLocationAndMethodAndRegion(
                @Param("category") String category,
                @Param("method") Integer method,
                @Param("region") String region,
                Pageable pageable);


        /**
         *  Redis에  NEW 및 HOT 아이템 저장
         */
        @Query("SELECT api " +
                "FROM AuctionProgressItem api " +
                "JOIN FETCH api.item i " +
                "JOIN FETCH i.category c " +
                "WHERE (i.itemId = :itemId)")
        AuctionProgressItem findByHotItemList(@Param("itemId") Long itemId);




        Item findTopByOrderByItemIdDesc();


}
