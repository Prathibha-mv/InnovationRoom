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
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

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

                // Copy image to external storage
                File filepath = Environment.getExternalStorageDirectory();
                String Test= getResources().getString(R.string.FolderName);
                File dir = new File(filepath.getAbsolutePath() +"/"+Test+"/");
                dir.mkdirs();
                Date d=new Date();
                String n=d.getTime()+".jpeg";
                File file = new File(dir,n);

                try {

                    output = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                    Toast.makeText(MainActivity.this, "Image Saved ",Toast.LENGTH_SHORT).show();
                    Log.d("Filename","Image saved at"+file.getAbsoluteFile());
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
        //cameraObject.setParameters();
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