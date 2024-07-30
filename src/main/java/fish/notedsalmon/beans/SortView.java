package fish.notedsalmon.beans;

import fish.notedsalmon.dao.Product;
import fish.notedsalmon.services.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
@Named("sortAndTest")
@ViewScoped
public class SortView implements Serializable {
    private List<Product> products3;
    private List<SortMeta> sortBy;

    @Inject
    private ProductService service;

    @PostConstruct
    public void init() {
        products3 = service.getProducts(50);

        sortBy = new ArrayList<>();
        sortBy.add(SortMeta.builder()
                .field("name")
                .order(SortOrder.ASCENDING)
                .build());

        sortBy.add(SortMeta.builder()
                .field("category")
                .order(SortOrder.ASCENDING)
                .priority(1)
                .build());
    }

    public List<Product> getProducts3() {
        return products3;
    }

    public void setService(ProductService service) {
        this.service = service;
    }
}
 */