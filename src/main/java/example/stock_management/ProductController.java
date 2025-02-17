package example.stock_management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.security.Principal;



import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @GetMapping("/{requestedId}")
    private ResponseEntity<Product> findById(@PathVariable Long requestedId,
                                             Principal principal) {
        Optional<Product> productOptional = Optional.ofNullable(productRepository.findByIdAndOwner(requestedId,
                principal.getName()));
        return productOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    private ResponseEntity<Void> createProduct(@RequestBody Product newProductRequest,
                                               UriComponentsBuilder ucb,
                                               Principal principal) {
        Product productWithOwner = new Product(null,
                newProductRequest.getName(),
                newProductRequest.getPrice(),
                principal.getName());
        Product savedProduct = productRepository.save(productWithOwner);
        URI locationOfNewProduct = ucb
                .path("products/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewProduct).build();
    }

    @GetMapping()
    private ResponseEntity<Iterable<Product>> findAll(Pageable pageable, Principal principal) {
        Page<Product> page = productRepository.findByOwner(principal.getName(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSort()
                ));
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId,
                                             @RequestBody Product productUpdate,
                                             Principal principal) {
        Product product = productRepository.findByIdAndOwner(requestedId, principal.getName());
        if (product != null) {
            Product updatedProduct = new Product(product.getId(), product.getName(), productUpdate.getPrice(), principal.getName());
            productRepository.save(updatedProduct);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteProduct(@PathVariable Long id,
                                               Principal principal) {
        if (!productRepository.existsByIdAndOwner(id, principal.getName())) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
