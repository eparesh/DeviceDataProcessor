package device.processor;

import device.data.DeviceEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class DeviceDataParser {

    List<DeviceEvent> events;

    public void run(String filePath) {

        File directoryPath = new File(filePath);
        //List of all files and directories
        File filesList[] = directoryPath.listFiles();
        System.out.println("List of files and directories in the specified directory:");
        events = new ArrayList<>();
        for (File file : filesList) {
            System.out.println("File name: " + file.getName());
            if (isValidFile(file.getName())) {
                inputProcessor(file);
            }
            System.out.println("File path: " + file.getAbsolutePath());
            System.out.println("Size :" + file.getTotalSpace());


        }
        System.out.println("Start: Aggregation ");
        Map<String, Map<String, Long>> eventsByIdandType = events.stream()
                .collect(Collectors.groupingBy(DeviceEvent::getDevice_id, Collectors.groupingBy(DeviceEvent::getEvent_type, Collectors.counting())));
        eventsByIdandType.entrySet().stream()
                .forEach(device -> {
                    String minEventType = device.getValue().entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
                    String maxEventType = device.getValue().entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
                    Long minValue = device.getValue().entrySet().stream().min(Map.Entry.comparingByValue()).get().getValue();
                    Long maxValue = device.getValue().entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
                    Map.Entry<String, Long> squirrelCount = device.getValue().entrySet().stream().filter(a -> a.getKey().equals("squirrel")).findAny().orElse(null);
                    StringBuffer sb = new StringBuffer();
                    sb.append("DeviceID: ");
                    sb.append(device.getKey());
                    sb.append(" Event type wth Minimum count:");
                    sb.append(minEventType);
                    sb.append(" Maximum Count:");
                    sb.append(minValue);
                    sb.append(" Minimum type wth Minimum count:");
                    sb.append(maxEventType);
                    sb.append(" Maximum Count:");
                    sb.append(maxValue);
                    if (squirrelCount != null) {
                        sb.append(" squirrel Count:");
                        sb.append(squirrelCount.getValue());
                    }
                    System.out.println(sb.toString());
                    //System.out.println("DeviceID:" + device.getKey() + "Minimum Count:" + minValue + " Maximum Count: " + maxValue);
                });

        System.out.println("End: Aggregation ");
    }

    public boolean isValidFile(String fileName) {
        boolean result = false;
        final String regex = "ev_dump_.*.csv";
        //final String fileName = "ev_dump_2.csv";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(fileName);

        while (matcher.find()) {
            result = true;
        }

        return result;
    }

    public void inputProcessor(File file) {
        String delimiter = ",";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String[] tempArr;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                tempArr = line.split(delimiter);
                try {
                    DeviceEvent event = processRow(tempArr);
                    events.add(event);
                } catch (Exception ex) {
                    String message = String.format("Exception: in parsing a row. Ignoring it. Raw data {0} {1}", line, ex);
                    System.out.println(message);
                }
            }
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private DeviceEvent processRow(String[] tempArr) {
        DeviceEvent event = new DeviceEvent();
        for (int i = 0; i < tempArr.length; i++) {
            event.setTimestamp(convertToTimeStamp(tempArr[0]));
            event.setDevice_id(tempArr[1]);
            event.setEvent_type(tempArr[2]);
            event.setEvent_payload(tempArr[3]);
        }
        return event;
    }

    private Double convertToTimeStamp(String rawTimeStamp) {
        return Double.parseDouble(rawTimeStamp);
    }
}
