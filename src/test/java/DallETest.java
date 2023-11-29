import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DallETest {
    class DallEMock implements IDallE{
        @Override 
        public String createImage(String p) throws IOException, InterruptedException, URISyntaxException {
            return "Fake API: This is a fake image";
        }

    }
    class DallEMock2 implements IDallE{
        @Override 
        public String createImage(String p) throws IOException, InterruptedException, URISyntaxException {
            throw new IOException("Fake API exception");
        }

    }

    private IDallE fakeDallE; 
    private IDallE fakeDallE2; 
    private boolean real = false; 

    @BeforeEach
    public void setup(){
        fakeDallE = new DallEMock();
        fakeDallE2 = new DallEMock2();
    }

    public String createImage(String p) throws IOException, InterruptedException, URISyntaxException{
        if (real){
            return fakeDallE.createImage(p);
        }
        else 
        return fakeDallE2.createImage(p);
    }

    @Test 
    public void testCreateImageSuccess() throws Exception{
        real = true; 
        String response = createImage("test");
        assertEquals(response, "Fake API: This is a fake image");

    }

    @Test 
    public void testCreateImageException() throws Exception{
        Exception exception = assertThrows(IOException.class, () -> {
            createImage("test");
        });

        // You can add more specific assertions on the exception if needed
        assertTrue(exception.getMessage().contains("Fake API exception"));
    }
    
}
