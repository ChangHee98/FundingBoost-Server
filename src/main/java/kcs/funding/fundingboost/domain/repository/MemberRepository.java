package kcs.funding.fundingboost.domain.repository;

import java.util.Optional;
import kcs.funding.fundingboost.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickName(String nickName);

    Optional<Member> findByEmail(String email);
}
