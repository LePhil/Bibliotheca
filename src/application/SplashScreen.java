package application;

import i18n.Messages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import views.MainView;

public class SplashScreen {

    private JDialog dialog;
    private JProgressBar progress;

    protected void initUI(final MainView mainView) throws MalformedURLException {
        showSplashScreen();
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(15);// Simulate loading
                    publish(i);// Notify progress
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                progress.setValue(chunks.get(chunks.size() - 1));
            }

            @Override
            protected void done() {
                showFrame( mainView );
                hideSplashScreen();
            }

        };
        worker.execute();
    }

    protected void hideSplashScreen() {
        dialog.setVisible(false);
        dialog.dispose();
    }

    protected void showSplashScreen() throws MalformedURLException {
        dialog = new JDialog((Frame) null);
        dialog.setModal(false);
        dialog.setUndecorated(true);
        JLabel background = new JLabel( new ImageIcon("images/books_1_bw.png") );
        background.setLayout(new BorderLayout());
        dialog.add(background);
        JLabel text = new JLabel( Messages.getString("Splash.Loading.text") );
        text.setFont(new Font("Sans-Serif", Font.PLAIN, 30));
        text.setForeground(Color.WHITE);
        text.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
        background.add( text, BorderLayout.CENTER );
        progress = new JProgressBar();
        background.add(progress, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    protected void showFrame(MainView mainView) {
    	mainView.setVisible(true);
    }
}