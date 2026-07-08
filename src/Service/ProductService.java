package Service;

import Model.Invoice;
import Model.InvoiceItem;
import Model.Product;
import Model.ProductCatalog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductService {

    private ProductService() {
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    public static ProductCatalog loadProducts() {
        ObjectMapper mapper = createObjectMapper();
        File file = new File("database/products.json");
        if (!file.exists())
            return new ProductCatalog();
        try {
            ProductCatalog catalog = mapper.readValue(file, ProductCatalog.class);
            return catalog != null ? catalog : new ProductCatalog();
        } catch (IOException e) {
            e.printStackTrace();
            return new ProductCatalog();
        }
    }

    public static void saveProducts(ProductCatalog catalog) {
        ObjectMapper mapper = createObjectMapper();
        File file = new File("database/products.json");
        file.getParentFile().mkdirs();
        try {
            mapper.writeValue(file, catalog);
            System.out.println("Products saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateProductId(ProductCatalog catalog) {
        int max = 0;
        if (catalog != null)
            for (Product p : catalog.getProducts()) {
                if (p.getId() != null && p.getId().startsWith("PRD_")) {
                    try {
                        int num = Integer.parseInt(p.getId().substring(4));
                        if (num > max)
                            max = num;
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        return "PRD_" + String.format("%06d", max + 1);
    }
}
