package server;

import java.util.List;

public interface ProductServiceInterface {
    // public abstract 생략 가능하다.
    void 상품등록(String name, int price, int qty);
    List<Product> 상품목록();
    Product 상품상세(int id);
    Product 상품삭제(int id);
}
