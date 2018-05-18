package miinaharava;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import miinaharava.ui.MiinaharavaUI;

public class Main {
    
    public static void main(String[] args) throws URISyntaxException, MalformedURLException, IOException {
        
        javafx.application.Application.launch(MiinaharavaUI.class);
    }
}
