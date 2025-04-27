package marketplace;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchBar {
    public List<Product> searchProducts(String query, List<Product> products) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(products);
        }
        
        String lowercaseQuery = query.toLowerCase().trim();
        
        return products.stream()
            .filter(product -> 
                product.getName().toLowerCase().contains(lowercaseQuery) ||
                product.getDescription().toLowerCase().contains(lowercaseQuery) ||
                (product.getCategory() != null && 
                 product.getCategory().getName().toLowerCase().contains(lowercaseQuery)))
            .collect(Collectors.toList());
    }
    
    public List<Product> filterByCategory(Category category, List<Product> products) {
        if (category == null) {
            return new ArrayList<>(products);
        }
        
        return products.stream()
            .filter(product -> product.getCategory() != null && 
                    product.getCategory().getCategoryId().equals(category.getCategoryId()))
            .collect(Collectors.toList());
    }
    
    public List<Product> filterByPriceRange(double minPrice, double maxPrice, List<Product> products) {
        return products.stream()
            .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
            .collect(Collectors.toList());
    }
    
    public List<Product> sortByPrice(List<Product> products, boolean ascending) {
        List<Product> sortedList = new ArrayList<>(products);
        if (ascending) {
            sortedList.sort((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
        } else {
            sortedList.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        }
        return sortedList;
    }
    
    public List<Product> sortByRating(List<Product> products) {
        List<Product> sortedList = new ArrayList<>(products);
        sortedList.sort((p1, p2) -> Double.compare(p2.getAverageRating(), p1.getAverageRating()));
        return sortedList;
    }
    
    @Override
    public String toString() {
        return "Search and Filter Tool";
    }
}
