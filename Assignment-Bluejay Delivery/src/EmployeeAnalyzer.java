import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class EmployeeAnalyzer {
    public static void main(String[] args) {

        try{


            //input file
            String inputFile = "C:\\Assignment-Bluejay Delivery\\src\\File\\Assignment.csv";
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            //read csv file
            List<Employee> employees = readFromFile(br);

            //find  employees who has worked for 7 consecutive days.
            employeeWhoWork7ConsecutiveDays(employees);
            
            // who have less than 10 hours of time between shifts but greater than 1 hour
            try(BufferedWriter writer3 = new BufferedWriter(new FileWriter("C:\\Assignment-Bluejay Delivery\\src\\File\\output3.txt"))){
                writer3.write("\n"+"Employee who have less than 10 hours of time between shifts but greater than 1 hour"+"\n \n");
                for (int i = 0; i < employees.size() - 1; i++) {
                    Employee currEmp = employees.get(i);
                    Employee nextEmp = employees.get(i + 1);

                    if (hasLessThan10HoursBetweenShifts(currEmp, nextEmp)) {
                        String output = "Employee Name: " + currEmp.getName() +
                                ", Position: " + currEmp.getPosition()+"\n";
                        //System.out.println(output);
                        writer3.write(output);


                    }
                }
            }catch (Exception ex){

            }
            //Who has worked for more than 14 hours in a single shift
            try(BufferedWriter writer2 = new BufferedWriter(new FileWriter("C:\\Assignment-Bluejay Delivery\\src\\File\\output2.txt"))){
                writer2.write("\n"+"Employee who has worked for more than 14 hours in a single shift"+"\n \n");
                for (Employee record : employees) {
                    if (hasWorkedMoreThan14HoursInAShift(record)) {
                        String output = "Employee Name: " + record.getName() +
                                ", Position: " + record.getPosition();
                        //System.out.println("3     "+output);
                        writer2.write(output);
                        // writer.newLine();
                    }
                }

            }catch (Exception exc){

            }
            br.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean hasWorkedMoreThan14HoursInAShift(Employee record) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        try {
            // Parse the time in and time out of the record
            Date timeIn = record.getStartTime();
            Date timeOut = record.getEndTime();

            // Calculate the time difference in hours
            long timeDifferenceInHours = (timeOut.getTime() - timeIn.getTime()) / (60 * 60 * 1000);

            // Check if the time difference is more than 14 hours
            return timeDifferenceInHours > 14;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean hasLessThan10HoursBetweenShifts(Employee currentEmp, Employee nextEmp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        try {
            // Parse the time out of the current record and time in of the next record
            Date currentTimeOut = currentEmp.getEndTime();
            Date nextTimeIn = nextEmp.getStartTime();

            // Calculate the time difference in hours
            long timeDifferenceInHours = (nextTimeIn.getTime() - currentTimeOut.getTime()) / (60 * 60 * 1000);

            // Check if the time difference is less than 10 hours but greater than 1 hour
            return (timeDifferenceInHours > 1 && timeDifferenceInHours < 10);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static List<Employee> readFromFile(BufferedReader br) throws IOException, ParseException {
        List<Employee> employees = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String line = "";
        int c = 0;
        while((line = br.readLine()) != null){
            c++;
            if(c == 1) continue;
            String data[]  = line.split(",");
            String name = data[7].trim();
            String position = data[0].trim();
            Date timeIn = data[2].trim() == "" ? dateFormat.parse("00/00/0000 00:00 PM") : dateFormat.parse(data[2].trim());
            Date timeOut = data[3].trim() == "" ? dateFormat.parse("00/00/0000 00:00 PM") : dateFormat.parse(data[3].trim());
            employees.add(new Employee(name,position,timeIn,timeOut));
        }
        return employees;
    }

    private static void employeeWhoWork7ConsecutiveDays(List<Employee> employees) throws IOException {
        int count = 0;
        HashMap<String, String> consecutive7Days = new HashMap<>();

        // work for 7 consecutive days employee
        for (int i = 0; i < employees.size() - 1; i++) {
            Employee currentEmployee = employees.get(i);
            Employee nextEmployee = employees.get(i + 1);
            int diffInDays = (int) ((nextEmployee.getStartTime().getTime() - currentEmployee.getStartTime().getTime())/(24 * 60 * 60 * 1000));
            //System.out.println(diffInDays);
            if(currentEmployee.getName().equals(nextEmployee.getName()) && diffInDays !=0){
                count = 0;
            }else {
                count++;
                if(count >= 7){
                    consecutive7Days.put(currentEmployee.getName(), currentEmployee.getPosition());
                }
            }
        }

        // write data on output.txt file for 7 consecutive days
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Assignment-Bluejay Delivery\\src\\File\\output1.txt"));
        ){
            writer.write("\n"+"Employee who work for 7 consecutive days"+"\n \n");
            List<String> output = new ArrayList<>();
            // print all employee whao has work for 7 consecutive days
            for(String name : consecutive7Days.keySet()){
                String res = "Employee name "+name+"    "+"Position "+ consecutive7Days.get(name);
                output.add("Employee name "+name+ "       "+"Position " + consecutive7Days.get(name)+"\n");
                writer.write(res);
                writer.newLine();

                //System.out.println(name+ "       " + consecutive7Days.get(name));


            }


        }catch(Exception e){
            e.printStackTrace();
        }


    }



}