package com.bytecodecomp.npos.Utils;

import com.bytecodecomp.npos.Data_Models.App_Settings;

public class Permissions_Control {

    public static boolean check_perms(String permission){


        if (App_Settings.staff_uid.equals("0")){

            return true;
        }

        else {

            if (App_Settings.staff_permissions.contains(permission)){

                return true;

            }

            else {

                return false;

            }

        }

    }

}
