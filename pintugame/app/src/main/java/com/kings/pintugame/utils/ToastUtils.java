/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kings.pintugame.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.kings.pintugame.ApplicationImp;


/**
 * Created by Yan Zhenjie on 2016/10/16.
 */
public class ToastUtils {

    public static void showCoustomToast(int layoutId) {
        Context context = ApplicationImp.getContext();
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(View.inflate(context, layoutId, null));
        toast.show();
    }


    public static void show(CharSequence msg) {
        Toast.makeText(ApplicationImp.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, @StringRes int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
    }

    public static void show(View view, CharSequence msg) {
        show(msg);
    }

    public static void show(View view, @StringRes int stringId) {
        show(view.getContext(), stringId);
    }



}
