package smp_mod.smp_mod.MixinHelperMethods;

import java.util.List;

public class ContainsString {


    public static boolean ListContainsSubString(String cmd, List<String> cmds){

        for(String Substring:cmds){
            if(cmd.contains(Substring)){
                return true;
            }
        }
        return false;

    }
}
