package vn.iotstar.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.iotstar.Entity.Category;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long>{

}
