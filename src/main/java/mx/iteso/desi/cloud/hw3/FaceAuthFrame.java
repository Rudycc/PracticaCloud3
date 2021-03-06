package mx.iteso.desi.cloud.hw3;

import mx.iteso.desi.vision.ImagesMatUtils;
import mx.iteso.desi.vision.WebCamStream;
import com.amazonaws.util.IOUtils;

import org.opencv.core.Mat;

import java.nio.ByteBuffer;

import com.amazonaws.regions.Regions;

public class FaceAuthFrame extends javax.swing.JFrame {

    WebCamStream webCam;
    Mat lastFrame;
    
    public FaceAuthFrame() {
        this.webCam = new WebCamStream(0);
        initComponents();
        startCam();
    }
    
    private void startCam() {
        this.webCam.startStream(this.photoPanel);
        this.authButton.setEnabled(true);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        photoPanel = new javax.swing.JPanel();
        authButton = new javax.swing.JButton();
        nameTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                closeWindow(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeWindow(evt);
            }
        });

        photoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Webcam"));
        photoPanel.setAutoscrolls(true);
        photoPanel.setLayout(new javax.swing.BoxLayout(photoPanel, javax.swing.BoxLayout.LINE_AXIS));

        authButton.setText("Auth");
        authButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(photoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(authButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nameTextField)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(photoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(authButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameTextField)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void authButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authButtonActionPerformed
        this.lastFrame = webCam.stopStream();
        this.authButton.setEnabled(false);
        Face f = this.doAuthLogic();
        if(f.name.isEmpty()) {
            this.nameTextField.setText("Not Match");
        } else {
            this.nameTextField.setText(f.name+"("+f.cofidence+")");
        }
    }//GEN-LAST:event_authButtonActionPerformed

    private Face doAuthLogic() {
        // TODO
        Face face = null;
        AWSFaceCompare awsFaceCompare = new AWSFaceCompare("", "", Regions.US_WEST_2, "rodolfo-carrillo-photos");
        try {
            face = awsFaceCompare.compare(ByteBuffer.wrap(IOUtils.toByteArray(ImagesMatUtils.MatToInputStream(this.lastFrame))));
        } catch (Exception e) {
            System.err.println(e);
        }
        return face;
    }
    
    private void closeWindow(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeWindow
        webCam.stopStream();
    }//GEN-LAST:event_closeWindow

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton authButton;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPanel photoPanel;
    // End of variables declaration//GEN-END:variables
}
