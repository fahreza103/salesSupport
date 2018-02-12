package id.co.myrepublic.salessupport.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.support.BitmapProcessor;
import id.co.myrepublic.salessupport.util.GlobalVariables;

import static android.app.Activity.RESULT_OK;


public class FragmentCustomerUpload extends Fragment implements View.OnClickListener {

    private static final int IMAGE_ID_GALLERY_PREVIEW = 0;
    private static final int IMAGE_OTHER_GALLERY_PREVIEW = 1;
    private static final int IMAGE_ID_CAMERA_PREVIEW = 2;
    private static final int IMAGE_OTHER_CAMERA_PREVIEW = 3;

    //private Button btnGalleryId;
    private Button btnCameraId;
    //private Button btnGallerySelfie;
    private Button btnCameraOther;
    private Button btnConfirm;
    private ImageView imageViewPreviewId;
    private ImageView imageViewPreviewOther;

    private Uri cameraImageUri;
    private String idImagePath;
    private String otherImagePath;
    private Uri idImageUri;
    private Uri otherImageUri;

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

        //btnGalleryId = (Button) getActivity().findViewById(R.id.customer_btn_id_gallery);
        btnCameraId = (Button) getActivity().findViewById(R.id.customer_btn_id_takephoto);
        //btnGallerySelfie = (Button) getActivity().findViewById(R.id.customer_btn_selfie_gallery);
        btnCameraOther = (Button) getActivity().findViewById(R.id.customer_btn_other_takephoto);
        btnConfirm = (Button) getActivity().findViewById(R.id.customer_btn_confirm) ;

        imageViewPreviewId = (ImageView) getActivity().findViewById(R.id.customer_image_id_preview);
        imageViewPreviewOther = (ImageView) getActivity().findViewById(R.id.customer_image_other_preview);

        //btnGalleryId.setOnClickListener(this);
        //btnGallerySelfie.setOnClickListener(this);
        btnCameraId.setOnClickListener(this);
        btnCameraOther.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        if(idImageUri != null) {
            showImageFromUri(idImageUri,imageViewPreviewId);
        }

        if(otherImageUri != null) {
            showImageFromUri(otherImageUri,imageViewPreviewOther);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //case R.id.customer_btn_id_gallery :
            //    openGallery(IMAGE_ID_GALLERY_PREVIEW);
            //    break;
            case R.id.customer_btn_id_takephoto :
                openCamera(IMAGE_ID_CAMERA_PREVIEW);
                break;
            //case R.id.customer_btn_selfie_gallery :
            //    openGallery(IMAGE_OTHER_GALLERY_PREVIEW);
            //    break;

            case R.id.customer_btn_other_takephoto :
               openCamera(IMAGE_OTHER_CAMERA_PREVIEW);
                break;
            case R.id.customer_btn_confirm :
                Bundle bundle = this.getArguments();
                bundle.putString("customerIdPhoto",idImagePath);
                bundle.putString("customerOtherPhoto", otherImagePath);

                Fragment fragment = new FragmentPlan();
                fragment.setArguments(bundle);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_from_right,R.anim.right_from_left, R.anim.left_from_right,R.anim.right_from_left);
                ft.replace(R.id.content_frame, fragment,fragment.getClass().getName());
                ft.addToBackStack(fragment.getClass().getName());
                ft.commit();

                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    private void openCamera(int type) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = BitmapProcessor.createTempBitmapFile();
        //getting uri of the file
        cameraImageUri = Uri.fromFile(imageFile);


        //Setting the file Uri to my photo
        intent.putExtra(MediaStore.EXTRA_OUTPUT,cameraImageUri);

        if(intent.resolveActivity(getActivity().getPackageManager())!=null)
        {
            startActivityForResult(intent, type);
        }
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

        if (resultCode == RESULT_OK) {
            Uri imageUri = cameraImageUri;

            // Compress the image
            File imageFile = new File(imageUri.getPath());
            String imagePath = imageFile.getAbsolutePath();
            imageFile = BitmapProcessor.compressAndResize(imageFile);
            Log.i("Image File compressed",imageFile.length()+"");
            imageUri = Uri.fromFile(imageFile);

            if (requestCode == IMAGE_ID_GALLERY_PREVIEW) {
                imageViewPreviewId.setImageURI(imageUri);
                imageViewPreviewId.setVisibility(View.VISIBLE);
                idImageUri = imageUri;
            } else if (requestCode == IMAGE_OTHER_GALLERY_PREVIEW) {
                imageViewPreviewOther.setImageURI(imageUri);
                imageViewPreviewOther.setVisibility(View.VISIBLE);
                otherImageUri = imageUri;
            } else if (requestCode == IMAGE_ID_CAMERA_PREVIEW) {
                showImageFromUri(cameraImageUri, imageViewPreviewId);
                idImageUri = imageUri;
                idImagePath = imagePath;
            } else if (requestCode == IMAGE_OTHER_CAMERA_PREVIEW) {
                showImageFromUri(cameraImageUri, imageViewPreviewOther);
                otherImageUri = imageUri;
                otherImagePath = imagePath;
            }
        }

    }

    private void showImageFromUri(Uri uri, ImageView imageView) {
        Bitmap bmp = null;
        GlobalVariables gVar = GlobalVariables.getInstance();
        try {
            bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            imageView.setImageBitmap(bmp);
            imageView.startAnimation(gVar.getFadeInAnim());
            imageView.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
