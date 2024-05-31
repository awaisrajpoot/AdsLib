package com.example.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class JniUtils {

    public static Bitmap loadBitmapFromAssets(Context context, String name) {
        try {
            InputStream is = context.getAssets().open(name);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //this function is using for loading saved background on activity runtime
    public static byte[] getAssetsBytesOnThread(final String address, Context context){

        final byte[][] fileBytes = {new byte[0]};

        Thread thread = new Thread(){
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    try {
                        fileBytes[0] = copyURLToByteArrayForThread(address,3000,3000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();

//        ((AppCompatActivity)context).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    try {
//                        fileBytes[0] = copyURLToByteArrayForThread(address,3000,3000);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });


        return fileBytes[0];
    }

    //this function is using in saved template where all user saved templates are showing
    public static byte[] getAssetsBytes(String address,Context context){

        byte[] fileBytes= new byte[0];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                fileBytes = copyURLToByteArray(address,3000,3000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileBytes;
    }


    public static byte[] getLocalAssetsBytes(String address,Context context){

        InputStream is = null;
        try {
            is = context.getAssets().open(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] fileBytes= new byte[0];
        try {
            fileBytes = new byte[is.available()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.read( fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileBytes;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static byte[] copyURLToByteArray(final String urlStr,
                                            final int connectionTimeout, final int readTimeout)
            throws IOException {
        final URL url = new URL(urlStr);
        final URLConnection connection = url.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        try (InputStream input = connection.getInputStream();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[8192];
            for (int count; (count = input.read(buffer)) > 0;) {
                output.write(buffer, 0, count);
            }
            return output.toByteArray();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static byte[] copyURLToByteArrayForThread(final String urlStr,
                                            final int connectionTimeout, final int readTimeout)
            throws IOException {
        final URL url = new URL(urlStr);
        final URLConnection connection = url.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        try (InputStream input = connection.getInputStream();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[8192];
            for (int count; (count = input.read(buffer)) > 0;) {
                output.write(buffer, 0, count);
            }
            return output.toByteArray();
        }
    }


    public static Bitmap getAssetsBitmap(String address,Context context){

        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            //BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }

        //decode base64 string to image
        //decode base64 string to image
        byte[] imageBytes = Base64.decode(""+text, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }
    
    private static native byte[] decodeBytesJNI(byte[] bArr);

    private static native byte[] decryptResByIdJNI(Context context, int i);

    private static native byte[] decryptResByNameJNI(Context context, String str);

    private static native byte[] encodeBytesJNI(byte[] bArr);

    private static native String stringFromJNI();

    /////////////////////////////////////////////////////////////////////////

    private static native byte[] encryptResJNI(Context context, String str);


    ///////////////////////////////////////////////////////////////////////////

    static {
        //System.loadLibrary("imageprocess");
    }

    public byte[] getBytes(Context ctx, String resName) {
        byte[] b = new byte[0];
        try {
            InputStream inStream = ctx.getResources().openRawResource(ctx.getResources().
                    getIdentifier(resName, "raw", ctx.getPackageName()));
            b = new byte[inStream.available()];
            inStream.read(b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
    }

    public byte[] getBytes(Context ctx, int resId) {
        byte[] b = new byte[0];
        try {
            InputStream inStream = ctx.getResources().openRawResource(resId);
            b = new byte[inStream.available()];
            inStream.read(b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
    }

    public static boolean deleteLogoFile(Uri uri,Context context) {
        boolean bl = false;
        try {
            File file = new File(uri.getPath());
            bl = file.delete();
            if (file.exists()) {
                try {
                    bl = file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    bl = context.deleteFile(file.getName());
                }
            }
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (Exception e2) {
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
        }
        return bl;
    }

    public static String stringFromJni() {
        return stringFromJNI();
    }

    public static byte[] encryptResJni(Context context, String str) {
        return encryptResJNI(context, str);
    }

    public static byte[] decryptResourceJNI(Context ctx, String resName) {
        return decryptResByNameJNI(ctx, resName);
    }

    public static byte[] decryptResourceJNI(Context ctx, int resId) {
        return decryptResByIdJNI(ctx, resId);
    }

    public static byte[] encodeBytesArrayJNI(byte[] bytes) {
        return encodeBytesJNI(bytes);
    }

    public static byte[] decodeBytesArrayJNI(byte[] bytes) {
        return decodeBytesJNI(bytes);
    }
}
