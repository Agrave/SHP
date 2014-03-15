
package shp;

import java.io.File;
import java.io.IOException;
import shp.parser.Handler;
import shp.parser.Parser;


public class SHP {


    public static void main(String[] args) throws IOException {
        String fileName1 = "rus_dmitry.htm";
        File file = new File(fileName1);
        Handler handler = new HandlerImpl();
        Parser parser = new Parser();
        parser.parse(file, handler);


    }
}
