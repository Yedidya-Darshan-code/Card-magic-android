package com.yedidya.magiccardtrick;

import android.content.Context;
import android.widget.Toast;

/**
 * Utility class for common helper methods.
 */
public class Utilities {
    /**
     * Shows a toast message.
     *
     * @param context The application context
     * @param message The message to display
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}