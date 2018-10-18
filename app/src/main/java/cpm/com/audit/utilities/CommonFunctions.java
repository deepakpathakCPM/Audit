package cpm.com.audit.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import cpm.com.audit.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;

/**
 * Created by deepakp on 2/16/2017.
 */

public class CommonFunctions {

    public static String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        return formatter.format(m_cal.getTime());

    }

    public static void startCameraActivity(Activity activity, String path) {
        String gallery_package = "";
        Uri outputFileUri = null;

        try {
            File file = new File(path);
            outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = activity.getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                /*Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                Log.e("TAG", "package name  : " + list.get(n).packageName);*/

                    //temp value in case camera is gallery app above jellybean
                    if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Gallery")) {
                        gallery_package = list.get(n).packageName;
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    } else {
                        if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    }
                }
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
            activity.startActivityForResult(intent, 1);
            //startActivityForResult(intent, position);

        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(gallery_package);
            activity.startActivityForResult(intent, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startAnncaCameraActivity(Context context, final String path, Fragment fragment, final boolean showGrid) {
        //final AnncaConfiguration.Builder dialogDemo = new AnncaConfiguration.Builder((Activity) context, CommonString.CAPTURE_MEDIA);
        final AnncaConfiguration.Builder dialogDemo;
        if (fragment == null) {
            dialogDemo = new AnncaConfiguration.Builder((Activity) context, CommonString.CAPTURE_MEDIA);
        } else {
            dialogDemo = new AnncaConfiguration.Builder(fragment, CommonString.CAPTURE_MEDIA);
        }
        dialogDemo.setMediaAction(AnncaConfiguration.MEDIA_ACTION_PHOTO);
        dialogDemo.setMediaResultBehaviour(AnncaConfiguration.PREVIEW);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.view_horizontal_camera);
        dialog.setCancelable(false);
        if (dialog != null && (!dialog.isShowing())) {
            dialog.show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                new Annca(dialogDemo.build()).launchCamera(path,showGrid);
            }
        }, 3000);
    }


    public static void setScaledImage(ImageView imageView, final String path) {
        final ImageView iv = imageView;
        ViewTreeObserver viewTreeObserver = iv.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                iv.getViewTreeObserver().removeOnPreDrawListener(this);
                int imageViewHeight = iv.getMeasuredHeight();
                int imageViewWidth = iv.getMeasuredWidth();
                iv.setImageBitmap(decodeSampledBitmapFromPath(path, imageViewWidth, imageViewHeight));
                return true;
            }
        });
    }

    private static Bitmap decodeSampledBitmapFromPath(String path,
                                                      int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds = true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    public static String getCurrentTimeHHMMSS() {
        Calendar m_cal = Calendar.getInstance();
        return m_cal.get(Calendar.HOUR_OF_DAY) + ""
                + m_cal.get(Calendar.MINUTE) + "" + m_cal.get(Calendar.SECOND);
    }

   /* public static void referenceImageAlert(Context context, final Runnable runnable, String image) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_dialog_refimage, null);
        final ImageView imageView = alertLayout.findViewById(R.id.imageView);
        if (image == null || image.equalsIgnoreCase("")) {
            Glide.with(context)
                    .load(R.drawable.no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            if (new File(CommonString.FILE_PATH_Downloaded + image).exists()) {
                Glide.with(context)
                        .load(CommonString.FILE_PATH_Downloaded + image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);

            } else {
                Glide.with(context)
                        .load(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);

            }
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Reference Image");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                runnable.run();
            }
        });
        AlertDialog dialog = alert.create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }*/

}
