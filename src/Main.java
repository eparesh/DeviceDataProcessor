
import device.processor.*;

public class Main {
    public static DeviceDataParser deviceDataParser;

    public static void main(String[] args) {
        //test();
        String path = "";
        if(args.length > 0)    {
            path=args[0];
        }
        else{
            System.out.println("Please provide valid inout path");
        }

        deviceDataParser = new DeviceDataParser();
        deviceDataParser.run(path);
    }
}
