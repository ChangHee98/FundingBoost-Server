package kcs.funding.fundingboost.domain.repository;

import kcs.funding.fundingboost.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
