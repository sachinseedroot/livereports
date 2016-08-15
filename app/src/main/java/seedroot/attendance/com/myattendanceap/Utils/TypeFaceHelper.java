package seedroot.attendance.com.myattendanceap.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by nitesh on 11/6/15.
 */
public class TypeFaceHelper {

    private static TypeFaceHelper typeFaceHelper;
    private final Context context;

    public static final String FONT_AWESOME = "fonts/FontAwesome.otf";
    public static final String FONTS_HELVETICA_NEUE_LTSTD_BD_CN_OTF = "fonts/HelveticaNeueLTStd-BdCn.otf";
    public static final String FONT_HelveticaNeueLTStd_LtCn  = "fonts/HelveticaNeueLTStd-LtCn.otf";
    public static final String FONT_Roboto_Bold  = "fonts/Roboto-Bold.ttf";

    private TypeFaceHelper(Context context) {
        this.context = context;
    }

    public static TypeFaceHelper getInstance(Context context){
        if (typeFaceHelper == null){
            typeFaceHelper = new TypeFaceHelper(context);
        }
        return typeFaceHelper;
    }


    private static HashMap<String, Typeface> sTypeFaces = new HashMap<>(1);

    public Typeface getStyleTypeFace(String fileName) {
        Typeface typeface = sTypeFaces.get(fileName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), fileName);
            sTypeFaces.put(fileName, typeface);
        }
        return typeface;
    }
}
