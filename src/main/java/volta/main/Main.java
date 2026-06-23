package volta.main;

import java.io.IOException;
import java.util.List;

import volta.utils.PackageScanner;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
       ;
        List<Class<?>> classes = PackageScanner.getAllClasses();
        for(Class<?> c : classes){
            System.out.println(c.getName());
        }
    }
}
