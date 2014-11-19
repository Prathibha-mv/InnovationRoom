package atd.csc.druganalyzer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    private Camera cameraObject;
    private ShowCamera showCamera;
    private ImageView pic;

    public static Camera isCameraAvailiable() {
        Camera object = null;
        try {
            object = Camera.open();
        } catch (Exception e) {
        }
        return object;
    }

    private PictureCallback capturedIt = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
            }

            else {

                OutputStream output;

                // Find the SD Card path
                File filepath = Environment.getExternalStorageDirectory();

                // Create a new folder in SD Card
                File dir = new File(filepath.getAbsolutePath()
                        + "/DrugAnalyser/");
                dir.mkdirs();

                // Create a name for the saved image
                File file = new File(dir, "myimage.png");

                // Show a toast message on successful save

                try {

                    output = new FileOutputStream(file);

                    // Compress into png format image from 0% - 100%
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                    Toast.makeText(MainActivity.this, "Image Saved ",Toast.LENGTH_SHORT).show();
                    output.flush();
                    output.close();
                }

                catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Failed to save", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }




            }
            cameraObject.release();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pic = (ImageView) findViewById(R.id.imageView1);
        cameraObject = isCameraAvailiable();
        showCamera = new ShowCamera(this, cameraObject);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(showCamera);
    }

    public void snapIt(View view) {
        cameraObject.takePicture(null, null, capturedIt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}