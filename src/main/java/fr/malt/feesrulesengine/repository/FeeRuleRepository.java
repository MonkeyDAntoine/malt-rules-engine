package fr.malt.feesrulesengine.repository;

import fr.malt.feesrulesengine.model.FeeRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRuleRepository
    extends MongoRepository<FeeRule, String>, PagingAndSortingRepository<FeeRule, String> {}
