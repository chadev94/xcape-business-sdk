package repository;

import domain.entity.Account;
import domain.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    List<Merchant> findAll();

    List<Merchant> findMerchantsByAccount(Account account);

    Merchant findByName(String name);

}