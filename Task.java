import java.util.TreeMap;
import java.io.File;
import java.io.BufferedReader;
import java.util.Set;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;

public class Task {
    
    /*
    Encapsulated multiset implementation
    Underlying data structure is TreeMap
    */
    public static class Multiset<A> {
        TreeMap<A,Integer> data;
        public Multiset() {
            data = new TreeMap<A,Integer>();
        }

        //Add element of generic type to TreeMap
        public void add(A a) {
            if (data.containsKey(a)) {
                data.put(a, data.get(a) + 1);
            }
            else {
                data.put(a, 1);
            }
        }

        //Remove one count of specific element from TreeMap
        //Note: does not remove all counts of element
        public void remove(A a) {
            if (data.containsKey(a)) {
                if (data.get(a) == 1) {
                    data.remove(a);
                }
                else {
                    data.put(a, data.get(a) - 1);
                }
            }
        }

        //Returns count of a specific element in the map
        public int count(A a) {
            return data.get(a);
        }

        //Returns the underlying keySet of the backing TreeMap
        //Useful for iterating through elements of the multiset
        public Set<A> keySet() {
            return data.keySet();
        }
    }

    /*
    @param Input Multiset<String> instance m
    @return list of all Strings with maximum count in the multiset
    */
    public static List<String> maxMultiset(Multiset<String> m) {
        int maxNum = -1;
        ArrayList<String> ans = new ArrayList<String>();

        for (String s : m.keySet()) {
            if (m.count(s) > maxNum) {
                maxNum = m.count(s);
            }
        }

        for (String s : m.keySet()) {
            if (m.count(s) == maxNum) {
                ans.add(s);
            }
        }

        return ans;
    }

    //Quick function to print a list
    public static void printList(List<String> l) {
        for (String s : l) {
            System.out.println(s);
        }
    }

    /*
    @param BufferedReader fRead: File reader right at the beginning of the data to be loaded in
    @return TreeMap<String, Multiset<String>>: Returns a treemap mapping dates to multisets of cookies
    Each multiset of cookies represents the cookies used in that day as a multiset for easy counting
    */
    public static TreeMap<String, Multiset<String>> loadData(BufferedReader fRead) {
        TreeMap<String, Multiset<String>> data = new TreeMap<String, Multiset<String>>();
        boolean hasMoreLines = true;
        while (hasMoreLines) {
            String line = null;
            try {
                line = fRead.readLine();
            }
            catch (IOException e) {
                break;
            }

            //No more lines are available case
            if (line == null) {
                hasMoreLines = false;
            }
            //Next line is available case
            else {
                //String parse out information
                String[] tokens = line.split(",");
                String cookieName = tokens[0];
                tokens = tokens[1].split("T");
                String date = tokens[0];

                //Add date cookie pair to data structure
                if (data.containsKey(date)) {
                    data.get(date).add(cookieName);
                }
                else {
                    data.put(date, new Multiset<String>());
                    data.get(date).add(cookieName);
                }
            }
        }    
        return data;   
    } 

    public static void main(String[] args) {
        //Simple error handling
        if (args.length < 2) {
            System.out.println("Two arguments required: filename & date");
            return;
        }

        //Get arguments in from args array
        String filename = args[0];
        String dateToCheck = args[1];

        //Open file
        File fObj = new File(filename);
        BufferedReader fRead;
        try {
            fRead = new BufferedReader(new FileReader(fObj));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return;
        }

        //Read junk line at top of csv
        try {
            fRead.readLine(); 
        }
        catch (IOException e) {
            System.out.println("IO Exception found");
        }

        //Load data
        TreeMap<String, Multiset<String>> data = loadData(fRead);


        //Process data
        if (data.containsKey(dateToCheck)) {
            printList(maxMultiset(data.get(dateToCheck)));
        }
        else {
            System.out.println("Date not contained in log.");
        }
    }
}