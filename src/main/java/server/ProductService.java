package server;

import java.util.ArrayList;
import java.util.List;

public class ProductService implements ProductServiceInterface {

    private final ProductRepository repo = new ProductRepository();


    private int sequence = 1;
    @Override
    public void 상품등록(String name, int price, int qty) {
        repo.insert(name, price, qty);
    }

    @Override
    public List<Product> 상품목록() {
        return repo.findAll();
    }

    @Override
    public Product 상품상세(int id) {
        Product p = repo.findById(id);
        if (p == null){
            throw new RuntimeException("id가 없습니다.");
        }
        return p;
    }

    @Override
    public Product 상품삭제(int id) {
        Product p = repo.findById(id);
        if (p == null){
            throw new RuntimeException("id가 없습니다.");
        }

        int affected = repo.deleteById(id);
        if (affected == 0){
            throw new RuntimeException("id가 없습니다.");
        }
        return p;
    }
}
