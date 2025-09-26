package vn.iotstar.Config;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import vn.iotstar.Entity.Category;
import vn.iotstar.Entity.Product;
import vn.iotstar.Entity.User;
import vn.iotstar.Repository.CategoryRepository;
import vn.iotstar.Repository.ProductRepository;
import vn.iotstar.Repository.UserRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@Transactional
public class GraphQLMutation {

    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    public GraphQLMutation(UserRepository userRepo, CategoryRepository categoryRepo, ProductRepository productRepo) {
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    // ==================== USER CRUD ====================
    @MutationMapping
    public User createUser(@Argument("input") UserInput input) {
        User u = new User();
        u.setFullname(input.fullname());
        u.setEmail(input.email());
        u.setPassword(input.password());
        u.setPhone(input.phone());
        return userRepo.save(u);
    }

    @MutationMapping
    public User updateUser(@Argument Long id, @Argument("input") UserInput input) {
        return userRepo.findById(id).map(u -> {
            u.setFullname(input.fullname());
            u.setEmail(input.email());
            u.setPassword(input.password());
            u.setPhone(input.phone());
            return userRepo.save(u);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // ==================== CATEGORY CRUD ====================
    @MutationMapping
    public Category createCategory(@Argument("input") CategoryInput input) {
        Category c = new Category();
        c.setName(input.name());
        c.setImages(input.images());
        return categoryRepo.save(c);
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument("input") CategoryInput input) {
        return categoryRepo.findById(id).map(c -> {
            c.setName(input.name());
            c.setImages(input.images());
            return categoryRepo.save(c);
        }).orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // ==================== PRODUCT CRUD ====================
    @MutationMapping
    public Product createProduct(@Argument("input") ProductInput input) {
        Product p = new Product();
        p.setTitle(input.title());
        p.setQuantity(input.quantity());
        p.setDesc(input.desc());
        p.setPrice(BigDecimal.valueOf(input.price()));

        if (input.ownerId() != null) {
            userRepo.findById(input.ownerId()).ifPresent(p::setOwner);
        }

        if (input.categoryIds() != null && !input.categoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepo.findAllById(input.categoryIds()));
            p.setCategories(categories);
        }

        return productRepo.save(p);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument("input") ProductInput input) {
        Optional<Product> opt = productRepo.findById(id);
        if (opt.isEmpty()) throw new RuntimeException("Product not found with id " + id);

        Product p = opt.get();
        p.setTitle(input.title());
        p.setQuantity(input.quantity());
        p.setDesc(input.desc());
        p.setPrice(BigDecimal.valueOf(input.price()));

        if (input.ownerId() != null) {
            userRepo.findById(input.ownerId()).ifPresent(p::setOwner);
        }

        if (input.categoryIds() != null) {
            Set<Category> categories = new HashSet<>(categoryRepo.findAllById(input.categoryIds()));
            p.setCategories(categories);
        }

        return productRepo.save(p);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // ==================== INPUT CLASSES ====================
    public record UserInput(String fullname, String email, String password, String phone) {}
    public record CategoryInput(String name, String images) {}
    public record ProductInput(String title, Integer quantity, String desc, Double price, Long ownerId, Set<Long> categoryIds) {}
}
