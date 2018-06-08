package ddq.model;

public class Global {

	public static final ThreadLocal<Integer> userlogid =new ThreadLocal<Integer>() {  
        public Integer initialValue() {  
            return 0;  
        }  
    };
    
    public static ThreadLocal<Integer> userlogid1 =new ThreadLocal<Integer>() {  
        public Integer initialValue() {  
            return 0;  
        }  
    };
}
