package client;

import com.google.gson.Gson;
import dto.RequestDTO;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MyClient {
    public static void main(String[] args) {
        try {
            // 1. Socket 연결 완료
            Socket socket = new Socket("localhost", 20000);

            // 2. 키보드 입력 버퍼
            InputStream keyStream = System.in;
            InputStreamReader keyReader = new InputStreamReader(keyStream);
            BufferedReader keyBuf = new BufferedReader(keyReader);

            // 3. BW 버퍼
            OutputStream out = socket.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(out);
            BufferedWriter bw = new BufferedWriter(ow);

            // 4. BR 버퍼
            InputStream in = socket.getInputStream();
            InputStreamReader ir = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(ir);

            // 5. 통신 시작
            Gson gson = new Gson();
            while(true){
                // 요청
                System.out.println("1. 상품등록");
                System.out.println("2. 상품삭제");
                System.out.println("3. 상품 상세보기");
                System.out.println("4. 상품 전체보기");
                System.out.println("5. 종료");
                System.out.println("원하는 번호를 입력해 주세요");

                // 값 받기
                String number = keyBuf.readLine();

                // 1. 상품등록
                // 요청
                if ("1".equals(number)){
                    System.out.println("상품명을 입력하세요: ");
                    String name = keyBuf.readLine();

                    System.out.println("가격을 입력하세요: ");
                    int price = Integer.parseInt(keyBuf.readLine());

                    System.out.println("수량을 입력하세요: ");
                    int qty = Integer.parseInt(keyBuf.readLine());

                    RequestDTO req = new RequestDTO();
                    req.method ="post";
                    req.querystring = null;

                    Map<String, Object> body = new HashMap<>();
                    body.put("name", name);
                    body.put("price", price);
                    body.put("qty", qty);

                    req.body = body;

                    String json = gson.toJson(req);

                    // System.out.println("보내는 JSON → " + json); // 콘솔로 확인하기

                    bw.write(json);
                    bw.write("\n");
                    bw.flush();

                    String line = br.readLine();
                    System.out.println("서버 응답: " + line);

                }

                // 2. 상품삭제
                else if ("2".equals(number)) {
                    System.out.println("삭제할 상품의 id를 입력하세요: ");
                    int id = Integer.parseInt(keyBuf.readLine());

                    RequestDTO req = new RequestDTO();
                    req.method = "delete";

                    Map<String,Integer> qs = new HashMap<>();
                    qs.put("id", id);

                    req.querystring = qs;
                    req.body = null;

                    String json = gson.toJson(req);

                    //System.out.println("보내는 JSON → " + json); // 콘솔로 확인하기

                    bw.write(json);
                    bw.write("\n");
                    bw.flush();

                    String line = br.readLine();
                    System.out.println("서버 응답: " + line);
                }

                // 3. 상품상세
                else if ("3".equals(number)) {
                    System.out.println("조회할 상품 id를 입력하세요: ");
                    int id = Integer.parseInt(keyBuf.readLine());

                    RequestDTO req = new RequestDTO();
                    req.method ="get";

                    Map<String, Integer> qs = new HashMap<>();
                    qs.put("id", id);

                    req.querystring = qs;
                    req.body = null;

                    String json = gson.toJson(req);

                    //System.out.println("보내는 JSON → " + json); // 콘솔로 확인하기

                    bw.write(json);
                    bw.write("\n");
                    bw.flush();

                    String line = br.readLine();
                    System.out.println("서버 응답: " + line);
                }

                // 4. 전체상품
                else if ("4".equals(number)) {

                    RequestDTO req = new RequestDTO();
                    req.method ="get";
                    req.querystring = null;
                    req.body = null;

                    String json = gson.toJson(req);

                    //System.out.println("보내는 JSON → " + json); // 콘솔로 확인하기

                    bw.write(json);
                    bw.write("\n");
                    bw.flush();

                    String line = br.readLine();
                    System.out.println("서버 응답: " + line);
                }

                // 5. 종료
                else if ("5".equals(number)) {
                    bw.write("exit\n");
                    bw.flush();
                    System.out.println("클라이언트를 종료합니다");
                    break;
                }

                // 잘못 입력
                else{
                    System.out.println("잘못된 번호입니다. 다시 입력하세요.");
                }

                System.out.println();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
