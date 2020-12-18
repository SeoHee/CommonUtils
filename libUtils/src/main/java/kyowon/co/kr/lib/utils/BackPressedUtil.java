package kyowon.co.kr.lib.utils;

/**
 * Created by 29074 on 2018-05-02.
 */

public class BackPressedUtil {

   private OnCallBackBackPressedUtilsListener onCallBackBackPressedUtilsListener;

   private long TIME_MILLIS = 2000;
   private long backKeyPressedTime = 0;

   private static BackPressedUtil mInstance;

   public interface OnCallBackBackPressedUtilsListener {

      void setWarning();

      void setFinish();
   }

   public static BackPressedUtil getInstance() {
      if (mInstance == null) {
         mInstance = new BackPressedUtil();
      }
      return mInstance;
   }

   public void setBackPressedMillis(long millis) {
      TIME_MILLIS = millis;
   }

   public void setOnCallBackBackPressedUtilsListener(OnCallBackBackPressedUtilsListener listener) {
      onCallBackBackPressedUtilsListener = listener;
   }

   public void onBackPressed() {
      if (System.currentTimeMillis() > backKeyPressedTime + TIME_MILLIS) {
         backKeyPressedTime = System.currentTimeMillis();

         if (onCallBackBackPressedUtilsListener != null) {
            onCallBackBackPressedUtilsListener.setWarning();
         }
      } else {
         backKeyPressedTime = 0;//초기화

         if (onCallBackBackPressedUtilsListener != null) {
            onCallBackBackPressedUtilsListener.setFinish();
         }
      }
   }
}
