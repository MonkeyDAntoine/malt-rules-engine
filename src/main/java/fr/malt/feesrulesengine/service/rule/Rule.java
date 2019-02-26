package fr.malt.feesrulesengine.service.rule;

import com.fasterxml.jackson.databind.JsonNode;
import fr.malt.feesrulesengine.model.Contract;

public interface Rule {
    boolean evaluate(final Contract contract, final JsonNode node);
}
