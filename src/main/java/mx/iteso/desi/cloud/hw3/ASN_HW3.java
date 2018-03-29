package mx.iteso.desi.cloud.hw3;

import org.opencv.core.Core;

public class ASN_HW3 {

    public void initAuth() {
        FaceAuthFrame frame;
        frame = new FaceAuthFrame();
        frame.setVisible(true);
    }

    public void initAdd() {
        FaceAddFrame frame;
        frame = new FaceAddFrame();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
 
        System.load("/usr/local/Cellar/opencv/3.4.1_2/share/OpenCV/java/libopencv_java341.dylib");
        
        ASN_HW3 self = new ASN_HW3();
 
        if(args.length > 0) {
            if(args[0].compareToIgnoreCase("-add")==0) {
                self.initAdd();
            } else {
                self.initAuth();
            }
        } else {
            self.initAuth();
        }
    }
    
}
