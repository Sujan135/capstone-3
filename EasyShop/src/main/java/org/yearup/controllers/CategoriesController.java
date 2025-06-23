package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")  // http://localhost:8080/categories
@CrossOrigin
public class CategoriesController {
    private CategoryDao categoryDao;
    private ProductDao productDao;


    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }
    // add the appropriate annotation for a get action
    @GetMapping()
    public List<Category> getAll(@RequestParam(required = false)String name) {
        List<Category> categories = categoryDao.getAllCategories();

        if(name != null) {
            return categories.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        }

        // find and return all categories
        return categories;
    }

    // add the appropriate annotation for a get action
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Category getById(@PathVariable int id) {

        // get the category by id
        return categoryDao.getById(id);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
                // get a list of product by categoryId
        return productDao.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping
    public Category addCategory(@RequestBody Category category) {
        categoryDao.create(category);
        // insert the category
        return category;
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        category.setCategoryId(id);
        categoryDao.update(category);
        // update the category by id

        return category;
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable int id)
    {
        categoryDao.delete(id);
        // delete the category by id;
    }
}
