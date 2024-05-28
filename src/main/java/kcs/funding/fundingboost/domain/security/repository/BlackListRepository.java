package kcs.funding.fundingboost.domain.security.repository;

import kcs.funding.fundingboost.domain.security.entity.BlackList;
import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
