package Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Tool {




        public static Map<String,ArrayList> getCollageToMajorMap() throws IOException {
            String data = getData("http://jwzx.cqupt.edu.cn/jwzxtmp/pubBjsearch.php?action=bjStu");
            Pattern pattern = Pattern.compile("<tr><td>(.*?)</td>");
            Matcher matcher = pattern.matcher(data);
            int i = 0;
            ArrayList collage = getCollageList();
            Map<String, ArrayList> collageToMajor = new LinkedHashMap();
            for (String string:getCollageList()){
                collageToMajor.put(string,new ArrayList<String>());
            }
            while (matcher.find()) {
                String temp = matcher.group(1);
                if (temp.equals("专业")) {
                    i++;
                    continue;
                }
                collageToMajor.get(collage.get(i-1)).add(matcher.group(1));
            }
            return collageToMajor;
        }

        public static ArrayList<String> getCollageList() throws IOException {
            String data = getData("http://jwzx.cqupt.edu.cn/jwzxtmp/pubBjsearch.php?action=bjStu");
            ArrayList<String> collage = new ArrayList<>();
            Pattern pattern = Pattern.compile("<h3>(.*?)</h3>");
            Matcher matcher = pattern.matcher(data);
            while (matcher.find()) {
                collage.add(matcher.group(1));
            }
            return collage;

        }

        public static String getData(String strUrl) throws IOException {
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream(), "UTF-8"
                    )
            );
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            connection.disconnect();
            return builder.toString();
        }

        public static Map<String, ArrayList<String>> getClassList(String ClassID) throws IOException {
            URL url = new URL("http://jwzx.cqupt.edu.cn/jwzxtmp/showBjStu.php?bj=" + ClassID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream(), "UTF-8"
                    )
            );
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            connection.disconnect();
            String PageData = builder.toString();
            Pattern pattern=Pattern.compile("<tr><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td><td>(.*?)</td>");
            Matcher matcher = pattern.matcher(PageData);
            Map<String, ArrayList<String>> students = new LinkedHashMap<>();
            while (matcher.find()) {
                if(matcher.group(1).equals("No."))
                    continue;
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    if (i==3)
                        continue;
                    temp.add(matcher.group(i));
                }
                students.put(matcher.group(3), temp);
            }
            return students;
        }

        public static Map<String,List<String>> getMajorToClassList() throws IOException {
            String data = getData("http://jwzx.cqupt.edu.cn/jwzxtmp/pubBjsearch.php?action=bjStu");
            Pattern pattern = Pattern.compile("<tr><td>(.*?)</td>(.*?)</tr>");
            Matcher matcher = pattern.matcher(data);
            Map<String, List<String>> clazzes = new HashMap<>();
            while (matcher.find()) {
                Pattern p = Pattern.compile("showBjStu\\.php\\?bj\\=(.*?)\\'");
                Matcher m = p.matcher(matcher.group(2));
                List<String> c = new ArrayList<>();
                while (m.find()) {
                    c.add(m.group(1));
                }
                clazzes.put(matcher.group(1), c);
            }
            return clazzes;
        }
        public static void main(String[] args) throws IOException {
        Map<String,List<String>> collegeInfo = getMajorToClassList();
        String majorStr = "微电子实验与科学专业";
        List<String> classIds = collegeInfo.get(majorStr);
        for (String classId :classIds){
            Map<String, ArrayList<String>> studentInfo =getClassList(classId);
            for (Map.Entry<String, ArrayList<String>> entry:studentInfo.entrySet()){
                String stuName =entry.getKey();
                List<String> personInfo=entry.getValue();
                Map<String,String>stuInfo=  new HashMap<>();
                stuInfo.put("name",stuName);
                stuInfo.put("stuId",personInfo.get(1));
                stuInfo.put("gender",personInfo.get(2));
                students.add(stuInfo);


            }
            clazzes.put( classId,students);
        }
    }

}

