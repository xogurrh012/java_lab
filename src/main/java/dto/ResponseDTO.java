package dto;

public class ResponseDTO<T> {
    public String msg;
    public T body;

    public ResponseDTO(String msg, T body){
        this.msg = msg;
        this.body = body;
    }
}
