package tech.joes.Config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableElasticsearchRepositories(basePackages = "tech.joes.Repositories")
public class MovieConfig {

    @Bean
    public Client client() throws UnknownHostException {

        Settings elasticsearchSettings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build();

        return TransportClient.builder().settings(elasticsearchSettings).build().addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
        return new ElasticsearchTemplate(client());
    }
}
