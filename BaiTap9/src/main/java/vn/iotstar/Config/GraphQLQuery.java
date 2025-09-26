package vn.iotstar.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import vn.iotstar.Entity.Product;
import vn.iotstar.Entity.User;
import vn.iotstar.Entity.Category;
import vn.iotstar.Repository.ProductRepository;
import vn.iotstar.Repository.UserRepository;
import vn.iotstar.Repository.CategoryRepository;

@Controller
public class GraphQLQuery {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Query: lấy product sắp xếp theo giá tăng dần
    @QueryMapping
    public List<Product> productsSortedByPrice() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    // Query: lấy product theo category id
    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }
    @QueryMapping
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @QueryMapping
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }

    @QueryMapping
    public List<Product> allProducts() {
        return productRepository.findAll();
    }

}
