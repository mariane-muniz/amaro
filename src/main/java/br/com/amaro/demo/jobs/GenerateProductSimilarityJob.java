package br.com.amaro.demo.jobs;

import br.com.amaro.demo.entities.Product;
import br.com.amaro.demo.services.ProductService;
import br.com.amaro.demo.services.ProductSimilarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Job responsible for generating the similarities between the products
 *
 * @author Mariane Muniz
 * @version 1.0.0
 */
@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional(propagation= Propagation.REQUIRED, noRollbackFor=Exception.class)
public class GenerateProductSimilarityJob {
    @Value("${job.similarity.product.quantity}")
    private int quantity;
    private final ProductService productService;
    private final ProductSimilarService productSimilarService;

    /**
     * Asynchronous method that is executed every two minutes to generate the similarities between the products.
     */
    @Async
    @Scheduled(fixedDelay = 2000L)
    public void execute () {
        final Pageable pageable = PageRequest.of(0, this.quantity);
        final List<Product> productWithoutSimilarityList = productService.findWithoutSimilarity(pageable);

        if (!CollectionUtils.isEmpty(productWithoutSimilarityList)) {
            productWithoutSimilarityList.forEach(product -> {
                this.productSimilarService.generateSimilarity(product);
                product.setWithSimilarity(true);
            });
            this.productService.persistInDatabase(productWithoutSimilarityList);
        }
    }
}