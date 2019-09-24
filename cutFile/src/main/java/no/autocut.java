package no;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class autocut {
    public static void main(String[] args) {
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Data");

        //This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[] {1, "title", "Tieu de."});
        try {
            //1: create object, stream and link data source.
            File f1 = new File("/home/khacngoc/Documents/filemoodle/TranslationMoodle/vi/competency.php");
            FileReader fr1 = new FileReader(f1);

            //2: Read file.
            BufferedReader br1 = new BufferedReader(fr1);

            String line;
            int k=1;
            while ((line = br1.readLine()) != null){
                String name=null;
                String vi=null;
                String en=null;
                if(line.contains("$string[")) {
                    int root = line.indexOf("=");
                    String check;
                    name = line.substring(0, root).trim();
                    vi = line.substring(root+1).trim();
                    if(!line.endsWith(";")){
                        do{
                            check=br1.readLine();
                            vi=vi+"\n"+check;
                        }
                        while(!check.endsWith(";"));
                    }
                    System.out.println(vi);
                    if(name!=null) {
                        String line1 = null;
                        File f2 = new File("/home/khacngoc/Documents/filemoodle/langmoodle/en/competency.php");
                        FileReader fr2 = new FileReader(f2);
                        BufferedReader br2 = new BufferedReader(fr2);
                        while ((line1 = br2.readLine()) != null) {
                            if(line1.contains(name)){
                                String check1;
                                int root1=line1.indexOf("=");
                                en=line1.substring(root1+1).trim();
                                if(!line1.endsWith(";")){
                                    do{
                                        check1=br2.readLine();
                                        en=en+"\n"+check1;
                                    }
                                    while(!check1.endsWith(";"));
                                }
                                fr2.close();
                                br2.close();
                                break;
                            }
                        }
                    }
                }

                if(vi!=null) {
                    data.put(Integer.toString(k), new Object[]{k, en, vi});
                    k++;
                }
            }
            //3: close stream.
            fr1.close();

            br1.close();

        } catch (Exception ex) {
            System.out.println("Loi doc file: "+ex.getCause());
        }

        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
                Cell cell = row.createCell(cellnum++);
                if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }
        try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("name.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("name.xlsx written successfully on disk.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
