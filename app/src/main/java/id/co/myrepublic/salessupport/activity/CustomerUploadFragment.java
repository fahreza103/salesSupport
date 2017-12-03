package id.co.myrepublic.salessupport.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.co.myrepublic.salessupport.R;

import static android.app.Activity.RESULT_OK;


public class CustomerUploadFragment extends Fragment implements View.OnClickListener {

    private static final int IMAGE_ID_GALLERY_PREVIEW = 0;
    private static final int IMAGE_SELFIE_GALLERY_PREVIEW = 1;
    private static final int IMAGE_ID_CAMERA_PREVIEW = 2;
    private static final int IMAGE_SELFIE_CAMERA_PREVIEW = 3;

    private Button btnGalleryId;
    private Button btnCameraId;
    private Button btnGallerySelfie;
    private Button btnCameraSelfie;
    private ImageView imageViewPreviewId;
    private ImageView imageViewPreviewSelfie;

    private Uri cameraImageUri;
    private String mCurrentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        return inflater.inflate(R.layout.fragment_customer_upload, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getActivity().getString(R.string.fragment_view_salesorder));

        btnGalleryId = (Button) getActivity().findViewById(R.id.customer_btn_id_gallery);
        btnCameraId = (Button) getActivity().findViewById(R.id.customer_btn_id_takephoto);
        btnGallerySelfie = (Button) getActivity().findViewById(R.id.customer_btn_selfie_gallery);
        btnCameraSelfie = (Button) getActivity().findViewById(R.id.customer_btn_selfie_takephoto);

        imageViewPreviewId = (ImageView) getActivity().findViewById(R.id.customer_image_id_preview);
        imageViewPreviewSelfie = (ImageView) getActivity().findViewById(R.id.customer_image_selfie_preview) ;

        btnGalleryId.setOnClickListener(this);
        btnGallerySelfie.setOnClickListener(this);
        btnCameraId.setOnClickListener(this);
        btnCameraSelfie.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customer_btn_id_gallery :
                openGallery(IMAGE_ID_GALLERY_PREVIEW);
                break;
            case R.id.customer_btn_id_takephoto :
                openCamera(IMAGE_ID_CAMERA_PREVIEW);
                break;
            case R.id.customer_btn_selfie_gallery :
                openGallery(IMAGE_SELFIE_GALLERY_PREVIEW);
                break;

            case R.id.customer_btn_selfie_takephoto :
                openCamera(IMAGE_SELFIE_CAMERA_PREVIEW);
                break;
        }
    }

    private void openCamera(int type) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //getting uri of the file
        cameraImageUri = Uri.fromFile(getFile());

        //Setting the file Uri to my photo
        intent.putExtra(MediaStore.EXTRA_OUTPUT,cameraImageUri);

        if(intent.resolveActivity(getActivity().getPackageManager())!=null)
        {
            startActivityForResult(intent, type);
        }
    }

    //this method will create and return the path to the image file
    private File getFile() {
        File folder = Environment.getExternalStoragePublicDirectory("/salessupport/");// the file path

        //if it doesn't exist the folder will be created
        if(!folder.exists()) {
            if(!folder.getParentFile().exists())
                folder.getParentFile().mkdirs();
            boolean a = folder.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "salessupport_"+ timeStamp + "_";
        File image_file = null;

        try {
            image_file = File.createTempFile(imageFileName,".jpg",folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = image_file.getAbsolutePath();
        return image_file;
    }

    private void openGallery(int type) {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, type);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the result is OK (user finished the action)
        // and the request code is our code,
        // then this is our request and we can process the result
        try {
            if (resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                if (requestCode == IMAGE_ID_GALLERY_PREVIEW) {
                    imageViewPreviewId.setImageURI(imageUri);
                    imageViewPreviewId.setVisibility(View.VISIBLE);
                } else if (requestCode == IMAGE_SELFIE_GALLERY_PREVIEW) {
                    imageViewPreviewSelfie.setImageURI(imageUri);
                    imageViewPreviewSelfie.setVisibility(View.VISIBLE);
                } else if (requestCode == IMAGE_ID_CAMERA_PREVIEW) {
                    Bundle extras = data.getExtras();
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), cameraImageUri);
                    imageViewPreviewId.setImageBitmap(bmp);
                    imageViewPreviewId.setVisibility(View.VISIBLE);
                } else if (requestCode == IMAGE_SELFIE_CAMERA_PREVIEW) {
                    imageViewPreviewSelfie.setImageURI(cameraImageUri);
                    imageViewPreviewSelfie.setVisibility(View.VISIBLE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}