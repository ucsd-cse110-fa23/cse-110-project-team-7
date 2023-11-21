import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.*;
import java.net.*;
import org.json.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WhisperTest {

    class WhisperMock implements IWhisper{

        @Override
        public String display() throws IOException, URISyntaxException {
            return "Fake transcription result: This is a transcription result for testing.";
        }

        

    }

    class WhisperMock2 implements IWhisper{
        @Override
        public String display() throws IOException, URISyntaxException {
            throw new IOException("Fake transcription result exception");
        }
    }

    private IWhisper fakeWhisper;
    private IWhisper fakeWhisper2;

    @BeforeEach
    public void setup() {
        // Create a fake API instance with the mock strategy
        fakeWhisper = new WhisperMock();
        fakeWhisper2 = new WhisperMock2();
    }

    @Test
    public void testDisplay() throws Exception {
        // Call the method as if it were the real API

        String response = fakeWhisper.display();

        // Assert the response
        assertEquals(response, "Fake transcription result: This is a transcription result for testing.");
    }

    @Test
    public void testGetCookingInstructionException() throws Exception {
       Exception exception = assertThrows(IOException.class, () -> {
            fakeWhisper2.display();
        });

        assertTrue(exception.getMessage().contains("Fake transcription result exception"));
    
    }

}
