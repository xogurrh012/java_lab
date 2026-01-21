package server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.RequestDTO;
import dto.ResponseDTO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class MyServer {
    public static void main(String[] args) {
        try {
            // 1. 20000번 포트에서 대기중..
            ServerSocket ss = new ServerSocket(20000);
            //System.out.println("대기중..");
            Socket socket = ss.accept();
            //System.out.println("접속됨");

            // 2. 새로운 소켓에 버퍼달기 (BR, BW)
            InputStream in = socket.getInputStream();
            InputStreamReader ir = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(ir);

            OutputStream out = socket.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(out);
            BufferedWriter bw = new BufferedWriter(ow);

            // 서버 시작
            Gson gson = new GsonBuilder()
                    .serializeNulls() // null이여도 json으로 보여주기 위함
                    .create();

            ProductServiceInterface service = new ProductService();

            while(true){
                // 1. 클라이언트부터 받은 메세지
                String line = br.readLine(); // 엔터키까지 읽기
                RequestDTO req;

                if (line == null || "exit".equals(line)) {
                    System.out.println("클라이언트 종료");
                    break;
                }

                try {
                    req = gson.fromJson(line, RequestDTO.class);
                }catch (Exception e) {
                    bw.write("{\"msg\":\"잘못된 JSON\",\"body\":null}\n");
                    bw.flush();
                    continue;
                }



                // 확인하기
                boolean isCreate = "post".equals(req.method) && req.querystring == null && req.body != null;
                boolean isDelete = "delete".equals(req.method) && req.querystring != null && req.querystring.containsKey("id") && req.body == null;
                boolean isDetail = "get".equals(req.method) && req.querystring != null && req.querystring.containsKey("id") && req.body == null;
                boolean isList = "get".equals(req.method) && req.querystring == null && req.body == null;

                // 생성
                String resJson;
                if (isCreate) {
                    try {
                        String name = (String) req.body.get("name");
                        int price = ((Number) req.body.get("price")).intValue();
                        int qty   = ((Number) req.body.get("qty")).intValue();

                        service.상품등록(name, price, qty);

                        ResponseDTO<Void> res = new ResponseDTO<>("ok", null);
                        resJson = gson.toJson(res);

                    }catch (RuntimeException e){
                        ResponseDTO<Void> res = new ResponseDTO<>(e.getMessage(),null);
                        resJson = gson.toJson(res);
                    }
                    bw.write(resJson + "\n");
                    bw.flush();
                    continue;

                // 삭제
                } else if (isDelete) {
                    try {
                        int id = req.querystring.get("id");
                        service.상품삭제(id);

                        ResponseDTO<Void> res = new ResponseDTO<>("ok",null);
                        resJson = gson.toJson(res);

                    }catch (RuntimeException e) {
                        ResponseDTO<Void> res = new ResponseDTO<>(e.getMessage(),null);
                        resJson = gson.toJson(res);
                    }

                    bw.write(resJson + "\n");
                    bw.flush();
                    continue;

                // 상세
                } else if (isDetail) {
                    try {
                        int id = req.querystring.get("id");
                        Product product = service.상품상세(id);

                        ResponseDTO<Product> res = new ResponseDTO<>("ok", product);
                        resJson = gson.toJson(res);

                    } catch (RuntimeException e) {
                        ResponseDTO<Void> res = new ResponseDTO<>(e.getMessage(), null);
                        resJson = gson.toJson(res);
                    }

                    bw.write(resJson + "\n");
                    bw.flush();
                    continue;

                // 전체
                } else if (isList) {
                    try {
                        List<Product> products = service.상품목록();
                        ResponseDTO<List<Product>> res = new ResponseDTO<>("ok",products);
                        resJson = gson.toJson(res);

                    }catch (RuntimeException e) {
                        ResponseDTO<Void> res = new ResponseDTO<>(e.getMessage(),null);
                        resJson = gson.toJson(res);
                    }

                    bw.write(resJson + "\n");
                    bw.flush();
                    continue;

                // 그외
                } else {
                    ResponseDTO<Void> res = new ResponseDTO<>("잘못된 요청입니다", null);
                    resJson = gson.toJson(res);
                    bw.write(resJson + "\n");
                    bw.flush();
                    continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
