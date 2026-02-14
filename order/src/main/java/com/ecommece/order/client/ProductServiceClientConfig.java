package com.ecommece.order.client;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ProductServiceClientConfig {

    @Bean
    public ProductServiceClient restClientInterfaceProduct(@Qualifier("loadBalancedRestClientBuilder") RestClient.Builder builder){
        RestClient restClient = builder
                .baseUrl("http://product-service").build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                                            .builderFor(restClientAdapter).build();

        return factory.createClient(ProductServiceClient.class);
    }

}
