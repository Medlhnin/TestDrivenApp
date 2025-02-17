package example.stock_management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ProductRepository extends
        CrudRepository<Product, Long>,
        PagingAndSortingRepository<Product, Long> {

    Product findByIdAndOwner(Long id, String owner);
    Page<Product> findByOwner(String owner, PageRequest pageRequest);
    boolean existsByIdAndOwner(Long id, String owner);
}
