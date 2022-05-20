package com.mh.soc.repository;

import com.mh.soc.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query(value = "select id from rating right join book_rating on " +
            "rating.id=book_rating.rating_id where book_id=:bookId " +
            "and user_id=:userId", nativeQuery = true)
    Long getRatingId(Long userId, Long bookId);
}
