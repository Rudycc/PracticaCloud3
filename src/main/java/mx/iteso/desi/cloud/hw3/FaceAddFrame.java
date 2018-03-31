package mx.iteso.desi.cloud.hw3;

import mx.iteso.desi.vision.ImagesMatUtils;
import mx.iteso.desi.vision.WebCamStream;
import org.opencv.core.Mat;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.regions.Regions;

public class FaceAddFrame extends javax.swing.JFrame {
    final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_WEST_2).build();

    WebCamStream webCam;
    Mat lastFrame;
    
    public FaceAddFrame() {
        this.webCam = new WebCamStream(0);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        photoPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        uploadButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
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

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        uploadButton.setText("Upload");
        uploadButton.setEnabled(false);
        uploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nameTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadButton))
                    .addComponent(photoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stopButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton))
                    .addComponent(photoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uploadButton)
                    .addComponent(jLabel1)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        webCam.startStream(this.photoPanel);
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }//GEN-LAST:event_startButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        this.lastFrame = webCam.stopStream();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        uploadButton.setEnabled(true);
    }//GEN-LAST:event_stopButtonActionPerformed

    private void uploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadButtonActionPerformed
        if (!s3.doesBucketExist("rodolfo-carrillo-photos")) {
            try {
                s3.createBucket("rodolfo-carrillo-photos");
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
        try {
            InputStream byteArrayInputStream = ImagesMatUtils.MatToInputStream(this.lastFrame);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(byteArrayInputStream.available());
            PutObjectRequest putObjectRequest = new PutObjectRequest("rodolfo-carrillo-photos", this.nameTextField.getText(), byteArrayInputStream, metadata);
            s3.putObject(putObjectRequest);
            this.uploadButton.setEnabled(false);
            this.nameTextField.setText("");
            this.lastFrame = null;
        } catch (Exception e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_uploadButtonActionPerformed

    private void closeWindow(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeWindow
        webCam.stopStream();
    }//GEN-LAST:event_closeWindow

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JPanel photoPanel;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton uploadButton;
    // End of variables declaration//GEN-END:variables
}
