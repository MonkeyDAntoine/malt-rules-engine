package fr.malt.feesrulesengine.provider.ipstack;

import fr.malt.feesrulesengine.model.Contractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class IPStackClient {
  private static IPStackClient INSTANCE = null;
  private final RestTemplate rest = new RestTemplate();
  private final UriComponentsBuilder uriBuilder;

  public IPStackClient(
      @Value("${ipstack.host}") String host, @Value("${ipstack.api-key}") String apiKey) {
    this.uriBuilder = UriComponentsBuilder.fromHttpUrl(host).queryParam("access_key", apiKey);
    setInstance(this);
  }

  public static IPStackClient getInstance() {
    return INSTANCE;
  }

  private static void setInstance(final IPStackClient client) {
    if (INSTANCE == null) {
      INSTANCE = client;
    }
  }

  public IPStackResponse locate(final Contractor contractor) {
    return rest.getForEntity(
            uriBuilder.cloneBuilder().path(contractor.getIp()).build().toUri(),
            IPStackResponse.class)
        .getBody();
  }
}
